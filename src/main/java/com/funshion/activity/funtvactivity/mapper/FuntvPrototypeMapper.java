package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvPrototype;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvPrototypeMapper {

    @DataSource(DataSourceType.MEMBER_SLAVE_01)
    @Select("select * from fm_tv_prototype where mac=#{mac}")
    FuntvPrototype getFuntvPrototypeByMac(@Param("mac") String mac);
}
