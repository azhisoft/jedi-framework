package cn.jedisoft.jediframework.core.conf;

/**
 * 配置文件的自动注入参数定义
 * 
 * @author lzm
 *
 */
public class RenderDefinition {

	public final static String USING_FREEMARKER ="freemarker";
	public final static String USING_JSP ="jsp";

	private String using = USING_FREEMARKER;

	public String getUsing() {
		return using;
	}

	public void setUsing(String using) {
		this.using = using;
	}

}
