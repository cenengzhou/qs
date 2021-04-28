package com.gammon.pcms.service;

import com.gammon.pcms.dao.ClaimKpiRepository;
import com.gammon.pcms.model.ClaimKpi;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ClaimKpiService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ClaimKpiRepository repository;
	
	public List<ClaimKpi> findByJobNo(String jobNo) {
		return repository.findByNoJob(jobNo);
	}
	
	public List<ClaimKpi> getByYear(String jobNo, int year) {
		return repository.getByYear(jobNo, year);
	}
	
	public ClaimKpi save(String jobNo, ClaimKpi kpi) {
		ClaimKpi claimKpi = new ClaimKpi();
		try {
			kpi.setNoJob(jobNo);
			claimKpi = repository.save(kpi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claimKpi;
	}
	
	public List<ClaimKpi> save(String jobNo, List<ClaimKpi> kpiList) {
		kpiList.forEach(kpi -> {
			kpi.setNoJob(jobNo);
		});
		return repository.save(kpiList);
	}
	
	public void delete(String jobNo, Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		idList.forEach(id -> {
			ClaimKpi kpiDb = repository.getOne((Long)id);
			if(kpiDb.getNoJob().equals(jobNo)) {
				repository.delete(kpiDb);
			} else {
				throw new IllegalAccessError("cannot delete claim kpi");
			}
		});
	}
	
	public Page<ClaimKpi> filterPagination(
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
			String remarks,
			String eojSecured,
			String eojUnsecured,
			String eojTotal
			) {
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
				remarks  + "%",
				eojSecured  + "%",
				eojUnsecured  + "%",
				eojTotal  + "%"
				);
	}
}
