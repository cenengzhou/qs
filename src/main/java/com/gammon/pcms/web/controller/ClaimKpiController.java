package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.ClaimKpi;
import com.gammon.pcms.service.ClaimKpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "service/claimKpi/")
public class ClaimKpiController {
	
	@Autowired
	private ClaimKpiService service;

	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','get', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "{jobNo}", method = RequestMethod.GET)
	public List<ClaimKpi> get(@PathVariable String jobNo) {
		return service.findByJobNo(jobNo);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','post', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "{jobNo}", method = RequestMethod.POST)
	public ClaimKpi post(@PathVariable String jobNo, @RequestBody ClaimKpi kpi) {
		return service.save(jobNo, kpi);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','post', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public List<ClaimKpi> post(@PathVariable String jobNo, @RequestBody List<ClaimKpi> kpiList) {
		return service.save(jobNo, kpiList);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','delete', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "{jobNo}/{ids}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String jobNo, @PathVariable Long[] ids) {
		service.delete(jobNo, ids);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','getByYear', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getByYear/{jobNo}/{year}", method = RequestMethod.GET)
	public List<ClaimKpi> getByYear(@PathVariable String jobNo, @PathVariable int year) {
		return service.getByYear(jobNo, year);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ClaimKpiController','getByPage', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "{page}/{size}", method = RequestMethod.GET)
	public Page<ClaimKpi> getByPage(
			@PathVariable int page, 
			@PathVariable int size, 
			@RequestParam(defaultValue = "ASC") String direction, 
			@RequestParam(defaultValue = "noJob, year, month") String property,
			@RequestParam(defaultValue = "") String noJob,
			@RequestParam(defaultValue = "") String year,
			@RequestParam(defaultValue = "") String month,
			@RequestParam(defaultValue = "") String numberIssued,
			@RequestParam(defaultValue = "") String amountIssued,
			@RequestParam(defaultValue = "") String numberSubmitted,
			@RequestParam(defaultValue = "") String amountSubmitted,
			@RequestParam(defaultValue = "") String numberAssessed,
			@RequestParam(defaultValue = "") String amountAssessed,
			@RequestParam(defaultValue = "") String numberApplied,
			@RequestParam(defaultValue = "") String amountApplied,
			@RequestParam(defaultValue = "") String numberCertified,
			@RequestParam(defaultValue = "") String amountCertified,
			@RequestParam(defaultValue = "") String remarks,
			@RequestParam(defaultValue = "") String eojSecured,
			@RequestParam(defaultValue = "") String eojUnsecured,
			@RequestParam(defaultValue = "") String eojTotal,
			@RequestParam(defaultValue = "") String exceptionComment
			) {
		Pageable pageable = new PageRequest(
				page, 
				size, 
				Enum.valueOf(Sort.Direction.class, direction),
				property);
		Page<ClaimKpi> pageData = service.filterPagination(
				pageable, 
				noJob, 
				year, 
				month, 
				numberIssued, 
				amountIssued, 
				numberSubmitted, 
				amountSubmitted, 
				numberAssessed, 
				amountAssessed, 
				numberApplied, 
				amountApplied, 
				numberCertified, 
				amountCertified, 
				remarks,
				eojSecured,
				eojUnsecured,
				eojTotal,
				exceptionComment
				);
		return pageData;
	}
}
