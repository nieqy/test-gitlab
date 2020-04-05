package com.funshion.activity.jinli.constant;

public class JinliConstants {


	public static class PrizeType {
		// 产品
		public static final String PRODUCT = "product";
		// 产品（赠品）
		public static final String GIFT = "gift";
		// 会员卡
		public static final String VIP = "vip";
		// 第三方
		public static final String THIRDPARTY = "thirdparty";
		// 抽奖券
		public static final String RAFFLE_TICKET = "raffleTicket";
		// 积分奖品
		public static final String BONUS_POINTS = "bonusPoints";
		// 现金红包
		public static final String RED_PACKET = "redPacket";
		// 活动
		public static final String ACTIVITY = "activity";
		// 谢谢参与
		public static final String NOTHING = "nothing";
	}


	public static class PrizeStatus {
		// 待领取
		public static final int TO_ACCEPT = 0;

		// 已领取
		public static final int ACCEPT = 1;

		// 已过期
		public static final int EXPIRED = 2;
	}


}
