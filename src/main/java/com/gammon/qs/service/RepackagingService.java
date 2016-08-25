package com.gammon.qs.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Repackaging;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class RepackagingService {
	
	private Logger logger = Logger.getLogger(RepackagingService.class.getName());
	@Autowired
	private RepackagingHBDao repackagingDao;
	@Autowired
	private JobInfoHBDao jobInfoDao;
	@Autowired
	private RepackagingDetailService repackagingDetailService;
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	

	public List<Repackaging> getRepackagingEntriesByJob(JobInfo job)
			throws Exception {
		return repackagingDao.getRepackagingEntriesByJob(job);
	}

	
	
	public Repackaging getRepackagingEntry(Long id) throws Exception{
		return repackagingDao.get(id);
	}
	
	public Repackaging getLatestRepackaging(JobInfo job) throws Exception{
		return repackagingDao.getLatestRepackaging(job);
	}

	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public Repackaging getLatestRepackaging(String jobNo) throws Exception {
		return repackagingDao.getLatestRepackaging(jobNo);
	}
	
	public List<Repackaging> getRepackagingListByJobNo(String jobNo) throws Exception {
		return repackagingDao.getRepackagingListByJobNo(jobNo);
	}
	
	public List<BigDecimal> getRepackagingMonthlySummary(String jobNo, String year) throws Exception {
		
		List<Repackaging> repackagingList = repackagingDao.getRepackagingMonthlySummary(jobNo, year);
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();

		if (repackagingList != null && repackagingList.size()>0){
			while(dataList.size()<12){
				dataList.add(new BigDecimal(repackagingList.get(repackagingList.size()-1).getTotalResourceAllowance()));
			}
		}else{
			while(dataList.size()<12){
				dataList.add(new BigDecimal(0));
			}
		}

		for(Repackaging repackaging: repackagingList){
			if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==1){
				dataList.set(0, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==2){
				dataList.set(1, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==3){
				dataList.set(2, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==4){
				dataList.set(3, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==5){
				dataList.set(4, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==6){
				dataList.set(5, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==7){
				dataList.set(6, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==8){
				dataList.set(7, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==9){
				dataList.set(8, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==10){
				dataList.set(9, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==11){
				dataList.set(10, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}
			else if(Integer.valueOf(DateHelper.formatDate(repackaging.getCreateDate(), "MM"))==12){
				dataList.set(11, new BigDecimal(repackaging.getTotalResourceAllowance()).setScale(2, RoundingMode.HALF_UP));
			}

		}
		
		return dataList;
	}
	
	//Create and save a repackaging entry
	public String addRepackaging(String jobNo) throws Exception{
		String result = "";
		Repackaging currentRepackaging = this.getLatestRepackaging(jobNo);
		
		logger.info("currentRepackaging: "+currentRepackaging);
		logger.info("version: "+currentRepackaging.getRepackagingVersion());
		logger.info("currentRepackaging: "+currentRepackaging.getStatus());
		
		if(currentRepackaging != null && currentRepackaging.getRepackagingVersion() != null &&  "900".equals(currentRepackaging.getStatus())){
			logger.info("Create new repackage");
			Repackaging newEntry = new Repackaging();
			newEntry.setRepackagingVersion(currentRepackaging.getRepackagingVersion()+1);
			newEntry.setJobInfo(jobInfoDao.obtainJobInfo(jobNo));
			newEntry.setCreateDate(new Date());
			newEntry.setStatus("100");
			repackagingDao.saveOrUpdate(newEntry);
		}
		else{
			result = "Repackaging has been unlocked already.";
		}
		return result;
	}
	
	
	
	public String updateRepackaging(Repackaging repackaging) throws Exception{
		String result = "";
		logger.info(repackaging.getRemarks());
		Repackaging entryInDB = repackagingDao.get(repackaging.getId());
		if (entryInDB.getStatus()!=null && "900".equals(entryInDB.getStatus().trim())){
			result = "Repackaging status is 900. Job "+entryInDB.getJobInfo().getJobNumber()+" is locked already.";
			//throw new ValidateBusinessLogicException("Repackaging status is 900. Job "+entryInDB.getJobInfo().getJobNumber()+" is locked already.");
		}

		entryInDB.setStatus(repackaging.getStatus());
		entryInDB.setRemarks(repackaging.getRemarks());
		if(repackaging.getRemarks().length()>255)
			entryInDB.setRemarks(repackaging.getRemarks().substring(0, 255));

		repackagingDao.saveOrUpdate(entryInDB);
		return result;
	}
	
	/**
	 * @author tikywong
	 * created on January 05, 2012
	 */
	public String deleteRepackaging(Long id) throws Exception {
		String result = "";
		Repackaging entryInDB  =repackagingDao.get(id);
		
		if(entryInDB==null){
			result = "Repackaging Entry doesn't exist in the database.";
			//throw new ValidateBusinessLogicException("Repackaging Entry doesn't exist in the database.");
		}
		
		//Validation
		if(entryInDB.getStatus()==null || !entryInDB.getStatus().equals("100")){
			result = "Repackaging Status is not 100. Entry cannot be deleted.";
			//throw new ValidateBusinessLogicException("Repackaging Status is not 100. Entry cannot be deleted.");
		}
		repackagingDao.deleteById(entryInDB.getId());
		
		return result;
	}
	
	/**
	 * @author koeyyeung
	 * created on June 29, 2016
	 */
	public String generateSnapshot(Long id, String jobNo) throws Exception {
		String result = "";
		
		JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
		Repackaging repackagingEntry = repackagingDao.get(id);
		
		if(job.getRepackagingType().equals("1")){
			repackagingDetailService.prepareRepackagingDetails(repackagingEntry);
		}else if(job.getRepackagingType().equals("2")){
			boolean updated = resourceSummaryService.generateSnapshotMethodTwo(job, repackagingEntry);
			if(updated)
				repackagingDetailService.prepareRepackagingDetails(repackagingEntry);
			else
				result = "Snapshot cannot be generated.";
		}else if(job.getRepackagingType().equals("3")){
			boolean updated = resourceSummaryService.generateSnapshotMethodThree(job, repackagingEntry);
			if(updated)
				repackagingDetailService.prepareRepackagingDetails(repackagingEntry);
			else
				result = "Snapshot cannot be generated.";
		}
		
		
		return result;
	}



	public String confirmAndPostRepackaingDetails(Long repackagingID) {
		String error = "";
		boolean posted = false;
		try {
			Repackaging repackaging = repackagingDao.getRepackagingEntryWithJob(repackagingID);
			if("900".equals(repackaging.getStatus()))
				error = "This repackaging has already been confirmed";
			else if("200".equals(repackaging.getStatus()))
				error = "New repackaging update has been made - please regenerate the snapshot before doing confirm.";
			else{
				posted = repackagingDetailService.postRepackagingDetails(repackaging);
				if(!posted)
					error = "Repackaging cannot be confimred.";
				repackaging.setStatus("900");
				repackagingDao.update(repackaging);
			}
		} catch (Exception e) {
			error = "Repackaging cannot be confirmed and posted.";
			e.printStackTrace();
		}
		return error;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
