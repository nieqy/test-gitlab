package com.funshion.activity.jinli.entity;

public class JinliUserInfo {

	private Integer id;

	private String mac;

	private Integer newPlayTime;
	
	private Integer totalPlayTime;
	
	private Integer remainDrawNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(Integer remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	public Integer getNewPlayTime() {
		return newPlayTime;
	}

	public void setNewPlayTime(Integer newPlayTime) {
		this.newPlayTime = newPlayTime;
	}

	public Integer getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(Integer totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}
}
