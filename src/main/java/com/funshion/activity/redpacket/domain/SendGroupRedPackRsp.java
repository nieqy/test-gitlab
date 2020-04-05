package com.funshion.activity.redpacket.domain;

import lombok.Data;

/**
 * 现金红包发送结果
 */
@Data
public class SendGroupRedPackRsp {
    /*
     * 微信返回的发送结果 {total_amount=100, result_code=SUCCESS, mch_id=1388107602,
     * mch_billno=138810760220160908152254, err_code=SUCCESS,
     * send_listid=10000417012016090830053806097, wxappid=wx45310e1220339304,
     * err_code_des=发放成功, return_msg=发放成功,
     * re_openid=oiCHPv2Et1ujnas2UgknXeK5Bxhc, return_code=SUCCESS}
     */
    private int total_amount;
    private int total_num;
    private String result_code;
    private String mch_id;
    private String mch_billno;
    private String err_code;
    private String send_listid;
    private String wxappid;
    private String err_code_des;
    private String return_msg;
    private String re_openid;
    private String return_code;

}