package com.funshion.activity.common.constants;

public class RedisPrefix {

    /**
     * 定时任务同步锁
     */
    public static final String SYNC_LOCK = "mercury_redis_sync_lock_";

    public static final String ACCOUNT_PRIVILETE_KEY = "mercury_account_privilege_";

    /**
     * 积分兑换券ID缓存
     */
    public static final String INTEGRAL_TICKET_INFO = "mercury_redis_integral_ticket_";

    /**
     * 锦鲤活动播放时长排行榜
     */
    public static final String JINLI_PLAY_TIME_TOP = "mercury_jinli_play_time_top_";

    /**
     * 锦鲤活动用户每日信息
     */
    public static final String JINLI_DAILY_USER_INFO = "mercury_jinli_daily_user_info1_";


    /**
     * 橙子视频签到码
     */
    public static final String ORANGE_SIGN_CODE = "mercury_orange_sign_code_";

    /**
     * 微信抽奖活动参与者信息缓存
     */
    public static final String ORANGE_PRIZE_USER_INFO = "mercury_orange_prize_user_";
}
