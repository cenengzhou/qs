package com.gammon.pcms.application;

import java.lang.reflect.UndeclaredThrowableException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody PCMSDTO handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) {
		e.printStackTrace();
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, e.getCause().getMessage());
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
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, e.getCause().getMessage());
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
		undeclared = undeclared.getCause();
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		PCMSDTO error = new PCMSDTO(PCMSDTO.Status.FAIL, undeclared.getMessage());
		return error;
	}

}
