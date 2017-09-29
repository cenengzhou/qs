package com.gammon.qs.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.wrapper.transitBQResourceReconciliationReportRecordWrapper.TransitBQResourceReconciliationReportRecordWrapper;
@Repository
public class TransitResourceHBDao extends BaseHibernateDao<TransitResource> {
	public TransitResourceHBDao() {
		super(TransitResource.class);
	}

	private Logger logger = Logger.getLogger(TransitResourceHBDao.class.getName());

	public void deleteResourcesByHeader(Transit header) throws Exception{
		String hqlDelete = "delete TransitResource res where res.transitBpi in (from TransitBpi bq where bq.transit = :header)";
		Query deleteQuery = getSession().createQuery(hqlDelete);
		deleteQuery.setEntity("header", header);
		int numDeleted = deleteQuery.executeUpdate();
		logger.info(numDeleted + " transit resources deleted");
	}
	
	public void saveResources(List<TransitResource> resources) throws Exception{
		if(resources == null)
			return;

		Session session = getSession();
		for(int i = 0; i < resources.size(); i++){
			session.save(resources.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
	}
	
	public boolean dummyAccountCodesExist(Transit transit){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transit", transit));
		criteria.add(Restrictions.or(Restrictions.eq("objectCode", "000000"), Restrictions.eq("subsidiaryCode", "00000000")));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		return count.intValue() > 0;
	}
	
	// Last modified: Brian Tse
	@SuppressWarnings("unchecked")
	public List<TransitBQResourceReconciliationReportRecordWrapper> getBQResourceTransitReportFields(Transit transit){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transit", transit));
		criteria.createAlias("transitBpi.transit", "transit");
		criteria.addOrder(Order.asc("type"));
		criteria.addOrder(Order.asc("objectCode"));
		criteria.addOrder(Order.asc("resourceCode"));
		criteria.addOrder(Order.asc("packageNo"));
		criteria.addOrder(Order.asc("description"));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sum("value"),"eCAValue")
				.add(Projections.groupProperty("transit.jobNumber"), "jobNumber")
				.add(Projections.groupProperty("type"), "type")
				.add(Projections.groupProperty("objectCode"), "objectCode")
				.add(Projections.groupProperty("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.groupProperty("resourceCode"), "resourceCode")
				.add(Projections.groupProperty("packageNo"), "packageNo")
				.add(Projections.groupProperty("description"), "description")
				);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(TransitBQResourceReconciliationReportRecordWrapper.class));
		return criteria.list();
	}

	// Last modified: Brian Tse
		@SuppressWarnings("unchecked")
		public List<ResourceSummayDashboardDTO> getResourceGroupByObjectCode(String jobNo){
			List<ResourceSummayDashboardDTO> rsList = new ArrayList<ResourceSummayDashboardDTO>();
			
			String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
			String hql =
					"select objectCode, Sum(value) As amountBudget from "
					+"(select Substr (Objectcode, 1, 2) As objectCode, value" 
					+ " from "+schema+".TRANSIT_RESOURCE where transit_bpi_id  in "
					+"(Select id From "+schema+".TRANSIT_BPI Where transit_id = (Select id From "+schema+".Transit Where Jobnumber = '"+jobNo+"')))"
					+" Group By Objectcode order by Objectcode";
			
			//Object Code: 11,12,13,14,15,19
			
			SQLQuery query = getSession().createSQLQuery(hql)
										.addScalar("objectCode", StringType.INSTANCE)
										.addScalar("amountBudget", DoubleType.INSTANCE);
			
			rsList = query.setResultTransformer(new AliasToBeanResultTransformer(ResourceSummayDashboardDTO.class)).list();
			
			return rsList;
		}
	
	
	@SuppressWarnings("unchecked")
	public List<TransitResource> obtainTransitResourceListByTransitBQ(TransitBpi transitBpi) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("transitBpi", transitBpi));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TransitResource> searchTransitResources(Transit transit) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transit", transit));
		List<TransitResource> resources = criteria.list();
		return resources;
	}

	public Double getTransitTotalECAAmount(String jobNo) {
		Double amount = 0.0;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.createAlias("transitBpi.transit", "transit");
		criteria.add(Restrictions.eq("transit.jobNumber", jobNo));
		criteria.add(Restrictions.ne("transitBpi.billNo", "80"));//exclude Genuine Markup
		criteria.setProjection(Projections.sum("value"));
		amount = (Double) criteria.uniqueResult();
		if (amount == null)
			amount = 0.0;
		return amount;
	}
}
