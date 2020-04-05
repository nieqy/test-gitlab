package com.funshion.activity.funtvactivity.service;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.funtvactivity.dto.FuntvActivityQuizConfigResponse;
import com.funshion.activity.funtvactivity.entity.*;
import com.funshion.activity.funtvactivity.mapper.FuntvActivityFirstPageConfigMapper;
import com.funshion.activity.funtvactivity.mapper.FuntvActivityQuizConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class FuntvActivityQuizConfigService {

    @Autowired
    private FuntvActivityQuizConfigMapper funtvActivityQuizConfigMapper;

    @Autowired
    private FuntvActivityFirstPageConfigMapper funtvActivityFirstPageConfigMapper;

    public Result<?> funtvActivityQuizConfig(int activityType) {
        FuntvActivityQuizConfigResponse res = new FuntvActivityQuizConfigResponse();
        FuntvActivityFirstPageInfo firstPage = funtvActivityFirstPageConfigMapper.findActivity1(activityType);
        if (firstPage == null) {
            return Result.getFailureResult("401", "没有此活动~");
        }
        res.setActivityType(activityType);
        res.setActivityName(firstPage.getActivityName());
        FuntvActivityQuizConfigInfo config = funtvActivityQuizConfigMapper.findByActivityType(activityType);
        if (config == null) {
            return Result.getFailureResult("402", "没有此活动~");
        }
        res.setBgColor(config.getBgColor());
        res.setCnzzLogId(config.getCnzzLogId());
        res.setHeaderImg(ImgUtils.getImgPath(config.getHeaderImg()));
        res.setFooterImg(ImgUtils.getImgPath(config.getFooterImg()));
        res.setSubmitBtnImg(ImgUtils.getImgPath(config.getSubmitBtnImg()));
        FuntvActivityQuizHeaderConfigInfo headerConfig = null;
        if (config.getHeaderConfigId() != null) {
            headerConfig = funtvActivityQuizConfigMapper.findHeaderConfig(config.getHeaderConfigId());
        }
        if (headerConfig != null) {
            headerConfig.setAnswerImg(ImgUtils.getImgPath(headerConfig.getAnswerImg()));
            headerConfig.setPrizeListImg(ImgUtils.getImgPath(headerConfig.getPrizeListImg()));
            headerConfig.setRuleImg(ImgUtils.getImgPath(headerConfig.getRuleImg()));
            //headerConfig.setVideoImg(ImgUtils.getImgPath(headerConfig.getVideoImg()));
            res.setHeaderConfig(headerConfig);
        }
        FuntvActivityQuizQuestionConfigInfo questionConfig = null;
        if (config.getQuestionConfigId() != null) {
            questionConfig = funtvActivityQuizConfigMapper.findQuestionConfig(config.getQuestionConfigId());
        }
        if (questionConfig != null) {
            String indexImgs = questionConfig.getIndexImgs();
            if (StringUtils.isNotBlank(indexImgs)) {
                String[] imgs = indexImgs.split(",");
                List<String> indexImages = new ArrayList<>();
                for (String img : imgs) {
                    indexImages.add(ImgUtils.getImgPath(img));
                }
                questionConfig.setIndexImages(indexImages);
            }
            questionConfig.setAnswerTipBtnImg(ImgUtils.getImgPath(questionConfig.getAnswerTipBtnImg()));
            questionConfig.setAnswerRightImg(ImgUtils.getImgPath(questionConfig.getAnswerRightImg()));
            questionConfig.setAnswerWrongImg(ImgUtils.getImgPath(questionConfig.getAnswerWrongImg()));
            res.setQuestionConfig(questionConfig);
        }
        FuntvActivityQuizDialogConfigInfo dialogConfig = null;
        if (config.getDialogConfigId() != null) {
            dialogConfig = funtvActivityQuizConfigMapper.findDialogConfig(config.getDialogConfigId());
        }
        if (dialogConfig != null) {
            dialogConfig.setBgImg(ImgUtils.getImgPath(dialogConfig.getBgImg()));
            dialogConfig.setSubmitBtnImg(ImgUtils.getImgPath(dialogConfig.getSubmitBtnImg()));
            res.setDialogConfig(dialogConfig);
        }
        FuntvActivityQuizRulePageInfo rulePage = null;
        if (config.getRulePageId() != null) {
            rulePage = funtvActivityQuizConfigMapper.findRulePage(config.getRulePageId());
        }
        if (rulePage != null) {
            String bgimgs = rulePage.getBgImg();
            if (StringUtils.isNotBlank(bgimgs)) {
                String[] imgs = bgimgs.split(",");
                List<String> bgImgs = new ArrayList<>();
                for (String img : imgs) {
                    bgImgs.add(ImgUtils.getImgPath(img));
                }
                rulePage.setBgImgs(bgImgs);
            }
            res.setRulePage(rulePage);
        }
        FuntvActivityQuizAnswerPageInfo answerPage = null;
        if (config.getAnswerPageId() != null) {
            answerPage = funtvActivityQuizConfigMapper.findAnswerPage(config.getAnswerPageId());
        }
        if (answerPage != null) {
            answerPage.setBgImg(ImgUtils.getImgPath(answerPage.getBgImg()));
            answerPage.setPrizeBtn(ImgUtils.getImgPath(answerPage.getPrizeBtn()));
            res.setAnswerPage(answerPage);
        }
        FuntvActivityQuizPrizePageInfo prizePage = null;
        if (config.getPrizePageId() != null) {
            prizePage = funtvActivityQuizConfigMapper.findPrizePage(config.getPrizePageId());
        }
        if (prizePage != null) {
            prizePage.setBgImg(ImgUtils.getImgPath(prizePage.getBgImg()));
            res.setPrizePage(prizePage);
        }
        return Result.getSuccessResult(res);
    }

}
