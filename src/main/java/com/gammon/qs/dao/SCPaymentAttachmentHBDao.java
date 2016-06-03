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
import com.gammon.qs.domain.SCPaymentAttachment;
import com.gammon.qs.domain.SCPaymentCert;

@Repository
public class SCPaymentAttachmentHBDao extends BaseHibernateDao<SCPaymentAttachment> {

	private Logger logger = Logger.getLogger(SCPaymentAttachmentHBDao.class.getName());
	public SCPaymentAttachmentHBDao() {
		super(SCPaymentAttachment.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPaymentAttachment> getSCPaymentAttachment(String jobNumber, String subcontractNo, String paymentCertNo) throws DatabaseOperationException{
		try{
			List<SCPaymentAttachment> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scPaymentCert", "scPaymentCert");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scPaymentCert.paymentCertNo", new Integer(paymentCertNo)));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<SCPaymentAttachment>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCPaymentAttachment getSCPaymentAttachment(SCPaymentCert scPaymentCert, Integer attachmentSequenceNo) throws DatabaseOperationException{
		try{
			SCPaymentCert dbObj = new SCPaymentCert();
			//dbObj = scPaymentCert;
			dbObj.setJobNo(scPaymentCert.getScPackage().getJob().getJobNumber());
			dbObj.setPackageNo(scPaymentCert.getScPackage().getPackageNo());
			dbObj.setPaymentCertNo(scPaymentCert.getPaymentCertNo());
			Criteria criteria = getSession().createCriteria(this.getType());
			/*criteria.createAlias("scPaymentCert.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");*/
			//criteria.createAlias("scPaymentCert", "scPaymentCert");
			/*criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scPaymentCert.paymentCertNo", new Integer (paymentCertNo)));*/
			criteria.add(Restrictions.eq("scPaymentCert", scPaymentCert));
			criteria.add(Restrictions.eq("sequenceNo", attachmentSequenceNo));
			return (SCPaymentAttachment)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPaymentAttachment> getSCPaymentAttachment(SCPaymentCert scPaymentCert) throws DatabaseOperationException{
		try{
			List<SCPaymentAttachment> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPaymentCert", scPaymentCert));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCPaymentAttachment getSCPaymentAttachment(String jobNumber, String subcontractNo,String paymentCertNo, Integer sequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPaymentCert.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scPaymentCert", "scPaymentCert");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scPaymentCert.paymentCertNo", new Integer (paymentCertNo)));
			criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
			return (SCPaymentAttachment)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCPaymentAttachment getSCPaymentAttachment(SCPaymentAttachment scPaymentAttachment) throws DatabaseOperationException{
		try{
			return getSCPaymentAttachment(scPaymentAttachment.getScPaymentCert(),new Integer(scPaymentAttachment.getSequenceNo()));
		}catch (HibernateException he) {
			logger.info("Fail: getSCPaymentAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCPaymentAttachment(SCPaymentAttachment scPaymentAttachment) throws Exception{
		SCPaymentAttachment dbObj = this.getSCPaymentAttachment(scPaymentAttachment);
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

	public boolean addSCAttachment(SCPaymentAttachment scPaymentAttachment) throws DatabaseOperationException{
		SCPaymentAttachment dbObj = this.getSCPaymentAttachment(scPaymentAttachment);
		if (dbObj!=null)
			throw new DatabaseOperationException("Upload Fail."); 
		scPaymentAttachment.setCreatedDate(new Date());
		saveOrUpdate(scPaymentAttachment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(SCPaymentAttachment scPaymentAttachment, String user) throws Exception{

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
