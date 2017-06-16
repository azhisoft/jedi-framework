package cn.jedisoft.jediframework.web.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述方法对应的 HTTP 方法
 * 
 * @author azhi
 *
 */
@Target(value=ElementType.ANNOTATION_TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
@Documented
public @interface HttpMethod {

	public static final String METHOD_ANY = "ANY";
	public static final String METHOD_GET = "GET";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_DELETE = "DELETE";

	public String value() default METHOD_ANY;

}
