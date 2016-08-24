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

import com.gammon.pcms.model.adl.AccountBalance;

@Repository
public class AccountBalanceDao extends BaseAdlHibernateDao<AccountBalance> {

	public AccountBalanceDao() {
		super(AccountBalance.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalance> find(	BigDecimal yearStart,
										BigDecimal yearEnd,
										String typeLedger,
										String noJob,
										String codeObject,
										String codeSubsidiary) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		
		// Where (optional)
		if (yearEnd.intValue() > 0)
			criteria.add(Restrictions.le("fiscalYear", yearEnd));
		if (yearStart.intValue() > 0)
			criteria.add(Restrictions.ge("fiscalYear", yearStart));
		if (StringUtils.isNotBlank(codeObject))
			criteria.add(Restrictions.ilike("accountObject", codeObject, MatchMode.START));
		if (StringUtils.isNotEmpty(codeSubsidiary))
			criteria.add(Restrictions.ilike("accountSubsidiary", codeSubsidiary, MatchMode.START));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));

		return new ArrayList<AccountBalance>(criteria.list());
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalance> findAndGroup(	BigDecimal yearStart,
												BigDecimal yearEnd,
												String typeLedger,
												String noJob) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		
		// Where (optional)
		if (yearEnd.intValue() > 0)
			criteria.add(Restrictions.le("fiscalYear", yearEnd));
		if (yearStart.intValue() > 0)
			criteria.add(Restrictions.ge("fiscalYear", yearStart));
		
		// TODO: FACT_ACCT_BAL_SL_UNPIVOT : group by Select (Convert to DTO)
//		ProjectionList projectionList = Projections.projectionList();
//		projectionList	.add(Projections.property("fiscalYear"), "fiscalYear")
//						.add(Projections.property("accountTypeLedger"), "accountTypeLedger")
//						.add(Projections.property("entityBusinessUnitKey"), "entityBusinessUnitKey")
//						.add(Projections.property("entityCompanyKey"), "entityCompanyKey")
//						.add(Projections.sum("amountPeriod01"), "amountPeriod01")
//						.add(Projections.sum("amountPeriod02"), "amountPeriod02")
//						.add(Projections.sum("amountPeriod03"), "amountPeriod03")
//						.add(Projections.sum("amountPeriod04"), "amountPeriod04")
//						.add(Projections.sum("amountPeriod05"), "amountPeriod05")
//						.add(Projections.sum("amountPeriod06"), "amountPeriod06")
//						.add(Projections.sum("amountPeriod07"), "amountPeriod07")
//						.add(Projections.sum("amountPeriod08"), "amountPeriod08")
//						.add(Projections.sum("amountPeriod09"), "amountPeriod09")
//						.add(Projections.sum("amountPeriod10"), "amountPeriod10")
//						.add(Projections.sum("amountPeriod11"), "amountPeriod11")
//						.add(Projections.sum("amountPeriod12"), "amountPeriod12")
//						.add(Projections.sum("amountPeriod13"), "amountPeriod13")
//						.add(Projections.sum("amountPeriod14"), "amountPeriod14")
//						.add(Projections.sum("amountAccumPeriod01"), "amountAccumPeriod01")
//						.add(Projections.sum("amountAccumPeriod02"), "amountAccumPeriod02")
//						.add(Projections.sum("amountAccumPeriod03"), "amountAccumPeriod03")
//						.add(Projections.sum("amountAccumPeriod04"), "amountAccumPeriod04")
//						.add(Projections.sum("amountAccumPeriod05"), "amountAccumPeriod05")
//						.add(Projections.sum("amountAccumPeriod06"), "amountAccumPeriod06")
//						.add(Projections.sum("amountAccumPeriod07"), "amountAccumPeriod07")
//						.add(Projections.sum("amountAccumPeriod08"), "amountAccumPeriod08")
//						.add(Projections.sum("amountAccumPeriod09"), "amountAccumPeriod09")
//						.add(Projections.sum("amountAccumPeriod10"), "amountAccumPeriod10")
//						.add(Projections.sum("amountAccumPeriod11"), "amountAccumPeriod11")
//						.add(Projections.sum("amountAccumPeriod12"), "amountAccumPeriod12")
//						.add(Projections.sum("amountAccumPeriod13"), "amountAccumPeriod13")
//						.add(Projections.sum("amountAccumPeriod14"), "amountAccumPeriod14")
//						.add(Projections.groupProperty("fiscalYear"))
//						.add(Projections.groupProperty("accountTypeLedger"))
//						.add(Projections.groupProperty("entityBusinessUnitKey"))
//						.add(Projections.groupProperty("entityCompanyKey"));
//		criteria.setProjection(projectionList);
//		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountBalance.class));
		
		// Order By
		criteria.addOrder(Order.asc("fiscalYear"));
		return new ArrayList<AccountBalance>(criteria.list());
	}

	/**
	 * To select an appropriate column based on the month
	 *
	 * @param month
	 * @param cumulative
	 * @return
	 * @author tikywong
	 * @since Jun 22, 2016 5:28:41 PM
	 */
	@SuppressWarnings("unused")
	private String columnSelection(BigDecimal month, boolean cumulative) {
		String amountAccumPeriod = "amountAccumPeriod";
		String amountPeriod = "amountPeriod";
		switch (month.intValue()) {
		case 1:
			amountAccumPeriod += "01";
			amountPeriod += "01";
			break;
		case 2:
			amountAccumPeriod += "02";
			amountPeriod += "02";
			break;
		case 3:
			amountAccumPeriod += "03";
			amountPeriod += "03";
			break;
		case 4:
			amountAccumPeriod += "04";
			amountPeriod += "04";
			break;
		case 5:
			amountAccumPeriod += "05";
			amountPeriod += "05";
			break;
		case 6:
			amountAccumPeriod += "06";
			amountPeriod += "06";
			break;
		case 7:
			amountAccumPeriod += "07";
			amountPeriod += "07";
			break;
		case 8:
			amountAccumPeriod += "08";
			amountPeriod += "08";
			break;
		case 9:
			amountAccumPeriod += "09";
			amountPeriod += "09";
			break;
		case 10:
			amountAccumPeriod += "10";
			amountPeriod += "10";
			break;
		case 11:
			amountAccumPeriod += "11";
			amountPeriod += "11";
			break;
		case 12:
			amountAccumPeriod += "12";
			amountPeriod += "12";
			break;
		}

		if (cumulative)
			return amountAccumPeriod;
		else
			return amountPeriod;
	}
}
