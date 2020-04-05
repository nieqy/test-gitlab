package com.funshion.activity.funtvactivity.dto;

/**
 * Created by zhangfei on 2018/8/2/002.
 */
public class FuntvActivityAnswerSubmitRequest {

	private String ctime;

	private String sign;

	private int activityType;

	private Integer accountId;

	private Integer contentId;

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

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public static class SubmitAnswer {

		private Integer contentDetailId;

		private String answer;

		public Integer getContentDetailId() {
			return contentDetailId;
		}

		public void setContentDetailId(Integer contentDetailId) {
			this.contentDetailId = contentDetailId;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}
	}

}
