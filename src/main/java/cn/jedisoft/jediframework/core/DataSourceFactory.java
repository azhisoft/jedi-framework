package cn.jedisoft.jediframework.core;

import javax.sql.DataSource;

/**
 * 数据源工厂
 * 
 * @author lzm
 *
 */
public interface DataSourceFactory {

	/**
	 * 从工厂中获取一个只读数据源
	 * 
	 * @return
	 */
	public DataSource getReadOnlyDataSource();

	/**
	 * 从工厂中获取一个读写数据源
	 * @return
	 */
	public DataSource getReadWriteDataSource();

}
