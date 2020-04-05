package com.funshion.activity.moviesummer.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class MovieSummerQuizInfo {

	private Integer id;

	private String subjectName;

	private String subjectNamePicture;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	private Integer movie1Id;

	private String movie1Name;

	private String movie1Picture;

	private Integer movie2Id;

	private String movie2Name;

	private String movie2Picture;

	private Integer movie3Id;

	private String movie3Name;

	private String movie3Picture;

	private Integer movie4Id;

	private String movie4Name;

	private String movie4Picture;

	private Integer movie5Id;

	private String movie5Name;

	private String movie5Picture;

	private Integer topicId;

	private String topicPicture;

	private int showSeq;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public Integer getMovie1Id() {
		return movie1Id;
	}

	public void setMovie1Id(Integer movie1Id) {
		this.movie1Id = movie1Id;
	}

	public String getMovie1Name() {
		return movie1Name;
	}

	public void setMovie1Name(String movie1Name) {
		this.movie1Name = movie1Name;
	}

	public String getMovie1Picture() {
		return movie1Picture;
	}

	public void setMovie1Picture(String movie1Picture) {
		this.movie1Picture = movie1Picture;
	}

	public Integer getMovie2Id() {
		return movie2Id;
	}

	public void setMovie2Id(Integer movie2Id) {
		this.movie2Id = movie2Id;
	}

	public String getMovie2Name() {
		return movie2Name;
	}

	public void setMovie2Name(String movie2Name) {
		this.movie2Name = movie2Name;
	}

	public String getMovie2Picture() {
		return movie2Picture;
	}

	public void setMovie2Picture(String movie2Picture) {
		this.movie2Picture = movie2Picture;
	}

	public Integer getMovie3Id() {
		return movie3Id;
	}

	public void setMovie3Id(Integer movie3Id) {
		this.movie3Id = movie3Id;
	}

	public String getMovie3Name() {
		return movie3Name;
	}

	public void setMovie3Name(String movie3Name) {
		this.movie3Name = movie3Name;
	}

	public String getMovie3Picture() {
		return movie3Picture;
	}

	public void setMovie3Picture(String movie3Picture) {
		this.movie3Picture = movie3Picture;
	}

	public Integer getMovie4Id() {
		return movie4Id;
	}

	public void setMovie4Id(Integer movie4Id) {
		this.movie4Id = movie4Id;
	}

	public String getMovie4Name() {
		return movie4Name;
	}

	public void setMovie4Name(String movie4Name) {
		this.movie4Name = movie4Name;
	}

	public String getMovie4Picture() {
		return movie4Picture;
	}

	public void setMovie4Picture(String movie4Picture) {
		this.movie4Picture = movie4Picture;
	}

	public Integer getMovie5Id() {
		return movie5Id;
	}

	public void setMovie5Id(Integer movie5Id) {
		this.movie5Id = movie5Id;
	}

	public String getMovie5Name() {
		return movie5Name;
	}

	public void setMovie5Name(String movie5Name) {
		this.movie5Name = movie5Name;
	}

	public String getMovie5Picture() {
		return movie5Picture;
	}

	public void setMovie5Picture(String movie5Picture) {
		this.movie5Picture = movie5Picture;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getTopicPicture() {
		return topicPicture;
	}

	public void setTopicPicture(String topicPicture) {
		this.topicPicture = topicPicture;
	}

	public int getShowSeq() {
		return showSeq;
	}

	public void setShowSeq(int showSeq) {
		this.showSeq = showSeq;
	}

	public String getSubjectNamePicture() {
		return subjectNamePicture;
	}

	public void setSubjectNamePicture(String subjectNamePicture) {
		this.subjectNamePicture = subjectNamePicture;
	}

}
