package com.funshion.activity.orange.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PrizeInfoReq {
    //@NotEmpty(message = "openId is null")
    private String openId;

    private Integer prizeId;

    @NotEmpty(message = "ctime is empty")
    private String ctime;

    @NotEmpty(message = "sign is empty")
    private String sign;
}
