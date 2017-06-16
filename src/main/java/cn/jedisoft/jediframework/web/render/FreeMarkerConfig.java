package cn.jedisoft.jediframework.web.render;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;

public class FreeMarkerConfig extends Configuration {

	protected static final Logger log = Logger.getLogger(FreeMarkerConfig.class);

	public FreeMarkerConfig() {
		super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		super.setDefaultEncoding("utf-8");
	}

	public FreeMarkerConfig(String basePath) {
		super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		super.setDefaultEncoding("utf-8");

		try {
			super.setTemplateLoader(new FileTemplateLoader(new File(basePath)));

		} catch (IOException e) {
			log.error("setTemplateLoader() failed: ", e);
		}
	}

}
