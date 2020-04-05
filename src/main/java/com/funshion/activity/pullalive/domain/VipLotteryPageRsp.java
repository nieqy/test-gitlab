package com.funshion.activity.pullalive.domain;

import lombok.Data;

import java.util.List;

@Data
public class VipLotteryPageRsp {
    private String bgImg;

    private String bgColor;

    private String ruleContent;

    private String prizeContent;

    private Integer lotteryChances;

    private List<VipLotteryBlock> blocks;
}
