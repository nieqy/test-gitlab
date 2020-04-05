package com.funshion.activity.jinli.web;

import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.req.JinliAcceptPrizeReq;
import com.funshion.activity.jinli.req.JinliDrawReq;
import com.funshion.activity.jinli.service.JinliCopyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/jinli")
public class JinliController {

    private static final Logger logger = LoggerFactory.getLogger(JinliController.class);

    @Autowired
    private JinliCopyService jinliCopyService;

    @RequestMapping("/activityInfo")
    public Result<?> getActivityInfo(String ctime, String sign, Integer tvId, String mac) {
        if (tvId == null || StringUtils.isEmpty(ctime) || StringUtils.isEmpty(mac) || StringUtils.isEmpty(sign)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(tvId + mac + ctime + ActivityConstants.SignKey.JINLI_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return jinliCopyService.getActivityInfo(tvId, mac);
        } catch (Exception e) {
            logger.error("getActivityInfo got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping("/lottery")
    public Result<?> lottery(JinliDrawReq req) {
        if (req.getTvId() == null || StringUtils.isEmpty(req.getVersion()) || StringUtils.isEmpty(req.getMac())
                || StringUtils.isEmpty(req.getCtime()) || StringUtils.isEmpty(req.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        // 数字签名校验
        if (!(MD5Utils
                .getMD5String(req.getTvId() + req.getMac() + req.getCtime() + ActivityConstants.SignKey.JINLI_SIGN))
                .equals(req.getSign().toLowerCase())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return jinliCopyService.lottery(req);
        } catch (Exception e) {
            logger.error("lottery got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }


    @RequestMapping("/getMyPrizes")
    public Result<?> getMyPrizes(String ctime, String sign, Integer tvId, String mac) {
        if (tvId == null || StringUtils.isEmpty(ctime) || StringUtils.isEmpty(mac) || StringUtils.isEmpty(sign)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(tvId + mac + ctime + ActivityConstants.SignKey.JINLI_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return jinliCopyService.getMyPrizes(tvId);
        } catch (Exception e) {
            logger.error("getMyPrizes got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping(value = "/acceptPrize")
    public Result<?> acceptPrice(JinliAcceptPrizeReq req) {
        if (req.getTvId() == null || StringUtils.isEmpty(req.getCtime()) || StringUtils.isEmpty(req.getMac())
                || StringUtils.isEmpty(req.getSign()) || req.getWinId() == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!req.getSign()
                .equals(MD5Utils.getMD5String(req.getTvId() + req.getMac() + req.getWinId() + req.getCtime()
                        + ActivityConstants.SignKey.JINLI_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            return jinliCopyService.acceptPrice(req);
        } catch (Exception e) {
            logger.error("acceptPrice got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping("/playtime/upload")
    public Result<?> uploadPlaytime(HttpServletRequest request, @RequestBody String body) {
        if (!"ecc3fed47446c980".equals(request.getHeader("auth"))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            jinliCopyService.uploadPlaytime(body);
        } catch (Exception e) {
            logger.error("uploadPlaytime got exception! ", e);
            return Result.SYSTEM_ERROR;
        }

        return Result.getSuccessResult();

    }
}
