package com.funshion.activity.funtvactivity.common;

/**
 * Created by zhangfei on 2019/5/24/024.
 */
public enum FuntvActivityPrizeType {

	//0未开奖，1未参与，2未中奖，3幸运奖，4二等奖，5一等奖
	NOT_LOTTERY(0, "未开奖"),
	NOT_JOIN_ACTIVITY(1, "未参与"),
	NOT_WIN_PRIZE(2, "未中奖"),
	CONTENT_PRIZE(3, "内容中奖"),
	LUCKY_PRIZE(4, "幸运奖"),
	SPECIAL_PRIZE(5, "特等奖"),
	/*SECOND_PRIZE(6, "二等奖"),
	THIRD_PRIZE(7, "三等奖"),
	FOURTH_PRIZE(8, "四等奖")*/
	;

	private int prizeType;

	private String prizeName;

	FuntvActivityPrizeType(int prizeType, String prizeName) {
		this.prizeType = prizeType;
		this.prizeName = prizeName;
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
}
