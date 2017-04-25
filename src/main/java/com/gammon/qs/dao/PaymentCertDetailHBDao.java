package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
@Repository
public class PaymentCertDetailHBDao extends BaseHibernateDao<PaymentCertDetail> {

	private Logger logger = Logger.getLogger(PaymentCertDetailHBDao.class.getName());
	@Autowired
	private PaymentCertHBDao paymentCertDao;

	public PaymentCertDetailHBDao() {
		super(PaymentCertDetail.class);
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> getSCPaymentDetail(PaymentCert paymentCert, String lineType) throws DatabaseOperationException {
		if (paymentCert == null)
			throw new NullPointerException("SC Payment Detail is Null");
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
			criteria.add(Restrictions.eq("lineType", lineType));
			return (List<PaymentCertDetail>) criteria.list();

		} catch (HibernateException he) {
			logger.info("Fail: getSCPaymentDetail(SCPaymentCert scPaymentCert, String lineType)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> getPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo) throws DatabaseOperationException {
		if (jobNumber == null || packageNo == null || paymentCertNo == null)
			throw new NullPointerException("Job No. / Package No. / Payment Certificate No. is Null");

		PaymentCert paymentCert = paymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo);
		if (paymentCert == null)
			throw new NullPointerException("Payment Certificate is Null");

		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.sqlRestriction("Payment_Cert_ID = '" + (paymentCert.getId() + "'")));
			return (List<PaymentCertDetail>) criteria.list();

		} catch (HibernateException he) {
			logger.info("Fail: getPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo)");
			throw new DatabaseOperationException(he);
		}
	}

	public Double getCertCpfAmount(PaymentCert paymentCert) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
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

	/**
	 * Payment Dashboard
	 * @author koeyyeung
	 * created on 08 Sep, 2016
	 */
	public Double getCumPaymentResourceDistribution(PaymentCert paymentCert, String[] lineTypes) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("paymentCert", paymentCert));
		criteria.add(Restrictions.in("lineType", lineTypes));
		criteria.setProjection(Projections.sum("cumAmount"));
		return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
	}

	/**
	 * Payment Summary Dashboard
	 * @author koeyyeung
	 * created on 08 Sep, 2016
	 */
	public Double getMovPaymentResourceDistribution(PaymentCert paymentCert, String[] lineTypes) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("paymentCert", paymentCert));
		criteria.add(Restrictions.in("lineType", lineTypes));
		criteria.setProjection(Projections.sum("movementAmount"));
		return criteria.uniqueResult() == null ? 0.0 : Double.valueOf(criteria.uniqueResult().toString());
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

		criteria.createAlias("paymentCert", "paymentCert");
		criteria.createAlias("subcontractDetail", "subcontractDetail");

		criteria.add(Restrictions.eq("paymentCert", paymentCert));
		criteria.add(Restrictions.eq("subcontractDetail", subcontractDetail));
		/*criteria.add(Restrictions.eq("this.objectCode", subcontractDetail.getObjectCode()));
		criteria.add(Restrictions.eq("this.subsidiaryCode", subcontractDetail.getSubsidiaryCode()));
		criteria.add(Restrictions.eq("this.billItem", subcontractDetail.getBillItem()));*/

		scPaymentDetail = (PaymentCertDetail) criteria.uniqueResult();

		return scPaymentDetail;
	}

	@SuppressWarnings("unchecked")
	public List<PaymentCertDetail> obtainSCPaymentDetailBySCPaymentCert(PaymentCert paymentCert){
		Criteria criteria = getSession().createCriteria(PaymentCertDetail.class);
		criteria.add(Restrictions.eq("paymentCert", paymentCert));
		criteria.addOrder(Order.asc("lineType"));
		return criteria.list();
	}

	public long deleteDetailByPaymentCertID(Long paymentCertID) throws DatabaseOperationException {
		long noOfRecord = 0;
		getSession().clear();
		Query query = getSession().createQuery("delete from PaymentCertDetail paymentCertDetail where Payment_Cert_ID =" + paymentCertID);
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}

}
