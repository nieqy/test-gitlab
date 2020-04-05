package com.funshion.activity.pullalive.domain;

import lombok.Data;

@Data
public class TravelPlayBlockInfo {
    private Integer isSigned;
    private Integer signDays;
    private Integer activityType;
    private String icon;

    private String focusIcon;

    private String brightIcon;
    /**
     * 抽奖状态。0:未达到抽奖条件;1:达到条件，未到开奖时间；2：达到开奖时间，未抽奖; 3:已抽奖，未中奖; 4:已中奖,待领取; 5:已领取。
     */
    private Integer status;
    private String buttonText;
    private String prizeType;
    private String prizeUrl;
    private String prizeName;
    private String prizeId;
    private String prizeImg;
    private String amount;
    private String mediaType;
    private Integer mediaId;
    private Integer bFocus = 0;
}
