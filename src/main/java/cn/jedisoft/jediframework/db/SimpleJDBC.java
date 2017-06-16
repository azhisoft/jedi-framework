package cn.jedisoft.jediframework.db;

import java.util.List;

/**
 * 数据库操作接口
 * 
 * @author lzm
 *
 */
public interface SimpleJDBC {

	/**
	 * 对数据库进行插入操作
	 * @param sql
	 * @return
	 */
	public int insert(String sql);

	/**
	 * 使用 PreparedStatement 进行数据库插入
	 * @param sql
	 * @param params
	 * @return
	 */
	public int insert(String sql, Object... params);

	/**
	 * 从查询结果中返回第一条记录
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public <T> T select1st(Class<T> clazz, String sql);

	/**
	 * 从查询结果中返回第一条记录（使用 PreparedStatement 进行查询）
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> T select1st(Class<T> clazz, String sql, Object... params);

	/**
	 * 直接使用 sql 语句进行查询
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public <T> List<T> select(Class<T> clazz, String sql);

	/**
	 * 使用 PreparedStatement 进行查询
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> select(Class<T> clazz, String sql, Object... params);

	/**
	 * 直接使用 sql 更新数据库
	 * @param sql
	 * @return
	 */
	public int update(String sql);

	/**
	 * 使用 PreparedStatement 进行更新
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params);

	/**
	 * 直接使用 sql 删除数据
	 * @param sql
	 * @return
	 */
	public int delete(String sql);

	/**
	 * 使用 PreparedStatement 删除数据
	 * @param sql
	 * @param params
	 * @return
	 */
	public int delete(String sql, Object... params);

}
