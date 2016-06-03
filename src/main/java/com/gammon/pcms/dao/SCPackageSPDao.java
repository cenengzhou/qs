package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;

public interface SCPackageSPDao extends CrudRepository<SCPackage, Long> {

	List<SCPackage> findByJob_JobNumberAndSubcontractStatusAndSystemStatus(String jobNumber, Integer scStatus, String systemStatus) throws DatabaseOperationException;
}
