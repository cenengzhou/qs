package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.CreateGLWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.JobInfoWSDao;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.MainCertRetentionReleaseHBDao;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.retentionReleaseSchedule.RetentionReleaseScheduleExcelGenerator;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;
import com.gammon.qs.wrapper.RetentionReleaseSchedulePaginationWrapper;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MainCertRetentionReleaseService implements Serializable {
	
	private static final long serialVersionUID = -1406168215541798928L;

	private static final int MAX_CRITERIA_LIST = 900; 
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private transient MainCertRetentionReleaseHBDao retentionReleaseScheduleHBDao;
	@Autowired
	private transient JobInfoHBDao jobHBDao;
	@Autowired
	private transient JobInfoWSDao jobWSDao;
	@Autowired
	private transient MainCertHBDao mainContractCertificateDao;
	@Autowired
	private transient MainCertWSDao mainContractCertificateWSDao;
	@Autowired
	private transient CreateGLWSDao createGLDao;
	@Autowired
	private transient MasterListWSDao masterListDao;
	@Autowired
	private MainCertService mainContractCertificateRepository;
	@Autowired
	private UserAccessRightsService userAccessRightsRepository;
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private JasperConfig jasperConfig;
	
	//For Pagination (Work in Session beans only)
	private List<RetentionReleaseScheduleEnquiryWindowsWrapper> cachedRetentionReleaseList;
	private static final int RR_PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	
	public MainCertRetentionReleaseHBDao getRetentionReleaseScheduleHBDao() {
		return retentionReleaseScheduleHBDao;
	}
	public void setRetentionReleaseScheduleHBDao(
			MainCertRetentionReleaseHBDao retentionReleaseScheduleHBDao) {
		this.retentionReleaseScheduleHBDao = retentionReleaseScheduleHBDao;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 18 Dec, 2013
	 * **/
	public String obtainCumulativeRetentionReleaseByJob(MainCert mainCert) throws DatabaseOperationException{
		String errorMessage = "";
		
		List<MainCertRetentionRelease> rrList = retentionReleaseScheduleHBDao.obtainRetentionReleaseScheduleByJob(mainCert.getJobNo());
		Double cumRRCalculatedByMainCert = mainCert.getCertifiedMainContractorRetentionReleased()+mainCert.getCertifiedRetentionforNSCNDSCReleased()+mainCert.getCertifiedMOSRetentionReleased();
		Double cumRetentionRelease = 0.0;
		Double cumActualRetentionRelease = 0.0;
		
		if (rrList!=null && rrList.size()>0)
			for (MainCertRetentionRelease rr:rrList){
				if (MainCertRetentionRelease.STATUS_ACTUAL.equals(rr.getStatus())){
					cumActualRetentionRelease+=rr.getActualReleaseAmt();
					cumRetentionRelease+=rr.getActualReleaseAmt();
				}
				else
					cumRetentionRelease+=rr.getForecastReleaseAmt();
			}
		cumRetentionRelease=CalculationUtil.round(cumRetentionRelease,2);
		
		Double rrDiff = CalculationUtil.round(cumRetentionRelease-mainCert.getCertifiedMOSRetention()-mainCert.getCertifiedMainContractorRetention()-mainCert.getCertifiedRetentionforNSCNDSC(), 2);
		Double actualRRDiff = CalculationUtil.round(cumActualRetentionRelease - cumRRCalculatedByMainCert, 2);
		
		if(rrDiff==0.00 && actualRRDiff==0.00){
			logger.info("Retention Release is balanced.");
		}else{
			if(rrDiff!=0.00)
				errorMessage = "Total Cumulative Retention Release is not balanced.";
			else if(actualRRDiff !=0.0)
				errorMessage = "Actual Cumulative Retention Release is not balanced.";
			logger.info(errorMessage);
			logger.info("Total Retention Release Diff.: "+rrDiff);
			logger.info("Actual Retention Release Diff.: "+actualRRDiff+"- cumActualRetentionRelease: "+cumActualRetentionRelease+"- cumRRCalculatedByMainCert: "+cumRRCalculatedByMainCert);
		}
		return errorMessage;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 18 Dec, 2013
	 * **/
	public List<MainCertRetentionRelease> calculateRetentionReleaseScheduleByJob(MainCert mainCert) throws DatabaseOperationException{
		logger.info("STARTED - calculateRetentionReleaseScheduleByJob");
		String jobNumber = mainCert.getJobNo();
		
		try {
			String username = securityService.getCurrentUser().getUsername();
			List<String> securityList = userAccessRightsRepository.getAccessRights(username, RoleSecurityFunctions.F010405_RETENTION_RELEASE_WINDOW);
			
			if (securityList.contains("WRITE")){
				List<MainCert> mainCertList = mainContractCertificateRepository.getMainContractCertificateList(jobNumber);
				ArrayList<MainCertRetentionRelease> newRRSList = new ArrayList<MainCertRetentionRelease>();
				MainCert prevMainCert = null;

				if (mainCertList!=null && mainCertList.size()>0)
					for (int i=0;i<mainCertList.size();i++){//Calculate all certs
						if (mainCertList.get(i).getCertificateNumber().equals(mainCert.getCertificateNumber()-1))
							prevMainCert=mainCertList.get(i);

						Double retentionRelease=0.0;
						retentionRelease+=mainCertList.get(i).getCertifiedMainContractorRetentionReleased()==null?0:mainCertList.get(i).getCertifiedMainContractorRetentionReleased();
						retentionRelease+=mainCertList.get(i).getCertifiedRetentionforNSCNDSCReleased()==null?0:mainCertList.get(i).getCertifiedRetentionforNSCNDSCReleased();
						retentionRelease+=mainCertList.get(i).getCertifiedMOSRetentionReleased()==null?0:mainCertList.get(i).getCertifiedMOSRetentionReleased();
						if (i>0){
							retentionRelease=retentionRelease - (mainCertList.get(i-1).getCertifiedMainContractorRetentionReleased()==null?0:mainCertList.get(i-1).getCertifiedMainContractorRetentionReleased());
							retentionRelease=retentionRelease - (mainCertList.get(i-1).getCertifiedRetentionforNSCNDSCReleased()==null?0:mainCertList.get(i-1).getCertifiedRetentionforNSCNDSCReleased());
							retentionRelease=retentionRelease - (mainCertList.get(i-1).getCertifiedMOSRetentionReleased()==null?0:mainCertList.get(i-1).getCertifiedMOSRetentionReleased());
						}
						//logger.info("CertificateNumber: "+mainCertList.get(i).getCertificateNumber()+" - retentionRelease: "+retentionRelease);
						if (CalculationUtil.round(retentionRelease, 2) != 0.00){//Insert rr if not exist in db	
						MainCertRetentionRelease dbRR = retentionReleaseScheduleHBDao.obtainActualRetentionReleaseByMainCertNo(jobNumber, mainCertList.get(i).getCertificateNumber());
							if(dbRR==null){
								MainCertRetentionRelease newRR = new MainCertRetentionRelease();
								newRR.setJobNumber(jobNumber);
								newRR.setMainCertNo(mainCertList.get(i).getCertificateNumber());

								newRR.setDueDate(mainCertList.get(i).getCertDueDate());
								newRR.setActualReleaseAmt(CalculationUtil.round(retentionRelease, 2));
								newRR.setForecastReleaseAmt(new Double(0.0));
								newRR.setStatus(MainCertRetentionRelease.STATUS_ACTUAL);
								//newRR.setReleasePercent(RoundingUtil.round(retentionRelease*100.0/totalRetentionAmount,2));//@deprecated
								newRR.setSequenceNo(0);//@deprecated
								newRRSList.add(newRR);
							}
						}
					}
				updateActualRetentionRelease(jobNumber, mainCert,prevMainCert,newRRSList);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed: error occured in calculating the retention release.");
		}

		logger.info("END - calculateRetentionReleaseScheduleByJob");
		return retentionReleaseScheduleHBDao.obtainRetentionReleaseScheduleByJob(jobNumber);
	}
	
	/**
	 * @author koeyyeung
	 * modified on 18 Dec, 2013
	 * **/
	private void updateActualRetentionRelease(String jobNumber, MainCert mainCert, MainCert prevMainCert, List<MainCertRetentionRelease> newRRSList) throws DatabaseOperationException {
		logger.info("STARTED - generateActualRetentionRelease");
		
		double rrDiff = CalculationUtil.round(calRetentionReleaseAmount(mainCert)-calRetentionReleaseAmount(prevMainCert), 2);
		MainCertRetentionRelease rr = retentionReleaseScheduleHBDao.obtainActualRetentionReleaseByMainCertNo(jobNumber, mainCert.getCertificateNumber());

		if(rr!=null && (mainCert.getCertificateNumber().equals(rr.getMainCertNo()))){
			double dbRRDiff= CalculationUtil.round(rrDiff-rr.getActualReleaseAmt(), 2);
			//update the record if the amount changed (the last cert, staus < 300)
			if (rrDiff!=0.00 && dbRRDiff!=0.00){
				rr.setActualReleaseAmt(rrDiff);
				rr.setStatus(MainCertRetentionRelease.STATUS_ACTUAL);
				rr.setDueDate(mainCert.getCertDueDate());
				rr.setMainCertNo(mainCert.getCertificateNumber());
				
				retentionReleaseScheduleHBDao.update(rr);
				logger.info("UPDATE Retention Release: Main Cert Number: "+rr.getMainCertNo()+" - Actual Release Amount: "+rr.getActualReleaseAmt());
			}
			//remove it if retention release is zero
			else if (rrDiff==0.00){
				retentionReleaseScheduleHBDao.inactivate(rr);
				logger.info("INACTIVE Retention Release: Main Cert Number: "+rr.getMainCertNo());
			}
		}

		for(MainCertRetentionRelease rrToInsert: newRRSList){
			retentionReleaseScheduleHBDao.insert(rrToInsert);
			logger.info("INSERT Retention Release: Main Cert Number: "+rrToInsert.getMainCertNo()+" - Actual Release Amount: "+rrToInsert.getActualReleaseAmt());
		}
		
		logger.info("END - generateActualRetentionRelease");
	}
	
	private double calRetentionReleaseAmount(MainCert mainCert){
		if (mainCert==null)
			return 0.0;
		return 
			  (mainCert.getCertifiedRetentionforNSCNDSCReleased()==null?0:mainCert.getCertifiedRetentionforNSCNDSCReleased().doubleValue())
			+ (mainCert.getCertifiedMainContractorRetentionReleased()==null?0:mainCert.getCertifiedMainContractorRetentionReleased().doubleValue())
			+ (mainCert.getCertifiedMOSRetentionReleased()==null?0:mainCert.getCertifiedMOSRetentionReleased().doubleValue());
	}
	
	public Boolean updateForecastRetentionRelease(String jobNo, List<MainCertRetentionRelease> modifiedList) throws DatabaseOperationException{
		logger.info("STARTED - updateForecastRetentionRelease");
		Boolean updated = false;
		List<MainCertRetentionRelease> rrToUpdateList = new ArrayList<MainCertRetentionRelease>();
		List<MainCertRetentionRelease> rrToInsertList = new ArrayList<MainCertRetentionRelease>();
		
			for(MainCertRetentionRelease rr: modifiedList)
				rrToInsertList.add(rr);
			
			List<MainCertRetentionRelease> dbList = retentionReleaseScheduleHBDao.obtainRetentionReleaseScheduleByJob(jobNo);

			for(MainCertRetentionRelease dbRR: dbList){
				for(int i = 0; i < modifiedList.size(); i++){
					if(modifiedList.get(i).getId().equals(dbRR.getId())){
						if("F".equals(modifiedList.get(i).getStatus())){
							if(CalculationUtil.round(modifiedList.get(i).getForecastReleaseAmt()-dbRR.getForecastReleaseAmt(), 2) != 0.00 
									|| modifiedList.get(i).getContractualDueDate()!=dbRR.getContractualDueDate()
									|| modifiedList.get(i).getDueDate()!=dbRR.getDueDate())
								rrToUpdateList.add(modifiedList.get(i));
						}
						rrToInsertList.remove(modifiedList.get(i));
						break;
					}
					if(i==modifiedList.size()-1){
						logger.info("INACTIVE-retention release id:"+dbRR.getId());
						retentionReleaseScheduleHBDao.inactivate(dbRR);
						updated=true;
					}
				}
				if(modifiedList.size()==0){
					logger.info("INACTIVE-retention release id:"+dbRR.getId());
					retentionReleaseScheduleHBDao.inactivate(dbRR);
					updated=true;
				}
			}
			
			for(MainCertRetentionRelease rrToUpdate: rrToUpdateList){//update records
				logger.info("Update retention release id:"+rrToUpdate.getId());
				retentionReleaseScheduleHBDao.merge(rrToUpdate);
				updated=true;
			}
			
			for (MainCertRetentionRelease rrToInsert: rrToInsertList){//insert new records
				logger.info("Insert retention release forecast amount:"+rrToInsert.getForecastReleaseAmt());
				rrToInsert.setSequenceNo(0);
				retentionReleaseScheduleHBDao.insert(rrToInsert);
				updated=true;
			}
		
		logger.info("END - updateForecastRetentionRelease");
		return updated;
	}

	
	public ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseSchedule(
			String division, String company, String jobNo) throws DatabaseOperationException{
		List<JobInfo> jobList = jobHBDao.obtainJobsByDivCoJob(division,company,jobNo);
		List<String> jobNoList = new ArrayList<String>();
		if (jobList==null || jobList.size()<1)
			return new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>();
		List<MainCertRetentionRelease> rrList = new ArrayList<MainCertRetentionRelease>();
		for (JobInfo job:jobList)
			jobNoList.add(job.getJobNumber().trim());
		for (int i = 0; i*MAX_CRITERIA_LIST<jobNoList.size();i++)
			if ((i+1)*MAX_CRITERIA_LIST>jobNoList.size())
				rrList.addAll(retentionReleaseScheduleHBDao.searchByJobList(jobNoList.subList(i*MAX_CRITERIA_LIST, jobNoList.size())));
			else
				rrList.addAll(retentionReleaseScheduleHBDao.searchByJobList(jobNoList.subList(i*MAX_CRITERIA_LIST,(i+1)*MAX_CRITERIA_LIST )));
		ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper> resultList = new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>();
		
		JobInfo aJob = null;
		String jobCurrency = "";
		String companyName = "";
		HashMap<String, String> companyList = new HashMap<String, String>();
		for (MainCertRetentionRelease rr:rrList){
			if (aJob==null || !aJob.getJobNumber().equals(rr.getJobNumber())){
				aJob = jobHBDao.obtainJobInfo(rr.getJobNumber());
				try {
					GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(aJob.getCompany(), new Date());
					jobCurrency = "";
					jobCurrency = periodYear.getCurrencyCodeFrom();

					if (aJob.getCompany() != null && !"".equals(aJob.getCompany().trim())) {
						companyName = companyList.get(aJob.getCompany());
						if (companyName == null || "".equals(companyName.trim())) {
							companyName = masterListDao.getOneVendor(Integer.parseInt(aJob.getCompany()) + "").getVendorName();
							companyList.put(aJob.getCompany(), companyName);
						}
					} else
						companyName = "";
				} catch (Exception e) {
					logger.info("Company:" + aJob.getCompany() + " of Job:" + rr.getJobNumber() + " have following exception.\n" + e.getMessage());
				}
			}
			RetentionReleaseScheduleEnquiryWindowsWrapper rrwrapper = new RetentionReleaseScheduleEnquiryWindowsWrapper();
			rrwrapper.setDivision(aJob.getDivision());
			rrwrapper.setCompany(aJob.getCompany());
			rrwrapper.setJobNo(rr.getJobNumber());
			rrwrapper.setJobDesc(aJob.getDescription());
			rrwrapper.setCompanyName(companyName);
			rrwrapper.setCurrency(jobCurrency);
			if ("S".equalsIgnoreCase(aJob.getSoloJV()))
				rrwrapper.setSoloJV("Solo");
			else if ("JV".equalsIgnoreCase(aJob.getSoloJV()))
				rrwrapper.setSoloJV("JV");
			
			
			try{
				rrwrapper.setProjectedContractValue(aJob.getProjectedContractValue());
				rrwrapper.setMaxRetentionPercentage(aJob.getMaxRetentionPercentage());
				rrwrapper.setEstimatedRetention(RoundingUtil.round(RoundingUtil.multiple(RoundingUtil.multiple(aJob.getMaxRetentionPercentage(),aJob.getProjectedContractValue()),0.01),2));
			}catch(NullPointerException npe){
				logger.log(Level.SEVERE,"NullPointerException @ setting EstimatedRetention:"+aJob.getJobNumber());
			}
			try {
				rrwrapper.setCompletionDate(jobWSDao.obtainJobDates(rr.getJobNumber()).getAnticipatedCompletionDate());
			} catch (Exception e) {
				logger.info(e.getLocalizedMessage());
			}
			
			try {
				JobInfo job = jobHBDao.obtainJobInfo(rr.getJobNumber());
				rrwrapper.setActualPCCDate(job!=null?job.getActualPCCDate():null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			rrwrapper.setDlp(aJob.getDefectLiabilityPeriod());
			try {
				MainCert latestPostedCert = mainContractCertificateDao.searchLatestPostedCert(rr.getJobNumber());
				rrwrapper.setCumRetentionRec(
						 (latestPostedCert.getCertifiedMainContractorRetention()==null?0:latestPostedCert.getCertifiedMainContractorRetention())
						+(latestPostedCert.getCertifiedRetentionforNSCNDSC()==null?0:latestPostedCert.getCertifiedRetentionforNSCNDSC())
						+(latestPostedCert.getCertifiedMOSRetention()==null?0:latestPostedCert.getCertifiedMOSRetention()));
			}catch (NullPointerException npe){
				logger.log(Level.SEVERE,"NullPointerException @ setting CumRetentionRec:"+aJob.getJobNumber());
			}
			rrwrapper.setRetentionReleaseSeq(rr.getSequenceNo());
			rrwrapper.setReceiptAmt(rr.getActualReleaseAmt());
			rrwrapper.setStatus(rr.getStatus());
			if (MainCertRetentionRelease.STATUS_ACTUAL.equals(rr.getStatus()))
				try {
					rrwrapper.setReceiptDate(mainContractCertificateWSDao.obtainParentMainContractCertificate(rr.getJobNumber(), rr.getMainCertNo()).getReceivedDate());
					MainCert thisMainCert = mainContractCertificateDao.obtainMainContractCert(rr.getJobNumber(), rr.getMainCertNo());
					if (thisMainCert.getCertificateStatus()!=null && Integer.parseInt(thisMainCert.getCertificateStatus().trim())<300 )
						rrwrapper.setStatus(MainCertRetentionRelease.STATUS_NOT_YET_RECEIVE);
				} catch (Exception e) {
					logger.info(e.getLocalizedMessage());
				}
			else
				rrwrapper.setOutstandingAmt(rr.getForecastReleaseAmt());
			rrwrapper.setMainCert(rr.getMainCertNo());
			rrwrapper.setDueDate(rr.getDueDate());
			rrwrapper.setContractualDueDate(rr.getContractualDueDate());
			resultList.add(rrwrapper);
	//		}
		}
		return resultList;
	}

	public ExcelFile retentionReleaseScheduleDownloadExcelFile(String division,
			String company, String jobNo) throws DatabaseOperationException {
		RetentionReleaseScheduleExcelGenerator generator = new RetentionReleaseScheduleExcelGenerator(obtainRetentionReleaseSchedule(division,company,jobNo));
		return generator.generate();
	}
	
	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseScheduleForPagination(
			String division, String company, String jobNo) throws DatabaseOperationException {
		cachedRetentionReleaseList = obtainRetentionReleaseSchedule(division, company, jobNo);
		return obtainRetentionReleaseScheduleForPaginationByPage(0);
	}
	
	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseScheduleForPaginationByPage(
			int pageNo) throws DatabaseOperationException{
		PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> wrapper = new PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper>();
		wrapper.setCurrentPage(pageNo);
		wrapper.setTotalRecords(cachedRetentionReleaseList.size());
		wrapper.setTotalPage(((wrapper.getTotalRecords()-1)/RR_PAGE_SIZE)+1);
		int fromInd =  (pageNo) * RR_PAGE_SIZE;
		if (fromInd<0){
			fromInd = 0;
			wrapper.setCurrentPage(0);
		}
		int toInd = fromInd+ RR_PAGE_SIZE;
		if(toInd > cachedRetentionReleaseList.size())
			toInd = cachedRetentionReleaseList.size();
		wrapper.setCurrentPageContentList(new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>());
		wrapper.getCurrentPageContentList().addAll(cachedRetentionReleaseList.subList(fromInd, toInd));
		return wrapper;
	}

	/**
	 * add by paulyiu 20150901
	 */

	public ByteArrayOutputStream downloadRetentionReleaseScheduleReportFile(String jobNumber, String division, String company, String jobNo, String jasperReportName,String fileType) throws Exception {
		List<RetentionReleaseScheduleEnquiryWindowsWrapper> wrapperList = obtainRetentionReleaseSchedule(division,company,jobNo);

		String fileFullPath = jasperConfig.getTemplatePath() + jasperReportName;
		Date asOfDate = new Date();
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		parameters.put("AS_OF_DATE", asOfDate);
		parameters.put("JOB_NO", jobNumber);
		parameters.put("JOB_DESC", job.getDescription());
		parameters.put("REPORT_TYPE", fileType);
		parameters.put("SEARCH_DIVISION", division);
		parameters.put("SEARCH_COMPANY", company);
		parameters.put("SEARCH_JOBNUMBER", jobNo);

		if (wrapperList.size() == 0) {
			RetentionReleaseScheduleEnquiryWindowsWrapper wrapper = new RetentionReleaseScheduleEnquiryWindowsWrapper();
			wrapperList.add(wrapper);
		}
		if(fileType.contentEquals(JasperReportHelper.OUTPUT_PDF))
			return JasperReportHelper.get().setCurrentReport(wrapperList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
		else
			return JasperReportHelper.get().setCurrentReport(wrapperList, fileFullPath, parameters).compileAndAddReport().exportAsExcel();
		
	}
	
	/**
	 * add by paulnpyiu
	 * 2015-10-06
	 * add search filter for status and cert status
	 */

	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(
			String division, String company, String jobNo, String status, String certStatus) throws DatabaseOperationException {
		cachedRetentionReleaseList = obtainRetentionReleaseSchedule(division, company, jobNo, status, certStatus);
		return obtainRetentionReleaseScheduleForPaginationWrapperByPage(0);
	}
	
	/**
	 * modify by paulnpyiu
	 * 2015-10-28
	 * add job security
	 */
	public ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseSchedule(
			String division, String company, String jobNo, String status, String certStatus) throws DatabaseOperationException{
		//TODO: GSF | List<JobSecurity> obtainCompanyListByUsername(username) | remark MainCertRetentionReleaseService.obtainRetentionReleaseSchedule(String division, String company, String jobNo, String status, String certStatus)
		throw new RuntimeException("GSF | List<JobSecurity> obtainCompanyListByUsername(username) | remark MainCertRetentionReleaseService.obtainRetentionReleaseSchedule(String division, String company, String jobNo, String status, String certStatus)");
//		List<String> jobNoInJobSecurityList = new ArrayList<String>();
//		String username = securityService.getCurrentUser().getUsername();
//		if(!GenericValidator.isBlankOrNull(jobNo)){
//			if(adminService.canAccessJob(username, jobNo)){
//				jobNoInJobSecurityList.add(jobNo);
//				logger.info("User: " + username + " search job:"+jobNo);
//			} else {
//				logger.info("User: " + username + " is not authorized to access Job:"+jobNo);
//			}
//		}else{
//			List<JobSecurity> jobSecurityList = adminService.obtainCompanyListByUsername(username);
//			String authorizedJobs = "";
//			for(JobSecurity jobSecurity:jobSecurityList){
//				if(jobSecurity.getCompany().equals("NA")){
//					authorizedJobs = null;
//					break;
//				}
//				String jobNoInJobSecurity = jobSecurity.getJob_No();
//				jobNoInJobSecurityList.add(jobNoInJobSecurity);
//				authorizedJobs += jobNoInJobSecurity;
//			}
//			logger.info("User: " + username + " search job list:" + authorizedJobs);
//		}
//		List<JobInfo> jobInJobSecurityList = jobHBDao.obtainJobsByDivCoJobList(division,company,jobNoInJobSecurityList);
//		List<String> jobNoList = new ArrayList<String>();
//		if (jobInJobSecurityList==null || jobInJobSecurityList.size()<1)
//			return new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>();
//		List<MainCertRetentionRelease> rrList = new ArrayList<MainCertRetentionRelease>();
//		for (JobInfo job:jobInJobSecurityList){
//			jobNoList.add(job.getJobNumber().trim());
//		}
//		for (int i = 0; i*MAX_CRITERIA_LIST<jobNoList.size();i++)
//			if ((i+1)*MAX_CRITERIA_LIST>jobNoList.size())
//				rrList.addAll(retentionReleaseScheduleHBDao.searchByJobList(jobNoList.subList(i*MAX_CRITERIA_LIST, jobNoList.size())));
//			else
//				rrList.addAll(retentionReleaseScheduleHBDao.searchByJobList(jobNoList.subList(i*MAX_CRITERIA_LIST,(i+1)*MAX_CRITERIA_LIST )));
//		ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper> resultList = new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>();
//		
//		JobInfo aJob = null;
//		String jobCurrency = "";
//		String companyName = "";
//		HashMap<String, String> companyList = new HashMap<String, String>();
//		for (MainCertRetentionRelease rr:rrList){
//			if (aJob==null || !aJob.getJobNumber().equals(rr.getJobNumber())){
//				aJob = jobHBDao.obtainJobInfo(rr.getJobNumber());
//				try {
//					GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(aJob.getCompany(), new Date());
//					jobCurrency = "";
//					jobCurrency = periodYear.getCurrencyCodeFrom();
//
//					if (aJob.getCompany() != null && !"".equals(aJob.getCompany().trim())) {
//						companyName = companyList.get(aJob.getCompany());
//						if (companyName == null || "".equals(companyName.trim())) {
//							companyName = masterListDao.getOneVendor(Integer.parseInt(aJob.getCompany()) + "").getVendorName();
//							companyList.put(aJob.getCompany(), companyName);
//						}
//					} else
//						companyName = "";
//				} catch (Exception e) {
//					logger.info("Company:" + aJob.getCompany() + " of Job:" + rr.getJobNumber() + " have following exception.\n" + e.getMessage());
//				}
//			}
//			RetentionReleaseScheduleEnquiryWindowsWrapper rrwrapper = new RetentionReleaseScheduleEnquiryWindowsWrapper();
//			rrwrapper.setDivision(aJob.getDivision());
//			rrwrapper.setCompany(aJob.getCompany());
//			rrwrapper.setJobNo(rr.getJobNumber());
//			rrwrapper.setJobDesc(aJob.getDescription());
//			rrwrapper.setCompanyName(companyName);
//			rrwrapper.setCurrency(jobCurrency);
//			if ("S".equalsIgnoreCase(aJob.getSoloJV()))
//				rrwrapper.setSoloJV("Solo");
//			else if ("JV".equalsIgnoreCase(aJob.getSoloJV()))
//				rrwrapper.setSoloJV("JV");
//			
//			
//			try{
//				rrwrapper.setProjectedContractValue(aJob.getProjectedContractValue());
//				rrwrapper.setMaxRetentionPercentage(aJob.getMaxRetentionPercentage());
//				rrwrapper.setEstimatedRetention(RoundingUtil.round(RoundingUtil.multiple(RoundingUtil.multiple(aJob.getMaxRetentionPercentage(),aJob.getProjectedContractValue()),0.01),2));
//			}catch(NullPointerException npe){
//				logger.log(Level.SEVERE,"NullPointerException @ setting EstimatedRetention:"+aJob.getJobNumber());
//			}
//			try {
//				rrwrapper.setCompletionDate(jobWSDao.obtainJobDates(rr.getJobNumber()).getAnticipatedCompletionDate());
//			} catch (Exception e) {
//				logger.info(e.getLocalizedMessage());
//			}
//			
//			try {
//				JobInfo job = jobHBDao.obtainJobInfo(rr.getJobNumber());
//				rrwrapper.setActualPCCDate(job!=null?job.getActualPCCDate():null);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//			
//			rrwrapper.setDlp(aJob.getDefectLiabilityPeriod());
//			try {
//				MainCert latestPostedCert = mainContractCertificateDao.searchLatestPostedCert(rr.getJobNumber());
//				rrwrapper.setCumRetentionRec(
//						 (latestPostedCert.getCertifiedMainContractorRetention()==null?0:latestPostedCert.getCertifiedMainContractorRetention())
//						+(latestPostedCert.getCertifiedRetentionforNSCNDSC()==null?0:latestPostedCert.getCertifiedRetentionforNSCNDSC())
//						+(latestPostedCert.getCertifiedMOSRetention()==null?0:latestPostedCert.getCertifiedMOSRetention()));
//			}catch (NullPointerException npe){
//				logger.log(Level.SEVERE,"NullPointerException @ setting CumRetentionRec:"+aJob.getJobNumber());
//			}
//			rrwrapper.setRetentionReleaseSeq(rr.getSequenceNo());
//			rrwrapper.setReceiptAmt(rr.getActualReleaseAmt());
//			rrwrapper.setStatus(rr.getStatus());
//			MainCert thisMainCert = mainContractCertificateDao.obtainMainContractCert(rr.getJobNumber(), rr.getMainCertNo());
//			String thisMainCertStatus ="";
//			if(thisMainCert!=null) thisMainCertStatus = thisMainCert.getCertificateStatus();
//			if(!certStatus.equals("") && !thisMainCertStatus.equals(certStatus))continue;
//
//			if (MainCertRetentionRelease.STATUS_ACTUAL.equals(rr.getStatus())){
//				try {
//					rrwrapper.setReceiptDate(mainContractCertificateWSDao.obtainParentMainContractCertificate(rr.getJobNumber(), rr.getMainCertNo()).getReceivedDate());
//					if (thisMainCert.getCertificateStatus()!=null && Integer.parseInt(thisMainCert.getCertificateStatus().trim())<300 )
//						rrwrapper.setStatus(MainCertRetentionRelease.STATUS_NOT_YET_RECEIVE);
//				} catch (Exception e) {
//					logger.info(e.getLocalizedMessage());
//				}
//			} else {
//				rrwrapper.setOutstandingAmt(rr.getForecastReleaseAmt());
//			}
//			rrwrapper.setMainCert(rr.getMainCertNo());
//			rrwrapper.setMainCertStatus(thisMainCertStatus);
//			rrwrapper.setDueDate(rr.getDueDate());
//			rrwrapper.setContractualDueDate(rr.getContractualDueDate());
//			if(!status.equals("") && !rr.getStatus().equals(status)) continue;
//			resultList.add(rrwrapper);
//		}
//		return resultList;
	}
	
	/**
	 * add by paulnpyiu
	 * 2015-10-06
	 * add search filter for status and cert status
	 */
	
	public ByteArrayOutputStream downloadRetentionReleaseScheduleReportFile(String jobNumber, String division, String company, String jobNo, String status, String certStatus, String jasperReportName,String fileType) throws Exception {
		List<RetentionReleaseScheduleEnquiryWindowsWrapper> wrapperList = obtainRetentionReleaseSchedule(division,company,jobNo,status,certStatus);

		String fileFullPath = jasperConfig.getTemplatePath() + jasperReportName;
		Date asOfDate = new Date();
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		parameters.put("AS_OF_DATE", asOfDate);
		parameters.put("JOB_NO", jobNumber);
		parameters.put("JOB_DESC", job.getDescription());
		parameters.put("REPORT_TYPE", fileType);
		parameters.put("SEARCH_DIVISION", division);
		parameters.put("SEARCH_COMPANY", company);
		parameters.put("SEARCH_JOBNUMBER", jobNo);
		parameters.put("SEARCH_STATUS", status);
		parameters.put("SEARCH_CERTSTATUS", certStatus);

		if (wrapperList.size() == 0) {
			RetentionReleaseScheduleEnquiryWindowsWrapper wrapper = new RetentionReleaseScheduleEnquiryWindowsWrapper();
			wrapperList.add(wrapper);
		}
		if(fileType.contentEquals(JasperReportHelper.OUTPUT_PDF))
			return JasperReportHelper.get().setCurrentReport(wrapperList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
		else
			return JasperReportHelper.get().setCurrentReport(wrapperList, fileFullPath, parameters).compileAndAddReport().exportAsExcel();
	}
	
	/**
	 * add by paulyiu 20150901
	 */
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(
			String division, String company, String jobNo) throws DatabaseOperationException {
		cachedRetentionReleaseList = obtainRetentionReleaseSchedule(division, company, jobNo);
		return obtainRetentionReleaseScheduleForPaginationWrapperByPage(0);
	}
	
	/**
	 * return pagination wrapper of retention release schedule
	 * @since Sep 03 2015
	 */

	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapperByPage(int pageNo) throws DatabaseOperationException{
		RetentionReleaseSchedulePaginationWrapper wrapper = new RetentionReleaseSchedulePaginationWrapper();
		double sumProjectedValues = 0.0;
		double sumEstimatedRetentions = 0.0;
		double sumCumulativeReceivables = 0.0;
		double sumReceiptAmounts = 0.0;
		double sumOutstatndingReceivables = 0.0;
		
		wrapper.setCurrentPage(pageNo);
		wrapper.setTotalRecords(cachedRetentionReleaseList.size());
		wrapper.setTotalPage(((wrapper.getTotalRecords()-1)/RR_PAGE_SIZE)+1);
		int fromInd =  (pageNo) * RR_PAGE_SIZE;
		if (fromInd<0){
			fromInd = 0;
			wrapper.setCurrentPage(0);
		}
		int toInd = fromInd+ RR_PAGE_SIZE;
		if(toInd > cachedRetentionReleaseList.size())
			toInd = cachedRetentionReleaseList.size();
		wrapper.setCurrentPageContentList(new ArrayList<RetentionReleaseScheduleEnquiryWindowsWrapper>());
		wrapper.getCurrentPageContentList().addAll(cachedRetentionReleaseList.subList(fromInd, toInd));
		
		for (RetentionReleaseScheduleEnquiryWindowsWrapper w : cachedRetentionReleaseList) {
			sumProjectedValues += w.getProjectedContractValue()!=null?w.getProjectedContractValue():0;
			sumEstimatedRetentions += w.getEstimatedRetention()!=null?w.getEstimatedRetention():0;
			sumCumulativeReceivables += w.getCumRetentionRec()!=null?w.getCumRetentionRec():0;
			sumReceiptAmounts += w.getReceiptAmt()!=null?w.getReceiptAmt():0;
			sumOutstatndingReceivables += w.getOutstandingAmt()!=null?w.getOutstandingAmt():0;
		}
		wrapper.setSumProjectedValues(sumProjectedValues);;
		wrapper.setSumEstimatedRetentions(sumEstimatedRetentions);
		wrapper.setSumCumulativeReceivables(sumCumulativeReceivables);
		wrapper.setSumReceiptAmounts(sumReceiptAmounts);
		wrapper.setSumOutstatndingReceivables(sumOutstatndingReceivables);
		
		return wrapper;
	}
	
	/**
	 * Cached list (For App Server Clustering)
	 * @author peterchan 
	 * Date: Jun 21, 2011
	 * @return
	 */
	public List<RetentionReleaseScheduleEnquiryWindowsWrapper> getCachedRetentionReleaseList() {
		return cachedRetentionReleaseList;
	}
	/**
	 * Cached list (For App Server Clustering)
	 * @author peterchan 
	 * Date: Jun 21, 2011
	 * @param cachedRetentionReleaseList
	 */
	public void setCachedRetentionReleaseList(
			List<RetentionReleaseScheduleEnquiryWindowsWrapper> cachedRetentionReleaseList) {
		this.cachedRetentionReleaseList = cachedRetentionReleaseList;
	}
	
}
