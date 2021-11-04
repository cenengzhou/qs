package com.gammon.pcms.dao;

import com.gammon.pcms.model.RocCutoffPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RocCutoffPeriodRepository extends JpaRepository<RocCutoffPeriod, Long>{

	@Query("select a from RocCutoffPeriod a where a.id = :id " )
	RocCutoffPeriod getByID(@Param("id") Long id);

	@Query("select a.cutoffDate from RocCutoffPeriod a where rownum <= 1")
	String findCutoffDate();

	@Query("select a.period from RocCutoffPeriod a where rownum <= 1")
	String findPeriod();
}
