package com.gammon.qs.dao.application;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.quartz.CronExpression;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.domain.quartz.CronTriggers;
@Repository
public class CronTriggerHBDao extends BaseHibernateDao<CronTriggers> {
	private Logger logger = Logger.getLogger(getClass());
	
	public CronTriggerHBDao() {
		super(CronTriggers.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CronTriggers> getAllTriggers(){
		Criteria criteria = getSession().createCriteria(this.getType());
		return criteria.list();
	}
	
	public CronTriggers getTrigger(String triggerName, String triggerGroup){
		Criteria criteria = getSession().createCriteria(this.getType());
		if (triggerGroup!=null)
			triggerGroup=triggerGroup.trim();
		if (triggerName!=null)
			triggerName=triggerName.trim();
		criteria.add(Restrictions.eq("triggerName", triggerName));
		criteria.add(Restrictions.eq("triggerGroup", triggerGroup));
		return (CronTriggers) criteria.uniqueResult();
	}
	
	public void updateSpecificColumnOfTrigger(CronTriggers trigger) throws DatabaseOperationException, ParseException{
		CronTriggers oldTrigger = getTrigger(trigger.getTriggerName(),trigger.getTriggerGroup());
		String expression = trigger.getCronExpression();
		if(CronExpression.isValidExpression(expression)){
			if(!expression.equals(oldTrigger.getCronExpression())){
				logger.info("updating " + oldTrigger.getTriggerName() + ": set cron from:" +oldTrigger.getCronExpression() + " to:" + expression);
				oldTrigger.setCronExpression(expression);
				saveOrUpdate(oldTrigger);
			}
		} else {
			throw new ParseException("Invalid expression:" + expression, 0);
		}
	}
}
