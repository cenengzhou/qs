package com.gammon.qs.service.businessLogic;

import java.util.List;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsAP;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsRT;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.updateAddendum.UpdateAddendumWrapper;

public class SCDetailsLogic {
		
	/**
	 * modified by matthew lam
	 * This method sets values to the scDetails object <b style="color:red">ONLY</b>:<br />
	 * data in database are NOT updated in this method
	 *
	 * @param scDetails
	 * @param wrapper
	 * @return
	 * @throws ValidateBusinessLogicException
	 * @since Feb 11, 2015 4:30:16 PM
	 */
	public static final boolean updateSCDetails(SCDetails scDetails, UpdateAddendumWrapper wrapper) throws ValidateBusinessLogicException {
		double postedWDQty = 0;
		if (scDetails instanceof SCDetailsOA) {
			postedWDQty = ((SCDetailsOA) scDetails).getPostedWorkDoneQuantity();
		}

		if (scDetails.getCostRate() != null && (Math.abs(scDetails.getCostRate()) > 0))
			if (RoundingUtil.round(Math.abs(wrapper.getToBeApprovedQty() - scDetails.getToBeApprovedQuantity()), 7) > 0)
				throw new ValidateBusinessLogicException("Cannot change the quantity of VO line with budget!");

		if (Math.abs(scDetails.getPostedCertifiedQuantity()) > 0 || Math.abs(postedWDQty) > 0) {
			if (scDetails instanceof SCDetailsVO) {
				if (RoundingUtil.round(Math.abs(wrapper.getToBeApprovedRate() - scDetails.getToBeApprovedRate()), 7) > 0)
					throw new ValidateBusinessLogicException("posted Cert Qty and posted Workdone Qty must be zero!");
				if (Math.abs(wrapper.getToBeApprovedQty()) - Math.abs(scDetails.getCumCertifiedQuantity()) < 0 || Math.abs(wrapper.getToBeApprovedQty()) - Math.abs(scDetails.getPostedCertifiedQuantity()) < 0)
					throw new ValidateBusinessLogicException("To be approved Quantity must be larger than Cert Quantity!");
				// Workdone are not neccessary to check
				// if (Math.abs(wrapper.getToBeApprovedQty())-Math.abs(scDetails.getCumWorkDoneQuantity())<0 ||Math.abs(wrapper.getToBeApprovedQty())-Math.abs(scDetails.getPostedWorkDoneQuantity())<0)
				// throw new ValidateBusinessLogicException("To be approved Quantity must be larger than Workdone Quantity!");
				if (wrapper.getToBeApprovedQty() != null && (scDetails.getCostRate() == null || !(Math.abs(scDetails.getCostRate()) > 0)))
					scDetails.setToBeApprovedQuantity(wrapper.getToBeApprovedQty());
				if (wrapper.getToBeApprovedRate() != null)
					scDetails.setToBeApprovedRate(wrapper.getToBeApprovedRate());
			}
			if (!(scDetails instanceof SCDetailsBQ)) {
				if (wrapper.getBqQuantity() != null)
					scDetails.setQuantity(wrapper.getBqQuantity());
				/* 
				 * modified by matthewlam
				 * Bug fix #9 -SCRATE of C1 can be updated even with certified quantity > 0
				 * the logic was not changed, however, the custom algorithm comparison of doubles is replaced
				 * and the exception message is updated 
				 */
				if (Double.compare(wrapper.getScRate(), scDetails.getScRate()) != 0)
					throw new ValidateBusinessLogicException("SC Rate cannot be changed");
			}

			if (scDetails.getObjectCode() == null) {
				if (wrapper.getObject() != null)
					return false;
			} else if (!scDetails.getObjectCode().equals(wrapper.getObject()))
				return false;
			if (scDetails.getSubsidiaryCode() == null || "".equals(scDetails.getSubsidiaryCode().trim())) {
				if (wrapper.getSubsidiary() != null && !"".equals(wrapper.getSubsidiary().trim()))
					return false;
			} else if (!scDetails.getSubsidiaryCode().equals(wrapper.getSubsidiary()))
				return false;
		} else {

			scDetails.setObjectCode(wrapper.getObject());
			scDetails.setSubsidiaryCode(wrapper.getSubsidiary());
			if (scDetails instanceof SCDetailsBQ) {
				((SCDetailsBQ) scDetails).setToBeApprovedQuantity(wrapper.getToBeApprovedQty());
			} else
				scDetails.setQuantity(wrapper.getBqQuantity());
			if (scDetails instanceof SCDetailsVO) {
				((SCDetailsVO) scDetails).setToBeApprovedRate(wrapper.getToBeApprovedRate());
				if (!SCDetails.APPROVED.equals(scDetails.getApproved()))
					scDetails.setScRate(wrapper.getToBeApprovedRate());
			} else if (!(scDetails instanceof SCDetailsBQ))
				scDetails.setScRate(wrapper.getScRate());
		}
		if (wrapper.getAltObjectCode() != null && !"".equals(wrapper.getAltObjectCode().trim()) && !wrapper.getAltObjectCode().startsWith(
				"11"))
			throw new ValidateBusinessLogicException("Alternate Object Code must be in Labour Object Code!");
		return true;
	}
		
	public static final boolean isToBeApprovedLine(SCDetails scDetails){
		
		if (!SCDetails.SUSPEND.equals(scDetails.getApproved())&& 
			!SCDetails.APPROVED.equals(scDetails.getApproved()))
			return true;
		
		if (scDetails instanceof SCDetailsVO){
			if (Math.abs(((SCDetailsVO)scDetails).getTotalAmount()-((SCDetailsVO)scDetails).getToBeApprovedAmount())>=0.01)
				return true;
		}
		else if (scDetails instanceof SCDetailsBQ){
			if (Math.abs(((SCDetailsBQ)scDetails).getTotalAmount()-((SCDetailsBQ)scDetails).getToBeApprovedAmount())>=0.01)
				return true;
		}
		return false;
	}
	
//	public static final List<SCPaymentDetail> createPaymentFromSCDetail(SCPaymentCert previousPaymentCert, SCPaymentCert scPaymentCert, String lastModifiedUser){	
//		if (scPaymentCert.getScPackage().getScDetails()!=null){
//			List<SCPaymentDetail> resultList = new ArrayList<SCPaymentDetail>();
//			double totalPaidAmount = 0;
//			double totalPaidMovement = 0;
//			double totalMOSAmount = 0;
//			double totalMOSMovement = 0;
//			String tempSubsidCode = "";
//			String tempObjCode = "";
//			Date createdDate = new Date();
//			
//			for (SCDetails scDetails:scPaymentCert.getScPackage().getScDetails()){
//				SCPaymentDetail scPaymentDetail = new SCPaymentDetail();
//				scPaymentDetail.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
//				scPaymentDetail.setBillItem(scDetails.getBillItem());
//				scPaymentDetail.setScSeqNo(scDetails.getSequenceNo());
//				scPaymentDetail.setObjectCode(scDetails.getObjectCode());
//				scPaymentDetail.setSubsidiaryCode(scDetails.getSubsidiaryCode());
//				scPaymentDetail.setDescription(scDetails.getDescription());
//				scPaymentDetail.setLineType(scDetails.getLineType());
//				
//				//Cumulative Certified Amount
//				if (scDetails.getCumCertifiedQuantity()!=null)
//					scPaymentDetail.setCumAmount(scDetails.getCumCertifiedQuantity()*scDetails.getScRate());
//				else 
//					scPaymentDetail.setCumAmount(0.0);
//				if(previousPaymentCert!=null){
////					SCPaymentDetail previousSCPaymentDetail = 
//				}
//				
//				//Cumulative Certified Amount
//				if (scDetails.getPostedCertifiedQuantity()!=null){
//					double postedCertifiedAmount = CalculationUtil.round(scDetails.getPostedCertifiedQuantity()*scDetails.getScRate(), 2);
//					scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount()-postedCertifiedAmount);
//				}
//				else 
//					scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount());
//				
//				scPaymentDetail.setScPaymentCert(scPaymentCert);
//				scPaymentDetail.setScDetail(scDetails);
//				scPaymentDetail.setCreatedUser(lastModifiedUser);
//				scPaymentDetail.setCreatedDate(createdDate);
//				scPaymentDetail.setLastModifiedUser(lastModifiedUser);
//				
//				if (scDetails instanceof SCDetailsBQ){
//					totalPaidAmount += scPaymentDetail.getCumAmount();
//					totalPaidMovement += scPaymentDetail.getMovementAmount();
//				}
//				
//				if (scDetails.getLineType()!=null && "MS".equals(scDetails.getLineType().trim())){
//					totalMOSAmount += scPaymentDetail.getCumAmount();
//					totalMOSMovement += scPaymentDetail.getMovementAmount();
//					tempSubsidCode = scDetails.getSubsidiaryCode();
//					tempObjCode = scDetails.getObjectCode();
//				}
//				resultList.add(scPaymentDetail);
//			}
//			
//			double preRTAmount = 0.0;
//			double preMRAmount = 0.0;
//			double preGPAmount = 0.0;
//			double preGRAmount = 0.0;
//			SCPaymentDetail scPaymentDetailGP = new SCPaymentDetail();
//			SCPaymentDetail scPaymentDetailGR = new SCPaymentDetail();
//			SCPaymentDetail scPaymentDetailRT = new SCPaymentDetail();
//			if (scPaymentCert.getPaymentCertNo()>1)
//				for (SCPaymentCert searchSCPaymentCert:scPaymentCert.getScPackage().getScPaymentCertList())
//					if (searchSCPaymentCert.getPaymentCertNo().equals(scPaymentCert.getPaymentCertNo()-1)){
//						for (SCPaymentDetail preSCPaymentDetail:searchSCPaymentCert.getScPaymentDetailList())
//							if ("RT".equals(preSCPaymentDetail.getLineType()))
//								preRTAmount = preSCPaymentDetail.getCumAmount();
//							else if ("MR".equals(preSCPaymentDetail.getLineType()))
//								preMRAmount += preSCPaymentDetail.getCumAmount();
//							else if ("GP".equals(preSCPaymentDetail.getLineType())){
//								preGPAmount = preSCPaymentDetail.getCumAmount();
//								scPaymentDetailGP.setCumAmount(preSCPaymentDetail.getCumAmount());}
//							else if ("GR".equals(preSCPaymentDetail.getLineType())){
//								preGRAmount = preSCPaymentDetail.getCumAmount();
//								scPaymentDetailGR.setCumAmount(preSCPaymentDetail.getCumAmount());
//							}
//						break;
//					}
//			//create MR payment Detail
//			//Multiply double rounding problem
//			//@author Peter Chan
//			double cumMOSRetention = RoundingUtil.round(RoundingUtil.multiple(scPaymentCert.getScPackage().getMosRetentionPercentage(),totalMOSAmount)/100.0,2);
//			if(cumMOSRetention != 0 || preMRAmount != 0){
//				SCPaymentDetail scPaymentDetailMR = new SCPaymentDetail();
//				scPaymentDetailMR.setBillItem("");
//				scPaymentDetailMR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
//				scPaymentDetailMR.setScPaymentCert(scPaymentCert);
//				scPaymentDetailMR.setLineType("MR");
//				scPaymentDetailMR.setObjectCode(tempObjCode);
//				scPaymentDetailMR.setSubsidiaryCode(tempSubsidCode);
//				scPaymentDetailMR.setCumAmount(cumMOSRetention);
//				scPaymentDetailMR.setMovementAmount(cumMOSRetention - preMRAmount);
//				scPaymentDetailMR.setScSeqNo(100002);
//				scPaymentDetailMR.setLastModifiedUser(lastModifiedUser);
//				scPaymentDetailMR.setCreatedUser(lastModifiedUser);
//				scPaymentDetailMR.setCreatedDate(createdDate);
//				resultList.add(scPaymentDetailMR);
//			}
//			
//			//Multiply double rounding problem
//			//@author Peter Chan
//			double cumRetention = RoundingUtil.round(RoundingUtil.multiple(scPaymentCert.getScPackage().getInterimRentionPercentage(),totalPaidAmount)/100.0,2);
//
//			//Fixing:
//			//Define upper limit of retention
//			double retentionUpperLimit = scPaymentCert.getScPackage().getRetentionAmount();
//			if (scPaymentCert.getScPackage().getRetentionAmount()<scPaymentCert.getScPackage().getAccumlatedRetention())
//				retentionUpperLimit = scPaymentCert.getScPackage().getAccumlatedRetention();
//			
//			//SCPayment's RT cannot be larger than "Retention Amount"/"Accumulated Retention Amount" in SC Header
//			if (cumRetention>retentionUpperLimit)
//				cumRetention = retentionUpperLimit;
//			
//			//create MS payment Detail
//			
//			//create RT Payment Detail
//			if(scPaymentCert.getPaymentStatus()!=null && "F".equalsIgnoreCase(scPaymentCert.getIntermFinalPayment().trim())){
//				cumRetention = preRTAmount;
//			}
//			scPaymentDetailRT.setScPaymentCert(scPaymentCert);
//			scPaymentDetailRT.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
//			scPaymentDetailRT.setBillItem("");
//			scPaymentDetailRT.setLineType("RT");
//			scPaymentDetailRT.setObjectCode("");
//			scPaymentDetailRT.setSubsidiaryCode("");
//			scPaymentDetailRT.setCumAmount(cumRetention);
//			scPaymentDetailRT.setMovementAmount(cumRetention - preRTAmount);
//			scPaymentDetailRT.setScSeqNo(100001);
//			scPaymentDetailRT.setLastModifiedUser(lastModifiedUser);
//			scPaymentDetailRT.setCreatedUser(lastModifiedUser);
//			scPaymentDetailRT.setCreatedDate(createdDate);
//			resultList.add(scPaymentDetailRT);
//
//			//create GP payment Detail
//			scPaymentDetailGP.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
//			scPaymentDetailGP.setScPaymentCert(scPaymentCert);
//			scPaymentDetailGP.setBillItem("");
//			scPaymentDetailGP.setLineType("GP");
//			scPaymentDetailGP.setObjectCode("");
//			scPaymentDetailGP.setSubsidiaryCode("");
//			scPaymentDetailGP.setCumAmount(preGPAmount);
//			scPaymentDetailGP.setMovementAmount(0.00);
//			scPaymentDetailGP.setScSeqNo(100003);
//			scPaymentDetailGP.setLastModifiedUser(lastModifiedUser);
//			scPaymentDetailGP.setCreatedUser(lastModifiedUser);
//			scPaymentDetailGP.setCreatedDate(createdDate);
//			resultList.add(scPaymentDetailGP);
//			
//			//create GP payment Detail
//			scPaymentDetailGR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
//			scPaymentDetailGR.setScPaymentCert(scPaymentCert);
//			scPaymentDetailGR.setBillItem("");
//			scPaymentDetailGR.setLineType("GR");
//			scPaymentDetailGR.setObjectCode("");
//			scPaymentDetailGR.setSubsidiaryCode("");
//			scPaymentDetailGR.setCumAmount(preGRAmount);
//			scPaymentDetailGR.setMovementAmount(0.00);
//			scPaymentDetailGR.setScSeqNo(100004);
//			scPaymentDetailGR.setLastModifiedUser(lastModifiedUser);
//			scPaymentDetailGR.setCreatedUser(lastModifiedUser);
//			scPaymentDetailGR.setCreatedDate(createdDate);
//			resultList.add(scPaymentDetailGR);
//			
//			return resultList;
//		}
//		return null;
//	}

	public static String generateBillItem(SCPackage scPackage, String scLineType, Integer maxSeqNo) {
		String maxSeqNoStr="";
		String billItemMid="/ /"+scLineType.trim()+"/";
		for (int i=0;i<4-maxSeqNo.toString().length();i++)
			maxSeqNoStr+="0";
		maxSeqNoStr+=maxSeqNo;
		if ("D1".equals(scLineType)||"D2".equals(scLineType))
			return "94"+billItemMid+maxSeqNoStr;
		if ("V1".equals(scLineType)||"V2".equals(scLineType)||"V3".equals(scLineType))
			return "95"+billItemMid+maxSeqNoStr;
		if ("BS".equals(scLineType)||"CF".equals(scLineType)||"L1".equals(scLineType)||"L2".equals(scLineType))
			return "96"+billItemMid+maxSeqNoStr;
		if ("MS".equals(scLineType)||"RR".equals(scLineType)||"RA".equals(scLineType))
			return "97"+billItemMid+maxSeqNoStr;
		if ("AP".equals(scLineType)||"OA".equals(scLineType))
			return "98"+billItemMid+maxSeqNoStr;
		if ("C1".equals(scLineType)||"C2".equals(scLineType))
			return "99"+billItemMid+maxSeqNoStr;
		return null;
	}

	/*public static Integer generateSequenceNo(List<SCDetails> scDetails) {
		Integer maxSeqNo=0;
		
		for (SCDetails scDetail:scDetails){
			if (maxSeqNo<scDetail.getSequenceNo())
				maxSeqNo=scDetail.getSequenceNo();
		}
		maxSeqNo++;
		return maxSeqNo;
	}*/

	public static SCDetails createSCDetailByLineType(String scLineType) {
		if ("V1".equals(scLineType)||"V2".equals(scLineType)|| "V3".equals(scLineType)||
			"D1".equals(scLineType)||"D2".equals(scLineType)||
			"L1".equals(scLineType)||"L2".equals(scLineType)||
			"CF".equals(scLineType) )
			return new SCDetailsVO();
		if ("C1".equals(scLineType)||"C2".equals(scLineType))
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

	public static String validateAddendumApproval(SCPackage scPackage, List<SCDetails> scDetailsList){
		if ("1".equalsIgnoreCase(scPackage.getSubmittedAddendum().trim())){
			return "Cannot submit the Request. Addendum approval request submitted already.";
		}else {
			for(SCDetails scDetails:scDetailsList){
				if(scDetails instanceof SCDetailsBQ && BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus())){
					if(scDetails.getApproved()==null || "".equalsIgnoreCase(scDetails.getApproved().trim())){
						return null;
					}else if(SCDetails.APPROVED.equalsIgnoreCase(scDetails.getApproved().trim())){
						if(!scDetails.getQuantity().equals(scDetails.getToBeApprovedQuantity()) || !scDetails.getScRate().equals(scDetails.getToBeApprovedRate())){
							return null;
						}
					}
				}
			}
			return "No changes found in Subcontract";
		}
	}
	
//	/**
//	 * 
//	 * @author tikywong
//	 * modified on Dec 18, 2013 3:56:21 PM
//	 * 
//	 */
//	public static List<SCDetailProvisionHistory> generateProvisionHistory(List<SCDetails> scDetailList, Integer postYear, Integer postMonth){
//		List<SCDetailProvisionHistory> scDetailProvisionHistoryList = new ArrayList<SCDetailProvisionHistory>();
//		for (SCDetails scDetails:scDetailList){
//			//BQ, B1, V1, V2, V3, L1, L2, D1, D2, CF, OA
//			if (scDetails instanceof SCDetailsOA){
//				//Generate Provision History	
//				if (scDetails.getProvision()!=null && scDetails.getProvision()!=0)		
//					scDetailProvisionHistoryList.add(new SCDetailProvisionHistory(scDetails, postYear, postMonth));
//				//Update Posted Work Done Quantity = Cumulative Work Done Quantity for all SCDetail with Work Done
//				((SCDetailsOA)scDetails).setPostedWorkDoneQuantity(((SCDetailsOA)scDetails).getCumWorkDoneQuantity());
//			}
//		}
//		return scDetailProvisionHistoryList;
//	}
	
	public static final Boolean splitSCPackage(List<SCDetails> scDetailsList) throws Exception{
		for(SCDetails scDetails: scDetailsList){
			if("BQ".trim().equalsIgnoreCase(scDetails.getLineType().trim())){
				if(scDetails.getCumWorkDoneQuantity()>scDetails.getNewQuantity())
					throw new Exception("New Quantity has to be larger than current Work Done");
				else
					return true;
			}
		}
		return null;
	}
	public void toTerminate(List<SCDetails> scDetailsList){
		for(SCDetails scDetails: scDetailsList){
			scDetails.setNewQuantity(scDetails.getCumWorkDoneQuantity());
		}
	}
}
