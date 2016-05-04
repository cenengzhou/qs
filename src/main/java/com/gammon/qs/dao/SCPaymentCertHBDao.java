package com.gammon.qs.dao;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.domain.poweruserAdmin.SCPaymentCertControl;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWrapper;
@Repository
public class SCPaymentCertHBDao extends BaseHibernateDao<SCPaymentCert> {
	private Logger logger = Logger.getLogger(SCPaymentCertHBDao.class.getName());
	@Autowired
	private SCPackageHBDao scPackageHBDao;
	@Autowired
	private SCPaymentDetailHBDao scPaymentDetailHBDao;

	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public boolean addSCPaymentCert(SCPaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new NullPointerException();
		try {		
			if (getSCPaymentCert(scPaymentCert)==null){
				scPaymentCert.setScPackage(
						scPackageHBDao.obtainSCPackage(	
								scPaymentCert.getScPackage().getJob().getJobNumber().trim(),
								scPaymentCert.getScPackage().getPackageNo()
						)
				);
				this.saveOrUpdate(scPaymentCert);
			}else
				logger.info("SCPayment Cert Existed");
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			throw new DatabaseOperationException(e); 
		}

		return false;
	}

	public SCPaymentCert obtainPaymentCertificate(String jobNumber, String packageNo,Integer paymentCertNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		if (paymentCertNo==null)
			throw new DatabaseOperationException("paymentCertNo is null");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("paymentCertNo", paymentCertNo));
			criteria.createAlias("scPackage.job","job" );
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim() ));
			criteria.createAlias("scPackage","scPackage" );
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			return (SCPaymentCert) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(String jobNumber, String packageNo,String paymentCertNo)");
			throw new DatabaseOperationException(he);
		}
	
	}

	
	public SCPaymentCert getSCPaymentCert(SCPaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try{
			return obtainPaymentCertificate(scPaymentCert.getScPackage().getJob().getJobNumber(),
									scPaymentCert.getScPackage().getPackageNo(),
									scPaymentCert.getPaymentCertNo());
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(SCPaymentCert scPaymentCert) ");
			throw new DatabaseOperationException(he);
		}
	}
	public boolean updateSCPaymentCert(SCPaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
			SCPaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
			if (scPaymentCertDB==null)
				throw new DatabaseOperationException("Record of SCPaymentCert was not exist");
			scPaymentCertDB.setPaymentStatus(scPaymentCert.getPaymentStatus());
			scPaymentCertDB.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());
			scPaymentCertDB.setDueDate(scPaymentCert.getDueDate());
			scPaymentCertDB.setAsAtDate(scPaymentCert.getAsAtDate());
			scPaymentCertDB.setIpaOrInvoiceReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
			scPaymentCertDB.setCertIssueDate(scPaymentCert.getCertIssueDate());
			scPaymentCertDB.setCertAmount(scPaymentCert.getCertAmount());
//			scPaymentCertDB.setGstPayable(scPaymentCert.getGstPayable());
//			scPaymentCertDB.setGstReceivable(scPaymentCert.getGstReceivable());
			scPaymentCertDB.setLastModifiedUser(scPaymentCert.getLastModifiedUser());
			saveOrUpdate(scPaymentCertDB);
			return true;
		}catch (HibernateException he){
			logger.info("Fail: updateSCPaymentCert(SCPaymentCert scPaymentCert)");
			throw new DatabaseOperationException(he);
		}
	}
	public boolean saveOrUpdateSCPaymentCert(SCPaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
			SCPaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
			if (scPaymentCertDB==null){		
//				scPaymentCert.setCreatedDate(new Date());
				//scPaymentCert.setCreatedUser(scPaymentCert.getLastModifiedUser());
				
				insert(scPaymentCert);
				return true;
			}
			scPaymentCertDB.setPaymentStatus(scPaymentCert.getPaymentStatus());
			scPaymentCertDB.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());
			scPaymentCertDB.setDueDate(scPaymentCert.getDueDate());
			scPaymentCertDB.setAsAtDate(scPaymentCert.getAsAtDate());
			scPaymentCertDB.setIpaOrInvoiceReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
			scPaymentCertDB.setCertIssueDate(scPaymentCert.getCertIssueDate());
			scPaymentCertDB.setCertAmount(scPaymentCert.getCertAmount());
			scPaymentCertDB.setIntermFinalPayment(scPaymentCert.getIntermFinalPayment());
			scPaymentCertDB.setAddendumAmount(scPaymentCert.getAddendumAmount());
			scPaymentCertDB.setRemeasureContractSum(scPaymentCert.getRemeasureContractSum());
//			scPaymentCertDB.setGstPayable(scPaymentCert.getGstPayable());
//			scPaymentCertDB.setGstReceivable(scPaymentCert.getGstReceivable());
			if (scPaymentCertDB.getCreatedDate()==null)
				scPaymentCertDB.setCreatedDate(new Date());
			scPaymentCertDB.setLastModifiedUser(scPaymentCert.getLastModifiedUser());
			List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(scPaymentCertDB);
			for(SCPaymentDetail scPaymentDetail : scPaymentDetailList){
				scPaymentDetail.setScPaymentCert(scPaymentCertDB);
				scPaymentDetailHBDao.saveOrUpdate(scPaymentDetail);
			}
			
			saveOrUpdate(scPaymentCertDB);
			return true;
		}catch (HibernateException he){
			logger.info("Fail: updateSCPaymentCert(SCPaymentCert scPaymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCPaymentCert> obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage.job","job" );
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim() ));
			criteria.createAlias("scPackage","scPackage" );
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			if(status!=null)
				criteria.add(Restrictions.eq("paymentStatus",status));
			if(directPayment!=null)
				criteria.add(Restrictions.eq("directPayment",directPayment));
			criteria.addOrder(Order.desc("paymentCertNo"));
			return (List<SCPaymentCert>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage.job","job" );
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim() ));
			criteria.createAlias("scPackage","scPackage" );
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			return (List<SCPaymentCert>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(String jobNumber, String packageNo,String paymentCertNo");
			throw new DatabaseOperationException(he);
		}
	}


	@SuppressWarnings("unchecked")
	public SCPaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");

		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage.job","job" );
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim() ));
			criteria.createAlias("scPackage","scPackage" );
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			criteria.setFetchMode("scPackage", FetchMode.JOIN);
			List<SCPaymentCert> resultList = criteria.list();
			if (resultList!=null && !resultList.isEmpty())
				return resultList.get(0);
			return null;
		}catch (HibernateException he){
			logger.info("Fail: obtainPaymentLatestCert(String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public SCPaymentCert getSCPaymentLatestCert(Job job, String packageNo) throws DatabaseOperationException{
		if (job==null)
			throw new DatabaseOperationException("Job is null");

		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPackage.job", job ));
			criteria.createAlias("scPackage","scPackage" );
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			criteria.setFetchMode("scPackage", FetchMode.JOIN);
			List<SCPaymentCert> resultList = criteria.list();
			if (resultList!=null && !resultList.isEmpty())
				return resultList.get(0);
			return null;
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentLatestCert(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	/**
	 * 
	 * To obtain a payment certificate with a specific payment status
	 * @author tikywong
	 * created on Feb 21, 2014 12:10:08 PM
	 */
	public SCPaymentCert obtainPaymentCert(String jobNumber, String packageNo, String paymentStatus) throws DatabaseOperationException{
		if (jobNumber==null || jobNumber.trim().equals(""))
			throw new DatabaseOperationException("jobnumber is null/empty");
		else
			jobNumber = jobNumber.trim();
		if (packageNo==null || packageNo.trim().equals(""))
			throw new DatabaseOperationException("packageNo is null/empty");
		else
			packageNo = packageNo.trim();
		if (paymentStatus==null || paymentStatus.trim().equals(""))
			throw new DatabaseOperationException("paymentStatus is null/empty");
		else
			paymentStatus = paymentStatus.trim();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
			
			return (SCPaymentCert) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: obtainPaymentCert(String jobNumber, String packageNo, String paymentStatus)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPaymentCert> getSCPaymentCertsPCS() throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("paymentStatus", "PCS"));
			List<SCPaymentCert> paymentCerts = criteria.list();
			for(SCPaymentCert paymentCert : paymentCerts){
				Hibernate.initialize(paymentCert.getScPackage());
				Hibernate.initialize(paymentCert.getScPackage().getJob());
			}
			return paymentCerts;
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCertsPCS()");
			throw new DatabaseOperationException(he);
		}
	}
		
	public List<SCPaymentDetail> obtainPaymentDetailList(String jobNumber, String packageNo,Integer paymentCertNo) throws DatabaseOperationException{
		return scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo));
	}

	public boolean updateSCPaymentCertAdmin(SCPaymentCert scPaymentCert) throws DatabaseOperationException {
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
			SCPaymentCertControl paymentControl = new SCPaymentCertControl();
			SCPaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
			if (scPaymentCertDB==null){
				throw new DatabaseOperationException("SC Payment Cert "+scPaymentCert.getId()+" is not existed.");
			}

			paymentControl.setBeforeValues(scPaymentCertDB);
			paymentControl.setAfterValues(scPaymentCert);
			paymentControl.setActionType("UPDATE");

			scPaymentCertDB.setPaymentStatus(scPaymentCert.getPaymentStatus());
			scPaymentCertDB.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());
			scPaymentCertDB.setDueDate(scPaymentCert.getDueDate());
			scPaymentCertDB.setAsAtDate(scPaymentCert.getAsAtDate());
			scPaymentCertDB.setIpaOrInvoiceReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
			scPaymentCertDB.setCertIssueDate(scPaymentCert.getCertIssueDate());
			scPaymentCertDB.setCertAmount(scPaymentCert.getCertAmount());
			scPaymentCertDB.setIntermFinalPayment(scPaymentCert.getIntermFinalPayment());
			scPaymentCertDB.setAddendumAmount(scPaymentCert.getAddendumAmount());
			scPaymentCertDB.setRemeasureContractSum(scPaymentCert.getRemeasureContractSum());
			scPaymentCertDB.setDirectPayment(scPaymentCert.getDirectPayment());
			if (scPaymentCertDB.getCreatedDate()==null)
				scPaymentCertDB.setCreatedDate(new Date());
			scPaymentCertDB.setLastModifiedUser(scPaymentCert.getLastModifiedUser());
			saveOrUpdate(scPaymentCertDB);
			this.getSessionFactory().getCurrentSession().save(paymentControl);
			return true;
		}catch (HibernateException he){
			logger.info("Fail: updateSCPaymentCert(SCPaymentCert scPaymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCPaymentCert> obtainDirectPaymentRecords(String division,
			String company, String jobNumber, String vendorNo, String packageNo, List<Integer> scStatusList) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("scPackage1.job","job1" );
		criteria.createAlias("scPackage","scPackage1" );
		criteria.add(Restrictions.eq("directPayment", SCPaymentCert.DIRECT_PAYMENT));
		criteria.add(Restrictions.eq("paymentStatus", "APR"));
		if (scStatusList!=null && scStatusList.size()>0)
			criteria.add(Restrictions.in("scPackage1.subcontractStatus", scStatusList));
		if (packageNo!=null && packageNo.trim().length()>0)
			criteria.add(Restrictions.eq("scPackage1.packageNo", packageNo));
		if (vendorNo!=null && vendorNo.trim().length()>0)
			criteria.add(Restrictions.eq("scPackage1.vendorNo",vendorNo));
		else 
			criteria.add(Restrictions.isNotNull("scPackage1.vendorNo"));
		if (jobNumber!=null && jobNumber.trim().length()>0)
			criteria.add(Restrictions.eq("job1.jobNumber", jobNumber));
		else
			criteria.add(Restrictions.isNotNull("job1.jobNumber"));
		if (division!=null && division.trim().length()>0)
			criteria.add(Restrictions.eq("job1.division", division));
		if (company!=null && company.trim().length()>0)
			criteria.add(Restrictions.eq("job1.company", company));
		criteria.addOrder(Order.asc("job1.jobNumber"))
				.addOrder(Order.asc("scPackage1.packageNo"))
				.addOrder(Order.asc("paymentCertNo"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPaymentCert> obtainSCPaymentCertList(SCPaymentCertWrapper scPaymentCertWrapper, List<String> companyList, String dueDateType){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		
		criteria.createAlias("scPackage", "scPackage");
		criteria.createAlias("scPackage.job", "scPackageJob");
		
		if(scPaymentCertWrapper.getJobNo()!=null && !"".equals(scPaymentCertWrapper.getJobNo().trim()))
			criteria.add(Restrictions.eq("scPackageJob.jobNumber", scPaymentCertWrapper.getJobNo().trim()));
		if(scPaymentCertWrapper.getJob()!=null && scPaymentCertWrapper.getJob().getCompany()!=null && !"".equals(scPaymentCertWrapper.getJob().getCompany().trim()))
			criteria.add(Restrictions.eq("scPackageJob.company", scPaymentCertWrapper.getJob().getCompany().trim()));
		else if(companyList!=null)
			criteria.add(Restrictions.in("scPackageJob.company", companyList));
		if(scPaymentCertWrapper.getScPackage().getVendorNo()!=null && !"".equals(scPaymentCertWrapper.getScPackage().getVendorNo().trim()))
			criteria.add(Restrictions.eq("scPackage.vendorNo", scPaymentCertWrapper.getScPackage().getVendorNo().trim()));
		if(scPaymentCertWrapper.getScPackage().getPaymentTerms()!=null && !"".equals(scPaymentCertWrapper.getScPackage().getPaymentTerms().trim()))
			criteria.add(Restrictions.eq("scPackage.paymentTerms", scPaymentCertWrapper.getScPackage().getPaymentTerms().trim()));
		if(scPaymentCertWrapper.getScPackage().getPackageNo()!=null && !"".equals(scPaymentCertWrapper.getScPackage().getPackageNo().trim()))
			criteria.add(Restrictions.eq("packageNo", scPaymentCertWrapper.getScPackage().getPackageNo().trim()));
		if(scPaymentCertWrapper.getPaymentStatus()!=null && !"".equals(scPaymentCertWrapper.getPaymentStatus().trim()))
			criteria.add(Restrictions.eq("paymentStatus", scPaymentCertWrapper.getPaymentStatus().trim()));
		if(scPaymentCertWrapper.getIntermFinalPayment()!=null && !"".equals(scPaymentCertWrapper.getIntermFinalPayment().trim()))
			criteria.add(Restrictions.eq("intermFinalPayment", scPaymentCertWrapper.getIntermFinalPayment().trim()));
		if(scPaymentCertWrapper.getDirectPayment()!=null && !"".equals(scPaymentCertWrapper.getDirectPayment().trim()))
			criteria.add(Restrictions.eq("directPayment", scPaymentCertWrapper.getDirectPayment().trim()));
		if(scPaymentCertWrapper.getCertIssueDate()!=null ){
			criteria.add(Restrictions.between("certIssueDate",obtainDateStart(scPaymentCertWrapper.getCertIssueDate()),obtainDateEnd(scPaymentCertWrapper.getCertIssueDate())));
		}
			
		/**
		 * @author koeyyeung
		 * added on 20Aug, 2014**/
		if (scPaymentCertWrapper.getDueDate() != null){
			if ("exactDate".equals(dueDateType)) {
				criteria.add(Restrictions.eq("dueDate", scPaymentCertWrapper.getDueDate()));
			}else if ("onOrBefore".equals(dueDateType)){
				criteria.add(Restrictions.le("dueDate", scPaymentCertWrapper.getDueDate()));
			}
		}
		
		
		criteria.addOrder(Order.asc("jobNo"))
				.addOrder(Order.asc("packageNo"))
				.addOrder(Order.asc("paymentCertNo"));
		
		return criteria.list();
	}
	
	/**@author koeyyeung
	 * created on 24th Apr, 2015
	 * Call Stored Procedure to update F58011 from ScPayment Cert**/
	public Boolean callStoredProcedureToUpdateF58011() throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call "+sfi.getSettings().getDefaultSchemaName() +  "."+storedProcedureConfig.getStoredProcedureUpdateF58011()+"}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
//		sfi.close(); // will cause Could not open Hibernate Session
		
		
        completed = true;
		
		return completed;
	}
	
	private Date obtainDateStart(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	private Date obtainDateEnd(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 23);
	    cal.set(Calendar.MINUTE, 59);
	    cal.set(Calendar.SECOND, 59);
	    cal.set(Calendar.MILLISECOND, 999);
	    return cal.getTime();
	}
	
	public SCPaymentCertHBDao() {
		super(SCPaymentCert.class);
	}
	
}
