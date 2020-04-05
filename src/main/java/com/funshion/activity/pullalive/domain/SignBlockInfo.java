package com.funshion.activity.pullalive.domain;

import lombok.Data;

@Data
public class SignBlockInfo {
    private Integer activityType;
    private String icon;
    /**
     * 抽奖状态。0:未达到抽奖条件;1:达到条件，未到开奖时间；2：达到开奖时间，未抽奖; 3:已抽奖，未中奖; 4:已中奖,待领取; 5:已领取。
     */
    private Integer status;
    private String buttonText;
    private String prizeType;
    private String prizeUrl;
    private String prizeName;
    private String prizeImg;
    private Integer requireDays;
    private String amount;
}
