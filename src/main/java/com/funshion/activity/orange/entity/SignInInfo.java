/**
 * IntegralTaskInfo.java
 * com.funshion.mercury.base.dao.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年12月22日 		nieqy
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package com.funshion.activity.orange.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;



/**
 * @ClassName:IntegralTaskInfo
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   nieqy
 * @version  
 * @since    Ver 1.1
 * @Date	 2017年12月22日		上午11:42:25
 *
 * @see 	 
 * 
 */
public class SignInInfo {
	
	private Integer signId;
	
	private String name;
	
	private String aword;
	
	private String bgImg;
	
	private Date startTime; 
	
	private Date endTime;

	public Integer getSignId() {
		return signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAword() {
		return aword;
	}

	public void setAword(String aword) {
		this.aword = aword;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
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

	
	
}

