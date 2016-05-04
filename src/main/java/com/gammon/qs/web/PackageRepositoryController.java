package com.gammon.qs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.service.PackageService;
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

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class PackageRepositoryController extends GWTSpringController implements PackageRepositoryRemote {

	private static final long serialVersionUID = -6525205095758595907L;

	private Logger logger = Logger.getLogger(PackageRepositoryController.class.getName());
	@Autowired
	private PackageService packageRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<SCPackage> getPackages(String jobNumber) throws Exception {
		return packageRepository.getPackages(jobNumber);
	}

	/**
	 * @author tikywong
	 * modified on April 17, 2013
	 */
	public SCPackage getSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException{
		return packageRepository.obtainSCPackage(jobNumber, packageNo);
	}

	public SCPackage getNotAwardedPackage(String jobNumber, String packageNo, String packageType) throws Exception {
		SCPackage result = null;

		result = packageRepository.obtainNotAwardedPackage(jobNumber, packageNo, packageType);

		return result;
	}

	public PaginationWrapper<SCDetailsWrapper> getSCDetailsOfPackageByPagination(String jobNumber, String packageNumber, String packageType) throws Exception {
		return packageRepository.getSCDetailsOfPackage(jobNumber, packageNumber, packageType, false);
	}

	public PaginationWrapper<SCDetailsWrapper> getSCDetailListAtPage(int pageNum) throws Exception{
		return packageRepository.obtainSCDetailListAtPage(pageNum);

	}

	public List<SCDetails> obtainSCDetails(String jobNumber, String packageNumber, String packageType) throws DatabaseOperationException {
		return packageRepository.obtainSCDetails(jobNumber, packageNumber, packageType);
	}


	public String updateWDandCertQuantity(List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList){
		return packageRepository.updateWDandCertQuantity(updateSCPackageSaveWrapperList);	
	}

	public List<SCDetails> getAddendumEnquiry(String jobNumber, String packageNo) throws Exception {
		return packageRepository.getAddendumEnquiry(jobNumber, packageNo);
	}

	public String addAddendumByWrapperStr(AddAddendumWrapper wrapper) throws Exception{
		return packageRepository.addAddendumByWrapperStr(wrapper);
	}

	public SCDetails getSCLine(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode)  throws Exception{
		return packageRepository.getSCLine(jobNumber, subcontractNumber, sequenceNumber, billItem, resourceNumber, subsidiaryCode, objectCode);
	}

	public AddendumApprovalResponseWrapper submitAddendumApproval(String jobNumber, Integer subcontractNumber, Double certAmount, String userID) throws Exception{
		return packageRepository.submitAddendumApproval(jobNumber, subcontractNumber, certAmount, userID);
	}

	public Boolean updateAddendumByWrapper(UpdateAddendumWrapper wrapper) throws Exception{

		return packageRepository.updateAddendumByWrapper(wrapper);
	}

	public SCDetails defaultValuesForAddingSCDetailLines(String jobNumber, Integer subcontractNumber, String scLineType) throws Exception{
		return packageRepository.defaultValuesForAddingSCDetailLines(jobNumber, subcontractNumber, scLineType);
	}

	public ScListView getSCListView(String jobNumber, String packageNumber,
			String packageType) throws Exception {
		return packageRepository.getSCListView(jobNumber, packageNumber, packageType);
	}

	public String suspendAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception {
		return packageRepository.suspendAddendum(jobNumber, packageNo, sequenceNo);
	}

	public String deleteAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception {
		return packageRepository.deleteAddendum(jobNumber, packageNo, sequenceNo);
	}

	public NonAwardedSCApprovalResponseWrapper nonAwardedSCApproval(
			String jobNumber, Integer subcontractNumber,
			Integer subcontractorNumber, Double subcontractSumInDomCurr,
			Double comparableBudgetAmount, String username) {
		return packageRepository.nonAwardedSCApproval(
				jobNumber, subcontractNumber,
				subcontractorNumber, subcontractSumInDomCurr,
				comparableBudgetAmount, username);
	}

	public List<ListNonAwardedSCPackageWrapper> retrieveNonAwardedSCPackageList(
			String jobNumber) {
		return packageRepository.retrieveNonAwardedSCPackageList(jobNumber);
	}

	public List<TenderAnalysisWrapper> retrieveTenderAnalysisList(String jobNumber,
			String packageNumber) throws Exception {
		return packageRepository.retrieveTenderAnalysisList(jobNumber, packageNumber);
	}

	public String submitSplitTerminateSC(String jobNumber, Integer subcontractNumber,
			String splitTerminate, String userID) throws Exception {
		return packageRepository.submitSplitTerminateSC(jobNumber, subcontractNumber, splitTerminate, userID);
	}

	public Boolean triggerUpdateSCPaymentDetail(String jobNumber, String packageNo, String ifPayment, String createUser, String directPaymentIndicator) throws DatabaseOperationException {
		return packageRepository.triggerUpdateSCPaymentDetail(jobNumber, packageNo, ifPayment, createUser, directPaymentIndicator);
	}

	public List<SCPackage> getPackagesFromList(Job job, List<String> packageNos) throws Exception{
		return packageRepository.getPackagesFromList(job, packageNos);
	}

	public List<SCPackage> getSCPackages(Job job) throws Exception{
		return packageRepository.getSCPackages(job);
	}

	public SCPackage obtainSCPackage(Job job, String packageNo) throws Exception{
		return packageRepository.obtainSCPackage(job, packageNo);
	}

	public SCPackage getSCPackageForPackagePanel(Job job, String packageNo) throws Exception{
		return packageRepository.getSCPackageForPackagePanel(job, packageNo);
	}

	public String saveOrUpdateSCPackage(SCPackage scPackage) throws Exception{
		return packageRepository.saveOrUpdateSCPackage(scPackage);
	}

	public String[][] getUnawardedPackageNos(Job job) throws Exception{
		return packageRepository.getUnawardedPackageNos(job);
	}

	public String[][] getAwardedPackageStore(Job job) throws Exception {
		return packageRepository.getAwardedPackageStore(job);
	}

	public List<String> getUneditablePackageNos(Job job) throws Exception{
		return packageRepository.getUneditablePackageNos(job);
	}

	public List<String> getAwardedPackageNos(Job job) throws Exception{
		return packageRepository.getAwardedPackageNos(job);
	}

	public Boolean updateNewQuantity(Long id, Double newQuantity) throws Exception {
		return packageRepository.updateNewQuantity(id, newQuantity);
	}
	public String submitAwardApproval(String jobNumber, String subcontractNumber, String vendorNumber, String currencyCode, String userID) throws Exception{
		return packageRepository.submitAwardApproval(jobNumber, subcontractNumber, vendorNumber,currencyCode, userID);
	}
	public SystemConstant getSystemConstant(String systemCode, String company)throws Exception{
		return packageRepository.obtainSystemConstant(systemCode, company);
	}

	public List<SystemConstant> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review)  throws DatabaseOperationException {
		return packageRepository.searchSystemConstants(systemCode, company, scPaymentTerm, scMaxRetentionPercent, scInterimRetentionPercent, scMOSRetentionPercent, retentionType, finQS0Review);
	}
	
	@Override
	public ArrayList<String> obtainSystemConstantSearchOption(
			String fieldName) throws DatabaseOperationException {
		return packageRepository.obtainSystemConstantSearchOption(fieldName);
	}

	public Boolean updateMultipleSystemConstants(List<SystemConstant> requests, String user) throws DatabaseOperationException {
		return packageRepository.updateMultipleSystemConstants(requests, user);
	}

	public Boolean createSystemConstant(SystemConstant request, String user) throws DatabaseOperationException {
		return packageRepository.createSystemConstant(request, user);
	}

	public UDC obtainWorkScope(String userDefinedCode) throws DatabaseOperationException {
		return packageRepository.obtainWorkScope(userDefinedCode);
	}

	public List<UDC> obtainWorkScopeList() throws DatabaseOperationException {
		return packageRepository.obtainWorkScopeList();
	}

	public String currencyCodeValidation(String currencyCode) throws Exception {
		return packageRepository.currencyCodeValidation(currencyCode);
	}
	
	/**
	 * To run Provision Posting manually
	 *
	 * @param jobNumber
	 * @param glDate
	 * @param overrideOldPosting
	 * @param username
	 * @return
	 * @author	tikywong
	 * @since	Mar 24, 2016 3:08:27 PM
	 */
	public String generateSCProvisionManually(String jobNumber, Date glDate, Boolean overrideOldPosting, String username) {
		try {
			packageRepository.runProvisionPostingManually(jobNumber, glDate, overrideOldPosting, username);
		} catch (Exception e) {
			return e.getMessage();
		}

		return "Manual Subcontract Provision has been posted successfully.";
	}

	public Boolean calculateTotalWDandCertAmount(String jobNumber, String packageNo, boolean recalculateRententionAmount){
		return packageRepository.calculateTotalWDandCertAmount(jobNumber, packageNo, recalculateRententionAmount);
	}

	public String addAddendumByWrapperListStr(
			List<AddAddendumWrapper> wrapperList) throws Exception {
		return  packageRepository.addAddendumByWrapperListStr(wrapperList);
	}

	public SCListPaginationWrapper obtainSubcontractListPaginationWrapper(
			SubcontractListWrapper searchWrapper) throws Exception {
		return packageRepository.obtainSubcontractListPaginationWrapper(searchWrapper);
	}

	public List<UDC> obtainWorkScopeList(String searchStr) throws DatabaseOperationException {
		return packageRepository.obtainWorkScopeList(searchStr);
	}

	public SCListPaginationWrapper obtainSubcontractListByPage(int pageNum) throws Exception{
		return packageRepository.obtainSubcontractListByPage(pageNum);
	}

	public PaginationWrapper<SCDetails> getScDetailsByPage(String jobNumber,
			String packageNo, String billItem, String description,
			String lineType, int pageNum) throws Exception {
		return packageRepository.getScDetailsByPage(jobNumber, packageNo, billItem, description, lineType, pageNum);
	}

	public Boolean recalculateResourceSummaryIvFromSc(Job job, String packageNo) {
		return packageRepository.recalculateResourceSummaryIV(job, packageNo, false);
	}

	public Boolean recalculateResourceSummaryIvFromSc(Job job)
	throws Exception {
		return packageRepository.recalculateResourceSummaryIVbyJob(job);
	}

	public PerformanceAppraisalPaginationWrapper getSearchAppraisalResult(
			String userName, String jobNumber, Integer sequenceNumber, String appraisalType,
			String groupCode, Integer vendorNumber, Integer subcontractNumber,
			String status, int pageNumber) {
		return packageRepository.getSearchAppraisalResult(userName, jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status, pageNumber);
	}

	public PerformanceAppraisalPaginationWrapper getAppraisalResultByPage(
			String jobNumber, int pageNumber) {

		logger.info("[PackageRepositoryController][getAppraisalResultByPage] pagination by page");
		return packageRepository.getAppraisalResultByPage(jobNumber, pageNumber);
	}

	// added by brian on 20110223
	// get the copmpany base currency
	public String getCompanyBaseCurrency(String jobNumber) {
		return packageRepository.getCompanyBaseCurrency(jobNumber);
	}

	// added by brian on 20110421
	// get the currency code list
	public List<CurrencyCodeWrapper> getCurrencyCodeList(String currencyCode) {
		return packageRepository.getCurrencyCodeList(currencyCode);
	}

	// added by brian on 20110427
	public List<SCDetailProvisionHistoryWrapper> searchProvisionHistory(String jobNumber, String packageNo, String year, String month) {
		return packageRepository.searchProvisionHistory(jobNumber, packageNo, year, month);
	}

	// added by brian on 20110428
	public SCDetailProvisionHistoryPaginationWrapper populateGridByPage(int pageNumber) {
		return packageRepository.populateGridByPage(pageNumber);
	}

	// added by brian on 20110429
	public Integer deleteProvisionHistories(String jobNumber, String packageNo,
			Integer postYr, Integer postMonth) {
		try {
			return packageRepository.deleteProvisionHistories(jobNumber, packageNo, postYr, postMonth);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return Integer.valueOf(0);
		}
	}

	public Boolean updateSCStatus(String jobNumber, String packageNo,
			String newValue) throws DatabaseOperationException {
		return packageRepository.updateSCStatus(jobNumber, packageNo, newValue);
	}

	public Boolean updateAddendumStatus(String jobNumber, String packageNo,
			String newValue) throws DatabaseOperationException {
		return packageRepository.updateAddendumStatus(jobNumber, packageNo, newValue);
	}

	public Boolean updateSCSplitTerminateStatus(String jobNumber,
			String packageNo, String newValue) throws DatabaseOperationException {
		return packageRepository.updateSCSplitTerminateStatus(jobNumber,packageNo,newValue);
	}

	public Boolean updateSCFinalPaymentStatus(String jobNumber,
			String packageNo, String newValue) throws DatabaseOperationException {
		return packageRepository.updateSCFinalPaymentStatus(jobNumber,packageNo,newValue);
	}

	public String saveOrUpdateSCPackageAdmin(SCPackage scPackage) throws DatabaseOperationException {
		return packageRepository.saveOrUpdateSCPackageAdmin(scPackage);
	}

	public String updateSCDetailsNewQuantity(List<UpdateSCDetailNewQuantityWrapper> updateSCDetailNewQuantityWrappers) throws DatabaseOperationException {
		return packageRepository.updateSCDetailsNewQuantity(updateSCDetailNewQuantityWrappers);
	}

	public Boolean generateSCPackageSnapshotManually() throws Exception {
		return packageRepository.generateSCPackageSnapshotManually();
	}

	public String generateSCDetailsForPaymentRequisition(String jobNo, String subcontractNo, String vendorNo) throws Exception {
		return packageRepository.generateSCDetailsForPaymentRequisition(jobNo, subcontractNo, vendorNo);
	}

	public List<String> obtainUneditableUnawardedPackageNos(Job job) throws Exception{
		return packageRepository.obtainUneditableUnawardedPackageNos(job);
	}

	public List<String> obtainUnawardedPackageNosUnderPaymentRequisition(Job job) throws Exception {
		return packageRepository.obtainUnawardedPackageNosUnderPaymentRequisition(job);
	}

	public Boolean inactivateSystemConstant(SystemConstant request, String user) throws DatabaseOperationException {
		return packageRepository.inactivateSystemConstant(request, user);
	}

	public Boolean updateF58001FromSCPackageManually() throws Exception {
		return packageRepository.updateF58001FromSCPackageManually();
	} 

	
	public Boolean recalculateSCDetailPostedCertQty(String jobNumber, String packageNo) throws DatabaseOperationException {
		return packageRepository.recalculateSCDetailPostedCertQty(jobNumber, packageNo);
	}

	@Override
	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeListPaginationWrapper(String jobNumber, String lineType, String BQItem, String description, String objectCode, String subsidiaryCode, String subcontractNumber, String subcontractorNumber) throws DatabaseOperationException{
		return packageRepository.obtainContraChargeListPaginationWrapper(jobNumber, lineType, BQItem, description, objectCode, subsidiaryCode, subcontractNumber, subcontractorNumber);
	}
	
	@Override
	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeByPage(int pageNum) throws DatabaseOperationException{
		return packageRepository.obtainContraChargeByPage(pageNum);
	}

	public String obtainSubcontractHoldMessage() {
		return packageRepository.obtainSubcontractHoldMessage();
	}
}
