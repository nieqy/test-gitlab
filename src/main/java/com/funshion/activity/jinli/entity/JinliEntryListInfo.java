package com.funshion.activity.jinli.entity;

public class JinliEntryListInfo {
	
    private Integer id;

	private String type;

	private String title;

	private String bgImg;

	private String cornerImg;

	private String bgColor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}