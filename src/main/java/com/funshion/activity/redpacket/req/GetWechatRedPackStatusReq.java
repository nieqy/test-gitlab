package com.funshion.activity.redpacket.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GetWechatRedPackStatusReq {

    @NotNull(message = "redPackId is null")
    private Integer redPackId;

    @NotNull(message = "tvId is null")
    private Integer tvId;

    @NotEmpty(message = "ctime is null")
    private String ctime;

    @NotEmpty(message = "sign is null")
    private String sign;
}
