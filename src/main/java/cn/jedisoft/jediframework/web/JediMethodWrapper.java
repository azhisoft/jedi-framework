package cn.jedisoft.jediframework.web;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.web.annotations.HttpMethod;
import cn.jedisoft.jediframework.web.annotations.parameter.AttrParam;
import cn.jedisoft.jediframework.web.annotations.parameter.BodyParam;
import cn.jedisoft.jediframework.web.annotations.parameter.CookieParam;
import cn.jedisoft.jediframework.web.annotations.parameter.FileParam;
import cn.jedisoft.jediframework.web.annotations.parameter.FormParam;
import cn.jedisoft.jediframework.web.annotations.parameter.HeaderParam;
import cn.jedisoft.jediframework.web.annotations.parameter.PathParam;
import cn.jedisoft.jediframework.web.annotations.parameter.QueryParam;
import cn.jedisoft.jediframework.web.annotations.parameter.SessionParam;
import cn.jedisoft.jediframework.web.common.QueryStringParser;
import cn.jedisoft.jediframework.web.common.UriMatcher;
import cn.jedisoft.jediframework.web.core.MethodWrapper;
import cn.jedisoft.jediframework.web.core.WebApiParam;
import cn.jedisoft.jediframework.web.result.ErrorResult;
import cn.jedisoft.jediframework.web.result.WebResult;

/**
 * MethodWrapper 的实现类
 * 
 * @author lzm
 *
 */
public class JediMethodWrapper implements MethodWrapper {

	private static final Logger log = Logger.getLogger(JediMethodWrapper.class);

	private String requestMethod = HttpMethod.METHOD_ANY;
	private Method method = null;

	public JediMethodWrapper(String method) {
		this.requestMethod = method.trim().toUpperCase();
	}

	// 参数类型列表
    private List<WebApiParam> params = new ArrayList<WebApiParam>();

    /**
     * 对 java 对象的方法进行检测和包装
     * @param method
     * @return
     */
    public boolean wrap(Method method) {
    	Class<?> returnClass = method.getReturnType();

    	if(!returnClass.getName().equals(WebResult.class.getName())) {
    		log.warn("warp() failed: returnClass not matched.");

    		return false;
    	}

    	this.method = method;

    	Class<?>[] fieldClass = method.getParameterTypes();

    	// 保存方法的参数类型
		for(Class<?> clazz : fieldClass) {
			params.add(new WebApiParam(clazz));
		}

		int i = 0;

    	// 通过注解遍历，保存方法的参数源
		for(Annotation[] annos : method.getParameterAnnotations()) {
			WebApiParam param = params.get(i ++);

			if(annos.length > 0) {
				for(Annotation anno : annos) {
					if(anno instanceof AttrParam) {
						AttrParam attr = (AttrParam)anno;
	
						param.setAnnoScope(WebApiParam.SCOPE_ATTR);
						param.setAnnoValue(attr.value());

					} else if(anno instanceof BodyParam) {
						BodyParam body = (BodyParam)anno;
	
						param.setAnnoScope(WebApiParam.SCOPE_BODY);
						param.setAnnoValue(body.value());

					} else if(anno instanceof CookieParam) {
						CookieParam cookie = (CookieParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_COOKIE);
						param.setAnnoValue(cookie.value());

					} else if(anno instanceof FileParam) {
						FileParam file = (FileParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_FILE);
						param.setAnnoValue(file.value());

					} else if(anno instanceof FormParam) {
						FormParam form = (FormParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_FORM);
						param.setAnnoValue(form.value());

					} else if(anno instanceof HeaderParam) {
						HeaderParam header = (HeaderParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_HEADER);
						param.setAnnoValue(header.value());

					} else if(anno instanceof PathParam) {
						PathParam path = (PathParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_PATH);
						param.setAnnoValue(path.value());

					} else if(anno instanceof QueryParam) {
						QueryParam query = (QueryParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_QUERY);
						param.setAnnoValue(query.value());

					} else if(anno instanceof SessionParam) {
						SessionParam session = (SessionParam)anno;

						param.setAnnoScope(WebApiParam.SCOPE_SESSION);
						param.setAnnoValue(session.value());
					}
				}
			}
		}

		return true;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public WebResult invoke(Object bean, HttpServletRequest request, HttpServletResponse response, UriMatcher m) {
		WebResult failed = null;

    	try {
    		Map<String, String> bodyParams = null;

    		if("PUT".equals(requestMethod))
    			bodyParams = buildBodyParams(request);
    			
    		List<Object> values = buildParamValues(params, request, response, m, bodyParams);

    		return (WebResult)method.invoke(bean, values.toArray());

    	} catch (IllegalArgumentException e) {
    		failed = new ErrorResult(e);

		} catch (IllegalAccessException e) {
    		failed = new ErrorResult(e);

		} catch (InvocationTargetException e) {
    		failed = new ErrorResult(e);
		}

		return failed;
	}

	/**
	 * 当请求方法是 PUT 时，对 RequestBody 进行参数解析
	 * @param request
	 * @return
	 */
    protected Map<String, String> buildBodyParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
    	String queryString = "";

		try {
			queryString = IOUtils.toString(request.getInputStream(), "utf-8");

		} catch (IOException e) {
			log.warn("buildBodyParams() read from request failed: ", e);
		}

		if(StringUtils.isNotBlank(queryString)) {
			QueryStringParser parser = new QueryStringParser(queryString);

			while(parser.next()) {
				String pn = parser.getName();
				String pv = parser.getValue();

				if(params.containsKey(pn)) {
					params.put(pn, String.format("%s,%s", params.get(pn), pv));

				} else {
					params.put(pn, pv);
				}
			}

			params.put("", queryString);
		}

    	return params;
    }

    /**
     * 根据绑定方法的参数列表，构建参数值，并完成基本的参数类型转换
     * @param params
     * @param request
     * @param response
     * @param m
     * @param bodyParams
     * @return
     */
	protected List<Object> buildParamValues(List<WebApiParam> params, HttpServletRequest request, HttpServletResponse response, UriMatcher m, Map<String, String> bodyParams) {
		List<Object> values = new ArrayList<Object>();
		
		for(int i = 0; i < params.size(); i ++) {
			WebApiParam p = params.get(i);
			Object v = null;

			switch(p.getAnnoScope()) {
			case WebApiParam.SCOPE_ANY:
				String type = p.getClazz().getName();

				if(type.startsWith("class ")) {
					type = type.substring(5).trim();
				}

				if("javax.servlet.http.HttpServletRequest".equals(type)) {
					v = request;

				} else if("javax.servlet.http.HttpServletResponse".equals(type)) {
					v = response;

				} else if("javax.servlet.http.HttpSession".equals(type)) {
					v = request.getSession();
				}
				break;

			case WebApiParam.SCOPE_ATTR:
				v = request.getAttribute(p.getAnnoValue());
				break;

			case WebApiParam.SCOPE_BODY:
				v = buildParamValue(p, bodyParams.get(p.getAnnoValue()));
				break;

			case WebApiParam.SCOPE_COOKIE:
				Cookie[] cookies = request.getCookies();

				if(cookies != null) for(Cookie cookie : cookies) {
					if(cookie != null && p.getAnnoValue().equals(cookie.getName())) {
						v = buildParamValue(p,  cookie.getValue());

						break;
					}
				}
				break;

			case WebApiParam.SCOPE_FILE:
				break;
				
			case WebApiParam.SCOPE_FORM:
				v = buildParamValue(p, request.getParameter(p.getAnnoValue()));
				break;
				
			case WebApiParam.SCOPE_HEADER:
				v = buildParamValue(p, request.getHeader(p.getAnnoValue()));
				break;

			case WebApiParam.SCOPE_PATH:
				v = buildParamValue(p, m.group(p.getAnnoValue()));
				break;

			case WebApiParam.SCOPE_QUERY:
				v = buildParamValue(p, request.getParameter(p.getAnnoValue()));
				break;
				
			case WebApiParam.SCOPE_SESSION:
				v = request.getSession().getAttribute(p.getAnnoValue());
				break;
			}

			values.add(v);
		}

		return values;
	}

	/**
	 * 对参数进行类型转换
	 * @param param
	 * @param value
	 * @return
	 */
	protected Object buildParamValue(WebApiParam param, String value) {
		String type = param.getClazz().getName();

		if(type.startsWith("class ")) {
			type = type.substring(5).trim();
		}

		if("java.lang.String".equals(type)) {
			return value;

		} else if("int".equals(type) || "java.lang.Integer".equals(type)) {
			return StringUtils.intVal(value);

		} else if("long".equals(type) || "java.lang.Long".equals(type)) {
			return StringUtils.longVal(value);

		} else if("float".equals(type) || "java.lang.Float".equals(type)) {
			return StringUtils.floatVal(value);

		} else if("double".equals(type) || "java.lang.Double".equals(type)) {
			return StringUtils.dblVal(value);

		} else if("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
			return StringUtils.boolVal(value);

		} else {
			log.warn(String.format("buildParamValue() not support \"%s\".",  type));
		}

		return value;
	}

}
