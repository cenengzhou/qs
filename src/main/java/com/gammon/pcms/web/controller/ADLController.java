package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.adl.JobDashboardDTO;
import com.gammon.pcms.dto.rs.provider.response.view.AddressBookView;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountBalanceAAJI;
import com.gammon.pcms.model.adl.AccountBalanceSC;
import com.gammon.pcms.model.adl.AccountLedger;
import com.gammon.pcms.model.adl.AccountMaster;
import com.gammon.pcms.model.adl.AddressBook;
import com.gammon.pcms.model.adl.ApprovalDetail;
import com.gammon.pcms.model.adl.ApprovalHeader;
import com.gammon.pcms.model.adl.BusinessUnit;
import com.gammon.pcms.service.ADLService;

@RestController
@RequestMapping(value = "service/adl/",
				produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class ADLController {

	// private static Logger logger = Logger.getLogger(ADLController.class);

	@Autowired
	private ADLService adlService;

	/**
	 * * To get Job Cost records (limited to AA & JI Ledgers) based on year, month, ledger type, job and sub-contract <br/>
	 * AA Ledger - Actual Spending with SCRate for Sub-contract & CostRate for L, P, M, O (including Document Type: PS, JS, RI, RM, JE, etc...) <br/>
	 * JI Ledger - Internal Valuation with CostRate (including Document Type: JI) <br/>
	 * Account Codes limited to Object Codes start with '1' with Subsidiary Code <br/>
	 *
	 * @param noJob
	 * @param noSubcontract
	 * @param year
	 * @param month
	 * @return
	 * @author tikywong
	 * @since Jul 6, 2016 1:49:37 PM
	 */
	@RequestMapping(value = "getMonthlyJobCostList",
					method = RequestMethod.GET)
	public List<?> getMonthlyJobCostList(
											@RequestParam(required = true) String noJob,
											@RequestParam(required = false) String noSubcontract,
											@RequestParam(	required = false,
															defaultValue = "0") BigDecimal year,
											@RequestParam(	required = false,
															defaultValue = "0") BigDecimal month) {
		try {
			return adlService.getMonthlyJobCostList(year, month, noJob, noSubcontract);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * * * To get AA and JI records based on Account Code <br/>
	 * AA Ledger - Actual Spending with SCRate for Sub-contract & CostRate for L, P, M, O (including Document Type: PS, JS, RI, RM, JE, etc...) <br/>
	 * JI Ledger - Internal Valuation with CostRate (including Document Type: JI) <br/>
	 *
	 * @param noJob
	 * @param codeObject
	 * @param yearStart
	 * @param yearEnd
	 * @param codeSubsidiary
	 * @return
	 * @author tikywong
	 * @since Jul 6, 2016 1:53:23 PM
	 */
	@RequestMapping(value = "getAAJILedgerList",
					method = RequestMethod.GET)
	public List<?> getAAJILedgerList(	@RequestParam(required = true) String noJob,
										@RequestParam(required = false) String noSubcontract,
										@RequestParam(	required = false,
														defaultValue = "0") BigDecimal yearStart,
										@RequestParam(	required = false,
														defaultValue = "0") BigDecimal yearEnd,
										@RequestParam(required = false) String codeObject,
										@RequestParam(required = false) String codeSubsidiary) {
		try {
			return adlService.getAAJILedgerList(yearStart, yearEnd, noJob, noSubcontract, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountBalanceAAJI>();
		}
	}

	/**
	 * Monthly Cash Flow including: </br>
	 * Contract Receivable (AA-2312XX) </br>
	 * Turnover/IV - Cost Rate * SC WD Quantity & Cost Rate * LPMO WD Quantity (AA-221120) </br>
	 * Original Budget (OB-All) </br>
	 * Payment - SC Rate * SC Certified Quantity & Cost Rate * LPMO Certified Quantity (AA-311100+311200) </br>
	 *
	 * @param noJob
	 * @param yearStart
	 * @param yearEnd
	 * @return
	 * @author tikywong
	 * @since Jul 11, 2016 9:26:48 AM
	 */
	@RequestMapping(value = "getJobDashboardData",
					method = RequestMethod.GET)
	public JobDashboardDTO getJobDashboardData(	@RequestParam(required = true) String noJob,
												@RequestParam(	required = false,
																defaultValue = "0") BigDecimal yearStart,
												@RequestParam(	required = false,
																defaultValue = "0") BigDecimal yearEnd) {
		try {
			return adlService.getJobDashboardData(yearStart, yearEnd, noJob);
		} catch (Exception e) {
			e.printStackTrace();
			return new JobDashboardDTO();
		}
	}

	@RequestMapping(value = "getAccountBalanceList",
					method = RequestMethod.GET)
	public List<AccountBalance> getAccountBalanceList(	@RequestParam(required = true) String noJob,
														@RequestParam(	required = false,
																		defaultValue = "0") BigDecimal yearStart,
														@RequestParam(	required = false,
																		defaultValue = "0") BigDecimal yearEnd,
														@RequestParam(required = false) String typeLedger,
														@RequestParam(required = false) String codeObject,
														@RequestParam(required = false) String codeSubsidiary) {
		try {
			return adlService.getAccountBalanceList(yearStart, yearEnd, typeLedger, noJob, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountBalance>();
		}
	}

	@RequestMapping(value = "getAccountBalanceSCList", method = RequestMethod.GET)
	public List<AccountBalanceSC> getAccountBalanceSCList(	@RequestParam(required = true) String noJob,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal yearStart,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal yearEnd,
													@RequestParam(required = false) String typeLedger,
													@RequestParam(required = false) String codeObject,
													@RequestParam(required = false) String codeSubsidiary) {
		try {
			return adlService.getAccountBalanceSCList(yearStart, yearEnd, typeLedger, noJob, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountBalanceSC>();
		}
	}

	@RequestMapping(value = "getAccountLedgerList",
					method = RequestMethod.GET)
	public List<AccountLedger> getAccountLedgerList(@RequestParam String noJob,
													@RequestParam String typeLedger,
													@RequestParam(defaultValue = "0") BigDecimal yearStart,
													@RequestParam(defaultValue = "0") BigDecimal yearEnd,
													@RequestParam(defaultValue = "0") BigDecimal monthStart,
													@RequestParam(defaultValue = "0") BigDecimal monthEnd,
													@RequestParam(required = false) String typeDocument,
													@RequestParam(required = false) String noSubcontract,
													@RequestParam(required = false) String codeObject,
													@RequestParam(required = false) String codeSubsidiary) {
		try {
			return adlService.getAccountLedgerList(yearStart, yearEnd, monthStart, monthEnd, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountLedger>();
		}
	}

	@RequestMapping(value = "getAccountMasterList",
					method = RequestMethod.GET)
	public List<AccountMaster> getAccountMasterList(@RequestParam(required = true) String noJob) {
		try {
			return adlService.getAccountMasterList(noJob);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountMaster>();
		}
	}

	@RequestMapping(value = "getAccountMaster",
					method = RequestMethod.GET)
	public AccountMaster getAccountMaster(	@RequestParam(required = true) String noJob,
											@RequestParam(required = true) String codeObject,
											@RequestParam(	required = false,
															defaultValue = AccountMaster.CODE_SUBSIDIARY_EMPTY) String codeSubsidiary) {
		try {
			return adlService.getAccountMaster(noJob, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// TODO: migrate from web service to ADL
	@JsonView(AddressBookView.NameAndCodeAndApproved.class)
	@RequestMapping(value = "getAddressBookListOfSubcontractorAndClient",
					method = RequestMethod.GET)
	public List<AddressBook> getAddressBookListOfSubcontractorAndClient() {
		try {
			return adlService.getAddressBookListOfSubcontractorAndClient();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	// TODO: migrate from web service to ADL
	@RequestMapping(value = "getAddressBook",
					method = RequestMethod.GET)
	public AddressBook getAddressBook(@RequestParam(required = true) BigDecimal addressBookNo) {
		try {
			return adlService.getAddressBook(addressBookNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// TODO: migrate from web service to ADL
	@RequestMapping(value = "getBusinessUnit",
					method = RequestMethod.GET)
	public BusinessUnit getBusinessUnitList(@RequestParam(required = true) String jobNo) {
		try {
			return adlService.getBusinessUnit(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "getApprovalHeaderList",
					method = RequestMethod.GET)
	public List<ApprovalHeader> getApprovalHeaderList(	@RequestParam(	required = true,
																		defaultValue = "APPROVED") String statusApproval,
														@RequestParam(required = true) String noJob,
														@RequestParam(	required = true,
																		defaultValue = "SC") String typeApprovalCategory,
														@RequestParam(required = false) String typeApproval,
														@RequestParam(required = false) String noSubcontract,
														@RequestParam(required = false) String noPayment,
														@RequestParam(required = false) String noMainCert,
														@RequestParam(required = false) String noAddendum,
														@RequestParam(	required = false,
																		defaultValue = "0") BigDecimal recordKeyInstance) {

		try {
			return adlService.getApprovalHeaderList(statusApproval, noJob, typeApprovalCategory, typeApproval, noSubcontract, noPayment, noMainCert, noAddendum, recordKeyInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ApprovalHeader>();
		}
	}

	// TODO: Pending for CNC team to add unique id (2016-07-15)
	@RequestMapping(value = "getApprovalDetailList",
					method = RequestMethod.GET)
	public List<ApprovalDetail> getApprovalDetailList(	@RequestParam(	required = false,
																		defaultValue = "APPROVED") String statusApproval,
														@RequestParam(required = false) String noJob,
														@RequestParam(	required = true,
																		defaultValue = "SC") String typeApprovalCategory,
														@RequestParam(	required = false,
																		defaultValue = "PAYMENT") String typeApproval,
														@RequestParam(required = false) String noSubcontract,
														@RequestParam(required = false) String noPayment,
														@RequestParam(required = false) String noMainCert,
														@RequestParam(required = false) String noAddendum,
														@RequestParam(	required = false,
																		defaultValue = "0") BigDecimal recordKeyInstance) {
		try {
			return adlService.getApprovalDetailList(statusApproval, noJob, typeApprovalCategory, typeApproval, noSubcontract, noPayment, noMainCert, noAddendum, recordKeyInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ApprovalDetail>();
		}
	}
}
