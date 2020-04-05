package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
public interface FmCommodityMapper {

    @DataSource(DataSourceType.MEMBER_SLAVE_01)
    @Select("select * from fm_commodity where commodity_id=#{commodityId}")
    Map<String, Object> findCommodityById(Integer commodityId);

}
