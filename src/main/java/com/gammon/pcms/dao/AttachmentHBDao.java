package com.gammon.pcms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDate;
import com.gammon.pcms.model.Attachment;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class AttachmentHBDao extends BaseHibernateDao<Attachment> {

	public AttachmentHBDao() {
		super(Attachment.class);
	}

	@SuppressWarnings("unchecked")
	public List<Attachment> obtainAttachmentList(String nameTable, BigDecimal idTable) {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("nameTable", nameTable));
			criteria.add(Restrictions.eq("idTable", idTable));
			criteria.addOrder(Order.asc("noSequence"));
			return criteria.list();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}

	public List<Attachment> obtainSubcontractDateAttachmentList(Long idTable) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("nameTable", Attachment.SUBCONTRACT_TABLE));
		criteria.add(Restrictions.eq("idTable", new BigDecimal(idTable)));
		criteria.add(Restrictions.or(
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_requisitionApprovedDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_tenderAnalysisApprovedDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_preAwardMeetingDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_loaSignedDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocScrDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocLegalDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_workCommenceDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_onSiteStartDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccDraftDate).getDescription() + "%"),
				Restrictions.like("nameFile", SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccSignoffDate).getDescription() + "%")
		));
		return criteria.list();
	}
	
	public Attachment obtainAttachment(String nameTable, BigDecimal idTable, BigDecimal noSequence) {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("nameTable", nameTable));
			criteria.add(Restrictions.eq("idTable", idTable));
			criteria.add(Restrictions.eq("noSequence", noSequence));
			return (Attachment) criteria.uniqueResult();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}

}
