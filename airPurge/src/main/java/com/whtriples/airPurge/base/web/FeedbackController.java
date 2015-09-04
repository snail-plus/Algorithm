package com.whtriples.airPurge.base.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rps.util.D;
import com.whtriples.airPurge.base.model.Feedback;
import com.whtriples.airPurge.util.PageModel;
import com.whtriples.airPurge.util.PageUtil;

@Controller
@RequestMapping("base/feedback")
public class FeedbackController {
	@RequestMapping({ "/init", "" })
	public String init(Model model) {
		return "base/feedback";
	}

	@ResponseBody
	@RequestMapping("/list")
	public PageModel list(HttpServletRequest request) {
		String sql = "sql.feedback/getPageFeedback";
		return PageUtil.getPageModel(Feedback.class, sql, request);
	}

	@ResponseBody
	@RequestMapping("/edit/{feedbackId}")
	public Feedback edit(@PathVariable Integer feedbackId) {
		return D.sql(
				"SELECT t_d_feedback.*, t_p_user.user_name FROM t_d_feedback LEFT JOIN t_p_user ON t_d_feedback.user_ID = t_p_user.user_id WHERE FEEDBACK_ID=?")
				.one(Feedback.class, feedbackId);
	}

	@ResponseBody
	@RequestMapping("/update")
	public boolean update(@RequestBody Feedback feedback) {
		return	D.updateWithoutNull(feedback)>0;
	
	}
}
