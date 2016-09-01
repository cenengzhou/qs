package com.gammon.pcms.service;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.helper.FreeMarkerHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class HTMLService implements Serializable{

	private static final long serialVersionUID = -6313629009064651927L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MasterListWSDao masterListDao;
	@Autowired
	private JobInfoHBDao jobInfoHBDao;
	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private PaymentCertHBDao paymentCertHBDao;
	@Autowired
	private	TenderHBDao tenderHBDao;
	@Autowired
	private MainCertHBDao mainCertHBDao;
	@Autowired
	private ResourceSummaryHBDao resourceSummaryHBDao;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private MainCertWSDao mainCertWSDao;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private SubcontractDetailHBDao subcontractDetailHBDao;
	@Autowired
	private TenderDetailHBDao tenderDetailHBDao;
	@Autowired
	private AdminService adminService;
	
	public String makeHTMLStringForSCPaymentCert(String jobNumber, String subcontractNumber, String paymentNo, String htmlVersion){
//		adminService.canAccessJob(jobNumber);
		String strHTMLCodingContent = "";
		JobInfo job = new JobInfo();
		Subcontract scPackage = new Subcontract();
		Double clientCertAmount = new Double(0);
		PaymentCertViewWrapper paymentCertViewWrapper = new PaymentCertViewWrapper();
		List<PaymentCert> scPaymentCertList = null;
		

		String strPaymentDueDate = null;
		String strPaymentAsAtDate = null;
		
		Double ivCumAmt = new Double(0);
		
		int mainCertNumber = 0;
		int currentPaymentNo = 0;
		logger.info("Input parameter: jobNo["+jobNumber+"] - Package No["+subcontractNumber+"] - PaymentNo["+paymentNo+"]");
		try {
			job = jobInfoHBDao.obtainJobInfo(jobNumber);
			  
			if(paymentNo==null || "".equals(paymentNo.trim()) || paymentNo.trim().length()==0){// check the paymentNo
				logger.info("Payment number is null --> Max. Payment No. will be used.");
				scPaymentCertList = paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, subcontractNumber);

				int maxPaymentCertNumber = 0;
				if (scPaymentCertList.size() > 0) {
					maxPaymentCertNumber = scPaymentCertList.get(0).getPaymentCertNo();
					for (int i=0; i<scPaymentCertList.size(); i++) {
						if (scPaymentCertList.get(i).getPaymentCertNo() >= maxPaymentCertNumber) 
							maxPaymentCertNumber = scPaymentCertList.get(i).getPaymentCertNo();   // new maximum payment cert. number
					}
				}
				currentPaymentNo = maxPaymentCertNumber;
			}else{
				currentPaymentNo = Integer.valueOf(paymentNo);
				logger.info("PaymentNo: "+paymentNo);
			}
			
			logger.info("Job No.: "+jobNumber+"- Package No.: "+subcontractNumber+"- Payment No.: "+currentPaymentNo);
			paymentCertViewWrapper = paymentService.calculatePaymentCertificateSummary(jobNumber, subcontractNumber, currentPaymentNo);
			
			mainCertNumber 		= paymentCertViewWrapper.getMainCertNo();
			strPaymentDueDate 	= paymentCertViewWrapper.getDueDate();
			strPaymentAsAtDate	= paymentCertViewWrapper.getAsAtDate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = subcontractHBDao.obtainSCPackage(paymentCertViewWrapper.getJobNumber(), paymentCertViewWrapper.getSubContractNo().toString());
			if(mainCertNumber != 0)
				clientCertAmount = mainCertWSDao.obtainParentMainContractCertificate(jobNumber, mainCertNumber).getAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<ResourceSummary> resourceList = new ArrayList<ResourceSummary>();
			resourceList = resourceSummaryHBDao.getResourceSummariesSearch(job, subcontractNumber, "14*", null);
			
			for (int i=0; i<resourceList.size(); i++) {
					ivCumAmt += resourceList.get(i).getCurrIVAmount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("scPackage", scPackage);
		data.put("paymentCertViewWrapper", paymentCertViewWrapper);
		data.put("job", job);
		data.put("clientCertAmount", clientCertAmount);
		data.put("scPaymentCertList", scPaymentCertList);
		data.put("ivCumAmt", ivCumAmt);
		data.put("mainCertNumber", mainCertNumber);
		data.put("strPaymentDueDate", strPaymentDueDate);
		data.put("strPaymentAsAtDate", strPaymentAsAtDate);
		data.put("currentPaymentNo", currentPaymentNo);
		
		if (htmlVersion.equals("W"))
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("SCPaymentCert_W.ftl", data);

		if (htmlVersion.equals("B"))
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("SCPaymentCert_B.ftl", data);
			
		return strHTMLCodingContent;
	}
	
	public String makeHTMLStringForTenderAnalysis(String jobNumber, String subcontractNumber, String htmlVersion){
//		adminService.canAccessJob(jobNumber);
		String strHTMLCodingContent = "";
		List<Tender> listTenderAnalysis = new ArrayList<Tender>();
		logger.info("Tender Analysis List size: " + listTenderAnalysis.size());
			try {
				listTenderAnalysis = tenderHBDao.obtainTenderAnalysis(jobNumber, subcontractNumber);
			} catch (NumberFormatException e4) {
				e4.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			@SuppressWarnings("unused")
			List<SubcontractDetail> scDetailsList = new ArrayList<SubcontractDetail>();
			JobInfo jobHeaderInfo = new JobInfo();
			String vendorName = "";
			Double totalSubcontractSum = new Double(0);
			List<String> vendorNameList = null;
			List<Double> totalSubcontractSumList = null;
			List<Boolean> vendorNumZero = null;
			
			Subcontract scPackage = new Subcontract();
			
			try {
				jobHeaderInfo = jobInfoHBDao.obtainJobInfo(jobNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				scPackage = subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				scDetailsList = subcontractDetailHBDao.getSCDetails(subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			//Set it to be the budget amount of TA
			Double comparableBudgetAmount = new Double(0);
			String recommendVendor = "";

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("jobNumber", jobNumber);
			data.put("jobHeaderInfo", jobHeaderInfo);
			data.put("subcontractNumber", subcontractNumber);
			data.put("scPackage", scPackage);
			data.put("listTenderAnalysis", listTenderAnalysis);
			
			if (htmlVersion.equals("W")) {
				vendorNameList = new ArrayList<String>();
				totalSubcontractSumList = new ArrayList<Double>();
				vendorNumZero = new ArrayList<Boolean>();
				
				for(int i=0; i < listTenderAnalysis.size();  i++ )
				{
					Tender curTenderAnalysis = listTenderAnalysis.get(i);
					if (curTenderAnalysis.getVendorNo().equals(0)){
						try {
							for(TenderDetail curTenderAnalysisDetail: tenderDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis)){
								comparableBudgetAmount += curTenderAnalysisDetail.getRateBudget()*curTenderAnalysisDetail.getQuantity();
							}
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
						vendorNameList.add(i, "");
						totalSubcontractSumList.add(i,0.0);
						vendorNumZero.add(i,true);
					}else{
					    logger.info("Vendor Number: "+curTenderAnalysis.getVendorNo());
						try {
							vendorName = receiveVendorName(curTenderAnalysis.getVendorNo().toString());
							vendorNameList.add(i, vendorName);
							logger.info("Vendor Name: " + vendorName);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if ("RCM".equals(curTenderAnalysis.getStatus())){
							recommendVendor=vendorName;
						}
							logger.info("RecommendVendor: " + recommendVendor);
							
						List<TenderDetail> listTenderAnalysisDetails = new ArrayList<TenderDetail>();
						try {
							listTenderAnalysisDetails = tenderDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);//tenderHBDao.getTenderAnalysisDetail(jobNumber, new Integer(curTenderAnalysis.getVendorNo().toString()), new Integer(subcontractNumber));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (int j=0; j<listTenderAnalysisDetails.size(); j++) {
							TenderDetail curTenderAnalysisDetail = listTenderAnalysisDetails.get(j);
							if (curTenderAnalysisDetail.getRateBudget()!=null && curTenderAnalysisDetail.getQuantity()!=null)
								totalSubcontractSum += curTenderAnalysisDetail.getQuantity() * curTenderAnalysisDetail.getRateBudget();
						}
						totalSubcontractSumList.add(i,totalSubcontractSum);
						totalSubcontractSum = new Double(0);
						vendorNumZero.add(i,false);
					}
				}
				data.put("totalSubcontractSumList", totalSubcontractSumList);
				data.put("vendorNameList", vendorNameList);
				data.put("comparableBudgetAmount",comparableBudgetAmount);
				data.put("recommendVendor", recommendVendor);
				data.put("vendorNoZero", vendorNumZero);
//				strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("TenderAnalysis_W.ftl", data);
				strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("TenderAnalysis_W_Test.html", data);
			}
			
			if (htmlVersion.equals("B")) {
				comparableBudgetAmount = new Double(0);
				recommendVendor = "";
				vendorNameList = new ArrayList<String>();
				vendorNumZero = new ArrayList<Boolean>();
				totalSubcontractSumList = new ArrayList<Double>();
				
				for(int i=0; i < listTenderAnalysis.size();  i++ )
				{
					Tender curTenderAnalysis = listTenderAnalysis.get(i);
					if (!curTenderAnalysis.getVendorNo().equals(0)){
						try {
							vendorName = receiveVendorName(curTenderAnalysis.getVendorNo().toString());
							vendorNameList.add(i, vendorName);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if ("RCM".equals(curTenderAnalysis.getStatus()) || "AWD".equals(curTenderAnalysis.getStatus()))
							recommendVendor=vendorName;

						List<TenderDetail> listTenderAnalysisDetails = new ArrayList<TenderDetail>();

						try {
							listTenderAnalysisDetails = tenderDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);				} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}

							for (int j=0; j<listTenderAnalysisDetails.size(); j++) {
								TenderDetail curTenderAnalysisDetail = listTenderAnalysisDetails.get(j);

								if (curTenderAnalysisDetail.getRateBudget()!=null)
									logger.info("curTenderAnalysisDetails.getFeedbackRate() curTenderAnalysisDetails.getFeedbackRate() curTenderAnalysisDetails.getFeedbackRate()" + curTenderAnalysisDetail.getRateBudget());
								if (curTenderAnalysisDetail.getQuantity()!=null)
									logger.info("curTenderAnalysisDetails.getBqQty() curTenderAnalysisDetails.getBqQty() curTenderAnalysisDetails.getBqQty()" + curTenderAnalysisDetail.getQuantity());

								if (curTenderAnalysisDetail.getRateBudget()!=new Double(0))
									totalSubcontractSum +=((curTenderAnalysisDetail.getQuantity()!=null?curTenderAnalysisDetail.getQuantity():new Double(0))
											*(curTenderAnalysisDetail.getRateBudget()!=null?curTenderAnalysisDetail.getRateBudget():new Double(0)));

							}
							totalSubcontractSumList.add(i,totalSubcontractSum);
							totalSubcontractSum = new Double(0);
							vendorNumZero.add(i,false);
						}
						/**
						 * modified by Tiky Wong on 22/05/2012
						 * 
						 * Fixing: Blackberry version's Tender Budget cannot be shown correctly which always shows 0
						 */
						else {
							try {
								for (TenderDetail curTenderAnalysisDetail : tenderDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis)){
									comparableBudgetAmount += curTenderAnalysisDetail.getRateBudget() * curTenderAnalysisDetail.getQuantity();
								}
							} catch (DataAccessException e) {
								e.printStackTrace();
							}
							vendorNameList.add(i, "");
							totalSubcontractSumList.add(i, 0.0);
							vendorNumZero.add(i, true);
						}
					}
					
					data.put("totalSubcontractSumList", totalSubcontractSumList);
					data.put("vendorNameList", vendorNameList);
					data.put("comparableBudgetAmount",comparableBudgetAmount);
					data.put("recommendVendor", recommendVendor);
					data.put("vendorNoZero", vendorNumZero);
					strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("TenderAnalysis_B.ftl", data);			
		}

			return strHTMLCodingContent;
	}
	
	public String makeHTMLStringForAddendumApproval(String jobNumber, String subcontractNumber, String htmlVersion){
//		adminService.canAccessJob(jobNumber);
		String strHTMLCodingContent = "";
		List<SubcontractDetail> scDetailsList = new ArrayList<SubcontractDetail>();
		boolean boolChangedLine = false;
		boolean highlight = true;
		Double totalToBeApprovedValue = new Double(0.0);
		Double totalThisAddendumValue = new Double(0.0);
		Double totalToBeApprovedVOValue = new Double(0.0);
		Double totalToBeApprovedRemeasuredSCSumValue = new Double(0.0);
		JobInfo jobHeaderInfo = new JobInfo();
		String vendorName = "";
		Subcontract scPackage = new Subcontract();
		
		try {
			jobHeaderInfo = jobInfoHBDao.obtainJobInfo(jobNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			scDetailsList = subcontractDetailHBDao.getSCDetails(scPackage);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		try {
			vendorName = receiveVendorName(scPackage.getVendorNo());
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("subcontractNumber", subcontractNumber);
		data.put("jobNumber", jobNumber);
		data.put("scPackage", scPackage);
		data.put("vendorName", vendorName);
		data.put("scDetailsList", scDetailsList);
		data.put("highlight", highlight);
		data.put("jobHeaderInfo", jobHeaderInfo);
		
		if (htmlVersion.equals("W")) {
			List<Boolean> boolChangedLineList = new ArrayList<Boolean>();
			for(int i=0; i < scDetailsList.size();  i++ ){
			SubcontractDetail curaddendumEnquiryList = scDetailsList.get(i);
			//Equal to Yellow of AddendumEnquiryYellowAquaQuantityRenderer
			if ("A".equals(curaddendumEnquiryList.getSourceType()) && !"OA".equals(curaddendumEnquiryList.getLineType())) {
				if (SubcontractDetail.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()) && !SubcontractDetail.SUSPEND.equals(curaddendumEnquiryList.getApproved())) {	
					
					totalToBeApprovedVOValue += curaddendumEnquiryList.getToBeApprovedAmount();
					if (Math.abs(curaddendumEnquiryList.getTotalAmount()-curaddendumEnquiryList.getToBeApprovedAmount())>0 || !SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved())) {
							boolChangedLine = true;  
					} else {
						boolChangedLine = false;
					}
				} else {
					boolChangedLine = false;
				}
			//Equal to Aqua of AddendumEnquiryYellowAquaQuantityRenderer
			} else if ("B1".equals(curaddendumEnquiryList.getLineType()) || "BQ".equals(curaddendumEnquiryList.getLineType())) {
				if(SubcontractDetail.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()))
					totalToBeApprovedRemeasuredSCSumValue += curaddendumEnquiryList.getToBeApprovedAmount();
				//Bug fix - Approval Content - Addendum Approval Request
				//By Peter Chan 2009-12-18
					if (Math.abs(curaddendumEnquiryList.getQuantity()-curaddendumEnquiryList.getToBeApprovedQuantity())>0)
						boolChangedLine = true; 
					else 
						boolChangedLine = false;
			} else {
				boolChangedLine = false;
			}
			if (boolChangedLine==true) {	
				totalToBeApprovedValue += curaddendumEnquiryList.getToBeApprovedAmount();
				totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount()-(SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved())?curaddendumEnquiryList.getTotalAmount():0.0));
			}
			boolChangedLineList.add(boolChangedLine);
		}
			
		data.put("totalToBeApprovedRemeasuredSCSumValue", totalToBeApprovedRemeasuredSCSumValue);
		data.put("totalToBeApprovedVOValue", totalToBeApprovedVOValue);
		data.put("totalToBeApprovedValue", totalToBeApprovedValue);
		data.put("totalThisAddendumValue", totalThisAddendumValue);
		data.put("boolChangedLineList", boolChangedLineList);
		
		strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("AddendumApproval_W.ftl", data);
	}
	if (htmlVersion.equals("B")) {
		List<Boolean> boolChangedLineList = new ArrayList<Boolean>();
		totalToBeApprovedRemeasuredSCSumValue = 0.0;
		totalToBeApprovedVOValue = 0.0;
		for(int i=0; i < scDetailsList.size();  i++ ){
			SubcontractDetail curaddendumEnquiryList = scDetailsList.get(i);		
			//Equal to Yellow of AddendumEnquiryYellowAquaQuantityRenderer
			if (SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getSourceType()) && !"OA".equals(curaddendumEnquiryList.getLineType())) {
				if (SubcontractDetail.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()) && !SubcontractDetail.SUSPEND.equals(curaddendumEnquiryList.getApproved())) {
					totalToBeApprovedVOValue += curaddendumEnquiryList.getToBeApprovedAmount();
					if (Math.abs(curaddendumEnquiryList.getTotalAmount() - curaddendumEnquiryList.getToBeApprovedAmount()) > 0 || !SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved()))
						boolChangedLine = true;
					else
						boolChangedLine = false;
				} else
					boolChangedLine = false;
			//Equal to Aqua of AddendumEnquiryYellowAquaQuantityRenderer
			} else if ("B1".equals(curaddendumEnquiryList.getLineType()) || "BQ".equals(curaddendumEnquiryList.getLineType())) {
				if(SubcontractDetail.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()))
					totalToBeApprovedRemeasuredSCSumValue += curaddendumEnquiryList.getToBeApprovedAmount();
				//Bug fix - Approval Content - Addendum Approval Request
				//By Peter Chan 2009-12-18
				if (Math.abs(curaddendumEnquiryList.getQuantity()-curaddendumEnquiryList.getToBeApprovedQuantity())>0)
					boolChangedLine = true; 
				else 
					boolChangedLine = false;
			} else {
				boolChangedLine = false;
			}
			if (boolChangedLine == true) {
				totalToBeApprovedValue += curaddendumEnquiryList.getToBeApprovedAmount();
				totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount() - (SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved()) ? curaddendumEnquiryList.getTotalAmount() : 0.0));
			}
			boolChangedLineList.add(boolChangedLine);
		}
		
		data.put("totalToBeApprovedRemeasuredSCSumValue", totalToBeApprovedRemeasuredSCSumValue);
		data.put("totalToBeApprovedVOValue", totalToBeApprovedVOValue);
		data.put("totalToBeApprovedValue", totalToBeApprovedValue);
		data.put("totalThisAddendumValue", totalThisAddendumValue);
		data.put("boolChangedLineList", boolChangedLineList);
		
		strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("AddendumApproval_B.ftl", data);
	}
	
	return strHTMLCodingContent;
}

	public String makeHTMLStringForSplitTermSC(String jobNumber, String subcontractNumber, String htmlVersion){
//		adminService.canAccessJob(jobNumber);
		JobInfo jobHeaderInfo = new JobInfo();
		double newSCSum = 0.00;
		String strHTMLCodingContent = "";
		try {
			Subcontract scPackage;
			scPackage = subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber);
			for (SubcontractDetail scDetail: subcontractDetailHBDao.getSCDetails(scPackage)){
//				if (scDetail instanceof SCDetailsBQ && !(scDetail instanceof SCDetailsVO)){
				if (scDetail instanceof SubcontractDetailBQ && SubcontractDetail.APPROVED.equals(scDetail.getApproved()) && scDetail.getSystemStatus().equals(SubcontractDetail.ACTIVE)){
					if ((!(scDetail instanceof SubcontractDetailVO))||Math.abs(((SubcontractDetailBQ)scDetail).getCostRate()==null?0:((SubcontractDetailBQ)scDetail).getCostRate())>0)
						newSCSum = newSCSum + ((scDetail.getNewQuantity()!=null?scDetail.getNewQuantity():0)*scDetail.getScRate());
					else
						newSCSum = newSCSum + ((scDetail.getQuantity()!=null?scDetail.getQuantity():0)*scDetail.getScRate());
				}
			}
			//newSCSum = packageDao.getSCSumOfSplitSC(jobNumber, Integer.parseInt(subcontractNumber));
		} catch (NumberFormatException e4) {
			System.err.println(e4);
			newSCSum = 0;
		} catch (Exception e4) {
			System.err.println(e4);
			newSCSum = 0;
		}
		String vendorName = "";
		
		Subcontract scPackage = new Subcontract();
		
		try {
			jobHeaderInfo = jobInfoHBDao.obtainJobInfo(jobNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			vendorName = receiveVendorName(scPackage.getVendorNo());
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("jobNumber", jobNumber);
		data.put("jobHeaderInfo", jobHeaderInfo);
		data.put("subcontractNumber", subcontractNumber);
		data.put("scPackage", scPackage);
		data.put("newSCSum", newSCSum);
		data.put("vendorName", vendorName);

		if (htmlVersion.equals("W")) {
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("SplitTermSC_W.ftl", data);
		}
	
		if (htmlVersion.equals("B")) {
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("SplitTermSC_B.ftl", data);
		}
	return strHTMLCodingContent;
	}
	
	/**
	 * @author koeyyeung
	 * created on 25 Mar, 2015
	 * HTML String For Main Cert Information being called by Web Service
	 * **/
	public String makeHTMLStringForMainCert(String jobNumber, String mainCertNo, String htmlVersion) {
//		adminService.canAccessJob(jobNumber);
		String strHTMLCodingContent = "";
		logger.info("makeHTMLStringForSCMainCert --> Input parameter: jobNo["+jobNumber+"] - Main Cert No["+mainCertNo+"]");
		if(!GenericValidator.isBlankOrNull(jobNumber) && !GenericValidator.isBlankOrNull(mainCertNo)){
			try {
				JobInfo job = jobInfoHBDao.obtainJobInfo(jobNumber);
				
				List<MasterListVendor> clientList = null;
				try {
					clientList = masterListService.obtainAllClientList(job.getEmployer());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				MainCert mainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo));
				MainCert previousCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo)-1);
				if(previousCert==null)
					previousCert = new MainCert();
				
				if(mainCert!=null){
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("job", job);
					data.put("clientList", clientList);
					data.put("mainCert", mainCert);
					data.put("previousCert", previousCert);
					
					if (htmlVersion.equals("W")){						
						strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("MainCert_W.ftl", data);
					}
					else if (htmlVersion.equals("B")){
						strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("MainCert_B.ftl", data);
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
		}
		return strHTMLCodingContent;
	}	
	
	public HTMLService() {
	}
	
	public String amountRender(Double doubleValue){

		NumberFormat formatter = new DecimalFormat("#,##0.00");
		String refinedNumber = formatter.format(doubleValue);

		return refinedNumber;
	}
	
	public String blackberryAmountRender(Double doubleValue){

		NumberFormat formatter = new DecimalFormat("#,##0");
		String refinedNumber = formatter.format(doubleValue);

		return refinedNumber;
	}

	public String receiveVendorName(String addressNumber) throws Exception {
		return masterListDao.getVendorNameList(addressNumber).get(0).getVendorName();
	}
	
}
