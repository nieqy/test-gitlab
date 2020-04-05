package com.funshion.activity.orange.web;

import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.orange.service.OrangeSignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/orange")
public class OrangeSignController {


    @Autowired
    private OrangeSignService orangeSignService;

    @RequestMapping("/get_sign_info")
    @ResponseBody
    public Result<?> getSignInfo(String account_id, String token, String ctime, String sign) {
        if (StringUtils.isBlank(account_id) || StringUtils.isBlank(sign) || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(account_id + ctime + ActivityConstants.SignKey.ORANGE_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        return orangeSignService.getSignInfo(account_id);
    }


    @RequestMapping("/sign_in")
    @ResponseBody
    public Result<?> signIn(String account_id, Integer sign_id, String token, String ctime, String sign) {
        if (StringUtils.isBlank(account_id)
                || StringUtils.isBlank(sign)
                || sign_id == null
                || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(account_id + sign_id + ctime + ActivityConstants.SignKey.ORANGE_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        return orangeSignService.signIn(account_id, sign_id);
    }

    @RequestMapping("/get_sign_code")
    @ResponseBody
    public Result<?> getSignCode(String mac, String account_id, Integer sign_id, String token, String ctime, String sign) {
        if (StringUtils.isBlank(account_id)
                || StringUtils.isBlank(sign)
                || sign_id == null
                || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(account_id + sign_id + ctime + ActivityConstants.SignKey.ORANGE_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        return orangeSignService.getSignCode(mac, account_id, sign_id);
    }

    @RequestMapping("/use_sign_code")
    @ResponseBody
    public Result<?> useSignCode(String mac, String account_id, Integer sign_id, String code, String token, String ctime, String sign) {
        if (StringUtils.isBlank(account_id)
                || StringUtils.isBlank(sign)
                || StringUtils.isBlank(code)
                || sign_id == null
                || StringUtils.isBlank(ctime)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(account_id + sign_id + code + ctime + ActivityConstants.SignKey.ORANGE_SIGN))) {
            return Result.getFailureResult("401", "补签邀请码不存在");
        }

        return orangeSignService.useSignCode(mac, account_id, sign_id, code);
    }


}
