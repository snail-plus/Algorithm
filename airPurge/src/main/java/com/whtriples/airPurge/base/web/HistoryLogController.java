package com.whtriples.airPurge.base.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.Transducer;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;


@Controller
@RequestMapping("device/history")
public class HistoryLogController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "device/history";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getDevice")
	public List<ComboxModel> getDevice() {
		return D.sql("select device_id as id,remarks as text from t_d_device where device_level=3").many(ComboxModel.class);
	}
	
	@ResponseBody
	@RequestMapping(value = "dataType")
	public List<ComboxModel> dataType() {
		return D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "DATA_TYPE");
	}
	
	@ResponseBody
	@RequestMapping(value = "list")
	public PageModel list(HttpServletRequest request) {
		PageModel pageModel = PageUtil.getPageModel(Transducer.class, "sql.history/getPageHistory", request);
		return pageModel;
	}
	
	@ResponseBody
	@RequestMapping(value = "listTree")
	public List<ComboxModel> listTree() {
		List<ComboxModel> comboxs = Lists.newArrayList();
		List<ComboxModel> comboxs1 = D.sql("select device_guid as id,remarks as text from t_d_device where device_level=1").many(ComboxModel.class);
		List<ComboxModel> comboxs2 = D.sql("select device_guid as id,remarks as text,left(device_guid,32) as pid from t_d_device where device_level=3").many(ComboxModel.class);;
		comboxs.addAll(comboxs1);
		comboxs.addAll(comboxs2);
		return comboxs;
	}
	
	/**
	 * 室内外数据对比 
	 * @param request
	 * @param device_id
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "consmeRes")
	public Map<String,Object> consmeRes(String startTime,String device_id,String type) {
		Device device = D.sql("select * from t_d_device where device_id=?").one(Device.class, device_id);
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> allLine = new ArrayList<Map<String, Object>>();//包含多条折线
		List<String> hourList = new ArrayList<String>();//x轴数据
		List<String> returnhourList = new ArrayList<String>();//x轴数据
		ArrayList<String> dataType = Lists.newArrayList("realData", "localData");
		DateTime start_time = null;
		DateTime now = DateTime.now();
		if(now.toString().substring(0, 10).equals(startTime)){
			 start_time = DateTime.parse(startTime+"T" + now.toString().substring(11));
		}else{
			 start_time = DateTime.parse(startTime+"T" + "23:59:59");
		}
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
		   line.put("name",  data_type.equals("realData") == true ? "设备数据": device.getCity_name() +"室外数据");
		   for (String string : hourList) {
			   Transducer transducerData = null;
			   Transducer localData = null;
			   if(!returnhourList.contains(string.substring(11, 13))){
				   returnhourList.add(string.substring(11, 13));
			   }

			   if(data_type.equals("realData")){
				   transducerData = D.sql(sql1)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",device.getDevice_guid());
			   }else{
				   localData = D.sql(sql2)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",device.getCity_id());
			   }
			  
			   switch(type){
			   case "0" : 
				    
				    if(data_type.equals("realData")){
				    	 data.add(Double.parseDouble(transducerData==null ? "0":transducerData.getPm25()));
				    }else{
				    	data.add(Double.parseDouble(localData==null ? "0":localData.getPm25()));
				    }
			        break;
			        
			   case "1" :
				   if(data_type.equals("realData")){
					   data.add(transducerData==null?0D:transducerData.getHum());
				   }else{
			    	      data.add(localData==null?0D:localData.getHum());
				   }
				   break;
				   
			   case "2" :
				   if(data_type.equals("realData")){
					   data.add(transducerData==null?0D:transducerData.getTemp());
				   }else{
			    	      data.add(localData==null?0D:localData.getTemp());
				   }
				   break;
			   }
		   }
		   Collections.reverse(data);
		   line.put("data", data);
		   allLine.add(line);
	   }
	   
	    Collections.reverse(returnhourList);
		resMap.put("xAxis", returnhourList);
		resMap.put("seriesData", allLine);
		
		return resMap;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "compareDeviceData")
	public Map<String,Object> compareDeviceData(String startTime,String device_guids,String type) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> allLine = new ArrayList<Map<String, Object>>();//包含多条折线
		List<String> hourList = new ArrayList<String>();//x轴数据
		List<String> returnhourList = new ArrayList<String>();//x轴数据
		String[] _device_guids = device_guids.split(",");
		DateTime now = DateTime.now();
		String nowClock = now.toString("yyyy-MM-dd HH:mm:ss").substring(11);
		DateTime start_time = null;
		if(now.toString().substring(0, 10).equals(startTime)){
			 start_time = DateTime.parse(startTime+"T" + nowClock);
		}else{
			 start_time = DateTime.parse(startTime+"T" + "23:59:59");
		}
		DateTime end_time= start_time.minusDays(1);
		while(end_time.isBefore(start_time)) {
			String dateStr = start_time.toString("yyyy-MM-dd HH:mm:ss");
			hourList.add(dateStr);
			start_time = start_time.minusHours(1);
		}
       
	   String sql1 = "select * from t_d_transducer where record_time >= ? and  record_time <= ? and device_guid = ? order by record_time limit 1";
	   for (int i=0; i<_device_guids.length ;i++) {
		   List<Double> data = new ArrayList<Double>();
		   Map<String, Object> line = new HashMap<String, Object>();
		   Device device = D.sql("select * from t_d_device where device_guid=?").one(Device.class, _device_guids[i]);
		   if(device == null){
			   break;
		   }
		   line.put("id",  _device_guids[i]);
		   line.put("name", device.getRemarks());
		   for (String string : hourList) {
			   Transducer transducerData = null;
			   if(!returnhourList.contains(string.substring(11, 13))){
				   returnhourList.add(string.substring(11, 13));
			   }
			   
			   transducerData = D.sql(sql1)
						   .one(Transducer.class, string.substring(0, 13)+":00:00" ,string.substring(0, 13)+":59:59",_device_guids[i]);
			  
			   switch(type){
			   case "0" : 
				    	 data.add(Double.parseDouble(transducerData==null ? "0":transducerData.getPm25()));
			        break;
			        
			   case "1" :
					   data.add(transducerData==null?0D:transducerData.getHum());
				   break;
				   
			   case "2" :
					   data.add(transducerData==null?0D:transducerData.getTemp());
				   break;
			   }
		   }
		   Collections.reverse(data);
		   line.put("data",data);
		   allLine.add(line);
	   }
	    Collections.reverse(returnhourList);
		resMap.put("xAxis", returnhourList);
		resMap.put("seriesData", allLine);
		return resMap;
	}
	
}
