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

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

	public AddendumDetailHBDao() {
		super(AddendumDetail.class);
	}

}
