package cn.jedisoft.jediframework.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 字符串工具类
 * 
 * @author azhi
 *
 */
public class StringUtils {

	private static final Logger log = Logger.getLogger(StringUtils.class);

	/**
	 * 判断字符串是否为空(包括null)
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if(str != null) {
			return "".equals(str.trim());
		}

		return true;
	}

	/**
	 * 判断字符串是否非空(包括null)
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 判断字符串是否是URL
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		if(isNotBlank(url)) {
			String t = url.trim().toLowerCase();

			return (t.startsWith("http://") || t.startsWith("https://"));
		}
		
		return false;
	}

	/**
	 * 把byte数组转换成十六进制的字符串
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {
		StringBuilder str = new StringBuilder();

		for(int i = 0; i < b.length; i ++) {
			String hex = Integer.toHexString(b[i] & 0xFF);

			if(hex.length() == 1) {
				str.append("0");
		    }

			str.append(hex);
		  } 

		return str.toString();
	}

	/**
	 * 将字符串转换成布尔型
	 * @param bool
	 * @return
	 */
	public static boolean boolVal(String bool) {
		return boolVal(bool, false);
	}

	/**
	 * 将字符串转换成布尔型，转换失败时，返回给定的默认值
	 * @param bool
	 * @param def
	 * @return
	 */
	public static boolean boolVal(String boolVal, boolean defVal) {
		if(isNotBlank(boolVal)) {
			String t = boolVal.trim();

			return ("checked".equalsIgnoreCase(t) ||
					"enabled".equalsIgnoreCase(t) ||
					"enable".equalsIgnoreCase(t) ||
					"true".equalsIgnoreCase(t) ||
					"yes".equalsIgnoreCase(t) ||
					"on".equalsIgnoreCase(t) ||
					"1".equalsIgnoreCase(t));
		}

		return defVal;
	}

	/**
	 * 将字符串转换成数字
	 * @param num
	 * @return
	 */
	public static int intVal(String intVal) {
		return intVal(intVal, 0);
	}

	/**
	 * 将字符串转换成数字，转换失败时，返回给定的默认值
	 * @param num
	 * @param def
	 * @return
	 */
	public static int intVal(String intVal, int defVal) {
		try {
			if(isNotBlank(intVal)) {
				return Integer.valueOf(intVal.trim());
			}

		} catch(NumberFormatException e) {
			log.error(String.format("intVal(%s) failed: ", intVal), e);
		}

		return defVal;
	}

	/**
	 * 将字符串转换成数字
	 * @param num
	 * @return
	 */
	public static long longVal(String longVal) {
		return longVal(longVal, 0);
	}

	/**
	 * 将字符串转换成数字，转换失败时，返回给定的默认值
	 * @param num
	 * @param def
	 * @return
	 */
	public static long longVal(String longVal, long defVal) {
		try {
			if(isNotBlank(longVal)) {
				return Long.valueOf(longVal.trim());
			}

		} catch(NumberFormatException e) {
			log.error(String.format("longVal(%s) failed: ", longVal), e);
		}

		return defVal;
	}

	/**
	 * 将字符串转换成数字
	 * @param num
	 * @return
	 */
	public static float floatVal(String floatVal) {
		return floatVal(floatVal, 0.00f);
	}

	/**
	 * 将字符串转换成数字，转换失败时，返回给定的默认值
	 * @param num
	 * @param def
	 * @return
	 */
	public static float floatVal(String floatVal, float defVal) {
		try {
			if(isNotBlank(floatVal)) {
				BigDecimal db = new BigDecimal(floatVal.trim());

				return db.floatValue();
			}

		} catch(NumberFormatException e) {
			log.error(String.format("floatVal(%s) failed: ", floatVal), e);
		}

		return defVal;
	}

	/**
	 * 将字符串转换成数字
	 * @param num
	 * @return
	 */
	public static double dblVal(String dblVal) {
		return dblVal(dblVal, 0.00);
	}

	/**
	 * 将字符串转换成数字，转换失败时，返回给定的默认值
	 * @param num
	 * @param def
	 * @return
	 */
	public static double dblVal(String dblVal, double defVal) {
		try {
			if(isNotBlank(dblVal)) {
				BigDecimal db = new BigDecimal(dblVal.trim());

				return db.doubleValue();
			}

		} catch(NumberFormatException e) {
			log.error(String.format("dblVal(%s) failed: ", dblVal), e);
		}

		return defVal;
	}

	/**
	 * 将字符串转换成日期
	 * @param date
	 * @return
	 */
	public static Date dateVal(String dateVal, String dateFromat) {
		return dateVal(dateVal, dateFromat, new Date());
	}

	/**
	 * 将字符串转换成日期
	 * @param date
	 * @return
	 */
	public static Date dateVal(String dateVal, String dateFromat, Date defVal) {
        SimpleDateFormat df = new SimpleDateFormat(dateFromat.trim());

        try {
        	return df.parse(dateVal.trim());

        } catch (ParseException e) {
			log.error(String.format("dateVal(%s, %s) failed: ", dateVal, dateFromat), e);
		}

        return defVal;
	}

	/**
	 * 将日期转换成字符串
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String valueOf(Date date, String dateFromat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFromat);

        return df.format(date);
	}

	/**
	 * 格式化方法/属性名
	 * 以"."、"_"和"-"等三个符号作为分隔符号，格式化成java的驼峰式命名
	 * @param name
	 * @return
	 */
	public static String formatMethodName(String name) {
		StringBuffer fieldName = new StringBuffer();
		String[] names = name.split("[\\._-]+");

		fieldName.append(names[0].trim());

		for(int i = 1; i < names.length; i ++) {
			String n = names[i].trim();

			if(n.length() > 0) {
				fieldName.append(n.substring(0, 1).toUpperCase());
				fieldName.append(n.substring(1));
			}
		}

		return fieldName.toString();
	}

	/**
	 * 格式化 Bean 名字
	 * @param className
	 * @return
	 */
	public static String formatBeanName(String className) {
		int n = className.indexOf("<");

		if(n != -1) {
			className = className.substring(0, n);
		}

		n = className.lastIndexOf(".");

		if(n != -1) {
			String beanName = className.substring(n + 1);

			return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
		}

		return className;
	}

	/**
	 * 把字符串解析成List<Long>
	 * @param str
	 * @return
	 */
	public static List<Long> listVal(String listVal) {
		return listVal(listVal, ",");
	}

	/**
	 * 把字符串根据指定的分隔符号分割，并解析成List<Long>
	 * @param str
	 * @param token
	 * @return
	 */
	public static List<Long> listVal(String listVal, String token) {
		List<Long> r = new ArrayList<Long>();

		if(isNotBlank(listVal)) {
			String[] ids = listVal.split(token);

			if(ids != null) {
				for(String id : ids) {
					r.add(longVal(id, 0));
				}
			}
		}

		return r;
	}

	public static <T> T[] toArray(String str, String token) {
		List<T> r = new ArrayList<T>();

		if(isNotBlank(str)) {
			String[] ids = str.split(token);

			if(ids != null) {
				for(String id : ids) {
//					r.add(id.trim());
				}
			}
		}

		return (T[])r.toArray();
	}

	/**
	 * 把字符串解析成List<String>
	 * @param str
	 * @return
	 */
	public static List<String> toList(String str) {
		return toList(str, ",");
	}

	/**
	 * 把字符串根据指定的分隔符号分割，并解析成List<String>
	 * @param str
	 * @param token
	 * @return
	 */
	public static List<String> toList(String str, String token) {
		List<String> r = new ArrayList<String>();

		if(isNotBlank(str)) {
			String[] ids = str.split(token);

			if(ids != null) {
				for(String id : ids) {
					r.add(id.trim());
				}
			}
		}

		return r;
	}

}
