package com.gammon.qs.client.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryPaginationWrapper;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryWrapper;
import com.gammon.qs.wrapper.SCDetailsWrapper;
import com.gammon.qs.wrapper.SCListPaginationWrapper;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper;
import com.gammon.qs.wrapper.addendumApproval.AddendumApprovalResponseWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeEnquiryWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargePaginationWrapper;
import com.gammon.qs.wrapper.currencyCode.CurrencyCodeWrapper;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.listNonAwardedSCPackage.ListNonAwardedSCPackageWrapper;
import com.gammon.qs.wrapper.nonAwardedSCApproval.NonAwardedSCApprovalResponseWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalPaginationWrapper;
import com.gammon.qs.wrapper.sclist.ScListView;
import com.gammon.qs.wrapper.splitTerminateSC.UpdateSCDetailNewQuantityWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisWrapper;
import com.gammon.qs.wrapper.updateAddendum.UpdateAddendumWrapper;
import com.gammon.qs.wrapper.updateSCPackage.UpdateSCPackageSaveWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface PackageRepositoryRemote extends RemoteService {
	List<SCPackage> getPackages(String jobNumber) throws Exception;
	SCPackage getNotAwardedPackage(String jobNumber, String packageNo, String packageType) throws Exception;
	List<String> obtainUneditableUnawardedPackageNos(Job job) throws Exception;
	List<String> obtainUnawardedPackageNosUnderPaymentRequisition(Job job) throws Exception;
	
	String updateWDandCertQuantity(List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList);	
	String addAddendumByWrapperStr(AddAddendumWrapper wrapper) throws Exception;
	String addAddendumByWrapperListStr(List<AddAddendumWrapper> wrapperList) throws Exception;

	List<SCDetails> getAddendumEnquiry(String jobNumber, String packageNo) throws Exception;
	List<SCDetails> obtainSCDetails(String jobNumber, String packageNumber, String packageType) throws DatabaseOperationException;
	SCDetails getSCLine(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode) throws Exception;
	PaginationWrapper<SCDetailsWrapper> getSCDetailsOfPackageByPagination(String jobNumber, String packageNumber, String packageType) throws Exception;
	PaginationWrapper<SCDetailsWrapper> getSCDetailListAtPage(int pageNum) throws Exception;
	AddendumApprovalResponseWrapper submitAddendumApproval(String jobNumber, Integer subcontractNumber, Double certAmount, String userID) throws Exception;
	NonAwardedSCApprovalResponseWrapper nonAwardedSCApproval(String jobNumber, Integer subcontractNumber, Integer subcontractorNumber, Double certAmount, Double budgetAmount, String userID) throws Exception;
	Boolean updateAddendumByWrapper(UpdateAddendumWrapper wrapper) throws Exception;
	Boolean updateNewQuantity(Long id, Double newQuantity) throws Exception;
	String updateSCDetailsNewQuantity(List<UpdateSCDetailNewQuantityWrapper> updateSCDetailNewQuantityWrappers) throws DatabaseOperationException;
		
	SCDetails defaultValuesForAddingSCDetailLines(String jobNumber, Integer subcontractNumber, String scLineType) throws Exception;
	
	ScListView getSCListView(String jobNumber, String packageNumber, String packageType) throws Exception;
	String suspendAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception;
	String deleteAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception;
	List<ListNonAwardedSCPackageWrapper> retrieveNonAwardedSCPackageList(String jobNumber) throws Exception;
	List<TenderAnalysisWrapper> retrieveTenderAnalysisList(String trim, String trim2) throws Exception;
	String submitSplitTerminateSC(String jobNumber, Integer subcontractNumber, String splitTerminate, String userID) throws Exception;
	Boolean triggerUpdateSCPaymentDetail(String jobNumber, String packageNo, String ifPayment, String createUser, String directPaymentIndicator) throws DatabaseOperationException;
	
	List<SCPackage> getPackagesFromList(Job job, List<String> packageNos) throws Exception;
	List<SCPackage> getSCPackages(Job job) throws Exception;
	SCPackage obtainSCPackage(Job job, String packageNo) throws Exception;
	SCPackage getSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException;
	SCPackage getSCPackageForPackagePanel(Job job, String packageNo) throws Exception;
	String saveOrUpdateSCPackage(SCPackage scPackage) throws Exception;
	String[][] getUnawardedPackageNos(Job job) throws Exception;
	String[][] getAwardedPackageStore(Job job) throws Exception;
	List<String> getUneditablePackageNos(Job job) throws Exception;
	List<String> getAwardedPackageNos(Job job) throws Exception;
	String submitAwardApproval(String jobNumber, String subcontractNumber, String vendorNumber, String currencyCode, String userID) throws Exception;
	SystemConstant getSystemConstant(String systemCode, String company)throws Exception;
	Boolean inactivateSystemConstant(SystemConstant request, String user) throws DatabaseOperationException;
	List<SystemConstant> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review)  throws DatabaseOperationException;
	ArrayList<String> obtainSystemConstantSearchOption(String fieldName) throws DatabaseOperationException;
	Boolean updateMultipleSystemConstants(List<SystemConstant> requests, String user) throws DatabaseOperationException;
	Boolean createSystemConstant(SystemConstant request, String user) throws DatabaseOperationException;
	
	List<UDC> obtainWorkScopeList() throws DatabaseOperationException;
	List<UDC> obtainWorkScopeList(String searchStr) throws DatabaseOperationException; 
	UDC obtainWorkScope(String workScopeCode) throws DatabaseOperationException;
	
	String currencyCodeValidation(String currencyCode) throws Exception;
	
	String generateSCProvisionManually(String jobNumber, Date glDate,Boolean overrideOldPosting, String user);
	Boolean calculateTotalWDandCertAmount(String jobNumber, String packageNo, boolean recalculateRententionAmount);
	SCListPaginationWrapper obtainSubcontractListPaginationWrapper(SubcontractListWrapper searchWrapper) throws Exception;
	SCListPaginationWrapper obtainSubcontractListByPage(int pageNum) throws Exception;
	
	PaginationWrapper<SCDetails> getScDetailsByPage(String jobNumber, String packageNo, String billItem, 
			String description, String lineType, int pageNum) throws Exception;
	
	Boolean recalculateResourceSummaryIvFromSc(Job job, String packageNo);
	Boolean recalculateResourceSummaryIvFromSc(Job job) throws Exception;
	
	
	// last modified: brian tse
	// get the search result and populate
	PerformanceAppraisalPaginationWrapper getSearchAppraisalResult(String userName, String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status, int pageNumber);
	
	// last modified: brian tse
	// populate the performance appraisal by page
	PerformanceAppraisalPaginationWrapper getAppraisalResultByPage(String jobNumber, int pageNumber);
	
	// added by brian on 20110223
	// get the company base currency
	public String getCompanyBaseCurrency(String jobNumber);
	
	// added by brian on 20110322
	// get the currency code list from JDE web services
	public List<CurrencyCodeWrapper> getCurrencyCodeList(String currencyCode);
	
	// added by brian on 20110427
	// search the provision history
	// return the full list of search result
	public List<SCDetailProvisionHistoryWrapper> searchProvisionHistory(String jobNumber, String packageNo, String year, String month);
	
	// added by brian on 20110428
	// populate provision history by page
	public SCDetailProvisionHistoryPaginationWrapper populateGridByPage(int pageNumber);
	
	// added by brian on 20110429
	// delete the provision histories
	public Integer deleteProvisionHistories(String jobNumber, String packageNo, Integer postYr, Integer postMonth);
	
	/**
	 * For reset function
	 * @author peterchan
	 * @throws DatabaseOperationException 
	 */
	public Boolean updateSCStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException;
	public Boolean updateAddendumStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException;
	public Boolean updateSCSplitTerminateStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException;
	public Boolean updateSCFinalPaymentStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException;
	public String saveOrUpdateSCPackageAdmin(SCPackage scPackage) throws DatabaseOperationException;
	
	//duplicate all the records in QS_SC_PACKAGE to QS_SC_PACKAGE_SNAPSHOT on 26th of every month at 7:30am
	public Boolean generateSCPackageSnapshotManually() throws Exception;
	
	//Payment Requisition Revamp
	public String generateSCDetailsForPaymentRequisition(String jobNo, String subcontractNo, String vendorNo) throws Exception;
	
	public Boolean updateF58001FromSCPackageManually () throws Exception;
	public Boolean recalculateSCDetailPostedCertQty(String jobNumber, String packageNo) throws DatabaseOperationException;
	
	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeListPaginationWrapper(String jobNumber, String lineType, String BQItem, String description, String objectCode, String subsidiaryCode, String subcontractNumber, String subcontractorNumber) throws DatabaseOperationException;
	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeByPage(int pageNum) throws DatabaseOperationException;

	public String obtainSubcontractHoldMessage();
}
