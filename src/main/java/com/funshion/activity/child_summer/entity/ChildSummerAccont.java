package com.funshion.activity.child_summer.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class ChildSummerAccont {
	
	@JSONField (serialize= false)
	private Integer id;
	
	private Integer accountId;
	
	@JSONField(name="consignee")
	private String receivingName;
	
	@JSONField(name = "phone")
	private String receivingPhone;
	
	@JSONField(name = "address")
	private String receivingAddress;
	
	@JSONField (serialize= false)
	private String phone;
	
	@JSONField (serialize= false)
	private Integer points;
	
	@JSONField (serialize= false)
	private Integer pRank;
	
	@JSONField (serialize= false)
	private Integer type;
	
	@JSONField (serialize= false)
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
