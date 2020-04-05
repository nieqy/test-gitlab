package com.funshion.activity.orange.service;

import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.RedisPrefix;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.orange.constants.OrangeConstants;
import com.funshion.activity.orange.domain.WechatPrizeSimpleInfo;
import com.funshion.activity.orange.entity.PrizeInfo;
import com.funshion.activity.orange.entity.PrizeReceiver;
import com.funshion.activity.orange.entity.PrizeTicket;
import com.funshion.activity.orange.mapper.WechatPrizeInfoMapper;
import com.funshion.activity.orange.mapper.WechatPrizeReceiverMapper;
import com.funshion.activity.orange.mapper.WechatPrizeTicketMapper;
import com.funshion.activity.orange.req.GainTicketReq;
import com.funshion.activity.orange.req.PrizeDetailReq;
import com.funshion.activity.orange.req.PrizeInfoReq;
import com.funshion.activity.orange.req.UploadAddressReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class WechatLotteryService {

    @Autowired
    private WechatPrizeInfoMapper wechatPrizeInfoMapper;

    @Autowired
    private WechatPrizeTicketMapper wechatPrizeTicketMapper;

    @Autowired
    private WechatPrizeReceiverMapper wechatPrizeReceiverMapper;

    @Autowired
    private RedisService redisService;

    public Result getPrizeInfos(PrizeInfoReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        Map<String, Object> data = new HashMap<>();
        List<PrizeInfo> prizeInfos = wechatPrizeInfoMapper.getValidPrizeInfos();
        if (CollectionUtils.isEmpty(prizeInfos)) {
            return Result.getSuccessResult(data);
        }

        List<Integer> prizeIds = null;
        if(StringUtils.isNotBlank(req.getOpenId())){
            prizeIds = wechatPrizeTicketMapper.getTicketsByAccountId(req.getOpenId());
        }
        for (PrizeInfo prizeInfo : prizeInfos) {
            prizeInfo.setPoster(ImgUtils.getImgUrl(prizeInfo.getPoster()));
            // 是否参与
            if (!CollectionUtils.isEmpty(prizeIds) && prizeIds.contains(prizeInfo.getId())) {
                prizeInfo.setStatus(1);
            } else {
                prizeInfo.setStatus(0);
            }
        }

        data.put("infos", prizeInfos);
        return Result.getSuccessResult(data);
    }

    public Result getPrizeDetails(PrizeDetailReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getPrizeId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        Map<String, Object> data = new HashMap<>();
        PrizeInfo prizeInfo = wechatPrizeInfoMapper.getPrizeInfoById(req.getPrizeId());
        List<String> images = new ArrayList<>();
        List<String> oids = wechatPrizeInfoMapper.getPrizePics(req.getPrizeId());
        for (String oid : oids) {
            images.add(ImgUtils.getImgUrl(oid));
        }

        // 我的抽奖券
        List<PrizeTicket> myTickets = wechatPrizeTicketMapper.getPrizeTickets(req.getOpenId(), req.getPrizeId());

        // 中奖用户奖券
        List<PrizeTicket> luckyTickets = new ArrayList<>();

        // 中奖用户信息
        Long totalNum = redisService.zCard(RedisPrefix.ORANGE_PRIZE_USER_INFO + req.getPrizeId());
        List<String> icons = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> tops = redisService.reverseRangeWithScores(RedisPrefix.ORANGE_PRIZE_USER_INFO + req.getPrizeId(), 0, 1000);
        if (!CollectionUtils.isEmpty(tops)) {
            for (ZSetOperations.TypedTuple tuple : tops) {
                if (tuple.getValue() != null) {
                    icons.add(StringUtils.substringAfter(tuple.getValue().toString(), "_"));
                }
            }
        }

        // 设置中奖状态
        int status = 0;
        if (prizeInfo.getLotteryTime().after(new Date())) {
            status = OrangeConstants.PrizeStatus.INIT;
        } else {
            luckyTickets = wechatPrizeTicketMapper.getLuckyTicketsByPrizeId(req.getPrizeId());
            status = OrangeConstants.PrizeStatus.LOSE;
            for (PrizeTicket myTicket : myTickets) {
                if (myTicket.getStatus() > OrangeConstants.PrizeStatus.INIT) {
                    status = myTicket.getStatus();
                    break;
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data.put("status", status);
        data.put("images", images);
        data.put("threshold", prizeInfo.getThreshold());
        data.put("name", prizeInfo.getName());
        data.put("lotteryType", prizeInfo.getLotteryType());
        data.put("endTime", sdf.format(prizeInfo.getEndTime()));
        data.put("lotteryTime", sdf.format(prizeInfo.getLotteryTime()));
        data.put("description", prizeInfo.getDescription());
        data.put("myTickets", myTickets);
        data.put("luckyTickets", luckyTickets);
        data.put("participantsNum", totalNum == null ? 0 : totalNum);
        data.put("participants", icons);
        return Result.getSuccessResult(data);
    }

    public Result gainTickets(GainTicketReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getPrizeId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        List<PrizeTicket> tickets = new ArrayList<>();
        // 场景类型。1：我要抽奖；2：看广告；3：接收好友邀请
        if (req.getScene() == 1) {
            List<String> codes = genTicketCodes(1);
            for (String code : codes) {
                PrizeTicket ticket = new PrizeTicket();
                ticket.setAccountId(req.getOpenId());
                ticket.setPrizeId(req.getPrizeId());
                ticket.setSource(ActivityConstants.TicketSource.ORIGINAL);
                ticket.setStatus(0);
                ticket.setCode(code);
                ticket.setIcon(req.getIcon());
                wechatPrizeTicketMapper.addTicket(ticket);
                tickets.add(ticket);
            }
        } else if (req.getScene() == 2) {
            List<String> codes = genTicketCodes(1);
            for (String code : codes) {
                PrizeTicket ticket = new PrizeTicket();
                ticket.setAccountId(req.getOpenId());
                ticket.setPrizeId(req.getPrizeId());
                ticket.setSource(ActivityConstants.TicketSource.AD);
                ticket.setStatus(0);
                ticket.setCode(code);
                ticket.setIcon(req.getIcon());
                wechatPrizeTicketMapper.addTicket(ticket);
                tickets.add(ticket);
            }
        } else if (req.getScene() == 3) {
            if (StringUtils.isBlank(req.getSourceId())) {
                return Result.HTTP_PARAMS_NOT_ENOUGH;
            }
            // 避免重复赠送
            List<PrizeTicket> historyTickets = wechatPrizeTicketMapper.getTicketsForInvitation(req.getSourceId(), req.getOpenId());
            if (!CollectionUtils.isEmpty(historyTickets)) {
                return Result.getSuccessResult();
            }
            // 接收邀请，给发出邀请的好友加三张券
            List<String> inviteCodes = genTicketCodes(3);
            for (String code : inviteCodes) {
                PrizeTicket ticket = new PrizeTicket();
                ticket.setCode(code);
                ticket.setAccountId(req.getSourceId());
                ticket.setPrizeId(req.getPrizeId());
                ticket.setSource(ActivityConstants.TicketSource.INVITATION);
                ticket.setStatus(0);
                ticket.setSourceId(req.getOpenId());
                ticket.setIcon(req.getIcon());
                wechatPrizeTicketMapper.addTicket(ticket);
            }
        } else {
            return Result.getSuccessResult();
        }
        String randomStr = req.getOpenId().substring(req.getOpenId().length() - 5).replaceAll("_", "");
        redisService.zAdd(RedisPrefix.ORANGE_PRIZE_USER_INFO + req.getPrizeId(), randomStr + "_" + req.getIcon(), new Date().getTime() / 1000);
        Map<String, Object> data = new HashMap<>();
        data.put("tickets", tickets);
        return Result.getSuccessResult(data);
    }

    public Result uploadAddress(UploadAddressReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getPrizeId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        PrizeInfo prizeInfo = wechatPrizeInfoMapper.getPrizeInfoById(req.getPrizeId());
        if (prizeInfo == null) {
            return Result.getFailureResult("905", "未查到中奖记录");
        }

        // 校验中奖券
        List<PrizeTicket> luckyTickets = wechatPrizeTicketMapper.getLuckyTickets(req.getOpenId(), req.getPrizeId());
        if (CollectionUtils.isEmpty(luckyTickets)) {
            return Result.getFailureResult("905", "未查到中奖记录");
        }

        PrizeReceiver info = new PrizeReceiver();
        info.setAccountId(req.getOpenId());
        info.setPrizeId(req.getPrizeId());
        info.setPrizeName(prizeInfo.getName());
        info.setPrizeImg(ImgUtils.getImgUrl(prizeInfo.getPoster()));
        info.setPrizeType("product");
        info.setConsignee(req.getConsignee());
        info.setPhone(req.getPhone());
        info.setAddress(req.getProvince() + req.getCity() + req.getCounty() + " " + req.getAddress());
        wechatPrizeReceiverMapper.insert(info);
        wechatPrizeTicketMapper.updateStatus(req.getOpenId(), req.getPrizeId(), OrangeConstants.PrizeStatus.ACCEPTED);
        return Result.getSuccessResult();
    }


    public Result history(PrizeInfoReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }
        List<PrizeTicket> joinPrizes = wechatPrizeTicketMapper.getPrizesByAccountId(req.getOpenId());
        if (CollectionUtils.isEmpty(joinPrizes)) {
            return Result.getSuccessResult();
        }

        Map<Integer, Integer> statusMap = new HashMap<>();
        List<Integer> prizeIds = new ArrayList<>();
        for (PrizeTicket joinPrize : joinPrizes) {
            if (!prizeIds.contains(joinPrize.getPrizeId())) {
                prizeIds.add(joinPrize.getPrizeId());
            }
            if (joinPrize.getStatus() > OrangeConstants.PrizeStatus.INIT) {
                statusMap.put(joinPrize.getPrizeId(), joinPrize.getStatus());
            }
        }
        List<WechatPrizeSimpleInfo> newPrizes = new ArrayList<>();
        List<WechatPrizeSimpleInfo> oldPrizes = new ArrayList<>();
        List<PrizeInfo> prizeInfos = wechatPrizeInfoMapper.getPrizesByIds(StringUtils.join(prizeIds, ","));
        for (PrizeInfo prizeInfo : prizeInfos) {
            // 待开奖
            WechatPrizeSimpleInfo info = new WechatPrizeSimpleInfo();
            info.setId(prizeInfo.getId());
            info.setName(prizeInfo.getName());
            info.setLotteryType(prizeInfo.getLotteryType());
            info.setLotteryTime(prizeInfo.getLotteryTime());
            info.setPoster(ImgUtils.getImgUrl(prizeInfo.getPoster()));
            info.setEndTime(prizeInfo.getEndTime());
            if (prizeInfo.getLotteryTime().after(new Date())) {
                info.setStatus(OrangeConstants.PrizeStatus.INIT);
                newPrizes.add(info);
                continue;
            }

            // 已开奖
            Integer status = statusMap.get(prizeInfo.getId());
            // 未中奖
            if (status == null) {
                info.setStatus(OrangeConstants.PrizeStatus.LOSE);
                oldPrizes.add(info);
            }
            // 中奖
            else {
                info.setStatus(status);
                oldPrizes.add(info);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("newPrizes", newPrizes);
        data.put("oldPrizes", oldPrizes);
        return Result.getSuccessResult(data);
    }

    public Result getWinList(PrizeInfoReq req) {
        if (!req.getSign().equals(MD5Utils.getMD5String(req.getOpenId() + req.getCtime() + ActivityConstants.SignKey.MERCURY_SIGN))) {
            return Result.HTTP_RESPONSE_UNAUTHORIZED;
        }

        List<PrizeTicket> winPrizes = wechatPrizeTicketMapper.getWinPrizesByAccountId(req.getOpenId());
        if (CollectionUtils.isEmpty(winPrizes)) {
            return Result.getSuccessResult();
        }

        Map<Integer, Integer> statusMap = new HashMap<>();
        List<Integer> prizeIds = new ArrayList<>();
        for (PrizeTicket winPrize : winPrizes) {
            if (!prizeIds.contains(winPrize.getPrizeId())) {
                prizeIds.add(winPrize.getPrizeId());
            }
            statusMap.put(winPrize.getPrizeId(), winPrize.getStatus());
        }

        List<WechatPrizeSimpleInfo> prizes = new ArrayList<>();
        List<PrizeInfo> prizeInfos = wechatPrizeInfoMapper.getPrizesByIds(StringUtils.join(prizeIds, ","));
        for (PrizeInfo prizeInfo : prizeInfos) {
            WechatPrizeSimpleInfo prize = new WechatPrizeSimpleInfo();
            prize.setId(prizeInfo.getId());
            prize.setName(prizeInfo.getName());
            prize.setLotteryType(prizeInfo.getLotteryType());
            prize.setLotteryTime(prizeInfo.getLotteryTime());
            prize.setPoster(ImgUtils.getImgUrl(prizeInfo.getPoster()));
            prize.setStatus(statusMap.get(prizeInfo.getId()));
            prize.setEndTime(prizeInfo.getEndTime());
            prizes.add(prize);
        }

        Collections.sort(prizes);
        Map<String, Object> data = new HashMap<>();
        data.put("prizes", prizes);
        return Result.getSuccessResult(data);
    }

    private List<String> genTicketCodes(int count) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String code = RandomStringUtils.randomAlphanumeric(2) + RandomStringUtils.randomNumeric(10);
            codes.add(code.toUpperCase());
        }
        return codes;
    }

    public Result login(String code, String ed, String iv, String app_code) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(ed) || StringUtils.isBlank(iv) || StringUtils.isBlank(app_code)) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(OrangeConstants.WECHAT_LOGIN)
                    .append("?code=").append(URLEncoder.encode(code, "utf-8"))
                    .append("&ed=").append(URLEncoder.encode(ed, "utf-8"))
                    .append("&iv=").append(URLEncoder.encode(iv, "utf-8"))
                    .append("&app_code=").append(URLEncoder.encode(app_code, "utf-8"));
            String getString = sb.toString();
            String res = HttpClientUtils.get(getString);
            if (StringUtils.isBlank(res)) {
                log.warn(getString);
                return Result.getFailureResult("400", "登录失败");
            }

            JSONObject jo = JSONObject.parseObject(res);
            if (!"200".equals(jo.getString("code"))) {
                log.warn("[req]: " + getString + "  [rsp]:" + res);
                return Result.getFailureResult(jo.getString("code"), jo.getString("msg"));
            }

            jo.remove("code");
            jo.remove("msg");
            return Result.getSuccessResult(jo);
        } catch (Exception e) {
            return Result.getFailureResult("400", "登录失败");
        }
    }
}
