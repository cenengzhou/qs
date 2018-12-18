package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.Personnel;
import com.gammon.pcms.model.PersonnelMap;
import com.gammon.pcms.service.PersonnelService;
import com.gammon.qs.application.exception.DatabaseOperationException;

@RestController
@RequestMapping(path = {"/service/personnel", "/ws/personnel"})
public class PersonnelController {

	@Autowired
	private PersonnelService service;

	@RequestMapping(value = "{jobNo}/replaceAll", method = RequestMethod.POST)
	public void replaceAll(@PathVariable String jobNo, @RequestBody List<Personnel> personnelList) {
		service.replaceAll(jobNo, personnelList);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE) 
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public void save(@PathVariable String jobNo, @RequestBody List<Personnel> personnelList) {
		service.save(jobNo, personnelList);
	}
	
	@RequestMapping(value = "save/{jobNo}", method = RequestMethod.POST)
	public void save(@PathVariable String jobNo, @RequestBody Personnel personnel) {
		service.save(jobNo, personnel);
	}

	@RequestMapping(value = "clearDraft/{jobNo}", method = RequestMethod.POST)
	public void clearDraft(@PathVariable String jobNo) {
		service.clearDraft(jobNo);
	}
	
	@RequestMapping(value = "submitForApproval/{jobNo}", method = RequestMethod.POST)
	public void submitForApproval(@PathVariable String jobNo) throws Exception{
		service.submitForApproval(jobNo);
	}
	
	@RequestMapping(value = "getEmailContext/{formCode}/{refNo}", method = RequestMethod.POST)
	public String getEmailContext(@PathVariable String formCode, @PathVariable Long refNo) throws Exception {
		return service.getEmailContext(formCode, null, refNo);
	}
	
	@RequestMapping(value ="generateHtmlPdf/{formCode}/{refNo}", method = RequestMethod.POST)
	public void genterateHtmlPdf(@PathVariable String formCode, @PathVariable Long refNo) throws Exception {
		service.generateHtmlPdf(formCode, null, refNo);
	}
	
	@RequestMapping(value ="cancelApproval/{refNo}", method = RequestMethod.POST)
	public void cancelApproval(@PathVariable Long refNo) {
		service.cancelApproval(refNo);
	}

	@RequestMapping(value = "completeApproval/{refNo}/{decision}", method = RequestMethod.POST)
	public void completeApproval(@PathVariable Long refNo, @PathVariable String decision, @RequestBody Map<String, String> returnDecisionMap) {
		service.completeApproval(refNo, decision, returnDecisionMap);
	}
	
	@RequestMapping(value = "returnDecisionMap/{refNo}", method = RequestMethod.GET)
	public Map<String, Object> returnDecisionMap(@PathVariable Long refNo) {
		return service.returnDecisionMpa(refNo);
	}

	@RequestMapping(value = "getActivePersonnel/{jobNo}", method = RequestMethod.GET)
	public List<Personnel> getActivePersonnel(@PathVariable String jobNo) {
		return service.getActivePersonnel(jobNo);
	}

	@RequestMapping(value = "getAllPersonnelMap", method = RequestMethod.GET)
	public List<PersonnelMap> getAllPersonnelMap() {
		return service.getAllPersonnelMap();
	}
}
