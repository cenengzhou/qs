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
import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.BpiPage;
@Repository
public class BpiPageHBDao extends BaseHibernateDao<BpiPage> {

	public BpiPageHBDao() {
		super(BpiPage.class);
	}



	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(BpiPageHBDao.class.getName());

	
	public boolean addPage(BpiPage bpiPage) throws DatabaseOperationException{
		if (bpiPage==null)
			throw new NullPointerException();
		if (getBpiPage(bpiPage)==null)
		try {
			if (bpiPage.getPageNo().trim()!=null)
				bpiPage.setPageNo(bpiPage.getPageNo().trim());
			if (getBpiPage(bpiPage)==null){
				saveOrUpdate(bpiPage);
				return true;
			}
			
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			throw new DatabaseOperationException(e); 
		}
		return false;
	}

	public BpiPage getBpiPage(BpiPage bpiPage) throws DatabaseOperationException{

		if (bpiPage==null)
			throw new NullPointerException("Page is Null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			if (bpiPage.getPageNo()==null ||bpiPage.getPageNo().trim().length()<1 )
				criteria.add(Restrictions.isNull("pageNo"));
			else
				criteria.add(Restrictions.eq("pageNo", bpiPage.getPageNo().trim()));
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", bpiPage.getBpiBill().getJobInfo().getJobNumber().trim()));
			criteria.createAlias("bpiBill", "bpiBill");
			criteria.add(Restrictions.eq("bpiBill.billNo", bpiPage.getBpiBill().getBillNo().trim()));
			if (bpiPage.getBpiBill().getSubBillNo()==null|| bpiPage.getBpiBill().getSubBillNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.subBillNo", bpiPage.getBpiBill().getSubBillNo().trim()));
			if (bpiPage.getBpiBill().getSectionNo()==null||bpiPage.getBpiBill().getSectionNo().trim().length()<1)
				criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
			else
				criteria.add(Restrictions.eq("bpiBill.sectionNo", bpiPage.getBpiBill().getSectionNo().trim()));			
			return (BpiPage) criteria.uniqueResult();
			
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	public BpiPage getPage(String jobNumber, String billNo, String subBillNo, String sectionNo, String pageNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("bpiBill", "bpiBill");
		criteria.createAlias("bpiBill.jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("bpiBill.billNo", billNo));
		
		if(StringUtils.isEmpty(subBillNo))
			criteria.add(Restrictions.isNull("bpiBill.subBillNo"));
		else
			criteria.add(Restrictions.eq("bpiBill.subBillNo", subBillNo));
		if(StringUtils.isEmpty(sectionNo))
			criteria.add(Restrictions.isNull("bpiBill.sectionNo"));
		else
			criteria.add(Restrictions.eq("bpiBill.sectionNo", sectionNo));
		if(StringUtils.isEmpty(pageNo))
			criteria.add(Restrictions.isNull("pageNo"));
		else
			criteria.add(Restrictions.eq("pageNo", pageNo));
		return (BpiPage)criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BpiPage> getBpiPage(String jobNumber) throws DatabaseOperationException{

		if (jobNumber==null)
			throw new NullPointerException("Job number is Null");
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("bpiBill.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
			criteria.addOrder(Order.asc("pageNo"));
			criteria.createAlias("bpiBill", "bpiBill");
//			getPageSQL(jobNumber);
			return (List<BpiPage>) criteria.list();
			
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiPage> obtainPageListByBpiBill(BpiBill bpiBill) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("bpiBill", bpiBill));
		criteria.addOrder(Order.asc("pageNo"));
		return (List<BpiPage>) criteria.list();
	}
//	public List<Page> getPageSQL(String jobNumber) throws DatabaseOperationException{
//		String sql = "select job_id, bill_id, billNo, subBillNo, pageNo from job, bill, page where job.id=bill.job_id and bill.id=page.bill_id and jobNumber="+jobNumber+" group by order by billno, subbillno, pageno";
//		List<BillPageWrapper> result = getSession().createSQLQuery(sql).addEntity(BillPageWrapper.class).list();
//		for (BillPageWrapper billPage: result)
//			logger.info(billPage.getBillNo()+"/"+billPage.getSubBillNo()+"//"+billPage.getPageNo());
//		return null;
//	}
}