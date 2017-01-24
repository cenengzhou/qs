package com.gammon.qs.application.exception;

public class ValidateBusinessLogicException extends Exception {

	private static final long serialVersionUID = -1572293849352267392L;

	public ValidateBusinessLogicException() {
		super("Validation Exception");
	}

	public ValidateBusinessLogicException(String errorMsg) {
		super("Validation Exception: "+errorMsg);
	}
	
	public ValidateBusinessLogicException(Exception exception){
		super(exception);
	}
}
