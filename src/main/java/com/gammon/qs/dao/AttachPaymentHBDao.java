package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AttachPayment;
import com.gammon.qs.domain.PaymentCert;

@Repository
public class AttachPaymentHBDao extends BaseHibernateDao<AttachPayment> {

	private Logger logger = Logger.getLogger(AttachPaymentHBDao.class.getName());
	public AttachPaymentHBDao() {
		super(AttachPayment.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachPayment> getSCPaymentAttachment(String jobNumber, String subcontractNo, String paymentCertNo) throws DatabaseOperationException{
		try{
			List<AttachPayment> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("SUBCONTRACT", "SUBCONTRACT");
			criteria.createAlias("SUBCONTRACT.jobInfo", "jobInfo");
			criteria.createAlias("paymentCert", "paymentCert");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("SUBCONTRACT.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scPaymentCert.paymentCertNo", new Integer(paymentCertNo)));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<AttachPayment>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachPayment getSCPaymentAttachment(PaymentCert paymentCert, Integer attachmentSequenceNo) throws DatabaseOperationException{
		try{
			PaymentCert dbObj = new PaymentCert();
			//dbObj = scPaymentCert;
			dbObj.setJobNo(paymentCert.getSubcontract().getJobInfo().getJobNumber());
			dbObj.setPackageNo(paymentCert.getSubcontract().getPackageNo());
			dbObj.setPaymentCertNo(paymentCert.getPaymentCertNo());
			Criteria criteria = getSession().createCriteria(this.getType());
			/*criteria.createAlias("paymentCert.subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");*/
			//criteria.createAlias("paymentCert", "paymentCert");
			/*criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scPaymentCert.paymentCertNo", new Integer (paymentCertNo)));*/
			criteria.add(Restrictions.eq("paymentCert", paymentCert));
			criteria.add(Restrictions.eq("sequenceNo", attachmentSequenceNo));
			return (AttachPayment)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachPayment> getSCPaymentAttachment(PaymentCert scPaymentCert) throws DatabaseOperationException{
		try{
			List<AttachPayment> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", scPaymentCert));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachPayment getSCPaymentAttachment(String jobNumber, String subcontractNo,String paymentCertNo, Integer sequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("paymentCert.subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.createAlias("paymentCert", "paymentCert");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("paymentCert.paymentCertNo", new Integer (paymentCertNo)));
			criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
			return (AttachPayment)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachPayment getSCPaymentAttachment(AttachPayment scPaymentAttachment) throws DatabaseOperationException{
		try{
			return getSCPaymentAttachment(scPaymentAttachment.getPaymentCert(),new Integer(scPaymentAttachment.getSequenceNo()));
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCPaymentAttachment(AttachPayment scPaymentAttachment) throws Exception{
		AttachPayment dbObj = this.getSCPaymentAttachment(scPaymentAttachment);
		if (dbObj==null)
			throw new DatabaseOperationException("Update Fail.");
		dbObj.setFileLink(scPaymentAttachment.getFileLink());
		dbObj.setFileName(scPaymentAttachment.getFileName());
		dbObj.setDocumentType(scPaymentAttachment.getDocumentType());
		dbObj.setTextAttachment(scPaymentAttachment.getTextAttachment());
		dbObj.setLastModifiedUser(scPaymentAttachment.getLastModifiedUser());
		saveOrUpdate(dbObj);
		return true;
	}

	public boolean addSCAttachment(AttachPayment scPaymentAttachment) throws DatabaseOperationException{
		AttachPayment dbObj = this.getSCPaymentAttachment(scPaymentAttachment);
		if (dbObj!=null)
			throw new DatabaseOperationException("Upload Fail."); 
		scPaymentAttachment.setCreatedDate(new Date());
		saveOrUpdate(scPaymentAttachment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(AttachPayment scPaymentAttachment, String user) throws Exception{

		try {
			scPaymentAttachment.setLastModifiedUser(user);
			return saveSCPaymentAttachment(scPaymentAttachment);
		}catch (DatabaseOperationException dbe){
			scPaymentAttachment.setCreatedUser(user);
			return addSCAttachment(scPaymentAttachment);
		}
	}
	
	public long deleteAttachmentByByPaymentCertID(Long paymentCertID) throws DatabaseOperationException {
		long noOfRecord = 0;
		getSession().clear();
		Query query = getSession().createQuery("delete from SCPaymentAttachment scPaymentAttachment where scPaymentCert_ID =" + paymentCertID);
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}

}
