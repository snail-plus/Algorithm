package com.whtriples.airPurge.base.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rps.util.D;
import com.whtriples.airPurge.base.model.DictDetail;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.util.PageModel;


@Controller
@RequestMapping("base/dictDetail")
public class DictDetailController {

	@RequestMapping("")
	public String init(String dictId, Model model) {
		model.addAttribute("dictId", dictId);
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "base/dict_detail";
	}

	@ResponseBody
	@RequestMapping("all")
	public List<DictDetail> listAll(String dictId) {
		String sql = "SELECT * FROM T_P_DICT_DETAIL WHERE DICT_ID = ?";
		return D.sql(sql).many(DictDetail.class, dictId);
	}

	@ResponseBody
	@RequestMapping("list")
	public PageModel list(String dictId) {
		String sql = "SELECT * FROM  T_P_DICT_DETAIL WHERE DICT_ID = ?";
		List<DictDetail> list = D.sql(sql).many(DictDetail.class, dictId);
		return new PageModel(list.size(), list);
	}

	@ResponseBody
	@RequestMapping("create")
	public boolean create(@RequestBody DictDetail dictDetail) {
		D.insert(dictDetail);
		return true;
	}

	@ResponseBody
	@RequestMapping("{dictId}/{dictParamValue}/{dictParamName}")
	public DictDetail get(@PathVariable("dictId") String dictId, @PathVariable("dictParamValue") String dictParamValue,
			@PathVariable("dictParamName") String dictParamName) {
		String sql = "SELECT * FROM T_P_DICT_DETAIL WHERE DICT_ID = ? AND DICT_PARAM_VALUE = ?";
		return D.sql(sql).oneOrNull(DictDetail.class, dictId, dictParamValue);
	}

	@ResponseBody
	@RequestMapping("update")
	public boolean update(@RequestBody DictDetail dictDetail) {
		try {
			String sql = "update t_p_dict_detail set dict_param_name = ?,dict_param_name_en = ? ,dict_param_status = ?  where dict_id = ? and dict_param_value = ?";
			D.sql(sql).update(dictDetail.getDict_param_name(),
					dictDetail.getDict_param_name_en(), dictDetail.getDict_param_status(), dictDetail.getDict_id(),dictDetail.getDict_param_value());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping("delete/{dictId}/{dictParamValue}")
	public boolean delete(@PathVariable("dictId") String id, @PathVariable("dictParamValue") String value) {
		String sql = "DELETE FROM T_P_DICT_DETAIL WHERE DICT_ID = ? AND DICT_PARAM_VALUE = ?";
		D.sql(sql).update(id, value);
		return true;
	}

	/**
	 * 添加的时候判断是否存在
	 * @param dictId
	 * @param dictParamValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/check")
	public boolean createCheck(String dictId, String dictParamValue) {
		String sql = "SELECT * FROM T_P_DICT_DETAIL WHERE DICT_ID = ? AND DICT_PARAM_VALUE = ?";
		return D.sql(sql).oneOrNull(DictDetail.class, dictId, dictParamValue) == null;
	}

	
}
