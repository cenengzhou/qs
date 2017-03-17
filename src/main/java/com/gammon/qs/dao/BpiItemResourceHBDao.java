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
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.BpiPage;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.BudgetPostingWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBpiItemGroupedByIDWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBpiResourceSummaryGroupedBySCWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;

@Repository
public class BpiItemResourceHBDao extends BaseHibernateDao<BpiItemResource> {
	
	public BpiItemResourceHBDao() {
		super(BpiItemResource.class);
	}

	private Logger logger = Logger.getLogger(BpiItemResourceHBDao.class.getName());
	@Autowired
	private BpiBillHBDao bpiBillHBDao;
	@Autowired
	private BpiPageHBDao bpiPageHBDao;
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	
	//Variable names in Resource.java (Example: @Resource.java-refBillNo @Resource Table-BILLNOREF)
	private static final String JOB_NUMBER = "jobNumber";
	private static final String BpiITEM = "bpiItem";
	
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

	public BpiBillHBDao getBillHBDao() {
		return bpiBillHBDao;
	}


	public void setBillHBDao(BpiBillHBDao billHBDao) {
		this.bpiBillHBDao = billHBDao;
	}


	public BpiPageHBDao getPageHBDao() {
		return bpiPageHBDao;
	}


	public void setPageHBDao(BpiPageHBDao pageHBDao) {
		this.bpiPageHBDao = pageHBDao;
	}
	
	public BpiItemResource getResourceWithBpiItem(Long id) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", id));
		BpiItemResource resource = (BpiItemResource) criteria.uniqueResult();
		Hibernate.initialize(resource.getBpiItem());
		return resource;
	}
	
	public Integer getNextResourceNoForBpiItem(BpiItem bpiItem) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq(BpiITEM, bpiItem));
		criteria.setProjection(Projections.max(RESOURCE_NUMBER));
		Integer maxResourceNo = (Integer) criteria.uniqueResult();
		if(maxResourceNo == null)
			return Integer.valueOf(1);
		return maxResourceNo + 1;
	}

	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getBQResourceListByBPI(String jobNumber,
			String billNo, String subBillNo, String sectionNo, String pageNo,
			String itemNo) throws Exception {
		
		
			try{
				Criteria criteria = getSession().createCriteria(this.getType());
				criteria.createAlias("bpiBill.jobInfo", "jobInfo");
				if (jobNumber!=null &&  jobNumber.length()>0)
					criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
				else
					throw new DatabaseOperationException("Job Number cannot be null");
				criteria.createAlias("bpiItem.bpiPage", "bpiPage");
				if (pageNo==null || pageNo.trim().length()<1)
					criteria.add(Restrictions.isNull("bpiPage.pageNo"));
				else
					criteria.add(Restrictions.eq("bpiPage.pageNo", pageNo));
				
				if (billNo==null||billNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bpiBill.billNo"));
				else
					criteria.add(Restrictions.eq("bpiBill.billNo", billNo));
				criteria.createAlias("bpiPage.bpiBill", "bpiBill");
				
				if (subBillNo==null||subBillNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
				else 
					criteria.add(Restrictions.eq("bpiBill.subBillNo",subBillNo));
				if (sectionNo==null||sectionNo.trim().length()<1) 
					criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
				else
					criteria.add(Restrictions.eq("bpiBill.sectionNo",sectionNo));
				criteria.createAlias(BpiITEM, BpiITEM);
				criteria.add(Restrictions.eq("bqItem.itemNo", itemNo.trim()));
				criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));

				return (List<BpiItemResource>) criteria.list();  
			}catch (HibernateException he){
				throw new DatabaseOperationException(he);
			}
	}

	public BpiItemResource getResource(BpiItemResource resource) throws DatabaseOperationException{
		if (resource==null){
			logger.info("Resource is null");
			throw new NullPointerException();
		}
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(RESOURCE_NUMBER, resource.getResourceNo()));
			criteria.createAlias(BpiITEM, BpiITEM);
			if (resource.getBpiItem().getItemNo()==null || resource.getBpiItem().getItemNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiItem.itemNo"));
			else
				criteria.add(Restrictions.eq("bpiItem.itemNo", resource.getBpiItem().getItemNo()));
			criteria.createAlias("bpiItem.page", "bpiPage");
			if (resource.getBpiItem().getBpiPage().getPageNo()==null||resource.getBpiItem().getBpiPage().getPageNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiPage.pageNo"));
			else
				criteria.add(Restrictions.eq("bpiPage.pageNo", resource.getBpiItem().getBpiPage().getPageNo().trim()));
			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			criteria.add(Restrictions.eq("bpiBill.billNo", resource.getBpiItem().getBpiPage().getBpiBill().getBillNo()));
			if (resource.getBpiItem().getBpiPage().getBpiBill().getSubBillNo()==null||resource.getBpiItem().getBpiPage().getBpiBill().getSubBillNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.subBillNo", resource.getBpiItem().getBpiPage().getBpiBill().getSubBillNo().trim()));
			if (resource.getBpiItem().getBpiPage().getBpiBill().getSectionNo()==null||resource.getBpiItem().getBpiPage().getBpiBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.sectionNo", resource.getBpiItem().getBpiPage().getBpiBill().getSectionNo().trim()));
			return (BpiItemResource) criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: getResource(Resource resource)");
			throw new DatabaseOperationException(he);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getAllResourcesByJobNumber(String jobNumber)  throws Exception {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
			criteria.createAlias("bpiBill.bpiPage.bpiBill.jobInfo", "jobInfo");
			if (jobNumber!=null &&  jobNumber.length()>0)
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
			else
				throw new DatabaseOperationException("Job Number cannot be null");
			criteria.createAlias("bpiItem.bpiPage", "bpiPage");
			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			criteria.createAlias(BpiITEM, BpiITEM);
			criteria.addOrder(Order.asc(REF_BILL_NO));
			criteria.addOrder(Order.asc(REF_SUB_BILL_NO));
			criteria.addOrder(Order.asc(REF_SECTION_NO));
			criteria.addOrder(Order.asc(REF_PAGE_NO));
			criteria.addOrder(Order.asc(REF_ITEM_NO));
			criteria.addOrder(Order.asc(RESOURCE_NUMBER));
			
			logger.info("Number of Resources:"+(criteria.list()==null?0:criteria.list().size()));
			return (List<BpiItemResource>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public List<BpiItem> getAllBpiByJob(String jobNumber)  throws Exception{
		List<BpiItem> bpiList = new LinkedList<BpiItem>();
		return bpiList;
	}	
	public void testDAO(){
		try {
			List<BpiItemResource> tmp = this.getBQResourceListByBPI("13140","80","","","1","A");
			for (int i=0; i<tmp.size();i++)
				logger.info("Test***"+tmp.get(i).getSubsidiaryCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSpecialObj(JobInfo job,List<BpiItem> resultList) {
		BpiBill prevBill = null;
		BpiPage prevPage = null;
		if (resultList==null)
			logger.info("Fail: ResultList is Null");
		else 
		for (BpiItem bqitem:resultList){
			if (prevBill==null || prevBill.getBillNo().trim().equals(bqitem.getBpiPage().getBpiBill().getBillNo().trim())){
				prevBill = bqitem.getBpiPage().getBpiBill();
				prevBill.setJobInfo(job);
				prevBill.setCreatedUser("Peter");
				prevBill.setLastModifiedUser("PeterC");
				try {
					bpiBillHBDao.addBill(prevBill);
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
			if (prevPage==null || prevPage.getPageNo().trim().equals(bqitem.getBpiPage().getPageNo().trim())){
				try {
					prevPage = bqitem.getBpiPage();
					prevPage.setBpiBill(bpiBillHBDao.getBpiBill(prevPage.getBpiBill()));
					prevPage.getBpiBill().setJobInfo(job);
					prevPage.setCreatedUser("Peter");
					prevPage.setLastModifiedUser("PeterC");
					bpiPageHBDao.addPage(prevPage);
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
	public List<BpiItemResource> obtainResources(ResourceWrapper wrapper) throws DatabaseOperationException {
		List<BpiItemResource> resultList = new LinkedList<BpiItemResource>();
		
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
			criteria.createAlias("bpiItem.bpiPage", "bpiPage");
			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			criteria.createAlias(BpiITEM,BpiITEM);
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			
			if(wrapper.getJobNumber().contains("%")){
				criteria.add(Restrictions.like("jobInfo.jobNumber", wrapper.getJobNumber()));
			}
			else
				criteria.add(Restrictions.eq("jobInfo.jobNumber", wrapper.getJobNumber()));

			if (wrapper.getPageNo()!=null && wrapper.getPageNo().trim().length()>0){
				if (wrapper.getPageNo().contains("%"))
					criteria.add(Restrictions.like("bpiPage.pageNo", wrapper.getPageNo()));
				else
					criteria.add(Restrictions.eq("bpiPage.pageNo", wrapper.getPageNo()));
			}

			if (wrapper.getBillNo()!=null && wrapper.getBillNo().trim().length()>0){
				if (wrapper.getBillNo().contains("%"))
					criteria.add(Restrictions.like("bpiBill.billNo", wrapper.getBillNo()));
				else
					criteria.add(Restrictions.eq("bpiBill.billNo", wrapper.getBillNo()));
			}
			if (wrapper.getSubBillNo()!=null && wrapper.getSubBillNo().trim().length()>0){
				if (wrapper.getSubBillNo().contains("%"))
					criteria.add(Restrictions.like("bpiBill.subBillNo", wrapper.getSubBillNo()));
				else
					criteria.add(Restrictions.eq("bpiBill.subBillNo", wrapper.getSubBillNo()));
			}
			if (wrapper.getItemNo()!=null && wrapper.getItemNo().trim().length()>0){
				if (wrapper.getItemNo().contains("%"))
					criteria.add(Restrictions.like("bpiItem.itemNo", wrapper.getItemNo()));
				else
					criteria.add(Restrictions.eq("bpiItem.itemNo", wrapper.getItemNo()));
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
			criteria.addOrder(Order.asc("bpiBill.billNo"));
			criteria.addOrder(Order.asc("bpiBill.subBillNo"));
			criteria.addOrder(Order.asc("bpiBill.sectionNo"));
			criteria.addOrder(Order.asc("bpiPage.pageNo"));
			criteria.addOrder(Order.asc("bpiItem.itemNo"));

			resultList = (List<BpiItemResource>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getResourcesByBpiItem(BpiItem bqItem) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq(BpiITEM, bqItem));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getResourcesByBpiItemId(Long id) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias(BpiITEM, BpiITEM);
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		criteria.add(Restrictions.eq("bqItem.id", id));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public RepackagingPaginationWrapper<BpiItemResource> searchResourcesByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
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
		List<BpiItemResource> resources = criteria.list();
		
		criteria = getSession().createCriteria(this.getType());
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
		
		RepackagingPaginationWrapper<BpiItemResource> wrapper = new RepackagingPaginationWrapper<BpiItemResource>();
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalRecords(rows);
		wrapper.setTotalBudget(totalAmount);
		wrapper.setTotalPage((rows + PAGE_SIZE - 1)/PAGE_SIZE);
		wrapper.setCurrentPageContentList(resources);
		
		return wrapper;
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getResourcesByPackage(String jobNumber, String packageNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
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
	
	public BpiItemResource getResourceByBpiItemAndResourceNo(BpiItem bpiItem, Integer resourceNo){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(BpiITEM, bpiItem));
		criteria.add(Restrictions.eq(RESOURCE_NUMBER, resourceNo));
		criteria.add(Restrictions.eq(SYSTEM_STATUS, "ACTIVE"));
		return (BpiItemResource)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiItemResource> getResourcesByBpi(String jobNumber, String bpi) throws Exception{
		logger.info("Job: " + jobNumber + ", bpi: " + bpi);
		String[] bpiSplit = bpi.split("/");
		if(bpiSplit.length != 5)
			return null;
		Criteria criteria = getSession().createCriteria(this.getType());
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
		logger.info("getBudgetForPackage(JobInfo jobInfo, String packageNo)");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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
		Criteria criteria = getSession().createCriteria(this.getType());
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
		Criteria criteria = getSession().createCriteria(this.getType());
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
	public List<BpiItemResource> searchResourcesByBPIDescriptionObjSubsiPackageNo( String jobNumber, String billNo, String subBillNo, 
																			String pageNo, String itemNo, String resourceDescription, 
																			String objectCode, String subsidiaryCode, String packageNo) 
																			throws DatabaseOperationException {
//		logger.info("STARTED -> searchResourcesByBPIDescriptionObjSubsiPackageNo()");
//		logger.info("J#:"+jobNumber+" Bill#:"+billNo+" Sub-Bill#:"+subBillNo+" Page#:"+pageNo+" Item#:"+itemNo+
//					" Description:"+resourceDescription+" Object:"+objectCode+" Subsidiary:"+subsidiaryCode+" Package#:"+packageNo);
		List<BpiItemResource> resultList = new ArrayList<BpiItemResource>();

		//Wildcard Settings
		if (!GenericValidator.isBlankOrNull(objectCode))
			objectCode = objectCode.replace("*","%");
		if (!GenericValidator.isBlankOrNull(subsidiaryCode))
			subsidiaryCode = subsidiaryCode.replace("*","%");
		if (!GenericValidator.isBlankOrNull(packageNo))
			packageNo = packageNo.replace("*","%");
		
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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
			
			resultList = (List<BpiItemResource>) criteria.list();
//			logger.info("DONE -> searchResourcesByBPIDescriptionObjSubsiPackageNo() - Number of Resources found: "+resultList.size());
		}catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		
		return resultList;
	}
	

	
	@SuppressWarnings("unchecked")
	public List<AccountCodeWrapper> getAccountCodeListByJob(String jobNumber) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNumber", jobNumber.trim()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"),"objectAccount")
				.add(Projections.groupProperty("subsidiaryCode"),"subsidiary"));
		criteria.addOrder(Order.asc("subsidiaryCode")).addOrder(Order.asc("objectCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountCodeWrapper.class));
		return criteria.list();
	}


	public void saveBpiItemResources(List<BpiItemResource> bqItemResources) {
		if(bqItemResources == null)
			return;
		
		Session session = getSession();
		
		for(int i = 0; i < bqItemResources.size(); i++){
			session.save(bqItemResources.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
	}
}
