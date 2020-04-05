/**
 *
 */
package com.funshion.activity.common.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AESUtils {

    /**
     * 加密 
     *
     * @param content 需要加密的内容 
     * @param password  加密密码
     * @return
     * @throws NoSuchPaddingException
     */
    public static byte[] encrypt(String content, String password) throws Exception {
        //android
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//algorithmStr
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**解密 
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return
     * @throws NoSuchPaddingException
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception {
        //android
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//algorithmStr
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(content);
        return result;
    }


    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static byte[] encrypt2(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(content.getBytes("utf-8")); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt3(String content, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        String encode = new String(Base64Utils.encode(cipher.doFinal(content.getBytes("utf-8"))));
        return encode;
    }

    public static String decrypt3(String content, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//algorithmStr
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(Base64Utils.decodeByte(content));
        String decode = new String(result, "utf-8");
        return decode;
    }

    public static void main(String[] args) throws Exception {
        String s = encrypt3("订单已支付成功，请勿重复支付！", "1234567890abcdef");
        System.out.println(s);
        String s1 = decrypt3(s, "1234567890abcdef");
        System.out.println(s1);
        System.out.println(parseByte2HexStr(encrypt("订单已支付成功，请勿重复支付！", "123l567x90ab7def")));
        System.out.println(new String(decrypt(parseHexStr2Byte("95B012B0BC898E5C37EEED6588635F0902DEC5652C0215F93AF69F009F0F6D58"), "1234567890abcdef")));
    }
}
