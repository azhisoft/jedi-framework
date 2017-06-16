package cn.jedisoft.jediframework.web.render;

import org.apache.log4j.Logger;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;

public class FreeMarkerUrlBuilder extends FreeMarkerRenderer {

	protected static final Logger log = Logger.getLogger(FreeMarkerUrlBuilder.class);

	private Configuration conf = new FreeMarkerConfig();
	private String name = "";

	public FreeMarkerUrlBuilder(String appKey, String taskName, String ftl) {
		StringTemplateLoader loader = new StringTemplateLoader();

		name = String.format("%s_%s_dataUrl", appKey, taskName);

		loader.putTemplate(name, ftl);

		conf.setTemplateLoader(loader);
	}

	public String getUrl(SimpleHash dataModel) {
		return renderText(name, dataModel, conf);
	}

}
