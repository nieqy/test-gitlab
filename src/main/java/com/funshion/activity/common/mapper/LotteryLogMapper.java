package com.funshion.activity.common.mapper;

import com.funshion.activity.common.bean.LotteryLog;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LotteryLogMapper {
    @Insert("insert into fa_lottery_log (tv_id,activity_type,remarks,create_time) values ("
            + " #{tvId,jdbcType=INTEGER}, "
            + " #{activityType,jdbcType=INTEGER}, "
            + " #{remarks,jdbcType=VARCHAR}, "
            + " now())")
    int saveLotteryLog(LotteryLog info);

    @DataSource(DataSourceType.SLAVE)
    @Select(" select * from fa_lottery_log where tv_id=#{tvId} and activity_type =#{activityType} order by id desc")
    List<LotteryLog> getLotteryLog(@Param("tvId") Integer tvId, @Param("activityType") Integer activityType);

    @DataSource(DataSourceType.SLAVE)
    @Select(" select * from fa_lottery_log where tv_id=#{tvId} and activity_type =#{activityType} and DATEDIFF(NOW(),create_time)=0 order by id desc")
    List<LotteryLog> getTodayLotteryLog(@Param("tvId") Integer tvId, @Param("activityType") Integer activityType);

}
