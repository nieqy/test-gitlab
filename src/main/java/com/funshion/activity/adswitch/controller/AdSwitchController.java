package com.funshion.activity.adswitch.controller;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.job.LoadDataConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/funtv/ad/switch")
public class AdSwitchController {

	private static final String KEY = "61686acf64a6d529";

	@RequestMapping("/v1")
	public Result<?> firstPage(String ctime, String sign, String mac, Integer activityType) {
		if (StringUtils.isBlank(mac) || StringUtils.isBlank(ctime) || activityType == null) {
			return Result.HTTP_PARAMS_NOT_ENOUGH;
		}
		if (!sign.equals(MD5Utils.getMD5String(activityType + ctime + mac + KEY))) {
			return Result.HTTP_RESPONSE_UNAUTHORIZED;
		}
		Map<String, String> datas = LoadDataConfig.getFuntvActivityData(activityType);
		//默认返回风行 0， 耐看为1，其它待加。。。。
		for (String s : datas.keySet()) {
			if (StringUtils.isBlank(s)) {
				continue;
			}
			String data = datas.get(s);
			if ("1".equals(data)) {
				return Result.getSuccessResult(AdType.getCodeByName(s));
			}
			String[] macRegions = data.split(",");
			for (String macRegion : macRegions) {
				String[] macs = macRegion.split("-");
				if (macs.length != 2) {
					continue;
				}
				if (macs[0].trim().toUpperCase().compareTo(mac.toUpperCase()) <= 0 && macs[1].trim().toUpperCase().compareTo(mac.toUpperCase()) >= 0) {
                    return Result.getSuccessResult(AdType.getCodeByName(s));
				}
			}
		}
		return Result.getSuccessResult(AdType.DEFAULT.getCode());
	}


}
