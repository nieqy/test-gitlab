/**
 * AddressInfo.java
 * com.funshion.activity.common.bean
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2019年1月18日 		xiaowei
 *
 * Copyright (c) 2019, TNT All Rights Reserved.
*/

package com.funshion.activity.common.bean;
/**
 * @ClassName:AddressInfo
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   xiaowei
 * @version  
 * @since    Ver 1.1
 * @Date	 2019年1月18日		下午3:02:15
 *
 * @see 	 
 * 
 */
public class AddressInfo {
	private Integer tvId;

	private String mac;

	private String ctime;

	private String sign;

	private String activityType;

	private Integer sourceId;

	private String consignee;

	private String phone;

	private String address;

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getTvId() {
		return tvId;
	}

	public void setTvId(Integer tvId) {
		this.tvId = tvId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

}

