package cn.jedisoft.jediframework.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jedisoft.jediframework.core.AutowireBeanFactory;

/**
 * Api 的映射包装
 * 
 * 每个 Api 都有一个对应的映射包装类
 * 
 * @author lzm
 *
 */
public interface WebApiWrapper {

	/**
	 * Api 对应的 URI 表达式
	 * @return
	 */
	public String getRequestPath();

	/**
	 * 当前 url 是否与 Api 处理表达式匹配
	 * @param url
	 * @param method
	 * @return
	 */
	public boolean matches(String url, String method);

	/**
	 * 对当前 url 进行处理
	 * @param url
	 * @param method
	 * @param request
	 * @param response
	 * @param factory
	 * @return
	 */
	public boolean process(String url, String method, HttpServletRequest request, HttpServletResponse response, AutowireBeanFactory factory);

}
