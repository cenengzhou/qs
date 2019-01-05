package com.gammon.pcms.helper;

import java.net.URL;

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

}
