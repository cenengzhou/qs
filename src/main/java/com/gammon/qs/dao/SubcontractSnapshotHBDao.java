package com.gammon.qs.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SubcontractSnapshot;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.subcontractDashboard.SubcontractSnapshotWrapper;

/**
 * koeyyeung
 * Aug 26, 2014 9:36:01 AM
 */
@Repository
public class SubcontractSnapshotHBDao extends BaseHibernateDao<SubcontractSnapshot> {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	
	public SubcontractSnapshotHBDao() {
		super(SubcontractSnapshot.class);
	}

	@SuppressWarnings("unchecked")
	public List<SubcontractSnapshot> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber, String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> companyList, Integer status, String month, String year, String reportType) throws DatabaseOperationException {
		List<SubcontractSnapshot> result = null;
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.createAlias("jobInfo", "jobInfo");
			if (company!=null && !"".equals(company))
				criteria.add(Restrictions.eq("jobInfo.company", company));
			else if (companyList!=null)
				criteria.add(Restrictions.in("jobInfo.company", companyList));
			if (division!=null && !"".equals(division))
				criteria.add(Restrictions.eq("jobInfo.division", division));
			if (jobNumber!=null && !"".equals(jobNumber))
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			if (clientNo!=null && !"".equals(clientNo))
				criteria.add(Restrictions.eq("jobInfo.employer", clientNo));
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
				throw new RuntimeException("Subcontract does not contain SCWorkscope due to remove one to many linking");
//				criteria.createAlias("subcontract", "subcontract");
//				criteria.createAlias("subcontract.scWorkscope", "scWorkscope");
//				criteria.add(Restrictions.eq("scWorkscope.workScope", workScope));
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
				criteria.addOrder(Order.asc("jobInfo.company"))
						.addOrder(Order.asc("jobInfo.division"))
						.addOrder(Order.asc("jobInfo.jobNumber"))
						.addOrder(Order.asc("packageNo"));
			}
			
			result = (List<SubcontractSnapshot>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
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
				criteria.add(Restrictions.ge("snapshotDate", DateUtil.parseDate("01-"+startMonth+"-"+startYear, "dd-MM-yyyy")));
				criteria.add(Restrictions.lt("snapshotDate", DateUtil.parseDate("01-"+endMonth+"-"+endYear, "dd-MM-yyyy")));
			}else {
				criteria.add(Restrictions.ge("snapshotDate", DateUtil.parseDate("01-01-"+endYear, "dd-MM-yyyy")));
				criteria.add(Restrictions.le("snapshotDate", DateUtil.parseDate("31-12-"+endYear, "dd-MM-yyyy")));
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
