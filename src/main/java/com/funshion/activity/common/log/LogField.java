package com.funshion.activity.common.log;

public class LogField {

	/**
	 * 日志级别
	 */
	private String level;

	/**
	 * 服务名
	 */
	private String serviceName;

	/**
	 * 接口名
	 */
	private String actionName;

	/**
	 * url
	 */
	private String url;

	/**
	 * 请求
	 */
	private String request;

	/**
	 * 响应
	 */
	private String response;

	/**
	 * 手机号码
	 */
	// private String phone;

	/**
	 * 消耗时间
	 */
	private String costTime;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getCostTime() {
		return costTime;
	}

	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String toLog() {
		StringBuilder sb = new StringBuilder();
		sb.append(level).append("|").append(actionName).append("|").append(serviceName).append("|").append(url)
				.append("|").append(request).append("|").append(response).append("|")
				.append(costTime);
		return sb.toString();
	}



}
