package com.whtriples.airPurge.rbac.shiro;

import java.io.Serializable;

import com.whtriples.airPurge.rbac.model.User;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuData;

    public ShiroUser(Long userId, String loginId, String userName) {
        super.setUser_id(userId);
        super.setLogin_id(loginId);
        super.setUser_name(userName);
    }

    /**
     * @return the menuData
     */
    public String getMenuData() {
        return menuData;
    }

    /**
     * @param menuData the menuData to set
     */
    public void setMenuData(String menuData) {
        this.menuData = menuData;
    }
}