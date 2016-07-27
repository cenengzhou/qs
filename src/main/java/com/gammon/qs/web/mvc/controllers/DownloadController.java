package com.gammon.qs.web.mvc.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.BpiItemService;
import com.gammon.qs.service.IVPostingHistService;
import com.gammon.qs.service.JdeAccountLedgerService;
import com.gammon.qs.service.JobCostService;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.RepackagingDetailService;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.service.SubcontractorService;
import com.gammon.qs.service.TenderService;
import com.gammon.qs.service.transit.TransitService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * koeyyeung
 * Jul 31, 2013 1:46:12 PM
 */
@Controller
public class DownloadController{
	private Logger logger = Logger.getLogger(DownloadController.class.getName());
	
	private String RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM = "application/octet-stream";
	private String RESPONSE_CONTENT_TYPE_APPLICATION_PDF = "application/pdf";
	private String RESPONSE_HEADER_NAME_CONTENT_DISPOSITION = "Content-Disposition";
	
	@Autowired
	private SubcontractorService subcontractorRepository;
	@Autowired
	private SubcontractService packageRepository;
	@Autowired
	private JobCostService jobCostRepository;
	@Autowired
	private BpiItemService bqRepository;
	@Autowired
	private ResourceSummaryService bqResourceSummaryRepository;
	@Autowired
	private JdeAccountLedgerService accountLedgerRepository;
	@Autowired
	private IVPostingHistService ivPostingHistoryRepository;
	@Autowired
	private RepackagingDetailService repackagingDetailRepository;
	@Autowired
	private PaymentService paymentRepository;
	@Autowired
	private TenderService tenderAnalysisRepository;
	@Autowired
	private TransitService transitRepository;
	@Autowired
	private AttachmentService attachmentRepository;
	@Autowired
	private MasterListService masterListRepository;
	
	@RequestMapping(value="/gammonqs/subcontractorExcelExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractorExcel(	@RequestParam(required=true,value="subcontractor") String subcontractor,
											@RequestParam(required=true,value="workScope") String workScope,
											@RequestParam(required=true,value="type") String type,
											HttpServletRequest request, HttpServletResponse response ){
		logger.info("generateSubcontractorExcel");
		try {
			ExcelFile excelFile = subcontractorRepository.subcontractorExcelExport(workScope, subcontractor, type);
			
			if(excelFile!=null){
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
				
				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			}else
				showReportError(response);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			showReportError(response);
		} catch (IOException e) {
			e.printStackTrace();
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/awardedSubcontractsExcelExport.rpt",method=RequestMethod.GET)
		public void generateAwardedSubcontractsExcel(@RequestParam(required=true,value="subcontractorNo") String subcontractorNo,
													@RequestParam(required=true,value="division") String division,
													@RequestParam(required=true,value="jobNumber") String jobNumber,
													@RequestParam(required=true,value="packageNumber") String packageNumber,
													@RequestParam(required=true,value="paymentTerm") String paymentTerm,
													@RequestParam(required=true,value="paymentType") String paymentType,
													HttpServletRequest request, HttpServletResponse response ){
		logger.info("generateAwardedSubcontractsExcel");
		try {
			ExcelFile excelFile = subcontractorRepository.awardedSubcontractsExcelExport(subcontractorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
			
			if(excelFile!=null){
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
				
				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			}else
				showReportError(response);
		
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			showReportError(response);
		} catch (IOException e) {
			e.printStackTrace();
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/subcontractReportExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractReport(@RequestParam(required=true,value="company") String company,
									 @RequestParam(required=true,value="division") String division,
									 @RequestParam(required=true,value="jobNumber") String jobNumber,	
									 @RequestParam(required=true,value="subcontractNumber") String subcontractNumber,
									 @RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
									 @RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
									 @RequestParam(required=true,value="paymentType") String paymentType,
									 @RequestParam(required=true,value="workScope") String workScope,
									 @RequestParam(required=true,value="clientNo") String clientNo,
									 @RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDate,
									 @RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
									 @RequestParam(required=true,value="month") String month,
									 @RequestParam(required=true,value="year") String year,
									 HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSubcontractReport");
		try {
//			ByteArrayOutputStream outputStream = packageRepository.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
//																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractReport");
//			if (outputStream!=null){
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(outputStream.size());
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractReport."+".pdf\"");
//				response.getOutputStream().write(outputStream.toByteArray());
//				response.getOutputStream().flush();
//			}else{
//				logger.info("No file is generated.");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/subcontractLiabilityReportExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractLiabilityReport(@RequestParam(required=true,value="company") String company,
									 @RequestParam(required=true,value="division") String division,
									 @RequestParam(required=true,value="jobNumber") String jobNumber,	
									 @RequestParam(required=true,value="subcontractNumber") String subcontractNumber,
									 @RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
									 @RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
									 @RequestParam(required=true,value="paymentType") String paymentType,
									 @RequestParam(required=true,value="workScope") String workScope,
									 @RequestParam(required=true,value="clientNo") String clientNo,
									 @RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDate,
									 @RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
									 @RequestParam(required=true,value="month") String month,
									 @RequestParam(required=true,value="year") String year,
									 HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSubcontractLiabilityReport");
		try {
//			ByteArrayOutputStream outputStream = packageRepository.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
//																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractLiabilityReport");
//			if (outputStream!=null){
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(outputStream.size());
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractLiabilityReport."+".pdf\"");
//				response.getOutputStream().write(outputStream.toByteArray());
//				response.getOutputStream().flush();
//			}else{
//				logger.info("No file is generated.");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value="/gammonqs/subcontractLiabilityExcelExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractLiabilityExcel(@RequestParam(required=true,value="company") String company,
														@RequestParam(required=true,value="division") String division,
														@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="subcontractNumber") String subcontractNumber,	
														@RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
														@RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
														@RequestParam(required=true,value="paymentType") String paymentType,
														@RequestParam(required=true,value="workScope") String workScope,
														@RequestParam(required=true,value="clientNo") String clientNo,
														@RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDateString,
														@RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
														@RequestParam(required=true,value="month") String month,
														@RequestParam(required=true,value="year") String year,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSubcontractLiabilityExcel");

		try {
//			Boolean includeJobCompletionDate = false;
//			if("true".equals(includeJobCompletionDateString))
//				includeJobCompletionDate = true;
//			
//			ExcelFile excelFile = packageRepository.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
//					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year,"subcontractLiabilityReport");
//
//			if (excelFile != null) {
//				byte[] file = excelFile.getBytes();
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(file.length);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@RequestMapping(value="/gammonqs/subcontractorAnalysisReportExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractorAnalysisReport(@RequestParam(required=true,value="company") String company,
									 @RequestParam(required=true,value="division") String division,
									 @RequestParam(required=true,value="jobNumber") String jobNumber,	
									 @RequestParam(required=true,value="subcontractNumber") String subcontractNumber,
									 @RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
									 @RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
									 @RequestParam(required=true,value="paymentType") String paymentType,
									 @RequestParam(required=true,value="workScope") String workScope,
									 @RequestParam(required=true,value="clientNo") String clientNo,
									 @RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDate,
									 @RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
									 @RequestParam(required=true,value="month") String month,
									 @RequestParam(required=true,value="year") String year,
									 HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSubcontractorAnalysisReport");
		try {
//			ByteArrayOutputStream outputStream = packageRepository.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
//																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractorAnalysisReport");
//			if (outputStream!=null){
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(outputStream.size());
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractorAnalysisReport."+".pdf\"");
//				response.getOutputStream().write(outputStream.toByteArray());
//				response.getOutputStream().flush();
//			}else{
//				logger.info("No file is generated.");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	@RequestMapping(value="/gammonqs/subcontractorAnalysisExcelExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractorAnalysisExcel(@RequestParam(required=true,value="company") String company,
														@RequestParam(required=true,value="division") String division,
														@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="subcontractNumber") String subcontractNumber,	
														@RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
														@RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
														@RequestParam(required=true,value="paymentType") String paymentType,
														@RequestParam(required=true,value="workScope") String workScope,
														@RequestParam(required=true,value="clientNo") String clientNo,
														@RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDateString,
														@RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
														@RequestParam(required=true,value="month") String month,
														@RequestParam(required=true,value="year") String year,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSubcontractorAnalysisExcel");

		try {
//			Boolean includeJobCompletionDate = false;
//			if("true".equals(includeJobCompletionDateString))
//				includeJobCompletionDate = true;
//			
//			ExcelFile excelFile = packageRepository.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
//					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year, "SubcontractorAnalysisReport");
//
//			if (excelFile != null) {
//				byte[] file = excelFile.getBytes();
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(file.length);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@RequestMapping(value="/gammonqs/imageDownload.smvc", method=RequestMethod.GET)
	public void downloadImage(@RequestParam(required=true, value="imageID") String imageID,
							  @RequestParam(required=true, value="filename") String filename,
							  HttpServletRequest request, HttpServletResponse response){
		throw new RuntimeException("remove entity | messageBoardAttachment | remark downloadImage(...)");
		//TODO: remove entity | messageBoardAttachment | remark downloadImage(...) 
//		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
//		String mimeType = mimeTypesMap.getContentType(filename);
//		response.setContentType(mimeType);
//		
//		byte[] fileBytes = messageBoardAttachmentRepository.generateBytesForImage(Long.parseLong(imageID));
//		if(fileBytes!=null){
//			try {
//				response.getOutputStream().write(fileBytes);
//				response.getOutputStream().flush();
//			}
//			catch(IOException e) {
//				e.printStackTrace();
//				logger.info("Error: "+e.getLocalizedMessage());
//				showAttachmentError(response);
//			} finally{
//				try {
//					response.getOutputStream().close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		else{
//			logger.info("No image was found.");
//			showAttachmentError(response);
//			try {
//				response.getOutputStream().close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}		
	}
	

	/**
	 * add by paulyiu 20150730
	 */
	@RequestMapping(value = "/gammonqs/accountCustomerLedgerReportExport.rpt",
					method = RequestMethod.GET)
	public void generateAccountCustomerLedgerReportFile(@RequestParam(required=true,value="jobNumber") String jobNumber,
															@RequestParam(required=true,value="reference") String reference,
															@RequestParam(required=false,value="customerNumber") String customerNumber,
															@RequestParam(required=false,value="documentNumber") String documentNumber,
															@RequestParam(required=false,value="documentType") String documentType,
															@RequestParam(required=false,value="fileType") String fileType,
															HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String jasperReportName = "CustomerLedgerReport";

			ByteArrayOutputStream outputStream = jobCostRepository.downloadAccountCustomerLedgerReportFile(jobNumber, reference, customerNumber, documentNumber, documentType, jasperReportName, fileType);

			if (outputStream != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + "Customer Ledger Report " + DateHelper.formatDate(new Date(), "yyyy-MM-dd HHmmss") + "." + fileType + "\"");
				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			} else {
				logger.info("No file is generated.");
				showReportError(response);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: " + e.getLocalizedMessage());
			showReportError(response);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * add by paulyiu 20150730
	 */
	@RequestMapping(value = "/gammonqs/accountPayableReportExport.rpt",method = RequestMethod.GET)
	public void generateAccountPayableReport(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="subledger") String subLedger,
												@RequestParam(required=true,value="subledgerType") String subLedgerType,
												@RequestParam(required=true,value="invoiceNumber") String invoiceNumber,
												@RequestParam(required=true,value="supplierNumber") String supplierNumber,
												@RequestParam(required=true,value="documentNumber") String documentNumber,
												@RequestParam(required=true,value="documentType") String documentType,
												@RequestParam(required=true,value="fileType") String fileType,
												HttpServletRequest request, HttpServletResponse response) {
		logger.info("generateAccountPayablePDF");

		try {
			String jasperReportName = "SupplierLedgerReport";
			ByteArrayOutputStream outputStream = jobCostRepository.downloadAccountPayableReportFile(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subLedger, subLedgerType, jasperReportName, fileType);

			if (outputStream != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + "Supplier Ledger Report " + DateHelper.formatDate(new Date(), "yyyy-MM-dd HHmmss") + "." + fileType + "\"");
				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			} else {
				logger.info("No file is generated.");
				showReportError(response);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: " + e.getLocalizedMessage());
			showReportError(response);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


//	/**
//	 * @author koeyyeung
//	 * created on 5th Nov, 2015**/
//	@RequestMapping(value="/gammonqs/contractReceivableEnquiry.rpt", method=RequestMethod.GET)
//	public void generateContractReceivableReport(	@RequestParam(required=true,value="jobNo") String jobNo,
//	                                                   	@RequestParam(required=true,value="division") String division,
//	                                                   	@RequestParam(required=true,value="company") String company,
//	                                                   	@RequestParam(required=true,value="status") String status,
//	                                                   	@RequestParam(required=true,value="fileType") String fileType,
//	                                                   	HttpServletRequest request, HttpServletResponse response) {
//		logger.info("generateContractReceivableReport");
//		try {
//			ContractReceivableWrapper wrapper = new ContractReceivableWrapper();
//			wrapper.setCompany(WildCardStringFinder.removeWildCard(company));
//			wrapper.setJobNo(WildCardStringFinder.removeWildCard(jobNo));
//			wrapper.setDivision(WildCardStringFinder.removeWildCard(division));
//			wrapper.setCertStatus(status);
//			ByteArrayOutputStream outputStream = mainContractCertificateRepository.downloadContractReceivableReport(wrapper,fileType);
//
//			if (outputStream != null) {
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + "Contract Receivable Settlement Report " + DateHelper.formatDate(new Date(), "dd-MM-yyyy") + "." + fileType + "\"");
//				response.getOutputStream().write(outputStream.toByteArray());
//				response.getOutputStream().flush();
//			} else {
//				logger.info("No file is generated.");
//				showReportError(response);
//			}
//		} catch (Exception e) {
//			logger.info("No file is generated.");
//			e.printStackTrace();
//			logger.info("Error: " + e.getLocalizedMessage());
//			showReportError(response);
//		} finally {
//			try {
//				response.getOutputStream().close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	@RequestMapping(value="/gammonqs/accountPayableExcelDownload.smvc",method=RequestMethod.GET)
	public void generateAccountPayableExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												 @RequestParam(required=true,value="subledger") String subLedger,
												 @RequestParam(required=true,value="subledgerType") String subLedgerType,	
												 @RequestParam(required=true,value="invoiceNumber") String invoiceNumber,
												 @RequestParam(required=true,value="supplierNumber") String supplierNumber,
												 @RequestParam(required=true,value="documentNumber") String documentNumber,
												 @RequestParam(required=true,value="documentType") String documentType,
												 HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateAccountPayableExcel");

		try {
			
			ExcelFile excelFile = jobCostRepository.downloadAccountPayableExcelFilegetAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subLedger, subLedgerType);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : AccountBalanceExcelDownload - Excel File Error (File is null)");
				showReportError(response);
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/bqExcelDownload.smvc",method=RequestMethod.GET)
	public void generatebqExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
									 @RequestParam(required=true,value="billNo") String billNo,
									 @RequestParam(required=true,value="subBillNo") String subBillNo,	
									 @RequestParam(required=true,value="pageNo") String pageNo,
									 @RequestParam(required=true,value="itemNo") String itemNo,
									 @RequestParam(required=true,value="bqDesc") String bqDesc,
									 HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generatebqExcel");

		try {
			
			ExcelFile excelFile = bqRepository.getBQExcelFileByJob(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDesc);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : AccountBalanceExcelDownload - Excel File Error (File is null)");
				showReportError(response);
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/bqItemIVDownload.smvc",method=RequestMethod.GET)
	public void generatebqItemIVExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
											@RequestParam(required=true,value="billNo") String billNo,
											@RequestParam(required=true,value="subBillNo") String subBillNo,
											@RequestParam(required=true,value="pageNo") String pageNo,
											@RequestParam(required=true,value="itemNo") String itemNo,
											@RequestParam(required=true,value="bqDescription") String bqDescription,
											HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generatebqItemIVExcel");

		try {
			ExcelFile excelFile = bqRepository.downloadBQItemIVExcel(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDescription);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else
				showReportError(response);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/bqResourceSummaryIVDownload.smvc",method=RequestMethod.GET)
	public void generatebqResourceSummaryIVExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
													@RequestParam(required=true,value="packageNo") String packageNo,
													@RequestParam(required=true,value="objectCode") String objectCode,
													@RequestParam(required=true,value="subsidiaryCode") String subsidiaryCode,
													@RequestParam(required=true,value="description") String description,
													@RequestParam(required=true,value="packageList") String packageList,
													HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generatebqResourceSummaryIVExcel");

		try {
			
			ExcelFile excelFile = bqResourceSummaryRepository.downloadIVExcel(jobNumber, packageNo, objectCode, subsidiaryCode, description, packageList);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else
				showReportError(response);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/budgetForecastExcelDownload.smvc",method=RequestMethod.GET)
	public void generateBudgetForecastExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="excelType") String excelType,
												@RequestParam(required=false,value="month") Integer month,
												@RequestParam(required=false,value="year") Integer year,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateBudgetForecastExcel");

		try {
			
			ExcelFile excelFile = accountLedgerRepository.downloadBudgetForecastExcel(jobNumber, excelType, month, year);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else
				showReportError(response);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/financeSubcontractListDownload.smvc",method=RequestMethod.GET)
	public void generateFinanceSubcontractListExcel(@RequestParam(required=true,value="company") String company,
														@RequestParam(required=true,value="division") String division,
														@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="subcontractNumber") String subcontractNumber,	
														@RequestParam(required=true,value="subcontractorNumber") String subcontractorNumber,
														@RequestParam(required=true,value="subcontractorNature") String subcontractorNature,
														@RequestParam(required=true,value="paymentType") String paymentType,
														@RequestParam(required=true,value="workScope") String workScope,
														@RequestParam(required=true,value="clientNo") String clientNo,
														@RequestParam(required=true,value="includeJobCompletionDate") String includeJobCompletionDateString,
														@RequestParam(required=true,value="splitTerminateStatus") String splitTerminateStatus,
														@RequestParam(required=true,value="month") String month,
														@RequestParam(required=true,value="year") String year,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateFinanceSubcontractListExcel");

		try {
//			Boolean includeJobCompletionDate = false;
//			if("true".equals(includeJobCompletionDateString))
//				includeJobCompletionDate = true;
//			
//			ExcelFile excelFile = packageRepository.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
//					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year, "subcontractListReport");
//
//			if (excelFile != null) {
//				byte[] file = excelFile.getBytes();
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(file.length);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
//				showReportError(response);
//			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/ivExcelDownload.smvc",method=RequestMethod.GET)
	public void generateIVExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
									@RequestParam(required=true,value="billNo") String billNo,
									@RequestParam(required=true,value="subBillNo") String subBillNo,	
									@RequestParam(required=true,value="pageNo") String pageNo,
									@RequestParam(required=true,value="itemNo") String itemNo,
									@RequestParam(required=true,value="subsidiaryCode") String subsidiaryCode,
									@RequestParam(required=true,value="objectCode") String objectCode,
									@RequestParam(required=true,value="resDescription") String resDescription,
									@RequestParam(required=true,value="packageNo") String packageNo,
									@RequestParam(required=true,value="packageType") String packageType,
									HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateIVExcel");

		try {
			ResourceWrapper wrapper = new ResourceWrapper();
			wrapper.setJobNumber(jobNumber);
			wrapper.setBillNo(billNo.replace("*", "%"));
			wrapper.setSubBillNo(subBillNo.replace("*", "%"));
			wrapper.setPageNo(pageNo.replace("*", "%"));
			wrapper.setItemNo(itemNo.replace("*", "%"));
			wrapper.setSubsidiaryCode(subsidiaryCode.replace("*", "%"));
			wrapper.setObjectCode(objectCode.replace("*", "%"));
			wrapper.setResDescription(resDescription.replace("*", "%"));
			wrapper.setPackageNo(packageNo.replace("*", "%"));
			wrapper.setPackageType(packageType.replace("*", "%"));
			
			ExcelFile excelFile = bqRepository.getIVResourceExcelFileByJob(wrapper);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : generateIVExcel - Excel File Error (File is Null)");
				showReportError(response);
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/ivPostingHistoryExcelDownload.smvc",method=RequestMethod.GET)
	public void generateIVPostingHistoryExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="packageNo") String packageNo,
														@RequestParam(required=true,value="objectCode") String objectCode,
														@RequestParam(required=true,value="subsidiaryCode") String subsidiaryCode,	
														@RequestParam(required=true,value="fromDate") String fromDateString,
														@RequestParam(required=true,value="toDate") String toDateString,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateIVPostingHistoryExcel");

		try {
			Date fromDate = fromDateString.equals("") ? null : DateHelper.parseDate(fromDateString, "dd/MM/yyyy");
			Date toDate = toDateString.equals("") ? null : DateHelper.parseDate(toDateString, "dd/MM/yyyy");

			ExcelFile excelFile = ivPostingHistoryRepository.downloadIvPostingHistoryExcelFile(jobNumber, packageNo, objectCode, subsidiaryCode, fromDate, toDate);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : IV Posting History Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/performanceAppraisalExcelDownload.smvc",method=RequestMethod.GET)
	public void generatePerformanceAppraisalExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="sequenceNumber") String sequenceNumberString,
														@RequestParam(required=true,value="appraisalType") String appraisalType,
														@RequestParam(required=true,value="groupCode") String groupCode,	
														@RequestParam(required=true,value="vendorNumber") String vendorNumberString,
														@RequestParam(required=true,value="subcontractNumber") String subcontractNumberString,
														@RequestParam(required=true,value="status") String status,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generatePerformanceAppraisalExcel");

		try {
			Integer sequenceNumber = sequenceNumberString.length() > 0 ? Integer.parseInt(sequenceNumberString) : null;
			Integer vendorNumber = vendorNumberString.length() > 0 ? Integer.parseInt(vendorNumberString) : null;
			Integer subcontractNumber = subcontractNumberString.length() > 0 ? Integer.parseInt(subcontractNumberString) : null;
			status = status.length() > 0 ? status : null;
						
			ExcelFile excelFile = packageRepository.downloadPerformanceAppraisalExcelFile(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : Performance Appraisal Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author heisonwong
	 * 	 * 3Aug 2015 14:40
	 * 	 * Change Order type to dropdown list, add Excel and PDF Report
	 * 	 * **/
	@RequestMapping(value="/gammonqs/purchaseOrderReportExport.rpt",method=RequestMethod.GET)
	public void generatePurchaseOrderReport(@RequestParam(required=true,value="jobNumber") String jobNumber,
			@RequestParam(required=true,value="supplierNumber") String supplierNumber,
			@RequestParam(required=true,value="orderNumber") String orderNumber,
			@RequestParam(required=true,value="orderType") String orderType,	
			@RequestParam(required=true,value="lineDescription") String lineDescription,
			@RequestParam(required=true,value="reportName") String reportName,			
			HttpServletRequest request, HttpServletResponse response ){
		logger.info("GeneratePurchaseOrderReportExport");
		try {
			ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jobCostRepository.downloadPurchaseOrderReportFile(jobNumber,
					supplierNumber,orderNumber,orderType,lineDescription,reportName);
			if (outputStream!=null){
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"PurchaseOrderReport."+".pdf\"");
				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			}else{
				logger.info("No file is generated.");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//added by heisonwong
	@RequestMapping(value="/gammonqs/purchaseOrderExcelExport.rpt",method=RequestMethod.GET)
	public void generatePurchaseOrderExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
			@RequestParam(required=true,value="supplierNumber") String supplierNumber,
			@RequestParam(required=true,value="orderNumber") String orderNumber,
			@RequestParam(required=true,value="orderType") String orderType,	
			@RequestParam(required=true,value="lineDescription") String lineDescription,
			@RequestParam(required=true,value="reportName") String reportName,			
			HttpServletRequest request, HttpServletResponse response ){
		logger.info("GeneratePurchaseOrderExcelExport");
		try {
			ExcelFile excelFile = (ExcelFile) jobCostRepository.downloadPurchaseOrderExcelFile(jobNumber,
					supplierNumber,orderNumber,orderType,lineDescription,reportName);
			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : generatePurChaseOrderListExcel - Excel File Error (File is Null)");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	@RequestMapping(value="/gammonqs/repackagingDetailExcelDownload.smvc",method=RequestMethod.GET)
	public void generateRepackagingDetailExcel(@RequestParam(required=true,value="repackagingEntryId") String repackagingEntryId,
														@RequestParam(required=true,value="packageNo") String packageNo,
														@RequestParam(required=true,value="objectCode") String objectCode,
														@RequestParam(required=true,value="subsidiaryCode") String subsidiaryCode,	
														@RequestParam(required=true,value="changesOnly") String changesOnlyString,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateRepackagingDetailExcel");

		try {
			Boolean changesOnly = Boolean.parseBoolean(changesOnlyString);
			ExcelFile excelFile = repackagingDetailRepository.downloadRepackagingDetailExcelFile(repackagingEntryId, packageNo, objectCode, subsidiaryCode, changesOnly);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : Repackaging Detail Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/resourceIVDownload.smvc",method=RequestMethod.GET)
	public void generateResourceIVExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="billNo") String billNo,
														@RequestParam(required=true,value="subBillNo") String subBillNo,
														@RequestParam(required=true,value="pageNo") String pageNo,
														@RequestParam(required=true,value="itemNo") String itemNo,
														@RequestParam(required=true,value="resourceDescription") String resourceDescription,
														@RequestParam(required=true,value="objectCode") String objectCode,
														@RequestParam(required=true,value="subsidiaryCode") String subsidiaryCode,
														@RequestParam(required=true,value="packageNo") String packageNo,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateResourceIVExcel");

		try {
			ExcelFile excelFile = bqRepository.downloadResourceIVExcel(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : ResourceIVExcelDownload - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scDetailsDownload.smvc",method=RequestMethod.GET)
	public void generateSCDetailsExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
											@RequestParam(required=true,value="packageNumber") String packageNumber,
											@RequestParam(required=true,value="packageType") String packageType,
											HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCDetailsExcel");

		try {
//			ExcelFile excelFile = packageRepository.getSCDetailsExcelFileByJobPackageNoPackageType(jobNumber, packageNumber, packageType);
//
//			if (excelFile != null) {
//				byte[] file = excelFile.getBytes();
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(file.length);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				logger.info("Download Controller : SC Details Excel - Excel File Error (File is null)");
//				showReportError(response);
//			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scDetailForJobDownload.smvc",method=RequestMethod.GET)
	public void generateSCDetailForJobExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCDetailForJobExcel");

		try {
			ExcelFile excelFile = packageRepository.getSCDetailsExcelFileByJob(jobNumber);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : SC Detail For Job Excel - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scLiabilitiesAndPaymentDownload.smvc",method=RequestMethod.GET)
	public void generateSCLiabilitiesAndPaymentExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
														HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCLiabilitiesAndPaymentExcel");

		try {
//			ExcelFile excelFile = packageRepository.downloadSubcontractEnquiryReportExcelFile("","",jobNumber,"", "", "", "", "", "","", false,"","","subcontractListReport");
//
//			if (excelFile != null) {
//				byte[] file = excelFile.getBytes();
//				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
//				response.setContentLength(file.length);
//				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				logger.info("Download Controller : SC Liabilities And Payment Excel - Excel File Error (File is null)");
//				showReportError(response);
//			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scPaymentCertDownload.smvc",method=RequestMethod.GET)
	public void generateSCPaymentCertExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="packageNumber") String packageNumber,
												@RequestParam(required=true,value="paymentCertNo") String paymentCertNo,
												@RequestParam(required=true,value="addendum") String addendum,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCPaymentCertExcel");

		try {
			boolean addendumIncluedBoolean = false;
			if (addendum != null && "Y".equalsIgnoreCase(addendum.trim()))
				addendumIncluedBoolean = true;
			ByteArrayOutputStream outputStream = paymentRepository.getSCPaymentCertViewDetailWrapper(jobNumber, packageNumber, paymentCertNo,addendumIncluedBoolean);

			if (outputStream != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : SC Liabilities And Payment Excel - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/printUnpaidPaymentCertificateReportPdf.rpt",method=RequestMethod.GET)
	public void printUnpaidPaymentCertificateReportPdf(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="company") String company,
												@RequestParam(required=true,value="dueDate") String dueDateString,
												@RequestParam(required=true,value="dueDateType") String dueDateType,
												@RequestParam(required=true,value="supplierNumber") String supplierNumber,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("printUnpaidPaymentCertificateReportPdf");
		try {
			Date dueDate = dueDateString.trim().equals("") ? null : DateHelper.parseDate(dueDateString, "dd/MM/yyyy");
			List<ByteArrayInputStream> outputStreamList = paymentRepository.obtainPaymentCertificatesReport(company, dueDate, jobNumber, dueDateType, supplierNumber);
			
			logger.info("Merge reprot");
			//To merge the report
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			concatPDFs(outputStreamList, output, true);

			if (outputStreamList != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_PDF);
				response.setContentLength(output.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

				response.getOutputStream().write(output.toByteArray());
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : SC Liabilities And Payment Excel - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/printPaymentCertificateReportPdf.rpt",method=RequestMethod.GET)
	public void printPaymentCertificateReportPdf(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="company") String company,
												@RequestParam(required=true,value="dueDate") String dueDateString,
												@RequestParam(required=true,value="dueDateType") String dueDateType,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("printPaymentCertificateReportPdf");
		try {
			Date dueDate = dueDateString.trim().equals("") ? null : DateHelper.parseDate(dueDateString, "dd/MM/yyyy");
			List<ByteArrayInputStream> outputStreamList = paymentRepository.obtainAllPaymentCertificatesReport(company, dueDate, jobNumber, dueDateType);
			
			logger.info("Merge reprot");
			//To merge the report
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			concatPDFs(outputStreamList, output, true);

			if (outputStreamList != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_PDF);
				response.setContentLength(output.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

				response.getOutputStream().write(output.toByteArray());
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : Error in Print Payment Certificate Report");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scPaymentDownload.smvc",method=RequestMethod.GET)
	public void generateSCPaymentExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="packageNumber") String packageNumber,
												@RequestParam(required=true,value="paymentCertNo") String paymentCertNo,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCPaymentExcel");

		try {
			
			ExcelFile excelFile = paymentRepository.getSCPaymentDetailsExcelFile(jobNumber, packageNumber, paymentCertNo);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : SC Payment Excel - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scProvisionHistoryExcelDownload.smvc",method=RequestMethod.GET)
	public void generateSCProvisionHistoryExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="packageNo") String packageNo,
												@RequestParam(required=true,value="postedYear") String postedYear,
												@RequestParam(required=true,value="postedMonth") String postedMonth,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateSCProvisionHistoryExcel");

		try {
			
			ExcelFile excelFile = packageRepository.downloadScProvisionHistoryExcelFile(jobNumber, packageNo, postedYear, postedMonth);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : SC Provision History Excel Download - Excel File Error (File is null)");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/tenderAnalysisExcelDownload.smvc",method=RequestMethod.GET)
	public void generateTenderAnalysisExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
												@RequestParam(required=true,value="packageNo") String packageNo,
												HttpServletRequest request, HttpServletResponse response ){		
		
		logger.info("generateTenderAnalysisExcel");

		try {
			
			ExcelFile excelFile = bqResourceSummaryRepository.downloadTenderAnalysisBaseDetailsExcel(jobNumber, packageNo);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/tenderAnalysisVendorExcelDownload.smvc",method=RequestMethod.GET)
	public void generateTenderAnalysisVendorExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
														@RequestParam(required=true,value="packageNo") String packageNo,
														@RequestParam(required=true,value="vendorNo") String vendorNo,
														HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateTenderAnalysisVendorExcel");

		try {
			
			ExcelFile excelFile = tenderAnalysisRepository.downloadTenderAnalysisDetailsExcel(jobNumber, packageNo, vendorNo);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/transitDownload.smvc",method=RequestMethod.GET)
	public void generateTransitExcel(@RequestParam(required=true,value="type") String type,
														HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateTransitExcel");

		try {
			
			ExcelFile excelFile = null;
			if(GlobalParameter.TRANSIT_CODE_MATCHING.equals(type))
				excelFile = transitRepository.downloadCodeMatching();
			else if(GlobalParameter.TRANSIT_UOM_MATCHING.equals(type))
				excelFile = transitRepository.downloadUomMatching();
			else if(GlobalParameter.TRANSIT_ERROR.equals(type))
				excelFile = transitRepository.downloadErrorList();
			else if(GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING.equals(type))
				excelFile = transitRepository.downloadWarningList();
			

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/gammonqs/workScopeExcelDownload.smvc", method = RequestMethod.GET)
	public void generateWorkScopeExcel(@RequestParam(required = true, value = "query") String query, HttpServletRequest request, HttpServletResponse response) {		
		logger.info("generateWorkScopeExcel");

		try {

			ExcelFile excelFile = packageRepository.downloadWorkScopeExcelFile(query);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else {
				logger.info("Download Controller : Work Scope Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * @author Henry Lai created on 13-10-2014
	 * @parameter: query - object code or description (Example: 1363* or Ancho*) 
	 */
	@RequestMapping(value = "/gammonqs/accountCodeObjectExcelDownload.rpt", method = RequestMethod.GET)
	public void generateAccountCodeObjectExcel(@RequestParam(required = true, value = "query") String query, HttpServletRequest request, HttpServletResponse response) {
		logger.info("generateAccountCodeObjectExcel");

		try {
			Integer.parseInt(query.trim());
		} catch (NumberFormatException e) {
			query = ("*".concat(query)).concat("*"); // input String is a description but not a number	
		}

		try {
			ExcelFile excelFile = masterListRepository.downloadAccountCodeObjectExcelFile(query);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else {
				logger.info("Download Controller : Account Code (Object) Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * @author Henry Lai created on 13-10-2014
	 * @parameter: query - subsidiary code or description (Example: 1199* or Prelim*)
	 */
	@RequestMapping(value = "/gammonqs/accountCodeSubsidiaryExcelDownload.rpt", method = RequestMethod.GET)
	public void generateAccountCodeSubsidiaryExcel(@RequestParam(required = true, value = "query") String query, HttpServletRequest request, HttpServletResponse response) {

		logger.info("generateAccountCodeSubsidiaryExcel");

		try {
			Integer.parseInt(query.trim());
		} catch (NumberFormatException e) {
			query = ("*".concat(query)).concat("*"); // input String is a description but not a number	
		}

		try {
			ExcelFile excelFile = masterListRepository.downloadAccountCodeSubsidiaryExcelFile(query);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else {
				logger.info("Download Controller : Account Code (Subsidiary) Excel Download - Excel File Error (File is Null)");
				showReportError(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	@RequestMapping(value = "/gammonqs/printBQMasterReconciliationReport.smvc", method = RequestMethod.GET)
	public void generatePrintBQMasterReconciliationReport(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
																HttpServletRequest request, HttpServletResponse response) {

		logger.info("generatePrintBQMasterReconciliationReport");

		try {
			Transit header = transitRepository.getTransitHeader(jobNumber);
			if (Transit.RESOURCES_CONFIRMED.equals(header.getStatus()) || Transit.REPORT_PRINTED.equals(header.getStatus()) || Transit.TRANSIT_COMPLETED.equals(header.getStatus())) {
				ByteArrayOutputStream outputStream = transitRepository.GenerateTransitBQMasterReconciliationJasperReport(jobNumber);
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + "BQ_Master_Reconciliation_Report" + ".pdf" + "\"");

				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();

			} else {
				logger.info("Transit Master Report is not printed");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/printBQRecourseReconciliationReport.smvc",method=RequestMethod.GET)
	public void generatePrintBQResourceRecociliationReport(@RequestParam(required=true,value="jobNumber") String jobNumber,
														HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generatePrintBQResourceRecociliationReport");

		try {
			Transit header = transitRepository.getTransitHeader(jobNumber);
			if(Transit.RESOURCES_CONFIRMED.equals(header.getStatus()) || Transit.REPORT_PRINTED.equals(header.getStatus()) || Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
				ByteArrayOutputStream outputStream = transitRepository.GenerateTransitBQResourceReconciliationJasperReport(jobNumber);

				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION,  "attachment; filename=\"" +"BQ_Resource_Reconciliation_Report"+".pdf" + "\"");

				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();			
				
			} else{
				logger.info("Transit Resource report is not printed");
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	@RequestMapping(value="/gammonqs/paymentCertificateEnquiryExcelExport.smvc",method=RequestMethod.GET)
	public void generatePaymentCertificateEnquiryExcel(@RequestParam(required=true,value="jobNo") String jobNo,
															@RequestParam(required=true,value="company") String company,
															@RequestParam(required=true,value="packageNo") String packageNo,
															@RequestParam(required=true,value="subcontractorNo") String subcontractorNo,
															@RequestParam(required=true,value="paymentStatus") String paymentStatus,
															@RequestParam(required=true,value="paymentType") String paymentType,
															@RequestParam(required=true,value="directPayment") String directPayment,
															@RequestParam(required=true,value="paymentTerm") String paymentTerm,
															@RequestParam(required=true,value="dueDateType") String dueDateType,
															@RequestParam(required=true,value="dueDate") String dueDateString,
															@RequestParam(required=true,value="certIssueDate") String certIssueDateString,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generatePaymentCertificateEnquiryExcel");
		Date dueDate = dueDateString.equals("")?null:DateHelper.parseDate(dueDateString, "dd/MM/yyyy");
		Date certIssueDate = certIssueDateString.equals("")?null:DateHelper.parseDate(certIssueDateString, "dd/MM/yyyy");
		

		try {
			
			ExcelFile excelFile = paymentRepository.generatePaymentCertificateEnquiryExcel(jobNo, company, packageNo, subcontractorNo, paymentStatus, paymentType, directPayment, paymentTerm, dueDateType, dueDate, certIssueDate);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/mainCertificateAttachmentDownload.smvc",method=RequestMethod.GET)
	public void generateMainCertificateAttachment(@RequestParam(required=true,value="jobNumber") String jobNumber,
															@RequestParam(required=true,value="mainCertNumber") String mainCertNumber,
															@RequestParam(required=true,value="sequenceNo") String sequenceNo,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateMainCertificateAttachment");

		try {
			
			AttachmentFile attachementFile = attachmentRepository.getMainCertFileAttachment(jobNumber, Integer.parseInt(mainCertNumber), Integer.parseInt(sequenceNo));

			if (attachementFile != null) {
				byte[] file = attachementFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + attachementFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showAttachmentError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showAttachmentError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/repackagingAttachmentDownload.smvc",method=RequestMethod.GET)
	public void generateRepackingAttachment(@RequestParam(required=true,value="repackagingEntryID") String repackagingEntryID,
															@RequestParam(required=true,value="sequenceNo") String sequenceNo,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateMainCertificateAttachment");

		try {
			
			AttachmentFile attachementFile = attachmentRepository.getRepackagingFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo));

			if (attachementFile != null) {
				byte[] file = attachementFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + attachementFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showAttachmentError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showAttachmentError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	@RequestMapping(value="/gammonqs/scAttachmentDownload.smvc",method=RequestMethod.GET)
	public void generateSCAttachment(@RequestParam(required=true,value="nameObject") String nameObject,
															@RequestParam(required=true,value="textKey") String textKey,
															@RequestParam(required=true,value="sequenceNo") String sequenceNoString,
															HttpServletRequest request, HttpServletResponse response ) {		
				
		logger.info("generateSCAttachment");

		try {
			
			Integer sequenceNo = Integer.parseInt(sequenceNoString);
			AttachmentFile attachmentFile = attachmentRepository.obtainFileAttachment(nameObject.trim(), textKey, sequenceNo);

			if (attachmentFile != null) {
				byte[] file = attachmentFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showAttachmentError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showAttachmentError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/gammonqs/scPaymentExceptionReportDownload.smvc",method=RequestMethod.GET)
	public void generateSubcontractorPaymentExceptionalExcel(@RequestParam(required=false,value="division") String division,
															@RequestParam(required=false,value="company") String company,
															@RequestParam(required=false,value="jobNumber") String jobNumber,
															@RequestParam(required=false,value="vendorNo") String vendorNo,
															@RequestParam(required=false,value="packageNo") String packageNo,
															@RequestParam(required=true,value="scStatus") String scStatus,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateSubcontractorPaymentExceptionalExcel");
		try {
			ExcelFile excelFile = paymentRepository.downloadSubcontractorPaymentExceptionReport(division, company, jobNumber, vendorNo, packageNo, scStatus);

			if (excelFile != null){
				byte[] file = excelFile.getBytes();
				response.setContentType("application/octet-stream");
				response.setContentLength(file.length);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			}else
				showReportError(response);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	@RequestMapping(value="/gammonqs/subcontractPaymentExcelDownload.smvc",method=RequestMethod.GET)
	public void generateSubcontractPaymentExcel(@RequestParam(required=true,value="company") String company,
															@RequestParam(required=true,value="dueDate") String dueDateString,
															@RequestParam(required=true,value="jobNumber") String jobNumber,
															@RequestParam(required=true,value="dueDateType") String dueDateType,
															@RequestParam(required=true,value="supplierNumber") String supplierNumber,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateSubcontractPaymentExcel");
		
		Date dueDate = dueDateString.equals("")?null:DateHelper.parseDate(dueDateString, "dd/MM/yyyy");

		try {
			
			ExcelFile excelFile = paymentRepository.generateSubcontractPaymentExcel(company, dueDate, jobNumber, dueDateType, supplierNumber);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				showReportError(response);
			}			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
			e.printStackTrace();
			logger.info("Error: "+e.getLocalizedMessage());
			showReportError(response);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	//To merge PDF files
	public static void concatPDFs(List<ByteArrayInputStream> streamOfPDFFiles, ByteArrayOutputStream outputStream, boolean paginate) {
		Document document = new Document();
		try {
			@SuppressWarnings("unused")
			int totalPages = 0;
			
			List<ByteArrayInputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			Iterator<ByteArrayInputStream> iteratorPDFs = pdfs.iterator();
			
			// Create Readers for the pdfs.     
			while (iteratorPDFs.hasNext()) {
				ByteArrayInputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages(); 
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			
			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent();
			// Holds the PDF
			// data 
			PdfImportedPage page;
			@SuppressWarnings("unused")
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();
			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();
				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						//cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (document.isOpen()) 
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();    
			}
			catch (IOException ioe) {
				ioe.printStackTrace();  
			}
		}
	}
	
	/** 
	 * @author Henry Lai created on 17-10-2014
	 * @parameter: response - HttpServletResponse object that contains the http servlet response
	 */
	private void showAttachmentError(HttpServletResponse response){
		try {
			response.getOutputStream().print("<html><head><title>Attachment not found</title></head>");
			response.getOutputStream().print("<script>alert(\"Attachment not found\");window.close();");
			response.getOutputStream().print("</script></html>");
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @author Henry Lai created on 11-11-2014
	 * @parameter: response - HttpServletResponse object that contains the http servlet response
	 */
	private void showReportError(HttpServletResponse response){
		try {
			response.getOutputStream().print("<html><head><title>Report Error</title></head>");
			response.getOutputStream().print("<script>alert(\"The report cannot be generated\");window.close();");
			response.getOutputStream().print("</script></html>");
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author xethhung Jun 29, 2015 Generate main Certificate Enquiry Report as
	 *         PDF
	 */
	@RequestMapping(value = "/gammonqs/contraChargeEnquiryReportPDFExport.rpt", method = RequestMethod.GET)
	public void generateContraChargeReportPDF(
			@RequestParam(required = true, value = "jobNo") String jobNo,
			@RequestParam(required = true, value = "lineType") String lineType,
			@RequestParam(required = true, value = "BQItem") String BQItem,
			@RequestParam(required = true, value = "description") String description,
			@RequestParam(required = true, value = "objectCode") String objectCode,
			@RequestParam(required = true, value = "subsidiaryCode") String subsidiaryCode,
			@RequestParam(required = true, value = "subcontractNumber") String subcontractNumber,
			@RequestParam(required = true, value = "subcontractorNumber") String subcontractorNumber,
			HttpServletRequest request, HttpServletResponse response) {
		String functionName = "generateMainCertificateReportPDF";
		logger.info("Start -> " + functionName);
		try {
			ContraChargeSearchingCriteriaWrapper wrapper = new ContraChargeSearchingCriteriaWrapper();
			wrapper.setJobNumber(jobNo);
			wrapper.setLineType(lineType);
			wrapper.setBQItem(BQItem);
			wrapper.setDescription(description);
			wrapper.setObjectCode(objectCode);
			wrapper.setSubsidiaryCode(subsidiaryCode);
			wrapper.setSubcontractNumber(subcontractNumber);
			wrapper.setSubcontractorNumber(subcontractorNumber);
			ByteArrayOutputStream outputStream = packageRepository
					.obtainContraChargeReportPDF(wrapper);

			if (outputStream != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION,
						"attachment; filename=\"ContraChargeEnquiryReport"
								+ ".pdf\"");
				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			} else {
				logger.info("No file is generated.");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: " + e.getLocalizedMessage());
			showReportError(response);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("End -> " + functionName);
	}

	/**
	 * @author xethhung Jun 29, 2015 Generate main Certificate Enquiry Report as
	 *         PDF
	 */
	@RequestMapping(value = "/gammonqs/contraChargeEnquiryReportExcelExport.rpt", method = RequestMethod.GET)
	public void generateContraChargeReportExcel(
			@RequestParam(required = true, value = "jobNo") String jobNo,
			@RequestParam(required = true, value = "lineType") String lineType,
			@RequestParam(required = true, value = "BQItem") String BQItem,
			@RequestParam(required = true, value = "description") String description,
			@RequestParam(required = true, value = "objectCode") String objectCode,
			@RequestParam(required = true, value = "subsidiaryCode") String subsidiaryCode,
			@RequestParam(required = true, value = "subcontractNumber") String subcontractNumber,
			@RequestParam(required = true, value = "subcontractorNumber") String subcontractorNumber,
			HttpServletRequest request, HttpServletResponse response) {
		String functionName = "contraChargeEnquiryReportExcelExport";
		logger.info("Start -> " + functionName);
		try {
			ContraChargeSearchingCriteriaWrapper wrapper = new ContraChargeSearchingCriteriaWrapper();
			wrapper.setJobNumber(jobNo);
			wrapper.setLineType(lineType);
			wrapper.setBQItem(BQItem);
			wrapper.setDescription(description);
			wrapper.setObjectCode(objectCode);
			wrapper.setSubsidiaryCode(subsidiaryCode);
			wrapper.setSubcontractNumber(subcontractNumber);
			wrapper.setSubcontractorNumber(subcontractorNumber);
			ByteArrayOutputStream outputStream = packageRepository
					.obtainContraChargeReportExcel(wrapper);

			if (outputStream != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION,
						"attachment; filename=\"ContraChargeEnquiryReport.xls\"");

				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
			} else {
				logger.info("No file is generated.");
				showReportError(response);
			}
		} catch (Exception e) {
			logger.info("No file is generated.");
			e.printStackTrace();
			logger.info("Error: " + e.getLocalizedMessage());
			showReportError(response);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("End -> " + functionName);
	}

}
