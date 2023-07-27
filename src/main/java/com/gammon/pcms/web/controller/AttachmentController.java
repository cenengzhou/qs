package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gammon.pcms.model.Attachment;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.admin.MailContentGenerator;
import com.google.gson.Gson;

@RestController
@RequestMapping(path = { "service/attachment/", "ws/attachment/" })
public class AttachmentController {

	private Logger logger = Logger.getLogger(getClass());
	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private MailContentGenerator mailContentGenerator;

	/**
	 * @author Henry Lai created on 17-10-2014
	 * @parameter: response - HttpServletResponse object that contains the http
	 *             servlet response
	 */
	private void showAttachmentError(HttpServletResponse response) {
		try {
			response.getOutputStream().print("<html><head><title>Attachment not found</title></head>");
			response.getOutputStream().print("<script>alert(\"Attachment not found\");window.close();");
			response.getOutputStream().print("</script></html>");
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','obtainAttachmentList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainAttachmentList", method = RequestMethod.POST)
	public List<Attachment> obtainAttachmentList(@RequestParam String nameObject, @RequestParam String textKey)
			throws Exception {
		List<Attachment> result = attachmentService.obtainAttachmentList(nameObject, textKey);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteAttachment', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsDoc())")
	@RequestMapping(value = "deleteAttachment", method = RequestMethod.POST)
	public Boolean deleteAttachment(@RequestParam String nameObject, @RequestParam String textKey,
			@RequestParam Integer sequenceNumber) throws Exception {
		Boolean result = attachmentService.deleteAttachment(nameObject, textKey, sequenceNumber);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','deleteAttachment', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteAttachmentAdmin", method = RequestMethod.POST)
	public Boolean deleteAttachmentAdmin(@RequestParam String nameObject, @RequestParam String textKey,
			@RequestParam Integer sequenceNumber) throws Exception {
		Boolean result = attachmentService.deleteAttachment(nameObject, textKey, sequenceNumber);
		mailContentGenerator.sendAdminFnEmail("deleteAttachmentAdmin",
				nameObject + "-" + textKey + "-" + sequenceNumber);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadTextAttachment', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsDoc())")
	@RequestMapping(value = "uploadTextAttachment", method = RequestMethod.POST)
	public Boolean uploadTextAttachment(@RequestParam String nameObject, @RequestParam String textKey,
			@RequestParam Integer sequenceNo, @RequestParam String fileName, @RequestParam String textAttachment)
			throws Exception {
		Boolean result = attachmentService.uploadTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','uploadAttachment', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsDoc())")
	@RequestMapping(value = "uploadAttachment", method = RequestMethod.POST)
	public void uploadAttachment(
			@RequestParam(required = true, value = "nameObject") String nameObject,
			@RequestParam(required = true, value = "textKey") String textKey,
			@RequestParam(required = true, value = "sequenceNo") String sequenceNo,
			@RequestParam(required = false, value = "") String refilename,
			@RequestParam("files") List<MultipartFile> multipartFiles,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload uploadAttachment - START");

		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);

		// List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);
		int sn = Integer.valueOf(sequenceNo);
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {
				String originalFilename = multipartFile.getOriginalFilename();
				int extIndex = originalFilename.lastIndexOf(".");
				String ext = extIndex > 0 ? originalFilename.substring(extIndex) : "";
				String filename = StringUtils.isEmpty(refilename) ? originalFilename : refilename + ext;
				boolean result = attachmentService.uploadAttachment(nameObject, textKey, new BigDecimal(sn), filename, file,
						null);

				sn++;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (result) {
					resultMap.put("success", true);
					resultMap.put("fileName", multipartFile.getOriginalFilename());
					logger.info("Upload " + multipartFile.getOriginalFilename() + " success.");
				} else {
					resultMap.put("success", false);
					logger.error("error: " + multipartFile.getOriginalFilename());
				}
				// response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(result));
				logger.info("Upload uploadAttachment -END");
			}
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('AttachmentController','obtainFileAttachment', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainFileAttachment", method = RequestMethod.GET)
	public void obtainFileAttachment(@RequestParam(required = true, value = "nameObject") String nameObject,
			@RequestParam(required = true, value = "textKey") String textKey,
			@RequestParam(required = true, value = "sequenceNo") String sequenceNoString,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("generateSCAttachment");

		try {

			Integer sequenceNo = Integer.parseInt(sequenceNoString);
			AttachmentFile attachmentFile = attachmentService.obtainFileAttachment(nameObject,
					URLDecoder.decode(textKey, "UTF-8"), sequenceNo);

			if (attachmentFile != null) {
				byte[] file = attachmentFile.getBytes();
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setContentLength(file.length);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + attachmentFile.getFileName() + "\"");

				response.getOutputStream().write(file);
				response.getOutputStream().flush();
			} else {
				showAttachmentError(response);
			}
		} catch (Exception e) {
			logger.info("WEB LAYER EXCEPTION ");
			e.printStackTrace();
			logger.info("Error: " + e.getLocalizedMessage());
			showAttachmentError(response);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "CheckAndCopyToSharePoint/{jobNo}", method = { RequestMethod.GET }, produces = {
			MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public String CheckAndCopyToSharePoint(@PathVariable("jobNo") String jobNo, boolean sendAlertEmail) {
		return attachmentService.CheckAndCopyToSharePoint(jobNo, null, null, sendAlertEmail);
	}

	@RequestMapping(value = "CheckAndCopyToSharePoint/{jobNo}/{subcontractNo}", method = {
			RequestMethod.GET }, produces = {
			MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public String CheckAndCopyToSharePoint(@PathVariable("jobNo") String jobNo, @PathVariable("subcontractNo") String subcontractNo, boolean sendAlertEmail) {
		return attachmentService.CheckAndCopyToSharePoint(jobNo, subcontractNo, null, sendAlertEmail);
	}

	@RequestMapping(value = "CheckAndCopyToSharePoint/{jobNo}/{subcontractNo}/{paymentCertNo}", method = {
			RequestMethod.GET }, produces = {
			MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public String CheckAndCopyToSharePoint(@PathVariable("jobNo") String jobNo, @PathVariable("subcontractNo") String subcontractNo, @PathVariable("paymentCertNo") String paymentCertNo, boolean sendAlertEmail) {
		return attachmentService.CheckAndCopyToSharePoint(jobNo, subcontractNo, paymentCertNo, sendAlertEmail);
	}

	@RequestMapping(value = "CheckAndCopyToSharePoint", method = { RequestMethod.POST }, produces = {
			MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public String CheckAndCopyToSharePoint(@RequestBody(required = false) Map<String, Map<String, String[]>> jobsFilterMap, boolean sendAlertEmail) {
		return attachmentService.CheckAndCopyToSharePoint(jobsFilterMap, sendAlertEmail);
	}
}
