/**
 * GammonQS-PH3
 * MainCertificateAttachmentHBDaoImpl.java
 * @author tikywong
 * created on Jan 18, 2012 4:08:20 PM
 * 
 */
package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AttachMainCert;
import com.gammon.qs.domain.MainCert;
@Repository
public class AttachMainCertHBDao extends BaseHibernateDao<AttachMainCert> {

	public AttachMainCertHBDao(){
		super(AttachMainCert.class);
	}

	public AttachMainCert obtainAttachment(Long mainCertID, Integer sequenceNo) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		
		try{
		criteria.add(Restrictions.eq("mainCertificate.id", mainCertID));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		
		return (AttachMainCert) criteria.uniqueResult();
		}catch(HibernateException e){
			throw new DatabaseOperationException(e);
		}
	}

	public AttachMainCert obtainMainCertAttachment(MainCert mainContractCertificate, Integer sequenceNo) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		
		try{
		criteria.add(Restrictions.eq("mainCert", mainContractCertificate));		
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		
		return (AttachMainCert) criteria.uniqueResult();
		}catch(HibernateException e){
			throw new DatabaseOperationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachMainCert> obtainMainCertAttachmentList(MainCert mainCert) throws DatabaseOperationException{
		try{
			List<AttachMainCert> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCert", mainCert));	
			criteria.addOrder(Order.asc("sequenceNo"));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
}
