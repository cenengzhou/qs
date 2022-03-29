package com.gammon.pcms.service;

import com.gammon.pcms.dao.FinalAccountRepository;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.FinalAccount;
import com.gammon.qs.dao.AddendumHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

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
	private PaymentCertHBDao paymentCertDao;
	@Autowired
	private PaymentService paymentService;
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
	
	 public FinalAccount prepareFinalAcount(Addendum addendum, String jobNo, String subcontractNo, String addendumNo, Long addendumID) {
	    	FinalAccount fa = this.getFinalAccount(jobNo, addendumNo, addendumID);
			if (fa == null){
				fa = new FinalAccount();
				fa.setAddendum(addendum);
				fa.setAddendumNo(addendumNo);
				fa.setJobNo(jobNo);
				fa.setPreparedDate(new Date());
				fa.setPreparedUser(addendum.getUsernamePreparedBy());
				fa.setStatus(FinalAccount.PENDING);
			}
			
			if(addendum.getStatus().equals(Addendum.STATUS.PENDING.toString())){
				PaymentCertViewWrapper paymentCertSummary = new PaymentCertViewWrapper();

				try {
					PaymentCert lastPostedCert = paymentCertDao.obtainPaymentLatestPostedCert(jobNo, subcontractNo);
					if(lastPostedCert != null){
						paymentCertSummary = paymentService.getSCPaymentCertSummaryWrapper(jobNo, subcontractNo, String.valueOf(lastPostedCert.getPaymentCertNo()));

						fa.setFinalAccountThisAmt(addendum.getAmtSubcontractRevisedTba());
						fa.setFinalAccountThisCCAmt(BigDecimal.valueOf(-paymentCertSummary.getLessContraChargesTotal()));
						fa.setFinalAccountPreAmt(BigDecimal.valueOf(paymentCertSummary.getSubTotal4()));
						fa.setFinalAccountPreCCAmt(BigDecimal.valueOf(-paymentCertSummary.getLessContraChargesTotal()));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return fa;
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
