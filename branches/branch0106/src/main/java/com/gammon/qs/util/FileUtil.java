package com.gammon.qs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class FileUtil {
	private static final int MAX_FILE_SIZE = Integer.MAX_VALUE;
	
	/**
	 * 
	 * @author koeyyeung
	 */
	@SuppressWarnings("resource")
	public static byte[] readFile(File file) throws Exception{	
		byte[] fileBytes = null;

		if(file==null)
			throw new Exception("File is null.");
		if (file.length() > MAX_FILE_SIZE)
	        throw new Exception("File excesses the maximum file size.");
		
		//Read file
		fileBytes = new byte[(int) file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		
		//read the whole file and put it into the byte[]
		if (fileInputStream.read(fileBytes) == -1)
            throw new IOException("Reached EOF while trying to read the file.");

		return fileBytes;
	}


	public static boolean writeNewFile(String path, byte[] fileBytes) throws Exception{
		boolean inserted = false;		
		File newFile = new File(path);
		if(newFile.exists())
			throw new IOException("File exists already.");
		else{
			if(fileBytes==null)
				throw new Exception("File Byte is null.");
			else{
				FileOutputStream outFile = new FileOutputStream(newFile);
				outFile.write(fileBytes);
				outFile.close();
				inserted = true;
			}
		}

		return inserted;
	}
	

	public static List<MultipartFile> getMultipartFiles(HttpServletRequest request) throws Exception {
		@SuppressWarnings("unused")
		int numberOfFiles = 0;

//		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		HttpServletRequest originalRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		MultipartHttpServletRequest req = new DefaultMultipartHttpServletRequest(originalRequest);

		MultipartFile file = null;
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		
		for(Object object: req.getFileMap().values()) {
			if (object !=null) {
				file = (MultipartFile)object;
				files.add(file);
				numberOfFiles++;
			}
		}
		return files;
	}
}
