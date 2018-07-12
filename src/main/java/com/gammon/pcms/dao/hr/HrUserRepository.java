package com.gammon.pcms.dao.hr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gammon.pcms.model.hr.HrUser;

public interface HrUserRepository extends JpaRepository<HrUser, String> {
	List<HrUser> findAllByOrderByEmployeeIdAsc();
	List<HrUser> findAllByOrderByUsernameAsc();
	List<HrUser> findByUsernameIsNotNull();
	HrUser findByUsername(String username);
	HrUser findByEmployeeId(String employeeId);
	HrUser findByEmail(String email);
}
