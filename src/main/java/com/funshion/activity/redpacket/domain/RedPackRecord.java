package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedPackRecord {

    private Integer id;

    private Integer templateId;

    private Integer activityType;

    private Integer tvId;

    private String openId;

    private Integer winId;

    private Integer status;

    private BigDecimal amount;

    private String orderCode;

    private String pCode;

    private String gCode;

    private Date invalidTime;

    private Date updateTime;

    private Date createTime;
}