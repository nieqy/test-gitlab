package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.util.List;

/**
 * 现金红包发送结果
 */
@Data
public class GetRedPackStatusRsp {
    /*
     * 微信返回的发送结果 {total_amount=100, result_code=SUCCESS, mch_id=1388107602,
     * mch_billno=138810760220160908152254, err_code=SUCCESS,
     * send_listid=10000417012016090830053806097, wxappid=wx45310e1220339304,
     * err_code_des=发放成功, return_msg=发放成功,
     * re_openid=oiCHPv2Et1ujnas2UgknXeK5Bxhc, return_code=SUCCESS}
     */
    // SUCCESS/FAIL 非红包发放结果标识，红包发放是否成功需要查看status字段来判断
    private String result_code;
    // SUCCESS/FAIL 此字段是通信标识，非红包发放结果标识，红包发放是否成功需要结合result_code以及status来判断
    private String return_code;
    private String err_code_des;
    private String return_msg;

    private Integer total_amount;
    private String mch_id;
    private String mch_billno;
    private String err_code;
    private String send_listid;
    private String wxappid;
    private String re_openid;

    // 使用API发放现金红包时返回的红包单号
    private String detail_id;

    /**
     * SENDING:发放中
     * SENT:已发放待领取
     * FAILED：发放失败
     * RECEIVED:已领取
     * RFUND_ING:退款中
     * REFUND:已退款
     */
    private String status;
    // API:通过API接口发放
    private String send_type;

    // GROUP:裂变红包; NORMAL:普通红包
    private String hb_type;

    private String reason;

    private String send_time;
    private String refund_time;
    private Integer refund_amount;
    private String openid;
    private Integer amount;
    private String rcv_time;
    private Integer total_num;

    private List<HbInfo> hblist;
}