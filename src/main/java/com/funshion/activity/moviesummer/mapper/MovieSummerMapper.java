package com.funshion.activity.moviesummer.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.moviesummer.entity.*;
import com.funshion.activity.moviesummer.entity.MovieSummerAccountPrizeListInfo.MovieSummerAccountFinalPrizeInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface MovieSummerMapper {

    @Select("select * from fa_movie_summer_quiz where is_deleted=0 and status!=0 order by show_seq asc")
    List<MovieSummerQuizInfo> findQuizs();

    @Select("select * from fa_movie_summer_quiz where is_deleted=0 and status!=0 and id=#{id}")
    MovieSummerQuizInfo findQuizById(Integer id);

    @Select("select * from fa_movie_summer_quiz_detail where status=1 and quiz_id=#{quizId} order by show_seq asc")
    List<MovieSummerDetailInfo> findDetailsByQuizId(Integer quizId);

    @DataSource(DataSourceType.MASTER)
    @Insert("<script>" +
            "insert into fa_movie_summer_answer(account_id,quiz_id,subject_id,create_time,answer,status) values " +
            "<foreach collection=\"list\" item=\"item\" index= \"index\" separator =\",\">" +
            "(#{item.accountId},#{item.quizId},#{item.subjectId},now(),#{item.answer},#{item.status})" +
            "</foreach>" +
            "</script>")
    void submitAnswer(@Param("list") List<MovieSummerAccountAnswerInfo> list);

    @Select("select subject_id, answer, status from fa_movie_summer_answer where account_id=#{accountId} and quiz_id =#{quizId}")
    List<MovieSummerAccountAnswerInfo> getAnswerInfo(@Param("accountId") Integer accountId, @Param("quizId") Integer quizId);

    @DataSource(DataSourceType.MASTER)
    @Insert("insert into fa_movie_summer_account(account_id, phone, last_commit) values(#{accountId},#{phone}, now())")
    void addAccountPhone(@Param("accountId") Integer accountId, @Param("phone") String phone);

    @Select("select * from fa_movie_summer_account where account_id=#{accountId}")
    MovieSummerAccontInfo findAccountInfoByAccountId(Integer accountId);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_movie_summer_account set phone=#{phone}, last_commit=now() where id=#{id}")
    void updateAccountPhone(@Param("id") Integer id, @Param("phone") String phone);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_movie_summer_account set receiving_name=#{receivingName}, receiving_address=#{receivingAddress}, receiving_phone=#{receivingPhone}, last_commit=now() where id=#{id}")
    void updateAccountAddress(@Param("id") Integer id, @Param("receivingName") String receivingName, @Param("receivingAddress") String receivingAddress, @Param("receivingPhone") String receivingPhone);

    @Select("select #{accountId} account_id, b.id quiz_id, b.start_time, b.end_time, count(a.status) all_count, sum(case when a.`status`=1 then 1 else 0 end) correct_count " +
            "from fa_movie_summer_quiz b " +
            "left join fa_movie_summer_answer a on a.quiz_id=b.id and a.account_id=#{accountId} " +
            "where b.is_deleted=0 " +
            "group by b.id " +
            "order by b.show_seq asc")
    List<MovieSummerAccountQuizAnswerListInfo> getAccountQuizAnswerList(Integer accountId);

    @Select("<script>" +
            "select * from fa_movie_summer_account_prize " +
            "where account_id=#{accountId} and quiz_id in " +
            "<foreach collection=\"list\" item=\"item\" index= \"index\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item} " +
            "</foreach>" +
            "</script>")
    List<MovieSummerAccountPrizeInfo> getAccountPrizeByAccountIdAndQuizId(@Param("accountId") Integer accountId, @Param("list") List<Integer> quizIds);

    @Select("select * from fa_movie_summer_account_prize " +
            "where account_id=#{accountId} and quiz_id is null and prize_type in (3,5)")
    List<MovieSummerAccountPrizeInfo> getAccountFinalPrize(@Param("accountId") Integer accountId);

    @DataSource(DataSourceType.MASTER)
    @Update("update fa_movie_summer_account_prize set is_accept=#{isAccept},create_time=now() " +
            "where id=#{id}")
    void updateAccountPrize(@Param("id") Integer id, @Param("isAccept") Integer isAccept);

    @Select("<script>" +
            "select * from fa_movie_summer_account_prize " +
            "where account_id=#{accountId} and prize_type=#{prizeType} " +
            "<if test=\"quizId != null\">" +
            " and quiz_id=#{quizId} " +
            "</if>" +
            "<if test=\"quizId == null\">" +
            " and quiz_id is null " +
            "</if>" +
            "</script>")
    MovieSummerAccountPrizeInfo getAccountPrize(@Param("accountId") Integer accountId, @Param("quizId") Integer quizId, @Param("prizeType") Integer prizeType);

    @Select(" select a.subject_name,a.start_time,a.end_time,group_concat(c.phone) phone  " +
            "from fa_movie_summer_account_prize b " +
            "inner join fa_movie_summer_account c on c.account_id=b.account_id " +
            "right join fa_movie_summer_quiz a  on a.id=b.quiz_id " +
            "where  a.is_deleted=0 and a.status=2 " +
            "group by a.id order by a.show_seq desc ")
    List<MovieSummerAccountPrizeListInfo> getAllAccountPrize();

    @Select("select a.prize_type, b.phone from fa_movie_summer_account_prize a, fa_movie_summer_account b " +
            "where a.account_id=b.account_id and a.quiz_id is null and a.prize_type in (3,5) ")
    List<MovieSummerAccountFinalPrizeInfo> getAllAccountFinalPrize();
}
