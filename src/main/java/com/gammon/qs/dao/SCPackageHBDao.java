package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
@Repository
public class SCPackageHBDao extends BaseHibernateDao<SCPackage> {

	@Autowired
	private StoredProcedureConfig storedProcedureConfig;
	@Autowired
	private TenderAnalysisHBDao tenderAnalysisHBdao;
	@Autowired
	private SCPaymentCertHBDao scPaymentCertHBDao;
	
	private Logger logger = Logger.getLogger(SCPackageHBDao.class.getName());
	public SCPackageHBDao() {
		super(SCPackage.class);
	}

	public SCPackage obtainSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException {
		SCPackage result;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.add(Restrictions.eq("packageType", SCPackage.SUBCONTRACT_PACKAGE));
			result = (SCPackage) criteria.uniqueResult();
		} catch (HibernateException he) {
			logger.info("Fail: obtainSCPackage(String jobNumber, Integer packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	public SCPackage obtainPackage(Job job, String packageNo) throws DatabaseOperationException{
		SCPackage result = null;
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			result = (SCPackage) criteria.uniqueResult();
			if(result != null){
				Hibernate.initialize(result.getJob());
			}
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainPackages(String jobNumber, String packageNo, String packageType)	throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("job", "job");
		criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("packageType", SCPackage.SUBCONTRACT_PACKAGE));
		
		return criteria.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<SCPackage> getSCPackages(Job job) throws Exception {
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<SCPackage>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getSCPackages(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainPackageList(String jobNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber)){
			logger.info("Fail: getPackagesList(String jobNumber)");
			throw new IllegalArgumentException("Job number is null or empty");
		}
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<SCPackage>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getPackagesList(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> getUnawardedPackages(Job job) throws Exception {
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.or(Restrictions.isNull("subcontractStatus"), Restrictions.lt("subcontractStatus", 500)));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<SCPackage>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getUnawardedPackages(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUnawardedPackageNos(Job job) throws Exception{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.or
							(Restrictions.and(
									Restrictions.ne("subcontractStatus", 330), Restrictions.ne("subcontractStatus", 500)), Restrictions.isNull("subcontractStatus")));		
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("packageNo"))
					.add(Projections.property("description"))
					.add(Projections.property("subcontractStatus")));
			criteria.addOrder(Order.asc("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getUnawardedPackageNos(Job job)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAwardedPackageStore(Job job) throws Exception{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("subcontractStatus", 500));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("packageNo"))
					.add(Projections.property("description"))
					.add(Projections.property("subcontractStatus")));
			criteria.addOrder(Order.asc("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getAwardedPackageNos(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getUneditablePackageNos(Job job) throws Exception{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			//Getting Packages with sub-contract status > 160 except 340
			criteria.add(Restrictions.gt("subcontractStatus", 160));
			criteria.add(Restrictions.not(Restrictions.eq("subcontractStatus", 340)));
			criteria.setProjection(Projections.property("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getUneditablePackageNos(Job job)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAwardedPackageNos(Job job) throws Exception{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
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
	public List<String> obtainFinalizedPackageNos(Job job, String packageNo) throws DatabaseOperationException{
		try{
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"))
							.add(Restrictions.eq("job", job))
							.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
							.setProjection(Projections.property("packageNo"));

			DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
			detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
								.add(Restrictions.eq("job", job))
								.add(Restrictions.like("objectCode", "14%"))
								.add(Property.forName("packageNo").in(detachedCriteria))
								.setProjection(Projections.distinct(Projections.property("packageNo")));
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("job", job));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			criteria.add(Restrictions.eq("subcontractStatus", 500));
			criteria.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT));
			
			criteria.add(Property.forName("packageNo").in(detachedCriteria2));
			
			criteria.setProjection(Projections.property("packageNo"));
			return criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean addSCPackage(SCPackage scPackage) throws Exception{
		try {
			if (obtainSCPackage(scPackage.getJob().getJobNumber(), scPackage.getPackageNo())!=null){
				logger.info("Fail: addAddendumLine(SCPackage scPackage)");
				throw new IllegalArgumentException("SC Package existed.");
			}
			saveOrUpdate(scPackage);
			return true;
		} catch (DatabaseOperationException e) {
			logger.info("Fail: addAddendumLine(SCPackage)");
			throw new DatabaseOperationException(e);
		}
	}

	public boolean updateSCPackage(SCPackage scPackage) throws Exception{
		saveOrUpdate(scPackage);
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> getPackages(String jobNumber) throws Exception {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<SCPackage>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	/**
	 * @author koeyyeung Payment Requisition Revamp Non-awarded ScPackages with payment requisition are allowed for provision posting
	 * @throws DatabaseOperationException
	 **/
	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainPackagesForProvisionPosting(String jobNumber) throws DatabaseOperationException {
		List<SCPackage> scPackageList = new ArrayList<SCPackage>();

		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		try {
			// Obtain awarded ScPackages
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("subcontractStatus", new Integer(500)));
			criteria.add(Restrictions.or(Restrictions.not(Restrictions.eq("paymentStatus", "F")), Restrictions.isNull("paymentStatus")));
			criteria.addOrder(Order.asc("packageNo"));

			List<SCPackage> awardedPackageList = criteria.list();
			if (awardedPackageList != null)
				scPackageList.addAll(awardedPackageList);

			// Obtain non-awarded ScPackages with payment requisition
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.lt("subcontractStatus", new Integer(500)));
			criteria.add(Restrictions.eq("paymentStatus", "D"));
			criteria.addOrder(Order.asc("packageNo"));

			List<SCPackage> nonAwardedPackageList = criteria.list();

			if (nonAwardedPackageList != null)
				scPackageList.addAll(nonAwardedPackageList);

//			logger.info("Job: " + jobNumber + " - No. of Packages: " + scPackageList.size());
			return scPackageList;

		} catch (HibernateException he) {
			throw new DatabaseOperationException(he);
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber,String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> companyList, Integer status, String reportType) throws DatabaseOperationException{
		List<SCPackage> result = null;
		try{			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
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
				criteria.createAlias("scWorkscope", "scWorkscope");
				criteria.add(Restrictions.eq("scWorkscope.workScope", workScope));
			}
			if(status!=null && !"".equals(status)){
				criteria.add(Restrictions.eq("subcontractStatus", status));
			}
			
			if(reportType!=null && "SubcontractorAnalysisReport".equals(reportType))
				criteria.addOrder(Order.asc("vendorNo"));
			else{
				criteria.addOrder(Order.asc("job.company"))
						.addOrder(Order.asc("job.division"))
						.addOrder(Order.asc("job.jobNumber"))
						.addOrder(Order.asc("packageNo"));
			}
			result = (List<SCPackage>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPackage> getPackagesFromList(Job job, List<String> packageNos) throws Exception{
		List<SCPackage> result = null;
		try{			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.in("packageNo", packageNos));
			result = (List<SCPackage>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	public String getPackageDescription(Job job, String packageNo) throws DatabaseOperationException{
		try{			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.setProjection(Projections.property("description"));
			return (String)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public void resetPackageTA(SCPackage scPackage) throws DatabaseOperationException{
		//delete all ta records
		List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBdao.obtainTenderAnalysisListWithDetails(scPackage);
		if(tenderAnalysisList != null){
			for(TenderAnalysis tenderAnalysis : tenderAnalysisList){
				tenderAnalysisHBdao.delete(tenderAnalysis);
			}
		}
	}

	/**
	 * @author tikywong
	 * May 24, 2011 4:48:38 PM
	 * @throws DatabaseOperationException 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getReadyToUpdateWorkdoneQuantityPackageNos(String jobNumber) throws DatabaseOperationException {
		try{			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));			
			criteria.add(Restrictions.and(Restrictions.isNotNull("packageNo"),Restrictions.ne("packageNo", "0")));
			criteria.add(Restrictions.eq("splitTerminateStatus", "0"));
			criteria.add(Restrictions.ne("paymentStatus", "F"));

			criteria.setProjection(Projections.property("packageNo"));
			criteria.addOrder(Order.asc("packageNo"));
			
			List<String> result = (List<String>)criteria.list();
			//logger.info("NUMBER OF PACKAGES FOUND: "+result.size());
			return result;
		}catch(HibernateException e){
			throw new DatabaseOperationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainPackageListForSCPaymentPanel(String jobNumber) {
		String[] packageNoArray; 
		Criteria criteriaForPaymentCert = scPaymentCertHBDao.getSessionFactory().getCurrentSession().createCriteria(SCPaymentCert.class);
		criteriaForPaymentCert.add(Restrictions.eq("jobNo", jobNumber.trim()));
		List<SCPaymentCert> scPaymentCertList = criteriaForPaymentCert.list();
		
		if(scPaymentCertList.size()>0){
			packageNoArray = new String[scPaymentCertList.size()];
		} else {
			packageNoArray = new String[0];
		}
		
		for(int i = 0; i<packageNoArray.length; i++ ){
			packageNoArray[i] = scPaymentCertList.get(i).getPackageNo();
 		}
		
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("job", "job");
		criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));			
		criteria.add(Restrictions.and(Restrictions.isNotNull("packageNo"),Restrictions.ne("packageNo", "0")));
		criteria.add(Restrictions.or(Restrictions.eq("subcontractStatus", Integer.valueOf(500)), Restrictions.in("packageNo", packageNoArray)));
		criteria.addOrder(Order.asc("packageNo"));
		return criteria.list();
	}


	@SuppressWarnings("unchecked")
	public List<SCPackage> getScPackageListForSystemAdmin(String jobNo,String packageNo, String vendorNo, String packageStatus) throws DatabaseOperationException {
		List<SCPackage> resultList = new LinkedList<SCPackage>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			if (jobNo!=null && !jobNo.equals("")){
				criteria.add(Restrictions.eq("job.jobNumber", jobNo.trim()));
			}
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if (vendorNo!=null && !vendorNo.equals("")){
				criteria.add(Restrictions.eq("vendorNo", vendorNo));
			}
			if (packageStatus!=null && !packageStatus.equals("")){
				criteria.add(Restrictions.eq("packageStatus", packageStatus));
			}

			resultList = (List<SCPackage>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainParentJobList(String jobNo) throws DatabaseOperationException {
		if(jobNo==null)
			throw new DatabaseOperationException("jobNo is null");
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		
		criteria.createAlias("job", "job");
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("internalJobNo", jobNo.trim()));
		criteria.setProjection(Projections.distinct(Projections.property("job.jobNumber")));
		return criteria.list();
	}

	public SCPackage obtainPackageStatistics(String vendorNo, Date startDate) throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo==null?null:vendorNo.trim()));
		criteria.add(Restrictions.le("createdDate", new Date()));
		criteria.add(Restrictions.ge("createdDate", startDate));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sum("this.remeasuredSubcontractSum"),"remeasuredSubcontractSum")
				.add(Projections.sum("this.approvedVOAmount"),"approvedVOAmount")
				.add(Projections.sum("this.totalCumWorkDoneAmount"),"totalCumWorkDoneAmount")
		);
		criteria.setResultTransformer(Transformers.aliasToBean(SCPackage.class));
		return (SCPackage) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainPackagesByVendorNo(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo==null?null:vendorNo.trim()));
		if (division!=null && !"".equals(division))
			criteria.add(Restrictions.eq("job.division", division));
		if (jobNumber!=null && !"".equals(jobNumber))
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
		if (packageNumber!=null && !"".equals(packageNumber))
			criteria.add(Restrictions.eq("packageNo", packageNumber));
		if (paymentType!=null && !"".equals(paymentType)){
			if("I".equals(paymentType))
				criteria.add(Restrictions.or(	Restrictions.isNull("paymentStatus"),
							 Restrictions.or(	Restrictions.eq("paymentStatus", "I"),
							 Restrictions.or(	Restrictions.eq("paymentStatus", ""),
									 			Restrictions.eq("paymentStatus", " ")))));
			else 
				criteria.add(Restrictions.eq("paymentStatus", paymentType));
		}
		
		if (paymentTerm!=null && !"".equals(paymentTerm))
			criteria.add(Restrictions.eq("paymentTerms", paymentTerm)); 			
			
		criteria.createAlias("job", "job");
		criteria.addOrder(Order.asc("job.jobNumber")).addOrder(Order.asc("packageNo"));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SCPackage> obtainSubcontractFilteredList(String subcontractorNumber, String division, String jobNumber, String packageNumber, String paymentStatus, String paymentType) throws DatabaseOperationException{
		List<SCPackage> result = null;
		try{			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");

			if (division!=null && !"".equals(division))
				criteria.add(Restrictions.eq("job.division", division));
			if (jobNumber!=null && !"".equals(jobNumber))
				criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			if (packageNumber!=null && !"".equals(packageNumber))
				criteria.add(Restrictions.eq("packageNo", packageNumber));
			if (subcontractorNumber!=null && !"".equals(subcontractorNumber))
				criteria.add(Restrictions.eq("vendorNo", subcontractorNumber)); 
			if (paymentStatus!=null && !"".equals(paymentStatus)){
				if("I".equals(paymentStatus))
					criteria.add(Restrictions.or(	Restrictions.isNull("paymentStatus"),
								 Restrictions.or(	Restrictions.eq("paymentStatus", "I"),
								 Restrictions.or(	Restrictions.eq("paymentStatus", ""),
										 			Restrictions.eq("paymentStatus", " ")))));
				else 
					criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
			}
			
			if (paymentType!=null && !"".equals(paymentType))
				criteria.add(Restrictions.eq("paymentTerms", paymentType)); 
			
			//  Sort the subcontract list in Report > Finance > Subcontract List
			criteria.addOrder(Order.asc("job.jobNumber")).addOrder(Order.asc("packageNo"));
			result = (List<SCPackage>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainSCPackageNosUnderPaymentRequisition(String jobNumber) {
		List<String> packageNos = new ArrayList<String>();

		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("job", "job");
		if (jobNumber!=null && !"".equals(jobNumber))
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));

		criteria.add(Restrictions.eq("paymentStatus", "D"));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("packageNo"))
		);

		packageNos=criteria.list();

		return packageNos;
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
	
}
