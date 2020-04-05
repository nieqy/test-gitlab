package com.funshion.activity.pullalive.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.pullalive.domain.SignConfig;
import com.funshion.activity.pullalive.domain.SignConfigDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SignConfigMapper {
    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_sign_entry_list where activity_type=#{activityType} and status =2 order by id desc limit 1")
    SignConfig getConfigInfo(@Param("activityType") Integer activityType);


    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_sign_entry_list_detail where list_id=#{listId} and status =2 order by show_seq desc")
    List<SignConfigDetail> getConfigDetails(@Param("listId") Integer listId);

    @DataSource(DataSourceType.SLAVE)
    @Select("select d.activity_type from fa_sign_entry_list_detail d, fa_sign_entry_list l where l.activity_type=#{activityType}" +
            " and d.list_id=l.id and  d.status =2 ")
    List<Integer> getActivityTypes(@Param("activityType") Integer activityType);
}
