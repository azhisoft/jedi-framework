package cn.jedisoft.jediframework.web.result;

import java.io.InputStream;

/**
 * 封装流格式的调用返回
 * 
 * @author azhi
 *
 */
public class StreamResult extends DefaultResult {

	public StreamResult(InputStream stream) {
		super(WebResult.TYPE_STREAM, stream);
	}

}
