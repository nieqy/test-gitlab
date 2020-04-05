package com.funshion.activity.redpacket.domain;

import lombok.Data;

/**
 * 现金红包实体类
 *
 * @author xiaoym
 * @ClassName: SendRedPack
 * @Description: TODO
 * @date 2016年9月8日
 */
@Data
public class SendGroupRedPackReq {
    private String nonce_str;// 随机字符串
    private String sign;// 签名
    private String mch_billno;// 商户订单号
    private String mch_id;// 商户号
    private String wxappid;// 公众账号
    private String send_name;// 商户名称
    private String re_openid;// 用户
    private int total_amount;// 付款金额 单位：分
    private int total_num;// 红包发放总人数
    private String wishing;// 红包祝福语
    private String client_ip;// Ip地址
    private String act_name;// 活动名称
    private String remark;// 备注
    private String scene_id;// 场景id
    private String amt_type;//
}