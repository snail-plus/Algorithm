package com.whtriples.airPurge.rbac.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rps.util.D;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.rbac.model.Menue;


/**
 * MenueController 菜单管理模块相关功能操作实现 说明： zhangxiaomei 2013-8-25
 */
@Controller
@RequestMapping("/rbac/menue")
public class MenueController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "rbac/menue";
	}

	/*
	 * 菜单管理：新增 未选中节点进行新增
	 * 
	 * @param menue
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(Menue menue) {
		if (menue.getPar_menue_id() == null) {
			menue.setPar_menue_id(1);
		}
		Integer maxOrder= D.sql("SELECT MAX(t_p_menue.MENUE_ORDER) FROM t_p_menue WHERE t_p_menue.PAR_MENUE_ID=1").one(Integer.class);
		//设置添加的排序
		menue.setMenue_order((short) (maxOrder+1));
		return D.insert(menue)>0;
	}

	/*
	 * 菜单管理：新增，根据选中节点Id添加新节点
	 * 
	 * @param menueId
	 * 
	 * @param menue
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "createByPare/{checkMenueId}")
	public boolean createByPare(@PathVariable("checkMenueId") Integer checkMenueId, Menue menue) {
		try {
			menue.setPar_menue_id(checkMenueId);
			Integer maxOrder= D.sql("SELECT MAX(t_p_menue.MENUE_ORDER) FROM t_p_menue WHERE t_p_menue.PAR_MENUE_ID=?").one(Integer.class,checkMenueId);
			//设置添加的排序
			menue.setMenue_order((short) (maxOrder+1));
			D.insert(menue);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * 菜单管理：查询所有记录信息列表
	 * 
	 * @param request
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@RequestMapping(value = "listAll")
	public void listAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Menue> allMenue = D.sql("select * from T_P_MENUE order by MENUE_ORDER ASC").many(Menue.class);
		for (Menue funOpt : allMenue) {
			if (funOpt.getPar_menue_id() == 0) {
				allMenue.remove(funOpt);
				break;
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allMenue), response.getWriter());
	}

	/*
	 * 菜单管理：新增时验证菜单名称是否已经存在
	 * 
	 * @param menueNm
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "check")
	public boolean check(String menueNm) {
		List<Menue> list = D.sql("select * from T_P_MENUE where menue_nm = ?").many(Menue.class, menueNm);
		return list.isEmpty();
	}

	/*
	 * 菜单管理：修改时验证菜单名称是否存在
	 * 
	 * @param menueNm
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "updateCheckMenueNm")
	public boolean updateCheckMenueNm(String menueNm) {
		List<Menue> list = D.sql("select * from T_P_MENUE where menue_nm = ?").many(Menue.class, menueNm);
		return list.size() == 1 ? true : false;
	}

	/*
	 * 菜单管理：删除之前验证记录是否存在
	 * 
	 * @param menueId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "fredelcheck/{menueId}")
	public boolean fredelcheck(@PathVariable("menueId") Integer menueId) {
		Menue menue = D.selectById(Menue.class, menueId);
		return menue != null ? true : false;
	}

	/*
	 * 菜单管理：删除节点验证是否有子节点
	 * 
	 * @param menueId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "checkChild/{menueId}")
	public boolean checkChild(@PathVariable("menueId") Integer menueId) {
		List<Menue> list = D.sql("select * from T_P_MENUE where par_menue_id = ?").many(Menue.class, menueId);
		return list.isEmpty();
	}

	/*
	 * 菜单管理：删除选中的节点
	 * 
	 * @param menueId
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "deleteMenue/{menueId}")
	public boolean deleteMenue(@PathVariable("menueId") Integer menueId) {
		D.deleteById(Menue.class, menueId);
		return true;
	}

	/*
	 * 菜单管理：获取待更新记录信息
	 * 
	 * @param menueId
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@ResponseBody
	@RequestMapping("/getMenueById/{menueId}")
	public Menue getFunIds(@PathVariable("menueId") Integer menueId) {
		return D.selectById(Menue.class, menueId);
	}

	/*
	 * 菜单管理：更新
	 * 
	 * @param menue
	 * 
	 * @return zhangxiaomei 2013-8-25
	 */
	@ResponseBody
	@RequestMapping("update")
	public boolean updateMenue(Menue menue) {
		D.update(menue);
		return true;
	}

	/**
	 * 得到菜单下的排序号最小的序号
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMinIndex/{parentId}")
	public Map<String, Object> getMInIndex(@PathVariable(value = "parentId") Integer parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer minIndex = D.sql("select MIN(MENUE_ORDER) from t_p_menue WHERE t_p_menue.PAR_MENUE_ID=?").one(
				Integer.class, parentId);
		map.put("minIndex", minIndex);
		return map;
	}

	/**
	 * 得到菜单下的排序号最大的序号
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMaxIndex/{parentId}")
	public Map<String, Object> getMaxIndex(@PathVariable(value = "parentId") Integer parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer maxIndex = D.sql("select MAX(MENUE_ORDER) from t_p_menue WHERE t_p_menue.PAR_MENUE_ID=?").one(
				Integer.class, parentId);
		map.put("maxIndex", maxIndex);
		return map;
	}

	/**
	 * 菜单上移
	 * 
	 * @param infoType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/up/{menueId}/{parentId}")
	public void up(@PathVariable(value = "menueId") Integer menueId, @PathVariable(value = "parentId") Integer parentId) {
		List<Menue> list = D.sql(
				"select * from t_p_menue WHERE t_p_menue.PAR_MENUE_ID=? ORDER BY t_p_menue.MENUE_ORDER").many(
				Menue.class, parentId);
		for (int i = 0; i < list.size(); i++) {
			Short temp;
			if (list.get(i).getMenue_id() == menueId || menueId.equals(list.get(i).getMenue_id())) {
				temp = list.get(i).getMenue_order();
				list.get(i).setMenue_order(list.get(i - 1).getMenue_order());
				list.get(i - 1).setMenue_order(temp);
				// 交换位置后更新
				D.updateWithoutNull(list.get(i));
				D.updateWithoutNull(list.get(i - 1));

			}
		}
	}

	/**
	 * 菜单下移
	 * 
	 * @param infoType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/down/{menueId}/{parentId}")
	public void down(@PathVariable(value = "menueId") Integer menueId,
			@PathVariable(value = "parentId") Integer parentId) {
		List<Menue> list = D.sql(
				"select * from t_p_menue WHERE t_p_menue.PAR_MENUE_ID=? ORDER BY t_p_menue.MENUE_ORDER").many(
				Menue.class, parentId);
		for (int i = 0; i < list.size(); i++) {
			Short temp;
			if (list.get(i).getMenue_id() == menueId || menueId.equals(list.get(i).getMenue_id())) {
				temp = list.get(i).getMenue_order();
				list.get(i).setMenue_order(list.get(i + 1).getMenue_order());
				list.get(i + 1).setMenue_order(temp);
				// 交换位置后更新
				D.updateWithoutNull(list.get(i));
				D.updateWithoutNull(list.get(i + 1));

			}
		}
	}

}
