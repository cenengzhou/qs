package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.hr.HrUser;
import com.gammon.pcms.service.HrService;

@RestController
@RequestMapping(path = {"/service/hr"})
public class HrController {

	@Autowired HrService hrService;
	
	@RequestMapping(value = "findByUsername", method = RequestMethod.GET)
	public HrUser findByUsername(String username){
		return hrService.findByUsername(username);
	}

	@RequestMapping(value ="findByEmployeeId", method = RequestMethod.GET)
	public HrUser findByEmployeeId(String employeeId){
		return hrService.findByEmployeeId(employeeId);
	}

	@RequestMapping(value = "findByEmail", method = RequestMethod.GET)
	public HrUser findByEmail(String email){
		return hrService.findByEmail(email);
	}

	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public List<HrUser> findAll(){
		return hrService.findAll();
	}
	
	@RequestMapping(value = "findByUsernameIsNotNull", method = RequestMethod.GET)
	public List<HrUser> findByUsernameIsNotNull(){
		return hrService.findByUsernameIsNotNull();
	}

}
