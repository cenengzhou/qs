package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AuditResourceSummary;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.IVPostingHistory;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.ivPosting.AccountIVWrapper;
@Repository
public class BQResourceSummaryHBDao extends BaseHibernateDao<BQResourceSummary> {
	
	private static final int RECORDS_PER_PAGE = 400;
	
	public BQResourceSummaryHBDao(){
		super(BQResourceSummary.class);
	}
	
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private SCPackageHBDao scPackageDao;
	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> obtainSCResourceSummariesByJobNumber(String jobNumber) throws DatabaseOperationException{
		List<BQResourceSummary> resourceSummaries;
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.like("objectCode", "14%"));
			criteria.add(Restrictions.ne("packageNo", "0"));
			
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("BQResourceSummary Dao: obtainSCResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> getResourceSummariesByJobNumber(String jobNumber) throws DatabaseOperationException{
		List<BQResourceSummary> resourceSummaries;
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("BQResourceSummary Dao: getResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> getResourceSummariesByJob(Job job) throws DatabaseOperationException{
		List<BQResourceSummary> resourceSummaries;
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("BQResourceSummary Dao: getResourceSummariesByJob - " + job.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> getResourceSummariesSearch(Job job, 
			String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		logger.info("BQResourceSummary Dao: getResourceSummariesSearch - " + job.getJobNumber());
		List<BQResourceSummary> resourceSummaries;
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if(!GenericValidator.isBlankOrNull(packageNo)){
				if(packageNo.contains("*")){
					criteria.add(Restrictions.like("packageNo", packageNo.replace("*", "%")));
				}
				else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				if(objectCode.contains("*")){
					criteria.add(Restrictions.like("objectCode", objectCode.replace("*", "%")));
				}
				else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
				if(subsidiaryCode.contains("*")){
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode.replace("*", "%")));
				}
				else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("packageNo"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public RepackagingPaginationWrapper<BQResourceSummary> obtainResourceSummariesSearchByPage(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws DatabaseOperationException{
		logger.info("BQResourceSummary Dao: getResourceSummariesSearchByPage - " + job.getJobNumber());
		
		try{
			RepackagingPaginationWrapper<BQResourceSummary> paginationWrapper = new RepackagingPaginationWrapper<BQResourceSummary>();
			paginationWrapper.setCurrentPage(pageNum);
			//Get total number of records (pages)
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if(!GenericValidator.isBlankOrNull(packageNo)){
				packageNo = packageNo.replace("*", "%");
				if(packageNo.contains("%")){
					criteria.add(Restrictions.like("packageNo", packageNo));
				}
				else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				objectCode = objectCode.replace("*", "%");
				if(objectCode.contains("%")){
					criteria.add(Restrictions.like("objectCode", objectCode));
				}
				else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if(subsidiaryCode.contains("%")){
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				}
				else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if(!GenericValidator.isBlankOrNull(description)){
				description = description.replace("*", "%");
				if(description.contains("%")){
					criteria.add(Restrictions.ilike("resourceDescription", description));
				}
				else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			if(!GenericValidator.isBlankOrNull(type)){
				if(type.equals("blank"))
					criteria.add(Restrictions.isNull("resourceType"));
				else
					criteria.add(Restrictions.eq("resourceType", type));
			}
			if(!GenericValidator.isBlankOrNull(levyExcluded)){
				criteria.add(Restrictions.eq("excludeLevy", levyExcluded.equals("Y")?true:false));
			}
			if(!GenericValidator.isBlankOrNull(defectExcluded)){
				criteria.add(Restrictions.eq("excludeDefect", defectExcluded.equals("Y")?true:false));
			}
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount())
					.add(Projections.sqlProjection("sum({alias}.rate * {alias}.quantity) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE})));
			Object[] rowTotal = (Object[])criteria.uniqueResult();
			Integer rows = rowTotal[0] == null ? Integer.valueOf(0) : ((Long)rowTotal[0]).intValue();
			Double totalAmount = rowTotal[1] == null ? Double.valueOf(0) : (Double)rowTotal[1];
			logger.info("getResourceSummariesSearchByPage - Rows: " + rows + ", Total: " + totalAmount);
			paginationWrapper.setTotalRecords(rows);
			paginationWrapper.setTotalSellingValue(totalAmount);
			paginationWrapper.setTotalPage((rows + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
			
			if(rows.equals(Integer.valueOf(0))){
				paginationWrapper.setCurrentPageContentList(new ArrayList<BQResourceSummary>());
				return paginationWrapper;
			}
			
			//Get 1 page of records
			String hql = "from BQResourceSummary where job = :job and systemStatus = 'ACTIVE'"; 
			if(!GenericValidator.isBlankOrNull(packageNo)){
				if(packageNo.contains("%")){
					hql += " and packageNo like '" + packageNo + "'";
				}
				else{
					hql += " and packageNo = '" + packageNo + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				if(objectCode.contains("%")){
					hql += " and objectCode like '" + objectCode + "'";
				}
				else{
					hql += " and objectCode = '" + objectCode + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
				if(subsidiaryCode.contains("%")){
					hql += " and subsidiaryCode like '" + subsidiaryCode + "'";
				}
				else{
					hql += " and subsidiaryCode = '" + subsidiaryCode + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(description)){
				if(description.contains("%")){
					hql += " and lower(resourceDescription) like '" + description.toLowerCase() + "'";
				}
				else
					hql += " and lower(resourceDescription) like '%" + description.toLowerCase() + "%'";
			}
			if(!GenericValidator.isBlankOrNull(type)){
				if(type.equals("blank"))
					hql += " and resourceType is null";
				else
					hql += String.format(" and resourceType='%s'", type);
			}
			if(!GenericValidator.isBlankOrNull(levyExcluded)){
				hql += String.format(" and excludeLevy='%s'", levyExcluded.equals("Y")?1:0);
			}
			if(!GenericValidator.isBlankOrNull(defectExcluded)){
				hql += String.format(" and excludeDefect='%s'", defectExcluded.equals("Y")?1:0);
			}
			hql += " order by substr(objectCode, 1, 2) asc, packageNo asc, objectCode asc, subsidiaryCode asc, resourceDescription asc, unit asc, rate asc";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setEntity("job", job);
			query.setFirstResult(pageNum * RECORDS_PER_PAGE);
			query.setMaxResults(RECORDS_PER_PAGE);
			
			List<BQResourceSummary> resultList = query.list();
			paginationWrapper.setCurrentPageContentList(resultList==null? new ArrayList<BQResourceSummary>():resultList);
			
			return paginationWrapper;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public Double getMarkupAmountInSearch(Job job, String packageNo, String objectCode, 
			String subsidiaryCode, String description) throws DatabaseOperationException{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		criteria.add(Restrictions.like("subsidiaryCode", "9%"));
		if(!GenericValidator.isBlankOrNull(packageNo)){
			packageNo = packageNo.replace("*", "%");
			if(packageNo.contains("%")){
				criteria.add(Restrictions.like("packageNo", packageNo));
			}
			else
				criteria.add(Restrictions.eq("packageNo", packageNo));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%")){
				criteria.add(Restrictions.like("objectCode", objectCode));
			}
			else
				criteria.add(Restrictions.eq("objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%")){
				criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
			}
			else
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			description = description.replace("*", "%");
			if(description.contains("%")){
				criteria.add(Restrictions.ilike("resourceDescription", description));
			}
			else
				criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.sqlProjection("sum({alias}.rate * {alias}.quantity) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}));
		return (Double)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum) throws DatabaseOperationException{
		try{
			IVInputPaginationWrapper paginationWrapper = new IVInputPaginationWrapper();
			paginationWrapper.setCurrentPage(pageNum);
			//Get total number of records (pages)
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if(!GenericValidator.isBlankOrNull(packageNo)){
				packageNo = packageNo.replace("*", "%");
				if(packageNo.contains("%")){
					criteria.add(Restrictions.like("packageNo", packageNo));
				}
				else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				objectCode = objectCode.replace("*", "%");
				if(objectCode.contains("%")){
					criteria.add(Restrictions.like("objectCode", objectCode));
				}
				else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if(subsidiaryCode.contains("%")){
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				}
				else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if(!GenericValidator.isBlankOrNull(description)){
				description = description.replace("*", "%");
				if(description.contains("%")){
					criteria.add(Restrictions.ilike("resourceDescription", description));
				}
				else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount())
					.add(Projections.sum("currIVAmount"))
					.add(Projections.sum("postedIVAmount")));
			Object[] rowTotals = (Object[])criteria.uniqueResult();
			Integer rows = rowTotals[0] == null ? Integer.valueOf(0) : (Integer)rowTotals[0];
			Double ivCumTotal = rowTotals[1] == null ? Double.valueOf(0) : (Double)rowTotals[1];
			Double ivPostedTotal = rowTotals[2] == null ? Double.valueOf(0) : (Double)rowTotals[2];
			
			logger.info("rows ; " + rows +" ivCumTotal : " + ivCumTotal +" ivPostedTotal : " +ivPostedTotal );
			paginationWrapper.setTotalRecords(rows);
			paginationWrapper.setIvCumTotal(ivCumTotal);
			paginationWrapper.setIvMovementTotal(ivCumTotal - ivPostedTotal);
			paginationWrapper.setTotalPage((rows + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
			
			if(rows.equals(Integer.valueOf(0))){
				paginationWrapper.setCurrentPageContentList(new ArrayList<BQResourceSummary>());
				return paginationWrapper;
			}
			
			//Get 1 page of records
			String hql = "from BQResourceSummary where job = :job and systemStatus = 'ACTIVE'"; 
			if(!GenericValidator.isBlankOrNull(packageNo)){
				if(packageNo.contains("%")){
					hql += " and packageNo like '" + packageNo + "'";
				}
				else{
					hql += " and packageNo = '" + packageNo + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				if(objectCode.contains("%")){
					hql += " and objectCode like '" + objectCode + "'";
				}
				else{
					hql += " and objectCode = '" + objectCode + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
				if(subsidiaryCode.contains("%")){
					hql += " and subsidiaryCode like '" + subsidiaryCode + "'";
				}
				else{
					hql += " and subsidiaryCode = '" + subsidiaryCode + "'";
				}
			}
			if(!GenericValidator.isBlankOrNull(description)){
				if(description.contains("%")){
					hql += " and lower(resourceDescription) like '" + description.toLowerCase() + "'";
				}
				else
					hql += " and lower(resourceDescription) like '%" + description.toLowerCase() + "%'";
			}
			hql += " order by substr(objectCode, 1, 2) asc, packageNo asc, objectCode asc, subsidiaryCode asc, resourceDescription asc, unit asc, rate asc";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setEntity("job", job);
			query.setFirstResult(pageNum * RECORDS_PER_PAGE);
			query.setMaxResults(RECORDS_PER_PAGE);
			paginationWrapper.setCurrentPageContentList(query.list());
			return paginationWrapper;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	/**
	 * @author tikywong
	 * Apr 29, 2011 10:58:20 AM
	 */
	public BQResourceSummary getResourceSummary(Job job, String packageNo, String objectCode, 
			String subsidiaryCode, String resourceDescription, String unit, Double rate) throws DatabaseOperationException{
		logger.info("SEARCH: BQRESOURCESUMMARY - Job#"+job.getJobNumber()+" "+
					"Package#:"+packageNo+" "+
					"Object Code:"+objectCode+" "+
					"Subsidiary Code:"+subsidiaryCode+" "+
					"Resource Description:"+resourceDescription+" "+
					"Unit:"+unit+" "+
					"Rate:"+rate);
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("job", job));
			//packageNo
			if(GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.isNull("packageNo"));
			else
				criteria.add(Restrictions.eq("packageNo", packageNo));
			//objectCode
			if(GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.isNull("objectCode"));
			else
				criteria.add(Restrictions.eq("objectCode", objectCode));
			//subsidiaryCode
			if(GenericValidator.isBlankOrNull(subsidiaryCode))
				criteria.add(Restrictions.isNull("subsidiaryCode"));
			else
			criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			//description
			if(GenericValidator.isBlankOrNull(resourceDescription))
				criteria.add(Restrictions.isNull("resourceDescription"));
			else
				criteria.add(Restrictions.eq("resourceDescription", resourceDescription));
			//unit
			if(GenericValidator.isBlankOrNull(unit))
				criteria.add(Restrictions.isNull("unit"));
			else
			criteria.add(Restrictions.or(Restrictions.eq("unit", unit), Restrictions.eq("unit", unit + " ")));
			//rate
			criteria.add(Restrictions.eq("rate", rate));
			return (BQResourceSummary) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean groupResourcesIntoSummaries(Job job) throws DatabaseOperationException{
		logger.info("Grouping Resource into ResourceSummary...");
		try{
			//Check if resoureSummaries already exist
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			if(criteria.list().size() > 0)
				return Boolean.FALSE;
			
			//Non SC resources (object code does not start with 14)
			//Get list of resources
			logger.info("Grouping Non-SC Resource...");
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
			criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.not(Restrictions.like("this.objectCode", "14%")));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.groupProperty("description"), "resourceDescription")
					.add(Projections.groupProperty("unit"), "unit")
					.add(Projections.groupProperty("costRate"), "rate")
					.add(Projections.sum("quantity"), "quantity")
					.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					.add(Projections.sum("ivCumQuantity"), "currIVAmount")); //Put ivCumQty in currIVAmount first, calculate actual amount (* rate) later - allows for margin lines with rate = 0; For Data Conversion
			criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));		
			List<BQResourceSummary> resourceSummaries = (List<BQResourceSummary>)criteria.list();
			logger.info("Number of Non-SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			for(BQResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJob(job);
				//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
				if(resourceSummary.getPackageNo() != null && (resourceSummary.getPackageNo().trim().equals("") || resourceSummary.getPackageNo().trim().equals("0")))
					resourceSummary.setPackageNo(null);
				if(resourceSummary.getSubsidiaryCode().startsWith("9"))
					resourceSummary.setRate(Double.valueOf(1));
				Double ivQty = resourceSummary.getCurrIVAmount();
				if(resourceSummary.getRate() == null)
					resourceSummary.setRate(Double.valueOf(0));
				if(ivQty != null)
					resourceSummary.setCurrIVAmount(ivQty * resourceSummary.getRate());
				else
					resourceSummary.setCurrIVAmount(Double.valueOf(0));
				if(resourceSummary.getResourceDescription() != null)
					resourceSummary.setResourceDescription(resourceSummary.getResourceDescription().trim());
//				logger.info("Created Non-SC BQResourceSummary: "+resourceSummary.getPackageNo()+"-"+resourceSummary.getObjectCode()+"-"+resourceSummary.getSubsidiaryCode()+"-"+resourceSummary.getResourceDescription()+"-"+resourceSummary.getCurrIVAmount());
				this.getSessionFactory().getCurrentSession().saveOrUpdate(resourceSummary);
			}
			
			//SC resources (object code starts with 14)
			//Get list of resources
			logger.info("Grouping SC Resource...");
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
			criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.like("this.objectCode", "14%"));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.quantity) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "quantity")
					.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.ivCumQty) as currIVAmount", new String[]{"currIVAmount"}, new Type[]{DoubleType.INSTANCE}), "currIVAmount"));
			criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));		
			resourceSummaries = (List<BQResourceSummary>)criteria.list();
			logger.info("Number of SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			HashMap<String, String> packageDescriptions = new HashMap<String, String>();
			for(BQResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJob(job);
				resourceSummary.setRate(Double.valueOf(1));
				resourceSummary.setUnit("AM");
				String packageNo = resourceSummary.getPackageNo();
				//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
				if(packageNo != null && (packageNo.trim().equals("") || packageNo.trim().equals("0"))){
					resourceSummary.setPackageNo(null);
					packageNo = null;
				}
				if(packageNo != null){
					if(packageDescriptions.get(packageNo) == null){
						String description = scPackageDao.getPackageDescription(job, packageNo);
						if(description == null)
							description = "Package " + packageNo;
						packageDescriptions.put(packageNo, description);
						resourceSummary.setResourceDescription(description);
					}
					else
						resourceSummary.setResourceDescription(packageDescriptions.get(packageNo));
				}
				else
					resourceSummary.setResourceDescription("Unspecified package");
//				logger.info("Created SC BQResourceSummary: "+resourceSummary.getPackageNo()+"-"+resourceSummary.getObjectCode()+"-"+resourceSummary.getSubsidiaryCode()+"-"+resourceSummary.getResourceDescription()+"-"+resourceSummary.getCurrIVAmount());
				this.getSessionFactory().getCurrentSession().saveOrUpdate(resourceSummary);
			}
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return Boolean.TRUE;
	}

	/**
	 * For Generate Resource Summary for method three.
	 * 
	 * @author peterchan 
	 * Date: Aug 31, 2011
	 * @param job
	 * @return
	 * @throws DatabaseOperationException
	 */
	
	@SuppressWarnings("unchecked")
	public Boolean groupResourcesIntoSummariesMethodThree(Job job) throws DatabaseOperationException{
		logger.info("Grouping Resource into ResourceSummary...");
		try{
			//Check if resoureSummaries already exist
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			if(criteria.list().size() > 0)
				return Boolean.FALSE;
			
			//Non SC resources (object code does not start with 14)
			//Get list of resources
			logger.info("Grouping Non-SC Resource...");
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
			criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.not(Restrictions.like("this.objectCode", "14%")));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.groupProperty("description"), "resourceDescription")
					.add(Projections.groupProperty("unit"), "unit")
					.add(Projections.groupProperty("costRate"), "rate")
					//By Peter Chan 2011-09-20
					.add(Projections.sqlProjection("sum({alias}.quantity *{alias}.remeasuredFactor) as rquantity", new String[]{"rquantity"}, new Type[]{DoubleType.INSTANCE}), "quantity")
//					.add(Projections.sum("quantity"), "quantity")
					.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					.add(Projections.sum("ivCumAmount"), "currIVAmount")); //Put ivCumQty in currIVAmount first, calculate actual amount (* rate) later - allows for margin lines with rate = 0
			criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));		
			List<BQResourceSummary> resourceSummaries = (List<BQResourceSummary>)criteria.list();
			logger.info("Number of Non-SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			for(BQResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJob(job);
				//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
				if(resourceSummary.getPackageNo() != null && (resourceSummary.getPackageNo().trim().equals("") || resourceSummary.getPackageNo().trim().equals("0")))
					resourceSummary.setPackageNo(null);
				if(resourceSummary.getSubsidiaryCode().startsWith("9"))
					resourceSummary.setRate(Double.valueOf(1));
				@SuppressWarnings("unused")
				Double ivQty = resourceSummary.getCurrIVAmount();
				if(resourceSummary.getRate() == null)
					resourceSummary.setRate(Double.valueOf(0));
				if (resourceSummary.getCurrIVAmount() == null)
					resourceSummary.setCurrIVAmount(Double.valueOf(0));
				if(resourceSummary.getResourceDescription() != null)
					resourceSummary.setResourceDescription(resourceSummary.getResourceDescription().trim());
//				logger.info("Created Non-SC BQResourceSummary: "+resourceSummary.getPackageNo()+"-"+resourceSummary.getObjectCode()+"-"+resourceSummary.getSubsidiaryCode()+"-"+resourceSummary.getResourceDescription()+"-"+resourceSummary.getCurrIVAmount());
				this.getSessionFactory().getCurrentSession().saveOrUpdate(resourceSummary);
			}
			
			//SC resources (object code starts with 14)
			//Get list of resources
			logger.info("Grouping SC Resource...");
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
			criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.like("this.objectCode", "14%"));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.quantity *{alias}.remeasuredFactor) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "quantity")
					.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					.add(Projections.sum("ivCumAmount"), "currIVAmount"));
			criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));		
			resourceSummaries = (List<BQResourceSummary>)criteria.list();
			logger.info("Number of SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			HashMap<String, String> packageDescriptions = new HashMap<String, String>();
			for(BQResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJob(job);
				resourceSummary.setRate(Double.valueOf(1));
				resourceSummary.setUnit("AM");
				String packageNo = resourceSummary.getPackageNo();
				//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
				if(packageNo != null && (packageNo.trim().equals("") || packageNo.trim().equals("0"))){
					resourceSummary.setPackageNo(null);
					packageNo = null;
				}
				if(packageNo != null){
					if(packageDescriptions.get(packageNo) == null){
						String description = scPackageDao.getPackageDescription(job, packageNo);
						if(description == null)
							description = "Package " + packageNo;
						packageDescriptions.put(packageNo, description);
						resourceSummary.setResourceDescription(description);
					}
					else
						resourceSummary.setResourceDescription(packageDescriptions.get(packageNo));
				}
				else
					resourceSummary.setResourceDescription("Unspecified package");
//				logger.info("Created SC BQResourceSummary: "+resourceSummary.getPackageNo()+"-"+resourceSummary.getObjectCode()+"-"+resourceSummary.getSubsidiaryCode()+"-"+resourceSummary.getResourceDescription()+"-"+resourceSummary.getCurrIVAmount());
				this.getSessionFactory().getCurrentSession().saveOrUpdate(resourceSummary);
			}
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return Boolean.TRUE;
	}

	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> groupResourcesIntoSummariesForMethodTwo(Job job) throws Exception{
		//Non SC resources (object code does not start with 14)
		//Group by object, subsid, package, description, unit, rate
		//Sum quantity (remeasuredFactor * quantity)
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
		criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.not(Restrictions.like("this.objectCode", "14%")));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"), "objectCode")
				.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.groupProperty("packageNo"), "packageNo")
				.add(Projections.groupProperty("description"), "resourceDescription")
				.add(Projections.groupProperty("unit"), "unit")
				.add(Projections.groupProperty("costRate"), "rate")
				.add(Projections.sqlProjection("sum({alias}.remeasuredFactor * {alias}.quantity) as remeasuredQuant", new String[]{"remeasuredQuant"}, new Type[]{DoubleType.INSTANCE}), "quantity"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));
		List<BQResourceSummary> summaries = criteria.list();
		for(BQResourceSummary summary : summaries){
			summary.setJob(job);
			//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
			if(summary.getPackageNo() != null && (summary.getPackageNo().trim().equals("") || summary.getPackageNo().trim().equals("0")))
				summary.setPackageNo(null);
			if(summary.getSubsidiaryCode().startsWith("9")) // set the rate for margin lines as 1, rather than 0 (in JDE, only the quantity mattered for margin lines)
				summary.setRate(Double.valueOf(1));
			if(summary.getResourceDescription() != null)
				summary.setResourceDescription(summary.getResourceDescription().trim());
		}
		
		//SC resources (object code starts with 14)
		//Group by object, subsid, package
		//Sum amount as quantity, set rate as 1, unit as 'AM', description as package description (if it is found)
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
		criteria.add(Restrictions.eq("jobNumber", job.getJobNumber()));
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.like("this.objectCode", "14%"));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"), "objectCode")
				.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.groupProperty("packageNo"), "packageNo")
				.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.quantity * {alias}.remeasuredFactor) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "quantity"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(BQResourceSummary.class));		
		List<BQResourceSummary> scSummaries = (List<BQResourceSummary>)criteria.list();
		HashMap<String, String> packageDescriptions = new HashMap<String, String>();
		for(BQResourceSummary summary : scSummaries){
			summary.setJob(job);
			summary.setRate(Double.valueOf(1));
			summary.setUnit("AM");
			String packageNo = summary.getPackageNo();
			//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
			if(packageNo != null && (packageNo.trim().equals("") || packageNo.trim().equals("0"))){
				summary.setPackageNo(null);
				packageNo = null;
			}
			if(packageNo != null){
				if(packageDescriptions.get(packageNo) == null){
					String description = scPackageDao.getPackageDescription(job, packageNo);
					if(description == null)
						description = "Package " + packageNo;
					packageDescriptions.put(packageNo, description);
					summary.setResourceDescription(description);
				}
				else
					summary.setResourceDescription(packageDescriptions.get(packageNo));
			}
			else
				summary.setResourceDescription("Unspecified package");
		}
		summaries.addAll(scSummaries);
		return summaries;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package
	 * Add parameter: finalized**/
	@SuppressWarnings("unchecked")
	public List<AccountIVWrapper> obtainIVPostingAmounts(Job job, boolean finalized) throws DatabaseOperationException{
		logger.info("obtainIVPostingAmounts(Job job)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			
			if(!job.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
				detachedCriteria.add(Restrictions.eq("job", job))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));
				
				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("job", job))
									.add(Restrictions.like("objectCode", "14%"))
									.add(Property.forName("packageNo").in(detachedCriteria))
									.setProjection(Projections.property("id"));
				
				if(finalized)
					criteria.add(Property.forName("id").in(detachedCriteria2));
				else
					criteria.add(Property.forName("id").notIn(detachedCriteria2));
			}
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovement", new String[]{"ivMovement"}, new Type[]{DoubleType.INSTANCE}), "ivMovement")
					);
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountIVWrapper.class));
			return (List<AccountIVWrapper>) criteria.list();
		}catch (Exception he){
			throw new DatabaseOperationException(he);
		}
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package (Levy Expenses and Provision)
	 * Add parameter: finalized**/
	public Double obtainLevyAmount(Job job, boolean finalized) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.or(Restrictions.isNull("excludeLevy"), Restrictions.eq("excludeLevy", Boolean.FALSE)));
			
			if(!job.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
				detachedCriteria.add(Restrictions.eq("job", job))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("job", job))
									.add(Restrictions.like("objectCode", "14%"))
									.add(Property.forName("packageNo").in(detachedCriteria))
									.setProjection(Projections.property("id"));
				
				if(finalized)
					criteria.add(Property.forName("id").in(detachedCriteria2));
				else
					criteria.add(Property.forName("id").notIn(detachedCriteria2));
			}			
			criteria.setProjection(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovement", new String[]{"ivMovement"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: obtainIVMovementMargin(Job job)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package (Defect Expenses and Provision)
	 * Add parameter: finalized**/
	public Double obtainDefectAmount(Job job, boolean finalized) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.or(Restrictions.isNull("excludeDefect"), Restrictions.eq("excludeDefect", Boolean.FALSE)));
			
			if(!job.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
				detachedCriteria.add(Restrictions.eq("job", job))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("job", job))
									.add(Restrictions.like("objectCode", "14%"))
									.add(Property.forName("packageNo").in(detachedCriteria))
									.setProjection(Projections.property("id"));
				
				if(finalized)
					criteria.add(Property.forName("id").in(detachedCriteria2));
				else
					criteria.add(Property.forName("id").notIn(detachedCriteria2));
			}
			
			criteria.setProjection(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovement", new String[]{"ivMovement"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: getIVMovementMargin(Job job)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	public Double getIVcpfMovement(Job job, char first, char third, char fourth) throws DatabaseOperationException{
		logger.info("getIVMovementMargin(Job job)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.like("subsidiaryCode", first + "_" + third + fourth + "%"));
			criteria.setProjection(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovement", new String[]{"ivMovement"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: getIVMovementMargin(Job job)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package
	 * Add parameter: finalized**/
	@SuppressWarnings("unchecked")
	public Boolean updateBQResourceSummariesAfterPosting(Job job, String username, boolean finalized) throws DatabaseOperationException{
		logger.info("updateBQResourceSummariesAfterPosting(Job job, String username, boolean finalized)");
		try{
			//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
			detachedCriteria.add(Restrictions.eq("job", job))
							.add(Restrictions.eq("systemStatus", "ACTIVE"))
							.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
							.setProjection(Projections.property("packageNo"));

			DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
			detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
								.add(Restrictions.eq("job", job))
								.add(Restrictions.like("objectCode", "14%"))
								.add(Property.forName("packageNo").in(detachedCriteria))
								.setProjection(Projections.property("id"));
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
			
			if(finalized)
				criteria.add(Property.forName("id").in(detachedCriteria2));
			else
				criteria.add(Property.forName("id").notIn(detachedCriteria2));
			
			List<BQResourceSummary> bqResourceSummaries = (List<BQResourceSummary>)criteria.list();
			List<String> toBeUpdatedPackageNoList = new ArrayList<String>();
			//Update: CurrIVAmount --> PostedIVAmount
			for (BQResourceSummary resourceSummary: bqResourceSummaries){
				resourceSummary.setPostedIVAmount(resourceSummary.getCurrIVAmount());
				this.update(resourceSummary);
				
				if (finalized 
						&& !toBeUpdatedPackageNoList.contains(resourceSummary.getPackageNo())
						&& !BQResourceSummary.POSTED.equals(resourceSummary.getFinalized())){
					
					toBeUpdatedPackageNoList.add(resourceSummary.getPackageNo());
				}
			}
			
			//all resources of the same package will be updated to "Posted"
			for (String packageNo: toBeUpdatedPackageNoList){
				List<BQResourceSummary> resourceList = obtainBQResourceSummariesForFinalIVPosting(job.getJobNumber(), packageNo, "14*", null, null);
				for(BQResourceSummary resource: resourceList){
					resource.setFinalized(BQResourceSummary.POSTED);
					this.update(resource);
				}
			}
			
		}catch (Exception he){
			throw new DatabaseOperationException(he);
		}	
		return Boolean.TRUE;
	}
	
	public Boolean updateBQResourceSummariesAfterPostingForRepackaging3(Job job, String username) throws DatabaseOperationException{
		logger.info("updateBQResourceSummariesAfterPosting(Job job, String username)");
		try{
			String hql = "UPDATE BQResourceSummary SET postedIVAmount = currIVAmount, lastModifiedUser = :user, lastModifiedDate = :date WHERE job = :job AND postedIVAmount != currIVAmount";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setString("user", username);
			query.setDate("date", new Date());
			query.setEntity("job", job);
			query.executeUpdate();
		}catch (Exception he){
			logger.info("Fail: updateBQResourceSummariesAfterPosting(Job job, String username)");
			throw new DatabaseOperationException(he);
		}	
		return Boolean.TRUE;
	}
	
	public Double getTotalAccountAmount(Job job, String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		logger.info("getTotalAmountForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.add(Restrictions.eq("objectCode", objectCode));
			criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			criteria.setProjection(Projections.sqlProjection("sum({alias}.QUANTITY * {alias}.RATE) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: getTotalAmountForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	/*
	 * Get resource summaries for account (job, package, object, subsid codes)
	 * amount = 0 to get all resources
	 * amount > 0 to get resources with positive amount
	 * amount < 0 to get resources with negative amount
	 */
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> getResourceSummariesForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
//		logger.info("SEARCH: J#"+job.getJobNumber()+" Package#"+packageNo+" Object:"+objectCode+" Subsidiary:"+subsidiaryCode);
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			
			//packageNo
			if (GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.isNull("packageNo"));
			else
				criteria.add(Restrictions.eq("packageNo", packageNo));				
			//objectCode
			if(GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.isNull("objectCode"));
			else
				criteria.add(Restrictions.eq("objectCode", objectCode));
			//subsidiaryCode
			if(GenericValidator.isBlankOrNull(subsidiaryCode))
				criteria.add(Restrictions.isNull("subsidiaryCode"));
			else
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			return (List<BQResourceSummary>)criteria.list();
		}catch (Exception he){
			logger.info("Fail: getResourceSummariesForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	public Integer getCountOfSCSplitResources(Job job, String packageNo, String objectCode, String subsidiaryCode) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.isNull("packageNo"));
		criteria.add(Restrictions.eq("objectCode", objectCode));
		criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		criteria.add(Restrictions.eq("unit", "AM"));
		criteria.add(Restrictions.eq("rate", Double.valueOf(1)));
		criteria.add(Restrictions.like("resourceDescription", "Split from SC " + packageNo + "%"));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		if(count == null)
			return Integer.valueOf(0);
		return count;
	}
	
	public Double getBudgetForPackage(Job job, String packageNo) throws DatabaseOperationException{
		logger.info("getBudgetForPackage(Job job, String packageNo)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("packageNo", packageNo));
			criteria.add(Restrictions.like("objectCode", "14%"));
			criteria.setProjection(Projections.sqlProjection("sum({alias}.QUANTITY * {alias}.RATE) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: getBudgetForPackage(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package
	 * Add parameter: finalized**/
	@SuppressWarnings("unchecked")
	public void createIVPostingHistory(Job job, Integer documentNo, Integer ediBatchNo, boolean finalized) throws DatabaseOperationException{
		logger.info("createIVPostingHistory(Job job, String documentNo, String ediBatchNo, boolean finalized)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
			
			if(!job.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
				detachedCriteria.add(Restrictions.eq("job", job))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("job", job))
									.add(Restrictions.like("objectCode", "14%"))
									.add(Property.forName("packageNo").in(detachedCriteria))
									.setProjection(Projections.property("id"));
				
				if(finalized)
					criteria.add(Property.forName("id").in(detachedCriteria2));
				else
					criteria.add(Property.forName("id").notIn(detachedCriteria2));
			}
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.id(), "resourceSummaryId")
					.add(Projections.property("objectCode"), "objectCode")
					.add(Projections.property("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.property("packageNo"), "packageNo")
					.add(Projections.property("resourceDescription"), "resourceDescription")
					.add(Projections.property("unit"), "unit")
					.add(Projections.property("rate"), "rate")
					.add(Projections.sqlProjection("({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovementAmount", new String[]{"ivMovementAmount"}, new Type[]{DoubleType.INSTANCE}), "ivMovementAmount")
					);
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(IVPostingHistory.class));
			
			logger.info("created IV History Posting size: "+criteria.list().size());
			
			List<IVPostingHistory> posts = (List<IVPostingHistory>)criteria.list();
			for(IVPostingHistory post : posts){
				post.setJobNumber(job.getJobNumber());
				post.setDocumentNo(documentNo);
				post.setEdiBatchNo(ediBatchNo);
				this.getSessionFactory().getCurrentSession().saveOrUpdate(post);
			}
		}catch (Exception he){
			logger.info("createIVPostingHistory(Job job, String documentNo, String ediBatchNo, boolean finalized)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	public void resetIVAmountofPackage(Job job, String packageNo) throws DatabaseOperationException {
		String hql = "Update BQResourceSummary set currIVAmount = 0 where systemStatus = 'ACTIVE' and job = :job and packageNo = :packageNo and objectCode like '14%'";
		try {
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setEntity("job", job);
			query.setString("packageNo", packageNo);
			query.executeUpdate();
		} catch (HibernateException hbException) {
			throw new DatabaseOperationException(hbException);
		}
	}
	
	public void saveAudit(AuditResourceSummary audit){
		this.getSessionFactory().getCurrentSession().saveOrUpdate(audit);
	}

	public void setScPackageDao(SCPackageHBDao scPackageDao) {
		this.scPackageDao = scPackageDao;
	}

	public SCPackageHBDao getScPackageDao() {
		return scPackageDao;
	}

	/**
	 * 	
	 * add IV movemnt amount of Resource summary 
	 * @author peterchan
	 * May 11, 2011 3:56:28 PM
	 */
	public Double getIVMovementOfJob(Job job) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
		criteria.setProjection(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovementAmount", new String[]{"ivMovementAmount"}, new Type[]{DoubleType.INSTANCE}));
		Double result=(Double)criteria.uniqueResult();
		if (result==null)
			return new Double(0);
		return  result;
	}

	
	/**
	 * @author koeyyeung
	 * created on 3rd June, 2015
	 */
	public Double obtainIVMovementByJob(Job job, boolean finalized) {
		//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
		detachedCriteria.add(Restrictions.eq("job", job))
						.add(Restrictions.eq("systemStatus", "ACTIVE"))
						.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT))
						.setProjection(Projections.property("packageNo"));

		DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(BQResourceSummary.class);
		detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
							.add(Restrictions.eq("job", job))
							.add(Restrictions.like("objectCode", "14%"))
							.add(Property.forName("packageNo").in(detachedCriteria))
							.setProjection(Projections.property("id"));
		
		
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
		
		if(finalized)
			criteria.add(Property.forName("id").in(detachedCriteria2));
		else
			criteria.add(Property.forName("id").notIn(detachedCriteria2));
		
		criteria.setProjection(Projections.sqlProjection("sum({alias}.ivCumAmount - {alias}.ivPostedAmount) as ivMovementAmount", new String[]{"ivMovementAmount"}, new Type[]{DoubleType.INSTANCE}));
		
		
		Double result=(Double)criteria.uniqueResult();
		if (result==null)
			return new Double(0);
		return  result;
	}
	
	
	/**
	 * @author tikywong
	 * May 24, 2011 11:57:45 AM
	 */
	//Issue with creating duplicated Data in the database
	public void updateBQResourceSummaries(List<BQResourceSummary> bqResourceSummaries) {
		if(bqResourceSummaries==null)
			return;
		
		Session session = this.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		for(int i=0; i<bqResourceSummaries.size(); i++){
			session.update(bqResourceSummaries.get(i));
			
			if(i%20 == 0){
				session.flush();
				session.clear();
			}
		}
		
		tx.commit();
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 14, 2011 3:17:35 PM
	 */
	public boolean updateBQResourceSummaryIVAmountByHQL(BQResourceSummary bqResourceSummary, String username) throws DatabaseOperationException{
//		logger.info("UPDATE: updateBQResourceSummaryIVAmountByHQL(BQResourceSummary bqResourceSummary, String username)");
		try{
			Long bqResourceSummaryID = bqResourceSummary.getId();
			Job job = bqResourceSummary.getJob();
			Double updatedIVCumulativeAmount = bqResourceSummary.getCurrIVAmount();

			String hql = "UPDATE BQResourceSummary SET currIVAmount = :updatedIVCumulativeAmount, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :bqResourceSummaryID AND job = :job";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);

			query.setDouble("updatedIVCumulativeAmount", updatedIVCumulativeAmount);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("bqResourceSummaryID", bqResourceSummaryID);
			query.setEntity("job", job);

			query.executeUpdate();
			return true;
		}catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	/* added by irischau
	 * on 14 Apr 2014
	 * */
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> obtainBQResourceListForIVInput(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {

		List<BQResourceSummary> resourceSummaries;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if (!GenericValidator.isBlankOrNull(objectCode)) {
				objectCode = objectCode.replace("*", "%");
				if (objectCode.contains("%")) {
					criteria.add(Restrictions.like("objectCode", objectCode));
				} else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if (!GenericValidator.isBlankOrNull(subsidiaryCode)) {
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if (subsidiaryCode.contains("%")) {
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				} else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if (!GenericValidator.isBlankOrNull(description)) {
				description = description.replace("*", "%");
				if (description.contains("%")) {
					criteria.add(Restrictions.ilike("resourceDescription", description));
				} else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount()).add(Projections.sum("currIVAmount")).add(Projections.sum("postedIVAmount")));

			resourceSummaries = criteria.list();
			return resourceSummaries;
		} catch (HibernateException ex) {
			logger.info("BQResourceSummary Dao: getResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> obtainBQResourceSummaries(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		List<BQResourceSummary> resourceSummaries;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			if (!GenericValidator.isBlankOrNull(objectCode)) {
				objectCode = objectCode.replace("*", "%");
				if (objectCode.contains("%")) {
					criteria.add(Restrictions.like("objectCode", objectCode));
				} else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			if (!GenericValidator.isBlankOrNull(subsidiaryCode)) {
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if (subsidiaryCode.contains("%")) {
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				} else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if (!GenericValidator.isBlankOrNull(description)) {
				description = description.replace("*", "%");
				if (description.contains("%")) {
					criteria.add(Restrictions.ilike("resourceDescription", description));
				} else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			
			criteria.addOrder(Order.asc("objectCode"))
					.addOrder(Order.asc("subsidiaryCode"))
					.addOrder(Order.asc("packageNo"))
			;
			
			resourceSummaries = criteria.list();
			return resourceSummaries;
		} catch (HibernateException ex) {
			logger.info("BQResourceSummary Dao: obtainBQResourceSummaries - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * @author koeyyeung
	 * created on 29th May, 2015**/
	@SuppressWarnings("unchecked")
	public List<BQResourceSummary> obtainBQResourceSummariesForFinalIVPosting(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		List<BQResourceSummary> resourceSummaries;
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			detachedCriteria.createAlias("job", "job");
			detachedCriteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					detachedCriteria.add(Restrictions.like("packageNo", packageNo));
				} else
					detachedCriteria.add(Restrictions.eq("packageNo", packageNo));
			}
			detachedCriteria.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT));
			detachedCriteria.setProjection(Projections.property("packageNo"));
			
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			
			//Only object code starts with '14' can be obtained for subcontract package with status 'Final'
			if (!GenericValidator.isBlankOrNull(objectCode)) {
				objectCode = objectCode.replace("*", "%");
				if (objectCode.contains("%")) {
					criteria.add(Restrictions.like("objectCode", objectCode));
				} else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			
			if (!GenericValidator.isBlankOrNull(subsidiaryCode)) {
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if (subsidiaryCode.contains("%")) {
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				} else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			
			if (!GenericValidator.isBlankOrNull(description)) {
				description = description.replace("*", "%");
				if (description.contains("%")) {
					criteria.add(Restrictions.ilike("resourceDescription", description));
				} else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			
			criteria.add(Property.forName("packageNo").in(detachedCriteria));
			
			criteria.addOrder(Order.asc("objectCode"))
					.addOrder(Order.asc("subsidiaryCode"))
					.addOrder(Order.asc("packageNo"))
			;
		
			resourceSummaries = criteria.list();
			return resourceSummaries;
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	
	/**
	 * @author koeyyeung
	 * created on 10th Aug, 2015**/
	@SuppressWarnings("unchecked")
	public List<String> obtainPackageNoForIVRecalculation(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SCPackage.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			detachedCriteria.createAlias("job", "job");
			detachedCriteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					detachedCriteria.add(Restrictions.like("packageNo", packageNo));
				} else
					detachedCriteria.add(Restrictions.eq("packageNo", packageNo));
			}
			detachedCriteria.add(Restrictions.eq("paymentStatus", SCPackage.FINAL_PAYMENT));
			detachedCriteria.setProjection(Projections.property("packageNo"));
			
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					criteria.add(Restrictions.like("packageNo", packageNo));
				} else
					criteria.add(Restrictions.eq("packageNo", packageNo));
			}
			
			//Only object code starts with '14' can be obtained for subcontract package with status 'Final'
			if (!GenericValidator.isBlankOrNull(objectCode)) {
				objectCode = objectCode.replace("*", "%");
				if (objectCode.contains("%")) {
					criteria.add(Restrictions.like("objectCode", objectCode));
				} else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			
			if (!GenericValidator.isBlankOrNull(subsidiaryCode)) {
				subsidiaryCode = subsidiaryCode.replace("*", "%");
				if (subsidiaryCode.contains("%")) {
					criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
				} else
					criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			
			if (!GenericValidator.isBlankOrNull(description)) {
				description = description.replace("*", "%");
				if (description.contains("%")) {
					criteria.add(Restrictions.ilike("resourceDescription", description));
				} else
					criteria.add(Restrictions.ilike("resourceDescription", description, MatchMode.ANYWHERE));
			}
			
			criteria.add(Restrictions.eq("finalized", BQResourceSummary.NOT_FINALIZED));
			
			criteria.add(Property.forName("packageNo").in(detachedCriteria));
			
			criteria.setProjection(Projections.distinct(Projections.property("packageNo")));
		
			return criteria.list();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	public BQResourceSummary obtainResourceSummary(Job job, String packageNo, String objectCode, String subsidiaryCode, 
													String resourceDescription, String unit, Double rate) throws DatabaseOperationException{
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("job", job));
			//packageNo
			if(!GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.eq("packageNo", packageNo));
			//objectCode
			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("objectCode", objectCode));
			//subsidiaryCode
			if(!GenericValidator.isBlankOrNull(subsidiaryCode))
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			//description
			if(!GenericValidator.isBlankOrNull(resourceDescription))
				criteria.add(Restrictions.eq("resourceDescription", resourceDescription));
			//unit
			if(!GenericValidator.isBlankOrNull(unit))
				criteria.add(Restrictions.eq("unit", unit));
			//rate
			if(rate!=null)
				criteria.add(Restrictions.eq("rate", rate));
			
			return (BQResourceSummary) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
}
