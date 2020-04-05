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
public class SignButtonInfo {
	
	private String type;	
	
	private String text;

	public SignButtonInfo(String type, String text){
		this.type = type;
		this.text = text;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	

}

