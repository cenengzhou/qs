package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Job;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;

@Repository
public class BQHBDao extends BaseHibernateDao<BQItem> {

	private Level levelSetting = Level.INFO;
	@Autowired
	private PageHBDao pageHBDao;
	@Autowired
	private BillHBDao billHBDao;
	@Autowired
	private JobHBDao jobHBDao;
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	
	private Logger logger = Logger.getLogger(BQHBDao.class.getName());

	public BQHBDao() {
		super(BQItem.class);
	}

	@SuppressWarnings("unchecked")
	public List<BQItem> getBQItemByJob(String jobNumber) throws Exception{
		logger.info("STARTED ->getBQItemByJob() \n"+"J#"+jobNumber);
		List<BQItem> resultList = new LinkedList<BQItem>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("page", "page");
			criteria.createAlias("page.bill", "bill");
			criteria.createAlias("bill.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.addOrder(Order.asc("sequenceNo"));
			criteria.addOrder(Order.asc("bill.billNo"));
			criteria.addOrder(Order.asc("bill.subBillNo"));
			criteria.addOrder(Order.asc("bill.sectionNo"));
			criteria.addOrder(Order.asc("page.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));
			
			resultList =  (List<BQItem>) criteria.list();
			logger.info("Number of BQItems:"+(resultList==null?0:resultList.size()));		
		}catch (HibernateException he){
			logger.info("Fail: getBQItemByJob(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
		logger.info("DONE ->getBQItemByJob()");
		return resultList;
	}

	public BQItem getBQItem(BQItem bqItem) throws DatabaseOperationException{
		try{
			logger.log(levelSetting, "Start: getBQItem(BQItem bqItem)");
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("bill.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber",bqItem.getPage().getBill().getJob().getJobNumber().trim()));

			criteria.createAlias("page", "page");
			if (bqItem.getPage().getPageNo()==null||bqItem.getPage().getPageNo().trim().length()<1)
				criteria.add(Restrictions.isNull("page.pageNo"));
			else
				criteria.add(Restrictions.eq("page.pageNo",bqItem.getPage().getPageNo().trim()));

			criteria.createAlias("page.bill", "bill");
			criteria.add(Restrictions.eq("bill.billNo",bqItem.getPage().getBill().getBillNo().trim()));

			if (bqItem.getPage().getBill().getSubBillNo()==null ||bqItem.getPage().getBill().getSubBillNo().trim().length()<1 )
				criteria.add(Restrictions.isNull("bill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bill.subBillNo",bqItem.getPage().getBill().getSubBillNo().trim()));

			if (bqItem.getPage().getBill().getSectionNo()==null || bqItem.getPage().getBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bill.sectionNo",bqItem.getPage().getBill().getSectionNo().trim()));

			criteria.add(Restrictions.eq("itemNo", bqItem.getItemNo().trim()));

			logger.log(levelSetting, "End: getBQItem(BQItem bqItem)");			
			return (BQItem)criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getBQItem(BQItem)");
			throw new DatabaseOperationException(he);
		}

	}

	public boolean saveBQItem(BQItem bqItem) throws DatabaseOperationException{
		try {
			if (getBQItem(bqItem)==null){
				bqItem.setCreatedDate(new Date());
				bqItem.setCreatedUser("System");
				bqItem.setLastModifiedUser("System");
				if (bqItem.getDescription()!=null && bqItem.getDescription().length()>254)
					bqItem.setDescription(bqItem.getDescription().substring(0, 254));
				saveOrUpdate(bqItem);
				return true;
			}
			else 
				logger.info("BQ Item was existed");
		} catch (DatabaseOperationException e) {
			logger.info("Fail: saveBQItem(BQItem bqItem)");
			throw new DatabaseOperationException(e);
		}
		return false;
	}

	public boolean updateIV(BQItem bqItem) throws Exception{
		BQItem dbBQItem = getBQItem(bqItem);
		if (dbBQItem!=null){
			dbBQItem.setIvCumQty(bqItem.getIvCumQty());
			dbBQItem.setLastModifiedUser(bqItem.getLastModifiedUser());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<BQItem> getBQItemListByBP(String jobNumber, String billNo,
			String subBillNo, String sectionNo, String pageNo) throws DatabaseOperationException {

		List<BQItem> resultList = new LinkedList<BQItem>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("bill.job", "job");

			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));

			criteria.createAlias("page", "page");
			if (pageNo==null || pageNo.trim().length()<1)
				criteria.add(Restrictions.isNull("page.pageNo"));
			else
				criteria.add(Restrictions.eq("page.pageNo", pageNo));

			criteria.createAlias("page.bill", "bill");
			criteria.add(Restrictions.eq("bill.billNo", billNo));

			if (subBillNo==null || subBillNo.trim().length()<1)
				criteria.add(Restrictions.isNull("bill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bill.subBillNo", subBillNo));

			if (sectionNo==null || sectionNo.trim().length()<1)
				criteria.add(Restrictions.isNull("bill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bill.sectionNo", sectionNo));

			criteria.addOrder(Order.asc("sequenceNo"));
			criteria.addOrder(Order.asc("bill.billNo"));
			criteria.addOrder(Order.asc("bill.subBillNo"));
			criteria.addOrder(Order.asc("bill.sectionNo"));
			criteria.addOrder(Order.asc("page.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));

			resultList = (List<BQItem>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;

	}

	public BQItem getBQItemByRef(String refJobNumber, String refBillNo, String refSubBillNo, String refSectionNo, String refPageNo, String itemNo) throws DatabaseOperationException{
		logger.info("getBQItemByRef - jobNo: " + refJobNumber + ", bill: " + refBillNo + ", subbill: " + refSubBillNo + ", section: " + refSectionNo + ", page: " + refPageNo + ", item: " + itemNo);
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("refJobNumber", refJobNumber));
			if(refBillNo == null || refBillNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refBillNo"));
			else
				criteria.add(Restrictions.eq("refBillNo", refBillNo));
			if(refSubBillNo == null || refSubBillNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refSubBillNo"));
			else
				criteria.add(Restrictions.eq("refSubBillNo", refSubBillNo));
			if(refSectionNo == null || refSectionNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refSectionNo"));
			else
				criteria.add(Restrictions.eq("refSectionNo", refSectionNo));
			if(refPageNo == null || refPageNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refPageNo"));
			else
				criteria.add(Restrictions.eq("refPageNo", refPageNo));
			if(itemNo == null || itemNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("itemNo"));
			else
				criteria.add(Restrictions.eq("itemNo", itemNo));
			return (BQItem)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public void saveBQItemSpecial(BQItem bqitem) throws Exception {
		if (bqitem==null){
			logger.info("Fail: saveBQItemSpecial(BQItem bqitem)");
			return;
		}
		if (bqitem.getPage()==null)
			logger.info("Fail: bqitem.page null");
		else{
			bqitem.setPage(pageHBDao.getPage(bqitem.getPage()));
			if (bqitem.getPage().getBill()==null)
				logger.info("Fail: bqitem.page.bill null");
			else {
				bqitem.getPage().setBill(billHBDao.getBill(bqitem.getPage().getBill()));
				if (bqitem.getPage().getBill().getJob()==null)
					logger.info("Fail: bqitem.page.job null");
				else{
					bqitem.getPage().getBill().setJob(jobHBDao.obtainJob(bqitem.getPage().getBill().getJob().getJobNumber().trim()));
					saveBQItem(bqitem);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<BQItem> obtainBQItem(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDesc) throws DatabaseOperationException {
		List<BQItem> resultList = new LinkedList<BQItem>();
		logger.info("DAO - obtainBQItem --> STARTED");
		jobNumber = jobNumber==null?"":jobNumber.replace("*", "%");
		billNo = billNo==null?"":billNo.replace("*", "%");
		subBillNo = subBillNo==null?"":subBillNo.replace("*", "%");
		pageNo = pageNo==null?"":pageNo.replace("*", "%");
		itemNo = itemNo==null?"":itemNo.replace("*", "%");
		bqDesc = bqDesc==null?"":bqDesc.replace("*", "%");
		
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("bill.job", "job");
			if(jobNumber.contains("%")){
				criteria.add(Restrictions.like("job.jobNumber", jobNumber));
			}
			else
				criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			
			criteria.createAlias("page", "page");
			if (pageNo!=null && pageNo.trim().length()>0){
				if (pageNo.contains("%"))
					criteria.add(Restrictions.like("page.pageNo", pageNo));
				else
					criteria.add(Restrictions.eq("page.pageNo", pageNo));
			}
			
			criteria.createAlias("page.bill", "bill");
			if (billNo!=null && billNo.trim().length()>0){
				if (billNo.contains("%"))
					criteria.add(Restrictions.like("bill.billNo", billNo));
				else
					criteria.add(Restrictions.eq("bill.billNo", billNo));
			}
			if (subBillNo!=null && subBillNo.trim().length()>0){
				if (subBillNo.contains("%"))
					criteria.add(Restrictions.like("bill.subBillNo", subBillNo));
				else
					criteria.add(Restrictions.eq("bill.subBillNo", subBillNo));
			}
			/*if (sectionNo!=null && sectionNo.trim().length()>0){
				if (sectionNo.contains("%"))
					criteria.add(Restrictions.like("bill.sectionNo", sectionNo));
				else
					criteria.add(Restrictions.eq("bill.sectionNo", sectionNo));
			}*/
			if (itemNo!=null && itemNo.trim().length()>0){
				if (itemNo.contains("%"))
					criteria.add(Restrictions.like("itemNo", itemNo));
				else
					criteria.add(Restrictions.eq("itemNo", itemNo));
			}
			
			if (bqDesc!=null && bqDesc.trim().length()>0){
				if (bqDesc.contains("%"))
					criteria.add(Restrictions.like("description", bqDesc));
				else
					criteria.add(Restrictions.ilike("description", bqDesc, MatchMode.ANYWHERE));
			}
				
	
			criteria.addOrder(Order.asc("sequenceNo"));
			criteria.addOrder(Order.asc("bill.billNo"));
			criteria.addOrder(Order.asc("bill.subBillNo"));
			criteria.addOrder(Order.asc("bill.sectionNo"));
			criteria.addOrder(Order.asc("page.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));

			resultList = (List<BQItem>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	/**
	 * 
	 * modified by @author tikywong
	 * May 30, 2011 11:26:24 AM
	 */
	@SuppressWarnings("unchecked")
	public RepackagingPaginationWrapper<BQItem> searchBQItemsByRefByPage(	String jobNumber, String billNo, String subBillNo, String pageNo,
																			String itemNo, String description, int pageNum) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("refJobNumber", jobNumber));
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		//by Tiky Wong on 20110530
		//Skip Comment lines and Header lines
		criteria.add(Restrictions.and(Restrictions.isNotNull("bqStatus"), Restrictions.ne("bqStatus", " ")));
		
		if(billNo != null && billNo.trim().length() != 0){
			billNo = billNo.replace("*", "%");
			if(billNo.contains("%"))
				criteria.add(Restrictions.like("refBillNo", billNo));
			else
				criteria.add(Restrictions.eq("refBillNo", billNo));
		}
		if(subBillNo != null && subBillNo.trim().length() != 0){
			subBillNo = subBillNo.replace("*", "%");
			if(subBillNo.contains("%"))
				criteria.add(Restrictions.like("refSubBillNo", subBillNo));
			else
				criteria.add(Restrictions.eq("refSubBillNo", subBillNo));
		}
		if(pageNo != null && pageNo.trim().length() != 0){
			pageNo = pageNo.replace("*", "%");
			if(pageNo.contains("%"))
				criteria.add(Restrictions.like("refPageNo", pageNo));
			else
				criteria.add(Restrictions.eq("refPageNo", pageNo));
		}
		if(itemNo != null && itemNo.trim().length() != 0){
			itemNo = itemNo.replace("*", "%");
			if(itemNo.contains("%"))
				criteria.add(Restrictions.like("itemNo", itemNo));
			else
				criteria.add(Restrictions.eq("itemNo", itemNo));
		}
		if(description != null && description.trim().length() != 0){
			description = description.replace("*", "%");
			if(description.contains("%"))
				criteria.add(Restrictions.ilike("description", description));
			else
				criteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("refBillNo"));
		criteria.addOrder(Order.asc("refSubBillNo"));
		criteria.addOrder(Order.asc("refPageNo"));
		criteria.addOrder(Order.asc("itemNo"));
		criteria.setFirstResult(pageNum * PAGE_SIZE);
		criteria.setMaxResults(PAGE_SIZE);
		List<BQItem> bqItems = criteria.list();
		
		criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("refJobNumber", jobNumber));
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		//by Tiky Wong on 20110530
		//Skipp Comment lines and Header lines
		criteria.add(Restrictions.and(Restrictions.isNotNull("bqStatus"), Restrictions.ne("bqStatus", " ")));
		
		if(billNo != null && billNo.trim().length() != 0){
			if(billNo.contains("%"))
				criteria.add(Restrictions.like("refBillNo", billNo));
			else
				criteria.add(Restrictions.eq("refBillNo", billNo));
		}
		if(subBillNo != null && subBillNo.trim().length() != 0){
			if(subBillNo.contains("%"))
				criteria.add(Restrictions.like("refSubBillNo", subBillNo));
			else
				criteria.add(Restrictions.eq("refSubBillNo", subBillNo));
		}
		if(pageNo != null && pageNo.trim().length() != 0){
			if(pageNo.contains("%"))
				criteria.add(Restrictions.like("refPageNo", pageNo));
			else
				criteria.add(Restrictions.eq("refPageNo", pageNo));
		}
		if(itemNo != null && itemNo.trim().length() != 0){
			if(itemNo.contains("%"))
				criteria.add(Restrictions.like("itemNo", itemNo));
			else
				criteria.add(Restrictions.eq("itemNo", itemNo));
		}
		if(description != null && description.trim().length() != 0){
			description = description.replace("*", "%");
			if(description.contains("%"))
				criteria.add(Restrictions.ilike("description", description));
			else
				criteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount())
				.add(Projections.sqlProjection("sum({alias}.remeasuredQty * {alias}.costRate) as totalCost", new String[]{"totalCost"}, new Type[]{DoubleType.INSTANCE}))
				.add(Projections.sqlProjection("sum({alias}.remeasuredQty * {alias}.sellingRate) as totalRevenue", new String[]{"totalRevenue"}, new Type[]{DoubleType.INSTANCE})));
		//Project the result into an array with length 3
		Object[] results = (Object[])criteria.uniqueResult();
		
		int rows = results[0] != null ? ((Long)results[0]).intValue() : 0;
		Double totalCost = results[1] != null ? (Double)results[1] : Double.valueOf(0);
		Double totalRevenue = results[2] != null ? (Double)results[2] : Double.valueOf(0);
		
		logger.info("Total Number of Records found:"+rows+" Total Cost:"+totalCost+" Total Revenue:"+totalRevenue+" Page#:"+pageNum);
		
		RepackagingPaginationWrapper<BQItem> wrapper = new RepackagingPaginationWrapper<BQItem>();
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalRecords(rows);
		wrapper.setTotalBudget(totalCost);
		wrapper.setTotalSellingValue(totalRevenue);
		wrapper.setTotalPage((rows + PAGE_SIZE - 1)/PAGE_SIZE);
		wrapper.setCurrentPageContentList(bqItems);
		
		return wrapper;
	}
	
	public String getNextItemNoForBill(String jobNumber, String billNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		criteria.add(Restrictions.eq("refJobNumber", jobNumber));
		criteria.add(Restrictions.eq("refBillNo", billNo));
		criteria.add(Restrictions.sqlRestriction("LENGTH(TRIM(TRANSLATE(ITEMNO, ' 0123456789', ' '))) is null"));		//Only including numeric character
		//Cast ITEMNo to number before max()
		criteria.setProjection(Projections.sqlProjection("MAX(TO_NUMBER(ITEMNO)) as itemNo_num", new String[]{"itemNo_num"}, new Type[]{StringType.INSTANCE}));
		String maxItemNo = (String)criteria.uniqueResult();
		if(maxItemNo == null)
			return "0000001";
		String itemNo = "000000" + Integer.toString(Integer.parseInt(maxItemNo) + 1);
		return itemNo.substring(itemNo.length() - 7, itemNo.length());
	}
	
	public BQItem getBqItemWithResources(Long id) throws Exception{
		BQItem bqItem = this.get(id);
		return bqItem;
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 21, 2011 10:09:07 AM
	 */
	public void updateBQItems(List<BQItem> bqItems){
		if(bqItems == null)
			return;
		
//		Session session = this.getSessionFactory().getCurrentSession();
//		Transaction tx = session.beginTransaction(); // TransactionException: nested transactions not supported in Junit
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		for(int i = 0; i < bqItems.size(); i++){
			session.update(bqItems.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
		session.getTransaction().commit();
		session.close();
//		tx.commit();
	}

	/**
	 * @author tikywong
	 * July 19, 2011 10:16:00 AM
	 * @param bqItems
	 */
	public void saveBQItems(List<BQItem> bqItems){
		if(bqItems == null)
			return;
		
		Session session = this.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		for(int i = 0; i < bqItems.size(); i++){
			session.save(bqItems.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
		
		tx.commit();
	}

	/**
	 * @author tikywong
	 * Apr 1, 2011 5:22:54 PM
	 * @throws DatabaseOperationException 
	 */
	@SuppressWarnings("unchecked")
	public List<BQItem> searchBQItemsByBPIDescription(	String jobNumber, String billNo, String subBillNo, 
													String pageNo, String itemNo, String bqDescription) throws DatabaseOperationException {
		List<BQItem> resultList = new ArrayList<BQItem>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));

			if (!GenericValidator.isBlankOrNull(jobNumber))
				criteria.add(Restrictions.eq("refJobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(billNo))
				criteria.add(Restrictions.eq("refBillNo", billNo));
			if (!GenericValidator.isBlankOrNull(subBillNo))
				criteria.add(Restrictions.eq("refSubBillNo", subBillNo));
			if (!GenericValidator.isBlankOrNull(pageNo))
				criteria.add(Restrictions.eq("refPageNo", pageNo));
			if (!GenericValidator.isBlankOrNull(itemNo))
				criteria.add(Restrictions.eq("itemNo", itemNo));
			if (!GenericValidator.isBlankOrNull(bqDescription))
				criteria.add(Restrictions.ilike("description", bqDescription,MatchMode.ANYWHERE));

			resultList = (List<BQItem>) criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}

	/**
	 * @author tikywong
	 * Apr 14, 2011 3:58:15 PM
	 * @throws DatabaseOperationException 
	 */
	public Boolean updateBQItemsAfterPosting(Job job, String username) throws DatabaseOperationException {
		String jobNumber = job.getJobNumber();
		logger.info("STARTED -> updateBQItemsAfterPosting()");
		logger.info("J#:"+jobNumber+" Username:"+username);
		try{
			String hql = "UPDATE BQItem SET ivPostedAmount = ivCumAmount, ivPostedQty = ivCumQty, lastModifiedUser = :user, lastModifiedDate = :date WHERE refJobNumber = :jobNumber  AND ivPostedAmount != ivCumAmount";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setString("user", username);
			query.setString("jobNumber", jobNumber);
			query.setDate("date", new Date());
			query.executeUpdate();
		}catch(Exception he){
			logger.info("Fail: updateBQItemsAfterPosting(Job job, String username)");
			throw new DatabaseOperationException(he);
		}
		return Boolean.TRUE;
		
	}

	/**
	 * @author tikywong
	 * Apr 20, 2011 9:25:21 AM
	 * @throws DatabaseOperationException 
	 */
	public BQItem searchBQItem(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDescription, Double costRate, String unit) throws DatabaseOperationException {
		BQItem result = new BQItem();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));

			if (!GenericValidator.isBlankOrNull(jobNumber))
				criteria.add(Restrictions.eq("refJobNumber", jobNumber));
			if (!GenericValidator.isBlankOrNull(billNo))
				criteria.add(Restrictions.eq("refBillNo", billNo));
			if (!GenericValidator.isBlankOrNull(subBillNo))
				criteria.add(Restrictions.eq("refSubBillNo", subBillNo));
			if (!GenericValidator.isBlankOrNull(pageNo))
				criteria.add(Restrictions.eq("refPageNo", pageNo));
			if (!GenericValidator.isBlankOrNull(itemNo))
				criteria.add(Restrictions.eq("itemNo", itemNo));
			if (!GenericValidator.isBlankOrNull(bqDescription))
				criteria.add(Restrictions.eq("description", bqDescription));
			if(costRate!=null)
				criteria.add(Restrictions.eq("costRate", costRate));
			if (!GenericValidator.isBlankOrNull(unit))
				criteria.add(Restrictions.eq("unit", unit));
			
			result = (BQItem)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;
	}

	/**
	 * @author tikywong
	 * Jun 17, 2011 10:02:13 AM
	 * @throws DatabaseOperationException 
	 */
	public boolean updateBQItemIVAmountByHQL(BQItem bqItem, String username) throws DatabaseOperationException {
		try{
			Long bqItemID = bqItem.getId();
			String jobNumber = bqItem.getRefJobNumber();
			Double updatedIVCumulativeAmount = bqItem.getIvCumAmount();
			Double updatedIVCumulativeQuantity = bqItem.getIvCumQty();

			String hql = "UPDATE BQItem SET ivCumAmount = :updatedIVCumulativeAmount, ivCumQty = :updatedIVCumulativeQuantity, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :bqItemID AND refJobNumber = :jobNumber";
			Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);

			query.setDouble("updatedIVCumulativeAmount", updatedIVCumulativeAmount);
			query.setDouble("updatedIVCumulativeQuantity", updatedIVCumulativeQuantity);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("bqItemID", bqItemID);
			query.setString("jobNumber", jobNumber);

			query.executeUpdate();
			return true;
		}catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
}
