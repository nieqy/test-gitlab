//package com.funshion.activity.jinli.mapper;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Update;
//
//import com.funshion.activity.common.ds.DataSourceType;
//import com.funshion.activity.common.datasource.TargetDataSource;
//import com.funshion.activity.jinli.entity.JinliRankInfo;
//import com.funshion.activity.jinli.entity.JinliUserInfo;
//
//@Mapper
//@TargetDataSource(DataSourceType.MASTER)
//public interface JinliUserInfoMapper {
//
//	@TargetDataSource(DataSourceType.SLAVE)
//	@Select("select * from fa_jinli_user_info where mac=#{mac}")
//	public JinliUserInfo getUserInfoByMac(@Param("mac") String mac);
//
//	@TargetDataSource(DataSourceType.SLAVE)
//	@Select("select mac, total_play_time from fa_jinli_user_info order by total_play_time desc limit 100")
//	public List<JinliRankInfo> getRankInfos();
//
//	@TargetDataSource(DataSourceType.SLAVE)
//	@Select("select count(*) from fa_jinli_user_info")
//	public int getTotalCount();
//
//	@TargetDataSource(DataSourceType.SLAVE)
//	@Select("select count(*)+1 from fa_jinli_user_info where total_play_time > #{totalPlayTime}")
//	public int getMyRanking(@Param("totalPlayTime") Integer totalPlayTime);
//	
//	@Update("update fa_jinli_user_info set "
//			+ " remain_draw_num=#{remainDrawNum}, "
//			+ " new_play_time=#{newPlayTime},"
//			+ " total_play_time=total_play_time + #{newPlayTime},"
//			+ " update_time=now()  where mac=#{mac}")
//	public int updateUserInfo(JinliUserInfo info);
//
//	@Insert("insert into fa_jinli_user_info (mac,remain_draw_num,new_play_time,total_play_time,create_time) values ("
//			+ " #{mac,jdbcType=VARCHAR}, "
//			+ " #{remainDrawNum,jdbcType=INTEGER}, "
//			+ " #{newPlayTime,jdbcType=INTEGER}, "
//			+ " #{totalPlayTime,jdbcType=INTEGER}, "
//			+ " now())")
//	public int saveUserInfo(JinliUserInfo info);
//
//	@Update("update fa_jinli_user_info set remain_draw_num=remain_draw_num-1 where mac=#{mac}")
//	public int updateRemainDrawNumber(@Param("mac") String mac);
//
//}
