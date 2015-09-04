package com.whtriples.airPurge.base.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rps.util.D;
import com.whtriples.airPurge.base.model.Dict;
import com.whtriples.airPurge.base.model.DictDetail;
import com.whtriples.airPurge.config.ComboxModel;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;

@Controller
@RequestMapping("base/dict")
public class DictController {

	@RequestMapping()
	public String init(Model model) {
		model.addAttribute("commTypeList", D.sqlAt("sql.dict/getDict").many(ComboxModel.class, "COMM_TYPE"));
		return "base/dict";
	}

	@ResponseBody
	@RequestMapping("list")
	public PageModel list(HttpServletRequest request) {
		String sql = "sql.dict/getPageDict";
		return PageUtil.getPageModel(Dict.class, sql, request);
	}

	@ResponseBody
	@RequestMapping("check")
	public boolean check(String dictId) {
		Dict dict = D.selectById(Dict.class, dictId);
		return dict == null;
	}

	@ResponseBody
	@RequestMapping("get/{dictId}")
	public List<ComboxModel> getCombox(@PathVariable String dictId) {
		return D.sqlAt("sql.dict/getDict").many(ComboxModel.class, dictId);
	}
	
	@ResponseBody
	@RequestMapping("getStrKey/{dictId}")
	public List<ComboxModel> getStrKey(@PathVariable String dictId) {
		return D.sqlAt("sql.dict/getDict").many(ComboxModel.class, dictId);
	}

	@ResponseBody
	@RequestMapping("create")
	public boolean create(@RequestBody Dict dict) {
		try {
			D.insert(dict);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping("{dictId}")
	public Dict get(@PathVariable("dictId") String dictId) {
		return D.selectById(Dict.class, dictId);
	}

	@ResponseBody
	@RequestMapping("update")
	public boolean update(@RequestBody Dict dict) {
		try {
			D.updateWithoutNull(dict);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping("delete/{dictId}")
	public boolean delete(@PathVariable("dictId") String dictId) {
		boolean flag;
		try {
			D.deleteById(Dict.class, dictId);
			D.deleteByWhere(DictDetail.class, " DICT_ID = ?", dictId);
			flag=true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
}
