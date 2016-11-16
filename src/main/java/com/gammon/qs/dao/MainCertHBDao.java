package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.config.StoredProcedureConfig;
import com.gammon.pcms.dto.rs.provider.response.maincert.MainCertDashboardDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ContractReceivableWrapper;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
@Repository
public class MainCertHBDao extends BaseHibernateDao<MainCert> {
	@Autowired
	private StoredProcedureConfig storedProcedureConfig;

	public MainCertHBDao() {
		super(MainCert.class);
	}

	
	public MainCert findByJobNoAndCertificateNo(String noJob, Integer noMainCert) throws DataAccessException {
		if (GenericValidator.isBlankOrNull(noJob))
			throw new IllegalArgumentException("JobNo is null or empty");

		Criteria criteria = getSession().createCriteria(this.getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob))
				.add(Restrictions.eq("certificateNumber", noMainCert));

		return (MainCert) criteria.uniqueResult();
	}

	/**
	 * koeyyeung
	 * Created on Nov 2, 2015
	 * Contract Receivable Settlement Report
	 */
	@SuppressWarnings("unchecked")
	public List<ContractReceivableWrapper> obtainContractReceivableList(ContractReceivableWrapper wrapper, List<String> jobNoList) throws DatabaseOperationException {
		
		DetachedCriteria subquery = DetachedCriteria.forClass(JobInfo.class);
		if (StringUtils.isNotBlank(wrapper.getJobNo()))
			subquery.add(Restrictions.ilike("jobNumber", wrapper.getJobNo(), MatchMode.START));
		if (StringUtils.isNotBlank(wrapper.getCompany()))
			subquery.add(Restrictions.ilike("company", wrapper.getCompany(), MatchMode.START));
		
		if(jobNoList!= null && jobNoList.size()>0 && !jobNoList.get(0).equals("JOB_ALL")){
			subquery.add(Restrictions.in("jobNumber", jobNoList));
		}
		if (StringUtils.isNotBlank(wrapper.getDivision()))
			subquery.add(Restrictions.ilike("division", wrapper.getDivision(), MatchMode.START));
		
		
		
		subquery.setProjection(Property.forName("jobNumber"));
		
		Criteria criteria = getSession().createCriteria(ContractReceivableWrapper.class);
		criteria.add(Subqueries.propertyIn("jobNo", subquery));
		criteria.addOrder(Order.asc("division")).addOrder(Order.asc("company")).addOrder(Order.asc("jobNo"));
		
		
		return criteria.list();
	}

	public void saveMainCert(MainCert obj) throws DatabaseOperationException {
		if (findByJobNoAndCertificateNo(obj.getJobNo(), obj.getCertificateNumber()) != null)
			throw new DatabaseOperationException("Main Cert was existed already.");
		super.saveOrUpdate(obj);
	}

	public void updateMainCert(MainCert mainCert) throws DataAccessException {
		if (findByJobNoAndCertificateNo(mainCert.getJobNo(), mainCert.getCertificateNumber()) == null)
			throw new DataRetrievalFailureException("Main Cert does not exist");
		
		super.saveOrUpdate(mainCert);
	}

	public Date getAsAtDate(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber))
			throw new IllegalArgumentException("Job number is null or empty");
		MainCert result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber));
			criteria.add(Restrictions.eq("certificateNumber", mainCertNumber));
			result = (MainCert) criteria.uniqueResult();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}

		return result.getCertAsAtDate();
	}

	/**
	 * @author koeyyeung created on 5th Aug, 2015 
	 * Call Stored Procedure to update Actual Receipt Date of Main Cert from F03B14
	 **/
	public Boolean callStoredProcedureToUpdateMainCert() throws Exception {
		Boolean completed = false;

		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		Session session = sfi.openSession();
		session.beginTransaction();

		Query q = session.createSQLQuery(" { call " + sfi.getSettings().getDefaultSchemaName() + "." + storedProcedureConfig.getStoredProcedureUpdateMainCert() + "}");
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
		completed = true;

		return completed;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - START**************************************************************/
	/**
	 * To find all Main Contract Certificate of a particular job 
	 *
	 * @param noJob
	 * @return
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Jun 27, 2016 11:10:59 AM
	 */
	@SuppressWarnings("unchecked")
	public List<MainCert> findByJobNo(String noJob) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob));
		
		//Order By
		criteria.addOrder(Order.desc("certificateNumber"));

		return criteria.list();
	}
	
	/**
	 * To find all Main Contract Certificate in ascending order
	 * for Rentention Release Schedule
	 * @param noJob
	 * @return
	 * @throws DatabaseOperationException
	 * @author	koeyyeung
	 * @since	Jun 27, 2016 11:10:59 AM
	 */
	@SuppressWarnings("unchecked")
	public List<MainCert> getMainCertList(String noJob) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob));
		
		//Order By
		criteria.addOrder(Order.asc("certificateNumber"));

		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getMainCertNoList(String noJob, String status) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob));
		if(!GenericValidator.isBlankOrNull(status)){
			criteria.add(Restrictions.eq("certificateStatus", status));
		}
				
		//Order By
		criteria.addOrder(Order.desc("certificateNumber"));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("certificateNumber")));
		
		return criteria.list();
	}
	
	
	/**
	 * @author koeyyeung
	 * @since Aug, 28, 2016**/
	public MainCert getLatestMainCert(String noJob, String status) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("jobNo", noJob));
		if(!GenericValidator.isBlankOrNull(status)){
			criteria.add(Restrictions.eq("certificateStatus", status));
		}
		
		//Order By
		criteria.addOrder(Order.desc("certificateNumber"));
		criteria.setMaxResults(1);

		return (MainCert) criteria.uniqueResult();
	}
	
	/**
	 * Main Cert Dashboard IPA
	 * @author koeyyeung
	 * @since Aug, 28, 2016**/
	@SuppressWarnings("unchecked")
	public List<MainCertDashboardDTO> getMonthlySummayIPA(String jobNo, String year) {
		String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
						
	
		List<MainCertDashboardDTO> rsList = new ArrayList<MainCertDashboardDTO>();
		String hql =
				"Select Certmax.Month,"
				+"  (cert2.appmaincontractoramt"
				+"   + cert2.appnscndscamt"
				+"   + cert2.appmosamt" 
				+"   - cert2.appmaincontractorret" 
				+"   + cert2.appmaincontractorretrel" 
				+"   - cert2.appretfornscndsc" 
				+"   + cert2.appretrelfornscndsc" 
				+"   - cert2.appmosret" 
				+"   + cert2.appmosretrel" 
				+"   - cert2.appccamt" 
				+"   + cert2.appadjustmentamt" 
				+"   + Cert2.appadvancepayment"
				+"   + Cert2.Appcpfamt) As Amount"
				+" From "
				+"    (Select Certmonth.Month as month," 
				+"      Max(cert.Certno) as Certno"
				+"    From "
				+"        (Select To_Char(Cert_Issue_Date, 'MM') as month," 
				+"        Max(Cert_Issue_Date) as CertDate"
				+"        From "+schema+".Main_Cert"
				+"        Where Jobno = '"+jobNo+"' And System_Status = 'ACTIVE' and certificateStatus = '300' And Cert_Issue_Date>=to_date('01-01-20"+year+"', 'dd-mm-yyyy') And Cert_Issue_Date <= to_date('31-12-20"+year+"', 'dd-mm-yyyy')"
				+"        Group By To_Char(Cert_Issue_Date, 'MM')) Certmonth"
				+"    Inner Join "+schema+".Main_Cert Cert On Certmonth.Certdate = Cert.Cert_Issue_Date"
				+"    Where Jobno = '"+jobNo+"' And System_Status = 'ACTIVE' Group By Certmonth.Month, Certmonth.Certdate) Certmax"
				+" Inner Join "+schema+".Main_Cert Cert2 On Certmax.Certno = Cert2.Certno"
				+" Where Cert2.Jobno = '"+jobNo+"' And Cert2.System_Status = 'ACTIVE' order by Certmax.month";
				
		
		SQLQuery query = getSession().createSQLQuery(hql)
									.addScalar("month", StringType.INSTANCE)
									.addScalar("amount", BigDecimalType.INSTANCE);
		
		rsList = query.setResultTransformer(new AliasToBeanResultTransformer(MainCertDashboardDTO.class)).list();
		
		return rsList;
	}
	
	
	
	/**
	 * Main Cert Dashboard IPC
	 * @author koeyyeung
	 * @since Aug, 28, 2016**/

	@SuppressWarnings("unchecked")
	public List<MainCertDashboardDTO> getMonthlySummayIPC(String jobNo, String year) {
		String schema =((SessionFactoryImpl)this.getSessionFactory()).getSettings().getDefaultSchemaName();
						
	
		List<MainCertDashboardDTO> rsList = new ArrayList<MainCertDashboardDTO>();
		String hql =
				"Select Certmax.Month,"
				+ " (cert2.Certmaincontractoramt" 
				+ " + cert2.Certnscndscamt" 
				+ " + cert2.Certmosamt" 
				+ " - cert2.Certmaincontractorret" 
				+ " + cert2.Certmaincontractorretrel" 
				+ " - cert2.Certretfornscndsc" 
				+ " + cert2.Certretrelfornscndsc" 
				+ " - cert2.Certmosret" 
				+ " + cert2.Certmosretrel" 
				+ " - cert2.Certccamt"
				+ " + cert2.Certadjustmentamt"
				+ " + Cert2.Certadvancepayment "
				+ " + Cert2.Certcpfamt) As Amount"
				+" From "
				+"    (Select Certmonth.Month as month," 
				+"      Max(cert.Certno) as Certno"
				+"    From "
				+"        (Select To_Char(Cert_Issue_Date, 'MM') as month," 
				+"        Max(Cert_Issue_Date) as CertDate"
				+"        From "+schema+".Main_Cert"
				+"        Where Jobno = '"+jobNo+"' And System_Status = 'ACTIVE' and certificateStatus = '300' And Cert_Issue_Date>=to_date('01-01-20"+year+"', 'dd-mm-yyyy') And Cert_Issue_Date <= to_date('31-12-20"+year+"', 'dd-mm-yyyy')"
				+"        Group By To_Char(Cert_Issue_Date, 'MM')) Certmonth"
				+"    Inner Join "+schema+".Main_Cert Cert On Certmonth.Certdate = Cert.Cert_Issue_Date"
				+"    Where Jobno = '"+jobNo+"' And System_Status = 'ACTIVE' Group By Certmonth.Month, Certmonth.Certdate) Certmax"
				+" Inner Join "+schema+".Main_Cert Cert2 On Certmax.Certno = Cert2.Certno"
				+" Where Cert2.Jobno = '"+jobNo+"' And Cert2.System_Status = 'ACTIVE' order by Certmax.month";
				
		
		SQLQuery query = getSession().createSQLQuery(hql)
									.addScalar("month", StringType.INSTANCE)
									.addScalar("amount", BigDecimalType.INSTANCE);
		
		rsList = query.setResultTransformer(new AliasToBeanResultTransformer(MainCertDashboardDTO.class)).list();
		
		return rsList;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/

}
