package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.service.RocService;
import com.gammon.pcms.wrapper.RocWrapper;
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
import java.util.List;

@RestController
@RequestMapping(value = "service/roc/")
public class RocController {
	
	@Autowired
	private RocService rocService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocListSummary', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocListSummary/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public List<RocWrapper> getRocListSummary(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month){
		return rocService.getRocWrapperList(jobNo, year, month);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocClassDescMap', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocClassDescMap", method = RequestMethod.GET)
	public List<ROC_CLASS_DESC_MAP> getRocClassDescMap(){
		return rocService.getRocClassDescMap();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocCategoryList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocCategoryList", method = RequestMethod.GET)
	public List<String> getRocCategoryList(){
		return rocService.getRocCategoryList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getImpactList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getImpactList", method = RequestMethod.GET)
	public List<String> getImpactList(){
		return rocService.getImpactList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getStatusList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getStatusList", method = RequestMethod.GET)
	public List<String> getStatusList(){
		return rocService.getStatusList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','addRoc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "addRoc", method = RequestMethod.POST)
	public String addRoc(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocService.addRoc(jobNo, roc);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','updateRoc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateRoc", method = RequestMethod.POST)
	public String updateRoc(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocService.updateRoc(jobNo, roc);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','saveRocDetails', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveRocDetails", method = RequestMethod.POST)
	public String saveRocDetails(@RequestParam(required = true) String jobNo, @RequestBody List<RocWrapper> rocWrapperList){
		String result = "";
		try{
			result = rocService.saveRocDetails(jobNo, rocWrapperList);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}


}
