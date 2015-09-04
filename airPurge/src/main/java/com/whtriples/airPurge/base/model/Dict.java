package com.whtriples.airPurge.base.model;

import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("t_p_dict")
public class Dict {
	/**字典编号*/
	@Id
	private String dict_id;

	/**字段名称*/
	private String dict_name;

	/**字典描述*/
	private String dict_desc;

	/**字典状态。1、有效；0、失效*/
	private String dict_status;

	
	public String getDict_id() {
		return dict_id;
	}

	public void setDict_id(String dict_id) {
		this.dict_id = dict_id;
	}
	
	public String getDict_name() {
		return dict_name;
	}

	public void setDict_name(String dict_name) {
		this.dict_name = dict_name;
	}
	
	public String getDict_desc() {
		return dict_desc;
	}

	public void setDict_desc(String dict_desc) {
		this.dict_desc = dict_desc;
	}
	
	public String getDict_status() {
		return dict_status;
	}

	public void setDict_status(String dict_status) {
		this.dict_status = dict_status;
	}
}
 