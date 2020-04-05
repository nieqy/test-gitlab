package com.funshion.activity.funtvactivity.controller;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.funtvactivity.service.FuntvActivityQuizConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/funtv/autoquiz")
public class FuntvActivityQuizConfigController {

    private static Logger logger = LoggerFactory.getLogger(FuntvActivityQuizConfigController.class);

    @Autowired
    private FuntvActivityQuizConfigService funtvActivityQuizConfigService;

    @RequestMapping("/config")
    public Result<?> quizConfig(Integer activityType) {
        logger.info("request info - [activityType:{}] ", activityType);
        if (activityType == null) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
        Result<?> res = funtvActivityQuizConfigService.funtvActivityQuizConfig(activityType);
        logger.info("request info - [activityType:{}] , response info - {}", activityType, JSON.toJSONString(res));
        return res;
    }

}
