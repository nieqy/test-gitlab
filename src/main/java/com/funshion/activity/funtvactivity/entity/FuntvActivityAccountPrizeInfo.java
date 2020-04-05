package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityAccountPrizeInfo {

	private Integer id;

	private int activityType;

	private Integer accountId;

	private String mac;

	private String phone;

	private String prizeFlag;

	private Integer prizeType;//0未开奖，1未参与，2未中奖，3幸运奖，4二等奖，5一等奖

	private Integer isAccept;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getPrizeFlag() {
		return prizeFlag;
	}

	public void setPrizeFlag(String prizeFlag) {
		this.prizeFlag = prizeFlag;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
