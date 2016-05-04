package com.gammon.qs.client.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PackageRepositoryRemoteAsync {
	void getPackages(String jobNumber, AsyncCallback<List<SCPackage>> callback);
	void getSCDetailsOfPackageByPagination(String jobNumber, String packageNumber, String packageType, AsyncCallback<PaginationWrapper<SCDetailsWrapper>> callback);
	void getSCDetailListAtPage(int pageNum, AsyncCallback<PaginationWrapper<SCDetailsWrapper>> callback);
	void obtainSCDetails(String jobNumber, String packageNumber, String packageType, AsyncCallback<List<SCDetails>> callback);
	void obtainUneditableUnawardedPackageNos(Job job, AsyncCallback<List<String>> callback) ;
	void obtainUnawardedPackageNosUnderPaymentRequisition(Job job, AsyncCallback<List<String>> callback);
	
	void getNotAwardedPackage(String jobNumber, String packageNo, String packageType, AsyncCallback<SCPackage> callback);
	void updateWDandCertQuantity(List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList, AsyncCallback<String> callback);
	void addAddendumByWrapperStr(AddAddendumWrapper wrapper, AsyncCallback<String> callback);
	void addAddendumByWrapperListStr(List<AddAddendumWrapper> wrapperList, AsyncCallback<String> callback);

	void getAddendumEnquiry(String jobNumber, String packageNo, AsyncCallback<List<SCDetails>> callback);
	void getSCLine(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode, AsyncCallback<SCDetails> callback);
	void submitAddendumApproval(String jobNumber, Integer subcontractNumber, Double certAmount, String userID, AsyncCallback<AddendumApprovalResponseWrapper> callback);
	void nonAwardedSCApproval(String jobNumber, Integer subcontractNumber, Integer subcontractorNumber, Double certAmount, Double budgetAmount, String userID, AsyncCallback<NonAwardedSCApprovalResponseWrapper> callback);
	void updateAddendumByWrapper(UpdateAddendumWrapper wrapper, AsyncCallback<Boolean> callback);
		
	void defaultValuesForAddingSCDetailLines(String jobNumber, Integer subcontractNumber, String scLineType, AsyncCallback<SCDetails> callback);
	void getSCListView(String jobNumber, String packageNumber, String packageType, AsyncCallback<ScListView>callback);
	void suspendAddendum(String jobNumber,String packageNo, String sequenceNo, AsyncCallback<String>callback);
	void deleteAddendum(String jobNumber,String packageNo, String sequenceNo, AsyncCallback<String>callback);
	void retrieveNonAwardedSCPackageList(String jobNumber,AsyncCallback<List<ListNonAwardedSCPackageWrapper>> asyncCallback);
	void retrieveTenderAnalysisList(String trim, String trim2,AsyncCallback<List<TenderAnalysisWrapper>> asyncCallback);
	void submitSplitTerminateSC(String jobNumber, Integer subcontractNumber, String splitTerminate, String userID, AsyncCallback<String>callback);
	void triggerUpdateSCPaymentDetail(String jobNumber, String packageNo, String ifPayment, String createUser, String directPaymentIndicator, AsyncCallback<Boolean>callback);
	
	void updateNewQuantity(Long id, Double newQuantity, AsyncCallback<Boolean> callback);
	void updateSCDetailsNewQuantity(List<UpdateSCDetailNewQuantityWrapper> updateSCDetailNewQuantityWrappers, AsyncCallback<String> callback);
	void getPackagesFromList(Job job, List<String> packageNos, AsyncCallback<List<SCPackage>> callback);
	void getSCPackages(Job job, AsyncCallback<List<SCPackage>> callback);
	void getSCPackage(String jobNumber, String packageNo, AsyncCallback<SCPackage> callback);
	void obtainSCPackage(Job job, String packageNo, AsyncCallback<SCPackage> callback);
	void getSCPackageForPackagePanel(Job job, String packageNo, AsyncCallback<SCPackage> callback);
	void saveOrUpdateSCPackage(SCPackage scPackage, AsyncCallback<String> callback);
	void getUnawardedPackageNos(Job job, AsyncCallback<String[][]> callback);
	void getAwardedPackageStore(Job job, AsyncCallback<String[][]> callback);
	void getUneditablePackageNos(Job job, AsyncCallback<List<String>> callback);
	void getAwardedPackageNos(Job job, AsyncCallback<List<String>> callback);
	void submitAwardApproval(String jobNumber, String subcontractNumber, String vendorNumber, String currencyCode, String userID, AsyncCallback<String> callback);
	void getSystemConstant(String systemCode, String company, AsyncCallback<SystemConstant> callback);	
	void searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review, AsyncCallback<List<SystemConstant>> callback);
	void obtainSystemConstantSearchOption(String fieldName, AsyncCallback<ArrayList<String>> callback);
	void createSystemConstant(SystemConstant request, String user, AsyncCallback<Boolean> callback);
	void inactivateSystemConstant(SystemConstant request, String user, AsyncCallback<Boolean> callback);
	void updateMultipleSystemConstants(List<SystemConstant> requests, String user, AsyncCallback<Boolean> callback);
	void obtainWorkScopeList(AsyncCallback<List<UDC>> callback);
	void obtainWorkScopeList(String searchStr, AsyncCallback<List<UDC>>callback );
	void obtainWorkScope(String workScopeCode, AsyncCallback<UDC> callback);
	void currencyCodeValidation(String currencyCode ,AsyncCallback<String> callback ) throws Exception;
	void generateSCProvisionManually(String jobNumber, Date glDate,Boolean overrideOldPosting, String user,AsyncCallback<String> callback );
	void calculateTotalWDandCertAmount(String jobNumber, String packageNo, boolean recalculateRententionAmount, AsyncCallback<Boolean> callback);
	void obtainSubcontractListPaginationWrapper(SubcontractListWrapper searchWrapper,AsyncCallback<SCListPaginationWrapper>callback);
	void obtainSubcontractListByPage(int pageNum, AsyncCallback<SCListPaginationWrapper> asyncCallback);
	void getScDetailsByPage(String jobNumber, String packageNo, String billItem, String description, String lineType, int pageNum, AsyncCallback<PaginationWrapper<SCDetails>> callback);
	void recalculateResourceSummaryIvFromSc(Job job, String packageNo, AsyncCallback<Boolean> callback);
	void recalculateResourceSummaryIvFromSc(Job job, AsyncCallback<Boolean> callback);
	void getSearchAppraisalResult(String userName, String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status, int pageNumber, AsyncCallback<PerformanceAppraisalPaginationWrapper> callback);
	void getAppraisalResultByPage(String jobNumber, int pageNumber, AsyncCallback<PerformanceAppraisalPaginationWrapper> callback);
	void getCompanyBaseCurrency(String jobNumber, AsyncCallback<String> callback);
	void getCurrencyCodeList(String currencyCode, AsyncCallback<List<CurrencyCodeWrapper>> callback);
	/**
	 * modified by matthewlam
	 * Bug Fix #92: rearrange column names and order for SC Provision History Panel
	 */
	void searchProvisionHistory(String jobNumber, String packageNo, String year, String month, AsyncCallback<List<SCDetailProvisionHistoryWrapper>> callback);
	void populateGridByPage(int pageNumber, AsyncCallback<SCDetailProvisionHistoryPaginationWrapper> callback);
	void deleteProvisionHistories(String jobNumber, String packageNo, Integer postYr, Integer postMonth, AsyncCallback<Integer> callback);
	void updateSCStatus(String jobNumber, String packageNo, String newValue, AsyncCallback<Boolean> callback);
	void updateAddendumStatus(String jobNumber, String packageNo, String newValue, AsyncCallback<Boolean> callback);
	void updateSCSplitTerminateStatus(String jobNumber, String packageNo, String newValue, AsyncCallback<Boolean> callback);
	void updateSCFinalPaymentStatus(String jobNumber, String packageNo, String newValue, AsyncCallback<Boolean> callback);
	void saveOrUpdateSCPackageAdmin(SCPackage scPackage, AsyncCallback<String> asyncCallback);
	
	void generateSCPackageSnapshotManually(AsyncCallback<Boolean> asyncCallback);
	void generateSCDetailsForPaymentRequisition(String jobNo, String subcontractNo, String vendorNo, AsyncCallback<String> asyncCallback);
	void updateF58001FromSCPackageManually (AsyncCallback<Boolean> callback);
	
	void recalculateSCDetailPostedCertQty(String jobNumber, String packageNo, AsyncCallback<Boolean> asyncCallback);
	
	@SuppressWarnings("rawtypes")
	void obtainContraChargeListPaginationWrapper(String jobNumber, String lineType, String BQItem, String description, String objectCode, String subsidiaryCode, String subcontractNumber, String subcontractorNumber, AsyncCallback<ContraChargePaginationWrapper> asyncCallback);
	@SuppressWarnings("rawtypes")
	void  obtainContraChargeByPage(int pageNum, AsyncCallback<ContraChargePaginationWrapper> asyncCallback);
	
	public void obtainSubcontractHoldMessage(AsyncCallback<String> callback);
}