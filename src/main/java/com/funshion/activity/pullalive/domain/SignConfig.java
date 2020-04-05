package com.funshion.activity.pullalive.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class SignConfig {
    private Integer id;

    private Integer activityType;

    private String title;

    private String bgImg;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date finalPrizeDate;

    private Integer maxSignDays;

    private String ruleContent;
}
