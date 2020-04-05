package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityQuizConfigMapper {

    @Select("select * from fa_funtv_activity_quiz_config where is_deleted=0 and status=1 and activity_type=#{activityType}")
    FuntvActivityQuizConfigInfo findByActivityType(Integer activityType);

    @Select("select * from fa_funtv_activity_quiz_header_config where id=#{id}")
    FuntvActivityQuizHeaderConfigInfo findHeaderConfig(Integer id);

    @Select("select * from fa_funtv_activity_quiz_question_config where id=#{id}")
    FuntvActivityQuizQuestionConfigInfo findQuestionConfig(Integer id);

    @Select("select * from fa_funtv_activity_quiz_dialog_config where id=#{id}")
    FuntvActivityQuizDialogConfigInfo findDialogConfig(Integer id);

    @Select("select * from fa_funtv_activity_quiz_rule_page where id=#{id}")
    FuntvActivityQuizRulePageInfo findRulePage(Integer id);

    @Select("select * from fa_funtv_activity_quiz_answer_page where id=#{id}")
    FuntvActivityQuizAnswerPageInfo findAnswerPage(Integer id);

    @Select("select * from fa_funtv_activity_quiz_prize_page where id=#{id}")
    FuntvActivityQuizPrizePageInfo findPrizePage(Integer id);

}
