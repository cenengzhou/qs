package com.gammon.pcms.helper;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

public class FileHelper {
	
	private static URL baseUrl;
	/**
	 * @param baseUrl the baseUrl to set
	 */
	public static void setBaseUrl(URL baseUrl) {
		FileHelper.baseUrl = baseUrl;
	}

	/**
	 * @return the baseUrl
	 */
	public static URL getBaseUrl() {
		return baseUrl;
	}

	public static Path getConfigFilePath(String filename) {
		Path path = null;
		if(filename.indexOf("classpath:") > -1){
			String filePath = filename.split("classpath:")[1];
			try {
				path = Paths.get(FileHelper.class.getResource(filePath).toURI());
			} catch (URISyntaxException e) {
				LoggerFactory.getLogger(FileHelper.class).error(e.getMessage(), e);
			}
		} else if(filename.indexOf("file:") > -1){
			String filePath = filename.split("file:/")[1];
			path = Paths.get(filePath);
		} else {
			path = Paths.get(filename);
		}
		return path;
	}
}
