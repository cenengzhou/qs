package com.gammon.pcms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.UserPreference;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class UserPreferenceHBDao extends BaseHibernateDao<UserPreference> {

	public UserPreferenceHBDao() {
		super(UserPreference.class);
	}

	@SuppressWarnings("unchecked")
	public List<UserPreference> obtainUserPreferenceByUsername(String username) {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("username", username));
			return criteria.list();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}

}
