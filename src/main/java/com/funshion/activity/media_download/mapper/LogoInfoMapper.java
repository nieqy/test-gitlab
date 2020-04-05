package com.funshion.activity.media_download.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.funshion.activity.media_download.entity.MediaInfoEntity;

@Mapper
public interface LogoInfoMapper {

	@Select({"select * from media_download.fd_media_info where l_status = 'L'    and l_ip = #{ip}  limit 500 "})
	List<MediaInfoEntity> selectFinishedMedia(@Param("ip") String paramString);

	@Update({"update media_download.fd_media_info  set l_status = 'F'  where material_id = #{materialId}  "})
	void deleteFinishedMedia(@Param("materialId") String paramString);

	@Select({"select * from media_download.fd_media_info where l_status = 'N' and download_times < 5 order by id desc  limit 300 "})
	Set<MediaInfoEntity> selectDownloadMedia();

	@Select({"select * from media_download.fd_media_info where l_status = 'T'   and l_ip = #{ip}  "})
	List<MediaInfoEntity> selectTagsMedia(@Param("ip") String paramString);

	@Select({"select * from media_download.fd_media_info where  material_id = #{materialId} "})
	List<MediaInfoEntity> selectMediaInfoById(@Param("materialId") String paramString);

	@Select({"select * from media_download.fd_media_info where l_status = 'D'  and l_ip = #{ip} limit 60 "})
	List<MediaInfoEntity> selectPostMedia(@Param("ip") String paramString);

	@Select({"select * from media_download.fd_media_info where  l_status = 'P'   and l_ip = #{ip}  and logo is null and  now() > DATE_ADD(l_post_time,INTERVAL 60 MINUTE)  limit 60 "})
	List<MediaInfoEntity> selectPostedMedia(@Param("ip") String paramString);

	@Update({"update media_download.fd_media_info  set l_status = 'D',l_file_path = #{filePath} ,l_ip = #{ip}  where material_id = #{materialId} "})
	Integer updateDownloadMedia(@Param("materialId") String paramString1, @Param("finishTime") Date paramDate, @Param("filePath") String paramString2, @Param("ip") String paramString3);

	@Update({"update media_download.fd_media_info  set download_times = download_times+1 where material_id = #{materialId} "})
	Integer updateDownloadTimesMedia(@Param("materialId") String paramString);

	@Update({"update media_download.fd_media_info  set l_status = 'P',l_post_time = #{postTime} where material_id = #{materialId} "})
	Integer updatePostMedia(@Param("materialId") String paramString, @Param("postTime") Date paramDate);

	@Update({"update media_download.fd_media_info  set l_status = 'T',logo = #{tags} where material_id = #{materialId} "})
	Integer updateMediaTags(@Param("materialId") String paramString1, @Param("tagsTime") Date paramDate, @Param("tags") String paramString2);

	@Update({"update media_download.fd_media_info  set l_status = 'L' where material_id = #{materialId} "})
	Integer updateTagsMedia(@Param("materialId") String paramString);
	
	
}
