package cn.jedisoft.jediframework.web.result;

import java.io.InputStream;

/**
 * 默认的调用返回
 * 
 * @author azhi
 *
 */
public class DefaultResult implements WebResult {

	protected int type = WebResult.TYPE_TEXT;
	protected String body = "";
	protected InputStream stream = null;

	public DefaultResult() {
		
	}

	public DefaultResult(int type) {
		this.type = type;
	}

	public DefaultResult(String body) {
		this.body = body;
	}

	public DefaultResult(InputStream stream) {
		this.stream = stream;
	}

	public DefaultResult(int type, String body) {
		this.type = type;
		this.body = body;
	}

	public DefaultResult(int type, InputStream stream) {
		this.type = type;
		this.stream = stream;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBody(String result) {
		this.body = result;
	}

	public void setBody(InputStream stream) {
		this.stream = stream;
	}

	public int getType() {
		return type;
	}

	public String getBody() {
		return body;
	}

	public InputStream getStreamBody() {
//		return new ByteArrayInputStream(body.getBytes());
		return stream;
	}

}
