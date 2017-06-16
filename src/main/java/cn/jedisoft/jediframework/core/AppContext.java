package cn.jedisoft.jediframework.core;

/**
 * AppContext，应用上下文
 * 
 * @author lzm
 *
 */
public interface AppContext extends BeanFactory {

	public static final String JEDI_JSON_CONF = "jedi-framework.json";
	public static final String JEDI_XML_CONF = "jedi-framework.xml";

	/**
	 * 注销对象
	 */
	public void destroy();

}
