package cn.jedisoft.jediframework.common;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

/**
 * 类操作相关工具类
 * 
 * @author azhi
 *
 */
public class ClassUtils {

	private static final Logger log = Logger.getLogger(ClassUtils.class);

	/**
	 * 根据class创建实体类
	 * @param clazz
	 * @return
	 */
	public static <T> T createInstance(Class<T> clazz) {
		try {
			if(clazz != null) {
				String clazzName = clazz.getName().trim();

				if("java.lang.Integer".equals(clazzName)) {
					return (T)Integer.valueOf(0);
				}
				
				return clazz.newInstance();
			}

		} catch (InstantiationException e) {
			log.error(String.format("createInstance(%s) failed: ", clazz.getName()), e);

		} catch (IllegalAccessException e) {
			log.error(String.format("createInstance(%s) failed: ", clazz.getName()), e);
		}

		return null;
	}

	/**
	 * 获取实体对象的所有属性，及其字符串值
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, String> getFieldValues(T entity) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		Field[] fields = entity.getClass().getDeclaredFields();

		for(Field field : fields) {
			String fieldName = field.getName();
			String fieldValue = getFieldValue(entity, field);

			fieldValues.put(fieldName, fieldValue);
		}

		return fieldValues;
	}

	/**
	 * 获取实体对象的指定属性及值
	 * @param entity
	 * @param field
	 * @return
	 */
	protected static <T> String getFieldValue(T entity, Field field) {
		field.setAccessible(true);

		try {
			String type = field.getGenericType().toString();

			if(type.startsWith("class ")) {
				type = type.substring(5).trim();
			}

			if("java.lang.String".equals(type)) {
				return (String)field.get(entity);

			} else if("int".equals(type) || "java.lang.Integer".equals(type)) {
				return String.format("%d", field.getInt(entity));

			} else if("long".equals(type) || "java.lang.Long".equals(type)) {
				return String.format("%d", field.getLong(entity));

			} else if("float".equals(type) || "java.lang.Float".equals(type)) {
				return String.format("%.02f", field.getFloat(entity));

			} else if("double".equals(type) || "java.lang.Double".equals(type)) {
				return String.format("%.02f", field.getDouble(entity));

			} else if("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
				return field.getBoolean(entity) ? "true" : "false";

			} else {
				log.warn(String.format("getFieldValue(%s) not support \"%s\".", field.getName(), type));
			}

		} catch (IllegalArgumentException e) {
			log.error(String.format("getFieldValue(%s) failed: ", field.getName()), e);

		} catch (IllegalAccessException e) {
			log.error(String.format("getFieldValue(%s) failed: ", field.getName()), e);
		}

		return "";
	}

	/**
	 * 通过反射，把属性/值列表映射到实体对象
	 * @param entity
	 * @param fieldValues
	 */
	public static <T> void setFieldValues(T entity, Map<String, String> fieldValues) {
		Field[] fields = entity.getClass().getDeclaredFields();

		for(Field field : fields) {
			String fieldName = field.getName();

			if(fieldValues.containsKey(fieldName)) {
				String value = fieldValues.get(fieldName);

				if(!setFieldValue(entity, field, value)) {
					setFieldValue(entity, fieldName, value);
				}
			}
		}
	}

	/**
	 * 通过反射，为实体对象的属性赋值
	 * @param entity
	 * @param field
	 * @param value
	 */
	protected static <T> boolean setFieldValue(T entity, Field field, String value) {
		field.setAccessible(true);

		try {
			String type = field.getGenericType().toString();
			
			if(type.startsWith("class ")) {
				type = type.substring(5).trim();
			}

			if("java.lang.String".equals(type)) {
				field.set(entity, value);

			} else if("int".equals(type) || "java.lang.Integer".equals(type)) {
				field.set(entity, StringUtils.intVal(value));

			} else if("long".equals(type) || "java.lang.Long".equals(type)) {
				field.set(entity, StringUtils.longVal(value));

			} else if("float".equals(type) || "java.lang.Float".equals(type)) {
				field.set(entity, StringUtils.floatVal(value));

			} else if("double".equals(type) || "java.lang.Double".equals(type)) {
				field.set(entity, StringUtils.dblVal(value));

			} else if("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
				field.set(entity, StringUtils.boolVal(value));

			} else {
				log.warn(String.format("setFieldValue(%s, %s) not support \"%s\".", field.getName(), value, type));
			}

			return true;

		} catch (IllegalArgumentException e) {
			log.error(String.format("setFieldValue(%s, %s) failed: ", field.getName(), value), e);

		} catch (IllegalAccessException e) {
			log.error(String.format("setFieldValue(%s, %s) failed: ", field.getName(), value), e);
		}

		return false;
	}

	/**
	 * 通过反射，为实体对象的属性赋值
	 * @param entity
	 * @param field
	 * @param value
	 */
	public static <T, TV> boolean setFieldValue(T entity, Field field, TV value) {
		field.setAccessible(true);

		try {
			field.set(entity, value);

			return true;

		} catch (IllegalArgumentException e) {
			log.error(String.format("setFieldValue(%s, %s) failed: ", field.getName(), value), e);

		} catch (IllegalAccessException e) {
			log.error(String.format("setFieldValue(%s, %s) failed: ", field.getName(), value), e);
		}

		return false;
	}

	/**
	 * 通过反射，调用setter为实体对象的属性赋值
	 * @param entity
	 * @param field
	 * @param value
	 */
	protected static <T> boolean setFieldValue(T entity, String fieldName, String value) {
		String methodName = StringUtils.formatMethodName(String.format("set %s",fieldName));
		Method[] methods = entity.getClass().getDeclaredMethods();

		for(Method method : methods) {
			if(methodName.equals(method.getName())) {
				Class<?>[] ts = method.getParameterTypes();

				if(ts.length == 1) {
					List<Object> os = new ArrayList<Object>();
					String type = ts[0].getName();

					if("java.lang.String".equals(type)) {
						os.add(value);

					} else if("int".equals(type) || "java.lang.Integer".equals(type)) {
						os.add(StringUtils.intVal(value));

					} else if("long".equals(type) || "java.lang.Long".equals(type)) {
						os.add(StringUtils.longVal(value));

					} else if("float".equals(type) || "java.lang.Float".equals(type)) {
						os.add(StringUtils.floatVal(value));

					} else if("double".equals(type) || "java.lang.Double".equals(type)) {
						os.add(StringUtils.dblVal(value));

					} else if("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
						os.add(StringUtils.boolVal(value));

					} else {
						log.warn(String.format("setFieldValue(%s, %s) not support \"%s\".", methodName, value, type));
					}

					try {
						if(os.size() > 0) {
							method.invoke(entity, os.toArray());

							return true;
						}

					} catch (IllegalAccessException e) {
						log.error(String.format("setFieldValue(%s, %s) failed: ", methodName, value), e);

					} catch (IllegalArgumentException e) {
						log.error(String.format("setFieldValue(%s, %s) failed: ", methodName, value), e);

					} catch (InvocationTargetException e) {
						log.error(String.format("setFieldValue(%s, %s) failed: ", methodName, value), e.getTargetException());
					}
				}
			}
		}

		return false;
	}

    /**
	 * 通过包名进行扫描
	 * @param packName
	 * @return
	 */
	public static Set<Class<?>> scan(String packagePath) {
		return scan(packagePath, true);
	}

	/**
	 * 通过包名进行扫描，并指定是否开启深度扫描（即是否扫描子目录）
	 * @param packName
	 * @param recursive
	 * @return
	 */
	public static Set<Class<?>> scan(String packagePath, boolean recursive) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

		try {
			String packageName = packagePath;
			String localPath = packageName.replace('.', '/');

			Enumeration<URL> packages = Thread.currentThread().getContextClassLoader().getResources(localPath);

			while (packages.hasMoreElements()) {
				URL pack = packages.nextElement();
				String protocol = pack.getProtocol();

				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(pack.getFile(), "utf-8");

					scanInPath(packageName, filePath, recursive, classes);

				} else if ("jar".equals(protocol)) {
					scanInJar(packageName, pack, recursive, classes);
				}
			}

		} catch (IOException e) {
			log.error(String.format("scan(%s) failed: ", packagePath), e);
		}

		return classes;
	}

	/**
	 * 通过包名在文件系统扫描
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private static void scanInPath(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
		File path = new File(packagePath);

		if (!path.exists() || !path.isDirectory()) {
			log.warn(String.format("scanInPath(%s, %s) failed: Invalid classpath.", packageName, packagePath));

			return;
		}

		File[] clazzes = path.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});

		for (File clazz : clazzes) {
			if (clazz.isDirectory()) {
				scanInPath(String.format("%s.%s", packageName, clazz.getName()), clazz.getAbsolutePath(), recursive, classes);

			} else {
				String className = clazz.getName().substring(0, clazz.getName().length() - 6);

				try {
					classes.add(Class.forName(String.format("%s.%s", packageName, className)));

				} catch (ClassNotFoundException e) {
					log.warn(String.format("scanInPath(%s, %s) failed: ", packageName, packagePath), e);
				}
			}
		}
	}

	/**
	 * 通过包名，在jar包中进行扫描
	 * @param packageName
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private static void scanInJar(String packageName, URL packagePath, final boolean recursive, Set<Class<?>> classes) {
		String packageBase = packageName.replace('.', '/');

		try {
			JarFile jar = ((JarURLConnection) packagePath.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();

				if (name.charAt(0) == '/') {
					name = name.substring(1);
				}

				if (name.startsWith(packageBase)) {
					int idx = name.lastIndexOf('/');

					if (idx != -1) {
						packageName = name.substring(0, idx).replace('/', '.');
					}

					if ((idx != -1) || recursive) {
						if (name.endsWith(".class") && !entry.isDirectory()) {
							String className = name.substring(packageName.length() + 1, name.length() - 6);

							try {
								classes.add(Class.forName(String.format("%s.%s", packageName, className)));

							} catch (ClassNotFoundException e) {
								log.warn(String.format("scanInJar(%s, %s) failed: ", packageName, packagePath), e);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			log.error(String.format("scanInJar(%s) failed: ", packageName), e);
		}
	}

}
