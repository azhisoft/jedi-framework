package cn.jedisoft.jediframework.web.result;

import com.google.gson.Gson;

/**
 * 封装 JSON 格式的调用返回
 * 
 * @author azhi
 *
 */
public class JsonResult extends DefaultResult {

	public JsonResult(String json) {
		super(WebResult.TYPE_JSON, json);
	}

	public JsonResult(Object json) {
		super(WebResult.TYPE_JSON, new Gson().toJson(json));
	}

}
