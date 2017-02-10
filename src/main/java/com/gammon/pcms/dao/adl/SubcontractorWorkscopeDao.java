package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.SubcontractorWorkscope;

@Repository
public class SubcontractorWorkscopeDao extends BaseAdlHibernateDao<SubcontractorWorkscope> {

	public SubcontractorWorkscopeDao() {
		super(SubcontractorWorkscope.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<SubcontractorWorkscope> getAllWorkScopes(){
		Criteria criteria = getSession().createCriteria(getType());
		Set<SubcontractorWorkscope> workscopeSet = new TreeSet<>( criteria.list());
		return new ArrayList<>(workscopeSet);
	}

	@SuppressWarnings("unchecked")
	public List<SubcontractorWorkscope> obtainWorkscopeByVendorNo(String vendorNo) {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.createAlias("addressBook", "addr");
		criteria.add(Restrictions.eq("addr.addressBookNumber", new BigDecimal(vendorNo)));
		return criteria.list();
	}

}
