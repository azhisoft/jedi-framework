package cn.jedisoft.jediframework.web.render;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import freemarker.core.ParseException;
import freemarker.template.Configuration;	
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreeMarkerRenderer {

	protected static final Logger log = Logger.getLogger(FreeMarkerRenderer.class);

	public String renderText(String ftl, SimpleHash dataModel, Configuration conf) {
		String error = "";

		try {
			StringWriter result = new StringWriter();

			render(ftl, dataModel, conf, result);

			return result.toString();

		} catch (FreeMarkerException e) {
			error = e.getMessage();
		}

		return error;
	}

	public InputStream renderStream(String ftl, SimpleHash dataModel, Configuration conf) {
		String error = "";

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStreamWriter result = new OutputStreamWriter(os);

			render(ftl, dataModel, conf, result);

			InputStream is = new ByteArrayInputStream(os.toByteArray());

			result.flush();
			result.close();

			os.close();

			return is;

		} catch (FreeMarkerException e) {
			error = e.getMessage();

		} catch (IOException e) {
			error = e.getMessage();
		}

		return new ByteArrayInputStream(error.getBytes());
	}

	protected void render(String ftl, SimpleHash dataModel, Configuration conf, Writer out) throws FreeMarkerException {
		String error = "";

		try {
			Template template = conf.getTemplate(ftl);

			template.process(dataModel, out);

			return;

		} catch (TemplateNotFoundException e) {
			error = e.getMessage();

		} catch (MalformedTemplateNameException e) {
			error = e.getMessage();

		} catch (ParseException e) {
			error = e.getMessage();

		} catch (IOException e) {
			error = e.getMessage();

		} catch (TemplateException e) {
			error = e.getMessage();
		}

		throw new FreeMarkerException(String.format("Render template \"%s\" error: %s", ftl, error));
	}

}
