package com.funshion.activity.jinli.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.jinli.entity.JinliEntryListInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface JinliEntryListMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select(" select bg_img from fa_jinli_entry_list where type='topImg' and status=2 limit 1")
    String getTopBgImg();

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_jinli_entry_list where type='programList' and status=2 order by show_seq asc")
    List<JinliEntryListInfo> getEntryList();
}