package com.funshion.activity.funtvactivity.service;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.common.utils.ImgUtils;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo.FuntvActivityFirstPageFloorInfo;
import com.funshion.activity.funtvactivity.entity.FuntvActivityFirstPageInfo.FuntvActivityFirstPageItemInfo;
import com.funshion.activity.funtvactivity.mapper.FuntvActivityFirstPageConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class FuntvActivityFirstPageService {

    @Autowired
    private FuntvActivityFirstPageConfigMapper funtvActivityFirstPageConfigMapper;

    public Result<?> funtvActivityFirstPage(int activityType) {
        FuntvActivityFirstPageInfo config = funtvActivityFirstPageConfigMapper.findActivity(activityType);
        if (config == null) {
            return Result.getFailureResult("401", "没有此活动~");
        }
        List<FuntvActivityFirstPageFloorInfo> floors = funtvActivityFirstPageConfigMapper.findFloors(config.getId());
        if (floors == null || floors.size() == 0) {
            return Result.getFailureResult("402", "活动正在规划中~");
        }
        config.setFloors(floors);
        for (FuntvActivityFirstPageFloorInfo floor : floors) {
            floor.setBgImg(ImgUtils.getImgPath(floor.getBgImg()));
        }
        List<FuntvActivityFirstPageItemInfo> details = funtvActivityFirstPageConfigMapper.findFloorDetails(floors);
        for (FuntvActivityFirstPageItemInfo detail : details) {
            detail.setBgImg(ImgUtils.getImgPath(detail.getBgImg()));
            detail.setHoverImg(ImgUtils.getImgPath(detail.getHoverImg()));
            FuntvActivityFirstPageFloorInfo floor = this.getFuntvActivityFirstPageFloorInfo(floors, detail.getActivityFloorId());
            if (floor != null) {
                floor.getCols().add(detail);
            }
        }
        return Result.getSuccessResult(config);
    }

    private FuntvActivityFirstPageFloorInfo getFuntvActivityFirstPageFloorInfo(List<FuntvActivityFirstPageFloorInfo> floors, Integer id) {
        for (FuntvActivityFirstPageFloorInfo floor : floors) {
            if (floor.getId().equals(id)) {
                return floor;
            }
        }
        return null;
    }

}
