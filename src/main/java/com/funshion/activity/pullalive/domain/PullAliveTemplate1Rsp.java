package com.funshion.activity.pullalive.domain;

import lombok.Data;

import java.util.List;

@Data
public class PullAliveTemplate1Rsp {
    private Integer playDays;

    private String bgImg;

    private String ruleContent;

    private List<Template1Info> blocks;

}
