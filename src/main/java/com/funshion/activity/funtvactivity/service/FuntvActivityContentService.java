package com.funshion.activity.funtvactivity.service;

import com.alibaba.fastjson.JSON;
import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.funtvactivity.common.FuntvActivityPrizeStatus;
import com.funshion.activity.funtvactivity.common.FuntvActivityPrizeType;
import com.funshion.activity.funtvactivity.dto.FuntvActivityAnswerSubmitRequest;
import com.funshion.activity.funtvactivity.dto.FuntvActivityAnswerSubmitRequest.SubmitAnswer;
import com.funshion.activity.funtvactivity.dto.FuntvActivityContentAnswerListResponse;
import com.funshion.activity.funtvactivity.dto.FuntvActivityContentAnswerListResponse.FuntvActivityContentPrizeInfo;
import com.funshion.activity.funtvactivity.dto.FuntvActivityContentResponse;
import com.funshion.activity.funtvactivity.dto.FuntvActivityPrizeListResponse;
import com.funshion.activity.funtvactivity.entity.*;
import com.funshion.activity.funtvactivity.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class FuntvActivityContentService {

    @Autowired
    private FuntvActivityContentConfigMapper funtvActivityContentConfigMapper;

    @Autowired
    private FuntvActivityContentDetailMapper funtvActivityContentDetailMapper;

    @Autowired
    private FuntvActivityContentAnswerMapper funtvActivityContentAnswerMapper;

    @Autowired
    private FuntvActivityFirstPageConfigMapper funtvActivityFirstPageConfigMapper;

    @Autowired
    private FuntvActivityMapper funtvActivityMapper;

    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;

    @Autowired
    private FuntvActivityPrizeRuleMapper funtvActivityPrizeRuleMapper;

    @Autowired
    private FmCommodityMapper fmCommodityMapper;

    private String getSpecialTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + c.get(Calendar.HOUR_OF_DAY) + "点";
    }

    private String getSpecialTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + c.get(Calendar.HOUR_OF_DAY) + "点";
    }

    private Date getSpecialDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        return d;
    }

    public Result<?> funtvActivityContent(int activityType, int accountId, String mac) {
        Date now = new Date();
        FuntvActivityContentResponse res = new FuntvActivityContentResponse();
        FuntvActivityFirstPageInfo firstPage = funtvActivityFirstPageConfigMapper.findActivity1(activityType);
        if (firstPage == null || firstPage.getId() == null || firstPage.getId() == 0) {
            return Result.getFailureResult("301", "数据不存在!");
        }
        FuntvActivityContentConfigInfo config = funtvActivityContentConfigMapper.findConfig1(activityType);
        if (config == null) {
            config = funtvActivityContentConfigMapper.findConfig(activityType);
        }
        if (config == null) {
            return Result.getFailureResult("401", "没有此活动~");
        }
        res.setStartTime(this.getSpecialTime(config.getStartTime()));
        res.setEndTime(this.getSpecialTime(config.getEndTime()));
        List<FuntvActivityContentAnswerInfo> answers = funtvActivityContentAnswerMapper.getAnswerInfo(activityType, accountId, null);
        if (answers != null && answers.size() > 0) {
            res.setIsAnswered(1);
        } else {
            res.setIsAnswered(0);
        }
        FuntvActivityAccontInfo accountInfo = funtvActivityMapper.findFuntvActivityAccount(null, accountId, mac, activityType);
        if (accountInfo != null && StringUtils.isNotBlank(accountInfo.getPhone())) {
            res.setNeedPhone(0);
        } else {
            res.setNeedPhone(1);
        }
        config.setHeaderVideoBtnImg(ImgUtils.getImgPath(config.getHeaderVideoBtnImg()));
        config.setBottomBannerImg1(ImgUtils.getImgPath(config.getBottomBannerImg1()));
        config.setBottomBannerImg2(ImgUtils.getImgPath(config.getBottomBannerImg2()));
        res.setConfig(config);
        List<FuntvActivityContentDetailInfo> contents = funtvActivityContentDetailMapper.findAll(config.getId());
        for (FuntvActivityContentDetailInfo content : contents) {
            if (res.getIsAnswered() == 0) {
                content.setSubjectResult(null);
            }
            content.setOptionImg1(ImgUtils.getImgPath(content.getOptionImg1()));
            content.setOptionImg2(ImgUtils.getImgPath(content.getOptionImg2()));
            content.setOptionImg3(ImgUtils.getImgPath(content.getOptionImg3()));
            content.setOptionImg4(ImgUtils.getImgPath(content.getOptionImg4()));
            content.setQuestionIcon(ImgUtils.getImgPath(content.getQuestionIcon()));
            FuntvActivityContentAnswerInfo answer = this.getUserAnswerInfo(answers, content);
            if (answer != null) {
                content.setUserAnswer(answer.getAnswer());
            }
        }
        res.setContentDetail(contents);
        if (res.getIsAnswered() == 0) {
            if (config.getStartTime().after(now)) {
                return Result.getSuccessResult("202", "答题尚未开始！", res);
            }
            if (config.getEndTime().before(now)) {
                return Result.getSuccessResult("203", "答题已结束！", res);
            }
            return Result.getSuccessResult(res);
        } else {
            return Result.getSuccessResult("201", "您已答过题目，请勿重复提交！", res);
        }
    }

    private FuntvActivityContentAnswerInfo getUserAnswerInfo(List<FuntvActivityContentAnswerInfo> answers, FuntvActivityContentDetailInfo contentDetail) {
        for (FuntvActivityContentAnswerInfo answer : answers) {
            if (answer.getContentDetailId().equals(contentDetail.getId())) {
                return answer;
            }
        }
        return null;
    }

    public Result<?> answerSubmit(FuntvActivityAnswerSubmitRequest request) {
        List<FuntvActivityAnswerSubmitRequest.SubmitAnswer> answers = JSON.parseArray(request.getAnswers(), FuntvActivityAnswerSubmitRequest.SubmitAnswer.class);
        if (answers == null || answers.size() == 0) {
            return Result.getFailureResult("302", "请提交答案!");
        }
        FuntvActivityContentConfigInfo contentInfo = funtvActivityContentConfigMapper.findById(request.getContentId());
        Date now = new Date();
        if (contentInfo == null) {
            return Result.getFailureResult("307", "活动已关闭!");
        }
        if (contentInfo.getStartTime().after(now)) {
            return Result.getFailureResult("305", "活动还没开始!");
        }
        if (contentInfo.getEndTime().before(now)) {
            return Result.getFailureResult("306", "活动已结束!");
        }
        List<FuntvActivityContentDetailInfo> infos = funtvActivityContentDetailMapper.findAll(request.getContentId());
        if (infos == null || infos.size() == 0) {
            return Result.getFailureResult("303", "活动已结束!");
        }
        List<FuntvActivityContentAnswerInfo> answerInfos = funtvActivityContentAnswerMapper.getAnswerInfo(request.getActivityType(), request.getAccountId(), request.getContentId());
        if (answerInfos != null && answerInfos.size() > 0) {
            return Result.getFailureResult("304", "请勿重复提交!");
        }
        List<FuntvActivityContentAnswerInfo> submits = new ArrayList<>();
        int correctCount = 0;
        for (SubmitAnswer answer : answers) {
            FuntvActivityContentDetailInfo detailInfo = this.getDetailInfo(infos, answer.getContentDetailId());
            if (detailInfo == null) {
                continue;
            }
            FuntvActivityContentAnswerInfo answerInfo = new FuntvActivityContentAnswerInfo();
            answerInfo.setAccountId(request.getAccountId());
            answerInfo.setActivityType(request.getActivityType());
            answerInfo.setContentId(request.getContentId());
            answerInfo.setContentDetailId(answer.getContentDetailId());
            answerInfo.setAnswer(answer.getAnswer());
            if (detailInfo.getSubjectResult().equals(answer.getAnswer())) {
                answerInfo.setStatus(1);
                correctCount++;
            } else {
                answerInfo.setStatus(0);
            }
            submits.add(answerInfo);
        }
        funtvActivityContentAnswerMapper.submitAnswer(submits);
        return Result.getSuccessResult(correctCount);
    }

    private FuntvActivityContentDetailInfo getDetailInfo(List<FuntvActivityContentDetailInfo> infos, Integer contentDetailId) {
        if (infos == null) {
            return null;
        }
        for (FuntvActivityContentDetailInfo info : infos) {
            if (info.getId().equals(contentDetailId)) {
                return info;
            }
        }
        return null;
    }

    //所有关卡都要显示出来 后台运营人员填写的prizeType中，一等奖是1，幸运奖是4，特等奖是5
    public Result<?> getAccountAnswerList(Integer accountId, int activityType) {
        List<FuntvActivityContentAnswerListInfo> list = funtvActivityContentAnswerMapper.getAccountAnswerList(accountId, activityType);
        if (list == null || list.size() == 0) {
            return Result.getFailureResult("305", "没有参与记录!");
        }
        List<Integer> contentIds = new ArrayList<>();
        for (FuntvActivityContentAnswerListInfo answerListInfo : list) {
            contentIds.add(answerListInfo.getContentId());
        }
        List<LotteryPrize> prizes = lotteryPrizeMapper.getMyPrizes1(accountId, activityType, contentIds);
        List<FuntvActivityContentAnswerListResponse> res = new ArrayList<>();
        //int sumCorrectCount = 0;
        Date now = new Date();
        List<FuntvActivityContentAnswerListInfo> answerListInfos = new ArrayList<>();
        for (FuntvActivityContentAnswerListInfo answerListInfo : list) {
            //sumCorrectCount += answerListInfo.getCorrectCount();
            if (this.getSpecialDate(answerListInfo.getStartTime()).before(now) && this.getSpecialDate(answerListInfo.getEndTime()).after(now)) {
                answerListInfo.setStatus(FuntvActivityPrizeStatus.PROCESSING.getStatus());
            } else if (this.getSpecialDate(answerListInfo.getStartTime()).after(now)) {
                answerListInfo.setStatus(FuntvActivityPrizeStatus.NOT_START.getStatus());
            } else if (this.getSpecialDate(answerListInfo.getEndTime()).before(now)) {
                answerListInfo.setStatus(FuntvActivityPrizeStatus.FINISHED.getStatus());
            }
            LotteryPrize prizeInfo = this.getPrizeInfo(prizes, answerListInfo.getAccountId(), answerListInfo.getContentId());
            if (prizeInfo == null) {
                if (this.getSpecialDate(answerListInfo.getEndTime()).before(now)) {
                    if (answerListInfo.getAllCount() == 0) {
                        answerListInfo.setPrizeType(FuntvActivityPrizeType.NOT_JOIN_ACTIVITY.getPrizeType());
                    } else {
                        answerListInfo.setPrizeType(FuntvActivityPrizeType.NOT_WIN_PRIZE.getPrizeType());
                    }
                } else {
                    answerListInfo.setPrizeType(FuntvActivityPrizeType.NOT_LOTTERY.getPrizeType());
                }
            }
            answerListInfo.setStartTime(this.getSpecialTime(answerListInfo.getStartTime()));
            answerListInfo.setEndTime(this.getSpecialTime(answerListInfo.getEndTime()));
            List<FuntvActivityPrizeRuleInfo> contentPrizeRules = funtvActivityPrizeRuleMapper.findAllContentRule(activityType, answerListInfo.getContentId());
            for (FuntvActivityPrizeRuleInfo contentPrizeRule : contentPrizeRules) {
                FuntvActivityContentAnswerListInfo temp = new FuntvActivityContentAnswerListInfo();
                BeanUtils.copyProperties(answerListInfo, temp);
                temp.setIsVirtualPrize(contentPrizeRule.getIsVirtualPrize());
                if (prizeInfo != null && contentPrizeRule.getPrizeType() == Integer.parseInt(prizeInfo.getPrizeName())) {
                    temp.setPrizeType(FuntvActivityPrizeType.CONTENT_PRIZE.getPrizeType());
                    temp.setIsAccept(prizeInfo.getStatus());
                    temp.setWinId(prizeInfo.getId());
                } else if (prizeInfo != null) {
                    temp.setPrizeType(FuntvActivityPrizeType.NOT_WIN_PRIZE.getPrizeType());
					/*temp.setIsAccept(prizeInfo.getStatus());
					temp.setWinId(prizeInfo.getId());*/
                }

                if (contentPrizeRule.getIsVirtualPrize() == 0) {
                    temp.setPrizeItemName(contentPrizeRule.getActualPrize());
                } else {
                    Map<String, Object> fmc = fmCommodityMapper.findCommodityById(Integer.valueOf(contentPrizeRule.getVirtualPrizeValue()));
                    if (fmc != null) {
                        temp.setPrizeItemName(fmc.get("name").toString());
                    }
                    temp.setVirtualPrizeDesc(contentPrizeRule.getVirtualPrizeAlert());
                }
                answerListInfos.add(temp);
            }
        }
        FuntvActivityPrizeRuleInfo specialPrizeRule = funtvActivityPrizeRuleMapper.findRule(activityType, 0, 5, null);
        FuntvActivityFirstPageInfo firstPage = null;
        List<LotteryPrize> finalPrize = null;
        if (specialPrizeRule != null && specialPrizeRule.getId() > 0) {
            if (firstPage == null) {
                firstPage = funtvActivityFirstPageConfigMapper.findActivity1(activityType);
            }
            if (finalPrize == null) {
                finalPrize = lotteryPrizeMapper.getAccountFinalPrize(accountId, activityType);
            }
            //特等奖
            answerListInfos.add(this.winPrize(firstPage.getStartTime(), firstPage.getEndTime(), specialPrizeRule, finalPrize, 5, accountId, null));
        }

        FuntvActivityPrizeRuleInfo luckyPrizeRule = funtvActivityPrizeRuleMapper.findRule(activityType, 0, 4, null);
        if (luckyPrizeRule != null && luckyPrizeRule.getId() > 0) {
            if (firstPage == null) {
                firstPage = funtvActivityFirstPageConfigMapper.findActivity1(activityType);
            }
            if (finalPrize == null) {
                finalPrize = lotteryPrizeMapper.getAccountFinalPrize(accountId, activityType);
            }
            //幸运奖
            answerListInfos.add(this.winPrize(firstPage.getStartTime(), firstPage.getEndTime(), luckyPrizeRule, finalPrize, 4, accountId, null));
        }

        //优化后，改变了其数据结构，每一个关卡的所有中奖信息放在一个list里面
        for (FuntvActivityContentAnswerListInfo funtvActivityContentAnswerListInfo : answerListInfos) {
            this.getFuntvActivityContentAnswerListResponse(res, funtvActivityContentAnswerListInfo);
        }
        return Result.getSuccessResult(res);
    }

    private void getFuntvActivityContentAnswerListResponse(List<FuntvActivityContentAnswerListResponse> res, FuntvActivityContentAnswerListInfo answerListInfo) {
        FuntvActivityContentAnswerListResponse responseItem = null;
        for (FuntvActivityContentAnswerListResponse answerList : res) {
            if (answerListInfo.getContentId() == null) {
                if (answerList.getContentId() == null) {
                    responseItem = answerList;
                    break;
                }
                continue;
            }
            if (answerListInfo.getContentId().equals(answerList.getContentId())) {
                responseItem = answerList;
                break;
            }
        }
        if (responseItem == null) {
            responseItem = new FuntvActivityContentAnswerListResponse();
            responseItem.setAccountId(answerListInfo.getAccountId());
            responseItem.setContentId(answerListInfo.getContentId());
            responseItem.setContentName(answerListInfo.getContentName());
            responseItem.setStartTime(answerListInfo.getStartTime());
            responseItem.setEndTime(answerListInfo.getEndTime());
            responseItem.setAllCount(answerListInfo.getAllCount());
            responseItem.setCorrectCount(answerListInfo.getCorrectCount());
            res.add(responseItem);
        }
        FuntvActivityContentPrizeInfo contentPrizeInfo = new FuntvActivityContentPrizeInfo();
        contentPrizeInfo.setIsAccept(answerListInfo.getIsAccept());
        contentPrizeInfo.setIsVirtualPrize(answerListInfo.getIsVirtualPrize());
        contentPrizeInfo.setPrizeItemName(answerListInfo.getPrizeItemName());
        contentPrizeInfo.setPrizeType(answerListInfo.getPrizeType());
        contentPrizeInfo.setStatus(answerListInfo.getStatus());
        contentPrizeInfo.setVirtualPrizeDesc(answerListInfo.getVirtualPrizeDesc());
        contentPrizeInfo.setWinId(answerListInfo.getWinId());
        responseItem.getContentPrizeList().add(contentPrizeInfo);
    }

    private FuntvActivityContentAnswerListInfo winPrize(Date startDate, Date endDate, FuntvActivityPrizeRuleInfo rule, List<LotteryPrize> finalPrize, Integer prizeType, Integer accountId, Integer sumCorrectCount) {
        LotteryPrize prizeInfo = this.getFinalPrizeInfo(finalPrize, prizeType);
        FuntvActivityContentAnswerListInfo prize = new FuntvActivityContentAnswerListInfo();
        prize.setStartTime(this.getSpecialTime(startDate));
        prize.setEndTime(this.getSpecialTime(endDate));
        prize.setAccountId(accountId);
        prize.setIsVirtualPrize(rule.getIsVirtualPrize());
        if (prizeInfo == null) {
            Date now = new Date();
            if (now.after(endDate)) {
                prize.setPrizeType(FuntvActivityPrizeType.NOT_WIN_PRIZE.getPrizeType());
                prize.setStatus(FuntvActivityPrizeStatus.PROCESSING.getStatus());
            } else {
                prize.setPrizeType(FuntvActivityPrizeType.NOT_LOTTERY.getPrizeType());
                prize.setStatus(FuntvActivityPrizeStatus.NOT_START.getStatus());
            }
        } else {
            prize.setIsAccept(prizeInfo.getStatus());
            prize.setStatus(FuntvActivityPrizeStatus.PROCESSING.getStatus());
            prize.setWinId(prizeInfo.getId());
        }
        prize.setCorrectCount(sumCorrectCount);
        if (prizeType == 4) {
            prize.setContentName("幸运奖");
            if (prizeInfo != null) {
                prize.setPrizeType(FuntvActivityPrizeType.LUCKY_PRIZE.getPrizeType());
            }
        } else if (prizeType == 5) {
            prize.setContentName("终极大奖");
            if (prizeInfo != null) {
                prize.setPrizeType(FuntvActivityPrizeType.SPECIAL_PRIZE.getPrizeType());
            }
        }
        if (rule.getIsVirtualPrize() == 0) {
            prize.setPrizeItemName(rule.getActualPrize());
        } else {
            Map<String, Object> fmc = fmCommodityMapper.findCommodityById(Integer.valueOf(rule.getVirtualPrizeValue()));
            if (fmc != null) {
                prize.setPrizeItemName(fmc.get("name").toString());
            }
            prize.setVirtualPrizeDesc(rule.getVirtualPrizeAlert());
        }
        return prize;
    }

    private LotteryPrize getPrizeInfo(List<LotteryPrize> infos, Integer accountId, Integer contentId) {
        if (infos == null) {
            return null;
        }
        for (LotteryPrize info : infos) {
            if (info.getAccountId().equals(accountId) && info.getLotteryId().equals(contentId)) {
                return info;
            }
        }
        return null;
    }

    private LotteryPrize getFinalPrizeInfo(List<LotteryPrize> infos, Integer prizeType) {
        if (infos == null) {
            return null;
        }
        for (LotteryPrize info : infos) {
            if (info.getPrizeName().equals(prizeType + "")) {
                return info;
            }
        }
        return null;
    }

    public Result<?> prizeList(int activityType) {
        List<FuntvActivityPrizeListInfo> list = lotteryPrizeMapper.getAllPrize(activityType);
        List<FuntvActivityPrizeRuleInfo> rules = funtvActivityPrizeRuleMapper.findAllRule(activityType);
        List<FuntvActivityPrizeListResponse> response = new ArrayList<>();
        for (FuntvActivityPrizeListInfo info : list) {
            FuntvActivityPrizeListResponse res = new FuntvActivityPrizeListResponse();
            FuntvActivityPrizeRuleInfo rule = this.findRule(rules, Integer.parseInt(info.getPrizeType()));
            if (rule.getIsVirtualPrize() == 1) {
                Map<String, Object> fmc = fmCommodityMapper.findCommodityById(Integer.valueOf(rule.getVirtualPrizeValue()));
                if (fmc != null) {
                    res.setContentName(fmc.get("name").toString() + rule.getPrizeAwardCount() + "名");
                }
            } else {
                res.setContentName(rule.getActualPrize() + rule.getPrizeAwardCount() + "名");
            }
            if (StringUtils.isNotBlank(info.getPhone())) {
                List<String> ps = new ArrayList<>();
                String[] phones = info.getPhone().split(",");
                for (String phone : phones) {
                    ps.add(phone.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                }
                res.setPhone(ps);
            } else {
                res.setPhone(new ArrayList<String>());
            }
            res.setType(1);
            response.add(res);
        }
        FuntvActivityFirstPageInfo firstPage = funtvActivityFirstPageConfigMapper.findActivity1(activityType);
        Date now = new Date();
        if (now.before(firstPage.getEndTime())) {
            return Result.getSuccessResult(response);
        }
        FuntvActivityPrizeRuleInfo specialRule = this.findRule(rules, 5);
        FuntvActivityPrizeListResponse special = null;
        if (specialRule != null) {
            special = new FuntvActivityPrizeListResponse();
            //special.setStartTime(this.getSpecialTime(firstPage.getEndTime()));
            special.setContentName("恭喜以下用户中特等奖");
            special.setPhone(new ArrayList<String>());
            special.setType(2);
            if (specialRule.getIsVirtualPrize() == 1) {
                Map<String, Object> fmc = fmCommodityMapper.findCommodityById(Integer.valueOf(specialRule.getVirtualPrizeValue()));
                if (fmc != null) {
                    special.setContentName(fmc.get("name").toString() + specialRule.getPrizeAwardCount() + "名");
                }
            } else {
                special.setContentName(specialRule.getActualPrize() + specialRule.getPrizeAwardCount() + "名");
            }
        }

        FuntvActivityPrizeRuleInfo luckyRule = this.findRule(rules, 4);
        FuntvActivityPrizeListResponse lucky = null;
        if (luckyRule != null) {
            lucky = new FuntvActivityPrizeListResponse();
            //lucky.setStartTime(this.getSpecialTime(firstPage.getEndTime()));
            lucky.setPhone(new ArrayList<String>());
            lucky.setContentName("恭喜以下用户中幸运奖");
            lucky.setType(3);
            if (luckyRule.getIsVirtualPrize() == 1) {
                Map<String, Object> fmc = fmCommodityMapper.findCommodityById(Integer.valueOf(luckyRule.getVirtualPrizeValue()));
                if (fmc != null) {
                    lucky.setContentName(fmc.get("name").toString() + luckyRule.getPrizeAwardCount() + "名");
                }
            } else {
                lucky.setContentName(luckyRule.getActualPrize() + luckyRule.getPrizeAwardCount() + "名");
            }
        }

        List<FuntvActivityPrizeListInfo.FuntvActivityFinalPrizeInfo> finals = lotteryPrizeMapper.getAllFinalPrize(activityType);
        for (FuntvActivityPrizeListInfo.FuntvActivityFinalPrizeInfo afinal : finals) {
            if (StringUtils.isNotBlank(afinal.getPhone())) {
                if (afinal.getPrizeType().equals("5") && special != null) {
                    special.getPhone().add(afinal.getPhone().replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                } else if (afinal.getPrizeType().equals("4") && lucky != null) {
                    lucky.getPhone().add(afinal.getPhone().replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                }
            }
        }
        if (lucky != null && lucky.getPhone().size() > 0) {
            response.add(0, lucky);
        }
        if (special != null && special.getPhone().size() > 0) {
            response.add(0, special);
        }
        return Result.getSuccessResult(response);
    }

    private FuntvActivityPrizeRuleInfo findRule(List<FuntvActivityPrizeRuleInfo> rules, int prizeType) {
        for (FuntvActivityPrizeRuleInfo rule : rules) {
            if (rule.getPrizeType() == prizeType) {
                return rule;
            }
        }
        return null;
    }

}
