package com.funshion.activity.common.utils;

import org.apache.commons.lang3.StringUtils;


public class ImgUtils {

    private final static String img_url = "//img.funshion.com/sdw?oid=";

    public static String getImgPath(String oid) {
        if (StringUtils.isNotBlank(oid)) {
            if (oid.contains("img.funshion.com")) {
                return oid;
            } else {
                return img_url + oid + "&w=0&h=0";
            }
        }
        return oid;
    }

    public static String getImgUrl(String oid) {
        if (StringUtils.isNotBlank(oid)) {
            if (oid.contains(".com")) {
                return oid;
            } else {
                return "https:" + img_url + oid + "&w=0&h=0";
            }
        }
        return "";
    }
}
