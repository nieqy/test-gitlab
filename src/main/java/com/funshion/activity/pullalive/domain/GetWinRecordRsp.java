package com.funshion.activity.pullalive.domain;

import lombok.Data;

import java.util.List;

@Data
public class GetWinRecordRsp {

    private String bgImg;

    private List<MultPrizeInfo> prizes;
}
