package com.funshion.activity.common.web;

import com.funshion.activity.common.bean.AddressInfo;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.service.AddressServiceFactory;
import com.funshion.activity.common.service.IAddressService;
import com.funshion.activity.common.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @RequestMapping("/upload")
    public Result<?> uploadAddress(AddressInfo info) {
        if (info.getTvId() == null || StringUtils.isEmpty(info.getCtime()) || StringUtils.isEmpty(info.getMac())
                || StringUtils.isEmpty(info.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!info.getSign().equals(
                MD5Utils.getMD5String(
                        info.getTvId() + info.getMac() + info.getSourceId() + info.getCtime()
                                + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            IAddressService addressService = AddressServiceFactory.getFavoritesServiceByType(info.getActivityType());
            if (addressService == null) {
                return Result.getFailureResult("404", "不支持的活动类型");
            }
            return addressService.saveAddress(info);
        } catch (Exception e) {
            logger.error("uploadPlaytime got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }

    @RequestMapping("/status")
    public Result<?> uploadAddressStatus(AddressInfo info) {
        if (info.getTvId() == null || StringUtils.isEmpty(info.getCtime()) || StringUtils.isEmpty(info.getMac())
                || StringUtils.isEmpty(info.getSign())) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!info.getSign().equals(
                MD5Utils.getMD5String(
                        info.getTvId() + info.getMac() + info.getSourceId() + info.getCtime()
                                + ActivityConstants.SignKey.JINLI_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        try {
            IAddressService addressService = AddressServiceFactory.getFavoritesServiceByType(info.getActivityType());
            if (addressService == null) {
                return Result.getFailureResult("404", "不支持的活动类型");
            }
            return addressService.getAddressStatus(info);
        } catch (Exception e) {
            logger.error("uploadPlaytime got exception! ", e);
            return Result.SYSTEM_ERROR;
        }
    }
}
