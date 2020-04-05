/**
 * JinliEntryData.java
 * com.funshion.activity.jinli.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2019年1月10日 		xiaowei
 *
 * Copyright (c) 2019, TNT All Rights Reserved.
*/

package com.funshion.activity.jinli.entity;

import java.util.List;

/**
 * @ClassName:JinliEntryData
 * @Function: TODO ADD FUNCTION
 * @Reason:	 TODO ADD REASON
 *
 * @author   xiaowei
 * @version  
 * @since    Ver 1.1
 * @Date	 2019年1月10日		下午2:58:25
 *
 * @see 	 
 * 
 */
public class JinliEntryData {

	private String title;
	private String bgColor;
	private String cornerImg;
	private List<JinliEntryListDetail> details;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getCornerImg() {
		return cornerImg;
	}

	public void setCornerImg(String cornerImg) {
		this.cornerImg = cornerImg;
	}

	public List<JinliEntryListDetail> getDetails() {
		return details;
	}

	public void setDetails(List<JinliEntryListDetail> details) {
		this.details = details;
	}

}

