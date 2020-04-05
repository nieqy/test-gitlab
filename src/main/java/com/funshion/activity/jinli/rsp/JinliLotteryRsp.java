/**
 * JinliAddAddressReq.java
 * com.funshion.activity.jinli.req
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年1月7日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.jinli.rsp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xiaowei
 * @ClassName:JinliAddAddressReq
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2019年1月7日        下午2:47:43
 * @see
 * @since Ver 1.1
 */
@Data
public class JinliLotteryRsp {

    private Integer winId;

    private Integer remainDrawNum;

    private String prizeType;

    private String prizeUrl;

    private String prizeId;

    private String prizeName;

    private String prizeImg;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date invalidTime;

    private String amount;

}

