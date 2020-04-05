package com.funshion.activity.pullalive.domain;

import lombok.Data;

@Data
public class Template1Info {
    private String activityType;
    private String icon;
    /**
     * 抽奖状态。0:未达到抽奖条件;1：达到条件，未抽奖; 2:已抽奖，未中奖; 3:已中奖,待领取; 4:已领取。
     */
    private Integer status;
    private String buttonText;
    private String prizeUrl;
    private String prizeName;
    private String prizeImg;
    private Integer topicId;
    private String topicType;
}
