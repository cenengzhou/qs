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
import org.springframework.stereotype.Service;

import com.gammon.pcms.helper.FreeMarkerHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.MainContractCertificateHBDao;
import com.gammon.qs.dao.MainContractCertificateWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

@Service
public class HTMLStringService implements Serializable{

	private static final long serialVersionUID = -6313629009064651927L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MasterListWSDao masterListDao;
	@Autowired
	private JobHBDao jobHBDao;
	@Autowired
	private SCPackageHBDao packageHBDao;
	@Autowired
	private SCPaymentCertHBDao paymentHBDao;
	@Autowired
	private	TenderAnalysisHBDao taHBDao;
	@Autowired
	private MainContractCertificateHBDao mainCertHBDao;
	@Autowired
	private BQResourceSummaryHBDao bqResourceSummaryHBDao;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private MainContractCertificateWSDao mainCertWSDao;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private SCDetailsHBDao scDetailsHBDao;
	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	
	public String makeHTMLStringForSCPaymentCert(String jobNumber, String subcontractNumber, String paymentNo, String htmlVersion){
		
		String strHTMLCodingContent = "";
		Job job = new Job();
		SCPackage scPackage = new SCPackage();
		Double clientCertAmount = new Double(0);
		PaymentCertViewWrapper paymentCertViewWrapper = new PaymentCertViewWrapper();
		List<SCPaymentCert> scPaymentCertList = null;
		

		String strPaymentDueDate = null;
		String strPaymentAsAtDate = null;
		
		Double ivCumAmt = new Double(0);
		
		int mainCertNumber = 0;
		int currentPaymentNo = 0;
		logger.info("Input parameter: jobNo["+jobNumber+"] - Package No["+subcontractNumber+"] - PaymentNo["+paymentNo+"]");
		try {
			job = jobHBDao.obtainJob(jobNumber);
			  
			if(paymentNo==null || "".equals(paymentNo.trim()) || paymentNo.trim().length()==0){// check the paymentNo
				logger.info("Payment number is null --> Max. Payment No. will be used.");
				scPaymentCertList = paymentHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, new Integer(subcontractNumber));

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
			scPackage = packageHBDao.obtainSCPackage(paymentCertViewWrapper.getJobNumber(), paymentCertViewWrapper.getSubContractNo().toString());
			if(mainCertNumber != 0)
				clientCertAmount = mainCertWSDao.obtainParentMainContractCertificate(jobNumber, mainCertNumber).getAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<BQResourceSummary> resourceList = new ArrayList<BQResourceSummary>();
			resourceList = bqResourceSummaryHBDao.getResourceSummariesSearch(job, subcontractNumber, "14*", null);
			
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
		
		String strHTMLCodingContent = "";
		List<TenderAnalysis> listTenderAnalysis = new ArrayList<TenderAnalysis>();
		logger.info("Tender Analysis List size: " + listTenderAnalysis.size());
			try {
				listTenderAnalysis = taHBDao.obtainTenderAnalysis(jobNumber, subcontractNumber);
			} catch (NumberFormatException e4) {
				e4.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			@SuppressWarnings("unused")
			List<SCDetails> scDetailsList = new ArrayList<SCDetails>();
			Job jobHeaderInfo = new Job();
			String vendorName = "";
			Double totalSubcontractSum = new Double(0);
			List<String> vendorNameList = null;
			List<Double> totalSubcontractSumList = null;
			List<Boolean> vendorNumZero = null;
			
			SCPackage scPackage = new SCPackage();
			
			try {
				jobHeaderInfo = jobHBDao.obtainJob(jobNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				scDetailsList = scDetailsHBDao.getSCDetails(packageHBDao.obtainSCPackage(jobNumber, subcontractNumber));
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
					TenderAnalysis curTenderAnalysis = listTenderAnalysis.get(i);
					if (curTenderAnalysis.getVendorNo().equals(0)){
						try {
							for(TenderAnalysisDetail curTenderAnalysisDetail: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis)){
								comparableBudgetAmount += curTenderAnalysisDetail.getFeedbackRateDomestic()*curTenderAnalysisDetail.getQuantity();
							}
						} catch (DatabaseOperationException e) {
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
							
						List<TenderAnalysisDetail> listTenderAnalysisDetails = new ArrayList<TenderAnalysisDetail>();
						try {
							listTenderAnalysisDetails = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);//taHBDao.getTenderAnalysisDetail(jobNumber, new Integer(curTenderAnalysis.getVendorNo().toString()), new Integer(subcontractNumber));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (int j=0; j<listTenderAnalysisDetails.size(); j++) {
							TenderAnalysisDetail curTenderAnalysisDetail = listTenderAnalysisDetails.get(j);
							if (curTenderAnalysisDetail.getFeedbackRateDomestic()!=null && curTenderAnalysisDetail.getQuantity()!=null)
								totalSubcontractSum += curTenderAnalysisDetail.getQuantity() * curTenderAnalysisDetail.getFeedbackRateDomestic();
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
				strHTMLCodingContent = FreeMarkerHelper.returnHtmlString("TenderAnalysis_W.ftl", data);
			}
			
			if (htmlVersion.equals("B")) {
				comparableBudgetAmount = new Double(0);
				recommendVendor = "";
				vendorNameList = new ArrayList<String>();
				vendorNumZero = new ArrayList<Boolean>();
				totalSubcontractSumList = new ArrayList<Double>();
				
				for(int i=0; i < listTenderAnalysis.size();  i++ )
				{
					TenderAnalysis curTenderAnalysis = listTenderAnalysis.get(i);
					if (!curTenderAnalysis.getVendorNo().equals(0)){
						try {
							vendorName = receiveVendorName(curTenderAnalysis.getVendorNo().toString());
							vendorNameList.add(i, vendorName);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if ("RCM".equals(curTenderAnalysis.getStatus()) || "AWD".equals(curTenderAnalysis.getStatus()))
							recommendVendor=vendorName;

						List<TenderAnalysisDetail> listTenderAnalysisDetails = new ArrayList<TenderAnalysisDetail>();

						try {
							listTenderAnalysisDetails = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);				} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}

							for (int j=0; j<listTenderAnalysisDetails.size(); j++) {
								TenderAnalysisDetail curTenderAnalysisDetail = listTenderAnalysisDetails.get(j);

								if (curTenderAnalysisDetail.getFeedbackRateDomestic()!=null)
									logger.info("curTenderAnalysisDetails.getFeedbackRate() curTenderAnalysisDetails.getFeedbackRate() curTenderAnalysisDetails.getFeedbackRate()" + curTenderAnalysisDetail.getFeedbackRateDomestic());
								if (curTenderAnalysisDetail.getQuantity()!=null)
									logger.info("curTenderAnalysisDetails.getBqQty() curTenderAnalysisDetails.getBqQty() curTenderAnalysisDetails.getBqQty()" + curTenderAnalysisDetail.getQuantity());

								if (curTenderAnalysisDetail.getFeedbackRateDomestic()!=new Double(0))
									totalSubcontractSum +=((curTenderAnalysisDetail.getQuantity()!=null?curTenderAnalysisDetail.getQuantity():new Double(0))
											*(curTenderAnalysisDetail.getFeedbackRateDomestic()!=null?curTenderAnalysisDetail.getFeedbackRateDomestic():new Double(0)));

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
								for (TenderAnalysisDetail curTenderAnalysisDetail : tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis)){
									comparableBudgetAmount += curTenderAnalysisDetail.getFeedbackRateDomestic() * curTenderAnalysisDetail.getQuantity();
								}
							} catch (DatabaseOperationException e) {
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

		String strHTMLCodingContent = "";
		List<SCDetails> scDetailsList = new ArrayList<SCDetails>();
		boolean boolChangedLine = false;
		boolean highlight = true;
		Double totalToBeApprovedValue = new Double(0.0);
		Double totalThisAddendumValue = new Double(0.0);
		Double totalToBeApprovedVOValue = new Double(0.0);
		Double totalToBeApprovedRemeasuredSCSumValue = new Double(0.0);
		Job jobHeaderInfo = new Job();
		String vendorName = "";
		SCPackage scPackage = new SCPackage();
		
		try {
			jobHeaderInfo = jobHBDao.obtainJob(jobNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			scDetailsList = scDetailsHBDao.getSCDetails(scPackage);
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
			SCDetails curaddendumEnquiryList = scDetailsList.get(i);
			//Equal to Yellow of AddendumEnquiryYellowAquaQuantityRenderer
			if ("A".equals(curaddendumEnquiryList.getSourceType()) && !"OA".equals(curaddendumEnquiryList.getLineType())) {
				if (SCDetails.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()) && !SCDetails.SUSPEND.equals(curaddendumEnquiryList.getApproved())) {	
					
					totalToBeApprovedVOValue += curaddendumEnquiryList.getToBeApprovedAmount();
					if (Math.abs(curaddendumEnquiryList.getTotalAmount()-curaddendumEnquiryList.getToBeApprovedAmount())>0 || !SCDetails.APPROVED.equals(curaddendumEnquiryList.getApproved())) {
							boolChangedLine = true;  
					} else {
						boolChangedLine = false;
					}
				} else {
					boolChangedLine = false;
				}
			//Equal to Aqua of AddendumEnquiryYellowAquaQuantityRenderer
			} else if ("B1".equals(curaddendumEnquiryList.getLineType()) || "BQ".equals(curaddendumEnquiryList.getLineType())) {
				if(SCDetails.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()))
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
				totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount()-(SCDetails.APPROVED.equals(curaddendumEnquiryList.getApproved())?curaddendumEnquiryList.getTotalAmount():0.0));
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
			SCDetails curaddendumEnquiryList = scDetailsList.get(i);		
			//Equal to Yellow of AddendumEnquiryYellowAquaQuantityRenderer
			if (SCDetails.APPROVED.equals(curaddendumEnquiryList.getSourceType()) && !"OA".equals(curaddendumEnquiryList.getLineType())) {
				if (SCDetails.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()) && !SCDetails.SUSPEND.equals(curaddendumEnquiryList.getApproved())) {
					totalToBeApprovedVOValue += curaddendumEnquiryList.getToBeApprovedAmount();
					if (Math.abs(curaddendumEnquiryList.getTotalAmount() - curaddendumEnquiryList.getToBeApprovedAmount()) > 0 || !SCDetails.APPROVED.equals(curaddendumEnquiryList.getApproved()))
						boolChangedLine = true;
					else
						boolChangedLine = false;
				} else
					boolChangedLine = false;
			//Equal to Aqua of AddendumEnquiryYellowAquaQuantityRenderer
			} else if ("B1".equals(curaddendumEnquiryList.getLineType()) || "BQ".equals(curaddendumEnquiryList.getLineType())) {
				if(SCDetails.ACTIVE.equals(curaddendumEnquiryList.getSystemStatus()))
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
				totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount() - (SCDetails.APPROVED.equals(curaddendumEnquiryList.getApproved()) ? curaddendumEnquiryList.getTotalAmount() : 0.0));
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
		
		Job jobHeaderInfo = new Job();
		double newSCSum = 0.00;
		String strHTMLCodingContent = "";
		try {
			SCPackage scPackage;
			scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber);
			for (SCDetails scDetail: scDetailsHBDao.getSCDetails(scPackage)){
//				if (scDetail instanceof SCDetailsBQ && !(scDetail instanceof SCDetailsVO)){
				if (scDetail instanceof SCDetailsBQ && SCDetails.APPROVED.equals(scDetail.getApproved()) && scDetail.getSystemStatus().equals(SCDetails.ACTIVE)){
					if ((!(scDetail instanceof SCDetailsVO))||Math.abs(((SCDetailsBQ)scDetail).getCostRate()==null?0:((SCDetailsBQ)scDetail).getCostRate())>0)
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
		
		SCPackage scPackage = new SCPackage();
		
		try {
			jobHeaderInfo = jobHBDao.obtainJob(jobNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber);
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

		String strHTMLCodingContent = "";
		logger.info("makeHTMLStringForSCMainCert --> Input parameter: jobNo["+jobNumber+"] - Main Cert No["+mainCertNo+"]");
		if(!GenericValidator.isBlankOrNull(jobNumber) && !GenericValidator.isBlankOrNull(mainCertNo)){
			try {
				Job job = jobHBDao.obtainJob(jobNumber);
				
				List<MasterListVendor> clientList = null;
				try {
					clientList = masterListService.obtainAllClientList(job.getEmployer());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				MainContractCertificate mainCert = mainCertHBDao.obtainMainContractCert(jobNumber, Integer.valueOf(mainCertNo));
				MainContractCertificate previousCert = mainCertHBDao.obtainMainContractCert(jobNumber, Integer.valueOf(mainCertNo)-1);
				if(previousCert==null)
					previousCert = new MainContractCertificate();
				
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
	
	public HTMLStringService() {
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
