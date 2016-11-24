package com.gammon.pcms.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
			defaultJobNoPref = new UserPreference();
			defaultJobNoPref.setUsername(user.getUsername());
			defaultJobNoPref.setNoStaff(new BigDecimal(user.getStaffId()));
			defaultJobNoPref.setKeyPreference(UserPreference.DEFAULT_JOB_NO);
			defaultJobNoPref.setValuePreference(defaultJobNo);
			insert(defaultJobNoPref);
		}
	}
}
