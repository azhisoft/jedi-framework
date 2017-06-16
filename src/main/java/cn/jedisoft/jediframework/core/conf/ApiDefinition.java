package cn.jedisoft.jediframework.core.conf;

/**
 * 配置文件的 Api 参数定义
 * 
 * @author lzm
 *
 */
public class ApiDefinition {

	private String name = "";
	private String path = "";
	private String beanName = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

}
