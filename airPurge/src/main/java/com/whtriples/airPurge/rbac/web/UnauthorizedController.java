package com.whtriples.airPurge.rbac.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/unauthorized")
public class UnauthorizedController {

	@RequestMapping(method = RequestMethod.GET)
	public String unauthorized() {
		return "unauthorized";
	}
}
