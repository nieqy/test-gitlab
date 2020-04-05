package com.funshion.activity.pullalive.service;

import com.funshion.activity.common.bean.LotteryLog;
import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryLogMapper;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.service.LotteryService;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.entity.JinliUserInfoInRedis;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.pullalive.domain.PullAliveReq;
import com.funshion.activity.pullalive.domain.PullAliveTemplate1Rsp;
import com.funshion.activity.pullalive.domain.Template1Info;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.domain.RedPackRecord;
import com.funshion.activity.redpacket.mapper.RedPackRecordMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PullAliveService {
    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LotteryLogMapper lotteryLogMapper;

    @Autowired
    private RedPackRecordMapper redPackRecordMapper;

    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;

    public Result getTemplate1Launcher(PullAliveReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + req.getMac() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        PullAliveTemplate1Rsp rsp = new PullAliveTemplate1Rsp();

        JinliUserInfoInRedis userInfo = (JinliUserInfoInRedis) redisService.get(RedisPrefix.JINLI_DAILY_USER_INFO + req.getMac().toUpperCase());
        if (userInfo == null) {
            rsp.setPlayDays(0);
        } else {
            rsp.setPlayDays(userInfo.getPlayDays());
        }

        rsp.setBgImg(LoadDataConfig.getDataValueByKey("funtv.activity.20000.bgImg", ""));
        rsp.setRuleContent(LoadDataConfig.getDataValueByKey("funtv.activity.20000.ruleContent", ""));
        List<Template1Info> blocks = new ArrayList<Template1Info>();
        rsp.setBlocks(blocks);

        // 累计观看4天活动
        blocks.add(getBlockDetail(req.getTvId(), rsp.getPlayDays(), 4, 20004));

        // 累计观看8天活动
        blocks.add(getBlockDetail(req.getTvId(), rsp.getPlayDays(), 8, 20008));

        // 累计观看15天活动
        blocks.add(getBlockDetailFor15(req.getTvId(), rsp.getPlayDays(), 15, 20015));

        return Result.getSuccessResult(rsp);
    }


    private Template1Info getBlockDetail(Integer tvId, Integer playDays, Integer requiredDays, Integer activityType) {
        Template1Info info = new Template1Info();
        info.setActivityType(activityType + "");
        info.setIcon(LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".icon", ""));
        info.setTopicType(LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".topicType", ""));
        String topicId = LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".topicId", "");
        info.setTopicId(NumberUtils.toInt(topicId, 0));

        if (playDays < requiredDays) {
            info.setStatus(0);
            // info.setButtonText("再看{}天可领取".replace("{}", (requiredDays - playDays) + ""));
            info.setButtonText("未达成");
            return info;
        }

        List<LotteryLog> lotteryLogs = lotteryLogMapper.getLotteryLog(tvId, activityType);
        if (CollectionUtils.isEmpty(lotteryLogs)) {
            info.setStatus(1);
            info.setButtonText("立即抽奖");
            return info;
        }

        List<RedPackRecord> winRecords = redPackRecordMapper.getRecords(tvId, activityType);
        if (CollectionUtils.isEmpty(winRecords)) {
            info.setStatus(2);
            info.setButtonText("未中奖");
            return info;
        }

        RedPackRecord winRecord = winRecords.get(0);
        if (winRecord.getStatus() == RedPackConstant.Status.INIT) {
            info.setStatus(3);
            info.setButtonText("立即领取");
            info.setPrizeName(winRecord.getAmount() + "");
            info.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, winRecord.getId()));
            return info;
        }

        info.setStatus(4);
        info.setButtonText("已完成");
        return info;
    }

    private Template1Info getBlockDetailFor15(Integer tvId, Integer playDays, Integer requiredDays, Integer activityType) {
        Template1Info info = new Template1Info();
        info.setActivityType(activityType + "");
        info.setIcon(LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".icon", ""));
        info.setTopicType(LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".topicType", ""));
        String topicId = LoadDataConfig.getDataValueByKey("funtv.activity." + activityType + ".topicId", "");
        info.setTopicId(NumberUtils.toInt(topicId, 0));

        if (playDays < requiredDays) {
            info.setStatus(0);
            info.setButtonText("未达成");
            return info;
        }

        List<LotteryLog> lotteryLogs = lotteryLogMapper.getLotteryLog(tvId, activityType);
        if (CollectionUtils.isEmpty(lotteryLogs)) {
            info.setStatus(1);
            info.setButtonText("立即抽奖");
            return info;
        }

        List<LotteryPrize> winRecords = lotteryPrizeMapper.getMyPrizes(tvId, activityType);
        if (CollectionUtils.isEmpty(winRecords)) {
            info.setStatus(2);
            info.setButtonText("未中奖");
            return info;
        }

        LotteryPrize winRecord = winRecords.get(0);
        // 待领取状态
        if (winRecord.getStatus() == 0) {
            info.setStatus(3);
            info.setButtonText("立即领取");
            info.setPrizeUrl(lotteryService.getAcceptUrl(winRecord.getPrizeType(), winRecord.getId(), winRecord.getPrizeUrl(),
                    winRecord.getAccountId(), winRecord.getMac(), winRecord.getActivityType()));
            info.setPrizeImg(winRecord.getPrizeImg());
            info.setPrizeName(winRecord.getPrizeName());
            return info;
        }

        info.setStatus(4);
        info.setButtonText("已完成");
        return info;
    }
}
