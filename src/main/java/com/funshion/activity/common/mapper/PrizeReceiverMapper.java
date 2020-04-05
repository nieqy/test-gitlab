package com.funshion.activity.common.mapper;

import com.funshion.activity.common.bean.PrizeReceiver;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PrizeReceiverMapper {

    @Insert("insert into fa_common_prize_receiver (win_id,activity_type,account_id,mac,prize_name,prize_img,prize_type,consignee,phone,address,create_time) values ("
            + " #{winId,jdbcType=INTEGER}, "
            + " #{activityType,jdbcType=INTEGER}, "
            + " #{accountId,jdbcType=INTEGER}, "
            + " #{mac,jdbcType=VARCHAR}, "
            + " #{prizeName,jdbcType=VARCHAR}, "
            + " #{prizeImg,jdbcType=VARCHAR}, "
            + " #{prizeType,jdbcType=VARCHAR}, "
            + " #{consignee,jdbcType=VARCHAR}, "
            + " #{phone,jdbcType=VARCHAR}, "
            + " #{address,jdbcType=VARCHAR}, "
            + " now())")
    int savePrizeReceiver(PrizeReceiver info);

    @DataSource(DataSourceType.SLAVE)
    @Select(" select * from fa_common_prize_receiver where win_id=#{winId} order by id desc limit 1")
    PrizeReceiver getAddress(@Param("winId") Integer winId);

}
