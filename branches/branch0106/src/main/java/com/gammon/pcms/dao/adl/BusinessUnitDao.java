package com.gammon.pcms.dao.adl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.BusinessUnit;

@Repository
public class BusinessUnitDao extends BaseAdlHibernateDao<BusinessUnit> {

	public BusinessUnitDao() {
		super(BusinessUnit.class);
	}

	public BusinessUnit find(String jobNo) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Formatting
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("businessUnitCode", jobNo));

		return (BusinessUnit) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> obtainCompanyCodeAndName(){
		Criteria criteria = getSession().createCriteria(getType());
		Projection distinctProjection = Projections.distinct(
										Projections.projectionList()
										.add(Projections.property("companyCode"), "companyCode")
										);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(distinctProjection);
		projectionList.add(Projections.property("companyName"), "companyName");
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("companyCode"));
		List<String[]> objectList = criteria.list();
		Map<String, Map<String, String>> resultMap = new TreeMap<>();
		for(Object[] properties : objectList){
			Map<String, String> propertiesMap = new HashMap<>();
			propertiesMap.put("companyCode", properties[0].toString());
			propertiesMap.put("companyName", properties[1].toString().trim());
			resultMap.put(properties[0].toString(), propertiesMap);
		}
		
		return resultMap;
	}
}
