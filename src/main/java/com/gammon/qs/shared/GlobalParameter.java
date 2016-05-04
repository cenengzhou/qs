package com.gammon.qs.shared;

import java.util.Map;

import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SystemConstant;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;

public class GlobalParameter {
	public static final String BASED_URL = GWT.getModuleBaseURL(); 
	
	
	
	//URL 
	public static final String ACCOUNT_BALANCE_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountBalanceExcelDownload.smvc";
	public static final String ACCOUNT_BALANCE_PDF_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountBalancePDFDownload.smvc";
	public static final String IV_POSTING_HISTORY_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "ivPostingHistoryExcelDownload.smvc";
	public static final String SC_PROVISION_HISTORY_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "scProvisionHistoryExcelDownload.smvc";
	public static final String REPACKAGING_DETAIL_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "repackagingDetailExcelDownload.smvc";
	public static final String WORK_SCOPE_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "workScopeExcelDownload.smvc";
	public static final String ACCOUNT_CODE_OBJECT_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountCodeObjectExcelDownload.rpt";
	public static final String ACCOUNT_CODE_SUBSIDIARY_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountCodeSubsidiaryExcelDownload.rpt";
	public static final String PERFORMANCE_APPRAISAL_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "performanceAppraisalExcelDownload.smvc";
	public static final String BQ_REPOSITORY_URL = GWT.getModuleBaseURL() + "bq.smvc";
	public static final String FORECAST_REPOSITORY_URL = GWT.getModuleBaseURL() +"forecast.smvc";
	public static final String PAYMENT_REPOSITORY_URL = GWT.getModuleBaseURL() + "payment.smvc";
	public static final String MASTER_LIST_REPOSITORY_URL = GWT.getModuleBaseURL() + "masterList.smvc";
	public static final String JOB_REPOSITORY_URL = GWT.getModuleBaseURL() + "job.smvc";
	public static final String PACKAGE_REPOSITORY_URL = GWT.getModuleBaseURL() + "package.smvc";
	public static final String FORECAST_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "forecastExcelDownload.smvc";
	public static final String FORECAST_TEMPLATE_DOWNLOAD_URL = GWT.getModuleBaseURL() + "forecastTemplateDownload.smvc";
	public static final String IV_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "ivExcelDownload.smvc";
	public static final String BQ_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "bqExcelDownload.smvc";
	public static final String IV_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "ivExcelUpload.smvc";
	public static final String FORECAST_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "forecastExcelUpload.smvc";
	public static final String SCDETAIL_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "scDetailExcelUpload.smvc";
	public static final String SCLIAB_N_PAYMENT_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "scLiabilitiesAndPaymentDownload.smvc";
	public static final String SCDETAILS_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "scDetailsDownload.smvc";
	public static final String SCPAMENT_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "scPaymentDownload.smvc";
	public static final String SCPAMENTCERT_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "scPaymentCertDownload.smvc";
	public static final String PRINT_UNPAID_PAYMENT_CERTIFICATE_REPORT_PDF = GWT.getModuleBaseURL() + "printUnpaidPaymentCertificateReportPdf.rpt";
	public static final String PRINT_PAYMENT_CERTIFICATE_REPORT_PDF = GWT.getModuleBaseURL() + "printPaymentCertificateReportPdf.rpt";
	public static final String USER_SERVICE_URL = GWT.getModuleBaseURL() + "user.smvc";
	public static final String UPLOAD_SC_ATTACHMENT_URL = GWT.getModuleBaseURL() + "scAttachmentUpload.smvc";
	public static final String DOWNLOAD_SC_ATTACHMENT_URL = GWT.getModuleBaseURL() + "scAttachmentDownload.smvc";
	public static final String SYSTEM_MESSAGE_URL = GWT.getModuleBaseURL()+ "systemMessage.smvc";
	public static final String UNITOFMEASUREMENT_URL = GWT.getModuleBaseURL()+ "unit.smvc";
	public static final String JOBCOST_REPOSITORY_URL = GWT.getModuleBaseURL() + "jobcost.smvc";
	public static final String REPACKAGING_ENTRY_REPOSITORY_URL = GWT.getModuleBaseURL() + "repackagingEntry.smvc";
	public static final String REPACKAGING_DETAIL_REPOSITORY_URL = GWT.getModuleBaseURL() + "repackagingDetail.smvc";
	public static final String REPACKAGING_ATTACHMENT_DOWNLOAD_URL = GWT.getModuleBaseURL() + "repackagingAttachmentDownload.smvc";
	public static final String REPACKAGING_ATTACHMENT_UPLOAD_URL = GWT.getModuleBaseURL() + "repackagingAttachmentUpload.smvc";
	public static final String BQ_RESOURCE_SUMMARY_REPOSITORY_URL = GWT.getModuleBaseURL() + "bqResourceSummary.smvc";
	public static final String BQ_RESOURCE_SUMMARY_IV_DOWNLOAD_URL = GWT.getModuleBaseURL() + "bqResourceSummaryIVDownload.smvc";
	public static final String BQ_RESOURCE_SUMMARY_IV_UPLOAD_URL = GWT.getModuleBaseURL() + "bqResourceSummaryIVUpload.smvc";
	public static final String BQITEM_IV_DOWNLOAD_URL = GWT.getModuleBaseURL() + "bqItemIVDownload.smvc";
	public static final String BQITEM_IV_UPLOAD_URL = GWT.getModuleBaseURL() + "bqItemIVUpload.smvc";
	public static final String RESOURCE_IV_DOWNLOAD_URL = GWT.getModuleBaseURL() + "resourceIVDownload.smvc";
	public static final String RESOURCE_IV_UPLOAD_URL = GWT.getModuleBaseURL() + "resourceIVUpload.smvc";
	public static final String MAINCONTRACTCERTIFICATE_REPOSITORY_URL = GWT.getModuleBaseURL() + "mainContractCertificate.smvc";
	public static final String MAINCERTIFICATE_ATTACHMENT_UPLOAD_URL = GWT.getModuleBaseURL() + "mainCertificateAttachmentUpload.smvc";
	public static final String MAINCERTIFICATE_ATTACHMENT_DOWNLOAD_URL = GWT.getModuleBaseURL() + "mainCertificateAttachmentDownload.smvc";
	public static final String TENDER_ANALYSIS_REPOSITORY_URL = GWT.getModuleBaseURL() + "tenderAnalysis.smvc";
	public static final String TENDER_ANALYSIS_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "tenderAnalysisExcelUpload.smvc";
	public static final String TENDER_ANALYSIS_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "tenderAnalysisExcelDownload.smvc";
	public static final String TENDER_ANALYSIS_VENDOR_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "tenderAnalysisVendorExcelUpload.smvc";
	public static final String TENDER_ANALYSIS_VENDOR_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "tenderAnalysisVendorExcelDownload.smvc";
	public static final String USER_ACCESS_RIGHTS_REPOSITORY_URL = GWT.getModuleBaseURL() +"userAccessRights.smvc";
	public static final String USER_ACCESS_JOBS_REPOSITORY_URL = GWT.getModuleBaseURL() +"userAccessJobs.smvc";
	public static final String SYSTEM_ADMIN_REPOSITORY_URL = GWT.getModuleBaseURL() + "systemAdmin.smvc";
	public static final String ENVIRONMENT_CONFIG_URL = GWT.getModuleBaseURL() + "environmentConfig.smvc";
	public static final String IV_POSTING_HISTORY_REPOSITORY_URL = GWT.getModuleBaseURL() + "ivPostingHistory.smvc";
	public static final String SINGLESIGNONKEY_REPOSITORY_URL = GWT.getModuleBaseURL() +"singleSignOnKey.smvc";
	public static final String FINANCE_SUBCONTRACT_LIST_DOWNLOAD_URL = GWT.getModuleBaseURL()+ "financeSubcontractListDownload.smvc";
	public static final String QRTZ_TRIGGER_REPOSITORY_URL = GWT.getModuleBaseURL()+ "qrtzTrigger.smvc";
	public static final String TRANSIT_REPOSITORY_URL = GWT.getModuleBaseURL() + "transitRepository.smvc";
	public static final String TRANSIT_UPLOAD_URL = GWT.getModuleBaseURL() + "transitUpload.smvc";
	public static final String TRANSIT_DOWNLOAD_URL = GWT.getModuleBaseURL() + "transitDownload.smvc";
	public static final String BUDGET_POSTING_URL = GWT.getModuleBaseURL() + "budgetPostingService.smvc";
	public static final String PRINT_BQ_MASTER_RECONCILIATION_REPORT = GWT.getModuleBaseURL() + "printBQMasterReconciliationReport.smvc";
	public static final String PRINT_BQ_RECOURSE_RECONCILIATION_REPORT = GWT.getModuleBaseURL() + "printBQRecourseReconciliationReport.smvc";
	public static final String ACCOUNT_LEDGER_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountLedgerExcelDownload.smvc";
	public static final String ACCOUNT_LEDGER_PDF_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountLedgerPDFDownload.smvc";
	public static final String ACCOUNT_CUSTOMER_LEDGER_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountCustomerLedgerExcelDownload.smvc";
	public static final String ACCOUNT_CUSTOMER_LEDGER_PDF_DOWNLOAD_URL = GWT.getModuleBaseURL() + "accountCustomerLedgerPDFDownload.smvc";
	public static final String RETENTION_RELEASE_SCHEDULE_REPOSITORY_URL = GWT.getModuleBaseURL() + "retentionReleaseSchedule.smvc";;
	public static final String RETENTION_RELEASE_SCHEDULE_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL()+"retentionReleaseScheduleExcelDownload.smvc";
	public static final String RETENTION_RELEASE_SCHEDULE_REPORT_DOWNLOAD_URL = GWT.getModuleBaseURL()+"retentionReleaseScheduleReportDownload.rpt";
	public static final String ACCOUNT_PAYABLE_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL()+"accountPayableExcelDownload.smvc";
	public static final String ACCOUNT_PAYABLE_PDF_DOWNLOAD_URL = GWT.getModuleBaseURL()+"accountPayablePDFDownload.smvc";
	public static final String SCDETAILS_EXCEL_DOWNLOAD_FOR_JOB_URL = GWT.getModuleBaseURL()+"scDetailForJobDownload.smvc";
	public static final String UI_ERROR_MESSAGE_LOG_URL = GWT.getModuleBaseURL()+"uiErrorMessageLog.smvc";
	public static final String SC_PAYMENT_EXCEPTION_REPORT_DOWNLOAD_URL = GWT.getModuleBaseURL()+"scPaymentExceptionReportDownload.smvc";
	public static final String ACCOUNT_LEDGER_URL = GWT.getModuleBaseURL()+"accountLedger.smvc";
	public static final String APPLICATION_CONTEXT_PROPERTIES_URL = GWT.getModuleBaseURL()+"properties.smvc";
	public static final String BUDGET_FORECAST_EXCEL_UPLOAD_URL = GWT.getModuleBaseURL() + "budgetForecastExcelUpload.smvc";
	public static final String BUDGET_FORECAST_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "budgetForecastExcelDownload.smvc";
	public static final String SUBCONTRACTOR_REPOSITORY_URL = GWT.getModuleBaseURL() + "subcontractor.smvc";

	public static final String PAYMENT_CERTIFICATE_ENQUIRY_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "paymentCertificateEnquiryExcelExport.smvc";

	public static final String CONTRA_CHARGE_ENQUIRY_REPORT_PDF_URL = GWT.getModuleBaseURL() + "contraChargeEnquiryReportPDFExport.rpt";
	public static final String CONTRA_CHARGE_ENQUIRY_REPORT_EXCEL_URL = GWT.getModuleBaseURL() + "contraChargeEnquiryReportExcelExport.rpt";

	public static final String MAIN_CERTIFICATE_ENQUIRY_REPORT_EXCEL_URL = GWT.getModuleBaseURL() + "mainCertificateEnquiryReportExcelExport.smvc";
	public static final String MAIN_CERTIFICATE_ENQUIRY_REPORT_PDF_URL = GWT.getModuleBaseURL() + "mainCertificateEnquiryReportPDFExport.rpt";
	public static final String MAIN_CERTIFICATE_ENQUIRY_EXCEL_URL = GWT.getModuleBaseURL() + "mainCertificateEnquiryExcelExport.smvc";
	public static final String MAIN_CERTIFICATE_ENQUIRY_PDF_URL = GWT.getModuleBaseURL() + "mainCertificateEnquiryPDFExport.rpt";

	public static final String MAIN_CERTIFICATE_ENQUIRY_URL = GWT.getModuleBaseURL() + "mainContractCertificateEnquiryExport.rpt";
	public static final String CONTRACT_RECEIVABLE_ENQUIRY_URL = GWT.getModuleBaseURL() + "contractReceivableEnquiry.rpt";
	
	public static final String MESSAGE_BOARD_REPOSITORY_URL = GWT.getModuleBaseURL()+"messageBoard.smvc";
	public static final String MESSAGE_BOARD_ATTACH_REPOSITORY_URL = GWT.getModuleBaseURL()+"messageBoardAttachment.smvc";
	public static final String ATTACHMENT_REPOSITORY_URL = GWT.getModuleBaseURL()+"attachment.smvc";
	public static final String SCPAYMENTCERT_REPOSITORY_URL = GWT.getModuleBaseURL()+"scpaymentcert.smvc";
	public static final String SCWORKSCOPE_REPOSITORY_URL = GWT.getModuleBaseURL()+"scworkscope.smvc";
	public static final String PAGE_REPOSITORY_URL = GWT.getModuleBaseURL()+"page.smvc";
	public static final String RESOURCE_REPOSITORY_URL = GWT.getModuleBaseURL()+"resource.smvc";
	public static final String TENDERANALYSISDETAIL_REPOSITORY_URL = GWT.getModuleBaseURL()+"tenderanalysisdetail.smvc";
	public static final String MAINCERTIFICATEATTACHMENT_REPOSITORY_URL = GWT.getModuleBaseURL()+"maincertificateattachment.smvc";
	public static final String SUBCONTRACT_PAYMENT_ENQUIRY_EXCEL_DOWNLOAD_URL = GWT.getModuleBaseURL() + "subcontractPaymentExcelDownload.smvc";
	
	//Report
	public static final String SUBCONTRACTOR_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractorExcelExport.rpt";
	public static final String AWARDED_SUBCONTRACTS_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "awardedSubcontractsExcelExport.rpt";

	/**
	 * @author added by heisonwong for purchase order
	 * **/
	public static final String PURCHASE_ORDER_EXCEL_URL = GWT.getModuleBaseURL()+ "purchaseOrderExcelExport.rpt";
	public static final String PURCHASE_ORDER_REPORT_URL = GWT.getModuleBaseURL()+ "purchaseOrderReportExport.rpt";
	
	public static final String SUBCONTRACT_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractExcelExport.rpt";
	public static final String SUBCONTRACT_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractReportExport.rpt";
	public static final String SUBCONTRACT_LIABILITY_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractLiabilityExcelExport.rpt";
	public static final String SUBCONTRACT_LIABILITY_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractLiabilityReportExport.rpt";
	public static final String SUBCONTRACTOR_ANALYSIS_EXCEL_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractorAnalysisExcelExport.rpt";
	public static final String SUBCONTRACTOR_ANALYSIS_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "subcontractorAnalysisReportExport.rpt";
	public static final String ACCOUNT_BALANCE_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "accountBalanceReportExport.rpt";
	public static final String ACCOUNT_LEDGER_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "accountLedgerReportExport.rpt";
	public static final String ACCOUNT_PAYABLE_REPORT_EXPORT_URL = GWT.getModuleBaseURL()+"accountPayableReportExport.rpt";
	public static final String ACCOUNT_CUSTOMER_LEDGER_REPORT_EXPORT_URL = GWT.getModuleBaseURL() + "accountCustomerLedgerReportExport.rpt";
	
	//Image Upload
	public static final String IMAGE_UPLOAD_URL = GWT.getModuleBaseURL()+"imageUpload.smvc";
	//Image Download
	public static final String IMAGE_DOWNLOAD_URL = GWT.getModuleBaseURL()+"imageDownload.smvc";

	
	/**
	 * @author matthewatc
	 * 16:52:46 29 Dec 2011 (UTC+8)
	 * added URL for the object subsidiary rule repository
	 */
	public static final String OBJ_SUB_REPOSITORY_URL =  GWT.getModuleBaseURL() + "objectSubsidiaryRule.smvc";
	
	//UI config
	public static final String NULL_REPLACEMENT = "N/A";
	public static final String SEARCHING_MSG = "Searching...";
	public static final String LOADING_MSG = "Loading...";
	public static final String POSTING_MSG = "Posting...";
	public static final String SAVING_MSG = "Saving...";
	public static final String CALCULATING_MSG ="Calculating...";
	public static final String RECALCULATING_MSG = "Recalculating...";
	public static final String RESULT_OVERFLOW_MESSAGE = "The search result is more than 100 rows, please specifiy more searching criteria.";
	public static final String RESULT_OVERFLOW_800ROWS_MESSAGE = "The search result is more than 800 rows, please specifiy more searching criteria.";
	public static final String APPROVAL_LIST_OVERFLOW_800ROWS_MESSAGE = "Approval list is more than 800 rows, please specifiy subcontract number";
	
	//Parameter
	public static final String MAIN_SECTION_PANEL_ID = "main-section-panel";
	public static final String TREEVIEW_SECTION_PANEL_ID = "treeview-section-panel";
	public static final String DETAIL_SECTION_PANEL_ID ="detail-section-panel";
	public static final String MAIN_PANEL_ID = "main-panel";
	public static final String MASTER_LIST_TAB_PANEL_ID= "masterListTabPanel";
	
	//Setting	
	public final static String DATE_FORMAT = "dd/MM/yyyy";
    public final static String DATETIME_FORMAT = "dd/MM/yyyy hh:mm";
    /**
     *  for UI 
     */
    public final static String DATEFIELD_DATEFORMAT = "d/m/Y";
	public static final String DATEFIELD_ALTDATEFORMAT = "dmY";
    
	//COLOR
	public static final String RED_COLOR = "#FF0000";
	public static final String ORANGE_COLOR = "#e68550";
	public static final String GREEN_COLOR = "#007D00";
	public static final String GREY_COLOR = "#707070";
	
	
    // ROLE
    public final static String ROLE_READ = "ROLE_READ";
    public final static String ROLE_WRITE = "ROLE_WRITE";
    
    //PAGINATION
    public final static int PAGE_SIZE = 100;
    
    //RETRY INTERVERAL
    public final static long RETRY_INTERVERAL = 30000;
    
    //Error Message of Validate Cost Type and Cost Code
    public final static String ERROR_OBJ_SUB = "Invalid Combination of Cost Code and Cost Type";
	
    //Transit Import Types
    public final static String TRANSIT_BQ = "BQ";
    public final static String TRANSIT_RESOURCE = "Resource";
    public final static String TRANSIT_CODE_MATCHING = "Resource Code Matching";
    public final static String TRANSIT_UOM_MATCHING = "Unit Code Matching";
    public final static String TRANSIT_ERROR = "TERROR";
    public final static String TRANSIT_SUCCESS_WITH_WARNING = "SUCCESS_WITH_WARNING"; // added by brian on 20110225

	public final static Store SC_PAYMENT_TERMS_STORE = new SimpleStore(new String[]{"termsValue", "termsDisplay"}, getPaymentTerms(false));
	
	public static String[][] getContraChargeLineType(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"","All"},
					new String[]{"C1","C1"},
					new String[]{"C2","C2"},
		};
		else
		return new String[][]{
				new String[]{"C1","C1"},
				new String[]{"C2","C2"},
		};
	}

	
	public static String[][] getPaymentTerms(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",SystemConstant.CONST_ALL},
					new String[]{"QS0", "QS0 - Manual Input Due Date"},
					new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
					new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
					new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
					new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
					new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
					new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
					new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};	
		else
		return new String[][]{
				new String[]{"QS0", "QS0 - Manual Input Due Date"},
				new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
				new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
				new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
				new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
				new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
				new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
				new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};
	}
	
	public static String[][] getRetentionType(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",SystemConstant.CONST_ALL},
					new String[]{SCPackage.RETENTION_LUMPSUM,SCPackage.RETENTION_LUMPSUM},
					new String[]{SCPackage.RETENTION_ORIGINAL,SCPackage.RETENTION_ORIGINAL},
					new String[]{SCPackage.RETENTION_REVISED,SCPackage.RETENTION_REVISED}
		};
		else
		return new String[][]{
				new String[]{SCPackage.RETENTION_LUMPSUM,SCPackage.RETENTION_LUMPSUM},
				new String[]{SCPackage.RETENTION_ORIGINAL,SCPackage.RETENTION_ORIGINAL},
				new String[]{SCPackage.RETENTION_REVISED,SCPackage.RETENTION_REVISED}
		};
	}

	public static String[][] getReviewedByFinance(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",SystemConstant.CONST_ALL},
					new String[]{SystemConstant.FINQS0REVIEW_Y,SystemConstant.FINQS0REVIEW_Y},
					new String[]{SystemConstant.FINQS0REVIEW_N,SystemConstant.FINQS0REVIEW_N}
		};
		else
		return new String[][]{
				new String[]{SystemConstant.FINQS0REVIEW_Y,SystemConstant.FINQS0REVIEW_Y},
				new String[]{SystemConstant.FINQS0REVIEW_N,SystemConstant.FINQS0REVIEW_N}
		};
	}

	/*
	public final static String[][] getTerms(){
		return new String[][]{
				new String[]{"QS0", "QS0 - Manual Input Due Date"},
				new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
				new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
				new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
				new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
				new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
				new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
				new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};
	}
	*/

	public final static Store RETENTION_TERMS_STORE = new SimpleStore(new String[]{"termsValue","termDescription"},
		new String [][]{
				new String[]{"", "No Retention"},
				new String[]{SCPackage.RETENTION_LUMPSUM,SCPackage.RETENTION_LUMPSUM},
				new String[]{SCPackage.RETENTION_ORIGINAL,SCPackage.RETENTION_ORIGINAL},
				new String[]{SCPackage.RETENTION_REVISED,SCPackage.RETENTION_REVISED}
		}
	);
	
	public final static Store FORM_OF_SUBCONTRACT_STORE = new SimpleStore(new String[]{"value","description"},
		new String[][]{
			new String[]{SCPackage.MAJOR,SCPackage.MAJOR},
			new String[]{SCPackage.MINOR,SCPackage.MINOR},
			new String[]{SCPackage.CONSULTANCY_AGREEMENT,SCPackage.CONSULTANCY_AGREEMENT},
			new String[]{SCPackage.INTERNAL_TRADING,SCPackage.INTERNAL_TRADING}
		}
	);

	public final static Store SCPAYMENTCERT_INTRIM_FINAL_PAYMENT_STORE = new SimpleStore(new String[]{"value","description"},
		new String[][]{
			new String[]{"I",SCPaymentCert.INTERIM_PAYMENT},
			new String[]{"F",SCPaymentCert.FINAL_PAYMENT}
		}
	);
	public final static Store SCPAYMENTCERT_DIRECTPAYMENT_STORE = new SimpleStore(new String[]{"value","description"},
			new String[][]{
				new String[]{SCPaymentCert.DIRECT_PAYMENT,"Direct Payment"},
				new String[]{SCPaymentCert.NON_DIRECT_PAYMENT,"Normal Payment"
			}
		}
	);
	private static Map<String, String> performanceGroupMap;

	public static Map<String, String> getPerformanceGroupMap() {
		return performanceGroupMap;
	}

	public static void setPerformanceGroupMap(Map<String, String> performanceGroupMapArgument) {
		performanceGroupMap = performanceGroupMapArgument;
	}
	
	private static Map<String, String> scStatusCodeMap;

	public static Map<String, String> getScStatusCodeMap() {
		return scStatusCodeMap;
	}

	public static void setScStatusCodeMap(Map<String, String> scStatusCodeMap) {
		GlobalParameter.scStatusCodeMap = scStatusCodeMap;
	}
	
	public final static Store DIRECTPAYMENT_EXCEPTIONAL_ENQUIRY_SC_STATUS_STORE = new SimpleStore(new String[]{"value","description"},
			new String[][]{
			new String[]{"all","All Status"},
			new String[]{"awarded","Awarded SC [Status:500]"},
			new String[]{"nonawarded","Non-awarded SC [Status:0,100,160,330,340]"},
			new String[]{"submitted","Submitted SC Award [Status:330]"},
			new String[]{"notsubmitted","Not Submitted SC Award Yet [Status:0,100,160,340]"}
		}
	);   
	
	public final static Store MAIN_CERTIFICATE_STATUS_STORE = new SimpleStore(new String[]{"statusValue", "statusDisplay"}, getMainCertficateStatuses());
	
	public final static String[][] getMainCertficateStatuses(){
		return new String[][]{
				new String[]{MainContractCertificate.CERT_CREATED, 					MainContractCertificate.CERT_CREATED_DESC},
				new String[]{MainContractCertificate.IPA_SENT, 						MainContractCertificate.IPA_SENT_DESC},
				new String[]{MainContractCertificate.CERT_CONFIRMED, 				MainContractCertificate.CERT_CONFIRMED_DESC},
				new String[]{MainContractCertificate.CERT_WAITING_FOR_APPROVAL, 	MainContractCertificate.CERT_WAITING_FOR_APPROVAL_DESC},
				new String[]{MainContractCertificate.CERT_POSTED, 					MainContractCertificate.CERT_POSTED_DESC}
		};
	}
	
	public final static String[][] getMainCertficateStatusesFull(boolean withOptions){
		if(withOptions){
			return new String[][]{
					new String[]{"", 													"All"},
					new String[]{MainContractCertificate.CERT_CREATED, 					MainContractCertificate.CERT_CREATED_DESC},
					new String[]{MainContractCertificate.IPA_SENT, 						MainContractCertificate.IPA_SENT_DESC},
					new String[]{MainContractCertificate.CERT_CONFIRMED, 				MainContractCertificate.CERT_CONFIRMED_DESC},
					new String[]{MainContractCertificate.CERT_WAITING_FOR_APPROVAL, 	MainContractCertificate.CERT_WAITING_FOR_APPROVAL_DESC},
					new String[]{MainContractCertificate.CERT_POSTED, 					MainContractCertificate.CERT_POSTED_DESC},
					new String[]{"400", 												"Payment Received"}
			};
		}
		else
			return new String[][]{
					new String[]{MainContractCertificate.CERT_CREATED, 					MainContractCertificate.CERT_CREATED_DESC},
					new String[]{MainContractCertificate.IPA_SENT, 						MainContractCertificate.IPA_SENT_DESC},
					new String[]{MainContractCertificate.CERT_CONFIRMED, 				MainContractCertificate.CERT_CONFIRMED_DESC},
					new String[]{MainContractCertificate.CERT_WAITING_FOR_APPROVAL, 	MainContractCertificate.CERT_WAITING_FOR_APPROVAL_DESC},
					new String[]{MainContractCertificate.CERT_POSTED, 					MainContractCertificate.CERT_POSTED_DESC},
					new String[]{"400", 												"Payment Received"}
			};
	}
	
	 /*
     * GWT-EXT Date format (i.e. DateField of Panels, Windows etc)
     * 
    Format  Description                                                               Example returned values
    ------  -----------------------------------------------------------------------   -----------------------
      d     Day of the month, 2 digits with leading zeros                             01 to 31
      D     A short textual representation of the day of the week                     Mon to Sun
      j     Day of the month without leading zeros                                    1 to 31
      l     A full textual representation of the day of the week                      Sunday to Saturday
      N     ISO-8601 numeric representation of the day of the week                    1 (for Monday) through 7 (for Sunday)
      S     English ordinal suffix for the day of the month, 2 characters             st, nd, rd or th. Works well with j
      w     Numeric representation of the day of the week                             0 (for Sunday) to 6 (for Saturday)
      z     The day of the year (starting from 0)                                     0 to 364 (365 in leap years)
      W     ISO-8601 week number of year, weeks starting on Monday                    01 to 53
      F     A full textual representation of a month, such as January or March        January to December
      m     Numeric representation of a month, with leading zeros                     01 to 12
      M     A short textual representation of a month                                 Jan to Dec
      n     Numeric representation of a month, without leading zeros                  1 to 12
      t     Number of days in the given month                                         28 to 31
      L     Whether it's a leap year                                                  1 if it is a leap year, 0 otherwise.
      o     ISO-8601 year number (identical to (Y), but if the ISO week number (W)    Examples: 1998 or 2004
            belongs to the previous or next year, that year is used instead)
      Y     A full numeric representation of a year, 4 digits                         Examples: 1999 or 2003
      y     A two digit representation of a year                                      Examples: 99 or 03
      a     Lowercase Ante meridiem and Post meridiem                                 am or pm
      A     Uppercase Ante meridiem and Post meridiem                                 AM or PM
      g     12-hour format of an hour without leading zeros                           1 to 12
      G     24-hour format of an hour without leading zeros                           0 to 23
      h     12-hour format of an hour with leading zeros                              01 to 12
      H     24-hour format of an hour with leading zeros                              00 to 23
      i     Minutes, with leading zeros                                               00 to 59
      s     Seconds, with leading zeros                                               00 to 59
      u     Milliseconds, with leading zeros                                          001 to 999
      O     Difference to Greenwich time (GMT) in hours and minutes                   Example: +1030
      P     Difference to Greenwich time (GMT) with colon between hours and minutes   Example: -08:00
      T     Timezone abbreviation of the machine running the code                     Examples: EST, MDT, PDT ...
      Z     Timezone offset in seconds (negative if west of UTC, positive if east)    -43200 to 50400
      c     ISO 8601 date                                                             2007-04-17T15:19:21+08:00
      U     Seconds since the Unix Epoch (January 1 1970 00:00:00 GMT)                1193432466 or -2138434463
      
      
      JAVA - java.text.SimpleDateFormat
		Letter	Date or Time Component		Presentation		Examples
		G		Era designator				Text				AD
		y		Year						Year				1996; 96
		M		Month in year				Month				July; Jul; 07
		w		Week in year				Number				27
		W		Week in month				Number				2
		D		Day in year					Number				189
		d		Day in month				Number				10
		F		Day of week in month		Number				2
		E		Day in week					Text				Tuesday; Tue
		a		Am/pm marker				Text				PM
		H		Hour in day (0-23)			Number				0
		k		Hour in day (1-24)			Number				24
		K		Hour in am/pm (0-11)		Number				0
		h		Hour in am/pm (1-12)		Number				12
		m		Minute in hour				Number				30
		s		Second in minute			Number				55
		S		Millisecond					Number				978
		z		Time zone					General time zone	Pacific Standard Time; PST; GMT-08:00
		Z		Time zone					RFC 822 time zone	-0800
      */
    
    /*
     * GWT - org.gwtwidgets.client.util.SimpleDateFormat
     * 
     	TOKEN_DAY_OF_WEEK = "E";
		TOKEN_DAY_OF_MONTH = "d";
		TOKEN_MONTH = "M";
		TOKEN_YEAR = "y";
		TOKEN_HOUR_12 = "h";
		TOKEN_HOUR_24 = "H";
		TOKEN_MINUTE = "m";
		TOKEN_SECOND = "s";
		TOKEN_MILLISECOND = "S";
		TOKEN_AM_PM = "a";
     */
}
