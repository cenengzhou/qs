package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AttachSubcontract;

@Repository
public class AttachSubcontractHBDao extends BaseHibernateDao<AttachSubcontract> {

	private Logger logger = Logger.getLogger(AttachSubcontractHBDao.class.getName());
	public AttachSubcontractHBDao() {
		super(AttachSubcontract.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachSubcontract> getSCAttachment(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<AttachSubcontract> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));		
 			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
 			criteria.addOrder(Order.asc("sequenceNo"));
			resultList = criteria.list();
			if(resultList == null)
				resultList = new ArrayList<AttachSubcontract>();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachSubcontract obtainSCFileAttachment(String jobNumber, String subcontractNo, Integer sequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
			return (AttachSubcontract)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: obtainSCPackageAttachment(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public AttachSubcontract getSCAttachment(AttachSubcontract scAttachment) throws DatabaseOperationException{
		try{
			return obtainSCFileAttachment(scAttachment.getSubcontract().getJobInfo().getJobNumber(), scAttachment.getSubcontract().getPackageNo(),scAttachment.getSequenceNo());
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetail(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCAttachment(AttachSubcontract scAttachment) throws Exception{
		AttachSubcontract dbObj = this.getSCAttachment(scAttachment);
		if (dbObj==null)
			throw new DatabaseOperationException("SC Detail did not exist. Update Fail"); 
		dbObj.setFileLink(scAttachment.getFileLink());
		dbObj.setFileName(scAttachment.getFileName());
		dbObj.setDocumentType(scAttachment.getDocumentType());
		dbObj.setLastModifiedUser(scAttachment.getLastModifiedUser());
		dbObj.setTextAttachment(scAttachment.getTextAttachment());
		saveOrUpdate(dbObj);
		return true;
	}

	public boolean addSCAttachment(AttachSubcontract scAttachment) throws DatabaseOperationException{
		AttachSubcontract dbObj = this.getSCAttachment(scAttachment);
		if (dbObj!=null)
			throw new DatabaseOperationException(
					"Attachment:" + scAttachment.getFileName() 
					+ " SequenceNo: " + scAttachment.getSequenceNo() + " already exist in database\n" 
					+ "JobNo:" + scAttachment.getSubcontract().getJobInfo().getJobNumber() 
					+ "SubcontractNo:" + scAttachment.getSubcontract().getPackageNo()
					+ "SubcontractID:" + scAttachment.getSubcontract().getId()
					);
		scAttachment.setCreatedDate(new Date());
		saveOrUpdate(scAttachment);
		return true;
	}
	//public boolean addUpdateSCTextAttachment(String nameObject, String textKey, Integer sequenceNo, String text) throws Exception{
	public boolean addUpdateSCTextAttachment(AttachSubcontract scAttachment, String user) throws Exception{

		try {
			scAttachment.setLastModifiedUser(user);
			return saveSCAttachment(scAttachment);
		}catch (DatabaseOperationException dbe){
			scAttachment.setCreatedUser(user);
			return addSCAttachment(scAttachment);
		}
		
//		for (int i = 0; i < dbObjList.size();i++){
//			if (dbObjList.get(i).getSequenceNo() == sequenceNo){
//				saveSCAttachment(dbObj);
//				return true;
//			}
//		}
//		addSCAttachment(dbObj);
//		logger.info("Tester: C");
//		return true;
	}
/*	public String getSCTextAttachmentContent(String nameObject,
			String textKey, Integer sequenceNumber) throws Exception {
		String splittedTextKey[] = textKey.split("\\|");
		if ("GT58010".equalsIgnoreCase(nameObject))
//			if (this.getSCAttachment(splittedTextKey[0], splittedTextKey[1], sequenceNumber)!=null) 
				return this.getSCAttachment(splittedTextKey[0], splittedTextKey[1], sequenceNumber).getTextAttachment();
//			else
//				return "Attachment not exist";}
		else
			return "This nameObject is invalid";
	}*/
/*	public boolean deletSCTextAttachment(SCAttachment scAttachment)throws Exception{
		this.delete(scAttachment);
		return true;
	}*/
}
