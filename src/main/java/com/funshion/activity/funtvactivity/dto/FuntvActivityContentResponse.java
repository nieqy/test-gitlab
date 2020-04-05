package com.funshion.activity.funtvactivity.dto;

import com.funshion.activity.funtvactivity.entity.FuntvActivityContentConfigInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityContentDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityContentResponse {

	private FuntvActivityContentConfigInfo config;

	private String startTime;

	private String endTime;

	private int isAnswered;

	private int needPhone;

	private List<FuntvActivityContentDetailInfo> contentDetail = new ArrayList<>();

	public FuntvActivityContentConfigInfo getConfig() {
		return config;
	}

	public void setConfig(FuntvActivityContentConfigInfo config) {
		this.config = config;
	}

	public List<FuntvActivityContentDetailInfo> getContentDetail() {
		return contentDetail;
	}

	public void setContentDetail(List<FuntvActivityContentDetailInfo> contentDetail) {
		this.contentDetail = contentDetail;
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

	public int getIsAnswered() {
		return isAnswered;
	}

	public void setIsAnswered(int isAnswered) {
		this.isAnswered = isAnswered;
	}

	public int getNeedPhone() {
		return needPhone;
	}

	public void setNeedPhone(int needPhone) {
		this.needPhone = needPhone;
	}
}
