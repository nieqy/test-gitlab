package com.funshion.activity.redpacket.rsp;

import lombok.Data;

@Data
public class GetHbForSubWechatRsp {
    private Integer redPackId;

    private Integer tvId;

    private String amount;

    private String url;
}
