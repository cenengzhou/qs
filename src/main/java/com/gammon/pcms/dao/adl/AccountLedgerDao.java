package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
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
		
		System.out.println("["+typeLedger+"] ["+typeDocument+"] ["+noJob+"] ["+noSubcontract+"]");
		
		// Where
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountTypeLedger", typeLedger));
		
		if (yearEnd.intValue() > 0)
			criteria.add(Restrictions.le("accountFiscalYear", yearEnd));
		if (yearStart.intValue() > 0)
			criteria.add(Restrictions.ge("accountFiscalYear", yearStart));
		if (monthEnd.intValue() > 0)
			criteria.add(Restrictions.le("accountPeriod", monthEnd));
		if (monthStart.intValue() > 0)
			criteria.add(Restrictions.ge("accountPeriod", monthStart));
		
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
