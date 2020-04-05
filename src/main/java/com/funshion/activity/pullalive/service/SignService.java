package com.funshion.activity.pullalive.service;

import com.funshion.activity.common.bean.LotteryLog;
import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryLogMapper;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.service.LotteryService;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.orange.entity.ActivitySignInfo;
import com.funshion.activity.orange.entity.AppSignInfo;
import com.funshion.activity.orange.mapper.AppSignInfoMapper;
import com.funshion.activity.pullalive.domain.*;
import com.funshion.activity.pullalive.mapper.SignConfigMapper;
import com.funshion.activity.redpacket.constants.RedPackConstant;
import com.funshion.activity.redpacket.domain.RedPackRecord;
import com.funshion.activity.redpacket.mapper.RedPackRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class SignService {

    @Autowired
    LotteryService lotteryService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LotteryLogMapper lotteryLogMapper;

    @Autowired
    private RedPackRecordMapper redPackRecordMapper;

    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;

    @Autowired
    private AppSignInfoMapper appSignInfoMapper;

    @Autowired
    private SignConfigMapper signConfigMapper;

    public Result getSignTemplateLauncher(SignTemplateReq req) {
        // 签名校验
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        SignTemplateRsp rsp = new SignTemplateRsp();
        // 查询签到信息
        ActivitySignInfo appSignInfo = appSignInfoMapper.getLastSignRecord(req.getTvId() + "", req.getActivityType());
        rsp.setIsSigned(0);
        if (appSignInfo == null) {
            rsp.setSignDays(0);
        } else {
            // 判断是否签到过
            if (LocalDate.now().equals(appSignInfo.getCreateTime()) || (appSignInfo.getUpdateTime() != null && LocalDate.now().equals(appSignInfo.getUpdateTime()))) {
                rsp.setIsSigned(1);
            }
            rsp.setSignDays(appSignInfo.getTotal());
        }

        // 查询基础配置信息
        SignConfig signConfig = signConfigMapper.getConfigInfo(req.getActivityType());
        if (signConfig == null) {
            return Result.getSuccessResult(rsp);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long betweenDays = (sdf.parse(sdf.format(signConfig.getFinalPrizeDate())).getTime() - sdf.parse(sdf.format(new Date())).getTime()) / (3600 * 1000 * 24);
            if (betweenDays < 0) {
                betweenDays = 0;
            }
            rsp.setDaysBeforeFinalPrizeDate(betweenDays);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(signConfig.getFinalPrizeDate());
            String date = (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日";
            rsp.setFinalPrizeTitle(signConfig.getTitle());
            // 开奖当天及以后
            if (sdf.format(new Date()).equals(sdf.format(signConfig.getFinalPrizeDate())) || new Date().after(signConfig.getFinalPrizeDate())) {
                rsp.setDaysBeforeFinalPrizeDate(0);
                // "开奖时间：7月22日10点，请点击下方“立即抽奖”，祝你好运！"
                rsp.setFinalPrizeTitle("开奖时间：" + date + calendar.get(Calendar.HOUR_OF_DAY) + "点，请点击下方“立即抽奖”，祝您好运！");
            }

            rsp.setFinalPrizeDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        rsp.setBgImg(ImgUtils.getImgUrl(signConfig.getBgImg()));
        rsp.setRuleContent(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".ruleContent", signConfig.getRuleContent()));

        // 查询抽奖活动信息
        List<SignBlockInfo> blocks = new ArrayList();
        rsp.setBlocks(blocks);
        List<SignConfigDetail> configDetails = signConfigMapper.getConfigDetails(signConfig.getId());

        // 取最大的签到天数作为终极大奖的签到天数
        Integer maxSignDays = 0;
        for (SignConfigDetail detail : configDetails) {
            if (maxSignDays < detail.getSignDays()) {
                maxSignDays = detail.getSignDays();
            }
            // 分阶段的活动信息
            blocks.add(getBlockDetail(detail, rsp.getSignDays(), isReachFinalDate(signConfig.getFinalPrizeDate()), req.getTvId()));
        }

        // 设置距离终极大奖需要签到天数
        if (rsp.getSignDays() >= maxSignDays) {
            rsp.setDaysNeededForFinalPrize(0);
        } else {
            rsp.setDaysNeededForFinalPrize(maxSignDays - rsp.getSignDays());
        }
        rsp.setMaxSignDays(signConfig.getMaxSignDays());
        if (rsp.getSignDays() > signConfig.getMaxSignDays()) {
            rsp.setSignDays(signConfig.getMaxSignDays());
        }

        // 大奖已抽过，更改显示
        if (!CollectionUtils.isEmpty(rsp.getBlocks())) {
            SignBlockInfo signBlockInfo = rsp.getBlocks().get(rsp.getBlocks().size() - 1);
            if (signBlockInfo.getStatus() > 2) {
                rsp.setFinalPrizeTitle("");
            }
        }
        return Result.getSuccessResult(rsp);
    }

    private boolean isReachFinalDate(Date finalPrizeDate) {
        if (finalPrizeDate == null) {
            return false;
        }
        if (new Date().after(finalPrizeDate)) {
            return true;
        }

        return false;
    }


    public Result getVipLotteryPage(SignTemplateReq req) {
        // 签名校验
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        VipLotteryPageRsp rsp = new VipLotteryPageRsp();

        // 查询基础配置信息
        SignConfig signConfig = signConfigMapper.getConfigInfo(req.getActivityType());
        if (signConfig == null) {
            return Result.getSuccessResult(rsp);
        }

        rsp.setBgImg(ImgUtils.getImgUrl(signConfig.getBgImg()));
        rsp.setRuleContent(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".ruleContent", signConfig.getRuleContent()));
        rsp.setPrizeContent(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".prizeContent", ""));
        rsp.setBgColor(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".bgColor", ""));
        // 查询抽奖活动信息
        List<VipLotteryBlock> blocks = new ArrayList();
        rsp.setBlocks(blocks);
        List<SignConfigDetail> configDetails = signConfigMapper.getConfigDetails(signConfig.getId());
        for (SignConfigDetail detail : configDetails) {
            VipLotteryBlock block = new VipLotteryBlock();
            block.setActivityType(detail.getActivityType());
            block.setIcon(ImgUtils.getImgUrl(detail.getIcon()));
            blocks.add(block);
        }

        String chanceRedisKey = LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".lotteryChance", "");
        Integer chance = 0;
        if (StringUtils.isNotBlank(chanceRedisKey)) {
            String reidsKey = chanceRedisKey + req.getTvId();
            chance = (Integer) redisService.get(reidsKey);
        }
        rsp.setLotteryChances(chance == null ? 0 : chance);
        return Result.getSuccessResult(rsp);
    }

    public Result sign(SignTemplateReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("showFinalPrize", 0);
        ActivitySignInfo appSignInfo = appSignInfoMapper.getLastSignRecord(req.getTvId() + "", req.getActivityType());
        if (appSignInfo == null) {
            AppSignInfo signInfo = new AppSignInfo();
            signInfo.setAccountId(req.getTvId() + "");
            signInfo.setSignId(req.getActivityType());
            signInfo.setIsGive(0);
            signInfo.setTotal(1);
            signInfo.setCreateTime(new Date());
            dataMap.put("signDays", 1);
            if (showFinalPrize(req)) {
                dataMap.put("showFinalPrize", 1);
            }
            appSignInfoMapper.insertSignHistory(signInfo);
        } else if (LocalDate.now().equals(appSignInfo.getCreateTime()) || (appSignInfo.getUpdateTime() != null && LocalDate.now().equals(appSignInfo.getUpdateTime()))) {
            dataMap.put("signDays", appSignInfo.getTotal());
        } else {
            dataMap.put("signDays", appSignInfo.getTotal() + 1);
            appSignInfoMapper.updateSignTotal(appSignInfo.getId());
            if (showFinalPrize(req)) {
                dataMap.put("showFinalPrize", 1);
            }
        }

        SignConfig signConfig = signConfigMapper.getConfigInfo(req.getActivityType());
        if (signConfig != null && signConfig.getFinalPrizeDate() != null && new Date().after(signConfig.getFinalPrizeDate())) {
            dataMap.put("isReachFinalDate", 1);
        } else {
            dataMap.put("isReachFinalDate", 0);
        }

        return Result.getSuccessResult(dataMap);
    }

    public Result getMyPrizes(SignTemplateReq req) {
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        List<MultPrizeInfo> prizes = new ArrayList<MultPrizeInfo>();
        List<Integer> actTypes = signConfigMapper.getActivityTypes(req.getActivityType());
        if (CollectionUtils.isEmpty(actTypes)) {
            return Result.getSuccessResult();
        }

        // 加上活动总类型
        actTypes.add(req.getActivityType());
        List<LotteryPrize> commonPrizes = lotteryPrizeMapper.getMyPrizesWithActTypes(req.getTvId(), StringUtils.join(actTypes, ","));
        for (LotteryPrize prize : commonPrizes) {
            MultPrizeInfo multPrizeInfo = new MultPrizeInfo();
            multPrizeInfo.setAcceptUrl(lotteryService.getAcceptUrl(prize.getPrizeType(), prize.getId(), prize.getPrizeUrl(),
                    prize.getAccountId(), prize.getMac(), prize.getActivityType()));
            multPrizeInfo.setPrizeTitle("抽奖");
            multPrizeInfo.setPrizeCode(prize.getId() + "");
            multPrizeInfo.setPrizeName(prize.getPrizeName());
            multPrizeInfo.setStatus(prize.getStatus());
            multPrizeInfo.setCreateTime(prize.getCreateTime());
            multPrizeInfo.setType(prize.getPrizeType());
            multPrizeInfo.setPrizeImg(prize.getPrizeImg());
            multPrizeInfo.setAmount("");
            multPrizeInfo.setPrizeId(prize.getPrizeId());
            prizes.add(multPrizeInfo);
        }

        // 现金红包
        List<RedPackRecord> hbPrizes = redPackRecordMapper.getRecordsWithActTypes(req.getTvId(), StringUtils.join(actTypes, ","));
        for (RedPackRecord prize : hbPrizes) {
            MultPrizeInfo multPrizeInfo = new MultPrizeInfo();
            multPrizeInfo.setPrizeTitle("抢红包");
            multPrizeInfo.setPrizeCode(prize.getId() + "");
            multPrizeInfo.setPrizeName(String.format("现金%.2f元", prize.getAmount().doubleValue()));
            if (prize.getStatus() == 1) {
                multPrizeInfo.setStatus(JinliConstants.PrizeStatus.TO_ACCEPT);
                multPrizeInfo.setAcceptUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, prize.getTvId(), prize.getId()));
            } else if (prize.getStatus() == 5) {
                multPrizeInfo.setStatus(JinliConstants.PrizeStatus.EXPIRED);
            } else {
                multPrizeInfo.setStatus(JinliConstants.PrizeStatus.ACCEPT);
            }

            multPrizeInfo.setCreateTime(prize.getCreateTime());
            multPrizeInfo.setType(JinliConstants.PrizeType.RED_PACKET);
            multPrizeInfo.setPrizeImg("");
            multPrizeInfo.setAmount(new DecimalFormat("0.00").format(prize.getAmount()));
            prizes.add(multPrizeInfo);
        }


        Collections.sort(prizes);
        GetWinRecordRsp rsp = new GetWinRecordRsp();
        rsp.setPrizes(prizes);
        if (CollectionUtils.isEmpty(prizes)) {
            rsp.setBgImg(RedPackConstant.Img.NO_PRIZE_BG_IMG);
        } else {
            rsp.setBgImg(RedPackConstant.Img.PRIZE_BG_IMG);
        }
        return Result.getSuccessResult(rsp);
    }


    public Result getTravelPlayLauncher(SignTemplateReq req) {
        // 签名校验
        if (!MD5Utils.getMD5String(req.getTvId() + "" + req.getActivityType() + req.getCtime()
                + ActivityConstants.SignKey.MERCURY_SIGN).equals(req.getSign())) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        TravelPlayTemplateRsp rsp = new TravelPlayTemplateRsp();

        // 查询基础配置信息
        SignConfig signConfig = signConfigMapper.getConfigInfo(req.getActivityType());
        if (signConfig == null) {
            return Result.getSuccessResult(rsp);
        }

        rsp.setBgImg(ImgUtils.getImgUrl(signConfig.getBgImg()));
        rsp.setRuleContent(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".ruleContent", signConfig.getRuleContent()));
        rsp.setPrizeContent(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".prizeContent", ""));
        rsp.setBgColor(LoadDataConfig.getDataValueByKey("funtv.activity." + req.getActivityType() + ".bgColor", ""));
        // 查询抽奖活动信息
        List<TravelPlayBlockInfo> blocks = new ArrayList();
        rsp.setBlocks(blocks);
        List<SignConfigDetail> configDetails = signConfigMapper.getConfigDetails(signConfig.getId());
        for (SignConfigDetail detail : configDetails) {
            // 分阶段的活动信息
            blocks.add(getTravelPlayBlockDetail(detail, req.getTvId()));
        }
        // 第一个默认聚焦
        //blocks.get(0).setBFocus(1);
        // 校验是否达到终极大奖条件
        //        List<Integer> actTypes = signConfigMapper.getActivityTypes(300);
        //        int signedCount = appSignInfoMapper.getTravelSignedCount(req.getTvId(), StringUtils.join(actTypes, ","));
        //        // showFinalPrize: 0-未达到条件；1-达到条件，未抽奖；2-达到条件已抽奖。
        //        if (CollectionUtils.isEmpty(actTypes) || signedCount < actTypes.size()) {
        //            rsp.setShowFinalPrize(0);
        //        } else {
        //            List<LotteryLog> lotteryLogs = lotteryLogMapper.getTodayLotteryLog(req.getTvId(), 300);
        //            if (CollectionUtils.isEmpty(lotteryLogs)) {
        //                rsp.setShowFinalPrize(1);
        //            } else {
        //                rsp.setShowFinalPrize(2);
        //            }
        //        }
        return Result.getSuccessResult(rsp);
    }

    private SignBlockInfo getBlockDetail(SignConfigDetail detail, Integer signDays, boolean isReachFinalDate, Integer tvId) {
        SignBlockInfo info = new SignBlockInfo();
        info.setActivityType(detail.getActivityType());
        info.setIcon(ImgUtils.getImgUrl(detail.getIcon()));
        info.setRequireDays(detail.getSignDays());
        if (JinliConstants.PrizeType.RED_PACKET.equals(detail.getPrizeType())) {
            info.setPrizeType(detail.getPrizeType());
        }

        if (signDays < detail.getSignDays()) {
            info.setStatus(ActivityConstants.PrizeStatus.INIT);
            info.setButtonText("未达成");
            return info;
        }

        if (detail.getIsFinalPrize() == 1 && !isReachFinalDate) {
            info.setStatus(ActivityConstants.PrizeStatus.BEFORE_LOTTERY);
            info.setButtonText("等待开奖");
            return info;
        }

        List<LotteryLog> lotteryLogs = lotteryLogMapper.getLotteryLog(tvId, detail.getActivityType());
        if (CollectionUtils.isEmpty(lotteryLogs)) {
            info.setStatus(ActivityConstants.PrizeStatus.TO_LOTTERY);
            info.setButtonText("立即抽奖");
            return info;
        }

        List<RedPackRecord> hbRecords = redPackRecordMapper.getRecords(tvId, detail.getActivityType());
        if (JinliConstants.PrizeType.RED_PACKET.equals(detail.getPrizeType())) {
            if (CollectionUtils.isEmpty(hbRecords)) {
                info.setStatus(ActivityConstants.PrizeStatus.LOSE);
                info.setButtonText("未中奖");
                return info;
            }

            RedPackRecord winRecord = hbRecords.get(0);
            if (winRecord.getStatus() == RedPackConstant.Status.INIT) {
                info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                info.setButtonText("立即领取");
                info.setPrizeName(String.format("现金%.2f元", winRecord.getAmount().doubleValue()));
                info.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, winRecord.getId()));
                info.setAmount(new DecimalFormat("0.00").format(winRecord.getAmount()));
                return info;
            }
        } else {
            List<LotteryPrize> winRecords = lotteryPrizeMapper.getMyPrizes(tvId, detail.getActivityType());
            if (!CollectionUtils.isEmpty(winRecords)) {
                LotteryPrize winRecord = winRecords.get(0);
                // 待领取状态
                if (winRecord.getStatus() == 0) {
                    info.setPrizeUrl(lotteryService.getAcceptUrl(winRecord.getPrizeType(), winRecord.getId(), winRecord.getPrizeUrl(),
                            winRecord.getAccountId(), winRecord.getMac(), winRecord.getActivityType()));
                    info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                    info.setPrizeType(winRecord.getPrizeType());
                    info.setButtonText("立即领取");
                    info.setPrizeImg(winRecord.getPrizeImg());
                    info.setPrizeName(winRecord.getPrizeName());
                    return info;
                }
            } else if (!CollectionUtils.isEmpty(hbRecords)) {
                RedPackRecord winRecord = hbRecords.get(0);
                if (winRecord.getStatus() == RedPackConstant.Status.INIT) {
                    info.setPrizeType(JinliConstants.PrizeType.RED_PACKET);
                    info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                    info.setButtonText("立即领取");
                    info.setAmount(new DecimalFormat("0.00").format(winRecord.getAmount()));
                    info.setPrizeName(String.format("现金%.2f元", winRecord.getAmount().doubleValue()));
                    info.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, winRecord.getId()));
                    return info;
                }
            } else {
                info.setStatus(ActivityConstants.PrizeStatus.LOSE);
                info.setButtonText("未中奖");
                return info;
            }
        }

        info.setStatus(ActivityConstants.PrizeStatus.ACCEPTED);
        info.setButtonText("已完成");
        return info;
    }

    private TravelPlayBlockInfo getTravelPlayBlockDetail(SignConfigDetail detail, Integer tvId) {
        TravelPlayBlockInfo info = new TravelPlayBlockInfo();
        // 查询签到信息
        info.setIsSigned(0);
        ActivitySignInfo appSignInfo = appSignInfoMapper.getLastSignRecord(tvId + "", detail.getActivityType());
        if (appSignInfo == null) {
            info.setSignDays(0);
        } else {
            // 判断是否签到过
            if (LocalDate.now().equals(appSignInfo.getCreateTime()) || (appSignInfo.getUpdateTime() != null && LocalDate.now().equals(appSignInfo.getUpdateTime()))) {
                info.setIsSigned(1);
            }
            info.setSignDays(appSignInfo.getTotal());
        }

        info.setActivityType(detail.getActivityType());
        info.setIcon(ImgUtils.getImgUrl(detail.getIcon()));
        info.setFocusIcon(ImgUtils.getImgUrl(detail.getFocusIcon()));
        info.setBrightIcon(ImgUtils.getImgUrl(detail.getBrightIcon()));
        info.setMediaType(detail.getMediaType());
        info.setMediaId(detail.getMediaId());
        if (JinliConstants.PrizeType.RED_PACKET.equals(detail.getPrizeType())) {
            info.setPrizeType(detail.getPrizeType());
        }

        if (info.getIsSigned() == 0) {
            info.setStatus(ActivityConstants.PrizeStatus.INIT);
            info.setButtonText("未达成");
            return info;
        }

        List<LotteryLog> lotteryLogs = lotteryLogMapper.getTodayLotteryLog(tvId, detail.getActivityType());
        if (CollectionUtils.isEmpty(lotteryLogs)) {
            info.setStatus(ActivityConstants.PrizeStatus.TO_LOTTERY);
            info.setButtonText("立即抽奖");
            return info;
        }

        List<RedPackRecord> hbRecords = redPackRecordMapper.getTodayRecords(tvId, detail.getActivityType());
        if (JinliConstants.PrizeType.RED_PACKET.equals(detail.getPrizeType())) {
            if (CollectionUtils.isEmpty(hbRecords)) {
                info.setStatus(ActivityConstants.PrizeStatus.LOSE);
                info.setButtonText("未中奖");
                return info;
            }

            RedPackRecord winRecord = hbRecords.get(0);
            if (winRecord.getStatus() == RedPackConstant.Status.INIT) {
                info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                info.setButtonText("立即领取");
                info.setPrizeName(String.format("现金%.2f元", winRecord.getAmount().doubleValue()));
                info.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, winRecord.getId()));
                info.setAmount(new DecimalFormat("0.00").format(winRecord.getAmount()));
                return info;
            }
        } else {
            List<LotteryPrize> winRecords = lotteryPrizeMapper.getMyTodayPrizes(tvId, detail.getActivityType());
            if (!CollectionUtils.isEmpty(winRecords)) {
                LotteryPrize winRecord = winRecords.get(0);
                // 待领取状态
                if (winRecord.getStatus() == 0) {
                    info.setPrizeUrl(lotteryService.getAcceptUrl(winRecord.getPrizeType(), winRecord.getId(), winRecord.getPrizeUrl(),
                            winRecord.getAccountId(), winRecord.getMac(), winRecord.getActivityType()));
                    info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                    info.setPrizeType(winRecord.getPrizeType());
                    info.setButtonText("立即领取");
                    info.setPrizeImg(winRecord.getPrizeImg());
                    info.setPrizeName(winRecord.getPrizeName());
                    info.setPrizeId(winRecord.getPrizeId());
                    return info;
                }
            } else if (!CollectionUtils.isEmpty(hbRecords)) {
                RedPackRecord winRecord = hbRecords.get(0);
                if (winRecord.getStatus() == RedPackConstant.Status.INIT) {
                    info.setPrizeType(JinliConstants.PrizeType.RED_PACKET);
                    info.setStatus(ActivityConstants.PrizeStatus.TO_ACCEPT);
                    info.setButtonText("立即领取");
                    info.setAmount(new DecimalFormat("0.00").format(winRecord.getAmount()));
                    info.setPrizeName(String.format("现金%.2f元", winRecord.getAmount().doubleValue()));
                    info.setPrizeUrl(String.format(RedPackConstant.ClientUrl.RED_PACKET_URL, tvId, winRecord.getId()));
                    return info;
                }
            } else {
                info.setStatus(ActivityConstants.PrizeStatus.LOSE);
                info.setButtonText("未中奖");
                return info;
            }
        }

        info.setStatus(ActivityConstants.PrizeStatus.ACCEPTED);
        info.setButtonText("已完成");
        return info;
    }

    private boolean showFinalPrize(SignTemplateReq req) {
        // 看片游世界特殊逻辑
        if (req.getActivityType() > 300 && req.getActivityType() < 310) {
            List<LotteryLog> logs = lotteryLogMapper.getTodayLotteryLog(req.getTvId(), 300);
            if (!CollectionUtils.isEmpty(logs)) {
                return false;
            }
            // 校验是否达到终极大奖条件
            List<Integer> actTypes = signConfigMapper.getActivityTypes(300);
            int signedCount = appSignInfoMapper.getTravelSignedCount(req.getTvId(), StringUtils.join(actTypes, ","));
            // showFinalPrize: 0-未达到条件；1-达到条件，未抽奖；2-达到条件已抽奖。
            if (CollectionUtils.isEmpty(actTypes) || signedCount < actTypes.size() - 1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
