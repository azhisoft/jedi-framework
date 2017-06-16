package cn.jedisoft.jediframework.web.common;

import java.util.Map;

/**
 * URI 匹配结果包装对象
 * 
 * @author lzm
 *
 */
public class UriMatcher {

	private Map<String, String> group = null;

	/**
	 * 生成 url 的匹配结果对象
	 * @param group
	 * @return
	 */
	public static UriMatcher create(Map<String, String> group) {
		return new UriMatcher(group);
	}

	/**
	 * 构造方法
	 * @param group
	 */
	protected UriMatcher(Map<String, String> group) {
		this.group = group;
	}

	/**
	 * 是否匹配
	 * @return
	 */
	public boolean find() {
		return (group != null && group.size() > 0);
	}

	/**
	 * 根据指定的参数名，取匹配后的值
	 * @param name
	 * @return
	 */
	public String group(String name) {
		if(group != null && group.containsKey(name))
			return group.get(name);
		
		return "";
	}

}
