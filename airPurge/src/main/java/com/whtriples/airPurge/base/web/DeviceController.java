package com.whtriples.airPurge.base.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.DeviceUser;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.config.TimestampConfig;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.mobile.weather.Weather;
import com.whtriples.airPurge.rbac.model.Menue;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.util.ConfigUtil;
import com.whtriples.airPurge.util.UUIDs;

@Controller
@RequestMapping("/device/mang")
public class DeviceController {

	
	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "device/mang";
	}
	
	@RequestMapping(value = "printQR")
	public String printQR(Model model, HttpServletRequest request) {
		String device_guid = request.getParameter("device_guid");
		Device device = DeviceCache.getDeviceByGuid(device_guid);
		model.addAttribute("device_qr_url", device.getQRCode_url());
		model.addAttribute("device_name", device.getDevice_guid()+"("+device.getRemarks()+")");
		return "device/printQR";
	}
	
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(Device device) throws Exception {
		device.setLat(Double.parseDouble(ConfigUtil.getConfig("default.lat")));
		device.setLng(Double.parseDouble(ConfigUtil.getConfig("default.lng")));
		final String cityName = Weather.getCityName(device.getLat()+"," + device.getLng());
	    String city_id = D.sql("select city_id from t_d_city where city_zh like ?").one(String.class, cityName.split(",")[1].replace("市", "").trim());
	    device.setCity_id(city_id);
		if (device.getPar_device_id() == null) {
			device.setPar_device_id(0);
		}
		device.setCity_name(cityName);
		device.setRecord_time(TimestampConfig.datetampFormat.format(new Date()));
		return D.insert(device)>0;
	}
	
	@ResponseBody
	@RequestMapping(value = "getDeviceGuid")
	public String getDeviceGuid(Integer device_level,Integer par_device_id) {
		System.out.println("device_level: " + device_level + " par_device_id :" +par_device_id);
		if(device_level == 1){
			return UUIDs.getRandomUUID();
		}else if(device_level == 2){
			String max = D.sql("select  max(substring(device_guid,34)) from t_d_device where device_level = 2 and par_device_id=?").one(String.class,par_device_id);
			String device_guid = D.sql("select device_guid from t_d_device where device_id=?").one(String.class, par_device_id);
			String max_index = "";
			if(max == null){
				max_index = "1";
			}else{
				max_index = String.valueOf(Integer.parseInt(max) + 1);
			}
			return device_guid +"_" + max_index;
		}else{
			String max = D.sql("select  max(substring(device_guid,36)) from t_d_device where device_level = 3 and par_device_id=?").one(String.class,par_device_id);
			String device_guid = D.sql("select device_guid from t_d_device where device_id=?").one(String.class, par_device_id);
			String max_index = "";
			if(max == null){
				max_index = "1";
			}else{
				max_index = String.valueOf(Integer.parseInt(max) + 1);
			}
			return device_guid +"_" + max_index;
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "createByPare/{checkDeviceId}")
	public boolean createByPare(@PathVariable("checkDeviceId") Integer checkDeviceId, Device device) {
		try {
			Device parDevice = D.sql("select * from t_d_device where device_id = ?").one(Device.class, checkDeviceId);
			device.setCity_name(parDevice.getCity_name());
			device.setLat(parDevice.getLat());
			device.setLng(parDevice.getLng());
			device.setCity_id(parDevice.getCity_id());
			device.setPar_device_id(checkDeviceId);
			device.setRecord_time(TimestampConfig.datetampFormat.format(new Date()));
			if(device.getDevice_level().toString().equals("3")){
				device.setQRCode_url(createQR(device.getDevice_guid()));
			}
			D.insert(device);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value = "listAll")
	public void listAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Device> allDevice = D.sql("select * from t_d_device order by device_id ASC").many(Device.class);
		for (Device device : allDevice) {
			if (device.getPar_device_id() == 0) {
				allDevice.remove(device);
				break;
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allDevice), response.getWriter());
	}
	
	
	@ResponseBody
	@RequestMapping(value = "check")
	public boolean check(String menueNm) {
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateCheckMenueNm")
	public boolean updateCheckMenueNm(String menueNm) {
		List<Menue> list = D.sql("select * from T_P_MENUE where menue_nm = ?").many(Menue.class, menueNm);
		return list.size() == 1 ? true : false;
	}
	
	@ResponseBody
	@RequestMapping(value = "fredelcheck/{device_id}")
	public boolean fredelcheck(@PathVariable("device_id") Integer device_id) {
		Device device = D.selectById(Device.class, device_id);
		return device != null ? true : false;
	}

	@ResponseBody
	@RequestMapping(value = "checkChild/{device_id}")
	public boolean checkChild(@PathVariable("device_id") Integer device_id) {
		List<Device> list = D.sql("select * from t_d_device where par_device_id = ?").many(Device.class, device_id);
		return list.isEmpty();
	}

	
	@ResponseBody
	@RequestMapping(value = "deleteMenue/{device_id}")
	public boolean deleteMenue(@PathVariable("device_id") Integer device_id) {
		D.deleteById(Device.class, device_id);
		return true;
	}

	
	@ResponseBody
	@RequestMapping("/getDeviceById/{device_id}")
	public Device getFunIds(@PathVariable("device_id") Integer device_id) {
		return D.selectById(Device.class, device_id);
	}

	@ResponseBody
	@RequestMapping("update")
	public boolean updateMenue(Device device) {
		D.updateWithoutNull(device);
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value = "getAllUser/{device_id}")
	public List<Map<String, Object>> getAllRole(@PathVariable("device_id") Long device_id) {
		List<User> userList = D.sql("select * from t_p_user where user_type='1' ").many(User.class);
		List<DeviceUser> keyList = D.sql("select * from t_d_device_user where device_id = ?").many(DeviceUser.class, device_id);

		Set<Integer> roleIdSet = Sets.newHashSet();
		for (DeviceUser key : keyList) {
			roleIdSet.add(key.getUser_id());
			System.out.println(key.getUser_id() + " 绑定的用户ID");
		}
		List<Map<String, Object>> treeList = Lists.newArrayList();

		for (User user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getUser_id());
			map.put("text", user.getUser_name());
			if (roleIdSet.contains(user.getUser_id().intValue())) {
				map.put("ischecked", true);
			}
			treeList.add(map);
		}
		return treeList;
	}
	
	@ResponseBody
	@RequestMapping(value = "insertDeviceUser/{device_id}")
	public boolean insertDeviceUser(@PathVariable("device_id") final Integer device_id, final Integer[] boundInfos) {
		try {
			D.startTranSaction(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					D.sql("delete from t_d_device_user where device_id = ?").update(device_id);
					if (boundInfos != null) {
						for (Integer userId : boundInfos) {
							DeviceUser du = new DeviceUser();
							du.setDevice_id(device_id);
							du.setUser_id(userId);
							D.insert(du);
						}
					}
					return true;
				}
			});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//跳转至编辑商户地址页面
	@RequestMapping(value="toeditJsp")
	public String toeditJsp(){
		return "device/editAddress";
	}
	
	@ResponseBody
	@RequestMapping(value="updateAddress")
	public boolean updateAddress(final Device device){
		try {
			 final String cityName = Weather.getCityName(device.getLat()+"," + device.getLng());
			 final String city_id = D.sql("select city_id from t_d_city where city_zh like ?").one(String.class, cityName.split(",")[1].replace("市", "").trim());
			 device.setCity_name(cityName);
			 device.setCity_id(city_id.trim());
			 int result = D.updateWithoutNull(device);
			 new Thread(new Runnable(){
				@Override
				public void run() {
					List<Device> many = D.sql("select * from t_d_device where par_device_id =?").many(Device.class, device.getDevice_id());
					 for (Device device2:many) {
						device2.setCity_id(city_id);
						device2.setCity_name(cityName);
						device2.setLat(device.getLat());
						device2.setLng(device.getLng());
						D.updateWithoutNull(device2);
						 List<Device> many2 = D.sql("select * from t_d_device where par_device_id =?").many(Device.class, device2.getDevice_id());
						 for(Device device3:many2){
							 device3.setCity_id(city_id);
							 device3.setCity_name(cityName);
							 device3.setLat(device.getLat());
						     device3.setLng(device.getLng());
							 D.updateWithoutNull(device3);
						 }
					}
				}}).start();
			 
	         return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        
	}
	
	private static String createQR(String content) throws WriterException, IOException{
		QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bi = MatrixToImageWriter.toBufferedImage(matrix);
        String qrPath = ConfigUtil.getConfig("qr.upload.path");
        File qrFile = new File(qrPath ,content +".jpg");
        ImageIO.write(bi, "jpg", qrFile);
        return ConfigUtil.getConfig("qr.img.server") + File.separator + content + ".jpg";
	}
	
	public static void main(String[] args) throws WriterException, IOException {
		System.out.println(createQR("8af99fad6fb34768b06e8a00aa2e07aa_1_2"));
		System.out.println(System.getProperty("java.io.tmpdir"));
	}

	
}