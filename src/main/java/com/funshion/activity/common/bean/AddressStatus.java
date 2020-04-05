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
public class AddressStatus {
	private Integer status;
	private String phone;
	private String consignee;
	private String address;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}

