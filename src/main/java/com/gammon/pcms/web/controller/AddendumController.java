/**
 * com.gammon.pcms.web.controller
 * AddendumController.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.service.AddendumService;

@RestController
@RequestMapping(value = "service/addendum/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class AddendumController {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AddendumService addendumService;


	@RequestMapping(value = "getLatestAddendum", method = RequestMethod.GET)
	public Addendum getLatestAddendum(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		Addendum addendum = null;
		try{
			addendum = addendumService.getLatestAddendum(jobNo, subcontractNo);
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendum;
	}

	@RequestMapping(value = "getAddendum", method = RequestMethod.GET)
	public Addendum getAddendum(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo, @RequestParam(required = true) String addendumNo){
		Addendum addendum = null;
		try{
			addendum = addendumService.getAddendum(jobNo, subcontractNo, Long.valueOf(addendumNo));
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendum;
	}

	@RequestMapping(value = "getAddendumList", method = RequestMethod.GET)
	public List<Addendum> getAddendumList(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		List<Addendum> addendumList = null;
		try{
			addendumList = addendumService.getAddendumList(jobNo, subcontractNo);
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendumList;
	}
	
	@RequestMapping(value = "getTotalApprovedAddendumAmount", method = RequestMethod.GET)
	public Double getTotalApprovedAddendumAmount(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo){
		Double totalApprovedAddendumAmount = null;
		try{
			totalApprovedAddendumAmount = addendumService.getTotalApprovedAddendumAmount(jobNo, subcontractNo);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return totalApprovedAddendumAmount;
	}

	@RequestMapping(value = "getAddendumDetailHeader", method = RequestMethod.GET)
	public AddendumDetail getAddendumDetailHeader(BigDecimal addendumDetailHeaderRef){
		AddendumDetail addendumDetail = null;
		try{
			addendumDetail = addendumService.getAddendumDetailHeader(addendumDetailHeaderRef);
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendumDetail;
	}
	
	@RequestMapping(value = "getAddendumDetailsByHeaderRef", method = RequestMethod.GET)
	public List<AddendumDetail> getAddendumDetailsByHeaderRef(BigDecimal addendumDetailHeaderRef){
		List<AddendumDetail> addendumDetailList = null;
		try{
			addendumDetailList = addendumService.getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendumDetailList;
	}
	
	@RequestMapping(value = "getAddendumDetailsWithoutHeaderRef", method = RequestMethod.GET)
	public List<AddendumDetail> getAddendumDetailsWithoutHeaderRef(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) String addendumNo){
		List<AddendumDetail> addendumDetailList = null;
		try{
			addendumDetailList = addendumService.getAddendumDetailsWithoutHeaderRef(jobNo, subcontractNo, Long.valueOf(addendumNo));
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendumDetailList;
	}
	
	@RequestMapping(value = "getAllAddendumDetails", method = RequestMethod.GET)
	public List<AddendumDetail> getAllAddendumDetails(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo, 
												@RequestParam(required = true) String addendumNo){
		List<AddendumDetail> addendumDetailList = null;
		try{
			addendumDetailList = addendumService.getAllAddendumDetails(jobNo, subcontractNo, Long.valueOf(addendumNo));
		}catch(DataAccessException ex){
			ex.printStackTrace();
		}
		return addendumDetailList;
	}
	
	

	@RequestMapping(value = "getDefaultValuesForAddendumDetails", method = RequestMethod.GET)
	public AddendumDetail getDefaultValuesForAddendumDetails(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String subcontractNo, 
												@RequestParam(required = true) String lineType
												){
		AddendumDetail addendumDetail = null;
		try{
			addendumDetail = addendumService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, lineType);
		}catch(DatabaseOperationException ex){
			ex.printStackTrace();
		}
		return addendumDetail;
	}
	
	@RequestMapping(value = "createAddendum", method = RequestMethod.POST)
	public String createAddendum(@RequestBody Addendum addendum){
		String result = "";
		try{
			result = addendumService.createAddendum(addendum);
		}catch(Exception exception){
			result  = "Addendum cannot be created.";
			exception.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "updateAddendum", method = RequestMethod.POST)
	public String updateAddendum(@RequestBody Addendum addendum){
		String result = "";
		try{
			result = addendumService.updateAddendum(addendum);
		}catch(Exception exception){
			result  = "Addendum cannot be updated.";
			exception.printStackTrace();
		}
		return result;
	}
	
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
		}catch(Exception exception){
			result  = "Addendum Detail Header cannot be updated.";
			exception.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping(value = "deleteAddendumDetailHeader", method = RequestMethod.POST)
	public String deleteAddendumDetailHeader(@RequestParam(required = true)BigDecimal addendumDetailHeaderRef){
		String result = "";
		try{
			result = addendumService.deleteAddendumDetailHeader(addendumDetailHeaderRef);
		}catch(Exception exception){
			result  = "Addendum Detail Header cannot be deleted.";
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "deleteAddendumDetail", method = RequestMethod.POST)
	public String deleteAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo,
										@RequestBody List<AddendumDetail> addendumDetailList){
		String result = "";
		try{
			result = addendumService.deleteAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailList);
		}catch(Exception exception){
			result  = "Addendum Detail Header cannot be deleted.";
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "addAddendumDetail", method = RequestMethod.POST)
	public String addAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestBody AddendumDetail addendumDetail){
		String result = "";
		try{
			result = addendumService.addAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetail);
		}catch(Exception exception){
			result  = "Addendum Detail cannot be created.";
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "updateAddendumDetail", method = RequestMethod.POST)
	public String updateAddendumDetail(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestBody AddendumDetail addendumDetail){
		String result = "";
		try{
			result = addendumService.updateAddendumDetail(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetail);
		}catch(Exception exception){
			result  = "Addendum Detail cannot be updated.";
			exception.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping(value = "addAddendumFromResourceSummaries", method = RequestMethod.POST)
	public String addAddendumFromResourceSummaries(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestParam(required = true)BigDecimal addendumDetailHeaderRef,
										@RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try{
			result = addendumService.addAddendumFromResourceSummaries(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, resourceSummaryList);
		}catch(Exception exception){
			result  = "Addendum Detail cannot be created.";
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "deleteAddendumFromSCDetails", method = RequestMethod.POST)
	public String deleteAddendumFromSCDetails(@RequestParam(required = true) String jobNo, 
										@RequestParam(required = true) String subcontractNo,
										@RequestParam(required = true) String addendumNo, 
										@RequestParam(required = true)BigDecimal addendumDetailHeaderRef,
										@RequestBody List<SubcontractDetail> subcontractDetailList){
		String result = "";
		try{
			result = addendumService.deleteAddendumFromSCDetails(jobNo, subcontractNo, Long.valueOf(addendumNo), addendumDetailHeaderRef, subcontractDetailList);
		}catch(Exception exception){
			result  = "Addendum Detail cannot be created.";
			exception.printStackTrace();
		}
		return result;
	}
	
	

}
