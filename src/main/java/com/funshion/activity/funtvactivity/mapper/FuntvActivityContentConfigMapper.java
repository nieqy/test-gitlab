package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityContentConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zhangfei on 2019/4/25.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityContentConfigMapper {

    @Select("select * " +
            "from fa_funtv_activity_content_config " +
            "where  activity_type=#{activityType} and is_deleted=0 and status=1 " +
            "order by show_seq desc, id desc " +
            "limit 1 ")
    FuntvActivityContentConfigInfo findConfig(Integer activityType);

    @Select("select * " +
            "from fa_funtv_activity_content_config " +
            "where  activity_type=#{activityType} and is_deleted=0 and status=1 and now() between start_time and end_time " +
            "order by show_seq asc, id desc " +
            "limit 1 ")
    FuntvActivityContentConfigInfo findConfig1(Integer activityType);

    @Select("select * " +
            "from fa_funtv_activity_content_config " +
            "where  id=#{id}")
    FuntvActivityContentConfigInfo findById(Integer id);
}
