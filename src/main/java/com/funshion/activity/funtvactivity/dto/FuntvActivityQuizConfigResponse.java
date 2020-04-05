package com.funshion.activity.funtvactivity.dto;

import com.funshion.activity.funtvactivity.entity.*;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityQuizConfigResponse {

	private String activityName;

	private int activityType;

	private String bgColor;

	private String cnzzLogId;

	private String headerImg;

	private String footerImg;

	private String submitBtnImg;

	private FuntvActivityQuizHeaderConfigInfo headerConfig;

	private FuntvActivityQuizQuestionConfigInfo questionConfig;

	private FuntvActivityQuizDialogConfigInfo dialogConfig;

	private FuntvActivityQuizRulePageInfo rulePage;

	private FuntvActivityQuizAnswerPageInfo answerPage;

	private FuntvActivityQuizPrizePageInfo prizePage;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getCnzzLogId() {
		return cnzzLogId;
	}

	public void setCnzzLogId(String cnzzLogId) {
		this.cnzzLogId = cnzzLogId;
	}

	public String getHeaderImg() {
		return headerImg;
	}

	public void setHeaderImg(String headerImg) {
		this.headerImg = headerImg;
	}

	public String getFooterImg() {
		return footerImg;
	}

	public void setFooterImg(String footerImg) {
		this.footerImg = footerImg;
	}

	public String getSubmitBtnImg() {
		return submitBtnImg;
	}

	public void setSubmitBtnImg(String submitBtnImg) {
		this.submitBtnImg = submitBtnImg;
	}

	public FuntvActivityQuizHeaderConfigInfo getHeaderConfig() {
		return headerConfig;
	}

	public void setHeaderConfig(FuntvActivityQuizHeaderConfigInfo headerConfig) {
		this.headerConfig = headerConfig;
	}

	public FuntvActivityQuizQuestionConfigInfo getQuestionConfig() {
		return questionConfig;
	}

	public void setQuestionConfig(FuntvActivityQuizQuestionConfigInfo questionConfig) {
		this.questionConfig = questionConfig;
	}

	public FuntvActivityQuizDialogConfigInfo getDialogConfig() {
		return dialogConfig;
	}

	public void setDialogConfig(FuntvActivityQuizDialogConfigInfo dialogConfig) {
		this.dialogConfig = dialogConfig;
	}

	public FuntvActivityQuizRulePageInfo getRulePage() {
		return rulePage;
	}

	public void setRulePage(FuntvActivityQuizRulePageInfo rulePage) {
		this.rulePage = rulePage;
	}

	public FuntvActivityQuizAnswerPageInfo getAnswerPage() {
		return answerPage;
	}

	public void setAnswerPage(FuntvActivityQuizAnswerPageInfo answerPage) {
		this.answerPage = answerPage;
	}

	public FuntvActivityQuizPrizePageInfo getPrizePage() {
		return prizePage;
	}

	public void setPrizePage(FuntvActivityQuizPrizePageInfo prizePage) {
		this.prizePage = prizePage;
	}

}
