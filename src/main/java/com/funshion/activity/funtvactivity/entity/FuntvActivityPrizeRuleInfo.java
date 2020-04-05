package com.funshion.activity.funtvactivity.entity;

/**
 * Created by zhangfei on 2019/5/22/022.
 */
public class FuntvActivityPrizeRuleInfo {

	private Integer id;

	private int activityType;

	private int isContentPrize;

	private Integer contentId;

	private int prizeType;

	private String prizeCondition;

	private Integer prizeAwardCount;

	private int isVirtualPrize;

	private String actualPrize;

	private String virtualPrizeValue;

	private String virtualPrizeAlert;

	private int status;

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

	public int getIsContentPrize() {
		return isContentPrize;
	}

	public void setIsContentPrize(int isContentPrize) {
		this.isContentPrize = isContentPrize;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public String getPrizeCondition() {
		return prizeCondition;
	}

	public void setPrizeCondition(String prizeCondition) {
		this.prizeCondition = prizeCondition;
	}

	public Integer getPrizeAwardCount() {
		return prizeAwardCount;
	}

	public void setPrizeAwardCount(Integer prizeAwardCount) {
		this.prizeAwardCount = prizeAwardCount;
	}

	public int getIsVirtualPrize() {
		return isVirtualPrize;
	}

	public void setIsVirtualPrize(int isVirtualPrize) {
		this.isVirtualPrize = isVirtualPrize;
	}

	public String getVirtualPrizeValue() {
		return virtualPrizeValue;
	}

	public void setVirtualPrizeValue(String virtualPrizeValue) {
		this.virtualPrizeValue = virtualPrizeValue;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getActualPrize() {
		return actualPrize;
	}

	public void setActualPrize(String actualPrize) {
		this.actualPrize = actualPrize;
	}

	public String getVirtualPrizeAlert() {
		return virtualPrizeAlert;
	}

	public void setVirtualPrizeAlert(String virtualPrizeAlert) {
		this.virtualPrizeAlert = virtualPrizeAlert;
	}
}
