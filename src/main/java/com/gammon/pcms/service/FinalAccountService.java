package com.gammon.pcms.service;

import com.gammon.pcms.dao.FinalAccountRepository;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.FinalAccount;
import com.gammon.qs.dao.AddendumHBDao;
import com.gammon.qs.service.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Transactional
@Service
public class FinalAccountService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FinalAccountRepository finalAccountRepository;

	@Autowired
	private AddendumHBDao addendumHBDao;

	@Autowired
	private SecurityService securityServiceImpl;

    public String createFinalAccount(String jobNo, String addendumNo, Long addendumId, FinalAccount finalAccount) {
		String error = "";
		try {
			Addendum addendum = addendumHBDao.getAddendumById(addendumId);
			FinalAccount dbRecord = getFinalAccount(jobNo, addendumNo, addendumId);
			if (dbRecord == null) {
				FinalAccount newFinalAccount = new FinalAccount(
						jobNo,
						addendumNo,
						addendum,
						finalAccount.getFinalAccountAppAmt(),
						finalAccount.getFinalAccountAppCCAmt(),
						finalAccount.getLatestBudgetAmt(),
						finalAccount.getLatestBudgetAmtCC(),
						finalAccount.getComments(),
						new Date(),
						securityServiceImpl.getCurrentUser().getUsername(),
						FinalAccount.PENDING
				);
				finalAccountRepository.save(newFinalAccount);
			} else {
				dbRecord.setFinalAccountAppAmt(finalAccount.getFinalAccountAppAmt());
				dbRecord.setFinalAccountAppCCAmt(finalAccount.getFinalAccountAppCCAmt());
				dbRecord.setLatestBudgetAmt(finalAccount.getLatestBudgetAmt());
				dbRecord.setLatestBudgetAmtCC(finalAccount.getLatestBudgetAmtCC());
				dbRecord.setComments(finalAccount.getComments());
			}

		} catch (Exception e) {
			error = "Final Account cannot be created";
			e.printStackTrace();
		}
		return error;
    }

	public FinalAccount getFinalAccount(String jobNo, String addendumNo, Long addendumId) {
		return finalAccountRepository.findByJobNoAndAddendumNoAndAddendumId(jobNo, addendumNo, BigDecimal.valueOf(addendumId));
	}

	public FinalAccount getFinalAccountAdmin(String jobNo, String subcontractNo, String addendumNo) {
		Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, Long.valueOf(addendumNo));
		return finalAccountRepository.findByJobNoAndAddendumNoAndAddendumId(jobNo, addendumNo, addendum.getId());
	}

	public String updateFinalAccountAdmin(String jobNo, String subcontractNo, String addendumNo, FinalAccount finalAccount) {
		String error = "";
		try {
			FinalAccount dbRecord = getFinalAccountAdmin(jobNo, subcontractNo, addendumNo);
			if (dbRecord == null) {
				error ="Final Account does not exist";
			} else {
				dbRecord.setFinalAccountAppAmt(finalAccount.getFinalAccountAppAmt());
				dbRecord.setFinalAccountAppCCAmt(finalAccount.getFinalAccountAppCCAmt());
				dbRecord.setLatestBudgetAmt(finalAccount.getLatestBudgetAmt());
				dbRecord.setLatestBudgetAmtCC(finalAccount.getLatestBudgetAmtCC());
				dbRecord.setComments(finalAccount.getComments());
//				dbRecord.setPreparedDate(finalAccount.getStatus());
//				dbRecord.setPreparedUser(finalAccount.getStatus());
				dbRecord.setStatus(finalAccount.getStatus());
			}
		} catch (Exception e) {
			error = "Final Account cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteFinalAccountAdmin(String jobNo, String subcontractNo, String addendumNo, FinalAccount finalAccount) {
		if (jobNo.isEmpty() || subcontractNo.isEmpty() || finalAccount == null)
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			FinalAccount dbRecord = getFinalAccountAdmin(jobNo, subcontractNo, addendumNo);
			dbRecord.inactivate();
		} catch (Exception e) {
			error = "Final Account cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}

}
