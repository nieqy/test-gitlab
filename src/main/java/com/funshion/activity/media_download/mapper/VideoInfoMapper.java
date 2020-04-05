package com.funshion.activity.media_download.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.*;

import com.funshion.activity.media_download.entity.MediaInfoEntity;

@Mapper
public interface VideoInfoMapper {

	@Delete({"delete from  media_download.fd_media_info   where material_id = #{materialId}  "})
	void deleteMedia(@Param("materialId") String paramString);

	@Select({"select * from media_download.fd_media_info order by id asc  limit 5000 "})
	List<MediaInfoEntity> selectDeleteMedia();

	@Select({"select * from media_download.fd_media_info where v_status = 'L'    and v_ip = #{ip}  limit 500 "})
	List<MediaInfoEntity> selectFinishedMedia(@Param("ip") String paramString);

	@Update({"update media_download.fd_media_info  set v_status = 'F'  where material_id = #{materialId}  "})
	void deleteFinishedMedia(@Param("materialId") String paramString);

	@Select({"select * from media_download.fd_media_info where v_status = 'N' and download_times < 5 order by id desc  limit 300 "})
	Set<MediaInfoEntity> selectDownloadMedia();

	@Select({"select * from media_download.fd_media_info where v_status = 'T'   and v_ip = #{ip}  "})
	List<MediaInfoEntity> selectTagsMedia(@Param("ip") String paramString);

	@Select({"select * from media_download.fd_media_info where  material_id = #{materialId} "})
	List<MediaInfoEntity> selectMediaInfoById(@Param("materialId") String paramString);

	@Select({"select * from media_download.fd_media_info where v_status = 'D'  and v_ip = #{ip} limit 60 "})
	List<MediaInfoEntity> selectPostMedia(@Param("ip") String paramString);

	@Select({"select * from media_download.fd_media_info where  v_status = 'P'   and v_ip = #{ip}  and dirtywords is null and  now() > DATE_ADD(v_post_time,INTERVAL 60 MINUTE)  limit 60 "})
	List<MediaInfoEntity> selectPostedMedia(@Param("ip") String paramString);

	@Update({"update media_download.fd_media_info  set v_status = 'D',v_file_path = #{filePath} ,v_ip = #{ip}  where material_id = #{materialId} "})
	Integer updateDownloadMedia(@Param("materialId") String paramString1, @Param("finishTime") Date paramDate, @Param("filePath") String paramString2, @Param("ip") String paramString3);

	@Update({"update media_download.fd_media_info  set download_times = download_times+1 where material_id = #{materialId} "})
	Integer updateDownloadTimesMedia(@Param("materialId") String paramString);

	@Update({"update media_download.fd_media_info  set v_status = 'P',v_post_time = #{postTime} where material_id = #{materialId} "})
	Integer updatePostMedia(@Param("materialId") String paramString, @Param("postTime") Date paramDate);

	@Update({"update media_download.fd_media_info  set v_status = 'T',dirtywords = #{tags} where material_id = #{materialId} "})
	Integer updateMediaTags(@Param("materialId") String paramString1, @Param("tagsTime") Date paramDate, @Param("tags") String paramString2);

	@Update({"update media_download.fd_media_info  set v_status = 'L' where material_id = #{materialId} "})
	Integer updateTagsMedia(@Param("materialId") String paramString);
	
}
