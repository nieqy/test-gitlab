package com.funshion.activity.shopping.domain;

import lombok.Data;

@Data
public class DialogConfig {
    // 弹框背景色
    private String dialogBgColor;
    // 页面背景色--弹框之外的背景色
    private String bgColor;
    // 文字描述颜色
    private String textColor;
    // 购物车小图标icon
    private String shopIconImg;
    // 小程序小图标icon
    private String miniIconImg;

    private String nameTextColor;
    private String nameTextBgColor;
    private String selloutImg;
    private String cardBgColor;

}
