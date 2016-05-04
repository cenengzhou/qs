package com.gammon.qs.dao.application;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.domain.quartz.CronTriggers;
@Repository
public class CronTriggerHBDao extends BaseHibernateDao<CronTriggers> {
	public CronTriggerHBDao() {
		super(CronTriggers.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CronTriggers> getAllTriggers(){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		return criteria.list();
	}
	
	public CronTriggers getTrigger(String triggerName, String triggerGroup){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(this.getType());
		if (triggerGroup!=null)
			triggerGroup=triggerGroup.trim();
		if (triggerName!=null)
			triggerName=triggerName.trim();
		criteria.add(Restrictions.eq("triggerName", triggerName));
		criteria.add(Restrictions.eq("triggerGroup", triggerGroup));
		return (CronTriggers) criteria.uniqueResult();
	}
	
	public void updateSpecificColumnOfTrigger(CronTriggers trigger) throws DatabaseOperationException{
		CronTriggers oldTrigger = getTrigger(trigger.getTriggerName(),trigger.getTriggerGroup());
		oldTrigger.setCronExpression(trigger.getCronExpression());
		saveOrUpdate(oldTrigger);
	}
}
