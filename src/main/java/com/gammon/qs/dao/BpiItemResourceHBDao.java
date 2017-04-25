package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.wrapper.BudgetPostingWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;

@Repository
public class BpiItemResourceHBDao extends BaseHibernateDao<BpiItemResource> {
	
	public BpiItemResourceHBDao() {
		super(BpiItemResource.class);
	}

	@Autowired
	private BpiBillHBDao bpiBillHBDao;
	@Autowired
	private BpiPageHBDao bpiPageHBDao;
	
	//Variable names in Resource.java (Example: @Resource.java-refBillNo @Resource Table-BILLNOREF)
	private static final String JOB_NUMBER = "jobNumber";
	private static final String BpiITEM = "bpiItem";
	
	private static final String RESOURCE_NUMBER = "resourceNo";
	private static final String SUBSIDIARY_CODE = "subsidiaryCode";
	private static final String OBJECT_CODE = "objectCode";
	
	private static final String DESCRIPTION = "description";
	private static final String QUANTITY = "quantity";
	private static final String UNIT = "unit";
	private static final String COST_RATE = "costRate";
	private static final String PACKAGE_NO = "packageNo";
	private static final String PACKAGE_TYPE = "packageType";
	
	private static final String REF_BILL_NO = "refBillNo";
	private static final String REF_SUB_BILL_NO = "refSubBillNo";
	private static final String REF_SECTION_NO = "refSectionNo";
	private static final String REF_PAGE_NO = "refPageNo";
	private static final String REF_ITEM_NO = "refItemNo";
	
	private static final String SYSTEM_STATUS = "systemStatus";

	@SuppressWarnings("unused")
	private static final String RESOURCE_TYPE = "resourceType";
	@SuppressWarnings("unused")
	private static final String REMEASURED_FACTOR = "remeasuredFactor";
	@SuppressWarnings("unused")
	private static final String MATERIAL_WASTAGE = "materialWastage";
	@SuppressWarnings("unused")
	private static final String PACKAGE_NATURE = "packageNature";
	@SuppressWarnings("unused")
	private static final String SPLIT_STATUS = "splitStatus";
	@SuppressWarnings("unused")
	private static final String IV_POSTED_QUANTITY = "ivPostedQuantity";
	@SuppressWarnings("unused")
	private static final String IV_CUM_QUANTITY = "ivCumQuantity";
	@SuppressWarnings("unused")
	private static final String IV_POSTED_AMOUNT = "ivPostedAmount";
	private static final String IV_CUM_AMOUNT = "ivCumAmount";
	@SuppressWarnings("unused")
	private static final String IV_MOVEMENT_AMOUNT = "ivMovementAmount";

	public BpiBillHBDao getBillHBDao() {
		return bpiBillHBDao;
	}


	public void setBillHBDao(BpiBillHBDao billHBDao) {
		this.bpiBillHBDao = billHBDao;
	}


	public BpiPageHBDao getPageHBDao() {
		return bpiPageHBDao;
	}


	public void setPageHBDao(BpiPageHBDao pageHBDao) {
		this.bpiPageHBDao = pageHBDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<BudgetPostingWrapper> getAccountAmountsForOcLedger(String jobNumber) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.ne("this.subsidiaryCode", "99019999"));
		criteria.setProjection(Projections.projectionList().add(Projections.sqlProjection("sum({alias}.quantity * {alias}.remeasuredFactor * {alias}.costRate) as amount", new String[]{"amount"}, new Type[]{DoubleType.INSTANCE}), "amount")
				.add(Projections.groupProperty(OBJECT_CODE), OBJECT_CODE)
				.add(Projections.groupProperty(SUBSIDIARY_CODE), SUBSIDIARY_CODE));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(BudgetPostingWrapper.class));
		return criteria.list();
	}
	
	public Double getMarkupForObLedger(String jobNumber) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq(SYSTEM_STATUS, BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq(JOB_NUMBER, jobNumber));
		criteria.add(Restrictions.eq(SUBSIDIARY_CODE, "99019999"));
		criteria.setProjection(Projections.sum(QUANTITY));
		return (Double)criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<AccountCodeWrapper> getAccountCodeListByJob(String jobNumber) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNumber", jobNumber.trim()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"),"objectAccount")
				.add(Projections.groupProperty("subsidiaryCode"),"subsidiary"));
		criteria.addOrder(Order.asc("subsidiaryCode")).addOrder(Order.asc("objectCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountCodeWrapper.class));
		return criteria.list();
	}


	public void saveBpiItemResources(List<BpiItemResource> bqItemResources) {
		if(bqItemResources == null)
			return;
		
		Session session = getSession();
		
		for(int i = 0; i < bqItemResources.size(); i++){
			session.save(bqItemResources.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
	}
}
