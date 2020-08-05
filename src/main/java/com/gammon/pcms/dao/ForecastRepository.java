package com.gammon.pcms.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.Forecast;

public interface ForecastRepository extends JpaRepository<Forecast, Long>{

	List<Forecast> findByNoJob(String noJob);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"function('to_number', v.year) = :year "
			+"order by v.year, v.month"
	)
	List<Forecast> getByYear(
			@Param("noJob") String noJob,
			@Param("year") int year
	);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"v.forecastFlag = :forecastFlag and "
			+"function('to_number', v.year) = :year and "
			+"function('to_number', v.month) = :month "
			+"order by v.year, v.month"
	)
	List<Forecast> getByPeriod(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag
	);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"function('to_number', v.year) = :year and "
			+"function('to_number', v.month) = :month and "
			+"v.forecastFlag = :forecastFlag and "
			+"v.forecastType = :forecastType and "
			+"v.forecastDesc = :forecastDesc "
			+"order by v.year, v.month"
	)
	Forecast getByTypeDesc(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType,
			@Param("forecastDesc") String forecastDesc
	);
	
	
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"v.forecastFlag = :forecastFlag and "
			+"v.forecastType = :forecastType and "
			+"v.forecastDesc = :forecastDesc and "
			+"ROWNUM <= 1 "
			+"order by v.year desc, v.month desc"
	)
	
	Forecast getLatestForecast(
			@Param("noJob") String noJob,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType,
			@Param("forecastDesc") String forecastDesc
	);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"v.forecastFlag = :forecastFlag and "
			+"v.forecastType = :forecastType and "
			+"ROWNUM <= 1 "
			+"order by v.year desc, v.month desc"
	)
	
	Forecast getLatestProgramPeriod(
			@Param("noJob") String noJob,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType
	);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"v.forecastFlag = :forecastFlag and "
			+"ROWNUM <= 1 "
			+"order by v.year desc, v.month desc"
	)
	
	Forecast getLatestForecastPeriod(
			@Param("noJob") String noJob,
			@Param("forecastFlag") String forecastFlag
	);
	
	@Query("select v from Forecast v where "
			+"v.noJob = :noJob and "
			+"function('to_number', v.year) = :year and "
			+"function('to_number', v.month) = :month and "
			+"v.forecastFlag = :forecastFlag and "
			+"v.forecastType = :forecastType "
			+"order by v.year, v.month"
	)
	
	List<Forecast> getCriticalProgrammeList(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType
	);
	
	
}
