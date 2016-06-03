package com.gammon.qs.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.MainCertReceiveDateQueryManager.getMainCertReceiveDate.GetMainCertReceiveDateResponseObj;
import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ContractReceivableWrapper;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
@Repository
public class MainContractCertificateHBDao extends BaseHibernateDao<MainContractCertificate> {
	private static final int RECORDS_PER_PAGE = 50;
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;

	public MainContractCertificateHBDao() {
		super(MainContractCertificate.class);
	}

	public MainContractCertificate obtainMainContractCert(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		MainContractCertificate result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.eq("certificateNumber", mainCertNumber));
			result = (MainContractCertificate) criteria.uniqueResult();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
		return result;
	}

	public MainContractCertificate getMainContractCertCC(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		MainContractCertificate result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.eq("certificateNumber", mainCertNumber));
			criteria.setFetchMode("contraChargeList", FetchMode.JOIN);
			result = (MainContractCertificate) criteria.uniqueResult();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MainContractCertificate> getMainContractCertList(String jobNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		List<MainContractCertificate> result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.addOrder(Order.asc("certificateNumber"));
			result = criteria.list();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
		return result;
	}


	/**
	 * koeyyeung
	 * Created on Nov 2, 2015
	 * Contract Receivable Settlement Report
	 */
	@SuppressWarnings("unchecked")
	public List<ContractReceivableWrapper> obtainContractReceivableList(ContractReceivableWrapper wrapper, List<String> companyList) throws DatabaseOperationException {
		
		DetachedCriteria subquery = DetachedCriteria.forClass(Job.class);
		if (StringUtils.isNotBlank(wrapper.getJobNo()))
			subquery.add(Restrictions.ilike("jobNumber", wrapper.getJobNo(), MatchMode.START));
		if (StringUtils.isNotBlank(wrapper.getCompany()))
			subquery.add(Restrictions.ilike("company", wrapper.getCompany(), MatchMode.START));
		
		if(companyList!= null && companyList.size()>0 && !companyList.contains("NA")){
			subquery.add(Restrictions.in("company", companyList));
		}
		if (StringUtils.isNotBlank(wrapper.getDivision()))
			subquery.add(Restrictions.ilike("division", wrapper.getDivision(), MatchMode.START));
		
		
		
		subquery.setProjection(Property.forName("jobNumber"));
		
		Criteria criteria = getSession().createCriteria(ContractReceivableWrapper.class);
		criteria.add(Subqueries.propertyIn("jobNo", subquery));
		criteria.addOrder(Order.asc("division")).addOrder(Order.asc("company")).addOrder(Order.asc("jobNo"));
		
		
		return criteria.list();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<MainContractCertificate> obtainMainContractCertList(MainContractCertEnquirySearchingWrapper searchWrapper) throws DatabaseOperationException {
		if (searchWrapper==null)
			throw new IllegalArgumentException("Search Wrapper is null");
		PaginationWrapper<MainContractCertificate> wrapper = new PaginationWrapper<MainContractCertificate>();
		try {
			DetachedCriteria subquery = DetachedCriteria.forClass(Job.class);
			if (StringUtils.isNotBlank(searchWrapper.getJobNo()))
				subquery.add(Restrictions.ilike("jobNumber", searchWrapper.getJobNo(), MatchMode.START));
			if (StringUtils.isNotBlank(searchWrapper.getCompanyNo()))
				subquery.add(Restrictions.ilike("company", searchWrapper.getCompanyNo(), MatchMode.START));
			if (StringUtils.isNotBlank(searchWrapper.getDivisionCode()))
				subquery.add(Restrictions.ilike("division", searchWrapper.getDivisionCode(), MatchMode.START));
			
			subquery.setProjection(Property.forName("jobNumber"));
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Subqueries.propertyIn("jobNo", subquery));
			if(!searchWrapper.getStatus().trim().contentEquals(""))
				criteria.add(Restrictions.and(Restrictions.eq("certificateStatus", searchWrapper.getStatus()), Subqueries.propertyIn("jobNo", subquery)));
			else
				criteria.add(Subqueries.propertyIn("jobNo", subquery));
			criteria.addOrder(Order.asc("jobNo"));
			criteria.addOrder(Order.desc("certificateNumber"));
			return criteria.list();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
	}

	/**
	 * @author matthewatc
	 * 16:05:58 19 Dec 2011 (UTC+8)
	 * Added method to fetch certificate by page, uses PaginationWrapper. The page length is specified above as RECORDS_PER_PAGE.
	 */
	@SuppressWarnings("unchecked")
	public PaginationWrapper<MainContractCertificate> getMainContractCertificateByPage(String jobNumber, int pageNum) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		PaginationWrapper<MainContractCertificate> wrapper = new PaginationWrapper<MainContractCertificate>();
		try {
			// get total number of records
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.addOrder(Order.desc("certificateNumber"));
			criteria.setProjection(Projections.rowCount());
			Integer count = Integer.valueOf(criteria.uniqueResult().toString());

			wrapper.setTotalRecords(count);
			wrapper.setCurrentPage(pageNum);
			wrapper.setTotalPage((count + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE);

			// fetch current page
			criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.addOrder(Order.desc("certificateNumber"));
			criteria.setFirstResult(pageNum * RECORDS_PER_PAGE);
			criteria.setMaxResults(RECORDS_PER_PAGE);
			List<MainContractCertificate> tempResultSet = criteria.list();
			Collections.sort(tempResultSet, new Comparator<MainContractCertificate>(){
				public int compare(MainContractCertificate o1, MainContractCertificate o2) {
					if(o1!=null && o2!=null && o1.getCertificateNumber()!=null && o2.getCertificateNumber()!=null){
						return o1.getCertificateNumber()-o2.getCertificateNumber();
					}
					return 0;
				}
			});
			wrapper.setCurrentPageContentList(tempResultSet);
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
		return wrapper;
	}

	public void saveMainCert(MainContractCertificate obj) throws DatabaseOperationException {
		if (obtainMainContractCert(obj.getJobNo(), obj.getCertificateNumber()) != null)
			throw new DatabaseOperationException("Main Cert was existed already.");
		super.saveOrUpdate(obj);
	}

	public void updateMainCert(MainContractCertificate obj) throws DatabaseOperationException {
		if (obtainMainContractCert(obj.getJobNo(), obj.getCertificateNumber()) == null)
			throw new DatabaseOperationException("Main Cert was not existed");
		super.saveOrUpdate(obj);
	}

	public Date getAsAtDate(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		MainContractCertificate result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.eq("certificateNumber", mainCertNumber));
			result = (MainContractCertificate) criteria.uniqueResult();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}

		return result.getCertAsAtDate();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public MainContractCertificate searchLatestPostedCert(String jobNumber) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo", jobNumber));
		criteria.add(Restrictions.ge("certificateStatus", "300"));
		// criteria.setProjection(Projections.max("certificateNumber"));
		criteria.addOrder(Order.desc("certificateNumber"));
		List<MainContractCertificate> result = criteria.list();
		if (result != null && result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	
	/*
	 * Created by xethhung, 07-08-2015
	 * Query for main contract cert that contains Contra Charge
	 */
	@SuppressWarnings("unchecked")
	public List<MainContractCertificate> getMainContractCertListWithContraCharge(String jobNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		List<MainContractCertificate> result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.gt("certifiedContraChargeAmount", 0.0));
						
			criteria.addOrder(Order.asc("certificateNumber"));
			result = criteria.list();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
		return result;
	}


	/**
	 * @deprecated
	 */
	public GetMainCertReceiveDateResponseObj getMainCertReceiveDateAndAmount(String company, String refDocNo) {
		return null;
	}

	
	/**@author koeyyeung
	 * created on 5th Aug, 2015
	 ** Call Stored Procedure to update Actual Receipt Date of Main Cert from F03B14**/
	public Boolean callStoredProcedureToUpdateMainCert() throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call "+sfi.getSettings().getDefaultSchemaName() +  "."+storedProcedureConfig.getStoredProcedureUpdateMainCert()+"}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
//		sfi.close(); //DatabaseOperationException: org.hibernate.SessionException: Session is closed!
		
		
        completed = true;
		
		return completed;
	}
	
}
