package cn.jedisoft.jediframework.app;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.annotations.Autowired;
import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.core.BeanFactory;
import cn.jedisoft.jediframework.core.BeanWrapper;

/**
 * BeanFactory 实现类
 * 
 * @author lzm
 *
 */
public class JediBeanFactory implements BeanFactory {

	private static final Logger log = Logger.getLogger(JediBeanFactory.class);

	private Map<String, BeanWrapper> beans = new ConcurrentHashMap<String, BeanWrapper>();

	public <T> void addBean(String name, T bean) {
		BeanWrapper w = new JediBeanWrapper(name, bean);

		addBean(w);
	}

	/**
	 * 添加 Bean 包装到工厂
	 * @param bean
	 */
	public <T> void addBean(BeanWrapper bean) {
		if(beans.containsKey(bean.getName())) {
			log.warn(String.format("EXISTED: %s", bean.getName()));

		} else {
			beans.put(bean.getName(), bean);

			log.info(String.format("ADDED: %s", bean.getName()));
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name) {
		BeanWrapper bean = beans.get(name);

		if(bean != null) {
			return (T)bean.getBean(this);
		}

		return null;
	}

	public <T> void autowire(T obj) {
		Field[] fields = obj.getClass().getDeclaredFields();

		for(Field field : fields) {
			if(field.isAnnotationPresent(Autowired.class)) {
				Autowired wire = (Autowired)field.getAnnotation(Autowired.class);
				String beanName = wire.value();

				if(StringUtils.isBlank(beanName)) {
					beanName = StringUtils.formatBeanName(field.getGenericType().toString());
				}

				ClassUtils.setFieldValue(obj, field, getBean(beanName));

				log.info(String.format("AUTOWIRED: %s -> %s@%s.", beanName, field.getName(), obj.getClass().getName()));
			}
		}
	}

	/**
	 * 注销工厂类，以及工厂类中保存的对象
	 */
	protected void destroy() {
		
	}

}
