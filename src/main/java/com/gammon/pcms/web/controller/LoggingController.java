package com.gammon.pcms.web.controller;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/logging/")
public class LoggingController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping(value = "logToBackend", method = RequestMethod.POST)
	public void logToBackend(@RequestBody Object message){
		logger.error("JS Error:" + message);
	}


}
