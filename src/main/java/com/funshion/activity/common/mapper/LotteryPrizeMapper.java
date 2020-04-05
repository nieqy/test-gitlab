package com.funshion.activity.common.mapper;

import com.funshion.activity.common.bean.LotteryPrize;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityPrizeListInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface LotteryPrizeMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_common_lottery_prize where account_id=#{accountId} and activity_type=#{activityType} order by create_time desc limit 100")
    List<LotteryPrize> getMyPrizes(@Param("accountId") Integer accountId,
                                   @Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_common_lottery_prize where account_id=#{accountId} and activity_type=#{activityType} and DATEDIFF(NOW(),create_time)=0 order by create_time desc limit 100")
    List<LotteryPrize> getMyTodayPrizes(@Param("accountId") Integer accountId,
                                        @Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_common_lottery_prize where account_id=#{accountId} and activity_type in (${activityType}) order by create_time desc limit 100")
    List<LotteryPrize> getMyPrizesWithActTypes(@Param("accountId") Integer accountId,
                                               @Param("activityType") String activityType);


    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_common_lottery_prize where id=#{id}")
    LotteryPrize getWinRecordById(@Param("id") Integer id);

    @Insert("insert into  fa_common_lottery_prize (activity_type,account_id,lottery_id,mac,prize_type,prize_id,prize_name,prize_img,prize_url,status,invalid_time,create_time) values ("
            + " #{activityType,jdbcType=INTEGER},"
            + " #{accountId,jdbcType=INTEGER},"
            + " #{lotteryId,jdbcType=INTEGER},"
            + " #{mac,jdbcType=VARCHAR},"
            + " #{prizeType,jdbcType=VARCHAR},"
            + " #{prizeId,jdbcType=VARCHAR},"
            + " #{prizeName,jdbcType=VARCHAR},"
            + " #{prizeImg,jdbcType=VARCHAR},"
            + " #{prizeUrl,jdbcType=VARCHAR},"
            + " #{status,jdbcType=INTEGER},"
            + " #{invalidTime,jdbcType=TIMESTAMP},"
            + " now())")
    @SelectKey(before = false, keyProperty = "id", resultType = Integer.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    int addWinRecord(LotteryPrize info);

    @Update("update fa_common_lottery_prize set status=1, update_time=now()  where id=#{id}")
    int updateStatusById(@Param("id") Integer id);

    @Update("update fa_common_lottery_prize set status=2 where status=0 and prize_type!='jinli' and invalid_time < now() ")
    int updateExpiredStatus();

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_common_lottery_prize where activity_type = 10000  order by id desc limit 100 ")
    List<LotteryPrize> getWinRecords30();

    @Select("<script>" +
            "select * from fa_common_lottery_prize " +
            "where account_id=#{accountId} and activity_type=#{activityType} and lottery_id in " +
            "<foreach collection=\"list\" item=\"item\" index= \"index\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item} " +
            "</foreach>" +
            "</script>")
    List<LotteryPrize> getMyPrizes1(@Param("accountId") Integer accountId, @Param("activityType") Integer activityType, @Param("list") List<Integer> quizIds);

    @Select("select * from fa_common_lottery_prize " +
            "where account_id=#{accountId} and activity_type=#{activityType} and " +
            "lottery_id = 0 and prize_name in ('4','5')")
    List<LotteryPrize> getAccountFinalPrize(@Param("accountId") Integer accountId, @Param("activityType") Integer activityType);

    @Select("select a.id as content_id,a.content_name,a.start_time,a.end_time,group_concat(c.phone) phone,b.prize_name prize_type " +
            "from fa_common_lottery_prize b " +
            "inner join fa_funtv_activity_account c on c.account_id=b.account_id and b.activity_type=c.activity_type " +
            "right join fa_funtv_activity_content_config a on a.id=b.lottery_id and a.activity_type=b.activity_type " +
            "where  a.is_deleted=0 and a.status=1 and b.activity_type=#{activityType} " +
            "group by a.id,b.prize_name order by a.show_seq desc ")
    List<FuntvActivityPrizeListInfo> getAllPrize(@Param("activityType") Integer activityType);

    @Select("select a.prize_name prize_type, b.phone from fa_common_lottery_prize a, fa_funtv_activity_account b " +
            "where a.account_id=b.account_id and a.activity_type=#{activityType} and a.activity_type=b.activity_type " +
            "and a.lottery_id = 0 and a.prize_name in ('4','5') ")
    List<FuntvActivityPrizeListInfo.FuntvActivityFinalPrizeInfo> getAllFinalPrize(@Param("activityType") Integer activityType);
}
