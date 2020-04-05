package com.funshion.activity.redpacket.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.redpacket.domain.RedPackTemplate;
import com.funshion.activity.redpacket.domain.RedPackTemplateDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface RedPackTemplateMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_template where id=#{id} and status = 2")
    RedPackTemplate getTemplateById(@Param("id") Integer id);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_red_pack_template_detail where template_id=#{templateId} and status = 2")
    List<RedPackTemplateDetail> getTemplateDetailsById(@Param("templateId") Integer templateId);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_red_pack_template_detail set stock=stock-1 where id=#{id} and stock > 0")
    int updateStockById(@Param("id") Integer id);
}
