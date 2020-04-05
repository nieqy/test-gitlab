package com.funshion.activity.jinli.rsp;

import java.util.List;

import com.funshion.activity.jinli.entity.JinliEntryData;

public class JinliActivityInfoRsp {
	private List<JinliEntryData> rows;
	private String bgImg;
	private String bgColor;
	private Integer remainDrawNum;
	private Long rank;
	private String newPlayTime;
	private String totalPlayTime;
	private String phone;
	private String activityTime;
	private String activityTip;
	private String activityRule;
	private String activityPrize;
	private Integer playDays;

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public Integer getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(Integer remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	public List<JinliEntryData> getRows() {
		return rows;
	}

	public void setRows(List<JinliEntryData> rows) {
		this.rows = rows;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getNewPlayTime() {
		return newPlayTime;
	}

	public void setNewPlayTime(String newPlayTime) {
		this.newPlayTime = newPlayTime;
	}

	public String getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(String totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getActivityTip() {
		return activityTip;
	}

	public void setActivityTip(String activityTip) {
		this.activityTip = activityTip;
	}

	public String getActivityRule() {
		return activityRule;
	}

	public void setActivityRule(String activityRule) {
		this.activityRule = activityRule;
	}

	public String getActivityPrize() {
		return activityPrize;
	}

	public void setActivityPrize(String activityPrize) {
		this.activityPrize = activityPrize;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public Integer getPlayDays() {
		return playDays;
	}

	public void setPlayDays(Integer playDays) {
		this.playDays = playDays;
	}

}
