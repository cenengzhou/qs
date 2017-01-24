/**
 * PCMS-TC
 * com.gammon.pcms.dao.adl
 * AccountMasterDao.java
 * @since Jul 22, 2016 11:46:04 AM
 * @author tikywong
 */
package com.gammon.pcms.dao.adl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountMaster;

@Repository
public class AccountMasterDao extends BaseAdlHibernateDao<AccountMaster> {
	public AccountMasterDao() {
		super(AccountMaster.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountMaster> find(String noJob) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);

		// Where
		criteria.add(Restrictions.eq("businessUnit", noJob));

		// Order by
		criteria.addOrder(Order.asc("businessUnit"))
				.addOrder(Order.asc("object"))
				.addOrder(Order.asc("subsidiary"));

		return new ArrayList<AccountMaster>(criteria.list());
	}
	
	public AccountMaster findByAccountCode(String noJob, String codeObject, String codeSubsidiary) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);

		// Where
		criteria.add(Restrictions.eq("businessUnit", noJob))
				.add(Restrictions.eq("object", codeObject))
				.add(Restrictions.eq("subsidiary", codeSubsidiary));

		return (AccountMaster) criteria.uniqueResult();
	}
}