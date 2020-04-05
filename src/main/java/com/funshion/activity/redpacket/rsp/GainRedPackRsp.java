package com.funshion.activity.redpacket.rsp;

import lombok.Data;

@Data
public class GainRedPackRsp {
    private Integer redPackId;

    private String amount;

    private String url;
}
