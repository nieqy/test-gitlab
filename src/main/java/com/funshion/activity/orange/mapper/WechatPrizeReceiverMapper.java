package com.funshion.activity.orange.mapper;

import com.funshion.activity.orange.entity.PrizeReceiver;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatPrizeReceiverMapper {

    @Insert("insert into fa_prize_receiver (account_id,prize_id,prize_name,prize_img,prize_type,consignee,phone,address,create_time) values (" +
            "#{accountId},#{prizeId},#{prizeName},#{prizeImg},#{prizeType},#{consignee},#{phone},#{address},now())")
    int insert(PrizeReceiver info);

}
