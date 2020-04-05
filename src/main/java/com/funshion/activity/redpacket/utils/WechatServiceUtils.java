package com.funshion.activity.redpacket.utils;

import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.redpacket.domain.*;
import com.funshion.activity.redpacket.req.GetRedPackStatusReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WechatServiceUtils {
    private final static String WXAPPID = "wx6f08bbcf08c64921";

    private final static String MCHID = "1522844751";

    private final static String SIGN_KEY = "1mwtVidFxrqrm1peDXzgXV97ER5uxXTK";

    public static void main(String[] args) {
        //doGroupSend("oETFUwEcDqVqiFnCMEV4AM4XlBN8", "201905291200004001869", 100, 3);
        doSend("oETFUwEcDqVqiFnCMEV4AM4XlBN8", "20190724120000421869", 30);
        gethbinfo("20190529120000421869");
    }

    public static SendRedPackRsp doSend(String openid, String pCode, int amount) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            // 随机字符串
            paramMap.put("nonce_str", RandomStringUtils.randomAlphanumeric(20));
            // 商户订单号
            paramMap.put("mch_billno", pCode);
            // 微信支付分配的商户号
            paramMap.put("mch_id", MCHID);
            // 微信分配的公众账号ID
            paramMap.put("wxappid", WXAPPID);
            // 红包发送者名称
            paramMap.put("send_name", "风行橙子");
            // 接受红包的用户openid
            paramMap.put("re_openid", openid);
            // 付款金额，单位分
            paramMap.put("total_amount", amount + "");
            // 红包发放总人数
            paramMap.put("total_num", "1");
            paramMap.put("wishing", "恭喜您获得红包！");

            paramMap.put("client_ip", IPUtils.getIpAddress());
            // 活动名称
            paramMap.put("act_name", "抽奖送红包");
            paramMap.put("remark", "希望您的生活一切顺利");

            // 发放红包使用场景，红包金额大于200或者小于1元时必传
            // PRODUCT_1:商品促销;PRODUCT_2:抽奖;PRODUCT_3:虚拟物品兑奖;PRODUCT_4:企业内部福利
            // PRODUCT_5:渠道分润;PRODUCT_6:保险回馈;PRODUCT_7:彩票派奖;PRODUCT_8:税务刮奖
            paramMap.put("scene_id", "PRODUCT_2");

            // 进行排序
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : MapOpHelper.sortMapByKey(paramMap).entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(URLDecoder.decode((entry.getValue()), "UTF-8"));
            }
            String toSignStr = sb.toString().substring(1);

            String sign = MD5Utils.getMD5String(toSignStr + "&key=" + SIGN_KEY).toUpperCase();
            paramMap.put("sign", sign);
            SendRedPackReq req = JSONObject.parseObject(JSONObject.toJSONString(paramMap), SendRedPackReq.class);
            XmlUtils xmlUtil = new XmlUtils();
            xmlUtil.xstream().alias("xml", req.getClass());
            String xml = xmlUtil.xstream().toXML(req);

            String rsp = ssl("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack", xml);
            xmlUtil.xstream().alias("xml", SendRedPackRsp.class);
            xmlUtil.xstream().alias("hbinfo", HbInfo.class);
            SendRedPackRsp response = (SendRedPackRsp) xmlUtil.xstream().fromXML(rsp);
            return response;
        } catch (Exception e) {
            log.error("sendRedPack got exception!", e);
        }
        return null;
    }

    public static SendGroupRedPackRsp doGroupSend(String openid, String pCode, int amount, int totalNum) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            // 随机字符串
            paramMap.put("nonce_str", RandomStringUtils.randomAlphanumeric(20));
            // 商户订单号
            paramMap.put("mch_billno", pCode);
            // 微信支付分配的商户号
            paramMap.put("mch_id", MCHID);
            // 微信分配的公众账号ID
            paramMap.put("wxappid", WXAPPID);
            // 红包发送者名称
            paramMap.put("send_name", "风行橙子");
            // 接受红包的用户openid
            paramMap.put("re_openid", openid);
            // 付款金额，单位分
            paramMap.put("total_amount", amount + "");
            // 红包发放总人数
            paramMap.put("total_num", totalNum + "");
            // 红包金额设置方式
            paramMap.put("amt_type", "ALL_RAND");
            paramMap.put("wishing", "恭喜您获得红包！");

            paramMap.put("client_ip", IPUtils.getIpAddress());
            // 活动名称
            paramMap.put("act_name", "抽奖送红包");
            paramMap.put("remark", "希望您的生活一切顺利");

            // 发放红包使用场景，红包金额大于200或者小于1元时必传
            // PRODUCT_1:商品促销;PRODUCT_2:抽奖;PRODUCT_3:虚拟物品兑奖;PRODUCT_4:企业内部福利
            // PRODUCT_5:渠道分润;PRODUCT_6:保险回馈;PRODUCT_7:彩票派奖;PRODUCT_8:税务刮奖
            paramMap.put("scene_id", "PRODUCT_2");

            // 进行排序
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : MapOpHelper.sortMapByKey(paramMap).entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(URLDecoder.decode((entry.getValue()), "UTF-8"));
            }
            String toSignStr = sb.toString().substring(1);

            String sign = MD5Utils.getMD5String(toSignStr + "&key=" + SIGN_KEY).toUpperCase();
            paramMap.put("sign", sign);
            SendGroupRedPackReq req = JSONObject.parseObject(JSONObject.toJSONString(paramMap), SendGroupRedPackReq.class);
            XmlUtils xmlUtil = new XmlUtils();
            xmlUtil.xstream().alias("xml", req.getClass());
            String xml = xmlUtil.xstream().toXML(req);

            String rsp = ssl("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack", xml);
            xmlUtil.xstream().alias("xml", SendGroupRedPackRsp.class);
            xmlUtil.xstream().alias("hbinfo", HbInfo.class);
            SendGroupRedPackRsp response = (SendGroupRedPackRsp) xmlUtil.xstream().fromXML(rsp);
            return response;
        } catch (Exception e) {
            log.error("sendRedPack got exception!", e);
        }
        return null;
    }

    public static GetRedPackStatusRsp gethbinfo(String pCode) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            // 随机字符串
            paramMap.put("nonce_str", RandomStringUtils.randomAlphanumeric(20));
            // 商户订单号
            paramMap.put("mch_billno", pCode);
            // 微信支付分配的商户号
            paramMap.put("mch_id", MCHID);
            // 微信分配的公众账号ID
            paramMap.put("appid", WXAPPID);
            // MCHT:通过商户订单号获取红包信息。
            paramMap.put("bill_type", "MCHT");

            // 进行排序
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : MapOpHelper.sortMapByKey(paramMap).entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(URLDecoder.decode((entry.getValue()), "UTF-8"));
            }
            String toSignStr = sb.toString().substring(1);

            String sign = MD5Utils.getMD5String(toSignStr + "&key=" + SIGN_KEY).toUpperCase();
            paramMap.put("sign", sign);
            GetRedPackStatusReq req = JSONObject.parseObject(JSONObject.toJSONString(paramMap), GetRedPackStatusReq.class);
            XmlUtils xmlUtil = new XmlUtils();
            xmlUtil.xstream().alias("xml", req.getClass());
            String xml = xmlUtil.xstream().toXML(req);

            String rsp = ssl("https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo", xml);
            xmlUtil.xstream().alias("xml", GetRedPackStatusRsp.class);
            xmlUtil.xstream().aliasType("hbinfo", HbInfo.class);
            GetRedPackStatusRsp response = (GetRedPackStatusRsp) xmlUtil.xstream().fromXML(rsp);
            return response;
        } catch (Exception e) {
            log.error("gethbinfo got exception!", e);
        }
        return null;
    }

    /**
     * 发送请求
     */
    private static String ssl(String url, String data) {
        log.info("url->: " + url);
        log.info("req data->:" + data);
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读jar包根目录下的/certs/apiclient_cert.p12文件
            InputStream in = WechatServiceUtils.class.getClassLoader().getResourceAsStream("/certs/apiclient_cert.p12");
            if (in != null) {
                keyStore.load(in, MCHID.toCharArray());
            } else {
                // 非jar中读取
                File file = ResourceUtils.getFile("classpath:certs/apiclient_cert.p12");
                FileInputStream inStream = new FileInputStream(file);
                keyStore.load(inStream, MCHID.toCharArray());
            }

            //String certFilePath = WechatRedPackService.class.getResource("").getPath() + "/apiclient_cert.p12";

            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCHID.toCharArray()).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            HttpPost httpost = new HttpPost(url);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));

            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        log.info("rsp data->:" + message.toString());
        return message.toString();
    }

}
