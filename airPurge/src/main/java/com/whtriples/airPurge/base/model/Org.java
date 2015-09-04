package com.whtriples.airPurge.base.model;

import java.util.Date;

import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("T_D_ORG")
public class Org {

	@Id
	@GenerateByDb
	private Integer org_id;
	
	private String org_code;

	/** 机构名称 */
	private String org_name;

	/** 机构排序 */
	private Integer org_sort;

	/** 机构层级 */
	private Integer org_level;

	/** 状态 */
	private String org_status;

	private Integer par_id;

	/** 描述 */
	private String org_desc;

	/** 创建时间 */
	private String create_time;

	/** 创建人 */
	private Integer create_by;

	/** 更新时间 */
	private Date update_time;

	public Integer getOrg_id() {
		return org_id;
	}

	public String getOrg_code() {
		return org_code;
	}

	public String getOrg_name() {
		return org_name;
	}

	public Integer getOrg_sort() {
		return org_sort;
	}

	public Integer getOrg_level() {
		return org_level;
	}

	public String getOrg_status() {
		return org_status;
	}

	public Integer getPar_id() {
		return par_id;
	}

	public String getOrg_desc() {
		return org_desc;
	}


	public Integer getCreate_by() {
		return create_by;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public void setOrg_sort(Integer org_sort) {
		this.org_sort = org_sort;
	}

	public void setOrg_level(Integer org_level) {
		this.org_level = org_level;
	}

	public void setOrg_status(String org_status) {
		this.org_status = org_status;
	}

	public void setPar_id(Integer par_id) {
		this.par_id = par_id;
	}

	public void setOrg_desc(String org_desc) {
		this.org_desc = org_desc;
	}


	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
