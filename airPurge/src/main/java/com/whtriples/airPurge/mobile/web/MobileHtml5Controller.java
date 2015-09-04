package com.whtriples.airPurge.mobile.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.AppVersion;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.DeviceUser;
import com.whtriples.airPurge.base.model.Feedback;
import com.whtriples.airPurge.base.model.Transducer;
import com.whtriples.airPurge.cache.CacheMap;
import com.whtriples.airPurge.cache.CaptchaCache;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.cache.TokenCache;
import com.whtriples.airPurge.mobile.exception.AirPurgeError;
import com.whtriples.airPurge.mobile.vo.AboutOutVo;
import com.whtriples.airPurge.mobile.vo.BaseInVo;
import com.whtriples.airPurge.mobile.vo.BaseOutVo;
import com.whtriples.airPurge.mobile.vo.BindDeviceInVo;
import com.whtriples.airPurge.mobile.vo.CaptchaVo;
import com.whtriples.airPurge.mobile.vo.CompareDeviceDataInVo;
import com.whtriples.airPurge.mobile.vo.DeleteDetailInVo;
import com.whtriples.airPurge.mobile.vo.DeviceDetailInVo;
import com.whtriples.airPurge.mobile.vo.DeviceDetailOutVo;
import com.whtriples.airPurge.mobile.vo.FeedbackInVo;
import com.whtriples.airPurge.mobile.vo.LoginVo;
import com.whtriples.airPurge.mobile.vo.ModifyPwdInVo;
import com.whtriples.airPurge.mobile.vo.ModifyUserInfoInVo;
import com.whtriples.airPurge.mobile.vo.QueryDeviceInVo;
import com.whtriples.airPurge.mobile.vo.QueryDeviceOutVo;
import com.whtriples.airPurge.mobile.vo.QueryHistoryDataInVo;
import com.whtriples.airPurge.mobile.vo.QueryHistoryDataOutVo;
import com.whtriples.airPurge.mobile.vo.QueryUserInfoOutVo;
import com.whtriples.airPurge.mobile.vo.RegisterVo;
import com.whtriples.airPurge.mobile.vo.TokenOutVo;
import com.whtriples.airPurge.msg.SendSms;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.redis.JedisTemplate;
import com.whtriples.airPurge.util.ConfigUtil;
import com.whtriples.airPurge.util.FileUtil;
import com.whtriples.airPurge.util.JacksonUtils;
import com.whtriples.airPurge.util.Md5Util;
import com.whtriples.airPurge.util.UUIDs;

/**
 * 手机端数据服务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/html5")
public class MobileHtml5Controller {

	@Autowired
	private  JedisTemplate jedisTemplate;
	 
	private Logger logger = LoggerFactory.getLogger(MobileHtml5Controller.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String SEND_SMS = ConfigUtil.getConfig("sendSms");
	private static final Integer SEND_SMS_TIME = Integer.valueOf(ConfigUtil
			.getConfig("sendSms_time"));
	

	private static final ImmutableMap<String, String> smsTypeMap = ImmutableMap
			.of("01", "用户注册", "02", "用户登录", "03", "找回密码"); 
	
	/**
	 * 用户登录
	 * @param inVo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "login")
	public BaseOutVo login(LoginVo inVo) throws Exception {
		System.out.println("用户登录 " + inVo.getLogin_id() + " " + inVo.getPwd());
		TokenOutVo outVo = inVo.createOutVo(TokenOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}
		//每次用户登录修改token
		User user = D.sql("select * from t_p_user where USER_TYPE='2' and USER_STATUS='1' and LOGIN_ID=? and PWD=?")
				.one(User.class, inVo.getLogin_id(), Md5Util.md5Pwd(inVo.getPwd()));
		if (user == null || user.getUser_status().equals("0")) {
			outVo.setError(AirPurgeError.PWD_ERROR);
			return outVo;
		}
		user.setToken(UUIDs.getRandomUUID());
		D.updateWithoutNull(user);
		Map<String,String> userMap = Maps.newHashMap();
		userMap.put("token", user.getToken());
		userMap.put("user_id", user.getUser_id().toString());
		userMap.put("user_status", user.getUser_status());
		//将用户信息放入redis
		TokenCache.put(TokenCache.KEY_PROFIX + user.getUser_id(), userMap);
		outVo.setUser_id(user.getUser_id().toString());
		outVo.setToken(user.getToken());
		return outVo;
	}

	/**
	 * 用户注册
	 * @param inVo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "register")
	public BaseOutVo register(RegisterVo inVo) throws Exception {
		logger.info("用户注册 " + inVo.getLogin_id() + " " + inVo.getPwd());
		System.out.println("用户注册 " + inVo.getLogin_id() + " " + inVo.getPwd());
		TokenOutVo outVo = inVo.createOutVo(TokenOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}
		logger.warn("输入注册验证码：" + inVo.getCaptcha());
		if (!CaptchaCache.checkCaptcha(inVo.getLogin_id(), inVo.getCaptcha(),
				"01")) {
			 outVo.setError(AirPurgeError.CAPTCHA_ERROR);
			 return outVo;
		}

		Long existMobile = D.sql(
				"select count(0) from t_p_user where login_id = ?").one(
				Long.class, inVo.getLogin_id());
		if (existMobile > 0) {
			 outVo.setError(AirPurgeError.MOBILE_EXIST_ERROR);
			 return outVo;
		}
		
		java.util.List<User> users = D.sql("select * from t_p_user where USER_TYPE='2' and login_id=?").many(
				User.class, inVo.getLogin_id());
		if (users != null && users.size() > 0) {
			outVo.setError(AirPurgeError.USER_EXIST_ERROR);
			return outVo;
		}

		try {
			User user = new User();
			user.setLogin_id(inVo.getLogin_id());
			user.setPwd(Md5Util.md5Pwd(inVo.getPwd()));
			user.setUser_name(inVo.getLogin_id());
			user.setToken(UUIDs.getRandomUUID());
			user.setUser_type("2");
			user.setUser_status("1");
			user.setPhone(inVo.getLogin_id());
			Integer user_id = D.insertAndReturnInteger(user);
			outVo.setUser_id(user_id.toString());
			outVo.setToken(user.getToken());
			return outVo;
		} catch (Exception e) {
			outVo.setError(AirPurgeError.BUSINESS_ERROR);
			return outVo;
		}
	}

	/**
	 * 绑定设备
	 * @param inVo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "bindDevice")
	public BaseOutVo bindDevice(BindDeviceInVo inVo) throws Exception {
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		Device device = D.sql("select * from t_d_device where device_guid =? ")
				.one(Device.class, inVo.getDevice_guid());
		if (device == null) {
			outVo.setError(AirPurgeError.DEVICE_NOT_EXIST_ERROR);
			return outVo;
		}
		if (device.getStatus().equals("0")) {
			outVo.setError(AirPurgeError.DEVICE_STATUS_ERROR);
			return outVo;
		}
		List<DeviceUser> deviceUsers = D.sql("select * from t_d_device_user where device_id =? and user_id =?").many(
				DeviceUser.class, device.getDevice_id(), inVo.getUser_id());
		if (deviceUsers.size() > 0) {
			outVo.setError(AirPurgeError.DEVICE_ALERD_BIND_ERROR);
			return outVo;
		}
		DeviceUser deviceUser = new DeviceUser();
		deviceUser.setDevice_id(device.getDevice_id());
		deviceUser.setUser_id(Integer.parseInt(inVo.getUser_id()));
		deviceUser.setDevice_authority(0);
		try {
			D.insert(deviceUser);
		} catch (Exception e) {
			outVo.setError(AirPurgeError.BUSINESS_ERROR);
			return outVo;
		}
		return outVo;
	}

	/**
	 * 查询用户绑定设备
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryDevice")
	public QueryDeviceOutVo queryDevice(QueryDeviceInVo inVo) {
		QueryDeviceOutVo outVo = inVo.createOutVo(QueryDeviceOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		
		List<Device> devices =DeviceCache.getDeviceByUserId(inVo.getUser_id());
		for(Device device : devices){
			String data = CacheMap.cacheData.get(device.getDevice_guid());
			//device.setDevice_authority(user.getDevice_authority());
			device.setDeviceData(JSONObject.parse(data == null ? "": data));
			Transducer aqiByCityId = DeviceCache.getAqiByCityId(device.getCity_id());
			device.setAqi(aqiByCityId == null?"25":aqiByCityId.getPm25());
		}
		outVo.setDevices(devices);
		return outVo;
	}

	/**
	 * 设备详情
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deviceDetail")
	public DeviceDetailOutVo deviceDetail(DeviceDetailInVo inVo) {
		DeviceDetailOutVo outVo = inVo.createOutVo(DeviceDetailOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		Device device = D.sql("select * from t_d_device  where user_id=?").one(Device.class, inVo.getDevice_id());
		outVo.setDevice_guid(device.getDevice_guid());
		outVo.setRecord_time(device.getRecord_time());
		outVo.setRemarks(device.getRemarks());
		outVo.setDevice_id(device.getDevice_id());
		return outVo;
	}

	/**
	 * 反馈内容
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "feedback")
	public BaseOutVo feedback(FeedbackInVo inVo) {
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		Feedback feedback = new Feedback();
		feedback.setContent(inVo.getContent());
		feedback.setUser_id(Integer.parseInt(inVo.getUser_id()));
		feedback.setFeedback_time(new Date());
		D.insert(feedback);
		return outVo;
	}
	
	/**
	 * 关于
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "about")
	public BaseOutVo about(BaseInVo inVo) {
		AboutOutVo outVo = inVo.createOutVo(AboutOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}
		AppVersion one = D.sql("select * from t_d_app_version limit 1 ").one(AppVersion.class);
		outVo.setContent(one.getContent());
		return outVo;
	}
	
	/**
	 * 解除设备绑定
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "unbindDevice")
	public BaseOutVo unbindDevice(DeleteDetailInVo inVo) {
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		try {
			D.sql("delete from t_d_device_user where device_id=? and user_id=?").update(inVo.getDevice_id(),inVo.getUser_id());
		} catch (Exception e) {
			e.printStackTrace();
			outVo.setError(AirPurgeError.BUSINESS_ERROR);
		}
		return outVo;
	}
	
	/**
	 * 查询用户信息
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryUserInfo")
	public QueryUserInfoOutVo queryUserInfo(BaseInVo inVo) {
		QueryUserInfoOutVo outVo = inVo.createOutVo(QueryUserInfoOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		User user = D.selectById(User.class, inVo.getUser_id());
		outVo.setIcon_url(user.getIcon_url());
		outVo.setPhone(user.getPhone());
		outVo.setUser_name(user.getUser_name());
		outVo.setUser_id(user.getUser_id().toString());
		return outVo;
	}
	
	
	/**
	 * 历史数据查询
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryHistoryData")
	public BaseOutVo queryHistoryData(QueryHistoryDataInVo inVo) {
		QueryHistoryDataOutVo outVo = inVo.createOutVo(QueryHistoryDataOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}
		
		Device device = DeviceCache.getDeviceByGuid(inVo.getDevice_guid());
		if(device == null || "0".equals(device.getStatus())){
			outVo.setError(AirPurgeError.DEVICE_NOT_EXIST_ERROR);
			return outVo;
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> allLine = new ArrayList<Map<String, Object>>();//包含多条折线
		List<String> hourList = new ArrayList<String>();//x轴数据
		List<String> returnhourList = new ArrayList<String>();//x轴数据
		ArrayList<String> dataType = Lists.newArrayList("realData", "localData");
		DateTime start_time = DateTime.now();
		DateTime end_time= start_time.minusDays(1);
		while(end_time.isBefore(start_time)) {
			String dateStr = start_time.toString("yyyy-MM-dd HH:mm:ss");
			hourList.add(dateStr);
			start_time = start_time.minusHours(1);
		}
       
       
	   String sql1 = "select * from t_d_transducer where record_time >= ? and  record_time <= ? and device_guid = ? order by record_time limit 1";
	   String sql2 = "select * from t_d_transducer where record_time >= ? and  record_time <= ? and city_id = ? order by record_time limit 1";
	   for (String data_type : dataType) {
		   List<Double> data = new ArrayList<Double>();
		   Map<String, Object> line = new HashMap<String, Object>();
		   line.put("name",  data_type.equals("realData") == true ? device.getRemarks(): device.getCity_name()+"室外数据");
		   
		   String cs = "";
		   if(data_type.equals("realData")){
			    cs = jedisTemplate.get(inVo.getDevice_guid() + "_" + inVo.getType());
		   }else{
			    cs = jedisTemplate.get(device.getCity_id() + "_" + inVo.getType());
		   }
		   boolean flag = false;
		   if(!StringUtils.isEmpty(cs)){
			   flag = true;
			   logger.warn("从redis中获取数据");
			   data.addAll(JSONObject.parseArray(cs, Double.class));
		   }
		   
		   for (String string : hourList) {
			   Transducer transducerData = null;
			   Transducer localData = null;
			   if(!returnhourList.contains(string.substring(11, 13))){
				   returnhourList.add(string.substring(11, 13));
			   }
			   
			   if(flag){
				   continue;
			   }
			   if(data_type.equals("realData")){
				   transducerData = D.sql(sql1)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",device.getDevice_guid());
			   }else{
				   localData = D.sql(sql2)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",device.getCity_id());
			   }
			   switch(inVo.getType()){
			   
			   case "pm25" : 
				    
				    if(data_type.equals("realData")){
				    	 data.add(Double.parseDouble(transducerData==null ? "0":transducerData.getPm25()));
				    }else{
				    	data.add(Double.parseDouble(localData==null ? "0":localData.getPm25()));
				    }
			        break;
			        
			   case "hum" :
				   if(data_type.equals("realData")){
					   data.add(transducerData==null?0D:transducerData.getHum());
				   }else{
			    	      data.add(localData==null?0D:localData.getHum());
				   }
				   break;
				   
			   case "temp" :
				   if(data_type.equals("realData")){
					   data.add(transducerData==null?0D:transducerData.getTemp());
				   }else{
			    	      data.add(localData==null?0D:localData.getTemp());
				   }
				   break;
				   default:
			   }
		   }
		   
			   line.put("data",data);
			   if(data_type.equals("realData")){
			 	   jedisTemplate.setex(inVo.getDevice_guid()+"_"+inVo.getType(),JSONObject.toJSONString(data),3600);
			   }else{
			 	   jedisTemplate.setex(device.getCity_id() + "_"+inVo.getType(),JSONObject.toJSONString(data),3600);
			   }
			   allLine.add(line);
		   }
	   
	   
		resMap.put("xAxis", returnhourList);
		resMap.put("seriesData", allLine);
		resMap.put("device_name", device.getRemarks());
		outVo.setResultMap(resMap);
		return outVo;
	}
	

	/**
	 * 历史数据查询
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "compareDeviceData")
	public BaseOutVo compareDeviceData(CompareDeviceDataInVo inVo) {
		QueryHistoryDataOutVo outVo = inVo.createOutVo(QueryHistoryDataOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> allLine = new ArrayList<Map<String, Object>>();//包含多条折线
		List<String> hourList = new ArrayList<String>();//x轴数据
		List<String> returnhourList = new ArrayList<String>();//x轴数据
		String[] device_guids = inVo.getDevice_guid().split(",");
		
		DateTime start_time = DateTime.now();
		DateTime end_time= start_time.minusDays(1);
		while(end_time.isBefore(start_time)) {
			String dateStr = start_time.toString("yyyy-MM-dd HH:mm:ss");
			hourList.add(dateStr);
			start_time = start_time.minusHours(1);
		}
       
	   String sql1 = "select * from t_d_transducer where record_time >= ? and  record_time <= ? and device_guid = ? order by record_time limit 1";
	   for (int i=0; i<device_guids.length ;i++) {
		   List<Double> data = new ArrayList<Double>();
		   Map<String, Object> line = new HashMap<String, Object>();
			Device device = DeviceCache.getDeviceByGuid(device_guids[i]);
			if(device == null || "0".equals(device.getStatus())){
				outVo.setError(AirPurgeError.DEVICE_NOT_EXIST_ERROR);
				return outVo;
			}
		   line.put("id",  device_guids[i]);
		   line.put("name", device.getRemarks());
		   boolean flag = false;
		   String cs = jedisTemplate.get(device_guids[i] + "_" + inVo.getType());
		   if(!StringUtils.isEmpty(cs)){
			   flag = true;
			   logger.warn("从redis中获取数据");
			   data.addAll(JSONObject.parseArray(cs, Double.class));
		   }
		   for (String string : hourList) {
			   Transducer transducerData = null;
			   if(!returnhourList.contains(string.substring(11, 13))){
				   returnhourList.add(string.substring(11, 13));
			   }
			   
			   if(flag){
				   continue;
			   }
			   
			   transducerData = D.sql(sql1)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",device_guids[i]);
			  
			   switch(inVo.getType()){
			   case "pm25" : 
				    	 data.add(Double.parseDouble(transducerData==null ? "0":transducerData.getPm25()));
			        break;
			        
			   case "hum" :
					   data.add(transducerData==null?0D:transducerData.getHum());
				   break;
				   
			   case "temp" :
					   data.add(transducerData==null?0D:transducerData.getTemp());
				   break;
			   }
		   }
		   line.put("data", data);
		   //redis中没有数据 ，存入redis
		   if(!flag){
		 	   jedisTemplate.setex(device_guids[i]+"_"+inVo.getType(),JSONObject.toJSONString(data),3600);
		   }
		   allLine.add(line);
	   }
	   
		resMap.put("xAxis", returnhourList);
		resMap.put("seriesData", allLine);
		resMap.put("device_guids", inVo.getDevice_guid());
		outVo.setResultMap(resMap);
		return outVo;
	}
	
	/**
	 * 用户信息修改
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyUserInfo")
	public BaseOutVo modifyUserInfo(ModifyUserInfoInVo inVo) {
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		User user = new User();
		user.setIcon_url(inVo.getIcon_url());
		user.setPhone(inVo.getPhone());
		user.setUser_name(inVo.getUser_name());
		user.setUser_id(Long.valueOf(inVo.getUser_id()));
		try {
			D.updateWithoutNull(user);
		} catch (Exception e) {
			outVo.setError(AirPurgeError.BUSINESS_ERROR);
			return outVo;
		}
		return outVo;
	}

	/**
	 * 修改密码
	 * @param inVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyPwd")
	public BaseOutVo modifyPwd(ModifyPwdInVo inVo) {
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, true);
		if (outVo.hasError()) {
			return outVo;
		}
		User user  = D.sql("select * from t_p_user where LOGIN_ID = ? and USER_STATUS='1' and USER_TYPE = '2' ").one(User.class, inVo.getLogin_id());
		try {
			if(Md5Util.md5Pwd(inVo.getOld_pwd()).equals(user.getPwd())){
				D.sql("update t_p_user set pwd = ? where LOGIN_ID =? ").update(Md5Util.md5Pwd(inVo.getNew_pwd()), inVo.getLogin_id());
			}else{
				outVo.setError(AirPurgeError.PWD_ERROR);
				return outVo;
			}
		} catch (Exception e) {
			outVo.setError(AirPurgeError.BUSINESS_ERROR);
			e.printStackTrace();
			return outVo;
		}
		return outVo;
	}
	
    /**
     * 用户头像上传
     * @param servletRequest
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "uploadUserIcon")
	public String uploadUserIcon(HttpServletRequest servletRequest) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) servletRequest;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String uploadPath = ConfigUtil.getConfig("user.icon.path");
		HashMap<String, String> saveFile = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			try {
				 saveFile = FileUtil.saveFile(mf.getBytes(), mf.getOriginalFilename(),
						uploadPath);
				saveFile.get("saveFile");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ConfigUtil.getConfig("user.img.server") + saveFile.get("url");
	}
	
	/**
	 * 获取验证码
	 * @param inVo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getCaptcha")
	public BaseOutVo getCaptcha(CaptchaVo inVo)
			throws Exception {
		Long startTime = new Date().getTime();
		BaseOutVo outVo = inVo.createOutVo(BaseOutVo.class, false);
		if (outVo.hasError()) {
			return outVo;
		}

		String captcha_type = inVo.getCaptcha_type();
		String captcha_type_str = smsTypeMap.get(captcha_type);
		if(StringUtils.equals(captcha_type, "04") || captcha_type == "04"){
			captcha_type_str = "修改密码";
		}
		if (captcha_type_str == null) {
			outVo.setError(AirPurgeError.CAPTCHA_TYPE_ERROR);
			return outVo;
		}
		if(StringUtils.equals(captcha_type, "03") || captcha_type == "03"){
			
		}
		Long exist_count = null;
		if (StringUtils.startsWith(captcha_type, "0")) {
			exist_count = D.sql(
					"select count(0) from t_p_user where LOGIN_ID = ?")
					.one(Long.class, inVo.getLogin_id());
			
		}
		if (StringUtils.endsWith(captcha_type, "1")) {
			if (exist_count > 0) {
				outVo.setError(AirPurgeError.MOBILE_EXIST_ERROR);
				return outVo;
			}
		} else {
			if (exist_count == 0) {
				outVo.setError(AirPurgeError.MOBILE_EXIST_ERROR);
				return outVo;
			}
		}

		if (Integer.valueOf(SEND_SMS) == 1) {
			// 发送短信验证码
			String key = inVo.getLogin_id()+ "_" + captcha_type;
			String captcha = RandomStringUtils.randomNumeric(6);
			String value = captcha + sdf.format(new Date());
			// 设置对应的模板变量值
			String tpl_value = "#pName#=" + captcha_type_str + "&#code#="
					+ captcha;
			logger.warn("发送{}验证码:{}", captcha_type_str, captcha);
			System.out.println("发送验证码:" + captcha);
			return checkAndSendSms(inVo, outVo, key, value, tpl_value,
					startTime);
		} else {
			logger.warn("使用默认验证码:123456");
		}
		return outVo;
	}
	
	
	private BaseOutVo checkAndSendSms(CaptchaVo inVo, BaseOutVo outVo,
			String key, String value, String tpl_value, Long startTime)
			throws Exception, ParseException {
		String oldValue = CaptchaCache.get(key);
		if (oldValue == null || oldValue == "") {
			value = value + "1";
			sendSms(key, value, tpl_value, inVo);
			logger.warn("====getCaptcha end Time(ms):"
					+ (new Date().getTime() - startTime));
			outVo.setError(AirPurgeError.SUCCESS);
			return outVo;
		} else {
			String ymdHms = oldValue.substring(6, 20);
			Date lastTime = sdf.parse(ymdHms);
			if ((new Date().getTime() - lastTime.getTime()) <= 60 * 1000) {
				outVo.setError(AirPurgeError.ONE_MINUNTE_MORE_CAPTHCA);
				return outVo;
			}
			Integer timesNum = Integer.valueOf(oldValue.substring(20));
			if (timesNum < SEND_SMS_TIME) {
				timesNum = timesNum + 1;
				value = value + timesNum.toString();
				String resCode = sendSms(key, value, tpl_value, inVo);
				logger.warn("====getCaptcha end Time(ms):"
						+ (new Date().getTime() - startTime));
				if(resCode.equals("0") ){
					outVo.setError(AirPurgeError.SUCCESS);
					return outVo;
				}else{
					outVo.setError(AirPurgeError.CONNECT_SEND_SMS_SERVICE_FAIL);
					return outVo;
				}
			} else {
				 outVo.setError(AirPurgeError.ONE_DAY_PASS_TEN);
				 return outVo;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private String sendSms(String key, String value, String tpl_value,
			CaptchaVo inVo) throws Exception {
		String responseBody = SendSms
				.tplSendSms(tpl_value, inVo.getLogin_id());
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap = (HashMap<String, Object>) JacksonUtils.readValue(
				responseBody, Map.class);
		Integer resCode = (Integer) hashMap.get("code");
		if (resCode == 0) {
			CaptchaCache.set(key, value);
			logger.warn("发送验证码： " + value);
		}
		return resCode.toString();
	}

}