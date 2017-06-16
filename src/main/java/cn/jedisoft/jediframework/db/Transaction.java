package cn.jedisoft.jediframework.db;

/**
 * 数据库操作接口
 * 
 * @author lzm
 *
 */
public interface Transaction extends SimpleJDBC {

	/**
	 * 开始事务
	 * 
	 * @return
	 */
	public boolean begin();

	/**
	 * 提交事务
	 * 
	 * @return
	 */
	public boolean commit();

	/**
	 * 回滚事务
	 */
	public void rollback();
	
}
