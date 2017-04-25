package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
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
	
	public AttachPayment getAttachPayment(PaymentCert paymentCert, Integer attachmentSequenceNo) throws DatabaseOperationException{
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
			logger.info("Fail: getAttachPayment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachPayment> getAttachPayment(PaymentCert paymentCert) throws DatabaseOperationException{
		try{
			List<AttachPayment> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("paymentCert", paymentCert));	
			criteria.addOrder(Order.asc("sequenceNo"));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachPayment getSCPaymentAttachment(AttachPayment attachPayment) throws DatabaseOperationException{
		try{
			return getAttachPayment(attachPayment.getPaymentCert(),new Integer(attachPayment.getSequenceNo()));
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveAttachPayment(AttachPayment attachPayment) throws Exception{
		AttachPayment dbObj = this.getSCPaymentAttachment(attachPayment);
		if (dbObj==null)
			throw new DatabaseOperationException("[Update Fail] PaymentCertNo:" + attachPayment.getPaymentCert() + " SequenceNo:" + attachPayment.getSequenceNo());
		dbObj.setFileLink(attachPayment.getFileLink());
		dbObj.setFileName(attachPayment.getFileName());
		dbObj.setDocumentType(attachPayment.getDocumentType());
		dbObj.setTextAttachment(attachPayment.getTextAttachment());
		dbObj.setLastModifiedUser(attachPayment.getLastModifiedUser());
		saveOrUpdate(dbObj);
		return true;
	}

	public boolean addSCAttachment(AttachPayment attachPayment) throws DatabaseOperationException{
		AttachPayment dbObj = this.getSCPaymentAttachment(attachPayment);
		if (dbObj!=null)
			throw new DatabaseOperationException(
					"Attachment: " + attachPayment.getFileName() 
					+ " SequenceNo: " + attachPayment.getSequenceNo() + " already exist in database\n" 
					+ "JobNo: " + attachPayment.getPaymentCert().getJobNo()
					+ " Subcontract: " + attachPayment.getPaymentCert().getPackageNo()
					+ " PaymentCertNo: " + attachPayment.getPaymentCert().getPaymentCertNo()
					+ " PaymentCertID: " + attachPayment.getPaymentCert().getId()
					); 
		attachPayment.setCreatedDate(new Date());
		saveOrUpdate(attachPayment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(AttachPayment attachPayment, String user) throws Exception{

		try {
			attachPayment.setLastModifiedUser(user);
			return saveAttachPayment(attachPayment);
		}catch (DatabaseOperationException dbe){
			attachPayment.setCreatedUser(user);
			return addSCAttachment(attachPayment);
		}
	}
	
	public long deleteAttachmentByByPaymentCertID(Long paymentCertID) throws DatabaseOperationException {
		long noOfRecord = 0;
		getSession().clear();
		Query query = getSession().createQuery("delete from AttachPayment attachPayment where Payment_Cert_ID =" + paymentCertID);
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}

}
