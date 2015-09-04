package com.whtriples.airPurge.util;

import com.rps.util.D;
import com.rps.util.SpringContextUtil;
import com.rps.util.dao.dialet.MySqlDialet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import java.io.IOException;

/**
 * 系统启动时加载
 *
 * @author ghl
 * @version 1.0
 */
public class SysContextLoaderListener extends ContextLoaderListener implements ServletContextListener {

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext servletContext = event.getServletContext();
        if (servletContext != null) {
            ObjectFactory.getInstance(servletContext);
        }
        try {
            init();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ServletException, IOException {
        ApplicationContext ctx = getCurrentWebApplicationContext();
        SpringContextUtil.setApplicationContext(ctx);
        DataSource dataSource = SpringContextUtil.getBean("dataSource");
        D.setDataSourceAndDialet(dataSource, new MySqlDialet());
    }

}
