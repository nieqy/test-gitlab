package com.funshion.activity.orange.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.orange.entity.ActivitySignInfo;
import com.funshion.activity.orange.entity.AppSignInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
@DataSource(DataSourceType.MASTER)
public interface AppSignInfoMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_app_sign_info where sign_id= 10000 order by id desc limit 30 ")
    List<AppSignInfo> selectLastSign30();

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_app_sign_info where sign_id= 10000 and account_id = #{accountId} order by id desc limit 1 ")
    AppSignInfo selectLastSignHistory(@Param("accountId") String accountId);

    @DataSource(DataSourceType.MASTER)
    @Insert("insert into fa_app_sign_info (account_id,sign_id,create_time,is_give,total) values (#{accountId},#{signId},#{createTime},#{isGive},#{total} ) ")
    void insertSignHistory(AppSignInfo info);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_app_sign_info  set is_give = 1 where id = #{id} ")
    void updateSignHistory(@Param("id") Integer id);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_app_sign_info where account_id=#{accountId} and sign_id=#{signId} order by id desc limit 1 ")
    ActivitySignInfo getLastSignRecord(@Param("accountId") String accountId, @Param("signId") Integer signId);

    @Update("update fa_app_sign_info  set total=total+1, update_time=now() where id = #{id} ")
    int updateSignTotal(@Param("id") Integer id);

    @DataSource(DataSourceType.SLAVE)
    @Select("select count(*) from fa_app_sign_info where account_id=#{accountId} and sign_id in (${activityType}) and (DATEDIFF(now(),create_time)=0 or DATEDIFF(now(),update_time)=0) ")
    int getTravelSignedCount(@Param("accountId") Integer accountId, @Param("activityType") String activityType);
}
