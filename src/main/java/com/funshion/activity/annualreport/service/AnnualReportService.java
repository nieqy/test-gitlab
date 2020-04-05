package com.funshion.activity.annualreport.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.annualreport.entity.AnnualReportInfo;
import com.funshion.activity.annualreport.mapper.AnnualReportMapper;
import com.funshion.activity.common.constants.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by zhangfei on 2018/8/1/001.
 */
@Service
public class AnnualReportService {

    @Autowired
    private AnnualReportMapper annualReportMapper;

    public Result<?> annualReport(String mac) {
        AnnualReportInfo info = annualReportMapper.findInfo(mac);
        if (info == null) {
            return Result.getFailureResult("300", "数据不存在");
        }
        JSONObject json = JSON.parseObject(info.getAnnualInfo());
        int totalMediaNum = json.getIntValue("totalMediaNum");
        if (totalMediaNum < 10) {
            return Result.getFailureResult("301", "用户数据太少");
        }
        JSONObject watchTime = json.getJSONObject("watchTime");
        int movie = this.getHourTime(watchTime.getLongValue("movie"));
        int children = this.getHourTime(watchTime.getLongValue("children"));
        int sport = this.getHourTime(watchTime.getLongValue("sport"));
        int tv = this.getHourTime(watchTime.getLongValue("tv"));
        int life = this.getHourTime(watchTime.getLongValue("life"));
        int others = this.getHourTime(watchTime.getLongValue("others"));
        int total = movie + children + sport + tv + life + others;
        watchTime.put("movie", movie);
        watchTime.put("children", children);
        watchTime.put("sport", sport);
        watchTime.put("tv", tv);
        watchTime.put("life", life);
        watchTime.put("others", others + sport);
        watchTime.put("total", total);
        Double aboveRate = json.getDouble("aboveRate");
        json.put("aboveRate", this.getAboveRate(aboveRate));
		/*String annualKeyword = json.getString("annualKeyword");
		if ("陪伴".equals(annualKeyword)) {
			json.put("annualWord", "最温暖的事就是每天回家，有家人，有电视，陪伴是最长情的告白");
		}*/
        return Result.getSuccessResult(json);
    }

    private int getHourTime(long sec) {
        BigDecimal bd = new BigDecimal((double) sec / 3600);
        return bd.setScale(0, BigDecimal.ROUND_UP).intValue();
    }

    private double getAboveRate(Double aboveRate) {
        if (aboveRate == null) {
            return 0;
        }
        if (aboveRate >= 0.95) {
            return aboveRate - 0.05;
        } else if (aboveRate >= 0.8) {
            return aboveRate - 0.15;
        } else if (aboveRate >= 0.4) {
            return aboveRate - 0.25;
        } else if (aboveRate >= 0.15) {
            return aboveRate - 0.10;
        } else {
            return aboveRate;
        }
    }

}
