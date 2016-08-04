package com.gammon.pcms.dao;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.CrossTypeRevisionChangesReader;
import org.hibernate.envers.tools.Pair;
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
	
	public int housekeekpByAuditTableName(String tableName) throws DataAccessException, SQLException {
		AuditInfo auditInfo = auditConfig.getAuditInfoMap().get(tableName);
		int deletedRecord = 0;
		if(auditInfo != null) {
			String sql = "{call " + hibernateConfig.getHibernateDefault_schema() + "." +  storedProcedureConfig.getStoredProcedureAuditHousekeep() + "(:tableName, :period, :rcount)}";
			CallableStatement cs = ((SessionImpl)entityManager.getDelegate()).connection().prepareCall(sql);
			cs.setString("tableName", hibernateConfig.getHibernateDefault_schema() + "." +  auditInfo.getTableName());
			String[] periods = auditInfo.getPeriod().split("-");
			Calendar today = Calendar.getInstance();
			Calendar housekeepDate = Calendar.getInstance();
			housekeepDate.add(Calendar.YEAR, Integer.parseInt(periods[0]) * -1);
			housekeepDate.add(Calendar.MONTH, Integer.parseInt(periods[1]) * -1);
			housekeepDate.add(Calendar.DATE, Integer.parseInt(periods[2]) * -1);
			int period = (int) ((today.getTime().getTime() - housekeepDate.getTime().getTime()) / (1000*24*60*60));
			cs.setInt("period", period);
			cs.registerOutParameter("rcount", java.sql.Types.INTEGER);
			cs.execute();
			deletedRecord = cs.getInt("rcount");
			logger.info("Remove " + deletedRecord + " records older then " + period + " days from " + auditInfo.getTableName());
			cs.close();
		}
		return deletedRecord;
	}
	
	public Session getSession() {
		Session session = entityManager.unwrap(Session.class);
		return session;
	}

	public <T> T findEntityByIdRevision(Class<T> clazz, long id, int rev) {
		 AuditReader reader = AuditReaderFactory.get(entityManager);
		 T revision = reader.find(clazz, id, rev);
		 initializeField(clazz, revision);
		return revision;
	}

	@SuppressWarnings("unchecked")
	public <T> List<Object[]> findRevisionsByEntity(Class<T> clazz){
		AuditReader reader = AuditReaderFactory.get(entityManager);
		List<Object[]> revisionList = reader.createQuery().forRevisionsOfEntity(clazz, false, true).getResultList();
		for(Object[] revision : revisionList){
			initializeField(clazz, (T)revision[0]);
		}
		return revisionList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> findByRevision(Number revision){
		CrossTypeRevisionChangesReader reader = AuditReaderFactory.get(entityManager).getCrossTypeRevisionChangesReader();
		List<Object> resultList = reader.findEntities(revision.intValue());
		Set<Pair<String, Class>> resultClasses = reader.findEntityTypes(revision.intValue());
		for(Object result : resultList){
			for(Pair<String, Class> pair : resultClasses){
				Class clazz = pair.getSecond();
				if(result.getClass() == clazz ){
					initializeField(clazz, clazz.cast(result));
					break;
				}
			}
			
		}
		return resultList;
	}
	
	/**If entity not found in audit table load current entity
	 * @param clazz
	 * @param revision
	 */
	private <T> void initializeField(Class<T> clazz, T revision) {
		for (Field field : clazz.getDeclaredFields()) {
	            if (field.getType().toString().indexOf("com.gammon") > -1) {
	                try {
	                    field.setAccessible(true);
	                    Hibernate.initialize(field.get(revision));
	                } catch (IllegalAccessException | NullPointerException | EntityNotFoundException  e) {
	                	e.printStackTrace();
	                    logger.warn("Unable to initialize history: " + field.getName());
	                	try {
	                		String[] classId = e.getMessage().split("Unable\\sto\\sfind\\s|\\swith\\sid\\s");
	                		if(classId != null && classId.length == 3){
	                			field.set(revision, entityManager.find(field.getType(), new Long(classId[2])));
	                			logger.warn("Current entity loaded:" + field.getName());
	                		}
						} catch (IllegalArgumentException  | SecurityException | IllegalAccessException  e1) {
							e1.printStackTrace();
							logger.warn("Current entity fail to load:" + field.getName());
						}
	                }
	            }
	        }
	}
	
}
