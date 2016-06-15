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
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.wrapper.scPayment.PaymentCertWrapper;
@Repository
public class PaymentCertHBDao extends BaseHibernateDao<PaymentCert> {
	private Logger logger = Logger.getLogger(PaymentCertHBDao.class.getName());
	@Autowired
	private SubcontractHBDao scPackageHBDao;
	@Autowired
	private PaymentCertDetailHBDao scPaymentDetailHBDao;

	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public boolean addSCPaymentCert(PaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new NullPointerException();
		try {		
			if (getSCPaymentCert(scPaymentCert)==null){
				scPaymentCert.setSubcontract(
						scPackageHBDao.obtainSCPackage(	
								scPaymentCert.getSubcontract().getJobInfo().getJobNumber().trim(),
								scPaymentCert.getSubcontract().getPackageNo()
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

	public PaymentCert obtainPaymentCertificate(String jobNumber, String packageNo,Integer paymentCertNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		if (paymentCertNo==null)
			throw new DatabaseOperationException("paymentCertNo is null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("paymentCertNo", paymentCertNo));
			criteria.createAlias("subcontract.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo));
			return (PaymentCert) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(String jobNumber, String packageNo,String paymentCertNo)");
			throw new DatabaseOperationException(he);
		}
	
	}

	
	public PaymentCert getSCPaymentCert(PaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try{
			return obtainPaymentCertificate(scPaymentCert.getSubcontract().getJobInfo().getJobNumber(),
									scPaymentCert.getSubcontract().getPackageNo(),
									scPaymentCert.getPaymentCertNo());
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(SCPaymentCert scPaymentCert) ");
			throw new DatabaseOperationException(he);
		}
	}
	public boolean updateSCPaymentCert(PaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
			PaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
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
	public boolean saveOrUpdateSCPaymentCert(PaymentCert scPaymentCert) throws DatabaseOperationException{
		if (scPaymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
			PaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
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
			List<PaymentCertDetail> scPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(scPaymentCertDB);
			for(PaymentCertDetail scPaymentDetail : scPaymentDetailList){
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
	public List<PaymentCert> obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("SUBCONTRACT.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo));
			if(status!=null)
				criteria.add(Restrictions.eq("paymentStatus",status));
			if(directPayment!=null)
				criteria.add(Restrictions.eq("directPayment",directPayment));
			criteria.addOrder(Order.desc("paymentCertNo"));
			return (List<PaymentCert>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("SUBCONTRACT.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			return (List<PaymentCert>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCert(String jobNumber, String packageNo,String paymentCertNo");
			throw new DatabaseOperationException(he);
		}
	}


	@SuppressWarnings("unchecked")
	public PaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");

		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("SUBCONTRACT.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			criteria.setFetchMode("SUBCONTRACT", FetchMode.JOIN);
			List<PaymentCert> resultList = criteria.list();
			if (resultList!=null && !resultList.isEmpty())
				return resultList.get(0);
			return null;
		}catch (HibernateException he){
			logger.info("Fail: obtainPaymentLatestCert(String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public PaymentCert getSCPaymentLatestCert(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		if (jobInfo==null)
			throw new DatabaseOperationException("Job is null");

		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("SUBCONTRACT.jobInfo", jobInfo ));
			criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			criteria.setFetchMode("SUBCONTRACT", FetchMode.JOIN);
			List<PaymentCert> resultList = criteria.list();
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
	public PaymentCert obtainPaymentCert(String jobNumber, String packageNo, String paymentStatus) throws DatabaseOperationException{
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
			Criteria criteria = getSession().createCriteria(this.getType());
			
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.createAlias("SUBCONTRACT", "SUBCONTRACT");
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo));
			criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
			
			return (PaymentCert) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: obtainPaymentCert(String jobNumber, String packageNo, String paymentStatus)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCert> getSCPaymentCertsPCS() throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("paymentStatus", "PCS"));
			List<PaymentCert> paymentCerts = criteria.list();
			for(PaymentCert paymentCert : paymentCerts){
				Hibernate.initialize(paymentCert.getSubcontract());
				Hibernate.initialize(paymentCert.getSubcontract().getJobInfo());
			}
			return paymentCerts;
		}catch (HibernateException he){
			logger.info("Fail: getSCPaymentCertsPCS()");
			throw new DatabaseOperationException(he);
		}
	}
		
	public List<PaymentCertDetail> obtainPaymentDetailList(String jobNumber, String packageNo,Integer paymentCertNo) throws DatabaseOperationException{
		return scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo));
	}

	public boolean updateSCPaymentCertAdmin(PaymentCert scPaymentCert) throws DatabaseOperationException {
		throw new RuntimeException("remove entity | SCPaymentCertControl | remark updateSCPaymentCertAdmin(SCPaymentCert scPaymentCert)");
		//TODO: remove entity | SCPaymentCertControl | remark updateSCPaymentCertAdmin(SCPaymentCert scPaymentCert)
//		if (scPaymentCert==null)
//			throw new DatabaseOperationException("SCPayment Cert is Null");
//		try {
//			SCPaymentCertControl paymentControl = new SCPaymentCertControl();
//			SCPaymentCert scPaymentCertDB =getSCPaymentCert(scPaymentCert);
//			if (scPaymentCertDB==null){
//				throw new DatabaseOperationException("SC Payment Cert "+scPaymentCert.getId()+" is not existed.");
//			}
//
//			paymentControl.setBeforeValues(scPaymentCertDB);
//			paymentControl.setAfterValues(scPaymentCert);
//			paymentControl.setActionType("UPDATE");
//
//			scPaymentCertDB.setPaymentStatus(scPaymentCert.getPaymentStatus());
//			scPaymentCertDB.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());
//			scPaymentCertDB.setDueDate(scPaymentCert.getDueDate());
//			scPaymentCertDB.setAsAtDate(scPaymentCert.getAsAtDate());
//			scPaymentCertDB.setIpaOrInvoiceReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
//			scPaymentCertDB.setCertIssueDate(scPaymentCert.getCertIssueDate());
//			scPaymentCertDB.setCertAmount(scPaymentCert.getCertAmount());
//			scPaymentCertDB.setIntermFinalPayment(scPaymentCert.getIntermFinalPayment());
//			scPaymentCertDB.setAddendumAmount(scPaymentCert.getAddendumAmount());
//			scPaymentCertDB.setRemeasureContractSum(scPaymentCert.getRemeasureContractSum());
//			scPaymentCertDB.setDirectPayment(scPaymentCert.getDirectPayment());
//			if (scPaymentCertDB.getCreatedDate()==null)
//				scPaymentCertDB.setCreatedDate(new Date());
//			scPaymentCertDB.setLastModifiedUser(scPaymentCert.getLastModifiedUser());
//			saveOrUpdate(scPaymentCertDB);
//			getSession().save(paymentControl);
//			return true;
//		}catch (HibernateException he){
//			logger.info("Fail: updateSCPaymentCert(SCPaymentCert scPaymentCert)");
//			throw new DatabaseOperationException(he);
//		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCert> obtainDirectPaymentRecords(String division,
			String company, String jobNumber, String vendorNo, String packageNo, List<Integer> scStatusList) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("SUBCONTRACT.jobInfo","jobInfo" );
		criteria.createAlias("SUBCONTRACT","SUBCONTRACT" );
		criteria.add(Restrictions.eq("directPayment", PaymentCert.DIRECT_PAYMENT));
		criteria.add(Restrictions.eq("paymentStatus", "APR"));
		if (scStatusList!=null && scStatusList.size()>0)
			criteria.add(Restrictions.in("SUBCONTRACT.subcontractStatus", scStatusList));
		if (packageNo!=null && packageNo.trim().length()>0)
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo));
		if (vendorNo!=null && vendorNo.trim().length()>0)
			criteria.add(Restrictions.eq("SUBCONTRACT.vendorNo",vendorNo));
		else 
			criteria.add(Restrictions.isNotNull("SUBCONTRACT.vendorNo"));
		if (jobNumber!=null && jobNumber.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
		else
			criteria.add(Restrictions.isNotNull("jobInfo.jobNumber"));
		if (division!=null && division.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.division", division));
		if (company!=null && company.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.company", company));
		criteria.addOrder(Order.asc("jobInfo.jobNumber"))
				.addOrder(Order.asc("SUBCONTRACT.packageNo"))
				.addOrder(Order.asc("paymentCertNo"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCert> obtainSCPaymentCertList(PaymentCertWrapper scPaymentCertWrapper, List<String> companyList, String dueDateType){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		
		criteria.createAlias("SUBCONTRACT", "SUBCONTRACT");
		criteria.createAlias("SUBCONTRACT.jobInfo", "jobInfo");
		
		if(scPaymentCertWrapper.getJobNo()!=null && !"".equals(scPaymentCertWrapper.getJobNo().trim()))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", scPaymentCertWrapper.getJobNo().trim()));
		if(scPaymentCertWrapper.getJobInfo()!=null && scPaymentCertWrapper.getJobInfo().getCompany()!=null && !"".equals(scPaymentCertWrapper.getJobInfo().getCompany().trim()))
			criteria.add(Restrictions.eq("jobInfo.company", scPaymentCertWrapper.getJobInfo().getCompany().trim()));
		else if(companyList!=null)
			criteria.add(Restrictions.in("jobInfo.company", companyList));
		if(scPaymentCertWrapper.getSubcontract().getVendorNo()!=null && !"".equals(scPaymentCertWrapper.getSubcontract().getVendorNo().trim()))
			criteria.add(Restrictions.eq("SUBCONTRACT.vendorNo", scPaymentCertWrapper.getSubcontract().getVendorNo().trim()));
		if(scPaymentCertWrapper.getSubcontract().getPaymentTerms()!=null && !"".equals(scPaymentCertWrapper.getSubcontract().getPaymentTerms().trim()))
			criteria.add(Restrictions.eq("SUBCONTRACT.paymentTerms", scPaymentCertWrapper.getSubcontract().getPaymentTerms().trim()));
		if(scPaymentCertWrapper.getSubcontract().getPackageNo()!=null && !"".equals(scPaymentCertWrapper.getSubcontract().getPackageNo().trim()))
			criteria.add(Restrictions.eq("packageNo", scPaymentCertWrapper.getSubcontract().getPackageNo().trim()));
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
	
	public PaymentCertHBDao() {
		super(PaymentCert.class);
	}
	
}
