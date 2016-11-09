package com.gammon.pcms.scheduler.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.SubcontractSnapshotHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.service.admin.MailContentGenerator;
import com.gammon.qs.shared.util.CalculationUtil;
@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class PackageSnapshotGenerationService {
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;
	@Autowired
	private SubcontractSnapshotHBDao scPackageSnapshotDao;
	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private SubcontractHBDao scPackageHBDao;
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;
	
	/**
	 * @author koeyyeung
	 * created on 26 Aug, 2014
	 * **/
	//duplicate all the records in SUBCONTRACT to SUBCONTRACT_SNAPSHOT on 26th of every month at 7:30am
	public void generateSCPackageSnapshot(){
		logger.info("------------------------generateSCPackageSnapshot(START)----------------------------");
		String content="";
		Date startTime = new Date();
		
		List<JobInfo> jobList;
		try {
			jobList = jobHBDao.getAllActive();
			for (JobInfo job : jobList)
				calculateTotalWDAmountByJob(job.getJobNumber());

		} catch (DataAccessException e) {
			String exceptionMessage = e.getMessage();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Generate SCPackage Snapshot [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER - Generate SCPackage Snapshot.");
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
		
		
		try {
			content = mailContentGenerator.obtainEmailContentForScheduleJob(new Date(), null, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Generate SCPackage Snapshot [STARTED]", content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			scPackageSnapshotDao.callStoredProcedureToGenerateSnapshot();
		} catch (Exception e) {
			String exceptionMessage = e.getMessage(); 
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Generate SCPackage Snapshot [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER - Generate SCPackage Snapshot.");
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}

		try {
			Date endTime = new Date();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Generate SCPackage Snapshot [ENDED]", content);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		logger.info("-----------------------generateSCPackageSnapshot(END)------------------------------");
	}
	
	/**
	 * Calculate the summary information (Total Cumulative Work Done Amount & Total Posted Work Done Amount) by job
	 *
	 * @param jobNumber
	 * @return
	 * @author	tikywong
	 * @since	Mar 30, 2016 3:55:10 PM
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	public Boolean calculateTotalWDAmountByJob(String jobNumber) {
		logger.info("STARTED -> recalculateTotalWDAmountByJob()");
		try {
			List<Subcontract> scPackages = scPackageHBDao.obtainPackageList(jobNumber);

			// calculate the total work done amount for each subcontract of the specified job
			for (Subcontract scPackage : scPackages) {
				if (scPackage.isAwarded()) {
					List<SubcontractDetail> scDetails = scDetailsHBDao.getSCDetails(scPackage);
					double totalCumWorkDoneAmount = 0.00;
					double totalPostedWorkDoneAmount = 0.00;

					for (SubcontractDetail scDetail : scDetails) {
						if (BasePersistedAuditObject.ACTIVE.equals(scDetail.getSystemStatus())) {
							boolean toBeUpdated = !"C1".equals(scDetail.getLineType()) && !"C2".equals(scDetail.getLineType()) && !"MS".equals(scDetail.getLineType()) && !"RR".equals(scDetail.getLineType()) && !"RA".equals(scDetail.getLineType());

							if (toBeUpdated && scDetail.getCumWorkDoneQuantity() != null && scDetail.getPostedWorkDoneQuantity() != null && scDetail.getScRate() != null) {
								double cumWorkDoneAmount = CalculationUtil.round(scDetail.getCumWorkDoneQuantity() * scDetail.getScRate(), 2);
								double postedWorkDoneAmount = CalculationUtil.round(scDetail.getPostedWorkDoneQuantity() * scDetail.getScRate(), 2);

								totalCumWorkDoneAmount += cumWorkDoneAmount;
								totalPostedWorkDoneAmount += postedWorkDoneAmount;
							}
						}
					}
					scPackage.setTotalCumWorkDoneAmount(new BigDecimal(totalCumWorkDoneAmount));
					scPackage.setTotalPostedWorkDoneAmount(new BigDecimal(totalPostedWorkDoneAmount));

					scPackageHBDao.update(scPackage);
				}
			}

			// Hibernate keep writing these records into the database instead of caching them in the memory
			scPackageHBDao.getSession().flush();
			scPackageHBDao.getSession().clear();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
