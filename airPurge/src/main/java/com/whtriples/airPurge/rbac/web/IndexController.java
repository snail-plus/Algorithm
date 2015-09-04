package com.whtriples.airPurge.rbac.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

	@RequestMapping()
	public String login(HttpServletRequest request, HttpSession session) {
		return "index";
	}

	@RequestMapping("/left")
	public String left(Model model, HttpServletRequest request) {
		return "left";
	}

	@RequestMapping("/top")
	public String top(Model model, HttpServletRequest request) {
		return "top";
	}

	@RequestMapping("/main")
	public String main(Model model) {
		return "main";
	}

	@RequestMapping("/bottom")
	public String bottom(Model model) {
		return "bottom";
	}

}
