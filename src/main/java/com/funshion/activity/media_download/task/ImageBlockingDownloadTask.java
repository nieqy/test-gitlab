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
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.funshion.activity.common.redis.RedisService;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.TimeUtils;
import com.funshion.activity.media_download.entity.MediaInfoEntity;
import com.funshion.activity.media_download.mapper.ImageBlockingInfoMapper;
import com.funshion.activity.media_download.web.MediaDownloadController;

import ch.qos.logback.classic.net.SyslogAppender;

@Component
public class ImageBlockingDownloadTask implements SchedulingConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(MediaDownloadController.class);

	// private final static String filePath = "C:\\" + File.separator + "home" +
	// File.separator + "data" + File.separator + "video";
	private final static String filePath = File.separator + "home" + File.separator + "data" + File.separator + "video";

	private final BlockingQueue<MediaInfoEntity> downQueue = new LinkedBlockingQueue<MediaInfoEntity>();
	// 队列排列元素FIFO（先进先出）
	private Semaphore sem = new Semaphore(2);
	// 线程阻塞容器
	private final static String lindaUrl = "http://172.17.26.92:8080/linda/pcwCallBack";

	private final static String imgUrl = "http://img.funshion.com/sdw?w=0&h=0&oid=";

	private final static String REDIS_QUEUE_KEY = "imageblocking_download_redis_queue_key_";

	private final static String REDIS_SET_KEY = "imageblocking_download_redis_set_key_";

	private static String localIP = "";

	private static String REDIS_POST_SET_KEY;

	@Value("${schedule.on.off}")
	private String schedule_on_off;

	@Autowired
	private ImageBlockingInfoMapper imageBlockingInfoMapper;

	@Autowired
	private RedisService jedisService;

	@Autowired
	private RestTemplate restTemplate;

	private static String taskLable = "imageblocking";

	static {
		try {
			// 获得本机的所有网络接口
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();// 返回此计算机上的所有接口放入Enumeration中
			InetAddress ip = null;

			while (allNetInterfaces.hasMoreElements()) {// 测试此枚举是否包含更多的元素。
				NetworkInterface netInterface = allNetInterfaces.nextElement();// 如果此枚举对象至少还有一个可提供的元素，则返回此枚举的下一个元素。
				// 返回网络接口是否为环回接口。 返回此接口是否为虚拟接口（也称为子接口）。 返回网络接口是否已启动并正在运行。
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {// 同外层循环
						ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {// 是不是ipv4
							localIP = ip.getHostAddress();// 得到ip地址
							REDIS_POST_SET_KEY = "imageblocking_download_redis_post_set_key_" + localIP;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("IP地址获取失败", e);
		}
	}

	// 清空这个目录下面的文件夹 且根据i_status = 'L' 和i_ip 查询 前500行的地址 遍历删掉 修改 i_status = 'F'
	@Scheduled(cron = "0 * * * * ?")
	public void deletePostFile() {
		if (!taskLable.equals(schedule_on_off)) {
			return;
		}
		try {
			File root = new File(filePath);
			File[] files = root.listFiles();// 遍历这个目录下的文件

			for (File file : files) {// 遍历
				if (file.isDirectory() && file.list().length == 0) {// 如果是一个空目录
					file.delete();// 是就删除
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(), e);
		}
		List<MediaInfoEntity> medias = imageBlockingInfoMapper.selectFinishedMedia(localIP);
		// 根据i_status = 'L' 和i_ip 查询 前500行
		String day = TimeUtils.formatStringOnly(TimeUtils.getCurrentDate());
		String path = filePath + File.separator + day + File.separator;
		// System.out.println(localIP);
		// System.out.println("方法进入!"+"/n123");
		// System.out.println(medias.get(0).getiDownUrl());
		for (MediaInfoEntity media : medias) {// 遍历插到的数据
			// System.out.println(media.getDownUrl());
			// System.out.println(media.getMaterialId());
			// System.out.println(media.getiFilePath());
			if (StringUtils.isEmpty(media.getiFilePath())) {// 先判断是不是空的
				// System.out.println(media.getiFilePath());
			} else {
				JSONObject json = JSON.parseObject(media.getiFilePath());
//				System.out.println(json.toJSONString());
//				System.out.println(json.toString());
//				String j = json.get("still").toString();
//				System.out.println("--");
//				System.out.println(json.get("still").toString());
//				System.out.println(json.get("still"));
//				System.out.println(json);
				if (media.getiFilePath().indexOf("posterOid") != -1) {
					// String posterOid = (String) json.get("poster_oid");
					File file = new File(path + media.getMaterialId() + "posterOid.jpg");
					if (file.exists()) {
						file.delete();
					}
					logger.info("删掉处理后文件:" + path + media.getMaterialId() + "posterOid.jpg");
					// logger.info("delete media:id:{},name:{},size:{},path:{}",
					// media.getMaterialId(), media.getFileName(),
					// media.getFileSize(), media.getlFilePath());
				}
				if (media.getiFilePath().indexOf("stillOid") != -1) {
					// String stillOid = (String) json.get("still_oid");
					File file = new File(path + media.getMaterialId() + "stillOid.jpg");
					if (file.exists()) {
						file.delete();
					}
					logger.info("删掉处理后文件:" + path + media.getMaterialId() + "stillOid.jpg");
					// logger.info("delete media:id:{},name:{},size:{},path:{}",
					// media.getMaterialId(), media.getFileName(),
					// media.getFileSize(), media.getiFilePath());
				}
			}
//			File file = new File(media.getiFilePath());// 取出来地址
//			if (file.exists()) {// 判断存不存在
//				file.delete();// 存在就删除
//			}

			imageBlockingInfoMapper.deleteFinishedMedia(media.getMaterialId());
//			// 修改这条数据的 i_status = 'F'
			logger.info("delete media:id:{},name:{},size:{},path:{}", media.getMaterialId(), media.getFileName(),
					media.getFileSize(), media.getiFilePath());
		}
	}

	// 将数据放入Redis队列
	@Scheduled(cron = "0 * * * * ?")
	public void putDataInRedisQueue() { // 要处理的数据 要下载的数据
		if (!taskLable.equals(schedule_on_off)) {
			return;
		}
		logger.info("*********************image download task run:" + localIP + "*****************************");
		// 设置 String 类型 key-value 并添加过期时间,key已经存在则返回false 相当于加锁操作
		if (jedisService.setIfAbsent(REDIS_SET_KEY + "exist", 1, 30, TimeUnit.SECONDS)) {// 30是过期时间
			Set<MediaInfoEntity> medias = imageBlockingInfoMapper.selectDownloadMedia();
			// 查询l_status = 'N' and download_times(下载次数? ) < 5 前300条
			medias = medias == null ? new HashSet<MediaInfoEntity>() : medias;
			// 如果等于medias==null为new HashSet<MediaInfoEntity>() 否则medias(本身)
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>) jedisService.get(REDIS_SET_KEY);
			// 获取 String 类型 key-value
			existsMedias = existsMedias == null ? new HashSet<MediaInfoEntity>() : existsMedias;
			// 同上 不出意外new了个? 第二次就有
			if (existsMedias.size() > 1000) {
				return;
			}
			for (MediaInfoEntity media : medias) {// 遍历前面的 查询i_status = 'N' and download_times(下载次数? ) < 5 前300条的数据
				if (!existsMedias.contains(media) // 是否有子字符串(且五天内操作过?) 没有就进入
						&& media.getCreateTime().after(TimeUtils.getDateIncDayCount(new Date(), -5))) { // getDateIncDayCount计算日期累加多少天之后的日期
					// Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false； (五天前)
					jedisService.leftPush(REDIS_QUEUE_KEY, media);// list左入栈
				}
			}
			existsMedias.addAll(medias);
			jedisService.set(REDIS_SET_KEY, existsMedias); // 原本自己REDIS_SET_KEY的redis值加上medias的
		}
	}

	@Scheduled(cron = "0 * * * * ?") // 筛选数据
	public void postTags() {
		if (!taskLable.equals(schedule_on_off)) {
			return;
		}
		// redis中移除掉i_status = 'T' and i_ip = #{ip}(当前IP) 的数据
		logger.info("*********************image post data to linda:" + localIP + "*****************************");
		List<MediaInfoEntity> medias = imageBlockingInfoMapper.selectTagsMedia(localIP);
		// 根据l_status = 'T' and l_ip = #{ip} 查询数据
		for (MediaInfoEntity media : medias) {// 遍历插到的list集合
			Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>) jedisService.get(REDIS_SET_KEY);
			// redis中获取REDIS_SET_KEY 上一步的
			if (existsMedias != null) {
				if (existsMedias.remove(media)) {// 移除里面的这个元素
					jedisService.set(REDIS_SET_KEY, existsMedias);// 再存进去
				}
			}
			String res = null;
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("process_type", "5");
				params.put("data", media.getImageBlocking());
				res = HttpClientUtils.post(lindaUrl, params); // 客户端post请求
				if (!StringUtils.isEmpty(res)) {// 不为空true
					JSONObject json = JSON.parseObject(res);// 转成json格式
					if ("success".equals(json.getString("result"))) { // 获取这个和字符串判断
						imageBlockingInfoMapper.updateTagsMedia(media.getMaterialId());
						// 相等修改i_status = 'L'
					}
				}
				logger.info("post url:{},params:{},Res:{}", lindaUrl, JSON.toJSONString(params), res);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	// 提交数据给服务处理
	@Scheduled(cron = "0 * * * * ?")
	public void postDownload() {
		if (!taskLable.equals(schedule_on_off)) {
			return;
		}
		logger.info("*********************image post video to service:" + localIP + "*****************************");
		List<MediaInfoEntity> media1 = imageBlockingInfoMapper.selectPostMedia(localIP); // l_status = 'D'下载完 未提交的
		List<MediaInfoEntity> media2 = imageBlockingInfoMapper.selectPostedMedia(localIP);// l_status = 'P' 提交过未处理的
		media1.addAll(media2);
		for (MediaInfoEntity media : media1) {
			doPost(media);
		}

	}

	public void doPost(MediaInfoEntity media) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("video", media.getiFilePath());
		params.put("mediaId", media.getMaterialId());
		try {// REDIS_POST_SET_KEY 要给的key
			jedisService.add(REDIS_POST_SET_KEY, JSON.toJSONString(params));// 添加set
			imageBlockingInfoMapper.updatePostMedia(media.getMaterialId(), new Date());// 更新代码
			// l_status = 'P'
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(), e);
		}
		// logger.info("post
		// media:id:{},name:{},size:{},path:{},Res:{}",media.getMaterialId(),media.getFileName(),media.getFileSize(),media.getFilePath(),res);
	}

	// 开始处理数据
	@Scheduled(cron = "0/1 * * * * ?") // 每个1S
	public void downloadQueue() {
		if (!taskLable.equals(schedule_on_off)) {
			return;
		}
		File file = new File(filePath);
		long bytes = file.getFreeSpace();// 获取磁盘大小
		if (bytes / 1024 / 1024 < 10000) {
			return;// 还有10G就不处理
		}
		MediaInfoEntity media;
		try {
			while (null != (media = (MediaInfoEntity) jedisService.leftPop(REDIS_QUEUE_KEY))) {
				// 指定 list 从左出栈如果列表没有元素,会堵塞到列表一直有元素或者超时为止
				if (file.getFreeSpace() / 1024 / 1024 < 10000) {
					break;
				}
				sem.acquire();// 一个消费许可证
				final MediaInfoEntity runMedia = media;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						doDownload(runMedia);
					}
				}).start();
			}
			if (null == media) {
				jedisService.delete(REDIS_SET_KEY);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doDownload(MediaInfoEntity media) {// 传进来这个对象
		try {
			List<MediaInfoEntity> medias = imageBlockingInfoMapper.selectMediaInfoById(media.getMaterialId());
			// 根据material_id查询
			if (!downQueue.contains(media) && "N".equals(medias.get(0).getiStatus())) {
				// BlockingQueue阻塞队列 不包含media这个对象 且 这个对象的状态lStatus不为N
				downQueue.add(media);// 就在阻塞队列中添加media这个对象
				String day = TimeUtils.formatStringOnly(TimeUtils.getCurrentDate());// 根据date转化成string yyyy-MM-dd
				String path = filePath + File.separator + day + File.separator;// 地址
				// /home/data/video/day/
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();// 不存在就创建目录
				}
				// 需求修改
				// String fileName =
				// media.getDownUrl().substring(media.getDownUrl().lastIndexOf("/") + 1);// 截取地址
				// String filePath = path + fileName;
				// String filePath = path
				logger.info("begin  download:id:{},name:{},size:{},url:{}", media.getMaterialId(), media.getFileName(),
						media.getFileSize(), media.getiDownUrl());
				try {
					JSONObject json = JSON.parseObject(media.getiDownUrl());
					// System.out.println(json.toString());
					// System.out.println(json.toJSONString());
					if (StringUtils.isEmpty(media.getiDownUrl())) {
						return;
					}
					if (media.getiDownUrl().indexOf("poster_oid") != -1) {
						// 判断poster_oid存在
						String posterOid = (String) json.get("poster_oid");
						HttpClientUtils.download(imgUrl + posterOid, path + media.getMaterialId() + "posterOid.jpg"); // 海报
						// /home/data/video/day/MaterialId/posterOid 海报
						if (media.getiDownUrl().indexOf("still_oid") != -1) {
							// 判断still_oid存在 判断poster_oid存在
							String stillOid = (String) json.get("still_oid");
							HttpClientUtils.download(imgUrl + stillOid, path + media.getMaterialId() + "stillOid.jpg"); // 剧照
							// /home/data/video/day/MaterialId/stillOid 剧照
							File downFile = new File(path + media.getMaterialId() + "stillOid.jpg");
							logger.info("finish download:id:{},name:{},size:{},url:{}", media.getMaterialId(),
									media.getFileName(), downFile.length(), media.getiDownUrl());
							String Paths = "{\"still\":{\"oid\":\"" + stillOid + "\",\"path\":\"" + path
									+ media.getMaterialId() + "stillOid.jpg" + "\"},\"poster\":{\"oid\":\"" + posterOid
									+ "\",\"path\":\"" + path + media.getMaterialId() + "posterOid.jpg" + "\"}}";
							System.out.println("Paths" + Paths);
							imageBlockingInfoMapper.updateDownloadMedia(media.getMaterialId(), new Date(), Paths, // 这个地方改好上面可以直接用
									localIP);
							System.out.println("修改完成");
							// }
						} else {
							// 判断still_oid不存在 判断poster_oid存在
							File downFile = new File(path + media.getMaterialId() + "posterOid.jpg");
							logger.info("finish download:id:{},name:{},size:{},url:{}", media.getMaterialId(),
									media.getFileName(), downFile.length(), media.getiDownUrl());
							// if (downFile.length() == Long.parseLong(media.getFileSize())) {
							String Paths = "{\"still\":{\"oid\":\"" + "\",\"path\":\"" + "\"},\"poster\":{\"oid\":\""
									+ posterOid + "\",\"path\":\"" + path + media.getMaterialId() + "posterOid.jpg"
									+ "\"}}";
							imageBlockingInfoMapper.updateDownloadMedia(media.getMaterialId(), new Date(), Paths, // 这个地方改好上面可以直接用
									localIP);
							// }
						}
					} else {
						if (media.getiDownUrl().indexOf("still_oid") != -1) {
							// 判断still_oid存在 判断poster_oid不存在
							// String stillOid =
							// media.getDownUrl().substring(media.getDownUrl().indexOf("still_oid") + 12,
							// media.getDownUrl().indexOf("\"", media.getDownUrl().indexOf("still_oid") +
							// 12));
							String stillOid = (String) json.get("still_oid");
							HttpClientUtils.download(imgUrl + stillOid, path + media.getMaterialId() + "stillOid.jpg");

							File downFile = new File(path + media.getMaterialId() + "posterOid.jpg");
							logger.info("finish download:id:{},name:{},size:{},url:{}", media.getMaterialId(),
									media.getFileName(), downFile.length(), media.getiDownUrl());
							// if (downFile.length() == Long.parseLong(media.getFileSize())) {
							String Paths = "{\"still\":{\"oid\":\"" + stillOid + "\",\"path\":\"" + path
									+ media.getMaterialId() + "stillOid.jpg" + "\"},\"poster\":{\"oid\":\""
									+ "\",\"path\":\"" + "\"}}";
							imageBlockingInfoMapper.updateDownloadMedia(media.getMaterialId(), new Date(), Paths, // 这个地方改好上面可以直接用
									localIP);
							// }
						} else {
							// 判断still_oid不存在 判断poster_oid不存在
							return;
						}
					}
					// HttpClientUtils.download(media.getDownUrl(), filePath);// download file 下载文件
					/*
					 * File downFile = new File(filePath);
					 * logger.info("finish download:id:{},name:{},size:{},url:{}",
					 * media.getMaterialId(), media.getFileName(), downFile.length(),
					 * media.getDownUrl()); if (downFile.length() ==
					 * Long.parseLong(media.getFileSize())) {// 长解析?
					 * imageBlockingInfoMapper.updateDownloadMedia(media.getMaterialId(), new
					 * Date(), filePath, // 这个地方改好上面可以直接用 localIP);
					 */
					// l_status = 'D'

				} catch (Exception e) {
					// TODO: handle exception
					logger.error(e.getLocalizedMessage(), e);
				}
				imageBlockingInfoMapper.updateDownloadTimesMedia(media.getMaterialId());// 下载次数+1
				downQueue.remove(media);
				Set<MediaInfoEntity> existsMedias = (Set<MediaInfoEntity>) jedisService.get(REDIS_SET_KEY);
				if (existsMedias != null) {
					if (existsMedias.remove(media)) {
						jedisService.set(REDIS_SET_KEY, existsMedias);
					}
				}
			}
		} catch (

		Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(), e);
		} finally {
			sem.release();
		}
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5)); // 5个线程来处理。
	}

}
