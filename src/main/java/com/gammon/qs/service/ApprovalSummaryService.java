package com.gammon.qs.service;

import com.gammon.pcms.dao.ApprovalSummaryRepository;
import com.gammon.pcms.model.ApprovalSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
//SpringSession workaround: change "session" to "request"
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class ApprovalSummaryService {

	private Logger logger = Logger.getLogger(ApprovalSummaryService.class.getName());

	@Autowired
	private ApprovalSummaryRepository approvalSummaryRepository;

	public ApprovalSummary obtainApprovalSummary(String nameObject, String textKey) throws Exception{
		ApprovalSummary approvalSummary = null;
		approvalSummary = approvalSummaryRepository.getByTableNameAndTableId(nameObject, Long.valueOf(textKey));
		return approvalSummary;
	}

	public String updateApprovalSummary(String jobNo, String nameObject, String textKey, ApprovalSummary approvalSummary) {
		String result = "";
		if (!nameObject.equals(ApprovalSummary.SubcontractNameObject) && !nameObject.equals(ApprovalSummary.PaymentCertNameObject) && !nameObject.equals(ApprovalSummary.AddendumNameObject)) {
			return "invalid name object";
		}
		try {
			ApprovalSummary dbSummary = approvalSummaryRepository.getByTableNameAndTableId(nameObject, Long.valueOf(textKey));
			if (dbSummary == null) {
 				// create new record
				dbSummary = new ApprovalSummary(
						Long.valueOf(textKey),
						nameObject,
						jobNo,
						approvalSummary.getSummary(),
						approvalSummary.getEoj(),
						approvalSummary.getContingencies(),
						approvalSummary.getRiskOpps(),
						approvalSummary.getOthers()
				);
				approvalSummaryRepository.save(dbSummary);
			} else {
				// update record
				dbSummary.setSummary(approvalSummary.getSummary());
				dbSummary.setEoj(approvalSummary.getEoj());
				dbSummary.setContingencies(approvalSummary.getContingencies());
				dbSummary.setRiskOpps(approvalSummary.getRiskOpps());
				dbSummary.setOthers(approvalSummary.getOthers());
			}
		} catch (Exception e) {
			result = "failed to update approval summary";
			e.printStackTrace();
		}
		return result;
	}
}
