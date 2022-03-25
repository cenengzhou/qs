package com.gammon.pcms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gammon.pcms.model.CEDApproval;

public interface CEDApprovalRepository extends JpaRepository<CEDApproval, Long>{

	
	@Query("select c from CEDApproval c where "
			+" c.jobNo = :jobNo and"
			+" c.packageNo = :packageNo"
	)
	CEDApproval getByJobPackage(
			@Param("jobNo") String jobNo,
			@Param("packageNo") int packageNo
	);
	
	
}
