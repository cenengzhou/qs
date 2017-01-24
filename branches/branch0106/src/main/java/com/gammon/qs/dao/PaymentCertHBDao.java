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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
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
			criteria.createAlias("subcontract","subcontract" );
			criteria.createAlias("subcontract.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
			return (PaymentCert) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: obtainPaymentCertificate(" + jobNumber + ", " + packageNo + ", " + paymentCertNo + ")");
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
				scPaymentDetail.setPaymentCert(scPaymentCertDB);
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
			criteria.createAlias("subcontract","subcontract" );
			criteria.createAlias("subcontract.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
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
	public List<PaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, String packageNo) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new DatabaseOperationException("Jobnumber is null");
		if (packageNo==null)
			throw new DatabaseOperationException("packageNo is null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract","subcontract" );
			criteria.createAlias("subcontract.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
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
			criteria.createAlias("subcontract","subcontract" );
			criteria.createAlias("subcontract.jobInfo","jobInfo" );
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo.toString()));
			criteria.addOrder(Order.desc("paymentCertNo"));
			criteria.setFetchMode("subcontract", FetchMode.JOIN);
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

	public boolean updateSCPaymentCertAdmin(PaymentCert paymentCert) throws DatabaseOperationException {
//		throw new RuntimeException("remove entity | SCPaymentCertControl | remark updateSCPaymentCertAdmin(SCPaymentCert scPaymentCert)");
		//TODO: remove entity | SCPaymentCertControl | remark paymentControl.setBeforeValues setAfterValues
		if (paymentCert==null)
			throw new DatabaseOperationException("SCPayment Cert is Null");
		try {
//			SCPaymentCertControl paymentControl = new SCPaymentCertControl();
			PaymentCert paymentCertDB =getSCPaymentCert(paymentCert);
			if (paymentCertDB==null){
				throw new DatabaseOperationException("SC Payment Cert "+paymentCert.getId()+" is not existed.");
			}

//			paymentControl.setBeforeValues(scPaymentCertDB);
//			paymentControl.setAfterValues(scPaymentCert);
//			paymentControl.setActionType("UPDATE");

//			paymentCertDB.setPaymentStatus(paymentCert.getPaymentStatus());
//			paymentCertDB.setMainContractPaymentCertNo(paymentCert.getMainContractPaymentCertNo());
//			paymentCertDB.setDueDate(paymentCert.getDueDate());
//			paymentCertDB.setAsAtDate(paymentCert.getAsAtDate());
//			paymentCertDB.setIpaOrInvoiceReceivedDate(paymentCert.getIpaOrInvoiceReceivedDate());
//			paymentCertDB.setCertIssueDate(paymentCert.getCertIssueDate());
//			paymentCertDB.setCertAmount(paymentCert.getCertAmount());
//			paymentCertDB.setIntermFinalPayment(paymentCert.getIntermFinalPayment());
//			paymentCertDB.setAddendumAmount(paymentCert.getAddendumAmount());
//			paymentCertDB.setRemeasureContractSum(paymentCert.getRemeasureContractSum());
//			paymentCertDB.setDirectPayment(paymentCert.getDirectPayment());
//			if (paymentCertDB.getCreatedDate()==null)
//				paymentCertDB.setCreatedDate(new Date());
//			paymentCertDB.setLastModifiedUser(paymentCert.getLastModifiedUser());
			merge(paymentCert);
//			getSession().save(paymentControl);
			return true;
		}catch (HibernateException he){
			logger.info("Fail: updateSCPaymentCert(SCPaymentCert scPaymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCert> obtainDirectPaymentRecords(String division,
			String company, String jobNumber, String vendorNo, String packageNo, List<Integer> scStatusList) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("subcontract","subcontract" );
		criteria.createAlias("subcontract.jobInfo","jobInfo" );
		criteria.add(Restrictions.eq("directPayment", PaymentCert.DIRECT_PAYMENT));
		criteria.add(Restrictions.eq("paymentStatus", "APR"));
		if (scStatusList!=null && scStatusList.size()>0)
			criteria.add(Restrictions.in("subcontract.subcontractStatus", scStatusList));
		if (packageNo!=null && packageNo.trim().length()>0)
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
		if (vendorNo!=null && vendorNo.trim().length()>0)
			criteria.add(Restrictions.eq("subcontract.vendorNo",vendorNo));
		else 
			criteria.add(Restrictions.isNotNull("subcontract.vendorNo"));
		if (jobNumber!=null && jobNumber.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
		else
			criteria.add(Restrictions.isNotNull("jobInfo.jobNumber"));
		if (division!=null && division.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.division", division));
		if (company!=null && company.trim().length()>0)
			criteria.add(Restrictions.eq("jobInfo.company", company));
		criteria.addOrder(Order.asc("jobInfo.jobNumber"))
				.addOrder(Order.asc("subcontract.packageNo"))
				.addOrder(Order.asc("paymentCertNo"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCert> obtainSCPaymentCertList(PaymentCertWrapper scPaymentCertWrapper, List<String> jobNoList, String dueDateType){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		
		criteria.createAlias("subcontract", "subcontract");
		criteria.createAlias("subcontract.jobInfo", "jobInfo");
		
		if(scPaymentCertWrapper.getJobNo()!=null && !"".equals(scPaymentCertWrapper.getJobNo().trim()))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", scPaymentCertWrapper.getJobNo().trim()));
		if(scPaymentCertWrapper.getJobInfo()!=null && scPaymentCertWrapper.getJobInfo().getCompany()!=null && !"".equals(scPaymentCertWrapper.getJobInfo().getCompany().trim()))
			criteria.add(Restrictions.eq("jobInfo.company", scPaymentCertWrapper.getJobInfo().getCompany().trim()));
		if(jobNoList!=null){
			Disjunction or = Restrictions.disjunction();
			for(int i=0; i < jobNoList.size(); i+=500){
				int from = i;
				int to = i+499 < jobNoList.size() ? i+499 : jobNoList.size(); 
				logger.info("SubcontractHBDao.obtainSubcontractList from:" + from + " to:" + to);
				or.add(Restrictions.in("jobInfo.jobNumber", jobNoList.subList(from, to)));
			}
			criteria.add(or);
		}
		if(scPaymentCertWrapper.getSubcontract().getVendorNo()!=null && !"".equals(scPaymentCertWrapper.getSubcontract().getVendorNo().trim()))
			criteria.add(Restrictions.eq("subcontract.vendorNo", scPaymentCertWrapper.getSubcontract().getVendorNo().trim()));
		if(scPaymentCertWrapper.getSubcontract().getPaymentTerms()!=null && !"".equals(scPaymentCertWrapper.getSubcontract().getPaymentTerms().trim()))
			criteria.add(Restrictions.eq("subcontract.paymentTerms", scPaymentCertWrapper.getSubcontract().getPaymentTerms().trim()));
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
	
	
	@SuppressWarnings("unchecked")
	public PaymentCert obtainPaymentLatestPostedCert(String jobNumber, String subcontractNo) throws DataAccessException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("subcontract","subcontract" );
		criteria.createAlias("subcontract.jobInfo","jobInfo" );
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim() ));
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo.toString()));
		criteria.add(Restrictions.eq("paymentStatus", PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE));
		criteria.addOrder(Order.desc("paymentCertNo"));
		List<PaymentCert> resultList = criteria.list();
		if (resultList!=null && !resultList.isEmpty())
			return resultList.get(0);
		return null;
	}
	
	
	public Double getTotalPostedCertAmount(String jobNo, String subcontractNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.createAlias("subcontract","subcontract" );
		criteria.createAlias("subcontract.jobInfo","jobInfo" );
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
		criteria.add(Restrictions.eq("paymentStatus", PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE));
		criteria.setProjection(Projections.sum("certAmount"));
		
		return criteria.uniqueResult() == null ? 0.0 : (Double) criteria.uniqueResult();
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
