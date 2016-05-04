package com.gammon.qs.application.exception;

public class DatabaseOperationException extends Exception {

	private static final long serialVersionUID = -5466286691947323832L;

	public DatabaseOperationException() {
		super("Database Operation Exception");
	}
	
	public DatabaseOperationException(Exception ex) {
		super(ex);
	}
	
	public DatabaseOperationException(String message) {
		super("Database Operation Exception: "+message);
	}
	

}
