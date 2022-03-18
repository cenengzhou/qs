package com.gammon.pcms.dao;

import com.gammon.pcms.model.FinalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FinalAccountRepository extends JpaRepository<FinalAccount, Long>{

    @Query("select a from FinalAccount a " +
            "where a.jobNo = :jobNo " +
            "and a.addendumNo = :addendumNo " +
            "and a.addendum.id = :addendumId " +
            "and a.systemStatus='ACTIVE'")
    FinalAccount findByJobNoAndAddendumNoAndAddendumId(@Param("jobNo") String jobNo, @Param("addendumNo") String addendumNo, @Param("addendumId") BigDecimal addendumId);
}
