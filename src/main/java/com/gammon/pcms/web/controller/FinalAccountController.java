package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.FinalAccount;
import com.gammon.pcms.service.FinalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.UndeclaredThrowableException;

@RestController
@RequestMapping(value = "service/finalAccount/")
public class FinalAccountController {
	
	@Autowired
	private FinalAccountService finalAccountService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('FinalAccountController','getFinalAccount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getFinalAccount/{jobNo}/{addendumNo}/{addendumId}", method = RequestMethod.GET)
	public FinalAccount getFinalAccount(@PathVariable String jobNo, @PathVariable String addendumNo, @PathVariable Long addendumId){
		return finalAccountService.getFinalAccount(jobNo, addendumNo, addendumId);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('FinalAccountController','createFinalAccount', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createFinalAccount", method = RequestMethod.POST)
	public String createFinalAccount(@RequestParam(required = true) String jobNo,
									 @RequestParam(required = true) String addendumNo,
									 @RequestParam(required = true) Long addendumId,
									 @RequestBody FinalAccount finalAccount){
		String result = "";
		try{
			result = finalAccountService.createFinalAccount(jobNo, addendumNo, addendumId, finalAccount);
		}catch(Exception e){
			result  = "Final Account cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('FinalAccountController','getFinalAccountAdmin', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getFinalAccountAdmin/{jobNo}/{subcontractNo}/{addendumNo}", method = RequestMethod.GET)
	public FinalAccount getFinalAccountAdmin(@PathVariable String jobNo, @PathVariable String subcontractNo, @PathVariable String addendumNo){
		return finalAccountService.getFinalAccountAdmin(jobNo, subcontractNo, addendumNo);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('FinalAccountController','updateFinalAccountAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateFinalAccountAdmin", method = RequestMethod.POST)
	public String updateFinalAccountAdmin(@RequestParam(required = true) String jobNo,
									 @RequestParam(required = true) String subcontractNo,
									 @RequestParam(required = true) String addendumNo,
									 @RequestBody FinalAccount finalAccount){
		String result = "";
		try{
			result = finalAccountService.updateFinalAccountAdmin(jobNo, subcontractNo, addendumNo, finalAccount);
		}catch(Exception e){
			result  = "Final Account cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','deleteFinalAccountAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteFinalAccountAdmin", method = RequestMethod.POST)
	public String deleteFinalAccountAdmin(@RequestParam(required = true) String jobNo,
										  @RequestParam(required = true) String subcontractNo,
										  @RequestParam(required = true) String addendumNo,
										  @RequestBody FinalAccount finalAccount){
		String result = "";
		try{
			result = finalAccountService.deleteFinalAccountAdmin(jobNo, subcontractNo, addendumNo, finalAccount);
		}catch(Exception e){
			result  = "Final Account cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}


}
