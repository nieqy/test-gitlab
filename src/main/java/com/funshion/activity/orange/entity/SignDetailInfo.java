/**
 * SignAwradDetail.java
 * com.funshion.mercury.base.dao.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年3月21日 		nieqy
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package com.funshion.activity.orange.entity;


/**
 * @ClassName:SignAwradDetail
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   nieqy
 * @version  
 * @since    Ver 1.1
 * @Date	 2019年4月21日		上午10:42:29
 *
 * @see 	 
 * 
 */
public class SignDetailInfo {
	
	private String day;	
	
	private Integer prize = 0;
	
	private Integer status = 0;

	public  SignDetailInfo(String day,Integer status){
		this.day = day;
		this.status = status;
	}
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



}

