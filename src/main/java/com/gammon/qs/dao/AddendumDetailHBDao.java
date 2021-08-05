/**
 * com.gammon.qs.dao
 * AddendumDetailHBDao.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.qs.dao;


import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.gammon.pcms.model.AddendumDetailView;
import com.gammon.pcms.model.RecoverableSummary;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.AddendumDetail;
import com.gammon.qs.domain.SubcontractDetail;
@Repository
public class AddendumDetailHBDao extends BaseHibernateDao<AddendumDetail> {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(AddendumDetailHBDao.class.getName());


	public AddendumDetail getAddendumDetail(BigDecimal id) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", id));

		return (AddendumDetail) criteria.uniqueResult();
	}
	
	public AddendumDetail getAddendumDetailHeader(BigDecimal addendumDetailHeaderRef) throws DataAccessException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", addendumDetailHeaderRef));
		criteria.add(Restrictions.eq("typeHd", AddendumDetail.TYPE_HD.HEADER.toString()));

		return (AddendumDetail) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<AddendumDetail> getAddendumDetailsWithoutHeaderRef(String noJob, String noSubcontract, Long addendumNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("no", addendumNo));
		criteria.add(Restrictions.isNull("idHeaderRef"));
		criteria.add(Restrictions.eq("typeHd", AddendumDetail.TYPE_HD.DETAIL.toString()));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AddendumDetail> getAddendumDetailsByHeaderRef(BigDecimal addendumDetailHeaderRef) throws DataAccessException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("idHeaderRef", addendumDetailHeaderRef));
		criteria.add(Restrictions.eq("typeHd", AddendumDetail.TYPE_HD.DETAIL.toString()));
		criteria.addOrder(Order.desc("typeVo"));
		return criteria.list();
	}


	@SuppressWarnings("unchecked")
	public List<AddendumDetail> getAllAddendumDetails(String noJob, String noSubcontract, Long addendumNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("no", addendumNo));
		criteria.addOrder(Order.asc("idHeaderRef")).addOrder(Order.desc("typeHd"));
		return criteria.list();
	}



	@SuppressWarnings("unchecked")
	public RecoverableSummary getSumOfRecoverableAmount(String noJob, String noSubcontract, Long startAddendumNo, Long endAddendumNo) {
		String hql = "select sum(a.recoverableAmt) as recoverableAmt, sum(a.nonRecoverableAmt) as nonRecoverableAmt, sum(a.unclassifiedAmt) as unclassifiedAmt from AddendumDetailView a where a.noJob=:job_no and a.noSubcontract=:subcontract_no and a.no>=:start_add_no and a.no<=:end_add_no";
		Query q = getSession().createQuery(hql);
		q.setParameter("start_add_no", startAddendumNo);
		q.setParameter("end_add_no", endAddendumNo);
		q.setParameter("job_no", noJob);
		q.setParameter("subcontract_no", noSubcontract);
		q.setResultTransformer(Transformers.aliasToBean(RecoverableSummary.class));
		List<RecoverableSummary> results = q.list();
		RecoverableSummary result = results.get(0);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<AddendumDetailView> getAddendumDetailWithDiff(String noJob, String noSubcontract, Long addendumNo) {
		String hql = "select a from AddendumDetailView a where a.noJob=:job_no and a.noSubcontract=:subcontract_no and a.no=:add_no";
		Query q = getSession().createQuery(hql);
		q.setParameter("add_no", addendumNo);
		q.setParameter("job_no", noJob);
		q.setParameter("subcontract_no", noSubcontract);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<AddendumDetail> getAddendumDetails(String noJob, String noSubcontract, Long addendumNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("no", addendumNo));
		criteria.add(Restrictions.eq("typeHd", AddendumDetail.TYPE_HD.DETAIL.toString()));

		return criteria.list();
	}
	
	public AddendumDetail getAddendumDetailBySubcontractDetail(SubcontractDetail subcontractDetail, Long addendumNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("idSubcontractDetail", "idSubcontractDetail");
		criteria.add(Restrictions.eq("idSubcontractDetail.id", subcontractDetail.getId()));
		criteria.add(Restrictions.eq("no", addendumNo));

		return (AddendumDetail) criteria.uniqueResult();
	}

	public AddendumDetail getPreviousAddendumDetail(AddendumDetail addendumDetail) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", addendumDetail.getNoJob()));
		criteria.add(Restrictions.eq("noSubcontract", addendumDetail.getNoSubcontract()));
		criteria.add(Restrictions.eq("bpi", addendumDetail.getBpi()));
		criteria.add(Restrictions.le("no", addendumDetail.getNo()));
		criteria.addOrder(Order.desc("no"));
		List list = criteria.list();
		if (list.size() <= 1) return null;
		else return (AddendumDetail) list.get(1);
	}

	public AddendumDetailHBDao() {
		super(AddendumDetail.class);
	}

}
