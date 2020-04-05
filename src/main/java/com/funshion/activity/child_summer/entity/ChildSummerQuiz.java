package com.funshion.activity.child_summer.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import com.funshion.activity.common.utils.ImgUtils;

public class ChildSummerQuiz{

	private Integer id;
	
	private String prizeDesc;
	
	private String prizeIcon;
	
	private String videoIcon;
	
	private String videoId;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date startTime;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date endTime;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date nextStartTime;
	
	private String phone ;
	
	private List<ChildSummerDetail>  quizDetail = new ArrayList<ChildSummerDetail>(6);
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrizeDesc() {
		return prizeDesc;
	}

	public void setPrizeDesc(String prizeDesc) {
		this.prizeDesc = prizeDesc;
	}

	public String getPrizeIcon() {
		return ImgUtils.getImgPath(prizeIcon);
	}

	public void setPrizeIcon(String prizeIcon) {
		this.prizeIcon = prizeIcon;
	}

	public String getVideoIcon() {
		return ImgUtils.getImgPath(videoIcon);
	}

	public void setVideoIcon(String videoIcon) {
		this.videoIcon = videoIcon;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
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

	public List<ChildSummerDetail> getQuizDetail() {
		return quizDetail;
	}

	public void setQuizDetail(List<ChildSummerDetail> quizDetail) {
		this.quizDetail = quizDetail;
	}

	public Date getNextStartTime() {
		return nextStartTime;
	}

	public void setNextStartTime(Date nextStartTime) {
		this.nextStartTime = nextStartTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
