package com.funshion.activity.orange.entity;

import lombok.Data;

@Data
public class PrizeReceiver {
    private String accountId;

    private Integer prizeId;

    private String prizeType;

    private String prizeName;

    private String prizeImg;

    private String consignee;

    private String phone;

    private String address;
}
