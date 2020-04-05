package com.funshion.activity.dataconfig.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.dataconfig.entity.ActivityDataConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DataSource(DataSourceType.SLAVE)
public interface ActivityDataConfigMapper {

    @Select("select * from fa_data_config where is_deleted=0 and status=1")
    List<ActivityDataConfigInfo> findAllData();

}
