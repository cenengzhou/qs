package com.gammon.qs.service.businessLogic;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.util.RoundingUtil;

public class SCPaymentLogic {
	private static Logger logger = Logger.getLogger(SCPaymentLogic.class.getName());

	public static final int PAYMENT_TERM_DATE_QS1=7;
	public static final int PAYMENT_TERM_DATE_QS2=14;
	public static final int PAYMENT_TERM_DATE_QS3=56;
	public static final int PAYMENT_TERM_DATE_QS4=28;
	public static final int PAYMENT_TERM_DATE_QS5=30;
	public static final int PAYMENT_TERM_DATE_QS6=45;
	public static final int PAYMENT_TERM_DATE_QS7=60;

	@SuppressWarnings("static-access")
	public static final boolean ableToSubmit(PaymentCert scPaymentCert, List<PaymentCert> scPaymentCertList, List<SubcontractDetail> scDetailsList, List<PaymentCertDetail> scPaymentDetailList) throws ValidateBusinessLogicException{
		logger.info("SCPaymentLogic.ableToSubmit");
		
		// Validation 1 - Payment Status that can submit for payment
		if (!"PND".equals(scPaymentCert.getPaymentStatus()) && !"UFR".equals(scPaymentCert.getPaymentStatus()))
			throw new ValidateBusinessLogicException("SC Payment Status is not Pending or not to be Reviewed by Finance");
		
		// Validation 2 - Check on any pending addendum
		String checkResult=SCPackageLogic.ableToSubmitAddendum(scPaymentCert.getSubcontract(), scPaymentCertList);
		if (checkResult!=null)
			throw new ValidateBusinessLogicException("SC status invalid:"+checkResult);

		// Validation 3 - No further payment to be submitted if Final Paid
		if ("F".equals(scPaymentCert.getSubcontract().getPaymentStatus()))
			throw new ValidateBusinessLogicException("Subcontract was Final Paid");
		
		//Validation 4 - Calculate Retention Amount
		double retentionAmount=0.00;
		for (PaymentCertDetail scPaymentDetail: scPaymentDetailList){
			if ("RR".equals(scPaymentDetail.getLineType().trim()) || "RA".equals(scPaymentDetail.getLineType().trim()) || "RT".equals(scPaymentDetail.getLineType().trim()))
				retentionAmount+=scPaymentDetail.getCumAmount();
		}
		
		//RT + RA + RR must be less than or equal to maximum retention amount (round to 2 d.p. for comparison)
		int roundingDP = 2;
		if (!PaymentCert.DIRECT_PAYMENT.equals(scPaymentCert.getDirectPayment()))
			if (RoundingUtil.round(scPaymentCert.getSubcontract().getRetentionAmount(), roundingDP) < RoundingUtil.round(retentionAmount, roundingDP))
				throw new ValidateBusinessLogicException("Cum Retention exceed Retention Amount(limited)");
		
		Calendar currentPeriod = Calendar.getInstance();
		if (currentPeriod.get(Calendar.DATE)>25)
			if (currentPeriod.get(currentPeriod.MONTH)!=Calendar.DECEMBER)
				currentPeriod.set(currentPeriod.MONTH,currentPeriod.get(currentPeriod.MONTH)+1);
			else
				//last date of period 12 is 31.
				;
		
		if (currentPeriod.get(currentPeriod.MONTH)==Calendar.DECEMBER)
			currentPeriod.set(currentPeriod.DATE, 31);
		else
			currentPeriod.set(currentPeriod.DATE, 25);

		if ("QS0".equals(scPaymentCert.getSubcontract().getPaymentTerms())&& scPaymentCert.getDueDate()==null )
			throw new ValidateBusinessLogicException("Due Date cannot be null");
		
		// for check if AsAtDate is null
		if(scPaymentCert.getAsAtDate() == null)
			throw new ValidateBusinessLogicException("As at Date is required to submit payment");
		else if(scPaymentCert.getAsAtDate().after(currentPeriod.getTime()))
			throw new ValidateBusinessLogicException("As at Date should be on or before current period");

		if (retentionAmount<0 && !PaymentCert.DIRECT_PAYMENT.equals(scPaymentCert.getDirectPayment()))
			throw new ValidateBusinessLogicException("Retention Balance is less than zero");

		//Validation 5 - Final Payment (To be submitted one)
		if ("F".equals(scPaymentCert.getIntermFinalPayment())){
			for (SubcontractDetail scDetail:scDetailsList){
				//Skip inactive line
				if (SubcontractDetail.INACTIVE.equals(scDetail.getSystemStatus())){
					logger.info("SKIPPED - Line Type: "+scDetail.getLineType()+" ID: "+scDetail.getId()+"System Status: "+scDetail.getSystemStatus());
					continue;
				}
				//No provision allowed
				if (scDetail instanceof SubcontractDetailOA){ 
					if (Math.abs(scDetail.getScRate()*(((SubcontractDetailOA)scDetail).getCumCertifiedQuantity()-((SubcontractDetailOA)scDetail).getCumWorkDoneQuantity()))>0)
						throw new ValidateBusinessLogicException("Provision existed in "+scDetail.getSequenceNo());
				}
			}

			//Retention must be released
			if (RoundingUtil.round(retentionAmount,2)>=0.01)
				throw new ValidateBusinessLogicException("Retention balance must be zero");
		}
		
		// Check Split/Terminate Status if it implement

		// Valid Main Certificate /Main certificate received date

		return true;
	}
	
//	public static final SCPaymentCert createPaymentCert(SCPaymentCert previousPaymentCert, SCPackage scPackage, String paymentType, String createdUser) throws ValidateBusinessLogicException {
//		logger.info("Job: "+scPackage.getJob().getJobNumber()+" Package: "+scPackage.getPackageNo());
//		
//		SCPaymentCert scPaymentCert = new SCPaymentCert();
//		scPaymentCert.setDirectPayment(SCPaymentCert.NON_DIRECT_PAYMENT);
//		Double cumAmount = 0.00;
//		
//		if(scPackage.getPaymentStatus()!=null && "F".trim().equalsIgnoreCase(scPackage.getPaymentStatus().trim())){
//			throw new ValidateBusinessLogicException("The Subcontract is final paid.");
//		}
//		
//		boolean isAllApr = true;
//		if(scPackage.getScPaymentCertList()!=null && scPackage.getScPaymentCertList().size()>0){
//			int latestPaymentCert = 0;
//			for(SCPaymentCert tempSCPaymentCert:scPackage.getScPaymentCertList()){
//				if(!"APR".trim().equalsIgnoreCase(tempSCPaymentCert.getPaymentStatus().trim()))
//					isAllApr = false;
//				
//				if("PND".trim().equalsIgnoreCase(tempSCPaymentCert.getPaymentStatus().trim()))
//					scPaymentCert = tempSCPaymentCert;
//				
//				if (tempSCPaymentCert.getPaymentCertNo().intValue()>latestPaymentCert)
//					latestPaymentCert=tempSCPaymentCert.getPaymentCertNo().intValue();
//			}
//			
//			if(isAllApr){
//				scPaymentCert.setPaymentCertNo(latestPaymentCert+1);
//				scPaymentCert.setCreatedUser(createdUser);
//				scPaymentCert.setCreatedDate(new Date());
//			}
//		}else{
//			scPaymentCert.setPaymentCertNo(new Integer(1));
//			scPaymentCert.setCreatedUser(createdUser);
//			scPaymentCert.setCreatedDate(new Date());
//		}
//		scPaymentCert.setAddendumAmount(scPackage.getApprovedVOAmount());
//		scPaymentCert.setIntermFinalPayment(paymentType);
//		scPaymentCert.setPaymentStatus(SCPaymentCert.PAYMENTSTATUS_PND_PENDING);
//		scPaymentCert.setRemeasureContractSum(scPackage.getRemeasuredSubcontractSum());
//		scPaymentCert.setScPackage(scPackage);
//		
//		//Generate new Payment Detail
//		scPaymentCert.setScPaymentDetailList(SCDetailsLogic.createPaymentFromSCDetail(previousPaymentCert, scPaymentCert,createdUser));
//
//		//Calculate the Certified Amount based on Payment Posting
//		List<AccountMovementWrapper> accList = new ArrayList<AccountMovementWrapper>();
//		double cumAmount_RT = 0.0;
//		double cumAmount_CF = 0.0;
//		double cumAmount_MOS = 0.0;
//		double cumAmount_MR = 0.0;
//		for (SCPaymentDetail scPaymentDetail: scPaymentCert.getScPaymentDetailList()){
//			if ("RA".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RT".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
//				cumAmount_RT += scPaymentDetail.getMovementAmount();
//			else if ("CF".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
//				cumAmount_CF += scPaymentDetail.getMovementAmount();
//			else if ("MR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
//				cumAmount_MR = cumAmount_MR + scPaymentDetail.getMovementAmount();
//			else if ("MS".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
//				cumAmount_MOS += scPaymentDetail.getMovementAmount();
//			else if (!("GP".equalsIgnoreCase(scPaymentDetail.getLineType().trim())||"GR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))){
//				boolean accFound = false;
//				for (AccountMovementWrapper certMovement:accList)
//					if (((certMovement.getObjectCode() == null && scPaymentDetail.getObjectCode() == null) ||
//							(certMovement.getObjectCode() != null && scPaymentDetail.getObjectCode() != null &&
//							certMovement.getObjectCode().equals(scPaymentDetail.getObjectCode().trim()))) &&
//							((certMovement.getSubsidiaryCode() == null && scPaymentDetail.getSubsidiaryCode() == null) || (
//							certMovement.getSubsidiaryCode() != null && scPaymentDetail.getSubsidiaryCode() != null &&
//							certMovement.getSubsidiaryCode().equals(scPaymentDetail.getSubsidiaryCode().trim())))) {
//						accFound = true;
//						certMovement.setMovementAmount(certMovement.getMovementAmount()+scPaymentDetail.getMovementAmount());
//					}
//				
//				//Setting up Sub-contract Payment Detail lines except RA, RT, RR, CF, MR, MS, GP, GR
//				if (!accFound){
//					AccountMovementWrapper newAcc = new AccountMovementWrapper();
//					if (scPaymentDetail.getObjectCode()==null||"".equals(scPaymentDetail.getObjectCode().trim()))
//						newAcc.setObjectCode(null);
//					else 
//						newAcc.setObjectCode(scPaymentDetail.getObjectCode().trim());
//					if (scPaymentDetail.getSubsidiaryCode()==null||"".equals(scPaymentDetail.getSubsidiaryCode().trim()))
//						newAcc.setObjectCode(null);
//					else 
//						newAcc.setSubsidiaryCode(scPaymentDetail.getSubsidiaryCode().trim());
//					if (scPaymentDetail.getMovementAmount()==null||scPaymentDetail.getMovementAmount().isNaN())
//						newAcc.setMovementAmount(0.0);
//					else 
//						newAcc.setMovementAmount(scPaymentDetail.getMovementAmount());
//					accList.add(newAcc);
//				}
//					
//			}
//		}
//		
//		cumAmount = RoundingUtil.round(cumAmount_CF,2) - 
//					RoundingUtil.round(cumAmount_RT, 2)+
//					RoundingUtil.round(cumAmount_MOS, 2) -
//					RoundingUtil.round(cumAmount_MR, 2);
//		for (AccountMovementWrapper certMovement:accList)
//			cumAmount += RoundingUtil.round(certMovement.getMovementAmount(), 2);
//		scPaymentCert.setCertAmount(cumAmount);
//		scPaymentCert.setJobNo(scPackage.getJob().getJobNumber());
//		scPaymentCert.setPackageNo(scPackage.getPackageNo());
//		scPackage.getScPaymentCertList().add(scPaymentCert);
//		return scPaymentCert;
//	}
	
	/**
	 * To set Payment Status (PND, SBM, UFR, PCS, APR) of SCPayment Certificate and Payment Status (I, F) of SCPackage
	 * @author tikywong
	 * modified on May 18, 2012 9:20:47 AM
	 * Enhancement for SCPayment Review by Finance
	 */
	public static final Subcontract toCompleteApprovalProcess(PaymentCert scPaymentCert, AppSubcontractStandardTerms systemConstant, String approvalDecision)throws Exception{		
		if(scPaymentCert==null || systemConstant==null || approvalDecision==null)
			throw new DatabaseOperationException(scPaymentCert==null?"SCPayment Certificate = null":(systemConstant==null?"System Constant = null":"Approval Decision = null"));
		
		Subcontract scPackage = scPaymentCert.getSubcontract();
		if(scPackage==null)
			throw new DatabaseOperationException("SC:"+scPaymentCert.getPackageNo()+" does not exist.");
		
		JobInfo job = scPaymentCert.getSubcontract().getJobInfo();
		if(job==null)
			throw new DatabaseOperationException("Job:"+scPaymentCert.getJobNo()+" does not exist.");
		
		//Approved
		if("A".equals(approvalDecision.trim())){
			logger.info("SCPayment Certificate Approval - APPROVED");		
			logger.info("Payment Term: "+scPackage.getPaymentTerms()+
						" FinQS0Review: "+job.getFinQS0Review()+
						" SystemConstant: "+systemConstant.getFinQS0Review());
			
			//SBM --> UFR / PCS
			if(scPaymentCert.getPaymentStatus().equals("SBM")){
				if(	scPaymentCert.getSubcontract().getPaymentTerms().trim().equals("QS0") && 
					(job.getFinQS0Review().equals(JobInfo.FINQS0REVIEW_Y) || job.getFinQS0Review().equals(JobInfo.FINQS0REVIEW_D)) &&
					(systemConstant.getFinQS0Review().equals(AppSubcontractStandardTerms.FINQS0REVIEW_Y)))	
					scPaymentCert.setPaymentStatus("UFR");
				else	
					scPaymentCert.setPaymentStatus("PCS");
				
				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED+"-->"+scPaymentCert.getPaymentStatus());
			}
			//UFR --> PCS
			else if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){
				scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING);
				
				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW+"-->"+scPaymentCert.getPaymentStatus());
			}
			
			return scPaymentCert.getSubcontract(); 
		}
		//Rejected
		else{
			logger.info("SCPayment Certificate Approval - REJECTED");
			//SBM --> PND
			if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED)){
				scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PND_PENDING);
				
				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED+"-->"+scPaymentCert.getPaymentStatus());
			}
			//UFR --> remain UFR and send error message back to Approval System
			else if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))
				throw new ValidateBusinessLogicException("Payment cannot be rejected as it is going through the [Under Review by Finance] UFR process.");
			
			return scPaymentCert.getSubcontract();
		}
	}

	/**
	 * 
	 * @author tikywong
	 * refactored on Nov 28, 2013 12:11:11 PM
	 */
	public static final void updateSCDetailandPackageAfterPostingToFinance(Subcontract scPackage, List<SubcontractDetail> scDetailsList){
		logger.info("Update Package and Detail after successfully posting to Finance (AP) - Job: "+scPackage.getJobInfo().getJobNumber()+" Package: "+scPackage.getPackageNo());
		
		Double totalPostedCert = 0.0;
		Double totalCCPostedCert =0.0;
		Double totalMOSPostedCert = 0.0;
		
		for(SubcontractDetail scDetails : scDetailsList){
			if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus())){
				//1. update package detail
				scDetails.setPostedCertifiedQuantity(scDetails.getCumCertifiedQuantity());
				// Payment Requisition: Not approved but paid
				if("BQ".equals(scDetails.getLineType()) && SubcontractDetail.NOT_APPROVED.equals(scDetails.getApproved()))
					scDetails.setApproved(SubcontractDetail.NOT_APPROVED_BUT_PAID);

				//calculate total for package
				if (!"C1".equals(scDetails.getLineType()) && !"C2".equals(scDetails.getLineType()) &&!"MS".equals(scDetails.getLineType()) && !"RR".equals(scDetails.getLineType()) && !"RT".equals(scDetails.getLineType()) && !"RA".equals(scDetails.getLineType()))
					totalPostedCert = totalPostedCert + scDetails.getPostedCertifiedQuantity()*scDetails.getScRate();
				else if("C1".equals(scDetails.getLineType()) || "C2".equals(scDetails.getLineType()))
					totalCCPostedCert = totalCCPostedCert + scDetails.getPostedCertifiedQuantity()*scDetails.getScRate();
				else if("MS".equals(scDetails.getLineType()))
					totalMOSPostedCert = totalMOSPostedCert + scDetails.getPostedCertifiedQuantity()*scDetails.getScRate();
			}

		}
		
		//2. update package
		scPackage.setTotalPostedCertifiedAmount(totalPostedCert);
		scPackage.setTotalCCPostedCertAmount(totalCCPostedCert);
		scPackage.setTotalMOSPostedCertAmount(totalMOSPostedCert);
	}
}
