package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.AccountBalanceAAJIDao;
import com.gammon.pcms.dao.adl.AccountBalanceAAJISCDao;
import com.gammon.pcms.dao.adl.AccountBalanceDao;
import com.gammon.pcms.dao.adl.AccountBalanceSCDao;
import com.gammon.pcms.dao.adl.AccountLedgerDao;
import com.gammon.pcms.dao.adl.AccountMasterDao;
import com.gammon.pcms.dao.adl.AddressBookDao;
import com.gammon.pcms.dao.adl.ApprovalDetailDao;
import com.gammon.pcms.dao.adl.ApprovalHeaderDao;
import com.gammon.pcms.dao.adl.BusinessUnitDao;
import com.gammon.pcms.dao.adl.SubcontractorWorkscopeDao;
import com.gammon.pcms.dto.rs.provider.response.JobDashboardDTO;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountBalanceFigure;
import com.gammon.pcms.model.adl.AccountBalanceSC;
import com.gammon.pcms.model.adl.AccountLedger;
import com.gammon.pcms.model.adl.AccountMaster;
import com.gammon.pcms.model.adl.AddressBook;
import com.gammon.pcms.model.adl.ApprovalDetail;
import com.gammon.pcms.model.adl.ApprovalHeader;
import com.gammon.pcms.model.adl.BusinessUnit;
import com.gammon.pcms.model.adl.SubcontractorWorkscope;
import com.gammon.pcms.wrapper.DashboardDataRange;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.service.RepackagingService;
import com.gammon.qs.service.admin.AdminService;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(	readOnly = true, rollbackFor = Exception.class, value = "adlTransactionManager")
public class ADLService {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AccountMasterDao accountMasterDao;
	@Autowired
	private AccountLedgerDao accountLedgerDao;
	@Autowired
	private AccountBalanceDao accountBalanceDao;
	@Autowired
	private AccountBalanceSCDao accountBalanceSCDao;
	@Autowired
	private AccountBalanceAAJIDao accountBalanceAAJIDao;
	@Autowired
	private AccountBalanceAAJISCDao accountBalanceAAJISCDao;
	@Autowired
	private ApprovalHeaderDao approvalHeaderDao;
	@Autowired
	private ApprovalDetailDao approvalDetailDao;
	@Autowired
	private AddressBookDao addressBookDao;
	@Autowired
	private BusinessUnitDao businessUnitDao;
	@Autowired
	private RepackagingService repackagingService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SubcontractorWorkscopeDao subcontractorWorkscopeDao;
	
	/*
	 * ----------------------------------------------- JDE @ Data Layer -----------------------------------------------
	 */
	/**
	 * AA & JI Ledger only
	 *
	 * @param year
	 * @param month
	 * @param noJob
	 * @param noSubcontract
	 * @return
	 * @author tikywong
	 * @since Jul 6, 2016 1:49:12 PM
	 */
	public List<?> getMonthlyJobCostList(	BigDecimal year,
											BigDecimal month,
											String noJob,
											String noSubcontract) {
		if (StringUtils.isEmpty(noSubcontract))
			return accountBalanceAAJIDao.findMonthlyJobCost(year, month, noJob);
		else
			return accountBalanceAAJISCDao.findMonthlyJobCost(year, month, noJob, noSubcontract);
	}

	public List<?> getMonthlyJobCostListByPeriodRange(String noJob, String noSubcontract, BigDecimal fromYear, BigDecimal fromMonth, BigDecimal toYear, BigDecimal toMonth) {
		if (StringUtils.isEmpty(noSubcontract))
		return accountBalanceAAJIDao.findMonthlyJobCostByPeriodRange(noJob, fromYear, fromMonth, toYear, toMonth);
		else
		return accountBalanceAAJISCDao.findMonthlyJobCostByPeriodRange(noJob, noSubcontract, fromYear, fromMonth, toYear, toMonth);
	}

	/**
	 * General searching with account code in AA & JI Ledger only
	 *
	 * @param startYear
	 * @param endYear
	 * @param jobNo
	 * @param objectCode
	 * @return
	 * @author tikywong
	 * @since Jul 5, 2016 10:06:26 AM
	 */
	public List<?> getAAJILedgerList(	BigDecimal startYear,
										BigDecimal endYear,
										String jobNo,
										String subcontractNo,
										String objectCode,
										String subsidiaryCode) {
		if (StringUtils.isEmpty(subcontractNo))
			return accountBalanceAAJIDao.find(startYear, endYear, jobNo, objectCode, subsidiaryCode);
		else
			return accountBalanceAAJISCDao.find(startYear, endYear, jobNo, subcontractNo, objectCode, subsidiaryCode);
	}

	/**
	 * Account Balance with all Ledger Types
	 *
	 * @param year
	 * @param month
	 * @param ledgerType
	 * @param jobNo
	 * @param subcontractNo
	 * @param objectCode
	 * @param subsidiaryCode
	 * @return
	 * @author	tikywong
	 * @since	Aug 24, 2016 5:10:00 PM
	 */
	public List<?> getAccountBalanceList(	BigDecimal year,
											BigDecimal month,
											String ledgerType,
											String jobNo,
											String subcontractNo,
											String objectCode,
											String subsidiaryCode) {
		if (StringUtils.isEmpty(subcontractNo))
			return accountBalanceDao.find(year, month, ledgerType, jobNo, objectCode, subsidiaryCode);
		else
			return accountBalanceSCDao.find(year, month, ledgerType, jobNo, subcontractNo, objectCode, subsidiaryCode);
	}

	private List<BigDecimal> packDataListTo12Months(List<BigDecimal> dataList) {
		if (dataList != null && dataList.size()>0){
			while(dataList.size()<12){
				dataList.add(dataList.get(dataList.size()-1));
			}
		}else{
			while(dataList.size()<12){
				dataList.add(new BigDecimal(0));
			}
		}
		return dataList;
	}

	private List<BigDecimal> adaptToDashboardDataList(List<AccountBalanceFigure> list) {
		List<BigDecimal> result = new ArrayList<>();
		for(AccountBalanceFigure x : list) {
			result.add(x.getAmount());
		}
		return result;
	}

	private List<BigDecimal> packDataListByDashboardDataRange(List<AccountBalanceFigure> list, DashboardDataRange range) {
		List<BigDecimal> result = new ArrayList<>();

		Date start = new Date(range.getStartDate().getTime());
		Date end = new Date(range.getEndDate().getTime());
		BigDecimal lastAmount = new BigDecimal(0);

		// loop through every month from start month to end month
		for (Date current = start; current.before(end); ) {
			SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
			BigDecimal year = new BigDecimal(yearFormat.format(current));
			BigDecimal month = new BigDecimal(monthFormat.format(current));

			// find current figure by year and month
			AccountBalanceFigure currentFigure = list.stream().filter(x -> x.getYear().equals(year) && x.getMonth().equals(month)).findFirst().orElse(null);

			// put amount in data list
			if (currentFigure != null) {
				result.add(currentFigure.getAmount());
				lastAmount = currentFigure.getAmount();
			} else {
				result.add(lastAmount);
			}

			// iterate to next month
			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			cal.add(Calendar.MONTH, 1);
			current = cal.getTime();
		}
		return result;
	}

	/**
	 * Monthly Cash Flow for Job Dash Board
	 *
	 * @param noJob
	 * @param item
	 * @return
	 * @author tikywong
	 * @since Jul 12, 2016 2:16:59 PM
	 */
	public List<JobDashboardDTO> getJobDashboardData(String noJob, String item) {
		DashboardDataRange dashboardDataRange = new DashboardDataRange(item);

		BigDecimal startYearMonth = dashboardDataRange.getStartYYMM();
		BigDecimal endYearMonth = dashboardDataRange.getEndYYMM();
		String endYear = dashboardDataRange.getEndDateFormat("yy");

		List<JobDashboardDTO> result = new ArrayList<>();
		try {
			List<AccountBalanceFigure> contractReceivableList = accountBalanceDao.findFiguresByRangeYearMonth(startYearMonth, endYearMonth, AccountBalanceSC.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_CONTRACT_RECEIVABLE, AccountBalance.CODE_SUBSIDIARY_EMPTY);
			List<AccountBalanceFigure> turnoverList = accountBalanceDao.findFiguresByRangeYearMonth(startYearMonth, endYearMonth, AccountBalanceSC.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_TURNOVER, AccountBalance.CODE_SUBSIDIARY_EMPTY);
			List<AccountBalanceFigure> actualValueList = accountBalanceDao.calculateSumOfActualValueByRangeYearMonth(startYearMonth, endYearMonth, AccountBalance.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_COSTCODE_STARTER, AccountBalance.CODE_SUBSIDIARY_EMPTY);
			List<BigDecimal> totalBudgetList = repackagingService.getRepackagingMonthlySummary(noJob, endYear);

			packDataListTo12Months(totalBudgetList);

			JobDashboardDTO contractReceivableWrapper = new JobDashboardDTO();
			contractReceivableWrapper.setCategory("CR");
			contractReceivableWrapper.setStartYear(dashboardDataRange.getStartYear());
			contractReceivableWrapper.setEndYear(dashboardDataRange.getEndYear());
			contractReceivableWrapper.setMonthList(dashboardDataRange.toMonthList());
			List<BigDecimal> crDataList = packDataListByDashboardDataRange(contractReceivableList, dashboardDataRange);
			contractReceivableWrapper.setDetailList(crDataList.stream().map(x -> x.negate()).collect(Collectors.toList()));

			JobDashboardDTO turnoverWrapper = new JobDashboardDTO();
			turnoverWrapper.setCategory("IV");
			turnoverWrapper.setStartYear(dashboardDataRange.getStartYear());
			turnoverWrapper.setEndYear(dashboardDataRange.getEndYear());
			turnoverWrapper.setMonthList(dashboardDataRange.toMonthList());
			List<BigDecimal> ivDataList = packDataListByDashboardDataRange(turnoverList, dashboardDataRange);
			turnoverWrapper.setDetailList(ivDataList);

			JobDashboardDTO actualValueWrapper = new JobDashboardDTO();
			actualValueWrapper.setCategory("AV");
			actualValueWrapper.setStartYear(dashboardDataRange.getStartYear());
			actualValueWrapper.setEndYear(dashboardDataRange.getEndYear());
			actualValueWrapper.setMonthList(dashboardDataRange.toMonthList());
			List<BigDecimal> avDataList = packDataListByDashboardDataRange(actualValueList, dashboardDataRange);
			actualValueWrapper.setDetailList(avDataList);

			JobDashboardDTO totalBudgetWrapper = new JobDashboardDTO();
			totalBudgetWrapper.setCategory("TB");
			totalBudgetWrapper.setStartYear(dashboardDataRange.getStartYear());
			totalBudgetWrapper.setEndYear(dashboardDataRange.getEndYear());
			totalBudgetWrapper.setMonthList(dashboardDataRange.toMonthList());
			totalBudgetWrapper.setDetailList(totalBudgetList);

			result.add(contractReceivableWrapper);
			result.add(turnoverWrapper);
			result.add(actualValueWrapper);
			result.add(totalBudgetWrapper);
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Account Ledger general searching
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param monthStart
	 * @param monthEnd
	 * @param typeLedger
	 * @param typeDocument
	 * @param noJob
	 * @param noSubcontract
	 * @param codeObject
	 * @param codeSubsidiary
	 * @return
	 * @author tikywong
	 * @since Jul 11, 2016 3:17:56 PM
	 */
	public List<AccountLedger> getAccountLedgerList(BigDecimal yearStart, BigDecimal yearEnd, BigDecimal monthStart, BigDecimal monthEnd, String typeLedger, String typeDocument, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) {
		return accountLedgerDao.find(yearStart, yearEnd, monthStart, monthEnd, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
	}
	
	public List<AccountLedger> getAccountLedgerListByGlDate(Date fromDate, Date thruDate, String typeLedger, String typeDocument, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) {
		//filter by cost code only (i.e. object code starts with '1') if no object / subsidiary is passed in
		if(StringUtils.isEmpty(codeObject) && StringUtils.isEmpty(codeSubsidiary))
			codeObject = AccountLedger.CODE_OBJECT_COSTCODE_STARTER;
		
		return accountLedgerDao.find(fromDate, thruDate, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
	}
	/**
	 * Account Master general searching
	 *
	 * @param noJob
	 * @return
	 * @author tikywong
	 * @since Jul 22, 2016 11:44:46 AM
	 */
	public List<AccountMaster> getAccountMasterList(String noJob) {
		return accountMasterDao.find(noJob);
	}
	
	/**
	 * Find unique Account Master
	 *
	 * @param noJob
	 * @param codeObject
	 * @param codeSubsidiary
	 * @return
	 * @author	tikywong
	 * @since	Jul 22, 2016 12:00:56 PM
	 */
	public AccountMaster getAccountMaster(String noJob, String codeObject, String codeSubsidiary){
		return accountMasterDao.findByAccountCode(noJob, codeObject, codeSubsidiary);
	}
	
	public List<AddressBook> getAddressBookListOfSubcontractorAndClient(String addressBookParam, String addressBookTypeCode) throws Exception{
		List<String> addressBookTypeList = new ArrayList<String>();
		// should be enum of AddressBook class
		addressBookTypeList.add(AddressBook.TYPE_VENDOR);
		addressBookTypeList.add(AddressBook.TYPE_CLIENT);
		addressBookTypeList.add(AddressBook.TYPE_COMPANY);
		//Map<String, Set<String>> subcontractorWorkscopeMap = masterListService.obtainSubcontractorWorkScopeMap(null, null);

		List<AddressBook> addressBookList = new ArrayList<AddressBook>();
		if(!GenericValidator.isBlankOrNull(addressBookTypeCode)){
			if (!GenericValidator.isBlankOrNull(addressBookParam)){
				try {
					if (addressBookTypeCode.startsWith("V")) {
						String[] split = addressBookParam.split(",");
						String subcontractorName = split[0].equals("null") ? "": split[0];
						String workscope = split[1].equals("null") ? "": split[1];
						if (subcontractorName.isEmpty() && workscope.isEmpty()) {
							return addressBookList;
						} else if (subcontractorName.isEmpty() && !workscope.isEmpty()) {
							List<SubcontractorWorkscope> subcontractorWorkscopes = subcontractorWorkscopeDao.obtainWorkscopeByCode(workscope);
							for (SubcontractorWorkscope s : subcontractorWorkscopes) {
								if (s.getAddressBook() != null) {
									Hibernate.initialize(s.getAddressBook().getSubcontractorWorkscopes());
									addressBookList.add(s.getAddressBook());
								}
							}
						} else if (!subcontractorName.isEmpty() && workscope.isEmpty()) {
							if (StringUtils.isNumeric(subcontractorName)){
								addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, new BigDecimal(subcontractorName), null);
							} else {
								addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, null, subcontractorName);
							}
						} else {
							if (StringUtils.isNumeric(subcontractorName)) {
								addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, new BigDecimal(subcontractorName), null);
							} else {
								addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, null, subcontractorName);
							}
							addressBookList = addressBookList.stream().filter(e -> e.getSubcontractorWorkscopes().stream().anyMatch(o -> o.getCodeWorkscope().trim().equals(workscope))).collect(Collectors.toList());
						}
					} else {
						Integer.parseInt(addressBookParam);
						addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, new BigDecimal(addressBookParam), null);
					}
				} catch (Exception e) {
					addressBookList = addressBookDao.findByAddressBook(addressBookTypeCode, null, addressBookParam);
				}
			}
			
		}else
			addressBookList = addressBookDao.findByAddressBookTypeList(addressBookTypeList);		
		
		
			
//		for(AddressBook addressBook : addressBookList) {
//			PayeeMaster payeeMaster = payeeMasterHBDao.find(addressBook.getAddressBookNumber());
//			if(payeeMaster != null){
//				addressBook.setHoldPaymentCode(payeeMaster.getHoldPaymentCode());
//			}
//			addressBook.setWorkScopeList(subcontractorWorkscopeMap.get(addressBook.getAddressBookNumber().toString()));
//		}
				
		return addressBookList;
	}
	
	public AddressBook getAddressBook(BigDecimal addressBookNo) throws DataAccessException {
		return addressBookDao.get(addressBookNo);
	}
	
	public AddressBook obtainSubcontractor(BigDecimal addressBookNo) {
		return addressBookDao.obtainSubcontractor(addressBookNo);
	}
	
	public BusinessUnit getBusinessUnit(String jobNo) throws DataAccessException {
		return businessUnitDao.find(jobNo);
	}
	
	/*
	 * ----------------------------------------------- Approval System @ Data Layer -----------------------------------------------
	 */
	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApprovalCategory
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param recordKeyInstance
	 * @return
	 * @author tikywong
	 * @since Jul 12, 2016 2:24:46 PM
	 */
	public List<ApprovalHeader> getApprovalHeaderList(String statusApproval, String noJob, String typeApprovalCategory, String typeApproval, String noSubcontract, String noPayment, String noMainCert, String noAddendum, BigDecimal recordKeyInstance) {
		// Search with recordKeyInstance
		if (recordKeyInstance.longValue() > 0) {
			List<ApprovalHeader> approvalHeaderList = new ArrayList<ApprovalHeader>();
			approvalHeaderList.add(approvalHeaderDao.findByRecordKeyInstance(recordKeyInstance));
			return approvalHeaderList;
		}
		else {
			// Search with prefixes
			List<String> recordKeyDocumentList = new ArrayList<String>();

			if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.AWARD.toString())) {// Award - AW, ST, V5, V6
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.AW.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.ST.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.V5.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.V6.toString());

			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.PAYMENT.toString())) {// Payment - SP, SF, NP, FR
				if (StringUtils.isEmpty(noPayment)) {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SP.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SF.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.NP.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.FR.toString());
				} else {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SP.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SF.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.NP.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.FR.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
				}
			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.ADDENDUM.toString())) { // Addendum - SM, SL
				if (StringUtils.isEmpty(noAddendum)) {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SM.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SL.toString());
				} else {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SM.toString() + "-" + StringUtils.leftPad(noAddendum, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SL.toString() + "-" + StringUtils.leftPad(noAddendum, 3, '0'));
				}
			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.CERTIFICATE.toString())) // Main Certificate
				recordKeyDocumentList.add(noJob + "-" + noMainCert + "-" + ApprovalHeader.TYPE_DOCUMENT.RM.toString());
			else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.SPLIT.toString())) // Split Subcontract - VA
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.VA.toString());
			else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.TERMINATE.toString())) // Terminate Subcontract - VB
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.VB.toString());

			// Search with prepared prefixes
			List<ApprovalHeader> approvalHeaderList = new ArrayList<ApprovalHeader>();
			for (String recordKeyDocument : recordKeyDocumentList) {
				approvalHeaderList = approvalHeaderDao.findByRecordKeyDocument(recordKeyDocument, statusApproval);
				if (!approvalHeaderList.isEmpty()) {
					logger.info("Found with prefix: " + recordKeyDocument);
					return approvalHeaderList;
				}
			}
		}

		logger.info("No record found.");
		return new ArrayList<ApprovalHeader>();
	}

	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApprovalCategory
	 * @param typeApproval
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param noAddendum
	 * @param recordKeyInstance
	 * @return
	 * @author	tikywong
	 * @since	Jul 19, 2016 12:08:23 PM
	 */
	public List<ApprovalDetail> getApprovalDetailList(String statusApproval, String noJob, String typeApprovalCategory, String typeApproval, String noSubcontract, String noPayment, String noMainCert, String noAddendum, BigDecimal recordKeyInstance) {
		if (recordKeyInstance.longValue() > 0)
			return approvalDetailDao.obtainApprovalDetailByRecordKeyInstance(recordKeyInstance);
		else {
			// Search for "recordKeyInstance"(s) from Approval Header
			List<ApprovalHeader> approvalHeaderList = getApprovalHeaderList(statusApproval, noJob, typeApprovalCategory, typeApproval, noSubcontract, noPayment, noMainCert, noAddendum, recordKeyInstance);
			if (approvalHeaderList.isEmpty()){
				logger.error("No Approval Header is found.");
				return new ArrayList<ApprovalDetail>();
			}
			else if (approvalHeaderList.size() > 1) {
				logger.error("Non unique Approval Headers are found.");
				return new ArrayList<ApprovalDetail>();
			} else {
				List<ApprovalDetail> approvalDetailList = approvalDetailDao.obtainApprovalDetailByRecordKeyInstance(approvalHeaderList.get(0).getRecordKeyInstance());
				if (!approvalDetailList.isEmpty())
					return approvalDetailList;
			}
			return new ArrayList<ApprovalDetail>();
		}
	}
	
	/**
	 * 
	 * @return the company list which accessible to current user
	 * @throws DatabaseOperationException 
	 */
	public List<Map<String, String>> obtainCompanyCodeAndName() throws DatabaseOperationException{
		Map<String, Map<String, String>> allCompanyMap = businessUnitDao.obtainCompanyCodeAndName();
		List<Map<String, String>> resultList = new ArrayList<>();
		List<String> companyCodeList = adminService.obtainCompanyCodeListByCurrentUser();
		for(String companyCode : companyCodeList){
			Map<String, String> companyMap = allCompanyMap.get(companyCode);
			if(companyMap != null){
				resultList.add(companyMap);
			}
		}
		return resultList;
	}
	
	public List<SubcontractorWorkscope> getAllWorkScopes() throws DatabaseOperationException{
		return subcontractorWorkscopeDao.getAllWorkScopes();
	}
	
	
}
