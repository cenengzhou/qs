package com.gammon.pcms.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.AuditConfig;
import com.gammon.pcms.config.AuditConfig.AuditInfo;
import com.gammon.pcms.config.HibernateConfig;
import com.gammon.pcms.config.StoredProcedureConfig;
@Repository
public class AuditHousekeepHBDao {
	
	private Logger logger = Logger.getLogger(AuditHousekeepHBDao.class);
	@PersistenceContext(unitName = "PersistenceUnit")
	protected EntityManager entityManager;
	@Autowired
	private HibernateConfig hibernateConfig;
	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public AuditHousekeepHBDao(){}
	
	public int housekeekpByAuditInfo(String tableName) throws DataAccessException, SQLException {
		AuditInfo auditInfo = auditConfig.getAuditInfoMap().get(tableName);
		int deletedRecord = 0;
		if(auditInfo != null) {
			String sql = "{call " + hibernateConfig.getHibernateDefault_schema() + "." +  storedProcedureConfig.getStoredProcedureAuditHousekeep() + "(:tableName, :period, :rcount)}";
			CallableStatement cs = ((SessionImpl)entityManager.getDelegate()).connection().prepareCall(sql);
			cs.setString("tableName", hibernateConfig.getHibernateDefault_schema() + "." +  auditInfo.getTableName());
			cs.setInt("period", auditInfo.getPeriod());
			cs.registerOutParameter("rcount", java.sql.Types.INTEGER);
			cs.execute();
			deletedRecord = cs.getInt("rcount");
			logger.info("Remove " + deletedRecord + " from " + auditInfo.getTableName());
			cs.close();
		}
		return deletedRecord;
	}
	
	public Session getSession() {
		Session session = entityManager.unwrap(Session.class);
		return session;
	}
}
