package com.funshion.activity.media_download.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.funshion.activity.media_download.entity.MediaInfoEntity;

@Mapper
public interface ImageBlockingInfoMapper {

	@Update("update media_download.fd_media_info  set i_status = 'T',imageblocking = #{tags} where material_id = #{materialId} ")
	public Integer updateMediaTags(@Param("materialId") String materialId, @Param("tagsTime") Date tagsTime,
			@Param("tags") String tags);

	@Select("select * from media_download.fd_media_info where i_status = 'L'  and i_ip = #{ip}  limit 500 ")
	public List<MediaInfoEntity> selectFinishedMedia(@Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set i_status = 'F'  where material_id = #{materialId}  ")
	public void deleteFinishedMedia(@Param("materialId") String materialId);

	@Select("select * from media_download.fd_media_info where i_status = 'N' and download_times < 5 order by id desc  limit 300 ")
	public Set<MediaInfoEntity> selectDownloadMedia();

	@Select("select * from media_download.fd_media_info where i_status = 'T' and i_ip = #{ip}  ")
	public List<MediaInfoEntity> selectTagsMedia(@Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set i_status = 'L' where material_id = #{materialId} ")
	public Integer updateTagsMedia(@Param("materialId") String materialId);

	@Select("select * from media_download.fd_media_info where i_status = 'D'  and i_ip = #{ip} limit 60 ")
	public List<MediaInfoEntity> selectPostMedia(@Param("ip") String localIP);

	@Select("select * from media_download.fd_media_info where  i_status = 'P'   and i_ip = #{ip}  and imageblocking is null and  now() > DATE_ADD(i_post_time,INTERVAL 60 MINUTE)  limit 60 ")
	public List<MediaInfoEntity> selectPostedMedia(@Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set i_status = 'P',i_post_time = #{postTime} where material_id = #{materialId} ")
	public Integer updatePostMedia(@Param("materialId") String materialId, @Param("postTime") Date postTime);

	@Select("select * from media_download.fd_media_info where  material_id = #{materialId} ")
	public List<MediaInfoEntity> selectMediaInfoById(@Param("materialId") String materialId);

	@Update("update media_download.fd_media_info  set i_status = 'D',i_file_path = #{filePath} ,i_ip = #{ip}  where material_id = #{materialId} ")
	public Integer updateDownloadMedia(@Param("materialId") String materialId, @Param("finishTime") Date finishTime,
			@Param("filePath") String filePath, @Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set download_times = download_times+1 where material_id = #{materialId} ")
	public Integer updateDownloadTimesMedia(@Param("materialId") String materialId);

}
