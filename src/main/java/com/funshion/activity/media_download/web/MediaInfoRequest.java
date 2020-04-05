package com.funshion.activity.media_download.web;

import java.util.Date;

public class MediaInfoRequest {

	private String material_id;

	private String file_name;

	private String channel_name;

	private String file_path;

	private String file_size;

	private String down_url;

	private Date create_time;

	private String return_flag;// 针对视频排重 0：无需返回 ，1：需要返回

	private String process_type;// 1:排重，2：脏话，3：遮标logo，4：二维码识别

	private String status;

	private String vStatus;

	private String lStatus;

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

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getvStatus() {
		return vStatus;
	}

	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}

	public String getlStatus() {
		return lStatus;
	}

	public void setlStatus(String lStatus) {
		this.lStatus = lStatus;
	}

	public String getReturn_flag() {
		return return_flag;
	}

	public void setReturn_flag(String return_flag) {
		this.return_flag = return_flag;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	@Override
	public String toString() {
		return "MediaInfoRequest [material_id=" + material_id + ", file_name=" + file_name + ", channel_name="
				+ channel_name + ", return_flag=" + return_flag + ", process_type=" + process_type + "]";
	}

}
