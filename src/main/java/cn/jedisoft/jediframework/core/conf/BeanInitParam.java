package cn.jedisoft.jediframework.core.conf;

/**
 * 配置文件的 bean 初始化参数定义
 * 
 * @author lzm
 *
 */
public class BeanInitParam {

	private String paramName = "";
	private String paramValue = "";

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

}
