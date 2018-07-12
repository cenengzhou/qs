package com.gammon.pcms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.hr.HrUserRepository;
import com.gammon.pcms.model.hr.HrUser;

@Service
@Transactional("hrTransactionManager")
public class HrService {

	@Autowired
	private HrUserRepository hrUserRepository;
	
	public HrUser findByUsername(String username){
		try{
			HrUser hrUser = hrUserRepository.findByUsername(username);
			return hrUser;
		} catch(Exception e){
			throw new NullPointerException();
		}
	}
	
	public HrUser findByEmployeeId(String employeeId){
		return hrUserRepository.findByEmployeeId(employeeId); 
	}
	
	public HrUser findByEmail(String email){
		return hrUserRepository.findByEmail(email);
	}

	public List<HrUser> findAll() {
		return hrUserRepository.findAll();
	}
	
	public List<HrUser> findByUsernameIsNotNull(){
		return hrUserRepository.findByUsernameIsNotNull();
	}

}
