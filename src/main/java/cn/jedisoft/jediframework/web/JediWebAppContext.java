package cn.jedisoft.jediframework.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.app.JediAppContext;
import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.core.AutowireBeanFactory;
import cn.jedisoft.jediframework.core.conf.IoCDefinition;
import cn.jedisoft.jediframework.web.core.WebApiWrapper;
import cn.jedisoft.jediframework.web.core.WebAppContext;
import cn.jedisoft.jediframework.web.plugin.SpringAutowireBeanFactory;

/**
 * WebAppContext 实现类
 * 
 * @author lzm
 *
 */
public class JediWebAppContext extends JediAppContext implements WebAppContext {

	private static final Logger log = Logger.getLogger(JediWebAppContext.class);

	private ServletContext servletCtx = null;

	private AutowireBeanFactory autowireBeanFactory = this;

	private List<WebApiWrapper> apis = new ArrayList<WebApiWrapper>();
	
	public JediWebAppContext(ServletContext servletCtx) {
		this.servletCtx = servletCtx;
	}

	public void setAutowireUsing(String using) {
		if(StringUtils.isNotBlank(using) && IoCDefinition.USING_SPRING.equalsIgnoreCase(using.trim())) {
			autowireBeanFactory = new SpringAutowireBeanFactory(servletCtx);
		}
	}

	public AutowireBeanFactory getAutowireBeanFactory() {
		return autowireBeanFactory;
	}

	public WebApiWrapper matchApi(String url, String method) {
		String m = method.trim().toUpperCase();

		for(int i = 0; i < apis.size(); i ++) {
			WebApiWrapper api = apis.get(i);

			if(api.matches(url, m))
				return api;
		}

		return null;
	}

	/**
	 * 添加 Api 对象到上下文
	 * @param api
	 */
	public void addApi(WebApiWrapper api) {
		apis.add(api);

		log.info(String.format("ADDED: %s", api.getRequestPath()));
	}

}
