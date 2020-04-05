package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityContentAnswerInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityContentAnswerListInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangfei on 2019/4/25.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityContentAnswerMapper {

    @Select("<script>" +
            "select content_detail_id, answer, status " +
            "from fa_funtv_activity_content_answer " +
            "where activity_type=#{activityType} and " +
            "account_id=#{accountId} " +
            "<if test=\"contentId != null and contentId !=''\">" +
            " and content_id=#{contentId} " +
            "</if>" +
            "</script>")
    List<FuntvActivityContentAnswerInfo> getAnswerInfo(@Param("activityType") int activityType, @Param("accountId") Integer accountId, @Param("contentId") Integer contentId);

    @DataSource(DataSourceType.MASTER)
    @Insert("<script>" +
            "insert into fa_funtv_activity_content_answer " +
            "(activity_type,account_id,content_id,content_detail_id,create_time,answer,status) values " +
            "<foreach collection=\"list\" item=\"item\" index= \"index\" separator =\",\">" +
            "(#{item.activityType},#{item.accountId},#{item.contentId},#{item.contentDetailId},now(),#{item.answer},#{item.status})" +
            "</foreach>" +
            "</script>")
    void submitAnswer(@Param("list") List<FuntvActivityContentAnswerInfo> list);

    @Select("select #{accountId} account_id, b.id content_id, b.content_name, b.start_time, b.end_time, count(a.status) all_count, sum(case when a.`status`=1 then 1 else 0 end) correct_count " +
            "from fa_funtv_activity_content_config b " +
            "left join fa_funtv_activity_content_answer a " +
            "on a.content_id=b.id and a.account_id=#{accountId} and b.activity_type=a.activity_type " +
            "where b.is_deleted=0 and b.activity_type=#{activityType} " +
            "group by b.id " +
            "order by b.show_seq asc")
    List<FuntvActivityContentAnswerListInfo> getAccountAnswerList(@Param("accountId") Integer accountId, @Param("activityType") int activityType);

}
