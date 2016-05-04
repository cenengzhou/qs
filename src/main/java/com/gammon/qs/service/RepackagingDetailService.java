package com.gammon.qs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.DeleteRepackagingBudgetManager.getDeleteRepackagingBudget.DeleteRepackagingBudgetRequestObj;
import com.gammon.jde.webservice.serviceRequester.DeleteRepackagingBudgetManager.getDeleteRepackagingBudget.DeleteRepackagingBudgetResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertRepackagingBudgetManager.getInsertRepackagingBudget.E1Message;
import com.gammon.jde.webservice.serviceRequester.InsertRepackagingBudgetManager.getInsertRepackagingBudget.InsertRepackagingBudgetRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertRepackagingBudgetManager.getInsertRepackagingBudget.InsertRepackagingBudgetRequestObj;
import com.gammon.jde.webservice.serviceRequester.InsertRepackagingBudgetManager.getInsertRepackagingBudget.InsertRepackagingBudgetResponseObj;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.dao.RepackagingDetailHBDao;
import com.gammon.qs.dao.RepackagingEntryHBDao;
import com.gammon.qs.dao.application.UserHBDao;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingDetail;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.admin.MailService;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.EmailMessage;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gson.Gson;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@Transactional(rollbackFor = Exception.class)
public class RepackagingDetailService implements Serializable {
	
	private static final long serialVersionUID = 9105908214085435279L;
	private transient Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private transient RepackagingDetailHBDao repackagingDetailDao;
	@Autowired
	private transient RepackagingEntryHBDao repackagingEntryDao;
	@Autowired
	private transient BQResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	@Qualifier("insertRepackagingBudgetWebServiceTemplate")
	private transient WebServiceTemplate insertRepackagingBudgetTemplate;
	@Autowired
	@Qualifier("deleteRepackagingBudgetWebServiceTemplate")
	private transient WebServiceTemplate deleteRepackagingBudgetTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private transient WSConfig wsConfig;
	@Autowired
	private transient WebServiceConfig webServiceConfig;
	@Autowired
	private transient EnvironmentConfig environmentConfig; 	
	@Autowired
	private MailService mailService;
	@Autowired
	private UserHBDao userDao;
	
	private List<RepackagingDetailComparisonWrapper> cachedResults = new ArrayList<RepackagingDetailComparisonWrapper>();
	private Double totalBudget;
	private Double totalMarkup;
	private Double previousBudget;
	private Double previousMarkup;
	
	static final int RECORDS_PER_PAGE = 200;  
	private static final String ROLE_QS_APPROVER_ID = "ROLE_QS_APPROVER";

	public RepackagingEntry generateResourceSummaries(Job job) throws Exception {
		RepackagingEntry entry = new RepackagingEntry();
		entry.setJob(job);
		entry.setRepackagingVersion(Integer.valueOf(1));
		entry.setCreateDate(new Date());
		entry.setRemarks("Generated resource summaries");
		repackagingEntryDao.saveOrUpdate(entry);
		prepareRepackagingDetails(entry);
		if(postRepackagingDetails(entry, entry.getCreatedUser())){
			entry.setStatus("900");
			repackagingEntryDao.saveOrUpdate(entry);
		}
		return entry;
	}
	
	public Double prepareRepackagingDetails(RepackagingEntry repackagingEntry)
			throws Exception {
		logger.info("Generating resource snapshot");

		RepackagingEntry entryInDB;
		
		if(repackagingEntry.getRepackagingVersion()==1)
			entryInDB = repackagingEntry;
		else
			entryInDB = repackagingEntryDao.clearDetailsFromEntry(repackagingEntry.getId());

		List<BQResourceSummary> resourceSummaries = bqResourceSummaryDao.getResourceSummariesByJob(entryInDB.getJob());

		Double totalAmount = Double.valueOf(0);
		for(BQResourceSummary resourceSummary : resourceSummaries){
			RepackagingDetail repackagingDetail = new RepackagingDetail();
			repackagingDetail.setResourceSummaryId(resourceSummary.getId());
			repackagingDetail.setPackageNo(resourceSummary.getPackageNo());
			repackagingDetail.setObjectCode(resourceSummary.getObjectCode());
			repackagingDetail.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
			repackagingDetail.setResourceDescription(resourceSummary.getResourceDescription());
			repackagingDetail.setUnit(resourceSummary.getUnit());
			Double quantity = resourceSummary.getQuantity();
			Double rate = resourceSummary.getRate();
			Double amount = quantity != null && rate != null ? Double.valueOf(RoundingUtil.round(quantity * rate,4)) : Double.valueOf(0);
			if(!resourceSummary.getSubsidiaryCode().startsWith("9"))
				totalAmount += amount; //total Amount is budget only - don't include markup lines
			repackagingDetail.setQuantity(quantity);
			repackagingDetail.setRate(rate);
			repackagingDetail.setAmount(amount);
			repackagingDetail.setResourceType(resourceSummary.getResourceType());
			repackagingDetail.setExcludeLevy(resourceSummary.getExcludeLevy());
			repackagingDetail.setExcludeDefect(resourceSummary.getExcludeDefect());
			repackagingDetail.setRepackagingEntry(entryInDB);
		}
		entryInDB.setTotalResourceAllowance(totalAmount);
		entryInDB.setStatus("300");
		repackagingEntryDao.saveOrUpdate(entryInDB);
		return totalAmount;
	}
	
	public List<RepackagingDetailComparisonWrapper> searchRepackagingDetails(RepackagingEntry repackagingEntry, String packageNo, String objectCode, String subsidiaryCode) throws Exception {
		return repackagingDetailDao.searchRepackagingDetails(repackagingEntry, packageNo, objectCode, subsidiaryCode);
	}

	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsNewSearch(RepackagingEntry repackagingEntry, String packageNo,
			String objectCode, String subsidiaryCode, boolean changesOnly) throws Exception {
		setCachedResults(new ArrayList<RepackagingDetailComparisonWrapper>());
		totalBudget = Double.valueOf(0);
		totalMarkup = Double.valueOf(0);
		previousBudget = Double.valueOf(0);
		previousMarkup = Double.valueOf(0);
		List<RepackagingDetailComparisonWrapper> currentDetails = searchRepackagingDetails(repackagingEntry, packageNo, 
				objectCode, subsidiaryCode);
		if(currentDetails == null)
			return null;
		HashMap<Integer, RepackagingDetailComparisonWrapper> wrappers = new HashMap<Integer, RepackagingDetailComparisonWrapper>(currentDetails.size());
		for(RepackagingDetailComparisonWrapper detail : currentDetails){
//			wrappers.put(detail.hashCode(), detail);
			wrappers.put(detail.keyCode().hashCode(),detail);			
		}
		if(repackagingEntry.getRepackagingVersion() > 1){
			RepackagingEntry previousEntry = repackagingEntryDao.getRepackagingEntry(repackagingEntry.getJob(), repackagingEntry.getRepackagingVersion() - 1);
			List<RepackagingDetailComparisonWrapper> previousDetails = searchRepackagingDetails(previousEntry, packageNo, 
					objectCode, subsidiaryCode);
			if(previousDetails != null){
				for(RepackagingDetailComparisonWrapper detail : previousDetails){
					Double previousAmount = detail.getAmount() != null ? detail.getAmount() : Double.valueOf(0);
//					RepackagingDetailComparisonWrapper wrapper = wrappers.remove(detail.hashCode());
					RepackagingDetailComparisonWrapper wrapper = wrappers.remove(detail.keyCode().hashCode());
					if(wrapper != null){
						wrapper.setPreviousAmount(previousAmount);
						if(changesOnly){
							if(!wrapper.getAmount().equals(previousAmount)){
								if(detail.getSubsidiaryCode().startsWith("9")){
									totalMarkup += wrapper.getAmount();
									previousMarkup += previousAmount;	
								}
								else{
									totalBudget += wrapper.getAmount();
									previousBudget += previousAmount;
								}
								cachedResults.add(wrapper);
							}
						}
						else{
							if(detail.getSubsidiaryCode().startsWith("9")){
								totalMarkup += wrapper.getAmount();
								previousMarkup += previousAmount;	
							}
							else{
								totalBudget += wrapper.getAmount();
								previousBudget += previousAmount;
							}
							cachedResults.add(wrapper);
						}
					}
					else{
						detail.setPreviousAmount(previousAmount);
						detail.setAmount(null);
						if(detail.getSubsidiaryCode().startsWith("9"))
							previousMarkup += previousAmount;	
						else
							previousBudget += previousAmount;
						cachedResults.add(detail);
					}
				}
			}			
		}
		for(RepackagingDetailComparisonWrapper wrapper : wrappers.values()){
			if(wrapper.getSubsidiaryCode().startsWith("9"))
				totalMarkup += wrapper.getAmount();
			else
				totalBudget += wrapper.getAmount();
			cachedResults.add(wrapper);
		}
		Collections.sort(cachedResults);
		return getRepackagingDetailsByPage(0);
	}
	
	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsByPage(int pageNum){
		RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> wrapper = new RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper>();
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalBudget(totalBudget);
		wrapper.setTotalSellingValue(totalBudget + totalMarkup);
		wrapper.setPreviousBudget(previousBudget);
		wrapper.setPreviousSellingValue(previousBudget + previousMarkup);
		int size = cachedResults.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedResults.size())
			toInd = cachedResults.size();
		wrapper.setCurrentPageContentList(new ArrayList<RepackagingDetailComparisonWrapper>(cachedResults.subList(fromInd, toInd)));
		return wrapper;
	}
	
	public ExcelFile downloadRepackagingDetailExcelFile(String repackagingEntryId, String packageNo, String objectCode, String subsidiaryCode, boolean changesOnly) throws Exception  {
		List<RepackagingDetailComparisonWrapper> resultList = new ArrayList<RepackagingDetailComparisonWrapper>();
		
		RepackagingEntry repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(Long.parseLong(repackagingEntryId));
		if(repackagingEntry == null) {
			return null;
		}
		
		List<RepackagingDetailComparisonWrapper> currentDetails = searchRepackagingDetails(repackagingEntry, packageNo, objectCode, subsidiaryCode);
		if(currentDetails == null) {
			return null;
		}
			
		HashMap<Integer, RepackagingDetailComparisonWrapper> wrappers = new HashMap<Integer, RepackagingDetailComparisonWrapper>(currentDetails.size());
		for(RepackagingDetailComparisonWrapper detail : currentDetails) {
			wrappers.put(detail.keyCode().hashCode(), detail);			
		}
		
		if(repackagingEntry.getRepackagingVersion() > 1) {
			RepackagingEntry previousEntry = repackagingEntryDao.getRepackagingEntry(repackagingEntry.getJob(), repackagingEntry.getRepackagingVersion() - 1);
			List<RepackagingDetailComparisonWrapper> previousDetails = searchRepackagingDetails(previousEntry, packageNo, objectCode, subsidiaryCode);
			if(previousDetails != null) {
				for(RepackagingDetailComparisonWrapper detail : previousDetails) {
					Double previousAmount = detail.getAmount() != null ? detail.getAmount() : Double.valueOf(0);
					RepackagingDetailComparisonWrapper wrapper = wrappers.remove(detail.keyCode().hashCode());
					if(wrapper != null) {
						wrapper.setPreviousAmount(previousAmount);
						if(changesOnly) {
							if(!wrapper.getAmount().equals(previousAmount)) {
								resultList.add(wrapper);
							}
						} else {
							resultList.add(wrapper);
						}
					} else {
						detail.setPreviousAmount(previousAmount);
						detail.setAmount(null);
						resultList.add(detail);
					}
				}
			}			
		}
		
		for(RepackagingDetailComparisonWrapper wrapper : wrappers.values()) {
			resultList.add(wrapper);
		}
		
		Collections.sort(resultList);
		
		logger.info("DOWNLOAD EXCEL - REPACKAGING DETAIL: " + resultList.size());
		
		if(resultList==null || resultList.size()==0)
			return null;
	
		ExcelFile excelFile = new ExcelFile();
	
		RepackagingDetailExcelGenerator excelGenerator = new RepackagingDetailExcelGenerator(resultList);			
		excelFile = excelGenerator.generate();
	
		return excelFile;
	}
	
	public Boolean clearCache(){
		setCachedResults(null);
		return Boolean.TRUE;
	}
	
	public Boolean postRepackagingDetails(RepackagingEntry repackagingEntry, String username) throws Exception{
		logger.info("Posting repackaging details. Job: " + repackagingEntry.getJob().getJobNumber() + "; Version: " + repackagingEntry.getRepackagingVersion());
		//Delete old repackaging details from jde
		logger.info("Deleting old details from jde");
		DeleteRepackagingBudgetRequestObj deleteRequest = new DeleteRepackagingBudgetRequestObj();
		deleteRequest.setCostCenter(repackagingEntry.getJob().getJobNumber());
		DeleteRepackagingBudgetResponseObj deleteResponse = (DeleteRepackagingBudgetResponseObj)deleteRepackagingBudgetTemplate.marshalSendAndReceive(deleteRequest,
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		if(deleteResponse.getE1MessageList() != null && deleteResponse.getE1MessageList().size() != 0){
			for(com.gammon.jde.webservice.serviceRequester.DeleteRepackagingBudgetManager.getDeleteRepackagingBudget.E1Message msg : deleteResponse.getE1MessageList()){
				logger.severe(msg.getMessage());
			}
			return Boolean.FALSE;
		}
		//username to UPPERCASE
		username = username.toUpperCase();
		//Insert new details
		List<InsertRepackagingBudgetRequestObj> insertRequestList = repackagingDetailDao.preparePostingOfRepackagingDetails(repackagingEntry);
		Date postingDate = new Date();
		Calendar cal = Calendar.getInstance();
		int postingTime = 10000 * cal.get(Calendar.HOUR_OF_DAY) + 100 * cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
		for(InsertRepackagingBudgetRequestObj insertRequest : insertRequestList){
			insertRequest.setCostCenter(repackagingEntry.getJob().getJobNumber());
			if(insertRequest.getSubledger() != null && insertRequest.getSubledger().trim().length() != 0)
				insertRequest.setSubledgerType("X");
			insertRequest.setProgramId(WSPrograms.JP59140I_InsertRepackagingBudgetManager);
			insertRequest.setTransactionOriginator(webServiceConfig.getJdeWsUsername());
			insertRequest.setUserId(wsConfig.getUserName());
			insertRequest.setWorkStationId(environmentConfig.getNodeName());
			insertRequest.setDateUpdated(postingDate);
			insertRequest.setTimeLastUpdated(postingTime);
		}
		
		InsertRepackagingBudgetRequestListObj requestList = new InsertRepackagingBudgetRequestListObj();
		int records = insertRequestList.size();
		//Upload to JDE 1 page at a time
		for(int i = 0; i <= records/RECORDS_PER_PAGE; i++){
			int start = i * RECORDS_PER_PAGE;
			if(start >= records)
				break;
			int end = (i+1)*RECORDS_PER_PAGE > records ? records : (i+1)*RECORDS_PER_PAGE;
			logger.info("Inserting records " + start + " to " + end);
			requestList.setInsertFields(insertRequestList.subList(start, end));
			InsertRepackagingBudgetResponseObj response = (InsertRepackagingBudgetResponseObj)insertRepackagingBudgetTemplate.marshalSendAndReceive(requestList, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(response.getE1MessageList() != null && response.getE1MessageList().size() != 0){
				for(E1Message msg : response.getE1MessageList()){
					logger.severe(msg.getMessage());
				}
				return Boolean.FALSE;
			}
		}
		logger.info("Records posted successfully");
		return Boolean.TRUE;
	}
	
	public String sendRepackagingConfirmationEmail (EmailMessage emailMessage){
		logger.info("sendRepackagingConfirmationEmail(STARTED)");
		@SuppressWarnings("unused")
		List<User> user = new ArrayList<User>();
		try {
			user = userDao.obtainUserByAuthorities(ROLE_QS_APPROVER_ID);
			//logger.info(new Gson().toJson(user));
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		//logger.info("Test to send email" + user.size());
		Boolean emailSent = mailService.sendEmail(emailMessage);
		if(emailSent){
			return null;
		}else{
			return "Fail to send out the email. Please check the recipient address.";
		}
	}
	
	public List<User> obtainRepackagingEmailRecipients() {
		List<User> user = new ArrayList<User>();
		try {
			user = userDao.obtainUserByAuthorities(ROLE_QS_APPROVER_ID);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		logger.info(new Gson().toJson(user));
		return user;
	}

	/**
	 * Created by Henry Lai
	 * 05-Nov-2014
	 */
	public List<String> obtainUsernamesByEmails(List<String> emailAddressList) {
		List<String> usernames = new ArrayList<String>();
		for (int i=0; i<emailAddressList.size(); i++){
			User user = null;
			try {
				user = userDao.obtainByEmail(emailAddressList.get(i));
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
			if(user!=null)
				usernames.add(user.getUsername());
			else
				usernames.add(null);
		}
		return usernames;
	}

	public void setCachedResults(List<RepackagingDetailComparisonWrapper> cachedResults) {
		this.cachedResults = cachedResults;
	}

	public List<RepackagingDetailComparisonWrapper> getCachedResults() {
		return cachedResults;
	}

}
