package com.gammon.qs.client.ui.util;

//This class can be used for separating name, path and extension of a file from full path
public class FileNameSeparator {
	private String fullPath;
	private char pathSeparator, extensionSeparator;

	public FileNameSeparator(String fullPath, char pathSeparator, char ext) {
		this.fullPath = fullPath;
		this.pathSeparator = pathSeparator;
		this.extensionSeparator = ext;
	}

	//Function for return filename without extension
	public String filename() { 
		int dot = this.fullPath.lastIndexOf(extensionSeparator);
		int sep = this.fullPath.lastIndexOf(pathSeparator);
		return this.fullPath.substring(sep + 1, dot);
	}
	
	//Function for return file extension
	public String extension() {
		int dot = this.fullPath.lastIndexOf(extensionSeparator);
		return this.fullPath.substring(dot + 1);
	}
	
	public String path() {
		int sep = this.fullPath.lastIndexOf(pathSeparator);
		return this.fullPath.substring(0, sep);
	}
}
