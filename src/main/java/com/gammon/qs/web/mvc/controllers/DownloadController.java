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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractorService;
import com.gammon.qs.service.transit.TransitService;
import com.gammon.qs.shared.GlobalParameter;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@Controller
public class DownloadController{
	private Logger logger = Logger.getLogger(DownloadController.class.getName());

	private String RESPONSE_CONTENT_TYPE_APPLICATION_PDF = "application/pdf";

	@Autowired
	private SubcontractorService subcontractorService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private TransitService transitService;
	@Autowired
	private AttachmentService attachmentService;
	
	@RequestMapping(value="/gammonqs/subcontractorExcelExport.rpt",method=RequestMethod.GET)
	public void generateSubcontractorExcel(	@RequestParam(required=true,value="subcontractor") String subcontractor,
											@RequestParam(required=true,value="workScope") String workScope,
											@RequestParam(required=true,value="type") String type,
											HttpServletRequest request, HttpServletResponse response ){
		logger.info("generateSubcontractorExcel");
		try {
			ExcelFile excelFile = subcontractorService.subcontractorExcelExport(workScope, subcontractor, type);
			
			if(excelFile!=null){
				byte[] file = excelFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");
				
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
			ByteArrayOutputStream outputStream = paymentService.getSCPaymentCertViewDetailWrapper(jobNumber, packageNumber, paymentCertNo,addendumIncluedBoolean);

			if (outputStream != null) {
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(outputStream.size());
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

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
			List<ByteArrayInputStream> outputStreamList = paymentService.obtainPaymentCertificatesReport(company, dueDate, jobNumber, dueDateType, supplierNumber);
			
			logger.info("Merge reprot");
			//To merge the report
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			concatPDFs(outputStreamList, output, true);

			if (outputStreamList != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_PDF);
				response.setContentLength(output.size());
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

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
			List<ByteArrayInputStream> outputStreamList = paymentService.obtainAllPaymentCertificatesReport(company, dueDate, jobNumber, dueDateType);
			
			logger.info("Merge reprot");
			//To merge the report
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			concatPDFs(outputStreamList, output, true);

			if (outputStreamList != null) {
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_PDF);
				response.setContentLength(output.size());
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"SC_Payment_Cert"+".pdf" + "\"");

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
	
	@RequestMapping(value="/gammonqs/transitDownload.smvc",method=RequestMethod.GET)
	public void generateTransitExcel(@RequestParam(required=true,value="type") String type,
														HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateTransitExcel");

		try {
			
			ExcelFile excelFile = null;
			if(GlobalParameter.TRANSIT_CODE_MATCHING.equals(type))
				excelFile = transitService.downloadCodeMatching();
			else if(GlobalParameter.TRANSIT_UOM_MATCHING.equals(type))
				excelFile = transitService.downloadUomMatching();
			else if(GlobalParameter.TRANSIT_ERROR.equals(type))
				excelFile = transitService.downloadErrorList();
			else if(GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING.equals(type))
				excelFile = transitService.downloadWarningList();
			

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

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
	
	@RequestMapping(value = "/gammonqs/printBQMasterReconciliationReport.smvc", method = RequestMethod.GET)
	public void generatePrintBQMasterReconciliationReport(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
																HttpServletRequest request, HttpServletResponse response) {

		logger.info("generatePrintBQMasterReconciliationReport");

		try {
			Transit header = transitService.getTransitHeader(jobNumber);
			if (Transit.RESOURCES_CONFIRMED.equals(header.getStatus()) || Transit.REPORT_PRINTED.equals(header.getStatus()) || Transit.TRANSIT_COMPLETED.equals(header.getStatus())) {
				ByteArrayOutputStream outputStream = transitService.GenerateTransitBQMasterReconciliationJasperReport(jobNumber);
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(outputStream.size());
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "BQ_Master_Reconciliation_Report" + ".pdf" + "\"");

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
			Transit header = transitService.getTransitHeader(jobNumber);
			if(Transit.RESOURCES_CONFIRMED.equals(header.getStatus()) || Transit.REPORT_PRINTED.equals(header.getStatus()) || Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
				ByteArrayOutputStream outputStream = transitService.GenerateTransitBQResourceReconciliationJasperReport(jobNumber);

				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(outputStream.size());
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" +"BQ_Resource_Reconciliation_Report"+".pdf" + "\"");

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
	
	@RequestMapping(value="/gammonqs/mainCertificateAttachmentDownload.smvc",method=RequestMethod.GET)
	public void generateMainCertificateAttachment(@RequestParam(required=true,value="jobNumber") String jobNumber,
															@RequestParam(required=true,value="mainCertNumber") String mainCertNumber,
															@RequestParam(required=true,value="sequenceNo") String sequenceNo,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateMainCertificateAttachment");

		try {
			
			AttachmentFile attachementFile = attachmentService.getMainCertFileAttachment(jobNumber, Integer.parseInt(mainCertNumber), Integer.parseInt(sequenceNo));

			if (attachementFile != null) {
				byte[] file = attachementFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachementFile.getFileName() + "\"");

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
	
//	@RequestMapping(value="/gammonqs/repackagingAttachmentDownload.smvc",method=RequestMethod.GET)
//	public void generateRepackingAttachment(@RequestParam(required=true,value="repackagingEntryID") String repackagingEntryID,
//															@RequestParam(required=true,value="sequenceNo") String sequenceNo,
//															HttpServletRequest request, HttpServletResponse response ){		
//				
//		logger.info("generateMainCertificateAttachment");
//
//		try {
//			
//			AttachmentFile attachementFile = attachmentService.getRepackagingFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo));
//
//			if (attachementFile != null) {
//				byte[] file = attachementFile.getBytes();
//				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//				response.setContentLength(file.length);
//				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachementFile.getFileName() + "\"");
//
//				response.getOutputStream().write(file);
//				response.getOutputStream().flush();
//			} else{
//				showAttachmentError(response);
//			}			
//		} catch (Exception e) {
//			logger.log(Level.SEVERE, "WEB LAYER EXCEPTION ", e);
//			e.printStackTrace();
//			logger.info("Error: "+e.getLocalizedMessage());
//			showAttachmentError(response);
//		} finally{
//			try {
//				response.getOutputStream().close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	} 
//	
	@RequestMapping(value="/gammonqs/scAttachmentDownload.smvc",method=RequestMethod.GET)
	public void generateSCAttachment(@RequestParam(required=true,value="nameObject") String nameObject,
															@RequestParam(required=true,value="textKey") String textKey,
															@RequestParam(required=true,value="sequenceNo") String sequenceNoString,
															HttpServletRequest request, HttpServletResponse response ) {		
				
		logger.info("generateSCAttachment");

		try {
			
			Integer sequenceNo = Integer.parseInt(sequenceNoString);
			AttachmentFile attachmentFile = attachmentService.obtainFileAttachment(nameObject.trim(), textKey, sequenceNo);

			if (attachmentFile != null) {
				byte[] file = attachmentFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentFile.getFileName() + "\"");

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
			ExcelFile excelFile = paymentService.downloadSubcontractorPaymentExceptionReport(division, company, jobNumber, vendorNo, packageNo, scStatus);

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
			
			ExcelFile excelFile = paymentService.generateSubcontractPaymentExcel(company, dueDate, jobNumber, dueDateType, supplierNumber);

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

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
	
}
