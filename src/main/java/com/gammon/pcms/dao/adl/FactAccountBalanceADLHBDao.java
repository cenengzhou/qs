package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dto.rs.provider.response.MonthlyContractExpenditureDTO;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Repository
public class FactAccountBalanceADLHBDao extends BaseAdlHibernateDao<FactAccountBalance> {

	Logger logger = Logger.getLogger(FactAccountBalance.class.getName());

	public FactAccountBalanceADLHBDao() {
		super(FactAccountBalance.class);
	}
	
	/**
	 * Find all account codes for each job and its relation figures
	 *
	 * @param year
	 * @param typeLedger
	 * @param noJob
	 * @return
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Jun 22, 2016 2:01:21 PM
	 */
	@SuppressWarnings("unchecked")
	public List<MonthlyContractExpenditureDTO> findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubLedger(	BigDecimal year,
																																BigDecimal month,
																																String typeLedger,
																																String noJob,
																																String noSubcontract) throws DatabaseOperationException {
		List<MonthlyContractExpenditureDTO> monthlyContractExpenditureDTO = new ArrayList<MonthlyContractExpenditureDTO>();

		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		// Preset values
		String codeSubsidiary = "        "; // To filter non-cost code
		
		// Join
		criteria.createAlias("dimAccountMaster", "dimAccountMaster");
				
		// Where
		criteria.add(Restrictions.eq("fiscalYear", year));
		criteria.add(Restrictions.eq("accountTypeLedger", typeLedger));
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		criteria.add(Restrictions.eq("accountSubLedger", noSubcontract));
		criteria.add(Restrictions.ne("accountSubsidiary", codeSubsidiary));	
				
		// Order By
		criteria.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));
						
		// Select (Convert to DTO)
		ProjectionList projectionList = Projections.projectionList();
		projectionList
						.add(Projections.property("accountTypeLedger"), "typeLedger")
						.add(Projections.property("accountObject"), "codeObject")
						.add(Projections.property("accountSubsidiary"), "codeSubsidiary")
						.add(Projections.property("dimAccountMaster.accountDescription"), "accountDescription")
						.add(Projections.property("currencyLocal"), "currencyLocal")
						.add(Projections.property(columnSelection(month, true)), "amountCumulative")
						.add(Projections.property(columnSelection(month, false)), "amountMovement");
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(MonthlyContractExpenditureDTO.class));
		
		monthlyContractExpenditureDTO.addAll(criteria.list());
		
		return monthlyContractExpenditureDTO;
	}
	
	@SuppressWarnings("unchecked")
	public List<MonthlyContractExpenditureDTO> findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKey(BigDecimal year,
																											BigDecimal month,
																											String typeLedger,
																											String noJob) throws DatabaseOperationException {
		List<MonthlyContractExpenditureDTO> monthlyContractExpenditureDTO = new ArrayList<MonthlyContractExpenditureDTO>();

		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);
		// Preset values
		String codeSubsidiary = "        "; // To filter non-cost code
		
		// Join
		criteria.createAlias("dimAccountMaster", "dimAccountMaster");
				
		// Where
		criteria.add(Restrictions.eq("fiscalYear", year));
		criteria.add(Restrictions.eq("accountTypeLedger", typeLedger));
		criteria.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		criteria.add(Restrictions.ne("accountSubsidiary", codeSubsidiary));	
				
		// Order By
		criteria.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));
						
		// Select (Convert to DTO)
		ProjectionList projectionList = Projections.projectionList();
		projectionList
						.add(Projections.groupProperty("accountTypeLedger"), "typeLedger")
						.add(Projections.groupProperty("accountObject"), "codeObject")
						.add(Projections.groupProperty("accountSubsidiary"), "codeSubsidiary")
						.add(Projections.groupProperty("dimAccountMaster.accountDescription"), "accountDescription")
						.add(Projections.groupProperty("currencyLocal"), "currencyLocal")
						.add(Projections.sum(columnSelection(month, true)), "amountCumulative")
						.add(Projections.sum(columnSelection(month, false)), "amountMovement");
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(MonthlyContractExpenditureDTO.class));
		
		monthlyContractExpenditureDTO.addAll(criteria.list());

		return monthlyContractExpenditureDTO;
	}
	
	/**
	 * To select an appropriate column based on the month
	 *
	 * @param month
	 * @param cumulative
	 * @return
	 * @author	tikywong
	 * @since	Jun 22, 2016 5:28:41 PM
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
