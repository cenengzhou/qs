package com.gammon.qs.dao;


import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Page;
@Repository
public class PageHBDao extends BaseHibernateDao<Page> {

	public PageHBDao() {
		super(Page.class);
	}



	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(JobWSDao.class.getName());

	
	public boolean addPage(Page page) throws DatabaseOperationException{
		if (page==null)
			throw new NullPointerException();
		if (getPage(page)==null)
		try {
			if (page.getPageNo().trim()!=null)
				page.setPageNo(page.getPageNo().trim());
			if (getPage(page)==null){
				saveOrUpdate(page);
				return true;
			}
			
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			throw new DatabaseOperationException(e); 
		}
		return false;
	}

	public Page getPage(Page page) throws DatabaseOperationException{

		if (page==null)
			throw new NullPointerException("Page is Null");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			if (page.getPageNo()==null ||page.getPageNo().trim().length()<1 )
				criteria.add(Restrictions.isNull("pageNo"));
			else
				criteria.add(Restrictions.eq("pageNo", page.getPageNo().trim()));
			criteria.createAlias("bill.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", page.getBill().getJob().getJobNumber().trim()));
			criteria.createAlias("bill", "bill");
			criteria.add(Restrictions.eq("bill.billNo", page.getBill().getBillNo().trim()));
			if (page.getBill().getSubBillNo()==null|| page.getBill().getSubBillNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bill.subBillNo", page.getBill().getSubBillNo().trim()));
			if (page.getBill().getSectionNo()==null||page.getBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bill.sectionNo", page.getBill().getSectionNo().trim()));			
			return (Page) criteria.uniqueResult();
			
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public Page getPage(String jobNumber, String billNo, String subBillNo, String sectionNo, String pageNo) throws Exception{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("bill", "bill");
		criteria.createAlias("bill.job", "job");
		criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("bill.billNo", billNo));
		
		if(StringUtils.isEmpty(subBillNo))
			criteria.add(Restrictions.isNull("bill.subBillNo"));
		else
			criteria.add(Restrictions.eq("bill.subBillNo", subBillNo));
		if(StringUtils.isEmpty(sectionNo))
			criteria.add(Restrictions.isNull("bill.sectionNo"));
		else
			criteria.add(Restrictions.eq("bill.sectionNo", sectionNo));
		if(StringUtils.isEmpty(pageNo))
			criteria.add(Restrictions.isNull("pageNo"));
		else
			criteria.add(Restrictions.eq("pageNo", pageNo));
		return (Page)criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Page> getPage(String jobNumber) throws DatabaseOperationException{

		if (jobNumber==null)
			throw new NullPointerException("Job number is Null");
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("bill.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			criteria.addOrder(Order.asc("pageNo"));
			criteria.createAlias("bill", "bill");
//			getPageSQL(jobNumber);
			return (List<Page>) criteria.list();
			
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Page> obtainPageListByBill(Bill bill) throws DatabaseOperationException{
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("bill", bill));
		criteria.addOrder(Order.asc("pageNo"));
		return (List<Page>) criteria.list();
	}
//	public List<Page> getPageSQL(String jobNumber) throws DatabaseOperationException{
//		String sql = "select job_id, bill_id, billNo, subBillNo, pageNo from job, bill, page where job.id=bill.job_id and bill.id=page.bill_id and jobNumber="+jobNumber+" group by order by billno, subbillno, pageno";
//		List<BillPageWrapper> result = this.getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(BillPageWrapper.class).list();
//		for (BillPageWrapper billPage: result)
//			logger.info(billPage.getBillNo()+"/"+billPage.getSubBillNo()+"//"+billPage.getPageNo());
//		return null;
//	}
}