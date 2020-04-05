package com.funshion.activity.orange.entity;

import java.time.LocalDate;
import java.util.Date;

public class AppSignInfo implements Comparable<AppSignInfo> {

    private Integer id;

    private Integer signId;

    private String accountId;

    private Date createTime;

    private LocalDate updateTime;

    private Integer isGive;

    private Integer total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSignId() {
        return signId;
    }

    public void setSignId(Integer signId) {
        this.signId = signId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsGive() {
        return isGive;
    }

    public void setIsGive(Integer isGive) {
        this.isGive = isGive;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public LocalDate getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(AppSignInfo o) {
        // TODO Auto-generated method stub
        return createTime.compareTo(o.createTime);
    }


}
