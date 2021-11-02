package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.wrapper.RocSummaryWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RocDetailRepository extends JpaRepository<ROC_DETAIL, Long>{

	@Query("select a from ROC_DETAIL a where a.roc.id = :idRoc and a.year=:year and a.month=:month and a.systemStatus='ACTIVE'")
	ROC_DETAIL findDetailByRocIdAndYearMonth(@Param("idRoc") Long idRoc, @Param("year") int year, @Param("month") int month);

	@Query("select new com.gammon.pcms.wrapper.RocSummaryWrapper(b.projectNo, a.year, a.month, b.rocCategory, sum(a.amountExpected)) from ROC_DETAIL a " +
			"left join a.roc b where b.projectNo=:jobNo and a.year=:year and a.month=:month " + 
			"and a.systemStatus='ACTIVE' and a.status='Live' and b.systemStatus='ACTIVE' and b.status='Live' " +
			"group by b.projectNo, a.year, a.month, b.rocCategory")
	List<RocSummaryWrapper> findSumOfAmountExpectedGroupByRocCat(@Param("jobNo") String jobNo, @Param("year") int year, @Param("month") int month);

	@Query("select a from ROC_DETAIL a where a.roc.id=:idRoc and a.systemStatus='ACTIVE' and (a.year*100+a.month<=:year*100+:month) order by a.year desc, a.month desc")
	List<ROC_DETAIL> findByRocIdAndYearMonthOrderByYearDescAndMonthDesc(@Param("idRoc") Long idRoc, @Param("year") int year, @Param("month") int month);

	@Query("select a from ROC_DETAIL a where a.roc.id = :idRoc and a.systemStatus='ACTIVE'")
	List<ROC_DETAIL> findRocDetailsByRocIdAdmin(@Param("idRoc") Long idRoc);


}
