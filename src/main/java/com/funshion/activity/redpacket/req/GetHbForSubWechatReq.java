package com.funshion.activity.redpacket.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GetHbForSubWechatReq {
    @NotNull(message = "activityType is null")
    private Integer activityType;

    @NotEmpty(message = "openId is null")
    private String openId;

    private Integer templateId;

    @NotEmpty(message = "ctime is null")
    private String ctime;

    @NotEmpty(message = "sign is null")
    private String sign;

    private String ip;
}
