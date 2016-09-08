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

import com.gammon.pcms.dto.rs.provider.request.ap.AwardSCPackageRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CheckJobIsConvertedRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteAddendumApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteMainCertApprovalRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteSCPaymentRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.CompleteSplitTerminateRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.GetAttachmentListRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.GetTextAttachmentRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForAddendumServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForAwardServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForMainCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForPaymentCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForPaymentServiceRequest;
import com.gammon.pcms.dto.rs.provider.request.ap.MakeHTMLStrForSplitTerminateServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.ap.AwardSCPackageResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CheckJobIsConvertedResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteAddendumApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteMainCertApprovalResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteSCPaymentResponse;
import com.gammon.pcms.dto.rs.provider.response.ap.CompleteSplitTerminateResponse;
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
import com.gammon.pcms.service.HTMLService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.AttachPayment;
import com.gammon.qs.domain.AttachSubcontract;
import com.gammon.qs.domain.AttachSubcontractDetail;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.AddendumService;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.MainCertService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractService;

@RestController
public class APController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JobInfoService jobInfoService;
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
	@RequestMapping(path = "/ws/awardSCPackage",
					method = { RequestMethod.GET, RequestMethod.POST })
	public AwardSCPackageResponse awardSCPackage(@Valid @RequestBody AwardSCPackageRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		AwardSCPackageResponse responseObj = new AwardSCPackageResponse();
		responseObj.setCompleted(subcontractService.toCompleteSCAwardApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvedOrRejected
	 * @return
	 */
	@RequestMapping(path = "/ws/awardSCPackage/{jobNumber}/{packageNo}/{approvedOrRejected}", method = RequestMethod.GET)
	public AwardSCPackageResponse awardSCPackage(	HttpServletRequest request,
															@PathVariable String jobNumber,
															@PathVariable String packageNo,
															@PathVariable String approvedOrRejected) {
		AwardSCPackageRequest requestObj = new AwardSCPackageRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvedOrRejected);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		AwardSCPackageResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/awardSCPackage", requestObj, AwardSCPackageResponse.class);
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
	@RequestMapping(path = "/ws/checkJobIsConverted", method = { RequestMethod.GET, RequestMethod.POST })
	public CheckJobIsConvertedResponse checkJobIsConverted(@Valid @RequestBody CheckJobIsConvertedRequest requestObj, BindingResult result) throws DatabaseOperationException {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CheckJobIsConvertedResponse responseObj = new CheckJobIsConvertedResponse();
		JobInfo job = null;
		job = jobInfoService.obtainJob(requestObj.getJobNumber());
		if (job != null && job.getConversionStatus() != null)
			responseObj.setConverted(true);
		else
			responseObj.setConverted(false);
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @return
	 */
	@RequestMapping(path = "/ws/checkJobIsConverted/{jobNumber}", method = RequestMethod.GET)
	public CheckJobIsConvertedResponse checkJobIsConverted(HttpServletRequest request, @PathVariable String jobNumber) {
		CheckJobIsConvertedRequest requestObj = new CheckJobIsConvertedRequest();
		requestObj.setJobNumber(jobNumber);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CheckJobIsConvertedResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/checkJobIsConverted", requestObj, CheckJobIsConvertedResponse.class);
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
	@RequestMapping(path = "/ws/completeAddendumApproval/{jobNumber}/{packageNo}/{user}/{approvalDecision}", method = RequestMethod.GET)
	public CheckJobIsConvertedResponse completeAddendumApproval(HttpServletRequest request,
																@PathVariable String jobNumber,
																@PathVariable String packageNo,
																@PathVariable String user,
																@PathVariable String approvalDecision) {
		CompleteAddendumApprovalRequest requestObj = new CompleteAddendumApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setUser(user);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CheckJobIsConvertedResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeAddendumApproval", requestObj, CheckJobIsConvertedResponse.class);
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
	@RequestMapping(path = "/ws/completeMainCertApproval/{jobNumber}/{mainCertNo}/{approvalDecision}", method = RequestMethod.GET)
	public CompleteMainCertApprovalResponse completeMainCertApproval(	HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String mainCertNo,
																		@PathVariable String approvalDecision) {
		CompleteMainCertApprovalRequest requestObj = new CompleteMainCertApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNo);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
	@RequestMapping(path = "/ws/completeSCPayment", method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteSCPaymentResponse completeSCPayment(@Valid @RequestBody CompleteSCPaymentRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteSCPaymentResponse responseObj = new CompleteSCPaymentResponse();
		responseObj.setCompleted(paymentService.toCompleteSCPayment(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovalDecision()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvedOrRejected
	 * @return
	 */
	@RequestMapping(path = "/ws/completeSCPayment/{jobNumber}/{packageNo}/{approvalDecision}", method = RequestMethod.GET)
	public CompleteSCPaymentResponse completeSCPayment(	HttpServletRequest request,
														@PathVariable String jobNumber,
														@PathVariable String packageNo,
														@PathVariable String approvedOrRejected) {
		CompleteSCPaymentRequest requestObj = new CompleteSCPaymentRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovalDecision(approvedOrRejected);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CompleteSCPaymentResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/completeSCPayment", requestObj, CompleteSCPaymentResponse.class);
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
	@RequestMapping(path = "/ws/completeSplitTerminate", method = { RequestMethod.GET, RequestMethod.POST })
	public CompleteSplitTerminateResponse completeSplitTerminate(@Valid @RequestBody CompleteSplitTerminateRequest requestObj, BindingResult result) throws Exception {
		if (result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteSplitTerminateResponse responseObj = new CompleteSplitTerminateResponse();
		responseObj.setCompleted(subcontractService.toCompleteSplitTerminate(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected(), requestObj.getSplitOrTerminate()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and create request object then post via RestTemplate
	 * 
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvedOrRejected
	 * @return
	 */
	@RequestMapping(path = "/ws/completeSplitTerminate/{jobNumber}/{packageNo}/{approvedOrRejected}/{splitOrTerminate}", method = RequestMethod.GET)
	public CompleteSplitTerminateResponse completeSplitTerminate(	HttpServletRequest request,
																	@PathVariable String jobNumber,
																	@PathVariable String packageNo,
																	@PathVariable String approvedOrRejected,
																	@PathVariable String splitOrTerminate) {
		CompleteSplitTerminateRequest requestObj = new CompleteSplitTerminateRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvedOrRejected);
		requestObj.setSplitOrTerminate(splitOrTerminate);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CompleteSplitTerminateResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/awardSCPackage", requestObj, CompleteSplitTerminateResponse.class);
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
		List<? extends AbstractAttachment> attachmentList;
			if (AttachSubcontract.SCDetailsNameObject.equals(requestObj.getNameObject())) {
				logger.info("Web Service called by AP: SCDetail Attachments");
				attachmentList = attachmentService.getAddendumAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
			} else if (AttachSubcontract.SCPaymentNameObject.equals(requestObj.getNameObject())) {
				logger.info("Web Service called by AP: SCPayment Attachments");
				attachmentList = attachmentService.getPaymentAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
			} else {
				logger.info("Web Service called by AP: SCPackage Attachments / Vendor Attachments (@JDE)");
				attachmentList = attachmentService.getAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
			}
	
			if (attachmentList != null && attachmentList.size() > 0) {
				responseListObj.setAttachmentList(new ArrayList<GetAttachmentListResponse>());
	
				for (AbstractAttachment attachment : attachmentList) {
					GetAttachmentListResponse responseObj = new GetAttachmentListResponse();
					if (attachment instanceof AttachSubcontractDetail)
						responseObj.setTextKey(requestObj.getTextKey() + "|" + ((AttachSubcontractDetail) attachment).getSubcontractDetail().getSequenceNo());
					else if (attachment instanceof AttachPayment)
						responseObj.setTextKey(requestObj.getTextKey() + "|" + ((AttachPayment) attachment).getPaymentCert().getPaymentCertNo().toString());
					else
						responseObj.setTextKey(requestObj.getTextKey());
	
					responseObj.setDocumentType(attachment.getDocumentType());
					responseObj.setFileLink(attachment.getFileLink());
					responseObj.setFileName(attachment.getFileName());
					responseObj.setSequenceNo(attachment.getSequenceNo());
					logger.info("Response - Text Key: " + responseObj.getTextKey() + " Document Type: " + responseObj.getDocumentType() + " File Link: " + responseObj.getFileLink());
	
					responseListObj.getAttachmentList().add(responseObj);
				}
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
		responseObj.setTextAttachment(attachmentService.obtainTextAttachmentContent(requestObj.getNameObject(), requestObj.getTextKey(), requestObj.getSequenceNo()));
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
	public GetTextAttachmentResponse getTextAttachmentRequest(	HttpServletRequest request,
																@PathVariable String nameObject,
																@PathVariable String textKey,
																@PathVariable Integer sequenceNo) {
		GetTextAttachmentRequest requestObj = new GetTextAttachmentRequest();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(sequenceNo);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
	@RequestMapping(path = "/ws/makeHTMLStrForAddendum/{jobNumber}/{packageNo}/{htmlVersion}",
					method = RequestMethod.GET)
	public MakeHTMLStrForAddendumServiceResponse makeHTMLStrForAddendum(HttpServletRequest request,
																		@PathVariable String jobNumber,
																		@PathVariable String packageNo,
																		@PathVariable String htmlVersion) {
		MakeHTMLStrForAddendumServiceRequest requestObj = new MakeHTMLStrForAddendumServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
	@RequestMapping(path = "/ws/makeHTMLStrForPaymentCert/{jobNumber}/{packageNo}/{paymentNo}{htmlVersion}", method = RequestMethod.GET)
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
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
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		MakeHTMLStrForMainCertServiceResponse responseObj = restTemplate.postForObject("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/makeHTMLStrForMainCert", requestObj, MakeHTMLStrForMainCertServiceResponse.class);
		return responseObj;
	}
}
