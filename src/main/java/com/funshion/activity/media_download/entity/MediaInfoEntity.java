package com.funshion.activity.media_download.entity;

import java.util.Date;

public class MediaInfoEntity {

	private String materialId;

	private String fileName;

	private String channelName;

	private String filePath;

	private String vFilePath;

	private String lFilePath;

	private String fileSize;

	private String downUrl;

	private Date createTime;

	private String status;

	private String vStatus;

	private String lStatus;

	private Date downloadTime;

	private String tags;

	private String dirtywords;

	private String returnFlag;// 针对视频排重 0：无需返回 ，1：需要返回

	private String processType;// 1:排重，2：脏话，3：遮标logo，4：二维码识别 5: 图片遮标

	private String logo;

	private String iStatus;

	private String imageBlocking;

	private String iFilePath;

	private String iDownUrl;
	
	public String getiDownUrl() {
		return iDownUrl;
	}

	public void setiDownUrl(String iDownUrl) {
		this.iDownUrl = iDownUrl;
	}

	public String getiFilePath() {
		return iFilePath;
	}

	public void setiFilePath(String iFilePath) {
		this.iFilePath = iFilePath;
	}

	public String getImageBlocking() {
		return imageBlocking;
	}

	public void setImageBlocking(String imageBlocking) {
		this.imageBlocking = imageBlocking;
	}

	public String getiStatus() {
		return iStatus;
	}

	public void setiStatus(String iStatus) {
		this.iStatus = iStatus;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getlFilePath() {
		return lFilePath;
	}

	public void setlFilePath(String lFilePath) {
		this.lFilePath = lFilePath;
	}

	public String getlStatus() {
		return lStatus;
	}

	public void setlStatus(String lStatus) {
		this.lStatus = lStatus;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getvStatus() {
		return vStatus;
	}

	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}

	public String getDirtywords() {
		return dirtywords;
	}

	public void setDirtywords(String dirtywords) {
		this.dirtywords = dirtywords;
	}

	public String getvFilePath() {
		return vFilePath;
	}

	public void setvFilePath(String vFilePath) {
		this.vFilePath = vFilePath;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((downUrl == null) ? 0 : downUrl.hashCode());
		result = prime * result + ((materialId == null) ? 0 : materialId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaInfoEntity other = (MediaInfoEntity) obj;
		if (downUrl == null) {
			if (other.downUrl != null)
				return false;
		} else if (!downUrl.equals(other.downUrl))
			return false;

		if (materialId == null) {
			if (other.materialId != null)
				return false;
		} else if (!materialId.equals(other.materialId))
			return false;
		return true;
	}
}
