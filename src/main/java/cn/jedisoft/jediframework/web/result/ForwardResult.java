package cn.jedisoft.jediframework.web.result;

/**
 * 封装 Forward 处理
 * 
 * @author azhi
 *
 */
public class ForwardResult extends DefaultResult {

	public ForwardResult(String url) {
		super(WebResult.TYPE_FORWARD, url);
	}

}
