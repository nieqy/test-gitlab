package com.funshion.activity.child_summer.entity;


import com.funshion.activity.common.utils.ImgUtils;



public class ChildSummerDetail {
	
	private Integer subjectId;
	
	private Integer quizId;
	
	private String subjectIcon="";
	
	private String subjectName;
	
	private String optionTag1;
	
	private String optionName1;
	
	private String optionIcon1="";
	
	private String optionName2;
	
	private String optionIcon2="";
	
	private String optionName3;
	
	private String optionIcon3="";
	
	private String optionName4;
	
	private String optionIcon4="";
	
	private String subjectResult;
	
	private String optionTag2;
	
	private String optionTag3;
	
	private String optionTag4;
	
	private String userAnswer;
	

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getQuizId() {
		return quizId;
	}

	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}

	public String getSubjectIcon() {
		return ImgUtils.getImgPath(subjectIcon);
	}

	public void setSubjectIcon(String subjectIcon) {
		this.subjectIcon = subjectIcon;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getOptionName1() {
		return optionName1;
	}

	public void setOptionName1(String optionName1) {
		this.optionName1 = optionName1;
	}

	public String getOptionIcon1() {
		return ImgUtils.getImgPath(optionIcon1);
	}

	public void setOptionIcon1(String optionIcon1) {
		this.optionIcon1 = optionIcon1;
	}

	public String getOptionName2() {
		return optionName2;
	}

	public void setOptionName2(String optionName2) {
		this.optionName2 = optionName2;
	}

	public String getOptionIcon2() {
		return ImgUtils.getImgPath(optionIcon2);
	}

	public void setOptionIcon2(String optionIcon2) {
		this.optionIcon2 = optionIcon2;
	}

	public String getOptionName3() {
		return optionName3;
	}

	public void setOptionName3(String optionName3) {
		this.optionName3 = optionName3;
	}

	public String getOptionIcon3() {
		return ImgUtils.getImgPath(optionIcon3);
	}

	public void setOptionIcon3(String optionIcon3) {
		this.optionIcon3 = optionIcon3;
	}

	public String getOptionName4() {
		return optionName4;
	}

	public void setOptionName4(String optionName4) {
		this.optionName4 = optionName4;
	}

	public String getOptionIcon4() {
		return ImgUtils.getImgPath(optionIcon4);
	}

	public void setOptionIcon4(String optionIcon4) {
		this.optionIcon4 = optionIcon4;
	}

	public String getSubjectResult() {
		return subjectResult;
	}

	public void setSubjectResult(String subjectResult) {
		this.subjectResult = subjectResult;
	}


	public String getOptionTag1() {
		return optionTag1;
	}

	public void setOptionTag1(String optionTag1) {
		this.optionTag1 = optionTag1;
	}

	public String getOptionTag2() {
		return optionTag2;
	}

	public void setOptionTag2(String optionTag2) {
		this.optionTag2 = optionTag2;
	}

	public String getOptionTag3() {
		return optionTag3;
	}

	public void setOptionTag3(String optionTag3) {
		this.optionTag3 = optionTag3;
	}

	public String getOptionTag4() {
		return optionTag4;
	}

	public void setOptionTag4(String optionTag4) {
		this.optionTag4 = optionTag4;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	
	

}
