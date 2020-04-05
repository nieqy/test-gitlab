package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedPackTemplateDetail {

    private Integer id;

    private Integer templateId;

    private String name;

    private BigDecimal amount;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private BigDecimal probability;

    private Integer totalNum;

    private Integer stock;

    private String creator;

    private Date updateTime;

    private Date invalidTime;

    private Integer status;

}