/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * TenderController.java
 * @since Jul 4, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import com.gammon.pcms.dto.rs.provider.response.ta.TenderComparisonDTO;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.service.TenderVarianceService;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.TenderService;
import com.gammon.qs.service.admin.MailContentGenerator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "service/tender/")
public class TenderController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TenderService tenderService;
	@Autowired
	private TenderVarianceService tenderVarianceService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private MailContentGenerator mailContentGenerator;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getTenderDetailList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTenderDetailList", method = RequestMethod.GET)
	public List<TenderDetail> getTenderDetailList(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{

		List<TenderDetail> tenderDetailList = null;
		tenderDetailList = tenderService.obtainTenderDetailList(jobNo, subcontractNo, subcontractorNo);
		return tenderDetailList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getTender', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTender", method = RequestMethod.GET)
	public Tender getTender(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{

		Tender tender = null;
		tender = tenderService.obtainTender(jobNo, subcontractNo, subcontractorNo);
		return tender;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getRecommendedTender', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRecommendedTender", method = RequestMethod.GET)
	public Tender getRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{

		Tender tender = null;
		//VendorNo: 0 is excluded 
		tender = tenderService.obtainRecommendedTender(jobNo, subcontractNo);
		return tender;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getTenderList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTenderList", method = RequestMethod.GET)
	public List<Tender> getTenderList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{

		List<Tender> tenderList = null;
		//VendorNo: 0 is excluded 
		tenderList = tenderService.obtainTenderList(jobNo, subcontractNo);
		return tenderList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getTenderComparisonList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTenderComparisonList", method = RequestMethod.GET)
	public TenderComparisonDTO getTenderComparisonList(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo) throws Exception{
		return  tenderService.obtainTenderComparisonList(jobNo, subcontractNo);

	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getUneditableTADetailIDs', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getUneditableTADetailIDs", method = RequestMethod.GET)
	public List<String> getUneditableTADetailIDs(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="tenderNo") Integer tenderNo
										) throws Exception{
		return  tenderService.getUneditableTADetailIDs(jobNo, subcontractNo, tenderNo);

	}

	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','createTender', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createTender", method = RequestMethod.POST)
	public String createTender(@RequestParam(required = true) String jobNo, 
								@RequestParam(required = true) String subcontractNo,
								@RequestParam(required = false) Integer subcontractorNo, 
								@RequestParam(required = false) String subcontractorName
								) throws Exception{

		String result = "";
		result = tenderService.createTender(jobNo, subcontractNo, subcontractorNo, subcontractorName);
		return result;
	}
	
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','updateRecommendedTender', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateRecommendedTender", method = RequestMethod.POST)
	public String updateRecommendedTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo){
		String result = "";
		result = tenderService.updateRecommendedTender(jobNo, subcontractNo, subcontractorNo);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','updateTenderDetails', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateTenderDetails", method = RequestMethod.POST)
	public String updateTenderDetails(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo,
										@RequestParam(name="currencyCode") String currencyCode,
										@RequestParam(name="exchangeRate") Double exchangeRate,
										@RequestParam(name="remarks") String remarks,
										@RequestParam(name="validTenderer") String validTenderer,
										@RequestParam(name="latestBudgetForecast") BigDecimal latestBudgetForecast,
										@RequestParam(name="validate") Boolean validate,
										@Valid @RequestBody List<TenderDetail> taDetails) throws Exception{
		String result = "";
		result = tenderService.updateTenderAnalysisDetails(jobNo, subcontractNo, subcontractorNo, currencyCode, exchangeRate, remarks, validTenderer, latestBudgetForecast, taDetails, validate);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('TenderController','updateTenderDetailAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateTenderDetailAdmin", method = RequestMethod.POST)
	public void updateTenderDetailAdmin(@RequestBody TenderDetail tenderDetail) throws Exception {
		if((tenderDetail).getId() == null) throw new IllegalArgumentException("Invalid Tender Detail");
		String result = jobInfoService.canAdminJob(tenderDetail.getTender().getJobNo());
		if(StringUtils.isEmpty(result)){
			tenderService.updateTenderDetailAdmin(tenderDetail);
			mailContentGenerator.sendAdminFnEmail("updateTenderDetailAdmin", tenderDetail.toString());
		} else {
			throw new IllegalAccessException(result);
		}
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('TenderController','updateTenderDetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateTenderDetailListAdmin", method = RequestMethod.POST)
	public void updateTenderDetailListAdmin(@RequestBody List<TenderDetail> tenderDetailList) throws Exception {
		String jobNumber = tenderDetailList.get(0).getTender().getJobNo();
		String result = jobInfoService.canAdminJob(jobNumber);
		if(StringUtils.isEmpty(result)){
			tenderDetailList.forEach(tenderDetail -> {
				try {
					updateTenderDetailAdmin(tenderDetail);
				} catch (Exception e) {
					logger.error("error", e);
				}
			});
		} else {
			throw new IllegalAccessException(result);
		}
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('TenderController','updateTenderAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateTenderAdmin", method = RequestMethod.POST)
	public String updateTenderAdmin(@RequestBody Tender tender) throws Exception {
		if(tender.getId() == null) throw new IllegalArgumentException("Invalid Tender");
		String result = jobInfoService.canAdminJob(tender.getJobNo());
		if(StringUtils.isEmpty(result)){
			result = tenderService.updateTenderAdmin(tender);
			mailContentGenerator.sendAdminFnEmail("updateTenderAdmin", tender.toString());
		} else {
			throw new IllegalAccessException(result);
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','deleteTender', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteTender", method = RequestMethod.POST)
	public String deleteTender(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="subcontractorNo") Integer subcontractorNo) throws Exception{
		String result = "";
		result = tenderService.deleteTender(jobNo, subcontractNo, subcontractorNo);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','getTenderVarianceList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTenderVarianceList", method = RequestMethod.GET)
	public List<TenderVariance> getTenderVarianceList(@RequestParam(name="jobNo") String jobNo, 
														@RequestParam(name="subcontractNo") String subcontractNo,
														@RequestParam(name="subcontractorNo") String subcontractorNo){
		List<TenderVariance> tenderVarianceList = null;
		tenderVarianceList = tenderVarianceService.obtainTenderVarianceList(jobNo, subcontractNo, subcontractorNo);
		return tenderVarianceList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TenderController','createTenderVariance', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createTenderVariance", method = RequestMethod.POST)
	public String createTenderVariance(@RequestParam(name="jobNo") String jobNo, 
									@RequestParam(name="subcontractNo") String subcontractNo,
									@RequestParam(name="subcontractorNo") String subcontractorNo,
									@Valid @RequestBody List<TenderVariance> tenderVarianceList) throws Exception{
		String result = "";
		result = tenderVarianceService.createTenderVariance(jobNo, subcontractNo, subcontractorNo, tenderVarianceList);
		return result;
	}
}
