package com.funshion.activity.jinli.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.jinli.entity.AccountPriviligeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@DataSource(DataSourceType.SLAVE)
public interface AccountPriviligeMapper {

    @DataSource(DataSourceType.MEMBER_SLAVE_01)
    @Select(" select tv_id,package_id,valid_start_time,valid_end_time from fm_vip_account_privilege where tv_id=#{tvId}  and package_id = #{packageId} ")
    public AccountPriviligeInfo findAccountPriviligeInfo(@Param("tvId") Integer tvId,
                                                         @Param("packageId") Integer packageId);
}
