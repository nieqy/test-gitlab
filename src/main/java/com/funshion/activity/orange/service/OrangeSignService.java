package com.funshion.activity.orange.service;

import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.TimeUtils;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.orange.entity.AppSignInfo;
import com.funshion.activity.orange.entity.SignButtonInfo;
import com.funshion.activity.orange.entity.SignDetailInfo;
import com.funshion.activity.orange.entity.SignInInfo;
import com.funshion.activity.orange.mapper.AppSignInfoMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrangeSignService {

    private final int bigPrize = 7;

    private final int smallPrize = 3;

    private final String smallPrizeText = "抽小奖";

    private final String bigPrizeText = "抽大奖";

    private final String buttonText3 = "三天抽小奖";

    private final String buttonText7 = "七天抽大奖";

    private final String smallPrizeTip = "再签%o天抽小奖";

    private final String bigPrizeTip = "再签%o天抽大奖";

    private final String rollingTip = "用户%s  中奖 “%s”";

    @Autowired
    private AppSignInfoMapper appSignInfoMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;

    private static SignInInfo signInInfo = new SignInInfo();

    static {
        signInInfo.setBgImg(ImgUtils.getImgUrl("6976cfbcf42ef0c8478ce39f8f115254"));
        signInInfo.setEndTime(new Date());
        signInInfo.setName("橙子签到活动");
        signInInfo.setSignId(10000);
        ;
        signInInfo.setStartTime(new Date());
        signInInfo.setAword(""
                + "1.用户进入活动页面，点击“签到”按钮即可开启签到参加活动\n"
                + "2.连续签到3天，在第3天获得一次抽取小奖的机会，继续签到7天，在第7天获得一次抽取大奖的机会\n"
                + "3.每天进行签到或抽奖的有效期截止到当天的24点，每位用户可以在一轮7天签到后重复参加\n"
                + "4.如果用户中途因故漏签，可以点击“补签”按说明进行补签到\n"
                + "5.获得优惠券的用户请尽快扫码领取优惠券，优惠券的有效期以发放主体为准；获得实物奖品的用户请尽快扫码填写有效收货地址，活动结束时未领奖填写收货地址的用户将视作放弃领取该奖品\n"
                + "6.如果用户的收货地址因快递公司或不可抗力因素无法配送（新疆、西藏及港澳台地区无法配送），风行有权收回用户的获奖资格，奖品不可退换折现\n"
                + "7.用户可参与活动的客户端版本、活动时间和范围由风行公司管理，风行公司有权对活动进行临时变更\n"
                + "8.请用户理性合法地参与活动，若出现非正当方式参与活动的情况，风行公司有权取缔用户的活动资格及奖品。如有疑问可到橙子短视频APP内【我的】-【意见反馈】中联系客服进行反馈\n"
                + "");
    }

    public Result<?> getSignInfo(String account_id) {
        try {
            Date now = new Date();
            Map<String, Object> res = new HashMap<String, Object>();
            AppSignInfo lastSign = appSignInfoMapper.selectLastSignHistory(account_id);
            Integer isFinished = 0;
            if (lastSign != null && lastSign.getIsGive() == 1 && lastSign.getTotal() == 7) {
                if (TimeUtils.getCurrentDayOfYear(now) == TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime())) {
                    isFinished = 1;
                }
                lastSign = null;
            }
            //判断连续签到的天数
            int total = lastSign != null && lastSign.getTotal() >= bigPrize ? bigPrize : lastSign == null ? 0 : lastSign.getTotal();
            //判断签到完成是否领奖
            total = total == bigPrize && lastSign.getIsGive() == 1 ? 0 : total != bigPrize ? total : bigPrize;
            //判断是否能够补签
            if (total != bigPrize && lastSign != null
                    && TimeUtils.getCurrentDayOfYear(now) - TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime()) + total > bigPrize) {
                total = 0;
                lastSign = null;
            }
            List<SignDetailInfo> details = new ArrayList<SignDetailInfo>();
            for (int i = 0; i < 7; i++) {
                //计算当天签到状态
                Integer status = null;
                if (isFinished == 1) {
                    status = 1;
                } else if (i < total) {
                    status = 1;
                } else {
                    int nowYearDay = TimeUtils.getCurrentDayOfYear(now);
                    int yearDay = nowYearDay;
                    if (lastSign != null) {
                        yearDay = TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime());
                    }
                    if (yearDay + ((i - total) + 1) < nowYearDay) {
                        status = 0;
                    }
                }
                if (i == 2) {
                    SignDetailInfo detail = new SignDetailInfo(smallPrizeText, status);
                    detail.setPrize(1);
                    details.add(detail);
                } else if (i == 6) {
                    SignDetailInfo detail = new SignDetailInfo(bigPrizeText, status);
                    detail.setPrize(1);
                    details.add(detail);
                } else {
                    SignDetailInfo detail = new SignDetailInfo((i + 1) + "", status);
                    details.add(detail);
                }

            }

            //计算当天签到按钮
            List<SignButtonInfo> buttons = new ArrayList<SignButtonInfo>();

            if (isFinished == 1) {
                SignButtonInfo b1 = new SignButtonInfo("disable", "明日继续签到抽奖");
                buttons.add(b1);
            } else if (lastSign == null) {
                SignButtonInfo b1 = new SignButtonInfo("sign", "签到");
                buttons.add(b1);
            } else if (total == bigPrize && lastSign.getIsGive() == 0) {
                SignButtonInfo b1 = new SignButtonInfo("prize", buttonText7);
                buttons.add(b1);
            } else {
                int signDay = TimeUtils.getCurrentDayOfYear(new Date()) - TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime());
                if (signDay == 0) {
                    if (total == bigPrize || total == smallPrize) {
                        if (lastSign.getIsGive() == 1) {
                            SignButtonInfo b1 = new SignButtonInfo("disable", total < 3 ? String.format(smallPrizeTip, smallPrize - total) : String.format(bigPrizeTip, bigPrize - total));
                            buttons.add(b1);
                        } else {
                            SignButtonInfo b1 = new SignButtonInfo("prize", total == smallPrize ? buttonText3 : buttonText7);
                            buttons.add(b1);
                        }
                    } else {
                        SignButtonInfo b1 = new SignButtonInfo("disable", total < 3 ? String.format(smallPrizeTip, smallPrize - total) : String.format(bigPrizeTip, bigPrize - total));
                        buttons.add(b1);
                    }
                } else if (signDay == 1) {
                    if (total == bigPrize - 1 || total == smallPrize - 1) {
                        SignButtonInfo b1 = new SignButtonInfo("sign", total == smallPrize - 1 ? buttonText3 : buttonText7);
                        buttons.add(b1);
                    } else {
                        SignButtonInfo b1 = new SignButtonInfo("sign", "签到");
                        buttons.add(b1);
                    }
                } else if (signDay > 1 && signDay < bigPrize) {
                    SignButtonInfo b1 = new SignButtonInfo("sign_before", "补签");
                    buttons.add(b1);
                    SignButtonInfo b2 = new SignButtonInfo("resign", "重新签到");
                    buttons.add(b2);
                } else {
                    SignButtonInfo b1 = new SignButtonInfo("sign", "签到");
                    buttons.add(b1);
                }
            }
            signInInfo.setBgImg(ImgUtils.getImgUrl(LoadDataConfig.getDataValueByKey("funtv.activity.orange.sign", "6976cfbcf42ef0c8478ce39f8f115254")));
            if (lastSign == null) {
                signInInfo.setBgImg(ImgUtils.getImgUrl(LoadDataConfig.getDataValueByKey("funtv.activity.orange.first", "db75a889eb4640ae05ec768f8facfd19")));
            }
            res.put("info", signInInfo);
            res.put("buttons", buttons);
            res.put("details", details);
            res.put("rollingInfo", getRollingInfo());
            return Result.getSuccessResult(res);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return Result.HTTP_REQUEST_FAILED;
        }
    }


    public Result<?> signIn(String account_id, Integer sign_id) {
        AppSignInfo lastSign = appSignInfoMapper.selectLastSignHistory(account_id);
        if (lastSign != null && lastSign.getIsGive() == 1 && lastSign.getTotal() == 7) {
            lastSign = null;
        }
        int total = 1;
        if (lastSign != null) {
            if (TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime()) + 1 == TimeUtils.getCurrentDayOfYear(new Date())) {
                total = lastSign.getTotal() + 1;
            }
            if (TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime()) == TimeUtils.getCurrentDayOfYear(new Date())) {
                SignButtonInfo b1;
                total = lastSign.getTotal();
                if (total == bigPrize || total == smallPrize) {
                    b1 = new SignButtonInfo("prize", total == smallPrize ? buttonText3 : buttonText7);
                } else {
                    b1 = new SignButtonInfo("disable", total < 3 ? String.format(smallPrizeTip, smallPrize - total) : String.format(bigPrizeTip, bigPrize - total));
                }
                return Result.getSuccessResult(b1);
            }
        }
        AppSignInfo sign = new AppSignInfo();
        sign.setAccountId(account_id);
        sign.setSignId(sign_id);
        sign.setCreateTime(new Date());
        sign.setTotal(total);
        sign.setIsGive(0);
        appSignInfoMapper.insertSignHistory(sign);
        SignButtonInfo b1;
        if (total == bigPrize || total == smallPrize) {
            b1 = new SignButtonInfo("prize", total == smallPrize ? buttonText3 : buttonText7);
        } else {
            b1 = new SignButtonInfo("disable", total < 3 ? String.format(smallPrizeTip, smallPrize - total) : String.format(bigPrizeTip, bigPrize - total));
        }
        return Result.getSuccessResult(b1);
    }

    public Result<?> getSignCode(String mac, String account_id, Integer sign_id) {
        String code = (String) redisService.get(RedisPrefix.ORANGE_SIGN_CODE + account_id + "_" + sign_id);
        if (StringUtils.isBlank(code)) {
            code = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
            redisService.setForTimeCustom(RedisPrefix.ORANGE_SIGN_CODE + code, account_id + "_" + sign_id + "_" + mac, 7, TimeUnit.DAYS);
            redisService.setForTimeCustom(RedisPrefix.ORANGE_SIGN_CODE + account_id + "_" + sign_id, code, 7, TimeUnit.DAYS);
        }
        return Result.getSuccessResult(code);
    }


    public Result<?> useSignCode(String mac, String account_id, Integer sign_id, String code) {
        String signInfo = (String) redisService.get(RedisPrefix.ORANGE_SIGN_CODE + code.toUpperCase());

        if (StringUtils.isBlank(signInfo)) {
            return Result.getFailureResult("401", "补签邀请码不存在");
        }
        String accountId = signInfo.split("_")[0];
        String signId = signInfo.split("_")[1];
        String oMac = signInfo.split("_")[2];
        if (mac.equals(oMac)) {
            return Result.getFailureResult("401", "无法使用的补签邀请码");
        }
        AppSignInfo lastSign = appSignInfoMapper.selectLastSignHistory(accountId);
        if (lastSign == null) {
            return Result.getFailureResult("401", "无法使用补签邀请码");
        }
        int nowYearDay = TimeUtils.getCurrentDayOfYear(new Date());
        int signYearDay = TimeUtils.getCurrentDayOfYear(lastSign.getCreateTime());
        int total = lastSign.getTotal();
        while (total < bigPrize && signYearDay < nowYearDay) {
            total++;
            signYearDay++;
            AppSignInfo sign = new AppSignInfo();
            sign.setAccountId(accountId);
            sign.setSignId(Integer.valueOf(signId));
            sign.setCreateTime(new Date());
            sign.setTotal(total);
            sign.setIsGive(0);
            appSignInfoMapper.insertSignHistory(sign);
        }
        redisService.remove(RedisPrefix.ORANGE_SIGN_CODE + code.toUpperCase());
        redisService.remove(RedisPrefix.ORANGE_SIGN_CODE + accountId + "_" + signId);
        return Result.getSuccessResult();
    }


    public List<String> getRollingInfo() {
        List<String> oldInfos = (List<String>) redisService.get(RedisPrefix.ORANGE_SIGN_CODE + "_rolling_info");
        if (oldInfos != null) {
            return oldInfos;
        }
        List<String> infos = new ArrayList<String>();
        List<LotteryPrize> prizeList = lotteryPrizeMapper.getWinRecords30();
        int count = 1;
        for (LotteryPrize prize : prizeList) {
            if (!prize.getPrizeName().contains("10元")) {
                count++;
                infos.add(String.format(rollingTip, formatAccount(String.valueOf(prize.getAccountId())), prize.getPrizeName()));
            }
            if (count == 30) {
                break;
            }
        }
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小橙子布玩偶"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小熊双层煮蛋器"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小熊双层煮蛋器"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小熊双层煮蛋器"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小熊双层煮蛋器"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, "143***6", "先锋遥控落地扇"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, "21747***9", "先锋遥控落地扇"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, formatAccount(RandomStringUtils.randomNumeric(8)), "小熊滴漏式咖啡机"));
        infos.set(new Random().nextInt(infos.size() - 1), String.format(rollingTip, "201***5", "风行电视机58吋"));
        redisService.setForTimeCustom(RedisPrefix.ORANGE_SIGN_CODE + "_rolling_info", infos, 30, TimeUnit.SECONDS);
        return infos;
    }

    public String formatAccount(String accountId) {
        StringBuffer s = new StringBuffer(accountId);
        s.setCharAt(accountId.length() - 2, '*');
        s.setCharAt(accountId.length() - 3, '*');
        s.setCharAt(accountId.length() - 4, '*');
        return s.toString();
    }


}
