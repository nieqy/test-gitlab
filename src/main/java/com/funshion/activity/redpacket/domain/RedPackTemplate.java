package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedPackTemplate {

    private Integer id;

    private String name;

    private String title;

    private String type;

    private BigDecimal totalAmount;

    private Integer totalNum;

    private Integer userLimit;

    private String bottomImg;

    private String creator;

    private Integer status;

    private Integer validDay;

    private Date updateTime;
}