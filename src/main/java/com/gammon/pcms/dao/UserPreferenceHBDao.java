package com.gammon.pcms.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.model.UserPreference;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class UserPreferenceHBDao extends BaseHibernateDao<UserPreference> {

	public UserPreferenceHBDao() {
		super(UserPreference.class);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> obtainUserPreferenceByUsername(String username) {
		Map<String, String> preferenceMap = new HashMap<>();
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("username", username));
			List<UserPreference> preferenceList =  criteria.list();
			for(UserPreference preference : preferenceList){
				preferenceMap.put(preference.getKeyPreference(), preference.getValuePreference());
			}
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return preferenceMap;
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void deleteDefaultJobNo(User user){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("username", user.getUsername()));
		criteria.add(Restrictions.eq("keyPreference", UserPreference.DEFAULT_JOB_NO));
		criteria.setMaxResults(1);
		UserPreference defaultJobNoPref = (UserPreference) criteria.uniqueResult();
		if(defaultJobNoPref != null){
			delete(defaultJobNoPref);
		}
	}
	
	public void setDefaultJobNo(String defaultJobNo, User user){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("username", user.getUsername()));
		criteria.add(Restrictions.eq("keyPreference", UserPreference.DEFAULT_JOB_NO));
		criteria.setMaxResults(1);
		UserPreference defaultJobNoPref = (UserPreference) criteria.uniqueResult();
		if(defaultJobNoPref != null){
			defaultJobNoPref.setValuePreference(defaultJobNo);
			saveOrUpdate(defaultJobNoPref);
		} else {
			defaultJobNoPref = createUserPreference(user, UserPreference.DEFAULT_JOB_NO, defaultJobNo);
			insert(defaultJobNoPref);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void saveGridPreference(String gridDbKey, String gridSeparator, List<String> prefList, User user) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("username", user.getUsername()));
		criteria.add(Restrictions.like("keyPreference", gridDbKey, MatchMode.START));
		criteria.addOrder(Order.asc("keyPreference"));
		List<UserPreference> preferenceList = criteria.list();
		if(preferenceList != null){
			for(int i = prefList.size(); i < preferenceList.size(); i++){
				delete(preferenceList.get(i));
			}
		}
		int index = 0;
		for(String str : prefList){
			UserPreference pref = null;
			if(preferenceList != null && preferenceList.size() > 0 && index <= prefList.size()) {
				pref = preferenceList.get(index);
			}
			if(pref == null) {
				pref = createUserPreference(user, gridDbKey + gridSeparator + StringUtils.leftPad(""+ (index+1), 3, "0"), str);
			} else {
				pref.setKeyPreference(gridDbKey + gridSeparator + StringUtils.leftPad(""+ (index+1), 3, "0"));
				pref.setValuePreference(str);
			}
			saveOrUpdate(pref);
			index++;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void clearGridPreference(String gridDbKey, User user) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("username", user.getUsername()));
		criteria.add(Restrictions.like("keyPreference", gridDbKey, MatchMode.START));
		List<UserPreference> preferenceList = criteria.list();
		for(UserPreference pref: preferenceList){
			delete(pref);
		}
	}

	private UserPreference createUserPreference(User user, String key, String value){
		UserPreference pref = new UserPreference();
		pref.setUsername(user.getUsername());
		pref.setNoStaff(new BigDecimal(user.getStaffId()));
		pref.setKeyPreference(key);
		pref.setValuePreference(value);
		return pref;
	}
		
	public UserPreference getUserNotificationReadStatus(String username){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("keyPreference", UserPreference.NOTIFICATION_READ_STATUS));
		criteria.setMaxResults(1);
		return (UserPreference) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<UserPreference> getUserPreferenceAnnouncementList() {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("keyPreference", UserPreference.NOTIFICATION_READ_STATUS));
		return criteria.list();
	}

}
