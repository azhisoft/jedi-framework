package cn.jedisoft.jediframework.web.annotations.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.jedisoft.jediframework.web.annotations.HttpParam;

/**
 * 用于描述参数来自表单
 * 
 * @author azhi
 *
 */
@Target(value={ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
@HttpParam(value=HttpParam.SCOPE_FORM)
public @interface FormParam {

	public String value() default "";

}
