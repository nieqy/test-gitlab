package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityInfo {

	private Integer id;

	private int activityType; //买电视赢宝马为1

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	private String activityMediaType;

	private Integer activityMediaId;

	private String activityMediaPicture;

	private String activityMediaName;

	private int showSeq;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActivityMediaType() {
		return activityMediaType;
	}

	public void setActivityMediaType(String activityMediaType) {
		this.activityMediaType = activityMediaType;
	}

	public Integer getActivityMediaId() {
		return activityMediaId;
	}

	public void setActivityMediaId(Integer activityMediaId) {
		this.activityMediaId = activityMediaId;
	}

	public String getActivityMediaPicture() {
		return activityMediaPicture;
	}

	public void setActivityMediaPicture(String activityMediaPicture) {
		this.activityMediaPicture = activityMediaPicture;
	}

	public String getActivityMediaName() {
		return activityMediaName;
	}

	public void setActivityMediaName(String activityMediaName) {
		this.activityMediaName = activityMediaName;
	}

	public int getShowSeq() {
		return showSeq;
	}

	public void setShowSeq(int showSeq) {
		this.showSeq = showSeq;
	}
}
