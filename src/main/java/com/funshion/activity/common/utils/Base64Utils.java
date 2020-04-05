package com.funshion.activity.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;

import java.io.UnsupportedEncodingException;


public class Base64Utils {


    /**
     * 将二进制数据编码为BASE64字符串
     *
     * @param original
     * @return
     */
    public static String encode(String original) {
        try {
            if (StringUtils.isBlank(original)) {
                return null;
            }
            return Base64.encodeBase64String(original.getBytes(Consts.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 将BASE64字符串恢复为二进制数据
     *
     * @param base64
     * @return
     */
    public static String decode(String base64) {
        try {
            if (StringUtils.isBlank(base64)) {
                return null;
            }
            return new String(Base64.decodeBase64(base64.getBytes(Consts.UTF_8.name())));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decodeByte(String base64) throws Exception {
        return Base64.decodeBase64(base64.getBytes(Consts.UTF_8.name()));
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return Base64.encodeBase64String(bytes);
    }
}
