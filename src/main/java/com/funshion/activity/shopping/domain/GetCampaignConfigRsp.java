package com.funshion.activity.shopping.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCampaignConfigRsp {

    // 购物项目标题
    private String activityName;
    // 活动ID
    private String activityType;
    // 首页背景色
    private String homeBgColor;
    // 首页背景头图
    private String homeHeaderImg;
    // 首页底部背景图，图片链接
    private String homeFooterImg;

    private String homeCImg;

    // CNZZ日志上报ID
    private String cnzzLogId;

    private DialogConfig dialogConfig;

    private ListPageConfig listPageConfig;

    private FloorConfig floorConfig;
}
