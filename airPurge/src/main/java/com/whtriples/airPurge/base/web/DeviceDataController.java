package com.whtriples.airPurge.base.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.TreeNode;
import com.whtriples.airPurge.cache.CacheMap;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.rbac.shiro.ShiroUser;
import com.whtriples.airPurge.util.Collections3;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;

@Controller
@RequestMapping("/device/data")
public class DeviceDataController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		model.addAttribute("orgData", JsonMapper.nonEmptyMapper().toJson(getOrgList()));
		return "device/data";
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "list")
	public PageModel list(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("org_id"));
		PageModel pageModel = PageUtil.getPageModel(Device.class, "sql.data/getPageData", request);
		List<Device> devices = (List<Device>) pageModel.getData();
		JSONObject parseObject = null;
		for(Device device : devices){
			 parseObject = JSONObject.parseObject((CacheMap.cacheData.get(device.getDevice_guid())));
			 if(parseObject != null){
				
				 device.setHum(parseObject.get("hum").toString());
				 device.setGear(parseObject.get("gear").toString());
				 device.setTemp(parseObject.get("temp").toString());
				 device.setPm10(parseObject.get("pm10").toString());
				 device.setPm25(parseObject.get("pm25").toString());
				 device.setRun_state(parseObject.get("run_state").toString().equals("1")==true?"运行":"关闭");
			 }else{
				 device.setHum("暂无数据");
				 device.setGear("暂无数据");
				 device.setTemp("暂无数据");
				 device.setPm10("暂无数据");
				 device.setPm25("暂无数据");
				 device.setRun_state("暂无数据");
			 }
		}
		
		return pageModel;
	}
	
	
	@RequestMapping(value="toeContrJsp")
	public String toeContrJsp(HttpServletRequest request,Model model){
		Integer loginUserId = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getUser_id().intValue();
		String device_guid = request.getParameter("device_guid");
		User selectById = D.selectById(User.class, loginUserId);
		model.addAttribute("device_guid", device_guid);
		model.addAttribute("user_id", loginUserId);
		model.addAttribute("token", selectById.getToken());
		System.out.println(loginUserId);
		return "device/toContr";
	}
	
	@SuppressWarnings("unchecked")
	public static List<TreeNode> getOrgList(){
	   List<TreeNode> infoTypeList =D.sql(
			   "select org_id as 'id', org_name as 'text', par_id as 'parent_code' ,org_level as 'tree_level' "
						+ "from t_d_org where org_status = '1'"
						+ " order by org_id asc").many(TreeNode.class);
	   
	   List<TreeNode> rootList=Lists.newArrayList();
	   Map<String,TreeNode> map = Collections3.extractToMap(infoTypeList, "id", null);
	   for(TreeNode infoType:infoTypeList){
		   String parent_id=infoType.getParent_code();
		   if("1".equals(infoType.getTree_level())){
			   rootList.add(infoType);
		   }else{
			   	TreeNode parentInfoType = map.get(parent_id);
				if(parentInfoType == null) {
					continue;
				}
				if(parentInfoType.getChildren()==null){
					parentInfoType.setChildren(new ArrayList<TreeNode>());
				}
				parentInfoType.getChildren().add(infoType);
		   }
	   }
	   
	   rootList.get(0).setExpanded(true);
	   rootList.get(0).getChildren().get(0).setExpanded(true);
	   return rootList;
	}
	
}
