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
import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.wrapper.bpi.BillPageWrapper;
import com.gammon.qs.domain.BpiPage;
@Repository
public class BpiBillHBDao extends BaseHibernateDao<BpiBill> {
	
	public BpiBillHBDao() {
		super(BpiBill.class);
	}

	private Logger logger = Logger.getLogger(BpiBillHBDao.class.getName());

	
	public boolean addBill(BpiBill bpiBill) throws DatabaseOperationException{
		if (bpiBill==null)
			throw new NullPointerException();

		try {
			bpiBill.setBillNo(bpiBill.getBillNo().trim());
			bpiBill.setSubBillNo(bpiBill.getSubBillNo().trim());
			bpiBill.setSectionNo(bpiBill.getSectionNo().trim());
			if (getBpiBill(bpiBill)==null)
				this.saveOrUpdate(bpiBill);
		} catch (DatabaseOperationException e) {
			logger.info(e.getLocalizedMessage());
			throw new DatabaseOperationException(e); 
		}
		return false;
	}
	
	public boolean billsExistUnderJob(JobInfo jobInfo){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		return count.intValue() > 0;
	}

	public BpiBill getBpiBill(BpiBill bpiBill) throws DatabaseOperationException{
		if (bpiBill==null)
			throw new NullPointerException("Bill is Null");
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", bpiBill.getJobInfo().getJobNumber().trim()));
			criteria.add(Restrictions.eq("billNo", bpiBill.getBillNo().trim()));
			if (bpiBill.getSubBillNo()==null||bpiBill.getSubBillNo().length()<1)
				criteria.add(Restrictions.isNull("subBillNo"));
			else
				criteria.add(Restrictions.eq("subBillNo", bpiBill.getSubBillNo().trim()));
			if (bpiBill.getSectionNo()==null||bpiBill.getSectionNo().length()<1)
				criteria.add(Restrictions.isNull("sectionNo"));
			else
				criteria.add(Restrictions.eq("sectionNo", bpiBill.getSectionNo().trim()));
			return (BpiBill) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getBill(Bill bill:"+bpiBill.getBillNo()+"/"+bpiBill.getSubBillNo()+"/"+bpiBill.getSectionNo()+") job:"+bpiBill.getJobInfo().getJobNumber());
			throw new DatabaseOperationException(he);
		}
	}
	
	public BpiBill getBill(String jobNumber, String billNo, String subBillNo, String sectionNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
		criteria.add(Restrictions.eq("billNo", billNo));
		if(subBillNo == null)
			criteria.add(Restrictions.isNull("subBillNo"));
		else
			criteria.add(Restrictions.eq("subBillNo", subBillNo));
		if(sectionNo == null)
			criteria.add(Restrictions.isNull("sectionNo"));
		else
			criteria.add(Restrictions.eq("sectionNo", sectionNo));
		return (BpiBill)criteria.uniqueResult();
	}
	
	public List<BpiBill> getBill(String jobNumber) throws DatabaseOperationException{
		if (jobNumber==null)
			throw new NullPointerException("Job Number is Null");
		try {
//			logger.info("Original Criteria");
//			Criteria criteria = getSession().createCriteria(this.getType());
//			criteria.createAlias("jobInfo", "jobInfo");
//			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
//			criteria.addOrder(Order.asc("billNo"));
//			criteria.addOrder(Order.asc("subBillNo"));
//			criteria.addOrder(Order.asc("sectionNo"));
//			criteria.createAlias("bpiPage", "bpiPage");
//			criteria.addOrder(Order.asc("bpiPage.pageNo"));

			return (List<BpiBill>) getPageSQL(jobNumber);//criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getBill(Bill bill:"+jobNumber);
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiBill> getPageSQL(String jobNumber) throws DatabaseOperationException{
		
		String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
		List<BpiBill> billList = new ArrayList<BpiBill>();
		String sql = "select job_info_ID, bpi_bill_ID, billNo, subBillNo, sectionNo, pageNo from "+schema+".JOB_INFO,"+schema +".BPI_BILL, "
		+schema +".BPI_PAGE where JOB_INFO.id=BPI_BILL.job_id and BPI_BILL.id=BPI_PAGE.bill_id and jobNumber='"+jobNumber
		+"' order by billno, subbillno, pageno";// 
		List<BillPageWrapper> result = getSession().createSQLQuery(sql)
		.addScalar("job_info_ID", LongType.INSTANCE).addScalar("bpi_bill_ID", LongType.INSTANCE)
		.addScalar("billNo").addScalar("subBillNo").addScalar("sectionNo").addScalar("pageNo")
		.setResultTransformer(Transformers.aliasToBean(BillPageWrapper.class)).list();
		BpiBill preBill=null;
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
				preBill = new BpiBill();
				preBill.setBillNo(billPageWrapper.getBillNo());
				preBill.setSubBillNo(billPageWrapper.getSubBillNo());
				preBill.setSectionNo(billPageWrapper.getSectionNo());
			}

			BpiPage page = new BpiPage();
			page.setBpiBill(preBill);
			page.setPageNo(billPageWrapper.getPageNo());
		}
		if (preBill!=null)
			billList.add(preBill);
		return billList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<BpiBill> getBillListWithPagesByJob(JobInfo jobInfo)  throws Exception{
//		logger.info("SEARCH: getBillListWithPagesByJob() - J#"+job.getJobNumber());
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.addOrder(Order.asc("billNo"));
			criteria.addOrder(Order.asc("subBillNo"));
			criteria.addOrder(Order.asc("sectionNo"));
			List<BpiBill> bills = (List<BpiBill>) criteria.list();
			return bills;
		}catch (HibernateException he){
			logger.info("Fail: getBillListWithPagesByJob(Job job)");
			throw new DatabaseOperationException(he);
		}
	}

}
