package cn.jedisoft.jediframework.db;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.common.ClassUtils;
import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.core.DataSourceFactory;

/**
 * 简单 JDBC 操作封装
 * 
 * @author azhi
 *
 */
public class SimpleJDBCWrapper {

	public static final int CONN_READ_ONLY = 1;
	public static final int CONN_READ_WRITE = 2;
	
	private static final Logger log = Logger.getLogger(SimpleJDBCWrapper.class);

	protected DataSourceFactory dsf = null;
	
	public SimpleJDBCWrapper(DataSourceFactory dsf) {
		this.dsf = dsf;
	}

	/**
	 * 从连接池中获取可用的数据库连接，可以指定连接的模式（只读连接或读写连接）
	 * @param connMode
	 * @return
	 */
	protected Connection getConnection(int connMode) {
		try {
			if(dsf != null) {
				DataSource ds = null;
				
				switch(connMode) {
				case CONN_READ_ONLY:
					ds = dsf.getReadOnlyDataSource();
					break;

				case CONN_READ_WRITE:
					ds = dsf.getReadWriteDataSource();
					break;
				}

				if(ds != null) {
					return ds.getConnection();
				}
			}

		} catch (SQLException e) {
			log.error(String.format("getConnection(%d) failed: ", connMode), e);
		}

		return null;
	}

	/**
	 * 执行无返回的 sql
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(Connection conn, String sql) throws SQLException {
		if(conn != null && StringUtils.isNotBlank(sql)) {
			Statement stmt = conn.createStatement();

			return stmt.executeUpdate(sql);
		}

		return 0;
	}

	/**
	 * 执行无返回的 sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
		if(conn != null && StringUtils.isNotBlank(sql)) {
			PreparedStatement stmt = conn.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}

			return stmt.executeUpdate();
		}

		return 0;
	}

	/**
	 * 执行有返回集的 sql
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected ResultSet executeQuery(Connection conn, String sql) throws SQLException {
		if(conn != null && StringUtils.isNotBlank(sql)) {
			Statement stmt = conn.createStatement();

			return stmt.executeQuery(sql);
		}

		return null;
	}

	/**
	 * 执行有返回集的 sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected ResultSet executeQuery(Connection conn, String sql, Object... params) throws SQLException {
		if(conn != null && StringUtils.isNotBlank(sql)) {
			PreparedStatement stmt = conn.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}

			return stmt.executeQuery();
		}

		return null;
	}

	/**
	 * 把返回集的第一条记录映射到 javaBean
	 * @param clazz
	 * @param rs
	 * @return
	 */
	protected <T> T toBean(Class<T> clazz, ResultSet rs) {
		try {
			if(rs != null) {
				T result = null;
				List<FieldMetaData> fields = getDbFields(rs.getMetaData());
				
				matchBeanFields(clazz, fields);
				
				while(rs.next()) {
					result = toBean(clazz, rs, fields); break;
				}
				
				rs.close();
				
				return result;
			}
			
		} catch (SQLException e) {
			log.error("toBean() failed: ", e);
		}

		return null;
	}

	/**
	 * 把返回集的所有记录映射到 javaBean
	 * @param clazz
	 * @param rs
	 * @return
	 */
	protected <T> List<T> toBeanList(Class<T> clazz, ResultSet rs) {
		try {
			if(rs != null) {
				List<T> result = new ArrayList<T>();
				List<FieldMetaData> fields = getDbFields(rs.getMetaData());
				
				matchBeanFields(clazz, fields);

				while(rs.next()) {
					result.add(toBean(clazz, rs, fields));
				}
				
				rs.close();
				
				return result;
			}

		} catch (SQLException e) {
			log.error("toBeanList() failed: ", e);
		}

		return null;
	}

	/**
	 * 把某一条记录映射到 javaBean
	 * @param clazz
	 * @param rs
	 * @param fieldMetas
	 * @return
	 */
	protected <T> T toBean(Class<T> clazz, ResultSet rs, List<FieldMetaData> fieldMetas) {
		T result = ClassUtils.createInstance(clazz);

		try {
			for(FieldMetaData fd : fieldMetas) {
				if(fd.isMatched()) {
					ClassUtils.setFieldValue(result, fd.getJavaField(), rs.getObject(fd.getName()));
				}
			}

		} catch (SQLException e) {
			log.error("toBean() failed: ", e);
		}
		
		return result;
	}

	/**
	 * 从数据库返回集中获取字段信息
	 * @param meta
	 * @return
	 */
	protected List<FieldMetaData> getDbFields(ResultSetMetaData meta) {
		List<FieldMetaData> result = new ArrayList<FieldMetaData>();
		
		try {
			for(int i = 0; i < meta.getColumnCount(); i ++) {
				FieldMetaData fd = new FieldMetaData();
				
				fd.setIndex(i + 1);
				fd.setName(meta.getColumnName(i + 1));
				fd.setType(meta.getColumnType(i + 1));
				
				result.add(fd);
			}

		} catch (SQLException e) {
			log.error("findFields() failed: ", e);
		}
		
		return result;
	}

	/**
	 * 对 javaBean 的属性和数据库字段进行匹配
	 * @param clazz
	 * @param fieldMetas
	 */
	protected void matchBeanFields(Class<?> clazz, List<FieldMetaData> fieldMetas) {
		Map<String, FieldMetaData> tmp = new HashMap<String, FieldMetaData>();
		
		for(FieldMetaData fd : fieldMetas) {
			tmp.put(StringUtils.formatMethodName(fd.getName()), fd);
		}
		
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();
			FieldMetaData fd = tmp.get(fieldName);
			
			if(fd != null) {
				fd.setJavaName(fieldName);
				fd.setJavaType(field.getType());
				fd.setJavaField(field);
				
				if(matchFieldType(fd.getType(), fd.getJavaType())) {
					fd.setMatched(true);

				} else {
					log.warn(String.format("Field named \"%s\" not matched.", fieldName));
				}

			} else {
				log.warn(String.format("Field named \"%s\" not found.", fieldName));
			}
		}
	}

	/**
	 * 判断 SQL 的数据类型和 javaBean 属性的数据类型是否匹配
	 * @param type
	 * @param javaType
	 * @return
	 */
	protected boolean matchFieldType(int type, Type javaType) {
		int dt = mergeSqlType(type), jt = toSqlType(javaType);

		if(dt == jt)
			return true;

		if(dt == Types.INTEGER)
			return (jt == Types.BIGINT);
		
		if(dt == Types.FLOAT)
			return (jt == Types.DOUBLE);
		
		return false;
	}

	/**
	 * 合并 SQL 数据类型
	 * @param type
	 * @return
	 */
	protected int mergeSqlType(int type) {
		switch(type) {
		case Types.CHAR:
		case Types.NCHAR:
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
			return Types.CHAR;
			
		case Types.BIT:
		case Types.BOOLEAN:
			return Types.BOOLEAN;
			
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.INTEGER:
		case Types.NUMERIC:
			return Types.INTEGER;

		case Types.BIGINT:
		case Types.ROWID:
		case Types.TIMESTAMP:
			return Types.BIGINT;
			
		case Types.FLOAT:
			return Types.FLOAT;
			
		case Types.DOUBLE:
		case Types.DECIMAL:
		case Types.REAL:
			return Types.DOUBLE;

		case Types.ARRAY:
		case Types.BINARY:
		case Types.BLOB:
		case Types.CLOB:
		case Types.DATALINK:
		case Types.DATE:
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
		case Types.LONGVARBINARY:
		case Types.NCLOB:
		case Types.NULL:
		case Types.OTHER:
		case Types.REF:
		case Types.SQLXML:
		case Types.STRUCT:
		case Types.TIME:
		case Types.VARBINARY:
		default: break;
		}

		return Types.NULL;
	}

	/**
	 * 把 JAVA 的数据类型转换成对应的 SQL 数据类型
	 * @param javaType
	 * @return
	 */
	protected int toSqlType(Type javaType) {
		String type = javaType.toString();

		if(type.startsWith("class ")) {
			type = type.substring(5).trim();
		}

		if("java.lang.String".equals(type)) {
			return Types.CHAR;

		} else if("int".equals(type) || "java.lang.Integer".equals(type)) {
			return Types.INTEGER;

		} else if("long".equals(type) || "java.lang.Long".equals(type)) {
			return Types.BIGINT;

		} else if("float".equals(type) || "java.lang.Float".equals(type)) {
			return Types.FLOAT;

		} else if("double".equals(type) || "java.lang.Double".equals(type)) {
			return Types.DOUBLE;

		} else if("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
			return Types.BOOLEAN;

		} else {
			log.warn(String.format("toSqlType() not support \"%s\".", type));
		}

		return Types.NULL;
	}

}
