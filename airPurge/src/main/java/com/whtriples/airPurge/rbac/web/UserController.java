package com.whtriples.airPurge.rbac.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rps.util.D;
import com.whtriples.airPurge.cache.TokenCache;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.rbac.model.Role;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.rbac.model.UserRole;
import com.whtriples.airPurge.rbac.shiro.ShiroUser;
import com.whtriples.airPurge.redis.JedisTemplate;
import com.whtriples.airPurge.util.Digests;
import com.whtriples.airPurge.util.Encodes;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;
import com.whtriples.airPurge.util.Securities;
import com.whtriples.airPurge.util.UUIDs;

/**
 * 用户控制器类 方法上的注释为页面中Button的标题
 */
@Controller
@RequestMapping("/rbac/user")
public class UserController {
	
	@Autowired
	private  JedisTemplate jedisTemplate;
	
	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		model.addAttribute("userType", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "USER_TYPE"));
		model.addAttribute("AUTHORITY", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "AUTHORITY"));
		return "rbac/user";
	}

	@RequestMapping(value = "toUpdateJsp")
	public String toUpdateJsp() {
		return "login";
	}

	/*
	 * 显示-用户列表
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	public PageModel list(HttpServletRequest request) {
		return PageUtil.getPageModel(User.class, "sql.user/getPageUser", request);
	}

	/*
	 * 检查-用户信息是否合法
	 */
	@ResponseBody
	@RequestMapping(value = "check")
	public boolean check(HttpServletRequest request) {
		String user_name = request.getParameter("user_name");
		String login_id = request.getParameter("login_id");
		User user;
		if (!"".equals(user_name)) {
			user = D.sql("select * from T_P_USER where LOGIN_ID = ? and user_name != ?").oneOrNull(User.class,
					login_id, user_name);
		} else {
			user = D.sql("select * from T_P_USER where LOGIN_ID = ?").oneOrNull(User.class, login_id);
		}
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 添加用户
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(User user) {
		try {
			entryptPassword(user);
			user.setUser_type("1");
			user.setToken(UUIDs.getRandomUUID());
			D.insert(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 检测登录名是否存在
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkLoginId")
	public boolean checkLoginId(String loginId) {
		User user = D.sql("select * from T_P_USER where LOGIN_ID = ?").oneOrNull(User.class, loginId);
		return user == null;
	}

	/*
	 * 修改用户（之前）-查询用户信息
	 */
	@ResponseBody
	@RequestMapping(value = "{userId}")
	public User get(@PathVariable("userId") Long user_id) {
		User u = D.selectById(User.class, user_id);
		return u;
	}

	/*
	 * 修改用户
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public boolean update(User user) {
		boolean result = true;
		try {
			user.setToken(UUIDs.getRandomUUID());
			D.updateWithoutNull(user);
			User selectById = D.selectById(User.class, user.getUser_id());
			if("2".equals(selectById.getUser_type())){
				jedisTemplate.del(TokenCache.KEY_PROFIX + selectById.getUser_id());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/*
	 * 修改用户密码
	 * 
	 * @param user
	 * 
	 * @return zhangxiaomei 2013-10-18
	 */
	@ResponseBody
	@RequestMapping(value = "updatePwd")
	public boolean updatePwd(User user) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		User currentUser = D.selectById(User.class, shiroUser.getUser_id());

		byte[] hashPassword = Digests.sha1(user.getSalt().getBytes(), Encodes.decodeHex(currentUser.getSalt()),
				Securities.HASH_INTERATIONS);
		if (!StringUtils.equalsIgnoreCase(Encodes.encodeHex(hashPassword), currentUser.getPwd())) {
			return false;
		}

		try {
			user.setUser_id(currentUser.getUser_id());
			entryptPassword(user);
			D.updateWithoutNull(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "editPrivate")
	public boolean editPrivate(String device_authority,String user_id) {
		try {
			User user = new User();
			user.setUser_id(Long.parseLong(user_id));
			user.setDevice_authority(device_authority);
			D.updateWithoutNull(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * 绑定角色（之前）-查出所有角色并将该用户组已有角色默认选中
	 */
	@ResponseBody
	@RequestMapping(value = "getAllRole/{uesrId}")
	public List<Map<String, Object>> getAllRole(@PathVariable("uesrId") Long uesrId) {
		// 1、查询出所有角色
		List<Role> roleList = D.sql("select * from T_P_ROLE").many(Role.class);
		// 2、找出与用户角色关联的信息
		List<UserRole> keyList = D.sql("select * from T_P_USER_ROLE where user_id = ?").many(UserRole.class, uesrId);

		// 3、将与用户关联的角色编号保存到Set集合中
		Set<Integer> roleIdSet = Sets.newHashSet();
		for (UserRole key : keyList) {
			roleIdSet.add(key.getRole_id());
		}
		// 4、初始化treeList
		List<Map<String, Object>> treeList = Lists.newArrayList();

		// 5、将与用户角色关联的角色默认选中并添加到treeList中
		for (Role role : roleList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", role.getRole_id());
			map.put("text", role.getRole_nm());
			if (roleIdSet.contains(role.getRole_id())) {
				map.put("ischecked", true);
			}
			treeList.add(map);
		}
		return treeList;
	}

	/*
	 * 绑定角色
	 */
	@ResponseBody
	@RequestMapping(value = "insertUesrRole/{userId}")
	public boolean insertUesrGroupRole(@PathVariable("userId") final Long userId, final Integer[] boundInfos) {
		try {
			D.startTranSaction(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					// 1、删除已绑定的用户组信息
					D.sql("delete from T_P_USER_ROLE where user_id = ?").update(userId);
					// 2、添加用户组用户
					if (boundInfos != null) {
						for (Integer roleId : boundInfos) {
							UserRole tp = new UserRole();
							tp.setRole_id(roleId);
							tp.setUser_id(userId);
							D.insert(tp);
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

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(Securities.SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPwd().getBytes(), salt, Securities.HASH_INTERATIONS);
		user.setPwd(Encodes.encodeHex(hashPassword));
	}

}
