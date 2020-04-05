package com.funshion.activity.common.service;

import com.funshion.activity.common.bean.LotteryLog;
import com.funshion.activity.common.mapper.LotteryLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LotteryLogService {

    @Autowired
    private LotteryLogMapper lotteryLogMapper;

    @Async
    public void saveLotteryLog(LotteryLog info) {
        lotteryLogMapper.saveLotteryLog(info);
    }

    public List<LotteryLog> getLotteryLog(Integer tvId, Integer activityType) {
        return lotteryLogMapper.getLotteryLog(tvId, activityType);
    }

    public List<LotteryLog> getTodayLotteryLog(Integer tvId, Integer activityType) {
        return lotteryLogMapper.getTodayLotteryLog(tvId, activityType);
    }
}
