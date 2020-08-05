package com.gammon.pcms.dao.adl;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.adl.JDEForecast;

public interface JDEForecastRepository extends JpaRepository<JDEForecast, Long>{
	
	//select * from adlprd.FACT_JDE_FORECAST where business_unit_code_trimmed = '13777' and (account_type_ledger = 'AA' or account_type_ledger = latest_forecast_ledger) and year = 2020 and month = 7;
	
	@Query("select v from JDEForecast v where "
			+"v.businessUnit = :businessUnit and "
			+"(v.accLedgerType = 'AA' or v.accLedgerType = v.latestForecastLedger) and "
			+"function('to_number', v.year) = :year and "
			+"function('to_number', v.month) = :month and "
			+"v.desc = :desc"
	)
	
	List<JDEForecast> getJDEForecast(
			@Param("businessUnit") String businessUnit,
			@Param("year") int year,
			@Param("month") int month,
			@Param("desc") String desc
	);
	
	
}
