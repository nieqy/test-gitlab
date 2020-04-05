package com.funshion.activity.dataconfig.controller;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.job.LoadDataConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/funtv/data/config")
public class DataConfigController {

	private static final String KEY = "61686acf64a6d529";

	@RequestMapping("/all/v1")
	public Result<?> config(String ctime, String sign, Integer activityType) {
		if (StringUtils.isBlank(ctime) || activityType == null) {
			return Result.HTTP_PARAMS_NOT_ENOUGH;
		}
		if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + KEY))) {
			return Result.HTTP_RESPONSE_UNAUTHORIZED;
		}
		Map<String, String> data = LoadDataConfig.getFuntvActivityData(activityType);
		return Result.getSuccessResult(data);
	}

	@RequestMapping("/v1")
	public Result<?> config(String ctime, String sign, Integer activityType, String key) {
		if (StringUtils.isBlank(ctime) || activityType == null || StringUtils.isBlank(key)) {
			return Result.HTTP_PARAMS_NOT_ENOUGH;
		}
		if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + key + KEY))) {
			return Result.HTTP_RESPONSE_UNAUTHORIZED;
		}
		String data = LoadDataConfig.getFuntvActivityData(activityType, key, "");
		return Result.getSuccessResult(data);
	}

}
