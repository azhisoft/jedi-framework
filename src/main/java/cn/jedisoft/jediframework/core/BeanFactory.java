package cn.jedisoft.jediframework.core;

/**
 * Bean 工厂
 * @author lzm
 *
 */
public interface BeanFactory extends AutowireBeanFactory {

	/**
	 * 添加 Bean
	 * @param name
	 * @param bean
	 */
	public <T> void addBean(String name, T bean);

	/**
	 * 获取 Bean
	 * @param name
	 * @return
	 */
	public <T> T getBean(String name);

}
