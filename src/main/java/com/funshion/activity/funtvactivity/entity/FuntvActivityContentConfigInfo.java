package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityContentConfigInfo {

	private Integer id;

	private String activityName;

	private int activityType;

	private String contentName;

	private String headerVideoBtnImg;

	private String headerLinkType;

	private String headerLinkVal;

	private String bottomBannerImg1;

	private String bottomLinkType1;

	private String bottomLinkVal1;

	private String bottomBannerImg2;

	private String bottomLinkType2;

	private String bottomLinkVal2;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	private int showSeq;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getHeaderVideoBtnImg() {
		return headerVideoBtnImg;
	}

	public void setHeaderVideoBtnImg(String headerVideoBtnImg) {
		this.headerVideoBtnImg = headerVideoBtnImg;
	}

	public String getHeaderLinkType() {
		return headerLinkType;
	}

	public void setHeaderLinkType(String headerLinkType) {
		this.headerLinkType = headerLinkType;
	}

	public String getHeaderLinkVal() {
		return headerLinkVal;
	}

	public void setHeaderLinkVal(String headerLinkVal) {
		this.headerLinkVal = headerLinkVal;
	}

	public String getBottomBannerImg1() {
		return bottomBannerImg1;
	}

	public void setBottomBannerImg1(String bottomBannerImg1) {
		this.bottomBannerImg1 = bottomBannerImg1;
	}

	public String getBottomLinkType1() {
		return bottomLinkType1;
	}

	public void setBottomLinkType1(String bottomLinkType1) {
		this.bottomLinkType1 = bottomLinkType1;
	}

	public String getBottomBannerImg2() {
		return bottomBannerImg2;
	}

	public void setBottomBannerImg2(String bottomBannerImg2) {
		this.bottomBannerImg2 = bottomBannerImg2;
	}

	public String getBottomLinkType2() {
		return bottomLinkType2;
	}

	public void setBottomLinkType2(String bottomLinkType2) {
		this.bottomLinkType2 = bottomLinkType2;
	}

	public String getBottomLinkVal2() {
		return bottomLinkVal2;
	}

	public void setBottomLinkVal2(String bottomLinkVal2) {
		this.bottomLinkVal2 = bottomLinkVal2;
	}

	public String getBottomLinkVal1() {
		return bottomLinkVal1;
	}

	public void setBottomLinkVal1(String bottomLinkVal1) {
		this.bottomLinkVal1 = bottomLinkVal1;
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

	public int getShowSeq() {
		return showSeq;
	}

	public void setShowSeq(int showSeq) {
		this.showSeq = showSeq;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
}
