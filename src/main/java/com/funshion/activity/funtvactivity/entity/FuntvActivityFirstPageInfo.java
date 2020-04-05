package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuntvActivityFirstPageInfo {

	private Integer id;

	private String activityName;

	private int activityType;

	private String bgColor;

	private String cnzzLogId;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	private List<FuntvActivityFirstPageFloorInfo> floors = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getCnzzLogId() {
		return cnzzLogId;
	}

	public void setCnzzLogId(String cnzzLogId) {
		this.cnzzLogId = cnzzLogId;
	}

	public List<FuntvActivityFirstPageFloorInfo> getFloors() {
		return floors;
	}

	public void setFloors(List<FuntvActivityFirstPageFloorInfo> floors) {
		this.floors = floors;
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

	public static class FuntvActivityFirstPageFloorInfo {

		private Integer id;

		private String floorName;

		private String bgImg;

		private Integer height;

		private int showSeq;

		private List<FuntvActivityFirstPageItemInfo> cols = new ArrayList<>();

		public String getFloorName() {
			return floorName;
		}

		public void setFloorName(String floorName) {
			this.floorName = floorName;
		}

		public String getBgImg() {
			return bgImg;
		}

		public void setBgImg(String bgImg) {
			this.bgImg = bgImg;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

		public int getShowSeq() {
			return showSeq;
		}

		public void setShowSeq(int showSeq) {
			this.showSeq = showSeq;
		}

		public List<FuntvActivityFirstPageItemInfo> getCols() {
			return cols;
		}

		public void setCols(List<FuntvActivityFirstPageItemInfo> cols) {
			this.cols = cols;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
	}

	public static class FuntvActivityFirstPageItemInfo {

		private Integer activityFloorId;

		private String bgImg;

		private String hoverImg;

		private String borderRadius;

		private Integer width;

		private Integer height;

		private Integer positionTop;

		private Integer positionLeft;

		private String linkType;

		private String linkVal;

		private int showSeq;

		public String getBgImg() {
			return bgImg;
		}

		public void setBgImg(String bgImg) {
			this.bgImg = bgImg;
		}

		public String getHoverImg() {
			return hoverImg;
		}

		public void setHoverImg(String hoverImg) {
			this.hoverImg = hoverImg;
		}

		public String getBorderRadius() {
			return borderRadius;
		}

		public void setBorderRadius(String borderRadius) {
			this.borderRadius = borderRadius;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

		public Integer getPositionTop() {
			return positionTop;
		}

		public void setPositionTop(Integer positionTop) {
			this.positionTop = positionTop;
		}

		public Integer getPositionLeft() {
			return positionLeft;
		}

		public void setPositionLeft(Integer positionLeft) {
			this.positionLeft = positionLeft;
		}

		public String getLinkType() {
			return linkType;
		}

		public void setLinkType(String linkType) {
			this.linkType = linkType;
		}

		public String getLinkVal() {
			return linkVal;
		}

		public void setLinkVal(String linkVal) {
			this.linkVal = linkVal;
		}

		public int getShowSeq() {
			return showSeq;
		}

		public void setShowSeq(int showSeq) {
			this.showSeq = showSeq;
		}

		public Integer getActivityFloorId() {
			return activityFloorId;
		}

		public void setActivityFloorId(Integer activityFloorId) {
			this.activityFloorId = activityFloorId;
		}
	}

}
