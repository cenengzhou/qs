package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.SubcontractDetail;

@Repository
public class ProvisionPostingHistHBDao extends
		BaseHibernateDao<ProvisionPostingHist> {

	private Logger logger = Logger.getLogger(ProvisionPostingHistHBDao.class.getName());
	
	public ProvisionPostingHistHBDao(){
		super(ProvisionPostingHist.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProvisionPostingHist> getSCDetailProvision(String jobNumber, String packageNo, Integer year, Integer month){

//		String strSQL = "from SCDetailProvisionHistory scp where scp.scDetail_ID = (select id from SCDetails scd where scd.jobNo = "+jobNumber+"";
//		String strSQL = "from SCDetailProvisionHistory scp ,SCDetails scd, SCPackgae scpk where scp.scDetails=scd and scd.jobNo = '"+jobNumber
//			+"' and scd.scPackage=scpk and scpk.packageNo='"+packageNo+"' and postedYr='"+year+"' and postedMonth='"+month+"'";
		Criteria criteria = getSession().createCriteria(this.getType());
//		criteria.createAlias("subcontractDetail", "subcontractDetail");
//		criteria.createAlias("subcontractDetail.subcontract", "subcontract");
//		criteria.createAlias("subcontract.jobInfo", "jobInfo");
//		criteria.add(Restrictions.eq("job.jobNumber",jobNumber));
//		criteria.add(Restrictions.eq("scPackage.packageNo", packageNo));
		criteria.add(Restrictions.eq("jobNo",jobNumber));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("postedYr", year));
		criteria.add(Restrictions.eq("postedMonth", month));
		//return getSession().createQuery(strSQL).list();
		return criteria.list();
	}
	
	// added by brian on 20110427
	// search SC Detail Provision History by job number, package number, posted year and posted month
	// allow the search criteria except job number to be empty
	@SuppressWarnings("unchecked")
	public List<ProvisionPostingHist> searchSCDetailProvision(String jobNumber, String packageNo, String year, String month){
		
		logger.info("[SCDetailProvisionHistoryHBDaoImpl][searchSCDetailProvision]");
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo",jobNumber));
		if(!GenericValidator.isBlankOrNull(packageNo))
			criteria.add(Restrictions.eq("packageNo", packageNo));
		if(!GenericValidator.isBlankOrNull(year)){
			Integer postedYr = Integer.parseInt(year);
			criteria.add(Restrictions.eq("postedYr", postedYr));
		}
		if(!GenericValidator.isBlankOrNull(month)){
			Integer postedMonth = Integer.parseInt(month);
			criteria.add(Restrictions.eq("postedMonth", postedMonth));
		}
		criteria.addOrder(Order.desc("jobNo"));
		criteria.addOrder(Order.desc("postedYr"));
		criteria.addOrder(Order.desc("postedMonth"));
		criteria.addOrder(Order.desc("packageNo"));
		criteria.addOrder(Order.desc("objectCode"));
		criteria.addOrder(Order.desc("subsidiaryCode"));
		return criteria.list();
	}
	
	public ProvisionPostingHist getSCDetailProvision(String jobNumber, String packageNo, Integer year, Integer month,SubcontractDetail scDetail){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo",jobNumber));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("postedYr", year));
		criteria.add(Restrictions.eq("scDetails",scDetail));
		criteria.add(Restrictions.eq("postedMonth", month));
		return (ProvisionPostingHist)criteria.uniqueResult();
	}
	
	public void saveOrUpdate(ProvisionPostingHist scDetailProvisionHistory) throws DatabaseOperationException{
		
		ProvisionPostingHist dbObj = getSCDetailProvision(	scDetailProvisionHistory.getSubcontractDetail().getJobNo(),
													 			scDetailProvisionHistory.getSubcontractDetail().getSubcontract().getPackageNo(),
															 	scDetailProvisionHistory.getPostedYr(),scDetailProvisionHistory.getPostedMonth(), 
															 	scDetailProvisionHistory.getSubcontractDetail());
		if (dbObj!=null){
			dbObj.setPostedCertAmount(scDetailProvisionHistory.getPostedCertAmount());
			dbObj.setPostedCertQty(scDetailProvisionHistory.getPostedCertQty());
			dbObj.setCumLiabilitiesAmount(scDetailProvisionHistory.getCumLiabilitiesAmount());
			dbObj.setCumLiabilitiesQty(scDetailProvisionHistory.getCumLiabilitiesQty());
			dbObj.setObjectCode(scDetailProvisionHistory.getObjectCode());
			dbObj.setSubsidiaryCode(scDetailProvisionHistory.getSubsidiaryCode());
			dbObj.setScRate(scDetailProvisionHistory.getScRate());
		}else{
			dbObj=scDetailProvisionHistory;
			dbObj.setCreatedDate(new Date());
			if (dbObj.getCreatedUser()==null || dbObj.getCreatedUser().trim().length()<1)
				dbObj.setCreatedUser("SYSTEM");
		}
		super.saveOrUpdate(dbObj);
	}

	@SuppressWarnings("unchecked")
	public List<ProvisionPostingHist> obtainSCDetailProvisionGroupedByAccountCode(String jobNumber, String packageNo, Integer postYr, Integer postMonth) {
		List<ProvisionPostingHist> resultList = new ArrayList<ProvisionPostingHist>();
		Criteria criteria = getSession().createCriteria(this.getType());

		criteria.add(Restrictions.eq("jobNo",jobNumber));
		criteria.add(Restrictions.eq("packageNo", packageNo));
		criteria.add(Restrictions.eq("postedYr", postYr));
		criteria.add(Restrictions.eq("postedMonth", postMonth));
		criteria.setProjection(Projections.projectionList()
															.add(Projections.sum("cumLiabilitiesAmount"),"cumLiabilitiesAmount")
															.add(Projections.sum("postedCertAmount"),"postedCertAmount")
															.add(Projections.groupProperty("objectCode"),"objectCode")
															.add(Projections.groupProperty("subsidiaryCode"),"subsidiaryCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ProvisionPostingHist.class));
		
		resultList = criteria.list();
		return resultList!=null?resultList:new ArrayList<ProvisionPostingHist>();
	}
	
	public int delete(String jobNumber, String packageNo, Integer postYr, Integer postMonth) throws DatabaseOperationException {
		List<ProvisionPostingHist> deletingList = getSCDetailProvision(jobNumber, packageNo, postYr, postMonth);
		if (deletingList!=null && deletingList.size()>0){
			for (ProvisionPostingHist delItem:deletingList)
				super.delete(delItem);
			return deletingList.size();
		}
		return 0;
	}
}
