package cn.jedisoft.jediframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述属性可以自动注入
 * 
 * @author lzm
 *
 */
@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Autowired {

	public String value() default "";

}
