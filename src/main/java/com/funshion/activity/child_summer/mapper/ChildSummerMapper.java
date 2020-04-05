package com.funshion.activity.child_summer.mapper;

import com.funshion.activity.child_summer.entity.*;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
@DataSource(DataSourceType.MASTER)
public interface ChildSummerMapper {

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_child_summer_quiz where status = 1 and create_time >= '2018-11-24' and start_time <= now() and winners !='' and winners is not null  order by end_time desc   ")
    public List<ChildSummerPrize> getFinishedQuizInfo();

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_child_summer_quiz where status = 1 order by show_seq asc  ")
    public List<ChildSummerQuizAndAnswer> getStartedQuizInfo();

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_child_summer_quiz where status = 1 and start_time > now()  order by start_time asc  limit 1   ")
    public ChildSummerQuiz getNextStartQuizInfo();

    @Select("select * from fa_child_summer_quiz where status = 1 and need_answer = 1 and start_time <= now() and end_time >= now() limit  1 ")
    public ChildSummerQuiz getQuizInfo();

    @Select("select * from fa_child_summer_quiz where status = 1 and need_answer = 1 and start_time <= now() and end_time >= now() and id = #{id}")
    public ChildSummerQuiz getQuizInfoById(@Param("id") Integer id);

    @DataSource(DataSourceType.SLAVE)
    @Select("select * from fa_child_summer_quiz where status = 1 and need_answer = 1 and start_time > #{date} order by start_time asc limit  1 ")
    public ChildSummerQuiz getNextQuizInfo(@Param("date") Date date);

    @DataSource(DataSourceType.SLAVE)
    @Select("select a.id as subject_id ,a.* from fa_child_summer_quiz_detail a where a.status =1 and a.quiz_id = #{quizId} order by a.show_seq asc ")
    public List<ChildSummerDetail> getQuizDetailInfo(@Param("quizId") Integer quizId);

    @Insert("insert into fa_child_summer_account (account_id,phone) values (#{accountId},#{phone})")
    public void insertAccountPhone(@Param("phone") String phone, @Param("accountId") Integer accountId);

    @Update("update fa_child_summer_account set phone =#{phone}  where account_id=#{accountId}")
    public void updateAccountPhone(@Param("phone") String phone, @Param("accountId") Integer accountId);

    @Insert("insert into fa_child_summer_account (account_id,receiving_name,receiving_phone,receiving_address) values (#{accountId},#{receivingName},#{receivingPhone},#{receivingAddress})")
    public void insertAccountAdress(ChildSummerAccont info);

    @Update("update  fa_child_summer_account set receiving_name=#{receivingName},receiving_phone=#{receivingPhone},receiving_address=#{receivingAddress}  where id = #{id}")
    public void updateAccountAdress(ChildSummerAccont info);

    @Select("select * from fa_child_summer_account where account_id = #{accountId} ")
    public ChildSummerAccont selectAccountInfo(@Param("accountId") Integer accountId);

    @Select("select *  from fa_child_summer_answer  a where a.account_id = #{accountId} and a.quiz_id = #{quizId}  ")
    public List<ChildSummerAnswer> selectAccountAnswerByQuizId(@Param("accountId") Integer accountId, @Param("quizId") Integer quizId);

    @Insert("insert into fa_child_summer_answer (account_id,quiz_id,subject_id,create_time,answer,status) values (#{accountId},#{quizId},#{subjectId},now(),#{answer},#{status})")
    public void insertAccountAnswer(ChildSummerAnswer info);

    @Select("select * from  fa_child_summer_answer  a  where a.account_id = #{accountId} and quiz_id = #{quizId} and subject_id = #{subjectId}  ")
    public List<ChildSummerAnswer> selectAccountAnswerBySubjectId(@Param("accountId") Integer accountId, @Param("quizId") Integer quizId, @Param("subjectId") Integer subjectId);

}
