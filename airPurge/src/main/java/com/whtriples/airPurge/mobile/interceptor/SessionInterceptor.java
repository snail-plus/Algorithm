package com.whtriples.airPurge.mobile.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Max-Age", "60");
    	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    	return true;
    }
    
}
