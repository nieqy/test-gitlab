package com.funshion.activity.shopping.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GetCampaignConfigReq {
    @NotEmpty(message = "plat_type is null")
    private String plat_type;

    @NotNull(message = "account_id is null")
    private Integer account_id;

    @NotEmpty(message = "activityType is null")
    private String activityType;

    private String version;

    private String mac;

    @NotEmpty(message = "random is null")
    private String random;

    @NotEmpty(message = "sign is null")
    private String sign;

}
