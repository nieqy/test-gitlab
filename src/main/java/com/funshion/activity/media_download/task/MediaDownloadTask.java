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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.TimeUtils;
import com.funshion.activity.media_download.entity.MediaInfoEntity;
import com.funshion.activity.media_download.mapper.MediaInfoMapper;
import com.funshion.activity.media_download.web.MediaDownloadController;

@Component
public class MediaDownloadTask implements SchedulingConfigurer{
	private static final Logger logger = LoggerFactory.getLogger(MediaDownloadController.class);

	private static final String filePath = File.separator + "home" + File.separator + "data" + File.separator + "video";

	private final BlockingQueue<MediaInfoEntity> downQueue = new LinkedBlockingQueue<>();

	private Semaphore sem = new Semaphore(1);

	private static final String postTagUrl = "http://172.17.26.92:8080/linda/pcwCallBack";

	private static final String REDIS_QUEUE_KEY = "media_download_redis_queue_key_";

	private static final String REDIS_SET_KEY = "media_download_redis_set_key_";

	private static String REDIS_POST_SET_KEY;

	@Value("${schedule.on.off}")
	private String schedule_on_off;

	@Autowired
	private MediaInfoMapper mediaInfoMapper;

	@Autowired
	private RedisService jedisService;

	private static String localIP = "";

	private static String taskLable = "media";

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
						REDIS_POST_SET_KEY = "media_download_redis_post_set_key_" + localIP;
					}

				}
			}
		} catch (Exception e) {
			logger.error("IP", e);
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

		List<MediaInfoEntity> medias = this.mediaInfoMapper.selectFinishedMedia(localIP);
		for (MediaInfoEntity media : medias) {
			File file = new File(media.getFilePath());
			if (file.exists()) {
				file.delete();
			}
			this.mediaInfoMapper.deleteFinishedMedia(media.getMaterialId());
			logger.info("delete media:id:{},name:{},size:{},path:{}", new Object[] { media.getMaterialId(), media.getFileName(), media.getFileSize(), media.getFilePath() });
		}
	}

	@Scheduled(cron = "0 * * * * ?")
	public void putDataInRedisQueue() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		logger.info("*********************media putDataInRedisQueue task run:" + localIP + "*****************************");
		if (this.jedisService.setIfAbsent("media_download_redis_set_key_exist", Integer.valueOf(1), 30L, TimeUnit.SECONDS)) {
			Set<MediaInfoEntity> medias = this.mediaInfoMapper.selectDownloadMedia();
			medias = (medias == null) ? new HashSet<>() : medias;
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("media_download_redis_set_key_");
			existsMedias = (existsMedias == null) ? new HashSet<>() : existsMedias;
			if (existsMedias.size() > 1000) {
				return;
			}
			for (MediaInfoEntity media : medias) {
				if (!existsMedias.contains(media) && media.getCreateTime().after(TimeUtils.getDateIncDayCount(new Date(), -5))) {
					this.jedisService.leftPush("media_download_redis_queue_key_", media);
				}
			}
			existsMedias.addAll(medias);
			this.jedisService.set("media_download_redis_set_key_", existsMedias);
		}
	}



	@Scheduled(cron = "0 * * * * ?")
	public void postTags() {
		if (!taskLable.equals(this.schedule_on_off)) {
			return;
		}
		logger.info("*********************media postTags task run:" + localIP + "*****************************");
		List<MediaInfoEntity> medias = this.mediaInfoMapper.selectTagsMedia(localIP);
		for (MediaInfoEntity media : medias) {
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("media_download_redis_set_key_");
			if (existsMedias != null &&
					existsMedias.remove(media)) {
				this.jedisService.set("media_download_redis_set_key_", existsMedias);
			}

			String res = null;
			try {
				Map<String, String> params = new HashMap<>();
				params.put("process_type", "1");
				params.put("data", media.getTags());
				res = HttpClientUtils.post("http://172.17.26.92:8080/linda/pcwCallBack", params);
				if (!StringUtils.isEmpty(res)) {
					JSONObject json = JSON.parseObject(res);
					if ("success".equals(json.getString("result"))) {
						this.mediaInfoMapper.updateTagsMedia(media.getMaterialId());
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
		logger.info("*********************media postDownload task run:" + localIP + "*****************************");
		List<MediaInfoEntity> media1 = this.mediaInfoMapper.selectPostMedia(localIP);
		List<MediaInfoEntity> media2 = this.mediaInfoMapper.selectPostedMedia(localIP);
		media1.addAll(media2);
		for (MediaInfoEntity media : media1) {
			doPost(media);
		}
	}


	public void doPost(MediaInfoEntity media) {
		Map<String, String> params = new HashMap<>();
		params.put("video", media.getFilePath());
		params.put("channelName", media.getChannelName());
		params.put("mediaId", media.getMaterialId());
		params.put("returnFlag", media.getReturnFlag());
		try {
			this.jedisService.add(REDIS_POST_SET_KEY, new String[] { JSON.toJSONString(params) });
			this.mediaInfoMapper.updatePostMedia(media.getMaterialId(), new Date());
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
			return;
		}
		try {
			MediaInfoEntity media;
			while (null != (media = (MediaInfoEntity)this.jedisService.leftPop("media_download_redis_queue_key_")) &&
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
				this.jedisService.delete("media_download_redis_set_key_");
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}



	public void doDownload(MediaInfoEntity media) {
		try {
			List<MediaInfoEntity> medias = this.mediaInfoMapper.selectMediaInfoById(media.getMaterialId());
			if (!this.downQueue.contains(media) && "N".equals(((MediaInfoEntity)medias.get(0)).getStatus())) {
				this.downQueue.add(media);
				String day = TimeUtils.formatStringOnly(TimeUtils.getCurrentDate());
				String path = MediaDownloadTask.filePath + File.separator + day + File.separator;
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
						this.mediaInfoMapper.updateDownloadMedia(media.getMaterialId(), new Date(), filePath, localIP);
					}
				} catch (Exception e) {

					logger.error(e.getLocalizedMessage(), e);
				}
				this.mediaInfoMapper.updateDownloadTimesMedia(media.getMaterialId());
				this.downQueue.remove(media);
				Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>)this.jedisService.get("media_download_redis_set_key_");
				if (existsMedias != null &&
						existsMedias.remove(media)) {
					this.jedisService.set("media_download_redis_set_key_", existsMedias);
				}
			}

		} catch (Exception e) {

			logger.error(e.getLocalizedMessage(), e);
		} finally {
			this.sem.release();
		}
	}



	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5)); // 5个线程来处理。
	}
	
}
