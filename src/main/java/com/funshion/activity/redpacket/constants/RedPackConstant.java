package com.funshion.activity.redpacket.constants;

import com.funshion.activity.common.constants.Result;

public interface RedPackConstant {

    class ErrorCode {

        public final static Result<?> UN_START = Result.getFailureResult("901", "活动未开始");

        public final static Result<?> IS_OVER = Result.getFailureResult("902", "活动已结束");

        public final static Result<?> CHANCE_USE_OUT = Result.getFailureResult("903", "领取次数已用完");

        public final static Result<?> INFO_CHECK_ERROR = Result.getFailureResult("904", "信息校验失败");

        public final static Result<?> INVALID = Result.getFailureResult("905", "红包已失效");

        public final static Result<?> OVER_LIMIT = Result.getFailureResult("906", "超过领取次数限制");

        public final static Result<?> RED_PACK_LOSE = Result.getFailureResult("915", "未抽到红包");

        public final static Result<?> RED_PACK_EMPTY = Result.getFailureResult("916", "红包已领完");

        public final static Result<?> GIVE_ERROR = Result.getFailureResult("917", "红包发送失败");

        public final static Result<?> REPEAT_TRY = Result.getFailureResult("918", "请勿重复操作");

        public final static Result<?> AUTH_FAILED = Result.getFailureResult("919", "鉴权失败");

        public final static Result<?> CHANCE_LIMIT = Result.getFailureResult("920", "一个用户限领一次");

        public final static Result<?> UN_SUBSCRIBE = Result.getFailureResult("921", "未订阅公众号");
    }

    class TemplateType {
        public final static String FIXED = "fixed";
        public final static String RANDOM = "random";
    }

    class Status {
        // 待领取
        public final static int INIT = 1;
        // 已发送
        public final static int SENT = 2;
        // 已到账
        public final static int ARRIVAL = 3;
        // 红包退回
        public final static int REFUND = 4;
        // 红包过期
        public final static int EXPIRED = 5;
        // 人工作废
        public final static int DELETE = 6;
    }

    class Img {
        // 未中奖图片
        public final static String NO_PRIZE_IMG = "https://img.funshion.com/sdw?oid=1a5216de3402465b5d844ec662b072a5&w=0&h=0";
        // 活动已结束图片
        public final static String OVER_IMG = "https://img.funshion.com/sdw?oid=e87955eb017a7b2eac49d290dd9473df&w=0&h=0";
        // 机会已用完图片
        public final static String NO_CHANCE_IMG = "https://img.funshion.com/sdw?oid=5d7fc365abe39fe545b0c6e6d940034a&w=0&h=0";
        // 红包已抢完图片
        public final static String OUT_OF_STOCK_IMG = "https://img.funshion.com/sdw?oid=e87955eb017a7b2eac49d290dd9473df&w=0&h=0";
        // 中奖记录背景图
        public final static String PRIZE_BG_IMG = "https://img.funshion.com/sdw?oid=4c0bfe932e1094d1b24349d82428e213&w=0&h=0";
        // 未中奖记录背景图
        public final static String NO_PRIZE_BG_IMG = "https://img.funshion.com/sdw?oid=6b9596584dc4b4bf4c2cb5d42b736490&w=0&h=0";
    }


    class ClientUrl {
        // 红包领取页面地址
        public final static String RED_PACKET_URL = "https://wx-tv.funshion.com/redpacket?tvId=%s&redPackId=%s";
        // 红包领取页面地址2
        public final static String RED_PACKET_URL2 = "https://wx-tv.funshion.com/redpacket2?tvId=%s&redPackId=%s";
    }
}
