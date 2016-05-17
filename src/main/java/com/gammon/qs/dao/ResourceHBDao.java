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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Page;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.BudgetPostingWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQItemGroupedByIDWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQResourceSummaryGroupedBySCWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;

@Repository
public class ResourceHBDao extends BaseHibernateDao<Resource> {
	
	public ResourceHBDao() {
		super(Resource.class);
	}

	private Logger logger = Logger.getLogger(ResourceHBDao.class.getName());
	@Autowired
	private BillHBDao billHBDao;
	@Autowired
	private PageHBDao pageHBDao;
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	
	//Variable names in Resource.java (Example: @Resource.java-refBillNo @Resource Table-BILLNOREF)
	private static final String JOB_NUMBER = "jobNumber";
	private static final String BQITEM = "bqItem";
	
	private static final String RESOURCE_NUMBER = "resourceNo";
	private static final String SUBSIDIARY_CODE = "subsidiaryCode";
	private static final String OBJECT_CODE = "objectCode";
	
	private static final String DESCRIPTION = "description";
	private static final String QUANTITY = "quantity";
	private static final String UNIT = "unit";
	private static final String COST_RATE = "costRate";
	private static final String PACKAGE_NO = "packageNo";
	private static final String PACKAGE_TYPE = "packageType";
	
	private static final String REF_BILL_NO = "refBillNo";
	private static final String REF_SUB_BILL_NO = "refSubBillNo";
	private static final String REF_SECTION_NO = "refSectionNo";
	private static final String REF_PAGE_NO = "refPageNo";
	private static final String REF_ITEM_NO = "refItemNo";
	
	private static final String SYSTEM_STATUS = "systemStatus";

	@SuppressWarnings("unused")
	private static final String RESOURCE_TYPE = "resourceType";
	@SuppressWarnings("unused")
	private static final String REMEASURED_FACTOR = "remeasuredFactor";
	@SuppressWarnings("unused")
	private static final String MATERIAL_WASTAGE = "materialWastage";
	@SuppressWarnings("unused")
	private static final String PACKAGE_NATURE = "packageNature";
	@SuppressWarnings("unused")
	private static final String SPLIT_STATUS = "splitStatus";
	@SuppressWarnings("unused")
	private static final String IV_POSTED_QUANTITY = "ivPostedQuantity";
	@SuppressWarnings("unused")
	private static final String IV_CUM_QUANTITY = "ivCumQuantity";
	@SuppressWarnings("unused")
	private static final String IV_POSTED_AMOUNT = "ivPostedAmount";
	private static final String IV_CUM_AMOUNT = "ivCumAmount";
	@SuppressWarnings("unused")
	private static final String IV_MOVEMENT_AMOUNT = "ivMovementAmount";

	public BillHBDao getBillHBDao() {
		return billHBDao;
	}


	public void setBillHBDao(BillHBDao billHBDao) {
		this.billHBDao = billHBDao;
	}


	public PageHBDao getPageHBDao() {
		return pageHBDao;
	}


	public void setPageHBDao(PageHBDao pageHBDao) {
		this.pageHBDao = pageHBDao;
	}
	
	public Resource getResourceWithBQItem(Long id) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", id));
		Resource resource = (Resource) criteria.uniqueResult();
		Hibernate.initialize(resource.getBqItem());
		return resource;
	}
	
	public Integer getNextResourceNoForBQItem(BQItem bqItem) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq(BQITEM, bqItem));
		criteria.setProjection(Projections.max(RESOURCE_NUMBER));
		Integer maxResourceNo = (Integer) criteria.uniqueResult();
		if(maxResourceNo == null)
			return Integer.valueOf(1);
		return maxResourceNo + 1;
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getBQResourceListByBPI(String jobNumber,
			String billNo, String subBillNo, String sectionNo, String pageNo,
			String itemNo) throws Exception {
		
		
			try{
				Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
				criteria.createAlias("bill.job", "job");
				if (jobNumber!=null &&  jobNumber.length()>0)
					criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
				else
					throw new DatabaseOperationException("Job Number cannot be null");
				criteria.createAlias("bqItem.page", "page");
				if (pageNo==null || pageNo.trim().length()<1)
					criteria.add(Restrictions.isNull("page.pageNo"));
				else
					criteria.add(Restrictions.eq("page.pageNo", pageNo));
				
				if (billNo==null||billNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bill.billNo"));
				else
					criteria.add(Restrictions.eq("bill.billNo", billNo));
				criteria.createAlias("page.bill", "bill");
				
				if (subBillNo==null||subBillNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bill.subBillNo"));
				else 
					criteria.add(Restrictions.eq("bill.subBillNo",subBillNo));
				if (sectionNo==null||sectionNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bill.sectionNo"));
				else
					criteria.add(Restrictions.eq("bill.sectionNo",sectionNo));
				criteria.createAlias(BQITEM, BQITEM);
				criteria.add(Restrictions.eq("bqItem.itemNo", itemNo.trim()));
				criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));

				return (List<Resource>) criteria.list();  
			}catch (HibernateException he){
				throw new DatabaseOperationException(he);
			}
	}

	public Resource getResource(Resource resource) throws DatabaseOperationException{
		if (resource==null){
			logger.info("Resource is null");
			throw new NullPointerException();
		}
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(RESOURCE_NUMBER, resource.getResourceNo()));
			criteria.createAlias(BQITEM, BQITEM);
			if (resource.getBqItem().getItemNo()==null || resource.getBqItem().getItemNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bqItem.itemNo"));
			else
				criteria.add(Restrictions.eq("bqItem.itemNo", resource.getBqItem().getItemNo()));
			criteria.createAlias("bqItem.page", "page");
			if (resource.getBqItem().getPage().getPageNo()==null||resource.getBqItem().getPage().getPageNo().trim().length()<1)
				criteria.add(Restrictions.isNull("page.pageNo"));
			else
				criteria.add(Restrictions.eq("page.pageNo", resource.getBqItem().getPage().getPageNo().trim()));
			criteria.createAlias("page.bill", "bill");
			criteria.add(Restrictions.eq("bill.billNo", resource.getBqItem().getPage().getBill().getBillNo()));
			if (resource.getBqItem().getPage().getBill().getSubBillNo()==null||resource.getBqItem().getPage().getBill().getSubBillNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bill.subBillNo", resource.getBqItem().getPage().getBill().getSubBillNo().trim()));
			if (resource.getBqItem().getPage().getBill().getSectionNo()==null||resource.getBqItem().getPage().getBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bill.sectionNo", resource.getBqItem().getPage().getBill().getSectionNo().trim()));
			return (Resource) criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getResource(Resource resource)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveResource(Resource resource) throws DatabaseOperationException{
		if (resource==null)
			throw new NullPointerException("Resource is null");
		try {
			if (getResource(resource)==null){
				resource.setCreatedDate(new Date());
				resource.setCreatedUser("System");
				saveOrUpdate(resource);
				return true;
			}else
				logger.info("Resource is Existed");
				
		} catch (DatabaseOperationException e) {
			logger.info("Fail: saveResource(Resource resource)");
			throw new DatabaseOperationException(e);
		}
		return false;
	}

	public boolean updateResource(Resource resource) throws DatabaseOperationException{
		if (resource==null)
			throw new NullPointerException("Resource is null");
		try {
			Resource dbResource = getResource(resource);
			if (dbResource!=null){
				dbResource.setSubsidiaryCode(resource.getSubsidiaryCode());
				dbResource.setObjectCode(resource.getObjectCode());
				dbResource.setResourceType(resource.getResourceType());
				dbResource.setDescription(resource.getDescription());
				dbResource.setQuantity(resource.getQuantity());
				dbResource.setUnit(resource.getUnit());
				dbResource.setCostRate(resource.getCostRate());
				dbResource.setMaterialWastage(resource.getMaterialWastage());
				dbResource.setPackageNo(resource.getPackageNo());
				dbResource.setPackageNature(resource.getPackageNature());
				dbResource.setPackageStatus(resource.getPackageStatus());
				dbResource.setPackageType(resource.getPackageType());
				dbResource.setSplitStatus(resource.getSplitStatus());
				dbResource.setIvPostedQuantity(resource.getIvPostedQuantity());
				dbResource.setIvCumQuantity(resource.getIvCumQuantity());
				dbResource.setIvPostedAmount(resource.getIvPostedAmount());
				dbResource.setIvCumAmount(resource.getIvCumAmount());
				dbResource.setIvMovementAmount(resource.getIvMovementAmount());
				dbResource.setLastModifiedUser(resource.getLastModifiedUser());
				dbResource.setRemeasuredFactor(resource.getRemeasuredFactor());
				saveOrUpdate(dbResource);
				return true;
			}else
				logger.info("Resource does not exist");
				
		} catch (DatabaseOperationException e) {
			logger.info("Fail: updateResource(Resource resource)");
			throw new DatabaseOperationException(e);
		}
		return false;
	}
	



	@SuppressWarnings("unchecked")
	public List<Resource> getAllResourcesByJobNumber(String jobNumber)  throws Exception {
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
			criteria.createAlias("bqItem.page.bill.job", "job");
			if (jobNumber!=null &&  jobNumber.length()>0)
				criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			else
				throw new DatabaseOperationException("Job Number cannot be null");
			criteria.createAlias("bqItem.page", "page");
			criteria.createAlias("page.bill", "bill");
			criteria.createAlias(BQITEM, BQITEM);
			criteria.addOrder(Order.asc(REF_BILL_NO));
			criteria.addOrder(Order.asc(REF_SUB_BILL_NO));
			criteria.addOrder(Order.asc(REF_SECTION_NO));
			criteria.addOrder(Order.asc(REF_PAGE_NO));
			criteria.addOrder(Order.asc(REF_ITEM_NO));
			criteria.addOrder(Order.asc(RESOURCE_NUMBER));
			
			logger.info("Number of Resources:"+(criteria.list()==null?0:criteria.list().size()));
			return (List<Resource>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public List<BQItem> getAllBQByJob(String jobNumber)  throws Exception{
		List<BQItem> bqList = new LinkedList<BQItem>();
		return bqList;
	}	
	public void testDAO(){
		try {
			List<Resource> tmp = this.getBQResourceListByBPI("13140","80","","","1","A");
			for (int i=0; i<tmp.size();i++)
				logger.info("Test***"+tmp.get(i).getSubsidiaryCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSpecialObj(Job job,List<BQItem> resultList) {
		Bill prevBill = null;
		Page prevPage = null;
		if (resultList==null)
			logger.info("Fail: ResultList is Null");
		else 
		for (BQItem bqitem:resultList){
			if (prevBill==null || prevBill.getBillNo().trim().equals(bqitem.getPage().getBill().getBillNo().trim())){
				prevBill = bqitem.getPage().getBill();
				prevBill.setJob(job);
				prevBill.setCreatedUser("Peter");
				prevBill.setLastModifiedUser("PeterC");
				try {
					billHBDao.addBill(prevBill);
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
			if (prevPage==null || prevPage.getPageNo().trim().equals(bqitem.getPage().getPageNo().trim())){
				try {
					prevPage = bqitem.getPage();
					prevPage.setBill(billHBDao.getBill(prevPage.getBill()));
					prevPage.getBill().setJob(job);
					prevPage.setCreatedUser("Peter");
					prevPage.setLastModifiedUser("PeterC");
					pageHBDao.addPage(prevPage);
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
		}
			
	}

	/**
	 * @author koeyyeung
	 * modified on 6 Aug, 2013
	 * **/
	@SuppressWarnings("unchecked")
	public List<Resource> obtainResources(ResourceWrapper wrapper) throws DatabaseOperationException {
		List<Resource> resultList = new LinkedList<Resource>();
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
			criteria.createAlias("bqItem.page", "page");
			criteria.createAlias("page.bill", "bill");
			criteria.createAlias(BQITEM,BQITEM);
			criteria.createAlias("bill.job", "job");
			
			if(wrapper.getJobNumber().contains("%")){
				criteria.add(Restrictions.like("job.jobNumber", wrapper.getJobNumber()));
			}
			else
				criteria.add(Restrictions.eq("job.jobNumber", wrapper.getJobNumber()));

			if (wrapper.getPageNo()!=null && wrapper.getPageNo().trim().length()>0){
				if (wrapper.getPageNo().contains("%"))
					criteria.add(Restrictions.like("page.pageNo", wrapper.getPageNo()));
				else
					criteria.add(Restrictions.eq("page.pageNo", wrapper.getPageNo()));
			}

			if (wrapper.getBillNo()!=null && wrapper.getBillNo().trim().length()>0){
				if (wrapper.getBillNo().contains("%"))
					criteria.add(Restrictions.like("bill.billNo", wrapper.getBillNo()));
				else
					criteria.add(Restrictions.eq("bill.billNo", wrapper.getBillNo()));
			}
			if (wrapper.getSubBillNo()!=null && wrapper.getSubBillNo().trim().length()>0){
				if (wrapper.getSubBillNo().contains("%"))
					criteria.add(Restrictions.like("bill.subBillNo", wrapper.getSubBillNo()));
				else
					criteria.add(Restrictions.eq("bill.subBillNo", wrapper.getSubBillNo()));
			}
			if (wrapper.getItemNo()!=null && wrapper.getItemNo().trim().length()>0){
				if (wrapper.getItemNo().contains("%"))
					criteria.add(Restrictions.like("bqItem.itemNo", wrapper.getItemNo()));
				else
					criteria.add(Restrictions.eq("bqItem.itemNo", wrapper.getItemNo()));
			}
			if (wrapper.getResDescription()!=null && wrapper.getResDescription().trim().length()>0){
				if (wrapper.getResDescription().contains("%"))
					criteria.add(Restrictions.like(DESCRIPTION, wrapper.getResDescription()));
				else
					criteria.add(Restrictions.ilike(DESCRIPTION, wrapper.getResDescription(), MatchMode.ANYWHERE));
			}

			if (wrapper.getSubsidiaryCode()!=null && wrapper.getSubsidiaryCode().trim().length()>0){
				if (wrapper.getSubsidiaryCode().contains("%"))
					criteria.add(Restrictions.like(SUBSIDIARY_CODE, wrapper.getSubsidiaryCode()));
				else
					criteria.add(Restrictions.eq(SUBSIDIARY_CODE, wrapper.getSubsidiaryCode()));
			}
			if (wrapper.getObjectCode()!=null && wrapper.getObjectCode().trim().length()>0){
				if (wrapper.getObjectCode().contains("%"))
					criteria.add(Restrictions.like(OBJECT_CODE, wrapper.getObjectCode()));
				else
					criteria.add(Restrictions.eq(OBJECT_CODE, wrapper.getObjectCode()));
			}

			if (wrapper.getPackageType()!=null && wrapper.getPackageType().trim().length()>0){
				if (wrapper.getPackageType().contains("%"))
					criteria.add(Restrictions.like(PACKAGE_TYPE, wrapper.getPackageType()));
				else
					criteria.add(Restrictions.eq(PACKAGE_TYPE, wrapper.getPackageType()));
			}
			if (wrapper.getPackageNo()!=null && wrapper.getPackageNo().trim().length()>0){
				if (wrapper.getPackageNo().contains("%"))
					criteria.add(Restrictions.like(PACKAGE_NO, wrapper.getPackageNo()));
				else
					criteria.add(Restrictions.eq(PACKAGE_NO, wrapper.getPackageNo()));
			}
			criteria.addOrder(Order.asc("bill.billNo"));
			criteria.addOrder(Order.asc("bill.subBillNo"));
			criteria.addOrder(Order.asc("bill.sectionNo"));
			criteria.addOrder(Order.asc("page.pageNo"));
			criteria.addOrder(Order.asc("bqItem.itemNo"));

			resultList = (List<Resource>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesByBQItem(BQItem bqItem) throws DatabaseOperationException{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq(BQITEM, bqItem));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesByBQItemId(Long id) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias(BQITEM, BQITEM);
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq("bqItem.id", id));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public RepackagingPaginationWrapper<Resource> searchResourcesByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		if(billNo != null && billNo.trim().length() != 0){
			billNo = billNo.replace("*", "%");
			if(billNo.contains("%"))
				criteria.add(Restrictions.like(REF_BILL_NO, billNo));
			else
				criteria.add(Restrictions.eq(REF_BILL_NO, billNo));
		}
		if(subBillNo != null && subBillNo.trim().length() != 0){
			subBillNo = subBillNo.replace("*", "%");
			if(subBillNo.contains("%"))
				criteria.add(Restrictions.like(REF_SUB_BILL_NO, subBillNo));
			else
				criteria.add(Restrictions.eq(REF_SUB_BILL_NO, subBillNo));
		}
		if(pageNo != null && pageNo.trim().length() != 0){
			pageNo = pageNo.replace("*", "%");
			if(pageNo.contains("%"))
				criteria.add(Restrictions.like(REF_PAGE_NO, pageNo));
			else
				criteria.add(Restrictions.eq(REF_PAGE_NO, pageNo));
		}
		if(itemNo != null && itemNo.trim().length() != 0){
			itemNo = itemNo.replace("*", "%");
			if(itemNo.contains("%"))
				criteria.add(Restrictions.like(REF_ITEM_NO, itemNo));
			else
				criteria.add(Restrictions.eq(REF_ITEM_NO, itemNo));
		}
		if(packageNo != null && packageNo.trim().length() != 0){
			packageNo = packageNo.replace("*", "%");
			if(packageNo.contains("%"))
				criteria.add(Restrictions.like(PACKAGE_NO, packageNo));
			else
				criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
		}
		if(objectCode != null && objectCode.trim().length() != 0){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%"))
				criteria.add(Restrictions.like(OBJECT_CODE, objectCode));
			else
				criteria.add(Restrictions.eq(OBJECT_CODE, objectCode));
		}
		if(subsidiaryCode != null && subsidiaryCode.trim().length() != 0){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%"))
				criteria.add(Restrictions.like(SUBSIDIARY_CODE, subsidiaryCode));
			else
				criteria.add(Restrictions.eq(SUBSIDIARY_CODE, subsidiaryCode));
		}
		if(description != null && description.trim().length() != 0){
			description = description.replace("*", "%");
			if(description.contains("%"))
				criteria.add(Restrictions.ilike(DESCRIPTION, description));
			else
				criteria.add(Restrictions.ilike(DESCRIPTION, description, MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc(REF_BILL_NO));
		criteria.addOrder(Order.asc(REF_SUB_BILL_NO));
		criteria.addOrder(Order.asc(REF_PAGE_NO));
		criteria.addOrder(Order.asc(REF_ITEM_NO));
		criteria.addOrder(Order.asc(PACKAGE_NO));
		criteria.addOrder(Order.asc(OBJECT_CODE));
		criteria.addOrder(Order.asc(SUBSIDIARY_CODE));
		criteria.addOrder(Order.asc(DESCRIPTION));
		criteria.setFirstResult(pageNum * PAGE_SIZE);
		criteria.setMaxResults(PAGE_SIZE);
		List<Resource> resources = criteria.list();
		
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		if(billNo != null && billNo.trim().length() != 0){
			if(billNo.contains("%"))
				criteria.add(Restrictions.like(REF_BILL_NO, billNo));
			else
				criteria.add(Restrictions.eq(REF_BILL_NO, billNo));
		}
		if(subBillNo != null && subBillNo.trim().length() != 0){
			if(subBillNo.contains("%"))
				criteria.add(Restrictions.like(REF_SUB_BILL_NO, subBillNo));
			else
				criteria.add(Restrictions.eq(REF_SUB_BILL_NO, subBillNo));
		}
		if(pageNo != null && pageNo.trim().length() != 0){
			if(pageNo.contains("%"))
				criteria.add(Restrictions.like(REF_PAGE_NO, pageNo));
			else
				criteria.add(Restrictions.eq(REF_PAGE_NO, pageNo));
		}
		if(itemNo != null && itemNo.trim().length() != 0){
			if(itemNo.contains("%"))
				criteria.add(Restrictions.like(REF_ITEM_NO, itemNo));
			else
				criteria.add(Restrictions.eq(REF_ITEM_NO, itemNo));
		}
		if(packageNo != null && packageNo.trim().length() != 0){
			if(packageNo.contains("%"))
				criteria.add(Restrictions.like(PACKAGE_NO, packageNo));
			else
				criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
		}
		if(objectCode != null && objectCode.trim().length() != 0){
			if(objectCode.contains("%"))
				criteria.add(Restrictions.like(OBJECT_CODE, objectCode));
			else
				criteria.add(Restrictions.eq(OBJECT_CODE, objectCode));
		}
		if(subsidiaryCode != null && subsidiaryCode.trim().length() != 0){
			if(subsidiaryCode.contains("%"))
				criteria.add(Restrictions.like(SUBSIDIARY_CODE, subsidiaryCode));
			else
				criteria.add(Restrictions.eq(SUBSIDIARY_CODE, subsidiaryCode));
		}
		if(description != null && description.trim().length() != 0){
			description = description.replace("*", "%");
			if(description.contains("%"))
				criteria.add(Restrictions.ilike(DESCRIPTION, description));
			else
				criteria.add(Restrictions.ilike(DESCRIPTION, description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount())
				.add(Projections.sqlProjection("sum({alias}.quantity * {alias}.remeasuredFactor * {alias}.costRate) as totalAmount", new String[]{"totalAmount"}, new Type[]{DoubleType.INSTANCE})));
		Object[] results = (Object[])criteria.uniqueResult();
		int rows = results[0] != null ? ((Long)results[0]).intValue() : 0;
		Double totalAmount = results[1] != null ? (Double)results[1] : Double.valueOf(0);
		
		RepackagingPaginationWrapper<Resource> wrapper = new RepackagingPaginationWrapper<Resource>();
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalRecords(rows);
		wrapper.setTotalBudget(totalAmount);
		wrapper.setTotalPage((rows + PAGE_SIZE - 1)/PAGE_SIZE);
		wrapper.setCurrentPageContentList(resources);
		
		return wrapper;
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesByPackage(String jobNumber, String packageNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.like(OBJECT_CODE, "14%"));
		criteria.addOrder(Order.asc(REF_BILL_NO));
		criteria.addOrder(Order.asc(REF_SUB_BILL_NO));
		criteria.addOrder(Order.asc(REF_PAGE_NO));
		criteria.addOrder(Order.asc(REF_ITEM_NO));
		criteria.addOrder(Order.asc(RESOURCE_NUMBER));
		return criteria.list();
	}
	
	public Resource getResourceByBqItemAndResourceNo(BQItem bqItem, Integer resourceNo){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(BQITEM, bqItem));
		criteria.add(Restrictions.eq(RESOURCE_NUMBER, resourceNo));
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		return (Resource)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesByBpi(String jobNumber, String bpi) throws Exception{
		logger.info("Job: " + jobNumber + ", bpi: " + bpi);
		String[] bpiSplit = bpi.split("/");
		if(bpiSplit.length != 5)
			return null;
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(REF_BILL_NO, bpiSplit[0]));
		if(bpiSplit[1] == null || bpiSplit[1].length() == 0)
			criteria.add(Restrictions.isNull(REF_SUB_BILL_NO));
		else
			criteria.add(Restrictions.eq(REF_SUB_BILL_NO, bpiSplit[1]));
		if(bpiSplit[2] == null || bpiSplit[2].length() == 0)
			criteria.add(Restrictions.isNull(REF_SECTION_NO));
		else
			criteria.add(Restrictions.eq(REF_SECTION_NO, bpiSplit[2]));
		if(bpiSplit[3] == null || bpiSplit[3].length() == 0)
			criteria.add(Restrictions.isNull(REF_PAGE_NO));
		else
			criteria.add(Restrictions.eq(REF_PAGE_NO, bpiSplit[3]));
		if(bpiSplit[4] == null || bpiSplit[4].length() == 0)
			criteria.add(Restrictions.isNull(REF_ITEM_NO));
		else
			criteria.add(Restrictions.eq(REF_ITEM_NO, bpiSplit[4]));
		criteria.addOrder(Order.asc(RESOURCE_NUMBER));
		return criteria.list();
	}
	
	public Double getBudgetForPackage(String jobNumber, String packageNo) throws DatabaseOperationException{
		logger.info("getBudgetForPackage(Job job, String packageNo)");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
			criteria.add(Restrictions.like(OBJECT_CODE, "14%"));
			criteria.setProjection(Projections.sqlProjection("sum({alias}.quantity * {alias}.remeasuredFactor * {alias}.costRate) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}));
			return (Double)criteria.uniqueResult();
		}catch (Exception he){
			logger.info("Fail: getBudgetForPackage(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BudgetPostingWrapper> getAccountAmountsForOcLedger(String jobNumber) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.ne("this.subsidiaryCode", "99019999"));
		criteria.setProjection(Projections.projectionList().add(Projections.sqlProjection("sum({alias}.quantity * {alias}.remeasuredFactor * {alias}.costRate) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "amount")
				.add(Projections.groupProperty(OBJECT_CODE), OBJECT_CODE)
				.add(Projections.groupProperty(SUBSIDIARY_CODE), SUBSIDIARY_CODE));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(BudgetPostingWrapper.class));
		return criteria.list();
	}
	
	public Double getMarkupForObLedger(String jobNumber) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(SUBSIDIARY_CODE, "99019999"));
		criteria.setProjection(Projections.sum(QUANTITY));
		return (Double)criteria.uniqueResult();
	}


	/**
	 * @author tikywong
	 * Apr 7, 2011 11:32:00 AM
	 * @throws DatabaseOperationException 
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> searchResourcesByBPIDescriptionObjSubsiPackageNo( String jobNumber, String billNo, String subBillNo, 
																			String pageNo, String itemNo, String resourceDescription, 
																			String objectCode, String subsidiaryCode, String packageNo) 
																			throws DatabaseOperationException {
//		logger.info("STARTED -> searchResourcesByBPIDescriptionObjSubsiPackageNo()");
//		logger.info("J#:"+jobNumber+" Bill#:"+billNo+" Sub-Bill#:"+subBillNo+" Page#:"+pageNo+" Item#:"+itemNo+
//					" Description:"+resourceDescription+" Object:"+objectCode+" Subsidiary:"+subsidiaryCode+" Package#:"+packageNo);
		List<Resource> resultList = new ArrayList<Resource>();

		//Wildcard Settings
		if (!GenericValidator.isBlankOrNull(objectCode))
			objectCode = objectCode.replace("*","%");
		if (!GenericValidator.isBlankOrNull(subsidiaryCode))
			subsidiaryCode = subsidiaryCode.replace("*","%");
		if (!GenericValidator.isBlankOrNull(packageNo))
			packageNo = packageNo.replace("*","%");
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			
			if (!GenericValidator.isBlankOrNull(jobNumber))
				criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			if (!GenericValidator.isBlankOrNull(billNo))
				criteria.add(Restrictions.eq(REF_BILL_NO, billNo));
			if (!GenericValidator.isBlankOrNull(subBillNo))
				criteria.add(Restrictions.eq(REF_SUB_BILL_NO, subBillNo));
			if (!GenericValidator.isBlankOrNull(pageNo))
				criteria.add(Restrictions.eq(REF_PAGE_NO, pageNo));
			if (!GenericValidator.isBlankOrNull(itemNo))
				criteria.add(Restrictions.eq(REF_ITEM_NO, itemNo));
			if (!GenericValidator.isBlankOrNull(resourceDescription))
				criteria.add(Restrictions.ilike(DESCRIPTION, resourceDescription,MatchMode.ANYWHERE));
			
			//Wildcard Settings
			if (!GenericValidator.isBlankOrNull(objectCode)){
				if(objectCode.contains("%"))
					criteria.add(Restrictions.ilike(OBJECT_CODE, objectCode));
				else
					criteria.add(Restrictions.eq(OBJECT_CODE, objectCode));
			}
			if (!GenericValidator.isBlankOrNull(subsidiaryCode)){
				if (subsidiaryCode.contains("%"))
					criteria.add(Restrictions.ilike(SUBSIDIARY_CODE, subsidiaryCode));
				else
					criteria.add(Restrictions.eq(SUBSIDIARY_CODE, subsidiaryCode));
			}
			if (!GenericValidator.isBlankOrNull(packageNo)){
				if (packageNo.contains("%"))
				criteria.add(Restrictions.ilike(PACKAGE_NO, packageNo));
			else
				criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
			}
			
			resultList = (List<Resource>) criteria.list();
//			logger.info("DONE -> searchResourcesByBPIDescriptionObjSubsiPackageNo() - Number of Resources found: "+resultList.size());
		}catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		
		return resultList;
	}
	
	/**
	 * 
	 * @author tikywong
	 * Apr 11, 2011 3:12:21 PM
	 */
	public double getTotalResourceAmountbyBPI(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo) 
												throws DatabaseOperationException{
//		logger.info("STARTED -> getTotalResourceAmountbyBPI()");
//		logger.info("J#:"+jobNumber+" Bill#:"+billNo+" Sub-Bill#:"+subBillNo+" Page#:"+pageNo+" Item#:"+itemNo);
		double totalResourceAmount = 0.00;
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			
			if (!GenericValidator.isBlankOrNull(jobNumber))
				criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			if (!GenericValidator.isBlankOrNull(billNo))
				criteria.add(Restrictions.eq(REF_BILL_NO, billNo));
			if (!GenericValidator.isBlankOrNull(subBillNo))
				criteria.add(Restrictions.eq(REF_SUB_BILL_NO, subBillNo));
			if (!GenericValidator.isBlankOrNull(pageNo))
				criteria.add(Restrictions.eq(REF_PAGE_NO, pageNo));
			if (!GenericValidator.isBlankOrNull(itemNo))
				criteria.add(Restrictions.eq(REF_ITEM_NO, itemNo));
			
			//calculation
			criteria.setProjection(Projections.sqlProjection("sum({alias}.costRate * {alias}.quantity) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}));
			totalResourceAmount = (Double)criteria.uniqueResult()==null?0.00:((Double)criteria.uniqueResult()).doubleValue();
			
//			logger.info("DONE -> getTotalResourceAmountbyBPI() - Total Resource Amount: "+totalResourceAmount);
		}catch(HibernateException ex){
			logger.info("Fail: getTotalResourceAmountbyBPI(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo)");
			throw new DatabaseOperationException(ex);
		}
		
		return totalResourceAmount;
	}


	/**
	 * @author tikywong
	 * Apr 14, 2011 4:13:43 PM
	 * @throws DatabaseOperationException 
	 */
	public Boolean updateResourcesAfteringPosting(Job job, String username) throws DatabaseOperationException {
		logger.info("STARTED -> updateResourcesAfteringPosting()");
		String jobNumber = job.getJobNumber();
		logger.info("J#:"+jobNumber+" Username:"+username);
		try{
			String hql = "UPDATE Resource SET ivPostedAmount = ivCumAmount, ivPostedQuantity = ivCumQuantity, ivMovementAmount = 0, lastModifiedUser = :user, lastModifiedDate = :date WHERE jobNumber = :jobNumber AND ivPostedAmount != ivCumAmount";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setString("user", username);
			query.setString(JOB_NUMBER, jobNumber);
			query.setDate("date", new Date());
			query.executeUpdate();
		}catch(Exception he){
			logger.info("Fail: updateResourcesAfteringPosting(Job job, String username)");
			throw new DatabaseOperationException(he);
		}
		return Boolean.TRUE;
	}


	/**
	 * @author tikywong
	 * @author peterchan
	 * Apr 20, 2011 4:45:53 PM
	 * @throws DatabaseOperationException 
	 */
	public Resource obtainResource(	String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String packageNo,
									String objectCode, String subsidiaryCode, String resourceDescription, Double resourceRate, String unit,/*added by peterchan*/Integer resourceNo) throws DatabaseOperationException {
//		logger.info("SEARCH: RESOURCE - J#:"+jobNumber+" Bill#:"+billNo+" Sub-Bill#:"+subBillNo+" Page#:"+pageNo+" Item#:"+itemNo+
//				" Package#:"+packageNo+" Object:"+objectCode+" Subsidiary:"+subsidiaryCode+" Description:"+resourceDescription+
//				" Resource Rate:"+resourceRate+" Unit:"+unit);
		Resource result = new Resource();
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			
			if (!GenericValidator.isBlankOrNull(jobNumber))
				criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			if (!GenericValidator.isBlankOrNull(billNo))
				criteria.add(Restrictions.eq(REF_BILL_NO, billNo));
			if (!GenericValidator.isBlankOrNull(subBillNo))
				criteria.add(Restrictions.eq(REF_SUB_BILL_NO, subBillNo));
			if (!GenericValidator.isBlankOrNull(pageNo))
				criteria.add(Restrictions.eq(REF_PAGE_NO, pageNo));
			if (!GenericValidator.isBlankOrNull(itemNo))
				criteria.add(Restrictions.eq(REF_ITEM_NO, itemNo));
			if (!GenericValidator.isBlankOrNull(packageNo))
				criteria.add(Restrictions.eq(PACKAGE_NO, packageNo));
			if (!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq(OBJECT_CODE, objectCode));
			if (!GenericValidator.isBlankOrNull(subsidiaryCode))
				criteria.add(Restrictions.eq(SUBSIDIARY_CODE, subsidiaryCode));
			if (!GenericValidator.isBlankOrNull(resourceDescription))
				criteria.add(Restrictions.eq(DESCRIPTION, resourceDescription));
			if (resourceRate!=null)
				criteria.add(Restrictions.eq(COST_RATE, resourceRate));
			if (!GenericValidator.isBlankOrNull(unit))
				criteria.add(Restrictions.eq(UNIT, unit));
			if (resourceNo!=null)
				criteria.add(Restrictions.eq(RESOURCE_NUMBER, resourceNo));
			
			result = (Resource)criteria.uniqueResult();;
		}catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}


	/**
	 * @author tikywong
	 * May 20, 2011 3:24:48 PM
	 * @throws DatabaseOperationException 
	 */
	@SuppressWarnings("unchecked")
	public List<IVBQItemGroupedByIDWrapper> groupResourcesToBQItems(String jobNumber) throws DatabaseOperationException {
		logger.info("GROUP RESOURCE TO BQITEM BY: BQItem ID for J#"+jobNumber);
		
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		List<IVBQItemGroupedByIDWrapper> result = new ArrayList<IVBQItemGroupedByIDWrapper>();
		
		try{
			//where
			criteria.createAlias(BQITEM, BQITEM);
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));

			//calculation & groupby
			criteria.setProjection(Projections.projectionList()
					.add(Projections.sum("ivCumAmount"), "updatedIVCumulativeAmount")
					.add(Projections.groupProperty("bqItem.id"), "bqItemID"));
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(IVBQItemGroupedByIDWrapper.class));
			result = (List<IVBQItemGroupedByIDWrapper>)criteria.list();
			
			logger.info("NUMBER OF RECORDS After grouping: "+result.size());			
		}catch(HibernateException e){
			logger.info("Fail: getResourcesGroupedByBQItem(String jobNumber)");
			throw new DatabaseOperationException(e);
		}
		return result;
	}
	
	
	/**
	 * @author tikywong
	 * May 23, 2011 5:31:18 PM
	 * @throws DatabaseOperationException 
	 * @throws ValidateBusinessLogicException 
	 */
	@SuppressWarnings("unchecked")
	public List<IVBQResourceSummaryGroupedBySCWrapper> groupResourcesToBQResourceSummariesForSC(String jobNumber) throws DatabaseOperationException, ValidateBusinessLogicException {
		logger.info("GROUP RESOURCE TO BQRESOURCESUMMARY BY: Subcontract for J#"+jobNumber);
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		List<IVBQResourceSummaryGroupedBySCWrapper> result = new ArrayList<IVBQResourceSummaryGroupedBySCWrapper>();
		
		try{
			//Subcontract
			//where
			criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.like("this.objectCode", "14%"));
			//calculation & groupby
			criteria.setProjection(Projections.projectionList()
					.add(Projections.sum(IV_CUM_AMOUNT),  "updatedCurrentIVAmount")
					.add(Projections.groupProperty(PACKAGE_NO), PACKAGE_NO)
					.add(Projections.groupProperty(OBJECT_CODE), OBJECT_CODE)
					.add(Projections.groupProperty(SUBSIDIARY_CODE), SUBSIDIARY_CODE));
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(IVBQResourceSummaryGroupedBySCWrapper.class));
			result = (List<IVBQResourceSummaryGroupedBySCWrapper>)criteria.list();
			
			logger.info("NUMBER OF RECORDS After grouping: "+result.size());
		}catch(HibernateException e){
			logger.info("Fail: groupResourcesToWrappersBySC(Job job)");
			throw new DatabaseOperationException(e);
		}
		return result;
	}
	
	/**
	 * @author tikywong
	 * May 23, 2011 5:31:18 PM
	 * @throws DatabaseOperationException 
	 * @throws ValidateBusinessLogicException 
	 */
	@SuppressWarnings("unchecked")
	public List<IVBQResourceSummaryGroupedBySCWrapper> groupResourcesToBQResourceSummariesForNonSC(String jobNumber) throws DatabaseOperationException, ValidateBusinessLogicException {
		logger.info("GROUP RESOURCE TO BQRESOURCESUMMARY BY: Non-Subcontract for J#"+jobNumber);
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		List<IVBQResourceSummaryGroupedBySCWrapper> result = new ArrayList<IVBQResourceSummaryGroupedBySCWrapper>();
		
		try{		
			//Non-Subcontract
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			//where
			criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.not(Restrictions.like("this.objectCode", "14%")));
			//calculation & groupby
			criteria.setProjection(Projections.projectionList()
					.add(Projections.sum(IV_CUM_AMOUNT),  "updatedCurrentIVAmount")
					.add(Projections.groupProperty(PACKAGE_NO), PACKAGE_NO)
					.add(Projections.groupProperty(OBJECT_CODE), OBJECT_CODE)
					.add(Projections.groupProperty(SUBSIDIARY_CODE), SUBSIDIARY_CODE)
					.add(Projections.groupProperty(UNIT), UNIT)
					.add(Projections.groupProperty(COST_RATE), "rate")
					.add(Projections.groupProperty(DESCRIPTION), "resourceDescription"));
			
			criteria.setResultTransformer(new AliasToBeanResultTransformer(IVBQResourceSummaryGroupedBySCWrapper.class));
			result = (List<IVBQResourceSummaryGroupedBySCWrapper>)criteria.list();
			
			logger.info("NUMBER OF RECORDS After grouping: "+result.size());
		}catch(HibernateException e){
			logger.info("Fail: groupResourcesToBQResourceSummariesForNonSC(Job job)");
			throw new DatabaseOperationException(e);
		}
		return result;
	}


	/**
	 * @author tikywong
	 * May 24, 2011 10:40:04 AM
	 * @throws DatabaseOperationException 
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> obtainSCResources(String jobNumber) throws DatabaseOperationException {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		List<Resource> result = new ArrayList<Resource>();
		try{
			//where
			criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
			criteria.add(Restrictions.like(OBJECT_CODE, "14%"));
			criteria.add(Restrictions.ne(PACKAGE_NO, "0"));
			
			result = (List<Resource>) criteria.list();
			
			logger.info("NUMBER OF RECORDS: "+result.size());
		}catch(HibernateException e){
			logger.info("Fail: getSCResources(String jobNumber)");
			throw new DatabaseOperationException(e);
		}
		return result;
	}


	/**
	 * @author tikywong
	 * Jun 17, 2011 10:11:07 AM
	 * @throws DatabaseOperationException 
	 */
	public boolean updateResourceIVAmountByHQL(Resource resource, String username) throws DatabaseOperationException {
//		logger.info("UPDATE: updateResourceIVAmountByHQL(Resource resource, String username)");
		try{
			Long resourceID = resource.getId();
			String jobNumber = resource.getJobNumber();
			Double updatedIVCumulativeAmount = resource.getIvCumAmount();
			Double updatedIVCumulativeQuantity = resource.getIvCumQuantity();
			Double updatedIVMovementAmount = resource.getIvMovementAmount();

			String hql = "UPDATE Resource SET ivCumAmount = :updatedIVCumulativeAmount, ivCumQuantity = :updatedIVCumulativeQuantity, ivMovementAmount = :updatedIVMovementAmount, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :resourceID AND jobNumber = :jobNumber";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);

			query.setDouble("updatedIVCumulativeAmount", updatedIVCumulativeAmount);
			query.setDouble("updatedIVCumulativeQuantity", updatedIVCumulativeQuantity);
			query.setDouble("updatedIVMovementAmount", updatedIVMovementAmount);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("resourceID", resourceID);
			query.setString("jobNumber", jobNumber);

			query.executeUpdate();
			return true;
		}catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
		
	}
}