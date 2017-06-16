package cn.jedisoft.jediframework.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * 配置信息加载工具类
 * 
 * 支持对properties文件、JSON文件，以及XML文件进行对象化，即把一个配置文件映射成一个简单实体对象
 * 
 * @author azhi
 *
 */
public class ConfigUtils {

	private static final Logger log = Logger.getLogger(ConfigUtils.class);

	public final static int TYPE_PROPERTIES = 0;
	public final static int TYPE_JSON = 1;
	public final static int TYPE_XML = 2;

	private static Gson gson = null;
	private static XmlParser xml = null;

	/**
	 * 把一个properties流映射成一个指定的实体对象
	 * @param clazz 实体对象的class
	 * @param stream 
	 * @return
	 */
	public static synchronized <T> T load(Class<T> clazz, InputStream stream) {
		return _loadFromFile(clazz, stream, TYPE_PROPERTIES);
	}

	/**
	 * 把流映射成一个指定的实体对象，文件格式由type决定
	 * @param clazz 实体对象的class
	 * @param stream
	 * @param type 
	 * @return
	 */
	public static synchronized <T> T load(Class<T> clazz, InputStream stream, int type) {
		return _loadFromFile(clazz, stream, type);
	}

	/**
	 * 把一个properties流映射到一个指定的实体对象
	 * @param entity
	 * @param stream
	 * @return
	 */
	public static synchronized <T> T load(T entity, InputStream stream) {
		return _loadFromFile(entity, stream, TYPE_PROPERTIES);
	}

	/**
	 * 把流映射到一个指定的实体对象，文件格式由type决定
	 * @param entity
	 * @param stream
	 * @param type
	 * @return
	 */
	public static synchronized <T> T load(T entity, InputStream stream, int type) {
		return _loadFromFile(entity, stream, type);
	}

	/**
	 * 把一个properties文件映射成一个指定的实体对象
	 * @param clazz
	 * @param filename
	 * @return
	 */
	public static synchronized <T> T loadFromFile(Class<T> clazz, String filename) {
		return _loadFromFile(clazz, filename, TYPE_PROPERTIES);
	}

	/**
	 * 把文件映射成一个指定的实体对象，文件格式由type决定
	 * @param clazz
	 * @param filename
	 * @param type
	 * @return
	 */
	public static synchronized <T> T loadFromFile(Class<T> clazz, String filename, int type) {
		return _loadFromFile(clazz, filename, type);
	}

	/**
	 * 把一个properties文件映射到一个指定的实体对象
	 * @param entity
	 * @param filename
	 * @return
	 */
	public static synchronized <T> T loadFromFile(T entity, String filename) {
		return _loadFromFile(entity, filename, TYPE_PROPERTIES);
	}

	/**
	 * 把文件映射到一个指定的实体对象
	 * @param entity
	 * @param filename
	 * @param type
	 * @return
	 */
	public static synchronized <T> T loadFromFile(T entity, String filename, int type) {
		return _loadFromFile(entity, filename, type);
	}

	/**
	 * 把流转换成对象
	 * @param clazz
	 * @param stream
	 * @param type
	 * @return
	 */
	protected static <T> T _loadFromFile(Class<T> clazz, InputStream stream, int type) {
		switch(type) {
		case TYPE_PROPERTIES: return loadFromProp(clazz, stream);
		case TYPE_JSON: return loadFromJson(clazz, stream);
		case TYPE_XML: return loadFromXml(clazz, stream);
		}

		return null;
	}

	/**
	 * 把流映射到对象，并覆盖对象的现有属性值
	 * @param entity
	 * @param stream
	 * @param type
	 * @return
	 */
	protected static <T> T _loadFromFile(T entity, InputStream stream, int type) {
		switch(type) {
		case TYPE_PROPERTIES: return loadFromProp(entity, stream);
		case TYPE_JSON: break;
		case TYPE_XML: break;
		}

		return null;
	}

	/**
	 * 把文件转换成对象
	 * @param clazz
	 * @param filename
	 * @param type
	 * @return
	 */
	protected static <T> T _loadFromFile(Class<T> clazz, String filename, int type) {
		switch(type) {
		case TYPE_PROPERTIES: return loadFromProp(clazz, filename);
		case TYPE_JSON: return loadFromJson(clazz, filename);
		case TYPE_XML: return loadFromXml(clazz, filename);
		}

		return null;
	}

	/**
	 * 把文件映射到对象，并覆盖对象的现有属性值
	 * @param entity
	 * @param filename
	 * @param type
	 * @return
	 */
	protected static <T> T _loadFromFile(T entity, String filename, int type) {
		switch(type) {
		case TYPE_PROPERTIES: return loadFromProp(entity, filename);
		case TYPE_JSON: break;
		case TYPE_XML: break;
		}

		return null;
	}

	/**
	 * 加载properties流，并返回实体对象
	 * @param clazz
	 * @param stream
	 * @return
	 */
	protected static <T> T loadFromProp(Class<T> clazz, InputStream stream) {
		return loadFromProp(ClassUtils.createInstance(clazz), stream);
	}

	/**
	 * 加载properties流，映射实体对象属性值
	 * @param entity
	 * @param stream
	 * @return
	 */
	protected static <T> T loadFromProp(T entity, InputStream stream) {
		if(entity != null) {
			Properties props = loadPropertils(stream);

			if(props != null) {
				Map<String, String> fieldValues = buildFieldValues(props);

				ClassUtils.setFieldValues(entity, fieldValues);
			}

			return entity;
		}

		return null;
	}

	/**
	 * 加载properties文件，并返回实体对象
	 * @param clazz
	 * @param filename
	 * @return
	 */
	protected static <T> T loadFromProp(Class<T> clazz, String filename) {
		return loadFromProp(ClassUtils.createInstance(clazz), filename);
	}

	/**
	 * 加载properties文件，映射实体对象属性值
	 * @param entity
	 * @param filename
	 * @return
	 */
	protected static <T> T loadFromProp(T entity, String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(filename));

			return loadFromProp(entity, fis);

		} catch (FileNotFoundException e) {
			log.error(String.format("loadFromPropFile(%s) failed: ", filename), e);
		}

		return null;
	}

	/**
	 * 以properties方式打开流
	 * @param stream
	 * @return
	 */
	protected static Properties loadPropertils(InputStream stream) {
		if(stream != null) {
			try {
				Properties props = new Properties();

				props.load(stream);

				return props;

			} catch (IOException e) {
				log.error("loadPropertils() failed: ", e);
			}
		}

		return null;
	}

	/**
	 * 以properties方式打开文件
	 * @param stream
	 * @return
	 */
	protected static Properties loadPropertils(String filename) {
		try {
			FileInputStream stream = new FileInputStream(new File(filename));

			return loadPropertils(stream);

		} catch (FileNotFoundException e) {
			log.error(String.format("loadPropertils(%s) failed: ", filename), e);
		}

		return null;
	}

	/**
	 * 根据properties生成属性/值列表
	 * @param props
	 * @return
	 */
	protected static Map<String, String> buildFieldValues(Properties props) {
		Map<String, String> fieldValues = new HashMap<String, String>();

		for(Object key : props.keySet()) {
			String kn = key.toString();

			fieldValues.put(StringUtils.formatMethodName(kn), props.getProperty(kn, ""));
		}

		return fieldValues;
	}

	/**
	 * 把JSON格式的文本流映射成实体对象
	 * @param clazz
	 * @param stream
	 * @return
	 */
	protected static <T> T loadFromJson(Class<T> clazz, InputStream stream) {
		if(stream != null) {
			InputStreamReader reader = new InputStreamReader(stream);

			if(gson == null)
				gson = new Gson();

			return gson.fromJson(reader, clazz);
		}

		return null;
	}

	/**
	 * 把JSON文件流映射成实体对象
	 * @param clazz
	 * @param filename
	 * @return
	 */
	protected static <T> T loadFromJson(Class<T> clazz, String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(filename));

			return loadFromJson(clazz, fis);

		} catch (FileNotFoundException e) {
			log.error(String.format("loadFromJson(%s) failed: ", filename), e);
		}

		return null;
	}

	/**
	 * 把XML格式的文本流映射成实体对象
	 * @param clazz
	 * @param stream
	 * @return
	 */
	protected static <T> T loadFromXml(Class<T> clazz, InputStream stream) {
		if(stream != null) {
			if(xml == null)
				xml = new XmlParser();
			
			return xml.fromXml(stream, clazz);
		}

		return null;
	}

	/**
	 * 把XML文件流映射成实体对象
	 * @param clazz
	 * @param filename
	 * @return
	 */
	protected static <T> T loadFromXml(Class<T> clazz, String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(filename));

			return loadFromXml(clazz, fis);

		} catch (FileNotFoundException e) {
			log.error(String.format("loadFromXml(%s) failed: ", filename), e);
		}

		return null;
	}

}
