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
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
@Repository
public class BpiItemHBDao extends BaseHibernateDao<BpiItem> {

	private Level levelSetting = Level.INFO;
	@Autowired
	private BpiPageHBDao bpiPageHBDao;
	@Autowired
	private BpiBillHBDao bpiBillHBDao;
	@Autowired
	private JobInfoHBDao jobInfoHBDao;
	
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public BpiItemHBDao() {
		super(BpiItem.class);

	}

	@SuppressWarnings("unchecked")
	public List<BpiItem> getBpiItemListForSystemAdmin(String jobNumber,
			String billNumber, String subBillNumber, String pageNumber,
			String itemNumber, String subsidiaryCode, String description)
			throws DatabaseOperationException {

		List<BpiItem> resultList = new LinkedList<BpiItem>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());

			criteria.createAlias("bpiPage", "bpiPage");

			if (jobNumber!=null && !jobNumber.equals("")){
				criteria.add(Restrictions.eq("refJobNumber", jobNumber));
			}
			if (billNumber!=null && !billNumber.equals("")){
				criteria.add(Restrictions.eq("refBillNo", billNumber));
			}
			if (subBillNumber!=null && !subBillNumber.equals("")){
				criteria.add(Restrictions.eq("refSubBillNo", subBillNumber));
			}
			if (pageNumber!=null && !pageNumber.equals("")){
				criteria.add(Restrictions.eq("refPageNo", pageNumber));
			}
			if (itemNumber!=null && !itemNumber.equals("")){
				criteria.add(Restrictions.eq("itemNo", itemNumber));
			}
			if (subsidiaryCode!=null && !subsidiaryCode.equals("")){
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if (description!=null && !description.equals("")){
				criteria.add(Restrictions.ilike("description", description,MatchMode.ANYWHERE));
			}

			resultList = (List<BpiItem>) criteria.list();

		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiItem> getBpiItemByJob(String jobNumber) throws Exception{
		logger.info("STARTED ->getBQItemByJob() \n"+"J#"+jobNumber);
		List<BpiItem> resultList = new LinkedList<BpiItem>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("bpiPage", "bpiPage");
			criteria.createAlias("bpiPage.bpIbill", "bpiBill");
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.addOrder(Order.asc("sequenceNo"));
			criteria.addOrder(Order.asc("bpiBill.billNo"));
			criteria.addOrder(Order.asc("bpiBill.subBillNo"));
			criteria.addOrder(Order.asc("bpiBill.sectionNo"));
			criteria.addOrder(Order.asc("bpiPage.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));
			
			resultList =  (List<BpiItem>) criteria.list();
			logger.info("Number of BpiItems:"+(resultList==null?0:resultList.size()));		
		}catch (HibernateException he){
			logger.info("Fail: getBpiItemByJob(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
		logger.info("DONE ->getBpiItemByJob()");
		return resultList;
	}

	public BpiItem getBpiItem(BpiItem bpiItem) throws DatabaseOperationException{
		try{
			logger.log(levelSetting, "Start: getBpiItem(BpiItem bpiItem)");
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",bpiItem.getBpiPage().getBpiBill().getJobInfo().getJobNumber().trim()));

			criteria.createAlias("bpiPage", "bpiPage");
			if (bpiItem.getBpiPage().getPageNo()==null||bpiItem.getBpiPage().getPageNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiPage.pageNo"));
			else
				criteria.add(Restrictions.eq("bpiPage.pageNo",bpiItem.getBpiPage().getPageNo().trim()));

			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			criteria.add(Restrictions.eq("bpiBill.billNo",bpiItem.getBpiPage().getBpiBill().getBillNo().trim()));

			if (bpiItem.getBpiPage().getBpiBill().getSubBillNo()==null ||bpiItem.getBpiPage().getBpiBill().getSubBillNo().trim().length()<1 )
				criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.subBillNo",bpiItem.getBpiPage().getBpiBill().getSubBillNo().trim()));

			if (bpiItem.getBpiPage().getBpiBill().getSectionNo()==null || bpiItem.getBpiPage().getBpiBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.sectionNo",bpiItem.getBpiPage().getBpiBill().getSectionNo().trim()));

			criteria.add(Restrictions.eq("itemNo", bpiItem.getItemNo().trim()));

			logger.log(levelSetting, "End: getBQItem(BQItem bqItem)");			
			return (BpiItem)criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getBpiItem(BpiItem)");
			throw new DatabaseOperationException(he);
		}

	}

	public boolean saveBpiItem(BpiItem bpiItem) throws DatabaseOperationException{
		try {
			if (getBpiItem(bpiItem)==null){
				bpiItem.setCreatedDate(new Date());
				bpiItem.setCreatedUser("System");
				bpiItem.setLastModifiedUser("System");
				if (bpiItem.getDescription()!=null && bpiItem.getDescription().length()>254)
					bpiItem.setDescription(bpiItem.getDescription().substring(0, 254));
				saveOrUpdate(bpiItem);
				return true;
			}
			else 
				logger.info("Bpi Item was existed");
		} catch (DatabaseOperationException e) {
			logger.info("Fail: saveBQItem(BpiItem bpiItem)");
			throw new DatabaseOperationException(e);
		}
		return false;
	}

	public boolean updateIV(BpiItem bpiItem) throws Exception{
		BpiItem dbBpiItem = getBpiItem(bpiItem);
		if (dbBpiItem!=null){
			dbBpiItem.setIvCumQty(bpiItem.getIvCumQty());
			dbBpiItem.setLastModifiedUser(bpiItem.getLastModifiedUser());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<BpiItem> getBpiItemListByBP(String jobNumber, String billNo,
			String subBillNo, String sectionNo, String pageNo) throws DatabaseOperationException {

		List<BpiItem> resultList = new LinkedList<BpiItem>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");

			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));

			criteria.createAlias("bpiPage", "bpiPage");
			if (pageNo==null || pageNo.trim().length()<1)
				criteria.add(Restrictions.isNull("bpiPage.pageNo"));
			else
				criteria.add(Restrictions.eq("bpiPage.pageNo", pageNo));

			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			criteria.add(Restrictions.eq("bpiBill.billNo", billNo));

			if (subBillNo==null || subBillNo.trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.subBillNo", subBillNo));

			if (sectionNo==null || sectionNo.trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.sectionNo", sectionNo));

			criteria.addOrder(Order.asc("sequenceNo"));
			criteria.addOrder(Order.asc("bpiBill.billNo"));
			criteria.addOrder(Order.asc("bpiBill.subBillNo"));
			criteria.addOrder(Order.asc("bpiBill.sectionNo"));
			criteria.addOrder(Order.asc("bpiPage.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));

			resultList = (List<BpiItem>) criteria.list();  
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;

	}

	public BpiItem getBpiItemByRef(String refJobNumber, String refBillNo, String refSubBillNo, String refSectionNo, String refPageNo, String itemNo) throws DatabaseOperationException{
		logger.info("getBpiItemByRef - jobNo: " + refJobNumber + ", bill: " + refBillNo + ", subbill: " + refSubBillNo + ", section: " + refSectionNo + ", page: " + refPageNo + ", item: " + itemNo);
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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
			return (BpiItem)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public void saveBpiItemSpecial(BpiItem bpiItem) throws Exception {
		if (bpiItem==null){
			logger.info("Fail: saveBQItemSpecial(BpiItem bpiItem)");
			return;
		}
		if (bpiItem.getBpiPage()==null)
			logger.info("Fail: bpiItem.bpiPage null");
		else{
			bpiItem.setBpiPage(bpiPageHBDao.getBpiPage(bpiItem.getBpiPage()));
			if (bpiItem.getBpiPage().getBpiBill()==null)
				logger.info("Fail: bpiItem.bpiPage.bpiBill null");
			else {
				bpiItem.getBpiPage().setBpiBill(bpiBillHBDao.getBpiBill(bpiItem.getBpiPage().getBpiBill()));
				if (bpiItem.getBpiPage().getBpiBill().getJobInfo()==null)
					logger.info("Fail: bpiItem.bpiPage.jobInfo null");
				else{
					bpiItem.getBpiPage().getBpiBill().setJobInfo(jobInfoHBDao.obtainJobInfo(bpiItem.getBpiPage().getBpiBill().getJobInfo().getJobNumber().trim()));
					saveBpiItem(bpiItem);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpiItem> obtainBpiItem(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDesc) throws DatabaseOperationException {
		List<BpiItem> resultList = new LinkedList<BpiItem>();
		logger.info("DAO - obtainBQItem --> STARTED");
		jobNumber = jobNumber==null?"":jobNumber.replace("*", "%");
		billNo = billNo==null?"":billNo.replace("*", "%");
		subBillNo = subBillNo==null?"":subBillNo.replace("*", "%");
		pageNo = pageNo==null?"":pageNo.replace("*", "%");
		itemNo = itemNo==null?"":itemNo.replace("*", "%");
		bqDesc = bqDesc==null?"":bqDesc.replace("*", "%");
		
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			if(jobNumber.contains("%")){
				criteria.add(Restrictions.like("jobInfo.jobNumber", jobNumber));
			}
			else
				criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			
			criteria.createAlias("bpiPage", "bpiPage");
			if (pageNo!=null && pageNo.trim().length()>0){
				if (pageNo.contains("%"))
					criteria.add(Restrictions.like("bpiPage.pageNo", pageNo));
				else
					criteria.add(Restrictions.eq("bpiPage.pageNo", pageNo));
			}
			
			criteria.createAlias("bpiPage.bpiBill", "bpiBill");
			if (billNo!=null && billNo.trim().length()>0){
				if (billNo.contains("%"))
					criteria.add(Restrictions.like("bpiBill.billNo", billNo));
				else
					criteria.add(Restrictions.eq("bpiBill.billNo", billNo));
			}
			if (subBillNo!=null && subBillNo.trim().length()>0){
				if (subBillNo.contains("%"))
					criteria.add(Restrictions.like("bpiBill.subBillNo", subBillNo));
				else
					criteria.add(Restrictions.eq("bpiBill.subBillNo", subBillNo));
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
			criteria.addOrder(Order.asc("bpiBill.billNo"));
			criteria.addOrder(Order.asc("bpiBill.subBillNo"));
			criteria.addOrder(Order.asc("bpiBill.sectionNo"));
			criteria.addOrder(Order.asc("bpiPage.pageNo"));
			criteria.addOrder(Order.asc("itemNo"));

			resultList = (List<BpiItem>) criteria.list();  
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
	public RepackagingPaginationWrapper<BpiItem> searchBpiItemsByRefByPage(	String jobNumber, String billNo, String subBillNo, String pageNo,
																			String itemNo, String description, int pageNum) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
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
		List<BpiItem> bqItems = criteria.list();
		
		criteria = getSession().createCriteria(this.getType());
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
		
		RepackagingPaginationWrapper<BpiItem> wrapper = new RepackagingPaginationWrapper<BpiItem>();
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalRecords(rows);
		wrapper.setTotalBudget(totalCost);
		wrapper.setTotalSellingValue(totalRevenue);
		wrapper.setTotalPage((rows + PAGE_SIZE - 1)/PAGE_SIZE);
		wrapper.setCurrentPageContentList(bqItems);
		
		return wrapper;
	}
	
	public String getNextItemNoForBill(String jobNumber, String billNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
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
	
	public BpiItem getBpiItemWithResources(Long id) throws Exception{
		BpiItem bqItem = this.get(id);
		return bqItem;
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 21, 2011 10:09:07 AM
	 */
	public void updateBpiItems(List<BpiItem> bpiItems){
		if(bpiItems == null)
			return;
		
//		Session session = getSession();
//		Transaction tx = session.beginTransaction(); // TransactionException: nested transactions not supported in Junit
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		for(int i = 0; i < bpiItems.size(); i++){
			session.update(bpiItems.get(i));
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
	 * @param bpiItems
	 */
	public void saveBpiItems(List<BpiItem> bpiItems){
		if(bpiItems == null)
			return;
		
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		
		for(int i = 0; i < bpiItems.size(); i++){
			session.save(bpiItems.get(i));
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
	public List<BpiItem> searchBpiItemsByBPIDescription(	String jobNumber, String billNo, String subBillNo, 
													String pageNo, String itemNo, String bqDescription) throws DatabaseOperationException {
		List<BpiItem> resultList = new ArrayList<BpiItem>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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

			resultList = (List<BpiItem>) criteria.list();
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
	public Boolean updateBpiItemsAfterPosting(JobInfo jobInfo, String username) throws DatabaseOperationException {
		String jobNumber = jobInfo.getJobNumber();
		logger.info("STARTED -> updateBQItemsAfterPosting()");
		logger.info("J#:"+jobNumber+" Username:"+username);
		try{
			String hql = "UPDATE BQItem SET ivPostedAmount = ivCumAmount, ivPostedQty = ivCumQty, lastModifiedUser = :user, lastModifiedDate = :date WHERE refJobNumber = :jobNumber  AND ivPostedAmount != ivCumAmount";
			Query query = getSession().createQuery(hql);
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
	public BpiItem searchBpiItem(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDescription, Double costRate, String unit) throws DatabaseOperationException {
		BpiItem result = new BpiItem();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
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
			
			result = (BpiItem)criteria.uniqueResult();
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
	public boolean updateBpiItemIVAmountByHQL(BpiItem bpiItem, String username) throws DatabaseOperationException {
		try{
			Long bqItemID = bpiItem.getId();
			String jobNumber = bpiItem.getRefJobNumber();
			Double updatedIVCumulativeAmount = bpiItem.getIvCumAmount();
			Double updatedIVCumulativeQuantity = bpiItem.getIvCumQty();

			String hql = "UPDATE BpiItem SET ivCumAmount = :updatedIVCumulativeAmount, ivCumQty = :updatedIVCumulativeQuantity, lastModifiedUser = :username, lastModifiedDate = :date WHERE id = :bpiItemID AND refJobNumber = :jobNumber";
			Query query = getSession().createQuery(hql);

			query.setDouble("updatedIVCumulativeAmount", updatedIVCumulativeAmount);
			query.setDouble("updatedIVCumulativeQuantity", updatedIVCumulativeQuantity);
			query.setString("username", username);
			query.setDate("date", new Date());
			query.setLong("bpiItemID", bqItemID);
			query.setString("jobNumber", jobNumber);

			query.executeUpdate();
			return true;
		}catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

}
