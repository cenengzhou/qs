package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.CommercialAction;
import com.gammon.pcms.service.CommercialActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "service/commercialAction/")
public class CommercialActionController {

	@Autowired
	private CommercialActionService commercialActionService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','getCommercialActionList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="getCommercialActionList/{jobNo}/{year}/{month}", method=RequestMethod.GET)
	public List<CommercialAction> getCommercialActionList(@PathVariable String jobNo, @PathVariable String year, @PathVariable String month) {

		return commercialActionService.getCommercialActionList(jobNo, year, month);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','getCommercialActionList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="getCommercialActionList", method=RequestMethod.GET)
	public List<CommercialAction> getCommercialActionList() {
		return commercialActionService.getAllCommercialAction();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','addCommercialAction', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value="addCommercialAction/{jobNo}", method=RequestMethod.POST)
	public String addCommercialAction(@PathVariable String jobNo, @RequestBody CommercialAction commercialAction) {
		return commercialActionService.addCommercialAction(jobNo, commercialAction);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','saveById', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveById/{id}", method = RequestMethod.POST)
	public Boolean saveById(@PathVariable String id, @RequestBody CommercialAction ca) {
		return commercialActionService.saveById(id, ca);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','saveList', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public List<CommercialAction> saveList(@PathVariable String jobNo, @RequestBody List<CommercialAction> commercialActionList) {
		return commercialActionService.saveList(jobNo, commercialActionList);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('CommercialActionController','deleteById', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteById//{id}", method = RequestMethod.DELETE)
	public void deleteById(@PathVariable String id) {
		commercialActionService.deleteById(id);
	}

}
