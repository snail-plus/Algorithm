package com.whtriples.airPurge.base.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Log;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;


@Controller
@RequestMapping("device/log")
public class OperationLogController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "device/log";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "list")
	public PageModel list(HttpServletRequest request) {
		PageModel pageModel = PageUtil.getPageModel(Log.class, "sql.log/getPageLog", request);
		List<Log> logs = (List<Log>) pageModel.getData();
		for (Log log : logs) {
			JSONObject parseObject = JSONObject.parseObject(log.getOperation());
			JSONArray parseArray = JSONObject.parseArray(parseObject.get("data").toString());
			Object data1 = parseArray.get(1);
			Object data2 = parseArray.get(2);
			switch (data1.toString()) {
			case "switch":
				if(data2.toString().equals("0")){
					log.setOperation("关机");
				}else{
					log.setOperation("开机");
				}
				break;
			case "ctrlmode":
				if(data2.toString().equals("1")){
					log.setOperation("调整手动模式");
				}else{
					log.setOperation("调整自动模式");
				}
				break;
			case "gear":
				if(data2.toString().equals("0")){
					log.setOperation("调整档位0");
				}else if(data2.toString().equals("1")){
					log.setOperation("调整档位1");
			   }else{
				   log.setOperation("调整档位2");
			   }
				break;
			default:
				break;
			}
		}
		return pageModel;
	}
	
}
