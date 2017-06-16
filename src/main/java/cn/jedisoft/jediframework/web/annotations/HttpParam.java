package cn.jedisoft.jediframework.web.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述参数来源
 * 
 * @author azhi
 *
 */
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Documented
public @interface HttpParam {

	public static final String SCOPE_ANY = "";
	public static final String SCOPE_ATTR = "ATTR";
	public static final String SCOPE_BODY = "BODY";
	public static final String SCOPE_COOKIE = "COOKIE";
	public static final String SCOPE_FILE = "FILE";
	public static final String SCOPE_FORM = "FORM";
	public static final String SCOPE_HEADER = "HEADER";
	public static final String SCOPE_PATH = "PATH";
	public static final String SCOPE_QUERY = "QUERY";
	public static final String SCOPE_SESSION = "SESSION";

	public String value() default SCOPE_ANY;

}
