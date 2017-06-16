package cn.jedisoft.jediframework.web.result;

/**
 * 封装HTML格式的调用返回
 * 
 * @author azhi
 *
 */
public class HtmlResult extends DefaultResult {

	public HtmlResult() {
		super(WebResult.TYPE_HTML);
	}

	public HtmlResult(String html) {
		super(WebResult.TYPE_HTML, html);
	}

}
