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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "service/tender/")
public class TenderController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TenderService tenderService;
	
	
	@RequestMapping(value = "getTenderDetailList", method = RequestMethod.GET)
	public List<TenderDetail> getTenderDetailList(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo){

		List<TenderDetail> tenderDetailList = null;
		try {
			
			tenderDetailList = tenderService.obtainTenderDetailList(jobNo, subcontractNo, subcontractorNo);
			logger.info("tenderDetailList: "+tenderDetailList.size());;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tenderDetailList;
	}
	
	@RequestMapping(value = "getTender", method = RequestMethod.GET)
	public Tender getTender(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo){

		Tender tender = null;
		try {
			
			tender = tenderService.obtainTender(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tender;
	}
	
	@RequestMapping(value = "getRecommendedTender", method = RequestMethod.GET)
	public Tender getRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo){

		Tender tender = null;
		try {
			//VendorNo: 0 is excluded 
			tender = tenderService.obtainRecommendedTender(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tender;
	}
	
	@RequestMapping(value = "getTenderList", method = RequestMethod.GET)
	public List<Tender> getTenderList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo){

		List<Tender> tenderList = null;
		try {
			//VendorNo: 0 is excluded 
			tenderList = tenderService.obtainTenderList(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tenderList;
	}
	
	@RequestMapping(value = "getTenderComparisonList", method = RequestMethod.GET)
	public TenderAnalysisComparisonWrapper getTenderComparisonList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo){

		TenderAnalysisComparisonWrapper wrapper = null;
		try {
			wrapper = tenderService.obtainTenderComparisonList(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper;
	}
	
	
	@RequestMapping(value = "createTender", method = RequestMethod.POST)
	public String createTender(@RequestParam(name="jobNo") String jobNo, 
								@RequestParam(name="subcontractNo") String subcontractNo,
								@RequestParam(name="subcontractorNo") Integer subcontractorNo){

		String result = "";
		try {
			
			result = tenderService.createTender(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "updateRecommendedTender", method = RequestMethod.POST)
	public String updateRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo){
		String result = "";
		try {
			result = tenderService.updateRecommendedTender(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "updateTenderDetails", method = RequestMethod.POST)
	public String updateTenderDetails(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo,
										@RequestParam(name="currencyCode") String currencyCode,
										@RequestParam(name="exchangeRate") Double exchangeRate,
										@RequestParam(name="validate") Boolean validate,
										@Valid @RequestBody List<TenderDetail> taDetails){
		String result = "";
		try {
			result = tenderService.updateTenderAnalysisDetails(jobNo, subcontractNo, subcontractorNo, currencyCode, exchangeRate, taDetails, validate);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "deleteTender", method = RequestMethod.POST)
	public String deleteTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo){
		String result = "";
		try {
			result = tenderService.deleteTender(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	
}

