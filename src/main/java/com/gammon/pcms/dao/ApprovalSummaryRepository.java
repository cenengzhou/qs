package com.gammon.pcms.dao;

import com.gammon.pcms.model.ApprovalSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalSummaryRepository extends JpaRepository<ApprovalSummary, Long>{

	@Query("select a from ApprovalSummary a where a.nameTable=:nameTable and a.idTable=:idTable and a.systemStatus='ACTIVE'")
	ApprovalSummary getByTableNameAndTableId(@Param("nameTable") String nameTable, @Param("idTable") Long idTable);
}
