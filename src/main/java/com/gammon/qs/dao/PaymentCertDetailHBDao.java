package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
@Repository
public class PaymentCertDetailHBDao extends BaseHibernateDao<PaymentCertDetail> {

	private Logger logger = Logger.getLogger(PaymentCertDetailHBDao.class.getName());
	@Autowired
	private PaymentCertHBDao scPaymentCertDao;
	@Autowired
	private SubcontractDetailHBDao scDetailsDao;

	public PaymentCertDetailHBDao() {
		super(PaymentCertDetail.class);
	}

	public boolean addSCPaymentDetail(PaymentCertDetail scPaymentDetail) throws DatabaseOperationException {
		if (scPaymentDetail == null)
			throw new NullPointerException();
		try {
			if (getSCPaymentDetail(scPaymentDetail) == null) {
				scPaymentDetail.setCreatedDate(new Date());
				scPaymentDetail.setCreatedUser(scPaymentDetail.getCreatedUser());
				scPaymentDetail.setLastModifiedUser(scPaymentDetail.getLastModifiedUser());
				this.saveOrUpdate(scPaymentDetail);
			}
		} catch (DatabaseOperationException e) {
			logger.info("Fail: addSCPaymentCert(SCPaymentDetail scPaymentDetail)");
			throw new DatabaseOperationException(e);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> getSCPaymentDetail(PaymentCert scPaymentCert, String lineType) throws DatabaseOperationException {
		if (scPaymentCert == null)
			throw new NullPointerException("SC Payment Detail is Null");
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", scPaymentCert));
			criteria.add(Restrictions.eq("lineType", lineType));
			return (List<PaymentCertDetail>) criteria.list();

		} catch (HibernateException he) {
			logger.info("Fail: getSCPaymentDetail(SCPaymentCert scPaymentCert, String lineType)");
			throw new DatabaseOperationException(he);
		}
	}

	public PaymentCertDetail getSCPaymentDetail(PaymentCertDetail scPaymentDetail) throws DatabaseOperationException {
		if (scPaymentDetail == null)
			throw new NullPointerException("SC Payment Detail is Null");
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scSeqNo", scPaymentDetail.getScSeqNo()));
			criteria.add(Restrictions.sqlRestriction("scPaymentCert_ID = '" + (scPaymentCertDao.getSCPaymentCert(scPaymentDetail.getScPaymentCert()).getId() + "'")));
			return (PaymentCertDetail) criteria.uniqueResult();

		} catch (HibernateException he) {
			logger.info("Fail: getSCPaymentCert(SCPaymentDetail scPaymentDetail)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> getSCPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo) throws DatabaseOperationException {
		if (jobNumber == null || packageNo == null || paymentCertNo == null)
			throw new NullPointerException("SC Payment Detail is Null");
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.sqlRestriction("scPaymentCert_ID = '" + (scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo).getId() + "'")));
			return (List<PaymentCertDetail>) criteria.list();

		} catch (HibernateException he) {
			logger.info("Fail: getSCPaymentCert(String jobNumber, Integer packageNo, Integer paymentCertNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean updateSCPaymentDetail(PaymentCertDetail scPaymentDetail) throws DatabaseOperationException {
		if (scPaymentDetail == null)
			throw new NullPointerException("SCPayment Cert is Null");
		try {
			PaymentCertDetail scPaymentDetailDB = getSCPaymentDetail(scPaymentDetail);
			if (scPaymentDetailDB == null)
				throw new DatabaseOperationException("Record of SCPaymentCert was not exist");
			scPaymentDetailDB.setLineType(scPaymentDetail.getLineType());
			scPaymentDetailDB.setBillItem(scPaymentDetail.getBillItem());
			scPaymentDetailDB.setMovementAmount(scPaymentDetail.getMovementAmount());
			scPaymentDetailDB.setCumAmount(scPaymentDetail.getCumAmount());
			scPaymentDetailDB.setLastModifiedUser(scPaymentDetail.getLastModifiedUser());
			saveOrUpdate(scPaymentDetailDB);
			return true;
		} catch (HibernateException he) {
			logger.info("Fail: updateSCPaymentDetail(SCPaymentCert scPaymentDetail)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double getCertCpfAmount(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.eq("lineType", "CF"));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: getCertCpfAmount(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double obtainPaymentRetentionAmount(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.in("lineType", new String[] { "RT", "RA" }));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: obtainPaymentRetentionAmount(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double obtainPaymentRetentionReleasedAmount(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.eq("lineType", "RR"));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: obtainPaymentRetentionReleasedAmount(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double obtainPaymentGstPayable(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.eq("lineType", "GP"));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: obtainPaymentGstPayable(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double obtainPaymentGstReceivable(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.eq("lineType", "GR"));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: obtainPaymentGstReceivable(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccountMovementWrapper> obtainPaymentAccountMovements(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.not(Restrictions.in("lineType", new Object[] { "CF", "RT", "RA", "RR", "GP", "GR", "MR" })));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.sum("movementAmount"), "movementAmount")
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					);
			criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountMovementWrapper.class));
			return criteria.list();
		} catch (Exception he) {
			logger.info("Fail: obtainPaymentAccountMovements(SCPaymentCert paymentCert)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double obtainAccountMaterialRetention(PaymentCert paymentCert, String objectCode, String subsidiaryCode) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", paymentCert));
			criteria.add(Restrictions.eq("objectCode", objectCode));
			if (subsidiaryCode != null)
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			else
				criteria.add(Restrictions.isNull("subsidiaryCode"));
			criteria.add(Restrictions.eq("lineType", "MR"));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			logger.info("Fail: obtainAccountMaterialRetention(SCPaymentCert paymentCert, String objectCode, String subsidiaryCode)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCert> getSCPaymentDetail(String jobNumber, String packageNo) {
		Criteria criteria = getSession().createCriteria(PaymentCert.class);
		criteria.createAlias("SUBCONTRACT", "SUBCONTRACT");
		criteria.createAlias("SUBCONTRACT.jobInfo", "job");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", packageNo));
		criteria.setFetchMode("scPaymentDetailList", FetchMode.JOIN);
		return (List<PaymentCert>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCert> getSCPaymentDetail(Subcontract scPackage) {
		Criteria criteria = getSession().createCriteria(PaymentCert.class);
		criteria.add(Restrictions.eq("scPackage", scPackage));
		criteria.setFetchMode("scPaymentDetailList", FetchMode.JOIN);
		return (List<PaymentCert>) criteria.list();
	}

	/**
	 * 
	 * @author tikywong
	 * created on Feb 21, 2014 2:19:03 PM
	 */
	public PaymentCertDetail obtainPaymentDetail(PaymentCert paymentCert, SubcontractDetail subcontractDetail){
		if (paymentCert == null)
			throw new NullPointerException("scPaymentCert is Null");
		
		PaymentCertDetail scPaymentDetail = null;
		Criteria criteria = getSession().createCriteria(this.getType());
		
		try{
		criteria.createAlias("paymentCert", "paymentCert");
		criteria.createAlias("subcontractDetail", "subcontractDetail");
			
		criteria.add(Restrictions.eq("paymentCert", paymentCert));
		criteria.add(Restrictions.eq("subcontractDetail", subcontractDetail));
		criteria.add(Restrictions.eq("this.objectCode", subcontractDetail.getObjectCode()));
		criteria.add(Restrictions.eq("this.subsidiaryCode", subcontractDetail.getSubsidiaryCode()));
		criteria.add(Restrictions.eq("this.billItem", subcontractDetail.getBillItem()));
		
		scPaymentDetail = (PaymentCertDetail) criteria.uniqueResult();
		}catch(Exception he){
			logger.info("Fail: obtainPaymentDetail(SCPaymentCert scPaymentCert, SCDetails scDetail)");
			he.printStackTrace();
		}

		return scPaymentDetail;
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> obtainSCPaymentDetailBySCPaymentCert(PaymentCert scPaymentCert){
		Criteria criteria = getSession().createCriteria(PaymentCertDetail.class);
		criteria.add(Restrictions.eq("scPaymentCert", scPaymentCert));
		return criteria.list();
	}
	
	public long deleteDetailByJobSCPaymentNo(String jobNumber, Integer packageNo, Integer paymentCertNo) throws DatabaseOperationException {
		long noOfRecord = 0;
		PaymentCert scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo.toString(), paymentCertNo);
		getSession().merge(obtainSCPaymentDetailBySCPaymentCert(scPaymentCert));
		Query query = getSession().createQuery("delete from SCPaymentDetail scPaymentDetail where scPaymentCert_ID =" + scPaymentCert.getId());
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}

	public long deleteDetailByPaymentCertID(Long paymentCertID) throws DatabaseOperationException {
		long noOfRecord = 0;
		getSession().clear();
		Query query = getSession().createQuery("delete from SCPaymentDetail scPaymentDetail where scPaymentCert_ID =" + paymentCertID);
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}

	public void savePaymentCertDetails(List<PaymentCertDetail> scPaymentDetails) throws DatabaseOperationException {
		PaymentCert currCert = null;
		for (PaymentCertDetail scPaymentDetail : scPaymentDetails) {
			if (currCert == null
					|| !currCert.getSubcontract().getJobInfo().getJobNumber().equals(scPaymentDetail.getScPaymentCert().getSubcontract().getJobInfo().getJobNumber())
					|| !currCert.getSubcontract().getPackageNo().equals(scPaymentDetail.getScPaymentCert().getSubcontract().getPackageNo())
					|| !currCert.getPaymentCertNo().equals(scPaymentDetail.getScPaymentCert().getPaymentCertNo()))
				currCert = scPaymentCertDao.obtainPaymentCertificate(scPaymentDetail.getScPaymentCert().getSubcontract().getJobInfo().getJobNumber(),
							scPaymentDetail.getScPaymentCert().getSubcontract().getPackageNo(),
							scPaymentDetail.getScPaymentCert().getPaymentCertNo());
			scPaymentDetail.setScPaymentCert(currCert);
			scPaymentDetail.setCreatedUser(currCert.getCreatedUser());
			scPaymentDetail.setLastModifiedUser(currCert.getLastModifiedUser());
			scPaymentDetail.setCreatedDate(currCert.getCreatedDate());
			if (scPaymentDetail.getScDetail() != null) {
				scPaymentDetail.setScDetail(scDetailsDao.getSCDetail(scPaymentDetail.getScDetail()));
			}
			saveOrUpdate(scPaymentDetail);
		}

	}

	public Double obtainAccumulatedPostedCertQtyByDetail(Long scDetail_id) throws DatabaseOperationException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("paymentCert", "paymentCert");
			criteria.add(Restrictions.eq("paymentCert.paymentStatus", PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE));
			criteria.createAlias("subcontractDetail", "subcontractDetail");
			criteria.add(Restrictions.eq("subcontractDetail.id", scDetail_id));
			criteria.setProjection(Projections.sum("movementAmount"));
			return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
		} catch (Exception he) {
			throw new DatabaseOperationException(he);
		}
	}
	
	public int deleteSCPaymentDetailBySCPaymentCert(PaymentCert scPaymentCert) throws DatabaseOperationException{
		int count = 0;
		for(PaymentCertDetail scPaymentDetail : obtainSCPaymentDetailBySCPaymentCert(scPaymentCert)){
			delete(scPaymentDetail);
			count++;
		}
		return count;
	}
}