package com.funshion.activity.job;

import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.domain.GetRedPackStatusRsp;
import com.funshion.activity.redpacket.domain.RedPackRecord;
import com.funshion.activity.redpacket.mapper.RedPackRecordMapper;
import com.funshion.activity.redpacket.utils.WechatServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedPackRecordTask {

    @Autowired
    private RedPackRecordMapper redPackRecordMapper;

    @Autowired
    private RedisService redisService;

    @Value("${schedule.on.off}")
    private String schedule_on_off;

    private final static String LOCK_KEY = "activity_api.redpacket.tasklock";

    private final static String LOCK_KEY2 = "activity_api.redpacket.tasklock2";

    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateRedPackStatus() {
        if (!"on".equals(schedule_on_off)) {
            return;
        }
        if (redisService.setIfAbsent(LOCK_KEY, 1, 2, TimeUnit.MINUTES)) {
            List<RedPackRecord> records = redPackRecordMapper.getSentRecords();
            for (RedPackRecord record : records) {
                GetRedPackStatusRsp statusRsp = WechatServiceUtils.gethbinfo(record.getPCode());
                if (statusRsp != null && "SUCCESS".equals(statusRsp.getReturn_code()) && "SUCCESS".equals(statusRsp.getResult_code())) {
                    int status = 2;
                    if ("RECEIVED".equals(statusRsp.getStatus())) {
                        status = RedPackConstant.Status.ARRIVAL;
                    } else if ("RFUND_ING".equals(statusRsp.getStatus()) || "REFUND".equals(statusRsp.getStatus())) {
                        status = RedPackConstant.Status.REFUND;
                    }

                    if (status > 2) {
                        redPackRecordMapper.updateStatus(record.getId(), status);
                    }
                }
            }
        }
    }


    @Scheduled(cron = "0 0/30 * * * ?")
    public void expireRecord() {
        if (!"on".equals(schedule_on_off)) {
            return;
        }
        if (redisService.setIfAbsent(LOCK_KEY2, 1, 5, TimeUnit.MINUTES)) {
            redPackRecordMapper.updateExpireRecords();
        }
    }
}
