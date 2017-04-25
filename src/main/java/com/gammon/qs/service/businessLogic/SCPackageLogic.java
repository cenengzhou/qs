package com.gammon.qs.service.businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.shared.util.CalculationUtil;

public class SCPackageLogic {

	public static final String OriginalSCSum = Subcontract.RETENTION_ORIGINAL;
	public static final String RevisedSCSum = Subcontract.RETENTION_REVISED;
	public static final String LumpSum= Subcontract.RETENTION_LUMPSUM;
	
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
		scPackage.setRemeasuredSubcontractSum(CalculationUtil.roundToBigDecimal(scSum, 2));
		scPackage.setApprovedVOAmount(CalculationUtil.roundToBigDecimal(approvedVO, 2));
		
		if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.roundToBigDecimal((scSum * scPackage.getMaxRetentionPercentage()/100.00), 2));	
		if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.roundToBigDecimal(((scSum+approvedVO) * scPackage.getMaxRetentionPercentage()/100.00), 2));
		
		return scPackage;
	}

}
