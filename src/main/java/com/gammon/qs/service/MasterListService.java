package com.gammon.qs.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.AddressBookQueryManager.getAddressBookWithSCStatus.GetAddressBookWithSCStatusRequestObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.util.WildCardStringFinder;
import com.gammon.qs.wrapper.WorkScopeWrapper;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MasterListService{
	
	private Logger logger = Logger.getLogger(MasterListService.class.getName());
	@Autowired
	private MasterListWSDao masterListDao;
	@Autowired
	private AdminService adminService;
	
	//server cache
	private List<MasterListVendor> vendorList;
	private List<MasterListObject> objectList;
	private List<MasterListSubsidiary> subsidiaryList;
	private List<MasterListVendor> clientList;
	
	public List<MasterListVendor> getClientList() {
		return clientList;
	}
	public void setClientList(List<MasterListVendor> clientList) {
		this.clientList = clientList;
	}
	public List<MasterListVendor> getVendorList() {
		return vendorList;
	}
	public void setVendorList(List<MasterListVendor> vendorList) {
		this.vendorList = vendorList;
	}
	
	public List<MasterListVendor> obtainAllVendorList(String searchStr) throws Exception {
		logger.info("obtainAllVendorList --> START");
		boolean isRefreshed = false;
		List<String> addressBookTypeList = new ArrayList<String>();
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
		
		if(this.vendorList== null){
			this.vendorList = masterListDao.obtainAddressBookList(addressBookTypeList);
			isRefreshed = true;
		}
		List<MasterListVendor> resultList = searchVendorInLocalCacheList(searchStr);
		
		if(resultList.size()==0 && !isRefreshed){
			this.vendorList = this.masterListDao.obtainAddressBookList(addressBookTypeList);
			resultList = searchVendorInLocalCacheList(searchStr);
		}
		return resultList;
	}
	
	public List<MasterListVendor> obtainAllClientList(String searchStr) throws Exception {
		logger.info("obtainAllClientList --> START");
		boolean isRefreshed = false;
		List<String> addressBookTypeList = new ArrayList<String>();
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.CLIENT_ADDRESS_TYPE);
		if(this.clientList== null){
			this.clientList = masterListDao.obtainAddressBookList(addressBookTypeList);
			isRefreshed = true;
		}
		List<MasterListVendor> resultList = searchClientInLocalCacheList(searchStr);
		
		if(resultList.size()==0 && !isRefreshed){
			this.clientList = this.masterListDao.obtainAddressBookList(addressBookTypeList);
			resultList = searchClientInLocalCacheList(searchStr);
		}
		return resultList;
	}
	
	public List<MasterListVendor> obtainVendorListByWorkScopeWithUser(String workScope, String username) throws Exception{
		return trimVendorListAccessibleByUser(obtainSubcontractorListByWorkScopeCode(workScope), username);
	}
	
	private List<MasterListVendor> trimVendorListAccessibleByUser(List<MasterListVendor> vendors, String username) throws Exception {
		if (vendors == null || vendors.size() == 0)
			return vendors;

		Set<String> userCompanies = adminService.obtainCompanyListByUsernameViaWS(username);
		List<MasterListVendor> userVendors = new ArrayList<MasterListVendor>(vendors.size());

		// User can access to all vendors
		if (userCompanies.contains("NA"))
			return vendors;

		for (MasterListVendor vendor : vendors) {
			if (vendor != null) {
				if (vendor.getCostCenter() == null || vendor.getCostCenter().trim().length() == 0) {
					userVendors.add(vendor);
				} else {
					String mcu = vendor.getCostCenter().trim();
					String paddedMcu = mcu.length() < 5 ? "00000".substring(mcu.length()) + mcu : mcu;

					if (userCompanies.contains(mcu) || userCompanies.contains(paddedMcu))
						userVendors.add(vendor);
				}
			}
		}

		return userVendors;
	}
	
	public MasterListVendor searchVendorAddressDetails(String addressNumber) throws Exception{
		List<MasterListVendor> resultList = masterListDao.getVendorDetailsList(addressNumber);
		if (resultList!=null && resultList.size() > 0){
			return resultList.get(0);
		}else{
			return new MasterListVendor();
		}
	}
	
	private List<MasterListVendor> searchVendorInLocalCacheList(String searchStr) throws Exception{
		List<MasterListVendor> resultVendorList = new ArrayList<MasterListVendor>();
		for(MasterListVendor curVendor: this.vendorList)
			if (curVendor.getVendorType()!=null && !"".equals(curVendor.getVendorType().trim()) && Integer.parseInt(curVendor.getVendorType().trim())>0){
				String curVendorName = curVendor.getVendorName();
				String curVendorNumber = curVendor.getVendorNo();
				
				WildCardStringFinder finder = new WildCardStringFinder();
				WildCardStringFinder finder2 = new WildCardStringFinder();
				if(finder.isStringMatching(curVendorName,searchStr)){
					resultVendorList.add(curVendor);
				}
				else if (finder2.isStringMatching(curVendorNumber, searchStr)){				
					resultVendorList.add(curVendor);
				}
			}
		logger.info("vendorList.size() : " + vendorList.size()); 
		return resultVendorList;
	}
	
	private List<MasterListVendor> searchClientInLocalCacheList(String searchStr) throws Exception{
		List<MasterListVendor> resultClientList = new ArrayList<MasterListVendor>();
		for(MasterListVendor curVendor: this.clientList){
			String curVendorName = curVendor.getVendorName();
			String curVendorNumber = curVendor.getVendorNo();

			WildCardStringFinder finder = new WildCardStringFinder();
			WildCardStringFinder finder2 = new WildCardStringFinder();
			if(finder.isStringMatching(curVendorName,searchStr)){
				resultClientList.add(curVendor);
			}
			else if (finder2.isStringMatching(curVendorNumber, searchStr)){		
				resultClientList.add(curVendor);
			}
		}
		logger.info("ClientList size : " + clientList.size()); 
		return resultClientList;
	}

	private List<MasterListObject> searchMasterListObjectInLocalCacheList(String searchStr) 
	{
		List<MasterListObject> resultObjectList = new ArrayList<MasterListObject>();
		
		for(MasterListObject curObject: this.objectList)
		{
			String curObjectCode = curObject.getObjectCode();
			String curObjectDesc = curObject.getDescription();
			
			WildCardStringFinder finder = new WildCardStringFinder();
			WildCardStringFinder finder2 = new WildCardStringFinder();
			if(finder.isStringMatching(curObjectCode,searchStr))
				resultObjectList.add(curObject);
			else if (finder2.isStringMatching(curObjectDesc, searchStr))
				resultObjectList.add(curObject);
			
		}
		
		return resultObjectList;
	}
	
	
	
	private List<MasterListSubsidiary> searchMasterListSubsidiaryInLocalCacheList(String searchStr)
	{
		List<MasterListSubsidiary> resultSubsidiaryList = new ArrayList<MasterListSubsidiary>();
		
		for(MasterListSubsidiary curSubsidiary: this.subsidiaryList)
		{
			String curSubsidiaryCode = curSubsidiary.getSubsidiaryCode();
			String curSubsidiaryDesc = curSubsidiary.getDescription();
			
			WildCardStringFinder finder = new WildCardStringFinder();
			WildCardStringFinder finder2 = new WildCardStringFinder();
			if(finder.isStringMatching(curSubsidiaryCode,searchStr))
				resultSubsidiaryList.add(curSubsidiary);
			else if (finder2.isStringMatching(curSubsidiaryDesc, searchStr))
				resultSubsidiaryList.add(curSubsidiary);
			
		}
		
		return resultSubsidiaryList;
	}

	public boolean createAccountCode(String jobNumber, String objectCode, String subsidiaryCode){
		return masterListDao.createAccountCode(jobNumber, objectCode, subsidiaryCode);
	}
	
	public String validateObjectAndSubsidiaryCodes(String objectCode, String subsidiaryCode) throws Exception{
		if(objectCode == null || objectCode.length() != 6 || searchObjectList(objectCode).size() == 0)
			return "Invalid object code: " + objectCode;
		//Replace 2nd char of subsid code with '9' before validating - this allows for location codes
		//First, make sure that the 2nd char is a digit - if not, it is invalid
		char[] subsidChars = subsidiaryCode.toCharArray();
		if(!Character.isDigit(subsidChars[1])&&(subsidChars[1]<'A'||subsidChars[1]>'Z') )
			return "Invalid subsidiary code: " + subsidiaryCode;
		subsidChars[1] = '9';
		if(subsidiaryCode == null || subsidiaryCode.length() != 8 || searchSubsidiaryList(String.valueOf(subsidChars)).size() == 0)
			return "Invalid subsidiary code: " + subsidiaryCode;
		return null;
	}
	
	/**
	 * @author tikywong
	 * modified on April 22, 2013
	 */
	public List<MasterListVendor> obtainSubcontractorListByWorkScopeCode(String workScopeCode) throws DatabaseOperationException{
		List<String> subcontractorNumberList = masterListDao.obtainSubcontractorByWorkScope(workScopeCode);
		List<MasterListVendor> subcontractorList = new ArrayList<MasterListVendor>();
		for (String subcontractor:subcontractorNumberList)
			subcontractorList.add(obtainVendorByVendorNo(subcontractor));
		return subcontractorList;
	}

	public String checkObjectCodeInUCC(String objectAcc) throws Exception {
		boolean refreshed = false;
		if (objectList==null){
			try {
				objectList = masterListDao.obtainObjectCodeList();
				refreshed = true;
			} catch (Exception e) {
				e.printStackTrace();
				return "Object list cannot be fetched ";
			}
		}
		if (objectAcc==null || objectAcc.length()!=6)
			return "Invalid object code";
		String returnedResult = searchObject(objectList, objectAcc);
		if (returnedResult==null)
			return returnedResult;
		if (refreshed)
			return returnedResult;
		
		try {
			objectList = masterListDao.obtainObjectCodeList();
			return searchObject(objectList, objectAcc);
		} catch (Exception e) {
			e.printStackTrace();
			return "Object list cannot be fetched ";
		}
	}

	private String searchObject(List<MasterListObject> objectList,String targetObject){
		for (MasterListObject aObject:objectList)
			if(aObject.getObjectCode().equals(targetObject))
				return null;
		return "Object code cannot be found";
	}
	
	public String checkSubsidiaryCodeInUCC(String subsidiaryCode) throws Exception {
		boolean refreshed = false;
		String message = null;
		
		if (subsidiaryList==null){
			try {
				subsidiaryList = masterListDao.getAllSubsidiaryList();
				refreshed = true;
			} catch (Exception e) {
				e.printStackTrace();
				message = "Subsidary list cannot be fetched ";
				logger.info(message);
				return message;
			}
		}
		if (subsidiaryCode==null || subsidiaryCode.length()!=8){
			message = "Invalid subsidiary code";
			logger.info(message);
			return message;
		}
		String returnedResult = searchSubidiary(subsidiaryList, subsidiaryCode);
		if (returnedResult==null)
			return returnedResult;
		if (refreshed)
			return returnedResult;
		try {
			subsidiaryList = masterListDao.getAllSubsidiaryList();
			return searchSubidiary(subsidiaryList, subsidiaryCode);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Subsidary list cannot be fetched ";
			logger.info(message);
			return message;
		}
	}
	
	private String searchSubidiary(List<MasterListSubsidiary> subsidiaryList, String targetSubsidiary){
		String message = null;
		if (Character.isDigit(targetSubsidiary.charAt(1))||(targetSubsidiary.charAt(1)>='A'&&targetSubsidiary.charAt(1)<='Z') )
			for (MasterListSubsidiary aSubsidiary:subsidiaryList)
				if(aSubsidiary.getSubsidiaryCode().equals(targetSubsidiary.substring(0,1)+"9"+targetSubsidiary.substring(2)))
					return message;
		
		message = "Subsidiary code cannot be found";
		logger.info(message);
		return message;
	}

	/*************************************** FUNCTIONS FOR PCMS       **************************************************************/
	/**
	 * @author tikywong
	 * modified on April 22, 2013
	 * Cached a global vendor list, call web service if the global vendor list is null
	 */
	public MasterListVendor obtainVendorByVendorNo(String vendorNo) throws DatabaseOperationException{
//		logger.info("Vendor No.: "+vendorNo);
		boolean refreshed = false; //refreshing cached vendor list
		List<String> addressBookTypeList = new ArrayList<String>();
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
		
		//1. call web service for the whole vendor list
		if(vendorList == null){
			vendorList = masterListDao.obtainAddressBookList(addressBookTypeList);
			refreshed = true;
		}
		
		//2. loop the vendor list and find the requested vendor
		for(MasterListVendor vendor : vendorList){
			if(vendor.getVendorNo().equals(vendorNo))
				return vendor;
		}
		
		/*3. if the vendor couldn't be found the vendor list and the vendor list isn't just loading from the web service,
		 	 call web service to refresh the vendor list to the most updated one */	 
		if(!refreshed){
			vendorList = masterListDao.obtainAddressBookList(addressBookTypeList);
			for(MasterListVendor vendor : vendorList){
				if(vendor.getVendorNo().equals(vendorNo))
					return vendor;
			}
		}
		
		//4. if still cannot find, return null
		return null;
	}
	
	public List<MasterListVendor> searchVendorList(String searchStr) throws Exception {
		logger.info("searchVendorList --> START");
		boolean isRefreshed = false;
		List<String> addressBookTypeList = new ArrayList<String>();
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
		addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
		if(this.vendorList== null){
			this.vendorList = masterListDao.obtainAddressBookList(addressBookTypeList);
			isRefreshed = true;
			logger.info("vendorList.size() : " + vendorList.size()); 
		}
		if(searchStr==null || "*".equals(searchStr)|| "".equals(searchStr)){
			return this.vendorList.subList(0, 101);
		}
		List<MasterListVendor> resultList = searchVendorInLocalCacheList(searchStr);
		
		if(resultList.size()==0 && !isRefreshed){
			this.vendorList = this.masterListDao.obtainAddressBookList(addressBookTypeList);
			resultList = searchVendorInLocalCacheList(searchStr);
		}
		
		//trim size
		if(resultList!=null &&  resultList.size()>101){
			List<MasterListVendor> returnList = new ArrayList<MasterListVendor>();
			returnList.addAll(resultList.subList(0, 101));
			
			return returnList;
		}
		
		return resultList;
	}
	
	public List<MasterListObject> searchObjectList(String searchStr) throws Exception {
		
		boolean isRefreshed = false;
		
		if(this.objectList == null)
		{
			this.objectList = this.masterListDao.obtainObjectCodeList();
			isRefreshed = true;
		}
		
		if(searchStr==null || "*".equals(searchStr)|| "".equals(searchStr))
		{
			if(!isRefreshed)
				this.objectList = masterListDao.obtainObjectCodeList();
			
			return this.objectList;
		}
		
		List<MasterListObject> resultList = searchMasterListObjectInLocalCacheList(searchStr);
		
		if(resultList.size() ==0 && !isRefreshed)
		{
			this.objectList = this.masterListDao.obtainObjectCodeList();
			return searchMasterListObjectInLocalCacheList(searchStr);
		}
		
		
		//trim size
		if(resultList!=null &&  resultList.size()>101)
		{
			List<MasterListObject> returnList = new ArrayList<MasterListObject>();
			returnList.addAll(resultList.subList(0, 101));
			
			return returnList;
		}
		
		return resultList;
		
	}

	public List<MasterListSubsidiary> searchSubsidiaryList(String searchStr) throws Exception  {

		boolean isRefreshed = false;
		
		if(this.subsidiaryList == null){
			this.subsidiaryList = this.masterListDao.getAllSubsidiaryList();
			isRefreshed = true;
		}
		if(searchStr==null || "*".equals(searchStr)|| "".equals(searchStr))
		{
			if(!isRefreshed)
				this.subsidiaryList = masterListDao.getAllSubsidiaryList();
			
			return this.subsidiaryList;
		}
		
		List<MasterListSubsidiary> resultList =  searchMasterListSubsidiaryInLocalCacheList(searchStr);
		
		if(resultList.size() ==0 && !isRefreshed)
		{
			this.subsidiaryList = this.masterListDao.getAllSubsidiaryList();
			return searchMasterListSubsidiaryInLocalCacheList(searchStr);
		}
		
		
		//trim size
		if(resultList!=null &&  resultList.size()>101)
		{
			List<MasterListSubsidiary> returnList = new ArrayList<MasterListSubsidiary>();
			returnList.addAll(resultList.subList(0, 101));
			
			return returnList;
		}
		
		return resultList;
	}
	
	/**
	 * JDE web service to validate account code by object code, subsidiary code and object+subsidiary combination
	 *
	 * @param jobNumber
	 * @param objectCode
	 * @param subsidiaryCode
	 * @return
	 * @author	tikywong
	 * @since	Apr 11, 2016 3:26:31 PM
	 */
	public String validateAndCreateAccountCode(String jobNumber, String objectCode, String subsidiaryCode) {
		String errorMessage = null;
		try {
			// 1. Validate Account Code
			// 1a. Object Code
			errorMessage = checkObjectCodeInUCC(objectCode);
			if (errorMessage != null)
				return "Invalid object code: " + objectCode;
			// 1b. Subsidiary Code
			errorMessage = checkSubsidiaryCodeInUCC(subsidiaryCode);
			if (errorMessage != null)
				return "Invalid subsidiary code: " + subsidiaryCode;
			// 1c. Object+Subsidiary Combination
			/*if (!validateObjectSubsidiaryRule(objectCode, subsidiaryCode))
				return "Invalid Combination of object code and subsidiary code: " + jobNumber + "." + objectCode + "." + subsidiaryCode;*/

			// 2. Create Account Code
			if (!masterListDao.createAccountCode(jobNumber, objectCode, subsidiaryCode))
				return "Failed: Creating account code: " + jobNumber + "." + objectCode + "." + subsidiaryCode;
			else
				logger.info("Account Code: " + jobNumber + "." + objectCode + "." + subsidiaryCode + " has created successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed: Validating/Creating the account code: " + jobNumber + "." + objectCode + "." + subsidiaryCode;
		}
		return errorMessage;
	}
	
	public List<WorkScopeWrapper> getSubcontractorWorkScope(String vendorNo)throws Exception{
		return masterListDao.obtainSubcontractorWorkScope(vendorNo);
	}
	/*************************************** FUNCTIONS FOR PCMS - END **************************************************************/
}
