package cn.jedisoft.jediframework.core.conf;

/**
 * 配置文件的数据源参数定义
 * 
 * @author lzm
 *
 */
public class DsDefinition {

	public final static String MODE_READ_ONLY ="read";
	public final static String MODE_READ_WRITE ="write";
	
	private String mode = MODE_READ_WRITE;
	private boolean lookup = false;
	private String driver = "";
	private String url = "";
	private String username = "";
	private String password = "";

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean getLookup() {
		return lookup;
	}

	public void setLookup(boolean lookup) {
		this.lookup = lookup;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
