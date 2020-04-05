package com.funshion.activity.funtvactivity.entity;

/**
 * Created by zhangfei on 2019/05/24/001.
 */
public class FuntvActivityPrizeListInfo {

	private Integer contentId;

	private String contentName;

	private String startTime;

	private String endTime;

	private String phone;

	private String prizeType;

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public static class FuntvActivityFinalPrizeInfo {

		private String prizeType;

		private String phone;

		public String getPrizeType() {
			return prizeType;
		}

		public void setPrizeType(String prizeType) {
			this.prizeType = prizeType;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
}
