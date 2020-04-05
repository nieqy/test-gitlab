package com.funshion.activity.common.web;

import com.funshion.activity.common.bean.AcceptPrizeReq;
import com.funshion.activity.common.bean.AddPrizeReq;
import com.funshion.activity.common.bean.LotteryReq;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.service.LotteryService;
import com.funshion.activity.common.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);

    @Autowired
    private LotteryService lotteryService;


    @RequestMapping("/draw")
    public Result<?> draw(LotteryReq req) {
        if (req.getTvId() == null || req.getActivityType() == null || StringUtils.isEmpty(req.getVersion())
                || StringUtils.isEmpty(req.getMac()) || StringUtils.isEmpty(req.getCtime())
                || StringUtils.isEmpty(req.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        // 数字签名校验
        if (!(MD5Utils
                .getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                        + ActivityConstants.SignKey.ORANGE_SIGN)).equals(req.getSign().toLowerCase())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return lotteryService.doLottery(req);
        } catch (Exception e) {
            logger.error("lottery got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }


    @RequestMapping("/drawCommon")
    public Result<?> drawCommon(LotteryReq req) {
        if (req.getTvId() == null || req.getActivityType() == null || StringUtils.isEmpty(req.getVersion())
                || StringUtils.isEmpty(req.getCtime()) || StringUtils.isEmpty(req.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }

        // 数字签名校验
        if (!(MD5Utils
                .getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                        + ActivityConstants.SignKey.ORANGE_SIGN)).equals(req.getSign().toLowerCase())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return lotteryService.doCommonLottery(req);
        } catch (Exception e) {
            logger.error("lottery got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping("/getMyPrizes")
    public Result<?> getMyPrizes(String ctime, String sign, Integer tvId, String mac, Integer activityType) {
        if (tvId == null || activityType == null || StringUtils.isEmpty(ctime) || StringUtils.isEmpty(mac)
                || StringUtils.isEmpty(sign)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(
                MD5Utils.getMD5String(tvId + "" + activityType + ctime + ActivityConstants.SignKey.ORANGE_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return lotteryService.getMyPrizes(tvId, activityType);
        } catch (Exception e) {
            logger.error("getMyPrizes got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping(value = "/acceptPrize")
    public Result<?> acceptPrice(AcceptPrizeReq req) {
        if (req.getTvId() == null || req.getActivityType() == null || StringUtils.isEmpty(req.getCtime())
                || StringUtils.isEmpty(req.getMac()) || StringUtils.isEmpty(req.getSign()) || req.getWinId() == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }

        // 兼容锦鲤活动签名（h5代码共用了）
        if (!req.getSign().equals(
                MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getWinId() + req.getCtime()
                        + ActivityConstants.SignKey.ORANGE_SIGN)) && !req.getSign().equals(
                MD5Utils.getMD5String(req.getTvId() + req.getMac() + req.getWinId() + req.getCtime()
                        + ActivityConstants.SignKey.JINLI_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return lotteryService.acceptPrice(req);
        } catch (Exception e) {
            logger.error("acceptPrice got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }


    @RequestMapping(value = "/addPrize")
    public Result<?> addPrize(AddPrizeReq req) {
        if (req.getTvId() == null || req.getActivityType() == null || StringUtils.isEmpty(req.getCtime())
                || StringUtils.isEmpty(req.getMac()) || StringUtils.isEmpty(req.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }

        if (!req.getSign().equals(
                MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                        + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return lotteryService.addPrize(req);
        } catch (Exception e) {
            logger.error("addPrize got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }
}
