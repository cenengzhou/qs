package com.gammon.pcms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.pcms.config.FreemarkerConfig;
import com.gammon.pcms.config.ServletConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.pcms.helper.FileHelper;
import com.gammon.pcms.helper.FreeMarkerHelper;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.pcms.model.Personnel;
import com.gammon.pcms.model.PersonnelMap;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.model.adl.AddressBook;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AccountCodeWSDao;
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
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.dao.UnitWSDao;
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
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
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
	private SubcontractDetailHBDao subcontractDetailHBDao;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentCertHBDao paymentCertHBDao;
	@Autowired
	private	TenderHBDao tenderHBDao;
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
	private AccountCodeWSDao accountCodeDao;
	@Autowired
	private UnitWSDao unitDao;
	@Autowired
	private FreemarkerConfig freemarkerConfig;
	@Autowired
	private ServletConfig servletConfig;
	@Autowired
	private ADLService adlService;
	@Autowired
	private AttachmentConfig attachmentConfig;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private PersonnelService personnelService;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	public String makeHTMLStringForSCPaymentCert(String jobNumber, String subcontractNumber, String paymentNo, String htmlVersion) throws Exception{
		String strHTMLCodingContent = "";
		JobInfo job = new JobInfo();
		Subcontract scPackage = new Subcontract();
		Double clientCertAmount = new Double(0);
		PaymentCertViewWrapper paymentCertViewWrapper = new PaymentCertViewWrapper();
		List<PaymentCert> scPaymentCertList = null;
		MasterListVendor masterList = new MasterListVendor();

		String strIpaOrInvoiceReceivedDate = null;
		String strCertIssueDate = null;
		Double postedIVAmt = new Double(0);
		Double maxRetentionAmount = new Double(0);
		
		int mainCertNumber = 0;
		int currentPaymentNo = 0;
		PaymentCert paymentCert = null;
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
			paymentCert = paymentService.obtainPaymentCertificate(jobNumber, subcontractNumber, new Integer(currentPaymentNo));
			logger.info("Job No.: "+jobNumber+"- Package No.: "+subcontractNumber+"- Payment No.: "+currentPaymentNo);
			paymentCertViewWrapper = paymentService.getSCPaymentCertSummaryWrapper(jobNumber, subcontractNumber, String.valueOf(currentPaymentNo));
			
			mainCertNumber 		= paymentCertViewWrapper.getMainCertNo();
			strIpaOrInvoiceReceivedDate = DateHelper.formatDate(paymentCert.getIpaOrInvoiceReceivedDate(), GlobalParameter.DATE_FORMAT);
			strCertIssueDate = DateHelper.formatDate(paymentCert.getCertIssueDate(), GlobalParameter.DATE_FORMAT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			scPackage = subcontractHBDao.obtainSCPackage(paymentCertViewWrapper.getJobNumber(), paymentCertViewWrapper.getSubContractNo().toString());
			maxRetentionAmount = CalculationUtil.round(scPackage.getRetentionAmount().doubleValue(), 2);
			if(mainCertNumber != 0)
				clientCertAmount = mainCertWSDao.obtainParentMainContractCertificate(jobNumber, mainCertNumber).getAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<ResourceSummary> resourceList = new ArrayList<ResourceSummary>();
			resourceList = resourceSummaryHBDao.getResourceSummariesSearch(job, subcontractNumber, "14*", null);
			
			for (int i=0; i<resourceList.size(); i++) {
					postedIVAmt += resourceList.get(i).getPostedIVAmount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String strPaymentStatus = "";
		if(paymentCert == null || paymentCert.getPaymentStatus() == null){
			throw new NullPointerException("paymentCert " + paymentCert.getId() + " payment status is null");
		}
		switch(paymentCert.getPaymentStatus()){
		case "PND":
			strPaymentStatus = "Pending";
			break;
		case "SBM":
			strPaymentStatus = "Submitted";
			break;
		case "UFR":
			strPaymentStatus = "Under Finance Review";
			break;
		case "PCS":
			strPaymentStatus = "Waiting For Posting";
			break;
		case "APR":
			strPaymentStatus = "Posted To Finance";
			break;
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		String template = freemarkerConfig.getTemplates().get("payment");
		data.put("template", template);
		data.put("logo", freemarkerConfig.getPaths("logo"));
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("scPackage", scPackage != null ? scPackage : new Subcontract());
		data.put("paymentCert", paymentCert != null ? paymentCert : new PaymentCert());
		data.put("paymentCertViewWrapper", paymentCertViewWrapper != null ? paymentCertViewWrapper : new PaymentCertViewWrapper());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("clientCertAmount", clientCertAmount != null ? clientCertAmount : new Double(0));
		data.put("scPaymentCertList", scPaymentCertList != null ? scPaymentCertList : new ArrayList<>());
		data.put("postedIVAmt", postedIVAmt != null ? postedIVAmt : new Double(0));
		data.put("mainCertNumber", mainCertNumber);
		data.put("strIpaOrInvoiceReceivedDate", strIpaOrInvoiceReceivedDate != null ? strIpaOrInvoiceReceivedDate : "");
		data.put("strCertIssueDate", strCertIssueDate != null ? strCertIssueDate : "");
		data.put("strPaymentStatus", strPaymentStatus);
		data.put("currentPaymentNo", currentPaymentNo);
		data.put("maxRetentionAmount", maxRetentionAmount);
		
		
		strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(template, data);
			
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
		String companyCurrencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		
		String workScopeDescription = "";
		if(subcontract !=null && subcontract.getWorkscope() !=null){
			UDC workScope = unitDao.obtainWorkScope(subcontract.getWorkscope().toString());
			workScopeDescription = workScope.getCode().concat(" - ").concat(workScope.getDescription());
		}
		
		String paymentTerms = PaymentCert.PAYMENT_TERMS_DESCRIPTION.get(subcontract.getPaymentTerms());
		
		Map<String, Object> data = new HashMap<String, Object>();
		String template = freemarkerConfig.getTemplates().get("award");
		data.put("template", template);
		data.put("logo", freemarkerConfig.getPaths("logo"));
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("subcontract", subcontract != null ? subcontract : new Subcontract());
		data.put("budgetTender", budgetTender != null ? budgetTender : new Tender());
		data.put("tenderList", tenderList != null ? tenderList : new ArrayList<>());
		data.put("rcmTenderer", rcmTenderer != null ? rcmTenderer : new Tender());
		data.put("tenderVarianceList", tenderVarianceList != null ? tenderVarianceList : new ArrayList<>());
		data.put("companyCurrencyCode", companyCurrencyCode != null ? companyCurrencyCode: "");
		data.put("workScopeDescription", workScopeDescription);
		data.put("paymentTerms", paymentTerms);
		
		
		
		return FreeMarkerHelper.returnHtmlString(template, data);
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
		String template = freemarkerConfig.getTemplates().get("addendum");
		data.put("template", template);
		data.put("logo", freemarkerConfig.getPaths("logo"));
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("addendum", addendum != null ? addendum : new Addendum());
		data.put("subcontract", subcontract != null ? subcontract : new Subcontract());
		data.put("addendumDetailList", addendumDetailList != null ? addendumDetailList : new ArrayList<>());
		return FreeMarkerHelper.returnHtmlString(template, data);
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
				String currency = accountCodeDao.obtainCurrencyCode(jobNumber);
				AddressBook clientAddressBook = adlService.getAddressBook(new BigDecimal(job.getEmployer()));
				
				MainCert mainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo));
				MainCert previousCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo)-1);
				if(previousCert==null)
					previousCert = new MainCert();
				
				if(mainCert!=null){
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("job", job);
					data.put("currency", currency);
					data.put("clientAddressBook", clientAddressBook != null ? clientAddressBook : new AddressBook());
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
	
	public String receiveVendorName(String addressNumber) throws Exception {
		return masterListDao.getVendorNameList(addressNumber).get(0).getVendorName();
	}
	
	@SuppressWarnings("rawtypes")
	public String eformEmailTemplate(String formCode, String jobNo, List objectList) throws Exception {
		MasterListVendor masterList = null;
		JobInfo job = null;
		job = jobInfoHBDao.obtainJobInfo(jobNo);
		masterList = masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()) == null ?
		new MasterListVendor() :
		masterListDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()).get(0);
		String strHTMLCodingContent = "";
		Map<String, Object> data = new HashMap<String, Object>();
		String template = freemarkerConfig.getTemplates().get(formCode);
		data.put("template", template);
		data.put("logo", freemarkerConfig.getPaths("logo"));
		data.put("baseUrl", servletConfig.getBaseUrl());
		data.put("job", job != null ? job : new JobInfo());
		data.put("companyName", masterList != null ? masterList.getVendorName() : "");
		data.put("objectList", objectList);
		strHTMLCodingContent = FreeMarkerHelper.returnHtmlString(template, data);
			
		return strHTMLCodingContent;
	}
	
	public String getEformBasePath() {
		return attachmentConfig.getAttachmentServer("PATH") + 
				attachmentConfig.getPersonnelDirectory();
	}
	
	public String getEformAttachmentPath(String formCode, Long refNo) {
		return getEformBasePath() + "\\Attachment\\" + formCode + "\\" + refNo;
	}
	
	public String getEformEmailPath(String formCode, Long refNo) {
		return getEformBasePath() + "\\Email\\" + formCode + "\\" + refNo + ".html";
	}
	
	public String getEformPdfPath(String formCode, Long refNo, String jobNo) {
		return getEformAttachmentPath(formCode, refNo) + "\\" + jobNo + " " + webServiceConfig.getWsWfFileName().get(formCode) + " (" + refNo + ").pdf";
	}
	
	public void generateHtmlPdf(String formCode, String jobNo, Long refNo) throws Exception {
		String emailContext = getEmailContext(formCode, jobNo, refNo);
		String emailPathString = getEformEmailPath(formCode, refNo);
		String attachemntDirString = getEformAttachmentPath(formCode, refNo);
		String pdfDest = getEformPdfPath(formCode, refNo, jobNo);
		
		FileHelper.writeStringToFile(emailPathString, emailContext);
		File attachemntDir = new File(attachemntDirString);
		attachemntDir.mkdirs();
		
    	ConverterProperties properties = new ConverterProperties();
    	PdfWriter writer = new PdfWriter(pdfDest);
    	PdfDocument pdf = new PdfDocument(writer);
    	pdf.setDefaultPageSize(new PageSize(400, 14400));
    	Document document = HtmlConverter.convertToDocument(new FileInputStream(emailPathString), pdf, properties);

    	String logoUrl = freemarkerConfig.getPaths("template") +  "img_logo.gif";
        Image image = new Image(ImageDataFactory.create(logoUrl));
        image.scaleAbsolute(154, 41);
        image.setFixedPosition(30, 14345);
        document.add(image);
        
        EndPosition endPosition = new EndPosition();
    	LineSeparator separator = new LineSeparator(endPosition);
    	document.add(separator);
    	document.getRenderer().close();
    	PdfPage page = pdf.getPage(1);
    	float y = endPosition.getY() - 36;
    	page.setMediaBox(new Rectangle(0, y, 595, 14400 - y));

    	document.close();
    	writer.close();
	}
	
	public String getEmailContext(String formCode, String jobNo, Long refNo) throws Exception {
		JobInfo jobInfo = jobNo != null ? jobInfoService.obtainJob(jobNo) : jobInfoService.getByRefNo(refNo);
		List<Personnel> personnelListByJob = personnelService.getActivePersonnel(jobInfo.getJobNumber());
		List<PersonnelMap> allPersonnelMap = personnelService.getAllPersonnelMap();
		Map<BigDecimal, List<Personnel>> map = new TreeMap<>();
		personnelListByJob.forEach(personnel -> {
			List<Personnel> list = map.get(personnel.getPersonnelMap().getUserSequence());
			if(list == null) list = new ArrayList<>();
			list.add(personnel);
			map.put(personnel.getPersonnelMap().getUserSequence(), list);
		});
		allPersonnelMap.forEach(personnelMap -> {
			if(map.get(personnelMap.getUserSequence()) == null) {
				Personnel personnel = new Personnel();
				personnel.setPersonnelMap(personnelMap);
				map.put(personnelMap.getUserSequence(), Arrays.asList(new Personnel[] {personnel}));
			}
		});
		List<Personnel> listForReport = new ArrayList<>();
		map.forEach((key, value) -> {
			listForReport.addAll(value);
		});
		String emailContext = eformEmailTemplate(formCode, jobInfo.getJobNumber(), listForReport);
		return emailContext;
	}
	
    /**
     * Implementation of the ILineDrawer interface that won't draw a line,
     * but that will allow us to get the Y-position at the end of the file.
     */
    class EndPosition implements ILineDrawer {

    	/** A Y-position. */
	    protected float y;
    	
    	/**
	     * Gets the Y-position.
	     *
	     * @return the Y-position
	     */
	    public float getY() {
    		return y;
    	}
    	
		/* (non-Javadoc)
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#draw(com.itextpdf.kernel.pdf.canvas.PdfCanvas, com.itextpdf.kernel.geom.Rectangle)
		 */
		@Override
		public void draw(PdfCanvas pdfCanvas, Rectangle rect) {
			this.y = rect.getY();
		}

		/* (non-Javadoc)
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getColor()
		 */
		@Override
		public Color getColor() {
			return null;
		}

		/* (non-Javadoc)
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getLineWidth()
		 */
		@Override
		public float getLineWidth() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setColor(com.itextpdf.kernel.color.Color)
		 */
		@Override
		public void setColor(Color color) {
		}

		/* (non-Javadoc)
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setLineWidth(float)
		 */
		@Override
		public void setLineWidth(float lineWidth) {
		}
    	
    }
}
