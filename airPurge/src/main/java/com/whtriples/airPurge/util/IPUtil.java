package com.whtriples.airPurge.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {
    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
            logger.debug("PRoxy-Client-IP=" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.debug("WL-Proxy-Client-IP=" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.debug("remoteAddr=" + ip);
        }
        if (null == ip) {
            ip = "";
        }
        logger.debug("Exit method getIpAddr(). ip=" + ip);
        if (StringUtils.isNotEmpty(ip)) {
            String[] ipArr = ip.split(",");
            if (ipArr.length > 1) {
                ip = ipArr[0];
            }
        }
        return ip;
    }
}
