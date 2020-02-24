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

@Transactional
@Service
public class VariationKpiService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private VariationKpiRepository repository;
	
	public List<VariationKpi> findByJobNo(String jobNo) {
		return repository.findByNoJob(jobNo);
	}
	
	public VariationKpi save(String jobNo, VariationKpi kpi) {
		kpi.setNoJob(jobNo);
		return repository.save(kpi);
	}
	
	public void delete(StringVariationKpi kpi) {
		VariationKpi kpiDb = repository.getOne(kpi.getId());
		if(kpiDb.getNoJob().equals(kpi.getNoJob())) {
			repository.delete(kpiDb);
		} else {
			throw new IllegalAccessError("cannot delete variation kpi");
		}
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
