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

import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.service.TenderService;

@RestController
@RequestMapping(value = "service/tender/")
public class TenderController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TenderService tenderService;
	
	
	@RequestMapping(value = "getTenderDetailList", method = RequestMethod.GET)
	public List<TenderDetail> getTenderDetailList(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="packageNo") String packageNo,
													@RequestParam(name="vendorNo") String vendorNo){

		List<TenderDetail> tenderDetailList = null;
		try {
			
			tenderDetailList = tenderService.obtainTenderDetailList(jobNo, packageNo, Integer.valueOf(vendorNo));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tenderDetailList;
	}
	
	@RequestMapping(value = "updateTenderDetails", method = RequestMethod.POST)
	public String updateTenderDetails(@RequestParam(name="jobNo") String jobNo, 
										@RequestParam(name="subcontractNo") String subcontractNo,
										@RequestParam(name="vendorNo") Integer vendorNo,
										@RequestParam(name="currencyCode") String currencyCode,
										@RequestParam(name="exchangeRate") Double exchangeRate,
										@RequestParam(name="validate") Boolean validate,
										@Valid @RequestBody List<TenderDetail> taDetails){
		String result = "";
		try {
			result = tenderService.updateTenderAnalysisDetails(jobNo, subcontractNo, vendorNo, currencyCode, exchangeRate, taDetails, validate);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	
}

