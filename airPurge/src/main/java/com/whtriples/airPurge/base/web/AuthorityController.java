package com.whtriples.airPurge.base.web;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rps.util.D;
import com.whtriples.airPurge.base.model.DeviceUser;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;

@Controller
@RequestMapping("/device/authority")
public class AuthorityController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("AUTHORITY", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "AUTHORITY"));
		return "device/authority";
	}
	
	@ResponseBody
	@RequestMapping(value = "list")
	public PageModel list(HttpServletRequest request) {
		PageModel pageModel = PageUtil.getPageModel(DeviceUser.class, "sql.device_user/getPageDeviceUser", request);
		return pageModel;
	}
	



@ResponseBody
@RequestMapping(value = "editPrivate")
public boolean editPrivate(String device_authority,String user_id,String device_id) {
	try {
		DeviceUser user = new DeviceUser();
		user.setUser_id(Integer.parseInt(user_id));
		user.setDevice_authority(Integer.parseInt(device_authority.equals("0")==true?"1":"0"));
		user.setDevice_id(Integer.parseInt(device_id));
		D.updateWithoutNull(user);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
}

}
