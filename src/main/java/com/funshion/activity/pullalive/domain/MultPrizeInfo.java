package com.funshion.activity.pullalive.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MultPrizeInfo implements Comparable<MultPrizeInfo> {

    private String type;

    private String prizeCode;

    private String prizeId;

    private String prizeTitle;

    private String prizeImg;

    private String prizeName;

    private String acceptUrl;

    private Integer status;

    private String amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @Override
    public int compareTo(MultPrizeInfo o) {
        if (o.getCreateTime().before(this.createTime)) {
            return -1;
        }

        return 1;
    }
}
