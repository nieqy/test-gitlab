package com.funshion.activity.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.bean.*;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.mapper.PrizeReceiverMapper;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.jinli.rsp.JinliLotteryRsp;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.orange.entity.AppSignInfo;
import com.funshion.activity.orange.entity.SignButtonInfo;
import com.funshion.activity.orange.mapper.AppSignInfoMapper;
import com.funshion.activity.pullalive.mapper.SignConfigMapper;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.domain.RedPackRecord;
import com.funshion.activity.redpacket.rsp.GainRedPackRsp;
import com.funshion.activity.redpacket.service.WechatRedPackService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LotteryService {

    private static Logger logger = LoggerFactory.getLogger(LotteryService.class);

    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;
    @Autowired
    PrizeReceiverMapper prizeReceiverMapper;
    @Autowired
    private AppSignInfoMapper appSignInfoMapper;

    @Value("${mercury.host}")
    private String mercuryHost;

    @Autowired
    private LotteryLogService lotteryLogService;

    @Autowired
    private WechatRedPackService wechatRedPackService;

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private SignConfigMapper signConfigMapper;

    @Autowired
    private RedisService redisService;

    public Result<?> doCommonLottery(LotteryReq req) {
        JinliLotteryRsp rsp = new JinliLotteryRsp();
        // 抽奖次数限制
        Integer chance = 0;
        String chanceRedisKey = LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".lotteryChance", "");
        if (StringUtils.isNotBlank(chanceRedisKey)) {
            String reidsKey = chanceRedisKey + req.getTvId();
            chance = (Integer) redisService.get(reidsKey);
            if (chance == null || chance < 1) {
                // 机会已用完图片
                String noChanceImg =
                        LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".noChanceImg", RedPackConstant.Img.NO_CHANCE_IMG);
                rsp.setPrizeName("抽奖次数已用完");
                rsp.setPrizeType(JinliConstants.PrizeType.NOTHING);
                rsp.setPrizeImg(noChanceImg);
                return Result.getFailureResult("903", "抽奖次数已用完", rsp);
            }
        }
        String noPrizeImg = LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".noPrizeImg", RedPackConstant.Img.NO_PRIZE_IMG);
        // 一个活动只允许抽一次奖
        if (req.getActivityType() > 20000) {
            List<LotteryPrize> prizes = lotteryPrizeMapper.getMyPrizes(req.getTvId(), req.getActivityType());
            if (!CollectionUtils.isEmpty(prizes)) {
                LotteryPrize prize = prizes.get(0);
                if (JinliConstants.PrizeStatus.TO_ACCEPT == prize.getStatus()) {
                    BeanUtils.copyProperties(prize, rsp);
                    rsp.setWinId(prize.getId());
                    rsp.setPrizeUrl(getAcceptUrl(prize.getPrizeType(), prize.getId(), prize.getPrizeUrl(),
                            req.getTvId(), req.getMac(), req.getActivityType()));
                    return Result.getSuccessResult(rsp);
                }
            }

            List<RedPackRecord> hbRecords = wechatRedPackService.getHbRecords(req.getTvId(), req.getActivityType());
            if (!CollectionUtils.isEmpty(hbRecords)) {
                RedPackRecord record = hbRecords.get(0);
                if (RedPackConstant.Status.INIT == record.getStatus()) {
                    rsp.setWinId(record.getId());
                    rsp.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, req.getTvId(), record.getId()));
                    rsp.setPrizeType(JinliConstants.PrizeType.RED_PACKET);
                    rsp.setPrizeName(String.format("现金%.2f元", record.getAmount().doubleValue()));
                    rsp.setInvalidTime(record.getInvalidTime());
                    return Result.getSuccessResult(rsp);
                }
            }

            List<LotteryLog> lotteryLogs = lotteryLogService.getLotteryLog(req.getTvId(), req.getActivityType());
            if (!CollectionUtils.isEmpty(lotteryLogs)) {
                rsp.setPrizeName("谢谢参与");
                rsp.setPrizeType(JinliConstants.PrizeType.NOTHING);
                rsp.setPrizeImg(noPrizeImg);
                return Result.getSuccessResult(rsp);
            }
        }

        req.setLotteryId(NumberUtils.toInt(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".lotteryId"), 0));
        // 看片游世界活动
        if (req.getActivityType() == 300) {
            // 校验是否达到终极大奖条件
            List<Integer> actTypes = signConfigMapper.getActivityTypes(300);
            int signedCount = appSignInfoMapper.getTravelSignedCount(req.getTvId(), StringUtils.join(actTypes, ","));
            if (CollectionUtils.isEmpty(actTypes) || signedCount < actTypes.size()) {
                rsp.setPrizeName("谢谢参与");
                rsp.setPrizeType(JinliConstants.PrizeType.NOTHING);
                rsp.setPrizeImg(noPrizeImg);
                return Result.getSuccessResult(rsp);
            }
        }

        if (req.getActivityType() >= 300 && req.getActivityType() < 310) {
            List<LotteryLog> logs = lotteryLogService.getTodayLotteryLog(req.getTvId(), req.getActivityType());
            if (!CollectionUtils.isEmpty(logs)) {
                rsp.setPrizeName("谢谢参与");
                rsp.setPrizeType(JinliConstants.PrizeType.NOTHING);
                rsp.setPrizeImg(noPrizeImg);
                return Result.getSuccessResult(rsp);
            }
        }

        // 调用会员的抽奖接口
        req.setVersion(req.getVersion());
        String lotteryRes = this.commonLottery(req);
        if (StringUtils.isBlank(lotteryRes)) {
            return Result.SYSTEM_ERROR;
        }
        // 异步记录抽奖的操作
        LotteryLog logInfo = new LotteryLog();
        logInfo.setTvId(req.getTvId());
        logInfo.setActivityType(req.getActivityType());
        logInfo.setRemarks("通用抽奖");
        lotteryLogService.saveLotteryLog(logInfo);

        // 减少抽奖机会
        if (chance != null && chance > 0) {
            String reidsKey = chanceRedisKey + req.getTvId();
            redisService.setForTimeCustom(reidsKey, chance - 1, 30, TimeUnit.DAYS);
        }

        // 解析抽奖结果
        JSONObject jo = JSONObject.parseObject(lotteryRes);
        if ("200".equals(jo.getString("retCode"))) {
            JSONObject data = jo.getJSONObject("data");
            String prizeType = data.getString("prizeType");
            if (JinliConstants.PrizeType.NOTHING.equals(prizeType)) {
                rsp.setPrizeType(data.getString("prizeType"));
                rsp.setPrizeImg(noPrizeImg);
                rsp.setPrizeName(data.getString("prizeName"));
                rsp.setPrizeId(data.getString("prizeId"));
                rsp.setPrizeUrl(data.getString("prizeUrl"));
            } else if (JinliConstants.PrizeType.RED_PACKET.equals(prizeType)) {
                Integer templateId = data.getIntValue("prizeId");
                GainRedPackRsp gainRedPackRsp = wechatRedPackService.getHb(req.getTvId(), req.getActivityType(), templateId);
                if (gainRedPackRsp == null) {
                    rsp.setPrizeType(JinliConstants.PrizeType.NOTHING);
                    rsp.setPrizeImg(noPrizeImg);
                    rsp.setPrizeName("谢谢参与");
                    rsp.setPrizeId("");
                    rsp.setPrizeUrl("");
                } else {
                    rsp.setPrizeType(JinliConstants.PrizeType.RED_PACKET);
                    rsp.setPrizeUrl(gainRedPackRsp.getUrl());
                    rsp.setWinId(gainRedPackRsp.getRedPackId());
                    rsp.setPrizeName(String.format("现金%s元", gainRedPackRsp.getAmount()));
                    rsp.setAmount(gainRedPackRsp.getAmount());
                }
            } else {
                LotteryPrize record = new LotteryPrize();
                record.setAccountId(req.getTvId());
                record.setActivityType(req.getActivityType());
                record.setMac(req.getMac());
                record.setPrizeId(data.getString("prizeId"));
                record.setPrizeName(data.getString("prizeName"));
                record.setPrizeType(data.getString("prizeType"));
                record.setPrizeImg(data.getString("prizeImg"));
                record.setPrizeUrl(data.getString("prizeUrl"));

                if (StringUtils.isNotBlank(data.getString("invalidTime"))) {
                    record.setInvalidTime(JSONObject.parseObject(data.getString("invalidTime"), Date.class));
                }
                record.setLotteryId(req.getLotteryId());
                record.setStatus(JinliConstants.PrizeStatus.TO_ACCEPT);
                lotteryPrizeMapper.addWinRecord(record);
                BeanUtils.copyProperties(record, rsp);
                rsp.setPrizeUrl(getAcceptUrl(record.getPrizeType(), record.getId(), record.getPrizeUrl(),
                        req.getTvId(), req.getMac(), req.getActivityType()));
                rsp.setWinId(record.getId());
            }

            return Result.getSuccessResult(rsp);
        }

        return Result.getFailureResult(jo.getString("retCode"), jo.getString("retMsg"), rsp);
    }

    public Result<?> doLottery(LotteryReq req) {
        Map<String, Object> res = new HashMap<String, Object>();
        AppSignInfo lastSign = appSignInfoMapper.selectLastSignHistory(String.valueOf(req.getTvId()));
        int total = lastSign.getTotal();
        List<SignButtonInfo> buttons = new ArrayList<SignButtonInfo>();
        if (total < 7) {
            SignButtonInfo b1 = new SignButtonInfo("disable", total < 3 ? "再签" + (3 - total) + "天抽小奖" : "再签" + (7 - total) + "天抽大奖");
            buttons.add(b1);
        } else {
            SignButtonInfo b1 = new SignButtonInfo("disable", "明日继续签到抽奖");
            buttons.add(b1);
        }
        res.put("prizeDay", total);
        res.put("buttons", buttons);
        if (lastSign == null) {
            return Result.getFailureResult("903", "没有抽奖次数了", res);
        }
        if (lastSign.getTotal() == 3 || lastSign.getTotal() == 7) {
            if (lastSign.getIsGive() == 1) {
                return Result.getFailureResult("903", "没有抽奖次数了", res);
            }
        } else {
            return Result.getFailureResult("903", "没有抽奖次数了", res);
        }

        // 通过配置取抽奖活动ID
        req.setLotteryId(NumberUtils.toInt(LoadDataConfig
                .getDataValueByKey("funtv.activity.{}.lotteryId".replace("{}", String.valueOf(lastSign.getSignId() + lastSign.getTotal())))));
        String lotteryRes = this.commonLottery(req);
        if (StringUtils.isBlank(lotteryRes)) {
            return Result.SYSTEM_ERROR;
        }

        JSONObject jo = JSONObject.parseObject(lotteryRes);
        JinliLotteryRsp rsp = new JinliLotteryRsp();
        if ("200".equals(jo.getString("retCode"))) {
            JSONObject data = jo.getJSONObject("data");
            if (!JinliConstants.PrizeType.NOTHING.equals(data.getString("prizeType"))) {
                LotteryPrize record = new LotteryPrize();
                record.setAccountId(req.getTvId());
                record.setActivityType(req.getActivityType());
                record.setMac(req.getMac());
                record.setPrizeId(data.getString("prizeId"));
                record.setPrizeName(data.getString("prizeName"));
                record.setPrizeType(data.getString("prizeType"));
                record.setPrizeImg(data.getString("prizeImg"));
                record.setPrizeUrl(data.getString("prizeUrl"));

                if (StringUtils.isNotBlank(data.getString("invalidTime"))) {
                    record.setInvalidTime(JSONObject.parseObject(data.getString("invalidTime"), Date.class));
                }
                record.setLotteryId(req.getLotteryId());
                record.setStatus(JinliConstants.PrizeStatus.TO_ACCEPT);
                lotteryPrizeMapper.addWinRecord(record);
                if (record.getPrizeType().equals(JinliConstants.PrizeType.PRODUCT)) {
                    record.setPrizeUrl("http://wx-tv.funshion.com/address?tvId=" + req.getTvId() + "&mac=" + req.getMac() + "&actType=10000&sourceId=" + record.getId());
                }
                BeanUtils.copyProperties(record, rsp);
                rsp.setWinId(record.getId());
            } else {
                rsp.setPrizeType(data.getString("prizeType"));
                rsp.setPrizeImg(data.getString("prizeImg"));
                rsp.setPrizeName(data.getString("prizeName"));
                rsp.setPrizeId(data.getString("prizeId"));
                rsp.setPrizeUrl(data.getString("prizeUrl"));
            }
            // 剩余抽奖次数减一
            appSignInfoMapper.updateSignHistory(lastSign.getId());
            res.put("prize", rsp);
            return Result.getSuccessResult(res);
        }

        return Result.getFailureResult(jo.getString("retCode"), jo.getString("retMsg"), res);

    }

    public Result<?> getMyPrizes(Integer accountId, Integer activityType) {
        List<LotteryPrize> winRecords = lotteryPrizeMapper.getMyPrizes(accountId, activityType);
        for (LotteryPrize record : winRecords) {
            if (record.getPrizeType().equals(JinliConstants.PrizeType.PRODUCT)) {
                record.setPrizeUrl("http://wx-tv.funshion.com/address?tvId=" + accountId + "&mac=" + record.getMac() + "&actType=10000&sourceId=" + record.getId());
            }
            // 判断已过期，实时更新状态，数据库记录等定时任务更新
            if (record.getInvalidTime() != null && new Date().after(record.getInvalidTime())) {
                record.setStatus(JinliConstants.PrizeStatus.EXPIRED);
            }
        }
        return Result.getSuccessResult(winRecords);
    }

    public Result<?> acceptPrice(AcceptPrizeReq req) {
        LotteryPrize record = lotteryPrizeMapper.getWinRecordById(req.getWinId());
        if (record == null || !req.getTvId().equals(record.getAccountId())) {
            return Result.getFailureResult("905", "未查到中奖记录");
        }

        if (record.getStatus() == JinliConstants.PrizeStatus.ACCEPT) {
            return Result.getFailureResult("906", "奖品已领取");
        } else if (record.getStatus() == JinliConstants.PrizeStatus.EXPIRED) {
            return Result.getFailureResult("907", "奖品已过期");
        }

        // 金卡会员 、学而思、精品课堂
        if (JinliConstants.PrizeType.VIP.equals(record.getPrizeType())
                || JinliConstants.PrizeType.THIRDPARTY.equals(record.getPrizeType())) {
            String acceptRes = this.acceptFromMercury(req.getTvId(), NumberUtils.toInt(record.getPrizeId(), 0));
            JSONObject json = JSON.parseObject(acceptRes);
            int retCode = json.getIntValue("retCode");
            if (retCode != 200) {
                return Result.getFailureResult("908", "领奖失败，请联系：400-600-6258");
            }
        }
        // 实物奖品
        else if (JinliConstants.PrizeType.PRODUCT.equals(record.getPrizeType())) {
            PrizeReceiver info = new PrizeReceiver();
            info.setAccountId(req.getTvId());
            info.setMac(req.getMac());
            info.setWinId(req.getWinId());
            info.setConsignee(req.getConsignee());
            info.setPhone(req.getPhone());
            info.setAddress(req.getAddress());
            info.setPrizeName(record.getPrizeName());
            info.setPrizeImg(record.getPrizeImg());
            info.setPrizeType(record.getPrizeType());
            info.setActivityType(record.getActivityType());
            prizeReceiverMapper.savePrizeReceiver(info);
        }

        lotteryPrizeMapper.updateStatusById(req.getWinId());
        return Result.getSuccessResult();
    }


    private String acceptFromMercury(Integer accountId, Integer commodityId) {
        StringBuffer sb = new StringBuffer();
        String ctime = System.currentTimeMillis() + "";
        String key = "201f1696bea924252cbe0a9324576c2f";
        sb.append(mercuryHost).append("/api/summer/movie/give").append("?").append("accountId=").append(accountId)
                .append("&").append("ctime=").append(ctime).append("&");
        sb.append("commodityId=").append(commodityId).append("&").append("sign=")
                .append(MD5Utils.getStringMD5String(accountId + ctime + commodityId + key));
        //请求会员接口  accountId, commityId, ctime, sign
        try {
            String acceptResponse = restTemplate.getForObject(sb.toString(), String.class);
            return acceptResponse;
        } catch (Exception e) {
            logger.error("领奖失败: {} - {}", accountId, e);
        }
        return null;
    }

    private String commonLottery(LotteryReq req) {
        StringBuilder sb = new StringBuilder();
        sb.append(mercuryHost).append("/api/lottery/drawForActivity").append("?account_id=").append(req.getTvId())
                .append("&activityType=").append(req.getActivityType()).append("&version=").append(req.getVersion())
                .append("&ctime=").append(req.getCtime()).append("&lotteryId=").append(req.getLotteryId())
                .append("&sign=").append(MD5Utils
                .getMD5String(req.getTvId() + "" + req.getActivityType() + req.getLotteryId() + req.getCtime()
                        + "df2eb3e697746331"));

        //请求会员接口  accountId, lotteryId, ctime, sign
        try {
            String acceptResponse = restTemplate.getForObject(sb.toString(), String.class);
            return acceptResponse;
        } catch (Exception e) {
            logger.error("lotteryFromMercury exception ", e);
        }
        return null;
    }


    public Result<?> addPrize(AddPrizeReq req) {
        LotteryPrize record = new LotteryPrize();
        record.setAccountId(req.getTvId());
        record.setActivityType(req.getActivityType());
        record.setMac(req.getMac());
        record.setPrizeId(req.getPrizeId());
        record.setPrizeName(req.getPrizeName());
        record.setPrizeType(req.getPrizeType());
        record.setPrizeImg(req.getPrizeImg());
        record.setPrizeUrl(req.getPrizeUrl());

        if (StringUtils.isNotBlank(req.getInvalidTime())) {
            try {
                record.setInvalidTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(req.getInvalidTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        record.setLotteryId(req.getLotteryId());
        record.setStatus(JinliConstants.PrizeStatus.TO_ACCEPT);
        lotteryPrizeMapper.addWinRecord(record);

        Map dataMap = new HashMap();
        // 设置奖品URL
        dataMap.put("url", getAcceptUrl(record.getPrizeType(), record.getId(), record.getPrizeUrl(),
                req.getTvId(), req.getMac(), req.getActivityType()));
        return Result.getSuccessResult(dataMap);
    }

    public String getAcceptUrl(String prizeType, Integer winId, String prizeUrl, Integer tvId, String mac, Integer activityType) {
        if (JinliConstants.PrizeType.PRODUCT.equals(prizeType)) {
            return "http://wx-tv.funshion.com/address?tvId=" + tvId + "&mac=" + mac + "&actType=" + activityType + "&sourceId=" + winId;
        } else if (JinliConstants.PrizeType.VIP.equals(prizeType)) {
            String ctime = new Date().getTime() + "";
            String sign = MD5Utils.getMD5String(tvId + "" + activityType + winId + ctime + ActivityConstants.SignKey.ORANGE_SIGN);
            StringBuilder sb = new StringBuilder();
            sb.append("https://activities-tvapi.funshion.com/api/lottery/acceptPrize?tvId=").append(tvId)
                    .append("&mac=").append(mac)
                    .append("&activityType=").append(activityType)
                    .append("&winId=").append(winId)
                    .append("&ctime=").append(ctime)
                    .append("&sign=").append(sign);
            return sb.toString();
        } else {
            return prizeUrl;
        }
    }
}
