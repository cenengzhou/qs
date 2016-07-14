package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.ApprovalDetail;
@Repository
public class ApprovalDetailDao extends BaseAdlHibernateDao<ApprovalDetail> {

	public ApprovalDetailDao() {
		super(ApprovalDetail.class);
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalDetail> obtainApprovalDetailByRecordKeyInstance(BigDecimal recordKeyInstance){
		Criteria criteria = getSession().createCriteria(getType());
		criteria.createAlias("recordKeyInstance", "approvalDetail");
		criteria.add(Restrictions.eq("approvalDetail.recordKeyInstance", recordKeyInstance));
		List<ApprovalDetail>  approvalDetailList = (List<ApprovalDetail>) criteria.list();
		return approvalDetailList;
	}
}
