package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gammon.pcms.dto.rs.provider.request.ObtainSubcontractListRequest;
import com.gammon.pcms.dto.rs.provider.response.ObtainSubcontractListResponse;
import com.gammon.pcms.dto.rs.provider.response.QSServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.PackageService;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;

@RestController
@RequestMapping(path = "ws")
public class ObtainSubcontractListController {
	Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private RestTemplate restTemplate;

	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private PackageService packageService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private XmlMapper xmlMapper;
	@Value("${qs.ws.username}")
	private String qsWsUsername;
	
	/**
	 * Obtain SCListWrapper with jobNumber from Packagerepository.retrieveSubcontractList(jobNumber) and return packageNo, vendorNo, VendorName 
	 * as List of ObtainSubcontractListResponse
	 * 
	 * @param jobNumber
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = "ObtainSubcontractList" , method = {RequestMethod.POST,RequestMethod.GET})
	public QSServiceResponse obtainSubcontractList(@RequestParam(required = false) String jobNumber, 
			HttpServletRequest request, HttpServletResponse response) {
		QSServiceResponse serviceResponse = null;
		String message = null;
		ObjectMapper mapper = StringUtils.right(request.getRequestURI(), 4).equals(".xml") ? xmlMapper : objectMapper;
		try {
			// if @RequestParam is empty read the POST data
			if(StringUtils.isEmpty(jobNumber)){
				ObtainSubcontractListRequest requestObject = mapper.readValue(request.getInputStream(), ObtainSubcontractListRequest.class);
				jobNumber = requestObject.getJobNumber();
				if (StringUtils.isEmpty(jobNumber)) {
					message = "jobNumber cannot be null";
					logger.error(message);
					throw new IllegalArgumentException("jobNumber cannot be null");
				}
			}
			SubcontractListWrapper searchWrapper = new SubcontractListWrapper();
			searchWrapper.setJobNumber(jobNumber);
			// shared a service with Subcontract Enquiry ("" Month & Year means obtaining the current period instead of subcontract snapshot)
			searchWrapper.setMonth("");
			searchWrapper.setYear("");
			// passing web service name as username
			searchWrapper.setUsername(qsWsUsername);

			List<SCListWrapper> scListWrapperList = packageService.obtainSubcontractList(searchWrapper);
			List<ObtainSubcontractListResponse> filteredResults = new ArrayList<ObtainSubcontractListResponse>();
			for (SCListWrapper scListWrapper : scListWrapperList) {
				ObtainSubcontractListResponse filteredResult = new ObtainSubcontractListResponse();
				filteredResult.setSubcontractNo(scListWrapper.getPackageNo());
				filteredResult.setSubcontractorNo(scListWrapper.getVendorNo());
				filteredResult.setSubcontractorName(scListWrapper.getVendorName());
				filteredResults.add(filteredResult);
			}
			message = mapper.writeValueAsString(filteredResults);
			serviceResponse = new QSServiceResponse(QSServiceResponse.Status.SUCCESS, message);
		} catch (IllegalArgumentException e) {
			message = "Illegal Argument:\n" + e;
			logger.error(message);
			serviceResponse = new QSServiceResponse(QSServiceResponse.Status.FAILED, message);
		} catch (Exception e) {
			message = "Exception:\n" + e;
			logger.error(message);
			serviceResponse = new QSServiceResponse(QSServiceResponse.Status.FAILED, message);
		} 
		return serviceResponse;
	}
	
	@RequestMapping(path = "ObtainSubcontractList/{jobNumber}" , method = {RequestMethod.GET})
	public QSServiceResponse obtainSubcontractList(HttpServletRequest request, @PathVariable String jobNumber) {
		ObtainSubcontractListRequest requestObj = new ObtainSubcontractListRequest();
		requestObj.setJobNumber(jobNumber);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		QSServiceResponse serviceResponse = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/ObtainSubcontractList", requestObj, QSServiceResponse.class);
		return serviceResponse;	
	}
}
