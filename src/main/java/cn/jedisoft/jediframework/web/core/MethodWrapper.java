package cn.jedisoft.jediframework.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jedisoft.jediframework.web.common.UriMatcher;
import cn.jedisoft.jediframework.web.result.WebResult;

/**
 * Api 的方法映射包装
 * 
 * 结构上看，该包装属于 ApiWrapper 的子级类
 * 每个方法映射包装，对应 HTTP 的 4 个请求方法的其中一个，即：每个 ApiWrapper 包含最多 4 个 MethodWrapper
 * 
 * @author lzm
 *
 */
public interface MethodWrapper {

	/**
	 * 该对象需要处理的 HTTP 方法，即：GET、PUT、POST、DELETE 中的其中一个
	 * 
	 * @return
	 */
	public String getRequestMethod();

	/**
	 * 调用目标对象上的映射方法，完成对 HTTP 请求的处理
	 * @param bean
	 * @param request
	 * @param response
	 * @param m
	 * @return
	 */
	public WebResult invoke(Object bean, HttpServletRequest request, HttpServletResponse response, UriMatcher m);

}
