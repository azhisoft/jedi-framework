package cn.jedisoft.jediframework.web.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


/**
 * JediResult 返回值处理器
 * 
 * @author azhi
 *
 */
public class WebResultProcessor {

	protected static final Logger log = Logger.getLogger(WebResultProcessor.class);

	/**
	 * 将结果渲染给客户端
	 * @param result
	 * @param request
	 * @param response
	 */
    public void render(WebResult result, HttpServletRequest request, HttpServletResponse response) {
		switch(result.getType()) {
		case WebResult.TYPE_ERROR:
		case WebResult.TYPE_TEXT:
			response.setContentType("text/plain");
			break;

		case WebResult.TYPE_JSON:
			response.setContentType("application/json");
			break;

		case WebResult.TYPE_JSONP:
			response.setContentType("application/json-p");
			break;

		case WebResult.TYPE_XML:
			response.setContentType("text/xml");
			break;

		case WebResult.TYPE_HTML:
			response.setContentType("text/html");
			break;

		case WebResult.TYPE_STREAM:
			response.setContentType("application/octet-stream");
			render(result.getStreamBody(), response);
			return;

		case WebResult.TYPE_FORWARD:
			forward(result.getBody(), request, response);
			return;

		case WebResult.TYPE_REDIRECT:
			redirect(result.getBody(), request, response);
			return;
		}

		response.setCharacterEncoding("utf-8");

		render(result.getBody(), response);
	}

    /**
     * 使用字符串形式返回
     * @param body
     * @param response
     */
	protected void render(String body, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();

			writer.print(body);

			writer.flush();
			writer.close();

		} catch (IOException e) {
			log.error("render() failed: ", e);
		}
	}

	/**
	 * 使用流对象返回
	 * @param stream
	 * @param response
	 */
	protected void render(InputStream stream, HttpServletResponse response) {
		try {
			IOUtils.copy(stream, response.getOutputStream());

		} catch (IOException e) {
			log.error("render() failed: ", e);
		}
	}

	/**
	 * forward 到指定 url
	 * @param url
	 * @param request
	 * @param response
	 */
	protected void forward(String url, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(url).forward(request, response);

		} catch (ServletException e) {
			log.error(String.format("forward(%s) failed: ", url), e);

		} catch (IOException e) {
			log.error(String.format("forward(%s) failed: ", url), e);
		}
	}

	/**
	 * redirect 到指定 url
	 * @param url
	 * @param response
	 */
	protected void redirect(String url, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(url);

		} catch (IOException e) {
			log.error(String.format("redirect(%s) failed: ", url), e);
		}
	}

}
