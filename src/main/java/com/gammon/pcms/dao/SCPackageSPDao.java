package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;

public interface SCPackageSPDao extends CrudRepository<Subcontract, Long> {

	List<Subcontract> findByJobInfo_JobNumberAndSubcontractStatusAndSystemStatus(String jobNumber, Integer scStatus, String systemStatus) throws DatabaseOperationException;
}
