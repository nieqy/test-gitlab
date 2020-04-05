package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityPrizeRuleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangfei on 2019/4/25.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityPrizeRuleMapper {

    @Select("<script>" +
            "select * " +
            "from fa_funtv_activity_prize_rule " +
            "where  " +
            "activity_type=#{activityType} and " +
            "is_content_prize=#{isContentPrize} and " +
            "prize_type=#{prizeType} and " +
            "is_deleted=0 " +
            "<if test=\"contentId!=null and contentId >0 \">" +
            " and content_id=#{contentId} " +
            "</if>" +
            "</script>")
    FuntvActivityPrizeRuleInfo findRule(@Param("activityType") int activityType, @Param("isContentPrize") int isContentPrize,
                                        @Param("prizeType") int prizeType, @Param("contentId") Integer contentId);

    @Select("<script>" +
            "select * " +
            "from fa_funtv_activity_prize_rule " +
            "where  " +
            "activity_type=#{activityType} and is_deleted=0 " +
            "</script>")
    List<FuntvActivityPrizeRuleInfo> findAllRule(@Param("activityType") int activityType);

    @Select("<script>" +
            "select * " +
            "from fa_funtv_activity_prize_rule " +
            "where  " +
            "activity_type=#{activityType} and " +
            "is_content_prize=1 and " +
            "is_deleted=0 " +
            "and content_id=#{contentId} " +
            "</script>")
    List<FuntvActivityPrizeRuleInfo> findAllContentRule(@Param("activityType") int activityType, @Param("contentId") Integer contentId);
}
