package com.gammon.qs.web.mvc.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.BpiItemService;
import com.gammon.qs.service.JdeAccountLedgerService;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.service.TenderService;
import com.gammon.qs.service.subcontractDetail.UploadSubcontractDetailByExcelResponse;
import com.gammon.qs.service.transit.TransitImportResponse;
import com.gammon.qs.service.transit.TransitService;
import com.google.gson.Gson;

/**
 * koeyyeung Feb 11, 2014 7:11:58 PM
 */
@Controller
public class UploadController {
	private Logger logger = Logger.getLogger(UploadController.class.getName());

	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";

	@Autowired
	private JdeAccountLedgerService accountLedgerRepository;
	@Autowired
	private BpiItemService bqRepository;
	@Autowired
	private SubcontractService packageRepository;
	@Autowired
	private TenderService tenderAnalysisRepository;
	@Autowired
	private TransitService transitRepository;
	@Autowired
	private ResourceSummaryService bqResourceSummaryRepository;
	@Autowired
	private AttachmentService attachmentRepository;
	
	@RequestMapping(value="/gammonqs/imageUpload.smvc",method=RequestMethod.POST)
	public void uploadAttachment(@RequestParam(required=true,value="messageBoardID") String messageBoardID,
								 @RequestParam(required=true,value="docType") String docType,
								 @RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response ) throws Exception{
		throw new RuntimeException("remove entity | messageBoardAttachment | remark uploadAttachment(...)");
		//TODO: remove entity | messageBoardAttachment | remark uploadAttachment(...)
//		logger.info("Upload Attachment - START");
//		
//		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
//		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
//		
//		//List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
//		
//		for (MultipartFile multipartFile : multipartFiles) {
//			byte[] file = multipartFile.getBytes();
//			if (file != null) {
//				String error = messageBoardAttachmentRepository.uploadAttachment(file, multipartFile.getOriginalFilename(), messageBoardID, docType);
//				
//				Map<String, Object> resultMap = new HashMap<String, Object>();
//				if(error==null){
//					resultMap.put("success", true);
//					logger.info("Upload Attachment: success.");
//				}
//				else{
//					resultMap.put("success", false);
//					resultMap.put("error", error);
//					logger.info("error: "+error);
//				}
//
//				response.getWriter().print((new Gson()).toJson(resultMap));
//				logger.info("Upload Attachment -END");
//			}
//		}
	}
	
	@RequestMapping(value = "/gammonqs/bqItemIVUpload.smvc", method = RequestMethod.POST)
	public void uploadBQItem(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
											@RequestParam(required = true, value = "username") String username, 
											@RequestParam("files") List<MultipartFile> multipartFiles,
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload BQ Item - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {
				logger.info("Upload Budget Forecast Excel - START");

				String error = bqRepository.uploadBQItemIVExcel(jobNumber, username, file);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}

				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload BQ Item -END");
			}
		}
	}
	
	@RequestMapping(value = "/gammonqs/bqResourceSummaryIVUpload.smvc", method = RequestMethod.POST)
	public void uploadBQResourceSummaryIV(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
											@RequestParam("files") List<MultipartFile> multipartFiles,
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload BQ Resource Summary IV - START");
		
		

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				String error = bqResourceSummaryRepository.uploadIVExcel(jobNumber, file);

				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}
				
				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload BQ Resource Summary IV -END");
			}
		}
	}
	
	@RequestMapping(value = "/gammonqs/budgetForecastExcelUpload.smvc", method = RequestMethod.POST)
	public void uploadBudgetForecastExcel(@RequestParam(required = true, value = "ledgerType") String ledgerType, 
											@RequestParam(required = true, value = "jobNo") String jobNo, 
											@RequestParam("files") List<MultipartFile> multipartFiles,
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Budget Forecast Excel - START");
		
		

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				String error = accountLedgerRepository.uploadBudgetForecastExcel(jobNo, ledgerType, file);

				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}

				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info((new Gson()).toJson(error));
				logger.info("Upload Budget Forecast Excel -END");
			}
		}
	}
	
	@RequestMapping(value = "/gammonqs/resourceIVUpload.smvc", method = RequestMethod.POST)
	public void uploadResourceIV(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
											@RequestParam(required = true, value = "username") String username, 
											@RequestParam("files") List<MultipartFile> multipartFiles,
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Resource IV - START");
		
		

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				String error = bqRepository.uploadResourceIVExcel(jobNumber, username, file);

				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}

				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload Resource IV -END");
			}
		}
	}
	
	/* @RequestMapping(value = "/gammonqs/scDetailExcelUpload.smvc", method = RequestMethod.POST)
	public void uploadSCDetailExcel(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
										@RequestParam(required = true, value = "packageNumber") String packageNumberString, 
										@RequestParam(required = true, value = "paymentStatus") String paymentStatus, 
										@RequestParam(required = true, value = "paymentRequestStatus") String paymentRequestStatus, 
										@RequestParam(required = true, value = "legacyJobFlag") String legacyJobFlag, 
										@RequestParam(required = true, value = "allowManualInputSCWorkdone") String allowManualInputSCWorkdone,
										@RequestParam(required = true, value = "userID") String userID, 
										@RequestParam("files") List<MultipartFile> multipartFiles,
										HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload SC Detail Excel - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
		
		Integer packageNumber = Integer.parseInt(packageNumberString);

//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		UploadSubcontractDetailByExcelResponse uploadFileResponse = null;
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				uploadFileResponse =  packageRepository.uploadSCDetailByExcel(jobNumber, packageNumber, paymentStatus, paymentRequestStatus, legacyJobFlag, allowManualInputSCWorkdone, userID,file);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (uploadFileResponse == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", uploadFileResponse);
					logger.info("error: " + uploadFileResponse);
				}

				response.getWriter().print((new Gson()).toJson(uploadFileResponse));
				logger.info("Upload SC Detail Excel -END");
			}
		}
	} */
	
	@RequestMapping(value = "/gammonqs/tenderAnalysisExcelUpload.smvc", method = RequestMethod.POST)
	public void uploadTenderAnalysisExcel( @RequestParam(required = true, value = "jobNumber") String jobNumber, 
											@RequestParam(required = true, value = "packageNumber") String packageNumber,
											@RequestParam("files") List<MultipartFile> multipartFiles,
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Tender Analysis Excel - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
		
		
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				String error =  tenderAnalysisRepository.uploadTenderAnalysisDetailsFromExcel(jobNumber, packageNumber, file);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}
				
				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info((new Gson()).toJson(resultMap));
				logger.info("Upload Tender Analysis Excel -END");
			}
		}
	}
	
	@RequestMapping(value = "/gammonqs/tenderAnalysisVendorExcelUpload.smvc", method = RequestMethod.POST)
	public void uploadTenderAnalysisVendorExcel(@RequestParam("files") List<MultipartFile> multipartFiles, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Tender Analysis Vendor Excel - START");
		
		if (!(request instanceof MultipartHttpServletRequest)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
        }

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
		
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				String error =  tenderAnalysisRepository.uploadTenderAnalysisVendorDetailsFromExcel(file);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (error == null) {
					resultMap.put("success", true);
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", error);
					logger.info("error: " + error);
				}
				
//				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload Tender Analysis Vendor Excel -END");
			}
		}
	}
    
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "/gammonqs/transitUpload.smvc", method = RequestMethod.POST)
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

				transitImportResponse =  transitRepository.importBqItemsOrResourcesFromXls(jobNumber, type, file); 
				
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
	
	@RequestMapping(value = "/gammonqs/mainCertificateAttachmentUpload.smvc", method = RequestMethod.POST)
	public void uploadMainCertificateAttachment(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
								@RequestParam(required = true, value = "mainCertNumber") String mainCertNumber,
								@RequestParam(required = true, value = "sequenceNo") String sequenceNo, 
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Main Certificate Attachment - START");
		
		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
		
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {
				Boolean added = attachmentRepository.addMainCertFileAttachment(jobNumber, Integer.parseInt(mainCertNumber), Integer.parseInt(sequenceNo), multipartFile.getOriginalFilename(),file);
//				Boolean added = attachmentRepository.addFileAttachment(jobNumber, Integer.parseInt(mainCertNumber), Integer.parseInt(sequenceNo), multipartFile.getOriginalFilename(),file);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (added) {
					resultMap.put("success", true);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
					logger.info("Upload Attachment: success.");
				} else {
					resultMap.put("success", false);
					logger.info("error: ");
				}
//				response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
				response.getWriter().print((new Gson()).toJson(resultMap));
				logger.info("Upload Main Certificate Attachment -END");
			}
		}
	}

//	moved to AttachmentController
//	@RequestMapping(value = "/gammonqs/repackagingAttachmentUpload.smvc", method = RequestMethod.POST)
//	public void uploadRepackingAttachment(@RequestParam(required = true, value = "repackagingEntryID") String repackagingEntryID, 
//								@RequestParam(required = true, value = "sequenceNo") String sequenceNo, 
//								@RequestParam("files") List<MultipartFile> multipartFiles,
//								HttpServletRequest request, HttpServletResponse response) throws Exception {
//		logger.info("Upload Repacking Attachment - START");
//		
//		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
//		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
//
////		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
//
//
//		for (MultipartFile multipartFile : multipartFiles) {
//			
//			byte[] file = multipartFile.getBytes();
//			if (file != null) {
////				Integer seqNo = attachmentRepository.addFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo), multipartFile.getOriginalFilename(), file);
//				Integer seqNo = attachmentRepository.addRepackagingFileAttachment(new Long(repackagingEntryID), Integer.valueOf(sequenceNo), multipartFile.getOriginalFilename(), file);
//
//				Map<String, Object> resultMap = new HashMap<String, Object>();
//				if(seqNo != null){
//					resultMap.put("success", Boolean.TRUE);
//					resultMap.put("fileName", multipartFile.getOriginalFilename());
//				}
//				else{
//					resultMap.put("success", Boolean.FALSE);
//				}
////				response.setContentType("text/html");
//				response.getWriter().print((new Gson()).toJson(resultMap));
//				logger.info("Upload Repacking Attachment -END");
//			}
//		}
//	}
	
// moved to AttachmentContractor
//	@RequestMapping(value = "/gammonqs/scAttachmentUpload.smvc", method = RequestMethod.POST)
//	public void uploadSCAttachment(@RequestParam(required = true, value = "jobNumber") String jobNumber,
//								@RequestParam(required = true, value = "subcontractNo") String subcontractNo, 
//								@RequestParam(required = true, value = "sequenceNo") String sequenceNo, 
//								@RequestParam(required = true, value = "nameObject") String nameObject,
//								@RequestParam(required = true, value = "textKey") String textKey,
//								@RequestParam(required = true, value = "createdUser") String createdUser,
//								@RequestParam("files") List<MultipartFile> multipartFiles,
//								HttpServletRequest request, HttpServletResponse response) throws Exception {
//		logger.info("Upload SC Attachment - START");
//
//		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
//		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
//
////		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
//
//		for (MultipartFile multipartFile : multipartFiles) {
//			byte[] file = multipartFile.getBytes();
//			if (file != null) {
//
//				UploadSCAttachmentResponseObj result = attachmentRepository.uploadAttachment(nameObject, textKey, new Integer(sequenceNo), multipartFile.getOriginalFilename(), file, createdUser);
//				Map<String, Object> resultMap = new HashMap<String, Object>();
//				if (result != null) {
//					resultMap.put("success", true);
//					resultMap.put("fileName", multipartFile.getOriginalFilename());
//					logger.info("Upload Attachment: success.");
//				} else {
//					resultMap.put("success", false);
//					logger.info("error: ");
//				}
////				response.setContentType("text/html");
//				response.getWriter().print((new Gson()).toJson(result));
//				logger.info("Upload SC Attachment -END");
//			}
//		}
//	}
}
