package com.funshion.activity.funtvactivity.controller;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.funtvactivity.service.FuntvActivityFirstPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/funtv/autopage")
public class FuntvActivityFirstPageConfigController {

    private static Logger logger = LoggerFactory.getLogger(FuntvActivityFirstPageConfigController.class);

    @Autowired
    private FuntvActivityFirstPageService funtvActivityFirstPageService;

    @RequestMapping("/config")
    public Result<?> firstPage(Integer activityType) {
        logger.info("request info - [activityType:{}] ", activityType);
        if (activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        Result<?> res = funtvActivityFirstPageService.funtvActivityFirstPage(activityType);
        logger.info("request info - [activityType:{}] , response info - {}", activityType, JSON.toJSONString(res));
        return res;
    }

}
