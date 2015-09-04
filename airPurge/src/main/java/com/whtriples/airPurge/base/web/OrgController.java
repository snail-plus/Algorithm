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
import org.apache.shiro.SecurityUtils;
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
import com.whtriples.airPurge.base.model.Org;
import com.whtriples.airPurge.base.model.OrgDevice;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.config.TimestampConfig;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.rbac.shiro.ShiroUser;
import com.whtriples.airPurge.util.ConfigUtil;
import com.whtriples.airPurge.util.UUIDs;

@Controller
@RequestMapping("/device/org")
public class OrgController {

	
	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "device/org";
	}
	
	/**
	 * 创建跟节点
	 * @param org
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(Org org) {
		Integer loginUserId = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getUser_id().intValue();
		if (org.getPar_id() == null) {
			org.setPar_id(0);
		}
		org.setOrg_level(1);
		org.setOrg_code(UUIDs.getRandomUUID());
		org.setCreate_time(TimestampConfig.datetampFormat.format(new Date()));
		org.setCreate_by(loginUserId);
		return D.insert(org)>0;
	}
	
	/**
	 * 获取机构编码
	 * @param a_org_level
	 * @param a_par_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOrg_code")
	public String getDeviceGuid(Integer a_org_level,Integer a_par_id) {
		if(a_org_level == 1){
			return UUIDs.getRandomUUID();
		}else if(a_org_level == 2){
			String max = D.sql("select  max(substring(org_code,34)) from t_d_org where org_level = 2 and par_id=?").one(String.class,a_par_id);
			String org_code = D.sql("select org_code from t_d_org where org_id=?").one(String.class, a_par_id);
			String max_index = "";
			if(max == null){
				max_index = "1";
			}else{
				max_index = String.valueOf(Integer.parseInt(max) + 1);
			}
			return org_code +"_" + max_index;
		}else{
			String max = D.sql("select  max(substring(org_code,36)) from t_d_org where org_level = 3 and par_id=?").one(String.class,a_par_id);
			String org_code = D.sql("select org_code from t_d_org where org_id=?").one(String.class, a_par_id);
			String max_index = "";
			if(max == null){
				max_index = "1";
			}else{
				max_index = String.valueOf(Integer.parseInt(max) + 1);
			}
			return org_code +"_" + max_index;
		}
	}
	
	/**
	 * 创建机构（根据父节点）
	 * @param checkDeviceId
	 * @param org
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createByPare/{checkDeviceId}")
	public boolean createByPare(@PathVariable("checkDeviceId") Integer checkDeviceId, Org org) {
		try {
			org.setPar_id(checkDeviceId);
            org.setCreate_time(TimestampConfig.datetampFormat.format(new Date()));
			D.insert(org);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 机构列表
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "listAll")
	public void listAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Org> allOrg = D.sql("select * from t_d_org order by org_id ASC").many(Org.class);
		for (Org org : allOrg) {
			if (org.getPar_id() == 0) {
				allOrg.remove(org);
				break;
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allOrg), response.getWriter());
	}
	
	
	@ResponseBody
	@RequestMapping(value = "check")
	public boolean check(String menueNm) {
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value = "fredelcheck/{org_id}")
	public boolean fredelcheck(@PathVariable("org_id") Integer org_id) {
		Org org = D.selectById(Org.class, org_id);
		return org != null ? true : false;
	}

	@ResponseBody
	@RequestMapping(value = "checkChild/{device_id}")
	public boolean checkChild(@PathVariable("device_id") Integer device_id) {
		List<Device> list = D.sql("select * from t_d_device where par_device_id = ?").many(Device.class, device_id);
		return list.isEmpty();
	}

	/**
	 * 删除机构
	 * @param org_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteMenue/{org_id}")
	public boolean deleteMenue(@PathVariable("org_id") Integer org_id) {
		D.deleteById(Org.class, org_id);
		return true;
	}

	/**
	 * 查询机构
	 * @param org_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDeviceById/{org_id}")
	public Org getDeviceById(@PathVariable("org_id") Integer org_id) {
		return D.selectById(Org.class, org_id);
	}
/**
 * 机构更新
 * @param org
 * @return
 */
	@ResponseBody
	@RequestMapping("update")
	public boolean updateOrg(Org org) {
		org.setUpdate_time(new Date());
		D.updateWithoutNull(org);
		return true;
	}
	
	/**
	 * 获取设备
	 * @param device_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAllDevice/{org_id}")
	public List<Map<String, Object>> getAllDevice(@PathVariable("org_id") Long org_id) {
		List<Device> userList = D.sql("select * from t_d_device where status='1' ").many(Device.class);
		List<OrgDevice> keyList = D.sql("select * from t_d_org_device where org_id = ?").many(OrgDevice.class, org_id);

		Set<Integer> roleIdSet = Sets.newHashSet();
		for (OrgDevice key : keyList) {
			roleIdSet.add(key.getDevice_id());
		}
		List<Map<String, Object>> treeList = Lists.newArrayList();

		for (Device user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getDevice_id());
			map.put("text", user.getRemarks());
			if (roleIdSet.contains(user.getDevice_id().intValue())) {
				map.put("ischecked", true);
			}
			treeList.add(map);
		}
		return treeList;
	}
	
	/**
	 * 机构设备绑定
	 * @param org_id
	 * @param boundInfos
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "insertDeviceOrg/{org_id}")
	public boolean insertDeviceOrg(@PathVariable("org_id") final Integer org_id, final Integer[] boundInfos) {
		try {
			D.startTranSaction(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					D.sql("delete from t_d_org_device where org_id = ?").update(org_id);
					if (boundInfos != null) {
						for (Integer userId : boundInfos) {
							OrgDevice du = new OrgDevice();
							du.setOrg_id(org_id);
							du.setDevice_id(userId);
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