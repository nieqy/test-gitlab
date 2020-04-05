package com.funshion.activity.pullalive.controller;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.pullalive.domain.PullAliveReq;
import com.funshion.activity.pullalive.domain.SignTemplateReq;
import com.funshion.activity.pullalive.service.PullAliveService;
import com.funshion.activity.pullalive.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/activity/pullalive")
public class PullAliveController {

    @Autowired
    private PullAliveService pullAliveService;

    @Autowired
    private SignService signService;

    @RequestMapping("/template1")
    public Result template1Launcher(@Valid PullAliveReq req) {
        return pullAliveService.getTemplate1Launcher(req);
    }

    @RequestMapping("/signLauncher")
    public Result signTemplateLauncher(@Valid SignTemplateReq req) {
        return signService.getSignTemplateLauncher(req);
    }

    @RequestMapping("/vipLotteryPage")
    public Result vipLotteryPage(@Valid SignTemplateReq req) {
        return signService.getVipLotteryPage(req);
    }

    @RequestMapping("/sign")
    public Result sign(@Valid SignTemplateReq req) {
        return signService.sign(req);
    }

    @RequestMapping("/getWinList")
    public Result getWinList(@Valid SignTemplateReq req) {
        return signService.getMyPrizes(req);
    }

    @RequestMapping("/travelPlay")
    public Result travelPlayLauncher(@Valid SignTemplateReq req) {
        return signService.getTravelPlayLauncher(req);
    }
}
