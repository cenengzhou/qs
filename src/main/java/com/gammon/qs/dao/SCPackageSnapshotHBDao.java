package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackageSnapshot;
import com.gammon.qs.util.DateUtil;

/**
 * koeyyeung
 * Aug 26, 2014 9:36:01 AM
 */
@Repository
public class SCPackageSnapshotHBDao extends BaseHibernateDao<SCPackageSnapshot> {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public SCPackageSnapshotHBDao() {
		super(SCPackageSnapshot.class);
	}

	@SuppressWarnings("unchecked")
	public List<SCPackageSnapshot> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber, String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> companyList, Integer status, String month, String year, String reportType) throws DatabaseOperationException {
		List<SCPackageSnapshot> result = null;
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.createAlias("job", "job");
			if (company!=null && !"".equals(company))
				criteria.add(Restrictions.eq("job.company", company));
			else if (companyList!=null)
				criteria.add(Restrictions.in("job.company", companyList));
			if (division!=null && !"".equals(division))
				criteria.add(Restrictions.eq("job.division", division));
			if (jobNumber!=null && !"".equals(jobNumber))
				criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			if (clientNo!=null && !"".equals(clientNo))
				criteria.add(Restrictions.eq("job.employer", clientNo));
			if (subcontractNumber!=null && !"".equals(subcontractNumber))
				criteria.add(Restrictions.eq("packageNo", subcontractNumber));
			if (subcontractorNumber!=null && !"".equals(subcontractorNumber))
				criteria.add(Restrictions.eq("vendorNo", subcontractorNumber)); 
			if (subcontractorNature!=null && !"".equals(subcontractorNature))
				criteria.add(Restrictions.eq("subcontractorNature", subcontractorNature));
			if (paymentStatus!=null && !"".equals(paymentStatus)){
				if("NDI".equals(paymentStatus))
					criteria.add(Restrictions.or(	Restrictions.eq("paymentStatus", "N"),
								 Restrictions.or(	Restrictions.eq("paymentStatus", "D"),
										 			Restrictions.eq("paymentStatus", "I"))));
				else 
					criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
			}
				
			if(splitTerminateStatus!=null && !"".equals(splitTerminateStatus))
				criteria.add(Restrictions.eq("splitTerminateStatus", splitTerminateStatus));
				
			//Awarded Subcontracts
			criteria.add(Restrictions.eq("subcontractStatus", new Integer(500)));
			
			
			if(workScope!=null && !"".equals(workScope)){
				criteria.createAlias("scPackage", "scPackage");
				criteria.createAlias("scPackage.scWorkscope", "scWorkscope");
				criteria.add(Restrictions.eq("scWorkscope.workScope", workScope));
			}
			if(status!=null && !"".equals(status)){
				criteria.add(Restrictions.eq("subcontractStatus", status));
			}
			
			if(!"".equals(month) && !"".equals(year)){
				Date snapshotDate = obtainSnapshotDate(month, year);
				criteria.add(Restrictions.eq("snapshotDate", snapshotDate));
			}
			
			
			if(reportType!=null && "SubcontractorAnalysisReport".equals(reportType))
				criteria.addOrder(Order.asc("vendorNo"));
			else{
				criteria.addOrder(Order.asc("job.company"))
						.addOrder(Order.asc("job.division"))
						.addOrder(Order.asc("job.jobNumber"))
						.addOrder(Order.asc("packageNo"));
			}
			
			result = (List<SCPackageSnapshot>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	private Date obtainSnapshotDate(String month, String year){
		Date startDate = DateUtil.parseDate("01-"+convertToStringMonth(Integer.parseInt(month))+"-"+year, "dd-MM-yyyy");
		Date endDate = DateUtil.parseDate("31-"+convertToStringMonth(Integer.parseInt(month))+"-"+year, "dd-MM-yyyy");
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.ge("snapshotDate",startDate));
		criteria.add(Restrictions.lt("snapshotDate",endDate));
		criteria.setProjection(Projections.distinct(Projections.property("snapshotDate")));
		criteria.addOrder(Order.desc("snapshotDate"));
		criteria.setMaxResults(1);
		
		logger.info("Snapshot Date: "+(Date) criteria.uniqueResult());
		
		return (Date) criteria.uniqueResult();
	}
	
	public String convertToStringMonth(Integer month){
		if(month<10){
			return "0"+String.valueOf(month);
		}else{
			return String.valueOf(month);
		}
	}

	public Boolean callStoredProcedureToGenerateSnapshot() throws Exception {
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
