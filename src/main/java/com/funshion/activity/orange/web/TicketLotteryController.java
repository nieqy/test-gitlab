package com.funshion.activity.orange.web;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.orange.req.GainTicketReq;
import com.funshion.activity.orange.req.PrizeDetailReq;
import com.funshion.activity.orange.req.PrizeInfoReq;
import com.funshion.activity.orange.req.UploadAddressReq;
import com.funshion.activity.orange.service.WechatLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/orange/wechatLottery")
public class TicketLotteryController {

    @Autowired
    private WechatLotteryService wechatLotteryService;

    @RequestMapping("/login")
    public Result login(String code, String ed, String iv, String app_code) {
        return wechatLotteryService.login(code, ed, iv, app_code);
    }

    @RequestMapping("/infos")
    public Result infos(@Valid PrizeInfoReq req) {
        return wechatLotteryService.getPrizeInfos(req);
    }

    @RequestMapping("/getPrizeDetails")
    public Result getPrizeDetails(@Valid PrizeDetailReq req) {
        return wechatLotteryService.getPrizeDetails(req);
    }

    @RequestMapping("/gainTickets")
    public Result gainTickets(@Valid GainTicketReq req) {
        return wechatLotteryService.gainTickets(req);
    }

    @RequestMapping("/uploadAddress")
    public Result uploadAddress(@Valid UploadAddressReq req) {
        return wechatLotteryService.uploadAddress(req);
    }

    @RequestMapping("/history")
    public Result history(@Valid PrizeInfoReq req) {
        return wechatLotteryService.history(req);
    }

    @RequestMapping("/winList")
    public Result winList(@Valid PrizeInfoReq req) {
        return wechatLotteryService.getWinList(req);
    }

}
