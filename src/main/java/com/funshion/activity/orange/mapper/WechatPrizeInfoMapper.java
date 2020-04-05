package com.funshion.activity.orange.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.orange.entity.PrizeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WechatPrizeInfoMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_info where status=2 and start_time<now() and end_time>now() order by priority desc")
    List<PrizeInfo> getValidPrizeInfos();

    @DataSource(DataSourceType.SLAVE)
    @Select("select icon from fa_prize_pic where list_id=#{listId} and status=2 order by show_seq desc")
    List<String> getPrizePics(@Param("listId") Integer listId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_info where id=#{prizeId}")
    PrizeInfo getPrizeInfoById(@Param("prizeId") Integer prizeId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_info where id in (${ids})")
    List<PrizeInfo> getPrizesByIds(@Param("ids") String ids);
}
