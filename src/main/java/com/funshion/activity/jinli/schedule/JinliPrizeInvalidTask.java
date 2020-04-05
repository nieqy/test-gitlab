package com.funshion.activity.jinli.schedule;


import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.jinli.mapper.JinliWinRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class JinliPrizeInvalidTask {

    protected Logger logger = LoggerFactory.getLogger(JinliPrizeInvalidTask.class);

    private static final String KEY = RedisPrefix.SYNC_LOCK + "JinliPrizeInvalidTask";

    @Autowired
    JinliWinRecordMapper jinliWinRecordMapper;

    @Autowired
    private RedisService redisService;

    @Value("${schedule.on.off}")
    private String schedule_on_off;

    @Scheduled(cron = "0 5 * * * ?")
    public void prizeInvalidTask() {
        if (!"on".equals(schedule_on_off)) {
            return;
        }

        if (redisService.setIfAbsent(KEY, "1", 30, TimeUnit.MINUTES)) {
            try {
                // 中奖记录过期
                jinliWinRecordMapper.updateExpiredStatus();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            } finally {

            }
        }

    }

}
