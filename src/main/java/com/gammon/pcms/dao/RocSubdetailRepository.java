package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC_SUBDETAIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface RocSubdetailRepository extends JpaRepository<ROC_SUBDETAIL, Long>{

    @Query("select a from ROC_SUBDETAIL a where a.roc.id=:rocId and a.systemStatus='ACTIVE' order by a.inputDate desc")
    List<ROC_SUBDETAIL> findByRocId(@Param("rocId") Long rocId);

    @Query("select new com.gammon.pcms.model.ROC_SUBDETAIL(sum(a.amountBest), sum(a.amountExpected), sum(a.amountWorst)) from ROC_SUBDETAIL a " +
            "where a.roc.id=:rocId and a.systemStatus='ACTIVE' " +
            "and (a.inputDate >= :startDate and a.inputDate < :endDate)")
    ROC_SUBDETAIL findSumByRocId(@Param("rocId") Long rocId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
