package com.whtriples.airPurge.rbac.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rps.util.D;
import com.whtriples.airPurge.rbac.model.FunOpt;
import com.whtriples.airPurge.rbac.model.Role;
import com.whtriples.airPurge.rbac.model.RoleFunOpt;
import com.whtriples.airPurge.rbac.model.User;
import com.whtriples.airPurge.rbac.model.UserRole;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;

@Controller
@RequestMapping(value = "/rbac/role")
public class RoleController {

    /*
     * 左边树形菜单通过此方法跳转至页面
     *
     * @return zhangxiaomei 2013-8-25
     */
    @RequestMapping()
    public String init() {
        return "rbac/role";
    }

    /*
     * 角色管理：查询所有记录列表
     *
     * @param request
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "listAll")
    public PageModel listAll(HttpServletRequest request) {
        return PageUtil.getPageModel(Role.class, "sql.role/getPageRole", request);
    }

    /*
     * 角色管理：获取待更新记录信息
     *
     * @param roleId
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "{roleId}")
    public Role get(@PathVariable("roleId") String roleId) {
        return D.selectById(Role.class, roleId);
    }

    /*
     *
     * @param roleNm
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "checkRoleNm")
    public boolean checkRoleNm(HttpServletRequest request) {
        String roleNm = request.getParameter("role_nm");
        String roleId = request.getParameter("role_id");
        User user;
        if (!StringUtils.isEmpty(roleId)) {
            user = D.sql("select * from T_P_ROLE where role_id = ? and role_name != ?").oneOrNull(User.class, roleId,
                    roleNm);
        } else {
            user = D.sql("select * from T_P_ROLE where role_nm = ?").oneOrNull(User.class, roleNm);
        }
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 角色管理：新建角色
     *
     * @param role
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "create")
    public boolean create(Role role) {
        try {
            D.insert(role);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * 角色管理：更新所选记录信息
     *
     * @param role
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "update")
    public boolean update(Role role) {
        try {
            D.updateWithoutNull(role);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * 角色管理：查询所有资源
     *
     * @param roleId
     *
     * @return zhangxiaomei 2013-8-25
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "allOpt/{roleId}")
    public List<Map<String, Object>> allOpt(@PathVariable("roleId") Integer roleId) {
        List<RoleFunOpt> funRoleList = D.sql("select * from T_P_ROLE_FUN_OPT   where role_id = ?").many(
                RoleFunOpt.class, roleId);

        Set<Long> idList = Sets.newHashSet();// 已绑定资源Id
        for (RoleFunOpt key : funRoleList) {
            idList.add(key.getFun_opt_id());
        }

        List<FunOpt> list = D.sql("select *  from T_P_FUN_OPT ").many(FunOpt.class);
        Map<Long, Map<String, Object>> allFunOptMap = Maps.newHashMap();
        List<Map<String, Object>> rootFunOptList = Lists.newArrayList();

        for (FunOpt opt : list) {
            Map<String, Object> node = Maps.newHashMapWithExpectedSize(3);
            Long funOptId = opt.getFun_opt_id();
            node.put("id", funOptId);
            node.put("pid", opt.getParent_fun_opt_id());
            node.put("text", opt.getFun_opt_nm());
            if (idList.contains(funOptId)) {
                node.put("checked", true);
            }
            allFunOptMap.put(funOptId, node);
        }

        for (Map<String, Object> node : allFunOptMap.values()) {
            Long parentId = (Long) node.get("pid");
            if (parentId != null) {
                Map<String, Object> parent = allFunOptMap.get(parentId);
                if (parent != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    if (children == null) {
                        children = Lists.newArrayList();
                        parent.put("children", children);
                    }
                    children.add(node);
                }
            } else {
                rootFunOptList.add(node);
            }
        }
        return rootFunOptList;
    }

    /*
     * 角色管理：保存绑定资源 绑定资源前先删除已绑定资源
     *
     * @param roleId
     *
     * @param detailId
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "customer_bund/{roleId}/{checkedId}")
    public boolean customer_bund(@PathVariable("roleId") final String roleId,
                                 @PathVariable("checkedId") final Long[] detailId) {
        try {
            if (!StringUtils.isEmpty(roleId) && detailId.length > 0) {
                D.startTranSaction(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        D.sql("delete from T_P_ROLE_FUN_OPT where role_id = ?").update(roleId);
                        for (Long id : detailId) {
                            RoleFunOpt tp = new RoleFunOpt();
                            tp.setRole_id(Long.valueOf(roleId));
                            tp.setFun_opt_id(id);
                            D.insert(tp);
                        }
                        return true;
                    }
                });
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * 角色管理：删除角色之前验证所选角色是否存在
     *
     * @param roleId
     *
     * @return zhangxiaomei 2013-8-24
     */
    @ResponseBody
    @RequestMapping(value = "delCheckExits/{roleId}")
    public boolean delCheckExits(@PathVariable("roleId") Integer roleId) {
        Role role = D.selectById(Role.class, roleId);
        return role == null;
    }

    /*
     * 角色管理：验证是否有用户与此角色绑定
     *
     * @param roleId
     *
     * @return zhangxiaomei 2013-8-24
     */
    @ResponseBody
    @RequestMapping(value = "checkUserBind/{roleId}")
    public boolean roleCheckExits(@PathVariable("roleId") Integer roleId) {
        List<UserRole> userRoleKey = D.sql("select * from T_P_USER_ROLE where role_id = ?").many(UserRole.class,
                roleId);
        return userRoleKey.isEmpty();
    }

    /*
     * 角色管理：删除
     *
     * @param roleId
     *
     * @return zhangxiaomei 2013-8-25
     */
    @ResponseBody
    @RequestMapping(value = "delete/{roleId}")
    public boolean delete(@PathVariable("roleId") String roleId) {
        D.sql("delete from T_P_ROLE where role_id = ? ").update(roleId);
        return true;
    }

}
