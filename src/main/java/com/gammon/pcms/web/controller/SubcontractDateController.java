package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDate;
import com.gammon.pcms.service.SubcontractDateService;

@RestController
@RequestMapping(value = "service/subcontractDate/", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class SubcontractDateController {
	
	@Autowired
	private SubcontractDateService service;
	
	@RequestMapping(value = "getScDateList/{jobNo}/{packageNo}", method = RequestMethod.GET)
	public List<SubcontractDate> getScDateList(@PathVariable String jobNo, @PathVariable String packageNo) {
			return service.getScDateList(jobNo, packageNo);
	}
	
}
