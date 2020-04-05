package com.funshion.activity.funtvactivity.entity;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityContentAnswerListInfo {

	private Integer winId;

	private Integer accountId;

	private Integer contentId;

	private String startTime;

	private String endTime;

	private String contentName;

	private String prizeItemName;

	private int isVirtualPrize;

	private String virtualPrizeDesc;

	private Integer allCount;

	private Integer correctCount;

	private Integer prizeType;//0未开奖，1未参与，2未中奖，3幸运奖，4二等奖，5一等奖

	private int isAccept;//0未领奖，1已领奖

	private Integer status;//0没开始，1正在进行，2已结束

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
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

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(Integer prizeType) {
		this.prizeType = prizeType;
	}

	public int getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(int isAccept) {
		this.isAccept = isAccept;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getPrizeItemName() {
		return prizeItemName;
	}

	public void setPrizeItemName(String prizeItemName) {
		this.prizeItemName = prizeItemName;
	}

	public int getIsVirtualPrize() {
		return isVirtualPrize;
	}

	public void setIsVirtualPrize(int isVirtualPrize) {
		this.isVirtualPrize = isVirtualPrize;
	}

	public String getVirtualPrizeDesc() {
		return virtualPrizeDesc;
	}

	public void setVirtualPrizeDesc(String virtualPrizeDesc) {
		this.virtualPrizeDesc = virtualPrizeDesc;
	}

	public Integer getWinId() {
		return winId;
	}

	public void setWinId(Integer winId) {
		this.winId = winId;
	}
}
