package cn.jedisoft.jediframework.app;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;

import cn.jedisoft.jediframework.annotations.Component;
import cn.jedisoft.jediframework.annotations.Scope;
import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.common.ConfigUtils;
import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.core.BeanWrapper;
import cn.jedisoft.jediframework.core.ContextLoader;
import cn.jedisoft.jediframework.core.conf.AppContextConf;
import cn.jedisoft.jediframework.core.conf.BeanDefinition;
import cn.jedisoft.jediframework.core.conf.DsDefinition;
import cn.jedisoft.jediframework.db.DatabaseWrapper;

/**
 * 应用上下文加载器基类
 * 
 * @author lzm
 *
 * @param <T>
 */
public class BaseContextLoader<T> implements ContextLoader<T> {

	private static final Logger log = Logger.getLogger(BaseContextLoader.class);

	public T load(String confPath) {
		InputStream stream = IOUtils.toInputStream(confPath);
		AppContextConf conf = ConfigUtils.load(AppContextConf.class, stream, ConfigUtils.TYPE_XML);

		return load(conf);
	}

	public T load(AppContextConf conf) {
		return null;
	}

	/**
	 * 遍历配置文件中的 Bean 列表，并逐个添加到 AppContext
	 * @param context
	 * @param beans
	 */
	protected void addDefinitionBeans(JediAppContext context, List<BeanDefinition> beans) {
		if(beans == null)
			return;

		for(BeanDefinition bean : beans) {
			if(StringUtils.isNotBlank(bean.getName()) && StringUtils.isNotBlank(bean.getClassName())) {
				try {
					Class<?> clazz = Class.forName(bean.getClassName());
					int scope = BeanWrapper.SCOPE_SINGLETON;

					if (Scope.SCOPE_PROTOTYPE.equalsIgnoreCase(bean.getScope())) {
						scope = BeanWrapper.SCOPE_PROTOTYPE;
					}

					log.info(String.format("DEFINED: %s <- %s.", bean.getName(), clazz.getName()));

					context.addBean(new JediBeanWrapper(bean.getName(), clazz, scope));

				} catch (ClassNotFoundException e) {
					log.error("addDefinitionBeans() failed: ", e);
				}
			}
		}
	}

	/**
	 * 扫描指定的包或类路径（classpath）
	 * @param context
	 * @param packages
	 */
	protected void scanPackages(JediAppContext context, List<String> packages) {
		if(packages == null)
			return;
		
		for(String packagePath : packages) {
			Set<Class<?>> classes = ClassUtils.scan(packagePath);

			for(Class<?> clazz : classes) {
				BeanWrapper bean = detectComponentAnno(clazz);
				
				if(bean != null) {
					context.addBean(bean);
				}
			}
		}
	}

	/**
	 * 检查 Component 注解，如果有，则表示该对象可能是一个自动注入对象
	 * @param clazz
	 * @return
	 */
	protected BeanWrapper detectComponentAnno(Class<?> clazz) {
		// 有 Component 注解
		if(clazz.isAnnotationPresent(Component.class)) {
			int scope = BeanWrapper.SCOPE_PROTOTYPE;
			Scope scopeAnno = (Scope)clazz.getAnnotation(Scope.class);
	
			if (scopeAnno == null ||
				(scopeAnno != null && Scope.SCOPE_SINGLETON.equalsIgnoreCase(scopeAnno.value()))) {
				scope = BeanWrapper.SCOPE_SINGLETON;
			}
	
			Component comp = (Component)clazz.getAnnotation(Component.class);
			String beanName = comp.value();
	
			if(StringUtils.isBlank(beanName)) {
				beanName = StringUtils.formatBeanName(clazz.getName());
			}
	
			log.info(String.format("FOUND: %s <- %s.", beanName, clazz.getName()));
	
			return new JediBeanWrapper(beanName, clazz, scope);
		}

		return null;
	}

	/**
	 * 遍历配置文件中的 DataSource 列表，并逐个添加到 AppContext
	 * @param context
	 * @param dss
	 */
	protected void addDefinitionDss(JediAppContext context, List<DsDefinition> dss) {
		if(dss == null)
			return;

		DataSource ods = null;
		DataSource wds = null;
		
		for(DsDefinition ds : dss) {
			DruidDataSource dds = new DruidDataSource();
			
			dds.setDriverClassName(ds.getDriver());
			dds.setUrl(ds.getUrl());
			dds.setUsername(ds.getUsername());
			dds.setPassword(ds.getPassword());
			
			if(DsDefinition.MODE_READ_ONLY.equalsIgnoreCase(ds.getMode())) {
				ods = dds;

			} else if(DsDefinition.MODE_READ_WRITE.equalsIgnoreCase(ds.getMode())) {
				wds = dds;
				
				if(ods == null)
					ods = dds;
			}
		}

		context.setReadOnlyDataSource(ods);
		context.setReadWriteDataSource(wds);

		context.addBean("database", new DatabaseWrapper(context));
	}

}
