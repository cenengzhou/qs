package com.gammon.pcms.application;

import java.lang.reflect.UndeclaredThrowableException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gammon.pcms.config.ExceptionConfig;
import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ExceptionConfig exceptionConfig;
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody PCMSDTO handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) {
		e.printStackTrace();
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, getErrorMessage(e));
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return error;
	}
	
	/**
	 * catch application wide Exception 
	 * @param e
	 * @param response
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody PCMSDTO handleException(Exception e, HttpServletResponse response) {
		e.printStackTrace();
		Throwable exception = e.getCause();
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, getErrorMessage(exception));
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return error;
	}
	
	/**
	 * call application wide RuntimeException which wrapped by proxy
	 * @param e
	 * @param response
	 * @return
	 */
	@ExceptionHandler(UndeclaredThrowableException.class)
	@ResponseBody PCMSDTO handleUndeclaredThrowableException(UndeclaredThrowableException e, HttpServletResponse response) {
		e.printStackTrace();
		Throwable undeclared = e.getUndeclaredThrowable();
		while(undeclared.getCause() instanceof UndeclaredThrowableException){
			undeclared = undeclared.getCause();
			undeclared = ((UndeclaredThrowableException)undeclared).getUndeclaredThrowable();
		}
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, getErrorMessage(undeclared));
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return error;
	}
	
	private String getErrorMessage(Throwable exception){
		String msgKey = exception.getCause().getClass().getName();
		String msg = exceptionConfig.getMessages().get(msgKey);
		if(!StringUtils.isEmpty(msg)) return msg;
		switch(msgKey){
			case "org.springframework.security.access.AccessDeniedException":
				msg = exception.getCause().getMessage();
				break;
		}
		if(StringUtils.isEmpty(msg)) msg = exceptionConfig.getMessages().get("default");
		return msg;
	}

}
