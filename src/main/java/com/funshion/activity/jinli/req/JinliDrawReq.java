package com.funshion.activity.jinli.req;


public class JinliDrawReq {
	private Integer tvId;

	private String version;

	private String mac;

	private String ctime;

	private String sign;

	private Integer lotteryId;

	private Integer playDays;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Integer lotteryId) {
		this.lotteryId = lotteryId;
	}

	public Integer getTvId() {
		return tvId;
	}

	public void setTvId(Integer tvId) {
		this.tvId = tvId;
	}

	public Integer getPlayDays() {
		return playDays;
	}

	public void setPlayDays(Integer playDays) {
		this.playDays = playDays;
	}
}
