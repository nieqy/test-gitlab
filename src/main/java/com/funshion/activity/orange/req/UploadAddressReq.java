package com.funshion.activity.orange.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UploadAddressReq {
    @NotEmpty(message = "openId is empty")
    private String openId;

    @NotNull(message = "prizeId is null")
    private Integer prizeId;

    @NotEmpty(message = "consignee is empty")
    private String consignee;

    @NotEmpty(message = "phone is empty")
    private String phone;

    @NotEmpty(message = "province is empty")
    private String province;

    @NotEmpty(message = "city is empty")
    private String city;

    @NotEmpty(message = "county is empty")
    private String county;

    @NotEmpty(message = "address is empty")
    private String address;

    private String areaCode;

    private String zipCode;

    @NotEmpty(message = "ctime is empty")
    private String ctime;

    @NotEmpty(message = "sign is empty")
    private String sign;

    private String source;
}
