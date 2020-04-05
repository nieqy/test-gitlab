/**
 * ActivityConstants.java
 * com.funshion.activity.common.constants
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年1月21日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.common.constants;

/**
 * @author xiaowei
 * @ClassName:ActivityConstants
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2019年1月21日        下午1:23:46
 * @see
 * @since Ver 1.1
 */
public class ActivityConstants {

    public static class ActivityType {
        // 通用
        public static final String DEFAULT = "0";
        // 宝马活动
        public static final String BMW = "1";
        // 锦鲤活动
        public static final String JINLI = "2";
        // 红包雨
        public static final String HB = "101";
    }

    public static class SignKey {
        // 宝马活动
        public static final String BMW_SIGN = "61686acf64a6d529";
        // 锦鲤活动
        public static final String JINLI_SIGN = "61686acf64a6d529";
        // 会员sign
        public static final String MERCURY_SIGN = "df2eb3e697746331";

        public static final String ORANGE_SIGN = "xg2e5de69sc4673q";

        public static final String DATABASE_SIGN = "df2ebue697c46s31";
    }


    public static class PrizeStatus {
        // 未达到抽奖条件
        public static final int INIT = 0;
        // 达到抽奖条件, 等待开奖
        public static final int BEFORE_LOTTERY = 1;
        // 未抽奖
        public static final int TO_LOTTERY = 2;
        // 已抽奖，未中奖
        public static final int LOSE = 3;
        // 已中奖,待领取
        public static final int TO_ACCEPT = 4;
        // 已领取
        public static final int ACCEPTED = 5;
    }

    public static class TicketSource {
        // 原始奖券
        public static final String ORIGINAL = "original";

        // 邀请奖券
        public static final String INVITATION = "invitation";

        // 广告奖券
        public static final String AD = "ad";

    }
}

