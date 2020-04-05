package com.funshion.activity.annualreport.controller;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.annualreport.service.AnnualReportService;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/annual/report")
public class AnnualReportController {

    private static Logger logger = LoggerFactory.getLogger(AnnualReportController.class);

    private static final String KEY = "883664456b1057bd";

    @Autowired
    private AnnualReportService annualReportService;

    @RequestMapping("/index")
    public Result<?> firstPage(String ctime, String sign, Integer tvId, String mac) {
        logger.info("request info - [ctime:{},tvId:{},sign:{},mac:{}] ", ctime, tvId, sign, mac);
        if (StringUtils.isBlank(ctime) || StringUtils.isBlank(mac)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        if (!sign.equals(MD5Utils.getMD5String(ctime + mac + KEY))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        Result<?> res = annualReportService.annualReport(mac);
        logger.info("request info - [ctime:{},tvId:{},sign:{},mac:{}] , response info - {}", ctime, tvId, sign, mac, JSON.toJSONString(res));
        return res;
    }

}
