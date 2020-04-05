package com.funshion.activity.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author louyj
 * @CreateDate: [2015年6月10日 下午3:18:54]
 * @Description: MD5数字签名静态util类
 */
public class MD5Utils {

    private static char md5Chars[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final ThreadLocal<MessageDigest> messageDigestCache = new ThreadLocal<MessageDigest>();

    public static MessageDigest getInstance() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = messageDigestCache.get();
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigestCache.set(messageDigest);
        }
        return messageDigest;
    }

    /**
     * 获得字符串的数字签名摘要
     *
     * @param source
     * @return
     */
    public static String getMD5String(String source) {
        try {
            MessageDigest messageDigest = getInstance();
            messageDigest.update(source.getBytes("UTF-8"));
            return bufferToHex(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得字符串的数字签名摘要
     *
     * @param source
     * @return
     */
    public static String getStringMD5String(String source) {
        return getMD5String(source);
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = md5Chars[(bt & 0xf0) >> 4];
        char c1 = md5Chars[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getBase16String(byte[] byteArray) {
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    public static String getMD5Hex(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        return getBase16String(byteArray);
    }
}
