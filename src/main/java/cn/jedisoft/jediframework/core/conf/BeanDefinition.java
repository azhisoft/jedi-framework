package cn.jedisoft.jediframework.core.conf;

import java.util.List;

/**
 * 配置文件的 bean 参数定义
 * @author lzm
 *
 */
public class BeanDefinition {

	private String name = "";
	private String className = "";
	private String scope = "singleton";
	private List<BeanInitParam> initParam = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<BeanInitParam> getInitParam() {
		return initParam;
	}

	public void setInitParam(List<BeanInitParam> initParam) {
		this.initParam = initParam;
	}

}
