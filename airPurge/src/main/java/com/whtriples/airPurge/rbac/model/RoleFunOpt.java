package com.whtriples.airPurge.rbac.model;

import com.rps.util.dao.annotation.Pk;
import com.rps.util.dao.annotation.Table;

@Table("T_P_ROLE_FUN_OPT")
public class RoleFunOpt {

    @Pk
    private Long role_id;
    @Pk
    private Long fun_opt_id;


    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getFun_opt_id() {
        return fun_opt_id;
    }

    public void setFun_opt_id(Long fun_opt_id) {
        this.fun_opt_id = fun_opt_id;
    }

}