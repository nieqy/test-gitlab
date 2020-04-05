package com.funshion.activity.funtvactivity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.common.utils.SendPhoneCodeUtils;
import com.funshion.activity.funtvactivity.common.FuntvActivityConstants;
import com.funshion.activity.funtvactivity.dto.FuntvActivityPrizeResponse;
import com.funshion.activity.funtvactivity.dto.FuntvActivityPrizeResponse.FuntvActivityPrizeFlagResponse;
import com.funshion.activity.funtvactivity.dto.FuntvActivityResponse;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccontInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccountPrizeInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityInfo;
import com.funshion.activity.funtvactivity.entity.FuntvPrototype;
import com.funshion.activity.funtvactivity.mapper.FuntvActivityMapper;
import com.funshion.activity.funtvactivity.mapper.FuntvPrototypeMapper;
import com.funshion.activity.job.LoadDataConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class FuntvActivityService {

    private static Logger logger = LoggerFactory.getLogger(FuntvActivityService.class);

    private static final String QRCODE_KEY = "bf0a4fd904c70b89";

    private final String ACTIVITY_REDIS_KEY = "funtv_activity_key_";

    @Value("${mercury.host}")
    private String mercuryHost;

    @Autowired
    private FuntvActivityMapper funtvActivityMapper;

    @Autowired
    private FuntvPrototypeMapper funtvPrototypeMapper;

    @Autowired
    private RedisService redisService;

    public Result<?> funtvActivityFirstPage(int activityType) {
        List<FuntvActivityInfo> infos = funtvActivityMapper.findFuntvActivity(activityType);
        List<FuntvActivityResponse> res = new ArrayList<>();
        for (FuntvActivityInfo info : infos) {
            FuntvActivityResponse temp = new FuntvActivityResponse();
            temp.setActivityMediaId(info.getActivityMediaId());
            temp.setActivityMediaName(info.getActivityMediaName());
            temp.setActivityMediaType(info.getActivityMediaType());
            temp.setActivityMediaPicture(ImgUtils.getImgPath(info.getActivityMediaPicture()));
            temp.setShowSeq(info.getShowSeq());
            res.add(temp);
        }
        return Result.getSuccessResult(res);
    }

    public Result<?> qrcode(String mac, Integer tvId, Integer activityType) {
        String key = MD5Utils.getMD5String(mac + tvId + activityType + QRCODE_KEY).substring(8, 24);
        logger.info("qrcode info - [mac:{},tvId:{}]", mac, tvId);
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        redisService.setForTimeCustom(ACTIVITY_REDIS_KEY + key, mac + "," + tvId + "," + activityType, 1, TimeUnit.DAYS);
        return Result.getSuccessResult(map);
    }

    public Result<?> checkUserInfo(String key) {
        String macsInfo = (String) redisService.get(ACTIVITY_REDIS_KEY + key);
        if (StringUtils.isBlank(macsInfo) || macsInfo.split(",").length != 3) {
            Map<String, String> map = new HashMap<>();
            map.put("msg", "参数有误！");
            map.put("tip", "请你在电视上重新扫描二维码。");
            return Result.getFailureResult("401", "failed.", map);
        }
        Date now = new Date();
        String[] macs = macsInfo.split(",");
        String mac = macs[0];
        Integer tvId = Integer.valueOf(macs[1]);
        Integer activityType = Integer.valueOf(macs[2]);
        Map<String, Object> res = new HashMap<>();
        res.put("mac", mac);
        res.put("tvId", tvId);
        String startTime = LoadDataConfig.getFuntvActivityData(activityType, "startTime", "2018-11-01");
        String endTime = LoadDataConfig.getFuntvActivityData(activityType, "endTime", "2018-11-30");
        long sTime = this.getSpecialDate(startTime);//2018/11/01
        long eTime = this.getSpecialDate(endTime);//2018/11/30
        if (now.getTime() < sTime || now.getTime() > eTime) {
            res.put("msg", "活动还未开始");
            res.put("tip", "快了解详情，准备参加吧");
            return Result.getFailureResult("405", "failed", res);
        }
		/*String beginTime = this.getMonthBeginTime(0, now);
		String endTime = this.getMonthBeginTime(1, now);*/
        FuntvPrototype prototype = funtvPrototypeMapper.getFuntvPrototypeByMac(mac);
        if (prototype != null && prototype.getStatus() == 1) {
            res.put("msg", "该电视是样机");
            res.put("tip", "快去实体店购买新电视，或告知亲友吧～");
            return Result.getFailureResult("405", "该电视是样机", res);
        }
        if (prototype != null && prototype.getStatus() == 2) {
            long activateTime = prototype.getUpdateTime().getTime();
            if (sTime > activateTime || eTime < activateTime) {
                res.put("msg", "抱歉！电视不在活动范围内");
                res.put("tip", "快去实体店购买新电视，或告知亲友吧～");
                return Result.getFailureResult("402", "failed", res);
            }
        }
        if (prototype == null) {
            int count = 0;
            if (tvId.toString().length() == 9) {
                count = funtvActivityMapper.findAccountInfo1(tvId, startTime, endTime);
            } else if (tvId.toString().length() == 7) {
                count = funtvActivityMapper.findAccountInfo2(tvId, startTime, endTime);
            }
            if (count == 0) {
                res.put("msg", "抱歉！电视不在活动范围内");
                res.put("tip", "快去实体店购买新电视，或告知亲友吧～");
                return Result.getFailureResult("402", "failed", res);
            }
        }
        String machineType = funtvActivityMapper.getMachineType(mac.replaceAll(":", ""));
        if (StringUtils.isBlank(machineType)) {
            res.put("msg", "抱歉！电视不在活动范围内");
            res.put("tip", "快去实体店购买新电视，或告知亲友吧～");
            return Result.getFailureResult("403", "mac不存在", res);
        }
        if (FuntvActivityConstants.NON_ACTIVITY_MACHINE_TYPE.contains(machineType.trim().toUpperCase())) {
            res.put("msg", "抱歉！电视不在活动范围内");
            res.put("tip", "快去实体店购买新电视，或告知亲友吧～");
            return Result.getFailureResult("404", "该机型不能参加活动", res);
        }
        FuntvActivityAccontInfo accountInfo = funtvActivityMapper.findFuntvActivityAccount(null, null, mac, activityType);
        if (accountInfo != null && accountInfo.getId() != null && accountInfo.getId() > 0) {
            res.put("msg", "本台电视已经成功参加");
            res.put("tip", "参与的手机号：" + accountInfo.getPhone());
            return Result.getSuccessResult("201", "本台电视已经成功参加", res);
        }
        return Result.getSuccessResult(res);
    }

    private String getMonthBeginTime(int monthAmount, Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, monthAmount);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    private long getMonthBeginTimestamp(int monthAmount, Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, monthAmount);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime().getTime();
    }

    public Result<?> accountInfo(Integer tvId, FuntvActivityAccontInfo info) {
        //info.setAccountId(tvId);
        funtvActivityMapper.updateFuntvActivityAccount(info);
        return Result.getSuccessResult();
    }

    public Result<?> findAccountInfo(Integer tvId, String mac, Integer activityType) {
        FuntvActivityAccontInfo info = funtvActivityMapper.findFuntvActivityAccount(null, null, mac, activityType);
        if (info == null) {
            return Result.getFailureResult("450", "用户信息不存在");
        }
        Map<String, String> map = new HashMap<>();
        map.put("receivingName", info.getReceivingName());
        map.put("receivingPhone", info.getReceivingPhone());
        map.put("receivingAddress", info.getReceivingAddress());
        return Result.getSuccessResult(map);
    }

    public Result<?> accountInfo(String verifyCode, Integer tvId, FuntvActivityAccontInfo info) {
        int count;
        count = funtvActivityMapper.countFuntvActivityAccount(info.getPhone(), null, null, info.getActivityType());
        if (count > 0) {
            return Result.getFailureResult("420", "该手机号已经参加了活动");
        }
        count = funtvActivityMapper.countFuntvActivityAccount(null, null, info.getMac(), info.getActivityType());
        if (count > 0) {
            return Result.getFailureResult("421", "该电视已经参加了活动");
        }
        info.setAccountId(tvId);
        logger.info(JSON.toJSONString(info));
        funtvActivityMapper.addFuntvActivityAccount(info);
        return Result.getSuccessResult();
    }

    public Result<?> sendMessage(String phone) throws Exception {
        String content = "尊敬的用户，您正在参加\"买风行开宝马\"活动，验证码为：%verifycode%，有效期30分钟。";
        String res = SendPhoneCodeUtils.sendMsg1(phone, content, "funtv", "assfasdg");
        JSONObject json = JSON.parseObject(res);
        String retCode = json.getString("retcode");
        String retMsg = "发送验证码超过次数限制,请1小时后重试";
        if ("200".equals(retCode)) {
            retMsg = "ok.";
        }
        return Result.getSuccessResult(retCode, retMsg);
    }

    public Result<?> config(Integer activityType) {
        Map<String, String> configData = LoadDataConfig.getFuntvActivityData(activityType);
        return Result.getSuccessResult(configData);
    }

    public Result<?> getPrizeInfo(Integer activityType, String prizeFlag) {
        List<String> prizeFlags = funtvActivityMapper.getFuntvActivityPrizeFlag(activityType);
        if (prizeFlags == null || prizeFlags.size() == 0) {
            return Result.getFailureResult("440", "暂未开奖，敬请期待");
        }
        if (StringUtils.isBlank(prizeFlag)) {
            prizeFlag = prizeFlags.get(0);
        }
        int currentIndex = this.getFuntvActivityPrizeFlag(prizeFlags, prizeFlag);
        if (currentIndex == -1) {
            return Result.getFailureResult("440", "暂未开奖，敬请期待");
        }
        FuntvActivityPrizeFlagResponse prizeFlagRes = new FuntvActivityPrizeFlagResponse();
        prizeFlagRes.setCurrent(prizeFlags.get(currentIndex));
        if (currentIndex - 1 >= 0) {
            prizeFlagRes.setNext(prizeFlags.get(currentIndex - 1));
        }
        if (currentIndex + 1 <= prizeFlags.size() - 1) {
            prizeFlagRes.setPre(prizeFlags.get(currentIndex + 1));
        }
        List<FuntvActivityAccountPrizeInfo> prizeInfos = funtvActivityMapper.findAllFuntvActivityAccountPrize(activityType, prizeFlag);
        if (prizeInfos == null || prizeInfos.size() == 0) {
            return Result.getFailureResult("440", "暂未开奖，敬请期待");
        }
        List<FuntvActivityPrizeResponse> res = new ArrayList<>();
        for (FuntvActivityAccountPrizeInfo prizeInfo : prizeInfos) {
            FuntvActivityPrizeResponse info = this.getFuntvActivityPrizeResponse(res, prizeInfo.getPrizeType());
            if (info == null) {
                info = new FuntvActivityPrizeResponse();
                info.setPrizeType(prizeInfo.getPrizeType());
                res.add(info);
            }
            info.getPhone().add(prizeInfo.getPhone().replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
        }
        Collections.sort(res);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("prizeFlagInfo", prizeFlagRes);
        resMap.put("prizeInfo", res);
        return Result.getSuccessResult(resMap);
    }

    public int getFuntvActivityPrizeFlag(List<String> prizeFlags, String prizeFlag) {
        int index = 0;
        for (String flag : prizeFlags) {
            if (flag.equals(prizeFlag)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private FuntvActivityPrizeResponse getFuntvActivityPrizeResponse(List<FuntvActivityPrizeResponse> res, Integer prizeType) {
        for (FuntvActivityPrizeResponse info : res) {
            if (info.getPrizeType().equals(prizeType)) {
                return info;
            }
        }
        return null;
    }

    public Result<?> getUserPrizeInfo(Integer accountId, String mac, Integer activityType) {
        FuntvActivityAccountPrizeInfo prizeInfo = funtvActivityMapper.findFuntvActivityAccountPrize(null, mac, activityType);
        if (prizeInfo == null || prizeInfo.getPrizeType() == null) {
            return Result.getFailureResult("430", "未中奖");
        }
        FuntvActivityAccontInfo accountInfo = funtvActivityMapper.findFuntvActivityAccount(null, null, mac, activityType);
        Map<String, Object> res = new HashMap<>();
        res.put("prizeFlag", prizeInfo.getPrizeFlag());
        res.put("prizeType", prizeInfo.getPrizeType());
        res.put("phone", accountInfo.getPhone());
        res.put("isAccept", prizeInfo.getIsAccept());
        return Result.getSuccessResult(res);
    }

    public Result<?> acceptPrize(Integer accountId, String mac, Integer activityType, String prizeFlag) {
        FuntvActivityAccountPrizeInfo prizeInfo = funtvActivityMapper.findFuntvActivityAccountPrize(null, mac, activityType);
        if (prizeInfo == null || prizeInfo.getPrizeType() == null) {
            return Result.getFailureResult("430", "未中奖");
        }
        if (prizeInfo.getPrizeType().equals(5)) {
            if (prizeInfo.getIsAccept().equals(0)) {
                funtvActivityMapper.acceptPrize(null, mac, activityType, prizeFlag);
            }
            return Result.getSuccessResult("恭喜你，赢得一等奖：宝马一辆<br/>请电话联系：400-600-6258  获知如何领奖");
        }
        if (prizeInfo.getPrizeType().equals(4)) {
            if (prizeInfo.getIsAccept().equals(0)) {
                return Result.getSuccessResult("恭喜你，赢得二等奖：电瓶车一辆<br/>请扫码填写地址和电话<br/>我们将在15个工作日内邮寄给您");
            }
            if (prizeInfo.getIsAccept().equals(1)) {
                return Result.getSuccessResult("您已提交收货地址，我们将在15个工作日内邮寄给您。<br/>如有疑问请联系：400-600-6258");
            }
        }
        if (prizeInfo.getPrizeType().equals(3)) {
            if (prizeInfo.getIsAccept().equals(0)) {
                String acceptRes = this.acceptFromMercury(accountId, 303);
                JSONObject json = JSON.parseObject(acceptRes);
                int retCode = json.getIntValue("retCode");
                if (retCode == 200) {
                    funtvActivityMapper.acceptPrize(null, mac, activityType, prizeFlag);
                    return Result.getSuccessResult("恭喜你，赢得三等奖：风行电视金卡会员年卡一张<br/>奖品将直接赠送到本台电视，可前往“我的”查看");
                } else {
                    return Result.getFailureResult("431", "领奖失败，请联系：400-600-6258");
                }
            }
            if (prizeInfo.getIsAccept().equals(1)) {
                return Result.getSuccessResult("恭喜你，赢得三等奖：风行电视金卡会员年卡一张<br/>奖品已直接赠送到本台电视，可前往“我的”查看");
            }
        }
        return Result.getSuccessResult();
    }

    private String acceptFromMercury(Integer accountId, Integer commodityId) {
        StringBuffer sb = new StringBuffer();
        String ctime = System.currentTimeMillis() + "";
        String key = "201f1696bea924252cbe0a9324576c2f";
        sb.append(mercuryHost).append("/api/summer/movie/give").append("?").append("accountId=").append(accountId)
                .append("&").append("ctime=").append(ctime).append("&");
        sb.append("commodityId=").append(commodityId).append("&").append("sign=").append(MD5Utils.getStringMD5String(accountId + ctime + commodityId + key));
        //请求会员接口  accountId, commityId, ctime, sign
        try {
            String acceptResponse = HttpClientUtils.get(sb.toString());
            return acceptResponse;
        } catch (Exception e) {
            logger.error("领奖失败: {} - {}", accountId, e);
        }
        return null;
    }

    private long getSpecialDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        return d.getTime();
    }

}
