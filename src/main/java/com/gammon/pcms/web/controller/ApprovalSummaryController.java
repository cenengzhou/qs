
package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.ApprovalSummary;
import com.gammon.qs.service.ApprovalSummaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "service/approvalSummary/")
public class ApprovalSummaryController {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ApprovalSummaryService approvalSummaryService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('ApprovalSummaryController','obtainApprovalSummary', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainApprovalSummary", method = RequestMethod.GET)
	public ApprovalSummary obtainApprovalSummary(@RequestParam(required = true) String nameObject, @RequestParam(required = true) String textKey) throws Exception {
		ApprovalSummary approvalSummary = null;
		approvalSummary = approvalSummaryService.obtainApprovalSummary(nameObject, textKey);
		return approvalSummary;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ApprovalSummaryController','updateApprovalSummary', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateApprovalSummary", method = RequestMethod.POST)
	public String updateApprovalSummary(@RequestParam(required = true) String jobNo,
										@RequestParam(required = true) String nameObject,
												@RequestParam(required = true) String textKey,
												@RequestBody(required = false) ApprovalSummary approvalSummary) {
		String result = "";
		result = approvalSummaryService.updateApprovalSummary(jobNo, nameObject, textKey, approvalSummary);
		return result;
	}

	

}
