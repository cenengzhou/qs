package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetAddressResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetContactPersonResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetGeneralAddressRequestObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetGeneralAddressResponseListObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetGeneralAddressResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddress.GetPhoneNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookList.GetVendorNameListRequestListObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookList.GetVendorNameListRequestObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookList.GetVendorNameListResponseListObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookList.GetVendorNameListResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookWithSCStatus.GetAddressBookWithSCStatusRequestObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookWithSCStatus.GetAddressBookWithSCStatusResponseListObj;
import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookWithSCStatus.GetAddressBookWithSCStatusResponseObj;
import com.gammon.jde.webservice.serviceRequester.ChartTypeQueryManager.GetUCC.GetUCCRequestObj;
import com.gammon.jde.webservice.serviceRequester.ChartTypeQueryManager.GetUCC.GetUCCResponseListObj;
import com.gammon.jde.webservice.serviceRequester.ChartTypeQueryManager.GetUCC.GetUCCResponseObj;
import com.gammon.jde.webservice.serviceRequester.CheckAwardValidationManager.getCheckAwardValidationManager.CheckAwardValidationRequestObj;
import com.gammon.jde.webservice.serviceRequester.CheckAwardValidationManager.getCheckAwardValidationManager.CheckAwardValidationResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetSubcontractorWorkScopeQueryManager.getSubContractorWorkScope_Refactor.GetSubcontractorWSRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetSubcontractorWorkScopeQueryManager.getSubContractorWorkScope_Refactor.GetSubcontractorWSResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetSubcontractorWorkScopeQueryManager.getSubContractorWorkScope_Refactor.GetSubcontractorWSResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetUpdateAccMasterUsingCodeTypeCodeManager.getUpdateAccMasterUsingCodeTypeCode.UpdateAccMasterByObjSubRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetUpdateAccMasterUsingCodeTypeCodeManager.getUpdateAccMasterUsingCodeTypeCode.UpdateAccMasterByObjSubResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.VendorAddress;
import com.gammon.qs.domain.VendorContactPerson;
import com.gammon.qs.domain.VendorPhoneNumber;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.WorkScopeWrapper;
@Repository
public class MasterListWSDao{
	
	private Logger logger = Logger.getLogger(MasterListWSDao.class.getName());
	@Autowired
	@Qualifier("getAddressBookWithSCStatusWebServiceTemplate")
	private WebServiceTemplate getAddressBookWithSCStatusWebServiceTemplate;
	@Autowired
	@Qualifier("chartTypeQueryWebServiceTemplate")
	private WebServiceTemplate getChartTypeWebServiceTemplate;
	@Autowired
	@Qualifier("addressQueryWebServiceTemplate")
	private WebServiceTemplate getAddressWebServiceTemplate;
	@Autowired
	@Qualifier("checkAwardValidationWebServiceTemplate")
	private WebServiceTemplate checkAwardValidationWebServiceTemplate;
	@Autowired
	@Qualifier("getSubcontractorWorkScopeWebServiceTemplate")
	private WebServiceTemplate getSubcontractorWorkScopeWebServiceTemplate;
	@Autowired
	@Qualifier("getVendorNameListWebServiceTemplate")
	private WebServiceTemplate getVendorNameListWebServiceTemplate;
	@Autowired
	@Qualifier("getValidateAccNumWebServiceTemplate")
	private WebServiceTemplate validateAccNumWebServiceTemplate;
	@Autowired
	@Qualifier("getUpdateAccMasterByObjSubWebServiceTemplate")
	private WebServiceTemplate getUpdateAccMasterByObjSubWebServiceTemplate;
	@Autowired
	private UnitWSDao unitWSDao;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;

	/**
	 * Obtain the full list of Object Codes (Only with Level of Detail = 9)
	 */
	public List<MasterListObject> obtainObjectCodeList() throws Exception {
		
		List<MasterListObject> resultList = new LinkedList<MasterListObject>();
					
		GetUCCRequestObj requestObj = new GetUCCRequestObj();
		requestObj.setObjectAccount("0");
		
		GetUCCResponseListObj responseListObj = (GetUCCResponseListObj) getChartTypeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		for(GetUCCResponseObj responseObj : responseListObj.getGetUCCResponList()){
			MasterListObject masterListObject = new MasterListObject();
			
			masterListObject.setDescription(responseObj.getDescription());
			masterListObject.setObjectCode(responseObj.getObjectCode());
			
			if (responseObj.getLevelOfDetail()!=null && "9".equals(responseObj.getLevelOfDetail().trim()))
				resultList.add(masterListObject);
		}
			
		
		
		
		return resultList;
	}
	
	public List<MasterListSubsidiary> getAllSubsidiaryList() throws Exception{
		
		List<MasterListSubsidiary> resultList = new LinkedList<MasterListSubsidiary>();
				
		
			
		GetUCCRequestObj requestObj = new GetUCCRequestObj();
		requestObj.setSubsidiary("0");
		
		long start = System.currentTimeMillis();			
		GetUCCResponseListObj responseListObj = (GetUCCResponseListObj) getChartTypeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		
		logger.info("Time for call WS:(getAllSubsidiaryList)"+ ((end - start)/1000));
		
		
		for(GetUCCResponseObj responseObj : responseListObj.getGetUCCResponList())
		{
			MasterListSubsidiary masterListSubsidiary = new MasterListSubsidiary();
			
			masterListSubsidiary.setDescription(responseObj.getDescription());
			masterListSubsidiary.setSubsidiaryCode(responseObj.getSubsidiaryCode());
			if (responseObj.getLevelOfDetail()!=null&&"9".equals(responseObj.getLevelOfDetail().trim()))
				resultList.add(masterListSubsidiary);
		}
			
		
		return resultList;
	}
	
	/**
	 * created by Tiky Wong
	 * on 29 July, 2013
	 */
	public MasterListVendor obtainVendor(String addressNumber) throws WebServiceException{
		if(addressNumber==null){
			logger.info("Address Number is null.");
			return null;
		}
		
		//1. Obtain a list of vendor with Address Type(VENDOR_ADDRESS_TYPE): V 
		GetAddressBookWithSCStatusRequestObj requestObj = new GetAddressBookWithSCStatusRequestObj();
		requestObj.setAddressNumber(addressNumber);
		requestObj.setAddressType(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
		
		logger.info("call WS(getAddressBookWithSCStatusWebServiceTemplate): Request Object -" +
					" AddressNumber: "+ requestObj.getAddressNumber() +
					" AddressType(VENDOR_ADDRESS_TYPE): " + requestObj.getAddressType());
		GetAddressBookWithSCStatusResponseListObj responseListObj = (GetAddressBookWithSCStatusResponseListObj) getAddressBookWithSCStatusWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		MasterListVendor masterListVendor = null;
		if(responseListObj==null)
			logger.info("GetAddressBookWithSCStatusResponseListObj is null.");		
		else if(responseListObj.getGetAddressbookWithSCStatusResponseObjList()==null)
			logger.info("GetAddressbookWithSCStatusResponseObjList is null.");
		else if(responseListObj.getGetAddressbookWithSCStatusResponseObjList().size()==0)
			logger.info("No vendor is found with Address Number: "+requestObj.getAddressNumber());
		else if (responseListObj.getGetAddressbookWithSCStatusResponseObjList().size()>1)
			logger.info("More than vendors are found with Address Number: "+requestObj.getAddressNumber());
		else{
			GetAddressBookWithSCStatusResponseObj responseObj = responseListObj.getGetAddressbookWithSCStatusResponseObjList().get(0);
			
			masterListVendor = new MasterListVendor();
			
			masterListVendor.setApprovalStatus(responseObj.getReportCodeAddBook002());
			masterListVendor.setVendorType(responseObj.getReportCodeAddBook003());
			masterListVendor.setVendorName(responseObj.getAddressName());
			masterListVendor.setVendorNo(responseObj.getAddressNumber());
			masterListVendor.setVendorStatus(responseObj.getReportCodeAddBook010());
			masterListVendor.setCostCenter(responseObj.getCostCenter());
			masterListVendor.setVendorRegistrationNo(responseObj.getAlternateAddressKey());
		}
		
		if(masterListVendor!=null)
			return masterListVendor;
		
		//2. Obtain a list of vendor with Address Type(VENDOR_ADDRESS_TYPE): O
		requestObj = new GetAddressBookWithSCStatusRequestObj();
		requestObj.setAddressNumber(addressNumber);
		requestObj.setAddressType(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
		
		logger.info("call WS(getAddressBookWithSCStatusWebServiceTemplate): Request Object -" +
					" AddressNumber: "+ requestObj.getAddressNumber() +
					" AddressType(VENDOR_ADDRESS_TYPE): " + requestObj.getAddressType());
		responseListObj = (GetAddressBookWithSCStatusResponseListObj) getAddressBookWithSCStatusWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		masterListVendor = null;
		if(responseListObj==null)
			logger.info("GetAddressBookWithSCStatusResponseListObj is null.");		
		else if(responseListObj.getGetAddressbookWithSCStatusResponseObjList()==null)
			logger.info("GetAddressbookWithSCStatusResponseObjList is null.");
		else if(responseListObj.getGetAddressbookWithSCStatusResponseObjList().size()==0)
			logger.info("No vendor is found with Address Number: "+requestObj.getAddressNumber());
		else if (responseListObj.getGetAddressbookWithSCStatusResponseObjList().size()>1)
			logger.info("More than vendors are found with Address Number: "+requestObj.getAddressNumber());
		else{
			GetAddressBookWithSCStatusResponseObj responseObj = responseListObj.getGetAddressbookWithSCStatusResponseObjList().get(0);
			
			masterListVendor = new MasterListVendor();
			
			masterListVendor.setApprovalStatus(responseObj.getReportCodeAddBook002());
			masterListVendor.setVendorType(responseObj.getReportCodeAddBook003());
			masterListVendor.setVendorName(responseObj.getAddressName());
			masterListVendor.setVendorNo(responseObj.getAddressNumber());
			masterListVendor.setVendorStatus(responseObj.getReportCodeAddBook010());
			masterListVendor.setCostCenter(responseObj.getCostCenter());
			masterListVendor.setVendorRegistrationNo(responseObj.getAlternateAddressKey());
		}
		
		return masterListVendor;
	}
	

	/**
	 * @author tikywong
	 * modified on April 22, 2013
	 * modified by irischau 
	 * on 13 Mar 2014
	 * search by address book type
	 * @author koeyyeung
	 * modified on 8 Oct, 2015
	 * Add SC Financial Alert column for Credit Warning
	 */
	public List<MasterListVendor> obtainAddressBookList(List<String> addressBookTypeList) {
		List<MasterListVendor> masterListVendorList = new LinkedList<MasterListVendor>();

		for (String addressType : addressBookTypeList) {
			GetAddressBookWithSCStatusRequestObj requestObj = new GetAddressBookWithSCStatusRequestObj();
			requestObj.setAddressType(addressType);

			logger.info("call WS(getAddressBookWithSCStatusWebServiceTemplate): Request Object -" + " AddressType(VENDOR_ADDRESS_TYPE): " + requestObj.getAddressType());
			GetAddressBookWithSCStatusResponseListObj responseListObj = (GetAddressBookWithSCStatusResponseListObj) getAddressBookWithSCStatusWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

			for (GetAddressBookWithSCStatusResponseObj curResponseObj : responseListObj.getGetAddressbookWithSCStatusResponseObjList()) {
				MasterListVendor masterListVendor = new MasterListVendor();

				masterListVendor.setApprovalStatus(curResponseObj.getReportCodeAddBook002() == null ? null : curResponseObj.getReportCodeAddBook002().trim());
				masterListVendor.setVendorType(curResponseObj.getReportCodeAddBook003() == null ? null : curResponseObj.getReportCodeAddBook003().trim());
				masterListVendor.setVendorName(curResponseObj.getAddressName() == null ? null : curResponseObj.getAddressName().trim());
				masterListVendor.setVendorNo(curResponseObj.getAddressNumber() == null ? null : curResponseObj.getAddressNumber().trim());
				masterListVendor.setVendorStatus(curResponseObj.getReportCodeAddBook010() == null ? null : curResponseObj.getReportCodeAddBook010().trim());
				masterListVendor.setCostCenter(curResponseObj.getCostCenter() == null ? null : curResponseObj.getCostCenter().trim());
				masterListVendor.setVendorRegistrationNo(curResponseObj.getAlternateAddressKey() == null ? null : curResponseObj.getAlternateAddressKey().trim());
				masterListVendor.setScFinancialAlert(curResponseObj.getScFinancialAlert() == null ? null : curResponseObj.getScFinancialAlert().trim());
				
				masterListVendorList.add(masterListVendor);
			}
		}
		return masterListVendorList;
	}
	
	public List<MasterListVendor> getVendorNameList(String addressNumber) {
		List <MasterListVendor> resultList = new LinkedList<MasterListVendor>();
		GetAddressBookWithSCStatusRequestObj requestObj = new GetAddressBookWithSCStatusRequestObj();	
		requestObj.setAddressNumber(addressNumber);
		long start = System.currentTimeMillis();
		GetAddressBookWithSCStatusResponseListObj responseListObj = (GetAddressBookWithSCStatusResponseListObj) getAddressBookWithSCStatusWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();			
		logger.info("Time for call WS(getAllVendorList):"+ ((end- start)/1000));
		for(GetAddressBookWithSCStatusResponseObj curResponseObj : responseListObj.getGetAddressbookWithSCStatusResponseObjList() ){
			MasterListVendor curMasterListVendor = new MasterListVendor();
			
			String vendorName = curResponseObj.getAddressName();
			curMasterListVendor.setVendorName(vendorName);
			resultList.add(curMasterListVendor);
			
		}

		return resultList;
	}
	public List<MasterListVendor> getVendorNameListByBatch(List<String> addressNumberList){
		List <MasterListVendor> resultList = new LinkedList<MasterListVendor>();
		List <GetVendorNameListRequestObj> requestList = new ArrayList<GetVendorNameListRequestObj>();
		for(String curAN : addressNumberList){
			GetVendorNameListRequestObj requestObj = new GetVendorNameListRequestObj();
			requestObj.setAddressNumber(curAN);
			requestList.add(requestObj);
		}
		
		GetVendorNameListRequestListObj requestListObj = new GetVendorNameListRequestListObj();
		requestListObj.setInsertFields(requestList);
		long start = System.currentTimeMillis();
		GetVendorNameListResponseListObj responseListObj = (GetVendorNameListResponseListObj) getVendorNameListWebServiceTemplate.marshalSendAndReceive(requestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();

		logger.info("Time for call WS(getAllVendorList):"+ ((end- start)/1000));
		for(GetVendorNameListResponseObj curResponseObj : responseListObj.getGetAddressbookResponseObjList() )
		{
			MasterListVendor curMasterListVendor = new MasterListVendor();
			
			String vendorName = curResponseObj.getAddressName();
			curMasterListVendor.setVendorName(vendorName);
			curMasterListVendor.setVendorNo(curResponseObj.getAddressNumber());
			
			//Web Service do not have Subcontractor Status field
//			String standardCode = curResponseObj.getStandardIndustryCode()!=null?curResponseObj.getStandardIndustryCode().trim():"";
//			if("APPSUB".equals(standardCode) || "APPBOT".equals(standardCode))
//				curMasterListVendor.setApprovalStatus("Approved");
//			else
//				curMasterListVendor.setApprovalStatus("Not Approved");
//			if (curResponseObj.getReportCodeAddBook002()!=null && "Y".equals(curResponseObj.getReportCodeAddBook002().trim()))
//				curMasterListVendor.setApprovalStatus("Approved");
//			else 
//				curMasterListVendor.setApprovalStatus("Not Approved");			
			
			curMasterListVendor.setVendorType(curResponseObj.getReportCodeAddBook003());
			curMasterListVendor.setVendorStatus(curResponseObj.getReportCodeAddBook010());
			resultList.add(curMasterListVendor);
			
		}
		return resultList;
	}

	/** @author tikywong
	 *         modified on April 18, 2013
	 *         Used by:
	 *         1. Address Book Detail Window
	 *         2. Payment Certificate Window */
	public List<MasterListVendor> getVendorDetailsList(String addressNumber) throws Exception {
		logger.info("Address Number:" + addressNumber);

		List<MasterListVendor> resultList = new ArrayList<MasterListVendor>();
		List<VendorAddress> addressResultList = new ArrayList<VendorAddress>();
		List<VendorContactPerson> contactPersonResultList = new ArrayList<VendorContactPerson>();
		List<VendorPhoneNumber> phoneNumberResultList = new ArrayList<VendorPhoneNumber>();

		// Calling WebService
		GetGeneralAddressRequestObj requestObj = new GetGeneralAddressRequestObj();
		requestObj.setAddressNumber(addressNumber);
		
		logger.info("call WS(getAddressWebServiceTemplate): Request Object -"+
					" AddressNumber: "+requestObj.getAddressNumber());	
		GetGeneralAddressResponseListObj responseListObj = (GetGeneralAddressResponseListObj) getAddressWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		logger.info("RETURNED MASTERLISTVENDOR RECORDS(FULL LIST FROM WS)SIZE: " + responseListObj.getGetGeneralAddressResponseObjList().size());

		// Filling in MasterListVendor
		for (GetGeneralAddressResponseObj curResponseObj : responseListObj.getGetGeneralAddressResponseObjList()) {
			MasterListVendor masterListVendor = new MasterListVendor();

			String vendorName = curResponseObj.getAddressName() != null && !curResponseObj.getAddressName().toString().trim().equals("") ? curResponseObj.getAddressName().trim() : " ";
			String addressType = curResponseObj.getAddressType() != null && !curResponseObj.getAddressType().toString().trim().equals("") ? curResponseObj.getAddressType().trim() : " ";
			String costCenter = curResponseObj.getCostCenter() != null && !curResponseObj.getCostCenter().toString().trim().equals("") ? curResponseObj.getCostCenter().trim() : " ";
			String vendorNo = curResponseObj.getAddressNumber() != null && !curResponseObj.getAddressNumber().toString().trim().equals("") ? curResponseObj.getAddressNumber().trim() : " ";
			String vendorType = curResponseObj.getReportCodeAddBook003() != null && !curResponseObj.getReportCodeAddBook003().toString().trim().equals("") ? curResponseObj.getReportCodeAddBook003().trim() : " ";
			String vendorStatus = curResponseObj.getReportCodeAddBook010() != null && !curResponseObj.getReportCodeAddBook010().toString().trim().equals("") ? curResponseObj.getReportCodeAddBook010().trim() : " ";
			String standardCode = curResponseObj.getStandardIndustryCode() != null ? curResponseObj.getStandardIndustryCode().trim() : "";
			
			// Addresses
			if (curResponseObj.getAddress() != null) {
				// Filling in VendorAddress
				for (GetAddressResponseObj curAddressResponseObj : curResponseObj.getAddress()) {
					if (curAddressResponseObj != null) {
						VendorAddress vendorAddress = new VendorAddress();
						String addressLine1 = curAddressResponseObj.getAddressLine1() != null && !curAddressResponseObj.getAddressLine1().toString().trim().equals("") ? curAddressResponseObj.getAddressLine1().trim() : " ";
						String addressLine2 = curAddressResponseObj.getAddressLine2() != null && !curAddressResponseObj.getAddressLine2().toString().trim().equals("") ? curAddressResponseObj.getAddressLine2().trim() : " ";
						String addressLine3 = curAddressResponseObj.getAddressLine3() != null && !curAddressResponseObj.getAddressLine3().toString().trim().equals("") ? curAddressResponseObj.getAddressLine3().trim() : " ";
						String addressLine4 = curAddressResponseObj.getAddressLine4() != null && !curAddressResponseObj.getAddressLine4().toString().trim().equals("") ? curAddressResponseObj.getAddressLine4().trim() : " ";

						String city = curAddressResponseObj.getCity() != null && !curAddressResponseObj.getCity().toString().trim().equals("") ? curAddressResponseObj.getCity().trim() : " ";
						String country = curAddressResponseObj.getCountry() != null && !curAddressResponseObj.getCountry().toString().trim().equals("") ? curAddressResponseObj.getCountry().trim() : " ";
						String countryAddress = curAddressResponseObj.getCountyAddress() != null && !curAddressResponseObj.getCountyAddress().toString().trim().equals("") ? curAddressResponseObj.getCountyAddress().trim() : " ";
						String effectiveDate = curAddressResponseObj.getEffectiveDateExistence10() != null && !curAddressResponseObj.getEffectiveDateExistence10().toString().trim().equals("") ? curAddressResponseObj.getEffectiveDateExistence10().trim() : " ";
						String state = curAddressResponseObj.getState() != null && !curAddressResponseObj.getState().toString().trim().equals("") ? curAddressResponseObj.getState().trim() : " ";
						String zipCode = curAddressResponseObj.getZipCodePostal() != null && !curAddressResponseObj.getZipCodePostal().toString().trim().equals("") ? curAddressResponseObj.getZipCodePostal().trim() : " ";
						Integer addressNo = curAddressResponseObj.getAddressNumber() != null ? curAddressResponseObj.getAddressNumber() : 0;
						Date dateBeginningEffective = curAddressResponseObj.getDateBeginningEffective();

						vendorAddress.setAddressLine1(addressLine1);
						vendorAddress.setAddressLine2(addressLine2);
						vendorAddress.setAddressLine3(addressLine3);
						vendorAddress.setAddressLine4(addressLine4);
						vendorAddress.setAddressNumber(addressNo);
						vendorAddress.setCity(city);
						vendorAddress.setCountry(country);
						vendorAddress.setCountyAddress(countryAddress);
						if (dateBeginningEffective != null)
							vendorAddress.setDateBeginningEffective(dateBeginningEffective);
						if (effectiveDate != null)
							vendorAddress.setEffectiveDateExistence10(effectiveDate);
						vendorAddress.setState(state);
						vendorAddress.setZipCodePostal(zipCode);

						addressResultList.add(vendorAddress);
					}
				}
			}

			// Contact Persons
			if (curResponseObj.getContactPerson() != null) {
				// Filling in VendorContactPerson
				for (GetContactPersonResponseObj curContactPersonResponseObj : curResponseObj.getContactPerson()) {
					if (curContactPersonResponseObj != null) {
						VendorContactPerson vendorContactPerson = new VendorContactPerson();

						String contactTitle = curContactPersonResponseObj.getContactTitle() != null && !curContactPersonResponseObj.getContactTitle().toString().trim().equals("") ? curContactPersonResponseObj.getContactTitle().trim() : " ";
						String descripCompressed = curContactPersonResponseObj.getDescripCompressed() != null && !curContactPersonResponseObj.getDescripCompressed().toString().trim().equals("") ? curContactPersonResponseObj.getDescripCompressed().trim() : " ";
						Integer addressNo = curContactPersonResponseObj.getAddressNumber() != null ? curContactPersonResponseObj.getAddressNumber() : 0;
						Integer lineNumberID = curContactPersonResponseObj.getLineNumberID() != null ? curContactPersonResponseObj.getLineNumberID() : 0;
						Integer sequenceNumber = curContactPersonResponseObj.getSequenceNumber52Display() != null ? curContactPersonResponseObj.getSequenceNumber52Display() : 0;
						String nameMailing = curContactPersonResponseObj.getNameMailing() != null && !curContactPersonResponseObj.getNameMailing().toString().trim().equals("") ? curContactPersonResponseObj.getNameMailing().trim() : " ";
						String nameAlpha = curContactPersonResponseObj.getNameAlpha() != null && !curContactPersonResponseObj.getNameAlpha().toString().trim().equals("") ? curContactPersonResponseObj.getNameAlpha().trim() : " ";
						String nameGiven = curContactPersonResponseObj.getNameGiven() != null && !curContactPersonResponseObj.getNameGiven().toString().trim().equals("") ? curContactPersonResponseObj.getNameGiven().trim() : " ";
						String nameSurname = curContactPersonResponseObj.getNameSurname() != null && !curContactPersonResponseObj.getNameSurname().toString().trim().equals("") ? curContactPersonResponseObj.getNameSurname().trim() : " ";
						String nameMiddle = curContactPersonResponseObj.getNameMiddle() != null && !curContactPersonResponseObj.getNameMiddle().toString().trim().equals("") ? curContactPersonResponseObj.getNameMiddle().trim() : " ";

						vendorContactPerson.setAddressNumber(addressNo);
						vendorContactPerson.setContactTitle(contactTitle);
						vendorContactPerson.setDescripCompressed(descripCompressed);
						vendorContactPerson.setLineNumberID(lineNumberID);
						vendorContactPerson.setNameAlpha(nameAlpha);
						vendorContactPerson.setNameGiven(nameGiven);
						vendorContactPerson.setNameMailing(nameMailing);
						vendorContactPerson.setNameMiddle(nameMiddle);
						vendorContactPerson.setNameSurname(nameSurname);
						vendorContactPerson.setSequenceNumber52Display(sequenceNumber);
						contactPersonResultList.add(vendorContactPerson);
					}
				}
			}

			// Phone Numbers
			if (curResponseObj.getPhoneNumber() != null) {
				// Filling in VendorPhoneNumber
				for (GetPhoneNumberResponseObj curContactPersonResponseObj : curResponseObj.getPhoneNumber()) {
					if (curContactPersonResponseObj != null) {
						VendorPhoneNumber vendorPhoneNumber = new VendorPhoneNumber();
						String phoneNumberType = curContactPersonResponseObj.getPhoneNumberType() != null && !curContactPersonResponseObj.getPhoneNumberType().toString().trim().equals("") ? curContactPersonResponseObj.getPhoneNumberType().trim() : " ";
						String phoneAreaCode = curContactPersonResponseObj.getPhoneAreaCode1() != null && !curContactPersonResponseObj.getPhoneAreaCode1().toString().trim().equals("") ? curContactPersonResponseObj.getPhoneAreaCode1().trim() : " ";
						String phoneNumber = curContactPersonResponseObj.getPhoneNumber() != null && !curContactPersonResponseObj.getPhoneNumber().toString().trim().equals("") ? curContactPersonResponseObj.getPhoneNumber().trim() : " ";
						Integer addressNo = curContactPersonResponseObj.getAddressNumber() != null ? curContactPersonResponseObj.getAddressNumber() : 0;
						Integer lineNumberID = curContactPersonResponseObj.getLineNumberID() != null ? curContactPersonResponseObj.getLineNumberID() : 0;
						Integer sequenceNumber = curContactPersonResponseObj.getSequenceNumber70() != null ? curContactPersonResponseObj.getSequenceNumber70() : 0;
						Integer contactPersonalID = curContactPersonResponseObj.getContactPersonalID() != null ? curContactPersonResponseObj.getContactPersonalID() : 0;

						vendorPhoneNumber.setAddressNumber(addressNo);
						vendorPhoneNumber.setContactPersonalID(contactPersonalID);
						vendorPhoneNumber.setLineNumberID(lineNumberID);
						vendorPhoneNumber.setPhoneAreaCode1(phoneAreaCode);
						vendorPhoneNumber.setPhoneNumber(phoneNumber);
						vendorPhoneNumber.setPhoneNumberType(phoneNumberType);
						vendorPhoneNumber.setSequenceNumber70(sequenceNumber);

						phoneNumberResultList.add(vendorPhoneNumber);

					}
				}
			}

			masterListVendor.setVendorName(vendorName);
			masterListVendor.setAddressType(addressType);
			masterListVendor.setCostCenter(costCenter);
			masterListVendor.setVendorAddress(addressResultList);
			masterListVendor.setVendorContactPerson(contactPersonResultList);
			masterListVendor.setVendorNo(vendorNo);
			masterListVendor.setVendorPhoneNumber(phoneNumberResultList);
			masterListVendor.setVendorType(vendorType);
			masterListVendor.setVendorStatus(vendorStatus);
			
			if ("APPSUB".equals(standardCode) || "APPBOT".equals(standardCode))
				masterListVendor.setApprovalStatus("Y");
			else
				masterListVendor.setApprovalStatus("N");
			
			resultList.add(masterListVendor);
		}

		return resultList;
	}

	public MasterListVendor getOneVendor(String addressNumber){
		List<MasterListVendor> vendorList = getVendorNameList(addressNumber);
		return vendorList.size() == 0 ? null : vendorList.get(0);
	}

	public String checkAwardValidation(Integer vendorNo, String workScope) throws Exception{
		CheckAwardValidationRequestObj requestObj = new CheckAwardValidationRequestObj();
		requestObj.setAddressNumber(vendorNo);
		requestObj.setWorkScope(workScope);
		long start;
		long end;
		CheckAwardValidationResponseObj responseObj = new CheckAwardValidationResponseObj();
		try {
			start = System.currentTimeMillis();
			responseObj = (CheckAwardValidationResponseObj) checkAwardValidationWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			end = System.currentTimeMillis();
			logger.info("time for calling ws(CheckAwardValidation):"+ ((end-start)/1000.00));
		}catch (SoapFaultClientException e){
			
			logger.info("FAIL to call WS (CheckAwardValidation), pending for RETRY");
			start = System.currentTimeMillis();
			Thread.sleep(GlobalParameter.RETRY_INTERVERAL);				
			end = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end-start)/1000.00));
			try {
				logger.info("RETRY calling ws(CheckAwardValidation):"); 
				start = System.currentTimeMillis();
				responseObj = (CheckAwardValidationResponseObj) checkAwardValidationWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				end = System.currentTimeMillis();
				logger.info("time for calling ws(CheckAwardValidation):()(Retry)"+ ((end-start)/1000.00));
			}catch (SoapFaultClientException e2){
				logger.info("FAIL to call WS (CheckAwardValidation), FAIL operation ");
				//throw new JDEErrorException(e2,"1001");								
			}
		}
		return responseObj.getDescription();
	}
	public MasterListVendor getVendor(String vendorNo){
		List<String> addressBookTypeList = new ArrayList<String>();
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
		for(MasterListVendor curVendor:obtainAddressBookList(addressBookTypeList)){
			if (curVendor.getVendorNo().trim()!= null && vendorNo.trim().equalsIgnoreCase(curVendor.getVendorNo().trim()))
				return curVendor;
		}
		return null;
	}
	
	public Map<String, Set<String>> obtainSubcontractorWorkScopeMap(String subcontractorNo, String workscope){
		GetSubcontractorWSRequestObj requestObj = new GetSubcontractorWSRequestObj();
		Map<String, Set<String>> subcontractorWorkscopeMap = new TreeMap<>();
		requestObj.setSupplementalDatabaseCode("AB");
		requestObj.setTypeofData("WS");
		if(StringUtils.isNotEmpty(workscope)) requestObj.setUserDefinedCode(workscope);
		if(StringUtils.isNotEmpty(subcontractorNo)) requestObj.setSuppDataNumericKey1(new Integer(subcontractorNo));
		GetSubcontractorWSResponseListObj responseListObj = (GetSubcontractorWSResponseListObj) getSubcontractorWorkScopeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		for (GetSubcontractorWSResponseObj curResponseObj : responseListObj.getGetSubcontractorWSResponseObj()) {
			String subcon = curResponseObj.getSuppDataNumericKey1().toString();
			if(subcontractorWorkscopeMap.get(subcon) == null) {
				subcontractorWorkscopeMap.put(subcon, new TreeSet<String>());
			}
			if(StringUtils.isNotEmpty(curResponseObj.getUserDefinedCode())) {
				subcontractorWorkscopeMap.get(subcon).add(curResponseObj.getUserDefinedCode().trim());
			}
		}
		return subcontractorWorkscopeMap;
	}
	
	/**
	 * @author tikywong
	 * modified on April 19, 2013
	 * @throws DatabaseOperationException 
	 * 
	 */
	public List<WorkScopeWrapper> obtainSubcontractorWorkScope(String vendorNo) throws DatabaseOperationException {
		logger.info("START - Vendor No: "+vendorNo);
		List<WorkScopeWrapper> subcontractorWorkScopeWrapperList = new ArrayList<WorkScopeWrapper>();
		
		// Calling WebService
		// 1. Obtain a list of work scope that are listed under the provided vendor
		GetSubcontractorWSRequestObj requestObj = new GetSubcontractorWSRequestObj();
		requestObj.setSuppDataNumericKey1(new Integer(vendorNo));
		requestObj.setSupplementalDatabaseCode("AB");
		requestObj.setTypeofData("WS");
		
		logger.info("call WS(getSubcontractorWorkScopeWebServiceTemplate): Request Object -"+
					" SuppDataNumericKey1(Vendor No.): "+vendorNo+
					" SupplementalDatabaseCode: "+requestObj.getSupplementalDatabaseCode()+
					" TypeofData: "+requestObj.getTypeofData());	
		GetSubcontractorWSResponseListObj responseListObj = (GetSubcontractorWSResponseListObj) getSubcontractorWorkScopeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		//2. Obtain the work scope description for the vendor's work scope list
		List<UDC> workScopeList = unitWSDao.obtainWorkScopeList();
		for (GetSubcontractorWSResponseObj curResponseObj : responseListObj.getGetSubcontractorWSResponseObj()) {
			WorkScopeWrapper wrapper = new WorkScopeWrapper();
			//WorkScope
			wrapper.setWorkScopeCode(curResponseObj.getUserDefinedCode());
			
			//WorkScope Description
			for (UDC udc : workScopeList) {
				if (curResponseObj.getUserDefinedCode().trim().equals(udc.getCode()))
					wrapper.setDescription(udc.getDescription());
			}
			wrapper.setIsApproved(curResponseObj.getUdcEquivalentWw());
			subcontractorWorkScopeWrapperList.add(wrapper);
		}
		
		logger.info("RETURNED WORKSCOPE RECORDS WITH DESCRIPTION OF VENDOR ("+vendorNo+") SIZE: " + subcontractorWorkScopeWrapperList.size());
		return subcontractorWorkScopeWrapperList;
	}
	
	/**
	 * @author tikywong
	 * modified on April 22, 2013
	 * Supplement Database Code: AB
	 * Type of Data: WS
	 * User Defined Code(UDC): "workScope code"
	 */
	public List<String> obtainSubcontractorByWorkScope(String workScopeCode){
		GetSubcontractorWSRequestObj requestObj = new GetSubcontractorWSRequestObj();
		requestObj.setSupplementalDatabaseCode("AB");
		requestObj.setTypeofData("WS");
		requestObj.setUserDefinedCode(workScopeCode);
		
		logger.info("call WS(obtainSubcontractorByWorkScope): Request Object -"+
					" SupplementalDatabaseCode: "+requestObj.getSupplementalDatabaseCode()+
					" TypeofData: "+requestObj.getTypeofData()+
					" UserDefinedCode(WorkScope Code): "+requestObj.getSuppDataNumericKey1());
		GetSubcontractorWSResponseListObj responseListObj = (GetSubcontractorWSResponseListObj) getSubcontractorWorkScopeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		List<String> subcontractorList = new ArrayList<String>();
		if(responseListObj.getGetSubcontractorWSResponseObj()!=null && responseListObj.getGetSubcontractorWSResponseObj().size()>0){
			for(GetSubcontractorWSResponseObj curObj : responseListObj.getGetSubcontractorWSResponseObj()){
				if(curObj.getSuppDataNumericKey1()!=null)
					subcontractorList.add(curObj.getSuppDataNumericKey1().toString());
			}
		}
		
		logger.info("RETURNED SUBCONTRACTOR RECORDS(FULL LIST FROM WS)SIZE: "+subcontractorList.size());		
		return subcontractorList;
	}

	public boolean validateAccountNum(String jobNumber, String objectCode, String subsidiaryCode){
		if(jobNumber == null || objectCode == null || subsidiaryCode == null)
			return false;
		ValidateAccNumRequestObj accNumRequest = new ValidateAccNumRequestObj();
		accNumRequest.setJDEnterpriseOneEventPoint01("5");
		accNumRequest.setJDEnterpriseOneEventPoint02("2");
		accNumRequest.setBusinessUnit(jobNumber);
		accNumRequest.setObjectAccount(objectCode);
		accNumRequest.setSubsidiary(subsidiaryCode);
		try{
			ValidateAccNumResponseObj accNumResponse = (ValidateAccNumResponseObj) validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequest, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(accNumResponse.getAccountID() == null || accNumResponse.getAccountID().trim().length() == 0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean createAccountCode(String jobNumber, String objectCode, String subsidiaryCode) {
		UpdateAccMasterByObjSubRequestObj requestObj = new UpdateAccMasterByObjSubRequestObj();
		requestObj.setJobNumber(jobNumber);
		requestObj.setObjectAccount(objectCode);
		requestObj.setSubsidiary(subsidiaryCode);
		try{
			@SuppressWarnings("unused")
			UpdateAccMasterByObjSubResponseObj responseObj = (UpdateAccMasterByObjSubResponseObj) getUpdateAccMasterByObjSubWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

			logger.log(Level.INFO,"createAccountCode("+jobNumber+"."+objectCode+"."+subsidiaryCode+") ");
		}catch (SoapFaultClientException sme){
			try {
				@SuppressWarnings("unused")
				UpdateAccMasterByObjSubResponseObj responseObj = (UpdateAccMasterByObjSubResponseObj) getUpdateAccMasterByObjSubWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

				logger.log(Level.INFO,"RETRY: createAccountCode ("+jobNumber+"."+objectCode+"."+subsidiaryCode+") ");
				sme.printStackTrace();
				Thread.sleep(GlobalParameter.RETRY_INTERVERAL);
			} catch (Exception e) {
				logger.log(Level.SEVERE,"FAIL: createAccountCode("+jobNumber+"."+objectCode+"."+subsidiaryCode+") is failure");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
}
