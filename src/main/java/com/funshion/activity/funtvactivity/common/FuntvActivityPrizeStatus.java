package com.funshion.activity.funtvactivity.common;

/**
 * Created by zhangfei on 2019/5/24/024.
 */
public enum FuntvActivityPrizeStatus {

	//0没开始，1正在进行，2已结束

	NOT_START(0, "没开始"),
	PROCESSING(1, "正在进行"),
	FINISHED(2, "已结束");

	private int status;

	private String statusName;

	FuntvActivityPrizeStatus(int status, String statusName) {
		this.status = status;
		this.statusName = statusName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}
