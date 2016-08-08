package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountBalanceAAJISC;
import com.gammon.pcms.model.adl.AccountLedger;

@Repository
public class AccountLedgerDao extends BaseAdlHibernateDao<AccountLedger> {

	public AccountLedgerDao() {
		super(AccountLedger.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountLedger> find(BigDecimal yearStart, BigDecimal yearEnd, BigDecimal monthStart, BigDecimal monthEnd, String typeLedger, String typeDocument, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		typeDocument = typeDocument.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		noSubcontract = StringUtils.isNotBlank(noSubcontract) ?StringUtils.rightPad(StringUtils.defaultString(noSubcontract), 8) : "";
		
		// Where
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountTypeLedger", typeLedger));
		
		Criterion sameYear = Restrictions.and(
				Restrictions.eq("accountFiscalYear", yearStart),
				Restrictions.eq("accountFiscalYear", yearEnd),
				Restrictions.ge("accountPeriod", monthStart)
				);
		Criterion middlePeriod = Restrictions.and(
				Restrictions.gt("accountFiscalYear", yearStart),
				Restrictions.lt("accountFiscalYear", yearEnd)
				);
		Criterion lastYear = Restrictions.and(
				Restrictions.eq("accountFiscalYear", yearEnd),
				Restrictions.le("accountPeriod", monthEnd)
				);
		if(yearEnd.intValue() > 0 && yearStart.intValue() > 0){
			if(yearEnd.intValue() < yearStart.intValue()){
			// yearEnd < yearStart
				throw new IllegalArgumentException("yearEnd less then yearStart");
			} else if(yearEnd.intValue() == yearStart.intValue()){
			// yearEnd == yearStart
				if(monthEnd.intValue() < monthStart.intValue()){
					throw new IllegalArgumentException("monthEnd less then monthStart when yearEnd eq year yearStart");
				}
				criteria.add(sameYear);
			} else if(yearEnd.intValue() > yearStart.intValue()){
			// yearEnd > yearStart
				criteria.add(Restrictions.or(sameYear, middlePeriod, lastYear));
			}
		}
		
		// Where (optional)
		if(StringUtils.isNotBlank(typeDocument))
			criteria.add(Restrictions.eq("typeDocument", typeDocument));
		if (StringUtils.isNotBlank(noSubcontract)) {
			criteria.add(Restrictions.eq("accountSubLedger", noSubcontract));
			criteria.add(Restrictions.eq("accountTypeSubLedger", AccountBalanceAAJISC.TYPE_SUBLEDGER_X));
		}
		if (StringUtils.isNotBlank(codeObject))
			criteria.add(Restrictions.ilike("accountObject", codeObject, MatchMode.START));
		if (StringUtils.isNotEmpty(codeSubsidiary))
			criteria.add(Restrictions.ilike("accountSubsidiary", codeSubsidiary, MatchMode.START));

		// Order by
		criteria.addOrder(Order.asc("accountFiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("typeDocument"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"))
				.addOrder(Order.asc("accountSubLedger"));
				
				
		return new ArrayList<AccountLedger>(criteria.list());
	}
}
