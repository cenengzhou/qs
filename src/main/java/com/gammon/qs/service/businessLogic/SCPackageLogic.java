package com.gammon.qs.service.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailAP;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailCC;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailRT;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.util.RoundingUtil;

public class SCPackageLogic {

	@Autowired
	private static TenderDetailHBDao tenderAnalysisDetailHBDao;

	@PostConstruct
	public void init(){
		SCPackageLogic.setTenderAnalysisDetailHBDao(tenderAnalysisDetailHBDao);
	}
	
	public static final String OriginalSCSum = Subcontract.RETENTION_ORIGINAL;
	public static final String RevisedSCSum = Subcontract.RETENTION_REVISED;
	public static final String LumpSum= Subcontract.RETENTION_LUMPSUM;
	
	public static final String ableToSubmitAddendum(Subcontract scPackage, List<PaymentCert> scPaymentCertList){
		if ("1".equals(scPackage.getSubmittedAddendum()))
			return "Addendum Submitted";
		for (PaymentCert paymentCert:scPaymentCertList)
			if (!"APR".equals(paymentCert.getPaymentStatus()) && !"PND".equals(paymentCert.getPaymentStatus()))
				return "Payment Submitted";
		return null;
	}

	public static SubcontractDetail createSCDetailByLineType(String scLineType) {
		if ("V1".equals(scLineType)||"V2".equals(scLineType)||"D1".equals(scLineType)||
			"D2".equals(scLineType)||"L1".equals(scLineType)||"L2".equals(scLineType)||"CF".equals(scLineType))
			return new SubcontractDetailVO();
		if ("C1".equals(scLineType))
			return new SubcontractDetailCC();
		if ("RA".equals(scLineType)||"RR".equals(scLineType))
			return new SubcontractDetailRT();
		if ("OA".equals(scLineType))
			return new SubcontractDetailOA();
		if ("AP".equals(scLineType)||"MS".equals(scLineType))
			return new SubcontractDetailAP();
		if ("BQ".equals(scLineType)||"B1".equals(scLineType))
			return new SubcontractDetailBQ();
		return null;
	}

	
	public static Subcontract updateApprovedAddendum(Subcontract scPackage, List<SubcontractDetail> scDetailsList, String approvalResult){
		Double cum = new Double(0);
		BigDecimal remeasureSum = new BigDecimal(0);
		scPackage.setSubmittedAddendum(Subcontract.ADDENDUM_NOT_SUBMITTED);
		if (!"A".equals(approvalResult))
			return scPackage;
		for(SubcontractDetail scDetails: scDetailsList){
			if(scDetails instanceof SubcontractDetailVO){
				if(scDetails.getSystemStatus().equals(BasePersistedAuditObject.ACTIVE) && (scDetails.getApproved()==null || !(SubcontractDetail.SUSPEND.equalsIgnoreCase(scDetails.getApproved().trim())))){
					if(scDetails.getToBeApprovedQuantity() != scDetails.getQuantity())
						scDetails.setQuantity(scDetails.getToBeApprovedQuantity());
					if(scDetails.getToBeApprovedRate() != scDetails.getScRate())
						if (Double.valueOf(0.0).equals(scDetails.getToBeApprovedRate()) && !Double.valueOf(0).equals(scDetails.getPostedCertifiedQuantity())){
							scDetails.setQuantity(0.0);
							scDetails.setCumCertifiedQuantity(0.0);
						}
						else
							scDetails.setScRate(scDetails.getToBeApprovedRate());
					
					scDetails.setApproved(SubcontractDetail.APPROVED);
					cum+=scDetails.getTotalAmount();
				}
			}else if (scDetails instanceof SubcontractDetailBQ){
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
			scDetails.setJobNo(scPackage.getJobInfo().getJobNumber());
		}
		scPackage.setLatestAddendumValueUpdatedDate(new Date());
		scPackage.setApprovedVOAmount(cum);
		if (scPackage.getJobInfo().getRepackagingType()!=null &&("2".equals(scPackage.getJobInfo().getRepackagingType().trim())||"3".equals(scPackage.getJobInfo().getRepackagingType().trim())))
			scPackage.setRemeasuredSubcontractSum(remeasureSum.doubleValue());
		if(scPackage.getRetentionTerms() == null){
			scPackage.setRetentionAmount(0.00);
		}else if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms().trim())){
			scPackage.setRetentionAmount(RoundingUtil.round(scPackage.getMaxRetentionPercentage()*(scPackage.getApprovedVOAmount()+scPackage.getRemeasuredSubcontractSum())/100.00,2));
		}else if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms().trim())){
			scPackage.setRetentionAmount(RoundingUtil.round(scPackage.getMaxRetentionPercentage()*scPackage.getOriginalSubcontractSum()/100.00,2));
		}
		return scPackage;
	}
	public static Subcontract awardSCPackage(Subcontract scPackage, List<Tender> tenderAnalysisList){
		SubcontractDetailBQ scDetails;
		Double scSum = 0.00;
		Tender budgetTA = null;
		for (Tender TA: tenderAnalysisList){
			if (Integer.valueOf(0).equals(TA.getVendorNo())){
				budgetTA = TA;
			}
		}
		for(Tender TA: tenderAnalysisList){
			if(TA.getStatus()!=null && Tender.TA_STATUS_RCM.equalsIgnoreCase(TA.getStatus().trim())){
				TA.setStatus(Tender.TA_STATUS_AWD);
				scPackage.setVendorNo(TA.getVendorNo().toString());
				scPackage.setPaymentCurrency(TA.getCurrencyCode().trim());
				scPackage.setExchangeRate(TA.getExchangeRate());
				try { //
					for(TenderDetail TADetails: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA)){
						scSum = scSum + (TADetails.getQuantity()*TADetails.getRateBudget());
						scDetails = new SubcontractDetailBQ();
						scDetails.setSubcontract(scPackage);
						scDetails.setSequenceNo(TADetails.getSequenceNo());
						scDetails.setResourceNo(TADetails.getResourceNo());
						if("BQ".equalsIgnoreCase(TADetails.getLineType())){
							if (budgetTA!=null)
								try {
									for (TenderDetail budgetTADetail:tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(budgetTA))
										if (TADetails.getSequenceNo().equals(budgetTADetail.getSequenceNo())){
											scDetails.setCostRate(budgetTADetail.getRateBudget());
										}
								} catch (DataAccessException e) {
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
						scDetails.setScRate(TADetails.getRateBudget());
						scDetails.setSubsidiaryCode(TADetails.getSubsidiaryCode());
						scDetails.setObjectCode(TADetails.getObjectCode());
						scDetails.setLineType(TADetails.getLineType());
						scDetails.setUnit(TADetails.getUnit());
						scDetails.setRemark(TADetails.getRemark());
						scDetails.setApproved(SubcontractDetail.APPROVED);
						scDetails.setNewQuantity(TADetails.getQuantity());
						scDetails.setJobNo(scPackage.getJobInfo().getJobNumber());
						scDetails.setTenderAnalysisDetail_ID(TADetails.getId());
						scDetails.populate(TADetails.getLastModifiedUser()!=null?TADetails.getLastModifiedUser():TADetails.getCreatedUser());
						scDetails.setSubcontract(scPackage);
						/**
						 * @author koeyyeung
						 * created on 12 July, 2016
						 * Convert to amount based**/
						scDetails.setAmountBudget(TADetails.getAmountBudget());
						scDetails.setAmountSubcontract(TADetails.getAmountSubcontract());
						scDetails.setAmountSubcontractTBA(TADetails.getAmountSubcontract());
					}
				} catch (DataAccessException e) {
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
	
	public static Subcontract toCompleteSplitTerminate(Subcontract scPackage, List<SubcontractDetail> scDetailsIncludingInactive){
		Double scSum = 0.00;
		Double approvedVO = 0.00;
		
		for (SubcontractDetail scDetail: scDetailsIncludingInactive){
			if(scDetail==null || scDetail.getSystemStatus().equals(SubcontractDetail.INACTIVE))
				continue;
			
			if (scDetail instanceof SubcontractDetailBQ){
				// if Qty <> newQty and either it is BQ/B1 or has budget(cost Rate >0)
				if (scDetail.getCostRate()!=null && 
					((!(scDetail instanceof SubcontractDetailVO)) || Math.abs(scDetail.getCostRate())>0) && 
					!scDetail.getNewQuantity().equals(scDetail.getQuantity())){
					((SubcontractDetailBQ)scDetail).setQuantity(scDetail.getNewQuantity());
					((SubcontractDetailBQ)scDetail).setToBeApprovedQuantity(scDetail.getNewQuantity());
				}
				if (scDetail instanceof SubcontractDetailVO){
					if (SubcontractDetail.APPROVED.equalsIgnoreCase(scDetail.getApproved()))
						approvedVO = approvedVO + (scDetail.getQuantity()*scDetail.getScRate());
				}
				else
					scSum = scSum + (scDetail.getQuantity()*scDetail.getScRate());

			}
		}
		//scPackage.setOriginalSubcontractSum(scSum);	originalSCSum cannot be updated after being awarded
		scPackage.setRemeasuredSubcontractSum(scSum);
		scPackage.setApprovedVOAmount(approvedVO);
		
		if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(RoundingUtil.round(scSum * scPackage.getMaxRetentionPercentage()/100.00,2));	
		if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(RoundingUtil.round((scSum+approvedVO) * scPackage.getMaxRetentionPercentage()/100.00,2));
		
		return scPackage;
	}

	public TenderDetailHBDao getTenderAnalysisDetailHBDao() {
		return tenderAnalysisDetailHBDao;
	}

	public static void setTenderAnalysisDetailHBDao(TenderDetailHBDao tenderAnalysisDetailHBDao) {
		SCPackageLogic.tenderAnalysisDetailHBDao = tenderAnalysisDetailHBDao;
	}
}
