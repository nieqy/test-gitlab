package com.funshion.activity.funtvactivity.controller;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.funtvactivity.dto.FuntvActivityAnswerSubmitRequest;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccontInfo;
import com.funshion.activity.funtvactivity.service.FuntvActivityContentService;
import com.funshion.activity.funtvactivity.service.FuntvActivityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/funtv/autoquiz")
public class FuntvActivityContentController {

    private static final String KEY = "e8ec2f7ecd214e45";

    private static Logger logger = LoggerFactory.getLogger(FuntvActivityContentController.class);

    @Autowired
    private FuntvActivityContentService funtvActivityContentService;

    @Autowired
    private FuntvActivityService funtvActivityService;

    @RequestMapping("/content")
    public Result<?> quizContent(String ctime, String sign, Integer accountId, Integer activityType, String mac) {
        logger.info("request info - [activityType:{}] ", activityType);
        if (accountId == null || StringUtils.isBlank(ctime) || activityType == null || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + accountId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityContentService.funtvActivityContent(activityType, accountId, mac);
        logger.info("request info - [activityType:{}] , response info - {}", activityType, JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/answer/submit")
    public Result<?> answerSubmit(FuntvActivityAnswerSubmitRequest request) {
        if (request.getAccountId() == null || StringUtils.isBlank(request.getCtime()) || request.getContentId() == null || request.getActivityType() == 0) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!request.getSign().equals(MD5Utils.getMD5String(request.getActivityType() + request.getCtime() + request.getAccountId() + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return funtvActivityContentService.answerSubmit(request);
    }

    @RequestMapping("/account/info")
    public Result<?> accountInfo(String ctime, String sign, Integer accountId, FuntvActivityAccontInfo info) {
        if (StringUtils.isBlank(ctime) || accountId == null || StringUtils.isBlank(info.getMac()) || StringUtils.isBlank(info.getPhone())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(info.getActivityType() + ctime + accountId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = funtvActivityService.accountInfo(null, accountId, info);
        return res;
    }

    @RequestMapping("/account/answer/list")
    public Result<?> getAccountQuizAnswerList(String ctime, String sign, Integer accountId, Integer activityType) {
        if (accountId == null || StringUtils.isBlank(ctime) || activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + accountId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return funtvActivityContentService.getAccountAnswerList(accountId, activityType);
    }

    @RequestMapping("/account/prize/list")
    public Result<?> prizeList(String ctime, String sign, Integer accountId, Integer activityType) {
        if (accountId == null || StringUtils.isBlank(ctime) || activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + accountId + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return funtvActivityContentService.prizeList(activityType);
    }

}
