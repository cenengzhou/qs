package com.gammon.qs.dao.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.Authority;
import com.gammon.qs.application.JobSecurity;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.domain.Job;
@Repository
public class JobSecurityHBDao extends BaseHibernateDao<JobSecurity> {

	private Logger logger = Logger.getLogger(JobSecurityHBDao.class.getName());

	@Autowired
	public UserHBDao userDao;

	public JobSecurityHBDao() {
		super(JobSecurity.class);
	}

	public List<JobSecurity> getJobSecurityByJob(Job job){
		if (job!=null)
			return getJobSecurityByJob(job.getJobNumber());
		else 
			throw new NullPointerException("Job is null");
	}
	
	@SuppressWarnings("unchecked")
	public List<JobSecurity> getJobSecurityByJob(String jobNumber){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job_No", jobNumber.trim()));
		return (List<JobSecurity>) criteria.list();
	}
	
	public List<JobSecurity> getJobSecurityByUser(User user){
		return getJobSecurityByUser(user.getUsername());
	}

	@SuppressWarnings("unchecked")
	public List<JobSecurity> getJobSecurityByUser(String username) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("user", "user");
		criteria.add(Restrictions.eq("user.username", username.trim()));
		return (List<JobSecurity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<JobSecurity> getJobSecurityByRoleName(String roleName) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("roleName", roleName.trim()));
		return ((List<JobSecurity>) criteria.list());
	}
	public JobSecurity getJobSecurity(JobSecurity jobSecurity){
		String userName = null;
		if (jobSecurity.getUser()!=null)
			userName= jobSecurity.getUser().getUsername();
		return getJobSecurity(userName,
							  jobSecurity.getRoleName(),
							  jobSecurity.getCompany(),
							  jobSecurity.getDivision(),
							  jobSecurity.getDepartment(),
							  jobSecurity.getJob_No());
	}
	
	//added Rolename for JobSecurity searching (Kafu Wong March 17, 2010)
	public JobSecurity getJobSecurity(String username, String roleName, String company,
			String division, String department, String jobNumber) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.createAlias("user", "user");
		if (username==null||username.trim().length()<1)
			criteria.add(Restrictions.isNull("user.username"));
		else	
			criteria.add(Restrictions.eq("user.username", username.trim()));
		if (roleName==null||roleName.trim().length()<1)
			criteria.add(Restrictions.isNull("roleName"));
		else
			criteria.add(Restrictions.eq("roleName", roleName.trim()));
		if (jobNumber==null||jobNumber.trim().length()<1)
			criteria.add(Restrictions.isNull("job_No"));
		else
			criteria.add(Restrictions.eq("job_No", jobNumber.trim()));
		if (company==null||company.trim().length()<1)
			criteria.add(Restrictions.isNull("company"));
		else
			criteria.add(Restrictions.eq("company", company));
		if (division==null||division.trim().length()<1)
			criteria.add(Restrictions.isNull("division"));
		else
			criteria.add(Restrictions.eq("division", division));
		if (department==null||department.trim().length()<1)
			criteria.add(Restrictions.isNull("department"));
		else
			criteria.add(Restrictions.eq("department", department));
		return (JobSecurity)criteria.uniqueResult();
	}

	/**
	 * @author tikywong
	 * @Date 16-10-2013
	 */
	@SuppressWarnings("unchecked")
	public List<JobSecurity> obtainJobSecurityList(String username) throws DatabaseOperationException {
		List<JobSecurity> jobSecurityList = new ArrayList<JobSecurity>();
	
		if(!GenericValidator.isBlankOrNull(username)){
			User user = userDao.getByUsername(username);
			Set<Authority> authorities = user.getAuthorities();
			
			String message = "";
			for(Authority authority: authorities){
				Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
				
				String role_JOB = authority.getName();
				if (!GenericValidator.isBlankOrNull(role_JOB) && role_JOB.startsWith("JOB")){
					criteria.add(Restrictions.eq("roleName",authority.getName()));
					message += "ROLE: "+authority.getName()+" COMPANY: [";
					
					List<JobSecurity> sList = (List<JobSecurity>)criteria.list();
					for(JobSecurity s:sList){
						if(!jobSecurityList.contains(s)){
							jobSecurityList.add(s);
							message += " "+s.getCompany()+";";
						}
					}
					message+="]";
					logger.info(message);
				}
			}
		}
		
		return jobSecurityList;
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkJobSecurity(String username, String company, String division, String department, String jobNumber){
//		logger.info("Username:"+username+"\tCompany:"+company+"\tDivision:"+division+"\tDepartment:"+department+"\tJobNumber:"+jobNumber);
		// Add by Vincent Mok (25/03/2010) for Job Security by Role
		try {
			User user = userDao.getByUsername(username);
			Set<Authority> authorities = user.getAuthorities();
			List<String> rolelist = new ArrayList<String>();

			for(Authority authority: authorities){
				//logger.info(authority.getName());
				rolelist.add(authority.getName());
			}
			
			//Job Security by Role (JOB_ALL, JOB_HKG, etc...)
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.in("roleName", rolelist.toArray()));
			criteria.add(Restrictions.in("job_No", new Object[]{jobNumber.trim(),"NA"}));
			criteria.add(Restrictions.in("company", new Object[]{company,"NA"}));
			criteria.add(Restrictions.in("division", new Object[]{division,"NA"}));
			criteria.add(Restrictions.in("department", new Object[]{department,"NA"}));

			List<JobSecurity> resultList = criteria.list();
			if (resultList!=null && resultList.size()>0)
				return true;
			
			//Job Security by User
			Criteria criteria2 = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria2.createAlias("user", "user");
			criteria2.add(Restrictions.eq("user.username", username.trim()));
			criteria2.add(Restrictions.in("job_No", new Object[]{jobNumber.trim(),"NA"}));
			criteria2.add(Restrictions.in("company", new Object[]{company,"NA"}));
			criteria2.add(Restrictions.in("division", new Object[]{division,"NA"}));
			criteria2.add(Restrictions.in("department", new Object[]{department,"NA"}));

			List<JobSecurity> resultList2 = criteria2.list();
			if (resultList2!=null && resultList2.size()>0)
				return true;

			return false;

		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createJobSecurity(JobSecurity jobSecurity) throws DatabaseOperationException{
		if (getJobSecurity(jobSecurity)==null){
			
			jobSecurity.setCreatedDate(new Date());
			jobSecurity.setJob_No(jobSecurity.getJob_No().trim());
			saveOrUpdate(jobSecurity);
			return true;
		}else
			throw new DatabaseOperationException("Job Security existed! No duplicate record can be saved");
	}
}
