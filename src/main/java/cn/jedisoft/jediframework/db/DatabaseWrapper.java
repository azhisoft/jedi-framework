package cn.jedisoft.jediframework.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.core.DataSourceFactory;

public class DatabaseWrapper extends SimpleJDBCWrapper implements Database {

	private static final Logger log = Logger.getLogger(DatabaseWrapper.class);

	public DatabaseWrapper(DataSourceFactory dsf) {
		super(dsf);
	}

	public Transaction getTransaction() {
		return new TransactionWrapper(dsf);
	}

	public int insert(String sql) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql);

		} catch (SQLException e) {
			log.error(String.format("insert(%s) failed: ", sql), e);
		}

		return 0;
	}

	public int insert(String sql, Object... params) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql, params);

		} catch (SQLException e) {
			log.error(String.format("insert(%s) failed: ", sql), e);
		}

		return 0;
	}

	public <T> T select1st(Class<T> clazz, String sql) {
		try {
			ResultSet rs = executeQuery(getConnection(SimpleJDBCWrapper.CONN_READ_ONLY), sql);

			return toBean(clazz, rs);

		} catch (SQLException e) {
			log.error(String.format("selectOne(%s) failed: ", sql), e);
		}

		return null;
	}

	public <T> T select1st(Class<T> clazz, String sql, Object... params) {
		try {
			ResultSet rs = executeQuery(getConnection(SimpleJDBCWrapper.CONN_READ_ONLY), sql, params);

			return toBean(clazz, rs);

		} catch (SQLException e) {
			log.error(String.format("selectOne(%s) failed: ", sql), e);
		}

		return null;
	}

	public <T> List<T> select(Class<T> clazz, String sql) {
		try {
			ResultSet rs = executeQuery(getConnection(SimpleJDBCWrapper.CONN_READ_ONLY), sql);

			return toBeanList(clazz, rs);

		} catch (SQLException e) {
			log.error(String.format("select(%s) failed: ", sql), e);
		}

		return null;
	}

	public <T> List<T> select(Class<T> clazz, String sql, Object... params) {
		try {
			ResultSet rs = executeQuery(getConnection(SimpleJDBCWrapper.CONN_READ_ONLY), sql, params);

			return toBeanList(clazz, rs);

		} catch (SQLException e) {
			log.error(String.format("select(%s) failed: ", sql), e);
		}

		return null;
	}

	public int update(String sql) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql);

		} catch (SQLException e) {
			log.error(String.format("update(%s) failed: ", sql), e);
		}

		return 0;
	}

	public int update(String sql, Object... params) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql, params);

		} catch (SQLException e) {
			log.error(String.format("update(%s) failed: ", sql), e);
		}

		return 0;
	}

	public int delete(String sql) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql);

		} catch (SQLException e) {
			log.error(String.format("delete(%s) failed: ", sql), e);
		}

		return 0;
	}

	public int delete(String sql, Object... params) {
		try {
			return executeUpdate(getConnection(SimpleJDBCWrapper.CONN_READ_WRITE), sql, params);

		} catch (SQLException e) {
			log.error(String.format("delete(%s) failed: ", sql), e);
		}

		return 0;
	}

}
