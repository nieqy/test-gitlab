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

package com.funshion.activity.common.bean;

import lombok.Data;

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
public class AddPrizeReq {
    private Integer tvId;
    private Integer activityType;
    private String mac;
    private Integer lotteryId;
    private String prizeType;
    private String prizeName;
    private String prizeId;
    private String prizeImg;
    private String prizeUrl;
    private String invalidTime;
    private String ctime;
    private String sign;
}

