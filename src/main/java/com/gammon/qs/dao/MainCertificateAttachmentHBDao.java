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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
@Repository
public class MainCertificateAttachmentHBDao extends BaseHibernateDao<MainCertificateAttachment> {

	public MainCertificateAttachmentHBDao(){
		super(MainCertificateAttachment.class);
	}

	public MainCertificateAttachment obtainAttachment(Long mainCertID, Integer sequenceNo) throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		
		try{
		criteria.add(Restrictions.eq("mainCertificate.id", mainCertID));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		
		return (MainCertificateAttachment) criteria.uniqueResult();
		}catch(HibernateException e){
			throw new DatabaseOperationException(e);
		}
	}

	public MainCertificateAttachment obtainMainCertAttachment(MainContractCertificate mainContractCertificate, Integer sequenceNo) throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		
		try{
		criteria.add(Restrictions.eq("mainCertificate", mainContractCertificate));		
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		
		return (MainCertificateAttachment) criteria.uniqueResult();
		}catch(HibernateException e){
			throw new DatabaseOperationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MainCertificateAttachment> obtainMainCertAttachmentList(MainContractCertificate mainContractCertificate) throws DatabaseOperationException{
		try{
			List<MainCertificateAttachment> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCertificate", mainContractCertificate));		
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
}
