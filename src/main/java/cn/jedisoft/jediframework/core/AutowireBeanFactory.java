package cn.jedisoft.jediframework.core;

/**
 * 自动注入 Bean 工厂
 * 
 * @author lzm
 *
 */
public interface AutowireBeanFactory {

	/**
	 * 对指定的 Bean 进行自动注入
	 * @param obj
	 */
	public <T> void autowire(T obj);

}
