package cn.jedisoft.jediframework.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.core.DataSourceFactory;

/**
 * 数据库操作接口
 * 
 * @author lzm
 *
 */
public class TransactionWrapper extends DatabaseWrapper implements Transaction {

	private static final Logger log = Logger.getLogger(TransactionWrapper.class);

	protected Connection conn = null;
	
	public TransactionWrapper(DataSourceFactory dsf) {
		super(dsf);
	}

	public boolean begin() {
		try {
			conn = super.getConnection(CONN_READ_WRITE);

			conn.setAutoCommit(false);

			return true;
			
		} catch (SQLException e) {
			log.error("begin() failed: ", e);
		}
		
		return false;
	}

	public boolean commit() {
		try {
			conn.commit();

			return true;

		} catch (SQLException e) {
			log.error("commit() failed: ", e);
		}

		return false;
	}

	public void rollback() {
		try {
			conn.rollback();

		} catch (SQLException e) {
			log.error("rollback() failed: ", e);
		}
	}

	@Override
	protected Connection getConnection(int connMode) {
		return conn;
	}

}
