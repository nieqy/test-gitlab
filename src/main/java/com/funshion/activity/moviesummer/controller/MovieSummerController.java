package com.funshion.activity.moviesummer.controller;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.moviesummer.dto.MovieSummerSubmitRequest;
import com.funshion.activity.moviesummer.service.MovieSummerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/movie/summer")
public class MovieSummerController {

    private static final String KEY = "07743ca7caaa7862";

    @Autowired
    private MovieSummerService movieSummerService;

    @RequestMapping("/firstPage")
    public Result<?> quizList(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.movieSummerfirstPage();
    }

    @RequestMapping("/detail")
    public Result<?> quizDetail(String ctime, String sign, Integer accountId, Integer id) {
        if (accountId == null || StringUtils.isBlank(ctime) || id == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.movieSummerDetails(accountId, id);
    }

    @RequestMapping("/detail/submit")
    public Result<?> detailSubmit(MovieSummerSubmitRequest request) {
        if (request.getAccountId() == null || StringUtils.isBlank(request.getCtime()) || request.getQuizId() == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!request.getSign().equals(MD5Utils.getMD5String(request.getAccountId() + request.getCtime() + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.movieSummerDetailSubmit(request);
    }

    @RequestMapping("/account/phone/submit")
    public Result<?> accountPhone(String ctime, String sign, Integer accountId, String phone) {
        if (accountId == null || StringUtils.isBlank(ctime) || StringUtils.isBlank(phone)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.submitAccountPhone(accountId, phone);
    }

    @RequestMapping("/account/address/info")
    public Result<?> getAccountAddress(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.getAccountAddressInfo(accountId);
    }

    @RequestMapping("/account/address/submit")
    public Result<?> accountAddress(String ctime, String sign, Integer accountId, String receivingName, String receivingAddress, String receivingPhone) {
        if (accountId == null || StringUtils.isBlank(ctime) || StringUtils.isBlank(receivingName) || StringUtils.isBlank(receivingAddress)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.submitAccountAddress(accountId, receivingName, receivingAddress, receivingPhone);
    }

    @RequestMapping("/account/answer/list")
    public Result<?> getAccountQuizAnswerList(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.getAccountQuizAnswerList(accountId);
    }

    @RequestMapping("/account/accept/prize")
    public Result<?> acceptPrize(String ctime, String sign, Integer accountId, Integer quizId, Integer prizeType) {
        if (accountId == null || StringUtils.isBlank(ctime) || prizeType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.acceptPrize(accountId, quizId, prizeType);
    }

    @RequestMapping("/account/prize/list")
    public Result<?> prizeList(String ctime, String sign, Integer accountId) {
        if (accountId == null || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(accountId + ctime + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return movieSummerService.prizeList();
    }

}
