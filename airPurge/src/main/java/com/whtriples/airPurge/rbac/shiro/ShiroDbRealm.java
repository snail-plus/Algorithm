package com.whtriples.airPurge.rbac.shiro;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rps.util.D;
import com.whtriples.airPurge.exception.NoRoleException;
import com.whtriples.airPurge.mapper.JsonMapper;
import com.whtriples.airPurge.rbac.model.Menue;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.util.Encodes;


public class ShiroDbRealm extends AuthorizingRealm {

	private static final String HASH_ALGORITHM = "SHA-1";

	private static final int HASH_INTERATIONS = 1024;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = getUserByLoginId(token.getUsername());
		if (user != null) {
			ShiroUser shiroUser = new ShiroUser(user.getUser_id(), user.getLogin_id(), user.getUser_name());
			Integer role_id = D.sql("select role_id from t_p_user_role where user_id=?").one(Integer.class, shiroUser.getUser_id());
			if(role_id == null){
				throw new NoRoleException("请联系管理员为此用户指定角色");//未给该用户指定角色抛出
			}else{
					shiroUser.setMenuData(JsonMapper.nonEmptyMapper().toJson(getMenuList(user.getUser_id())));
					byte[] salt = Encodes.decodeHex(user.getSalt());
					return new SimpleAuthenticationInfo(shiroUser, user.getPwd(), ByteSource.Util.bytes(salt), getName());
//				}
			}
		} else {
			throw new AccountException("Null usernames are not allowed by this realm.");
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		List<String> roleNameList = D.sqlAt("sql.role/getRoleName").many(String.class, shiroUser.getUser_id());
		List<String> funOptUrlList = D.sqlAt("sql.role/getOptUrl").many(String.class, shiroUser.getUser_id());
		info.setRoles(Sets.newLinkedHashSet(roleNameList));
		info.setStringPermissions(Sets.newLinkedHashSet(funOptUrlList));
		return info;
	}

	//清除缓存的用户信息，需要时调用（如用户资源 权限改变时）
	@Override
	protected  void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		 SimplePrincipalCollection principal = new SimplePrincipalCollection(
				 principals, getName());
	     clearCachedAuthenticationInfo(principal);
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
		matcher.setHashIterations(HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	private User getUserByLoginId(String loginId) {
		return D.sql("select * from  T_P_USER where login_id = ? AND user_status=1 ").oneOrNull(User.class, loginId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List getMenuList(Long userId) {
		List list = Lists.newArrayList();
		// 查询根节点
		List<Menue> rootMenuList = D.sql("select * from T_P_MENUE where par_menue_id = 1  and state='1' order by MENUE_ORDER").many(
				Menue.class);
//		Map<Integer, String> rootMenuMap = Maps.newLinkedHashMap();
		// 查询该用户可访问的菜单
		Map result;
		try {
			List<Map> menuMap = D.sqlAt("sql.role/getMenueByUserId").many(Map.class, userId);
			for (int i = 0; i < rootMenuList.size(); i++) {
				result = Maps.newLinkedHashMap();
				List childrenList = Lists.newArrayList();
				Map childrenMap;
				for (Map map : menuMap) {
					childrenMap = Maps.newLinkedHashMap();
						if (map.get("PAR_MENUE_ID").equals(rootMenuList.get(i).getMenue_id())) {
							childrenMap.put("id", map.get("MENUE_ID"));
							childrenMap.put("url", map.get("URL"));
							childrenMap.put("text", map.get("MENUE_NM"));
							childrenList.add(childrenMap);
							result.put("text", rootMenuList.get(i).getMenue_nm());
							result.put("isexpand", false);
							result.put("children", childrenList);
						}
				}
				// 如果没有子菜单不显示根目录
				if(result.containsKey("children")==true){					
					list.add(result);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
