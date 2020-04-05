  package com.funshion.activity.child_summer.entity;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class ChildSummerPrize {

	private String title;
	
	@JSONField (serialize = false)
	private String name;
	
	@JSONField (serialize = false)
	private String prizeBrand;
	
	@JSONField (serialize = false)
	private String winners;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date startTime;
	
	@JSONField(format = "MM月dd日 HH点")
	private Date endTime;

	private List<String> phones;
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrizeBrand() {
		return prizeBrand;
	}

	public void setPrizeBrand(String prizeBrand) {
		this.prizeBrand = prizeBrand;
	}

	public String getWinners() {
		return winners;
	}

	public void setWinners(String winners) {
		this.winners = winners;
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

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	
	
	
	
}
