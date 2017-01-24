package com.gammon.qs.io;

public class ExcelFile extends AbstractPhysicalFile {
	public static final String EXTENSION = ".xlsx";
	private ExcelWorkbook document;
	private boolean empty;
	
	public ExcelFile() {
		this.document = new ExcelWorkbook();
	}
	
	public ExcelFile(ExcelWorkbook document) {
		this.document = document;
	}
	
	public byte[] getBytes() {
		return this.document.toBytes();
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public ExcelWorkbook getDocument() {
		return this.document;
	}
}
