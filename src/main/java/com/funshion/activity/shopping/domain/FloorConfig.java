package com.funshion.activity.shopping.domain;

import lombok.Data;

@Data
public class FloorConfig {
    // 商品卡片背景色
    private String cardBgColor;
    // 商品抢完/售空图
    private String selloutImg;
    // 秒杀价格文字颜色
    private String seckillTextColor;
    // 原价文字颜色
    private String primeCostTextColor;
    // 卡片底部商品名字文字颜色
    private String nameTextColor;
    // 抢购按钮
    private String seckillBtnImg;


}
