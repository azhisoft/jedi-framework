package cn.jedisoft.jediframework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jedisoft.jediframework.web.core.WebApiWrapper;
import cn.jedisoft.jediframework.web.core.WebAppContext;

/**
 * Servlet Filter implementation class JediFilter
 */
public class JediWebFilter implements Filter {

	protected WebAppContext appCtx = null;
	
    /**
     * Default constructor. 
     */
    public JediWebFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext servletCtx = fConfig.getServletContext();

		appCtx = (WebAppContext)servletCtx.getAttribute(WebAppContext.JEDI_WEBAPP_CONTEXT);
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String ctxPath = req.getContextPath();
		String path = req.getRequestURI();
		String url = path.substring(ctxPath.length());
		String method = req.getMethod();
		WebApiWrapper api = appCtx.matchApi(url, method);

		if(api != null && api.process(url, method, req, (HttpServletResponse)response, appCtx.getAutowireBeanFactory()))
			return;

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
