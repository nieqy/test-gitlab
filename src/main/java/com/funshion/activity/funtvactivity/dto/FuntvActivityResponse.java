package com.funshion.activity.funtvactivity.dto;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityResponse {

	private String activityMediaType;

	private Integer activityMediaId;

	private String activityMediaPicture;

	private String activityMediaName;

	private int showSeq;

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
