/**
 * JinliAddressService.java
 * com.funshion.activity.common.service
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年1月18日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.jinli.service;

import com.funshion.activity.common.bean.AddressInfo;
import com.funshion.activity.common.bean.AddressStatus;
import com.funshion.activity.common.constants.ActivityConstants;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.service.IAddressService;
import com.funshion.activity.jinli.constant.JinliConstants;
import com.funshion.activity.jinli.entity.JinliPrizeReceiver;
import com.funshion.activity.jinli.entity.JinliWinRecord;
import com.funshion.activity.jinli.mapper.JinliPrizeReceiverMapper;
import com.funshion.activity.jinli.mapper.JinliWinRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:JinliAddressService
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 *
 * @author xiaowei
 * @version
 * @since Ver 1.1
 * @Date 2019年1月18日        下午2:55:59
 *
 * @see
 *
 */
@Service
public class JinliAddressService implements IAddressService {

    @Autowired
    private JinliWinRecordMapper jinliWinRecordMapper;

    @Autowired
    private JinliPrizeReceiverMapper jinliPrizeReceiverMapper;

    @Override
    public String getActivityType() {
        return ActivityConstants.ActivityType.JINLI;
    }

    @Override
    public Result<?> saveAddress(AddressInfo info) {
        JinliWinRecord record = jinliWinRecordMapper.getWinRecordById(info.getSourceId());
        if (record == null || !info.getTvId().equals(record.getAccountId())) {
            return Result.getFailureResult("905", "未查到中奖记录");
        }

        if (record.getStatus() == JinliConstants.PrizeStatus.ACCEPT) {
            return Result.getFailureResult("906", "奖品已领取");
        } else if (record.getStatus() == JinliConstants.PrizeStatus.EXPIRED) {
            return Result.getFailureResult("907", "奖品已过期");
        }

        // 实物奖品
        JinliPrizeReceiver receiver = new JinliPrizeReceiver();
        receiver.setAccountId(info.getTvId());
        receiver.setMac(info.getMac());
        receiver.setWinId(info.getSourceId());
        receiver.setConsignee(info.getConsignee());
        receiver.setPhone(info.getPhone());
        receiver.setAddress(info.getAddress());
        receiver.setPrizeName(record.getPrizeName());
        receiver.setPrizeImg(record.getPrizeImg());
        receiver.setPrizeType(record.getPrizeType());
        jinliPrizeReceiverMapper.savePrizeReceiver(receiver);
        jinliWinRecordMapper.updateStatusById(info.getSourceId());
        return Result.getSuccessResult();
    }

    @Override
    public Result<?> getAddressStatus(AddressInfo info) {
        AddressStatus addressStatus = new AddressStatus();
        JinliPrizeReceiver receiverInfo = jinliPrizeReceiverMapper.getAddress(info.getSourceId());
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

