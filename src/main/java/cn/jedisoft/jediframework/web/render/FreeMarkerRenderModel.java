package cn.jedisoft.jediframework.web.render;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cn.jedisoft.jediframework.common.StringUtils;
import cn.jedisoft.jediframework.web.common.QueryStringParser;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;

public class FreeMarkerRenderModel extends SimpleHash {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3944266615429870655L;

	protected static final Logger log = Logger.getLogger(FreeMarkerRenderModel.class);

	public FreeMarkerRenderModel(ObjectWrapper wrapper, HttpServletRequest request) {
		setObjectWrapper(wrapper);

		init(request);
	}

	private void init(HttpServletRequest request) {
		// headers
		Enumeration<String> keys = request.getHeaderNames();

		while(keys.hasMoreElements()) {
			String key = keys.nextElement();

			super.put(formatModelName(key), request.getHeader(key));
		}

		// parameters
		keys = request.getParameterNames();

		while(keys.hasMoreElements()) {
			String key = keys.nextElement();

			super.put(formatModelName(key), request.getParameter(key));
		}

		// parameters in RequestBody, only PUT request
		String method = request.getMethod();
		
		if("PUT".equals(method)) {
			Map<String, String> p = buildBodyParameters(request);
			Iterator<String> ki = p.keySet().iterator();
			
			while(ki.hasNext()) {
				String k = ki.next();

				super.put(formatModelName(k), p.get(k));
			}
		}

		// attributes
		keys = request.getAttributeNames();

		while(keys.hasMoreElements()) {
			String key = keys.nextElement();

			super.put(formatModelName(key), request.getAttribute(key));
		}
	}

	private Map<String, String> buildBodyParameters(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
    	String queryString = "";

		try {
			queryString = IOUtils.toString(request.getInputStream(), "UTF-8");

		} catch (IOException e) {
			log.error(e.getMessage());
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
		}

    	return params;
    }

	private String formatModelName(String name) {
		StringBuffer modelName = new StringBuffer();
		String[] names = name.split("[\\s\\._-]+");

		modelName.append(names[0].trim());

		for(int i = 1; i < names.length; i ++) {
			String n = names[i].trim();

			modelName.append(n.substring(0, 1).toUpperCase());
			modelName.append(n.substring(1));
		}

		return modelName.toString();
	}

}
