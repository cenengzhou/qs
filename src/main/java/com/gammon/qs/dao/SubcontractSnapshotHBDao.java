package com.gammon.qs.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractSnapshot;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.subcontractDashboard.SubcontractSnapshotWrapper;

/**
 * koeyyeung
 * Aug 26, 2014 9:36:01 AM
 */
@Repository
public class SubcontractSnapshotHBDao extends BaseHibernateDao<SubcontractSnapshot> {
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public SubcontractSnapshotHBDao() {
		super(SubcontractSnapshot.class);
	}

	@SuppressWarnings("unchecked")
	public List<SubcontractSnapshot> findByPeriod(String noJob, BigDecimal year, BigDecimal month, boolean awardedOnly) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(this.getType());
		
		// Join
		criteria.createAlias("jobInfo", "jobInfo");
		// Where
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
				.add(Restrictions.eq("packageType", Subcontract.SUBCONTRACT_PACKAGE));
		
		if (StringUtils.isNotBlank(noJob))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", noJob));
		if (awardedOnly)
			criteria.add(Restrictions.and(Restrictions.isNotNull("subcontractStatus"), Restrictions.ge("subcontractStatus", 500)));
		if (month.intValue() > 0 && year.intValue() > 0) {
			// start date (first day of the month)
			Calendar startCalendar = new GregorianCalendar(year.intValue(), month.intValue()-1, 1, 0, 0, 0);
			// end date (last day of the month)
			Calendar endCalendar = new GregorianCalendar(year.intValue(), month.intValue()-1, startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);

			// Formatting
			String startDate = DateHelper.formatDate(startCalendar.getTime(), GlobalParameter.DATE_FORMAT);
			String endDate = DateHelper.formatDate(endCalendar.getTime(), GlobalParameter.DATE_FORMAT);
			
			criteria.add(Restrictions.between("snapshotDate", DateHelper.parseDate(startDate, GlobalParameter.DATE_FORMAT), DateHelper.parseDate(endDate, GlobalParameter.DATE_FORMAT)));
		}

		// order by
		criteria.addOrder(Order.asc("jobInfo.company"))
				.addOrder(Order.asc("jobInfo.division"))
				.addOrder(Order.asc("jobInfo.jobNumber"))
				.addOrder(Order.asc("packageNo"));

		return (List<SubcontractSnapshot>) criteria.list();
	}

	/**
	 * @author koeyyeung
	 * created on 20 Jul,2016
	 * Subcontract Dashboard**/
	@SuppressWarnings("unchecked")
	public List<SubcontractSnapshotWrapper> obtainSubcontractMonthlyStat(String jobNo, String subcontractNo, String startMonth, String startYear, String endMonth, String endYear) throws DatabaseOperationException {
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.createAlias("jobInfo", "jobInfo");
			if (jobNo!=null && !"".equals(jobNo))
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
			
			criteria.add(Restrictions.eq("packageNo", subcontractNo));
			if(startMonth !=""){
				criteria.add(Restrictions.ge("snapshotDate", DateHelper.parseDate("01-"+startMonth+"-"+startYear, "dd-MM-yyyy")));
				criteria.add(Restrictions.lt("snapshotDate", DateHelper.parseDate("01-"+endMonth+"-"+endYear, "dd-MM-yyyy")));
			}else {
				criteria.add(Restrictions.ge("snapshotDate", DateHelper.parseDate("01-01-"+endYear, "dd-MM-yyyy")));
				criteria.add(Restrictions.le("snapshotDate", DateHelper.parseDate("31-12-"+endYear, "dd-MM-yyyy")));
			}
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("totalPostedCertifiedAmount"), "totalPostedCertifiedAmount");
			projectionList.add(Projections.sum("totalPostedWorkDoneAmount"), "totalPostedWorkDoneAmount");
			
			projectionList.add(Projections.groupProperty("snapshotDate"), "snapshotDate");
			criteria.setProjection(projectionList);
			
			criteria.setResultTransformer(Transformers.aliasToBean(SubcontractSnapshotWrapper.class));
			
			return criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public Boolean callStoredProcedureToGenerateSnapshot() throws DatabaseOperationException {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
        Session session = sfi.openSession();
        session.beginTransaction();

        /*@SuppressWarnings("deprecation")
		Connection con = session.connection();
        CallableStatement cs = con.prepareCall("{ call "+sfi.getSettings().getDefaultSchemaName() +  "."+ storedProcedureGeneratePackageSnapshot+"}");
        cs.execute();*/
        
        Query q = session.createSQLQuery("{ call "+sfi.getSettings().getDefaultSchemaName() +  "."+ storedProcedureConfig.getStoredProcedureGeneratepackagesnapshot()+"}");
		q.executeUpdate();
		
		session.getTransaction().commit();
		session.close();
//		sfi.close();     //will cause Could not open Hibernate Session

        completed = true;
		
		return completed;
	}
	
}
