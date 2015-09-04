package com.whtriples.airPurge.rbac.model;

import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

import java.util.Date;

@Table("T_P_SYS_LOG")
public class SysLog {
    @Id
    @GenerateByDb
    private Long log_id;

    private Integer user_id;

    private Date oper_time;

    private String device_code;

    private Integer fun_opt_id;

    private String url;

    private String param;

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getOper_time() {
        return oper_time;
    }

    public void setOper_time(Date oper_time) {
        this.oper_time = oper_time;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public Integer getFun_opt_id() {
        return fun_opt_id;
    }

    public void setFun_opt_id(Integer fun_opt_id) {
        this.fun_opt_id = fun_opt_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}