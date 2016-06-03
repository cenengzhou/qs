package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.IVPostingHistory;
@Repository
public class IVPostingHistoryDaoHB extends BaseHibernateDao<IVPostingHistory> {

	public IVPostingHistoryDaoHB() {
		super(IVPostingHistory.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<IVPostingHistory> obtainIVPostingHistory(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, Date fromDate, Date toDate) throws Exception{
		List<IVPostingHistory> resultList = new ArrayList<IVPostingHistory>();
		
		Criteria criteria = getSession().createCriteria(this.getType());
		
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		
		if(!GenericValidator.isBlankOrNull(packageNo)){
			packageNo = packageNo.replace("*", "%");
			if(packageNo.contains("%"))
				criteria.add(Restrictions.like("this.packageNo", packageNo));
			else
				criteria.add(Restrictions.eq("this.packageNo", packageNo));
		}
		
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%"))
				criteria.add(Restrictions.like("this.objectCode", objectCode));
			else
				criteria.add(Restrictions.eq("this.objectCode", objectCode));
		}
		
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%"))
				criteria.add(Restrictions.like("this.subsidiaryCode", subsidiaryCode));
			else
				criteria.add(Restrictions.eq("this.subsidiaryCode", subsidiaryCode));
		}
		
		if(fromDate != null)
			criteria.add(Restrictions.ge("createdDate", fromDate));
		
		if(toDate != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, 1);
			toDate = cal.getTime();
			criteria.add(Restrictions.lt("createdDate", toDate));
		}
		
		criteria.addOrder(Order.desc("createdDate")).addOrder(Order.asc("this.packageNo")).addOrder(Order.asc("this.objectCode")).addOrder(Order.asc("this.subsidiaryCode"));
		
		if(criteria.list()!=null)
			resultList = criteria.list();
		
		return resultList;
	}
}
