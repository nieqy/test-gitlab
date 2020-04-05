package com.funshion.activity.jinli.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.jinli.entity.JinliPrizeReceiver;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface JinliPrizeReceiverMapper {

    @Insert("insert into fa_jinli_prize_receiver (win_id,account_id,mac,prize_name,prize_img,prize_type,consignee,phone,address,create_time) values ("
            + " #{winId,jdbcType=INTEGER}, "
            + " #{accountId,jdbcType=INTEGER}, "
            + " #{mac,jdbcType=VARCHAR}, "
            + " #{prizeName,jdbcType=VARCHAR}, "
            + " #{prizeImg,jdbcType=VARCHAR}, "
            + " #{prizeType,jdbcType=VARCHAR}, "
            + " #{consignee,jdbcType=VARCHAR}, "
            + " #{phone,jdbcType=VARCHAR}, "
            + " #{address,jdbcType=VARCHAR}, "
            + " now())")
    public int savePrizeReceiver(JinliPrizeReceiver info);

    @DataSource(DataSourceType.SLAVE)
    @Select(" select * from fa_jinli_prize_receiver where win_id=#{winId} order by id desc limit 1")
    JinliPrizeReceiver getAddress(@Param("winId") Integer winId);

}
