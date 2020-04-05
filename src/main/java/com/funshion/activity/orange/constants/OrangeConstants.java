package com.funshion.activity.orange.constants;

public class OrangeConstants {

    public static final String WECHAT_LOGIN = "http://puser.funshion.com/oauth/applet/weixin";

    public static class PrizeStatus {
        // 待开奖
        public static final int INIT = 0;
        // 已抽奖，未中奖
        public static final int LOSE = 1;
        // 已中奖,待领取
        public static final int TO_ACCEPT = 2;
        // 已领取
        public static final int ACCEPTED = 3;
        // 未领取，已过期
        public static final int EXPIRED = 4;
    }
}
