package com.gammon.pcms.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gammon.pcms.model.Attachment;

public class FileHelper {
	
	private static URL baseUrl;
	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);
	public static final String LINE_BREAK = System.lineSeparator();
	public static final String UTF_BOM = "\uFEFF";
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
	
	public static Long getFolderSize(String attachmentDirPath) {
		Long totalSize = 0L;
		File folder = new File(attachmentDirPath);
		File[] files = folder.listFiles();
		if(files != null)
		for (File file : files) {
			if (file.isFile()) {
				totalSize += file.length();
			}
		}
		return totalSize;
	}
	
	public static List<Attachment> getAttachmentList(String attachmentDirPath) {
		return getAttachmentList(attachmentDirPath, false);
	}
	
	public static List<Attachment> getAttachmentList(String attachmentDirPath, Boolean fullPath) {
		List<Attachment> attachmentList = new ArrayList<Attachment>();

		File folder;
		try {
			folder = new File(attachmentDirPath);
			File[] files = folder.listFiles();
			if(files != null)
			for (File file : files) {
				if (file.isFile()) {
					Attachment attach = new Attachment();
					attach.setNameFile(fullPath ? attachmentDirPath + "\\" + file.getName() : file.getName());
					attach.setPathFile(file.getName());

					attachmentList.add(attach);
				}
			}
		} catch (NullPointerException | SecurityException e) {
			e.printStackTrace();
		}

		return attachmentList;
	}

	public static String convertFileToString(String htmlFilePath) {
		String content = "";

		try {
			List<String> allLines = new ArrayList<>();
			Path path = getConfigFilePath(htmlFilePath);
			logger.debug("convertFileToString from:" + path);
			allLines = Files.readAllLines(path);
			content = String.join("", allLines);
			content = content.replaceAll(UTF_BOM,"");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return content;
	}

	public static String readFileAsString(String path) {
		File file = new File(path);
		StringBuilder sb = new StringBuilder();
		String inputLine;
		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
			while ((inputLine = br.readLine()) != null)
				sb.append(inputLine).append(LINE_BREAK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void writeStringToFile(String path, String content) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw);) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
