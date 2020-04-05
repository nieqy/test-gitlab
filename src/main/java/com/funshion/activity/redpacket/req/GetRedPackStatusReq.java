package com.funshion.activity.redpacket.req;

import lombok.Data;

/**
 * 现金红包发送状态查询请求类
 */
@Data
public class GetRedPackStatusReq {
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;
    // 商户订单号
    private String mch_billno;
    // 商户号
    private String mch_id;
    // 微信分配的公众账号ID
    private String appid;
    // 订单类型
    private String bill_type;

}