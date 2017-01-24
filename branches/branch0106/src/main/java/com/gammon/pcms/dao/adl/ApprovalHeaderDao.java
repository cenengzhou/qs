package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.ApprovalHeader;

@Repository
public class ApprovalHeaderDao extends BaseAdlHibernateDao<ApprovalHeader> {

	public ApprovalHeaderDao() {
		super(ApprovalHeader.class);
	}

	public ApprovalHeader findByRecordKeyInstance(BigDecimal recordKeyInstance) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		criteria.add(Restrictions.eq("recordKeyInstance", recordKeyInstance));

		return (ApprovalHeader) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalHeader> findByRecordKeyDocument(String recordKeyDocument, String statusApproval) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		criteria.add(Restrictions.ilike("recordKeyDocument", recordKeyDocument, MatchMode.START));
		criteria.add(Restrictions.ilike("statusApproval", statusApproval));

		return criteria.list();
	}

}
