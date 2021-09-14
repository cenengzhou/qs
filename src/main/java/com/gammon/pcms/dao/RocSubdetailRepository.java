package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC_SUBDETAIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RocSubdetailRepository extends JpaRepository<ROC_SUBDETAIL, Long>{

    @Query("select a from ROC_SUBDETAIL a where a.roc.id=:rocId and a.systemStatus='ACTIVE' order by (a.year*100 + a.month) desc, a.createdDate desc")
    List<ROC_SUBDETAIL> findByRocId(@Param("rocId") Long rocId);
//
    @Query("select new com.gammon.pcms.model.ROC_SUBDETAIL(sum(a.amountBest), sum(a.amountExpected), sum(a.amountWorst)) from ROC_SUBDETAIL a " +
            "where a.roc.id=:rocId and a.systemStatus='ACTIVE' " +
            "and (a.year=:year and a.month=:month)")
    ROC_SUBDETAIL findSumByRocId(@Param("rocId") Long rocId, @Param("year") int year, @Param("month") int month);

    @Query("select new com.gammon.pcms.model.ROC_SUBDETAIL(sum(a.amountBest), sum(a.amountExpected), sum(a.amountWorst)) from ROC_SUBDETAIL a " +
            "where a.roc.id=:rocId and a.systemStatus='ACTIVE' " +
            "and (a.year*100+a.month<=:year*100+:month)")
    ROC_SUBDETAIL findSumByRocIdAndEndPeriod(@Param("rocId") Long rocId, @Param("year") int year, @Param("month") int month);

    @Modifying
    @Query("update ROC_SUBDETAIL a set a.amountBest=0, a.amountExpected=0, a.amountWorst=0 where a.roc.id=:rocId and a.year=:year and a.month=:month")
    int updateSubdetailToZeroByRocIdAndPeriod(@Param("rocId") Long rocId, @Param("year") int year, @Param("month") int month);
}
