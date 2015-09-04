package com.whtriples.airPurge.rbac.model;

import java.util.List;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("T_P_FUN_OPT")
public class FunOpt {
    @Id
    @GenerateByDb
    private Long fun_opt_id;

    private String fun_opt_nm;

    private String url;

    private String remarks;

    private Long parent_fun_opt_id;
    @ColumnIgnore
    private List<FunOpt> children;

    public Long getFun_opt_id() {
        return fun_opt_id;
    }

    public void setFun_opt_id(Long fun_opt_id) {
        this.fun_opt_id = fun_opt_id;
    }

    public String getFun_opt_nm() {
        return fun_opt_nm;
    }

    public void setFun_opt_nm(String fun_opt_nm) {
        this.fun_opt_nm = fun_opt_nm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getParent_fun_opt_id() {
        return parent_fun_opt_id;
    }

    public void setParent_fun_opt_id(Long parent_fun_opt_id) {
        this.parent_fun_opt_id = parent_fun_opt_id;
    }

    public List<FunOpt> getChildren() {
        return children;
    }

    public void setChildren(List<FunOpt> children) {
        this.children = children;
    }

}