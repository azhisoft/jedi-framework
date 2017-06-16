package cn.jedisoft.jediframework.app;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.core.BeanFactory;
import cn.jedisoft.jediframework.core.BeanWrapper;

/**
 * BeanWrapper 包装类
 * 
 * @author lzm
 *
 * @param <T>
 */
public class JediBeanWrapper<T> implements BeanWrapper {

	private static final Logger log = Logger.getLogger(JediBeanWrapper.class);

	private int scope = SCOPE_SINGLETON;
	private String name = null;
	private Class<?> clazz = null;
	private T bean = null;

	/**
	 * 对指定实体进行包装
	 * @param name
	 * @param bean
	 */
	public JediBeanWrapper(String name, T bean) {
		this.name = name;
		this.clazz = bean.getClass();
		this.bean = bean;

		log.info(String.format("WRAPED: %s <- %s.", name, clazz.getName()));
	}

	/**
	 * 对指定 class 进行包装
	 * @param name
	 * @param clazz
	 */
	public JediBeanWrapper(String name, Class<?> clazz) {
		this.name = name;
		this.clazz = clazz;

		log.info(String.format("WRAPED: %s <- %s.", name, clazz.getName()));
	}

	/**
	 * 对指定 class ，按生命周期进行包装
	 * @param name
	 * @param clazz
	 * @param scope
	 */
	public JediBeanWrapper(String name, Class<?> clazz, int scope) {
		this.name = name;
		this.clazz = clazz;
		this.scope = scope;

		log.info(String.format("WRAPED: %s <- %s.", name, clazz.getName()));
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public T getBean(BeanFactory factory) {
		if(scope == SCOPE_PROTOTYPE) {
			T bean = (T)ClassUtils.createInstance(clazz);

			if(factory != null)
				factory.autowire(bean);

			log.info(String.format("PROTOTYPE: %s.", clazz.getName()));

			return bean;
		}

		if(bean == null) {
			bean = (T)ClassUtils.createInstance(clazz);

			if(factory != null)
				factory.autowire(bean);
		}

		log.info(String.format("SINGLETON: %s.", clazz.getName()));

		return bean;
	}

}
