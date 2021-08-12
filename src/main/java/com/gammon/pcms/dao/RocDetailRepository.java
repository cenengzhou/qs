package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC_DETAIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocDetailRepository extends JpaRepository<ROC_DETAIL, Long>{

	@Query("select a from ROC_DETAIL a where a.roc.id = :idRoc and a.year=:year and a.month=:month")
	ROC_DETAIL findDetailByRocIdAndYearMonth(@Param("idRoc") Long idRoc, @Param("year") int year, @Param("month") int month);

}
