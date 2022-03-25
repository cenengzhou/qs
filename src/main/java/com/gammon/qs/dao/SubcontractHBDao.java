package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractSnapshotDTO;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
@Repository
public class SubcontractHBDao extends BaseHibernateDao<Subcontract> {

	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	@Autowired
	private TenderHBDao tenderAnalysisHBdao;
	@Autowired
	private TenderDetailHBDao tenderDetailDao;
	@Autowired
	private TenderVarianceHBDao tenderVarianceHBDao;
	
	private Logger logger = Logger.getLogger(SubcontractHBDao.class.getName());
	public SubcontractHBDao() {
		super(Subcontract.class);
	}

	public Subcontract obtainSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException {
		Subcontract result;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.add(Restrictions.eq("packageType", Subcontract.SUBCONTRACT_PACKAGE));
			result = (Subcontract) criteria.uniqueResult();
		} catch (HibernateException he) {
			logger.info("Fail: obtainSCPackage(String jobNumber, Integer packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	public Subcontract obtainPackage(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		Subcontract result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			result = (Subcontract) criteria.uniqueResult();
			if(result != null){
				Hibernate.initialize(result.getJobInfo());
			}
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainPackageList(String jobNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber)){
			logger.info("Fail: getPackagesList(String jobNumber)");
			throw new IllegalArgumentException("Job number is null or empty");
		}
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<Subcontract>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getPackagesList(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> getUnawardedPackages(JobInfo jobInfo) throws Exception {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.or(Restrictions.isNull("subcontractStatus"), Restrictions.lt("subcontractStatus", 500)));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<Subcontract>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getUnawardedPackages(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getAwardedPackageNos(JobInfo jobInfo) throws Exception{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("subcontractStatus", 500));
			criteria.setProjection(Projections.property("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getAwardedPackageNos(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getFinalizedSubcontractNos(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		try{
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"))
							.add(Restrictions.eq("jobInfo", jobInfo))
							.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
							.setProjection(Projections.property("packageNo"));

			DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
			detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
								.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.like("objectCode", "14%"))
								.add(Property.forName("packageNo").in(detachedCriteria))
								.setProjection(Projections.distinct(Projections.property("packageNo")));
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			criteria.add(Restrictions.eq("subcontractStatus", 500));
			criteria.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT));
			
			criteria.add(Property.forName("packageNo").in(detachedCriteria2));
			
			criteria.setProjection(Projections.property("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean addSCPackage(Subcontract subcontract) throws Exception{
		try {
			if (obtainSCPackage(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo())!=null){
				logger.info("Fail: addAddendumLine(Subcontract subcontract)");
				throw new IllegalArgumentException("subcontract existed.");
			}
			saveOrUpdate(subcontract);
			return true;
		} catch (DatabaseOperationException e) {
			logger.info("Fail: addAddendumLine(subcontract)");
			throw new DatabaseOperationException(e);
		}
	}

	public boolean updateSubcontract(Subcontract subcontract) throws Exception{
		saveOrUpdate(subcontract);
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> getPackages(String jobNumber) throws Exception {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<Subcontract>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	/**
	 * @author koeyyeung Payment Requisition Revamp Non-awarded ScPackages with payment requisition are allowed for provision posting
	 * @throws DatabaseOperationException
	 **/
	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainPackagesForProvisionPosting(String jobNumber) throws DatabaseOperationException {
		List<Subcontract> scPackageList = new ArrayList<Subcontract>();

		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		try {
			// Obtain awarded ScPackages
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("subcontractStatus", new Integer(500)));
			criteria.add(Restrictions.or(Restrictions.not(Restrictions.eq("paymentStatus", "F")), Restrictions.isNull("paymentStatus")));
			criteria.addOrder(Order.asc("packageNo"));

			List<Subcontract> awardedPackageList = criteria.list();
			if (awardedPackageList != null)
				scPackageList.addAll(awardedPackageList);

			// Obtain non-awarded ScPackages with payment requisition
			criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.add(Restrictions.lt("subcontractStatus", new Integer(500)));
			criteria.add(Restrictions.eq("paymentStatus", "D"));
			criteria.addOrder(Order.asc("packageNo"));

			List<Subcontract> nonAwardedPackageList = criteria.list();

			if (nonAwardedPackageList != null)
				scPackageList.addAll(nonAwardedPackageList);

//			logger.info("Job: " + jobNumber + " - No. of Packages: " + scPackageList.size());
			return scPackageList;

		} catch (HibernateException he) {
			throw new DatabaseOperationException(he);
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber,String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> jobNoList, Integer status, String reportType) throws DatabaseOperationException{
		List<Subcontract> result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			if (company!=null && !"".equals(company))
				criteria.add(Restrictions.eq("jobInfo.company", company));
			if (jobNoList!=null){
				Disjunction or = Restrictions.disjunction();
				for(int i=0; i < jobNoList.size(); i+=500){
					int from = i;
					int to = i+499 < jobNoList.size() ? i+499 : jobNoList.size(); 
					logger.info("SubcontractHBDao.obtainSubcontractList from:" + from + " to:" + to);
					or.add(Restrictions.in("jobInfo.jobNumber", jobNoList.subList(from, to)));
				}
				criteria.add(or);
			}
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
				throw new RuntimeException("Subcontract does not contain List<SCWorkScope> due to remove one to many");
//				criteria.createAlias("scWorkscope", "scWorkscope");
//				criteria.add(Restrictions.eq("scWorkscope.workScope", workScope));
			}
			if(status!=null && !"".equals(status)){
				criteria.add(Restrictions.eq("subcontractStatus", status));
			}
			
			if(reportType!=null && "SubcontractorAnalysisReport".equals(reportType))
				criteria.addOrder(Order.asc("vendorNo"));
			else{
				criteria.addOrder(Order.asc("jobInfo.company"))
						.addOrder(Order.asc("jobInfo.division"))
						.addOrder(Order.asc("jobInfo.jobNumber"))
						.addOrder(Order.asc("packageNo"));
			}
			result = (List<Subcontract>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	public String getPackageDescription(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.setProjection(Projections.property("description"));
			return (String)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public void resetPackageTA(Subcontract subcontract) throws DataAccessException{
		//delete all ta records
		List<Tender> tenderAnalysisList = tenderAnalysisHBdao.obtainTenderAnalysisList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
		for(Tender tenderAnalysis : tenderAnalysisList){
			List<TenderVariance> tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo(), String.valueOf(tenderAnalysis.getVendorNo()));
			for (TenderVariance tenderVariance: tenderVarianceList){
				tenderVarianceHBDao.delete(tenderVariance);
			}
			
			List<TenderDetail> details = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
			for (TenderDetail detail: details){
				tenderDetailDao.delete(detail);
			}

			tenderAnalysisHBdao.delete(tenderAnalysis);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainParentJobList(String jobNo) throws DatabaseOperationException {
		if(jobNo==null)
			throw new DatabaseOperationException("jobNo is null");
		Criteria criteria = getSession().createCriteria(this.getType());
		
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("internalJobNo", jobNo.trim()));
		criteria.add(Restrictions.ne("jobInfo.jobNumber", jobNo.trim()));
		criteria.setProjection(Projections.distinct(Projections.property("jobInfo.jobNumber")));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainParentSubcontractList(String jobNo) throws DatabaseOperationException {
		if(jobNo==null)
			throw new DatabaseOperationException("jobNo is null");
		Criteria criteria = getSession().createCriteria(this.getType());
		
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("internalJobNo", jobNo.trim()));
		criteria.add(Restrictions.ne("jobInfo.jobNumber", jobNo.trim()));
		return criteria.list();
	}

	public Subcontract obtainPackageStatistics(String vendorNo, Date startDate) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo==null?null:vendorNo.trim()));
		criteria.add(Restrictions.le("createdDate", new Date()));
		criteria.add(Restrictions.ge("createdDate", startDate));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sum("this.remeasuredSubcontractSum"),"remeasuredSubcontractSum")
				.add(Projections.sum("this.approvedVOAmount"),"approvedVOAmount")
				.add(Projections.sum("this.totalCumWorkDoneAmount"),"totalCumWorkDoneAmount")
		);
		criteria.setResultTransformer(Transformers.aliasToBean(Subcontract.class));
		return (Subcontract) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainPackagesByVendorNo(String vendorNo) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo==null?null:vendorNo.trim()));		
			
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.addOrder(Order.asc("jobInfo.jobNumber")).addOrder(Order.asc("packageNo"));
		
		return criteria.list();
	}
	
	/**@author koeyyeung
	 * created on 24th Apr, 2015
	 ** Call Stored Procedure to update F58001 from ScPackage**/
	public Boolean callStoredProcedureToUpdateF58001() throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call "+sfi.getSettings().getDefaultSchemaName() +  "."+storedProcedureConfig.getStoredProcedureUpdateF58001()+"}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
//		sfi.close(); //will cause Could not open Hibernate Session
		
		
        completed = true;
		
		return completed;
	}
	
	/**22 Mar 2022
	 ** Call Stored Procedure P_UPDATE_CED_APPROVAL_TO_QS **/
	public Boolean callStoredProcedureToUpdateCEDApproval(String jobNo, String packageNo) throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call "+sfi.getSettings().getDefaultSchemaName() +  "."+storedProcedureConfig.getStoredProcedureUpdateCEDApproval()+"("+jobNo +"," + packageNo+")}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
//		sfi.close(); //will cause Could not open Hibernate Session
		
        completed = true;
		
		return completed;
	}
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public Subcontract obtainSubcontract(String jobNo, String packageNo) throws DatabaseOperationException{
		Subcontract result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			result = (Subcontract) criteria.uniqueResult();
			if(result != null){
				Hibernate.initialize(result.getJobInfo());
			}
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainSubcontractSnapshotList(boolean awardedOnly, Map<String, String> commonKeyValue) throws DataAccessException {
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<>();
		orderMap.put("packageNo", "ASC");
		Criteria criteria = getCriteriaByKeyValue(commonKeyValue, orderMap, true);
		if (awardedOnly)
			criteria.add(Restrictions.and(Restrictions.isNotNull("subcontractStatus"), Restrictions.ge("subcontractStatus", 500)));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Subcontract> find(String jobNo, boolean awardedOnly) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(this.getType());

		// Join
		criteria.createAlias("jobInfo", "jobInfo");

		// Where
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
				.add(Restrictions.eq("packageType", Subcontract.SUBCONTRACT_PACKAGE));
		if(StringUtils.isNotBlank(jobNo))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
		if (awardedOnly)
			criteria.add(Restrictions.and(Restrictions.isNotNull("subcontractStatus"), Restrictions.ge("subcontractStatus", 500)));

		// Order by
		criteria.addOrder(Order.asc("packageNo"));
		
		return (List<Subcontract>) criteria.list();
	}

	public SubcontractSnapshotDTO obtainSubcontractCurrentStat(String jobNo, String subcontractNo) throws DatabaseOperationException {
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.createAlias("jobInfo", "jobInfo");
			if (jobNo!=null && !"".equals(jobNo))
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
			
			criteria.add(Restrictions.eq("packageNo", subcontractNo));
			
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("totalPostedCertifiedAmount"), "totalPostedCertifiedAmount");
			projectionList.add(Projections.sum("totalPostedWorkDoneAmount"), "totalPostedWorkDoneAmount");
			projectionList.add(Projections.sum("totalCumCertifiedAmount"), "totalCumCertifiedAmount");
			projectionList.add(Projections.sum("totalCumWorkDoneAmount"), "totalCumWorkDoneAmount");

			criteria.setProjection(projectionList);
			
			criteria.setResultTransformer(Transformers.aliasToBean(SubcontractSnapshotDTO.class));
			
			return (SubcontractSnapshotDTO) criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END*********************************************************/	
}
