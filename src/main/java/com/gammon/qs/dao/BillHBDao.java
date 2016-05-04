package com.gammon.qs.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Page;
import com.gammon.qs.wrapper.bq.BillPageWrapper;
@Repository
public class BillHBDao extends BaseHibernateDao<Bill> {
	
	public BillHBDao() {
		super(Bill.class);
	}

	private Logger logger = Logger.getLogger(BillHBDao.class.getName());

	
	public boolean addBill(Bill bill) throws DatabaseOperationException{
		if (bill==null)
			throw new NullPointerException();

		try {
			bill.setBillNo(bill.getBillNo().trim());
			bill.setSubBillNo(bill.getSubBillNo().trim());
			bill.setSectionNo(bill.getSectionNo().trim());
			if (getBill(bill)==null)
				this.saveOrUpdate(bill);
		} catch (DatabaseOperationException e) {
			logger.info(e.getLocalizedMessage());
			throw new DatabaseOperationException(e); 
		}
		return false;
	}
	
	public boolean billsExistUnderJob(Job job){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job", job));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		return count.intValue() > 0;
	}

	public Bill getBill(Bill bill) throws DatabaseOperationException{
		if (bill==null)
			throw new NullPointerException("Bill is Null");
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", bill.getJob().getJobNumber().trim()));
			criteria.add(Restrictions.eq("billNo", bill.getBillNo().trim()));
			if (bill.getSubBillNo()==null||bill.getSubBillNo().length()<1)
				criteria.add(Restrictions.isNull("subBillNo"));
			else
				criteria.add(Restrictions.eq("subBillNo", bill.getSubBillNo().trim()));
			if (bill.getSectionNo()==null||bill.getSectionNo().length()<1)
				criteria.add(Restrictions.isNull("sectionNo"));
			else
				criteria.add(Restrictions.eq("sectionNo", bill.getSectionNo().trim()));
			return (Bill) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getBill(Bill bill:"+bill.getBillNo()+"/"+bill.getSubBillNo()+"/"+bill.getSectionNo()+") job:"+bill.getJob().getJobNumber());
			throw new DatabaseOperationException(he);
		}
	}
	
	public Bill getBill(String jobNumber, String billNo, String subBillNo, String sectionNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("job", "job");
		criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("billNo", billNo));
		if(subBillNo == null)
			criteria.add(Restrictions.isNull("subBillNo"));
		else
			criteria.add(Restrictions.eq("subBillNo", subBillNo));
		if(sectionNo == null)
			criteria.add(Restrictions.isNull("sectionNo"));
		else
			criteria.add(Restrictions.eq("sectionNo", sectionNo));
		return (Bill)criteria.uniqueResult();
	}
	
	public List<Bill> getBill(String jobNumber) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new NullPointerException("Job Number is Null");
		try {
//			logger.info("Original Criteria");
//			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
//			criteria.createAlias("job", "job");
//			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
//			criteria.addOrder(Order.asc("billNo"));
//			criteria.addOrder(Order.asc("subBillNo"));
//			criteria.addOrder(Order.asc("sectionNo"));
//			criteria.createAlias("pages", "pages");
//			criteria.addOrder(Order.asc("pages.pageNo"));

			return (List<Bill>) getPageSQL(jobNumber);//criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getBill(Bill bill:"+jobNumber);
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Bill> getPageSQL(String jobNumber) throws DatabaseOperationException{
		
		String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
		List<Bill> billList = new ArrayList<Bill>();
		String sql = "select job_ID, bill_ID, billNo, subBillNo, sectionNo, pageNo from "+schema+".qs_job,"+schema +".qs_bill, "
		+schema +".qs_page where qs_job.id=qs_bill.job_id and qs_bill.id=qs_page.bill_id and jobNumber='"+jobNumber
		+"' order by billno, subbillno, pageno";// 
		List<BillPageWrapper> result = this.getSessionFactory().getCurrentSession().createSQLQuery(sql)
		.addScalar("job_ID", LongType.INSTANCE).addScalar("bill_ID", LongType.INSTANCE)
		.addScalar("billNo").addScalar("subBillNo").addScalar("sectionNo").addScalar("pageNo")
		.setResultTransformer(Transformers.aliasToBean(BillPageWrapper.class)).list();
		Bill preBill=null;
		for (BillPageWrapper billPageWrapper: result){
			if (preBill==null ||
					!(preBill.getBillNo()==null && billPageWrapper.getBillNo()==null || 
							preBill.getBillNo().equals(billPageWrapper.getBillNo()))|| 
					!(preBill.getSubBillNo()==null && billPageWrapper.getSubBillNo()==null || 
							preBill.getSubBillNo().equals(billPageWrapper.getSubBillNo()))||
					!(preBill.getSectionNo()==null && billPageWrapper.getSectionNo()==null || 
							preBill.getSectionNo().equals(billPageWrapper.getSectionNo()))
			){
				if (preBill!=null)
					billList.add(preBill);
				preBill = new Bill();
				preBill.setBillNo(billPageWrapper.getBillNo());
				preBill.setSubBillNo(billPageWrapper.getSubBillNo());
				preBill.setSectionNo(billPageWrapper.getSectionNo());
			}

			Page page = new Page();
			page.setBill(preBill);
			page.setPageNo(billPageWrapper.getPageNo());
		}
		if (preBill!=null)
			billList.add(preBill);
		return billList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Bill> getBillListWithPagesByJob(Job job)  throws Exception{
//		logger.info("SEARCH: getBillListWithPagesByJob() - J#"+job.getJobNumber());
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.addOrder(Order.asc("billNo"));
			criteria.addOrder(Order.asc("subBillNo"));
			criteria.addOrder(Order.asc("sectionNo"));
			List<Bill> bills = (List<Bill>) criteria.list();
			return bills;
		}catch (HibernateException he){
			logger.info("Fail: getBillListWithPagesByJob(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

}
