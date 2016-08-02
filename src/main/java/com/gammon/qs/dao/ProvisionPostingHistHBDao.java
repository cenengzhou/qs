package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.SubcontractDetail;

@Repository
public class ProvisionPostingHistHBDao extends BaseHibernateDao<ProvisionPostingHist> {

	public ProvisionPostingHistHBDao() {
		super(ProvisionPostingHist.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProvisionPostingHist> findByPeriod(String jobNo, String subcontractNo, Integer year, Integer month) {
		Criteria criteria = getSession().createCriteria(this.getType());

		// Where
		if (StringUtils.isNotBlank(jobNo) && year.intValue() > 0 && month.intValue() > 0)
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
					.add(Restrictions.eq("jobNo", jobNo))
					.add(Restrictions.eq("postedYr", year))
					.add(Restrictions.eq("postedMonth", month));

		// Optional
		if (StringUtils.isNotBlank(subcontractNo))
			criteria.add(Restrictions.eq("packageNo", subcontractNo));

		// Order By
		criteria.addOrder(Order.asc("jobNo"))
				.addOrder(Order.asc("packageNo"))
				.addOrder(Order.asc("objectCode"))
				.addOrder(Order.asc("subsidiaryCode"));

		return (List<ProvisionPostingHist>) criteria.list();
	}

	public ProvisionPostingHist getSCDetailProvision(String jobNumber, String packageNo, Integer year, Integer month, SubcontractDetail scDetail) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo", jobNumber));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("postedYr", year));
		criteria.add(Restrictions.eq("scDetails", scDetail));
		criteria.add(Restrictions.eq("postedMonth", month));
		return (ProvisionPostingHist) criteria.uniqueResult();
	}

	public void saveOrUpdate(ProvisionPostingHist scDetailProvisionHistory) throws DataAccessException {

		ProvisionPostingHist dbObj = getSCDetailProvision(scDetailProvisionHistory.getSubcontractDetail().getJobNo(), scDetailProvisionHistory.getSubcontractDetail().getSubcontract().getPackageNo(), scDetailProvisionHistory.getPostedYr(), scDetailProvisionHistory.getPostedMonth(), scDetailProvisionHistory.getSubcontractDetail());
		if (dbObj != null) {
			dbObj.setPostedCertAmount(scDetailProvisionHistory.getPostedCertAmount());
			dbObj.setPostedCertQty(scDetailProvisionHistory.getPostedCertQty());
			dbObj.setCumLiabilitiesAmount(scDetailProvisionHistory.getCumLiabilitiesAmount());
			dbObj.setCumLiabilitiesQty(scDetailProvisionHistory.getCumLiabilitiesQty());
			dbObj.setObjectCode(scDetailProvisionHistory.getObjectCode());
			dbObj.setSubsidiaryCode(scDetailProvisionHistory.getSubsidiaryCode());
			dbObj.setScRate(scDetailProvisionHistory.getScRate());
		} else {
			dbObj = scDetailProvisionHistory;
			dbObj.setCreatedDate(new Date());
			if (dbObj.getCreatedUser() == null || dbObj.getCreatedUser().trim().length() < 1)
				dbObj.setCreatedUser("SYSTEM");
		}
		super.saveOrUpdate(dbObj);
	}

	@SuppressWarnings("unchecked")
	public List<ProvisionPostingHist> obtainSCDetailProvisionGroupedByAccountCode(String jobNumber, String packageNo, Integer postYr, Integer postMonth) {
		List<ProvisionPostingHist> resultList = new ArrayList<ProvisionPostingHist>();
		Criteria criteria = getSession().createCriteria(this.getType());

		criteria.add(Restrictions.eq("jobNo", jobNumber));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("postedYr", postYr));
		criteria.add(Restrictions.eq("postedMonth", postMonth));
		criteria.setProjection(Projections.projectionList().add(Projections.sum("cumLiabilitiesAmount"), "cumLiabilitiesAmount").add(Projections.sum("postedCertAmount"), "postedCertAmount").add(Projections.groupProperty("objectCode"), "objectCode").add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ProvisionPostingHist.class));

		resultList = criteria.list();
		return resultList != null ? resultList : new ArrayList<ProvisionPostingHist>();
	}

	public int delete(String jobNumber, String packageNo, Integer postYr, Integer postMonth) throws DatabaseOperationException {
		List<ProvisionPostingHist> deletingList = findByPeriod(jobNumber, packageNo, postYr, postMonth);
		if (deletingList != null && deletingList.size() > 0) {
			for (ProvisionPostingHist delItem : deletingList)
				super.delete(delItem);
			return deletingList.size();
		}
		return 0;
	}
}
