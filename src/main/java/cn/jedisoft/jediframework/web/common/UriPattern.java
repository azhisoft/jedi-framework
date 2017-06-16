package cn.jedisoft.jediframework.web.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URI 匹配类，用于对 url 进行匹配，并提取参数
 * 
 * @author lzm
 *
 */
public class UriPattern {

	private static final Pattern NAME_PATTERN = Pattern.compile("[{]([^/()]+?)[}]");
	
	private Pattern pattern = null;
	private List<String> names = null;

	/**
	 * 根据表达式生成一个 Pattern 对象
	 * @param expr
	 * @return
	 */
	public static UriPattern compile(String expr) {
		Pattern pattern = Pattern.compile(buildRegex(expr));

		if(pattern != null) {
			return new UriPattern(pattern, getNames(expr));
		}

		return null;
	}

	/**
	 * 构造方法
	 * @param pattern
	 * @param names
	 */
	protected UriPattern(Pattern pattern, List<String> names) {
		this.pattern = pattern;
		this.names = names;
	}

	/**
	 * 检测 url 是否匹配
	 * @param url
	 * @return
	 */
	public boolean matches(String url) {
		Matcher matcher = pattern.matcher(url);

		return matcher.matches();
	}

	/**
	 * 对 url 进行匹配，并返回 UriMatcher 对象
	 * @param url
	 * @return
	 */
	public UriMatcher matcher(String url) {
		Matcher matcher = pattern.matcher(url);

		if(matcher.matches()) {
			Map<String, String> group = new LinkedHashMap<String, String>();
			int count = matcher.groupCount();

			for(int i = 0; i < count; i++) {
				group.put(names.get(i), matcher.group(i + 1));
			}

			return UriMatcher.create(group);
		}

		return null;
	}

	/**
	 * 从表达式中提取参数名
	 * @param pattern
	 * @return
	 */
	protected static List<String> getNames(String pattern) {
		List<String> list = new ArrayList<String>();
		Matcher matcher = NAME_PATTERN.matcher(pattern);

		while(matcher.find()) {
			list.add(matcher.group(1));
		}

		return list;
	}

	/**
	 * 根据表达式生成最终的正在表达式
	 * @param pattern
	 * @return
	 */
	protected static String buildRegex(String pattern) {
		String regex = "\\Q" + pattern.replace("{", "\\E{").replace("}", "}\\Q") + "\\E";
		String end = "/*\\E";

		if(regex.endsWith(end)) {
			regex = regex.substring(0, regex.length() - end.length()) + "\\E.*";
		}

		regex = "^" + regex.replace("\\Q\\E", "") + "$";

		Matcher matcher = NAME_PATTERN.matcher(regex);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(sb, Matcher.quoteReplacement("([^/]+)"));
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

}
