package cn.jedisoft.jediframework.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/**
 * XML 读取辅助类
 * 
 * @author lzm
 *
 */
public class XmlReader extends XmlNode {

	private static final Logger log = Logger.getLogger(XmlReader.class);

	private DocumentBuilderFactory factory = null;
    private DocumentBuilder builder = null;
    private Document doc = null;

    public XmlReader() {
    	super(null);
    }

    /**
     * 准备解析XML必须的类
     */
    private void prepare() {
    	if(builder == null) {
	        factory = DocumentBuilderFactory.newInstance();
	
	        factory.setValidating(false);
	        factory.setNamespaceAware(true);
	        factory.setIgnoringComments(true);
	        factory.setIgnoringElementContentWhitespace(true);
	
	        try {
	        	builder = factory.newDocumentBuilder();
	
	        } catch (ParserConfigurationException e) {
	            log.error("prepare() failed: ", e);
	        }
    	}
    }

    /**
     * 从文件加载XML
     * @param filename
     * @return
     */
    public boolean load(String filename) {
    	prepare();

        try {
        	doc = builder.parse(new File(filename));

        	rootElement = doc.getDocumentElement();

        } catch (SAXParseException e) {
			log.error(String.format("load(%s) failed(line: %d): ", filename, e.getLineNumber()), e);

        } catch (Exception e) {
			log.error(String.format("load(%s) failed: ", filename), e);
        }

        return (doc != null);
	}

    /**
     * 从流中加载 XML
     * @param stream
     * @return
     */
    public boolean load(InputStream stream) {
    	return load(stream, "utf-8");
    }

    /**
     * 指定编码，从流中加载XML
     * @param stream
     * @param encoding
     * @return
     */
    public boolean load(InputStream stream, String encoding) {
    	prepare();

    	try {
        	doc = builder.parse(stream, encoding);

        	rootElement = doc.getDocumentElement();

        } catch (SAXParseException e) {
			log.error(String.format("load failed(line: %d): ", e.getLineNumber()), e);

        } catch (Exception e) {
			log.error("load() failed: ", e);
        }

        return (doc != null);
    }

    /**
     * 从字符串加载XML
     * @param xml
     * @return
     */
    public boolean loadXml(String xml) {
    	return loadXml(xml, "utf-8");
    }

    /**
     * 指定编码，从字符串加载XML
     * @param xml
     * @param encoding
     * @return
     */
    public boolean loadXml(String xml, String encoding) {
    	try {
			return load(IOUtils.toInputStream(xml, encoding), encoding);

    	} catch (IOException e) {
			log.error("loadXml() failed: ", e);
		}

    	return false;
    }

}
