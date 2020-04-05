package com.funshion.activity.redpacket.rsp;

import com.funshion.activity.redpacket.domain.RedPackInfo;
import lombok.Data;

import java.util.List;

@Data
public class GetRedPackRecordsRsp {

    private List<RedPackInfo> infos;

}
