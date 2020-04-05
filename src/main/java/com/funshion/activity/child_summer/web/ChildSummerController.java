package com.funshion.activity.child_summer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.funshion.activity.child_summer.entity.ChildSummerAccont;
import com.funshion.activity.child_summer.entity.ChildSummerAnswer;
import com.funshion.activity.child_summer.service.ChildSummerService;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/child/summer")
public class ChildSummerController {

    private static final Logger logger = LoggerFactory.getLogger(ChildSummerController.class);

    private static final String KEY = "lkeix83mx7snw7qm";

    @Autowired
    private ChildSummerService childSummerService;

    @RequestMapping("/quizList")
    public Result<?> quizList(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            return childSummerService.getQuizList(accountId);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

    @RequestMapping(value = "/answerUpload")
    public Result<?> answerUpload(String ctime, String sign, Integer accountId, Integer quizId, String answers) {
        if (accountId == null || StringUtils.isEmpty(ctime) || StringUtils.isEmpty(answers)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            List<ChildSummerAnswer> answerList = JSON.parseObject(answers, new TypeReference<List<ChildSummerAnswer>>() {
            }.getType());
            return childSummerService.answerUpload(accountId, quizId, answerList);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

    @RequestMapping("/phoneUpload")
    public Result<?> phoneUpload(String ctime, String sign, Integer accountId, String phone) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            childSummerService.phoneUpload(phone, accountId);
            return Result.getSuccessResult();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

    @RequestMapping("/userAnswers")
    public Result<?> userAnswers(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            return childSummerService.accountAnswerList(accountId);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

    @RequestMapping("/prizeList")
    public Result<?> prizeList(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            return childSummerService.prizeList(accountId);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

    @RequestMapping("/addressUpload")
    public Result<?> addressUpload(String ctime, String sign, String address, String phone, String consignee, Integer accountId) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            ChildSummerAccont account = new ChildSummerAccont();
            account.setAccountId(accountId);
            account.setReceivingAddress(address);
            account.setReceivingName(consignee);
            account.setReceivingPhone(phone);
            childSummerService.addressUpload(account);
            return Result.getSuccessResult();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }


    @RequestMapping("/addressInfo")
    public Result<?> addressInfo(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isEmpty(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        try {
            return Result.getSuccessResult(childSummerService.addressInfo(accountId));
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
            return Result.SYSTEM_ERROR;
        }

    }

}
