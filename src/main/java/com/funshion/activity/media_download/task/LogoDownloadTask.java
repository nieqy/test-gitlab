package com.funshion.activity.media_download.task;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.TimeUtils;
import com.funshion.activity.media_download.entity.MediaInfoEntity;
import com.funshion.activity.media_download.mapper.LogoInfoMapper;
import com.funshion.activity.media_download.web.MediaDownloadController;

@Component
public class LogoDownloadTask implements SchedulingConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(MediaDownloadController.class);

	private static final String filePath = File.separator + "home" + File.separator + "data" + File.separator + "video";

	private final BlockingQueue<MediaInfoEntity> downQueue = new LinkedBlockingQueue<>();

	private Semaphore sem = new Semaphore(2);

	private static final String lindaUrl = "http://172.17.26.92:8080/linda/pcwCallBack";

	private static final String REDIS_QUEUE_KEY = "logo_download_redis_queue_key_";

	private static final String REDIS_SET_KEY = "logo_download_redis_set_key_";

	private static String localIP = "";

	private static String REDIS_POST_SET_KEY;

	@Value("${schedule.on.off}")
	private String schedule_on_off;

	@Autowired
	private LogoInfoMapper logoInfoMapper;

	@Autowired
	private RedisService jedisService;

	private static String taskLable = "logo";


	static  {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				}
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && ip instanceof java.net.Inet4Address) {
						localIP = ip.getHostAddress();
						REDIS_POST_SET_KEY = "logo_download_redis_post_set_key_" + localIP;
					}

				}
			}
		} catch (Exception e) {
			logger.error("localIP ERROR", e);
		}
	}




	@Scheduled(cron = "0 * * * * ?")
	public void deletePostFile() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		try {
			File root = new File(filePath);
			File[] files = root.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					if ((file.list()).length == 0) {
						file.delete();
					} else {
						String fileName = file.getName();
						Date fileDay = TimeUtils.parseToDate(fileName);
						if (fileDay.before(TimeUtils.getDateIncDayCount(new Date(), -3))) {
							File[] medias = file.listFiles();
							for (File media : medias) {
								media.delete();
							}
						}

					}
				}
			}
		} catch (Exception e) {

			logger.error(e.getLocalizedMessage(), e);
		}
		List<MediaInfoEntity> medias = this.logoInfoMapper.selectFinishedMedia(localIP);
		for (MediaInfoEntity media : medias) {
			File file = new File(media.getlFilePath());
			if (file.exists()) {
				file.delete();
			}
			this.logoInfoMapper.deleteFinishedMedia(media.getMaterialId());
			logger.info("delete media:id:{},name:{},size:{},path:{}", new Object[] { media.getMaterialId(), media.getFileName(), media.getFileSize(), media.getlFilePath() });
		}
	}

	@Scheduled(cron = "0 * * * * ?")
	public void putDataInRedisQueue() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		logger.info("*********************logo download task run:" + localIP + "*****************************");
		if (this.jedisService.setIfAbsent("logo_download_redis_set_key_exist", Integer.valueOf(1), 30L, TimeUnit.SECONDS)) {
			Set<MediaInfoEntity> medias = this.logoInfoMapper.selectDownloadMedia();
			medias = (medias == null) ? new HashSet<>() : medias;
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("logo_download_redis_set_key_");
			existsMedias = (existsMedias == null) ? new HashSet<>() : existsMedias;
			if (existsMedias.size() > 1000) {
				return;
			}
			for (MediaInfoEntity media : medias) {
				if (!existsMedias.contains(media) && media.getCreateTime().after(TimeUtils.getDateIncDayCount(new Date(), -2))) {
					this.jedisService.leftPush("logo_download_redis_queue_key_", media);
				}
			}
			existsMedias.addAll(medias);
			this.jedisService.set("logo_download_redis_set_key_", existsMedias);
		}
	}



	@Scheduled(cron = "0 * * * * ?")
	public void postTags() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		logger.info("*********************logo post data to linda:" + localIP + "*****************************");
		List<MediaInfoEntity> medias = this.logoInfoMapper.selectTagsMedia(localIP);
		for (MediaInfoEntity media : medias) {
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("logo_download_redis_set_key_");
			if (existsMedias != null &&
					existsMedias.remove(media)) {
				this.jedisService.set("logo_download_redis_set_key_", existsMedias);
			}

			String res = null;
			try {
				Map<String, String> params = new HashMap<>();
				params.put("process_type", "3");
				params.put("data", media.getLogo());
				res = HttpClientUtils.post("http://172.17.26.92:8080/linda/pcwCallBack", params);
				if (!StringUtils.isEmpty(res)) {
					JSONObject json = JSON.parseObject(res);
					if ("success".equals(json.getString("result"))) {
						this.logoInfoMapper.updateTagsMedia(media.getMaterialId());
					}
				}
				logger.info("post url:{},params:{},Res:{}", new Object[] { "http://172.17.26.92:8080/linda/pcwCallBack", JSON.toJSONString(params), res });
			} catch (Exception e) {

				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	@Scheduled(cron = "0 * * * * ?")
	public void postDownload() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		logger.info("*********************logo post video to service:" + localIP + "*****************************");
		List<MediaInfoEntity> media1 = this.logoInfoMapper.selectPostMedia(localIP);
		List<MediaInfoEntity> media2 = this.logoInfoMapper.selectPostedMedia(localIP);
		media1.addAll(media2);
		for (MediaInfoEntity media : media1) {
			doPost(media);
		}
	}


	public void doPost(MediaInfoEntity media) {
		Map<String, String> params = new HashMap<>();
		params.put("video", media.getlFilePath());
		params.put("mediaId", media.getMaterialId());
		try {
			this.jedisService.add(REDIS_POST_SET_KEY, new String[] { JSON.toJSONString(params) });
			this.logoInfoMapper.updatePostMedia(media.getMaterialId(), new Date());
			logger.info("add task:{}", JSON.toJSONString(params) );
		} catch (Exception e) {

			logger.error(e.getLocalizedMessage(), e);
		}
	}


	@Scheduled(cron = "0/1 * * * * ?")
	public void downloadQueue() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		File file = new File(filePath);
		long bytes = file.getFreeSpace();
		if (bytes / 1024L / 1024L < 10000L) {
			logger.info("***************************停止下载任务，磁盘空间不足："+bytes / 1024L / 1024L +"M ***********************************");
			return;
		}
		long size = jedisService.setSize(REDIS_POST_SET_KEY);

		if (size > 100){
			logger.info("***************************停止下载任务，任务队列已满："+size +"***********************************");
			return;
		}
		try {
			MediaInfoEntity media;
			while (null != (media = (MediaInfoEntity)this.jedisService.leftPop("logo_download_redis_queue_key_")) &&
					file.getFreeSpace() / 1024L / 1024L >= 10000L) {


				this.sem.acquire();
				MediaInfoEntity runMedia = media;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						doDownload(runMedia);
					}
				}).start();
			}
			if (null == media) {
				this.jedisService.delete("logo_download_redis_set_key_");
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}



	public void doDownload(MediaInfoEntity media) {
		try {
			List<MediaInfoEntity> medias = this.logoInfoMapper.selectMediaInfoById(media.getMaterialId());
			if (!this.downQueue.contains(media) && "N".equals(((MediaInfoEntity)medias.get(0)).getlStatus())) {
				this.downQueue.add(media);
				String day = TimeUtils.formatStringOnly(TimeUtils.getCurrentDate());
				String path = LogoDownloadTask.filePath + File.separator + day + File.separator;
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				String fileName = media.getDownUrl().substring(media.getDownUrl().lastIndexOf("/") + 1);
				String filePath = path + fileName;
				logger.info("begin  download:id:{},name:{},size:{},url:{}", new Object[] { media.getMaterialId(), media.getFileName(), media.getFileSize(), media.getDownUrl() });
				try {
					HttpClientUtils.download(media.getDownUrl(), filePath);
					File downFile = new File(filePath);
					logger.info("finish download:id:{},name:{},size:{},url:{}", new Object[] { media.getMaterialId(), media.getFileName(), Long.valueOf(downFile.length()), media.getDownUrl() });
					if (downFile.length() == Long.parseLong(media.getFileSize())) {
						this.logoInfoMapper.updateDownloadMedia(media.getMaterialId(), new Date(), filePath, localIP);
					}
				} catch (Exception e) {

					logger.error(e.getLocalizedMessage(), e);
				}
				this.logoInfoMapper.updateDownloadTimesMedia(media.getMaterialId());
				this.downQueue.remove(media);
				Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("logo_download_redis_set_key_");
				if (existsMedias != null &&
						existsMedias.remove(media)) {
					this.jedisService.set("logo_download_redis_set_key_", existsMedias);
				}
			}

		} catch (Exception e) {

			logger.error(e.getLocalizedMessage(), e);
		} finally {
			this.sem.release();
		}
	}





	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) { taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5)); }
}
