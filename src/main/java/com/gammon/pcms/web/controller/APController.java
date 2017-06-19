/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * APController.java
 * @since Jun 27, 2016 5:10:04 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteAddendumApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteAwardApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteMainCertApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompletePaymentApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteSplitTerminateApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.GetAttachmentListRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.GetTextAttachmentRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForAddendumServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForAwardServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForMainCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForPaymentCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForPaymentServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForSplitTerminateServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteAddendumApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteAwardApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteMainCertApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompletePaymentApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteSplitTerminateApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.GetAttachmentListResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.GetAttachmentListResponseList;
import com.gammon.pcms.dto.rs.provider.response.ap.GetTextAttachmentResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForAddendumServiceResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForAwardServiceResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForMainCertServiceResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForPaymentCertServiceResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForPaymentServiceResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.MakeHTMLStrForSplitTerminateServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.model.Attachment;
import com.gammon.pcms.service.HTMLService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.AddendumService;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.MainCertService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractService;

@RestController
public class APController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private AddendumService addendumService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private MainCertService mainCertService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private HTMLService htmlService;
	@Autowired
	private AttachmentConfig attachmentConfig;
	@Autowired
	RestTemplateHelper restTemplateHelper;

	private RestTemplate restTemplate;

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/completeAwardApproval",
					method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteAwardApprovalResponse completeAwardApproval(@Valid @RequestBody CompleteAwardApprovalRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteAwardApprovalResponse responseObj = new CompleteAwardApprovalResponse();
		responseObj.setCompleted(subcontractService.toCompleteSCAwardApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = {"/ws/completeAwardApproval/{jobNumber}/{packageNo}/{approvalDecision}",
							"service/ap/completeAwardApproval/{jobNumber}/{packageNo}/{approvalDecision}"}, 
							method = RequestMethod.GET)
	public CompleteAwardApprovalResponse completeAwardApproval(	HttpServletRequest request,
															@PathVariable String jobNumber,
															@PathVariable String packageNo,
															@PathVariable String approvalDecision) {
		CompleteAwardApprovalRequest requestObj = new CompleteAwardApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		CompleteAwardApprovalResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeAwardApproval", requestObj, CompleteAwardApprovalResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/completeAddendumApproval", method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteAddendumApprovalResponse completeAddendumApproval(@Valid @RequestBody CompleteAddendumApprovalRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteAddendumApprovalResponse responseObj = new CompleteAddendumApprovalResponse();
		responseObj.setCompleted(addendumService.toCompleteAddendumApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getUser(), requestObj.getApprovalDecision()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param user
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = {"/ws/completeAddendumApproval/{jobNumber}/{packageNo}/{user}/{approvalDecision}",
							"service/ap/completeAddendumApproval/{jobNumber}/{packageNo}/{user}/{approvalDecision}"},
							method = RequestMethod.GET)
	public CompleteAddendumApprovalResponse completeAddendumApproval(HttpServletRequest request,
																@PathVariable String jobNumber,
																@PathVariable String packageNo,
																@PathVariable String user,
																@PathVariable String approvalDecision) {
		CompleteAddendumApprovalRequest requestObj = new CompleteAddendumApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setUser(user);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		CompleteAddendumApprovalResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeAddendumApproval", requestObj, CompleteAddendumApprovalResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws DatabaseOperationException 
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/completeMainCertApproval", method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteMainCertApprovalResponse completeMainCertApproval(@Valid @RequestBody CompleteMainCertApprovalRequest requestObj, BindingResult result) throws DatabaseOperationException {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteMainCertApprovalResponse response = new CompleteMainCertApprovalResponse();
		response.setCompleted(mainCertService.toCompleteMainCertApproval(requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getApprovalDecision()));
		return response;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param mainCertNo
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = {"/ws/completeMainCertApproval/{jobNumber}/{mainCertNo}/{approvalDecision}",
							"service/ap/completeMainCertApproval/{jobNumber}/{mainCertNo}/{approvalDecision}"},
							method = RequestMethod.GET)
	public CompleteMainCertApprovalResponse completeMainCertApproval(	HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String mainCertNo,
																		@PathVariable String approvalDecision) {
		CompleteMainCertApprovalRequest requestObj = new CompleteMainCertApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNo);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		CompleteMainCertApprovalResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeMainCertApproval", requestObj, CompleteMainCertApprovalResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/completePaymentApproval", method = { RequestMethod.GET, RequestMethod.POST })
	public CompletePaymentApprovalResponse completePaymentApproval(@Valid @RequestBody CompletePaymentApprovalRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompletePaymentApprovalResponse responseObj = new CompletePaymentApprovalResponse();
		responseObj.setCompleted(paymentService.toCompleteSCPayment(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovalDecision()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = {"/ws/completePaymentApproval/{jobNumber}/{packageNo}/{approvalDecision}",
							"service/ap/completePaymentApproval/{jobNumber}/{packageNo}/{approvalDecision}"}, 
							method = RequestMethod.GET)
	public CompletePaymentApprovalResponse completePaymentApproval(	HttpServletRequest request,
														@PathVariable String jobNumber,
														@PathVariable String packageNo,
														@PathVariable String approvalDecision) {
		CompletePaymentApprovalRequest requestObj = new CompletePaymentApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		CompletePaymentApprovalResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completePaymentApproval", requestObj, CompletePaymentApprovalResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/completeSplitTerminateApproval", method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteSplitTerminateApprovalResponse completeSplitTerminateApproval(@Valid @RequestBody CompleteSplitTerminateApprovalRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteSplitTerminateApprovalResponse responseObj = new CompleteSplitTerminateApprovalResponse();
		responseObj.setCompleted(subcontractService.toCompleteSplitTerminate(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected(), requestObj.getSplitOrTerminate()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = {"/ws/completeSplitTerminateApproval/{jobNumber}/{packageNo}/{approvalDecision}/{splitOrTerminate}",
							"service/ap/completeSplitTerminateApproval/{jobNumber}/{packageNo}/{approvalDecision}/{splitOrTerminate}"},
							method = RequestMethod.GET)
	public CompleteSplitTerminateApprovalResponse completeSplitTerminateApproval(	HttpServletRequest request,
																	@PathVariable String jobNumber,
																	@PathVariable String packageNo,
																	@PathVariable String approvalDecision,
																	@PathVariable String splitOrTerminate) {
		CompleteSplitTerminateApprovalRequest requestObj = new CompleteSplitTerminateApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvalDecision);
		requestObj.setSplitOrTerminate(splitOrTerminate);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		CompleteSplitTerminateApprovalResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeSplitTerminateApproval", requestObj, CompleteSplitTerminateApprovalResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/getAttachmentList", method = { RequestMethod.GET, RequestMethod.POST })
	public GetAttachmentListResponseList getAttachmentList(@Valid @RequestBody GetAttachmentListRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		GetAttachmentListResponseList responseListObj = new GetAttachmentListResponseList();
			logger.info("Web Service called by AP: getAttachmentList");
			List<Attachment> attachmentList = attachmentService.obtainAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
			if (attachmentList != null && attachmentList.size() > 0) {
				List<GetAttachmentListResponse> responseAttachmentList = new ArrayList<GetAttachmentListResponse>();
				String serverPath = attachmentConfig.getAttachmentServer("PATH")+attachmentConfig.getJobAttachmentsDirectory();
				for (Attachment attachment : attachmentList) {
					GetAttachmentListResponse responseObj = new GetAttachmentListResponse();
					switch(requestObj.getNameObject()){
					case Attachment.AddendumNameObject:
					case Attachment.SCPackageNameObject:
						responseObj.setTextKey(requestObj.getTextKey() + "|" +  attachment.getNoSequence());
					case Attachment.SCPaymentNameObject:
						responseObj.setTextKey(requestObj.getTextKey() + "|--|" +  attachment.getNoSequence());
						break;
					default:
						responseObj.setTextKey(requestObj.getTextKey());
					}
					
					responseObj.setDocumentType(Integer.valueOf(attachment.getTypeDocument()));
					responseObj.setFileLink(responseObj.getDocumentType() == Integer.valueOf(Attachment.FILE) ? serverPath + attachment.getPathFile() : attachment.getPathFile());
					responseObj.setFileName(attachment.getNameFile());
					responseObj.setSequenceNo(attachment.getNoSequence().intValue());
					logger.info("Response - Text Key: " + responseObj.getTextKey() + " Document Type: " + responseObj.getDocumentType() + " File Link: " + responseObj.getFileLink());
					responseAttachmentList.add(responseObj);
				}
				responseListObj.setAttachmentList(responseAttachmentList);
			}
		return responseListObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param nameObject
	 * @param textKey
	 * @return
	 */
	@RequestMapping(path = "/ws/getAttachmentList/{nameObject}/{textKey}", method = RequestMethod.GET)
	public GetAttachmentListResponseList getAttachmentList(	HttpServletRequest request,
															@PathVariable String nameObject,
															@PathVariable String textKey) {
		GetAttachmentListRequest requestObj = new GetAttachmentListRequest();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		GetAttachmentListResponseList responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/getAttachmentList", requestObj, GetAttachmentListResponseList.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/ws/getTextAttachment",
					method = { RequestMethod.GET, RequestMethod.POST })
	public GetTextAttachmentResponse getTextAttachment(@Valid @RequestBody GetTextAttachmentRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		GetTextAttachmentResponse responseObj = new GetTextAttachmentResponse();
		AttachmentFile attachmentFile = attachmentService.obtainFileAttachment(requestObj.getNameObject(), requestObj.getTextKey(), requestObj.getSequenceNo());
		responseObj.setTextAttachment(new String(attachmentFile.getBytes()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param nameObject
	 * @param textKey
	 * @param sequenceNo
	 * @return
	 */
	@RequestMapping(path = "/ws/getTextAttachment/{nameObject}/{textKey}/{sequenceNo}", method = RequestMethod.GET)
	public GetTextAttachmentResponse getTextAttachment(	HttpServletRequest request,
																@PathVariable String nameObject,
																@PathVariable String textKey,
																@PathVariable Integer sequenceNo) {
		GetTextAttachmentRequest requestObj = new GetTextAttachmentRequest();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(sequenceNo);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		GetTextAttachmentResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/getTextAttachment", requestObj, GetTextAttachmentResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForAward", "/service/html/makeHTMLStrForAward"},
					method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForAwardServiceResponse makeHTMLStrForAward(@Valid @RequestBody MakeHTMLStrForAwardServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForAwardServiceResponse responseObj = new MakeHTMLStrForAwardServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForTenderAnalysis(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForAward/{jobNumber}/{packageNo}/{htmlVersion}",
					method = RequestMethod.GET)
	public MakeHTMLStrForAwardServiceResponse makeHTMLStrForAward(	HttpServletRequest request,
																	@PathVariable String jobNumber,
																	@PathVariable String packageNo,
																	@PathVariable String htmlVersion) {
		MakeHTMLStrForAwardServiceRequest requestObj = new MakeHTMLStrForAwardServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForAwardServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForAward", requestObj, MakeHTMLStrForAwardServiceResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForAddendum", "/service/html/makeHTMLStrForAddendum"},
					method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForAddendumServiceResponse makeHTMLStrForAddendum(@Valid @RequestBody MakeHTMLStrForAddendumServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForAddendumServiceResponse responseObj = new MakeHTMLStrForAddendumServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForAddendumApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getAddendumNo(), requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForAddendum/{jobNumber}/{packageNo}/{addendumNo}/{htmlVersion}",
					method = RequestMethod.GET)
	public MakeHTMLStrForAddendumServiceResponse makeHTMLStrForAddendum(HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String packageNo,
																		@PathVariable Long addendumNo,
																		@PathVariable String htmlVersion) {
		MakeHTMLStrForAddendumServiceRequest requestObj = new MakeHTMLStrForAddendumServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setAddendumNo(addendumNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForAddendumServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForAddendum", requestObj, MakeHTMLStrForAddendumServiceResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForPayment", "/service/html/makeHTMLStrForPayment"},
					method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForPaymentServiceResponse makeHTMLStrForPayment(@Valid @RequestBody MakeHTMLStrForPaymentServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForPaymentServiceResponse responseObj = new MakeHTMLStrForPaymentServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), null, requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param mainCertNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForPayment/{jobNumber}/{packageNo}/{htmlVersion}", method = RequestMethod.GET)
	public MakeHTMLStrForPaymentServiceRequest makeHTMLStrForPayment(	HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String packageNo,
																		@PathVariable String htmlVersion) {
		MakeHTMLStrForPaymentServiceRequest requestObj = new MakeHTMLStrForPaymentServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForPaymentServiceRequest responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForPayment", requestObj, MakeHTMLStrForPaymentServiceRequest.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForPaymentCert", "/service/html/makeHTMLStrForPaymentCert"}, method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForPaymentCertServiceResponse makeHTMLStrForPaymentCert(@Valid @RequestBody MakeHTMLStrForPaymentCertServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForPaymentCertServiceResponse responseObj = new MakeHTMLStrForPaymentCertServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getPaymentNo(), requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param paymentNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForPaymentCert/{jobNumber}/{packageNo}/{paymentNo}/{htmlVersion}", method = RequestMethod.GET)
	public MakeHTMLStrForPaymentCertServiceResponse makeHTMLStrForPaymentCert(	HttpServletRequest request,
																				@PathVariable String jobNumber,
																				@PathVariable String packageNo,
																				@PathVariable String paymentNo,
																				@PathVariable String htmlVersion) {
		MakeHTMLStrForPaymentCertServiceRequest requestObj = new MakeHTMLStrForPaymentCertServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setPaymentNo(paymentNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForPaymentCertServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForPaymentCert", requestObj, MakeHTMLStrForPaymentCertServiceResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForSplitTerminate", "/service/html/makeHTMLStrForSplitTerminate"}, method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForSplitTerminateServiceResponse makeHTMLStrForSplitTerminate(@Valid @RequestBody MakeHTMLStrForSplitTerminateServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForSplitTerminateServiceResponse responseObj = new MakeHTMLStrForSplitTerminateServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForSplitTermSC(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForSplitTerminate/{jobNumber}/{packageNo}/{htmlVersion}", method = RequestMethod.GET)
	public MakeHTMLStrForSplitTerminateServiceResponse makeHTMLStrForSplitTerminate(HttpServletRequest request,
																					@PathVariable String jobNumber,
																					@PathVariable String packageNo,
																					@PathVariable String htmlVersion) {
		MakeHTMLStrForSplitTerminateServiceRequest requestObj = new MakeHTMLStrForSplitTerminateServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForSplitTerminateServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForSplitTerminate", requestObj, MakeHTMLStrForSplitTerminateServiceResponse.class);
		return responseObj;
	}

	/**
	 * REST mapping
	 * 
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = {"/ws/makeHTMLStrForMainCert", "/service/html/makeHTMLStrForMainCert"}, method = { RequestMethod.GET, RequestMethod.POST })
	public MakeHTMLStrForMainCertServiceResponse makeHTMLStrForMainCert(@Valid @RequestBody MakeHTMLStrForMainCertServiceRequest requestObj, BindingResult result) {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForMainCertServiceResponse responseObj = new MakeHTMLStrForMainCertServiceResponse();
		try{
			responseObj.setHtmlStr(htmlService.makeHTMLStringForMainCert(requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getHtmlVersion()));
		} catch (Exception e){
			e.printStackTrace();
			responseObj.setHtmlStr(e.getMessage());
		}
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param mainCertNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "/ws/makeHTMLStrForMainCert/{jobNumber}/{mainCertNo}/{htmlVersion}", method = RequestMethod.GET)
	public MakeHTMLStrForMainCertServiceResponse makeHTMLStrForMainCert(HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String mainCertNo,
																		@PathVariable String htmlVersion) {
		MakeHTMLStrForMainCertServiceRequest requestObj = new MakeHTMLStrForMainCertServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForAPI(request.getServerName());
		MakeHTMLStrForMainCertServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForMainCert", requestObj, MakeHTMLStrForMainCertServiceResponse.class);
		return responseObj;
	}
}
