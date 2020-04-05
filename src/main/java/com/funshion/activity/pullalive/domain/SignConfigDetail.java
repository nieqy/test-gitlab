package com.funshion.activity.pullalive.domain;

import lombok.Data;

@Data
public class SignConfigDetail {
    private Integer id;

    private String name;

    private Integer signDays;

    private Integer activityType;

    private String prizeType;

    private Integer isFinalPrize;

    private String icon;

    private String focusIcon;

    private String brightIcon;

    private String mediaType;

    private Integer mediaId;
}
