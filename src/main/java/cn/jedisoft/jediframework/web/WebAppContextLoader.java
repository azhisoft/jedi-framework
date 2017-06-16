package cn.jedisoft.jediframework.web;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.app.BaseContextLoader;
import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.core.BeanWrapper;
import cn.jedisoft.jediframework.core.conf.ApiDefinition;
import cn.jedisoft.jediframework.core.conf.AppContextConf;
import cn.jedisoft.jediframework.web.annotations.Path;
import cn.jedisoft.jediframework.web.core.WebApiWrapper;
import cn.jedisoft.jediframework.web.core.WebAppContext;

/**
 * WebAppContext 加载器
 * 
 * @author lzm
 *
 */
public class WebAppContextLoader extends BaseContextLoader<WebAppContext> {

	private static final Logger log = Logger.getLogger(WebAppContextLoader.class);

	private ServletContext servletCtx = null;
	
	public WebAppContextLoader(ServletContext servletCtx) {
		this.servletCtx = servletCtx;
	}

	@Override
	public WebAppContext load(AppContextConf conf) {
		JediWebAppContext context = new JediWebAppContext(servletCtx);

		context.setAutowireUsing(conf.getJediIoC().getUsing());

		addDefinitionBeans(context, conf.getJediBean());

		scanPackages(context, conf.getJediScan());

		addDefinitionApis(context, conf.getJediApi());

		addDefinitionDss(context, conf.getJediDs());

		return context;
	}

	/**
	 * 扫描指定的包或类路径（classpath）
	 * @param context
	 * @param packages
	 */
	protected void scanPackages(JediWebAppContext context, List<String> packages) {
		if(packages == null)
			return;

		for(String packagePath : packages) {
			Set<Class<?>> classes = ClassUtils.scan(packagePath);

			for(Class<?> clazz : classes) {
				BeanWrapper bean = detectComponentAnno(clazz);
				
				if(bean != null) {
					context.addBean(bean);
				}
				
				WebApiWrapper api = detectPathAnno(clazz);
				
				if(api != null) {
					context.addApi(api);
				}
			}
		}
	}

	/**
	 * 遍历配置文件中的 Api 列表，并逐个添加到 WebAppContext
	 * @param context
	 * @param apis
	 */
	protected void addDefinitionApis(JediWebAppContext context, List<ApiDefinition> apis) {
		if(apis == null)
			return;

		for(ApiDefinition api : apis) {
			String path = api.getPath();
			String beanName = api.getBeanName();

			if(StringUtils.isNotBlank(path) && StringUtils.isNotBlank(beanName)) {
				Object bean = context.getBean(beanName);
				
				if(bean != null) {
					JediApiWrapper wrapper = new JediApiWrapper();

					if(wrapper.wrap(path, bean)) {
						log.info(String.format("DEFINED: %s <- %s.", path, beanName));

						context.addApi(wrapper);
					}

				} else {
					log.warn(String.format("addDefinitionApis() failed: \"%s\" not found.", beanName));
				}
			}
		}
	}

	/**
	 * 检查 Path 注解，如果有，则表示该对象可能是一个 Api 处理对象
	 * @param clazz
	 * @return
	 */
	public WebApiWrapper detectPathAnno(Class<?> clazz) {
		// 有 Path 注解
		if(clazz.isAnnotationPresent(Path.class)) {
			Path path = (Path)clazz.getAnnotation(Path.class);
			String url = path.value();

			if(StringUtils.isNotBlank(url)) {
				JediApiWrapper wrapper = new JediApiWrapper();
	
				if(wrapper.wrap(url, clazz)) {
					log.info(String.format("FOUND: %s <- %s.", url, clazz.getName()));
	
					return wrapper;
				}

			} else {
				log.warn("detectPathAnno(): path is empty.");
			}
		}

		return null;
	}

}
