package com.funshion.activity.shopping.domain;

import lombok.Data;

@Data
public class ListPageConfig {
    // 商品卡片背景色
    private String cardBgColor;
    // 卡片底部商品名字文字颜色
    private String nameTextColor;
    // 商品价格文字颜色
    private String priceTextColor;
    // 返回首页按钮图片
    private String bkHomeBtnImg;
}
