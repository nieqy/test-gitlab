package com.funshion.activity.common.log;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class InterfaceLogManager {
	private static final Logger LOG = LoggerFactory.getLogger("interface");

	public static void info(LogField field) {
		field.setLevel(LogConstant.INFO);
		LOG.info(field.toLog());
	}

	public static void debug(LogField field) {
		field.setLevel(LogConstant.DEBUG);
		LOG.debug(field.toLog());
	}

	public static void warn(LogField field) {
		field.setLevel(LogConstant.WARN);
		LOG.warn(field.toLog());
	}

	public static void error(LogField field) {
		field.setLevel(LogConstant.ERROR);
		LOG.error(field.toLog());
	}

	public static void info(String actionName, String url, String request, String response,
			long costTime) {
		LogField field = new LogField();
		field.setActionName(actionName);
		field.setServiceName(LogConstant.SERVICE_NAME);
		field.setRequest(request);
		field.setResponse(response);
		field.setUrl(url);
		field.setCostTime(String.valueOf(costTime));
		field.setLevel(LogConstant.INFO);
		LOG.info(field.toLog());
	}

	public static void debug(String actionName, String url, String request, String response,
			long costTime) {
		LogField field = new LogField();
		field.setActionName(actionName);
		field.setServiceName(LogConstant.SERVICE_NAME);
		field.setRequest(request);
		field.setResponse(response);
		field.setUrl(url);
		field.setCostTime(String.valueOf(costTime));
		field.setLevel(LogConstant.DEBUG);
		LOG.debug(field.toLog());
	}

	public static void warn(String actionName, String url, String request, String response,
			long costTime) {
		LogField field = new LogField();
		field.setActionName(actionName);
		field.setServiceName(LogConstant.SERVICE_NAME);
		field.setRequest(request);
		field.setResponse(response);
		field.setUrl(url);
		field.setCostTime(String.valueOf(costTime));
		field.setLevel(LogConstant.WARN);
		LOG.warn(field.toLog());
	}

	public static void error(String actionName, String url, String request, String response,
			long costTime) {
		LogField field = new LogField();
		field.setActionName(actionName);
		field.setServiceName(LogConstant.SERVICE_NAME);
		field.setRequest(request);
		field.setResponse(response);
		field.setUrl(url);
		field.setCostTime(String.valueOf(costTime));
		field.setLevel(LogConstant.ERROR);
		LOG.error(field.toLog());
	}

	public static void log(String level, String actionName, String url, String request, String response,
			long costTime) {

		if (LogConstant.INFO.equals(level)) {
			info(actionName, url, request, response, costTime);
		}
		else if (LogConstant.DEBUG.equals(level)) {
			debug(actionName, url, request, response, costTime);
		}
		else if (LogConstant.WARN.equals(level)) {
			warn(actionName, url, request, response, costTime);
		}
		else if (LogConstant.ERROR.equals(level)) {
			error(actionName, url, request, response, costTime);
		}

	}

	public static void logForSpecialInterface(HttpServletRequest request, long startTime, String res) {
		String uri = request.getRequestURI();
		// 接口名
		String actionName = "";
		if (uri.lastIndexOf("/") > -1) {
			actionName = uri.substring(uri.lastIndexOf("/") + 1);
		}
		String req = JSONObject.toJSONString(request.getParameterMap());
		if (StringUtils.isNotBlank(req) && req.length() > 1000) {
			req = req.substring(0, 1000);
		}
		// 记录接口日志
		InterfaceLogManager.log(LogConstant.INFO, actionName, request.getRequestURL().toString(),
				req, res, System.currentTimeMillis() - startTime);
	}
}
