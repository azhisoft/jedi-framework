package cn.jedisoft.jediframework.web.plugin;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.jedisoft.jediframework.core.AutowireBeanFactory;

/**
 * 用于支持 Spring 自动注入
 * 
 * @author lzm
 *
 */
public class SpringAutowireBeanFactory implements AutowireBeanFactory {

	private static final Logger log = Logger.getLogger(SpringAutowireBeanFactory.class);

	private ServletContext servletCtx = null;
	private ApplicationContext appCtx = null;
	private AutowireCapableBeanFactory factory = null; 
	
	public SpringAutowireBeanFactory(ServletContext servletCtx) {
		this.servletCtx = servletCtx;
	}

	public <T> void autowire(T obj) {
		if(factory == null) {
			appCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletCtx);
			factory = appCtx.getAutowireCapableBeanFactory();
		}

		try {
			factory.autowireBean((Object)obj);

		} catch (BeansException e) {
			log.error("autowire() failed: ", e);
		}
	}

}
