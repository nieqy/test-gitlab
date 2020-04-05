//package com.funshion.activity.redpacket.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.funshion.activity.redpacket.entity.GiveRedPacketRsp;
//import com.funshion.activity.redpacket.entity.RedPacketDetail;
//import com.funshion.activity.redpacket.entity.RedPacketProbability;
//import com.funshion.activity.redpacket.utils.MapOpHelper;
//import com.funshion.common.utils.Base64Util;
//import com.funshion.common.utils.MD5Util;
//import com.funshion.common.utils.httpClient.HttpClientUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.net.URLDecoder;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class RedPacketService {
//
//    private GiveRedPacketRsp giveRedPacket(String openid, String pCode, BigDecimal amount) {
//        try {
//
//            Map<String, String> paramMap = new HashMap<String, String>();
//            paramMap.put("nonce_str", "123");
//            paramMap.put("mch_billno", pCode);
//
//            paramMap.put("mch_id", "123");
//
//            paramMap.put("partner_id", "");
//            paramMap.put("ctime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            paramMap.put("openid", openid);
//            paramMap.put("pcode", pCode);
//            paramMap.put("money", amount.toString());
//            // paramMap.put("ip", "172.17.5.112");
//            paramMap.put("payment_id", "123");
//            paramMap.put("wishing", "恭喜发财，大吉大利");
//            paramMap.put("act_name", "抽奖送红包");
//            paramMap.put("send_name", "风行电视");
//            paramMap.put("remark", "希望您的生活一切顺利");
//            // 进行排序
//            StringBuffer sb = new StringBuffer();
//            for (Map.Entry<String, String> entry : MapOpHelper.sortMapByKey(paramMap).entrySet()) {
//                sb.append("&").append(entry.getKey()).append("=");
//                sb.append(URLDecoder.decode((entry.getValue()), "UTF-8"));
//            }
//            String toSignStr = sb.toString().substring(1);
//
//            String sign = MD5Util.getMD5String(toSignStr + "&key=" + Base64Util.decode(""));
//
//            StringBuffer sendSB = new StringBuffer();
//            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//                sendSB.append("&").append(entry.getKey()).append("=");
//                sendSB.append(entry.getValue());
//            }
//            String sbb = handleSpeChar(sendSB.toString());
//            String res =
//                    HttpClientUtils.get(
//                            "http://ppay.funshion.com/api/weixin/giveRedPacket?sign=" + sign + sbb);
//            return JSONObject.parseObject(res, GiveRedPacketRsp.class);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    public RedPacketProbability getPacketAmount(List<RedPacketDetail> details) {
//        List<RedPacketProbability> probabilitys = new ArrayList<RedPacketProbability>();
//        BigDecimal sum = new BigDecimal(0.00);
//        for (RedPacketDetail detail : details) {
//            if (detail.getStatus() == 2 && detail.getStock() > 0) {
//                RedPacketProbability probability = new RedPacketProbability();
//                probability.setStart(sum);
//                probability.setEnd(sum.add(detail.getProbability()));
//                sum = sum.add(detail.getProbability());
//                BeanUtils.copyProperties(detail, probability);
//                probabilitys.add(probability);
//            }
//        }
//        BigDecimal randNum = BigDecimal.valueOf(Math.random()).multiply(sum);
//
//        for (RedPacketProbability probability : probabilitys) {
//            if (randNum.compareTo(probability.getStart()) >= 0
//                    && randNum.compareTo(probability.getEnd()) < 0) {
//                return probability;
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 根据本地订单号生规则生成本地充值订单号 订单生成规则为yyyyMMddHHmmss-accountId(10位，不够前边补0)
//     */
//    private String getLocalPayCode(Integer accountId) {
//        if (accountId == null) {
//            accountId = 0;
//        }
//        return new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date())
//                + String.format("%010d", accountId);
//    }
//
//    private static String handleSpeChar(String sbb) {
//        sbb = sbb.replaceAll(" ", "%20");
//        sbb = sbb.replaceAll("\"", "%22");
//        sbb = sbb.replaceAll("#", "%23");
//        sbb = sbb.replaceAll("\\(", "%28");
//        sbb = sbb.replaceAll("\\)", "%29");
//        sbb = sbb.replaceAll(",", "%2C");
//        sbb = sbb.replaceAll("/", "%2F");
//        sbb = sbb.replaceAll(":", "%3A");
//        sbb = sbb.replaceAll(";", "%3B");
//        sbb = sbb.replaceAll("<", "%3C");
//        sbb = sbb.replaceAll(">", "%3E");
//        sbb = sbb.replaceAll("@", "%40");
//        sbb = sbb.replaceAll("\\\\", "%5C");
//        sbb = sbb.replaceAll("\\|", "%7C");
//        return sbb;
//    }
//}
