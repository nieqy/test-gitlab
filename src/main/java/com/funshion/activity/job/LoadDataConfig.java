package com.funshion.activity.job;

import com.funshion.activity.dataconfig.entity.ActivityDataConfigInfo;
import com.funshion.activity.dataconfig.mapper.ActivityDataConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangfei on 2018/8/10/010.
 */
@Component
public class LoadDataConfig {

	private Logger logger = LoggerFactory.getLogger(LoadDataConfig.class);

	@Autowired
	private ActivityDataConfigMapper activityDataConfigMapper;

	private static Map<String, String> DATA_CONFIG = new ConcurrentHashMap<>();

	@PostConstruct
	public void loadDataConfig() {
		this.load();
	}

	@Scheduled(cron = "0 0/10 * * * ?")
	public void doExecute() {
		this.load();
	}

	private void load() {
		logger.info("begin to load config data.");
		try {
			List<ActivityDataConfigInfo> datas = activityDataConfigMapper.findAllData();
			Map<String, String> temp = new ConcurrentHashMap<>();
			for (ActivityDataConfigInfo data : datas) {
				temp.put(data.getDataKey(), data.getDataValue());
			}
			DATA_CONFIG = temp;
		} catch (Exception e) {
			logger.error("load data error: {}", e);
		}
	}

	public static String getDataValueByKey(String dataKey, String defaultValue) {
		if (StringUtils.isBlank(dataKey)) {
			return defaultValue;
		}
		if (!DATA_CONFIG.containsKey(dataKey)) {
			return defaultValue;
		}
		return DATA_CONFIG.get(dataKey);
	}

	public static String getDataValueByKey(String dataKey) {
		return LoadDataConfig.getDataValueByKey(dataKey, null);
	}

	public static Map<String, String> getFuntvActivityData(Integer activityType) {
		Map<String, String> map = new HashMap<>();
		Set<String> keys = DATA_CONFIG.keySet();
		String prefix = "funtv.activity." + activityType + ".";
		for (String key : keys) {
			if (key.startsWith(prefix) && key.length() > prefix.length()) {
				String k = key.substring(prefix.length());
				map.put(k, DATA_CONFIG.get(key));
			}
		}
		return map;
	}

	public static String getFuntvActivityData(Integer activityType, String key, String defaultValue) {
		String v = getFuntvActivityData(activityType).get(key);
		if (StringUtils.isBlank(v)) {
			return defaultValue;
		}
		return v;
	}
}
