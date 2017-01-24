/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.ResourceSummarySplitMergeWrapper;

@RestController
@RequestMapping(value = "service/resourceSummary/")
public class ResourceSummaryController {
	
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummaries', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummaries", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummaries(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo, 
													@RequestParam(name="objectCode") String objectCode) throws Exception{
		List<ResourceSummary> resourceSummaries = null;
		resourceSummaries = resourceSummaryService.getResourceSummaries(jobNo, subcontractNo, objectCode);
		return resourceSummaries;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesForAddendum', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesForAddendum", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesForAddendum(@RequestParam(required =true) String jobNo){
		List<ResourceSummary> resourceSummaries = null;
		resourceSummaries = resourceSummaryService.getResourceSummariesForAddendum(jobNo);
		return resourceSummaries;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','obtainResourceSummariesByJobNumberForAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "obtainResourceSummariesByJobNumberForAdmin", method = RequestMethod.GET)
	public List<ResourceSummary> obtainResourceSummariesByJobNumberForAdmin(@RequestParam String jobNumber){
		return resourceSummaryService.obtainResourceSummariesByJobNumberForAdmin(jobNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesBySC', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesBySC", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesBySC(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo 
													) throws Exception{
		List<ResourceSummary> resourceSummaries = null;
		resourceSummaries = resourceSummaryService.getResourceSummariesBySC(jobNo, subcontractNo);
		return resourceSummaries;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesByAccountCode', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesByAccountCode", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesByAccountCode(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="objectCode") String objectCode,
													@RequestParam(name="subsidiaryCode") String subsidiaryCode
													) throws Exception{
		List<ResourceSummary> resourceSummaries = null;
		resourceSummaries = resourceSummaryService.getResourceSummariesByAccountCode(jobNo, subcontractNo, objectCode, subsidiaryCode);
		return resourceSummaries;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesByLineType', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesByLineType", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesByLineType(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="objectCode") String objectCode,
													@RequestParam(name="subsidiaryCode") String subsidiaryCode,
													@RequestParam(name="lineType") String lineType,
													@RequestParam(name="resourceNo") Integer resourceNo
													){
		List<ResourceSummary> resourceSummaries = null;
		resourceSummaries = resourceSummaryService.getResourceSummariesByLineType(jobNo, subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo);
		return resourceSummaries;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesForIV', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesForIV", method = RequestMethod.GET)
	public IVInputPaginationWrapper getResourceSummariesForIV(@RequestParam(required =true) String jobNo) throws DatabaseOperationException{
		IVInputPaginationWrapper ivInputPaginationWrapper = null;
		ivInputPaginationWrapper = resourceSummaryService.obtainResourceSummariesForIV(jobNo);
		return ivInputPaginationWrapper;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getResourceSummariesGroupByObjectCode', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getResourceSummariesGroupByObjectCode", method = RequestMethod.GET)
	public List<ResourceSummayDashboardDTO> getResourceSummariesGroupByObjectCode(@RequestParam(required =true) String jobNo){
		List<ResourceSummayDashboardDTO> rsList = null;
		rsList = resourceSummaryService.getResourceSummariesGroupByObjectCode(jobNo);
		return rsList;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','getUneditableResourceSummaryID', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getUneditableResourceSummaryID", method = RequestMethod.GET)
	public List<Integer> getUneditableResourceSummaryID(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<Integer> rsList = null;
		try {
			rsList = resourceSummaryService.getUneditableResourceSummaryID(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','addResourceSummary', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addResourceSummary", method = RequestMethod.POST)
	public String addResourceSummary(@RequestParam(required =true) String jobNo, 
									@RequestParam(required =false) String repackagingId,
									 @RequestBody ResourceSummary resourceSummary) throws NumberFormatException, Exception{
		String result = "";
		try {
			result = resourceSummaryService.addResourceSummary(jobNo, repackagingId, resourceSummary);
		} catch (Exception e) {
			result = "Failed to add resource summary.";
			e.printStackTrace();
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','deleteResources', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteResources", method = RequestMethod.POST)
	public String deleteResources( @RequestBody List<ResourceSummary> resourceSummaryList) throws Exception{
		String result = "";
		result = resourceSummaryService.deleteResources(resourceSummaryList);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','updateResourceSummaries', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateResourceSummaries", method = RequestMethod.POST)
	public String updateResourceSummaries(@RequestParam(required =true) String jobNo, 
									 @RequestBody List<ResourceSummary> resourceSummaryList) throws Exception{
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		wrapper = resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
		return wrapper.getError();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','updateResourceSummariesForAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateResourceSummariesForAdmin", method = RequestMethod.POST)
	public void updateResourceSummariesForAdmin(@RequestParam(required =true) String jobNo, 
									 @RequestBody List<ResourceSummary> resourceSummaryList) throws Exception{
		resourceSummaryService.updateResourceSummariesForAdmin(resourceSummaryList, jobNo);
	}
		
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','splitOrMergeResources', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "splitOrMergeResources", method = RequestMethod.POST)
	public String splitOrMergeResources(@RequestParam(required =true) String jobNo, 
										 @RequestBody ResourceSummarySplitMergeWrapper resourceSummarySplitMergeWrapper) throws NumberFormatException, Exception{
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		wrapper = resourceSummaryService.splitOrMergeResources(resourceSummarySplitMergeWrapper.getOldResourceSummaryList(), resourceSummarySplitMergeWrapper.getNewResourceSummaryList(), jobNo);
		return wrapper.getError();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','updateIVAmount', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateIVAmount", method = RequestMethod.POST)
	public String updateIVAmount(@RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try {
			result = resourceSummaryService.updateIVAmount(resourceSummaryList);
		} catch (Exception e) {
			result = "IV cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','postIVAmounts', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "postIVAmounts", method = RequestMethod.POST)
	public String postIVAmounts(@RequestParam(required =true) String jobNo, @RequestParam(required =true) boolean finalized){
		String result = "";
		try {
			result = resourceSummaryService.postIVAmounts(jobNo, finalized);
		} catch (Exception e) {
			result = "IV cannot be posted.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','generateResourceSummaries', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "generateResourceSummaries", method = RequestMethod.POST)
	public String generateResourceSummaries(@RequestParam(required =true) String jobNo){
		String result = "";
		try {
			result = resourceSummaryService.generateResourceSummaries(jobNo);
		} catch (Exception e) {
			result = "Resource Summaries cannot be generated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		}
		return result;
	}

	
	@PreAuthorize(value = "@GSFService.isFnEnabled('ResourceSummaryController','updateIVForSubcontract', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateIVForSubcontract", method = RequestMethod.POST)
	public String updateIVForSubcontract(@RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try {
			result = resourceSummaryService.updateIVForSubcontract(resourceSummaryList);
		} catch (Exception e) {
			result = "IV cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
}

