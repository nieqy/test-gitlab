/*
package com.funshion.activity.job;

import com.funshion.activity.funtvactivity.entity.ActivityMovieFirstPageConfigInfo;
import com.funshion.activity.funtvactivity.mapper.MovieFirstPageConfigMapper;
import com.funshion.common.utils.ListBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

*/
/**
 * Created by zhangfei on 2018/8/10/010.
 *//*

@Component
public class LoadMovieFirstPageConfigData {

	private Logger logger = LoggerFactory.getLogger(LoadMovieFirstPageConfigData.class);

	@Autowired
	private FuntvActivityFirstPageConfigMapper movieFirstPageConfigMapper;

	private static Map<Integer, Map<String, Object>> MOVIE_FIRST_PAGE_CONFIG_DATA = new ConcurrentHashMap<>();

	public static Map<String, Object> getMovieFirstPageConfigData(Integer activityType){
		if(activityType == null || !MOVIE_FIRST_PAGE_CONFIG_DATA.containsKey(activityType)){
			return new HashMap<>();
		}
		return MOVIE_FIRST_PAGE_CONFIG_DATA.get(activityType);
	}

	@PostConstruct
	public void loadDataConfig() {
		this.load();
	}

	@Scheduled(cron = "0 0/10 * * * ?")
	public void doExecute() {
		this.load();
	}

	private void load() {
		logger.info("begin to load movie first page config data.");
		try {
			List<ActivityMovieFirstPageConfigInfo> datas = movieFirstPageConfigMapper.findAllMovieFirstPageConfig();
			Map<Integer, List<ActivityMovieFirstPageConfigInfo>> allDatas = new HashMap<>();
			Map<Integer, Map<String, Object>> map = new ConcurrentHashMap<>();
			for (ActivityMovieFirstPageConfigInfo data : datas) {
				if (allDatas.containsKey(data.getActivityType())) {
					allDatas.get(data.getActivityType()).add(data);
				} else {
					List<ActivityMovieFirstPageConfigInfo> list = ListBuilder.<ActivityMovieFirstPageConfigInfo>getBuilder().add(data).build();
					allDatas.put(data.getActivityType(), list);
				}
			}
			for (Integer key : allDatas.keySet()) {
				List<ActivityMovieFirstPageConfigInfo> configs = allDatas.get(key);
				Map<String, Object> result = new HashMap<>();
				this.process(configs, configs.get(0), result);
				map.put(key, result);
			}
			MOVIE_FIRST_PAGE_CONFIG_DATA = map;
		} catch (Exception e) {
			logger.error("load movie first page config data error: {}", e);
		}
	}

	private void process(List<ActivityMovieFirstPageConfigInfo> configs, ActivityMovieFirstPageConfigInfo config, Object result) {
		Integer parentId = config.getId();
		List<ActivityMovieFirstPageConfigInfo> children = this.getChildren(configs, parentId);
		if (children.size() == 0) {
			return;
		}
		for (ActivityMovieFirstPageConfigInfo child : children) {
			Object obj = null;
			if (result instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) result;
				if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.STRING.getKey())) {
					map.put(child.getDataKey(), child.getDataValue());
				} else if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.MAP.getKey())) {
					obj = new HashMap<String, Object>();
					map.put(child.getDataKey(), obj);
				} else if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.ARRAY.getKey())) {
					obj = new ArrayList<>();
					map.put(child.getDataKey(), obj);
				}
			} else if (result instanceof List) {
				List<Object> list = (List<Object>) result;
				if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.STRING.getKey())) {
					list.add(child.getDataValue());
				} else if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.MAP.getKey())) {
					obj = new HashMap<String, Object>();
					list.add(obj);
				} else if (child.getDataValueType().equals(ActivityMovieFirstPageConfigInfo.DataValueType.ARRAY.getKey())) {
					obj = new ArrayList<>();
					list.add(obj);
				}
			}
			this.process(configs, child, obj);
		}
	}

	private List<ActivityMovieFirstPageConfigInfo> getChildren(List<ActivityMovieFirstPageConfigInfo> configs, Integer parentId) {
		List<ActivityMovieFirstPageConfigInfo> list = new ArrayList<>();
		for (ActivityMovieFirstPageConfigInfo config : configs) {
			if (config.getParentId().equals(parentId)) {
				list.add(config);
			}
		}
		return list;
	}

}
*/
