package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.scPackage.UploadSCAttachmentResponseObj;
import com.google.gson.Gson;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/attachment/")
public class AttachmentController {

	private Logger logger = Logger.getLogger(AttachmentController.class.getName());
	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";
	
	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "getRepackagingAttachments", method = RequestMethod.POST)
	public List<AttachRepackaging> getRepackagingAttachments(@RequestParam Long repackagingEntryID) throws Exception {
		List<AttachRepackaging> resultList = new ArrayList<AttachRepackaging>();
		resultList.addAll(attachmentService.getRepackagingAttachments(repackagingEntryID));
		return resultList;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteRepackagingAttachment", method = RequestMethod.POST)
	public Boolean deleteRepackagingAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo) throws Exception{
		return attachmentService.deleteRepackagingAttachment(repackagingEntryID, sequenceNo);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveRepackagingTextAttachment", method = RequestMethod.POST)
	public Boolean saveRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment) throws Exception{
		return attachmentService.saveRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addRepackagingTextAttachment", method = RequestMethod.POST)
	public Integer addRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, String textAttachment) throws Exception{
		return attachmentService.addRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
	}
	
	@RequestMapping(value = "getAttachmentListForPCMS", method = RequestMethod.POST)
	public List<? extends AbstractAttachment> getAttachmentListForPCMS(@RequestParam String nameObject, @RequestParam String textKey) throws Exception {
		return attachmentService.getAttachmentListForPCMS(nameObject, textKey);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteAttachment", method = RequestMethod.POST)
	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception{
		return attachmentService.deleteAttachment(nameObject, textKey,sequenceNumber);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "uploadTextAttachment", method = RequestMethod.POST)
	public Boolean uploadTextAttachment(@RequestParam String nameObject, @RequestParam String textKey, 
				@RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment ) throws Exception{
		return attachmentService.uploadTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment);
	}
	
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
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteMainCertAttachment", method = RequestMethod.POST)
	public Boolean deleteMainCertAttachment(@RequestParam String noJob, Integer noMainCert, Integer noSequence) throws DatabaseOperationException {
		return attachmentService.deleteMainCertAttachment(noJob, noMainCert,noSequence);
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
	
}
