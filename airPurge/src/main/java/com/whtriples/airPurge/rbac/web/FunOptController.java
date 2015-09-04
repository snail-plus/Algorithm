package com.whtriples.airPurge.rbac.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rps.util.D;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.rbac.model.FunOpt;
import com.whtriples.airPurge.rbac.model.Menue;
import com.whtriples.airPurge.rbac.model.RoleFunOpt;


/**
 * FunOptController 说明：资源管理模块中相关功能实现 zhangxiaomei 2013-8-23
 */
@Controller
@RequestMapping(value = "/rbac/funopt")
public class FunOptController {
	/*
	 * 左边树形菜单通过此方法跳转至页面
	 * 
	 * @return zhangxiaomei 2013-8-23
	 */
	@RequestMapping()
	public String init() {
		return "rbac/funOpt";
	}

	/*
	 * 资源管理：页面加载时列表信息查询
	 * 
	 * @param request
	 * 
	 * @return zhangxiaomei 2013-8-23
	 */
	@RequestMapping(value = "listAll")
	public void listAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<FunOpt> allFunOpt = D.sql("Select * from T_P_FUN_OPT").many(FunOpt.class);
		List<FunOpt> rootFunOpt = Lists.newArrayList();
		Map<Long, FunOpt> allFunOptMap = Maps.newHashMap();

		for (FunOpt funOpt : allFunOpt) {
			allFunOptMap.put(funOpt.getFun_opt_id(), funOpt);
		}
		for (FunOpt funOpt : allFunOpt) {
			Long parentId = funOpt.getParent_fun_opt_id();
			if (parentId != null) {
				FunOpt parent = allFunOptMap.get(parentId);
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<FunOpt>());
					}
					parent.getChildren().add(funOpt);
				}
			} else {
				rootFunOpt.add(funOpt);
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allFunOpt), response.getWriter());
		// IOUtils.write(JsonMapper.nonNullMapper()
		// .toJson(new PageModel(allFunOpt.size(), rootFunOpt)),
		// response.getWriter());
	}

	/**
	 * 资源管理：添加只显示未被绑定的资源
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllNotBind/{parent_fun_opt_id}")
	public void getAllNotBind(HttpServletRequest request, HttpServletResponse response,@PathVariable(value="parent_fun_opt_id") Long parent_fun_opt_id) throws IOException {
		String sql="";
		if(parent_fun_opt_id!=0){
			 sql="SELECT * FROM t_p_fun_opt fo WHERE NOT EXISTS ( SELECT 1 FROM t_p_menue m WHERE fo.fun_opt_id = m.fun_opt_id) and fo.PARENT_FUN_OPT_ID="+parent_fun_opt_id;
		}else{
			 sql="SELECT * FROM t_p_fun_opt fo WHERE NOT EXISTS ( SELECT 1 FROM t_p_menue m WHERE fo.fun_opt_id = m.fun_opt_id )";
		}
		List<FunOpt> allFunOpt = D.sql(sql).many(FunOpt.class);
		List<FunOpt> rootFunOpt = Lists.newArrayList();
		Map<Long, FunOpt> allFunOptMap = Maps.newHashMap();

		for (FunOpt funOpt : allFunOpt) {
			allFunOptMap.put(funOpt.getFun_opt_id(), funOpt);
		}
		for (FunOpt funOpt : allFunOpt) {
			Long parentId = funOpt.getParent_fun_opt_id();
			if (parentId != null) {
				FunOpt parent = allFunOptMap.get(parentId);
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<FunOpt>());
					}
					parent.getChildren().add(funOpt);
				}
			} else {
				rootFunOpt.add(funOpt);
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allFunOpt), response.getWriter());
		// IOUtils.write(JsonMapper.nonNullMapper()
		// .toJson(new PageModel(allFunOpt.size(), rootFunOpt)),
		// response.getWriter());
	}
	
	/**
	 * 资源管理：编辑只显示当前已经绑定和未被绑定的资源
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/editOptTreeSelect/{funOptId}")
	public void editOptTreeSelect(@PathVariable(value="funOptId") Long funOptId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取选中的菜单项
		FunOpt opt=D.selectById(FunOpt.class, funOptId);
		List<FunOpt> notBindFunOpt=null;
		//未被绑定的父菜单以及子菜单
		if(null==opt.getParent_fun_opt_id()){
		   notBindFunOpt=D.sql("select * from t_p_fun_opt fo where not exists ( select 1 from t_p_menue m where fo.fun_opt_id = m.fun_opt_id ) ").many(FunOpt.class);
		}else{
		   notBindFunOpt=D.sql("select * from t_p_fun_opt fo where not exists ( select 1 from t_p_menue m where fo.fun_opt_id = m.fun_opt_id ) and fo.parent_fun_opt_id=?").many(FunOpt.class,opt.getParent_fun_opt_id());
		}
		
		List<FunOpt> allFunOpt = Lists.newArrayList();
		//如果编辑父菜单
		if(null==opt.getParent_fun_opt_id()){
			String sql="select * from t_p_fun_opt fo where not exists ( select 1 from t_p_menue m where fo.fun_opt_id = m.fun_opt_id ) or fo.fun_opt_id=? or fo.parent_fun_opt_id=?";
			//未被绑定的父菜单
			allFunOpt = D.sql(sql).many(FunOpt.class,opt.getFun_opt_id(),opt.getFun_opt_id());
		}else{
			//编辑子菜单
			//当前选中的子菜单以及父菜单当前选中的子菜单和父菜单
			allFunOpt=	D.sql("SELECT * FROM t_p_fun_opt fo WHERE fo.fun_opt_id = ? OR fo.fun_opt_id = ? ").many(FunOpt.class,opt.getFun_opt_id(),opt.getParent_fun_opt_id());
			
			//未被绑定的子菜单以及父菜单
			allFunOpt.addAll(notBindFunOpt);
		}
		List<FunOpt> rootFunOpt = Lists.newArrayList();
		Map<Long, FunOpt> allFunOptMap = Maps.newHashMap();

		for (FunOpt funOpt : allFunOpt) {
			allFunOptMap.put(funOpt.getFun_opt_id(), funOpt);
		}
		for (FunOpt funOpt : allFunOpt) {
			Long parentId = funOpt.getParent_fun_opt_id();
			if (parentId != null) {
				FunOpt parent = allFunOptMap.get(parentId);
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<FunOpt>());
					}
					parent.getChildren().add(funOpt);
				}
			} else {
				rootFunOpt.add(funOpt);
			}
		}
		IOUtils.write(JsonMapper.nonNullMapper().toJson(allFunOpt), response.getWriter());
	}
	
	
	
	/*
	 * 资源管理：新增时验证录入的功能操作名称是否已存在
	 * 
	 * @param funOptNm
	 * 
	 * @return zhangxiaomei 2013-8-23
	 */
	@ResponseBody
	@RequestMapping(value = "check")
	public boolean check(String funOptNm) {
		List<FunOpt> foList = D.sql("select * from T_P_FUN_OPT where fun_opt_nm = ?").many(FunOpt.class, funOptNm);
		return foList.isEmpty();
	}

	/*
	 * 资源管理：新增时验证URL是否已存在
	 * 
	 * @param url
	 * 
	 * @return zhangxiaomei 2013-8-23
	 */
	@ResponseBody
	@RequestMapping(value = "checkUrl")
	public boolean checkUrl(String url) {
		// String regEx = "^[A-Za-z]+$";
		// Pattern pat = Pattern.compile(regEx);
		// Matcher mat = pat.matcher(url);
		// boolean rs = mat.find();
		// if(rs){
		List<FunOpt> foList = D.sql("select * from T_P_FUN_OPT where url = ?").many(FunOpt.class, url);
		return foList.isEmpty();
		// }
		// return false;
	}

	/*
	 * 资源管理：根据选择的父节点创建子节点
	 * 
	 * @param foId
	 * 
	 * @param fo
	 * 
	 * @return zhangxiaomei 2013-8-23
	 */
	@ResponseBody
	@RequestMapping(value = "createByParam/{parFunOptId}")
	public boolean createByParam(FunOpt funOpt, @PathVariable("parFunOptId") String parFunOptId) {
		funOpt.setParent_fun_opt_id(Long.valueOf(parFunOptId));
		return D.insert(funOpt) > 0;
	}

	/*
	 * 资源管理：没有选择节点，直接新建节点
	 * 
	 * @param fopt
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(FunOpt funOpt) {
		try {
			D.insert(funOpt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * 资源管理：获取更新待对象信息
	 * 
	 * @param funOptId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "getFunopt/{funOptId}")
	public FunOpt getFunopt(@PathVariable("funOptId") Long funOptId) {
		return D.selectById(FunOpt.class, funOptId);
	}

	/*
	 * 资源管理：更新保存
	 * 
	 * @param funopt
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public boolean update(FunOpt funopt) {
		D.updateWithoutNull(funopt);
		return true;
	}

	/*
	 * 资源管理：删除之前验证所选记录是否存在
	 * 
	 * @param funOptId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "delFreCheck/{funOptId}")
	public boolean delFreCheck(@PathVariable("funOptId") Long funOptId) {
		FunOpt funopt = D.selectById(FunOpt.class, funOptId);
		return funopt == null;
	}

	/*
	 * 资源管理：删除之前验证所选记录是否有子节点
	 * 
	 * @param funOptId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "checkChild/{funOptId}")
	public boolean checkChild(@PathVariable("funOptId") Long funOptId) {
		List<FunOpt> list = D.sql("select * from T_P_FUN_OPT where parent_fun_opt_id = ?").many(FunOpt.class, funOptId);
		return list.isEmpty();
	}

	/*
	 * 资源管理：删除
	 * 
	 * @param funOptId
	 * 
	 * @return zhangxiaomei 2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "delete/{funOptId}")
	public boolean delete(@PathVariable("funOptId") Long funOptId) {
		D.deleteById(FunOpt.class, funOptId);
		return true;
	}

	/*
	 * 资源管理：查询对应模块功能操作
	 * 
	 * @return zhangxiaomei 2013-8-26
	 */
	@ResponseBody
	@RequestMapping("selFunopt")
	public List<Map<String, Object>> selFunopt() {
		List<Map<String, Object>> treeList = Lists.newArrayList();// 鍒濆鍖栨爲
		List<FunOpt> list = D.sql("select * from T_P_FUN_OPT where parent_fun_opt_id is not null ").many(FunOpt.class);
		for (FunOpt fo : list) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", fo.getFun_opt_id());
			map.put("text", fo.getFun_opt_nm());
			treeList.add(map);
		}
		return treeList;
	}

	/**
	 * 删除前验证是否绑定角色
	 * 
	 * @param funOptId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkBindRole/{funOptId}")
	public boolean checkBindRole(@PathVariable("funOptId") Long funOptId) {
		return D.sql("SELECT * FROM t_p_role_fun_opt WHERE t_p_role_fun_opt.FUN_OPT_ID=?").oneOrNull(RoleFunOpt.class,
				funOptId) == null;
	}

	/**
	 * 删除前验证是否绑定菜单
	 * 
	 * @param funOptId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkBindMenu/{funOptId}")
	public boolean checkBindMenu(@PathVariable("funOptId") Long funOptId) {
		return D.sql("SELECT * FROM t_p_menue WHERE t_p_menue.FUN_OPT_ID=?").oneOrNull(Menue.class, funOptId) == null;
	}
}
