/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * TenderController.java
 * @since Jul 4, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.service.TenderService;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/tender/")
public class TenderController {
//	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TenderService tenderService;
	
	
	@RequestMapping(value = "getTenderDetailList", method = RequestMethod.GET)
	public List<TenderDetail> getTenderDetailList(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{

		List<TenderDetail> tenderDetailList = null;
		tenderDetailList = tenderService.obtainTenderDetailList(jobNo, subcontractNo, subcontractorNo);
		return tenderDetailList;
	}
	
	@RequestMapping(value = "getTender", method = RequestMethod.GET)
	public Tender getTender(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{

		Tender tender = null;
		tender = tenderService.obtainTender(jobNo, subcontractNo, subcontractorNo);
		return tender;
	}
	
	@RequestMapping(value = "getRecommendedTender", method = RequestMethod.GET)
	public Tender getRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{

		Tender tender = null;
		//VendorNo: 0 is excluded 
		tender = tenderService.obtainRecommendedTender(jobNo, subcontractNo);
		return tender;
	}
	
	@RequestMapping(value = "getTenderList", method = RequestMethod.GET)
	public List<Tender> getTenderList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{

		List<Tender> tenderList = null;
		//VendorNo: 0 is excluded 
		tenderList = tenderService.obtainTenderList(jobNo, subcontractNo);
		return tenderList;
	}
	
	@RequestMapping(value = "getTenderComparisonList", method = RequestMethod.GET)
	public TenderAnalysisComparisonWrapper getTenderComparisonList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{

		TenderAnalysisComparisonWrapper wrapper = null;
		wrapper = tenderService.obtainTenderComparisonList(jobNo, subcontractNo);
		return wrapper;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createTender", method = RequestMethod.POST)
	public String createTender(@RequestParam(name="jobNo") String jobNo, 
								@RequestParam(name="subcontractNo") String subcontractNo,
								@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{

		String result = "";
		result = tenderService.createTender(jobNo, subcontractNo, subcontractorNo);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateRecommendedTender", method = RequestMethod.POST)
	public String updateRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo){
		String result = "";
		result = tenderService.updateRecommendedTender(jobNo, subcontractNo, subcontractorNo);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateTenderDetails", method = RequestMethod.POST)
	public String updateTenderDetails(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo,
										@RequestParam(name="currencyCode") String currencyCode,
										@RequestParam(name="exchangeRate") Double exchangeRate,
										@RequestParam(name="remarks") String remarks,
										@RequestParam(name="statusChangeExecutionOfSC") String statusChangeExecutionOfSC,
										@RequestParam(name="validate") Boolean validate,
										@Valid @RequestBody List<TenderDetail> taDetails) throws Exception{
		String result = "";
		result = tenderService.updateTenderAnalysisDetails(jobNo, subcontractNo, subcontractorNo, currencyCode, exchangeRate, remarks, statusChangeExecutionOfSC, taDetails, validate);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteTender", method = RequestMethod.POST)
	public String deleteTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{
		String result = "";
		result = tenderService.deleteTender(jobNo, subcontractNo, subcontractorNo);
		return result;
	}
	
	
}

