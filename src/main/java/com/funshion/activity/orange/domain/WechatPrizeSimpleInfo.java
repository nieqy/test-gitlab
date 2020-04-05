package com.funshion.activity.orange.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class WechatPrizeSimpleInfo implements Comparable<WechatPrizeSimpleInfo> {
    private Integer id;

    private String name;

    private String poster;

    private String lotteryType;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lotteryTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer status;


    @Override
    public int compareTo(WechatPrizeSimpleInfo o) {
        return lotteryTime.compareTo(o.getLotteryTime());
    }
}
