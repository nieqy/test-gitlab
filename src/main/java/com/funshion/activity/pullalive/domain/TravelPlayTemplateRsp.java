package com.funshion.activity.pullalive.domain;

import lombok.Data;

import java.util.List;

@Data
public class TravelPlayTemplateRsp {
    private String bgImg;

    private String bgColor;

    private String ruleContent;

    private String prizeContent;

    private Integer showFinalPrize;

    private List<TravelPlayBlockInfo> blocks;

}
