package com.funshion.activity.shopping.service;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.shopping.domain.*;
import com.funshion.activity.shopping.mapper.CampaignConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CampaignsService {

    @Autowired
    private CampaignConfigMapper campaignConfigMapper;


    public Result<?> getCampaignsConfig(GetCampaignConfigReq req) {
        CampaignConfig config = campaignConfigMapper.getConfigInfo(req.getActivityType());
        if (config == null) {
            return Result.getSuccessResult(new Object());
        }

        GetCampaignConfigRsp rsp = new GetCampaignConfigRsp();
        // 首页基本信息配置
        rsp.setActivityName(config.getActivityName());
        rsp.setActivityType(config.getActivityType());
        rsp.setCnzzLogId(config.getCnzzLogId());
        rsp.setHomeBgColor(config.getHomeBgColor());
        rsp.setHomeHeaderImg(ImgUtils.getImgUrl(config.getHomeHeaderImg()));
        rsp.setHomeFooterImg(ImgUtils.getImgUrl(config.getHomeFooterImg()));
        rsp.setHomeCImg(ImgUtils.getImgUrl(config.getHomeCImg()));

        // 楼层配置
        FloorConfig floorConfig = new FloorConfig();
        floorConfig.setCardBgColor(config.getFCardBgColor());
        floorConfig.setNameTextColor(config.getFNameTextColor());
        floorConfig.setPrimeCostTextColor(config.getFPrimeCostTextColor());
        floorConfig.setSeckillTextColor(config.getFSeckillTextColor());
        floorConfig.setSelloutImg(ImgUtils.getImgUrl(config.getFSelloutImg()));
        floorConfig.setSeckillBtnImg(ImgUtils.getImgUrl(config.getFSeckillBtnImg()));

        // 专场页面配置
        ListPageConfig listPageConfig = new ListPageConfig();
        listPageConfig.setCardBgColor(config.getLCardBgColor());
        listPageConfig.setNameTextColor(config.getLNameTextColor());
        listPageConfig.setPriceTextColor(config.getLPriceTextColor());
        listPageConfig.setBkHomeBtnImg(ImgUtils.getImgUrl(config.getLBkHomeBtnImg()));

        // 商品二维码弹框样式配置
        DialogConfig dialogConfig = new DialogConfig();
        dialogConfig.setBgColor(config.getDBgColor());
        dialogConfig.setDialogBgColor(config.getDDialogBgColor());
        dialogConfig.setMiniIconImg(ImgUtils.getImgUrl(config.getDMiniIconImg()));
        dialogConfig.setShopIconImg(ImgUtils.getImgUrl(config.getDShopIconImg()));
        dialogConfig.setTextColor(config.getDTextColor());
        dialogConfig.setNameTextColor(config.getDNameTextColor());
        dialogConfig.setNameTextBgColor(config.getDNameTextBgColor());
        dialogConfig.setSelloutImg(ImgUtils.getImgUrl(config.getDSelloutImg()));
        dialogConfig.setCardBgColor(config.getDCardBgColor());

        rsp.setFloorConfig(floorConfig);
        rsp.setDialogConfig(dialogConfig);
        rsp.setListPageConfig(listPageConfig);

        return Result.getSuccessResult(rsp);

    }
}
