/**
 * GammonQS-PH3
 * AccountLedgerDaoHBImpl.java
 * @author koeyyeung
 * Created on May 9, 2013 11:33:59 AM
 */
package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AccountLedger;
import com.gammon.qs.wrapper.BudgetForecastWrapper;
@Repository
public class AccountLedgerHBDao extends BaseHibernateDao<AccountLedger> {

	public AccountLedgerHBDao() {
		super(AccountLedger.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountLedger> obtainAccountLedgersByJobNo(String jobNo, String ledgerType) throws DatabaseOperationException {
		Criteria criteria = null;
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", jobNo));
		criteria.add(Restrictions.eq("ledgerType", ledgerType));
		
		return criteria.list();
	}
	
	/**
	 * modified by matthewlam, 2015-01-30
	 * Bug fix #97 - add fiscal year field
	 */
	@SuppressWarnings("unchecked")
	public List<BudgetForecastWrapper> obtainAccountLedgersByJobNo(String jobNo, Integer year) throws DatabaseOperationException {
		Criteria criteria = null;
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("this.jobNo", jobNo));
		// sqlRestriction() is used due to Hibernate bug - normal restriction will result in invalid identifier generated in SQL
		if (year != null)
			criteria.add(Restrictions.sqlRestriction("fiscal_Year = " + year));

		criteria.addOrder(Order.asc("objectCode"));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("jobNo"), "jobNo")
				.add(Projections.groupProperty("objectCode"), "objectCode")
				.add(Projections.groupProperty("subledger"), "subledger")
				.add(Projections.groupProperty("subledgerType"), "subledgerType")
				.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.groupProperty("fiscalYear"), "fiscalYear")
		);
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(BudgetForecastWrapper.class));
		return (List<BudgetForecastWrapper>) criteria.list();
	}


	public AccountLedger obtainAccountLedgerByAccountLedger(AccountLedger accountLedger) throws DatabaseOperationException {
		Criteria criteria = null;
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", accountLedger.getJobNo()));
		criteria.add(Restrictions.eq("objectCode", accountLedger.getObjectCode()));
		criteria.add(Restrictions.eq("ledgerType", accountLedger.getLedgerType()));
		if(accountLedger.getSubledger()!=null)
			criteria.add(Restrictions.eq("subledger", accountLedger.getSubledger()));
		else
			criteria.add(Restrictions.isNull("subledger"));
		
		if(accountLedger.getSubledger()!=null){
			criteria.add(Restrictions.eq("subledgerType", accountLedger.getSubledgerType()));
		}
		if(accountLedger.getSubsidiaryCode()!=null)
			criteria.add(Restrictions.eq("subsidiaryCode", accountLedger.getSubsidiaryCode()));
		else
			criteria.add(Restrictions.isNull("subsidiaryCode"));
		
		criteria.add(Restrictions.eq("fiscalYear", accountLedger.getFiscalYear()));
		
		return (AccountLedger) criteria.uniqueResult();
	}


	@SuppressWarnings("unchecked")
	public List<AccountLedger> obtainAccountLedgersByBudgetForecastWrapper(BudgetForecastWrapper budgetForecastWrapper) throws DatabaseOperationException {
		List<AccountLedger> accountLedgerList = new ArrayList<AccountLedger>();
		
		Criteria criteria = null;
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", budgetForecastWrapper.getJobNo()));
		criteria.add(Restrictions.eq("objectCode", budgetForecastWrapper.getObjectCode()));
		criteria.add(Restrictions.eq("ledgerType", "FC"));
		
		if(budgetForecastWrapper.getSubledger()!=null)
			criteria.add(Restrictions.eq("subledger", budgetForecastWrapper.getSubledger()));
		else
			criteria.add(Restrictions.isNull("subledger"));
		if(budgetForecastWrapper.getSubledger()!=null){
			criteria.add(Restrictions.eq("subledgerType", budgetForecastWrapper.getSubledgerType()));
		}
		if(budgetForecastWrapper.getSubsidiaryCode()!=null)
			criteria.add(Restrictions.eq("subsidiaryCode", budgetForecastWrapper.getSubsidiaryCode()));
		else
			criteria.add(Restrictions.isNull("subsidiaryCode"));
		criteria.add(Restrictions.eq("fiscalYear", budgetForecastWrapper.getFiscalYear()));
		
		List<AccountLedger> fc = criteria.list();
		if(fc!=null)
			accountLedgerList.addAll(fc);
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", budgetForecastWrapper.getJobNo()));
		criteria.add(Restrictions.eq("objectCode", budgetForecastWrapper.getObjectCode()));
		criteria.add(Restrictions.eq("ledgerType", "OB"));
		
		if(budgetForecastWrapper.getSubledger()!=null)
			criteria.add(Restrictions.eq("subledger", budgetForecastWrapper.getSubledger()));
		else
			criteria.add(Restrictions.isNull("subledger"));
		if(budgetForecastWrapper.getSubledger()!=null){
			criteria.add(Restrictions.eq("subledgerType", budgetForecastWrapper.getSubledgerType()));
		}
		if(budgetForecastWrapper.getSubsidiaryCode()!=null)
			criteria.add(Restrictions.eq("subsidiaryCode", budgetForecastWrapper.getSubsidiaryCode()));
		else
			criteria.add(Restrictions.isNull("subsidiaryCode"));
		criteria.add(Restrictions.eq("fiscalYear", budgetForecastWrapper.getFiscalYear()));
		
		List<AccountLedger> ob = criteria.list();
		if(ob!=null)
			accountLedgerList.addAll(ob);
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", budgetForecastWrapper.getJobNo()));
		criteria.add(Restrictions.eq("objectCode", budgetForecastWrapper.getObjectCode()));
		criteria.add(Restrictions.eq("ledgerType", "AA"));
		
		if(budgetForecastWrapper.getSubledger()!=null)
			criteria.add(Restrictions.eq("subledger", budgetForecastWrapper.getSubledger()));
		else
			criteria.add(Restrictions.isNull("subledger"));
		if(budgetForecastWrapper.getSubledger()!=null){
			criteria.add(Restrictions.eq("subledgerType", budgetForecastWrapper.getSubledgerType()));
		}
		if(budgetForecastWrapper.getSubsidiaryCode()!=null)
			criteria.add(Restrictions.eq("subsidiaryCode", budgetForecastWrapper.getSubsidiaryCode()));
		else
			criteria.add(Restrictions.isNull("subsidiaryCode"));
		criteria.add(Restrictions.eq("fiscalYear", budgetForecastWrapper.getFiscalYear()));
		
		List<AccountLedger> aa = criteria.list();
		if(aa!=null)
			accountLedgerList.addAll(aa);
		
		
		return accountLedgerList;
	}

	@SuppressWarnings("unchecked")
	public List<AccountLedger> obtainAccountLedgersByLedgerType(String jobNo, String ledgerType) throws DatabaseOperationException {
		Criteria criteria = null;
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE ));
		criteria.add(Restrictions.eq("jobNo", jobNo));
		criteria.add(Restrictions.eq("ledgerType", ledgerType));
		
		return criteria.list();
	}


}
