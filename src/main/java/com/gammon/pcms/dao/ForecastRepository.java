package com.gammon.pcms.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.Forecast;

public interface ForecastRepository extends JpaRepository<Forecast, Long>{

	
	@Query("select f from Forecast f where "
			+"f.id = :id "
	)
	Forecast getByID(
			@Param("id") Long id
	);
	
	@Query("select f from Forecast f where "
			+"f.noJob = :noJob and "
			+"function('to_number', f.year) = :year "
			+"order by f.year, f.month"
	)
	List<Forecast> getByYear(
			@Param("noJob") String noJob,
			@Param("year") int year
	);
	
	@Query("select f from Forecast f where "
			+"f.noJob = :noJob and "
			+"f.forecastFlag = :forecastFlag and "
			+"function('to_number', f.year) = :year and "
			+"function('to_number', f.month) = :month "
			+"order by f.year, f.month"
	)
	List<Forecast> getByPeriod(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag
	);

	

	@Query("select distinct a.noJob from Forecast a")
	List<String> findAllJobNumber();
	
	@Query("select f from Forecast f where "
			+"f.noJob = :noJob and "
			+"function('to_number', f.year) = :year and "
			+"function('to_number', f.month) = :month and "
			+"f.forecastFlag = :forecastFlag and "
			+"f.forecastType = :forecastType and "
			+"trim(lower(f.forecastDesc)) = trim(lower(:forecastDesc)) "
			+"order by f.year, f.month"
	)
	Forecast getByTypeDesc(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType,
			@Param("forecastDesc") String forecastDesc
	);
	
	
	
	// @Query("select f from Forecast f where "
	// 		+"f.noJob = :noJob and "
	// 		+"f.forecastFlag = :forecastFlag and "
	// 		+"f.forecastType = :forecastType and "
	// 		+"trim(lower(f.forecastDesc)) = trim(lower(:forecastDesc)) and "
	// 		+"ROWNUM <= 1 "
	// 		+"order by f.year desc, f.month desc"
	// )
	// Forecast getLatestForecast(
	// 		@Param("noJob") String noJob,
	// 		@Param("forecastFlag") String forecastFlag,
	// 		@Param("forecastType") String forecastType,
	// 		@Param("forecastDesc") String forecastDesc
	// );
	Forecast findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(String noJob, String forecastFlag, String forecastType, String forecastDesc);
	
	// @Query("select f from Forecast f where "
	// 		+"f.noJob = :noJob and "
	// 		+"f.forecastFlag = :forecastFlag and "
	// 		+"f.forecastType = :forecastType and "
	// 		+"ROWNUM <= 1 "
	// 		+"order by f.year desc, f.month desc"
	// )
	// Forecast getLatestProgramPeriod(
	// 		@Param("noJob") String noJob,
	// 		@Param("forecastFlag") String forecastFlag,
	// 		@Param("forecastType") String forecastType
	// );
	Forecast findTopByNoJobAndForecastFlagAndForecastTypeOrderByYearDescMonthDesc(String noJob, String forecastFlag, String forecastType);
	
	// @Query("select f from Forecast f where "
	// 		+"f.noJob = :noJob and "
	// 		+"f.forecastFlag = :forecastFlag and "
	// 		+"ROWNUM <= 1 "
	// 		+"order by f.year desc, f.month desc"
	// )
	// Forecast getLatestForecastPeriod(
	// 		@Param("noJob") String noJob,
	// 		@Param("forecastFlag") String forecastFlag
	// );
	Forecast findTopByNoJobAndForecastFlagOrderByYearDescMonthDesc(String noJob, String forecastFlag);
	@Query("select f from Forecast f where "
			+"f.noJob = :noJob and "
			+"function('to_number', f.year) = :year and "
			+"function('to_number', f.month) = :month and "
			+"f.forecastFlag = :forecastFlag and "
			+"f.forecastType = :forecastType "
			+"order by f.year, f.month"
	)
	List<Forecast> getCriticalProgramList(
			@Param("noJob") String noJob,
			@Param("year") int year,
			@Param("month") int month,
			@Param("forecastFlag") String forecastFlag,
			@Param("forecastType") String forecastType
	);
	
	
	@Modifying
	@Query("update Forecast f set f.forecastDesc = :newForecastDesc where f.noJob = :noJob and trim(lower(f.forecastDesc)) = trim(lower(:oldForecastDesc))")
	void updateCriticalProgramDesc(
			@Param("noJob") String noJob,
			@Param("oldForecastDesc") String oldForecastDesc, 
			@Param("newForecastDesc") String newForecastDesc
	);

	Long deleteByNoJobAndYearAndMonthAndForecastFlag(String noJob, Integer year, Integer month, String forecastFlag);
	Long deleteByNoJobAndForecastDescIgnoreCase(String noJob, String forecastDesc);
}
