package com.funshion.activity.moviesummer.entity;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class MovieSummerAccountQuizAnswerListInfo {

	private Integer accountId;

	private Integer quizId;

	private String startTime;

	private String endTime;

	private Integer allCount;

	private Integer correctCount;

	private Integer prizeType;//0未开奖，1未参与，2未中奖，3幸运奖，4二等奖，5一等奖

	private Integer isAccept;//0未领奖，1已领奖

	private Integer status;//0没开始，1正在进行，2已结束

	public Integer getQuizId() {
		return quizId;
	}

	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
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

	public Integer getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Integer isAccept) {
		this.isAccept = isAccept;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
