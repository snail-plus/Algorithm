package com.whtriples.airPurge.rbac.model;

import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("T_P_ROLE")
public class Role {
    @Id
    @GenerateByDb
    private Integer role_id;

    private String role_nm;

    private String role_desc;

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getRole_nm() {
        return role_nm;
    }

    public void setRole_nm(String role_nm) {
        this.role_nm = role_nm;
    }

    public String getRole_desc() {
        return role_desc;
    }

    public void setRole_desc(String role_desc) {
        this.role_desc = role_desc;
    }

}