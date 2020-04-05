package com.funshion.activity.pullalive.domain;

import lombok.Data;

import java.util.List;

@Data
public class SignTemplateRsp {
    private String bgImg;

    private Integer signDays;

    private Integer isSigned;

    private String finalPrizeTitle;

    private String finalPrizeDate;

    private long daysBeforeFinalPrizeDate;

    private int daysNeededForFinalPrize;

    private Integer maxSignDays;

    private String ruleContent;

    private List<SignBlockInfo> blocks;

}
