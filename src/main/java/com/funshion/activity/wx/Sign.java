package com.funshion.activity.wx;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.utils.HttpClientUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Sign {

    private final static String APP_ID = "wx084599372c8a56b4";

    private final static String APP_SECRET = "a23d4cae3b4a8eabe4466d27c70f7d88";

    private final static String TICKET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static String ticketToken;

    private static long lastFetchTime = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(getTicketToken());
    }


    public static String getTicketToken() throws Exception {
        if (new Date().getTime() - lastFetchTime < 1000 * 60 * 90 && ticketToken != null && !ticketToken.trim().equals("")) {
            return ticketToken;
        }
        String accessTokenUrl = String.format(ACCESS_TOKEN_URL, APP_ID, APP_SECRET);
        String accessToken = JSON.parseObject(HttpClientUtils.get(accessTokenUrl)).getString("access_token");
        String ticketTokenUrl = String.format(TICKET_TOKEN_URL, accessToken);
        ticketToken = JSON.parseObject(HttpClientUtils.get(ticketTokenUrl)).getString("ticket");
        lastFetchTime = new Date().getTime();
        return ticketToken;
    }


    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", APP_ID);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
