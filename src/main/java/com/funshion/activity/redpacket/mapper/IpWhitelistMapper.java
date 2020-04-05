package com.funshion.activity.redpacket.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.redpacket.domain.IpWhiteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IpWhitelistMapper {
    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_ip_white_list where type=#{type} and ip=#{ip} limit 1")
    IpWhiteList getRecord(@Param("type") String type, @Param("ip") String ip);
}
