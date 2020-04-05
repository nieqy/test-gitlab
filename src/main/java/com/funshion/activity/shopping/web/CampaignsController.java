package com.funshion.activity.shopping.web;


import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.shopping.domain.GetCampaignConfigReq;
import com.funshion.activity.shopping.service.CampaignsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/shopping/campaigns")
public class CampaignsController {

    @Autowired
    private CampaignsService campaignService;

    @RequestMapping("/config")
    public Result<?> getCampaignsConfig(@Valid GetCampaignConfigReq req) {
        if (!MD5Utils.getMD5String(req.getAccount_id() + req.getActivityType() + req.getRandom()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        return campaignService.getCampaignsConfig(req);
    }
}
