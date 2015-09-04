package com.whtriples.airPurge.base.model;

import com.rps.util.dao.annotation.Pk;
import com.rps.util.dao.annotation.Table;

@Table("t_p_dict_detail")
public class DictDetail {
	/** 数据字典ID */
	@Pk
	private String dict_id;

	/** 字典参数值 */
	@Pk
	private String dict_param_value;

	/** 字典参数名称 */
	private String dict_param_name;

	/** 字典参数名称英文 */
	private String dict_param_name_en;

	/** 字典参数状态。1、有效；0、失效 */
	private String dict_param_status;

	public String getDict_id() {
		return dict_id;
	}

	public void setDict_id(String dict_id) {
		this.dict_id = dict_id;
	}

	public String getDict_param_value() {
		return dict_param_value;
	}

	public void setDict_param_value(String dict_param_value) {
		this.dict_param_value = dict_param_value;
	}

	public String getDict_param_name() {
		return dict_param_name;
	}

	public void setDict_param_name(String dict_param_name) {
		this.dict_param_name = dict_param_name;
	}

	public String getDict_param_name_en() {
		return dict_param_name_en;
	}

	public void setDict_param_name_en(String dict_param_name_en) {
		this.dict_param_name_en = dict_param_name_en;
	}

	public String getDict_param_status() {
		return dict_param_status;
	}

	public void setDict_param_status(String dict_param_status) {
		this.dict_param_status = dict_param_status;
	}
}
