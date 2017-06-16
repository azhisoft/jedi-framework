package cn.jedisoft.jediframework.web.result;

import com.google.gson.Gson;

/**
 * 封装 JSONP 格式的调用返回
 * 
 * @author azhi
 *
 */
public class JsonpResult extends DefaultResult {

	public JsonpResult(String callback, String jsonp) {
		super(WebResult.TYPE_JSONP, String.format("%s(%s)", callback, jsonp));
	}

	public JsonpResult(String callback, Object jsonp) {
		super(WebResult.TYPE_JSONP, String.format("%s(%s)", callback, new Gson().toJson(jsonp)));
	}

	public JsonpResult(String jsonp) {
		super(WebResult.TYPE_JSONP, jsonp);
	}

}
