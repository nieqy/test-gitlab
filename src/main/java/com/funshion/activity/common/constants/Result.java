package com.funshion.activity.common.constants;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结果代码
     */
    private String retCode;
    /**
     * 结果信息
     */
    private String retMsg;
    /**
     * 数据载体
     */
    private T data;

    public final static Result HTTP_PARAMS_NOT_ENOUGH = Result.getFailureResult("502", "传入参数不全");

    public final static Result HTTP_RESPONSE_UNAUTHORIZED = Result.getFailureResult("501", "数字签名验证失败");

    public final static Result HTTP_REQUEST_FAILED = Result.getFailureResult("401", "操作失败，请重试");

    public final static Result SYSTEM_ERROR = Result.getFailureResult("400", "网络异常，请重试");


    /**
     * 获取成功返回结果
     *
     * @param data
     * @return
     */
    public static <T> Result getSuccessResult(T data) {
        return getResult("200", "ok", data);
    }

    public static <T> Result getSuccessResult(String resultCode, String resultMessage, T data) {
        return getResult(resultCode, resultMessage, data);
    }

    public static Result getSuccessResult(String resultCode, String resultMessage) {
        return getResult(resultCode, resultMessage, null);
    }

    /**
     * 获取成功返回结果
     *
     * @return
     */
    public static Result getSuccessResult() {
        return getResult("200", "ok", null);
    }

    /**
     * 获取成功返回结果
     *
     * @return
     */
    public static Result getSuccessMessageResult(String message) {
        return getResult("200", message, null);
    }

    /**
     * 描述:获取失败返回结果
     *
     * @param code 错误码
     * @return
     * @author zuokeli
     */
    public static Result getFailureResult(String code) {
        return getResult(code, null, null);
    }

    /**
     * 描述:获取失败返回结果
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return
     * @author zuokeli
     */
    public static Result getFailureResult(String code, String msg) {
        return getResult(code, msg, new HashMap<String, String>());
    }

    /**
     * 描述:获取失败返回结果
     *
     * @param code 错误码
     * @param msg  错误信息
     * @param data 数据信息（可以保留失败时的交易快照）
     * @return
     * @author zuokeli
     */
    public static <T> Result getFailureResult(String code, String msg, T data) {
        return getResult(code, msg, data);
    }

    @SuppressWarnings("unchecked")
    private static <T> Result getResult(String code, String msg, T data) {
        Result result = new Result();
        result.setRetCode(code);
        result.setRetMsg(msg);
        result.setData(data);
        return result;
    }

    public static boolean isSuccessResult(Result<?> result) {
        if ("200".equals(result.getRetCode())) {
            return true;
        }
        return false;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "retCode='" + retCode + '\'' +
                ", reMsg='" + retMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
