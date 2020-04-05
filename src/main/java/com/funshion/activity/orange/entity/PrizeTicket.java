package com.funshion.activity.orange.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PrizeTicket {
    private String code;
    private String source;
    private String icon;

    @JSONField(serialize = false)
    private String accountId;

    @JSONField(serialize = false)
    private Integer prizeId;

    @JSONField(serialize = false)
    private String sourceId;

    @JSONField(serialize = false)
    private Integer status;
}
