package cn.jedisoft.jediframework.web.common;

import java.net.URLDecoder;

/**
 * QueryStirng 解析类，主要用于解析 PUT 方法的 RequestBody
 * 
 * @author lzm
 *
 */
public class QueryStringParser {

	private final String queryString;

	private int paramBegin;
	private int paramEnd;
	private int paramNameEnd;
	private String paramName;
	private String paramValue;

	/**
	 * 构造方法
	 * @param queryString
	 */
	public QueryStringParser(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * 是否有下一个参数
	 * @return
	 */
	public boolean next() {
		int len = queryString.length();

		if (paramEnd == len) {
			return false;
		}

		paramBegin = paramEnd == 0 ? 0 : paramEnd + 1;

		int idx = queryString.indexOf('&', paramBegin);

		paramEnd = idx == -1 ? len : idx;

		idx = queryString.indexOf('=', paramBegin);

		paramNameEnd = idx == -1 || idx > paramEnd ? paramEnd : idx;
		paramName = null;
		paramValue = null;

		return true;
	}

	/**
	 * 获取当前解析到的参数名
	 * @return
	 */
	public String getName() {
		if (paramName == null) {
			paramName = queryString.substring(paramBegin, paramNameEnd);
		}

		return paramName;
	}

	/**
	 * 获取当前解析到的参数值
	 * @return
	 */
	public String getValue() {
		if (paramValue == null) {
			if (paramNameEnd == paramEnd) {
				return null;
			}

			paramValue = URLDecoder.decode(queryString.substring(paramNameEnd+1, paramEnd));
		}

		return paramValue;
	}

}
