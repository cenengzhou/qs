/**
 * com.gammon.pcms.web.controller
 * AddendumController.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.pcms.wrapper.AddendumFinalFormWrapper;
import com.gammon.pcms.wrapper.Form2SummaryWrapper;
import com.gammon.pcms.wrapper.ResourceSummaryWrapper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.service.AddendumService;
import com.gammon.qs.service.admin.MailContentGenerator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "service/addendum/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class AddendumController {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AddendumService addendumService;

	@Autowired
	private MailContentGenerator mailContentGenerator;

	@PreAuthorize(value = "@GSFService.isRoleExisted('AddendumController','getAddendumFinalForm', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendumFinalForm", method = RequestMethod.GET)
	public AddendumFinalFormWrapper getAddendumFinalForm(@RequestParam(required = true) String jobNo,
														 @RequestParam(required = true) String subcontractNo,
														 @RequestParam(required = true) String addendumNo){
		return addendumService.getAddendumFinalForm(jobNo, subcontractNo, addendumNo);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getLatestAddendum', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getLatestAddendum", method = RequestMethod.GET)
	public Addendum getLatestAddendum(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		Addendum addendum = null;
		addendum = addendumService.getLatestAddendum(jobNo, subcontractNo);
		return addendum;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAddendum', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendum", method = RequestMethod.GET)
	public Addendum getAddendum(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo, @RequestParam(required = true) String addendumNo){
		Addendum addendum = null;
		addendum = addendumService.getAddendum(jobNo, subcontractNo, Long.valueOf(addendumNo));
		return addendum;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAddendumList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendumList", method = RequestMethod.GET)
	public List<Addendum> getAddendumList(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		List<Addendum> addendumList = null;
		addendumList = addendumService.getAddendumList(jobNo, subcontractNo);
		return addendumList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getTotalApprovedAddendumAmount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTotalApprovedAddendumAmount", method = RequestMethod.GET)
	public Double getTotalApprovedAddendumAmount(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo){
		Double totalApprovedAddendumAmount = null;
		totalApprovedAddendumAmount = addendumService.getTotalApprovedAddendumAmount(jobNo, subcontractNo);
		return totalApprovedAddendumAmount;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAddendumDetailHeader', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendumDetailHeader", method = RequestMethod.GET)
	public AddendumDetail getAddendumDetailHeader(BigDecimal addendumDetailHeaderRef){
		AddendumDetail addendumDetail = null;
		addendumDetail = addendumService.getAddendumDetailHeader(addendumDetailHeaderRef);
		return addendumDetail;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAddendumDetailsByHeaderRef', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendumDetailsByHeaderRef", method = RequestMethod.GET)
	public List<AddendumDetail> getAddendumDetailsByHeaderRef(BigDecimal addendumDetailHeaderRef){
		List<AddendumDetail> addendumDetailList = null;
		addendumDetailList = addendumService.getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
		return addendumDetailList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAddendumDetailsWithoutHeaderRef', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAddendumDetailsWithoutHeaderRef", method = RequestMethod.GET)
	public List<AddendumDetail> getAddendumDetailsWithoutHeaderRef(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) String addendumNo){
		List<AddendumDetail> addendumDetailList = null;
		addendumDetailList = addendumService.getAddendumDetailsWithoutHeaderRef(jobNo, subcontractNo, Long.valueOf(addendumNo));
		return addendumDetailList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAllAddendumDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAllAddendumDetails", method = RequestMethod.GET)
	public List<AddendumDetail> getAllAddendumDetails(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo, 
												@RequestParam(required = true) String addendumNo){
		List<AddendumDetail> addendumDetailList = null;
		addendumDetailList = addendumService.getAllAddendumDetails(jobNo, subcontractNo, Long.valueOf(addendumNo));
		return addendumDetailList;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getAllAddendumDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getForm2Summary", method = RequestMethod.GET)
	public Form2SummaryWrapper getForm2Summary(@RequestParam(required = true) String jobNo,
											   @RequestParam(required = true) String subcontractNo,
											   @RequestParam(required = true) String addendumNo) {
		return addendumService.getForm2Summary(jobNo, subcontractNo, Long.valueOf(addendumNo));
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','getDefaultValuesForAddendumDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getDefaultValuesForAddendumDetails", method = RequestMethod.GET)
	public AddendumDetail getDefaultValuesForAddendumDetails(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo, 
												@RequestParam(required = true) String addendumNo,
												@RequestParam(required = true) String lineType,
												@RequestParam(required = false) Integer nextSeqNo
												) throws NumberFormatException, DatabaseOperationException{
		AddendumDetail addendumDetail = null;
		addendumDetail = addendumService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, Long.valueOf(addendumNo), lineType, nextSeqNo);
		return addendumDetail;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','createAddendum', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createAddendum", method = RequestMethod.POST)
	public String createAddendum(@RequestBody Addendum addendum){
		String result = "";
		try{
			result = addendumService.createAddendum(addendum);
		}catch(Exception e){
			result  = "Addendum cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('AddendumController','updateAddendumAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateAddendumAdmin", method = RequestMethod.POST)
	public String updateAddendumAdmin(@RequestBody Addendum addendum) {
		String result = updateAddendum(addendum);
		mailContentGenerator.sendAdminFnEmail("updateAddendumAdmin", addendum.toString());
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','updateAddendum', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateAddendum", method = RequestMethod.POST)
	public String updateAddendum(@RequestBody Addendum addendum){
		String result = "";
		try{
			result = addendumService.updateAddendum(addendum);
		}catch(Exception e){
			result  = "Addendum cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','updateAddendumDetailHeader', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateAddendumDetailHeader", method = RequestMethod.POST)
	public String updateAddendumDetailHeader(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) String addendumNo, 
												@RequestParam(required = true) String addendumDetailHeaderRef,
												@RequestParam(required = true) String description 
												){
		String result = "";
		try{
			result = addendumService.updateAddendumDetailHeader(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, description);
		}catch(Exception e){
			result  = "Addendum Detail Header cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','deleteAddendumDetailHeader', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAddendumDetailHeader", method = RequestMethod.POST)
	public String deleteAddendumDetailHeader(@RequestParam(required = true)BigDecimal addendumDetailHeaderRef){
		String result = "";
		try{
			result = addendumService.deleteAddendumDetailHeader(addendumDetailHeaderRef);
		}catch(Exception e){
			result  = "Addendum Detail Header cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','deleteAddendumDetail', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAddendumDetail", method = RequestMethod.POST)
	public String deleteAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo,
										@RequestBody List<AddendumDetail> addendumDetailList){
		String result = "";
		try{
			result = addendumService.deleteAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailList);
		}catch(Exception e){
			result  = "Addendum Detail cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','addAddendumDetail', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addAddendumDetail", method = RequestMethod.POST)
	public String addAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestBody AddendumDetail addendumDetail){
		String result = "";
		try{
			result = addendumService.addAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetail);
		}catch(Exception e){
			result  = "Addendum Detail cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','updateAddendumDetail', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateAddendumDetail", method = RequestMethod.POST)
	public String updateAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestBody AddendumDetail addendumDetail){
		String result = "";
		try{
			result = addendumService.updateAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetail);
		}catch(Exception e){
			result  = "Addendum Detail cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('AddendumController','updateAddendumDetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateAddendumDetailListAdmin", method = RequestMethod.POST)
	public String updateAddendumDetailListAdmin(@RequestBody List<AddendumDetail> addendumDetailList) {
		String result = updateAddendumDetailList(addendumDetailList);
		mailContentGenerator.sendAdminFnEmail("updateAddendumDetailListAdmin", addendumDetailList.toString());
		return result;
	}
		
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','updateAddendumDetail', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateAddendumDetailList", method = RequestMethod.POST)
	public String updateAddendumDetailList(@RequestBody List<AddendumDetail> addendumDetailList){
		StringBuilder result = new StringBuilder();
		addendumDetailList.forEach(addendumDetail -> {
			String jobNo = addendumDetail.getNoJob();
			String subcontractNo = addendumDetail.getNoSubcontract();
			Long addendumNo = addendumDetail.getNo();
			String error = addendumService.updateAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail);
			if(error != null) result.append(error);
		});
		return result.toString();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','addAddendumFromResourceSummaries', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addAddendumFromResourceSummaries", method = RequestMethod.POST)
	public String addAddendumFromResourceSummaries(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestParam(required = false)BigDecimal addendumDetailHeaderRef,
										@RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try{
			result = addendumService.addAddendumFromResourceSummaries(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, resourceSummaryList, null);
		}catch(Exception e){
			result  = "Addendum Detail cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','addAddendumFromResourceSummaries', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addAddendumFromResourceSummaryAndAddendumDetail", method = RequestMethod.POST)
	public String addAddendumFromResourceSummaryAndAddendumDetail(@RequestParam(required = true) String jobNo,
												   @RequestParam(required = true) String subcontractNo,
												   @RequestParam(required = true) String addendumNo,
												   @RequestParam(required = false)BigDecimal addendumDetailHeaderRef,
												   @RequestBody ResourceSummaryWrapper resourceSummaryWrapper){
		String result = "";
		try{
			ResourceSummary resourceSummary = resourceSummaryWrapper.getResourceSummary();
			AddendumDetail addendumDetail = resourceSummaryWrapper.getAddendumDetail();
			result = addendumService.addAddendumFromResourceSummaryAndAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, resourceSummary, addendumDetail);
		}catch(Exception e){
			result  = "Addendum Detail cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','deleteAddendumFromSCDetails', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAddendumFromSCDetails", method = RequestMethod.POST)
	public String deleteAddendumFromSCDetails(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestParam(required = false)BigDecimal addendumDetailHeaderRef,
										@RequestBody List<SubcontractDetail> subcontractDetailList){
		String result = "";
		try{
			result = addendumService.deleteAddendumFromSCDetails(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, subcontractDetailList);
		}catch(Exception e){
			result  = "Addendum Detail cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AddendumController','submitAddendumApproval', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitAddendumApproval", method = RequestMethod.POST)
	public String submitAddendumApproval(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo){
		String result = "";
		try{
			result = addendumService.submitAddendumApproval(jobNo, subcontractNo, Long.valueOf(addendumNo));
		}catch(Exception e){
			result  = "Addendum failed to be submitted to Approval System.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('AddendumController','enquireAddendumList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "enquireAddendumList", method = RequestMethod.POST)
	public List<Addendum> enquireAddendumList(@RequestParam(required = false) String jobNo,
											  @RequestParam(required = false) String subcontractNo,
											  @RequestBody Map<String, String> commonKeyValue) throws DatabaseOperationException{
		List<Addendum> result = new ArrayList<>();
		try {
			result = addendumService.enquireAddendumList(jobNo, commonKeyValue);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}
	
}
