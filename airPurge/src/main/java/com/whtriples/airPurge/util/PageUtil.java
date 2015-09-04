package com.whtriples.airPurge.util;

import com.rps.util.D;
import com.rps.util.dao.Page;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

public class PageUtil {

    /**
     * 获取分页数据
     *
     * @return
     */
    public static PageModel getPageModel(Class<?> clazz, String clojureId, HttpServletRequest request) {
        Page<?> page = D.defSql(clojureId, request).pageSqlAndParams(clazz);
        PageModel PageModel = new PageModel(page.getTotalCount(), page.getData());
        return PageModel;
    }

    public static PageModel getPageModel(Class<?> clazz, String clojureId, HttpServletRequest request, @SuppressWarnings("rawtypes") Map map) {
        Page<?> page = D.defSql(clojureId, request, map).pageSqlAndParams(clazz);
        PageModel PageModel = new PageModel(page.getTotalCount(), page.getData());
        return PageModel;
    }
}
