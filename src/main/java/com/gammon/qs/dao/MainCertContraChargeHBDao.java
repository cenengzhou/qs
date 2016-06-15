package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCertContraCharge;
import com.gammon.qs.domain.MainCert;
@Repository
public class MainCertContraChargeHBDao extends BaseHibernateDao<MainCertContraCharge> {

	public MainCertContraChargeHBDao() {
		super(MainCertContraCharge.class);
	}
	
	public MainCertContraCharge obtainMainCertContraCharge(String objectCode, String subsidCode, MainCert mainCert) throws DatabaseOperationException{
		MainCertContraCharge result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCertificate", mainCert));
			criteria.add(Restrictions.eq("objectCode", objectCode));
			criteria.add(Restrictions.eq("subsidiary", subsidCode));
			result = (MainCertContraCharge)criteria.uniqueResult();
		}catch (HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MainCertContraCharge> obtainMainCertContraChargeList(String jobNumber) throws DatabaseOperationException{
		List<MainCertContraCharge> result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType(),"cc");
			criteria.createAlias("cc.mainCertificate", "mc")
			   .add(Restrictions.eq("mc.jobNo", jobNumber))
			   .add(Restrictions.gt("mc.certifiedContraChargeAmount", 0.0));
			criteria.addOrder(Order.asc("mc.certificateNumber"));
			result = criteria.list();
		}catch (HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MainCertContraCharge> obtainMainCertContraChargeList(MainCert mainCert) throws DatabaseOperationException{
		List<MainCertContraCharge> result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCertificate", mainCert));
			result = criteria.list();
		}catch (HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	public boolean addMainCertContraCharge(MainCertContraCharge obj) throws DatabaseOperationException{

		if (obtainMainCertContraCharge(obj.getObjectCode(),obj.getSubsidiary(),obj.getMainCertificate())!=null)
			throw new DatabaseOperationException("Main Cert Contra Charge was existed already.");
		super.saveOrUpdate(obj);
		return true;
	}
	public boolean saveMainCertContraCharge(MainCertContraCharge obj) throws DatabaseOperationException{
		if (obtainMainCertContraCharge(obj.getObjectCode(),obj.getSubsidiary(),obj.getMainCertificate())==null)
			throw new DatabaseOperationException("Main Cert Contra Charge does not exist.");
		MainCertContraCharge dbObj = this.obtainMainCertContraCharge(obj.getObjectCode(), obj.getSubsidiary(), obj.getMainCertificate());
		dbObj.setCurrentAmount(obj.getCurrentAmount());
		dbObj.setPostAmount(obj.getPostAmount());
		super.saveOrUpdate(dbObj);
		return true;
	}
	public boolean addUpdate(MainCertContraCharge obj) throws DatabaseOperationException{
		try {
			return saveMainCertContraCharge(obj);
		}catch (DatabaseOperationException dbe){
			return addMainCertContraCharge(obj);
		}
//		save(obj);
//		return true;
	}

	public Integer deleteMainCertContraCharge(MainCert mainContractCertificate) throws DatabaseOperationException {
		List<MainCertContraCharge> mainCertificateContraChargeList = obtainMainCertContraChargeList(mainContractCertificate);
		int count = 0;
		for(MainCertContraCharge mainCertificateContraCharge : mainCertificateContraChargeList){
			delete(mainCertificateContraCharge);
			count++;
		}
		return count;
	}
}
