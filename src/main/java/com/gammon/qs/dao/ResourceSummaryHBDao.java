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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.IVPostingHist;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.ResourceSummaryAuditCustom;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.ivPosting.AccountIVWrapper;
@Repository
public class ResourceSummaryHBDao extends BaseHibernateDao<ResourceSummary> {
	
	private static final int RECORDS_PER_PAGE = 400;
	
	public ResourceSummaryHBDao(){
		super(ResourceSummary.class);
	}
	
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private SubcontractHBDao scPackageDao;
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> obtainSCResourceSummariesByJobNumber(String jobNumber) throws DatabaseOperationException{
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.like("objectCode", "14%"));
			criteria.add(Restrictions.ne("packageNo", "0"));
			
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("ResourceSummary Dao: obtainSCResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> obtainResourceSummariesByJobNumberForAdmin(String jobNumber){
		List<ResourceSummary> resourceSummaries = null;
		try{
			Criteria criteria = getSession().createCriteria(getType());
			criteria.createAlias("jobInfo",  "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			resourceSummaries = criteria.list();
		} catch(HibernateException e){
			e.printStackTrace();
		}
		return resourceSummaries;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummariesByJobNumber(String jobNumber) throws DatabaseOperationException{
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("ResourceSummary Dao: getResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummariesByJob(JobInfo jobInfo) throws DatabaseOperationException{
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			logger.info("ResourceSummary Dao: getResourceSummariesByJob - " + jobInfo.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummariesSearch(JobInfo jobInfo, 
			String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		//logger.info("ResourceSummary Dao: getResourceSummariesSearch - " + jobInfo.getJobNumber());
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public RepackagingPaginationWrapper<ResourceSummary> obtainResourceSummariesSearchByPage(JobInfo jobInfo, 
			String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws DatabaseOperationException{
		//logger.info("ResourceSummary Dao: getResourceSummariesSearchByPage - " + jobInfo.getJobNumber());
		
		try{
			RepackagingPaginationWrapper<ResourceSummary> paginationWrapper = new RepackagingPaginationWrapper<ResourceSummary>();
			paginationWrapper.setCurrentPage(pageNum);
			//Get total number of records (pages)
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
				paginationWrapper.setCurrentPageContentList(new ArrayList<ResourceSummary>());
				return paginationWrapper;
			}
			
			//Get 1 page of records
			String hql = "from ResourceSummary where job = :job and systemStatus = 'ACTIVE'"; 
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
			Query query = getSession().createQuery(hql);
			query.setEntity("job", jobInfo);
			query.setFirstResult(pageNum * RECORDS_PER_PAGE);
			query.setMaxResults(RECORDS_PER_PAGE);
			
			List<ResourceSummary> resultList = query.list();
			paginationWrapper.setCurrentPageContentList(resultList==null? new ArrayList<ResourceSummary>():resultList);
			
			return paginationWrapper;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public Double getMarkupAmountInSearch(JobInfo jobInfo, String packageNo, String objectCode, 
			String subsidiaryCode, String description) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(JobInfo jobInfo, 
			String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum) throws DatabaseOperationException{
		try{
			IVInputPaginationWrapper paginationWrapper = new IVInputPaginationWrapper();
			paginationWrapper.setCurrentPage(pageNum);
			//Get total number of records (pages)
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
				paginationWrapper.setCurrentPageContentList(new ArrayList<ResourceSummary>());
				return paginationWrapper;
			}
			
			//Get 1 page of records
			String hql = "from ResourceSummary where job = :job and systemStatus = 'ACTIVE'"; 
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
			Query query = getSession().createQuery(hql);
			query.setEntity("jobInfo", jobInfo);
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
	public ResourceSummary getResourceSummary(JobInfo jobInfo, String packageNo, String objectCode, 
			String subsidiaryCode, String resourceDescription, String unit, Double rate, Double quantity) throws DatabaseOperationException{
		/*logger.info("SEARCH: BQRESOURCESUMMARY - Job#"+jobInfo.getJobNumber()+" "+
					"Package#:"+packageNo+" "+
					"Object Code:"+objectCode+" "+
					"Subsidiary Code:"+subsidiaryCode+" "+
					"Resource Description:"+resourceDescription+" "+
					"Unit:"+unit+" "+
					"Rate:"+rate);*/
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
			
			//quantity
			if(quantity!=null)
				criteria.add(Restrictions.eq("quantity", quantity));
			return (ResourceSummary) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean groupResourcesIntoSummaries(JobInfo jobInfo) throws DatabaseOperationException{
		logger.info("Grouping Resource into ResourceSummary...");
		try{
			//Check if resoureSummaries already exist
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			if(criteria.list().size() > 0)
				return Boolean.FALSE;
			
			//Non SC resources (object code does not start with 14)
			//Get list of resources
			logger.info("Grouping Non-SC Resource...");
			criteria = getSession().createCriteria(BpiItemResource.class);
			criteria.add(Restrictions.eq("jobNumber", jobInfo.getJobNumber()));
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
					.add(Projections.sum("amountBudget"), "amountBudget"));
					//.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					//.add(Projections.sum("ivCumQuantity"), "currIVAmount")); //Put ivCumQty in currIVAmount first, calculate actual amount (* rate) later - allows for margin lines with rate = 0; For Data Conversion
			criteria.setResultTransformer(new AliasToBeanResultTransformer(ResourceSummary.class));		
			List<ResourceSummary> resourceSummaries = (List<ResourceSummary>)criteria.list();
			logger.info("Number of Non-SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			for(ResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJobInfo(jobInfo);
				//Set PackageNo = null if the packageNo@Resource = "" or 0 (packageNo 1-999 from Legacy system will not be affected)
				if(resourceSummary.getPackageNo() != null && (resourceSummary.getPackageNo().trim().equals("") || resourceSummary.getPackageNo().trim().equals("0")))
					resourceSummary.setPackageNo(null);
				if(resourceSummary.getSubsidiaryCode().startsWith("9")){
					resourceSummary.setRate(Double.valueOf(1));
					resourceSummary.setAmountBudget(resourceSummary.getQuantity());
				}
					
				if(resourceSummary.getRate() == null)
					resourceSummary.setRate(Double.valueOf(0));
				/*Double ivQty = resourceSummary.getCurrIVAmount();
				if(ivQty != null)
					resourceSummary.setCurrIVAmount(ivQty * resourceSummary.getRate());
				else
					resourceSummary.setCurrIVAmount(Double.valueOf(0));*/
				if(resourceSummary.getResourceDescription() != null)
					resourceSummary.setResourceDescription(resourceSummary.getResourceDescription().trim());
				if(resourceSummary.getAmountBudget() == null){
					resourceSummary.setAmountBudget(0.0);
				}
				getSession().saveOrUpdate(resourceSummary);
			}
			
			//SC resources (object code starts with 14)
			//Get list of resources
			logger.info("Grouping SC Resource...");
			criteria = getSession().createCriteria(BpiItemResource.class);
			criteria.add(Restrictions.eq("jobNumber", jobInfo.getJobNumber()));
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.like("this.objectCode", "14%"));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("objectCode"), "objectCode")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
					.add(Projections.groupProperty("packageNo"), "packageNo")
					.add(Projections.sum("amountBudget"), "quantity")
					.add(Projections.sum("amountBudget"), "amountBudget"));
					//.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.quantity) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "quantity")
					//.add(Projections.sum("ivPostedAmount"), "postedIVAmount")
					//.add(Projections.sqlProjection("sum({alias}.costRate * {alias}.ivCumQty) as currIVAmount", new String[]{"currIVAmount"}, new Type[]{DoubleType.INSTANCE}), "currIVAmount"));
			criteria.setResultTransformer(new AliasToBeanResultTransformer(ResourceSummary.class));		
			resourceSummaries = (List<ResourceSummary>)criteria.list();
			logger.info("Number of SC ResourceSummaries: "+resourceSummaries.size());
			//Save Resource Summaries
			HashMap<String, String> packageDescriptions = new HashMap<String, String>();
			for(ResourceSummary resourceSummary : resourceSummaries){
				resourceSummary.setJobInfo(jobInfo);
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
						String description = scPackageDao.getPackageDescription(jobInfo, packageNo);
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
				
				getSession().saveOrUpdate(resourceSummary);
			}
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return Boolean.TRUE;
	}

	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for finalized package
	 * Add parameter: finalized**/
	@SuppressWarnings("unchecked")
	public List<AccountIVWrapper> obtainIVPostingAmounts(JobInfo jobInfo, boolean finalized) throws DatabaseOperationException{
		logger.info("obtainIVPostingAmounts(Job job)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			
			if(!jobInfo.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
				detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));
				
				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("jobInfo", jobInfo))
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
	public Double obtainLevyAmount(JobInfo jobInfo, boolean finalized) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.or(Restrictions.isNull("excludeLevy"), Restrictions.eq("excludeLevy", Boolean.FALSE)));
			
			if(!jobInfo.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
				detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("jobInfo", jobInfo))
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
	public Double obtainDefectAmount(JobInfo jobInfo, boolean finalized) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.or(Restrictions.isNull("excludeDefect"), Restrictions.eq("excludeDefect", Boolean.FALSE)));
			
			if(!jobInfo.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
				detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("jobInfo", jobInfo))
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
	
	public Double getIVcpfMovement(JobInfo jobInfo, char first, char third, char fourth) throws DatabaseOperationException{
		logger.info("getIVMovementMargin(Job job)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public Boolean updateBQResourceSummariesAfterPosting(JobInfo jobInfo, String username, boolean finalized) throws DatabaseOperationException{
		logger.info("updateBQResourceSummariesAfterPosting(Job job, String username, boolean finalized)");
		try{
			//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
			detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
							.add(Restrictions.eq("systemStatus", "ACTIVE"))
							.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
							.setProjection(Projections.property("packageNo"));

			DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
			detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
								.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.like("objectCode", "14%"))
								.add(Property.forName("packageNo").in(detachedCriteria))
								.setProjection(Projections.property("id"));
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
			
			if(finalized)
				criteria.add(Property.forName("id").in(detachedCriteria2));
			else
				criteria.add(Property.forName("id").notIn(detachedCriteria2));
			
			List<ResourceSummary> bqResourceSummaries = (List<ResourceSummary>)criteria.list();
			List<String> toBeUpdatedPackageNoList = new ArrayList<String>();
			//Update: CurrIVAmount --> PostedIVAmount
			for (ResourceSummary resourceSummary: bqResourceSummaries){
				resourceSummary.setPostedIVAmount(resourceSummary.getCurrIVAmount());
				this.update(resourceSummary);
				
				if (finalized 
						&& !toBeUpdatedPackageNoList.contains(resourceSummary.getPackageNo())
						&& !ResourceSummary.POSTED.equals(resourceSummary.getFinalized())){
					
					toBeUpdatedPackageNoList.add(resourceSummary.getPackageNo());
				}
			}
			
			//all resources of the same package will be updated to "Posted"
			for (String packageNo: toBeUpdatedPackageNoList){
				List<ResourceSummary> resourceList = obtainBQResourceSummariesForFinalIVPosting(jobInfo.getJobNumber(), packageNo, "14*", null, null);
				for(ResourceSummary resource: resourceList){
					resource.setFinalized(ResourceSummary.POSTED);
					this.update(resource);
				}
			}
			
		}catch (Exception he){
			throw new DatabaseOperationException(he);
		}	
		return Boolean.TRUE;
	}
	
	public Boolean updateBQResourceSummariesAfterPostingForRepackaging3(JobInfo jobInfo, String username) throws DatabaseOperationException{
		logger.info("updateBQResourceSummariesAfterPosting(Job job, String username)");
		try{
			String hql = "UPDATE ResourceSummary SET postedIVAmount = currIVAmount, lastModifiedUser = :user, lastModifiedDate = :date WHERE job = :job AND postedIVAmount != currIVAmount";
			Query query = getSession().createQuery(hql);
			query.setString("user", username);
			query.setDate("date", new Date());
			query.setEntity("jobInfo", jobInfo);
			query.executeUpdate();
		}catch (Exception he){
			logger.info("Fail: updateBQResourceSummariesAfterPosting(Job job, String username)");
			throw new DatabaseOperationException(he);
		}	
		return Boolean.TRUE;
	}
	
	public Double getTotalAccountAmount(JobInfo jobInfo, String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		logger.info("getTotalAmountForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public List<ResourceSummary> getResourceSummariesForAccount(JobInfo jobInfo, String packageNo, String objectCode, String subsidiaryCode) throws DatabaseOperationException{
//		logger.info("SEARCH: J#"+job.getJobNumber()+" Package#"+packageNo+" Object:"+objectCode+" Subsidiary:"+subsidiaryCode);
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			
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
			return (List<ResourceSummary>)criteria.list();
		}catch (Exception he){
			logger.info("Fail: getResourceSummariesForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	public Integer getCountOfSCSplitResources(JobInfo jobInfo, String packageNo, String objectCode, String subsidiaryCode) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	
	public Double getBudgetForPackage(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		logger.info("getBudgetForPackage(Job job, String packageNo)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public void createIVPostingHistory(JobInfo jobInfo, Integer documentNo, Integer ediBatchNo, boolean finalized) throws DatabaseOperationException{
		logger.info("createIVPostingHistory(Job job, String documentNo, String ediBatchNo, boolean finalized)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.neProperty("currIVAmount", "postedIVAmount"));
			
			if(!jobInfo.getRepackagingType().equals("3")){
				//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
				detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
								.add(Restrictions.eq("systemStatus", "ACTIVE"))
								.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
								.setProjection(Projections.property("packageNo"));

				DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
				detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
									.add(Restrictions.eq("jobInfo", jobInfo))
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
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(IVPostingHist.class));
			
			logger.info("created IV History Posting size: "+criteria.list().size());
			
			List<IVPostingHist> posts = (List<IVPostingHist>)criteria.list();
			for(IVPostingHist post : posts){
				post.setJobNumber(jobInfo.getJobNumber());
				post.setDocumentNo(documentNo);
				post.setEdiBatchNo(ediBatchNo);
				getSession().saveOrUpdate(post);
			}
		}catch (Exception he){
			logger.info("createIVPostingHistory(Job job, String documentNo, String ediBatchNo, boolean finalized)");
			throw new DatabaseOperationException(he);
		}	
	}
	
	public void resetIVAmountofPackage(JobInfo jobInfo, String packageNo) throws DatabaseOperationException {
		String hql = "Update ResourceSummary set currIVAmount = 0 where systemStatus = 'ACTIVE' and jobInfo = :jobInfo and packageNo = :packageNo and objectCode like '14%'";
		try {
			Query query = getSession().createQuery(hql);
			query.setEntity("jobInfo", jobInfo);
			query.setString("packageNo", packageNo);
			query.executeUpdate();
		} catch (HibernateException hbException) {
			throw new DatabaseOperationException(hbException);
		}
	}

	public void saveAudit(ResourceSummaryAuditCustom audit){
		getSession().saveOrUpdate(audit);
	}

	public void setScPackageDao(SubcontractHBDao scPackageDao) {
		this.scPackageDao = scPackageDao;
	}

	public SubcontractHBDao getScPackageDao() {
		return scPackageDao;
	}

	/**
	 * 	
	 * add IV movemnt amount of Resource summary 
	 * @author peterchan
	 * May 11, 2011 3:56:28 PM
	 */
	public Double getIVMovementOfJob(JobInfo jobInfo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public Double obtainIVMovementByJob(JobInfo jobInfo, boolean finalized) {
		//Subquery: obtain resources with scPackage status "Final" and object code starts with "14" 
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
		detachedCriteria.add(Restrictions.eq("jobInfo", jobInfo))
						.add(Restrictions.eq("systemStatus", "ACTIVE"))
						.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT))
						.setProjection(Projections.property("packageNo"));

		DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(ResourceSummary.class);
		detachedCriteria2.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
							.add(Restrictions.eq("jobInfo", jobInfo))
							.add(Restrictions.like("objectCode", "14%"))
							.add(Property.forName("packageNo").in(detachedCriteria))
							.setProjection(Projections.property("id"));
		
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
	public void updateBQResourceSummaries(List<ResourceSummary> bqResourceSummaries) {
		if(bqResourceSummaries==null)
			return;
		
		Session session = getSession();
//		Transaction tx = session.beginTransaction();
		
		for(int i=0; i<bqResourceSummaries.size(); i++){
			session.update(bqResourceSummaries.get(i));
			
			if(i%20 == 0){
				session.flush();
				session.clear();
			}
		}
		
//		tx.commit();
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 14, 2011 3:17:35 PM
	 */
	public boolean updateBQResourceSummaryIVAmountByHQL(ResourceSummary bqResourceSummary, String username) throws DatabaseOperationException{
//		logger.info("UPDATE: updateBQResourceSummaryIVAmountByHQL(ResourceSummary bqResourceSummary, String username)");
		try{
			Long bqResourceSummaryID = bqResourceSummary.getId();
			JobInfo jobInfo = bqResourceSummary.getJobInfo();
			Double updatedIVCumulativeAmount = bqResourceSummary.getCurrIVAmount();

			String hql = "UPDATE ResourceSummary SET currIVAmount = :updatedIVCumulativeAmount, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :bqResourceSummaryID AND job = :job";
			Query query = getSession().createQuery(hql);

			query.setDouble("updatedIVCumulativeAmount", updatedIVCumulativeAmount);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("bqResourceSummaryID", bqResourceSummaryID);
			query.setEntity("jobInfo", jobInfo);

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
	public List<ResourceSummary> obtainBQResourceListForIVInput(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {

		List<ResourceSummary> resourceSummaries;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			logger.info("ResourceSummary Dao: getResourceSummariesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> obtainBQResourceSummaries(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		List<ResourceSummary> resourceSummaries;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			logger.info("ResourceSummary Dao: obtainBQResourceSummaries - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * @author koeyyeung
	 * created on 29th May, 2015**/
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> obtainBQResourceSummariesForFinalIVPosting(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		List<ResourceSummary> resourceSummaries;
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			detachedCriteria.createAlias("jobInfo", "jobInfo");
			detachedCriteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					detachedCriteria.add(Restrictions.like("packageNo", packageNo));
				} else
					detachedCriteria.add(Restrictions.eq("packageNo", packageNo));
			}
			detachedCriteria.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT));
			detachedCriteria.setProjection(Projections.property("packageNo"));
			
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subcontract.class);
			detachedCriteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			detachedCriteria.createAlias("jobInfo", "jobInfo");
			detachedCriteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(packageNo)) {
				packageNo = packageNo.replace("*", "%");
				if (packageNo.contains("%")) {
					detachedCriteria.add(Restrictions.like("packageNo", packageNo));
				} else
					detachedCriteria.add(Restrictions.eq("packageNo", packageNo));
			}
			detachedCriteria.add(Restrictions.eq("paymentStatus", Subcontract.FINAL_PAYMENT));
			detachedCriteria.setProjection(Projections.property("packageNo"));
			
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
			
			criteria.add(Restrictions.eq("finalized", ResourceSummary.NOT_FINALIZED));
			
			criteria.add(Property.forName("packageNo").in(detachedCriteria));
			
			criteria.setProjection(Projections.distinct(Projections.property("packageNo")));
		
			return criteria.list();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	public ResourceSummary obtainResourceSummary(JobInfo jobInfo, String packageNo, String objectCode, String subsidiaryCode, 
													String resourceDescription, String unit, Double rate) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
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
			
			return (ResourceSummary) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountCodeWrapper> getAccountCodeListByJob(String jobNumber) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"),"objectAccount")
				.add(Projections.groupProperty("subsidiaryCode"),"subsidiary"));
		criteria.addOrder(Order.asc("subsidiaryCode")).addOrder(Order.asc("objectCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountCodeWrapper.class));
		return criteria.list();
	}
	
	
	/**************************************************** FUNCTIONS FOR PCMS **************************************************************/
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummaries(JobInfo jobInfo, String packageNo, String objectCode) throws DatabaseOperationException{
		//logger.info("ResourceSummary Dao: getResourceSummariesSearch - " + jobInfo.getJobNumber());
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if(!GenericValidator.isBlankOrNull(packageNo)){
				criteria.add(Restrictions.or(Restrictions.eq("packageNo", packageNo), Restrictions.isNull("packageNo")));
			}
			if(!GenericValidator.isBlankOrNull(objectCode)){
				if(objectCode.contains("*")){
					criteria.add(Restrictions.like("objectCode", objectCode.replace("*", "%")));
				}
				else
					criteria.add(Restrictions.eq("objectCode", objectCode));
			}
			
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("packageNo"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("resourceDescription"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummariesForAddendum(String jobNo) throws DataAccessException{
		List<ResourceSummary> resourceSummaries = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.or(Restrictions.eq("packageNo", "0"), Restrictions.isNull("packageNo")));
			criteria.add(Restrictions.like("objectCode", "14%"));
			criteria.add(Restrictions.eq("postedIVAmount", 0.0));
			
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			resourceSummaries = criteria.list();
			return resourceSummaries;
		}
		catch(HibernateException ex){
			ex.printStackTrace();
		}
		return resourceSummaries;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummary> getResourceSummariesBySC(JobInfo jobInfo, String packageNo) throws DatabaseOperationException{
		logger.info("ResourceSummary Dao: getResourceSummariesSearch - " + jobInfo.getJobNumber());
		List<ResourceSummary> resourceSummaries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			if(!GenericValidator.isBlankOrNull(packageNo)){
				criteria.add(Restrictions.or(Restrictions.eq("packageNo", packageNo)));
			}
			criteria.add(Restrictions.like("objectCode", "14%"));
			
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
	public IVInputPaginationWrapper obtainResourceSummariesForIV(JobInfo job) throws DatabaseOperationException{
		try{
			IVInputPaginationWrapper paginationWrapper = new IVInputPaginationWrapper();

			//Get total number of records (pages)
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", job));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount())
					.add(Projections.sum("currIVAmount"))
					.add(Projections.sum("postedIVAmount")));
			Object[] rowTotals = (Object[])criteria.uniqueResult();
			Integer rows = rowTotals[0] == null ? Integer.valueOf(0) : ((Long)rowTotals[0]).intValue();
			Double ivCumTotal = rowTotals[1] == null ? Double.valueOf(0) : (Double)rowTotals[1];
			Double ivPostedTotal = rowTotals[2] == null ? Double.valueOf(0) : (Double)rowTotals[2];
			
			logger.info("rows ; " + rows +" ivCumTotal : " + ivCumTotal +" ivPostedTotal : " +ivPostedTotal );
			paginationWrapper.setTotalRecords(rows);
			paginationWrapper.setIvCumTotal(ivCumTotal);
			paginationWrapper.setIvMovementTotal(ivCumTotal - ivPostedTotal);
			
			/*if(rows.equals(Integer.valueOf(0))){
				paginationWrapper.setCurrentPageContentList(new ArrayList<ResourceSummary>());
				return paginationWrapper;
			}*/
			
			String hql = "from ResourceSummary where job_info_id = :job and systemStatus = 'ACTIVE'"; 
			hql += " order by substr(objectCode, 1, 2) asc, packageNo asc, objectCode asc, subsidiaryCode asc, resourceDescription asc, unit asc, rate asc";
			Query query = getSession().createQuery(hql);
			query.setEntity("job", job);
			paginationWrapper.setCurrentPageContentList(query.list());
			return paginationWrapper;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceSummayDashboardDTO> getResourceSummariesGroupByObjectCode(String jobNo) {
		String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
						
	
		List<ResourceSummayDashboardDTO> rsList = new ArrayList<ResourceSummayDashboardDTO>();
		String hql =
				"select objectCode, Sum(Amt_Budget) As amountBudget from "
				+"(select Substr (Objectcode, 1, 2) As objectCode, Amt_Budget" 
				+ " from "+schema+".RESOURCE_SUMMARY where job_info_id = (select id from "+schema+".JOB_INFO where jobNo = '"+jobNo+"') and system_Status = 'ACTIVE')"
				+" Group By Objectcode order by Objectcode";
		
		//Object Code: 11,12,13,14,15,19
		
		SQLQuery query = getSession().createSQLQuery(hql)
									.addScalar("objectCode", StringType.INSTANCE)
									.addScalar("amountBudget", DoubleType.INSTANCE);
		
		rsList = query.setResultTransformer(new AliasToBeanResultTransformer(ResourceSummayDashboardDTO.class)).list();
		
		return rsList;
	}
	/**************************************************** FUNCTIONS FOR PCMS - END **************************************************************/	
}
