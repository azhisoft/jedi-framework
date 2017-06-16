package cn.jedisoft.jediframework.web.result;

import java.io.InputStream;

/**
 * Jedi 的调用返回
 * 
 * 使用该接口统一服务和模板调用的返回形式
 * 
 * @author azhi
 *
 */
public interface WebResult {

	public static final int TYPE_ERROR = -1;
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_JSON = 1;
	public static final int TYPE_JSONP = 2;
	public static final int TYPE_XML = 4;
	public static final int TYPE_HTML = 8;
	public static final int TYPE_STREAM = 16;
	public static final int TYPE_FORWARD = 32;
	public static final int TYPE_REDIRECT = 64;

	/**
	 * 返回值的输出类型
	 * @return
	 */
	public int getType();

	/**
	 * 实际返回值
	 * @return
	 */
	public String getBody();

	/**
	 * 作为流对象的实际返回值
	 * @return
	 */
	public InputStream getStreamBody();

}
