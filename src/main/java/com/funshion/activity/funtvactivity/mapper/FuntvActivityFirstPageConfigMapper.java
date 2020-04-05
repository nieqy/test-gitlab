package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo.FuntvActivityFirstPageFloorInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo.FuntvActivityFirstPageItemInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityFirstPageConfigMapper {

	/*@Select("select * " +
			"from fa_funtv_movie_firstpage_config " +
			"where  is_deleted=0 order by activity_type desc,parent_id asc, data_value_seq asc")
	List<ActivityMovieFirstPageConfigInfo> findAllMovieFirstPageConfig();*/

    @Select("select * from fa_funtv_activity_firstpage_config " +
            "where activity_type=#{activityType} and status=1 and is_deleted=0 and now() between start_time and end_time " +
            "limit 1")
    FuntvActivityFirstPageInfo findActivity(@Param("activityType") Integer activityType);

    @Select("select * from fa_funtv_activity_firstpage_config " +
            "where activity_type=#{activityType} and status=1 and is_deleted=0 " +
            "limit 1")
    FuntvActivityFirstPageInfo findActivity1(@Param("activityType") Integer activityType);

    @Select("select * from fa_funtv_activity_firstpage_floor " +
            "where activity_id=#{activityId} and status=1 and is_deleted=0 order by show_seq asc")
    List<FuntvActivityFirstPageFloorInfo> findFloors(@Param("activityId") Integer activityId);

    @Select("<script>" +
            "select * from fa_funtv_activity_firstpage_item where  is_deleted=0 and " +
            "activity_floor_id in " +
            "<foreach collection=\"list\" item=\"item\" index= \"index\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item.id} " +
            "</foreach>" +
            "order by show_seq asc " +
            "</script>")
    List<FuntvActivityFirstPageItemInfo> findFloorDetails(@Param("list") List<FuntvActivityFirstPageFloorInfo> activityFloors);


}
