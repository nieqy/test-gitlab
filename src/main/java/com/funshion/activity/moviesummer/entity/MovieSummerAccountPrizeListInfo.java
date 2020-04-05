package com.funshion.activity.moviesummer.entity;

import java.util.Date;

/**
 * Created by zhangfei on 2018/8/7/007.
 */
public class MovieSummerAccountPrizeListInfo {

	private String subjectName;

	private Date startTime;

	private Date endTime;

	private String phone;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static class MovieSummerAccountFinalPrizeInfo{
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
