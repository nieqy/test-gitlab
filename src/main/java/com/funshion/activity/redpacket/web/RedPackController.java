package com.funshion.activity.redpacket.web;

import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.req.*;
import com.funshion.activity.redpacket.rsp.GainRedPackRsp;
import com.funshion.activity.redpacket.service.WechatRedPackService;
import com.funshion.activity.redpacket.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/redpack")
public class RedPackController {

    @Autowired
    private WechatRedPackService wechatRedPackService;

    @RequestMapping("/accept")
    public Result<?> acceptRedPack(@Valid AcceptWechatRedPackReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + req.getOpenId() + req.getRedPackId() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return wechatRedPackService.sendRedPack(req);
    }

    @RequestMapping("/gain")
    public Result<?> gainRedPack(@Valid GainWechatRedPackReq req, HttpServletRequest servletRequest) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        // 获取IP
        req.setIp(IPUtils.getIpAddr(servletRequest));
        return wechatRedPackService.gainRedPack(req);
    }

    @RequestMapping("/gain/v2")
    public Result<?> gainRedPackV2(@Valid GainWechatRedPackReq req, HttpServletRequest servletRequest) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        // 获取IP
        req.setIp(IPUtils.getIpAddr(servletRequest));
        Result<?> result = wechatRedPackService.gainRedPack(req);
        // 未中奖
        if ("915".equals(result.getRetCode())) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("prizeType", JinliConstants.PrizeType.NOTHING);
            dataMap.put("prizeImg", LoadDataConfig.getDataValueByKey(String.format("funtv.activity.%d.noPrizeImg", req.getActivityType()), RedPackConstant.Img.NO_PRIZE_IMG));
            dataMap.put("prizeUrl", "");
            dataMap.put("amount", "");
            dataMap.put("prizeName", "");
            return Result.getSuccessResult(dataMap);
        } else if ("916".equals(result.getRetCode())) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("prizeType", JinliConstants.PrizeType.NOTHING);
            dataMap.put("prizeImg", LoadDataConfig.getDataValueByKey(String.format("funtv.activity.%d.outOfStockImg", req.getActivityType()), RedPackConstant.Img.OUT_OF_STOCK_IMG));
            dataMap.put("prizeUrl", "");
            dataMap.put("amount", "");
            dataMap.put("prizeName", "");
            return Result.getSuccessResult(dataMap);
        }

        if ("200".equals(result.getRetCode())) {
            GainRedPackRsp res = (GainRedPackRsp) result.getData();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("prizeType", JinliConstants.PrizeType.RED_PACKET);
            dataMap.put("prizeImg", "");
            dataMap.put("prizeUrl", res.getUrl());
            dataMap.put("amount", res.getAmount());
            dataMap.put("prizeName", "现金红包");
            return Result.getSuccessResult(dataMap);
        }

        return result;
    }


    @RequestMapping("/subWechat")
    public Result<?> subWechat(@Valid GetHbForSubWechatReq req, HttpServletRequest servletRequest) {
        if (!MD5Utils.getMD5String(req.getOpenId() + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        // 获取IP
        req.setIp(IPUtils.getIpAddr(servletRequest));

        return wechatRedPackService.subWechat(req);
    }

    @RequestMapping("/getRecords")
    public Result<?> getRecords(@Valid GetRedPackRecordsReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return wechatRedPackService.getRecords(req);
    }

    @RequestMapping("/getCoverInfo")
    public Result<?> getCoverInfo(@Valid GetCoverInfoReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getRedPackId() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        return wechatRedPackService.getCoverInfo(req);
    }

}
