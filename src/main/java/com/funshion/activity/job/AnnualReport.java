/*
package com.funshion.activity.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.annualreport.entity.AnnualReportInfo;
import com.funshion.activity.annualreport.mapper.AnnualReportMapper;
import com.funshion.activity.common.utils.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnualReport {

	private Logger logger = LoggerFactory.getLogger(AnnualReport.class);

	private static Map<String, String> favText = new HashMap<>();

	private static Map<String, String> picUrl = new HashMap<>();

	private static Map<String, String[]> keyWorlds = new HashMap<>();

	@Autowired
	private AnnualReportMapper annualReportMapper;

	@Scheduled(initialDelay = 2000, fixedRate = 24 * 3600 * 1000)
	public void doExecute() {
		File dir = new File("C:\\Users\\zhangfei\\Desktop\\annual\\annual");
		File[] files = dir.listFiles();
		List<AnnualReportInfo> list = new ArrayList<>();
		for (File file : files) {
			logger.info("begin to import: "+ file.getName());
			List<String> infos = ReadFileTest.readFile2(file);
			for (String info : infos) {
				JSONObject reports = JSON.parseObject(info);
				String mac = reports.getString("mac");
				reports.put("favMovieText",this.getFavText(reports.getString("favMovieCate")));
				reports.put("favTvText",this.getFavText(reports.getString("favTvCate")));
				JSONObject favMovie = reports.getJSONObject("favMovie").getJSONObject("media");
				if(favMovie!=null){
					favMovie.put("imgId", ImgUtils.getImgPath(favMovie.getString("imgId")));
				}
				JSONObject oldestMovie = reports.getJSONObject("oldestMovie").getJSONObject("media");
				if(oldestMovie!=null){
					oldestMovie.put("imgId", ImgUtils.getImgPath(oldestMovie.getString("imgId")));
				}
				JSONObject favTv = reports.getJSONObject("favTv").getJSONObject("media");
				if(favTv!=null){
					favTv.put("imgId", ImgUtils.getImgPath(favTv.getString("imgId")));
				}
				JSONObject annualWord = reports.getJSONObject("annualWord");
				String[] annualWords;
				if(annualWord!=null){
					String mediaIdxName = annualWord.getString("mediaIdxName");
					String videoIdxName = annualWord.getString("videoIdxName");
					if(StringUtils.isBlank(mediaIdxName) || StringUtils.isBlank(videoIdxName)){
						annualWords = this.getAnnaulWorld(null);
					}else{
						annualWords = this.getAnnaulWorld("("+mediaIdxName+","+videoIdxName+")");
					}
				}else{
					annualWords = this.getAnnaulWorld(null);
				}
				reports.put("annualWord",annualWords[1]);
				reports.put("annualKeyword",annualWords[0].trim());
				reports.put("annualPicture",picUrl.get(annualWords[0].trim()));
				AnnualReportInfo ar = new AnnualReportInfo();
				ar.setMac(mac);
				ar.setAnnualInfo(JSON.toJSONString(reports));
				list.add(ar);
			}
			annualReportMapper.add(list);
			list.clear();
			logger.info("end to import: "+ file.getName());
		}
	}

	private String getFavText(String favCat){
		if(StringUtils.isBlank(favCat)){
			return "人生如戏，戏如人生，做好自己最重要";
		}
		if(favText.containsKey(favCat)){
			return favText.get(favCat);
		}
		return "人生如戏，戏如人生，做好自己最重要";
	}

	private  String[] getAnnaulWorld(String key){
		if(StringUtils.isBlank(key)){
			return new String[]{"陪伴","最温暖的事就是每天回家，有家人，有电视，陪伴是最长情的告白"};
		}
		if(keyWorlds.containsKey(key)){
			return keyWorlds.get(key);
		}
		return new String[]{"陪伴","最温暖的事就是每天回家，有家人，有电视，陪伴是最长情的告白"};
	}

	static{
		favText.put("动作","拯救世界是专业动作，请勿轻易模仿");
		favText.put("喜剧","喜剧的最高境界是笑着笑着就让人流泪了");
		favText.put("剧情","看戏中爱恨情仇，品戏外五彩人生");
		favText.put("动画","保持童心，享受这一份简单的快乐");
		favText.put("爱情","曾经有一份真挚的爱情摆在我面前…");
		favText.put("犯罪","惊叹于各种脑洞大开的犯罪奇才");
		favText.put("原创","原创是一种创造力,也是一种精神");
		favText.put("奇幻","喜欢天马行空的想象力,光怪陆离的魔法世界");
		favText.put("冒险","通过电影我也可以探索世界");
		favText.put("恐怖","浑身是胆说的就是我了");
		favText.put("惊悚","浑身是胆说的就是我了");
		favText.put("战争","感受战争的残酷，珍惜和平的时光");
		favText.put("科幻","星际穿越、粒子炮、基因改造…才是我们的口味");
		favText.put("历史","以史为镜，可以知兴替");
		favText.put("传记","向牛人学习，向大咖致敬");
		favText.put("梦幻","可能从梦幻中醒来的部分，不是在脑海里，而是在心上");
		favText.put("悬疑","现实是最精彩的电影，人心是最大的悬疑");
		favText.put("文艺","趁时光未老,带着些许青春的味道,感受这些文艺的画面吧");
		favText.put("古装","也想体验一把穿越，过一把甄嬛的瘾");
		favText.put("武侠","究竟降龙十八掌和九阴真经哪个厉害呢");
		favText.put("音乐","不知是哪一首歌触动了你的内心");
		favText.put("运动","躺在沙发看电影算不算运动…");
		favText.put("纪录","面对未知的世界充满了好奇心");
		favText.put("儿童","陪伴孩子成长是我们最幸福的事情");
		favText.put("抗战","致敬!抗战老兵的峥嵘岁月");
		favText.put("魔幻","喜欢天马行空的想象力,光怪陆离的魔法世界");
		favText.put("伦理","生活不只是眼前的苟且，生活还有诗和远方");
		favText.put("灾难","心里住着一个英雄，希望拯救世界");
		favText.put("西部","多么想在荒野纵马奔腾，像佐罗一样帅气拔枪");
		favText.put("年代","怀念属于那个年代专属的情感色调");
		favText.put("偶像","偶像的力量是无穷的，为爱豆打call");
		favText.put("农村","喜爱那份淳朴和真实，乡村也有爱情");
		favText.put("励志","要争取做自己生活的英雄");
		favText.put("谍战","惊险刺激、悬念迭起，感受革命工作者的生死较量");
		favText.put("经典","周星驰的喜剧，周润发的枪战，向经典致敬");
		favText.put("商战","商场如战场，无所不用其极");
		favText.put("军旅","向往热血军魂，向军人致敬");
		favText.put("家庭","幸福的家庭都是相似的，不幸的家庭各有各的不幸");
		favText.put("歌舞","好心情就是要唱出来跳出来");
		favText.put("都市","都市亦是森林，斗争依然残酷");
		favText.put("网络大电影","人生如戏，戏如人生，做好自己最重要");
		favText.put("剧场版","人生如戏，戏如人生，做好自己最重要");
		favText.put("儿童乐园","人生如戏，戏如人生，做好自己最重要");
		favText.put("搞笑","人生如戏，戏如人生，做好自己最重要");
		favText.put("治愈","人生如戏，戏如人生，做好自己最重要");
		favText.put("情感","人生如戏，戏如人生，做好自己最重要");
		favText.put("TVB","人生如戏，戏如人生，做好自己最重要");
		favText.put("宫廷","人生如戏，戏如人生，做好自己最重要");
		favText.put("亲情","人生如戏，戏如人生，做好自己最重要");
		favText.put("警匪","人生如戏，戏如人生，做好自己最重要");
		favText.put("热血","人生如戏，戏如人生，做好自己最重要");
		favText.put("青春","人生如戏，戏如人生，做好自己最重要");
		favText.put("网络剧","人生如戏，戏如人生，做好自己最重要");
		List<String> keyworldInfos = ReadFileTest.readFile2(new File("C:\\Users\\zhangfei\\Desktop\\annual\\a.txt"));
		for (String keyworldInfo : keyworldInfos) {
			String[] infos = keyworldInfo.split("\t");
			String[] keys = new String[]{infos[1],infos[2]};
			keyWorlds.put(infos[0],keys);
		}

		List<String> picUrlInfos = ReadFileTest.readFile2(new File("C:\\Users\\zhangfei\\Desktop\\annual\\pic_url.txt"));
		for (String pic : picUrlInfos) {
			String[] infos = pic.split(",");
			picUrl.put(infos[0].trim(),infos[1].trim());
		}
	}

}
*/
