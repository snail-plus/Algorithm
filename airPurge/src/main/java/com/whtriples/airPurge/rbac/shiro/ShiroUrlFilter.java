package com.whtriples.airPurge.rbac.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ShiroUrlFilter extends AuthorizationFilter {

	@Override
	public String getUnauthorizedUrl() {
		return super.getUnauthorizedUrl();
	}

	@Override
	public void setUnauthorizedUrl(String unauthorizedUrl) {
		super.setUnauthorizedUrl(unauthorizedUrl);
	}

	@Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);

        boolean isPermitted = true;
        String requestURI = getPathWithinApplication(request);
     
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //放行ajax请求
        if(subject.isAuthenticated() && (httpServletRequest.getHeader("x-requested-with") != null ) && "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("x-requested-with"))){
        	return true;
        }
        //请求请根路径时如果已经认证则转向index
        if(subject.isAuthenticated() && "/".equals(requestURI)){
        	 httpResponse.sendRedirect(httpServletRequest.getRequestURL().toString() + "index");
        	 return true;
        }
        if (!subject.isPermitted(requestURI)) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return isPermitted;
    }
}