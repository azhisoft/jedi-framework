package cn.jedisoft.jediframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述对象的生命周期
 * 
 * @author lzm
 *
 */
@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Scope {

	public static final String SCOPE_PROTOTYPE = "prototype";
	public static final String SCOPE_SINGLETON = "singleton";

	public String value() default SCOPE_SINGLETON;

}
