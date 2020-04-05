package com.funshion.activity.moviesummer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
public class MovieSummerFirstPageResponse {

	private Integer subjectId;

	private String subjectName;

	private String startTime;

	private String endTime;

	private int status;

	private List<MovieSummerFirstPageMedias> medias = new ArrayList<>();

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

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

	public List<MovieSummerFirstPageMedias> getMedias() {
		return medias;
	}

	public void setMedias(List<MovieSummerFirstPageMedias> medias) {
		this.medias = medias;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static class MovieSummerFirstPageMedias {
		private Integer mediaId;
		private String mediaName;
		private String mediaPicture;
		private int mediaType;//1 movie , 2 topic

		public Integer getMediaId() {
			return mediaId;
		}

		public void setMediaId(Integer mediaId) {
			this.mediaId = mediaId;
		}

		public String getMediaName() {
			return mediaName;
		}

		public void setMediaName(String mediaName) {
			this.mediaName = mediaName;
		}

		public String getMediaPicture() {
			return mediaPicture;
		}

		public void setMediaPicture(String mediaPicture) {
			this.mediaPicture = mediaPicture;
		}

		public int getMediaType() {
			return mediaType;
		}

		public void setMediaType(int mediaType) {
			this.mediaType = mediaType;
		}
	}

}
