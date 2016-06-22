package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.FactAccountBalance;
@Repository
public class FactAccountBalanceAdlHBDao extends BaseAdlHibernateDao<FactAccountBalance> {
	
	Logger logger = Logger.getLogger(FactAccountBalance.class.getName());
	public FactAccountBalanceAdlHBDao() {
		super(FactAccountBalance.class);
	}

	@SuppressWarnings("unchecked")
	public List<FactAccountBalance> findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubsidiaryNot(
			BigDecimal fiscalYear, String accountTypeLedger, String entityBusinessUnitKey, String accountSubsidiary){
		List<FactAccountBalance> factAccountBalanceList = new ArrayList<FactAccountBalance>();
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("fiscalYear", fiscalYear));
		criteria.add(Restrictions.eq("accountTypeLedger", accountTypeLedger));
		criteria.add(Restrictions.eq("entityBusinessUnitKey", entityBusinessUnitKey));
		criteria.add(Restrictions.ne("accountSubsidiary", accountSubsidiary));
		factAccountBalanceList.addAll(criteria.list());
		return factAccountBalanceList;
	}
}