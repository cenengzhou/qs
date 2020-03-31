package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.VariationKpi;

public interface VariationKpiRepository extends JpaRepository<VariationKpi, Long>{

	List<VariationKpi> findByNoJob(String noJob);
	
	@Query("select v from VariationKpi v where "
			+"v.noJob = :noJob and "
			+"function('to_number', v.year) = :year "
			+"order by v.year, v.month"
	)
	List<VariationKpi> getByYear(
			@Param("noJob") String noJob,
			@Param("year") int year
	);
	
	@Query("select v from VariationKpi v where "
			+"v.noJob = :noJob and "
			+"function('to_number', v.year) = :year and "
			+"function('to_number', v.month) = :month "
			+"order by v.year, v.month"
	)
	List<VariationKpi> getByPeriod(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month
	);
	
	@Query("select v from VariationKpi v where "
			+"v.noJob = :noJob and "
			+"function('to_char', v.year) like :year and "
			+"function('to_char', v.month) like :month and "
			+"function('to_char', v.numberIssued) like :numberIssued and "
			+"function('to_char', v.amountIssued) like :amountIssued and "
			+"function('to_char', v.numberSubmitted) like :numberSubmitted and "
			+"function('to_char', v.amountSubmitted) like :amountSubmitted and "			
			+"function('to_char', v.numberAssessed) like :numberAssessed and "
			+"function('to_char', v.amountAssessed) like :amountAssessed and "			
			+"function('to_char', v.numberApplied) like :numberApplied and "
			+"function('to_char', v.amountApplied) like :amountApplied and "			
			+"function('to_char', v.numberCertified) like :numberCertified and "
			+"function('to_char', v.amountCertified) like :amountCertified and "
			+"(v.remarks like :remarks or v.remarks is null) "
	)
	Page<VariationKpi> filterPagination(
			Pageable pageable,
			@Param("noJob") String noJob,
			@Param("year") String year, 
			@Param("month") String month, 
			@Param("numberIssued") String numberIssued, 
			@Param("amountIssued") String amountIssued, 
			@Param("numberSubmitted") String numberSubmitted,
			@Param("amountSubmitted") String amountSubmitted,
			@Param("numberAssessed") String numberAssessed,
			@Param("amountAssessed") String amountAssessed,
			@Param("numberApplied") String numberApplied,
			@Param("amountApplied") String amountApplied,
			@Param("numberCertified") String numberCertified,
			@Param("amountCertified") String amountCertified,
			@Param("remarks") String remarks
	);
}
