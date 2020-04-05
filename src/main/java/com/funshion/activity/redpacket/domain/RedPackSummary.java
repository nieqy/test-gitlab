package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RedPackSummary {
    private BigDecimal totalAmount;

    private Integer totalNum;
}
