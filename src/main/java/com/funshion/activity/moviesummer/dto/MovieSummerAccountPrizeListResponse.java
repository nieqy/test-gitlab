package com.funshion.activity.moviesummer.dto;

import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
public class MovieSummerAccountPrizeListResponse {

	private String subjectName;

	private String startTime;

	private String endTime;

	private int type;//种类

	private List<String> phone;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
