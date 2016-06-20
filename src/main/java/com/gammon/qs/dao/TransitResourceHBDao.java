package com.gammon.qs.dao;


import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.service.transit.TransitService;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.TransitResourceWrapper;
import com.gammon.qs.wrapper.transitBQResourceReconciliationReportRecordWrapper.TransitBQResourceReconciliationReportRecordWrapper;
@Repository
public class TransitResourceHBDao extends BaseHibernateDao<TransitResource> {
	public TransitResourceHBDao() {
		super(TransitResource.class);
	}

	private Logger logger = Logger.getLogger(TransitResourceHBDao.class.getName());

	public void deleteResourcesByHeader(Transit header) throws Exception{
		String hqlDelete = "delete TransitResource res where res.transitBQ in (from TransitBQ bq where bq.transitHeader = :header)";
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
	
	// modified by brian on 20110120
	// change to return the search result from PaginationWrapper<TransitResourceWrapper> to List<TransitResourceWrapper>
	@SuppressWarnings("unchecked")
	public List<TransitResourceWrapper> searchTransitResources(Transit transit, String billNo, String subBillNo,
			String pageNo, String itemNo, String resourceCode, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception{
		
//		PaginationWrapper<TransitResourceWrapper> wrapper = new PaginationWrapper<TransitResourceWrapper>();
		
		Criteria countCriteria = getSession().createCriteria(this.getType());
		countCriteria.createAlias("transitBpi", "transitBpi");
		countCriteria.add(Restrictions.eq("transitBpi.transit", transit));
		if(!GenericValidator.isBlankOrNull(billNo)){
			billNo = billNo.replace("*", "%");
			if(billNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.billNo", billNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.billNo", billNo));
		}
		if(!GenericValidator.isBlankOrNull(subBillNo)){
			subBillNo = subBillNo.replace("*", "%");
			if(subBillNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.subBillNo", subBillNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.subBillNo", subBillNo));
		}
		if(!GenericValidator.isBlankOrNull(pageNo)){
			pageNo = pageNo.replace("*", "%");
			if(pageNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.pageNo", pageNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.pageNo", pageNo));
		}
		if(!GenericValidator.isBlankOrNull(itemNo)){
			itemNo = itemNo.replace("*", "%");
			if(itemNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.itemNo", itemNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.itemNo", itemNo));
		}
		if(!GenericValidator.isBlankOrNull(resourceCode)){
			resourceCode = resourceCode.replace("*", "%");
			if(resourceCode.contains("%")){
				countCriteria.add(Restrictions.like("resourceCode", resourceCode));
			}
			else
				countCriteria.add(Restrictions.eq("resourceCode", resourceCode));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%")){
				countCriteria.add(Restrictions.like("objectCode", objectCode));
			}
			else
				countCriteria.add(Restrictions.eq("objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%")){
				countCriteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
			}
			else
				countCriteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			description = description.replace("*", "%");
			if(description.contains("%")){
				countCriteria.add(Restrictions.ilike("description", description));
			}
			else
				countCriteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		countCriteria.setProjection(Projections.rowCount());
		@SuppressWarnings("unused")
		Integer count = Integer.valueOf(countCriteria.uniqueResult().toString());
//		wrapper.setTotalRecords(count);
//		wrapper.setTotalPage((count + TransitRepositoryHBImpl.RECORDS_PER_PAGE - 1)/TransitRepositoryHBImpl.RECORDS_PER_PAGE);
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transit", transit));
		if(!GenericValidator.isBlankOrNull(billNo)){
			if(billNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.billNo", billNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.billNo", billNo));
		}
		if(!GenericValidator.isBlankOrNull(subBillNo)){
			if(subBillNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.subBillNo", subBillNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.subBillNo", subBillNo));
		}
		if(!GenericValidator.isBlankOrNull(pageNo)){
			if(pageNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.pageNo", pageNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.pageNo", pageNo));
		}
		if(!GenericValidator.isBlankOrNull(itemNo)){
			if(itemNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.itemNo", itemNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.itemNo", itemNo));
		}
		if(!GenericValidator.isBlankOrNull(resourceCode)){
			if(resourceCode.contains("%")){
				criteria.add(Restrictions.like("this.resourceCode", resourceCode));
			}
			else
				criteria.add(Restrictions.eq("this.resourceCode", resourceCode));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			if(objectCode.contains("%")){
				criteria.add(Restrictions.like("this.objectCode", objectCode));
			}
			else
				criteria.add(Restrictions.eq("this.objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			if(subsidiaryCode.contains("%")){
				criteria.add(Restrictions.like("this.subsidiaryCode", subsidiaryCode));
			}
			else
				criteria.add(Restrictions.eq("this.subsidiaryCode", subsidiaryCode));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			if(description.contains("%")){
				criteria.add(Restrictions.ilike("this.description", description));
			}
			else
				criteria.add(Restrictions.ilike("this.description", description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
				.add(Projections.property("transitBpi.billNo"), "billNo")
				.add(Projections.property("transitBpi.subBillNo"), "subBillNo")
				.add(Projections.property("transitBpi.pageNo"), "pageNo")
				.add(Projections.property("transitBpi.itemNo"), "itemNo")
				.add(Projections.property("resourceCode"), "resourceCode")
				.add(Projections.property("objectCode"), "objectCode")
				.add(Projections.property("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.property("type"), "type")
				.add(Projections.property("description"), "description")
				.add(Projections.property("unit"), "unit")
				.add(Projections.property("waste"), "waste")
				.add(Projections.property("totalQuantity"), "totalQuantity")
				.add(Projections.property("rate"), "rate")
				.add(Projections.property("value"), "value"));
		criteria.addOrder(Order.asc("bq.sequenceNo"));
		criteria.addOrder(Order.asc("resourceNo"));
//		criteria.setFirstResult(TransitRepositoryHBImpl.RECORDS_PER_PAGE * pageNum);
//		criteria.setMaxResults(TransitRepositoryHBImpl.RECORDS_PER_PAGE);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(TransitResourceWrapper.class));
		List<TransitResourceWrapper> resources = criteria.list();
		return resources;
//		wrapper.setCurrentPage(pageNum);
//		wrapper.setCurrentPageContentList(resources);
//		return wrapper;
	}
	
	@SuppressWarnings("unchecked")
	public PaginationWrapper<TransitResourceWrapper> searchTransitResourcesByPage(Transit header, String billNo, String subBillNo,
			String pageNo, String itemNo, String resourceCode, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception{
		PaginationWrapper<TransitResourceWrapper> wrapper = new PaginationWrapper<TransitResourceWrapper>();
		
		Criteria countCriteria = getSession().createCriteria(this.getType());
		countCriteria.createAlias("transitBpi", "transitBpi");
		countCriteria.add(Restrictions.eq("transitBpi.transit", header));
		if(!GenericValidator.isBlankOrNull(billNo)){
			billNo = billNo.replace("*", "%");
			if(billNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.billNo", billNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.billNo", billNo));
		}
		if(!GenericValidator.isBlankOrNull(subBillNo)){
			subBillNo = subBillNo.replace("*", "%");
			if(subBillNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.subBillNo", subBillNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.subBillNo", subBillNo));
		}
		if(!GenericValidator.isBlankOrNull(pageNo)){
			pageNo = pageNo.replace("*", "%");
			if(pageNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.pageNo", pageNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.pageNo", pageNo));
		}
		if(!GenericValidator.isBlankOrNull(itemNo)){
			itemNo = itemNo.replace("*", "%");
			if(itemNo.contains("%")){
				countCriteria.add(Restrictions.like("transitBpi.itemNo", itemNo));
			}
			else
				countCriteria.add(Restrictions.eq("transitBpi.itemNo", itemNo));
		}
		if(!GenericValidator.isBlankOrNull(resourceCode)){
			resourceCode = resourceCode.replace("*", "%");
			if(resourceCode.contains("%")){
				countCriteria.add(Restrictions.like("resourceCode", resourceCode));
			}
			else
				countCriteria.add(Restrictions.eq("resourceCode", resourceCode));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%")){
				countCriteria.add(Restrictions.like("objectCode", objectCode));
			}
			else
				countCriteria.add(Restrictions.eq("objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%")){
				countCriteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
			}
			else
				countCriteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			description = description.replace("*", "%");
			if(description.contains("%")){
				countCriteria.add(Restrictions.ilike("description", description));
			}
			else
				countCriteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		countCriteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(countCriteria.uniqueResult().toString());
		wrapper.setTotalRecords(count);
		wrapper.setTotalPage((count + TransitService.RECORDS_PER_PAGE - 1)/TransitService.RECORDS_PER_PAGE);
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transitHeader", header));
		if(!GenericValidator.isBlankOrNull(billNo)){
			if(billNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.billNo", billNo));
			}
			else
				criteria.add(Restrictions.eq("bq.billNo", billNo));
		}
		if(!GenericValidator.isBlankOrNull(subBillNo)){
			if(subBillNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.subBillNo", subBillNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.subBillNo", subBillNo));
		}
		if(!GenericValidator.isBlankOrNull(pageNo)){
			if(pageNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.pageNo", pageNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.pageNo", pageNo));
		}
		if(!GenericValidator.isBlankOrNull(itemNo)){
			if(itemNo.contains("%")){
				criteria.add(Restrictions.like("transitBpi.itemNo", itemNo));
			}
			else
				criteria.add(Restrictions.eq("transitBpi.itemNo", itemNo));
		}
		if(!GenericValidator.isBlankOrNull(resourceCode)){
			if(resourceCode.contains("%")){
				criteria.add(Restrictions.like("this.resourceCode", resourceCode));
			}
			else
				criteria.add(Restrictions.eq("this.resourceCode", resourceCode));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			if(objectCode.contains("%")){
				criteria.add(Restrictions.like("this.objectCode", objectCode));
			}
			else
				criteria.add(Restrictions.eq("this.objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			if(subsidiaryCode.contains("%")){
				criteria.add(Restrictions.like("this.subsidiaryCode", subsidiaryCode));
			}
			else
				criteria.add(Restrictions.eq("this.subsidiaryCode", subsidiaryCode));
		}
		if(!GenericValidator.isBlankOrNull(description)){
			if(description.contains("%")){
				criteria.add(Restrictions.ilike("this.description", description));
			}
			else
				criteria.add(Restrictions.ilike("this.description", description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
				.add(Projections.property("transitBpi.billNo"), "billNo")
				.add(Projections.property("transitBpi.subBillNo"), "subBillNo")
				.add(Projections.property("transitBpi.pageNo"), "pageNo")
				.add(Projections.property("transitBpi.itemNo"), "itemNo")
				.add(Projections.property("resourceCode"), "resourceCode")
				.add(Projections.property("objectCode"), "objectCode")
				.add(Projections.property("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.property("type"), "type")
				.add(Projections.property("description"), "description")
				.add(Projections.property("unit"), "unit")
				.add(Projections.property("waste"), "waste")
				.add(Projections.property("totalQuantity"), "totalQuantity")
				.add(Projections.property("rate"), "rate")
				.add(Projections.property("value"), "value"));
		criteria.addOrder(Order.asc("transitBpi.sequenceNo"));
		criteria.addOrder(Order.asc("resourceNo"));
		criteria.setFirstResult(TransitService.RECORDS_PER_PAGE * pageNum);
		criteria.setMaxResults(TransitService.RECORDS_PER_PAGE);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(TransitResourceWrapper.class));
		List<TransitResourceWrapper> resources = criteria.list();
		
		wrapper.setCurrentPage(pageNum);
		wrapper.setCurrentPageContentList(resources);
		return wrapper;
	}
	
	// Last modified: Brian Tse
	@SuppressWarnings("unchecked")
	public List<TransitBQResourceReconciliationReportRecordWrapper> getBQResourceTransitReportFields(Transit transit){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitBpi", "transitBpi");
		criteria.add(Restrictions.eq("transitBpi.transit", transit));
		criteria.addOrder(Order.asc("type"));
		criteria.addOrder(Order.asc("objectCode"));
		criteria.addOrder(Order.asc("resourceCode"));
		criteria.addOrder(Order.asc("packageNo"));
		criteria.addOrder(Order.asc("description"));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlProjection("sum(Round(totalQuantity * rate,2)) as eCAValue", new String[]{"eCAValue"}, new Type[]{DoubleType.INSTANCE}), "eCAValue")
				.add(Projections.groupProperty("type"), "type")
				.add(Projections.groupProperty("objectCode"), "objectCode")
				.add(Projections.groupProperty("resourceCode"), "resourceCode")
				.add(Projections.groupProperty("packageNo"), "packageNo")
				.add(Projections.groupProperty("description"), "description")
				);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(TransitBQResourceReconciliationReportRecordWrapper.class));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<TransitResource> obtainTransitResourceListByTransitBQ(TransitBpi transitBpi) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("transitBpi", transitBpi));
		return criteria.list();
	}
	
}
