package com.funshion.activity.pullalive.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PullAliveReq {
    @NotNull(message = "tvId is null")
    private Integer tvId;

    @NotEmpty(message = "mac is empty")
    private String mac;

    @NotEmpty(message = "ctime is empty")
    private String ctime;

    @NotEmpty(message = "sign is empty")
    private String sign;
}
