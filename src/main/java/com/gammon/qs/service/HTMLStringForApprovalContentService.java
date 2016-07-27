package com.gammon.qs.service;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.helper.DateHelper;
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
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class HTMLStringForApprovalContentService implements Serializable{

	private static final long serialVersionUID = -8118326890744998662L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MasterListWSDao masterListDao;
	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private PaymentCertHBDao paymentHBDao;
	@Autowired
	private	TenderHBDao taHBDao;
	@Autowired
	private MainCertHBDao mainCertHBDao;
	@Autowired
	private ResourceSummaryHBDao bqResourceSummaryHBDao;
	@Autowired
	private PaymentService paymentRepositoryHB;
	@Autowired
	private MainCertWSDao mainCertWSDao;
	@Autowired
	private MasterListService masterListRepository;
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;
	@Autowired
	private TenderDetailHBDao tenderAnalysisDetailHBDao;
	
	
	public String makeHTMLStringForSCPaymentCert(String jobNumber, String subcontractNumber, String paymentNo, String htmlVersion){
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
			job = jobHBDao.obtainJobInfo(jobNumber);
			  
			if(paymentNo==null || "".equals(paymentNo.trim()) || paymentNo.trim().length()==0){// check the paymentNo
				logger.info("Payment number is null --> Max. Payment No. will be used.");
				scPaymentCertList = paymentHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, subcontractNumber);

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
			paymentCertViewWrapper = paymentRepositoryHB.calculatePaymentCertificateSummary(jobNumber, subcontractNumber, currentPaymentNo);
			
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
			List<ResourceSummary> resourceList = new ArrayList<ResourceSummary>();
			resourceList = bqResourceSummaryHBDao.getResourceSummariesSearch(job, subcontractNumber, "14*", null);
			
			for (int i=0; i<resourceList.size(); i++) {
					ivCumAmt += resourceList.get(i).getCurrIVAmount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (htmlVersion.equals("W"))
			strHTMLCodingContent = "<html><head>"+
			"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
			"<title>Subcontract Payment Certificate</title>"+
			"<style type=\"text/css\">"+
			"table.simple {"+
			"border: 1px solid black;"+
			"padding: 10px;"+
			"font-family: arial;"+
			"font-size: 10pt;"+
			"width: 602px;"+
			"}"+
			"tr.first {"+
			"background-color: #ffffff;"+
			"height: 25px;"+
			"}"+
			"tr.second {"+
			"background-color: #ccffcc;"+
			"height: 25px;"+
			"}"+
			"td.cell02 {"+
			"border-bottom: 1px solid black;"+
			"}"+
			"td.cell03 {"+
			"font-weight: bold;"+
			"}"+
			"</style></head>"+
			"<body>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\" style=\"width: 160px;\">Job Number:</td>"+
			"<td style=\"width: 120px;\">"+paymentCertViewWrapper.getJobNumber()+"</td>"+
			"<td style=\"width: 300px;\">"+paymentCertViewWrapper.getJobDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Subcontract:</td>"+
			"<td>"+paymentCertViewWrapper.getSubContractNo()+"</td>"+
			"<td>"+paymentCertViewWrapper.getSubContractDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Subcontractor:</td>"+
			"<td>"+paymentCertViewWrapper.getSubcontractorNo()+"</td>"+
			"<td>"+paymentCertViewWrapper.getSubcontractorDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">LOA Signed Date:</td>"+
			"<td>"+(scPackage.getLoaSignedDate()!=null?DateHelper.formatDate(scPackage.getLoaSignedDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Subcontract Document Signed Date:</td>"+
			"<td>"+(scPackage.getScDocScrDate()!=null?DateHelper.formatDate(scPackage.getScDocScrDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Subcontract Sum:</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubcontractSum()!=null?amountRender(paymentCertViewWrapper.getSubcontractSum()):new Double(0))+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Addendum:</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getAddendum()!=null?amountRender(paymentCertViewWrapper.getAddendum()):new Double(0))+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Revised Value:</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getRevisedValue()!=null?amountRender(paymentCertViewWrapper.getRevisedValue()):new Double(0))+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Maxi Retention:</td>"+
			"<td style=\"text-align: right;\">"+((scPackage.getRetentionAmount()!=null)?amountRender(scPackage.getRetentionAmount()):new Double (0))+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Payment No:</td>"+
			"<td>"+paymentCertViewWrapper.getPaymentCertNo()+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Payment Type:</td>"+
			"<td>"+paymentCertViewWrapper.getPaymentType()+"</td>"+
			"<td></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td style=\"width: 160px;\"></td>"+
			"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Movement</td>"+
			"<td style=\"width: 20px;\"></td>"+
			"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Total</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Measured works</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getMeasuredWorksMovement()!=null?amountRender(paymentCertViewWrapper.getMeasuredWorksMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getMeasuredWorksTotal()!=null?amountRender(paymentCertViewWrapper.getMeasuredWorksTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Dayworks</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getDayWorkSheetMovement()!=null?amountRender(paymentCertViewWrapper.getDayWorkSheetMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getDayWorkSheetTotal()!=null?amountRender(paymentCertViewWrapper.getDayWorkSheetTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Variation</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getVariationMovement()!=null?amountRender(paymentCertViewWrapper.getVariationMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getVariationTotal()!=null?amountRender(paymentCertViewWrapper.getVariationTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Others</td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getOtherMovement()!=null?amountRender(paymentCertViewWrapper.getOtherMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getOtherTotal()!=null?amountRender(paymentCertViewWrapper.getOtherTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubMovement1()!=null?amountRender(paymentCertViewWrapper.getSubMovement1()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal1()!=null?amountRender(paymentCertViewWrapper.getSubTotal1()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">(Retention)</td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessRetentionMovement1()!=null?amountRender(paymentCertViewWrapper.getLessRetentionMovement1()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessRetentionTotal1()!=null?amountRender(paymentCertViewWrapper.getLessRetentionTotal1()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubMovement2()!=null?amountRender(paymentCertViewWrapper.getSubMovement2()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal2()!=null?amountRender(paymentCertViewWrapper.getSubTotal2()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">MOS</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getMaterialOnSiteMovement()!=null?amountRender(paymentCertViewWrapper.getMaterialOnSiteMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getMaterialOnSiteTotal()!=null?amountRender(paymentCertViewWrapper.getMaterialOnSiteTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">(Retention)</td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessRetentionMovement2()!=null?amountRender(paymentCertViewWrapper.getLessRetentionMovement2()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessRetentionTotal2()!=null?amountRender(paymentCertViewWrapper.getLessRetentionTotal2()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubMovement3()!=null?amountRender(paymentCertViewWrapper.getSubMovement3()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal3()!=null?amountRender(paymentCertViewWrapper.getSubTotal3()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Adjustments</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getAdjustmentMovement()!=null?amountRender(paymentCertViewWrapper.getAdjustmentMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getAdjustmentTotal()!=null?amountRender(paymentCertViewWrapper.getAdjustmentTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">GST payable</td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getGstPayableMovement()!=null?amountRender(paymentCertViewWrapper.getGstPayableMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getGstPayableTotal()!=null?amountRender(paymentCertViewWrapper.getGstPayableTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubMovement4()!=null?amountRender(paymentCertViewWrapper.getSubMovement4()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal4()!=null?amountRender(paymentCertViewWrapper.getSubTotal4()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">(Contra Charge)</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessContraChargesMovement()!=null?amountRender(paymentCertViewWrapper.getLessContraChargesMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessContraChargesTotal()!=null?amountRender(paymentCertViewWrapper.getLessContraChargesTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">(GST receivable)</td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessGSTReceivableMovement()!=null?amountRender(paymentCertViewWrapper.getLessGSTReceivableMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td class=\"cell02\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessGSTReceivableTotal()!=null?amountRender(paymentCertViewWrapper.getLessGSTReceivableTotal()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Amount due</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getAmountDueMovement()!=null?amountRender(paymentCertViewWrapper.getAmountDueMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal5()!=null?amountRender(paymentCertViewWrapper.getSubTotal5()):new Double(0))+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td></td>"+
			"<td style=\"text-align: right;\"></td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Previous Cert</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getLessPreviousCertificationsMovement()!=null?amountRender(paymentCertViewWrapper.getLessPreviousCertificationsMovement()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\"></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Cumulative Cert</td>"+
			"<td style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubTotal5()!=null?amountRender(paymentCertViewWrapper.getSubTotal5()):new Double(0))+"</td>"+
			"<td></td>"+
			"<td style=\"text-align: right;\"></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\" style=\"width: 160px;\">Payment terms:</td>"+
			"<td style=\"width: 160px;\">"+scPackage.getPaymentTerms()+"</td>"+
			"<td style=\"width: 260px;\"></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Payment due on:</td>"+
			"<td>"+strPaymentDueDate+"</td>"+
			"<td></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\">Payment as at:</td>"+
			"<td>"+strPaymentAsAtDate+"</td>"+
			"<td></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br>"+
			"<table class=\"simple\" style=\"text-align: left;\" 2=\"\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"cell03\" style=\"width: 160px;\">Client cert amount:</td>"+
			"<td style=\"text-align: right; width: 160px;\">"+
			(mainCertNumber > 0?amountRender(clientCertAmount):"-")+
			"</td>"+
			"<td style=\"width: 260px;\">&nbsp; &nbsp;"+
			(mainCertNumber > 0?"Cert# " + mainCertNumber: "")+
			"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"cell03\">Internal Value by Package:</td>"+
			"<td style=\"text-align: right;\">"+(amountRender(ivCumAmt))+"</td>"+
			"<td></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br>"+
			"<br>"+
			"<br>"+
			"</body></html>";

		if (htmlVersion.equals("B"))
			strHTMLCodingContent = "<html><head>"+
			"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
			"<title>Subcontract Payment Certificate</title>"+
			"<style type=\"text/css\">"+
			"table.simple {"+
			"border: 1px solid black;"+
			"padding: 5px;"+
			"font-family: arial;"+
			"font-size: 8pt;"+
			"width: 472px;"+
			"}"+
			"tr.first {"+
			"background-color: #ffffff;"+
			"height: 25px;"+
			"}"+
			"tr.second {"+
			"background-color: #ccffcc;"+
			"height: 25px;"+
			"}"+
			"tr.separater {"+
			"height: 25px;"+
			"}"+
			"td.T1C1cell01 {"+
			"width: 150px;"+
			"font-weight: bold;"+
			"}"+
			"td.T1C2cell01 {"+
			"width: 100px;"+
			"}"+
			"td.T1C3cell01 {"+
			"width: 210px;"+
			"}"+
			"td.T2C1cell01 {"+
			"width: 250px;"+
			"font-weight: bold;"+
			"}"+
			"td.T2C2cell01 {"+
			"width: 210px;"+
			"text-align: right;"+
			"}"+
			"td.T2C2cell02 {"+
			"width: 210px;"+
			"text-align: right;"+
			"border-bottom: 1px solid black;"+
			"}"+
			"td.T2C2cell03 {"+
			"width: 210px;"+
			"text-align: left;"+
			"}"+
			"td.T3C1cell01 {"+
			"width: 230px;"+
			"font-weight: bold;"+
			"}"+
			"td.T3C2cell01 {"+
			"width: 110px;"+
			"text-align: right;"+
			"}"+
			"td.T3C3cell01 {"+
			"width: 120px;"+
			"}"+
			"</style>"+
			"</head>"+
			"<body>"+
			"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">Job Number:</td>"+
			"<td class=\"T1C2cell01\">"+paymentCertViewWrapper.getJobNumber()+"</td>"+
			"<td class=\"T1C3cell01\">"+paymentCertViewWrapper.getJobDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T1C1cell01\">Subcontract:</td>"+
			"<td class=\"T1C2cell01\">"+paymentCertViewWrapper.getSubContractNo()+"</td>"+
			"<td class=\"T1C3cell01\">"+paymentCertViewWrapper.getSubContractDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">Subcontractor:</td>"+
			"<td class=\"T1C2cell01\">"+paymentCertViewWrapper.getSubcontractorNo()+"</td>"+
			"<td class=\"T1C3cell01\">"+paymentCertViewWrapper.getSubcontractorDescription()+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T1C1cell01\">LOA Signed Date:</td>"+
			"<td class=\"T1C2cell01\">"+(scPackage.getLoaSignedDate()!=null?DateHelper.formatDate(scPackage.getLoaSignedDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">SC Doc Signed Date:</td>"+
			"<td class=\"T1C2cell01\">"+(scPackage.getScDocScrDate()!=null?DateHelper.formatDate(scPackage.getScDocScrDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T1C1cell01\">Subcontract Sum:</td>"+
			"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getSubcontractSum()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubcontractSum()):0)+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">Addendum:</td>"+
			"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getAddendum()!=null?blackberryAmountRender(paymentCertViewWrapper.getAddendum()):0)+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T1C1cell01\">Revised Value:</td>"+
			"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(paymentCertViewWrapper.getRevisedValue()!=null?blackberryAmountRender(paymentCertViewWrapper.getRevisedValue()):0)+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">Maxi Retention:</td>"+
			"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+((scPackage.getMaxRetentionPercentage()!=null&&scPackage.getRetentionAmount()!=null)?blackberryAmountRender(scPackage.getMaxRetentionPercentage()*scPackage.getRetentionAmount()):0)+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T1C1cell01\">Payment No:</td>"+
			"<td class=\"T1C2cell01\">"+paymentCertViewWrapper.getPaymentCertNo()+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T1C1cell01\">Payment Type:</td>"+
			"<td class=\"T1C2cell01\">"+paymentCertViewWrapper.getPaymentType()+"</td>"+
			"<td class=\"T1C3cell01\"></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br><TABLE border=0><TBODY><TR><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\"></td>"+
			"<td class=\"T2C2cell01\" style=\"text-decoration: underline;font-weight: bold;\">Movement</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Measured works</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getMeasuredWorksMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getMeasuredWorksMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Dayworks</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getDayWorkSheetMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getDayWorkSheetMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Variation</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getVariationMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getVariationMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Others</td>"+
			"<td class=\"T2C2cell02\">"+(paymentCertViewWrapper.getOtherMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getOtherMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\"></td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getSubMovement1()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubMovement1()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">(Retention)</td>"+
			"<td class=\"T2C2cell02\">"+(paymentCertViewWrapper.getLessRetentionMovement1()!=null?blackberryAmountRender(paymentCertViewWrapper.getLessRetentionMovement1()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\"></td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getSubMovement2()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubMovement2()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">MOS</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getMaterialOnSiteMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getMaterialOnSiteMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">(Retention)</td>"+
			"<td class=\"T2C2cell02\">"+(paymentCertViewWrapper.getLessRetentionMovement2()!=null?blackberryAmountRender(paymentCertViewWrapper.getLessRetentionMovement2()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\"></td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getSubMovement3()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubMovement3()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Adjustments</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getAdjustmentMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getAdjustmentMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">GST payable</td>"+
			"<td class=\"T2C2cell02\">"+(paymentCertViewWrapper.getGstPayableMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getGstPayableMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\"></td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getSubMovement4()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubMovement4()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">(Contra Charge)</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getLessContraChargesMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getLessContraChargesMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">(GST receivable)</td>"+
			"<td class=\"T2C2cell02\">"+(paymentCertViewWrapper.getLessGSTReceivableMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getLessGSTReceivableMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Amount due</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getAmountDueMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getAmountDueMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">&nbsp;&nbsp;</td>"+
			"<td class=\"T2C2cell01\"></td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Previous Cert</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getLessPreviousCertificationsMovement()!=null?blackberryAmountRender(paymentCertViewWrapper.getLessPreviousCertificationsMovement()):0)+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Cumulative Cert</td>"+
			"<td class=\"T2C2cell01\">"+(paymentCertViewWrapper.getSubTotal5()!=null?blackberryAmountRender(paymentCertViewWrapper.getSubTotal5()):0)+"</td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Payment terms:</td>"+
			"<td class=\"T2C2cell03\">"+scPackage.getPaymentTerms()+"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Payment due on:</td>"+
			"<td class=\"T2C2cell03\">"+strPaymentDueDate+"</td>"+
			"</tr>"+
			"<tr class=\"first\">"+
			"<td class=\"T2C1cell01\">Payment as at:</td>"+
			"<td class=\"T2C2cell03\">"+strPaymentAsAtDate+"</td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"<table class=\"simple\" style=\"text-align: left;\" 2=\"\" cellspacing=\"0\">"+
			"<tbody>"+
			"<tr class=\"first\">"+
			"<td class=\"T3C1cell01\">Client cert amount:</td>"+
			"<td class=\"T3C2cell01\">"+
			(mainCertNumber!=-1?blackberryAmountRender(clientCertAmount):"-")+
			"</td>"+
			"<td class=\"T3C3cell01\">&nbsp;&nbsp;"+
			(mainCertNumber!=-1?"Cert#: "+mainCertNumber:" ")+"<br>"+
			"</td>"+
			"</tr>"+
			"<tr class=\"second\">"+
			"<td class=\"T3C1cell01\">Internal Value<br>by Package:</td>"+
			"<td class=\"T3C2cell01\">"+(blackberryAmountRender(ivCumAmt))+"</td>"+
			"<td class=\"T3C3cell01\">&nbsp;</td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"</body></html>";
			
		return strHTMLCodingContent;
	}
	
	public String makeHTMLStringForTenderAnalysis(String jobNumber, String subcontractNumber, String htmlVersion){
		
		String strHTMLCodingContent = "";
		List<Tender> listTenderAnalysis = new ArrayList<Tender>();
		logger.info("Tender Analysis List size: " + listTenderAnalysis.size());
			try {
				listTenderAnalysis = taHBDao.obtainTenderAnalysis(jobNumber, subcontractNumber);
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
			
			Subcontract scPackage = new Subcontract();
			
			try {
				jobHeaderInfo = jobHBDao.obtainJobInfo(jobNumber);
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

			if (htmlVersion.equals("W")) {
				strHTMLCodingContent= "<html><head>"+
				"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
				"<style type=\"text/css\">"+
				"table.simple {"+
				"border: 1px solid black;"+
				"padding: 10px;"+
				"font-family: arial;"+
				"font-size: 10pt;"+
				"width: 602px;"+
				"}"+
				"table.simple2 {"+
				"border: 0px;"+
				"padding: 10px;"+
				"font-family: arial;"+
				"font-size: 10pt;"+
				"width: 602px;"+
				"}"+
				"tr.first {"+
				"background-color: #ffffff;"+
				"height: 25px;"+
				"}"+
				"tr.second {"+
				"background-color: #ccffcc;"+
				"height: 25px;"+
				"}"+
				"td.cell02 {"+
				"border-bottom: 1px solid black;"+
				"}"+
				"td.cell03 {"+
				"font-weight: bold;"+
				"}"+
				"td.cell04 {"+
				"text-align: right;"+
				"}"+
				"</style>"+
				"</head>"+
				"<body style=\"font-family: arial; font-size: 8pt;\">"+
				"<table class=\"simple2\">"+
				"<tbody>"+
				"<tr>"+
				"<td class=\"cell03\""+
				"style=\"text-align: center; text-decoration: underline;\">SUB-CONTRACT TENDER ANALYSIS</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<br>"+
				"<table class=\"simple2\" cellpadding=\"2\" cellspacing=\"0\">"+
				"<tbody>"+
				"<tr>"+
				"<td class=\"cell03\" style=\"width: 160px;\">Job No:</td>"+
				"<td style=\"width: 100px;\">"+jobNumber+"</td>"+
				"<td style=\"width: 320px;\">"+jobHeaderInfo.getDescription()+"</td>"+
				"</tr>"+
				"<tr>"+
				"<td class=\"cell03\">Subcontract:</td>"+
				"<td>"+subcontractNumber+"</td>"+
				"<td>"+scPackage.getDescription()+"</td>"+
				"</tr>"+
				"<tr>"+
				"<td class=\"cell03\">LOA Signed Date:</td>"+
				"<td>"+(scPackage.getLoaSignedDate()!=null?DateHelper.formatDate(scPackage.getLoaSignedDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
				"<td>"+"</td>"+
				"</tr>"+
				"<tr>"+
				"<td class=\"cell03\">Subcontract Document Signed Date:</td>"+
				"<td>"+(scPackage.getScDocScrDate()!=null?DateHelper.formatDate(scPackage.getScDocScrDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
				"<td>"+"</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<br>"+
				"<table class=\"simple2\" cellpadding=\"2\" cellspacing=\"0\">"+
				"<tbody>"+
				"<tr>"+
				"<td>TENDER RESULT AFTER CHECKING</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<table class=\"simple\" cellpadding=\"2\" cellspacing=\"0\">"+
				"<tbody>"+
				"<tr>"+
				"<td style=\"width: 460px;\"><b>TENDER</b></td>"+
				"<td class=\"cell04\" style=\"width: 120px;\"><b>AMOUNT</b></td>"+
				"</tr>";		
				
				for(int i=0; i < listTenderAnalysis.size();  i++ )
				{
					Tender curTenderAnalysis = listTenderAnalysis.get(i);
					if (curTenderAnalysis.getVendorNo().equals(0)){
						try {
							for(TenderDetail curTenderAnalysisDetail: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis)){
								comparableBudgetAmount += curTenderAnalysisDetail.getRateBudget()*curTenderAnalysisDetail.getQuantity();
							}
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					}else{
					    logger.info("Vendor Number: "+curTenderAnalysis.getVendorNo());
						try {
							vendorName = receiveVendorName(curTenderAnalysis.getVendorNo().toString());
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
							listTenderAnalysisDetails = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);//taHBDao.getTenderAnalysisDetail(jobNumber, new Integer(curTenderAnalysis.getVendorNo().toString()), new Integer(subcontractNumber));
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

						if (i%2==0) {
							strHTMLCodingContent+= 
								"<tr class=\"second\">"+
								"<td>"+vendorName+"<br>"+
								"("+curTenderAnalysis.getVendorNo()+")</td>"+
								"<td class=\"cell04\">"+amountRender(totalSubcontractSum)+"</td>"+
								"</tr>";
						} else {
							strHTMLCodingContent+= 
								"<tr class=\"first\">"+
								"<td>"+vendorName+"<br>"+
								"("+curTenderAnalysis.getVendorNo()+")</td>"+
								"<td class=\"cell04\">"+amountRender(totalSubcontractSum)+"</td>"+
								"</tr>";
						}

						totalSubcontractSum = new Double(0);
					}
				}
					
				if (listTenderAnalysis.size()%2==0) {
					strHTMLCodingContent+=	
						"<tr class=\"second\">"+
						"<td><b>Tender Budget:&nbsp;&nbsp;</b>@tenderBudget@</td>"+
						"<td></td>"+
						"</tr>"+
						"</tbody>"+
						"</table>";
				}else{
					strHTMLCodingContent+=	
						"<tr class=\"first\">"+
						"<td><b>Tender Budget:&nbsp;&nbsp;</b>@tenderBudget@</td>"+
						"<td></td>"+
						"</tr>"+
						"</tbody>"+
						"</table>";
					
				}

				strHTMLCodingContent+=	
					"<br>"+
					"<table class=\"simple2\" cellpadding=\"2\" cellspacing=\"0\">"+
					"<tbody>"+
					"<tr>"+
					"<td colspan=\"2\" rowspan=\"1\">FURTHER ACTION(S) DECIDED:</td>"+
					"</tr>"+
					"<tr>"+
					"<td style=\"width: 100px;\">Award to:</td>"+
					"<td class=\"cell03\" style=\"width: 480px;\">"+recommendVendor+"</td>"+
					"</tr></tbody></table></body></html>";
				
				strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@tenderBudget@", (amountRender(comparableBudgetAmount).toString()));
			
		}
			
			if (htmlVersion.equals("B")) {
				strHTMLCodingContent= "<html><head>"+
				"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
				"<title>Tender Analysis</title>"+
				"<style type=\"text/css\">"+
				"table.simple {"+
				"border: 1px solid black;"+
				"padding: 5px;"+
				"font-family: arial;"+
				"font-size: 8pt;"+
				"width: 472px;"+
				"}"+
				"table.simple2 {"+
				"border: 0px;"+
				"padding: 5px;"+
				"font-family: arial;"+
				"font-size: 8pt;"+
				"width: 472px;"+
				"}"+
				"tr.first {"+
				"background-color: #ffffff;"+
				"height: 25px;"+
				"}"+
				"tr.second {"+
				"background-color: #ccffcc;"+
				"height: 25px;"+
				"}"+
				"tr.separater {"+
				"height: 25px;"+
				"}"+
				"td.T1C1cell01 {"+
				"width: 140px;"+
				"font-weight: bold;"+
				"}"+
				"td.T1C2cell01 {"+
				"width: 90px;"+
				"}"+
				"td.T1C3cell01 {"+
				"width: 230px;"+
				"}"+
				"td.T2C1cell01 {"+
				"width: 300px;"+
				"font-weight: bold;"+
				"}"+
				"td.T2C1cell02 {"+
				"width: 300px;"+
				"}"+
				"td.T2C2cell01 {"+
				"width: 160px;"+
				"text-align: right;"+
				"font-weight: bold;"+
				"}"+
				"td.T2C2cell02 {"+
				"width: 160px;"+
				"text-align: right;"+
				"}"+
				"td.T3C1cell01 {"+
				"width: 460px;"+
				"font-weight: bold;"+
				"}"+
				"td.T3C1cell02 {"+
				"width: 460px;"+
				"}"+
				"</style>"+
				"</head>"+
				"<body style=\"font-family: arial; font-size: 8pt;\">"+
				"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
				"<table class=\"simple2\">"+
				"<tbody>"+
				"<tr>"+
				"<td class=\"T3C1cell01\""+
				"style=\"text-align: center; text-decoration: underline;\">SUB-CONTRACT TENDER ANALYSIS</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
				"<table class=\"simple\" cellpadding=\"2\" cellspacing=\"0\">"+
				"<tbody>"+
				"<tr class=\"first\">"+
				"<td class=\"T1C1cell01\">Job No:</td>"+
				"<td class=\"T1C2cell01\">"+jobNumber+"</td>"+
				"<td class=\"T1C3cell01\">"+jobHeaderInfo.getDescription()+"</td>"+
				"</tr>"+
				"<tr class=\"second\">"+
				"<td class=\"T1C1cell01\">Subcontract:</td>"+
				"<td class=\"T1C2cell01\">"+subcontractNumber+"</td>"+
				"<td class=\"T1C3cell01\">"+scPackage.getDescription()+"</td>"+
				"</tr>"+
				"<tr>"+
				"<tr class=\"first\">"+
				"<td class=\"T1C1cell01\">LOA Signed Date:</td>"+
				"<td class=\"T1C2cell01\">"+(scPackage.getLoaSignedDate()!=null?DateHelper.formatDate(scPackage.getLoaSignedDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
				"<td class=\"T1C3cell01\">"+"</td>"+
				"</tr>"+
				"<tr>"+
				"<tr class=\"second\">"+
				"<td class=\"T1C1cell01\">SC Doc Signed Date:</td>"+
				"<td class=\"T1C2cell01\">"+(scPackage.getScDocScrDate()!=null?DateHelper.formatDate(scPackage.getScDocScrDate(), "dd/MM/yyyy"):"Not Signed")+"</td>"+
				"<td class=\"T1C3cell01\">"+"</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
				"<table class=\"simple2\">"+
				"<tbody>"+
				"<tr>"+
				"<td class=\"T3C1cell02\">TENDER RESULTS AFTER CHECKING</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
				"<tbody>"+
				"<tr>"+
				"<td class=\"T2C1cell01\">TENDERER</td>"+
				"<td class=\"T2C2cell01\">AMOUNT</td>"+
				"</tr>";
				
				for(int i=0; i < listTenderAnalysis.size();  i++ )
				{
					Tender curTenderAnalysis = listTenderAnalysis.get(i);
					if (!curTenderAnalysis.getVendorNo().equals(0)){
						try {
							vendorName = receiveVendorName(curTenderAnalysis.getVendorNo().toString());
						} catch (Exception e) {
							e.printStackTrace();
						}

						if ("RCM".equals(curTenderAnalysis.getStatus()))
							recommendVendor=vendorName;

						List<TenderDetail> listTenderAnalysisDetails = new ArrayList<TenderDetail>();

						try {
							listTenderAnalysisDetails = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis);				} catch (NumberFormatException e) {
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

							if (i%2==0) {
								strHTMLCodingContent+= 
									"<tr class=\"second\">"+
									"<td class=\"T2C1cell02\">"+vendorName+"<br>"+
									"("+curTenderAnalysis.getVendorNo()+")</td>"+
									"<td class=\"T2C2cell02\">"+amountRender(totalSubcontractSum)+"</td>"+
									"</tr>";
							} else {
								strHTMLCodingContent+= 
									"<tr class=\"first\">"+
									"<td class=\"T2C1cell02\">"+vendorName+"<br>"+
									"("+curTenderAnalysis.getVendorNo()+")</td>"+
									"<td class=\"T2C2cell02\">"+amountRender(totalSubcontractSum)+"</td>"+
									"</tr>";
							}

							totalSubcontractSum = new Double(0);
					}
					/**
					 * modified by Tiky Wong on 22/05/2012
					 * 
					 * Fixing: Blackberry version's Tender Budget cannot be shown correctly which always shows 0
					 */
					else{
						try {
							for(TenderDetail curTenderAnalysisDetail: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(curTenderAnalysis))
								comparableBudgetAmount += curTenderAnalysisDetail.getRateBudget()*curTenderAnalysisDetail.getQuantity();
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					}
				}
					
				if (listTenderAnalysis.size()%2==0) {
				strHTMLCodingContent+=	
					"<tr class=\"second\">"+
					"<td class=\"T2C1cell02\"><b>Tender Budget:</b>"+
					"</td>"+
					"<td class=\"T2C2cell02\">@tenderBudget@</td>"+
					"</tr>"+
					"</tbody>"+
					"</table>";
				} else {
					strHTMLCodingContent+=	
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell02\"><b>Tender Budget:</b>"+
						"</td>"+
						"<td class=\"T2C2cell02\">@tenderBudget@</td>"+
						"</tr>"+
						"</tbody>"+
						"</table>";
				}
					strHTMLCodingContent+=	
					"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
					"<table class=\"simple2\">"+
					"<tbody>"+
					"<tr>"+
					"<td class=\"T3C1cell02\">FURTHER ACTION(S) DECIDED:</td>"+
					"</tr>"+
					"<tr>"+
					"<td class=\"T3C1cell02\">Award to:&nbsp;&nbsp;&nbsp;&nbsp;<b>"+recommendVendor+"</b></td>"+
					"</tr></tbody></table>"+
					"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
					"</body></html>";

				strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@tenderBudget@", (amountRender(comparableBudgetAmount).toString()));
			
		}

			return strHTMLCodingContent;
	}
	
	public String makeHTMLStringForAddendumApproval(String jobNumber, String subcontractNumber, String htmlVersion){
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
			jobHeaderInfo = jobHBDao.obtainJobInfo(jobNumber);
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
		if (htmlVersion.equals("W")) {
		strHTMLCodingContent="<html><head>"+
		"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"><title>SC Addendum</title>"+
		"<style type=\"text/css\">"+
		"table.simple {"+
		"border: 1px solid black;"+
		"padding: 10px;"+
		"font-family: arial;"+
		"font-size: 10pt;"+
		"width: 602px;"+
		"}"+
		"table.simpleToBeApprovedCol {"+
		"padding: 10px;"+
		"font-family: arial;"+
		"font-size: 10pt;"+
		"width: 280px;"+
		"}"+
		"tr.first {"+
		"background-color: #ffffff;"+
		"height: 25px;"+
		"}"+
		"tr.second {"+
		"background-color: #ccffcc;"+
		"height: 25px;"+
		"}"+
		"td.cell02 {"+
		"border-bottom: 1px solid black;"+
		"}"+
		"td.cell03 {"+
		"font-weight: bold;"+
		"}"+
		"</style>"+
		"</head>"+
		"<body>"+
		"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\" style=\"width: 160px;\">Job Number:</td>"+
		"<td style=\"width: 120px;\">"+jobNumber+"</td>"+
		"<td style=\"width: 300px;\">"+jobHeaderInfo.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Subcontract:</td>"+
		"<td>"+subcontractNumber+"</td>"+
		"<td>"+scPackage.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\">Subcontractor:</td>"+
		"<td>"+scPackage.getVendorNo()+"</td>"+
		"<td>"+vendorName+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Original SC Sum:</td>"+
		"<td style=\"text-align: right;\">"+(amountRender(scPackage.getOriginalSubcontractSum()))+"</td>"+
		"<td></td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\"></td>"+
		"<td class=\"cell03\">Current</td>"+
		"<td style=\"text-align: right;\"><table class=\"simpleToBeApprovedCol\" style=\"text-align: left;\"><tr><td class=\"cell03\"  style=\"width: 120px;\">After Approved</td><td class=\"cell03\"  style=\"width: 170px;\"/></tr></table></td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Remeasured SC Sum:</td>"+
		"<td style=\"text-align: right;\">"+(amountRender(scPackage.getRemeasuredSubcontractSum()))+"</td>"+
		"<td><table class=\"simpleToBeApprovedCol\" style=\"text-align: right;\"><tr><td style=\"width: 120px;\">@newRemSCSum@</td><td class=\"cell03\"  style=\"width: 170px;\"/></tr></table></td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\">Approved VO</td>"+
		"<td style=\"text-align: right;\">"+(amountRender(scPackage.getApprovedVOAmount()))+"</td>"+
		"<td><table class=\"simpleToBeApprovedCol\" style=\"text-align: right;\"><tr><td style=\"width: 120px;\">@toBeApprovedVO@</td><td class=\"cell03\"  style=\"width: 170px;\"/></tr></table></td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Revised SC Sum</td>"+
		"<td style=\"text-align: right;\">"+(amountRender((scPackage.getRemeasuredSubcontractSum()+scPackage.getApprovedVOAmount())))+"</td>"+
		"<td><table class=\"simpleToBeApprovedCol\" style=\"text-align: right;\"><tr><td style=\"width: 120px;\">@newRevisedSCSum@</td><td class=\"cell03\"  style=\"width: 170px;\"/></tr></table></td>"+
		"</tr>"+
		"</tbody>"+
		"</table>"+
		"<br>"+
		"<table style=\"text-align: left; width: 952px; height: 74px;\" class=\"simple\" cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr>"+
		"<td style=\"width: 130px;font-weight: bold;\">BQ Item</td>"+
		"<td style=\"width: 220px; font-weight: bold;\">Description</td>"+
		"<td style=\"width: 110px; font-weight: bold; text-align: right;\">To be<br>Approved<br>Qty</td>"+
		"<td style=\"width: 110px; font-weight: bold; text-align: right;\">To be<br>Approved<br>Rate</td>"+
		"<td style=\"width: 110px; font-weight: bold; text-align: right;\">To be<br>Approved<br>Value</td>"+
		"<td style=\"width: 110px; font-weight: bold; text-align: right;\">Variance<br>Value</td>"+
		"<td style=\"width: 60px; font-weight: bold; text-align: center;\">Unit</td>"+
		"<td style=\"width: 80px; font-weight: bold;\">Remark</td>"+
		"</tr>";
		
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
				if (highlight){
					strHTMLCodingContent+=
						"<tr class=\"second\">"+
						"<td style=\"text-align: left;\">"+curaddendumEnquiryList.getBillItem()+"</td>"+
						"<td style=\"text-align: left;\">"+curaddendumEnquiryList.getDescription()+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getToBeApprovedQuantity()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getScRate()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getToBeApprovedAmount()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender((curaddendumEnquiryList.getToBeApprovedAmount()-(SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved())?curaddendumEnquiryList.getTotalAmount():0.0))))+"</td>"+
						"<td style=\"text-align: center;\">"+curaddendumEnquiryList.getUnit()+"</td>"+
						"<td style=\"text-align: left;\">"+(curaddendumEnquiryList.getRemark()!=null?curaddendumEnquiryList.getRemark():" " )+"</td>"+
						"</tr>";
				} else {
					strHTMLCodingContent+=
						"<tr class=\"first\">"+
						"<td style=\"text-align: left;\">"+curaddendumEnquiryList.getBillItem()+"</td>"+
						"<td style=\"text-align: left;\">"+curaddendumEnquiryList.getDescription()+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getToBeApprovedQuantity()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getScRate()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender(curaddendumEnquiryList.getToBeApprovedAmount()))+"</td>"+
						"<td style=\"text-align: right;\">"+(amountRender((curaddendumEnquiryList.getToBeApprovedAmount()-(SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved())?curaddendumEnquiryList.getTotalAmount():0.0))))+"</td>"+
						"<td style=\"text-align: center;\">"+curaddendumEnquiryList.getUnit()+"</td>"+
						"<td style=\"text-align: left;\">"+(curaddendumEnquiryList.getRemark()!=null?curaddendumEnquiryList.getRemark():" ")+"</td>"+
						"</tr>";
				}
				highlight = !highlight;
				totalToBeApprovedValue += curaddendumEnquiryList.getToBeApprovedAmount();
				totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount()-(SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved())?curaddendumEnquiryList.getTotalAmount():0.0));
			}
		}
		if (highlight) {
			strHTMLCodingContent+="</tr>"+
			"<tr class=\"second\">"+
			"<td style=\"font-weight: bold;\">Total:</td>"+
			"<td></td>"+
			"<td></td>"+
			"<td></td>"+
			"<td style=\"font-weight: bold; text-align: right;\">"+(amountRender(totalToBeApprovedValue))+"</td>"+
			"<td style=\"font-weight: bold; text-align: right;\">"+(amountRender(totalThisAddendumValue))+"</td>"+
			"<td></td>"+
			"<td></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"</body></html>";
		} else {
			strHTMLCodingContent+="</tr>"+
			"<tr class=\"first\">"+
			"<td style=\"font-weight: bold;\">Total:</td>"+
			"<td></td>"+
			"<td></td>"+
			"<td></td>"+
			"<td style=\"font-weight: bold; text-align: right;\">"+(amountRender(totalToBeApprovedValue))+"</td>"+
			"<td style=\"font-weight: bold; text-align: right;\">"+(amountRender(totalThisAddendumValue))+"</td>"+
			"<td></td>"+
			"<td></td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"</body></html>";
		}

		
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@addendumValue@", (amountRender(totalThisAddendumValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@newRemSCSum@", (amountRender(totalToBeApprovedRemeasuredSCSumValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@toBeApprovedVO@", (amountRender(totalToBeApprovedVOValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@newRevisedSCSum@", (amountRender(totalToBeApprovedVOValue+totalToBeApprovedRemeasuredSCSumValue).toString()));
	}
	if (htmlVersion.equals("B")) {
		strHTMLCodingContent="<html><head>"+
		"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
		"<title>SC Addendum</title>"+
		"<style type=\"text/css\">"+
		"table.simple {"+
		"border: 1px solid black;"+
		"padding: 5px;"+
		"font-family: arial;"+
		"font-size: 8pt;"+
		"width: 472px;"+
		"}"+
		"tr.first {"+
		"background-color: #ffffff;"+
		"height: 25px;"+
		"}"+
		"tr.second {"+
		"background-color: #ccffcc;"+
		"height: 25px;"+
		"}"+
		"tr.separater {"+
		"height: 25px;"+
		"}"+
		"td.T1C1cell01 {"+
		"width: 150px;"+
		"font-weight: bold;"+
		"}"+
		"td.T1C2cell01 {"+
		"width: 100px;"+
		"}"+
		"td.T1C3cell01 {"+
		"width: 210px;"+
		"}"+
		"td.T2C1cell01 {"+
		"width: 300px;"+
		"font-weight: bold;"+
		"}"+
		"td.T2C1cell02 {"+
		"width: 300px;"+
		"}"+
		"td.T2C2cell01 {"+
		"width: 160px;"+
		"font-weight: bold;"+
		"text-align: right;"+
		"}"+
		"td.T2C2cell02 {"+
		"width: 160px;"+
		"text-align: right;"+
		"}"+
		"td.T2C2cell03 {"+
		"width: 160px;"+
		"text-align: left;"+
		"}"+
		"</style>"+
		"</head>"+
		"<body>"+
		"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
		"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">Job Number:</td>"+
		"<td class=\"T1C2cell01\">"+jobNumber+"</td>"+
		"<td class=\"T1C3cell01\">"+jobHeaderInfo.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Subcontract:</td>"+
		"<td class=\"T1C2cell01\">"+subcontractNumber+"</td>"+
		"<td class=\"T1C3cell01\">"+scPackage.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">Subcontractor:</td>"+
		"<td class=\"T1C2cell01\">"+scPackage.getVendorNo()+"</td>"+
		"<td class=\"T1C3cell01\">"+vendorName+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Original Sum:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender(scPackage.getOriginalSubcontractSum()))+"</td>"+
		"<td class=\"T1C3cell01\"></td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\"></td>"+
		"<td class=\"T1C2cell01\">Current</td>"+
		"<td class=\"T1C3cell01\">After Approved</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Remeasured Sum:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender(scPackage.getRemeasuredSubcontractSum()))+"</td>"+
		"<td class=\"T1C3cell01\" style=\"text-align: right;\">@newRemSCSum@</td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">Approved VO:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender(scPackage.getApprovedVOAmount()))+"</td>"+
		"<td class=\"T1C3cell01\" style=\"text-align: right;\">@toBeApprovedVO@</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Revised SC Sum:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender((scPackage.getRemeasuredSubcontractSum()+scPackage.getApprovedVOAmount())))+"</td>"+
		"<td class=\"T1C3cell01\" style=\"text-align: right;\">@newRevisedSCSum@</td>"+
		"</tr>"+
		"</tbody>"+
		"</table>"+
		"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
		"<table class=\"simple\" style=\"text-align: left;\""+
		"cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr class=\"first\">"+
		"<td class=\"T2C1cell01\">Description</td>"+
		"<td class=\"T2C2cell01\">Variance Value</td>"+
		"</tr>";

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
					if (highlight) {
						strHTMLCodingContent +=
								"<tr class=\"second\">" +
								"<td class=\"T2C1cell02\">" + curaddendumEnquiryList.getDescription() + "</td>" +
								"<td class=\"T2C2cell02\">" + (blackberryAmountRender((curaddendumEnquiryList.getToBeApprovedAmount() - (SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved()) ? curaddendumEnquiryList.getTotalAmount() : 0.0)))) + "</td>" +
								"</tr>";
					} else {
						strHTMLCodingContent +=
								"<tr class=\"first\">" +
								"<td class=\"T2C1cell02\">" + curaddendumEnquiryList.getDescription() + "</td>" +
								"<td class=\"T2C2cell02\">" + (blackberryAmountRender((curaddendumEnquiryList.getToBeApprovedAmount() - (SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved()) ? curaddendumEnquiryList.getTotalAmount() : 0.0)))) + "</td>" +
								"</tr>";
					}
					highlight = !highlight;
					totalToBeApprovedValue += curaddendumEnquiryList.getToBeApprovedAmount();
					totalThisAddendumValue += (curaddendumEnquiryList.getToBeApprovedAmount() - (SubcontractDetail.APPROVED.equals(curaddendumEnquiryList.getApproved()) ? curaddendumEnquiryList.getTotalAmount() : 0.0));
				}

			}
		if (highlight) {
			strHTMLCodingContent+=
			"<tr class=\"second\">"+
			"<td class=\"T2C1cell01\">Total:</td>"+
			"<td class=\"T2C2cell01\">"+(blackberryAmountRender((totalThisAddendumValue)))+"</td>"+
			"</tr>"+
			"</tbody>"+
			"</table>"+
			"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
			"</body>"+
			"</html>";
		} else {
			strHTMLCodingContent+=
				"<tr class=\"first\">"+
				"<td class=\"T2C1cell01\">Total:</td>"+
				"<td class=\"T2C2cell01\">"+(blackberryAmountRender((totalThisAddendumValue)))+"</td>"+
				"</tr>"+
				"</tbody>"+
				"</table>"+
				"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
				"</body>"+
				"</html>";
		}

		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@addendumValue@", (blackberryAmountRender(totalThisAddendumValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@newRemSCSum@", (blackberryAmountRender(totalToBeApprovedRemeasuredSCSumValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@toBeApprovedVO@", (blackberryAmountRender(totalToBeApprovedVOValue).toString()));
		strHTMLCodingContent = strHTMLCodingContent.replaceFirst("@newRevisedSCSum@", (blackberryAmountRender(totalToBeApprovedVOValue+totalToBeApprovedRemeasuredSCSumValue).toString()));
	}
	
	return strHTMLCodingContent;
}

	public String makeHTMLStringForSplitTermSC(String jobNumber, String subcontractNumber, String htmlVersion){
		
		JobInfo jobHeaderInfo = new JobInfo();
		double newSCSum = 0.00;
		String strHTMLCodingContent = "";
		try {
			Subcontract scPackage;
			scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber);
			for (SubcontractDetail scDetail: scDetailsHBDao.getSCDetails(scPackage)){
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
			jobHeaderInfo = jobHBDao.obtainJobInfo(jobNumber);
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
		
		if (htmlVersion.equals("W")) {
		strHTMLCodingContent="<html><head>"+
		"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"><title>SC Addendum</title>"+
		"<style type=\"text/css\">"+
		"table.simple {"+
		"border: 1px solid black;"+
		"padding: 10px;"+
		"font-family: arial;"+
		"font-size: 10pt;"+
		"width: 602px;"+
		"}"+
		"tr.first {"+
		"background-color: #ffffff;"+
		"height: 25px;"+
		"}"+
		"tr.second {"+
		"background-color: #ccffcc;"+
		"height: 25px;"+
		"}"+
		"td.cell02 {"+
		"border-bottom: 1px solid black;"+
		"}"+
		"td.cell03 {"+
		"font-weight: bold;"+
		"}"+
		"</style>"+
		"</head>"+
		"<body>"+
		"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\" style=\"width: 160px;\">Job Number:</td>"+
		"<td style=\"width: 120px;\">"+jobNumber+"</td>"+
		"<td style=\"width: 300px;\">"+jobHeaderInfo.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Subcontract:</td>"+
		"<td>"+subcontractNumber+"</td>"+
		"<td>"+scPackage.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\">Subcontractor:</td>"+
		"<td>"+scPackage.getVendorNo()+"</td>"+
		"<td>"+vendorName+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"cell03\">Current Revised SC Sum:</td>"+
		"<td style=\"text-align: right;\">"+(amountRender(scPackage.getSubcontractSum()))+"</td>"+
		"<td></td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"cell03\">New Revised SC Sum:</td>"+
		"<td style=\"text-align: right;\">"+(amountRender(newSCSum))+"</td>"+
		"<td></td>"+
		"</tr>"+
		"</tbody>"+
		"</table>"+
		"</body></html>";
	}
	
	if (htmlVersion.equals("B")) {
		strHTMLCodingContent="<html><head>"+
		"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
		"<title>SC Addendum</title>"+
		"<style type=\"text/css\">"+
		"table.simple {"+
		"border: 1px solid black;"+
		"padding: 5px;"+
		"font-family: arial;"+
		"font-size: 8pt;"+
		"width: 472px;"+
		"}"+
		"tr.first {"+
		"background-color: #ffffff;"+
		"height: 25px;"+
		"}"+
		"tr.second {"+
		"background-color: #ccffcc;"+
		"height: 25px;"+
		"}"+
		"tr.separater {"+
		"height: 25px;"+
		"}"+
		"td.T1C1cell01 {"+
		"width: 150px;"+
		"font-weight: bold;"+
		"}"+
		"td.T1C2cell01 {"+
		"width: 100px;"+
		"}"+
		"td.T1C3cell01 {"+
		"width: 210px;"+
		"}"+
		"td.T2C1cell01 {"+
		"width: 300px;"+
		"font-weight: bold;"+
		"}"+
		"td.T2C1cell02 {"+
		"width: 300px;"+
		"}"+
		"td.T2C2cell01 {"+
		"width: 160px;"+
		"font-weight: bold;"+
		"text-align: right;"+
		"}"+
		"td.T2C2cell02 {"+
		"width: 160px;"+
		"text-align: right;"+
		"}"+
		"td.T2C2cell03 {"+
		"width: 160px;"+
		"text-align: left;"+
		"}"+
		"</style>"+
		"</head>"+
		"<body>"+
		"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
		"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
		"<tbody>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">Job Number:</td>"+
		"<td class=\"T1C2cell01\">"+jobNumber+"</td>"+
		"<td class=\"T1C3cell01\">"+jobHeaderInfo.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Subcontract:</td>"+
		"<td class=\"T1C2cell01\">"+subcontractNumber+"</td>"+
		"<td class=\"T1C3cell01\">"+scPackage.getDescription()+"</td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">Subcontractor:</td>"+
		"<td class=\"T1C2cell01\">"+scPackage.getVendorNo()+"</td>"+
		"<td class=\"T1C3cell01\">"+vendorName+"</td>"+
		"</tr>"+
		"<tr class=\"second\">"+
		"<td class=\"T1C1cell01\">Current Revised SC Sum:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender((scPackage.getSubcontractSum())))+"</td>"+
		"<td class=\"T1C3cell01\"></td>"+
		"</tr>"+
		"<tr class=\"first\">"+
		"<td class=\"T1C1cell01\">New Revised SC Sum:</td>"+
		"<td class=\"T1C2cell01\" style=\"text-align: right;\">"+(blackberryAmountRender(newSCSum))+"</td>"+
		"<td class=\"T1C3cell01\"></td>"+
		"</tr>"+
		"</tbody>"+
		"</table>"+
		"</body>"+
		"</html>";
		}
	return strHTMLCodingContent;
	}
	
	/**
	 * @author koeyyeung
	 * created on 25 Mar, 2015
	 * HTML String For Main Cert Information being called by Web Service
	 * **/
	public String makeHTMLStringForMainCert(	String jobNumber,
												String mainCertNo,
												String htmlVersion) {
		String strHTMLCodingContent = "";
		logger.info("makeHTMLStringForSCMainCert --> Input parameter: jobNo["+jobNumber+"] - Main Cert No["+mainCertNo+"]");
		if(!GenericValidator.isBlankOrNull(jobNumber) && !GenericValidator.isBlankOrNull(mainCertNo)){
			try {
				JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
				
				List<MasterListVendor> clientList = null;
				try {
					clientList = masterListRepository.obtainAllClientList(job.getEmployer());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				MainCert mainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo));
				MainCert previousCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo)-1);
				if(previousCert==null)
					previousCert = new MainCert();
				
				if(mainCert!=null){
					if (htmlVersion.equals("W")){
						strHTMLCodingContent = 
							"<html><head>"+
							"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
							"<title>Main Contract Certificate</title>"+
							"<style type=\"text/css\">"+
							"table.simple {"+
							"border: 1px solid black;"+
							"padding: 10px;"+
							"font-family: arial;"+
							"font-size: 10pt;"+
							"width: 602px;"+
							"}"+
							"tr.first {"+
							"background-color: #ffffff;"+
							"height: 25px;"+
							"}"+
							"tr.second {"+
							"background-color: #ccffcc;"+
							"height: 25px;"+
							"}"+
							"td.cell02 {"+
							"border-bottom: 1px solid black;"+
							"}"+
							"td.cell03 {"+
							"font-weight: bold;"+
							"}"+
							"</style></head>"+
							"<body>"+
							"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
							"<tbody>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\" style=\"width: 160px;\">Job Number:</td>"+
							"<td style=\"width: 120px;\">"+job.getJobNumber()+"</td>"+
							"<td style=\"width: 300px;\">"+job.getDescription()+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\" style=\"width: 160px;\">Client Number:</td>"+
							"<td style=\"width: 120px;\">"+job.getEmployer()+"</td>"+
							"<td style=\"width: 300px;\">"+((clientList!=null && clientList.size()==1)?clientList.get(0).getVendorName():"")+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Certificate No.:</td>"+
							"<td>"+(mainCert.getCertificateNumber()!=null?mainCert.getCertificateNumber():"")+"</td>"+
							"<td></td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Client Certificate No.:</td>"+
							"<td>"+(mainCert.getClientCertNo()!=null?mainCert.getClientCertNo():"")+"</td>"+
							"<td></td>"+
							"</tr>"+
							"</tbody>"+
							"</table>"+
							"<br>"+
							
							
							"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
							"<tbody>"+
							"<tr class=\"first\">"+
							"<td style=\"width: 300px;\"></td>"+
							"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Certificate Movement</td>"+
							"<td style=\"width: 20px;\"></td>"+
							"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Total</td>"+
							"</tr>"+
							/*"<tr class=\"second\">"+
							"<td class=\"cell03\">Main Contractor Amount</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMainContractorAmount()!=null?mainCert.getCertifiedMainContractorAmount():new Double(0))-(previousCert.getCertifiedMainContractorAmount()!=null?previousCert.getCertifiedMainContractorAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMainContractorAmount()!=null?amountRender(mainCert.getCertifiedMainContractorAmount()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">NSC/NDSC Amount</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedNSCNDSCAmount()!=null?mainCert.getCertifiedNSCNDSCAmount():new Double(0))-(previousCert.getCertifiedNSCNDSCAmount()!=null?previousCert.getCertifiedNSCNDSCAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedNSCNDSCAmount()!=null?amountRender(mainCert.getCertifiedNSCNDSCAmount()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">MOS Amount</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMOSAmount()!=null?mainCert.getCertifiedMOSAmount():new Double(0))-(previousCert.getCertifiedMOSAmount()!=null?previousCert.getCertifiedMOSAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMOSAmount()!=null?amountRender(mainCert.getCertifiedMOSAmount()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Main Contractor Retention</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMainContractorRetention()!=null?mainCert.getCertifiedMainContractorRetention():new Double(0))-(previousCert.getCertifiedMainContractorRetention()!=null?previousCert.getCertifiedMainContractorRetention():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMainContractorRetention()!=null?amountRender(mainCert.getCertifiedMainContractorRetention()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Main Contractor Retention Released</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMainContractorRetentionReleased()!=null?mainCert.getCertifiedMainContractorRetentionReleased():new Double(0))-(previousCert.getCertifiedMainContractorRetentionReleased()!=null?previousCert.getCertifiedMainContractorRetentionReleased():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMainContractorRetentionReleased()!=null?amountRender(mainCert.getCertifiedMainContractorRetentionReleased()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Retention for NSC/NDSC</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedRetentionforNSCNDSC()!=null?mainCert.getCertifiedRetentionforNSCNDSC():new Double(0))-(previousCert.getCertifiedRetentionforNSCNDSC()!=null?previousCert.getCertifiedRetentionforNSCNDSC():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedRetentionforNSCNDSC()!=null?amountRender(mainCert.getCertifiedRetentionforNSCNDSC()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Retention for NSC/NDSC Released</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedRetentionforNSCNDSCReleased()!=null?mainCert.getCertifiedRetentionforNSCNDSCReleased():new Double(0))-(previousCert.getCertifiedRetentionforNSCNDSCReleased()!=null?previousCert.getCertifiedRetentionforNSCNDSCReleased():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedRetentionforNSCNDSCReleased()!=null?amountRender(mainCert.getCertifiedRetentionforNSCNDSCReleased()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">MOS Retention</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMOSRetention()!=null?mainCert.getCertifiedMOSRetention():new Double(0))-(previousCert.getCertifiedMOSRetention()!=null?previousCert.getCertifiedMOSRetention():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMOSRetention()!=null?amountRender(mainCert.getCertifiedMOSRetention()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">MOS Retention Released</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedMOSRetentionReleased()!=null?mainCert.getCertifiedMOSRetentionReleased():new Double(0))-(previousCert.getCertifiedMOSRetentionReleased()!=null?previousCert.getCertifiedMOSRetentionReleased():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedMOSRetentionReleased()!=null?amountRender(mainCert.getCertifiedMOSRetentionReleased()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Contra Charge Amount</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedContraChargeAmount()!=null?mainCert.getCertifiedContraChargeAmount():new Double(0))-(previousCert.getCertifiedContraChargeAmount()!=null?previousCert.getCertifiedContraChargeAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedContraChargeAmount()!=null?amountRender(mainCert.getCertifiedContraChargeAmount()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Adjustment Amount</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedAdjustmentAmount()!=null?mainCert.getCertifiedAdjustmentAmount():new Double(0))-(previousCert.getCertifiedAdjustmentAmount()!=null?previousCert.getCertifiedAdjustmentAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedAdjustmentAmount()!=null?amountRender(mainCert.getCertifiedAdjustmentAmount()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Advance Payment</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedAdvancePayment()!=null?mainCert.getCertifiedAdvancePayment():new Double(0))-(previousCert.getCertifiedAdvancePayment()!=null?previousCert.getCertifiedAdvancePayment():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getCertifiedAdvancePayment()!=null?amountRender(mainCert.getCertifiedAdvancePayment()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">CPF Amount</td>"+
							"<td class=\"cell02\" style=\"text-align: right;\">"+(amountRender((mainCert.getCertifiedCPFAmount()!=null?mainCert.getCertifiedCPFAmount():new Double(0))-(previousCert.getCertifiedCPFAmount()!=null?previousCert.getCertifiedCPFAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td class=\"cell02\" style=\"text-align: right;\">"+(mainCert.getCertifiedCPFAmount()!=null?amountRender(mainCert.getCertifiedCPFAmount()):new Double(0))+"</td>"+
							"</tr>"+*/
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Net Amount</td>"+
							"<td class=\"cell03\" style=\"text-align: right;\">"+(amountRender((mainCert.calculateCertifiedNetAmount()!=null?mainCert.calculateCertifiedNetAmount():new Double(0))-(previousCert.calculateCertifiedNetAmount()!=null?previousCert.calculateCertifiedNetAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td class=\"cell03\" style=\"text-align: right;\">"+(mainCert.calculateCertifiedNetAmount()!=null?amountRender(mainCert.calculateCertifiedNetAmount()):new Double(0))+"</td>"+
							"</tr>"+
							/*"<tr class=\"second\">"+
							"<td class=\"cell03\">Gross Amount</td>"+
							"<td class=\"cell03\" style=\"text-align: right;\">"+(amountRender((mainCert.calculateCertifiedGrossAmount()!=null?mainCert.calculateCertifiedGrossAmount():new Double(0))-(previousCert.calculateCertifiedGrossAmount()!=null?previousCert.calculateCertifiedGrossAmount():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td class=\"cell03\" style=\"text-align: right;\">"+(mainCert.calculateCertifiedGrossAmount()!=null?amountRender(mainCert.calculateCertifiedGrossAmount()):new Double(0))+"</td>"+
							"</tr>"+*/
							"</tbody>"+
							"</table>"+
							"<br>"+

							/*"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
							"<tbody>"+
							"<tr class=\"first\">"+
							"<td style=\"width: 300px;\"></td>"+
							"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Certificate Movement</td>"+
							"<td style=\"width: 20px;\"></td>"+
							"<td class=\"cell03\" style=\"text-align: right; text-decoration: underline; width: 200px;\">Total</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">GST Amount(GST Receivable)</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getGstReceivable()!=null?mainCert.getGstReceivable():new Double(0))-(previousCert.getGstReceivable()!=null?previousCert.getGstReceivable():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getGstReceivable()!=null?amountRender(mainCert.getGstReceivable()):new Double(0))+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">GST for Contra Charge(GST Payable)</td>"+
							"<td style=\"text-align: right;\">"+(amountRender((mainCert.getGstPayable()!=null?mainCert.getGstPayable():new Double(0))-(previousCert.getGstPayable()!=null?previousCert.getGstPayable():new Double(0))))+"</td>"+
							"<td></td>"+
							"<td style=\"text-align: right;\">"+(mainCert.getGstPayable()!=null?amountRender(mainCert.getGstPayable()):new Double(0))+"</td>"+
							"</tr>"+
							"</tbody>"+
							"</table>"+
							"<br>"+*/

							"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
							"<tbody>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\" style=\"width: 100px;\">IPA Date:</td>"+
							"<td style=\"width: 120px;\">"+(mainCert.getIpaSubmissionDate()!=null?DateHelper.formatDate(mainCert.getIpaSubmissionDate(), "dd/MM/yyyy"):"")+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">IPA Sent out Date:</td>"+
							"<td>"+(mainCert.getIpaSentoutDate()!=null?DateHelper.formatDate(mainCert.getIpaSentoutDate(), "dd/MM/yyyy"):"")+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Certificate Date:</td>"+
							"<td>"+(mainCert.getCertIssueDate()!=null?DateHelper.formatDate(mainCert.getCertIssueDate(), "dd/MM/yyyy"):"")+"</td>"+
							"</tr>"+
							"<tr class=\"second\">"+
							"<td class=\"cell03\">Certificate As At Date:</td>"+
							"<td>"+(mainCert.getCertAsAtDate()!=null?DateHelper.formatDate(mainCert.getCertAsAtDate(), "dd/MM/yyyy"):"")+"</td>"+
							"</tr>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Certificate Due Date:</td>"+
							"<td>"+(mainCert.getCertDueDate()!=null?DateHelper.formatDate(mainCert.getCertDueDate(), "dd/MM/yyyy"):"")+"</td>"+
							"</tr>"+
							"</tbody>"+
							"</table>"+
							"<br>"+

							"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
							"<tbody>"+
							"<tr class=\"first\">"+
							"<td class=\"cell03\">Remark:</td>"+
							"<td>"+(mainCert.getRemark()!=null?mainCert.getRemark():"")+"</td>"+
							"</tr>"+
							"</tbody>"+
							"</table>"+

							"<br>"+
							"<br>"+
							"</body></html>";
					}
					else if (htmlVersion.equals("B")){
						strHTMLCodingContent = "<html><head>"+
						"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">"+
						"<title>Main Contract Certificate</title>"+
						"<style type=\"text/css\">"+
						"table.simple {"+
						"border: 1px solid black;"+
						"padding: 5px;"+
						"font-family: arial;"+
						"font-size: 8pt;"+
						"width: 472px;"+
						"}"+
						"tr.first {"+
						"background-color: #ffffff;"+
						"height: 25px;"+
						"}"+
						"tr.second {"+
						"background-color: #ccffcc;"+
						"height: 25px;"+
						"}"+
						"tr.separater {"+
						"height: 25px;"+
						"}"+
						"td.T1C1cell01 {"+
						"width: 150px;"+
						"font-weight: bold;"+
						"}"+
						"td.T1C2cell01 {"+
						"width: 100px;"+
						"}"+
						"td.T1C3cell01 {"+
						"width: 210px;"+
						"}"+
						"td.T2C1cell01 {"+
						"width: 250px;"+
						"font-weight: bold;"+
						"}"+
						"td.T2C2cell01 {"+
						"width: 210px;"+
						"text-align: right;"+
						"}"+
						"td.T2C2cell02 {"+
						"width: 210px;"+
						"text-align: right;"+
						"border-bottom: 1px solid black;"+
						"}"+
						"td.T2C2cell03 {"+
						"width: 210px;"+
						"text-align: left;"+
						"}"+
						"td.T3C1cell01 {"+
						"width: 230px;"+
						"font-weight: bold;"+
						"}"+
						"td.T3C2cell01 {"+
						"width: 110px;"+
						"text-align: right;"+
						"}"+
						"td.T3C3cell01 {"+
						"width: 120px;"+
						"}"+
						"</style>"+
						"</head>"+
						"<body>"+
						"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
						"<tbody>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">Job Number:</td>"+
						"<td class=\"T1C2cell01\">"+job.getJobNumber()+"</td>"+
						"<td class=\"T1C3cell01\">"+job.getDescription()+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T1C1cell01\">Client Number:</td>"+
						"<td class=\"T1C2cell01\">"+job.getEmployer()+"</td>"+
						"<td class=\"T1C3cell01\">"+((clientList!=null && clientList.size()==1)?clientList.get(0).getVendorName():"")+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">Certificate No.:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getCertificateNumber()!=null?mainCert.getCertificateNumber():"")+"</td>"+
						"<td></td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T1C1cell01\">Client Certificate No.:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getClientCertNo()!=null?mainCert.getClientCertNo():"")+"</td>"+
						"<td></td>"+
						"</tr>"+
						"</tbody>"+
						"</table>"+

						"<br><TABLE border=0><TBODY><TR><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
						"<tbody>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\"></td>"+
						"<td class=\"T2C2cell01\" style=\"text-decoration: underline;font-weight: bold;\">Certificate Movement</td>"+
						"</tr>"+
						/*"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Main Contractor Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMainContractorAmount()!=null?mainCert.getCertifiedMainContractorAmount():new Double(0))-(previousCert.getCertifiedMainContractorAmount()!=null?previousCert.getCertifiedMainContractorAmount():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">NSC/NDSC Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedNSCNDSCAmount()!=null?mainCert.getCertifiedNSCNDSCAmount():new Double(0))-(previousCert.getCertifiedNSCNDSCAmount()!=null?previousCert.getCertifiedNSCNDSCAmount():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">MOS Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMOSAmount()!=null?mainCert.getCertifiedMOSAmount():new Double(0))-(previousCert.getCertifiedMOSAmount()!=null?previousCert.getCertifiedMOSAmount():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">Main Contractor Retention</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMainContractorRetention()!=null?mainCert.getCertifiedMainContractorRetention():new Double(0))-(previousCert.getCertifiedMainContractorRetention()!=null?previousCert.getCertifiedMainContractorRetention():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Main Contractor Retention Released</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMainContractorRetentionReleased()!=null?mainCert.getCertifiedMainContractorRetentionReleased():new Double(0))-(previousCert.getCertifiedMainContractorRetentionReleased()!=null?previousCert.getCertifiedMainContractorRetentionReleased():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">Retention for NSC/NDSC</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedRetentionforNSCNDSC()!=null?mainCert.getCertifiedRetentionforNSCNDSC():new Double(0))-(previousCert.getCertifiedRetentionforNSCNDSC()!=null?previousCert.getCertifiedRetentionforNSCNDSC():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Retention for NSC/NDSC Released</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedRetentionforNSCNDSCReleased()!=null?mainCert.getCertifiedRetentionforNSCNDSCReleased():new Double(0))-(previousCert.getCertifiedRetentionforNSCNDSCReleased()!=null?previousCert.getCertifiedRetentionforNSCNDSCReleased():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">MOS Retention</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMOSRetention()!=null?mainCert.getCertifiedMOSRetention():new Double(0))-(previousCert.getCertifiedMOSRetention()!=null?previousCert.getCertifiedMOSRetention():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">MOS Retention Released</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedMOSRetentionReleased()!=null?mainCert.getCertifiedMOSRetentionReleased():new Double(0))-(previousCert.getCertifiedMOSRetentionReleased()!=null?previousCert.getCertifiedMOSRetentionReleased():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">Contra Charge Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedContraChargeAmount()!=null?mainCert.getCertifiedContraChargeAmount():new Double(0))-(previousCert.getCertifiedContraChargeAmount()!=null?previousCert.getCertifiedContraChargeAmount():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Adjustment Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedAdjustmentAmount()!=null?mainCert.getCertifiedAdjustmentAmount():new Double(0))-(previousCert.getCertifiedAdjustmentAmount()!=null?previousCert.getCertifiedAdjustmentAmount():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">Advance Payment</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getCertifiedAdvancePayment()!=null?mainCert.getCertifiedAdvancePayment():new Double(0))-(previousCert.getCertifiedAdvancePayment()!=null?previousCert.getCertifiedAdvancePayment():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">CPF Amount</td>"+
						"<td class=\"T2C2cell02\">"+(amountRender((mainCert.getCertifiedCPFAmount()!=null?mainCert.getCertifiedCPFAmount():new Double(0))-(previousCert.getCertifiedCPFAmount()!=null?previousCert.getCertifiedCPFAmount():new Double(0))))+"</td>"+
						"</tr>"+*/
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Net Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.calculateCertifiedNetAmount()!=null?mainCert.calculateCertifiedNetAmount():new Double(0))-(previousCert.calculateCertifiedNetAmount()!=null?previousCert.calculateCertifiedNetAmount():new Double(0))))+"</td>"+
						"</tr>"+
						/*"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">Gross Amount</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.calculateCertifiedGrossAmount()!=null?mainCert.calculateCertifiedGrossAmount():new Double(0))-(previousCert.calculateCertifiedGrossAmount()!=null?previousCert.calculateCertifiedGrossAmount():new Double(0))))+"</td>"+
						"</tr>"+*/
						"</tbody>"+
						"</table>"+

						"<br><TABLE border=0><TBODY><TR><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
						"<tbody>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\"></td>"+
						"<td class=\"T2C2cell01\" style=\"text-decoration: underline;font-weight: bold;\">Certificate Movement</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T2C1cell01\">GST Amount(GST Receivable)</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getGstReceivable()!=null?mainCert.getGstReceivable():new Double(0))-(previousCert.getGstReceivable()!=null?previousCert.getGstReceivable():new Double(0))))+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T2C1cell01\">GST for Contra Charge(GST Payable)</td>"+
						"<td class=\"T2C2cell01\">"+(amountRender((mainCert.getGstPayable()!=null?mainCert.getGstPayable():new Double(0))-(previousCert.getGstPayable()!=null?previousCert.getGstPayable():new Double(0))))+"</td>"+
						"</tr>"+
						"</tbody>"+
						"</table>"+

						"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
						"<tbody>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">IPA Date:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getIpaSubmissionDate()!=null?DateHelper.formatDate(mainCert.getIpaSubmissionDate(), "dd/MM/yyyy"):"")+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T1C1cell01\">IPA Sent out Date:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getIpaSentoutDate()!=null?DateHelper.formatDate(mainCert.getIpaSentoutDate(), "dd/MM/yyyy"):"")+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">Certificate Date:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getCertIssueDate()!=null?DateHelper.formatDate(mainCert.getCertIssueDate(), "dd/MM/yyyy"):"")+"</td>"+
						"</tr>"+
						"<tr class=\"second\">"+
						"<td class=\"T1C1cell01\">Certificate As At Date:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getCertAsAtDate()!=null?DateHelper.formatDate(mainCert.getCertAsAtDate(), "dd/MM/yyyy"):"")+"</td>"+
						"</tr>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">Certificate Due Date:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getCertDueDate()!=null?DateHelper.formatDate(mainCert.getCertDueDate(), "dd/MM/yyyy"):"")+"</td>"+
						"</tr>"+
						"</tbody>"+
						"</table>"+

						"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"<table class=\"simple\" style=\"text-align: left;\" cellpadding=\"2\" cellspacing=\"0\">"+
						"<tbody>"+
						"<tr class=\"first\">"+
						"<td class=\"T1C1cell01\">Remark:</td>"+
						"<td class=\"T1C2cell01\">"+(mainCert.getRemark()!=null?mainCert.getRemark():"")+"</td>"+
						"</tr>"+
						"</tbody>"+
						"</table>"+

						"<br><TABLE border=0><TBODY><TR class=\"separater\"><TD>&nbsp;&nbsp;</TD></TR></TBODY></TABLE>"+
						"</body></html>";
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
	
	public HTMLStringForApprovalContentService() {
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

	public MainCertHBDao getMainCertHBDao() {
		return mainCertHBDao;
	}

	public void setMainCertHBDao(MainCertHBDao mainCertHBDao) {
		this.mainCertHBDao = mainCertHBDao;
	}
	
}
