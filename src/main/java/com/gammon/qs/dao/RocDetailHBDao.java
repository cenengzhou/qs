package com.gammon.qs.dao;

import com.gammon.pcms.model.ROC_DETAIL;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class RocDetailHBDao extends BaseHibernateDao<ROC_DETAIL> {

	private Logger logger = Logger.getLogger(RocDetailHBDao.class.getName());

	public RocDetailHBDao() {
		super(ROC_DETAIL.class);
	}

	public List<ROC_DETAIL> getAuditHistory(Long id) {
		List<ROC_DETAIL> result = null;
		try {
			AuditReader reader = AuditReaderFactory.get(getSession());
			AuditQuery q = reader.createQuery().forRevisionsOfEntity(ROC_DETAIL.class, true, true);
			q.add(AuditEntity.id().eq(id));
			q.addOrder(AuditEntity.revisionNumber().desc());
			result = q.getResultList();
		} catch (Exception e) {
			logger.info("failed to get audit history");
			e.printStackTrace();
		}
		return result;
	}
}
