package com.gammon.pcms.dao.adl;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.adl.JDEForecastEOJ;

public interface JDEForecastEOJRepository extends JpaRepository<JDEForecastEOJ, Long>{
	
	@Query("select v from JDEForecastEOJ v where "
			+"v.businessUnit = :businessUnit and "
			+"v.accLedgerType = v.latestForecastLedger and "
			+"v.desc = :desc"
	)
	
	JDEForecastEOJ getLatestForecastEOJ(
			@Param("businessUnit") String businessUnit,
			@Param("desc") String desc
	);
	
	
}
