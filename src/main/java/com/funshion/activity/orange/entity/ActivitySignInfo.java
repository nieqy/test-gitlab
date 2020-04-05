package com.funshion.activity.orange.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivitySignInfo implements Comparable<ActivitySignInfo> {

    private Integer id;

    private Integer signId;

    private String accountId;

    private LocalDate createTime;

    private LocalDate updateTime;

    private Integer isGive;

    private Integer total;

    @Override
    public int compareTo(ActivitySignInfo o) {
        // TODO Auto-generated method stub
        return createTime.compareTo(o.createTime);
    }


}
