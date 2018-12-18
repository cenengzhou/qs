package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gammon.pcms.model.Personnel;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

	List<Personnel> findByJobInfo_JobNumberAndStatus(String jobNo, String status);
	List<Personnel> findByJobInfo_JobNumberAndStatusAndAction(String jobNo, String status, String action);
	List<Personnel> findByJobInfo_JobNumberAndStatusAndActionNot(String jobNo, String status, String action);
}
