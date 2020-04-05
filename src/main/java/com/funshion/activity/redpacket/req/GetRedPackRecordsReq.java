package com.funshion.activity.redpacket.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GetRedPackRecordsReq {
    @NotNull(message = "activityType is null")
    private Integer activityType;

    @NotNull(message = "tvId is null")
    private Integer tvId;

    @NotEmpty(message = "ctime is null")
    private String ctime;

    @NotEmpty(message = "sign is null")
    private String sign;
}
