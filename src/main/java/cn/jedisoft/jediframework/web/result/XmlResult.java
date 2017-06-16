package cn.jedisoft.jediframework.web.result;

/**
 * 封装 XML 格式的调用返回
 * 
 * @author azhi
 *
 */
public class XmlResult extends DefaultResult {

	public XmlResult(String xml) {
		super(WebResult.TYPE_XML, xml);
	}

}
