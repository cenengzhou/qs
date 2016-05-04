package com.gammon.qs.service.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsAP;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsRT;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.util.RoundingUtil;

public class SCPackageLogic {

	@Autowired
	private static TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;

	@PostConstruct
	public void init(){
		SCPackageLogic.setTenderAnalysisDetailHBDao(tenderAnalysisDetailHBDao);
	}
	
	public static final String OriginalSCSum = SCPackage.RETENTION_ORIGINAL;
	public static final String RevisedSCSum = SCPackage.RETENTION_REVISED;
	public static final String LumpSum= SCPackage.RETENTION_LUMPSUM;
	
	public static final String ableToSubmitAddendum(SCPackage scPackage, List<SCPaymentCert> scPaymentCertList){
		if ("1".equals(scPackage.getSubmittedAddendum()))
			return "Addendum Submitted";
		for (SCPaymentCert paymentCert:scPaymentCertList)
			if (!"APR".equals(paymentCert.getPaymentStatus()) && !"PND".equals(paymentCert.getPaymentStatus()))
				return "Payment Submitted";
		return null;
	}

	public static SCDetails createSCDetailByLineType(String scLineType) {
		if ("V1".equals(scLineType)||"V2".equals(scLineType)||"D1".equals(scLineType)||
			"D2".equals(scLineType)||"L1".equals(scLineType)||"L2".equals(scLineType)||"CF".equals(scLineType))
			return new SCDetailsVO();
		if ("C1".equals(scLineType))
			return new SCDetailsCC();
		if ("RA".equals(scLineType)||"RR".equals(scLineType))
			return new SCDetailsRT();
		if ("OA".equals(scLineType))
			return new SCDetailsOA();
		if ("AP".equals(scLineType)||"MS".equals(scLineType))
			return new SCDetailsAP();
		if ("BQ".equals(scLineType)||"B1".equals(scLineType))
			return new SCDetailsBQ();
		return null;
	}

	
	public static SCPackage updateApprovedAddendum(SCPackage scPackage, List<SCDetails> scDetailsList, String approvalResult){
		Double cum = new Double(0);
		BigDecimal remeasureSum = new BigDecimal(0);
		scPackage.setSubmittedAddendum(SCPackage.ADDENDUM_NOT_SUBMITTED);
		if (!"A".equals(approvalResult))
			return scPackage;
		for(SCDetails scDetails: scDetailsList){
			if(scDetails instanceof SCDetailsVO){
				if(scDetails.getSystemStatus().equals(BasePersistedAuditObject.ACTIVE) && (scDetails.getApproved()==null || !(SCDetails.SUSPEND.equalsIgnoreCase(scDetails.getApproved().trim())))){
					if(scDetails.getToBeApprovedQuantity() != scDetails.getQuantity())
						scDetails.setQuantity(scDetails.getToBeApprovedQuantity());
					if(scDetails.getToBeApprovedRate() != scDetails.getScRate())
						if (Double.valueOf(0.0).equals(scDetails.getToBeApprovedRate()) && !Double.valueOf(0).equals(scDetails.getPostedCertifiedQuantity())){
							scDetails.setQuantity(0.0);
							scDetails.setCumCertifiedQuantity(0.0);
						}
						else
							scDetails.setScRate(scDetails.getToBeApprovedRate());
					
					scDetails.setApproved(SCDetails.APPROVED);
					cum+=scDetails.getTotalAmount();
				}
			}else if (scDetails instanceof SCDetailsBQ){
				if(scDetails.getToBeApprovedQuantity() != scDetails.getQuantity())
					scDetails.setQuantity(scDetails.getToBeApprovedQuantity());
				remeasureSum=remeasureSum.add(BigDecimal.valueOf(scDetails.getTotalAmount()));
			}
			/**
			 * @author koeyyeung
			 * newQuantity should be set as BQ Quantity as initial setup
			 * 16th Apr, 2015
			 * **/
			scDetails.setNewQuantity(scDetails.getQuantity());
			scDetails.setJobNo(scPackage.getJob().getJobNumber());
		}
		scPackage.setLatestAddendumValueUpdatedDate(new Date());
		scPackage.setApprovedVOAmount(cum);
		if (scPackage.getJob().getRepackagingType()!=null &&("2".equals(scPackage.getJob().getRepackagingType().trim())||"3".equals(scPackage.getJob().getRepackagingType().trim())))
			scPackage.setRemeasuredSubcontractSum(remeasureSum.doubleValue());
		if(scPackage.getRetentionTerms() == null){
			scPackage.setRetentionAmount(0.00);
		}else if(SCPackage.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms().trim())){
			scPackage.setRetentionAmount(RoundingUtil.round(scPackage.getMaxRetentionPercentage()*(scPackage.getApprovedVOAmount()+scPackage.getRemeasuredSubcontractSum())/100.00,2));
		}else if(SCPackage.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms().trim())){
			scPackage.setRetentionAmount(RoundingUtil.round(scPackage.getMaxRetentionPercentage()*scPackage.getOriginalSubcontractSum()/100.00,2));
		}
		return scPackage;
	}
	public static SCPackage awardSCPackage(SCPackage scPackage, List<TenderAnalysis> tenderAnalysisList){
		SCDetailsBQ scDetails;
		Double scSum = 0.00;
		TenderAnalysis budgetTA = null;
		for (TenderAnalysis TA: tenderAnalysisList){
			if (Integer.valueOf(0).equals(TA.getVendorNo())){
				budgetTA = TA;
			}
		}
		for(TenderAnalysis TA: tenderAnalysisList){
			if(TA.getStatus()!=null && "RCM".equalsIgnoreCase(TA.getStatus().trim())){
				TA.setStatus("AWD");
				scPackage.setVendorNo(TA.getVendorNo().toString());
				scPackage.setPaymentCurrency(TA.getCurrencyCode().trim());
				scPackage.setExchangeRate(TA.getExchangeRate());
				try { //TODO: static injection
					for(TenderAnalysisDetail TADetails: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA)){
						scSum = scSum + (TADetails.getQuantity()*TADetails.getFeedbackRateDomestic());
						scDetails = new SCDetailsBQ();
						scDetails.setScPackage(scPackage);
						scDetails.setSequenceNo(TADetails.getSequenceNo());
						scDetails.setResourceNo(TADetails.getResourceNo());
						if("BQ".equalsIgnoreCase(TADetails.getLineType())){
							if (budgetTA!=null)
								try {
									for (TenderAnalysisDetail budgetTADetail:tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(budgetTA))
										if (TADetails.getSequenceNo().equals(budgetTADetail.getSequenceNo())){
											scDetails.setCostRate(budgetTADetail.getFeedbackRateDomestic());
										}
								} catch (DatabaseOperationException e) {
									e.printStackTrace();
								}
						}else{
							scDetails.setCostRate(0.00);
						}
						scDetails.setBillItem(TADetails.getBillItem()==null?" ":TADetails.getBillItem());
						scDetails.setDescription(TADetails.getDescription());
						scDetails.setOriginalQuantity(TADetails.getQuantity());
						scDetails.setQuantity(TADetails.getQuantity());
						scDetails.setToBeApprovedQuantity(TADetails.getQuantity());
						scDetails.setScRate(TADetails.getFeedbackRateDomestic());
						scDetails.setSubsidiaryCode(TADetails.getSubsidiaryCode());
						scDetails.setObjectCode(TADetails.getObjectCode());
						scDetails.setLineType(TADetails.getLineType());
						scDetails.setUnit(TADetails.getUnit());
						scDetails.setRemark(TADetails.getRemark());
						scDetails.setApproved(SCDetails.APPROVED);
						scDetails.setNewQuantity(TADetails.getQuantity());
						scDetails.setJobNo(scPackage.getJob().getJobNumber());
						scDetails.setTenderAnalysisDetail_ID(TADetails.getId());
						scDetails.populate(TADetails.getLastModifiedUser()!=null?TADetails.getLastModifiedUser():TADetails.getCreatedUser());
						scDetails.setScPackage(scPackage);;
					}
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
		}
		scPackage.setScApprovalDate(new Date());
		if (OriginalSCSum.equals(scPackage.getRetentionTerms()) || RevisedSCSum.equals(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(RoundingUtil.round(scSum*scPackage.getMaxRetentionPercentage()/100.00,2));
		else if(LumpSum.equals(scPackage.getRetentionTerms()))
			scPackage.setMaxRetentionPercentage(0.00);
		else{
			scPackage.setMaxRetentionPercentage(0.00);
			scPackage.setInterimRentionPercentage(0.00);
			scPackage.setMosRetentionPercentage(0.00);
			scPackage.setRetentionAmount(0.00);
		}
		
		scPackage.setApprovedVOAmount(0.00);
		scPackage.setRemeasuredSubcontractSum(scSum);
		scPackage.setOriginalSubcontractSum(scSum);
//		scPackage.setAccumlatedRetention(0.00);			//commented by Tiky Wong on 10 January, 2014 - Retention that was hold with direct payment was missing out in the Accumulated Retention
		scPackage.setSubcontractStatus(500);
		return scPackage;
	}
	
	public static SCPackage toCompleteSplitTerminate(SCPackage scPackage, List<SCDetails> scDetailsIncludingInactive){
		Double scSum = 0.00;
		Double approvedVO = 0.00;
		
		for (SCDetails scDetail: scDetailsIncludingInactive){
			if(scDetail==null || scDetail.getSystemStatus().equals(SCDetails.INACTIVE))
				continue;
			
			if (scDetail instanceof SCDetailsBQ){
				// if Qty <> newQty and either it is BQ/B1 or has budget(cost Rate >0)
				if (scDetail.getCostRate()!=null && 
					((!(scDetail instanceof SCDetailsVO)) || Math.abs(scDetail.getCostRate())>0) && 
					!scDetail.getNewQuantity().equals(scDetail.getQuantity())){
					((SCDetailsBQ)scDetail).setQuantity(scDetail.getNewQuantity());
					((SCDetailsBQ)scDetail).setToBeApprovedQuantity(scDetail.getNewQuantity());
				}
				if (scDetail instanceof SCDetailsVO){
					if (SCDetails.APPROVED.equalsIgnoreCase(scDetail.getApproved()))
						approvedVO = approvedVO + (scDetail.getQuantity()*scDetail.getScRate());
				}
				else
					scSum = scSum + (scDetail.getQuantity()*scDetail.getScRate());

			}
		}
		//scPackage.setOriginalSubcontractSum(scSum);	originalSCSum cannot be updated after being awarded
		scPackage.setRemeasuredSubcontractSum(scSum);
		scPackage.setApprovedVOAmount(approvedVO);
		
		if(SCPackage.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(RoundingUtil.round(scSum * scPackage.getMaxRetentionPercentage()/100.00,2));	
		if(SCPackage.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(RoundingUtil.round((scSum+approvedVO) * scPackage.getMaxRetentionPercentage()/100.00,2));
		
		return scPackage;
	}

	public TenderAnalysisDetailHBDao getTenderAnalysisDetailHBDao() {
		return tenderAnalysisDetailHBDao;
	}

	public static void setTenderAnalysisDetailHBDao(TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao) {
		SCPackageLogic.tenderAnalysisDetailHBDao = tenderAnalysisDetailHBDao;
	}
}
