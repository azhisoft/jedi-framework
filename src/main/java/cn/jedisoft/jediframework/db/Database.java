package cn.jedisoft.jediframework.db;

/**
 * 数据库操作接口
 * 
 * @author lzm
 *
 */
public interface Database extends SimpleJDBC {

	/**
	 * 获取事务对象，用于批量执行 sql
	 * 
	 * @return
	 */
	public Transaction getTransaction();

}
