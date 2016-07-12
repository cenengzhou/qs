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

import com.gammon.pcms.dto.rs.provider.response.adl.JobDashboardDTO;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountBalanceAAJI;
import com.gammon.pcms.model.adl.AccountLedger;
import com.gammon.pcms.service.ADLService;

@RestController
@RequestMapping(value = "service/adl/",
				produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class ADLController {

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

	@RequestMapping(value = "getAccountLedgerList",
					method = RequestMethod.GET)
	public List<AccountLedger> getAccountLedgerList(@RequestParam(required = true) String noJob,
													@RequestParam(	required = true,
																	defaultValue = "AA") String typeLedger,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal yearStart,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal yearEnd,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal monthStart,
													@RequestParam(	required = false,
																	defaultValue = "0") BigDecimal monthEnd,
													@RequestParam(	required = false,
																	defaultValue = "JS") String typeDocument,
													@RequestParam(required = false) String noSubcontract,
													@RequestParam(	required = false,
																	defaultValue = "3440") String codeObject,
													@RequestParam(required = false) String codeSubsidiary) {
		try {
			return adlService.getAccountLedgerList(yearStart, yearEnd, monthStart, monthEnd, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AccountLedger>();
		}
	}

	@RequestMapping(value = "getApprovalHeader",
					method = RequestMethod.GET)
	public Object getApprovalHeader(@RequestParam(required = true) String statusApproval,
									@RequestParam(required = true) String noJob,
									@RequestParam(	required = true,
													defaultValue = "SC") String typeApproval,
									@RequestParam(required = false) String noSubcontract,
									@RequestParam(required = false) String noPayment,
									@RequestParam(required = false) String noMainCert,
									@RequestParam(required = false) BigDecimal recordKeyInstance) {

		try {
			return adlService.getApprovalHeader(statusApproval, noJob, typeApproval, noSubcontract, noPayment, noMainCert, recordKeyInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return new Object();
		}
	}

	public List<Object> getApprovalDetailList(	@RequestParam(required = false) String statusApproval,
												@RequestParam(required = false) String noJob,
												@RequestParam(	required = true,
																defaultValue = "SC") String typeApproval,
												@RequestParam(required = false) String noSubcontract,
												@RequestParam(required = false) String noPayment,
												@RequestParam(required = false) String noMainCert,
												@RequestParam(required = false) BigDecimal recordKeyInstance) {
		return adlService.getApprovalDetailList(statusApproval, noJob, typeApproval, noSubcontract, noPayment, noMainCert, recordKeyInstance);
	}
}
