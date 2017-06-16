package cn.jedisoft.jediframework.common;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * XML 解析类，用于对 xml 反序列化
 * 
 * @author lzm
 *
 */
public class XmlParser {

	private class XmlValue {

		public final static int TYPE_VALUE = 1;
		public final static int TYPE_NODE = 2;

		public String name = "";
		public int type = 0;
		public String value = "";
		public XmlNode node = null;

	}

	private static final Logger log = Logger.getLogger(XmlParser.class);

	/**
	 * 从流中读取 XML，并反序列化为实体对象
	 * @param stream
	 * @param clazz
	 * @return
	 */
	public <T> T fromXml(InputStream stream, Class<T> clazz) {
		T entity = ClassUtils.createInstance(clazz);
		XmlReader xml = new XmlReader();

		if(entity != null && xml.load(stream)) {
			setFieldValues(entity, xml);

			return entity;
		}

		return null;
	}

	/**
	 * 基于指定节点构建属性列表
	 * @param node
	 * @return
	 */
	protected List<XmlValue> buildNodeValues(XmlNode node) {
		List<XmlValue> values = new ArrayList<XmlValue>();
		Map<String, String> attrs = node.getAttrs();

		for(Map.Entry<String, String> a : attrs.entrySet()) {
			XmlValue v = new XmlValue();
			
			v.type = XmlValue.TYPE_VALUE;
			v.name = StringUtils.formatMethodName(a.getKey());
			v.value = a.getValue();
			
			values.add(v);
		}

		List<XmlNode> nodes = node.getNodes();
		
		for(XmlNode n : nodes) {
			XmlValue v = new XmlValue();

			v.name = StringUtils.formatMethodName(n.tagName());

			if(n.getNodes().size() > 0 || n.getAttrs().size() > 0) {
				v.type = XmlValue.TYPE_NODE;
				v.node = n;

			} else {
				v.type = XmlValue.TYPE_VALUE;
				v.value = n.getValue();
			}
			
			values.add(v);
		}

		return values;
	}

	/**
	 * 把List转换为Map，提高访问效率
	 * @param values
	 * @return
	 */
	protected Map<String, List<XmlValue>> indexNodeValues(List<XmlValue> values) {
		Map<String, List<XmlValue>> r = new HashMap<String, List<XmlValue>>();

		for(XmlValue v : values) {
			if(r.containsKey(v.name)) {
				r.get(v.name).add(v);

			} else {
				List<XmlValue> l = new ArrayList<XmlValue>();

				l.add(v);

				r.put(v.name, l);
			}
		}
		
		return r;
	}

	/**
	 * 根据XML节点，为实体设置属性值
	 * @param entity
	 * @param node
	 */
	protected <T> void setFieldValues(T entity, XmlNode node) {
		Field[] fields = entity.getClass().getDeclaredFields();
		List<XmlValue> values = buildNodeValues(node);
		Map<String, List<XmlValue>> indexVals = indexNodeValues(values);

		for(Field field : fields) {
			setFieldValue(entity, field, indexVals);
		}
	}

	/**
	 * 为实体当属性赋值
	 * @param entity
	 * @param field
	 * @param values
	 */
	protected <T> void setFieldValue(T entity, Field field, Map<String, List<XmlValue>> values) {
		String fieldName = field.getName();
		List<XmlValue> values_t = values.get(fieldName);

		if(values_t == null || (values_t != null && values_t.size() <= 0)) {
			log.warn(String.format("setFieldValue(%s): value not found.", fieldName));
			
			return;
		}

		String type = field.getGenericType().toString();
		
		if(type.startsWith("class ")) {
			type = type.substring(5).trim();
		}

		XmlValue value = values_t.get(0);

		if("java.lang.String".equals(type) ||
			"int".equals(type) || "java.lang.Integer".equals(type) ||
			"long".equals(type) || "java.lang.Long".equals(type) ||
			"float".equals(type) || "java.lang.Float".equals(type) ||
			"double".equals(type) || "java.lang.Double".equals(type) ||
			"boolean".equals(type) || "java.lang.Boolean".equals(type)) {
			if(value.type == XmlValue.TYPE_VALUE) {
				ClassUtils.setFieldValue(entity, field, value.value);

			} else {
				ClassUtils.setFieldValue(entity, field, value.node.getValue());
			}

		} else if(type.startsWith("java.util.List")) {
			Pattern p = Pattern.compile("<([\\w.]+)>");
			Matcher m = p.matcher(type);

			if (m.find()) {
				String typeT = m.group(1).trim();

				switch (value.type) {
				case XmlValue.TYPE_NODE:
					if("java.lang.String".equals(typeT)) {
						List<String> val = new ArrayList<String>();
							
						for (XmlValue v : values_t) {
							val.add(v.node.getValue());
						}
		
						ClassUtils.setFieldValue(entity, field, val);

					} else {
						try {
							Class<?> clazz = Class.forName(typeT);
							List val = new ArrayList();
							
							for (XmlValue v : values_t) {
								Object o = ClassUtils.createInstance(clazz);
		
								if (o != null && v.node != null) {
									setFieldValues(o, v.node);
		
									val.add(o);
								}
							}
		
							ClassUtils.setFieldValue(entity, field, val);

						} catch (ClassNotFoundException e) {
							log.warn(String.format("setFieldValue(%s) failed: ", fieldName), e);
						}
					}
					break;

				case XmlValue.TYPE_VALUE:
					if("java.lang.String".equals(typeT)) {
						List<String> val = new ArrayList<String>();

						for (XmlValue v : values_t) {
							val.add(v.value);
						}

						ClassUtils.setFieldValue(entity, field, val);

					} else {
						log.warn(String.format("setFieldValue(%s) not support \"%s\".", fieldName, type));
					}
					break;
				}
			}

		} else if(type.startsWith("java.util.Map")) {
			Pattern p = Pattern.compile("<([\\w.]+),([\\w.]+)>");
			Matcher m = p.matcher(type);

			if (m.find()) {
				String typeV = m.group(2).trim();

				switch (value.type) {
				case XmlValue.TYPE_NODE:
					if("java.lang.String".equals(typeV)) {
						Map val = new HashMap();
							
						for (XmlValue v : values_t) {
							val.put(v.name, v.node.getValue());
						}

						ClassUtils.setFieldValue(entity, field, val);

					} else {
						try {
							Class<?> clazz = Class.forName(typeV);
							Map val = new HashMap();
							
							for (XmlValue v : values_t) {
								Object o = ClassUtils.createInstance(clazz);
		
								if (o != null) {
									setFieldValues(o, v.node);
		
									val.put(v.name, o);
								}
							}

							ClassUtils.setFieldValue(entity, field, val);

						} catch (ClassNotFoundException e) {
							log.warn(String.format("setFieldValue(%s) failed: ", fieldName), e);
						}
					}
					break;

				case XmlValue.TYPE_VALUE:
					if("java.lang.String".equals(typeV)) {
						Map<String, String> val = new HashMap<String, String>();

						for (XmlValue v : values_t) {
							val.put(v.name, v.value);
						}

						ClassUtils.setFieldValue(entity, field, val);

					} else {
						log.warn(String.format("setFieldValue(%s) not support \"%s\".", fieldName, type));
					}
					break;
				}
			}

		} else {
			try {
				Class<?> clazz = Class.forName(type);
				Object o = ClassUtils.createInstance(clazz);

				if (o != null && value.node != null) {
					setFieldValues(o, value.node);

					ClassUtils.setFieldValue(entity, field, o);
				}

			} catch (ClassNotFoundException e) {
				log.warn(String.format("setFieldValue(%s) failed: ", fieldName), e);
			}
		}
	}

}
