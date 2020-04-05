package com.funshion.activity.media_download.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.funshion.activity.media_download.entity.MediaInfoEntity;
import com.funshion.activity.media_download.web.MediaInfoRequest;

@Mapper
public interface MediaInfoMapper {

	@Update("delete from  media_download.fd_media_info   where material_id = #{materialId}  ")
	public void deleteMedia(@Param("materialId") String materialId);

	@Insert("insert into media_download.fd_media_info (material_id,file_name,channel_name,file_path,file_size,i_down_url,create_time,return_flag,process_type,status,v_status,l_status,i_status) "
			+ "values ( #{material_id},#{file_name},#{channel_name},#{file_path},#{file_size},#{down_url},#{create_time},#{return_flag},#{process_type},#{status},#{vStatus},#{lStatus},#{iStatus} )")
	public Integer insertMediaInfo(MediaInfoRequest info);

	@Select("select * from media_download.fd_media_info order by id asc  limit 500 ")
	public List<MediaInfoEntity> selectDeleteMedia();

	@Select("select * from media_download.fd_media_info where status = 'L' and ip = #{ip} limit 500 ")
	public List<MediaInfoEntity> selectFinishedMedia(@Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set status = #{status} ,process_type = #{process_type},return_flag = #{return_flag} where material_id = #{material_id}  ")
	public void updatePaiChongMediaStatus(MediaInfoRequest info);

	@Update("update media_download.fd_media_info  set v_status=#{vStatus}, process_type = #{process_type} where material_id = #{material_id}  ")
	public void updateZangHuaMediaStatus(MediaInfoRequest info);

	@Update("update media_download.fd_media_info  set l_status = #{lStatus}, process_type = #{process_type}  where material_id = #{material_id}  ")
	public void updateLogoMediaStatus(MediaInfoRequest info);

	@Update("update media_download.fd_media_info  set i_status = #{iStatus}, process_type = #{process_type}, i_down_url= #{down_url}  where material_id = #{material_id}  ")
	public void updateImageBlockingMediaStatus(MediaInfoRequest info);

	@Update("update media_download.fd_media_info  set status = 'F'  where material_id = #{materialId}  ")
	public void deleteFinishedMedia(@Param("materialId") String materialId);

	@Select("select * from media_download.fd_media_info where status = 'N' and download_times < 5  order by id desc  limit 300 ")
	public Set<MediaInfoEntity> selectDownloadMedia();

	@Select("select * from media_download.fd_media_info where status = 'T'   and ip = #{ip}  ")
	public List<MediaInfoEntity> selectTagsMedia(@Param("ip") String localIP);

	@Select("select * from media_download.fd_media_info where  material_id = #{materialId} ")
	public List<MediaInfoEntity> selectMediaInfoById(@Param("materialId") String materialId);

	@Select("select * from media_download.fd_media_info where status = 'D'  and ip = #{ip} limit 60 ")
	public List<MediaInfoEntity> selectPostMedia(@Param("ip") String localIP);

	@Select("select * from media_download.fd_media_info where  status = 'P'   and ip = #{ip}  and tags is null and  now() > DATE_ADD(post_time,INTERVAL 60 MINUTE)  limit 60 ")
	public List<MediaInfoEntity> selectPostedMedia(@Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set status = 'D',download_times = #{finishTime},file_path = #{filePath} ,ip = #{ip}  where material_id = #{materialId} ")
	public Integer updateDownloadMedia(@Param("materialId") String materialId, @Param("finishTime") Date finishTime,
			@Param("filePath") String filePath, @Param("ip") String localIP);

	@Update("update media_download.fd_media_info  set download_times = download_times+1 where material_id = #{materialId} ")
	public Integer updateDownloadTimesMedia(@Param("materialId") String materialId);

	@Update("update media_download.fd_media_info  set status = 'P',post_time = #{postTime} where material_id = #{materialId} ")
	public Integer updatePostMedia(@Param("materialId") String materialId, @Param("postTime") Date postTime);

	@Update("update media_download.fd_media_info  set status = 'T',tags_time = #{tagsTime},tags = #{tags} where material_id = #{materialId} ")
	public Integer updateMediaTags(@Param("materialId") String materialId, @Param("tagsTime") Date tagsTime,
			@Param("tags") String tags);

	@Update("update media_download.fd_media_info  set status = 'L' where material_id = #{materialId} ")
	public Integer updateTagsMedia(@Param("materialId") String materialId);

	@Insert("insert into media_download.fd_media_info (material_id,file_name,channel_name,file_path,file_size,down_url,create_time,return_flag,process_type,status,v_status,l_status,i_status) "
			+ "values ( #{material_id},#{file_name},#{channel_name},#{file_path},#{file_size},#{down_url},#{create_time},#{return_flag},#{process_type},#{status},#{vStatus},#{lStatus},#{iStatus} )")
	public Integer insertMediaInfos(MediaInfoRequest info);

}
