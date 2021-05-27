package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gammon.pcms.model.adl.AccountBalanceFigure;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountBalance;

@Repository
public class AccountBalanceDao extends BaseAdlHibernateDao<AccountBalance> {

	public AccountBalanceDao() {
		super(AccountBalance.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalance> find(	BigDecimal year,
										BigDecimal month,
										String ledgerType,
										String jobNo,
										String objectCode,
										String subsidiaryCode) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));
		
		// Where (optional)
		if (year.intValue() > 0)
			criteria.add(Restrictions.eq("fiscalYear", year));
		if (month.intValue() > 0)
			criteria.add(Restrictions.eq("accountPeriod", month));
		if (StringUtils.isNotBlank(objectCode))
			criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		if (StringUtils.isNotEmpty(subsidiaryCode))
			criteria.add(Restrictions.ilike("accountSubsidiary", subsidiaryCode, MatchMode.START));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));

		return new ArrayList<AccountBalance>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	public List<BigDecimal> findFiguresOnly(BigDecimal year,
											BigDecimal month,
											String ledgerType,
											String jobNo,
											String objectCode,
											String subsidiaryCode) throws DataAccessException {
//		System.out.println("year: "+year+" month: "+month+" ledgerType: "+ledgerType+" jobNo: "+" objectCode: "+objectCode+" subsidiaryCode: "+subsidiaryCode);
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));
		
		// Where (optional)
		if (year.intValue() > 0)
			criteria.add(Restrictions.eq("fiscalYear", year));
		if (month.intValue() > 0)
			criteria.add(Restrictions.eq("accountPeriod", month));
		if (StringUtils.isNotBlank(objectCode))
			criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		if (StringUtils.isNotEmpty(subsidiaryCode))
			criteria.add(Restrictions.ilike("accountSubsidiary", subsidiaryCode, MatchMode.START));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));
		
		// group By
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("amountAccum"), "cumulativeAmount"));
		
		return new ArrayList<BigDecimal>(criteria.list());
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalanceFigure> findFiguresByRangeYearMonth(BigDecimal startYearMonth,
																  BigDecimal endYearMonth,
																  String ledgerType,
																  String jobNo,
																  String objectCode,
																  String subsidiaryCode) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));
//		if (year.intValue() > 0)
//			criteria.add(Restrictions.eq("fiscalYear", year));
		if (StringUtils.isNotBlank(objectCode))
			criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		if (StringUtils.isNotEmpty(subsidiaryCode))
			criteria.add(Restrictions.ilike("accountSubsidiary", subsidiaryCode, MatchMode.START));
		criteria.add(Restrictions.le("accountPeriod", new BigDecimal(12)));
		criteria.add(Restrictions.sqlRestriction("(FISCAL_YEAR*100+ACCOUNT_PERIOD) >= " + startYearMonth));
		criteria.add(Restrictions.sqlRestriction("(FISCAL_YEAR*100+ACCOUNT_PERIOD) <= " + endYearMonth));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"));

		// Select result
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("fiscalYear"), "year");
		projectionList.add(Projections.property("accountPeriod"), "month");
		projectionList.add(Projections.property("amountAccum"), "amount");
		projectionList.add(Projections.sqlProjection("FISCAL_YEAR*100+ACCOUNT_PERIOD as yearMonth", new String[]{"yearMonth"}, new Type[]{StandardBasicTypes.BIG_DECIMAL}));
		criteria.setProjection(projectionList);

		List<AccountBalanceFigure> list = null;

		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountBalanceFigure.class));

		try {
			list = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Delegated to calculate Sum of Actual Value
	 *
	 * @param year
	 * @param month
	 * @param ledgerType
	 * @param jobNo
	 * @param objectCode
	 * @param subsidiaryCode
	 * @return
	 * @throws DataAccessException
	 * @author	tikywong
	 * @since	Aug 25, 2016 5:39:03 PM
	 */
	@SuppressWarnings("unchecked")
	public List<AccountBalance> calculateSumOfActualValue(	BigDecimal year,
														BigDecimal month,
														String ledgerType,
														String jobNo,
														String objectCode,
														String subsidiaryCode) throws DataAccessException {
//		System.out.println("year: "+year+" month: "+month+" ledgerType: "+ledgerType+" jobNo: "+" objectCode: "+objectCode+" subsidiaryCode: "+subsidiaryCode);
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));
		
		// Where (optional)
		if (year.intValue() > 0)
			criteria.add(Restrictions.eq("fiscalYear", year));
		if (month.intValue() > 0)
			criteria.add(Restrictions.eq("accountPeriod", month));
		criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		criteria.add(Restrictions.ne("accountSubsidiary", subsidiaryCode));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"));
		
		// group By
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sum("amountAccum"), "amountAccum")
				.add(Projections.groupProperty("fiscalYear"), "fiscalYear")
				.add(Projections.groupProperty("accountPeriod"), "accountPeriod"));
				
				
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountBalance.class));
		
		return criteria.list();
	}

	public List<AccountBalanceFigure> calculateSumOfActualValueByRangeYearMonth(BigDecimal startYearMonth,
																				BigDecimal endYearMonth,
																				String ledgerType,
																				String jobNo,
																				String objectCode,
																				String subsidiaryCode) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));

		// Where (optional)
//		if (year.intValue() > 0)
//			criteria.add(Restrictions.eq("fiscalYear", year));
//		if (month.intValue() > 0)
//			criteria.add(Restrictions.eq("accountPeriod", month));
		criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		criteria.add(Restrictions.ne("accountSubsidiary", subsidiaryCode));
		criteria.add(Restrictions.le("accountPeriod", new BigDecimal(12)));
		criteria.add(Restrictions.sqlRestriction("(FISCAL_YEAR*100+ACCOUNT_PERIOD) >= " + startYearMonth));
		criteria.add(Restrictions.sqlRestriction("(FISCAL_YEAR*100+ACCOUNT_PERIOD) <= " + endYearMonth));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"));

		// group By
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("fiscalYear"), "year")
				.add(Projections.groupProperty("accountPeriod"), "month")
				.add(Projections.sum("amountAccum"), "amount"));


		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountBalanceFigure.class));

		List<AccountBalanceFigure> result = null;
		try {
			result = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
