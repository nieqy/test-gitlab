package com.funshion.activity.jinli.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.jinli.entity.JinliWinRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface JinliWinRecordMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_jinli_win_record where account_id=#{accountId} and create_time >'2019-04-20' order by create_time desc limit 100")
    public List<JinliWinRecord> getMyWinRecords(@Param("accountId") Integer accountId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_jinli_win_record where id=#{id} ")
    public JinliWinRecord getWinRecordById(@Param("id") Integer id);

    @Insert("insert into  fa_jinli_win_record (account_id,mac,prize_type,prize_id,prize_name,prize_img,status,invalid_time,create_time) values ("
            + " #{accountId,jdbcType=INTEGER},"
            + " #{mac,jdbcType=VARCHAR},"
            + " #{prizeType,jdbcType=VARCHAR},"
            + " #{prizeId,jdbcType=VARCHAR},"
            + " #{prizeName,jdbcType=VARCHAR},"
            + " #{prizeImg,jdbcType=VARCHAR},"
            + " #{status,jdbcType=INTEGER},"
            + " #{invalidTime,jdbcType=TIMESTAMP},"
            + " now())")
    @SelectKey(before = false, keyProperty = "id", resultType = Integer.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    public int addWinRecord(JinliWinRecord info);

    @Update("update fa_jinli_win_record set status=1, update_time=now()  where id=#{id}")
    public int updateStatusById(@Param("id") Integer id);

    @Update("update fa_jinli_win_record set status=2 where status=0 and prize_type!='jinli' and invalid_time < now() ")
    public int updateExpiredStatus();

}
