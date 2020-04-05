package com.funshion.activity.orange.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class PrizeInfo {
    private Integer id;

    private String name;

    private String stock;

    private String poster;

    private String description;

    private String threshold;

    private String lotteryType;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lotteryTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer status;
}
