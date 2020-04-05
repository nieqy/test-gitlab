package com.funshion.activity.moviesummer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
public class MovieSummerDetailResponse {

	private String startTime;

	private String endTime;

	private String subjectPicture;

	private int isAnswered;

	private int needPhone;

	private List<SubjectDetails> quizDetails = new ArrayList<>();

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSubjectPicture() {
		return subjectPicture;
	}

	public void setSubjectPicture(String subjectPicture) {
		this.subjectPicture = subjectPicture;
	}

	public List<SubjectDetails> getQuizDetails() {
		return quizDetails;
	}

	public void setQuizDetails(List<SubjectDetails> quizDetails) {
		this.quizDetails = quizDetails;
	}

	public int getNeedPhone() {
		return needPhone;
	}

	public void setNeedPhone(int needPhone) {
		this.needPhone = needPhone;
	}

	public int getIsAnswered() {
		return isAnswered;
	}

	public void setIsAnswered(int isAnswered) {
		this.isAnswered = isAnswered;
	}

	public static class SubjectDetails{

		private Integer id;

		private String title;

		private String name;

		private String result;

		private String answer;

		private Integer status;

		private List<SubjectOptions> options = new ArrayList<>();

		private int optionTemplate;

		private Integer checkMediaId;

		private String checkTips;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<SubjectOptions> getOptions() {
			return options;
		}

		public void setOptions(List<SubjectOptions> options) {
			this.options = options;
		}

		public int getOptionTemplate() {
			return optionTemplate;
		}

		public void setOptionTemplate(int optionTemplate) {
			this.optionTemplate = optionTemplate;
		}

		public Integer getCheckMediaId() {
			return checkMediaId;
		}

		public void setCheckMediaId(Integer checkMediaId) {
			this.checkMediaId = checkMediaId;
		}

		public String getCheckTips() {
			return checkTips;
		}

		public void setCheckTips(String checkTips) {
			this.checkTips = checkTips;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
	}

	public static class SubjectOptions{
		private String optionTag;
		private String optionName;

		public String getOptionTag() {
			return optionTag;
		}

		public void setOptionTag(String optionTag) {
			this.optionTag = optionTag;
		}

		public String getOptionName() {
			return optionName;
		}

		public void setOptionName(String optionName) {
			this.optionName = optionName;
		}
	}

}
