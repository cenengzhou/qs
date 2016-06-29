package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.DimAccountMaster;
import com.gammon.pcms.model.adl.RptIvAcctBalanceSl;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Repository
public class RptIvAcctBalanceSlDao extends BaseAdlHibernateDao<RptIvAcctBalanceSl> {

	public RptIvAcctBalanceSlDao() {
		super(RptIvAcctBalanceSl.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<RptIvAcctBalanceSl> obtainRptIvAcctBalanceSl(DimAccountMaster dimAccountMaster,
			BigDecimal fiscalYear, String accountPeriod, String accountSubLedger, String accountTypeSubLedger) 
	throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("dimAccountMaster", dimAccountMaster));
		criteria.add(Restrictions.eq("fiscalYear", fiscalYear));
		criteria.add(Restrictions.eq("accountPeriod", accountPeriod));
		criteria.add(Restrictions.eq("accountSubLedger", accountSubLedger));
		criteria.add(Restrictions.eq("accountTypeSubLedger", accountTypeSubLedger));
		List<RptIvAcctBalanceSl> rptIvAcctBalanceSlList = criteria.list();
		return rptIvAcctBalanceSlList;
	}

}
