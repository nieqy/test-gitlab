package com.funshion.activity.orange.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.orange.entity.PrizeTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WechatPrizeTicketMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where account_id=#{accountId} and prize_id=#{prizeId}")
    List<PrizeTicket> getPrizeTickets(@Param("accountId") String accountId, @Param("prizeId") Integer prizeId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where account_id=#{accountId} and prize_id=#{prizeId} and status=2")
    List<PrizeTicket> getLuckyTickets(@Param("accountId") String accountId, @Param("prizeId") Integer prizeId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where prize_id=#{prizeId} and status>=2")
    List<PrizeTicket> getLuckyTicketsByPrizeId(@Param("prizeId") Integer prizeId);

    @Insert("insert into fa_prize_ticket (code,source,account_id,prize_id,icon,status,source_id,create_time) values (" +
            "#{code,jdbcType=VARCHAR}," +
            "#{source,jdbcType=VARCHAR}," +
            "#{accountId,jdbcType=VARCHAR}," +
            "#{prizeId,jdbcType=INTEGER}," +
            "#{icon,jdbcType=VARCHAR}," +
            "#{status,jdbcType=INTEGER}," +
            "#{sourceId,jdbcType=VARCHAR}," +
            "now())")
    int addTicket(PrizeTicket info);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where account_id=#{accountId} and source_id=#{sourceId}")
    List<PrizeTicket> getTicketsForInvitation(@Param("accountId") String accountId, @Param("sourceId") String sourceId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select distinct(prize_id) from fa_prize_ticket where account_id=#{openId}")
    List<Integer> getTicketsByAccountId(@Param("openId") String openId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where account_id=#{openId}")
    List<PrizeTicket> getPrizesByAccountId(@Param("openId") String openId);

    @Update("update fa_prize_ticket set status=#{status} where account_id=#{openId} and prize_id=#{prizeId} ")
    int updateStatus(@Param("openId") String openId, @Param("prizeId") Integer prizeId, @Param("status") Integer status);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_prize_ticket where account_id=#{openId} and status>=2")
    List<PrizeTicket> getWinPrizesByAccountId(@Param("openId") String openId);

}
