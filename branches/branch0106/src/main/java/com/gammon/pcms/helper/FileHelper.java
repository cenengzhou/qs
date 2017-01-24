package com.gammon.pcms.helper;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class FileHelper {
	
	private static URL baseUrl;
	

	public static String getFileAsString(String path) throws IOException {
		URL file = new URL(getBaseUrl(), path);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(file.openStream()));
        StringBuffer sb = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) sb.append(inputLine);
        in.close();
		return sb.toString();
	}

	public static String getImageAsBase64(String url) {
		URL imageUrl = null;
		BufferedImage image = null;
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			imageUrl = new URL(getBaseUrl(), url);
			image = ImageIO.read(imageUrl);
			if (image != null) {
				ImageIO.write(image, "PNG", bos);
				result = DatatypeConverter.printBase64Binary(bos.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "data:image/png;base64," + result;
	}

	/**
	 * @return the baseUrl
	 */
	public static URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public static void setBaseUrl(URL baseUrl) {
		FileHelper.baseUrl = baseUrl;
	}

}
