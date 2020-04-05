package com.funshion.activity.funtvactivity.controller;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccontInfo;
import com.funshion.activity.funtvactivity.service.FuntvActivityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/funtv/activity")
public class FuntvActivityController {

    private static Logger logger = LoggerFactory.getLogger(FuntvActivityController.class);

    private static final String KEY = "61686acf64a6d529";

    @Autowired
    private FuntvActivityService funtvActivityService;

    @RequestMapping("/firstPage")
    public Result<?> firstPage(String ctime, String sign, Integer tvId, Integer activityType) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{}] ", activityType, ctime, tvId, sign);
        if (tvId == null || StringUtils.isBlank(ctime) || activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.funtvActivityFirstPage(activityType);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{}] , response info - {}", activityType, ctime, tvId, sign, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/qrcode")
    public Result<?> qrcode(String ctime, String sign, Integer tvId, Integer activityType, String mac) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] ", activityType, ctime, tvId, sign, mac);
        if (StringUtils.isBlank(ctime) || activityType == null || tvId == null || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.qrcode(mac, tvId, activityType);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", activityType, ctime, tvId, sign, mac, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/check/role")
    public Result<?> checkUserInfo(String key, Integer activityType) {
        logger.info("request info - [key:{},activityType:{}] ", key, activityType);
        if (StringUtils.isBlank(key) || activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        Result<?> res = funtvActivityService.checkUserInfo(key);
        logger.info("request info - [key:{},activityType:{}] , response info - {}", key, activityType, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/verifyCode")
    public Result<?> verifyCode(String phone, String ctime, Integer tvId, String sign, Integer activityType) throws Exception {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},phone:{}] ", activityType, ctime, tvId, sign, phone);
        if (StringUtils.isBlank(ctime) || tvId == null || StringUtils.isBlank(phone)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.sendMessage(phone);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},phone:{}] , response info - {}", activityType, ctime, tvId, sign, phone, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/account/info")
    public Result<?> accountInfo(String verifyCode, String ctime, String sign, Integer tvId, FuntvActivityAccontInfo info) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},phone:{},verifyCode:{}] ", info.getActivityType(), ctime, tvId, sign, info.getPhone(), verifyCode);
        if (StringUtils.isBlank(ctime) || tvId == null || StringUtils.isBlank(info.getMac()) || StringUtils.isBlank(info.getPhone())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(info.getActivityType() + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.accountInfo(verifyCode, tvId, info);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},phone:{},verifyCode:{}] , response info - {}", info.getActivityType(), ctime, tvId, sign, info.getPhone(), verifyCode, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/config")
    public Result<?> config(String ctime, String sign, Integer tvId, Integer activityType) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{}] ", activityType, ctime, tvId, sign);
        if (StringUtils.isBlank(ctime) || activityType == null || tvId == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.config(activityType);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{}] , response info - {}", activityType, ctime, tvId, sign, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/prize")
    public Result<?> prize(String ctime, String sign, Integer tvId, String mac, Integer activityType, String prizeFlag) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] ", activityType, ctime, tvId, sign, mac);
        if (StringUtils.isBlank(ctime) || activityType == null || tvId == null || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.getPrizeInfo(activityType, prizeFlag);
        if ("200".equals(res.getRetCode())) {
            logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response success.", activityType, ctime, tvId, sign, mac);
        } else {
            logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", activityType, ctime, tvId, sign, mac, JSON.toJSONString(res));
        }
        return res;
    }

    @RequestMapping("/user/prize")
    public Result<?> userPrize(String ctime, String sign, Integer tvId, String mac, Integer activityType) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] ", activityType, ctime, tvId, sign, mac);
        if (StringUtils.isBlank(ctime) || activityType == null || tvId == null || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.getUserPrizeInfo(tvId, mac, activityType);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", activityType, ctime, tvId, sign, mac, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/user/accept/prize")
    public Result<?> userAcceptPrize(String ctime, String sign, Integer tvId, String mac, Integer activityType, String prizeFlag) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{},prizeFlag:{}] ", activityType, ctime, tvId, sign, mac, prizeFlag);
        if (StringUtils.isBlank(ctime) || activityType == null || tvId == null || StringUtils.isBlank(mac) || StringUtils.isBlank(prizeFlag)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.acceptPrize(tvId, mac, activityType, prizeFlag);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{},prizeFlag:{}] , response info - {}", activityType, ctime, tvId, sign, mac, prizeFlag, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/user/address/info")
    public Result<?> userAddressInfo(String ctime, String sign, Integer tvId, FuntvActivityAccontInfo info) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] ", info.getActivityType(), ctime, tvId, sign, info.getMac());
        if (StringUtils.isBlank(ctime) || tvId == null || StringUtils.isBlank(info.getMac())
                || StringUtils.isBlank(info.getReceivingAddress()) || StringUtils.isBlank(info.getReceivingName()) || StringUtils.isBlank(info.getReceivingPhone())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(info.getActivityType() + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.accountInfo(tvId, info);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", info.getActivityType(), ctime, tvId, sign, info.getMac(), JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/get/user/address/info")
    public Result<?> getUserAddressInfo(String ctime, String sign, Integer tvId, String mac, Integer activityType) {
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] ", activityType, ctime, tvId, sign, mac);
        if (StringUtils.isBlank(ctime) || tvId == null || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + tvId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.findAccountInfo(tvId, mac, activityType);
        logger.info("request info - [activityType:{},ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", activityType, ctime, tvId, sign, mac, JSON.toJSONString(res));
        return res;
    }

}
