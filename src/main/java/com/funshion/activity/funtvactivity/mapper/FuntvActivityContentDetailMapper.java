package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityContentDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityContentDetailMapper {

    @Select("select  * from  fa_funtv_activity_content_detail " +
            "where content_id=#{contentId} and status=1 " +
            "order by show_seq asc ")
    List<FuntvActivityContentDetailInfo> findAll(Integer contentId);

}
