package com.funshion.activity.orange.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PrizeDetailReq {
    @NotEmpty(message = "openId is null")
    private String openId;

    @NotNull(message = "prizeId is empty")
    private Integer prizeId;

    @NotEmpty(message = "ctime is empty")
    private String ctime;

    @NotEmpty(message = "sign is empty")
    private String sign;
}
