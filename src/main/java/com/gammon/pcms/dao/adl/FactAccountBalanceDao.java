package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dto.rs.provider.response.JobCostDTO;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Repository
public class FactAccountBalanceDao extends BaseAdlHibernateDao<FactAccountBalance> {

	public FactAccountBalanceDao() {
		super(FactAccountBalance.class);
	}

	@SuppressWarnings("unchecked")
	public List<FactAccountBalance> findByFiscalYearRangeAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountObject(	BigDecimal yearStart,
																														BigDecimal yearEnd,
																														String typeLedger,
																														String noJob,
																														String codeObject) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		// Preset values
		String codeSubsidiary = "        "; // To filter non-cost code

		// Where
		criteria.add(Restrictions.le("fiscalYear", yearEnd))
				.add(Restrictions.ge("fiscalYear", yearStart))
				.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountObject", codeObject))
				.add(Restrictions.eq("accountSubsidiary", codeSubsidiary));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"));

		return criteria.list();
	}

	/**
	 * Delegated for Monthly Job Cost by Sub-contract only
	 *
	 * @param year
	 * @param month
	 * @param typeLedger
	 * @param noJob
	 * @param noSubcontract
	 * @return
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Jun 24, 2016 9:30:05 AM
	 */
	@SuppressWarnings("unchecked")
	public List<JobCostDTO> findMonthlyJobCostBySubcontract(BigDecimal year,
															BigDecimal month,
															String typeLedger,
															String noJob,
															String noSubcontract) throws DatabaseOperationException {
		List<JobCostDTO> jobCostDTOList = new ArrayList<JobCostDTO>();

		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		noSubcontract = StringUtils.rightPad(StringUtils.defaultString(noSubcontract), 8);
		
		// Preset values
		String typeSubLedger = "X";
		String codeObject = "1";			// To filter cost account codes that are limited to start with "1"
		String codeSubsidiary = "        "; // To filter non-cost code

		// Join
		criteria.createAlias("dimAccountMaster", "dimAccountMaster");

		// Where
		criteria.add(Restrictions.eq("fiscalYear", year))
				.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.eq("accountTypeSubLedger", typeSubLedger))
				.add(Restrictions.eq("accountSubLedger", noSubcontract))
				.add(Restrictions.like("accountObject", codeObject, MatchMode.START))
				.add(Restrictions.ne("accountSubsidiary", codeSubsidiary));

		// Order By
		criteria.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));

		// Select (Convert to DTO)
		ProjectionList projectionList = Projections.projectionList();
		projectionList	.add(Projections.property("accountTypeLedger"), "typeLedger")
						.add(Projections.property("accountObject"), "codeObject")
						.add(Projections.property("accountSubsidiary"), "codeSubsidiary")
						.add(Projections.property("dimAccountMaster.accountDescription"), "accountDescription")
						.add(Projections.property("currencyLocal"), "currencyLocal")
						.add(Projections.property(columnSelection(month, true)), "amountCumulative")
						.add(Projections.property(columnSelection(month, false)), "amountMovement");
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(JobCostDTO.class));

		jobCostDTOList.addAll(criteria.list());

		return jobCostDTOList;
	}

	/**
	 * Delegated for Monthly Job Cost only
	 *
	 * @param year
	 * @param month
	 * @param typeLedger
	 * @param noJob
	 * @return
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Jun 24, 2016 9:31:42 AM
	 */
	@SuppressWarnings("unchecked")
	public List<JobCostDTO> findMonthlyJobCost(	BigDecimal year,
												BigDecimal month,
												String typeLedger,
												String noJob) throws DatabaseOperationException {
		List<JobCostDTO> jobCostDTOList = new ArrayList<JobCostDTO>();

		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		
		// Preset values
		String codeObject = "1";			// To filter cost account codes that are limited to start with "1"
		String codeSubsidiary = "        "; // To filter non-cost code

		// Join
		criteria.createAlias("dimAccountMaster", "dimAccountMaster");

		// Where
		criteria.add(Restrictions.eq("fiscalYear", year))
				.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob))
				.add(Restrictions.like("accountObject", codeObject, MatchMode.START))
				.add(Restrictions.ne("accountSubsidiary", codeSubsidiary));

		// Order By
		criteria.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));

		// Select (Convert to DTO)
		ProjectionList projectionList = Projections.projectionList();
		projectionList	.add(Projections.groupProperty("accountTypeLedger"), "typeLedger")
						.add(Projections.groupProperty("accountObject"), "codeObject")
						.add(Projections.groupProperty("accountSubsidiary"), "codeSubsidiary")
						.add(Projections.groupProperty("dimAccountMaster.accountDescription"), "accountDescription")
						.add(Projections.groupProperty("currencyLocal"), "currencyLocal")
						.add(Projections.sum(columnSelection(month, true)), "amountCumulative")
						.add(Projections.sum(columnSelection(month, false)), "amountMovement");
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(JobCostDTO.class));

		jobCostDTOList.addAll(criteria.list());

		return jobCostDTOList;
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
