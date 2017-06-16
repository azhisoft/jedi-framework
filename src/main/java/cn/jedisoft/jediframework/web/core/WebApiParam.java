package cn.jedisoft.jediframework.web.core;

/**
 * 用于描述 Api 的参数信息，包括参数的来源，类型，以及参数名等
 * 
 * @author lzm
 *
 */
public class WebApiParam {

	public static final int SCOPE_ANY = 0;
	public static final int SCOPE_ATTR = 1;
	public static final int SCOPE_BODY = 2;
	public static final int SCOPE_COOKIE = 3;
	public static final int SCOPE_FILE = 4;
	public static final int SCOPE_FORM = 5;
	public static final int SCOPE_HEADER = 6;
	public static final int SCOPE_PATH = 7;
	public static final int SCOPE_QUERY = 8;
	public static final int SCOPE_SESSION = 9;
	
	private Class<?> clazz = null;
	private String name = "";
	private int annoScope = SCOPE_ANY;
	private String annoValue = "";

	public WebApiParam() {
		
	}

	public WebApiParam(Class<?> clazz) {
		this.clazz = clazz;
	}

	public WebApiParam(int annoScope, String annoValue) {
		this.annoScope = annoScope;
		this.annoValue = annoValue;
	}

	public WebApiParam(Class<?> clazz, int annoScope, String annoValue) {
		this.clazz = clazz;
		this.annoScope = annoScope;
		this.annoValue = annoValue;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAnnoScope() {
		return annoScope;
	}

	public void setAnnoScope(int annoScope) {
		this.annoScope = annoScope;
	}

	public String getAnnoValue() {
		return annoValue;
	}

	public void setAnnoValue(String annoValue) {
		this.annoValue = annoValue;
	}

}
