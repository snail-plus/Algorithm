package com.whtriples.airPurge.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    //阻止异常被抛到浏览器
	@ExceptionHandler({Exception.class })
	public ModelAndView toException(Exception e) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("error/500");
		mv.addObject("error", e);
		return mv;
	}
}
