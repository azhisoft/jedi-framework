package cn.jedisoft.jediframework.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml 节点辅助类
 * 
 * @author lzm
 *
 */
public class XmlNode {

	private static final Logger log = Logger.getLogger(XmlNode.class);

    protected Element rootElement = null;

    public XmlNode(Element rootElement) {
    	this.rootElement = rootElement;
    }

    /**
     * 获取节点名
     * @return
     */
	public String tagName() {
		if(rootElement != null) {
			return rootElement.getTagName();
		}

		return "";
	}

	/**
	 * 获取指定的节点
	 * @param path
	 * @return
	 */
	public XmlNode getNode(String path) {
		Element element = getElem(rootElement, path);

		if(element != null)
			return new XmlNode(element);

		return null;
	}

	/**
	 * 获取当前节点的所有子节点
	 * @return
	 */
	public List<XmlNode> getNodes() {
		List<XmlNode> r = new ArrayList<XmlNode>();
		Element element = rootElement;

		if(element != null) {
			NodeList nodes = element.getChildNodes();
			
			if(nodes != null && nodes.getLength() > 0) {
				for(int i = 0, c = nodes.getLength(); i < c; i ++) {
					Node node = nodes.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {
						r.add(new XmlNode((Element)node));
					}
				}
			}
		}

		return r;
	}

	/**
	 * 获取指定节点的子节点
	 * @param path
	 * @return
	 */
	public List<XmlNode> getNodes(String path) {
		List<XmlNode> r = new ArrayList<XmlNode>();
		Element element = getElem(rootElement, path);

		if(element != null) {
			NodeList nodes = element.getChildNodes();
			
			if(nodes != null && nodes.getLength() > 0) {
				for(int i = 0, c = nodes.getLength(); i < c; i ++) {
					Node node = nodes.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {
						r.add(new XmlNode((Element)node));
					}
				}
			}
		}

		return r;
	}

	/**
	 * 获取节点内容
	 * @return
	 */
	public String getValue() {
		return getValue(rootElement);
	}

	public int getIntVal() {
		String value = getValue();

		return StringUtils.intVal(value, -1);
	}

	public long getLongVal() {
		String value = getValue();

		return StringUtils.longVal(value, -1);
	}

	public boolean getBoolVal() {
		String value = getValue();

		return StringUtils.boolVal(value, false);
	}

	/**
	 * 获取指定节点内容
	 * @param path
	 * @return
	 */
	public String getValue(String path) {
		return getValue(getElem(rootElement, path));
	}

	public int getIntVal(String path) {
		String value = getValue(path);

		return StringUtils.intVal(value, -1);
	}

	public long getLongVal(String path) {
		String value = getValue(path);

		return StringUtils.longVal(value, -1);
	}

	public boolean getBoolVal(String path) {
		String value = getValue(path);

		return StringUtils.boolVal(value, false);
	}

	/**
	 * 获取节点属性
	 * @param attrName
	 * @return
	 */
	public String getAttr(String attrName) {
		return getAttr(rootElement, attrName);
	}

	public int getIntAttr(String attrName) {
		String value = getAttr(attrName);

		return StringUtils.intVal(value, -1);
	}

	public long getLongAttr(String attrName) {
		String value = getAttr(attrName);

		return StringUtils.longVal(value, -1);
	}

	public boolean getBoolAttr(String attrName) {
		String value = getAttr(attrName);

		return StringUtils.boolVal(value, false);
	}

	/**
	 * 获取指定节点属性
	 * @param path
	 * @param attrName
	 * @return
	 */
	public String getAttr(String path, String attrName) {
		return getAttr(getElem(rootElement, path), attrName);
	}

	public int getIntAttr(String path, String attrName) {
		String value = getAttr(path, attrName);

		return StringUtils.intVal(value, -1);
	}

	public long getLongAttr(String path, String attrName) {
		String value = getAttr(path, attrName);

		return StringUtils.longVal(value, -1);
	}

	public boolean getBoolAttr(String path, String attrName) {
		String value = getAttr(path, attrName);

		return StringUtils.boolVal(value, false);
	}

	/**
	 * 获取节点所有属性
	 * @return
	 */
	public Map<String, String> getAttrs() {
		return getAttrs(rootElement);
	}

	/**
	 * 获取指定节点所有属性
	 * @param path
	 * @return
	 */
	public Map<String, String> getAttrs(String path) {
		return getAttrs(getElem(rootElement, path));
	}

	protected Map<String, String> getAttrs(Element elem) {
		Map<String, String> r = new HashMap<String, String>();

		if (elem != null) {
			NamedNodeMap attrs = elem.getAttributes();

	        if (attrs != null) {
	        	for(int i = 0; i < attrs.getLength(); i ++) {
	        		Node node = attrs.item(i);

	        		String name = node.getNodeName();
	        		String val = node.getNodeValue();
	        		
	        		r.put(name, val);
	        	}
	        }
        }

        return r;
	}

	/**
	 * 获取指定节点
	 * @param rootElement
	 * @param path
	 * @return
	 */
	protected Element getElem(Element rootElement, String path) {
		if(path == null || "".equals(path) || ".".equals(path)) {
			return rootElement;
		}

		int pos = path.indexOf(".");
		boolean hasNext = (pos != -1);
		String tagName = hasNext ? path.substring(0, pos) : path;

		if(rootElement != null) {
			NodeList elems = rootElement.getElementsByTagName(tagName);

			if (elems != null && elems.getLength() > 0) {
				if (hasNext) {
					return getElem((Element) elems.item(0), path.substring(pos + 1));

				} else {
					return (Element) elems.item(0);
				}
			}
		}

		return null;
	}

	/**
	 * 获取节点内容
	 * @param node
	 * @return
	 */
	protected String getValue(Node node) {
        if (node == null)
        	return null;

        NodeList children = node.getChildNodes();

        if (children != null) {
			Node child = children.item(0);

			if (child == null)
				return null;

    		int nodeType = child.getNodeType();

    		if(nodeType == Node.TEXT_NODE ||
   				nodeType == Node.CDATA_SECTION_NODE) {
				String value = child.getNodeValue();

				return value.trim();
			}
        }

        return null;
	}

	/**
	 * 获取节点属性
	 * @param node
	 * @param attrName
	 * @return
	 */
	protected String getAttr(Node node, String attrName) {
        if (node == null)
        	return null;

        NamedNodeMap attrs = node.getAttributes();

        if (attrs == null)
        	return null;

        Node attr = attrs.getNamedItem(attrName);

        if (attr == null)
        	return null;

        String value = attr.getNodeValue();

        if (value == null)
        	return null;

        return value.trim();
	}

}
