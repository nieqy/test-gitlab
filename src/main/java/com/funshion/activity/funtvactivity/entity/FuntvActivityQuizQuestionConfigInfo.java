package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityQuizQuestionConfigInfo {

	@JSONField(serialize = false)
	private String indexImgs;

	@JSONField(name = "indexImgs")
	private List<String> indexImages;

	private String bgColor;

	private String textColor;

	private String answerRightImg;

	private String answerWrongImg;

	private String itemBgColor;

	private String itemTextColor;

	private String itemHoverBgColor;

	private String itemHoverTextColor;

	private String answerTipBtnImg;

	public String getIndexImgs() {
		return indexImgs;
	}

	public void setIndexImgs(String indexImgs) {
		this.indexImgs = indexImgs;
	}

	public List<String> getIndexImages() {
		return indexImages;
	}

	public void setIndexImages(List<String> indexImages) {
		this.indexImages = indexImages;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getAnswerRightImg() {
		return answerRightImg;
	}

	public void setAnswerRightImg(String answerRightImg) {
		this.answerRightImg = answerRightImg;
	}

	public String getAnswerWrongImg() {
		return answerWrongImg;
	}

	public void setAnswerWrongImg(String answerWrongImg) {
		this.answerWrongImg = answerWrongImg;
	}

	public String getItemBgColor() {
		return itemBgColor;
	}

	public void setItemBgColor(String itemBgColor) {
		this.itemBgColor = itemBgColor;
	}

	public String getItemTextColor() {
		return itemTextColor;
	}

	public void setItemTextColor(String itemTextColor) {
		this.itemTextColor = itemTextColor;
	}

	public String getItemHoverBgColor() {
		return itemHoverBgColor;
	}

	public void setItemHoverBgColor(String itemHoverBgColor) {
		this.itemHoverBgColor = itemHoverBgColor;
	}

	public String getItemHoverTextColor() {
		return itemHoverTextColor;
	}

	public void setItemHoverTextColor(String itemHoverTextColor) {
		this.itemHoverTextColor = itemHoverTextColor;
	}

	public String getAnswerTipBtnImg() {
		return answerTipBtnImg;
	}

	public void setAnswerTipBtnImg(String answerTipBtnImg) {
		this.answerTipBtnImg = answerTipBtnImg;
	}
}
