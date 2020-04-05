package com.funshion.activity.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.redis.RedisService;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SendPhoneCodeUtils {

    private static RedisService redisService;

    @Autowired(required = true)
    public void setJedisService(RedisService redisService) {
        SendPhoneCodeUtils.redisService = redisService;
    }

    private final static String SEND_CODE_URL = "http://psms.funshion.com/v1/sms/sendwithsign";

    private final static String SEND_CODE_REDIS_KEY = "mercury_redis_phone_code_";

    private static UrlEncodedFormEntity buildParameter(String mobile, String msg, String cl, String key) throws IOException {
        Date date = new Date();
        String ctime = date.getTime() / 1000 + "";
        List<NameValuePair> ps = new ArrayList<NameValuePair>();
        ps.add(new BasicNameValuePair("cl", cl));
        ps.add(new BasicNameValuePair("ctime", ctime));
        ps.add(new BasicNameValuePair("mobile", mobile));
        ps.add(new BasicNameValuePair("msg", msg));
        ps.add(new BasicNameValuePair("sign", MD5Utils.getStringMD5String(mobile + msg + ctime + cl + key)));
        return new UrlEncodedFormEntity(ps, "utf-8");
    }

    public static String sendPlainMsg(String phone, String msg, String cl, String key) throws Exception {
        String response = HttpUtils.create().setUrl(SEND_CODE_URL).setMethod(RequestMethod.POST).setHttpEntity(buildParameter(phone, msg, cl, key)).execute();
        return response;
    }

    public static String sendMsg(String phone, String msg, String cl, String key) throws Exception {
        msg = getMsg(phone, msg);
        String response = HttpUtils.create().setUrl(SEND_CODE_URL).setMethod(RequestMethod.POST).setHttpEntity(buildParameter(phone, msg, cl, key)).execute();
        return response;
    }

    private final static List<String> SEND_CODE_EXCEPTION_RET = new ArrayList<String>();

    static {
        SEND_CODE_EXCEPTION_RET.add("2005");
        SEND_CODE_EXCEPTION_RET.add("2006");
    }

    public static String sendMsg1(String phone, String msg, String cl, String key) throws Exception {
        String code = createRandomCode(true, 6);
        msg = msg.replaceAll("%verifycode%", code);
        String response = HttpUtils.create().setUrl(SEND_CODE_URL).setMethod(RequestMethod.POST).setHttpEntity(buildParameter(phone, msg, cl, key)).execute();
        JSONObject json = JSON.parseObject(response);
        if (SEND_CODE_EXCEPTION_RET.contains(json.getString("retcode"))) {
            return response;
        } else {
            redisService.setForTimeCustom(SEND_CODE_REDIS_KEY + phone, code, 30, TimeUnit.MINUTES);
        }
        return response;
    }

    public static boolean verifyNotDelCode(String phone, String code) {
        String saveCode = (String) redisService.get(SEND_CODE_REDIS_KEY + phone);
        if (saveCode != null && saveCode.equals(code)) {
            return true;
        }
        return false;
    }

    public static boolean verify(String phone, String code) {
        String saveCode = (String) redisService.get(SEND_CODE_REDIS_KEY + phone);
        if (saveCode != null && saveCode.equals(code)) {
            delete(phone);
            return true;
        }
        return false;
    }

    public static String getCode(String phone) {
        String saveCode = (String) redisService.get(SEND_CODE_REDIS_KEY + phone);
        return saveCode;
    }

    public static void delete(String phone) {
        redisService.remove(SEND_CODE_REDIS_KEY + phone);
    }

    /**
     * 随机生成手机、图片验证码
     * 支持全数字、数字加字幕验证码生成 （字母排除了o）
     *
     * @param numberFlag 验证码格式选择
     * @param length     验证码长度选择
     */
    private static String createRandomCode(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    private static String getMsg(String phone, String msg) {
        String code = createRandomCode(true, 6);
        redisService.setForTimeCustom(SEND_CODE_REDIS_KEY + phone, code, 30, TimeUnit.MINUTES);
        return msg.replaceAll("%verifycode%", code);
    }

}
