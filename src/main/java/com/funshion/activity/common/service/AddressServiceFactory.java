package com.funshion.activity.common.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.funshion.activity.common.constants.ActivityConstants;

@Service
public class AddressServiceFactory {

	@Autowired
	private ApplicationContext applicationContext;

	private final static Map<String, IAddressService> ADDRESS_SERVICE_MAP = new ConcurrentHashMap<String, IAddressService>();

	@PostConstruct
	public void init() {
		Map<String, IAddressService> addressServices = applicationContext
				.getBeansOfType(IAddressService.class);
		for (Entry<String, IAddressService> addressServiceEntry : addressServices.entrySet()) {
			ADDRESS_SERVICE_MAP.put(addressServiceEntry.getValue().getActivityType(),
					addressServiceEntry.getValue());
		}
	}

	public static IAddressService getFavoritesServiceByType(String activityType) {
		if (ADDRESS_SERVICE_MAP.get(activityType) == null) {
			return ADDRESS_SERVICE_MAP.get(ActivityConstants.ActivityType.DEFAULT);
		}
		return ADDRESS_SERVICE_MAP.get(activityType);
	}

}
