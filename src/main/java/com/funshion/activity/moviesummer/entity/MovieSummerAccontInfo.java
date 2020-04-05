package com.funshion.activity.moviesummer.entity;

import java.util.Date;

public class MovieSummerAccontInfo {

	private Integer id;

	private Integer accountId;

	private String receivingName;

	private String receivingPhone;

	private String receivingAddress;

	private String phone;

	private Integer points;

	private Integer pRank;

	private Integer type;

	private Date lastCommit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getReceivingName() {
		return receivingName;
	}

	public void setReceivingName(String receivingName) {
		this.receivingName = receivingName;
	}

	public String getReceivingPhone() {
		return receivingPhone;
	}

	public void setReceivingPhone(String receivingPhone) {
		this.receivingPhone = receivingPhone;
	}

	public String getReceivingAddress() {
		return receivingAddress;
	}

	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getpRank() {
		return pRank;
	}

	public void setpRank(Integer pRank) {
		this.pRank = pRank;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getLastCommit() {
		return lastCommit;
	}

	public void setLastCommit(Date lastCommit) {
		this.lastCommit = lastCommit;
	}


}
