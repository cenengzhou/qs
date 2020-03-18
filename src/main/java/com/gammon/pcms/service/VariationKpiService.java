package com.gammon.pcms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.VariationKpiRepository;
import com.gammon.pcms.model.VariationKpi;

import edu.emory.mathcs.backport.java.util.Arrays;

@Transactional
@Service
public class VariationKpiService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private VariationKpiRepository repository;
	
	public List<VariationKpi> findByJobNo(String jobNo) {
		return repository.findByNoJob(jobNo);
	}
	
	public List<VariationKpi> getByYear(String jobNo, int year) {
		return repository.getByYear(jobNo, year);
	}
	
	public VariationKpi save(String jobNo, VariationKpi kpi) {
		kpi.setNoJob(jobNo);
		return repository.save(kpi);
	}
	
	public List<VariationKpi> save(String jobNo, List<VariationKpi> kpiList) {
		kpiList.forEach(kpi -> {
			kpi.setNoJob(jobNo);
		});
		return repository.save(kpiList);
	}
	
	public void delete(String jobNo, Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		idList.forEach(id -> {
			VariationKpi kpiDb = repository.getOne((Long)id);
			if(kpiDb.getNoJob().equals(jobNo)) {
				repository.delete(kpiDb);
			} else {
				throw new IllegalAccessError("cannot delete variation kpi");
			}
		});
	}
	
	public Page<VariationKpi> filterPagination(
			Pageable pageable,
			String noJob,
			String year, 
			String month, 
			String numberIssued, 
			String amountIssued, 
			String numberSubmitted,
			String amountSubmitted,
			String numberAssessed,
			String amountAssessed,
			String numberApplied,
			String amountApplied,
			String numberCertified,
			String amountCertified,
			String remarks) {
		return repository.filterPagination(
				pageable, 
				noJob, 
				year  + "%", 
				month + "%", 
				numberIssued  + "%", 
				amountIssued  + "%", 
				numberSubmitted  + "%", 
				amountSubmitted  + "%", 
				numberAssessed  + "%", 
				amountAssessed  + "%", 
				numberApplied  + "%", 
				amountApplied  + "%", 
				numberCertified  + "%", 
				amountCertified  + "%", 
				"%" + remarks + "%"
				);
	}
}
