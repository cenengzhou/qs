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
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.PaymentCert;
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
	private PaymentCertHBDao scPaymentCertHBDao;
	
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
	public List<Subcontract> obtainPackages(String jobNumber, String packageNo, String packageType)	throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("packageType", Subcontract.SUBCONTRACT_PACKAGE));
		
		return criteria.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<Subcontract> getSCPackages(JobInfo jobInfo) throws Exception {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.addOrder(Order.asc("packageNo"));
			return (List<Subcontract>) criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getSCPackages(Job job)");
			throw new DatabaseOperationException(he);
		}
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
	public List<Object[]> getUnawardedPackageNos(JobInfo jobInfo) throws Exception{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public List<Object[]> getAwardedPackageStore(JobInfo jobInfo) throws Exception{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public List<String> getUneditablePackageNos(JobInfo jobInfo) throws Exception{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public List<String> obtainFinalizedPackageNos(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
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
	public List<Subcontract> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber,String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> companyList, Integer status, String reportType) throws DatabaseOperationException{
		List<Subcontract> result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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
				criteria.addOrder(Order.asc("job.company"))
						.addOrder(Order.asc("job.division"))
						.addOrder(Order.asc("job.jobNumber"))
						.addOrder(Order.asc("packageNo"));
			}
			result = (List<Subcontract>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Subcontract> getPackagesFromList(JobInfo jobInfo, List<String> packageNos) throws Exception{
		List<Subcontract> result = null;
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.in("packageNo", packageNos));
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

	public void resetPackageTA(Subcontract subcontract) throws Exception{
		//delete all ta records
		List<Tender> tenderAnalysisList = tenderAnalysisHBdao.obtainTenderAnalysisList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
		for(Tender tenderAnalysis : tenderAnalysisList){
			List<TenderDetail> details = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
			for (TenderDetail detail: details){
				tenderDetailDao.delete(detail);
			}

			tenderAnalysisHBdao.delete(tenderAnalysis);
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
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
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
	public List<Subcontract> obtainPackageListForSCPaymentPanel(String jobNumber) {
		String[] packageNoArray; 
		Criteria criteriaForPaymentCert = scPaymentCertHBDao.getSession().createCriteria(PaymentCert.class);
		criteriaForPaymentCert.add(Restrictions.eq("jobNo", jobNumber.trim()));
		List<PaymentCert> scPaymentCertList = criteriaForPaymentCert.list();
		
		if(scPaymentCertList.size()>0){
			packageNoArray = new String[scPaymentCertList.size()];
		} else {
			packageNoArray = new String[0];
		}
		
		for(int i = 0; i<packageNoArray.length; i++ ){
			packageNoArray[i] = scPaymentCertList.get(i).getPackageNo();
 		}
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));			
		criteria.add(Restrictions.and(Restrictions.isNotNull("packageNo"),Restrictions.ne("packageNo", "0")));
		criteria.add(Restrictions.or(Restrictions.eq("subcontractStatus", Integer.valueOf(500)), Restrictions.in("packageNo", packageNoArray)));
		criteria.addOrder(Order.asc("packageNo"));
		return criteria.list();
	}


	@SuppressWarnings("unchecked")
	public List<Subcontract> getScPackageListForSystemAdmin(String jobNo,String packageNo, String vendorNo, String packageStatus) throws DatabaseOperationException {
		List<Subcontract> resultList = new LinkedList<Subcontract>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			if (jobNo!=null && !jobNo.equals("")){
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo.trim()));
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

			resultList = (List<Subcontract>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainParentJobList(String jobNo) throws DatabaseOperationException {
		if(jobNo==null)
			throw new DatabaseOperationException("jobNo is null");
		Criteria criteria = getSession().createCriteria(this.getType());
		
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("internalJobNo", jobNo.trim()));
		criteria.setProjection(Projections.distinct(Projections.property("jobInfo.jobNumber")));
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
	public List<Subcontract> obtainPackagesByVendorNo(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo==null?null:vendorNo.trim()));
		if (division!=null && !"".equals(division))
			criteria.add(Restrictions.eq("jobInfo.division", division));
		if (jobNumber!=null && !"".equals(jobNumber))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.addOrder(Order.asc("jobInfo.jobNumber")).addOrder(Order.asc("packageNo"));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Subcontract> obtainSubcontractFilteredList(String subcontractorNumber, String division, String jobNumber, String packageNumber, String paymentStatus, String paymentType) throws DatabaseOperationException{
		List<Subcontract> result = null;
		try{			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");

			if (division!=null && !"".equals(division))
				criteria.add(Restrictions.eq("jobInfo.division", division));
			if (jobNumber!=null && !"".equals(jobNumber))
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			criteria.addOrder(Order.asc("jobInfo.jobNumber")).addOrder(Order.asc("packageNo"));
			result = (List<Subcontract>)criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainSCPackageNosUnderPaymentRequisition(String jobNumber) {
		List<String> packageNos = new ArrayList<String>();

		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		if (jobNumber!=null && !"".equals(jobNumber))
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));

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
	
	
	/*************************************** FUNCTIONS FOR PCMS - END*********************************************************/	
}
