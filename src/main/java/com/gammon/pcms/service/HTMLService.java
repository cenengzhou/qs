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
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.FreemarkerConfig;
import com.gammon.pcms.config.ServletConfig;
import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.helper.FreeMarkerHelper;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AddendumDetailHBDao;
import com.gammon.qs.dao.AddendumHBDao;
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
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class HTMLService implements Serializable{

	private static final long serialVersionUID = -6313629009064651927L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MasterListWSDao masterListDao;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private JobInfoHBDao jobInfoHBDao;
	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private SubcontractDetailHBDao subcontractDetailHBDao;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentCertHBDao paymentCertHBDao;
	@Autowired
	private	TenderHBDao tenderHBDao;
	@Autowired
	private TenderDetailHBDao tenderDetailHBDao;
	@Autowired
	private TenderVarianceHBDao tenderVarianceHBDao;
	@Autowired
	private MainCertHBDao mainCertHBDao;
	@Autowired
	private MainCertWSDao mainCertWSDao;
	@Autowired
	private ResourceSummaryHBDao resourceSummaryHBDao;
	@Autowired
	private AddendumHBDao addendumHBDao;
	@Autowired
	private AddendumDetailHBDao addendumDetailHBDao;
	@Autowired
	private FreemarkerConfig freemarkerConfig;
	@Autowired
	private ServletConfig servletConfig;
	
	public String makeHTMLStringForSCPaymentCert(String jobNumber, String subcontractNumber, String paymentNo, String htmlVersion){
		String strHTMLCodingContent = "";
		JobInfo job = new JobInfo();
		Subcontract scPackage = new Subcontract();
		Double clientCertAmount = new Double(0);
		PaymentCertViewWrapper paymentCertViewWrapper = new PaymentCertViewWrapper();
		List<PaymentCert> scPaymentCertList = null;
		MasterListVendor masterList = new MasterListVendor();

		String strPaymentDueDate = null;
		String strPaymentAsAtDate = null;
		
		Double ivCumAmt = new Double(0);
		
		int mainCertNumber = 0;
		int currentPaymentNo = 0;
		logger.info("Input parameter: jobNo["+jobNumber+"] - Package No["+subcontractNumber+"] - PaymentNo["+paymentNo+"]");
		try {
			job = jobInfoHBDao.obtainJobInfo(jobNumber);

			if(job != null)
				masterList = masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()) == null ? new MasterListVendor() : masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()).get(0);
			
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
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("scPackage", scPackage != null ? scPackage : new Subcontract());
		data.put("paymentCertViewWrapper", paymentCertViewWrapper != null ? paymentCertViewWrapper : new PaymentCertViewWrapper());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("clientCertAmount", clientCertAmount != null ? clientCertAmount : new Double(0));
		data.put("scPaymentCertList", scPaymentCertList != null ? scPaymentCertList : new ArrayList<>());
		data.put("ivCumAmt", ivCumAmt != null ? ivCumAmt : new Double(0));
		data.put("mainCertNumber", mainCertNumber);
		data.put("strPaymentDueDate", strPaymentDueDate != null ? strPaymentDueDate : "");
		data.put("strPaymentAsAtDate", strPaymentAsAtDate != null ? strPaymentAsAtDate : "");
		data.put("currentPaymentNo", currentPaymentNo);
		
		strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates().get("payment"), data);
			
		return strHTMLCodingContent;
	}
	
	public String makeHTMLStringForTenderAnalysis(String noJob, String noSubcontract, String htmlVersion) throws Exception{
		JobInfo job = jobInfoHBDao.obtainJobInfo(noJob);
		MasterListVendor masterList = new MasterListVendor();
		if(job != null)
			masterList = masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()) == null ? new MasterListVendor() : masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()).get(0);
		Subcontract subcontract = subcontractHBDao.obtainSubcontract(noJob, noSubcontract);
		Tender budgetTender = tenderHBDao.obtainTender(noJob, noSubcontract, 0);
		List<Tender> tenderList = tenderHBDao.obtainTenderList(noJob, noSubcontract);
		Tender rcmTenderer = tenderHBDao.obtainRecommendedTender(noJob, noSubcontract);
		List<TenderVariance> tenderVarianceList = null;
		if(rcmTenderer != null){
			tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(noJob, noSubcontract, String.valueOf(rcmTenderer.getVendorNo()));
		}
		
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("subcontract", subcontract != null ? subcontract : new Subcontract());
		data.put("budgetTender", budgetTender != null ? budgetTender : new Tender());
		data.put("tenderList", tenderList != null ? tenderList : new ArrayList<>());
		data.put("rcmTenderer", rcmTenderer != null ? rcmTenderer : new Tender());
		data.put("tenderVarianceList", tenderVarianceList != null ? tenderVarianceList : new ArrayList<>());
		return FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates().get("award"), data);
	}

	public String makeHTMLStringForAddendumApproval(String noJob, String noSubcontract, Long noAddendum, String htmlVersion) throws Exception{
		JobInfo job = jobInfoHBDao.obtainJobInfo(noJob);
		Subcontract subcontract = subcontractHBDao.obtainSCPackage(noJob, noSubcontract);
		
		MasterListVendor masterList = new MasterListVendor();
		if(job != null)
			masterList = masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()) == null ? new MasterListVendor() : masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()).get(0);

		Addendum addendum = new Addendum();
		List<AddendumDetail> addendumDetailList = new ArrayList<AddendumDetail>(); 
		if(noAddendum !=null){
			addendum = addendumHBDao.getAddendum(noJob, noSubcontract, noAddendum);
			addendumDetailList = addendumDetailHBDao.getAllAddendumDetails(noJob, noSubcontract, noAddendum);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("addendum", addendum != null ? addendum : new Addendum());
		data.put("subcontract", subcontract != null ? subcontract : new Subcontract());
		data.put("addendumDetailList", addendumDetailList != null ? addendumDetailList : new ArrayList<>());
		return FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates().get("addendum"), data);
	}

	public String makeHTMLStringForSplitTermSC(String jobNumber, String subcontractNumber, String htmlVersion){
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
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates("splitTermW"), data);
		}
	
		if (htmlVersion.equals("B")) {
			strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates("splitTermB"), data);
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
						strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates("mainCertW"), data);
					}
					else if (htmlVersion.equals("B")){
						strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(freemarkerConfig.getTemplates("mainCertB"), data);
					}
				} else {
					strHTMLCodingContent = "Main certificate:" + mainCertNo + " not found";
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
