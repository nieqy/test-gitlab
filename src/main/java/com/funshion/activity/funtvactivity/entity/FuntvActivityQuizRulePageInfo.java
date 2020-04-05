package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class FuntvActivityQuizRulePageInfo {

	@JSONField(serialize = false)
	private String bgImg;

	@JSONField(name = "bgImg")
	private List<String> bgImgs;

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public List<String> getBgImgs() {
		return bgImgs;
	}

	public void setBgImgs(List<String> bgImgs) {
		this.bgImgs = bgImgs;
	}
}
