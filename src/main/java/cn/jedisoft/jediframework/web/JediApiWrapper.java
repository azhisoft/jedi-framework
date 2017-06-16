package cn.jedisoft.jediframework.web;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.core.AutowireBeanFactory;
import cn.jedisoft.jediframework.web.annotations.HttpMethod;
import cn.jedisoft.jediframework.web.annotations.method.DELETE;
import cn.jedisoft.jediframework.web.annotations.method.GET;
import cn.jedisoft.jediframework.web.annotations.method.POST;
import cn.jedisoft.jediframework.web.annotations.method.PUT;
import cn.jedisoft.jediframework.web.common.UriPattern;
import cn.jedisoft.jediframework.web.core.MethodWrapper;
import cn.jedisoft.jediframework.web.core.WebApiWrapper;
import cn.jedisoft.jediframework.web.result.WebResult;
import cn.jedisoft.jediframework.web.result.WebResultProcessor;

/**
 * ApiWrapper 包装类
 * 
 * @author lzm
 *
 */
public class JediApiWrapper extends WebResultProcessor implements WebApiWrapper {

	private static final Logger log = Logger.getLogger(JediApiWrapper.class);

	private UriPattern pattern = null;
	private String path = "";
	private Object bean = null;
	private Class<?> clazz = null;
	private Map<String, MethodWrapper> methods = new ConcurrentHashMap<String, MethodWrapper>();

	/**
	 * 对 URI 和 java 实例进行绑定和包装
	 * @param path
	 * @param bean
	 * @return
	 */
	public boolean wrap(String path, Object bean) {
		this.pattern = UriPattern.compile(path);
		this.path = path;
		this.bean = bean;

		return wrap(bean.getClass());
	}

	/**
	 * 对 URI 和 java 类进行绑定和包装
	 * @param path
	 * @param clazz
	 * @return
	 */
	public boolean wrap(String path, Class<?> clazz) {
		this.pattern = UriPattern.compile(path);
		this.path = path;
		this.clazz = clazz;

		return wrap(clazz);
	}

	/**
	 * 绑定 java 类
	 * @param clazz
	 * @return
	 */
	protected boolean wrap(Class<?> clazz) {
		Method[] declMethods = clazz.getDeclaredMethods();

		// 通过注解查找方法的映射关系
		for(Method method : declMethods) {
			GET getAnno = (GET)method.getAnnotation(GET.class);
			PUT putAnno = (PUT)method.getAnnotation(PUT.class);
			POST postAnno = (POST)method.getAnnotation(POST.class);
			DELETE delAnno = (DELETE)method.getAnnotation(DELETE.class);

			if(getAnno != null)wrapMethod(HttpMethod.METHOD_GET, method);
			if(putAnno != null)wrapMethod(HttpMethod.METHOD_PUT, method);
			if(postAnno != null)wrapMethod(HttpMethod.METHOD_POST, method);
			if(delAnno != null)wrapMethod(HttpMethod.METHOD_DELETE, method);
		}

		// 如果没有注解，或者注解不全
		if(methods.size() < 4) {
			// 通过方法名建立默认的映射关系
			for(Method method : declMethods) {
				String methodName = method.getName();

				if("doGet".equalsIgnoreCase(methodName)) {
					wrapMethod(HttpMethod.METHOD_GET, method);

				} else if("doPut".equalsIgnoreCase(methodName)) {
					wrapMethod(HttpMethod.METHOD_PUT, method);

				} else if("doPost".equalsIgnoreCase(methodName)) {
					wrapMethod(HttpMethod.METHOD_POST, method);

				} else if("doDelete".equalsIgnoreCase(methodName)) {
					wrapMethod(HttpMethod.METHOD_DELETE, method);
				}
			}
		}

		int mc = methods.size();

		if(mc > 0) {
			log.info(String.format("WRAP: %d methods wraped on %s.", mc, path));

		} else {
			log.warn(String.format("WRAP: %d method wraped on %s.", mc, path));
		}

		return (mc > 0);
	}

	/**
	 * 把 HTTP 请求方法和 java 对象的方法进行绑定
	 * @param method
	 * @param m
	 */
	protected void wrapMethod(String method, Method m) {
		if(methods.containsKey(method)) {
			log.warn(String.format("EXISTED: wrapping %s on %s.", method, path));

			return;
		}

		JediMethodWrapper wrapper = new JediMethodWrapper(method);

		if(wrapper.wrap(m)) {
			methods.put(method, wrapper);

			log.info(String.format("WRAPED: %s wraped on %s.", method, path));

		} else {
			log.warn(String.format("FAILED: wrapping %s on %s.", method, path));
		}
	}

	public String getRequestPath() {
		return path;
	}

	public boolean matches(String url, String method) {
		return (pattern.matches(url) && methods.containsKey(method));
	}

	public boolean process(String url, String method, HttpServletRequest request, HttpServletResponse response, AutowireBeanFactory factory) {
		if(bean == null) {
			bean = ClassUtils.createInstance(clazz);

			factory.autowire(bean);
		}

		MethodWrapper w = methods.get(method);

		if(w != null) {
			WebResult result = w.invoke(bean, request, response, pattern.matcher(url));

			if(result != null) {
				render(result, request, response);

				return true;
			}
		}

		return false;
	}

}
