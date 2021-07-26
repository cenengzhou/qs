package com.gammon.qs.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.dto.rs.provider.response.ta.TenderComparisonDTO;
import com.gammon.pcms.dto.rs.provider.response.ta.TenderDetailDTO;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.BpiItemHBDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.shared.util.CalculationUtil;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class TenderService implements Serializable {
	private static final long serialVersionUID = -3750073286095069814L;
	@Autowired
	private  TenderHBDao tenderDao;
	@Autowired
	private  TenderDetailHBDao tenderDetailDao;
	@Autowired
	private  TenderVarianceHBDao tenderVarianceHBDao;
	@Autowired
	private  SubcontractHBDao subcontractDao;
	@Autowired
	private  BpiItemHBDao bpiItemDao;
	@Autowired
	private  JobInfoHBDao jobInfoDao;
	@Autowired
	private  ResourceSummaryHBDao resourceSummaryDao;
	@Autowired
	private  MasterListService masterListService;
	@Autowired
	private  AccountCodeWSDao accountCodeDao;
	@Autowired
	private  SubcontractService subcontractService;
	@Autowired
	private  PaymentCertHBDao paymentCertDao;
	@Autowired
	private  PaymentCertDetailHBDao paymentCertDetailDao;
	@Autowired
	private  SubcontractDetailHBDao subcontractDetailDao;
	@Autowired
	private AttachmentService attachmentService;
	
	private  Logger logger = Logger.getLogger(TenderService.class.getName());
	
	public List<String> getUneditableTADetailIDs(String jobNo, String subcontractNo, Integer vendorNo){
		List<String> uneditableTADetailIDs = new ArrayList<String>();
		
		try {
			Tender tenderAnalysis = tenderDao.obtainTender(jobNo, subcontractNo, vendorNo);
			
			if(tenderAnalysis!=null && Tender.TA_STATUS_RCM.equals(tenderAnalysis.getStatus())){
				List<SubcontractDetail> scDetailList = subcontractDetailDao.obtainSCDetails(jobNo, subcontractNo);

				if(scDetailList!=null && scDetailList.size()>0){
					List<TenderDetail> taDetailList = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
					for(TenderDetail taDetail: taDetailList){
						if(taDetail!=null && taDetail.getId()!=null){
							SubcontractDetail scDetails = subcontractDetailDao.obtainSCDetailsByTADetailID(jobNo, subcontractNo, taDetail.getId());
							if(scDetails!=null && (scDetails.getAmountPostedCert().compareTo(new BigDecimal(0)) > 0 || scDetails.getAmountPostedWD().compareTo(new BigDecimal(0)) > 0 || scDetails.getAmountCumulativeWD().compareTo(new BigDecimal(0)) > 0)){
								uneditableTADetailIDs.add(String.valueOf(taDetail.getId()));
							}
						}
					}
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		
		return uneditableTADetailIDs;
	}
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public List<TenderDetail> obtainTenderDetailList(String jobNo, String packageNo, Integer vendorNo) throws Exception{
		return tenderDetailDao.getTenderAnalysisDetails(jobNo, packageNo, vendorNo);
	}

	public List<Tender> obtainTenderList(String jobNumber, String subcontractNo) throws Exception{
		List<Tender> tenderList= tenderDao.obtainTenderList(jobNumber, subcontractNo);
		return tenderList;
	} 

	public Tender obtainTender(String jobNo, String subcontractNo, Integer vendorNo) throws Exception{
		Tender tender = tenderDao.obtainTender(jobNo, subcontractNo, vendorNo);
		return tender;
	}
	
	public Tender obtainRecommendedTender(String jobNo, String subcontractNo) throws Exception{
		Tender tender = null;
		try {
			tender = tenderDao.obtainRecommendedTender(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tender;
	}

	
	/**
	 * @author koeyyeung
	 * created on 04 Jul, 2016
	 * @param subcontractorName **/
	public String createTender(String jobNo, String subcontractNo, Integer subcontractorNo, String subcontractorName) throws Exception{
		logger.info("jobNo: "+jobNo+" - subcontractNo: "+subcontractNo+" - subcontractorNo: "+subcontractorNo+" - subcontractorName: "+subcontractorName);
		String result = "";
		try {
			if(subcontractorNo != null){
				Tender tenderInDB = this.obtainTender(jobNo, subcontractNo, subcontractorNo);
				if(tenderInDB != null){
					result = "Subcontractor already exists.";
					return result;
				}
			}
			
			JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
			
			Tender tender = new Tender();
			tender.setSubcontract(subcontractDao.obtainSCPackage(jobNo, subcontractNo));
			
			tender.setJobNo(jobNo);
			tender.setPackageNo(subcontractNo);
			tender.setCurrencyCode("HKD");
			tender.setExchangeRate(Double.valueOf(1));
			//if("1".equals(job.getRepackagingType()))
			tender.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, subcontractNo));
			/*else
				tender.setBudgetAmount(bpiItemResourceDao.getBudgetForPackage(jobNo, subcontractNo));*/

			
			if(subcontractorNo != null){
				MasterListVendor masterListVendor = masterListService.searchVendorAddressDetails(String.valueOf(subcontractorNo));
				tender.setVendorNo(subcontractorNo);
				tender.setNameSubcontractor(masterListVendor!=null? masterListVendor.getVendorName():"");
			}else{
				List<Tender> tenderList = tenderDao.obtainTenderList(jobNo, subcontractNo);
				subcontractorNo = -1;
				if(tenderList !=null && tenderList.size()>0 && tenderList.get(0).getVendorNo() < 0)
					subcontractorNo = tenderList.get(0).getVendorNo() -1;
				tender.setVendorNo(subcontractorNo);
				tender.setNameSubcontractor(subcontractorName);
			}
			
			
			tenderDao.insert(tender);

			result = createTenderDetails(jobNo, subcontractNo, subcontractorNo);
			if(result.length()==0)
				result = subcontractorNo.toString();
		} catch (Exception e) {
			result = "Subcontractor cannot be created.";
			e.printStackTrace();
		}
		return result;
	}
	
	private String createTenderDetails(String jobNo, String subcontractNo, Integer subcontractorNo) throws Exception{
		logger.info("jobNo: "+jobNo+" - subcontractNo: "+subcontractNo+" - subcontractorNo: "+subcontractorNo);
		String error = "";
		try {
			Tender tenderInDB = tenderDao.obtainTender(jobNo, subcontractNo, subcontractorNo);
			List<TenderDetail> baseDetails = tenderDetailDao.getTenderAnalysisDetails(jobNo, subcontractNo, 0);

			for(TenderDetail detail : baseDetails){
				TenderDetail newDetail = new TenderDetail();
				newDetail.setTender(tenderInDB);
				newDetail.setSequenceNo(detail.getSequenceNo());
				newDetail.setAmountBudget(detail.getAmountBudget());
				newDetail.setResourceNo(detail.getResourceNo());
				newDetail.setBillItem(detail.getBillItem());
				newDetail.setDescription(detail.getDescription());
				newDetail.setQuantity(detail.getQuantity());
				newDetail.setRateBudget(detail.getRateBudget());
				newDetail.setRateSubcontract(detail.getRateBudget());
				newDetail.setAmountSubcontract(detail.getAmountBudget());
				newDetail.setAmountForeign(detail.getAmountBudget());
				newDetail.setObjectCode(detail.getObjectCode());
				newDetail.setSubsidiaryCode(detail.getSubsidiaryCode());
				newDetail.setLineType(detail.getLineType());
				newDetail.setUnit(detail.getUnit());
				newDetail.setRemark(detail.getRemark());
				newDetail.setResourceNo(detail.getResourceNo());
				
				tenderDetailDao.insert(newDetail);
			}
		} catch (Exception e) {
			error = "Subcontractor cannot be created.";
			e.printStackTrace();
		}
		return error;
	}

	public String updateTenderAdmin(Tender tender) {
		Tender tenderInDB = tenderDao.get(tender.getId());
		if(tenderInDB == null)
			return "Tender not found in database";
		tenderDao.merge(tender);
		return null;
	}

	public void updateTenderDetailAdmin(TenderDetail tenderDetail) throws Exception {
		tenderDetailDao.saveOrUpdate(tenderDetail);
	}
		
	public String updateTenderAnalysisDetails(String jobNo, String packageNo,
			Integer vendorNo, String currencyCode, Double exchangeRate, String remarks, String statusChangeExecutionOfSC,
			List<TenderDetail> taDetails, boolean validate)
			throws Exception {
		
		JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
		
		for(TenderDetail taDetail : taDetails){
			//base details bpi must be validated
			if(validate && taDetail.getBillItem() != null && taDetail.getBillItem().length() > 0){
				String[] bpi = taDetail.getBillItem().split("/");
				if(bpi.length != 5)
					return "Invalid B/P/I code: " + taDetail.getBillItem();
				BpiItem bqItem = bpiItemDao.getBpiItemByRef(job.getJobNumber(), bpi[0], bpi[1].length() == 0 ? null : bpi[1], bpi[2].length() == 0 ? null : bpi[2], bpi[3].length() == 0 ? null : bpi[3], bpi[4]);
				if(bqItem == null)
					return "B/P/I not found: " + taDetail.getBillItem();
			}
			if(taDetail.getResourceNo() == null || taDetail.getResourceNo().equals(Integer.valueOf(0))){
				String error = masterListService.validateAndCreateAccountCode(job.getJobNumber(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode());
				if(error != null)
					return error;
			}
		}
		
		//Save
		Subcontract scPackage = subcontractDao.obtainPackage(job, packageNo);
		if(scPackage == null)
			return "Invalid package number";
		else if(scPackage.isAwarded() || (scPackage.getSubcontractStatus()!=null && scPackage.getSubcontractStatus()==330))
			return "Package has been awarded or submitted.";
		
		
		PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
		if(latestPaymentCert!=null && (latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
				|| latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
				|| latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))){
					
			return "Payment Requisition Submitted. Tender Information will be frozen.";
		}
	
		Tender tenderAnalysis = this.obtainTender(jobNo, packageNo, vendorNo);
		String companyCurrencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		if(currencyCode == null || vendorNo.equals(Integer.valueOf(0)))
			currencyCode = companyCurrencyCode;
		

		if(tenderAnalysis!=null){
			if(vendorNo!=0){
				List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
				if(scPaymentCertList!=null && scPaymentCertList.size()>0){
					if(!tenderAnalysis.getCurrencyCode().equals(currencyCode)){
						return "Currency Code must be the same as the one in previous payment.";
					}
					if(!tenderAnalysis.getExchangeRate().equals(exchangeRate)){
						return "Exchange rate must be the same as the one in previous payment.";
					}
				}
				tenderAnalysis.setRemarks(remarks);
				tenderAnalysis.setStatusChangeExecutionOfSC(statusChangeExecutionOfSC);
			}
			tenderAnalysis.setCurrencyCode(currencyCode);
			
			if(tenderAnalysis.getCurrencyCode().equals(companyCurrencyCode)){
				tenderAnalysis.setExchangeRate(1.0);
			}else
				tenderAnalysis.setExchangeRate(exchangeRate);				

		}
		else{
			tenderAnalysis = new Tender();
			tenderAnalysis.setJobNo(jobNo);
			tenderAnalysis.setPackageNo(packageNo);
			tenderAnalysis.setVendorNo(vendorNo);
			tenderAnalysis.setSubcontract(scPackage);
			tenderAnalysis.setCurrencyCode(currencyCode);
			tenderAnalysis.setExchangeRate(1.0);
			tenderAnalysis.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
		}

		String error = updateTenderDetails(job, scPackage, vendorNo, tenderAnalysis, taDetails, companyCurrencyCode);
		if(error!=null)
			return error;

		
		
		scPackage.setSubcontractStatus(Integer.valueOf(160));
		subcontractDao.saveOrUpdate(scPackage);
		return null;
	}
	
	
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * **/
	private String updateTenderDetails(JobInfo job, Subcontract subcontract, Integer vendorNo, Tender tender, List<TenderDetail> toBeUpdatedTaDetails, String companyCurrencyCode) throws Exception{
		if(tender.getId()!=null){
			//Update Vendor TA
			if(tender.getVendorNo()!=0){
				//Payment Requisition exists
				List<SubcontractDetail> scDetailsList = subcontractDetailDao.getSCDetails(subcontract);
				if(scDetailsList!=null && scDetailsList.size()>0){
					logger.info("Update Vendor : Payment Requisition exists");
					for(TenderDetail taDetail: toBeUpdatedTaDetails){
						TenderDetail taDetailInDB = tenderDetailDao.obtainTADetailByResourceNo(tender, taDetail.getResourceNo());
						
						if(taDetailInDB !=null){
							taDetailInDB.setRateBudget(taDetail.getRateBudget());
							taDetailInDB.setRateSubcontract(taDetail.getRateSubcontract());
							taDetailInDB.setAmountSubcontract(taDetail.getAmountSubcontract());
							
							if(tender.getCurrencyCode().equals(companyCurrencyCode))
								taDetailInDB.setAmountForeign(taDetail.getAmountSubcontract());
							else
								taDetailInDB.setAmountForeign(CalculationUtil.round(taDetail.getAmountSubcontract()*tender.getExchangeRate(), 2));
							
							tenderDetailDao.update(taDetailInDB);
						}
						
					}
					//Update TA 
					tender.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, subcontract.getPackageNo()));
					tender.setAmtBuyingGainLoss(recalculateBuyingGainLoss(tender, toBeUpdatedTaDetails, companyCurrencyCode));
					
					if(Tender.TA_STATUS_RCM.equals(tender.getStatus())){
						//Recalculate SCPackage Subcon Sum
						subcontractService.recalculateSCPackageSubcontractSum(subcontract, toBeUpdatedTaDetails);
						
						//Delete Pending Payments
						deletePendingPaymentRequisition(paymentCertDao.obtainPaymentLatestCert(job.getJobNumber(), subcontract.getPackageNo()), scDetailsList);
					}
					subcontractDao.update(subcontract);
					
					tenderDao.update(tender);
					
				}else{
					logger.info("Update Vendor");
					this.updateTenderAndDetails(tender, toBeUpdatedTaDetails, subcontract, companyCurrencyCode);
				}
			}else{
				try {
					logger.info("Update Budget Tender Analysis");
					//Edit Budget Tender Analysis
					List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
					PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(job.getJobNumber(), subcontract.getPackageNo());

					if((scPaymentCertList!=null && scPaymentCertList.size() > 1)|| (latestPaymentCert!=null && PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(latestPaymentCert.getPaymentStatus()))){
						if(subcontract.getTotalPostedCertifiedAmount().compareTo(new BigDecimal(0))	!=0){
							logger.info("Update Budget Tender Analysis - Payment Requisition");
							//if total posted cert amount !=0 --> TA Detail must be identical to BQ Resource Summary for Repackaging 1
							for (TenderDetail taDetail: toBeUpdatedTaDetails){
								ResourceSummary resourceSummary = resourceSummaryDao.getResourceSummary(subcontract.getJobInfo(), subcontract.getPackageNo(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode(), taDetail.getDescription(), taDetail.getUnit(), taDetail.getRateBudget(), taDetail.getQuantity());
								if(resourceSummary==null)
									return "Tender Analysis should be identical to Resource Summaries in Repackaging for Payment Requisition.";
							}
							
							//Insert budget TA
							List<Tender> tenderAnalysisList = tenderDao.obtainTenderAnalysisList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
							for(Tender ta: tenderAnalysisList){
								if(ta.getVendorNo()==0){
									ta.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, subcontract.getPackageNo()));
									Integer seqNo = tenderDetailDao.getNextSequenceNoForTA(ta);
									for(TenderDetail taDetail: toBeUpdatedTaDetails){
										TenderDetail taDetailInDB = tenderDetailDao.obtainTADetailByResourceNo(ta, taDetail.getResourceNo());
										if(taDetailInDB == null){
											logger.info("INSERT BUDGET DETAIL");
											taDetail.setSequenceNo(seqNo);
											taDetail.setLineType("BQ");
											
											taDetail.setTender(ta);
											tenderDetailDao.insert(taDetail);
											
											seqNo++;
										}
									}
									tenderDao.update(ta);
									break;
								}
							}
							
							//Delete Pending Payments
							deletePendingPaymentRequisition(latestPaymentCert, subcontractDetailDao.getSCDetails(subcontract));
							
							//Insert Tender Details for other Tenders under Payment Requisition
							insertTenderDetailForVendor(job, subcontract.getPackageNo(), companyCurrencyCode);
						}else{
							logger.info("Update Budget Tender Analysis - Payment Requisition Roll Back");
							//Delete Pending Payments
							deletePendingPaymentRequisition(latestPaymentCert, subcontractDetailDao.getSCDetails(subcontract));
							
							//Update budget TA
							List<Tender> tenderAnalysisList = tenderDao.obtainTenderAnalysisList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
							Double budgetAmount = resourceSummaryDao.getBudgetForPackage(job, subcontract.getPackageNo());
							for(Tender ta: tenderAnalysisList){
								//Delete TA Details
								tenderDetailDao.deleteByTenderAnalysis(ta); 
								int sequence = 1;
								for(TenderDetail taDetail : toBeUpdatedTaDetails){
									taDetail.setSequenceNo(sequence++);
									taDetail.setTender(ta); //Should save taDetail through cascades
									taDetail.setLineType("BQ");
									taDetail.setRateSubcontract(taDetail.getRateBudget());
									taDetail.setAmountSubcontract(taDetail.getAmountBudget());
									taDetail.setAmountForeign(taDetail.getAmountBudget());
									
									tenderDetailDao.insert(taDetail);
								}
								ta.setBudgetAmount(budgetAmount);
								ta.setAmtBuyingGainLoss(new BigDecimal(0));
								tenderDao.update(ta);
							}
							
						}
					}else{
						logger.info("Update Budget Tender Analysis - No Payment");
						//Delete Pending Payments
						deletePendingPaymentRequisition(latestPaymentCert, subcontractDetailDao.getSCDetails(subcontract));
						
						this.updateTenderAndDetails(tender, toBeUpdatedTaDetails, subcontract, companyCurrencyCode);
						subcontract.setVendorNo(null);
						subcontract.setNameSubcontractor(null);
						subcontractDao.update(subcontract);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else{
			if(vendorNo ==0){
				logger.info("Create TA Budget");
				this.updateTenderAndDetails(tender, toBeUpdatedTaDetails, subcontract, companyCurrencyCode);
			}
		}
		
		return null;
	}
	

	
	private void updateTenderAndDetails(Tender tender, List<TenderDetail> tenderDetails, Subcontract subcontract, String companyCurrencyCode) throws DataAccessException{
			Double totalBudget = 0.0;
			int sequence = 1;
			
			if(tender.getVendorNo()==0){
				//Delete all Tenders when budget is modified
				subcontractDao.resetPackageTA(subcontract);

				tenderDao.insert(tender);
				
				for(TenderDetail taDetail : tenderDetails){
					taDetail.setSequenceNo(sequence++);
					taDetail.setTender(tender); //Should save taDetail through cascades
					taDetail.setLineType("BQ");
					
					totalBudget += taDetail.getAmountSubcontract();
					tenderDetailDao.insert(taDetail);
				}

			}else{
				//Delete TA Details
				tenderDetailDao.deleteByTenderAnalysis(tender); 

				for(TenderDetail taDetail : tenderDetails){
					taDetail.setSequenceNo(sequence++);
					taDetail.setTender(tender); //Should save taDetail through cascades
					taDetail.setLineType("BQ");
					
					if(tender.getCurrencyCode().equals(companyCurrencyCode)){
						taDetail.setAmountForeign(taDetail.getAmountSubcontract());
						totalBudget += taDetail.getAmountSubcontract();
					}
					else{
						taDetail.setAmountForeign(CalculationUtil.round(taDetail.getAmountSubcontract()*tender.getExchangeRate(), 2));
						totalBudget += taDetail.getAmountForeign();
					}
					tenderDetailDao.insert(taDetail);
				}

				//Set Buying Loss/Gain for Tender
				double budgetAmount = tender.getBudgetAmount() != null ? tender.getBudgetAmount() : 0;
				tender.setAmtBuyingGainLoss(CalculationUtil.roundToBigDecimal(budgetAmount-totalBudget, 2));

				tenderDao.saveOrUpdate(tender);
			}
			
	}
	
	private BigDecimal recalculateBuyingGainLoss(Tender tender, List<TenderDetail> tenderDetails, String companyCurrencyCode){
		BigDecimal amountBuyingGainLoss = new BigDecimal(0);
		Double totalBudget = 0.0;

		if(tender.getCurrencyCode().equals(companyCurrencyCode)){
			for(TenderDetail taDetail : tenderDetails)
				totalBudget += taDetail.getAmountSubcontract();
		}else{
			for(TenderDetail taDetail : tenderDetails)
				totalBudget += taDetail.getAmountForeign();
		}

		if(tender != null)
			amountBuyingGainLoss = new BigDecimal(tender.getBudgetAmount() - totalBudget).setScale(2, RoundingMode.HALF_UP);
		return amountBuyingGainLoss;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	public Boolean insertTenderDetailForVendor(JobInfo job, String packageNo, String companyCurrencyCode) throws Exception{
		logger.info("INSERT TENDER DETAIL FOR VENDOR");
		List<Tender> taList = tenderDao.obtainTenderList(job.getJobNumber(), packageNo);
		List<TenderDetail> budgetTADetailList =  obtainTenderDetailList(job.getJobNumber(), packageNo, 0);
		
		//Add new TA Details
		for(Tender ta: taList){
			for(TenderDetail budgetTADetail: budgetTADetailList){
				TenderDetail taDetailInDB = tenderDetailDao.obtainTADetailByResourceNo(ta, budgetTADetail.getResourceNo());
				/*else
						taDetailInDB = tenderDetailDao.obtainTADetailByBQItem(ta, budgetTADetail.getBillItem(), budgetTADetail.getObjectCode(), budgetTADetail.getSubsidiaryCode(), budgetTADetail.getResourceNo());*/

				if(taDetailInDB == null){
					//Insert new TA Details
					TenderDetail taDetail = new TenderDetail();
					taDetail.setLineType(budgetTADetail.getLineType());
					taDetail.setBillItem(budgetTADetail.getBillItem());
					taDetail.setObjectCode(budgetTADetail.getObjectCode());
					taDetail.setSubsidiaryCode(budgetTADetail.getSubsidiaryCode());
					taDetail.setDescription(budgetTADetail.getDescription());
					taDetail.setUnit(budgetTADetail.getUnit());
					taDetail.setQuantity(budgetTADetail.getQuantity());
					
					taDetail.setSequenceNo(budgetTADetail.getSequenceNo());
					taDetail.setResourceNo(budgetTADetail.getResourceNo());

					taDetail.setRateBudget(budgetTADetail.getRateBudget());
					taDetail.setRateSubcontract(budgetTADetail.getRateBudget());
					taDetail.setRateSubcontract(budgetTADetail.getRateBudget());
					taDetail.setAmountSubcontract(budgetTADetail.getAmountBudget());
					taDetail.setAmountBudget(budgetTADetail.getAmountBudget());
					taDetail.setAmountForeign(budgetTADetail.getAmountBudget()!=null && ta.getExchangeRate()!=null? budgetTADetail.getAmountBudget()*ta.getExchangeRate() : 0.0);
					

					taDetail.setTender(ta);
					tenderDetailDao.insert(taDetail);
				}
			}
		}
		
		//Recalculate Tender Budget
		for(Tender ta: taList){
			List<TenderDetail> tenderDetailsInDB = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(ta);
			ta.setAmtBuyingGainLoss(recalculateBuyingGainLoss(ta, tenderDetailsInDB, companyCurrencyCode));
			tenderDao.update(ta);
			
			if(Tender.TA_STATUS_RCM.equals(ta.getStatus())){
				//Recalculate SCPackage Subcon Sum
				subcontractService.recalculateSCPackageSubcontractSum(ta.getSubcontract(), tenderDetailsInDB);
			}
		}
		
		
		return true;
	}
	
	public String deleteTender(String jobNo, String subcontractNo, Integer subcontractorNo) throws Exception{
		String error = "";
		Tender tenderAnalysis = tenderDao.obtainTender(jobNo, subcontractNo, subcontractorNo);
		if(tenderAnalysis == null){
			error = "Tender does not exist.";
			return error;
		}
		JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
		Subcontract subcontract = subcontractDao.obtainPackage(job, subcontractNo);
		List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
		if(scPaymentCertList!=null && scPaymentCertList.size()>0 
		&& (tenderAnalysis.getStatus()!=null && tenderAnalysis.getStatus().equals(Tender.TA_STATUS_RCM))){
			error = "Recommended Tender under payment requistion cannot be deleted.";
			return error;
		}
		
		
		List<TenderDetail> details = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
		for (TenderDetail detail: details){
			tenderDetailDao.delete(detail);
		}
		
		List<TenderVariance> tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(jobNo, subcontractNo, String.valueOf(subcontractorNo));
		for (TenderVariance tenderVariance: tenderVarianceList){
			tenderVarianceHBDao.delete(tenderVariance);
		}
		
		tenderDao.delete(tenderAnalysis);
		
		return error;
	}

	private void deletePendingPaymentRequisition(PaymentCert latestPaymentCert, List<SubcontractDetail> scDetailsList) throws NumberFormatException, Exception{
		try {
			if(latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
				//Delete Pending Payment
				attachmentService.deleteAttachmentByPaymentCert(latestPaymentCert);
				paymentCertDao.delete(latestPaymentCert);
				paymentCertDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());

				//Reset cumulative Cert Amount in ScDetail
				for(SubcontractDetail scDetails: scDetailsList){
					if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
						scDetails.setAmountCumulativeCert(scDetails.getAmountPostedCert());
						subcontractDetailDao.update(scDetails);
					}
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
	}


	public String updateRecommendedTender(String jobNo, String subcontractNo, Integer vendorNo) {
		String error = "";
		try {
			if(vendorNo == null || vendorNo <0){
				error = "Only registered Tenderer in system can be selected as recommended Tenderer.";
				return error;
			}
			
			PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(jobNo, subcontractNo);
			
			if(latestPaymentCert!=null 
					&& latestPaymentCert.getDirectPayment().equals("Y") 
					&& latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE)){
				
				error = "Payment Requisition exists. Tenderer should not be changed.";
				return error;
			}
			
			//Delete 1st Pending Payment Requisition when changing Subcontractor 
			deletePendingPaymentRequisition(paymentCertDao.obtainPaymentLatestCert(jobNo, subcontractNo), subcontractDetailDao.obtainSCDetails(jobNo, subcontractNo));
			
			Subcontract subcontract = subcontractDao.obtainSCPackage(jobNo, subcontractNo);
			List<Tender> tenderList = this.obtainTenderList(jobNo, subcontractNo);
			
			for (Tender tender: tenderList){
				if(vendorNo.equals(tender.getVendorNo())){
					tender.setStatus(Tender.TA_STATUS_RCM);
					
					subcontract.setVendorNo(vendorNo.toString());
					subcontract.setNameSubcontractor(tender.getNameSubcontractor());
				}
				else 
					tender.setStatus(null);
				tenderDao.update(tender);
			}
			
			subcontractDao.update(subcontract);

		} catch (Exception e) {
			error = "Tender cannot be updated.";
			e.printStackTrace();
		}
		return error;
	}	
	
	
	public TenderComparisonDTO obtainTenderComparisonList(String jobNo, String packageNo) throws Exception{
		TenderComparisonDTO tenderComparisonDTO = new TenderComparisonDTO();
		Map<Integer, List<Map<String, Double>>> vendorDetailMap = new HashMap<Integer, List<Map<String, Double>>>();
		List<TenderDetailDTO> tenderDetailDTOList = new ArrayList<TenderDetailDTO>();
		
		List<Tender> tenderList = tenderDao.obtainTenderAnalysis(jobNo, packageNo);
		
		for(Tender tender: tenderList){
			List<TenderDetail> tenderDetailList = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tender);
			
			for(TenderDetail taDetail: tenderDetailList){
				if(tender.getVendorNo() == 0){
					TenderDetailDTO taDetailWrapper = new TenderDetailDTO();
					taDetailWrapper.setBillItem(taDetail.getBillItem());
					taDetailWrapper.setObjectCode(taDetail.getObjectCode());
					taDetailWrapper.setSubsidiaryCode(taDetail.getSubsidiaryCode());
					taDetailWrapper.setDescription(taDetail.getDescription());
					taDetailWrapper.setUnit(taDetail.getUnit());
					taDetailWrapper.setQuantity(taDetail.getQuantity());
					taDetailWrapper.setSequenceNo(taDetail.getSequenceNo());
					
					tenderDetailDTOList.add(taDetailWrapper);
				}
				
				Map<String, Double> vendorAmount = new HashMap<String, Double>();
				if(tender.getVendorNo() == 0){
					vendorAmount.put("Budget Amount ["+tender.getCurrencyCode().trim()+"]", taDetail.getAmountBudget());					
				}else
					vendorAmount.put(tender.getNameSubcontractor()+" ["+tender.getCurrencyCode().trim()+"]", taDetail.getAmountSubcontract());

				
				 
				 List<Map<String, Double>> vendorList = vendorDetailMap.get(taDetail.getSequenceNo());
				 if(vendorList == null)
					 vendorList = new ArrayList<Map<String, Double>>();
				
				 vendorList.add(vendorAmount);
				
				vendorDetailMap.put(taDetail.getSequenceNo(), vendorList);
			}
		}


		tenderComparisonDTO.setTenderDetailDTOList(tenderDetailDTOList);
		tenderComparisonDTO.setVendorDetailMap(vendorDetailMap);
		return tenderComparisonDTO;
	}



	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
}