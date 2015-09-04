package com.whtriples.airPurge.rbac.model;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

import java.util.List;

@Table("T_P_MENUE")
public class Menue {
    @Id
    @GenerateByDb
    private Integer menue_id;

    private String menue_nm;

    private Integer par_menue_id;

    private String remarks;

    private String state;

    private Short menue_order;

    private Long fun_opt_id;

    @ColumnIgnore
    private String url;

    @ColumnIgnore
    private List<Menue> children;

    public Integer getMenue_id() {
        return menue_id;
    }

    public void setMenue_id(Integer menue_id) {
        this.menue_id = menue_id;
    }

    public String getMenue_nm() {
        return menue_nm;
    }

    public void setMenue_nm(String menue_nm) {
        this.menue_nm = menue_nm;
    }


    public Integer getPar_menue_id() {
        return par_menue_id;
    }

    public void setPar_menue_id(Integer par_menue_id) {
        this.par_menue_id = par_menue_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Short getMenue_order() {
        return menue_order;
    }

    public void setMenue_order(Short menue_order) {
        this.menue_order = menue_order;
    }

    public Long getFun_opt_id() {
        return fun_opt_id;
    }

    public void setFun_opt_id(Long fun_opt_id) {
        this.fun_opt_id = fun_opt_id;
    }

    public List<Menue> getChildren() {
        return children;
    }

    public void setChildren(List<Menue> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}