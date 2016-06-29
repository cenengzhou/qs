package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ContractReceivableWrapper;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
@Repository
public class MainCertHBDao extends BaseHibernateDao<MainCert> {
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;

	public MainCertHBDao() {
		super(MainCert.class);
	}

	
	public MainCert findByJobNoAndCertificateNo(String noJob, Integer noMainCert) throws DataAccessException {
		if (GenericValidator.isBlankOrNull(noJob))
			throw new IllegalArgumentException("JobNo is null or empty");

		Criteria criteria = getSession().createCriteria(this.getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob))
				.add(Restrictions.eq("certificateNumber", noMainCert));

		return (MainCert) criteria.uniqueResult();
	}

	/**
	 * koeyyeung
	 * Created on Nov 2, 2015
	 * Contract Receivable Settlement Report
	 */
	@SuppressWarnings("unchecked")
	public List<ContractReceivableWrapper> obtainContractReceivableList(ContractReceivableWrapper wrapper, List<String> companyList) throws DatabaseOperationException {
		
		DetachedCriteria subquery = DetachedCriteria.forClass(JobInfo.class);
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

	public void saveMainCert(MainCert obj) throws DatabaseOperationException {
		if (findByJobNoAndCertificateNo(obj.getJobNo(), obj.getCertificateNumber()) != null)
			throw new DatabaseOperationException("Main Cert was existed already.");
		super.saveOrUpdate(obj);
	}

	public void updateMainCert(MainCert mainCert) throws DataAccessException {
		if (findByJobNoAndCertificateNo(mainCert.getJobNo(), mainCert.getCertificateNumber()) == null)
			throw new DataRetrievalFailureException("Main Cert does not exist");
		
		super.saveOrUpdate(mainCert);
	}

	public Date getAsAtDate(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		MainCert result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.eq("certificateNumber", mainCertNumber));
			result = (MainCert) criteria.uniqueResult();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}

		return result.getCertAsAtDate();
	}

	/**
	 * @author koeyyeung created on 5th Aug, 2015 
	 * Call Stored Procedure to update Actual Receipt Date of Main Cert from F03B14
	 **/
	public Boolean callStoredProcedureToUpdateMainCert() throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call " + sfi.getSettings().getDefaultSchemaName() + "." + storedProcedureConfig.getStoredProcedureUpdateMainCert() + "}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
		completed = true;

		return completed;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - START**************************************************************/
	/**
	 * To find all Main Contract Certificate of a particular job 
	 *
	 * @param noJob
	 * @return
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Jun 27, 2016 11:10:59 AM
	 */
	@SuppressWarnings("unchecked")
	public List<MainCert> findByJobNo(String noJob) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob));
		
		//Order By
		criteria.addOrder(Order.asc("jobNo"))
				.addOrder(Order.asc("certificateNumber"));

		return criteria.list();
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/

}
