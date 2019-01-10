package com.gammon.pcms.helper;

import java.nio.file.Path;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * Helper class for use FreeMarker to product HTML String
 * @author Paul Yiu
 * @since	2015-08-27
 */
public class FreeMarkerHelper {
	
	public static String ftlTemplatePath ;
	
	public static String getFtlTemplatePath() {
		return ftlTemplatePath;
	}

	public static void setFtlTemplatePath(String ftlTemplatePath) {
		FreeMarkerHelper.ftlTemplatePath = ftlTemplatePath;
	}
	
	private FreeMarkerHelper() {}
	/**
	 * Process template and data into string
	 * @param templateFile	the freemarker teamplate filename
	 * @param propertiesMap	the Property Map to inject into the template
	 * @return				the HTML String
	 * @QS.Usage	FreeMarkerHelper.returnHtmlString("filename.ftl", propertiesMap);
	 */
	public static String returnHtmlString(String templateFile, Map<String, Object> propertiesMap) {
		/*
		 * init FreeMarker config and bind static constraints
		 * Usage: ${statics["com.gammon.qs.shared.GlobalParameter"].IMAGE_DOWNLOAD_URL}
		 */
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		BeansWrapper w = new BeansWrapper(Configuration.VERSION_2_3_23); 
		TemplateModel statics = w.getStaticModels(); 
		propertiesMap.put("statics", statics); 
		try {
			/*
			 * set the directory as base template path
			 * and process the template with given propertiesMap
			 * then return the string 
			 * return null when fail
			 */
			String templatePath = getFtlTemplatePath();
			Path path = FileHelper.getConfigFilePath(templatePath);
			cfg.setDirectoryForTemplateLoading(path.toFile());
			Template template = cfg.getTemplate(templateFile);
			String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, propertiesMap);
			return result;
		} catch (java.io.IOException | TemplateException e) { 
			LoggerFactory.getLogger(FreeMarkerHelper.class).error(e.getMessage(), e);
		}
		return null;
	}
}
