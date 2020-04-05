package com.funshion.activity.funtvactivity.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author xiaowei
 * @ClassName:FuntvActivityPrototype
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2018 2018年12月12日 下午1:47:00
 * @see
 * @since Ver 1.1
 */
public class FuntvPrototype {

	private Integer id;

	private String mac;

	private int status;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
