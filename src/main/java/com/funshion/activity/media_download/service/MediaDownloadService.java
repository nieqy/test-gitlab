package com.funshion.activity.media_download.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.funshion.activity.media_download.entity.MediaInfoEntity;
import com.funshion.activity.media_download.mapper.ImageBlockingInfoMapper;
import com.funshion.activity.media_download.mapper.LogoInfoMapper;
import com.funshion.activity.media_download.mapper.MediaInfoMapper;
import com.funshion.activity.media_download.mapper.VideoInfoMapper;
import com.funshion.activity.media_download.web.MediaInfoRequest;

@Service
public class MediaDownloadService {

	private static final String PaiChong = "1";

	private static final String ZangHua = "2";

	private static final String ZheBiao = "3";

	private static final String ErWeiMa = "4";
	
	private static final String TuPianZheBiao = "5";

	@Autowired
	private MediaInfoMapper mediaInfoMapper;
	@Autowired
	private VideoInfoMapper videoInfoMapper;
	@Autowired
	private LogoInfoMapper logoInfoMapper;
	@Autowired
	private ImageBlockingInfoMapper imageBlockingInfoMapper;
	
	public void saveMediaInfo(MediaInfoRequest info) {
		// 根据Material_id (必要的伪id) 查询media_download这个数据库下fd_media_info表放入 list中 (可能有多条)
		List<MediaInfoEntity> medias = mediaInfoMapper.selectMediaInfoById(info.getMaterial_id());
		// private String process_type;//1:排重，2：脏话，3：遮标logo，4：二维码识别      5:图片遮标   放入processType
		String processType = info.getProcess_type();
		/**
		 * 将当前入参info的processType为123的Status置为N 
		 */
		if (!StringUtils.isEmpty(processType)) { // 先判空 不为空进入
			if (PaiChong.equals(processType)) {// 排重
				info.setStatus("N");// 将status置为N
			}
			if (ZangHua.equals(processType)) {// 脏话
				info.setvStatus("N");// 将status置为N
			}
			if (ZheBiao.equals(processType)) {// 遮标
				info.setlStatus("N");// 将status置为N
			}
			if (TuPianZheBiao.equals(processType)) {
				info.setiStatus("N");
			}
		}
		
		/**
		 * 先判断数据库里面有不有入参的信息, 没有就直接存入数据库
		 */
		
		if (medias == null || medias.size() <= 0) {// 判断medias(list里面的) MediaInfoEntity 为空进入
			if(TuPianZheBiao.equals(processType)) {//只有5的时候才插入i_down_url)
				mediaInfoMapper.insertMediaInfo(info); // 原封不动插入数据进库info
			}else {
				mediaInfoMapper.insertMediaInfos(info);
			}
		
			
			
		/**
		 * 数据有这条记录的其他信息 , 相当于信息合并  不添加,直接更新
		 */
		} else {// 不为空
			//medias第一条数据的ProcessType , 当前数据info的Process_type   都存入info的Process_type
			info.setProcess_type(medias.get(0).getProcessType() + "," + info.getProcess_type());
			if (PaiChong.equals(processType)) {//如果 processType info的getProcess_type相等
				mediaInfoMapper.updatePaiChongMediaStatus(info);//更新数据
			}
			if (ZangHua.equals(processType)) {
				mediaInfoMapper.updateZangHuaMediaStatus(info);
			}
			if (ZheBiao.equals(processType)) {
				mediaInfoMapper.updateLogoMediaStatus(info);
			}
			if (TuPianZheBiao.equals(processType)) {
				mediaInfoMapper.updateImageBlockingMediaStatus(info);
			}
		}
	}

	public void tagMediaInfo(String material_id, String tags) {
		mediaInfoMapper.updateMediaTags(material_id, new Date(), tags);
	}

	public void dirtywordsMediaInfo(String material_id, String tags) {
		videoInfoMapper.updateMediaTags(material_id, new Date(), tags);
	}

	public void logoMediaInfo(String material_id, String tags) {
		logoInfoMapper.updateMediaTags(material_id, new Date(), tags);
	}

	public void imageBlockingMediaInfo(String material_id, String tags) {
		imageBlockingInfoMapper.updateMediaTags(material_id, new Date(), tags);
	}

}
