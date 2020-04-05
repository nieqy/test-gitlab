package com.funshion.activity.jinli.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.jinli.entity.JinliEntryListDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface JinliEntryListDetailMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_jinli_entry_list_detail where list_id=#{listId} and status=2 order by show_seq desc")
    List<JinliEntryListDetail> getDetailsByListId(@Param("listId") Integer listId);

}