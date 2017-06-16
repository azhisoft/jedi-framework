package cn.jedisoft.jediframework.app;

import javax.sql.DataSource;

import cn.jedisoft.jediframework.core.AppContext;
import cn.jedisoft.jediframework.core.DataSourceFactory;

/**
 * AppContext 包装类
 * 
 * @author lzm
 *
 */
public class JediAppContext extends JediBeanFactory implements AppContext, DataSourceFactory {

	protected DataSource readOnlyDataSource = null;
	protected DataSource readWriteDataSource = null;

	public void setReadOnlyDataSource(DataSource ds) {
		readOnlyDataSource = ds;
	}

	public DataSource getReadOnlyDataSource() {
		return readOnlyDataSource;
	}

	public void setReadWriteDataSource(DataSource ds) {
		readWriteDataSource = ds;
	}

	public DataSource getReadWriteDataSource() {
		return readWriteDataSource;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
