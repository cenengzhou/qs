package com.gammon.qs.client.ui.exception;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;

public class JDEErrorException extends SerializationException implements IsSerializable{
	
	private static final long serialVersionUID = 7196197841918898511L;
	public static String ERROR_CODE_STR = "Error Code: ";
	private String errorCode; 
	
	
	public JDEErrorException()
	{
		super();
	}
	
	public JDEErrorException(String msg)
	{
		super(msg);
	}
	
	public JDEErrorException(Throwable e)
	{
		super(e);
	}
	
	public JDEErrorException(Throwable e, String errorCode)
	{
		super(e);
		this.errorCode = errorCode;
	}
	
	public JDEErrorException(String msg, Throwable e)
	{
		super(msg,e);
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return "Error Code: "+ errorCode ;
	}
	
	public String toString()
	{
		return super.toString()+ ERROR_CODE_STR + errorCode;		
	}
	
	
	

}
