package com.funshion.activity.child_summer.service;

import com.funshion.activity.child_summer.dao.ChildSummerDao;
import com.funshion.activity.child_summer.entity.*;
import com.funshion.activity.common.constants.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class ChildSummerService {

    @Autowired
    ChildSummerDao childSummerDao;

    public Result<?> getQuizList(Integer accountId) {
        ChildSummerQuiz data = childSummerDao.getQuizInfo();
        if (data == null) {
            data = childSummerDao.getNextStartQuizInfo();
            if (data != null) {
                List<ChildSummerDetail> datails = childSummerDao.getQuizDetailInfo(data.getId());
                for (ChildSummerDetail detail : datails) {
                    detail.setSubjectResult(null);
                }
                ChildSummerAccont account = childSummerDao.selectAccountInfo(accountId);
                if (account != null && !StringUtils.isEmpty(account.getPhone())) {
                    data.setPhone(account.getPhone());
                }
                data.setQuizDetail(datails);
                return Result.getFailureResult("501", "答题暂未开始，请耐心等待", data);
            } else {
                return Result.getFailureResult("503", "活动已结束");
            }
        }
        List<ChildSummerAnswer> userAnswerList = childSummerDao.selectAccountAnswerByQuizId(accountId, data.getId());
        List<ChildSummerDetail> datails = childSummerDao.getQuizDetailInfo(data.getId());
        //用户参加
        if (userAnswerList != null && userAnswerList.size() > 0) {
            for (ChildSummerDetail detail : datails) {
                detail.setUserAnswer(getUserAnswer(userAnswerList, detail));
            }
            ChildSummerQuiz nextQuiz = childSummerDao.getNextQuizInfo(data.getStartTime());
            if (nextQuiz != null) {
                data.setNextStartTime(nextQuiz.getStartTime());
            }
            data.setQuizDetail(datails);
            ChildSummerAccont account = childSummerDao.selectAccountInfo(accountId);
            if (account != null && !StringUtils.isEmpty(account.getPhone())) {
                data.setPhone(account.getPhone());
            }
            return Result.getFailureResult("502", "本期答题已完成，请等待下一期", data);
        } else {//用户未参加
            for (ChildSummerDetail detail : datails) {
                detail.setSubjectResult(null);
            }
            data.setQuizDetail(datails);
            ChildSummerAccont account = childSummerDao.selectAccountInfo(accountId);
            if (account != null && !StringUtils.isEmpty(account.getPhone())) {
                data.setPhone(account.getPhone());
            }
            return Result.getSuccessResult(data);
        }
    }

    private String getUserAnswer(List<ChildSummerAnswer> userAnswerList,
                                 ChildSummerDetail detail) {
        // TODO Auto-generated method stub
        for (ChildSummerAnswer answer : userAnswerList) {
            if (answer.getQuizId().equals(detail.getQuizId()) && answer.getSubjectId().equals(detail.getSubjectId())) {
                return answer.getAnswer();
            }
        }
        return null;
    }

    public Result<?> answerUpload(Integer accountId, Integer quizId, List<ChildSummerAnswer> answers) {
        ChildSummerQuiz quizInfo = childSummerDao.getQuizInfoById(quizId);
        if (quizInfo == null) {
            return Result.getFailureResult("504", "答题已结束");
        }
        List<ChildSummerAnswer> checkAnswer = childSummerDao.selectAccountAnswerByQuizId(accountId, quizId);
        if (checkAnswer != null && checkAnswer.size() > 0) {
            return Result.getFailureResult("503", "请勿重复提交");
        }
        List<ChildSummerDetail> datails = childSummerDao.getQuizDetailInfo(quizId);
        int correctNums = 0;
        for (ChildSummerAnswer answer : answers) {
            List<ChildSummerAnswer> checkSujectAnswer = childSummerDao.selectAccountAnswerBySubjectId(answer.getAccountId(), answer.getQuizId(), answer.getSubjectId());
            if (checkSujectAnswer == null || checkSujectAnswer.size() <= 0) {
                answer.setAccountId(accountId);
                answer.setQuizId(quizId);
                boolean res = checkUserAnswer(answer, datails);
                if (checkUserAnswer(answer, datails)) {
                    correctNums++;
                }
                answer.setStatus(res ? 1 : 0);
                childSummerDao.insertAccountAnswer(answer);
            }
        }
        return Result.getSuccessResult(correctNums);

    }

    private boolean checkUserAnswer(ChildSummerAnswer answer,
                                    List<ChildSummerDetail> datails) {
        // TODO Auto-generated method stub
        for (ChildSummerDetail detail : datails) {
            if (answer.getQuizId().equals(detail.getQuizId()) && answer.getSubjectId().equals(detail.getSubjectId()) && answer.getAnswer().equals(detail.getSubjectResult())) {
                return true;
            }
        }
        return false;
    }

    public void phoneUpload(String phone, Integer accountId) {
        ChildSummerAccont account = childSummerDao.selectAccountInfo(accountId);
        if (account == null) {
            childSummerDao.insertAccountPhone(phone, accountId);
        } else {
            childSummerDao.updateAccountPhone(phone, accountId);
        }
    }

    public void addressUpload(ChildSummerAccont info) {
        ChildSummerAccont account = childSummerDao.selectAccountInfo(info.getAccountId());
        if (account == null) {
            childSummerDao.insertAccountAdress(info);
        } else {
            //start update by zhangfei
            info.setId(account.getId());
            //end update by zhangfei
            childSummerDao.updateAccountAdress(info);
        }
    }


    public ChildSummerAccont addressInfo(Integer accountId) {
        return childSummerDao.selectAccountInfo(accountId);
    }

    public Result<?> accountAnswerList(Integer accountId) {
        ChildSummerAccont account = childSummerDao.selectAccountInfo(accountId);
        List<ChildSummerQuizAndAnswer> quizList = childSummerDao.getStartedQuizInfo();
        for (ChildSummerQuizAndAnswer quiz : quizList) {
            List<ChildSummerAnswer> answers = childSummerDao.selectAccountAnswerByQuizId(accountId, quiz.getId());
            if (quiz.getNeedAnswer() == 1 && (answers == null || answers.size() <= 0)) {
                quiz.setWin(3);
            } else {
                if (StringUtils.isEmpty(quiz.getWinners())) {
                    quiz.setWin(0);
                } else {
                    if (account != null && !StringUtils.isEmpty(account.getPhone())) {
                        if (Arrays.asList(quiz.getWinners().split(",")).contains(account.getPhone())) {
                            quiz.setWin(1);
                        } else {
                            quiz.setWin(2);
                        }
                    }
                }
            }

            if (answers != null && answers.size() > 0) {
                int nums = 0;
                for (ChildSummerAnswer answer : answers) {
                    if (answer.getStatus() == 1) {
                        nums++;
                    }
                }
                if (quiz.getNeedAnswer() == 1) {
                    quiz.setScore("正确" + nums + "道题");
                } else {
                    quiz.setScore("");
                }

            }
        }
        return Result.getSuccessResult(quizList);
    }

    public Result<?> prizeList(Integer accountId) {
        List<ChildSummerPrize> quizList = childSummerDao.getFinishedQuizInfo();
        for (ChildSummerPrize quiz : quizList) {
            List<String> phoneList = Arrays.asList(quiz.getWinners().split(","));
            for (int i = 0; i < phoneList.size(); i++) {
                phoneList.set(i, phoneList.get(i).replaceFirst("(\\d\\d\\d)(\\d{3,4})(\\d+)", "$1xxxx$3"));
            }
            quiz.setPhones(phoneList);
            quiz.setTitle(quiz.getName() + quiz.getPrizeBrand() + quiz.getPhones().size() + "名中奖用户");
        }
        return Result.getSuccessResult(quizList);
    }

}
