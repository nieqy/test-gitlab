package com.funshion.activity.jinli.entity;

public class JinliUserInfoInRedis {

	private Integer playDays = 0;
	
	private Integer newPlayTime;

	private Double totalPlayTime;
	
	private Integer remainDrawNum;

	private String updateDate;

	public Integer getNewPlayTime() {
		return newPlayTime;
	}

	public void setNewPlayTime(Integer newPlayTime) {
		this.newPlayTime = newPlayTime;
	}

	public Double getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(Double totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}

	public Integer getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(Integer remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getPlayDays() {
		return playDays;
	}

	public void setPlayDays(Integer playDays) {
		this.playDays = playDays;
	}

}
