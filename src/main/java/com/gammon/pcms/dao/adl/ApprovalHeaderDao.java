package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.ApprovalHeader;
@Repository
public class ApprovalHeaderDao extends BaseAdlHibernateDao<ApprovalHeader> {

	public ApprovalHeaderDao() {
		super(ApprovalHeader.class);
	}

	public ApprovalHeader getApprovalHeaderByRecordKeyInstance(BigDecimal recordKeyInstance) {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("recordKeyInstance", recordKeyInstance));
		ApprovalHeader approvalHeader = (ApprovalHeader) criteria.uniqueResult();
		return approvalHeader;
	}

}
