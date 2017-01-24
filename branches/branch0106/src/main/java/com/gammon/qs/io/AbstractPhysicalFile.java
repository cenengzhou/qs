package com.gammon.qs.io;

public abstract class AbstractPhysicalFile {
	private byte[] bytes;
	private String fileName;
	
	public byte[] getBytes() {
		return this.bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getFileName() {
		return this.fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
