package cn.jedisoft.jediframework.core;

/**
 * Bean 实例的封装
 * 
 * 实际在 BeanFactory 中保存的并不是 Bean 实体本身，而是这个包装类
 * 
 * @author lzm
 *
 */
public interface BeanWrapper {

	public static final int SCOPE_PROTOTYPE = 1;
	public static final int SCOPE_SINGLETON = 2;

	/**
	 * 获取 Bean 名字
	 * @return
	 */
	public String getName();

	/**
	 * 获取 Bean 实例，并通过传入的工厂类，对 Bean 实例完成自动注入
	 * @param factory
	 * @return
	 */
	public <T> T getBean(BeanFactory factory);

}
