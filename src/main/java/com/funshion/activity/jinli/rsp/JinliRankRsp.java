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

package com.funshion.activity.jinli.rsp;

import java.util.List;

import com.funshion.activity.jinli.entity.JinliRankInfo;

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
public class JinliRankRsp {
	private String mac;
	private String newPlayTime;
	private String totalPlayTime;
	private Long ranking;
	private List<JinliRankInfo> details;

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public List<JinliRankInfo> getDetails() {
		return details;
	}

	public void setDetails(List<JinliRankInfo> details) {
		this.details = details;
	}

	public String getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(String totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}

	public String getNewPlayTime() {
		return newPlayTime;
	}

	public void setNewPlayTime(String newPlayTime) {
		this.newPlayTime = newPlayTime;
	}

}

