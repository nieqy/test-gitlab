package com.funshion.activity.moviesummer.dto;

import java.util.List;

/**
 * Created by zhangfei on 2018/8/2/002.
 */
public class MovieSummerSubmitRequest {

	private String ctime;

	private String sign;

	private Integer accountId;

	private Integer quizId;

	//private List<SubmitAnswer> answers;
	private String answers;

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getQuizId() {
		return quizId;
	}

	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public static class SubmitAnswer {

		private Integer subjectId;

		private String answer;

		public Integer getSubjectId() {
			return subjectId;
		}

		public void setSubjectId(Integer subjectId) {
			this.subjectId = subjectId;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}
	}

}
