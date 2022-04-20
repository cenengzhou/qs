package com.gammon.pcms.dao;

import com.gammon.pcms.model.ScPaymentApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScPaymentApprovalRepository extends JpaRepository<ScPaymentApproval, Long>{

	
	@Query("select c from ScPaymentApproval c where "
			+" c.jobNo = :jobNo and " +
			"c.packageNo = :packageNo and " +
			"c.paymentCertNo = :paymentCertNo " +
			"order by c.approvalSequence"
	)
	List<ScPaymentApproval> getPaymentApproval(
			@Param("jobNo") String jobNo,
			@Param("packageNo") int packageNo,
			@Param("paymentCertNo") int paymentCertNo
	);
	
	
}
