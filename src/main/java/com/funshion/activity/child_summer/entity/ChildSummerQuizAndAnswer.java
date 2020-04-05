package com.funshion.activity.child_summer.entity;


import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.funshion.activity.common.utils.ImgUtils;


public class ChildSummerQuizAndAnswer{

	private Integer id;
	
	private String prizeOption;
	
	private String prizeBrand;
	
	private String specialIcon;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date startTime;
	
	@JSONField(format ="MM月dd日 HH点")
	private Date endTime;
	
	private String score;
	
	@JSONField(serialize = false)
	private Integer needAnswer;
	
	private Integer win = 0 ;
	
	@JSONField (serialize = false)
	private String winners;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrizeOption() {
		return prizeOption;
	}

	public void setPrizeOption(String prizeOption) {
		this.prizeOption = prizeOption;
	}

	public String getPrizeBrand() {
		return prizeBrand;
	}

	public void setPrizeBrand(String prizeBrand) {
		this.prizeBrand = prizeBrand;
	}

	public String getSpecialIcon() {
		return ImgUtils.getImgPath(specialIcon);
	}

	public void setSpecialIcon(String specialIcon) {
		this.specialIcon = specialIcon;
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}


	public Integer getWin() {
		return win;
	}

	public void setWin(Integer win) {
		this.win = win;
	}

	public String getWinners() {
		return winners;
	}

	public void setWinners(String winners) {
		this.winners = winners;
	}

	public Integer getNeedAnswer() {
		return needAnswer;
	}

	public void setNeedAnswer(Integer needAnswer) {
		this.needAnswer = needAnswer;
	}

	
	
}
