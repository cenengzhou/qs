package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper;
@Repository
public class SCDetailsHBDao extends BaseHibernateDao<SCDetails> {
	private static int RECORDS_PER_PAGE = 200;

	private Logger logger = Logger.getLogger(SCDetailsHBDao.class.getName());
	public SCDetailsHBDao() {
		super(SCDetails.class);
	}
	
	/**
	 * @author tikywong
	 * modified on May 15, 2013
	 */
	@SuppressWarnings("unchecked")
	public List<SCDetails> obtainSCDetails(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			criteria.createAlias("scPackage", "scPackage");

			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("sequenceNo"));
	
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<SCDetailsBQ> obtainSCDetailsBQ(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetailsBQ> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			criteria.createAlias("scPackage", "scPackage");

			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("sequenceNo"));
	
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public PaginationWrapper<SCDetails> getScDetailsByPage(SCPackage scPackage, String billItem, String description, String lineType, int pageNum) throws Exception{
		logger.info("JobNumber: " + scPackage.getJob().getJobNumber() + ", PackageNo: " + scPackage.getPackageNo() + ", billItem: " + billItem + ", description: " + description + ", lineType:" + lineType + ", pageNum: " + pageNum);
		PaginationWrapper<SCDetails> wrapper = new PaginationWrapper<SCDetails>();
		
		//Get count
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("scPackage", scPackage));
		if(!GenericValidator.isBlankOrNull(billItem)){
			billItem = billItem.replace('*', '%');
			criteria.add(Restrictions.ilike("billItem", billItem, MatchMode.ANYWHERE));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			description = description.replace('*', '%');
			criteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		if(!GenericValidator.isBlankOrNull(lineType))
			criteria.add(Restrictions.eq("lineType", lineType));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		
		wrapper.setTotalRecords(count);
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalPage((count + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		
		logger.info("Total Records: "+wrapper.getTotalRecords()+" Current Page: "+wrapper.getCurrentPage()+" Total Page: "+wrapper.getTotalPage());
		
		//Get page of records
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("scPackage", scPackage));
		if(!GenericValidator.isBlankOrNull(billItem)){
			criteria.add(Restrictions.ilike("billItem", billItem, MatchMode.ANYWHERE));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			criteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		if(!GenericValidator.isBlankOrNull(lineType))
			criteria.add(Restrictions.eq("lineType", lineType));
		criteria.addOrder(Order.asc("billItem"));
		criteria.addOrder(Order.asc("resourceNo"));
//		criteria.addOrder(Order.asc("sequenceNo"));
		criteria.setFirstResult(pageNum * RECORDS_PER_PAGE);
		criteria.setMaxResults(RECORDS_PER_PAGE+1);	//exclusive  1-201 (not including 201)
		
		wrapper.setCurrentPageContentList(criteria.list());
		return addTotalToWrapper(wrapper);
	}
	
	// Last modified: Brian Tse
	private PaginationWrapper<SCDetails> addTotalToWrapper(PaginationWrapper<SCDetails> wrapper){
		logger.info("Total Pages before Add Total: " + wrapper.getTotalPage());
		logger.info("Total Records before Add Total: " + wrapper.getTotalRecords());
		logger.info("Current Page before Add Total: " + wrapper.getCurrentPage());
		logger.info("Records size in current Page before Add Total: " + wrapper.getCurrentPageContentList().size());
		// check if last page have space to add total line = is it require to add a page to add total ?
		if(wrapper.getTotalRecords() % RECORDS_PER_PAGE == 0){
			// need to add a new page to add total (affect total record, total page or maybe current page)
			wrapper.setTotalRecords(wrapper.getTotalRecords() + 1);
			wrapper.setTotalPage(wrapper.getTotalPage() + 1);
			
			if(wrapper.getTotalPage() == (wrapper.getCurrentPage() + 1)){
				List<SCDetails> temp = new ArrayList<SCDetails>();
				temp.addAll(wrapper.getCurrentPageContentList());
				SCDetails totalLine = new SCDetails();
				totalLine.setDescription("TOTAL:");
				temp.add(totalLine);
				logger.info("List<SCDetails> temp size = " + temp.size());
				wrapper.getCurrentPageContentList().clear();
				wrapper.getCurrentPageContentList().addAll(temp);
				logger.info("List<SCDetails> of Wrapper size = " + wrapper.getCurrentPageContentList().size());
			}
		}
		else{
			// just put the total at the last page (affect total records only)
			wrapper.setTotalRecords(wrapper.getTotalRecords() + 1);
			
			// check if calling page is last page
			if(wrapper.getTotalPage() == (wrapper.getCurrentPage() + 1)){
				List<SCDetails> temp = new ArrayList<SCDetails>();
				temp.addAll(wrapper.getCurrentPageContentList());
				SCDetails totalLine = new SCDetails();
				totalLine.setDescription("TOTAL:");
				temp.add(totalLine);
				logger.info("List<SCDetails> temp size = " + temp.size());
				wrapper.getCurrentPageContentList().clear();
				wrapper.getCurrentPageContentList().addAll(temp);
				logger.info("List<SCDetails> of Wrapper size = " + wrapper.getCurrentPageContentList().size());
			}
		}
		logger.info("Total Pages after Add Total: " + wrapper.getTotalPage());
		logger.info("Total Records after Add Total: " + wrapper.getTotalRecords());
		logger.info("Current Page after Add Total: " + wrapper.getCurrentPage());
		logger.info("Records size in current Page after Add Total: " + wrapper.getCurrentPageContentList().size());
		
		return wrapper;
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetails> getSCDetails(SCPackage scPackage) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("scPackage", scPackage));
		return (List<SCDetails>)criteria.list();
	}
	
	public Integer obtainSCDetailsMaxSeqNo(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("scPackage", "scPackage");
		criteria.add(Restrictions.eq("jobNo",jobNumber));
		criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
		criteria.addOrder(Order.desc("sequenceNo"));
		criteria.setProjection(Projections.max("sequenceNo"));

		if(Integer.valueOf(criteria.uniqueResult().toString())==null)
			return 0;
		else
			return Integer.valueOf(criteria.uniqueResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Get BQSCDetails
	 * As V1/V3 included in Split SC, V1 and V3 line type will be included
	 */
	public List<SCDetails> getBQSCDetails(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.eq("lineType","BQ" ), Restrictions.eq("lineType","B1")),
					Restrictions.or( 
							Restrictions.and(Restrictions.eq("lineType","V1"),Restrictions.ne("costRate", Double.valueOf(0.0))),
							Restrictions.eq("lineType","V3"))
					));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			resultList = criteria.list();
			return resultList;
		}
		catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCDetails> getSCDetails_Deleted(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.INACTIVE));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCDetails obtainSCDetail(String jobNumber, String subcontractNo,String sequenceNo) throws DatabaseOperationException{
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("sequenceNo", new Integer(sequenceNo)));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			return (SCDetails)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: obtainSCDetail(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public SCDetails getSCDetail(SCPackage scPackage, String sequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("scPackage", scPackage));
			criteria.add(Restrictions.eq("sequenceNo", new Integer(sequenceNo)));
			return (SCDetails)criteria.uniqueResult();
		}catch(HibernateException he){
			throw new DatabaseOperationException("Failed: getSCDetail(SCPackage scPackage, String sequenceNo)");
		}
	}
	
	public SCDetails getSCDetail(SCPackage scPackage, String billItem, Integer resourceNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("scPackage", scPackage));
		criteria.add(Restrictions.or(Restrictions.eq("billItem", billItem), Restrictions.like("billItem", billItem + " %")));
		criteria.add(Restrictions.eq("resourceNo", resourceNo));
		return (SCDetails)criteria.uniqueResult();
	}
	
	/**
	 * @author tikywong
	 * Apr 12, 2011 4:49:25 PM
	 */
	public SCDetails getSCDetail(String jobNumber, String packageNo, String billItem, String objectCode, String subsidiaryCode, Integer resourceNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		
		criteria.createAlias("scPackage", "scPackage");
		
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		if(!GenericValidator.isBlankOrNull(jobNumber))
			criteria.add(Restrictions.eq("jobNo", jobNumber));
		if(!GenericValidator.isBlankOrNull(packageNo))
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
		if(!GenericValidator.isBlankOrNull(billItem))
			criteria.add(Restrictions.eq("billItem", billItem));
		if(!GenericValidator.isBlankOrNull(objectCode))
			criteria.add(Restrictions.eq("objectCode", objectCode));
		if(!GenericValidator.isBlankOrNull(subsidiaryCode))
			criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		if(resourceNo!=null)
			criteria.add(Restrictions.eq("resourceNo", resourceNo));
		return (SCDetails)criteria.uniqueResult();
	}
	
	public SCDetails getSCDetail(SCDetails scDetails) throws DatabaseOperationException{
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",scDetails.getScPackage().getJob().getJobNumber()));
			//criteria.add(Restrictions.eq("jobNo",scDetails.getJobNo()));
			criteria.add(Restrictions.eq("scPackage.packageNo", scDetails.getScPackage().getPackageNo()));
			criteria.add(Restrictions.eq("sequenceNo", scDetails.getSequenceNo()));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			return (SCDetails)criteria.uniqueResult();

		}catch (HibernateException he) {
			logger.info("Fail: getSCDetail(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCDetail(SCDetails scDetails) throws Exception{
		SCDetails dbObj = this.getSCDetail(scDetails);
		if (dbObj==null)
			throw new DatabaseOperationException("SC Detail did not exist. Update Fail"); 
		if (BasePersistedAuditObject.INACTIVE.equals(dbObj.getSystemStatus()))
			throw new Exception ("Deleted Record! Cannot be saved.");
		if (scDetails.equals(dbObj)){
			dbObj.updateSCDetails(scDetails);
			saveOrUpdate(dbObj);
			return true;
		}
		return false;
	}
		
	public void inactivateSCDetails(SCDetails scDetails) throws DatabaseOperationException {
		scDetails.inactivate();
		saveOrUpdate(scDetails);
	}
	
	public boolean addSCDetail(SCDetails scDetails) throws DatabaseOperationException{
		SCDetails dbObj = this.getSCDetail(scDetails);
		if (dbObj!=null)
			throw new DatabaseOperationException("New Addendum existed! Add SC Detail Fail"); 
		scDetails.setCreatedDate(new Date());
		saveOrUpdate(scDetails);
		return true;
	}
	
	public void addSCDetailVOWithBudget(SCDetails scDetailToUpdate) throws DatabaseOperationException {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getType());
		criteria.add(Restrictions.eq("scPackage", scDetailToUpdate.getScPackage()));
		criteria.add(Restrictions.eq("resourceNo", scDetailToUpdate.getResourceNo()));
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		@SuppressWarnings("unchecked")
		List<SCDetails> result = (ArrayList<SCDetails>)criteria.list();
		if (result!=null && result.size()>0){
			scDetailToUpdate=null;
			throw new DatabaseOperationException("New Addendum existed! Add SC Detail Fail");
		}
		saveOrUpdate(scDetailToUpdate);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetails> getSCDetails(String jobNumber, String subcontractNo,String lineType) throws Exception{
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			//criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("lineType", lineType));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Get BQ Lines and VO Lines From specified Sub-contract excluding suspended line
	 * Mainly use in SC Addendum Enquiry
	 * 
	 * @author peterchan 
	 */
	public List<SCDetails> getAddendumDetails(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(SCDetailsBQ.class);
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			//criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("scPackage.packageNo", subcontractNo));
			criteria.add(Restrictions.or(Restrictions.ne("approved",SCDetails.SUSPEND),Restrictions.isNull("approved")));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCDetails> getSCDetailByResourceNo(SCPackage scpackage,Integer resourceNo) throws DatabaseOperationException {
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPackage", scpackage));
			criteria.add(Restrictions.eq("resourceNo", resourceNo));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			return criteria.list();
		}catch (HibernateException he) {
			logger.info("Fail: getSCDetail(String jobNumber, String subcontractNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public Integer getNextSequenceNo(SCPackage scPackage) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("scPackage", scPackage));
		criteria.setProjection(Projections.max("sequenceNo"));
		Integer maxSequenceNo = (Integer)criteria.uniqueResult();
		if(maxSequenceNo == null)
			return Integer.valueOf(1);
		else
			return maxSequenceNo + 1;
	}

	/**
	 * @author tikywong
	 * May 24, 2011 12:12:07 PM
	 */
	//Issue with creating duplicated Data in the database
	public void updateSCDetails(List<SCDetails> scDetails) {
		try {
			if(scDetails==null)
				return;
			
			Session session = this.getSessionFactory().getCurrentSession();
			Transaction tx = session.beginTransaction();
			
			for(int i=0; i<scDetails.size(); i++){
				session.update(scDetails.get(i));
				
				if(i%20 == 0){
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} catch (Exception e) {
			/* 05 April, 2014
			The EJB cannot know when the encompassing transaction is truly done. 
			Only the transaction coordinator can know, and it is the coordinator that
			actually terminates the transaction, whether by committing it or rolling it back.
			For whatever reason, coding for WebLogic 7.0  has changed to throw an exception when
			you call commit().*/
			
			//e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 13, 2011 4:59:05 PM
	 */
	public boolean updateSCDetailWorkdoneQtybyHQL(SCDetails scDetail, String username) throws DatabaseOperationException{
//		logger.info("UPDATE: updateSCDetailbyHQL(SCDetails scDetail)");
		if(scDetail == null)
			return false;
		
		try{
			Long scDetailID = scDetail.getId();
			String jobNumber = scDetail.getJobNo();
			Double updatedCumulativeWorkdoneQuantity = scDetail.getCumWorkDoneQuantity();

			String hql = "UPDATE SCDetails SET cumWorkDoneQuantity = :updatedCumulativeWorkdoneQuantity, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :scDetailID AND jobNo = :jobNumber";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			
			query.setDouble("updatedCumulativeWorkdoneQuantity", updatedCumulativeWorkdoneQuantity);
			query.setString("jobNumber", jobNumber);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("scDetailID", scDetailID);

			query.executeUpdate();
			return true;
		}catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCDetails> getSCDetailsWithCorrSC(SCPackage scPackage) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getType());
		criteria.add(Restrictions.eq("scPackage", scPackage));
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.isNotNull("contraChargeSCNo"));
		criteria.add(Restrictions.ne("contraChargeSCNo","0"));
		criteria.add(Restrictions.in("lineType", new Object[]{"L2","D2"} ));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SCDetails> getScDetails(String jobNumber) throws DatabaseOperationException {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getType());
		criteria.createAlias("scPackage", "scPackage");
		criteria.add(Restrictions.eq("scPackage.subcontractStatus",Integer.valueOf(500)));
		criteria.createAlias("scPackage.job", "scJob");
		criteria.add(Restrictions.eq("scJob.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.addOrder(Order.asc("scPackage.packageNo"));
		criteria.addOrder(Order.asc("sequenceNo"));
		return criteria.list();
	}


	@SuppressWarnings("unchecked")
	public List<SCDetails> getScDetailsListForSystemAdmin(String jobNo,String packageNo, String billItem, String lineType,
			String description, String objectCode, String subsidiaryCode)
			throws DatabaseOperationException {
		List<SCDetails> resultList = new LinkedList<SCDetails>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());

			if (jobNo!=null && !jobNo.equals("")){
				criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			}
			criteria.createAlias("scPackage", "scPackage");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			}
			if (billItem!=null && !billItem.equals("")){
				criteria.add(Restrictions.eq("billItem", billItem));
			}
			if (lineType!=null && !lineType.equals("")){
				criteria.add(Restrictions.eq("lineType", lineType));
			}
			if (description!=null && !description.equals("")){
				criteria.add(Restrictions.ilike("description", description,MatchMode.ANYWHERE));
			}
			if (objectCode!=null && !objectCode.equals("")){
				criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if (subsidiaryCode!=null && !subsidiaryCode.equals("")){
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			resultList = (List<SCDetails>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;

	}

	@SuppressWarnings("unchecked")
	public List<AccountCodeWrapper> getAccountCodeListByJob(String jobNumber) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"),"objectAccount")
				.add(Projections.groupProperty("subsidiaryCode"),"subsidiary"));
		criteria.addOrder(Order.asc("subsidiaryCode")).addOrder(Order.asc("objectCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountCodeWrapper.class));
		return criteria.list();
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 1
	 * **/
	public SCDetailsBQ obtainSCDetailsByResourceNo(String jobNo,String packageNo, Integer resourceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("scPackage", "scPackage");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			}
			
			if(resourceNo!=null)
				criteria.add(Restrictions.eq("resourceNo", resourceNo));
			
			return (SCDetailsBQ) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 2, 3
	 * **/
	public SCDetailsBQ obtainSCDetailsByBQItem(	String jobNo,String packageNo, String billItem, 
												String objectCode, String subsidiaryCode, Integer resourceNo
												) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("scPackage", "scPackage");
			if(!GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));

			if(!GenericValidator.isBlankOrNull(billItem))
				criteria.add(Restrictions.eq("billItem", billItem));

			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("objectCode", objectCode));

			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));

			if(resourceNo!=null)
				criteria.add(Restrictions.eq("resourceNo", resourceNo));
			
			return (SCDetailsBQ) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * @author koeyyeung
	 * **/
	public SCDetailsBQ obtainSCDetailsByTADetailID(String jobNo,String packageNo, Long tenderAnalysisDetail_ID) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("scPackage", "scPackage");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			}

			if(tenderAnalysisDetail_ID==null)
				return null;

			criteria.add(Restrictions.eq("tenderAnalysisDetail_ID", tenderAnalysisDetail_ID));
			
			return (SCDetailsBQ) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetails> obtainSCDetailsObjectCodeList(String jobNo,String packageNo) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("scPackage", "scPackage");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
			}
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode"));
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(SCDetails.class));
			return criteria.list();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SCDetailsBQ> obtainSCDetailsByObjectCode(String jobNo,String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));

			criteria.createAlias("scPackage", "scPackage");
			if(!GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));


			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("objectCode", objectCode));

			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));


			return criteria.list();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SCDetails> obtainContraChargeDetail(ContraChargeSearchingCriteriaWrapper searchingCriteria)
			throws DatabaseOperationException {
		try{
			List<SCDetails> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			criteria.createAlias("scPackage", "scPackage");

			if(searchingCriteria.getJobNumber() != null && !searchingCriteria.getJobNumber().equals(""))
				criteria.add(Restrictions.eq("jobNo",searchingCriteria.getJobNumber()));
			if(searchingCriteria.getLineType() != null && !searchingCriteria.getLineType().equals(""))
				criteria.add(Restrictions.eq("lineType",searchingCriteria.getLineType()));
			else
				criteria.add(Restrictions.in("lineType", new String[]{"C1","C2"}));
			if(searchingCriteria.getBQItem() != null && !searchingCriteria.getBQItem().equals(""))
				criteria.add(Restrictions.ilike("billItem","%"+searchingCriteria.getBQItem()+"%"));
			if(searchingCriteria.getDescription() != null && !searchingCriteria.getDescription().equals(""))
				criteria.add(Restrictions.ilike("description","%"+searchingCriteria.getDescription()+"%"));
			if(searchingCriteria.getObjectCode() != null && !searchingCriteria.getObjectCode().equals(""))
				criteria.add(Restrictions.ilike("objectCode",searchingCriteria.getObjectCode()+"%"));
			if(searchingCriteria.getSubsidiaryCode() != null && !searchingCriteria.getSubsidiaryCode().equals(""))
				criteria.add(Restrictions.ilike("subsidiaryCode",searchingCriteria.getSubsidiaryCode()+"%"));
			if(searchingCriteria.getSubcontractNumber() != null && !searchingCriteria.getSubcontractNumber().equals(""))
				criteria.add(Restrictions.eq("scPackage.packageNo",searchingCriteria.getSubcontractNumber()));
			
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("sequenceNo"));
	
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainContraChargeDetailOptionList(String fieldName)
			throws DatabaseOperationException {
		try{
			List<String> resultList;
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			criteria.createAlias("scPackage", "scPackage");
			criteria.setProjection(Projections.distinct(Projections.property(fieldName)));
		    criteria.addOrder(Order.asc(fieldName));
		    
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
}
