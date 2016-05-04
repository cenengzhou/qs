package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCDetailsAttachment;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;

@Repository
public class SCDetailAttachmentHBDao extends BaseHibernateDao<SCDetailsAttachment> {

	private Logger logger = Logger.getLogger(SCDetailAttachmentHBDao.class.getName());
	public SCDetailAttachmentHBDao() {
		super(SCDetailsAttachment.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetailsAttachment> getSCDetailsAttachment(String jobNumber, String subcontractNo, String scDetailSequenceNo) throws DatabaseOperationException{
		try{
			List<SCDetailsAttachment> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scDetails", "scDetails");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scDetails.sequenceNo", new Integer (scDetailSequenceNo)));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<SCDetailsAttachment>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	@SuppressWarnings("unchecked")
	public List<SCDetailsAttachment> getSCDetailsAttachment(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetailsAttachment> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scDetails", "scDetails");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<SCDetailsAttachment>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetailsAttachment> getSCDetailsAttachment(SCDetails scDetail) throws DatabaseOperationException{
		try{
			List<SCDetailsAttachment> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scDetails", scDetail));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	@SuppressWarnings("unchecked")
	public List<SCDetailsAttachment> getSCDetailsAttachment(SCPackage scPackage) throws DatabaseOperationException{
		try{
			List<SCDetailsAttachment> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scDetails", "scDetails");
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage", scPackage));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public SCDetailsAttachment getSCDetailsAttachment(SCDetails scDetails, Integer attachmentSequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scDetails",scDetails));
			criteria.add(Restrictions.eq("sequenceNo", attachmentSequenceNo));
			return (SCDetailsAttachment)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetailsAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCDetailsAttachment getSCDetailsAttachment(SCDetailsAttachment scDetailsAttachment) throws DatabaseOperationException{
		try{
			return getSCDetailsAttachment(scDetailsAttachment.getScDetails(), scDetailsAttachment.getSequenceNo());
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetailsAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCDetailsAttachment(SCDetailsAttachment scDetailsAttachment) throws Exception{
		SCDetailsAttachment dbObj = this.getSCDetailsAttachment(scDetailsAttachment);
		if (dbObj==null)
			throw new DatabaseOperationException("Update Fail."); 
		dbObj.setFileLink(scDetailsAttachment.getFileLink());
		dbObj.setFileName(scDetailsAttachment.getFileName());
		dbObj.setTextAttachment(scDetailsAttachment.getTextAttachment());
		dbObj.setDocumentType(scDetailsAttachment.getDocumentType());
		dbObj.setLastModifiedUser(scDetailsAttachment.getLastModifiedUser());
		saveOrUpdate(dbObj);
		return true;
	}

	public boolean addSCAttachment(SCDetailsAttachment scDetailsAttachment) throws DatabaseOperationException{
		SCDetailsAttachment dbObj = this.getSCDetailsAttachment(scDetailsAttachment);
		if (dbObj!=null)
			throw new DatabaseOperationException("Upload Fail.");
		
		scDetailsAttachment.setCreatedDate(new Date());
		saveOrUpdate(scDetailsAttachment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(SCDetailsAttachment SCDetailsAttachment, String user) throws Exception{

		try {
			SCDetailsAttachment.setLastModifiedUser(user);
			return saveSCDetailsAttachment(SCDetailsAttachment);
		}catch (DatabaseOperationException dbe){
			SCDetailsAttachment.setCreatedUser(user);
			return addSCAttachment(SCDetailsAttachment);
		}
	}
}
