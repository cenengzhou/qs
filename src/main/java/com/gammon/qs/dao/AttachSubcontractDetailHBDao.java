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
import com.gammon.qs.domain.AttachSubcontractDetail;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.Subcontract;

@Repository
public class AttachSubcontractDetailHBDao extends BaseHibernateDao<AttachSubcontractDetail> {

	private Logger logger = Logger.getLogger(AttachSubcontractDetailHBDao.class.getName());
	public AttachSubcontractDetailHBDao() {
		super(AttachSubcontractDetail.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachSubcontractDetail> getSCDetailsAttachment(String jobNumber, String subcontractNo, String scDetailSequenceNo) throws DatabaseOperationException{
		try{
			List<AttachSubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scDetails", "scDetails");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("scDetails.sequenceNo", new Integer (scDetailSequenceNo)));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<AttachSubcontractDetail>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	@SuppressWarnings("unchecked")
	public List<AttachSubcontractDetail> getSCDetailsAttachment(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<AttachSubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.createAlias("scDetails", "scDetails");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<AttachSubcontractDetail>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachSubcontractDetail> getSCDetailsAttachment(SubcontractDetail scDetail) throws DatabaseOperationException{
		try{
			List<AttachSubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scDetails", scDetail));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	@SuppressWarnings("unchecked")
	public List<AttachSubcontractDetail> getSCDetailsAttachment(Subcontract scPackage) throws DatabaseOperationException{
		try{
			List<AttachSubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scDetails", "scDetails");
			criteria.createAlias("scDetails.scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage", scPackage));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public AttachSubcontractDetail getSCDetailsAttachment(SubcontractDetail scDetails, Integer attachmentSequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scDetails",scDetails));
			criteria.add(Restrictions.eq("sequenceNo", attachmentSequenceNo));
			return (AttachSubcontractDetail)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetailsAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachSubcontractDetail getSCDetailsAttachment(AttachSubcontractDetail scDetailsAttachment) throws DatabaseOperationException{
		try{
			return getSCDetailsAttachment(scDetailsAttachment.getSubcontractDetail(), scDetailsAttachment.getSequenceNo());
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetailsAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCDetailsAttachment(AttachSubcontractDetail scDetailsAttachment) throws Exception{
		AttachSubcontractDetail dbObj = this.getSCDetailsAttachment(scDetailsAttachment);
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

	public boolean addSCAttachment(AttachSubcontractDetail scDetailsAttachment) throws DatabaseOperationException{
		AttachSubcontractDetail dbObj = this.getSCDetailsAttachment(scDetailsAttachment);
		if (dbObj!=null)
			throw new DatabaseOperationException("Upload Fail.");
		
		scDetailsAttachment.setCreatedDate(new Date());
		saveOrUpdate(scDetailsAttachment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(AttachSubcontractDetail SCDetailsAttachment, String user) throws Exception{

		try {
			SCDetailsAttachment.setLastModifiedUser(user);
			return saveSCDetailsAttachment(SCDetailsAttachment);
		}catch (DatabaseOperationException dbe){
			SCDetailsAttachment.setCreatedUser(user);
			return addSCAttachment(SCDetailsAttachment);
		}
	}
}
