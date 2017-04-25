package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AttachSubcontractDetail;
import com.gammon.qs.domain.SubcontractDetail;

@Repository
public class AttachSubcontractDetailHBDao extends BaseHibernateDao<AttachSubcontractDetail> {

	private Logger logger = Logger.getLogger(AttachSubcontractDetailHBDao.class.getName());
	public AttachSubcontractDetailHBDao() {
		super(AttachSubcontractDetail.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachSubcontractDetail> getSCDetailsAttachment(SubcontractDetail subcontractDetail) throws DatabaseOperationException{
		try{
			List<AttachSubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("subcontractDetail", subcontractDetail));		
			criteria.addOrder(Order.asc("sequenceNo"));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public AttachSubcontractDetail getSCDetailsAttachment(SubcontractDetail subcontractDetail, Integer attachmentSequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("subcontractDetail",subcontractDetail));
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
			throw new DatabaseOperationException("[Update Fail] getSubcontractDetail:" + scDetailsAttachment.getSubcontractDetail() + " SequenceNo:" + scDetailsAttachment.getSequenceNo()); 
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
			throw new DatabaseOperationException(
					"Attachment: " + scDetailsAttachment.getFileName() 
					+ " SequenceNo: " + scDetailsAttachment.getSequenceNo() + " already exist in database\n" 
							+ "JobNo: " + scDetailsAttachment.getSubcontractDetail().getJobNo()
							+ " Subcontract: " + scDetailsAttachment.getSubcontractDetail().getSubcontract().getPackageNo()
							+ " SD SequenceNo: " + scDetailsAttachment.getSubcontractDetail().getSequenceNo()
							+ " SD SequenceID: " + scDetailsAttachment.getSubcontractDetail().getId()
							);
		
		scDetailsAttachment.setCreatedDate(new Date());
		saveOrUpdate(scDetailsAttachment);
		return true;
	}
	public boolean addUpdateSCTextAttachment(AttachSubcontractDetail attachSubcontractDetail, String user) throws Exception{

		try {
			attachSubcontractDetail.setLastModifiedUser(user);
			return saveSCDetailsAttachment(attachSubcontractDetail);
		}catch (DatabaseOperationException dbe){
			attachSubcontractDetail.setCreatedUser(user);
			return addSCAttachment(attachSubcontractDetail);
		}
	}
}
