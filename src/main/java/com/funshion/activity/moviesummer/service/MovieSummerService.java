package com.funshion.activity.moviesummer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.common.utils.MD5Utils;
import com.funshion.activity.job.LoadDataConfig;
import com.funshion.activity.moviesummer.dto.MovieSummerAccountPrizeListResponse;
import com.funshion.activity.moviesummer.dto.MovieSummerDetailResponse;
import com.funshion.activity.moviesummer.dto.MovieSummerDetailResponse.SubjectDetails;
import com.funshion.activity.moviesummer.dto.MovieSummerDetailResponse.SubjectOptions;
import com.funshion.activity.moviesummer.dto.MovieSummerFirstPageResponse;
import com.funshion.activity.moviesummer.dto.MovieSummerFirstPageResponse.MovieSummerFirstPageMedias;
import com.funshion.activity.moviesummer.dto.MovieSummerSubmitRequest;
import com.funshion.activity.moviesummer.dto.MovieSummerSubmitRequest.SubmitAnswer;
import com.funshion.activity.moviesummer.entity.*;
import com.funshion.activity.moviesummer.entity.MovieSummerAccountPrizeListInfo.MovieSummerAccountFinalPrizeInfo;
import com.funshion.activity.moviesummer.mapper.MovieSummerMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class MovieSummerService {

    private static Logger logger = LoggerFactory.getLogger(MovieSummerService.class);

    @Autowired
    private MovieSummerMapper movieSummerMapper;

    @Value("${mercury.host}")
    private String mercuryHost;

    private String getSpecialTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + c.get(Calendar.HOUR_OF_DAY) + "点";
    }

    private String getSpecialTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + c.get(Calendar.HOUR_OF_DAY) + "点";
    }

    private Date getSpecialDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        return d;
    }

    public Result<?> movieSummerfirstPage() {
        List<MovieSummerQuizInfo> infos = movieSummerMapper.findQuizs();
        List<MovieSummerFirstPageResponse> fps = new ArrayList<>();
        for (MovieSummerQuizInfo info : infos) {
            MovieSummerFirstPageResponse fp = new MovieSummerFirstPageResponse();
            fp.setSubjectId(info.getId());
            fp.setSubjectName(info.getSubjectName());
            fp.setStartTime(this.getSpecialTime(info.getStartTime()));
            fp.setEndTime(this.getSpecialTime(info.getEndTime()));
            Date now = new Date();
            if (now.after(info.getStartTime()) && now.before(info.getEndTime())) {
                fp.setStatus(1);
            } else if (now.before(info.getStartTime())) {
                fp.setStatus(0);
            } else {
                fp.setStatus(2);
            }
            MovieSummerFirstPageMedias medias1 = new MovieSummerFirstPageMedias();
            medias1.setMediaId(info.getMovie1Id());
            medias1.setMediaName(info.getMovie1Name());
            medias1.setMediaPicture(ImgUtils.getImgPath(info.getMovie1Picture()));
            medias1.setMediaType(1);
            fp.getMedias().add(medias1);
            MovieSummerFirstPageMedias medias2 = new MovieSummerFirstPageMedias();
            medias2.setMediaId(info.getMovie2Id());
            medias2.setMediaName(info.getMovie2Name());
            medias2.setMediaPicture(ImgUtils.getImgPath(info.getMovie2Picture()));
            medias2.setMediaType(1);
            fp.getMedias().add(medias2);
            MovieSummerFirstPageMedias medias3 = new MovieSummerFirstPageMedias();
            medias3.setMediaId(info.getMovie3Id());
            medias3.setMediaName(info.getMovie3Name());
            medias3.setMediaPicture(ImgUtils.getImgPath(info.getMovie3Picture()));
            medias3.setMediaType(1);
            fp.getMedias().add(medias3);
            MovieSummerFirstPageMedias medias4 = new MovieSummerFirstPageMedias();
            medias4.setMediaId(info.getMovie4Id());
            medias4.setMediaName(info.getMovie4Name());
            medias4.setMediaPicture(ImgUtils.getImgPath(info.getMovie4Picture()));
            medias4.setMediaType(1);
            fp.getMedias().add(medias4);
            MovieSummerFirstPageMedias medias5 = new MovieSummerFirstPageMedias();
            medias5.setMediaId(info.getMovie5Id());
            medias5.setMediaName(info.getMovie5Name());
            medias5.setMediaPicture(ImgUtils.getImgPath(info.getMovie5Picture()));
            medias5.setMediaType(1);
            fp.getMedias().add(medias5);
            MovieSummerFirstPageMedias medias6 = new MovieSummerFirstPageMedias();
            medias6.setMediaId(info.getTopicId());
            medias6.setMediaPicture(ImgUtils.getImgPath(info.getTopicPicture()));
            medias6.setMediaType(2);
            fp.getMedias().add(medias6);
            fps.add(fp);
        }
        return Result.getSuccessResult(fps);
    }

    public Result<?> movieSummerDetails(Integer accountId, Integer quizId) {
        MovieSummerQuizInfo quizInfo = movieSummerMapper.findQuizById(quizId);
        if (quizInfo == null || quizInfo.getId() == null || quizInfo.getId() == 0) {
            return Result.getFailureResult("301", "数据不存在!");
        }
        MovieSummerAccontInfo accountInfo = movieSummerMapper.findAccountInfoByAccountId(accountId);
        List<MovieSummerDetailInfo> infos = movieSummerMapper.findDetailsByQuizId(quizId);
        List<MovieSummerAccountAnswerInfo> answers = movieSummerMapper.getAnswerInfo(accountId, quizId);
        MovieSummerDetailResponse response = new MovieSummerDetailResponse();
        response.setStartTime(this.getSpecialTime(quizInfo.getStartTime()));
        response.setEndTime(this.getSpecialTime(quizInfo.getEndTime()));
        response.setSubjectPicture(ImgUtils.getImgPath(quizInfo.getSubjectNamePicture()));
        if (answers != null && answers.size() > 0) {
            response.setIsAnswered(1);
        } else {
            response.setIsAnswered(0);
        }
        if (accountInfo != null && StringUtils.isNotBlank(accountInfo.getPhone())) {
            response.setNeedPhone(0);
        } else {
            response.setNeedPhone(1);
        }
        for (MovieSummerDetailInfo info : infos) {
            SubjectDetails detail = new SubjectDetails();
            detail.setTitle(info.getSubjectTitle());
            detail.setName(info.getSubjectName());
            detail.setId(info.getId());
            MovieSummerAccountAnswerInfo answerInfo = this.getAnswerInfo(answers, info.getId());
            if (answerInfo != null) {
                detail.setResult(info.getSubjectResult());
                detail.setAnswer(answerInfo.getAnswer());
                detail.setStatus(info.getSubjectResult().equals(answerInfo.getAnswer()) ? 1 : 0);
            }
            if (info.getNeedCheck() == 1) {
                detail.setCheckMediaId(info.getCheckMediaId());
                detail.setCheckTips(info.getCheckTips());
            }
            detail.setOptionTemplate(info.getOptionTemplate());
            SubjectOptions opt1 = new SubjectOptions();
            opt1.setOptionTag(info.getOptionTag1());
            opt1.setOptionName(info.getOptionName1());
            detail.getOptions().add(opt1);
            SubjectOptions opt2 = new SubjectOptions();
            opt2.setOptionTag(info.getOptionTag2());
            opt2.setOptionName(info.getOptionName2());
            detail.getOptions().add(opt2);
            SubjectOptions opt3 = new SubjectOptions();
            opt3.setOptionTag(info.getOptionTag3());
            opt3.setOptionName(info.getOptionName3());
            detail.getOptions().add(opt3);
            SubjectOptions opt4 = new SubjectOptions();
            opt4.setOptionTag(info.getOptionTag4());
            opt4.setOptionName(info.getOptionName4());
            detail.getOptions().add(opt4);
            response.getQuizDetails().add(detail);
        }
        if (response.getIsAnswered() == 0) {
            Date now = new Date();
            if (quizInfo.getStartTime().after(now)) {
                return Result.getSuccessResult("202", "答题尚未开始！", response);
            }
            if (quizInfo.getEndTime().before(now)) {
                return Result.getSuccessResult("203", "答题已结束！", response);
            }
            return Result.getSuccessResult(response);
        } else {
            return Result.getSuccessResult("201", "您已答过题目，请勿重复提交！", response);
        }
    }

    public Result<?> movieSummerDetailSubmit(MovieSummerSubmitRequest request) {
        List<SubmitAnswer> answers = JSON.parseArray(request.getAnswers(), SubmitAnswer.class);
        if (answers == null || answers.size() == 0) {
            return Result.getFailureResult("302", "请提交答案!");
        }
        MovieSummerQuizInfo quizInfo = movieSummerMapper.findQuizById(request.getQuizId());
        Date now = new Date();
        if (quizInfo == null) {
            return Result.getFailureResult("307", "闯关已关闭!");
        }
        if (quizInfo.getStartTime().after(now)) {
            return Result.getFailureResult("305", "闯关还没开始!");
        }
        if (quizInfo.getEndTime().before(now)) {
            return Result.getFailureResult("306", "闯关已结束!");
        }
        List<MovieSummerDetailInfo> infos = movieSummerMapper.findDetailsByQuizId(request.getQuizId());
        if (infos == null || infos.size() == 0) {
            return Result.getFailureResult("303", "答题已结束!");
        }
        List<MovieSummerAccountAnswerInfo> answerInfos = movieSummerMapper.getAnswerInfo(request.getAccountId(), request.getQuizId());
        if (answerInfos != null && answerInfos.size() > 0) {
            return Result.getFailureResult("304", "请勿重复提交!");
        }
        List<MovieSummerAccountAnswerInfo> submits = new ArrayList<>();
        int correctCount = 0;
        for (SubmitAnswer answer : answers) {
            MovieSummerDetailInfo detailInfo = this.getDetailInfo(infos, answer.getSubjectId());
            if (detailInfo == null) {
                continue;
            }
            MovieSummerAccountAnswerInfo answerInfo = new MovieSummerAccountAnswerInfo();
            answerInfo.setAccountId(request.getAccountId());
            answerInfo.setQuizId(request.getQuizId());
            answerInfo.setSubjectId(answer.getSubjectId());
            answerInfo.setAnswer(answer.getAnswer());
            if (detailInfo.getSubjectResult().equals(answer.getAnswer())) {
                answerInfo.setStatus(1);
                correctCount++;
            } else {
                answerInfo.setStatus(0);
            }
            submits.add(answerInfo);
        }
        movieSummerMapper.submitAnswer(submits);
        if (correctCount == answers.size()) {
            return Result.getSuccessResult("恭喜您!全部答对!获得抽奖资格!");
        } else {
            return Result.getSuccessResult("答对" + correctCount + "道题! 下一轮继续加油哦!");
        }
    }

    public Result<?> submitAccountPhone(Integer accountId, String phone) {
        MovieSummerAccontInfo accountInfo = movieSummerMapper.findAccountInfoByAccountId(accountId);
        if (accountInfo != null && accountInfo.getId() > 0) {
            movieSummerMapper.updateAccountPhone(accountInfo.getId(), phone);
        } else {
            movieSummerMapper.addAccountPhone(accountId, phone);
        }
        return Result.getSuccessResult();
    }

    public Result<?> submitAccountAddress(Integer accountId, String receivingName, String receivingAddress, String receivingPhone) {
        MovieSummerAccontInfo accountInfo = movieSummerMapper.findAccountInfoByAccountId(accountId);
        if (accountInfo != null && accountInfo.getId() > 0) {
            movieSummerMapper.updateAccountAddress(accountInfo.getId(), receivingName, receivingAddress, receivingPhone);
        } else {
            return Result.getFailureResult("201", "failed.", "账号不存在!");
        }
        return Result.getSuccessResult();
    }

    public Result<?> getAccountAddressInfo(Integer accountId) {
        MovieSummerAccontInfo accountInfo = movieSummerMapper.findAccountInfoByAccountId(accountId);
        if (accountInfo != null && accountInfo.getId() > 0) {
            if (StringUtils.isNotBlank(accountInfo.getReceivingName()) && StringUtils.isNotBlank(accountInfo.getReceivingAddress())) {
                return Result.getSuccessResult();
            }
        }
        return Result.getFailureResult("201", "failed.", "收货地址不存在!");
    }

    private MovieSummerAccountAnswerInfo getAnswerInfo(List<MovieSummerAccountAnswerInfo> infos, Integer subjectId) {
        if (infos == null) {
            return null;
        }
        for (MovieSummerAccountAnswerInfo info : infos) {
            if (info.getSubjectId().equals(subjectId)) {
                return info;
            }
        }
        return null;
    }

    private MovieSummerDetailInfo getDetailInfo(List<MovieSummerDetailInfo> infos, Integer subjectId) {
        if (infos == null) {
            return null;
        }
        for (MovieSummerDetailInfo info : infos) {
            if (info.getId().equals(subjectId)) {
                return info;
            }
        }
        return null;
    }

    private MovieSummerAccountPrizeInfo getPrizeInfo(List<MovieSummerAccountPrizeInfo> infos, Integer accountId, Integer quizId) {
        if (infos == null) {
            return null;
        }
        for (MovieSummerAccountPrizeInfo info : infos) {
            if (info.getAccountId().equals(accountId) && info.getQuizId().equals(quizId)) {
                return info;
            }
        }
        return null;
    }

    private MovieSummerAccountPrizeInfo getFinalPrizeInfo(List<MovieSummerAccountPrizeInfo> infos, Integer prizeType) {
        if (infos == null) {
            return null;
        }
        for (MovieSummerAccountPrizeInfo info : infos) {
            if (info.getPrizeType().equals(prizeType)) {
                return info;
            }
        }
        return null;
    }

    private MovieSummerAccountQuizAnswerListInfo winPrize(List<MovieSummerAccountPrizeInfo> finalPrize, Integer prizeType, Integer accountId, int sumCorrectCount) {
        String finalPrizeTime = LoadDataConfig.getDataValueByKey("final_prize_time", "2018-09-17 12:00:00");
        Date prizeDate = this.getSpecialDate(finalPrizeTime); //2018/09/17 12:00:00
        MovieSummerAccountPrizeInfo prizeInfo = this.getFinalPrizeInfo(finalPrize, prizeType);
        MovieSummerAccountQuizAnswerListInfo prize = new MovieSummerAccountQuizAnswerListInfo();
        prize.setStartTime(this.getSpecialTime(prizeDate));
        prize.setAccountId(accountId);
        if (prizeInfo == null) {
            Date now = new Date();
            if (now.after(prizeDate)) {
                prize.setPrizeType(2);
                prize.setStatus(1);
            } else {
                prize.setPrizeType(0);
                prize.setStatus(0);
            }
            prize.setCorrectCount(sumCorrectCount);
        } else {
            prize.setPrizeType(prizeInfo.getPrizeType());
            prize.setIsAccept(prizeInfo.getIsAccept());
            prize.setCorrectCount(sumCorrectCount);
            prize.setStatus(1);
        }

        return prize;
    }

    //所有关卡都要显示出来
    public Result<?> getAccountQuizAnswerList(Integer accountId) {
        List<MovieSummerAccountQuizAnswerListInfo> list = movieSummerMapper.getAccountQuizAnswerList(accountId);
        if (list == null || list.size() == 0) {
            return Result.getFailureResult("305", "没有答题记录!");
        }
        List<Integer> quizIds = new ArrayList<>();
        for (MovieSummerAccountQuizAnswerListInfo movieSummerAccountQuizAnswerListInfo : list) {
            quizIds.add(movieSummerAccountQuizAnswerListInfo.getQuizId());
        }
        List<MovieSummerAccountPrizeInfo> prizes = movieSummerMapper.getAccountPrizeByAccountIdAndQuizId(accountId, quizIds);
        int sumCorrectCount = 0;
        Date now = new Date();
        for (MovieSummerAccountQuizAnswerListInfo movieSummerAccountQuizAnswerListInfo : list) {
            sumCorrectCount += movieSummerAccountQuizAnswerListInfo.getCorrectCount();
            MovieSummerAccountPrizeInfo prizeInfo = this.getPrizeInfo(prizes, movieSummerAccountQuizAnswerListInfo.getAccountId(), movieSummerAccountQuizAnswerListInfo.getQuizId());
            if (prizeInfo == null) {
                if (this.getSpecialDate(movieSummerAccountQuizAnswerListInfo.getEndTime()).before(now)) {
                    if (movieSummerAccountQuizAnswerListInfo.getAllCount() == 0) {
                        movieSummerAccountQuizAnswerListInfo.setPrizeType(1);
                    } else {
                        movieSummerAccountQuizAnswerListInfo.setPrizeType(2);
                    }
                } else {
                    movieSummerAccountQuizAnswerListInfo.setPrizeType(0);
                }
            } else {
                movieSummerAccountQuizAnswerListInfo.setPrizeType(prizeInfo.getPrizeType());
                movieSummerAccountQuizAnswerListInfo.setIsAccept(prizeInfo.getIsAccept());
            }
            movieSummerAccountQuizAnswerListInfo.setStartTime(this.getSpecialTime(movieSummerAccountQuizAnswerListInfo.getStartTime()));
            movieSummerAccountQuizAnswerListInfo.setEndTime(this.getSpecialTime(movieSummerAccountQuizAnswerListInfo.getEndTime()));
            if (this.getSpecialDate(movieSummerAccountQuizAnswerListInfo.getStartTime()).before(now) && this.getSpecialDate(movieSummerAccountQuizAnswerListInfo.getEndTime()).after(now)) {
                movieSummerAccountQuizAnswerListInfo.setStatus(1);
            } else if (this.getSpecialDate(movieSummerAccountQuizAnswerListInfo.getStartTime()).after(now)) {
                movieSummerAccountQuizAnswerListInfo.setStatus(0);
            } else if (this.getSpecialDate(movieSummerAccountQuizAnswerListInfo.getEndTime()).before(now)) {
                movieSummerAccountQuizAnswerListInfo.setStatus(2);
            }
        }
        List<MovieSummerAccountPrizeInfo> finalPrize = movieSummerMapper.getAccountFinalPrize(accountId);
        //一等奖
        list.add(this.winPrize(finalPrize, 5, accountId, sumCorrectCount));
        //幸运奖
        list.add(this.winPrize(finalPrize, 3, accountId, sumCorrectCount));
        return Result.getSuccessResult(list);
    }

    public Result<?> acceptPrize(Integer accountId, Integer quizId, Integer prizeType) {
        MovieSummerAccountPrizeInfo prize = movieSummerMapper.getAccountPrize(accountId, quizId, prizeType);
        if (prize == null || prize.getId() == 0) {
            return Result.getFailureResult("306", "未开奖!");
        } else if (prize.getIsAccept() == 1) {
            return Result.getFailureResult("306", "已经领取过奖品!");
        }
        if (prize.getPrizeType() == 4) {
            //请求会员接口
            return this.acceptFromMercury(prize.getId(), accountId, 1, "恭喜您获得了“电视金卡月包”二等奖</br>金卡会员时长会自动延长30天");
        } else if (prize.getPrizeType() == 5) {
            //请求会员接口
            return this.acceptFromMercury(prize.getId(), accountId, 303, "恭喜您获得了“电视金卡年包”大奖</br>金卡会员时长会自动延长365天");
        } else if (prize.getPrizeType() == 3) {
            movieSummerMapper.updateAccountPrize(prize.getId(), 1);
        }
        return Result.getSuccessResult();
    }

    private Result<?> acceptFromMercury(Integer prizeId, Integer accountId, Integer commodityId, String msg) {
        StringBuffer sb = new StringBuffer();
        String ctime = System.currentTimeMillis() + "";
        String key = "201f1696bea924252cbe0a9324576c2f";
        sb.append(mercuryHost).append("/api/summer/movie/give").append("?").append("accountId=").append(accountId)
                .append("&").append("ctime=").append(ctime).append("&");
        sb.append("commodityId=").append(commodityId).append("&").append("sign=").append(MD5Utils.getStringMD5String(accountId + ctime + commodityId + key));
        //请求会员接口  accountId, commityId, ctime, sign
        try {
            String acceptResponse = HttpClientUtils.get(sb.toString());
            JSONObject json = JSON.parseObject(acceptResponse);
            int retCode = json.getIntValue("retCode");
            if (retCode == 200) {
                movieSummerMapper.updateAccountPrize(prizeId, 1);
                return Result.getSuccessResult(msg);
            }
        } catch (Exception e) {
            logger.error("领奖失败: {} - {}", accountId, e);
        }
        return Result.getFailureResult("307", "failed.", "网络异常，请重试!");
    }

    public Result<?> prizeList() {
        List<MovieSummerAccountPrizeListInfo> list = movieSummerMapper.getAllAccountPrize();
        List<MovieSummerAccountPrizeListResponse> response = new ArrayList<>();
        for (MovieSummerAccountPrizeListInfo info : list) {
            MovieSummerAccountPrizeListResponse res = new MovieSummerAccountPrizeListResponse();
            res.setSubjectName(info.getSubjectName());
            res.setStartTime(this.getSpecialTime(info.getStartTime()));
            res.setEndTime(this.getSpecialTime(info.getEndTime()));
            if (StringUtils.isNotBlank(info.getPhone())) {
                List<String> ps = new ArrayList<>();
                String[] phones = info.getPhone().split(",");
                for (String phone : phones) {
                    ps.add(phone.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                }
                res.setPhone(ps);
            } else {
                res.setPhone(new ArrayList<String>());
            }
            res.setType(1);
            response.add(res);
        }
        String finalPrizeTime = LoadDataConfig.getDataValueByKey("final_prize_time", "2018-09-17 12:00:00");
        Date now = new Date();
        if (now.before(this.getSpecialDate(finalPrizeTime))) {
            return Result.getSuccessResult(response);
        }
        MovieSummerAccountPrizeListResponse first = new MovieSummerAccountPrizeListResponse();
        first.setStartTime(this.getSpecialTime(finalPrizeTime));
        first.setSubjectName("恭喜以下用户中一等奖");
        first.setPhone(new ArrayList<String>());
        first.setType(2);
        MovieSummerAccountPrizeListResponse lucky = new MovieSummerAccountPrizeListResponse();
        lucky.setStartTime(this.getSpecialTime(finalPrizeTime));
        lucky.setPhone(new ArrayList<String>());
        lucky.setSubjectName("恭喜以下用户中幸运奖");
        lucky.setType(3);
        List<MovieSummerAccountFinalPrizeInfo> finals = movieSummerMapper.getAllAccountFinalPrize();
        for (MovieSummerAccountFinalPrizeInfo afinal : finals) {
            if (StringUtils.isNotBlank(afinal.getPhone())) {
                if (afinal.getPrizeType().equals("5")) {
                    first.getPhone().add(afinal.getPhone().replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                } else if (afinal.getPrizeType().equals("3")) {
                    lucky.getPhone().add(afinal.getPhone().replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1****$3"));
                }
            }
        }
        response.add(0, lucky);
        response.add(0, first);
        return Result.getSuccessResult(response);
    }

}
