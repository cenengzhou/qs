package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.VariationKpi;
import com.gammon.pcms.service.VariationKpiService;

@RestController
@RequestMapping(value = "service/variationKpi/")
public class VariationKpiController {
	
	@Autowired
	private VariationKpiService service;

	@RequestMapping(value = "{jobNo}", method = RequestMethod.GET)
	public List<VariationKpi> get(@PathVariable String jobNo) {
		return service.findByJobNo(jobNo);
	}
	
	@RequestMapping(value = "{jobNo}", method = RequestMethod.POST)
	public VariationKpi post(@PathVariable String jobNo, @RequestBody VariationKpi kpi) {
		return service.save(jobNo, kpi);
	}

	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public List<VariationKpi> post(@PathVariable String jobNo, @RequestBody List<VariationKpi> kpiList) {
		return service.save(jobNo, kpiList);
	}
	
	@RequestMapping(value = "{jobNo}/{ids}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String jobNo, @PathVariable Long[] ids) {
		service.delete(jobNo, ids);
	}
	
	@RequestMapping(value = "getByYear/{jobNo}/{year}", method = RequestMethod.GET)
	public List<VariationKpi> getByYear(@PathVariable String jobNo, @PathVariable int year) {
		return service.getByYear(jobNo, year);
	}
	
	@RequestMapping(value = "{page}/{size}", method = RequestMethod.GET)
	public Page<VariationKpi> getByPage(
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
			@RequestParam(defaultValue = "") String remarks
			) {
		Pageable pageable = new PageRequest(
				page, 
				size, 
				Enum.valueOf(Sort.Direction.class, direction),
				property);
		Page<VariationKpi> pageData = service.filterPagination(
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
				remarks
				);
		return pageData;
	}
}
