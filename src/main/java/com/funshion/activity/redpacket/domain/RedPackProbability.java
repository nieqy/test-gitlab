/**
 * RedPacketProbability.java
 * com.funshion.mercury.lottery.pojo
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2018年8月17日 		xiaowei
 * <p>
 * Copyright (c) 2018, TNT All Rights Reserved.
 */

package com.funshion.activity.redpacket.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaowei
 * @ClassName:RedPacketProbability
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2018年8月17日        上午11:46:13
 * @see
 * @since Ver 1.1
 */
@Data
public class RedPackProbability {

    private Integer id;

    private BigDecimal start;

    private BigDecimal end;

    private BigDecimal amount;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private BigDecimal probability;

    private Integer totalNum;

    private Integer stock;

}

