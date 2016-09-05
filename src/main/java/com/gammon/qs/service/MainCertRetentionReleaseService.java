package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.MainCertRetentionReleaseHBDao;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.shared.util.CalculationUtil;

@Service
@Transactional(	rollbackFor = Exception.class,
				value = "transactionManager")
public class MainCertRetentionReleaseService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserAccessRightsService userAccessRightsService;
	@Autowired
	private MainCertService mainCertService;

	@Autowired
	private MainCertRetentionReleaseHBDao retentionReleaseHBDao;
	@Autowired
	private MainCertHBDao mainCertHBDao;

	/**
	 * @author koeyyeung modified on 18 Dec, 2013
	 **/
	public PCMSDTO getCumulativeRetentionReleaseByJob(String jobNo, Integer mainCertNumber) throws DataAccessException {
		String errorMessage = "";

		List<MainCertRetentionRelease> rrList = retentionReleaseHBDao.findByJobNo(jobNo);
		MainCert mainCert = mainCertService.getCertificate(jobNo, mainCertNumber);
		Double cumRRCalculatedByMainCert = mainCert.getCertifiedMainContractorRetentionReleased() + mainCert.getCertifiedRetentionforNSCNDSCReleased() + mainCert.getCertifiedMOSRetentionReleased();
		Double cumRetentionRelease = 0.0;
		Double cumActualRetentionRelease = 0.0;

		if (rrList != null && rrList.size() > 0)
			for (MainCertRetentionRelease rr : rrList) {
				if (MainCertRetentionRelease.STATUS_ACTUAL.equals(rr.getStatus())) {
					cumActualRetentionRelease += rr.getActualReleaseAmt();
					cumRetentionRelease += rr.getActualReleaseAmt();
				} else
					cumRetentionRelease += rr.getForecastReleaseAmt();
			}
		cumRetentionRelease = CalculationUtil.round(cumRetentionRelease, 2);

		Double rrDiff = CalculationUtil.round(cumRetentionRelease - mainCert.getCertifiedMOSRetention() - mainCert.getCertifiedMainContractorRetention() - mainCert.getCertifiedRetentionforNSCNDSC(), 2);
		Double actualRRDiff = CalculationUtil.round(cumActualRetentionRelease - cumRRCalculatedByMainCert, 2);

		if (rrDiff == 0.00 && actualRRDiff == 0.00) {
			logger.info("Retention Release is balanced.");
		} else {
			if (rrDiff != 0.00)
				errorMessage = "Total Cumulative Retention Release is not balanced.";
			else if (actualRRDiff != 0.0)
				errorMessage = "Actual Cumulative Retention Release is not balanced.";
			logger.info(errorMessage);
			logger.info("Total Retention Release Diff.: " + rrDiff);
			logger.info("Actual Retention Release Diff.: " + actualRRDiff + "- cumActualRetentionRelease: " + cumActualRetentionRelease + "- cumRRCalculatedByMainCert: " + cumRRCalculatedByMainCert);
		}

		return new PCMSDTO(StringUtils.isEmpty(errorMessage) ? PCMSDTO.Status.SUCCESS : PCMSDTO.Status.FAIL, errorMessage);
	}
	

	/**
	 * @author koeyyeung modified on 18 Dec, 2013
	 **/
	public List<MainCertRetentionRelease> getCalculatedRetentionRelease(String noJob, Integer noMainCert) {
		logger.info("STARTED - calculateRetentionReleaseScheduleByJob");
		MainCert mainCert;
		try {
			mainCert = mainCertService.getCertificate(noJob, noMainCert);
		} catch (DataAccessException e1) {
			e1.printStackTrace();
			return new ArrayList<MainCertRetentionRelease>();
		}

		try {
			String username = securityService.getCurrentUser().getUsername();
			List<String> securityList = userAccessRightsService.getAccessRights(username, RoleSecurityFunctions.F010405_RETENTION_RELEASE_WINDOW);

			if (securityList.contains("WRITE")) {
				List<MainCert> mainCertList = mainCertHBDao.getMainCertList(noJob);
				ArrayList<MainCertRetentionRelease> newRRSList = new ArrayList<MainCertRetentionRelease>();
				MainCert prevMainCert = null;

				if (mainCertList != null && mainCertList.size() > 0)
					for (int i = 0; i < mainCertList.size(); i++) {// Calculate all certs
						if (mainCertList.get(i).getCertificateNumber().equals(mainCert.getCertificateNumber() - 1))
							prevMainCert = mainCertList.get(i);

						Double retentionRelease = 0.0;
						retentionRelease += mainCertList.get(i).getCertifiedMainContractorRetentionReleased() == null ? 0 : mainCertList.get(i).getCertifiedMainContractorRetentionReleased();
						retentionRelease += mainCertList.get(i).getCertifiedRetentionforNSCNDSCReleased() == null ? 0 : mainCertList.get(i).getCertifiedRetentionforNSCNDSCReleased();
						retentionRelease += mainCertList.get(i).getCertifiedMOSRetentionReleased() == null ? 0 : mainCertList.get(i).getCertifiedMOSRetentionReleased();
						if (i > 0) {
							retentionRelease = retentionRelease - (mainCertList.get(i - 1).getCertifiedMainContractorRetentionReleased() == null ? 0 : mainCertList.get(i - 1).getCertifiedMainContractorRetentionReleased());
							retentionRelease = retentionRelease - (mainCertList.get(i - 1).getCertifiedRetentionforNSCNDSCReleased() == null ? 0 : mainCertList.get(i - 1).getCertifiedRetentionforNSCNDSCReleased());
							retentionRelease = retentionRelease - (mainCertList.get(i - 1).getCertifiedMOSRetentionReleased() == null ? 0 : mainCertList.get(i - 1).getCertifiedMOSRetentionReleased());
						}
						// logger.info("CertificateNumber: "+mainCertList.get(i).getCertificateNumber()+" - retentionRelease: "+retentionRelease);
						if (CalculationUtil.round(retentionRelease, 2) != 0.00) {// Insert rr if not exist in db
							MainCertRetentionRelease dbRR = retentionReleaseHBDao.obtainActualRetentionReleaseByMainCertNo(noJob, mainCertList.get(i).getCertificateNumber());
							if (dbRR == null) {
								MainCertRetentionRelease newRR = new MainCertRetentionRelease();
								newRR.setJobNo(noJob);
								newRR.setMainCertNo(mainCertList.get(i).getCertificateNumber());

								newRR.setDueDate(mainCertList.get(i).getCertDueDate());
								newRR.setActualReleaseAmt(CalculationUtil.round(retentionRelease, 2));
								newRR.setForecastReleaseAmt(new Double(0.0));
								newRR.setStatus(MainCertRetentionRelease.STATUS_ACTUAL);
								// newRR.setReleasePercent(RoundingUtil.round(retentionRelease*100.0/totalRetentionAmount,2));//@deprecated
								newRR.setSequenceNo(0);// @deprecated
								newRRSList.add(newRR);
							}
						}
					}
				updateActualRetentionRelease(noJob, mainCert, prevMainCert, newRRSList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed: error occured in calculating the retention release.");
			return new ArrayList<MainCertRetentionRelease>();
		}

		logger.info("END - calculateRetentionReleaseScheduleByJob");
		return retentionReleaseHBDao.findByJobNo(noJob);
	}
	
	/**
	 * @author koeyyeung modified on 18 Dec, 2013
	 **/
	private void updateActualRetentionRelease(String jobNumber, MainCert mainCert, MainCert prevMainCert, List<MainCertRetentionRelease> newRRSList) throws DatabaseOperationException {
		logger.info("STARTED - generateActualRetentionRelease");

		double rrDiff = CalculationUtil.round(calculateRetentionReleaseAmount(mainCert) - calculateRetentionReleaseAmount(prevMainCert), 2);
		MainCertRetentionRelease rr = retentionReleaseHBDao.obtainActualRetentionReleaseByMainCertNo(jobNumber, mainCert.getCertificateNumber());

		if (rr != null && (mainCert.getCertificateNumber().equals(rr.getMainCertNo()))) {
			double dbRRDiff = CalculationUtil.round(rrDiff - rr.getActualReleaseAmt(), 2);
			// update the record if the amount changed (the last cert, staus < 300)
			if (rrDiff != 0.00 && dbRRDiff != 0.00) {
				rr.setActualReleaseAmt(rrDiff);
				rr.setStatus(MainCertRetentionRelease.STATUS_ACTUAL);
				rr.setDueDate(mainCert.getCertDueDate());
				rr.setMainCertNo(mainCert.getCertificateNumber());

				retentionReleaseHBDao.update(rr);
				logger.info("UPDATE Retention Release: Main Cert Number: " + rr.getMainCertNo() + " - Actual Release Amount: " + rr.getActualReleaseAmt());
			}
			// remove it if retention release is zero
			else if (rrDiff == 0.00) {
				retentionReleaseHBDao.inactivate(rr);
				logger.info("INACTIVE Retention Release: Main Cert Number: " + rr.getMainCertNo());
			}
		}

		for (MainCertRetentionRelease rrToInsert : newRRSList) {
			retentionReleaseHBDao.insert(rrToInsert);
			logger.info("INSERT Retention Release: Main Cert Number: " + rrToInsert.getMainCertNo() + " - Actual Release Amount: " + rrToInsert.getActualReleaseAmt());
		}

		logger.info("END - generateActualRetentionRelease");
	}

	private double calculateRetentionReleaseAmount(MainCert mainCert) {
		if (mainCert == null)
			return 0.0;
		return 	(mainCert.getCertifiedRetentionforNSCNDSCReleased() == null ? 0 : mainCert.getCertifiedRetentionforNSCNDSCReleased().doubleValue()) + 
				(mainCert.getCertifiedMainContractorRetentionReleased() == null ? 0 :mainCert.getCertifiedMainContractorRetentionReleased().doubleValue()) + 
				(mainCert.getCertifiedMOSRetentionReleased() == null ? 0 : mainCert.getCertifiedMOSRetentionReleased().doubleValue());
	}
	

	/*************************************** FUNCTIONS FOR PCMS - START **************************************************************/
	@Transactional(readOnly = true)
	public List<MainCertRetentionRelease> getRetentionReleaseList(String noJob) {
		try {
			return retentionReleaseHBDao.findByJobNo(noJob);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return new ArrayList<MainCertRetentionRelease>();
		}
	}
	
	public PCMSDTO updateRetentionRelease(String noJob, List<MainCertRetentionRelease> modifiedList) {
		logger.info("STARTED - updateForecastRetentionRelease");
		Boolean updated = false;
		List<MainCertRetentionRelease> rrToUpdateList = new ArrayList<MainCertRetentionRelease>();
		List<MainCertRetentionRelease> rrToInsertList = new ArrayList<MainCertRetentionRelease>();

		for (MainCertRetentionRelease rr : modifiedList)
			rrToInsertList.add(rr);

		List<MainCertRetentionRelease> dbList = retentionReleaseHBDao.findByJobNo(noJob);

		for (MainCertRetentionRelease dbRR : dbList) {
			for (int i = 0; i < modifiedList.size(); i++) {
				if (modifiedList.get(i).getId().equals(dbRR.getId())) {
					if ("F".equals(modifiedList.get(i).getStatus())) {
						if (CalculationUtil.round(modifiedList.get(i).getForecastReleaseAmt() - dbRR.getForecastReleaseAmt(), 2) != 0.00 || modifiedList.get(i).getContractualDueDate() != dbRR.getContractualDueDate() || modifiedList.get(i).getDueDate() != dbRR.getDueDate())
							rrToUpdateList.add(modifiedList.get(i));
					}
					rrToInsertList.remove(modifiedList.get(i));
					break;
				}
				if (i == modifiedList.size() - 1) {
					logger.info("INACTIVE-retention release id:" + dbRR.getId());
					retentionReleaseHBDao.inactivate(dbRR);
					updated = true;
				}
			}
			if (modifiedList.size() == 0) {
				logger.info("INACTIVE-retention release id:" + dbRR.getId());
				retentionReleaseHBDao.inactivate(dbRR);
				updated = true;
			}
		}

		for (MainCertRetentionRelease rrToUpdate : rrToUpdateList) {// update records
			logger.info("Update retention release id:" + rrToUpdate.getId());
			retentionReleaseHBDao.merge(rrToUpdate);
			updated = true;
		}

		for (MainCertRetentionRelease rrToInsert : rrToInsertList) {// insert new records
			logger.info("Insert retention release forecast amount:" + rrToInsert.getForecastReleaseAmt());
			rrToInsert.setSequenceNo(0);
			retentionReleaseHBDao.insert(rrToInsert);
			updated = true;
		}

		logger.info("END - updateForecastRetentionRelease");
		return new PCMSDTO(updated ? PCMSDTO.Status.SUCCESS : PCMSDTO.Status.FAIL, "");
	}

	/*************************************** FUNCTIONS FOR PCMS - END **************************************************************/
}
