
package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.ConsultancyAgreement;
import com.gammon.pcms.wrapper.ConsultancyAgreementFormWrapper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.service.ConsultancyAgreementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.UndeclaredThrowableException;

@RestController
@RequestMapping(value = "service/consultancyAgreement/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class ConsultancyAgreementController {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ConsultancyAgreementService consultancyAgreementService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('ConsultancyAgreementController','getMemo', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getMemo", method = RequestMethod.GET)
	public ConsultancyAgreement getMemo(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException {
		return consultancyAgreementService.getMemo(jobNo, subcontractNo);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ConsultancyAgreementController','getFormSummary', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getFormSummary", method = RequestMethod.GET)
	public ConsultancyAgreementFormWrapper getFormSummary(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException {
		return consultancyAgreementService.getFormSummary(jobNo, subcontractNo);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ConsultancyAgreementController','saveMemo', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveMemo", method = RequestMethod.POST)
	public String saveMemo(@RequestParam(required = true) String jobNo,
										@RequestParam(required = true) String subcontractNo,
										@RequestBody ConsultancyAgreement ca){
		String result = "";
		try{
			result = consultancyAgreementService.saveMemo(jobNo, subcontractNo, ca);
		}catch(Exception e){
			result  = "Memorandum cannot be saved.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ConsultancyAgreementController','submitCAApproval', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitCAApproval", method = RequestMethod.POST)
	public String submitCAApproval(@RequestParam(required = true) String jobNo,
										@RequestParam(required = true) String subcontractNo){
		String result = "";
		try{
			result = consultancyAgreementService.submitCAApproval(jobNo, subcontractNo);
		}catch(Exception e){
			result  = "Consultancy Agreement failed to be submitted to Approval System.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ConsultancyAgreementController','updateConsultancyAgreementAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateConsultancyAgreementAdmin", method = RequestMethod.POST)
	public String updateConsultancyAgreementAdmin(@RequestParam(required = true) String jobNo,
						   @RequestParam(required = true) String subcontractNo,
						   @RequestBody ConsultancyAgreement ca){
		String result = "";
		try{
			result = consultancyAgreementService.updateConsultancyAgreementAdmin(jobNo, subcontractNo, ca);
		}catch(Exception e){
			result  = "Consultancy Agreement cannot be saved.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

}
