/**
 * JinliAddAddressReq.java
 * com.funshion.activity.jinli.req
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2019年1月7日 		xiaowei
 *
 * Copyright (c) 2019, TNT All Rights Reserved.
*/

package com.funshion.activity.jinli.req;
/**
 * @ClassName:JinliAddAddressReq
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   xiaowei
 * @version  
 * @since    Ver 1.1
 * @Date	 2019年1月7日		下午2:47:43
 *
 * @see 	 
 * 
 */
public class JinliAcceptPrizeReq {
	private Integer tvId;
	private Integer winId;
	private String mac;
	private String consignee;
	private String phone;
	private String address;
	private String ctime;
	private String sign;

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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public Integer getWinId() {
		return winId;
	}

	public void setWinId(Integer winId) {
		this.winId = winId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

}

