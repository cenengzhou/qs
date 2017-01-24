package com.gammon.pcms.web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.pcms.model.Attachment;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.service.scPackage.UploadSCAttachmentResponseObj;
import com.gammon.qs.service.transit.TransitImportResponse;
import com.gammon.qs.service.transit.TransitService;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping(value = "service/attachment/")
public class AttachmentController {

	private Logger logger = Logger.getLogger(AttachmentController.class.getName());
	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";
	private String RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM = "application/octet-stream";
	private String RESPONSE_CONTENT_TYPE_APPLICATION_PDF = "application/pdf";
	private String RESPONSE_HEADER_NAME_CONTENT_DISPOSITION = "Content-Disposition";
	
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private TransitService transitService;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private PaymentService paymentService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','getRepackagingAttachments', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRepackagingAttachments", method = RequestMethod.POST)
	public List<AttachRepackaging> getRepackagingAttachments(@RequestParam Long repackagingEntryID) throws Exception {
		List<AttachRepackaging> resultList = new ArrayList<AttachRepackaging>();
		resultList.addAll(attachmentService.getRepackagingAttachments(repackagingEntryID));
		return resultList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadRepackingAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadRepackingAttachment", method = RequestMethod.POST)
	public void uploadRepackingAttachment(@RequestParam(required = true, value = "repackagingEntryID") String repackagingEntryID, 
								@RequestParam(required = true, value = "sequenceNo") String sequenceNo, 
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Repacking Attachment - START");
		
		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		int sn = Integer.valueOf(sequenceNo);

		for (MultipartFile multipartFile : multipartFiles) {
			
			byte[] file = multipartFile.getBytes();
			if (file != null) {
//				Integer seqNo = attachmentRepository.addFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo), multipartFile.getOriginalFilename(), file);
				Integer seqNo = attachmentService.addRepackagingFileAttachment(new Long(repackagingEntryID), sn, multipartFile.getOriginalFilename(), file);
				sn++;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if(seqNo != null){
					resultMap.put("success", Boolean.TRUE);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
				}
				else{
					resultMap.put("success", Boolean.FALSE);
				}
//				response.setContentType("text/html");
//				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload Repacking Attachment -END");
			}
		}
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','downloadRepackagingAttachment', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="downloadRepackagingAttachment",method=RequestMethod.GET)
	public void downloadRepackagingAttachment(@RequestParam(required=true,value="repackagingEntryID") String repackagingEntryID,
															@RequestParam(required=true,value="sequenceNo") String sequenceNo,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateMainCertificateAttachment");

		try {
			
			AttachmentFile attachementFile = attachmentService.getRepackagingFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo));

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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteRepackagingAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteRepackagingAttachment", method = RequestMethod.POST)
	public Boolean deleteRepackagingAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo) throws Exception{
		return attachmentService.deleteRepackagingAttachment(repackagingEntryID, sequenceNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','saveRepackagingTextAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveRepackagingTextAttachment", method = RequestMethod.POST)
	public Boolean saveRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment) throws Exception{
		return attachmentService.saveRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','addRepackagingTextAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addRepackagingTextAttachment", method = RequestMethod.POST)
	public Integer addRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, String textAttachment) throws Exception{
		return attachmentService.addRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','getAttachmentListForPCMS', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAttachmentListForPCMS", method = RequestMethod.POST)
	public List<? extends AbstractAttachment> getAttachmentListForPCMS(@RequestParam String nameObject, @RequestParam String textKey) throws Exception {
		return attachmentService.getAttachmentListForPCMS(nameObject, textKey);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadSCAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadSCAttachment", method = RequestMethod.POST)
	public void uploadSCAttachment( 
								@RequestParam(required = true, value = "nameObject") String nameObject,
								@RequestParam(required = true, value = "textKey") String textKey,
								@RequestParam(required = true, value = "sequenceNo") String sequenceNo,
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload SC Attachment - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		int sn = Integer.valueOf(sequenceNo);
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				UploadSCAttachmentResponseObj result = attachmentService.uploadAttachment(nameObject, textKey, sn, multipartFile.getOriginalFilename(), file, null);
				sn++;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (result != null) {
					resultMap.put("success", true);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					logger.info("error: ");
				}
//				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(result));
				logger.info("Upload SC Attachment -END");
			}
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadAddendumAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadAddendumAttachment", method = RequestMethod.POST)
	public void uploadAddendumAttachment( 
								@RequestParam(required = true, value = "nameObject") String nameObject,
								@RequestParam(required = true, value = "textKey") String textKey,
								@RequestParam(required = true, value = "sequenceNo") String sequenceNo,
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload uploadAddendumAttachment - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		int sn = Integer.valueOf(sequenceNo);
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				boolean result = attachmentService.uploadAddendumAttachment(nameObject, textKey, new BigDecimal(sn), multipartFile.getOriginalFilename(), file, null);
				sn++;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (result) {
					resultMap.put("success", true);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					logger.info("error: ");
				}
//				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(result));
				logger.info("Upload uploadAddendumAttachment -END");
			}
		}
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','obtainAddendumFileAttachment', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="obtainAddendumFileAttachment",method=RequestMethod.GET)
	public void obtainAddendumFileAttachment(@RequestParam(required=true,value="nameObject") String nameObject,
															@RequestParam(required=true,value="textKey") String textKey,
															@RequestParam(required=true,value="sequenceNo") String sequenceNoString,
															HttpServletRequest request, HttpServletResponse response ) {		
				
		logger.info("generateSCAttachment");

		try {
			
			Integer sequenceNo = Integer.parseInt(sequenceNoString);
			AttachmentFile attachmentFile = attachmentService.obtainAddendumFileAttachment(nameObject.trim(), textKey, sequenceNo);

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

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteAddendumAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAddendumAttachment", method = RequestMethod.POST)
	public Boolean deleteAddendumAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception{
		return attachmentService.deleteAddendumAttachment(nameObject, textKey,sequenceNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadAddendumTextAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadAddendumTextAttachment", method = RequestMethod.POST)
	public Boolean uploadAddendumTextAttachment(@RequestParam String nameObject, @RequestParam String textKey, 
				@RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment ) throws Exception{
		return attachmentService.uploadAddendumTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAttachment", method = RequestMethod.POST)
	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception{
		return attachmentService.deleteAttachment(nameObject, textKey,sequenceNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadTextAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadTextAttachment", method = RequestMethod.POST)
	public Boolean uploadTextAttachment(@RequestParam String nameObject, @RequestParam String textKey, 
				@RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment ) throws Exception{
		return attachmentService.uploadTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSCAttachment', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="downloadScAttachment",method=RequestMethod.GET)
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
				response.setContentLength(file != null ? file.length : 0);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + (attachmentFile.getFileName()!=null?attachmentFile.getFileName().trim():"") + "\"");

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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','getMainCertFileAttachment', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="getMainCertFileAttachment",method=RequestMethod.POST)
	public void getMainCertFileAttachment(@RequestParam String noJob, @RequestParam Integer noMainCert, @RequestParam Integer noSequence,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generateMainCgetMainCertFileAttachmentertificateAttachment");

		try {
			
			AttachmentFile attachementFile = attachmentService.getMainCertFileAttachment(noJob, noMainCert, noSequence);

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
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} finally{
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','addMainCertFileAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addMainCertFileAttachment", method = RequestMethod.POST)
	public void addMainCertFileAttachment( 
								@RequestParam String noJob,
								@RequestParam Integer noMainCert,
								@RequestParam Integer noSequence,
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("addMainCertFileAttachment");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
		
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		int sn = Integer.valueOf(noSequence);
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				Boolean result = attachmentService.addMainCertFileAttachment(noJob, noMainCert, sn, multipartFile.getOriginalFilename(), file);
				sn++;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (result) {
					resultMap.put("success", true);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					logger.info("error: ");
				}
//				response.setContentType("text/html");
//				response.getWriter().print((new Gson()).toJson(result));
				logger.info("addMainCertFileAttachment -END");
			}
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteMainCertAttachment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteMainCertAttachment", method = RequestMethod.POST)
	public Boolean deleteMainCertAttachment(@RequestParam String noJob, Integer noMainCert, Integer noSequence) throws DatabaseOperationException {
		return attachmentService.deleteMainCertAttachment(noJob, noMainCert,noSequence);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','obtainAttachmentList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainAttachmentList", method = RequestMethod.POST)
	public List<Attachment> obtainAttachmentList(@RequestParam String nameObject, @RequestParam String textKey) throws Exception {
		return attachmentService.obtainAttachmentList(nameObject, textKey);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadTransit', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "transitUpload", method = RequestMethod.POST)
	public void uploadTransit(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
								@RequestParam(required = true, value = "type") String type, 
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Transit - START");
		
		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
	
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		TransitImportResponse transitImportResponse = null;
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				transitImportResponse =  transitService.importBqItemsOrResourcesFromXls(jobNumber, type, file); 
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (transitImportResponse == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", transitImportResponse);
					logger.info("error: " + transitImportResponse);
				}
				
//				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(transitImportResponse));
				logger.info("Upload Transit -END");
			}
		}
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSubcontractLiabilityReport', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="subcontractLiabilityReportExport",method=RequestMethod.GET)
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
			ByteArrayOutputStream outputStream = subcontractService.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractLiabilityReport");
			if (outputStream!=null){
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractLiabilityReport."+".pdf\"");
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generatePrintBQMasterReconciliationReport', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "printBQMasterReconciliationReport", method = RequestMethod.GET)
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generatePrintBQResourceRecociliationReport', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="printBQRecourseReconciliationReport",method=RequestMethod.GET)
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generatePaymentCertificateEnquiryExcel', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="paymentCertificateEnquiryExcelExport",method=RequestMethod.GET)
	public void generatePaymentCertificateEnquiryExcel(@RequestParam(required=true,value="jobNumber") String jobNumber,
															@RequestParam(required=true,value="company") String company,
															@RequestParam(required=false,value="packageNo") String packageNo,
															@RequestParam(required=false,value="subcontractorNo") String subcontractorNo,
															@RequestParam(required=false,value="paymentStatus") String paymentStatus,
															@RequestParam(required=false,value="paymentType") String paymentType,
															@RequestParam(required=false,value="directPayment") String directPayment,
															@RequestParam(required=false,value="paymentTerm") String paymentTerm,
															@RequestParam(required=true,value="dueDateType") String dueDateType,
															@RequestParam(required=true,value="dueDate") String dueDateString,
															@RequestParam(required=false,value="certIssueDate") String certIssueDateString,
															HttpServletRequest request, HttpServletResponse response ){		
				
		logger.info("generatePaymentCertificateEnquiryExcel");
		Date dueDate =StringUtils.isEmpty(dueDateString)?null:DateHelper.parseDate(dueDateString, "dd/MM/yyyy");
		Date certIssueDate = StringUtils.isEmpty(certIssueDateString)?null:DateHelper.parseDate(certIssueDateString, "dd/MM/yyyy");
		

		try {
			
			ExcelFile excelFile = paymentService.generatePaymentCertificateEnquiryExcel(jobNumber, company, packageNo, subcontractorNo, paymentStatus, paymentType, directPayment, paymentTerm, dueDateType, dueDate, certIssueDate);

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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','printPaymentCertificateReportPdf', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="printPaymentCertificateReportPdf",method=RequestMethod.GET)
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateFinanceSubcontractListExcel', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="financeSubcontractListDownload",method=RequestMethod.GET)
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
			Boolean includeJobCompletionDate = false;
			if("true".equals(includeJobCompletionDateString))
				includeJobCompletionDate = true;
			
			ExcelFile excelFile = subcontractService.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year, "subcontractListReport");

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSubcontractReport', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="subcontractReportExport",method=RequestMethod.GET)
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
			ByteArrayOutputStream outputStream = subcontractService.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractReport");
			if (outputStream!=null){
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractReport."+".pdf\"");
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSubcontractLiabilityExcel', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="subcontractLiabilityExcelExport",method=RequestMethod.GET)
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
			Boolean includeJobCompletionDate = false;
			if("true".equals(includeJobCompletionDateString))
				includeJobCompletionDate = true;
			
			ExcelFile excelFile = subcontractService.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year,"subcontractLiabilityReport");

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSubcontractorAnalysisExcel', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="subcontractorAnalysisExcelExport",method=RequestMethod.GET)
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
			Boolean includeJobCompletionDate = false;
			if("true".equals(includeJobCompletionDateString))
				includeJobCompletionDate = true;
			
			ExcelFile excelFile = subcontractService.downloadSubcontractEnquiryReportExcelFile(company,("EM".equals(division)?"E&M":division),jobNumber,subcontractNumber,
					subcontractorNumber, subcontractorNature, paymentType, workScope, clientNo, splitTerminateStatus, includeJobCompletionDate, month, year, "SubcontractorAnalysisReport");

			if (excelFile != null) {
				byte[] file = excelFile.getBytes();
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(file.length);
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"" + excelFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else{
				logger.info("Download Controller : generateFinanceSubcontractListExcel - Excel File Error (File is Null)");
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','generateSubcontractorAnalysisReport', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="subcontractorAnalysisReportExport",method=RequestMethod.GET)
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
			ByteArrayOutputStream outputStream = subcontractService.downloadSubcontractEnquiryReportPDFFile(company, ("EM".equals(division)?"E&M":division), jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, 
																									paymentType, workScope, clientNo, false, splitTerminateStatus, month, year, "SubcontractorAnalysisReport");
			if (outputStream!=null){
				response.setContentType(RESPONSE_CONTENT_TYPE_APPLICATION_OCTENT_STREAM);
				response.setContentLength(outputStream.size());
				response.setHeader(RESPONSE_HEADER_NAME_CONTENT_DISPOSITION, "attachment; filename=\"SubcontractorAnalysisReport."+".pdf\"");
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
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value="transitDownload",method=RequestMethod.GET)
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
	
}
