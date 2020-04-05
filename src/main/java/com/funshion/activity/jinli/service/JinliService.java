package com.funshion.activity.jinli.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccontInfo;
import com.funshion.activity.funtvactivity.mapper.FuntvActivityMapper;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.jinli.entity.*;
import com.funshion.activity.jinli.mapper.JinliEntryListDetailMapper;
import com.funshion.activity.jinli.mapper.JinliEntryListMapper;
import com.funshion.activity.jinli.mapper.JinliPrizeReceiverMapper;
import com.funshion.activity.jinli.mapper.JinliWinRecordMapper;
import com.funshion.activity.jinli.req.JinliAcceptPrizeReq;
import com.funshion.activity.jinli.req.JinliDrawReq;
import com.funshion.activity.jinli.rsp.JinliActivityInfoRsp;
import com.funshion.activity.jinli.rsp.JinliLotteryRsp;
import com.funshion.activity.jinli.rsp.JinliRankRsp;
import com.funshion.activity.job.LoadDataConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class JinliService {

    private static Logger logger = LoggerFactory.getLogger(JinliService.class);

    @Autowired
    JinliWinRecordMapper jinliWinRecordMapper;

    @Autowired
    JinliEntryListMapper jinliEntryListMapper;

    @Autowired
    JinliEntryListDetailMapper jinliEntryListDetailMapper;

    @Autowired
    JinliPrizeReceiverMapper jinliPrizeReceiverMapper;

    @Autowired
    private FuntvActivityMapper funtvActivityMapper;

    @Autowired
    private CommonService commonService;

    @Value("${mercury.host}")
    private String mercuryHost;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public void uploadPlaytime(String body) {
        String jinliStartTime = LoadDataConfig.getDataValueByKey("funtv.activity.2.startTime", "2019-01-30 00:00:01");
        String jinliEndTime = LoadDataConfig.getDataValueByKey("funtv.activity.2.endTime", "2019-02-12 12:00:00");
        long currentTime = System.currentTimeMillis();
        try {
            // 不在活动期间不统计播放时长
            if (currentTime < DATE_FORMAT.parse(jinliStartTime).getTime()
                    || currentTime > DATE_FORMAT.parse(jinliEndTime).getTime()) {
                return;
            }
        } catch (ParseException e) {
            logger.error("uploadPlaytime ParseException", e);
            // 配置错误导致的解析失败，取默认值再校验
            try {
                if (currentTime < DATE_FORMAT.parse("2019-01-30 00:00:01").getTime()
                        || currentTime > DATE_FORMAT.parse("2019-02-12 12:00:00").getTime()) {
                    return;
                }
            } catch (ParseException e1) {
                return;
            }
        }

        List<String> playTimeInfos = JSONObject.parseArray(body, String.class);
        for (String record : playTimeInfos) {
            try {
                String[] arr = record.split(",");
                if (arr.length == 2) {
                    int playTime = NumberUtils.toInt(arr[1], 0);
                    if (playTime > 60 * 24) {
                        playTime = 60 * 24;
                    }
                    int drawNum = playTime / 60;
                    if (drawNum > 0 && (playTime % 60 >= 40)) {
                        drawNum++;
                    }
                    if (drawNum > 24) {
                        drawNum = 24;
                    }

                    String mac = arr[0].toUpperCase();
                    // 保存锦鲤累计播放时长
                    Double score = redisService.zScore(RedisPrefix.JINLI_PLAY_TIME_TOP, mac);
                    score = score == null ? 0 : score;
                    redisService.zAdd(RedisPrefix.JINLI_PLAY_TIME_TOP, mac, score + playTime);
                    JinliUserInfoInRedis userInfo = new JinliUserInfoInRedis();
                    userInfo.setNewPlayTime(playTime);
                    userInfo.setTotalPlayTime(score + playTime);
                    userInfo.setRemainDrawNum(drawNum);
                    userInfo.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    redisService.setForTimeCustom(RedisPrefix.JINLI_DAILY_USER_INFO + mac, userInfo, 15, TimeUnit.DAYS);
                }
            } catch (Exception e) {
                logger.error("prase exception, the record is:" + record);
            }
        }

    }

    public Result<?> lottery(JinliDrawReq req) {
        JinliUserInfoInRedis userInfo = (JinliUserInfoInRedis) redisService.get(RedisPrefix.JINLI_DAILY_USER_INFO + req.getMac().toUpperCase());
        if (userInfo == null || userInfo.getRemainDrawNum() < 1
                || !SDF.format(new Date()).equals(userInfo.getUpdateDate())) {
            return Result.getFailureResult("903", "没有抽奖次数了");
        }

        // 通过配置取抽奖活动ID
        req.setLotteryId(NumberUtils.toInt(LoadDataConfig.getDataValueByKey("funtv.activity.2.lotteryId", "9"), 9));
        String lotteryRes = commonService.lotteryFromMercury(req);
        if (StringUtils.isBlank(lotteryRes)) {
            return Result.SYSTEM_ERROR;
        }

        JSONObject jo = JSONObject.parseObject(lotteryRes);
        JinliLotteryRsp rsp = new JinliLotteryRsp();
        if ("200".equals(jo.getString("retCode"))) {
            JSONObject data = jo.getJSONObject("data");
            if (!JinliConstants.PrizeType.NOTHING.equals(data.getString("prizeType"))) {
                JinliWinRecord record = new JinliWinRecord();
                record.setAccountId(req.getTvId());
                record.setMac(req.getMac());
                record.setPrizeId(data.getString("prizeId"));
                record.setPrizeName(data.getString("prizeName"));
                record.setPrizeType(data.getString("prizeType"));
                record.setPrizeImg(data.getString("prizeImg"));
                if (StringUtils.isNotBlank(data.getString("invalidTime"))) {
                    record.setInvalidTime(JSONObject.parseObject(data.getString("invalidTime"), Date.class));
                }

                record.setStatus(JinliConstants.PrizeStatus.TO_ACCEPT);
                jinliWinRecordMapper.addWinRecord(record);
                BeanUtils.copyProperties(record, rsp);
                rsp.setWinId(record.getId());
            } else {
                rsp.setPrizeType(data.getString("prizeType"));
                rsp.setPrizeImg(data.getString("prizeImg"));
                rsp.setPrizeName(data.getString("prizeName"));
                rsp.setPrizeId(data.getString("prizeId"));
            }
            // 剩余抽奖次数减一
            userInfo.setRemainDrawNum(userInfo.getRemainDrawNum() - 1);
            redisService.setForTimeCustom(RedisPrefix.JINLI_DAILY_USER_INFO + req.getMac().toUpperCase(), userInfo, 15, TimeUnit.DAYS);
            rsp.setRemainDrawNum(userInfo.getRemainDrawNum());
            return Result.getSuccessResult(rsp);
        }

        return Result.getFailureResult(jo.getString("retCode"), jo.getString("retMsg"));

    }

    public Result<?> getMyPrizes(Integer accountId) {
        List<JinliWinRecord> winRecords = jinliWinRecordMapper.getMyWinRecords(accountId);
        for (JinliWinRecord record : winRecords) {
            // 判断已过期，实时更新状态，数据库记录等定时任务更新
            if (record.getInvalidTime() != null && new Date().after(record.getInvalidTime())) {
                record.setStatus(JinliConstants.PrizeStatus.EXPIRED);
            }
        }
        return Result.getSuccessResult(winRecords);
    }

    public Result<?> rank(String mac) {
        JinliRankRsp res = new JinliRankRsp();
        res.setDetails(dailyTop());
        res.setMac(mac.toUpperCase());
        JinliUserInfoInRedis userInfo = (JinliUserInfoInRedis) redisService.get(RedisPrefix.JINLI_DAILY_USER_INFO + mac.toUpperCase());
        if (userInfo == null) {
            userInfo = new JinliUserInfoInRedis();
            res.setNewPlayTime("0小时0分钟");
            res.setTotalPlayTime("0小时0分钟");
            res.setRanking(9999L);
        } else {
            res.setNewPlayTime((userInfo.getNewPlayTime() / 60) + "小时" + (userInfo.getNewPlayTime() % 60) + "分钟");
            res.setTotalPlayTime(
                    (userInfo.getTotalPlayTime().intValue() / 60) + "小时" + (userInfo.getTotalPlayTime().intValue() % 60)
                            + "分钟");
            Long rank = redisService.zRank(RedisPrefix.JINLI_PLAY_TIME_TOP, mac.toUpperCase());
            res.setRanking(rank);
        }

        return Result.getSuccessResult(res);
    }

    public List<JinliRankInfo> dailyTop() {
        Set<ZSetOperations.TypedTuple<Object>> tops = redisService.reverseRangeWithScores(RedisPrefix.JINLI_PLAY_TIME_TOP, 0, 99);
        // Set<Tuple> tops = redisService.reverseRangeByScore(RedisPrefix.JINLI_PLAY_TIME_TOP, 0, 99);
        List<JinliRankInfo> topList = new ArrayList<JinliRankInfo>();
        if (tops != null) {
            int i = 1;
            for (ZSetOperations.TypedTuple tuple : tops) {
                JinliRankInfo info = new JinliRankInfo();
                info.setRanking(i++);
                info.setMac(tuple.getValue().toString().toUpperCase());
                info.setTotalPlayTime((tuple.getScore() / 60) + "小时" + (tuple.getScore() % 60) + "分钟");
                topList.add(info);
            }
        }

        return topList;
    }

    public Result<?> getActivityInfo(Integer tvId, String mac) {
        JinliActivityInfoRsp rsp = new JinliActivityInfoRsp();
        JinliUserInfoInRedis userInfo = (JinliUserInfoInRedis) redisService.get(RedisPrefix.JINLI_DAILY_USER_INFO + mac.toUpperCase());
        if (userInfo == null || userInfo.getRemainDrawNum() < 1
                || !SDF.format(new Date()).equals(userInfo.getUpdateDate())) {
            rsp.setRemainDrawNum(0);
        } else {
            rsp.setRemainDrawNum(userInfo.getRemainDrawNum());
        }

        List<JinliEntryData> rows = new ArrayList<JinliEntryData>();
        List<JinliEntryListInfo> entrys = jinliEntryListMapper.getEntryList();
        for (JinliEntryListInfo entry : entrys) {
            JinliEntryData data = new JinliEntryData();
            data.setBgColor(entry.getBgColor());
            data.setCornerImg(ImgUtils.getImgUrl(entry.getCornerImg()));
            data.setTitle(entry.getTitle());
            List<JinliEntryListDetail> details = jinliEntryListDetailMapper.getDetailsByListId(entry.getId());
            for (JinliEntryListDetail detail : details) {
                detail.setIcon(ImgUtils.getImgUrl(detail.getIcon()));
            }
            data.setDetails(details);
            rows.add(data);
        }
        rsp.setRows(rows);
        rsp.setBgImg(ImgUtils.getImgUrl(jinliEntryListMapper.getTopBgImg()));
        if (userInfo == null) {
            rsp.setNewPlayTime("1分钟");
            rsp.setTotalPlayTime("1分钟");
            rsp.setRank(99999L);
        } else {
            DecimalFormat df = new DecimalFormat("0.0");
            if (userInfo.getNewPlayTime() == null || userInfo.getNewPlayTime() < 1
                    || !SDF.format(new Date()).equals(userInfo.getUpdateDate())) {
                rsp.setNewPlayTime("1分钟");
            } else if (userInfo.getNewPlayTime() < 60) {
                rsp.setNewPlayTime(userInfo.getNewPlayTime() + "分钟");
            } else {
                rsp.setNewPlayTime(df.format((float) userInfo.getNewPlayTime() / 60) + "小时");
            }

            if (userInfo.getTotalPlayTime() < 1) {
                rsp.setTotalPlayTime("1分钟");
            } else if (userInfo.getTotalPlayTime() < 60) {
                rsp.setTotalPlayTime(userInfo.getTotalPlayTime().intValue() + "分钟");
            } else {
                rsp.setTotalPlayTime(df.format(userInfo.getTotalPlayTime() / 60) + "小时");
            }

            Long rank = redisService.reverseRank(RedisPrefix.JINLI_PLAY_TIME_TOP, mac.toUpperCase()) + 1;
            if (rank <= 0) {
                rank = 99999L;
            }
            rsp.setRank(rank);
        }

        FuntvActivityAccontInfo accountInfo = funtvActivityMapper.getAccountInfo(tvId,
                NumberUtils.toInt(ActivityConstants.ActivityType.JINLI));
        if (accountInfo != null) {
            rsp.setPhone(accountInfo.getPhone());
        }
        return Result.getSuccessResult(rsp);
    }

    public Result<?> acceptPrice(JinliAcceptPrizeReq req) {
        JinliWinRecord record = jinliWinRecordMapper.getWinRecordById(req.getWinId());
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
            JinliPrizeReceiver info = new JinliPrizeReceiver();
            info.setAccountId(req.getTvId());
            info.setMac(req.getMac());
            info.setWinId(req.getWinId());
            info.setConsignee(req.getConsignee());
            info.setPhone(req.getPhone());
            info.setAddress(req.getAddress());
            info.setPrizeName(record.getPrizeName());
            info.setPrizeImg(record.getPrizeImg());
            info.setPrizeType(record.getPrizeType());
            jinliPrizeReceiverMapper.savePrizeReceiver(info);
        }

        jinliWinRecordMapper.updateStatusById(req.getWinId());
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

}
