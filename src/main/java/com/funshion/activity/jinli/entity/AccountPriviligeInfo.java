/**
 * AccountPriviligeInfo.java
 * com.funshion.mercury.base.dao.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年9月26日 		nieqy
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package com.funshion.activity.jinli.entity;

import java.util.Date;

/**
 * @ClassName:AccountPriviligeInfo
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   nieqy
 * @version  
 * @since    Ver 1.1
 * @Date	 2017年9月26日		下午3:30:03
 *
 * @see 	 
 * 
 */
public class AccountPriviligeInfo {
	
	private Integer id;
	
	private Integer tvId;
	
	private String bestvId;
	  
	private Integer packageId;
	
	private Date validStartTime;
	
	private Date validEndTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTvId() {
		return tvId;
	}

	public void setTvId(Integer tvId) {
		this.tvId = tvId;
	}

	public String getBestvId() {
		return bestvId;
	}

	public void setBestvId(String bestvId) {
		this.bestvId = bestvId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}
	

}

