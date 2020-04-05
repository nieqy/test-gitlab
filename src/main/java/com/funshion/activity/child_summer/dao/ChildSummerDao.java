package com.funshion.activity.child_summer.dao;

import com.funshion.activity.child_summer.entity.*;
import com.funshion.activity.child_summer.mapper.ChildSummerMapper;
import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ChildSummerDao {
    @Autowired
    private ChildSummerMapper childSummerMapper;

    @DataSource(DataSourceType.SLAVE)
    public List<ChildSummerPrize> getFinishedQuizInfo() {
        return childSummerMapper.getFinishedQuizInfo();
    }

    @DataSource(DataSourceType.SLAVE)
    public List<ChildSummerQuizAndAnswer> getStartedQuizInfo() {
        return childSummerMapper.getStartedQuizInfo();
    }

    public ChildSummerQuiz getQuizInfoById(Integer id) {
        return childSummerMapper.getQuizInfoById(id);
    }

    public ChildSummerQuiz getQuizInfo() {
        return childSummerMapper.getQuizInfo();
    }

    @DataSource(DataSourceType.SLAVE)
    public ChildSummerQuiz getNextStartQuizInfo() {
        return childSummerMapper.getNextStartQuizInfo();
    }

    @DataSource(DataSourceType.SLAVE)
    public ChildSummerQuiz getNextQuizInfo(Date date) {
        return childSummerMapper.getNextQuizInfo(date);
    }

    @DataSource(DataSourceType.SLAVE)
    public List<ChildSummerDetail> getQuizDetailInfo(Integer quizId) {
        return childSummerMapper.getQuizDetailInfo(quizId);
    }

    public void insertAccountPhone(String phone, Integer accountId) {
        childSummerMapper.insertAccountPhone(phone, accountId);
    }

    public void updateAccountPhone(String phone, Integer accountId) {
        childSummerMapper.updateAccountPhone(phone, accountId);
    }

    public void insertAccountAdress(ChildSummerAccont info) {
        childSummerMapper.insertAccountAdress(info);
    }

    public void updateAccountAdress(ChildSummerAccont info) {
        childSummerMapper.updateAccountAdress(info);
    }

    public ChildSummerAccont selectAccountInfo(Integer accountId) {
        return childSummerMapper.selectAccountInfo(accountId);
    }

    public List<ChildSummerAnswer> selectAccountAnswerByQuizId(Integer accountId, Integer quizId) {
        return childSummerMapper.selectAccountAnswerByQuizId(accountId, quizId);
    }

    public void insertAccountAnswer(ChildSummerAnswer info) {
        childSummerMapper.insertAccountAnswer(info);
    }

    public List<ChildSummerAnswer> selectAccountAnswerBySubjectId(Integer accountId, Integer quizId, Integer subjectId) {
        return childSummerMapper.selectAccountAnswerBySubjectId(accountId, quizId, subjectId);
    }
}
