package com.funshion.activity.funtvactivity.mapper;

import com.funshion.activity.common.ds.DataSource;
import com.funshion.activity.common.ds.DataSourceType;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccontInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityAccountPrizeInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface FuntvActivityMapper {

    @Select("select * from fa_funtv_activity where is_deleted=0 and status=1 and activity_type=#{activityType} and now() BETWEEN start_time and end_time order by show_seq asc")
    List<FuntvActivityInfo> findFuntvActivity(int activityType);

    @DataSource(DataSourceType.MEMBER_SLAVE_01)
    @Select("select count(1) from fm_vip_account_chip where id=#{tvId} and create_time>=#{beginTime} and create_time<#{endTime}")
    int findAccountInfo2(@Param("tvId") Integer tvId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    @DataSource(DataSourceType.MEMBER_SLAVE_01)
    @Select("select count(1) from fm_vip_account where account_id=#{tvId} and create_time>=#{beginTime} and create_time<#{endTime}")
    int findAccountInfo1(@Param("tvId") Integer tvId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    @DataSource(DataSourceType.FUNTV_SLAVE_01)
    @Select("select model from tv_info where mac=#{mac} and brand='funshion'")
    String getMachineType(String mac);

    @DataSource(DataSourceType.MASTER)
    @Insert("insert into fa_funtv_activity_account" +
            "(activity_type, account_id,mac," +
            "receiving_name,receiving_phone,receiving_address," +
            "phone,last_commit) values(" +
            "#{activityType},#{accountId},#{mac}," +
            "#{receivingName},#{receivingPhone},#{receivingAddress}," +
            "#{phone},now())")
    void addFuntvActivityAccount(FuntvActivityAccontInfo info);

    @DataSource(DataSourceType.MASTER)
    @Update("<script>" +
            "update fa_funtv_activity_account " +
            "set receiving_name=#{receivingName}," +
            "receiving_phone=#{receivingPhone}," +
            "receiving_address=#{receivingAddress} " +
            "where mac=#{mac} and activity_type=#{activityType} " +
            "<if test=\"accountId != null\">" +
            " and account_id=#{accountId} " +
            "</if>" +
            "</script>")
    void updateFuntvActivityAccount(FuntvActivityAccontInfo info);

    @Select("<script>" +
            "select count(*) from fa_funtv_activity_account " +
            "<where> " +
            "<if test=\"phone != null and phone != ''\">" +
            " and phone=#{phone} " +
            "</if>" +
            "<if test=\"mac != null and mac != '' and activityType !=null\">" +
            " and mac=#{mac} and activity_type=#{activityType} " +
            "</if>" +
            "<if test=\"activityType !=null\">" +
            " and activity_type=#{activityType} " +
            "</if>" +
            "<if test=\"accountId != null\">" +
            " and account_id=#{accountId} " +
            "</if>" +
            "</where>" +
            "</script>")
    int countFuntvActivityAccount(@Param("phone") String phone, @Param("accountId") Integer accountId, @Param("mac") String mac, @Param("activityType") Integer activityType);

    @Select("<script>" +
            "select * from fa_funtv_activity_account " +
            "<where> " +
            "<if test=\"phone != null and phone != ''\">" +
            " and phone=#{phone} " +
            "</if>" +
            "<if test=\"mac != null and mac != '' and activityType !=null\">" +
            " and mac=#{mac} and activity_type=#{activityType} " +
            "</if>" +
            "<if test=\"accountId != null\">" +
            " and account_id=#{accountId} " +
            "</if>" +
            "</where>" +
            "</script>")
    FuntvActivityAccontInfo findFuntvActivityAccount(@Param("phone") String phone, @Param("accountId") Integer accountId, @Param("mac") String mac, @Param("activityType") Integer activityType);

    @Select("<script>" +
            "select * from fa_funtv_activity_account_prize " +
            "where mac=#{mac} and activity_type=#{activityType} " +
            "<if test=\"accountId != null\">" +
            " and account_id=#{accountId} " +
            "</if>" +
            "</script>")
    FuntvActivityAccountPrizeInfo findFuntvActivityAccountPrize(@Param("accountId") Integer accountId, @Param("mac") String mac, @Param("activityType") int activityType);

    @Select("select a.*,b.phone from fa_funtv_activity_account_prize a, fa_funtv_activity_account b " +
            "where a.activity_type=#{activityType} and a.prize_flag=#{prizeFlag} and " +
            "b.activity_type=#{activityType} and " +
            "a.account_id=b.account_id and a.mac=b.mac and a.activity_type=b.activity_type")
    List<FuntvActivityAccountPrizeInfo> findAllFuntvActivityAccountPrize(@Param("activityType") int activityType, @Param("prizeFlag") String prizeFlag);

    @DataSource(DataSourceType.MASTER)
    @Update("<script>" +
            "update fa_funtv_activity_account_prize set is_accept=1,create_time=now() " +
            "where mac=#{mac} and activity_type=#{activityType} and prize_flag=#{prizeFlag} " +
            "<if test=\"accountId != null\">" +
            " and account_id=#{accountId} " +
            "</if>" +
            "</script>")
    void acceptPrize(@Param("accountId") Integer accountId, @Param("mac") String mac, @Param("activityType") int activityType, @Param("prizeFlag") String prizeFlag);

    @Select("select * from fa_funtv_activity_account where mac=#{mac}")
    FuntvActivityAccontInfo getFuntvActivityAccontByMac(@Param("mac") String mac);

    @Select("<script>" +
            "select distinct prize_flag from fa_funtv_activity_account_prize " +
            "where activity_type=#{activityType} order by create_time desc" +
            "</script>")
    List<String> getFuntvActivityPrizeFlag(@Param("activityType") int activityType);

    @Select("select * from fa_funtv_activity_account where account_id=#{accountId} and activity_type=#{activityType} order by id desc limit 1")
    FuntvActivityAccontInfo getAccountInfo(@Param("accountId") Integer accountId,
                                           @Param("activityType") Integer activityType);
}
