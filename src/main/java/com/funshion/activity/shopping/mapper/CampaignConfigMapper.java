package com.funshion.activity.shopping.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.shopping.domain.CampaignConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CampaignConfigMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_campaigns_config where activity_type=#{activityType} order by id desc limit 1")
    CampaignConfig getConfigInfo(@Param("activityType") String activityType);
}
