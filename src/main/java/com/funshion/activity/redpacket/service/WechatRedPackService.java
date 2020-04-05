package com.funshion.activity.redpacket.service;

import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.bean.LotteryLog;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.service.LotteryLogService;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.domain.*;
import com.funshion.activity.redpacket.mapper.IpWhitelistMapper;
import com.funshion.activity.redpacket.mapper.RedPackRecordMapper;
import com.funshion.activity.redpacket.mapper.RedPackTemplateMapper;
import com.funshion.activity.redpacket.req.*;
import com.funshion.activity.redpacket.rsp.GainRedPackRsp;
import com.funshion.activity.redpacket.rsp.GetCoverInfoRsp;
import com.funshion.activity.redpacket.rsp.GetHbForSubWechatRsp;
import com.funshion.activity.redpacket.rsp.GetRedPackRecordsRsp;
import com.funshion.activity.redpacket.utils.WechatServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WechatRedPackService {

    @Autowired
    RedPackTemplateMapper redPackTemplateMapper;

    @Autowired
    RedPackRecordMapper redPackRecordMapper;

    @Autowired
    LotteryLogService lotteryLogService;

    @Autowired
    IpWhitelistMapper ipWhitelistMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    private final static String IP_REQ_KEY = "activity_api_ip_req_";

    private final static String RED_PACKET_SEND_KEY = "activity_api_red_packet_send_";

    private final static String RED_PACKET_TOKEN_KEY = "activity_api_red_packet_token";

    public Result<?> sendRedPack(AcceptWechatRedPackReq req) {
        // 防止前端重复发送红包
        if (redisService.get(RED_PACKET_SEND_KEY + req.getRedPackId()) != null) {
            return RedPackConstant.ErrorCode.REPEAT_TRY;
        }
        redisService.setForTimeCustom(RED_PACKET_SEND_KEY + req.getRedPackId(), 1, 20, TimeUnit.SECONDS);

        // 校验红包
        RedPackRecord record = redPackRecordMapper.getRecordById(req.getRedPackId());
        if (record == null || !record.getTvId().equals(req.getTvId())) {
            return RedPackConstant.ErrorCode.INFO_CHECK_ERROR;
        }

        // 校验红包状态
        if (record.getStatus() != RedPackConstant.Status.INIT
                || (record.getInvalidTime() != null && new Date().after(record.getInvalidTime()))) {
            return RedPackConstant.ErrorCode.INVALID;
        }

        RedPackTemplate redPackTemplate = redPackTemplateMapper.getTemplateById(record.getTemplateId());
        if (redPackTemplate == null) {
            return RedPackConstant.ErrorCode.INVALID;
        }

        if (redPackTemplate.getUserLimit() != null
                && redPackRecordMapper.getUserRecordNum(record.getActivityType(), req.getOpenId()) >= redPackTemplate.getUserLimit()) {
            return RedPackConstant.ErrorCode.OVER_LIMIT;
        }

        // 发送红包
        String pCode = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + RandomStringUtils.randomNumeric(4);
        SendRedPackRsp sendRedPackRsp = WechatServiceUtils.doSend(req.getOpenId(), pCode,
                record.getAmount().multiply(new BigDecimal(100)).intValue());

        // 发送失败
        if (sendRedPackRsp == null || !"SUCCESS".equals(sendRedPackRsp.getResult_code())) {
            String remarks = sendRedPackRsp == null ? "发送红包失败" : sendRedPackRsp.getReturn_msg();
            redPackRecordMapper.addRemarks(req.getRedPackId(), remarks);
            return RedPackConstant.ErrorCode.GIVE_ERROR;
        }

        // 更新红包记录状态等支付信息
        RedPackRecord redPackRecord = new RedPackRecord();
        redPackRecord.setStatus(RedPackConstant.Status.SENT);
        redPackRecord.setPCode(pCode);
        redPackRecord.setGCode(sendRedPackRsp.getSend_listid());
        redPackRecord.setId(req.getRedPackId());
        redPackRecord.setOpenId(req.getOpenId());
        redPackRecordMapper.updatePackRecord(redPackRecord);
        return Result.getSuccessResult();
    }

    public Result<?> gainRedPack(GainWechatRedPackReq req) {
        // 红包资格校验
        if (!doHbAuth(req)) {
            return RedPackConstant.ErrorCode.AUTH_FAILED;
        }

        // 记录抽取红包操作
        LotteryLog logInfo = new LotteryLog();
        logInfo.setActivityType(req.getActivityType());
        logInfo.setTvId(req.getTvId());
        logInfo.setRemarks("抽红包");
        lotteryLogService.saveLotteryLog(logInfo);

        Integer templateId = req.getTemplateId();
        if (templateId == null) {
            String templateIdStr = LoadDataConfig.getDataValueByKey(String.format("funtv.activity.%d.hbTemplateId", req.getActivityType()), "");
            templateId = NumberUtils.toInt(templateIdStr, 0);
        }
        RedPackTemplate templateInfo = redPackTemplateMapper.getTemplateById(templateId);
        if (templateInfo == null) {
            return RedPackConstant.ErrorCode.IS_OVER;
        }

        // 红包配置详情
        List<RedPackTemplateDetail> details = redPackTemplateMapper.getTemplateDetailsById(templateId);
        if (CollectionUtils.isEmpty(details)) {
            return RedPackConstant.ErrorCode.IS_OVER;
        }

        // 总额限制
        RedPackSummary summary = redPackRecordMapper.sumAmount(req.getActivityType());
        if (summary != null && summary.getTotalAmount() != null &&
                templateInfo.getTotalAmount().compareTo(summary.getTotalAmount()) <= 0) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 数量限制
        if (summary != null && summary.getTotalNum() >= templateInfo.getTotalNum()) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 根据概率随机红包配置信息
        RedPackProbability packProbability = getProbability(details);
        if (packProbability == null) {
            return RedPackConstant.ErrorCode.RED_PACK_LOSE;
        }

        // 固定数额红包模式
        BigDecimal amount = new BigDecimal("0.00");
        if (RedPackConstant.TemplateType.FIXED.equals(templateInfo.getType())) {
            amount = packProbability.getAmount();
        }
        // 随机金额红包模式
        else if (RedPackConstant.TemplateType.RANDOM.equals(templateInfo.getType())) {
            double randomAmount = RandomUtils.nextDouble(packProbability.getMinAmount().doubleValue(), packProbability.getMaxAmount().doubleValue());
            amount = new BigDecimal(new DecimalFormat("0.00").format(new BigDecimal(randomAmount)));
        }

        // 再次校验总额
        if (summary.getTotalAmount() != null && templateInfo.getTotalAmount() != null
                && templateInfo.getTotalAmount().compareTo(summary.getTotalAmount().add(amount)) < 0) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 更新库存成功
        if (redPackTemplateMapper.updateStockById(packProbability.getId()) > 0) {
            // 增加红包记录
            RedPackRecord record = new RedPackRecord();
            record.setTemplateId(templateId);
            record.setAmount(amount);
            record.setTvId(req.getTvId());
            record.setActivityType(req.getActivityType());
            record.setStatus(RedPackConstant.Status.INIT);
            record.setOrderCode(getLocalPayCode(req.getTvId()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, templateInfo.getValidDay());
            record.setInvalidTime(calendar.getTime());
            redPackRecordMapper.addRecord(record);

            GainRedPackRsp res = new GainRedPackRsp();
            res.setRedPackId(record.getId());
            res.setAmount(new DecimalFormat("0.00").format(amount));
            res.setUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, req.getTvId(), record.getId()));
            return Result.getSuccessResult(res);
        }


        return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
    }

    public Result<?> getCoverInfo(GetCoverInfoReq req) {
        RedPackRecord record = redPackRecordMapper.getRecordById(req.getRedPackId());
        GetCoverInfoRsp rsp = new GetCoverInfoRsp();
        if (record == null) {
            rsp.setStatus(-1);
        } else {
            RedPackTemplate template = redPackTemplateMapper.getTemplateById(record.getTemplateId());
            if (template != null) {
                rsp.setTitle(template.getTitle());
                rsp.setBottomImg(ImgUtils.getImgUrl(template.getBottomImg()));
            }
            rsp.setStatus(record.getStatus());
        }

        return Result.getSuccessResult(rsp);
    }

    public Result<?> subWechat(GetHbForSubWechatReq req) {
        // 红包资格校验
        if (ipLimit(req.getIp())) {
            return RedPackConstant.ErrorCode.AUTH_FAILED;
        }

        // 上次未领取的红包可以继续领取
        RedPackRecord lastRecord = redPackRecordMapper.getUserRecord(req.getActivityType(), req.getOpenId());
        if (lastRecord != null) {
            if (lastRecord.getStatus() == 1) {
                GetHbForSubWechatRsp res = new GetHbForSubWechatRsp();
                res.setTvId(lastRecord.getTvId());
                res.setRedPackId(lastRecord.getId());
                res.setAmount(new DecimalFormat("0.00").format(lastRecord.getAmount()));
                res.setUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL2, lastRecord.getTvId(), lastRecord.getId()));
                return Result.getSuccessResult(res);
            }
            return RedPackConstant.ErrorCode.CHANCE_LIMIT;
        }

        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return RedPackConstant.ErrorCode.UN_SUBSCRIBE;
        }

        // 调微信公众号接口，查询订阅情况
        String getRsp = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken
                + "&openid=" + req.getOpenId() + "&lang=zh_CN", String.class);
        if (StringUtils.isBlank(getRsp) || JSONObject.parseObject(getRsp).getIntValue("subscribe") != 1) {
            return RedPackConstant.ErrorCode.UN_SUBSCRIBE;
        }

        String templateIdStr = LoadDataConfig.getDataValueByKey(String.format("funtv.activity.%d.hbTemplateId", req.getActivityType()), "");
        int templateId = NumberUtils.toInt(templateIdStr, 0);

        RedPackTemplate templateInfo = redPackTemplateMapper.getTemplateById(templateId);
        if (templateInfo == null) {
            return RedPackConstant.ErrorCode.IS_OVER;
        }

        // 红包配置详情
        List<RedPackTemplateDetail> details = redPackTemplateMapper.getTemplateDetailsById(templateId);
        if (CollectionUtils.isEmpty(details)) {
            return RedPackConstant.ErrorCode.IS_OVER;
        }

        // 总额限制
        RedPackSummary summary = redPackRecordMapper.sumAmount(req.getActivityType());
        if (summary != null && summary.getTotalAmount() != null &&
                templateInfo.getTotalAmount().compareTo(summary.getTotalAmount()) <= 0) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 数量限制
        if (summary != null && summary.getTotalNum() >= templateInfo.getTotalNum()) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 根据概率随机红包配置信息
        RedPackProbability packProbability = getProbability(details);
        if (packProbability == null) {
            return RedPackConstant.ErrorCode.RED_PACK_LOSE;
        }

        // 固定数额红包模式
        BigDecimal amount = new BigDecimal("0.00");
        if (RedPackConstant.TemplateType.FIXED.equals(templateInfo.getType())) {
            amount = packProbability.getAmount();
        }
        // 随机金额红包模式
        else if (RedPackConstant.TemplateType.RANDOM.equals(templateInfo.getType())) {
            double randomAmount = RandomUtils.nextDouble(packProbability.getMinAmount().doubleValue(), packProbability.getMaxAmount().doubleValue());
            amount = new BigDecimal(new DecimalFormat("0.00").format(new BigDecimal(randomAmount)));
        }

        // 再次校验总额
        if (summary.getTotalAmount() != null && templateInfo.getTotalAmount() != null
                && templateInfo.getTotalAmount().compareTo(summary.getTotalAmount().add(amount)) < 0) {
            return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
        }

        // 更新库存成功
        if (redPackTemplateMapper.updateStockById(packProbability.getId()) > 0) {
            Integer randomTvId = NumberUtils.toInt(RandomStringUtils.randomNumeric(9), 0) + 999999999;
            // 增加红包记录
            RedPackRecord record = new RedPackRecord();
            record.setTemplateId(templateId);
            record.setAmount(amount);
            record.setTvId(randomTvId);
            record.setActivityType(req.getActivityType());
            record.setStatus(RedPackConstant.Status.INIT);
            record.setOrderCode(getLocalPayCode(randomTvId));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, templateInfo.getValidDay());
            record.setInvalidTime(calendar.getTime());
            record.setOpenId(req.getOpenId());
            redPackRecordMapper.addRecord(record);

            GetHbForSubWechatRsp res = new GetHbForSubWechatRsp();
            res.setTvId(randomTvId);
            res.setRedPackId(record.getId());
            res.setAmount(new DecimalFormat("0.00").format(amount));
            res.setUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL2, randomTvId, record.getId()));
            return Result.getSuccessResult(res);
        }

        return RedPackConstant.ErrorCode.RED_PACK_EMPTY;
    }

    public GainRedPackRsp getHb(Integer tvId, Integer activityType, Integer templateId) {
        Map<String, String> dataMap = new HashMap<>();
        RedPackTemplate templateInfo = redPackTemplateMapper.getTemplateById(templateId);
        if (templateInfo == null) {
            return null;
        }

        // 红包配置详情
        List<RedPackTemplateDetail> details = redPackTemplateMapper.getTemplateDetailsById(templateId);
        if (CollectionUtils.isEmpty(details)) {
            return null;
        }

        // 总额限制
        RedPackSummary summary = redPackRecordMapper.sumAmount(activityType);
        if (summary != null && summary.getTotalAmount() != null &&
                templateInfo.getTotalAmount().compareTo(summary.getTotalAmount()) <= 0) {
            return null;
        }

        // 数量限制
        if (summary != null && summary.getTotalNum() >= templateInfo.getTotalNum()) {
            return null;
        }

        // 根据概率随机红包配置信息
        RedPackProbability packProbability = getProbability(details);
        if (packProbability == null) {
            return null;
        }

        // 固定数额红包模式
        BigDecimal amount = new BigDecimal("0.00");
        if (RedPackConstant.TemplateType.FIXED.equals(templateInfo.getType())) {
            amount = packProbability.getAmount();
        }
        // 随机金额红包模式
        else if (RedPackConstant.TemplateType.RANDOM.equals(templateInfo.getType())) {
            double randomAmount = RandomUtils.nextDouble(packProbability.getMinAmount().doubleValue(), packProbability.getMaxAmount().doubleValue());
            amount = new BigDecimal(new DecimalFormat("0.00").format(new BigDecimal(randomAmount)));
        }

        // 再次校验总额
        if (summary.getTotalAmount() != null && templateInfo.getTotalAmount() != null
                && templateInfo.getTotalAmount().compareTo(summary.getTotalAmount().add(amount)) < 0) {
            return null;
        }

        // 更新库存成功
        if (redPackTemplateMapper.updateStockById(packProbability.getId()) > 0) {
            // 增加红包记录
            RedPackRecord record = new RedPackRecord();
            record.setTemplateId(templateId);
            record.setAmount(amount);
            record.setTvId(tvId);
            record.setActivityType(activityType);
            record.setStatus(RedPackConstant.Status.INIT);
            record.setOrderCode(getLocalPayCode(tvId));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, templateInfo.getValidDay());
            record.setInvalidTime(calendar.getTime());
            redPackRecordMapper.addRecord(record);

            GainRedPackRsp res = new GainRedPackRsp();
            res.setRedPackId(record.getId());
            res.setAmount(new DecimalFormat("0.00").format(amount));
            res.setUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, record.getId()));
            return res;
        }

        return null;
    }

    private boolean doHbAuth(GainWechatRedPackReq req) {
        // IP校验, 非白名单IP 一小时之内允许访问5次
        if (ipLimit(req.getIp())) {
            return false;
        }

        // 只抽取一次红包活动
        if (req.getActivityType() > 20000) {
            List<RedPackRecord> records = redPackRecordMapper.getRecordWithActivityType(req.getActivityType(), req.getTvId());
            if (!CollectionUtils.isEmpty(records)) {
                return false;
            }
        }
        // 可多次抽奖类型
        else if (req.getActivityType() >= 101 && req.getActivityType() < 1000) {
            return true;
        }
        // 其他未授信活动，不下发红包
        else {
            return false;
        }

        return true;
    }

    private boolean ipLimit(String ip) {
        String ipWhiteListStr = LoadDataConfig.getDataValueByKey("funtv.activity.hb.ip.whitelist", "");
        if (StringUtils.isBlank(ip) || !ip.matches(ipWhiteListStr)) {

            long reqCount = redisService.increment(IP_REQ_KEY + ip, 1);
            if (reqCount == 1) {
                redisService.expire(IP_REQ_KEY + ip, 1, TimeUnit.HOURS);
            } else if (reqCount > 10) {
                return true;
            }
        }
        return false;
    }

    public Result<?> getRecords(GetRedPackRecordsReq req) {
        GetRedPackRecordsRsp rsp = new GetRedPackRecordsRsp();
        List<RedPackInfo> infos = new ArrayList<RedPackInfo>();
        List<RedPackRecord> records = redPackRecordMapper.getRecords(req.getTvId(), req.getActivityType());
        for (RedPackRecord record : records) {
            RedPackInfo info = new RedPackInfo();
            info.setRedPackId(record.getId());
            info.setActivityType(record.getActivityType());
            info.setAmount(record.getAmount());
            info.setStatus(record.getStatus());
            info.setCreateTime(record.getCreateTime());
            infos.add(info);
        }
        rsp.setInfos(infos);
        return Result.getSuccessResult(rsp);
    }

    public List<RedPackRecord> getHbRecords(Integer tvId, Integer activityType) {
        return redPackRecordMapper.getRecords(tvId, activityType);
    }

    public RedPackProbability getProbability(List<RedPackTemplateDetail> details) {
        List<RedPackProbability> probabilitys = new ArrayList<RedPackProbability>();
        BigDecimal sum = new BigDecimal(0.00);
        for (RedPackTemplateDetail detail : details) {
            RedPackProbability probability = new RedPackProbability();
            probability.setStart(sum);
            probability.setEnd(sum.add(detail.getProbability()));
            sum = sum.add(detail.getProbability());
            BeanUtils.copyProperties(detail, probability);
            probabilitys.add(probability);
        }
        BigDecimal randNum = BigDecimal.valueOf(Math.random());

        for (RedPackProbability probability : probabilitys) {
            if (probability.getStock() > 0 && randNum.compareTo(probability.getStart()) >= 0
                    && randNum.compareTo(probability.getEnd()) < 0) {
                return probability;
            }
        }

        return null;
    }

    private RedPackProbability getProbability_bak(List<RedPackTemplateDetail> details) {
        List<RedPackProbability> probabilitys = new ArrayList<RedPackProbability>();
        BigDecimal sum = new BigDecimal(0.00);
        for (RedPackTemplateDetail detail : details) {
            if (detail.getStock() > 0) {
                RedPackProbability probability = new RedPackProbability();
                probability.setStart(sum);
                probability.setEnd(sum.add(detail.getProbability()));
                sum = sum.add(detail.getProbability());
                BeanUtils.copyProperties(detail, probability);
                probabilitys.add(probability);
            }
        }
        BigDecimal randNum = BigDecimal.valueOf(Math.random()).multiply(sum);

        for (RedPackProbability probability : probabilitys) {
            if (randNum.compareTo(probability.getStart()) >= 0 && randNum.compareTo(probability.getEnd()) < 0) {
                return probability;
            }
        }

        return null;
    }

    /**
     * 根据本地订单号生规则生成本地充值订单号 订单生成规则为yyyyMMddHHmmss-accountId(10位，不够前边补0)
     */
    private String getLocalPayCode(Integer accountId) {
        if (accountId == null) {
            accountId = 0;
        }
        return new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + String.format("%010d", accountId);
    }

    private String getAccessToken() {
        String accessToken = (String) redisService.get(RED_PACKET_TOKEN_KEY);
        if (StringUtils.isBlank(accessToken)) {
            String tokenRsp = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx6f08bbcf08c64921&secret=4e3da0d443f645b4a0993751286e12d2", String.class);
            if (StringUtils.isNotBlank(tokenRsp)) {
                JSONObject jo = JSONObject.parseObject(tokenRsp);
                if (StringUtils.isNotBlank(jo.getString("access_token")) && jo.getIntValue("expires_in") > 0) {
                    redisService.setForTimeCustom(RED_PACKET_TOKEN_KEY, jo.getString("access_token"), jo.getIntValue("expires_in"), TimeUnit.SECONDS);
                    return jo.getString("access_token");
                }
            }
            return "";
        }
        return accessToken;
    }


}
