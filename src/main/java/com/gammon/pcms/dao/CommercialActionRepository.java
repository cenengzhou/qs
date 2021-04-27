package com.gammon.pcms.dao;

import com.gammon.pcms.model.CommercialAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommercialActionRepository extends JpaRepository<CommercialAction, Long> {

    @Query("select c from CommercialAction c where c.noJob = :noJob and year(c.actionDate)=:year and month(c.actionDate)=:month")
    List<CommercialAction> findCommercialActionList(@Param("noJob") String noJob, @Param("year") int year, @Param("month") int month);
}