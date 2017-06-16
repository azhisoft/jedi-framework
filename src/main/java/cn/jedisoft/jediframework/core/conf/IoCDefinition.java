package cn.jedisoft.jediframework.core.conf;

/**
 * 配置文件的自动注入参数定义
 * 
 * @author lzm
 *
 */
public class IoCDefinition {

	public final static String USING_SPRING ="spring";
	public final static String USING_SELF ="self";
	
	private String using = USING_SELF;

	public String getUsing() {
		return using;
	}

	public void setUsing(String using) {
		this.using = using;
	}

}
