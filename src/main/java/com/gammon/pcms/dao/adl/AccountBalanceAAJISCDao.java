package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountBalanceAAJISC;

@Repository
public class AccountBalanceAAJISCDao extends BaseAdlHibernateDao<AccountBalanceAAJISC> {

	public AccountBalanceAAJISCDao() {
		super(AccountBalanceAAJISC.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountBalanceAAJISC> findMonthlyJobCost(BigDecimal year, BigDecimal month, String noJob, String noSubcontract) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());
		// Data Formatting
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		noSubcontract = StringUtils.rightPad(StringUtils.defaultString(noSubcontract), 8);

		// Where
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountSubLedger", noSubcontract))
				.add(Restrictions.eq("accountTypeSubLedger", AccountBalanceAAJISC.TYPE_SUBLEDGER_X))
				.add(Restrictions.ilike("accountObject", AccountBalanceAAJISC.CODE_OBJECT_COSTCODE_STARTER, MatchMode.START))
				.add(Restrictions.ne("accountSubsidiary", AccountBalanceAAJISC.CODE_SUBSIDIARY_EMPTY));
		// Where (optional)
		if (year.intValue() > 0)
			criteria.add(Restrictions.eq("fiscalYear", year));
		if (month.intValue() > 0)
			criteria.add(Restrictions.eq("accountPeriod", month));
		

		// Order by
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));
		
		
		return new ArrayList<AccountBalanceAAJISC>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountBalanceAAJISC> findMonthlyJobCostByPeriodRange(String noJob, String noSubcontract, BigDecimal fromYear, BigDecimal fromMonth, BigDecimal toYear, BigDecimal toMonth) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());
		Criterion sameYear, firstYear, middlePeriod, lastYear;
		// Data Formatting
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		noSubcontract = StringUtils.rightPad(StringUtils.defaultString(noSubcontract), 8);

		// Where
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountSubLedger", noSubcontract))
				.add(Restrictions.eq("accountTypeSubLedger", AccountBalanceAAJISC.TYPE_SUBLEDGER_X))
				.add(Restrictions.ilike("accountObject", AccountBalanceAAJISC.CODE_OBJECT_COSTCODE_STARTER, MatchMode.START))
				.add(Restrictions.ne("accountSubsidiary", AccountBalanceAAJISC.CODE_SUBSIDIARY_EMPTY));
		sameYear = Restrictions.and(
				Restrictions.eq("fiscalYear", fromYear),
				Restrictions.ge("accountPeriod", fromMonth),
				Restrictions.le("accountPeriod", toMonth)
				);
		firstYear = Restrictions.and(Restrictions.eq("fiscalYear", fromYear), Restrictions.ge("accountPeriod", fromMonth));
		middlePeriod = Restrictions.and(Restrictions.gt("fiscalYear", fromYear), Restrictions.le("accountPeriod", toYear));
		lastYear = Restrictions.and(Restrictions.eq("fiscalYear", toYear), Restrictions.le("accountPeriod", toMonth));
		if(toYear.intValue() > 0 && fromYear.intValue() > 0){
			if(toYear.intValue() < fromYear.intValue()){
			// yearEnd < yearStart
				throw new IllegalArgumentException("toYear less then fromYear");
			} else if(toYear.intValue() == fromYear.intValue()){
			// toYear == fromYear
				if(toMonth.intValue() < fromMonth.intValue()){
					throw new IllegalArgumentException("toMonth less then fromMonth when toYear eq fromYear");
				}
				criteria.add(sameYear);
			} else if(toYear.intValue() > fromYear.intValue()){
			// toYear > fromYear
				criteria.add(Restrictions.or(firstYear, middlePeriod, lastYear));
			}
		}
		
		criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("accountObject"), "accountObject")
                .add(Projections.groupProperty("accountSubsidiary"), "accountSubsidiary")
                .add(Projections.sum("aaAmountPeriod"), "aaAmountPeriod")
                .add(Projections.sum("jiAmountPeriod"), "jiAmountPeriod")
                .add(Projections.sum("aaAmountAccum"), "aaAmountAccum")
                .add(Projections.sum("jiAmountAccum"), "jiAmountAccum")
                .add(Projections.min("accountDescription"), "accountDescription")   
                .add(Projections.min("fiscalYear"), "fiscalYear")    
                .add(Projections.min("accountPeriod"), "accountPeriod")    
        );
		
		// Order by
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountBalanceAAJISC.class));
		List<AccountBalanceAAJISC> resultList = criteria.list();
		
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalanceAAJISC> find(BigDecimal yearStart, BigDecimal yearEnd, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());
		
		// Data Formatting
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		noSubcontract = StringUtils.isNotBlank(noSubcontract) ? StringUtils.rightPad(StringUtils.defaultString(noSubcontract), 8) : "";
			
		// Where
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		// Where (optional)
		if (yearEnd.intValue() > 0)
			criteria.add(Restrictions.le("fiscalYear", yearEnd));
		if (yearStart.intValue() > 0)
			criteria.add(Restrictions.ge("fiscalYear", yearStart));
		if (StringUtils.isNotBlank(noSubcontract)) {
			criteria.add(Restrictions.eq("accountSubLedger", noSubcontract));
			criteria.add(Restrictions.eq("accountTypeSubLedger", AccountBalanceAAJISC.TYPE_SUBLEDGER_X));
		}
		if (StringUtils.isNotBlank(codeObject))
			criteria.add(Restrictions.ilike("accountObject", codeObject, MatchMode.START));
		if (StringUtils.isNotEmpty(codeSubsidiary))
			criteria.add(Restrictions.ilike("accountSubsidiary", codeSubsidiary, MatchMode.START));

		// Order by
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"))
				.addOrder(Order.asc("accountSubLedger"));
		
		return new ArrayList<AccountBalanceAAJISC>(criteria.list());
	}
}
