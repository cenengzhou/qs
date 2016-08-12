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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.AttachmentService;
import com.google.gson.Gson;

@RestController
@RequestMapping(value = "service/attachment/")
public class AttachmentController {

	private Logger logger = Logger.getLogger(AttachmentController.class.getName());
	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";
	
	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "getRepackagingAttachments", method = RequestMethod.POST)
	public List<AttachRepackaging> getRepackagingAttachments(@RequestParam Long repackagingEntryID) {
		List<AttachRepackaging> resultList = new ArrayList<AttachRepackaging>();
		try{
			resultList.addAll(attachmentService.getRepackagingAttachments(repackagingEntryID));
		} catch (Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
	
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
	
	@RequestMapping(value = "deleteRepackagingAttachment", method = RequestMethod.POST)
	public Boolean deleteRepackagingAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo){
		try {
			return attachmentService.deleteRepackagingAttachment(repackagingEntryID, sequenceNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping(value = "saveRepackagingTextAttachment", method = RequestMethod.POST)
	public Boolean saveRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment){
		try{
			return attachmentService.saveRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value = "addRepackagingTextAttachment", method = RequestMethod.POST)
	public Integer addRepackagingTextAttachment(@RequestParam Long repackagingEntryID, @RequestParam Integer sequenceNo, @RequestParam String fileName, String textAttachment){
		try{
			return attachmentService.addRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
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
