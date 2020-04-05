package com.funshion.activity.funtvactivity.dto;

import java.util.List;

/**
 * Created by zhangfei on 2019/05/24/001.
 */
public class FuntvActivityPrizeListResponse {

	private String contentName;

	private String startTime;

	private String endTime;

	private int type;//种类

	private List<String> phone;

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
