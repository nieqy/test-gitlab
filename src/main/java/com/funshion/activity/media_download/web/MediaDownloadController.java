package com.funshion.activity.media_download.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.funshion.activity.common.constants.Result;
import com.funshion.activity.media_download.service.MediaDownloadService;




@RestController
@RequestMapping("/api/media_info")
public class MediaDownloadController {
	
	private static final Logger logger = LoggerFactory.getLogger(MediaDownloadController.class);
	
	
	@Autowired
	private MediaDownloadService mediaDownloadService;
	
	
	/**
	 * 接口地址：http://172.17.3.98:8081/api/media_info/notify
	 * 访问方式：http POST
	 * 入       参: material_id：媒体ID,file_name:媒体名称，channel_name：频道，down_url:媒体下载地址,file_size:文件字节数
	 * 出       参： 成功 ：{"data": null,"retCode": "200","retMsg": "ok"}
	 *        失败： {"data": null,"retCode": "400","retMsg": "网络异常，请重试"}
	 *        失败： {"data": null,"retCode": "502","retMsg": "传入参数不全"}
	 * 
	 ***/
	
	//相当于一个信息更新的操作 首要把status置N 然后更新
	@RequestMapping(value="/notify" ,method = RequestMethod.POST)
	public Result<?> notify(MediaInfoRequest info,HttpServletRequest request){//入参MediaInfoRequest这个对象
		Result res = Result.getSuccessResult();//默认获取成功返回结果 放入res
		try {
			//判断媒体下载地址,文件字节数,媒体
			if(StringUtils.isEmpty(info.getDown_url()) //判断MediaInfoRequest这个实体类的getDown_url还有getFile_size和getMaterial_id
					|| StringUtils.isEmpty(info.getFile_size())//判断某字符串是否为空,此方法没有忽略空字符串,为空的标准是 str==null 或 str.length()==0 
					|| StringUtils.isEmpty(info.getMaterial_id())){
				res =  Result.HTTP_PARAMS_NOT_ENOUGH;//为空 结果集res为 502,传入参数不全 
			}else{
				info.setCreate_time(new Date());//不为空 传入当前时间到对象Create_time字段
				mediaDownloadService.saveMediaInfo(info); //然后进入service层的saveMediaInfo方法 入参:MediaInfoRequest这个对象
				//status = N
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(),e);//打印错误日志
			res = Result.SYSTEM_ERROR; //异常结果集res为  400,网络异常，请重试
		}
		logger.info(request.getRequestURL()+"?"+info+"}"+"  {"+"retCode:"+res.getRetCode()+" retMsg:"+res.getRetMsg()+"}");
		return res;//返回结果集
	}

	
	/**
	 * 接口地址：http://172.17.3.98:8081/api/media_info/tags
	 * 访问方式：http POST
	 * 入       参: material_id：媒体ID,tags:媒体标签
	 * 出       参： 成功 ：{"data": null,"retCode": "200","retMsg": "ok"}
	 *        失败： {"data": null,"retCode": "400","retMsg": "网络异常，请重试"}
	 *        失败： {"data": null,"retCode": "502","retMsg": "传入参数不全"}
	 * 
	 ***/
	
	
	@RequestMapping(value="/tags" ,method = RequestMethod.POST)
	public Result<?> tags(String material_id, String tags ,HttpServletRequest request){
		Result res = Result.getSuccessResult();//默认获取成功返回结果 放入res
		try {
			//判断入参: material_id：媒体ID,tags:媒体标签 是不是空
			if(StringUtils.isEmpty(material_id) || StringUtils.isEmpty(tags)){
				res =  Result.HTTP_PARAMS_NOT_ENOUGH; //为空返回参数不全
			}else{
				mediaDownloadService.tagMediaInfo(material_id,tags); //没问题就传入service层,直接更新 
				//status = 'T'
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(),e); //异常就打印
			res = Result.SYSTEM_ERROR; //result状态修改 
		}
		logger.info("url:{},material_id:{},tags:{},retCode:{} ",request.getRequestURL(),material_id,tags,res.getRetCode());
		return res;
	}
	
	
	/**
	 * 接口地址：http://172.17.3.98:8081/api/media_info/dirtywords
	 * 访问方式：http POST
	 * 入       参: material_id：媒体ID,data:脏话标签
	 * 出       参： 成功 ：{"data": null,"retCode": "200","retMsg": "ok"}
	 *        失败： {"data": null,"retCode": "400","retMsg": "网络异常，请重试"}
	 *        失败： {"data": null,"retCode": "502","retMsg": "传入参数不全"}
	 * 
	 ***/
	@RequestMapping(value="/dirtywords" ,method = RequestMethod.POST)
	public Result<?> dirtywords(String material_id, String data ,HttpServletRequest request){
		Result res = Result.getSuccessResult();
		try {
			//判空
			if(StringUtils.isEmpty(material_id) || StringUtils.isEmpty(data)){
				res =  Result.HTTP_PARAMS_NOT_ENOUGH;
			}else{
				mediaDownloadService.dirtywordsMediaInfo(material_id,data);
				//v_status = 'T'
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(),e);
			res = Result.SYSTEM_ERROR;
		}
		logger.info("url:{},material_id:{},dirtywords:{},retCode:{} ",request.getRequestURL(),material_id,data,res.getRetCode());
		return res;
	}
	
	
	/**
	 * 接口地址：http://172.17.3.98:8081/api/media_info/logo
	 * 访问方式：http POST
	 * 入       参: material_id：媒体ID,data:logo标签
	 * 出       参： 成功 ：{"data": null,"retCode": "200","retMsg": "ok"}
	 *        失败： {"data": null,"retCode": "400","retMsg": "网络异常，请重试"}
	 *        失败： {"data": null,"retCode": "502","retMsg": "传入参数不全"}
	 * 
	 ***/
	@RequestMapping(value="/logo" ,method = RequestMethod.POST)
	public Result<?> logo(String material_id, String data ,HttpServletRequest request){
		Result res = Result.getSuccessResult();
		try {
			if(StringUtils.isEmpty(material_id) || StringUtils.isEmpty(data)){
				res =  Result.HTTP_PARAMS_NOT_ENOUGH;
			}else{
				mediaDownloadService.logoMediaInfo(material_id,data);
				//l_status = 'T'
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage(),e);
			res = Result.SYSTEM_ERROR;
		}
		logger.info("url:{},material_id:{},logo:{},retCode:{} ",request.getRequestURL(),material_id,data,res.getRetCode());
		return res;
	}
	
	/**
	 *  接口地址：http://172.17.3.98:8081/api/media_info/imageBlocking
	 *  访问方式：http POST 
	 *  入       参 :  material_id：素材ID, message:图片遮标标签
	 *  出       参： 成功 ：{"data": null,"retCode": "200","retMsg": "ok"}
	 *        失败： {"data": null,"retCode": "400","retMsg": "网络异常，请重试"}
	 *        失败： {"data": null,"retCode": "502","retMsg": "传入参数不全"}
	 */
	@RequestMapping(value = "/imageBlocking", method = RequestMethod.POST)
	public Result<?> imageBlocking(String material_id, String data, HttpServletRequest request) {
		Result res = Result.getSuccessResult();// 默认返回成功的结果集
		try {
			if (StringUtils.isEmpty(material_id) || StringUtils.isEmpty(data)) {
				res = Result.HTTP_PARAMS_NOT_ENOUGH; // 返回传入参数不全的结果集
				//System.out.println(material_id);
				//System.out.println(data);
			} else {
				mediaDownloadService.imageBlockingMediaInfo(material_id, data);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
			res = Result.SYSTEM_ERROR;
		}
		logger.info("url:{},material_id:{},imageblocaking:{},retCode:{} ",request.getRequestURL(),material_id,data,res.getRetCode());
		return res;
	}

}
