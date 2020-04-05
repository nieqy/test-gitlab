package com.funshion.activity.annualreport.mapper;

import com.funshion.activity.annualreport.entity.AnnualReportInfo;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.MASTER_SLAVE_01)
public interface AnnualReportMapper {

	/*@TargetDataSource(DataSourceType.MASTER_SLAVE_01)
	@Insert("<script>" +
			"insert into fa_funtv_annual_report" +
			"(mac,annual_info) values " +
			"<foreach collection=\"list\" item=\"item\" index= \"index\" separator =\",\">" +
			"(#{item.mac},#{item.annualInfo})" +
			"</foreach>" +
			"</script>")
	void add(@Param("list") List<AnnualReportInfo> list);*/

    @Select("select * from fa_funtv_annual_report " +
            "where mac=#{mac} limit 1")
    AnnualReportInfo findInfo(@Param("mac") String mac);

}
