package com.funshion.activity.redpacket.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.redpacket.domain.RedPackRecord;
import com.funshion.activity.redpacket.domain.RedPackSummary;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface RedPackRecordMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select sum(amount) as total_amount, count(1) as total_num from fa_red_pack_record where activity_type=#{activityType}")
    RedPackSummary sumAmount(@Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where tv_id=#{tvId} and activity_type=#{activityType} order by id desc limit 100")
    List<RedPackRecord> getRecords(@Param("tvId") Integer tvId, @Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where tv_id=#{tvId} and activity_type=#{activityType} and DATEDIFF(NOW(),create_time)=0 order by id desc limit 100")
    List<RedPackRecord> getTodayRecords(@Param("tvId") Integer tvId, @Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where tv_id=#{tvId} and activity_type in (${activityType}) order by id desc limit 100")
    List<RedPackRecord> getRecordsWithActTypes(@Param("tvId") Integer tvId, @Param("activityType") String activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where id=#{id} ")
    RedPackRecord getRecordById(@Param("id") Integer id);


    @DataSource(DataSourceType.MASTER)
    @Insert("insert into fa_red_pack_record (template_id,activity_type,open_id,order_code,tv_id,amount,status,invalid_time,create_time) "
            + " values("
            + " #{templateId,jdbcType=INTEGER},"
            + " #{activityType,jdbcType=VARCHAR},"
            + " #{openId,jdbcType=VARCHAR},"
            + " #{orderCode,jdbcType=VARCHAR},"
            + " #{tvId,jdbcType=INTEGER},"
            + " #{amount,jdbcType=DECIMAL},"
            + " #{status,jdbcType=INTEGER},"
            + " #{invalidTime,jdbcType=TIMESTAMP},"
            + " now())")
    @SelectKey(before = false, keyProperty = "id", resultType = Integer.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    int addRecord(RedPackRecord record);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_red_pack_record set remarks=#{remarks} where id=#{id}")
    int addRemarks(@Param("id") Integer redPackId, @Param("remarks") String remarks);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_red_pack_record set p_code=#{pCode,jdbcType=VARCHAR}, g_code=#{gCode,jdbcType=VARCHAR}, open_id=#{openId,jdbcType=VARCHAR}," +
            " status=#{status,jdbcType=INTEGER}, update_time= now()  where id=#{id}")
    int updatePackRecord(RedPackRecord redPackRecord);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where tv_id=#{tvId} and activity_type=#{activityType} ")
    List<RedPackRecord> getRecordWithActivityType(@Param("activityType") Integer activityType, @Param("tvId") Integer tvId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where status=2 ")
    List<RedPackRecord> getSentRecords();

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_red_pack_record set status=#{status},update_time=now() where id=#{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") int status);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_red_pack_record set status=5 where status=1 and invalid_time < now()")
    int updateExpireRecords();

    @DataSource(DataSourceType.SLAVE)
    @Select("select count(*) from fa_red_pack_record where activity_type=#{activityType} and open_id=#{openId} and status>1")
    int getUserRecordNum(@Param("activityType") Integer activityType, @Param("openId") String openId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_record where activity_type=#{activityType} and open_id=#{openId} order by id desc limit 1")
    RedPackRecord getUserRecord(@Param("activityType") Integer activityType, @Param("openId") String openId);
}
