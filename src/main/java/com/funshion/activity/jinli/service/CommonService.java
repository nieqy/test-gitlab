/**
 * CommonService.java
 * com.funshion.activity.jinli.service
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年1月7日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.jinli.service;

import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.entity.AccountPriviligeInfo;
import com.funshion.activity.jinli.mapper.AccountPriviligeMapper;
import com.funshion.activity.jinli.req.JinliDrawReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowei
 * @ClassName:CommonService
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2019年1月7日        下午5:18:03
 * @see
 * @since Ver 1.1
 */
@Service
public class CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private AccountPriviligeMapper accountPriviligeMapper;

    @Autowired
    private RedisService redisService;

    @Value("${mercury.host}")
    private String mercuryHost;

    @Autowired
    private RestTemplate restTemplate;

    public Date getVipTime(Integer accountId) {
        AccountPriviligeInfo privilige = findAccountVipPriviligeInfoFromRedis(accountId);
        if (privilige == null) {
            return null;
        } else {
            return privilige.getValidEndTime();
        }
    }


    private AccountPriviligeInfo findAccountVipPriviligeInfoFromRedis(Integer tvId) {
        String redisKey = RedisPrefix.ACCOUNT_PRIVILETE_KEY + "VIP_" + tvId;
        boolean redisWork = true;
        try {
            AccountPriviligeInfo privilege = (AccountPriviligeInfo) redisService.get(redisKey);
            if (privilege != null) {
                return privilege;
            }
        } catch (Exception e) {
            redisWork = false;
        }

        AccountPriviligeInfo privilege = new AccountPriviligeInfo();
        try {
            AccountPriviligeInfo movieVipPrivilege = accountPriviligeMapper.findAccountPriviligeInfo(tvId, 3585);
            AccountPriviligeInfo goldenVipPrivilege = accountPriviligeMapper.findAccountPriviligeInfo(tvId, 3);
            if (movieVipPrivilege != null && goldenVipPrivilege != null) {
                privilege = movieVipPrivilege.getValidEndTime().after(goldenVipPrivilege.getValidEndTime())
                        ? movieVipPrivilege : goldenVipPrivilege;
            } else if (movieVipPrivilege != null && goldenVipPrivilege == null) {
                privilege = movieVipPrivilege;
            } else if (movieVipPrivilege == null && goldenVipPrivilege != null) {
                privilege = goldenVipPrivilege;
            }
        } catch (Exception e) {
        }

        if (privilege != null && redisWork) {
            try {
                redisService.setForTimeCustom(redisKey, privilege, 1, TimeUnit.DAYS);
            } catch (Exception e) {
            }
        }
        return privilege;
    }

    public String lotteryFromMercury(JinliDrawReq req) {
        StringBuilder sb = new StringBuilder();
        sb.append(mercuryHost).append("/api/lottery/drawForJinli").append("?account_id=").append(req.getTvId())
                .append("&version=").append(req.getVersion()).append("&ctime=").append(req.getCtime())
                .append("&lotteryId=").append(req.getLotteryId()).append("&sign=").append(MD5Utils
                .getMD5String(req.getTvId() + "" + req.getLotteryId() + req.getCtime() + "df2eb3e697746331"));

        //请求会员接口  accountId, lotteryId, ctime, sign
        try {
            String acceptResponse = restTemplate.getForObject(sb.toString(), String.class);
            return acceptResponse;
        } catch (Exception e) {
            logger.error("lotteryFromMercury exception ", e);
        }
        return null;
    }
}

