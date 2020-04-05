/**
 * DefaultAddressService.java
 * com.funshion.activity.common.service
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年4月23日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.common.service;

import com.funshion.activity.common.bean.AddressInfo;
import com.funshion.activity.common.bean.AddressStatus;
import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.bean.PrizeReceiver;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.mapper.LotteryPrizeMapper;
import com.funshion.activity.common.mapper.PrizeReceiverMapper;
import com.funshion.activity.common.utils.AESUtils;
import com.funshion.activity.common.utils.SendPhoneCodeUtils;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.job.LoadDataConfig;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaowei
 * @ClassName:DefaultAddressService
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2019年4月23日        上午10:36:24
 * @see
 * @since Ver 1.1
 */
@Service
public class DefaultAddressService implements IAddressService {

    @Autowired
    private LotteryPrizeMapper lotteryPrizeMapper;

    @Autowired
    private PrizeReceiverMapper prizeReceiverMapper;

    @Override
    public String getActivityType() {
        return ActivityConstants.ActivityType.DEFAULT;
    }

    @Override
    public Result<?> saveAddress(AddressInfo info) {
        LotteryPrize record = lotteryPrizeMapper.getWinRecordById(info.getSourceId());
        if (record == null || !info.getTvId().equals(record.getAccountId())) {
            return Result.getFailureResult("905", "未查到中奖记录");
        }

        if (record.getStatus() == JinliConstants.PrizeStatus.ACCEPT) {
            return Result.getFailureResult("906", "奖品已领取");
        } else if (record.getStatus() == JinliConstants.PrizeStatus.EXPIRED) {
            return Result.getFailureResult("907", "奖品已过期");
        }

        // 实物奖品
        PrizeReceiver receiver = new PrizeReceiver();
        receiver.setActivityType(NumberUtils.toInt(info.getActivityType(), 0));
        receiver.setAccountId(info.getTvId());
        receiver.setMac(info.getMac());
        receiver.setWinId(info.getSourceId());
        receiver.setConsignee(info.getConsignee());
        receiver.setPhone(info.getPhone());
        receiver.setAddress(info.getAddress());
        receiver.setPrizeName(record.getPrizeName());
        receiver.setPrizeImg(record.getPrizeImg());
        receiver.setPrizeType(record.getPrizeType());
        prizeReceiverMapper.savePrizeReceiver(receiver);
        lotteryPrizeMapper.updateStatusById(info.getSourceId());
        if ("10000".equals(info.getActivityType())) {
            try {
                String key = new String(AESUtils.decrypt(AESUtils.parseHexStr2Byte(LoadDataConfig.getDataValueByKey("funtv.activity.text.orange")), ActivityConstants.SignKey.DATABASE_SIGN));
                SendPhoneCodeUtils.sendPlainMsg(info.getPhone(), String.format("您在“签到7天抽大奖”抽中的奖品“%s”已领奖成功，我们会尽快发送奖品，关注“风行橙子”微信公众号了解活动动态", record.getPrizeName()), "orange", key);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Result.getSuccessResult();
    }

    @Override
    public Result<?> getAddressStatus(AddressInfo info) {
        AddressStatus addressStatus = new AddressStatus();
        PrizeReceiver receiverInfo = prizeReceiverMapper.getAddress(info.getSourceId());
        if (receiverInfo == null) {
            addressStatus.setStatus(0);
        } else {
            addressStatus.setStatus(1);
            addressStatus.setPhone(receiverInfo.getPhone());
            addressStatus.setAddress(receiverInfo.getAddress());
            addressStatus.setConsignee(receiverInfo.getConsignee());
        }

        return Result.getSuccessResult(addressStatus);
    }

}

