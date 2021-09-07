package com.gammon.qs.dao;

import com.gammon.pcms.model.ROC;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class RocHBDao extends BaseHibernateDao<ROC> {

	private Logger logger = Logger.getLogger(RocHBDao.class.getName());

	public RocHBDao() {
		super(ROC.class);
	}

	public List<ROC> getAuditHistory(Long id) {
		List<ROC> result = null;
		try {
			AuditReader reader = AuditReaderFactory.get(getSession());
			AuditQuery q = reader.createQuery().forRevisionsOfEntity(ROC.class, true, true);
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
