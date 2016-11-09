package com.gammon.qs.service.businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailAP;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailCC;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailRT;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.RoundingUtil;

public class SCPackageLogic {

	public static final String OriginalSCSum = Subcontract.RETENTION_ORIGINAL;
	public static final String RevisedSCSum = Subcontract.RETENTION_REVISED;
	public static final String LumpSum= Subcontract.RETENTION_LUMPSUM;
	
	
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

	
	/*public static Subcontract updateApprovedAddendum(Subcontract scPackage, List<SubcontractDetail> scDetailsList, String approvalResult){
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
			*//**
			 * @author koeyyeung
			 * newQuantity should be set as BQ Quantity as initial setup
			 * 16th Apr, 2015
			 * **//*
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
	}*/
	
	
	public static Subcontract toCompleteSplitTerminate(Subcontract scPackage, List<SubcontractDetail> scDetailsIncludingInactive){
		Double scSum = 0.00;
		Double approvedVO = 0.00;
		
		for (SubcontractDetail scDetail: scDetailsIncludingInactive){
			if(scDetail==null || scDetail.getSystemStatus().equals(SubcontractDetail.INACTIVE))
				continue;
			
			if (scDetail instanceof SubcontractDetailBQ){
				if (scDetail.getCostRate()!=null && 
					((!(scDetail instanceof SubcontractDetailVO)) || Math.abs(scDetail.getCostRate())>0) && 
					!scDetail.getNewQuantity().equals(scDetail.getQuantity())){
					((SubcontractDetailBQ)scDetail).setQuantity(scDetail.getNewQuantity());
					/**
					 * convert to amount based
					 * @author koeyyeung
					 * modified on 6 Sep, 2016**/
					((SubcontractDetailBQ)scDetail).setAmountSubcontract(scDetail.getAmountSubcontractNew());
					((SubcontractDetailBQ)scDetail).setAmountBudget(new BigDecimal(scDetail.getNewQuantity()*scDetail.getCostRate()).setScale(2, RoundingMode.HALF_UP));
					//((SubcontractDetailBQ)scDetail).setToBeApprovedQuantity(scDetail.getNewQuantity());
				}
				if (scDetail instanceof SubcontractDetailVO){
					if (SubcontractDetail.APPROVED.equalsIgnoreCase(scDetail.getApproved()))
						approvedVO = approvedVO + CalculationUtil.round(scDetail.getAmountSubcontract().doubleValue(), 2);
				}
				else
					scSum = scSum + CalculationUtil.round(scDetail.getAmountSubcontract().doubleValue(), 2);

			}
		}
		//scPackage.setOriginalSubcontractSum(scSum);	originalSubcontractSum cannot be updated after being awarded
		scPackage.setRemeasuredSubcontractSum(new BigDecimal(scSum));
		scPackage.setApprovedVOAmount(new BigDecimal(approvedVO));
		
		if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.roundToBigDecimal(new BigDecimal(scSum * scPackage.getMaxRetentionPercentage()/100.00),2));	
		if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.roundToBigDecimal(new BigDecimal((scSum+approvedVO) * scPackage.getMaxRetentionPercentage()/100.00),2));
		
		return scPackage;
	}

}
