package com.funshion.activity.redpacket.rsp;

import lombok.Data;

@Data
public class GetCoverInfoRsp {
    private Integer status;
    private String bottomImg;
    private String title;
}
