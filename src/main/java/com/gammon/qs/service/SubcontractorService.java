package com.gammon.qs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterResponseObj;
import com.gammon.pcms.dao.adl.SubcontractorWorkscopeDao;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.pcms.model.adl.SubcontractorWorkscope;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.SupplierMasterWSDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;

/**
 * @author koeyyeung
 * created on 23 Jul, 2013
 */
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class SubcontractorService{
	private Logger logger = Logger.getLogger(SubcontractorService.class.getName());
	
	//Security Service
	@Autowired
	private SecurityService securityServiceImpl;
	//Matser List
	@Autowired
	private MasterListService masterListRepository;
	@Autowired
	private MasterListWSDao masterListDao;
	//Supplier Master
	@Autowired
	private SupplierMasterWSDao supplierMasterDao;
	//Tender Analysis
	@Autowired
	private TenderHBDao tenderAnalysisDao;
	//SC Package
	@Autowired
	private SubcontractHBDao scPackageDao;
	@Autowired
	private SubcontractorWorkscopeDao subcontractorWorkscopeDao;
		
	/*************************************** FUNCTIONS FOR PCMS       **************************************************************/
	public List<SubcontractorWrapper> obtainSubcontractorWrappers(String workScope, String subcontractor) throws DatabaseOperationException {
		logger.info("workscope: "+workScope+" - subcontractor: "+subcontractor);
		List<SubcontractorWrapper> subcontractorWrapperList = new ArrayList<SubcontractorWrapper>();
		
		/**
		 * Flow of search: Subcontractor-->WorkScope
		 **/
		if(subcontractor!=null && !"".equals(subcontractor)){
			logger.info("Method 1: Search by Subcontractor");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainAllVendorList(subcontractor);
				for(MasterListVendor masterListVendor: masterListVendorList){
					if(masterListVendor!=null){
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setVendorType(masterListVendor.getVendorType());
						subcontractorWrapper.setVendorStatus(masterListVendor.getVendorStatus());
						subcontractorWrapper.setSubcontractorApproval(masterListVendor.getApprovalStatus());
						subcontractorWrapper.setBusinessRegistrationNo(masterListVendor.getVendorRegistrationNo());
						subcontractorWrapper.setScFinancialAlert(masterListVendor.getScFinancialAlert());
						
						if(masterListVendor.getVendorNo()!=null &&!"".equals(masterListVendor.getVendorNo())){
							SupplierMasterResponseObj supplier = supplierMasterDao.obtainSupplierMaster(Integer.valueOf(masterListVendor.getVendorNo()));
							if(supplier==null)
								subcontractorWrapper.setHoldPayment("N/A");
							else
								subcontractorWrapper.setHoldPayment(supplier.getHoldPaymentCode());
						}
						
						if(workScope!=null && !"".equals(workScope.trim())){
							List<WorkScopeWrapper> workScopeWrapperList = masterListRepository.getSubcontractorWorkScope(masterListVendor.getVendorNo());
							for(WorkScopeWrapper workScopeWrapper: workScopeWrapperList){
								if(workScopeWrapper!=null && workScopeWrapper.getWorkScopeCode()!=null){
									if(workScope.trim().equals(workScopeWrapper.getWorkScopeCode().trim())){
										subcontractorWrapperList.add(subcontractorWrapper);
										break;
									}
								}
							}
						}
						else
							subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(workScope!=null && !"".equals(workScope)){
			logger.info("Method 2: Search by WorkScope");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainVendorListByWorkScopeWithUser(workScope, securityServiceImpl.getCurrentUser().getUsername());
				for(MasterListVendor masterListVendor :masterListVendorList){
					if(masterListVendor!=null){
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setVendorType(masterListVendor.getVendorType());
						subcontractorWrapper.setVendorStatus(masterListVendor.getVendorStatus());
						subcontractorWrapper.setSubcontractorApproval(masterListVendor.getApprovalStatus());
						subcontractorWrapper.setScFinancialAlert(masterListVendor.getScFinancialAlert());
						
						MasterListVendor vendor = masterListDao.obtainVendor(masterListVendor.getVendorNo());
						if(vendor!=null)
							subcontractorWrapper.setBusinessRegistrationNo(vendor.getVendorRegistrationNo());
						
						if(masterListVendor.getVendorNo()!=null &&!"".equals(masterListVendor.getVendorNo())){
							SupplierMasterResponseObj supplier = supplierMasterDao.obtainSupplierMaster(Integer.valueOf(masterListVendor.getVendorNo()));
							subcontractorWrapper.setHoldPayment(supplier.getHoldPaymentCode());
						}

						if(subcontractor!=null && !"".equals(subcontractor)){
							if((masterListVendor.getVendorNo().equals(subcontractor)) || masterListVendor.getVendorName().equals(subcontractor))
								subcontractorWrapperList.add(subcontractorWrapper);
						}
						else
							subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return subcontractorWrapperList;
	}
	
	public List<SubcontractorWrapper> obtainClientWrappers(String client) throws DatabaseOperationException {
		logger.info("Client: " + client);
		List<SubcontractorWrapper> subcontractorWrapperList = new ArrayList<SubcontractorWrapper>();

		if (client != null && !"".equals(client)) {
			logger.info("Method 3: Search by Client");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainAllClientList(client);
				for (MasterListVendor masterListVendor : masterListVendorList) {
					if (masterListVendor != null) {
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setBusinessRegistrationNo(masterListVendor.getVendorRegistrationNo());

						subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return subcontractorWrapperList;
	}

	public List<String> obtainSubconctractorStatistics(String subcontractorNo) throws DatabaseOperationException {
		logger.info("obtainSubconctractorStatistics - STARTED");
		List<String> resultList = new ArrayList<String>();
		BigDecimal revisedSCSum = new BigDecimal(0.0);
		BigDecimal totalCumWorkDoneAmount = new BigDecimal(0.0);
		BigDecimal balanceToComplete = new BigDecimal(0.0);
		
		Integer noOfQuotReturned =0;
		Integer noOfAward=0;
		
		String year = String.valueOf(Integer.valueOf(DateHelper.formatDate(new Date(), "yyyy")) - 1); 
		Date startDate = DateHelper.parseDate("0101"+year, "ddMMyyyy");
		
		if(subcontractorNo!=null&&!"".equals(subcontractorNo)){
			noOfQuotReturned = tenderAnalysisDao.obtainNoOfQuotReturned(Integer.valueOf(subcontractorNo));
			noOfAward = tenderAnalysisDao.obtainNoOfAward(Integer.valueOf(subcontractorNo));
		}
		logger.info("date: "+startDate);
		Subcontract scPackage = scPackageDao.obtainPackageStatistics(subcontractorNo, startDate);
		revisedSCSum = (scPackage.getRemeasuredSubcontractSum()==null? new BigDecimal(0.0) :scPackage.getRemeasuredSubcontractSum()).add((scPackage.getApprovedVOAmount()==null? new BigDecimal(0.0):scPackage.getApprovedVOAmount()));
		totalCumWorkDoneAmount = scPackage.getTotalCumWorkDoneAmount()==null? new BigDecimal(0.0):scPackage.getTotalCumWorkDoneAmount();
		balanceToComplete = revisedSCSum.subtract(totalCumWorkDoneAmount);
		
		resultList.add(noOfQuotReturned.toString());
		resultList.add(noOfAward.toString());
		resultList.add(String.valueOf(revisedSCSum));
		resultList.add(String.valueOf(balanceToComplete));
		logger.info("noOfQuotReturned: "+resultList.get(0)+" - noOfAward: "+resultList.get(1)+" - revisedSCSum: "+resultList.get(2)+" - balanceToComplete: "+resultList.get(3));
		logger.info("obtainSubconctractorStatistics - END");
		return resultList;
	}

	public List<Subcontract> obtainPackagesByVendorNo(String vendorNo) throws DatabaseOperationException {
		return scPackageDao.obtainPackagesByVendorNo(vendorNo);
	}

	public List<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByVendorNo(String vendorNo) throws DatabaseOperationException {
		List<SubcontractorTenderAnalysisWrapper> tenderAnalysisWrapperList = new ArrayList<SubcontractorTenderAnalysisWrapper>();
		logger.info("vendorNo : " + vendorNo  );

		if(vendorNo!=null && vendorNo.length()>1){
			vendorNo = vendorNo.trim();
			List<Tender> tenderAnalysisList = tenderAnalysisDao.obtainTenderAnalysisByVendorNo(Integer.valueOf(vendorNo));
			tenderAnalysisWrapperList = obtainSubcontractorTenderAnalysisWrapper(tenderAnalysisList);
		}
		return tenderAnalysisWrapperList;
	}
	
	private List<SubcontractorTenderAnalysisWrapper> obtainSubcontractorTenderAnalysisWrapper(List<Tender> tenderList) throws DatabaseOperationException{
		List<SubcontractorTenderAnalysisWrapper> tenderAnalysisWrapperList = new ArrayList<SubcontractorTenderAnalysisWrapper>();
		for (Tender tenderAnalysis: tenderList){
			SubcontractorTenderAnalysisWrapper tenderAnalysisWrapper = new SubcontractorTenderAnalysisWrapper();
			tenderAnalysisWrapper.setJobNo(tenderAnalysis.getJobNo());
			tenderAnalysisWrapper.setPackageNo(tenderAnalysis.getPackageNo());
			tenderAnalysisWrapper.setQuotedAmount(tenderAnalysis.getBudgetAmount());
			tenderAnalysisWrapper.setStatus(tenderAnalysis.getStatus());
			tenderAnalysisWrapper.setDivision(tenderAnalysis.getSubcontract().getJobInfo().getDivision());

			Tender tender = tenderAnalysisDao.obtainTender(tenderAnalysis.getJobNo(), tenderAnalysis.getPackageNo(),0);
			tenderAnalysisWrapper.setBudgetAmount((tender==null || tender.getBudgetAmount()==null)?0.0:tender.getBudgetAmount());

			tenderAnalysisWrapperList.add(tenderAnalysisWrapper);
		}
		return tenderAnalysisWrapperList;
	}
	
	public List<SubcontractorWorkscope> obtainWorkscopeByVendorNo(String vendorNo){
		return subcontractorWorkscopeDao.obtainWorkscopeByVendorNo(vendorNo);
	}
	/*************************************** FUNCTIONS FOR PCMS - END **************************************************************/
}
