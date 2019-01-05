package com.gammon.pcms.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;

import com.gammon.factory.AutowiringSpringBeanJobFactory;
import com.gammon.pcms.scheduler.job.AuditHousekeepJob;
import com.gammon.pcms.scheduler.job.JDEF58001SynchronizationQuartzJob;
import com.gammon.pcms.scheduler.job.JDEF58011SynchronizationJob;
import com.gammon.pcms.scheduler.job.MainCertificateSynchronizationJob;
import com.gammon.pcms.scheduler.job.PackageSnapshotGenerationJob;
import com.gammon.pcms.scheduler.job.PaymentPostingJob;
import com.gammon.pcms.scheduler.job.ProvisionPostingJob;

@Configuration
@PropertySource("${quartz.properties}")
public class QuartzConfig {

	@Value("${org.quartz.scheduler.instanceName}")
	private String schedulerInstance;
	@Value("${org.quartz.scheduler.instanceId}")
	private String orgQuartzSchedulerInstanceId;
	@Value("${org.quartz.scheduler.skipUpdateCheck}")
	private String orgQuartzSchedulerSkipUpdateCheck;
	
	@Value("${org.quartz.threadPool.class}")
	private String orgQuartzThreadPoolClass;
	@Value("${org.quartz.threadPool.threadCount}")
	private String orgQuartzThreadPoolThreadCount;
	@Value("${org.quartz.threadPool.threadPriority}")
	private String orgQuartzThreadPoolThreadPriority;
	
	@Value("${org.quartz.jobStore.isClustered}")
	private String orgQuartzJobStoreIsClustered;
	@Value("${org.quartz.jobStore.useProperties}")
	private String orgQuartzJobStoreUseProperties;
	@Value("${org.quartz.jobStore.clusterCheckinInterval}")
	private String orgQuartzJobStoreClusterCheckinInterval;
	@Value("${org.quartz.jobStore.misfireThreshold}")
	private String orgQuartzJobStoreMisfireThreshold;
	@Value("${org.quartz.jobStore.class}")
	private String orgQuartzJobStoreClass;
	@Value("${org.quartz.jobStore.driverDelegateClass}")
	private String orgQuartzJobStoreDriverDelegateClass;
	
	@Value("${pcms.quartz.timezone}")
	private String pcmsQuartzTimezone;
	
	//-----------------------Properties for Quartz Jobs-----------------------
	//1. Payment Posting
	@Value("${pcms.scheduler.job.description.paymentPosting}")
	private String jobDescriptionPaymentPosting;
	@Value("${pcms.scheduler.job.cronExpression.paymentPosting}")
	private String jobCronExpressionPaymentPosting;
	//2. Provision Posting
	@Value("${pcms.scheduler.job.description.provisionPosting}")
	private String jobDescriptionProvisionPosting;
	@Value("${pcms.scheduler.job.cronExpression.provisionPosting}")
	private String jobCronExpressionProvisionPosting;
	//3. Package Snapshot Generation
	@Value("${pcms.scheduler.job.description.packageSnapshotGeneration}")
	private String jobDescriptionPackageSnapshotGeneration;
	@Value("${pcms.scheduler.job.cronExpression.packageSnapshotGeneration}")
	private String jobCronExpressionPackageSnapshotGeneration;
	//4. JDE F58001 Synchronization
	@Value("${pcms.scheduler.job.description.jdeF58001Synchronization}")
	private String jobDescriptionJDEF58001Synchronization;
	@Value("${pcms.scheduler.job.cronExpression.jdeF58001Synchronization}")
	private String jobCronExpressionJDEF58001Synchronization;
	//5. JDE F58011 Synchronization
	@Value("${pcms.scheduler.job.description.jdeF58011Synchronization}")
	private String jobDescriptionJDEF58011Synchronization;
	@Value("${pcms.scheduler.job.cronExpression.jdeF58011Synchronization}")
	private String jobCronExpressionJDEF58011Synchronization;
	//6. Main Contract Certificate Synchronization
	@Value("${pcms.scheduler.job.description.mainCertificateSynchronization}")
	private String jobDescriptionMainCertificateSynchronization;
	@Value("${pcms.scheduler.job.cronExpression.mainCertificateSynchronization}")
	private String jobCronExpressionMainCertificateSynchronization;	
	//7. Housekeep AUDIT Table
	@Value("${pcms.scheduler.job.description.housekeepAuditTable}")
	private String jobDescriptionHousekeepAuditTable;
	@Value("${pcms.scheduler.job.cronExpression.housekeepAuditTable}")
	private String jobCronExpressionHousekeepAuditTable;
	
	@Value("#{${quartz.setting}}")
	private Map<String, Object> quartzSetting;
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private ApplicationConfig applicationConfig;
	@Autowired
	private Environment env;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	@PostConstruct  
	private void postConstruct() throws NamingException, SchedulerException {
		List<String> profileList = Arrays.asList(env.getActiveProfiles());
		if(!profileList.contains("junit") && new Boolean(getQuartzSetting("AUTOSTARTUP"))) schedulerFactoryBean().start();
	}

	/**
	 * 1a. Payment Posting Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:53:12 PM
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerPaymentPosting() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailPaymentPosting().getObject());
		bean.setCronExpression(jobCronExpressionPaymentPosting);
		bean.setDescription(jobDescriptionPaymentPosting);
		return bean;
	}

	/**
	 * 1b. Payment Posting Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:53:27 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailPaymentPosting() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(PaymentPostingJob.class);
		bean.setDurability(true);
		return bean;
	}
	
	/**
	 * 2a. Provision Posting Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:54:41 PM
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerProvisionPosting() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailProvisionPosting().getObject());
		bean.setCronExpression(jobCronExpressionProvisionPosting);
		bean.setDescription(jobDescriptionProvisionPosting);
		return bean;
	}

	/**
	 * 2b. Provision Posting Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:54:54 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailProvisionPosting() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(ProvisionPostingJob.class);
		bean.setDurability(true);
		return bean;
	}
	
	/**
	 * 3a. Package Snapshot Generation Quartz Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:56:35 PM
	 */
	@Bean 
	public CronTriggerFactoryBean cronTriggerPackageSnapshotGeneration() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailPackageSnapshotGeneration().getObject());
		bean.setCronExpression(jobCronExpressionPackageSnapshotGeneration);
		bean.setDescription(jobDescriptionPackageSnapshotGeneration);
		return bean;
	}
	
	/**
	 * 3b. Package Snapshot Generation Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 1:56:05 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailPackageSnapshotGeneration() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(PackageSnapshotGenerationJob.class);
		bean.setDurability(true);
		return bean;
	}

	/**
	 * 4a. JDE table F58001 synchronization Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:16:13 PM
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerJDEF58001Synchronization() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailJDEF58001Synchronization().getObject());
		bean.setCronExpression(jobCronExpressionJDEF58001Synchronization);
		bean.setDescription(jobDescriptionJDEF58001Synchronization);
		return bean;
	}

	/**
	 * 4b. JDE table F58001 synchronization Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:16:51 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailJDEF58001Synchronization() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(JDEF58001SynchronizationQuartzJob.class);
		bean.setDurability(true);
		return bean;
	}

	/**
	 * 5a. JDE table F58011 synchronization Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:27:35 PM
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerJDEF58011Synchronization() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailJDEF58011Synchronization().getObject());
		bean.setCronExpression(jobCronExpressionJDEF58011Synchronization);
		bean.setDescription(jobDescriptionJDEF58011Synchronization);
		return bean;
	}

	/**
	 * 5b. JDE table F58011 synchronization Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:28:02 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailJDEF58011Synchronization() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(JDEF58011SynchronizationJob.class);
		bean.setDurability(true);
		return bean;
	}

	/**
	 * 6a. Main Contract Certificate synchronization Cron-Expression Trigger
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:36:14 PM
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerMainCertificateSynchronization() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailMainCertificateSynchronization().getObject());
		bean.setCronExpression(jobCronExpressionMainCertificateSynchronization);
		bean.setDescription(jobDescriptionMainCertificateSynchronization);
		return bean;
	}

	/**
	 * 6b. Main Contract Certificate synchronization Quartz Job Detail
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 23, 2016 2:36:51 PM
	 */
	@Bean
	public JobDetailFactoryBean jobDetailMainCertificateSynchronization() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(MainCertificateSynchronizationJob.class);
		bean.setDurability(true);
		return bean;
	}

	/**
	 * 7a. Housekeep Audit table Cron-Expression Trigger
	 */
	@Bean
	public CronTriggerFactoryBean cronTriggerAuditHousekeep() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setStartDelay(10000);
		bean.setTimeZone(TimeZone.getTimeZone(pcmsQuartzTimezone));
		bean.setJobDetail(jobDetailAuditHousekeep().getObject());
		bean.setCronExpression(jobCronExpressionHousekeepAuditTable);
		bean.setDescription(jobDescriptionHousekeepAuditTable);
		return bean;
	}

	/**
	 * 7b. Housekeep Audit table Quartz Job Detail
	 */
	@Bean
	public JobDetailFactoryBean jobDetailAuditHousekeep() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(AuditHousekeepJob.class);
		bean.setDurability(true);
		return bean;
	}
	
	/**
	 * create a bean to create a scheduler
	 *
	 * @return
	 * @author tikywong
	 * @throws NamingException
	 * @since Aug 11, 2015 2:36:34 PM
	 */
	@Profile(value = {"LOC", "DEV", "UAT", "PRO"})
	@Bean(name = "pcmsScheduler", destroyMethod = "destroy")  
	public SchedulerFactoryBean schedulerFactoryBean() throws NamingException {
		SchedulerFactoryBean scheduler;
		// create a scheduler instance
		scheduler = new SchedulerFactoryBean();

		// load properties file and do all the configuration setups
		scheduler.setQuartzProperties(quartzProperties());
		scheduler.setAutoStartup(Boolean.valueOf(getQuartzSetting("AUTOSTARTUP")));

		// database and transaction configurations
		scheduler.setDataSource(dataSource);
		scheduler.setTransactionManager(transactionManager);
		scheduler.setOverwriteExistingJobs(true);

		// for Quartz to use @Autowire
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		scheduler.setJobFactory(jobFactory);

		// setup jobDetails
		scheduler.setJobDetails(jobDetailPackageSnapshotGeneration().getObject(), 
		                        jobDetailPaymentPosting().getObject(),
		                        jobDetailProvisionPosting().getObject(), 
		                        jobDetailJDEF58001Synchronization().getObject(),
		                        jobDetailJDEF58011Synchronization().getObject(), 
		                        jobDetailMainCertificateSynchronization().getObject(),
		                        jobDetailAuditHousekeep().getObject());
		// setup triggers
		scheduler.setTriggers(	cronTriggerPackageSnapshotGeneration().getObject(),
								cronTriggerPaymentPosting().getObject(),
								cronTriggerProvisionPosting().getObject(),
								cronTriggerJDEF58001Synchronization().getObject(),
								cronTriggerJDEF58011Synchronization().getObject(),
								cronTriggerMainCertificateSynchronization().getObject(),
								cronTriggerAuditHousekeep().getObject());
		return scheduler;
	}

	/**
	 * To load all the properties that are set at Quartz.properties
	 *
	 * @return
	 * @author tikywong
	 * @since Aug 11, 2015 3:52:27 PM
	 */
	private Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		String quartzPropertiesPath = applicationConfig.getQuartzProperties();
		if(quartzPropertiesPath.indexOf("classpath:") > -1) {
			quartzPropertiesPath = quartzPropertiesPath.split("classpath:/")[1];
			propertiesFactoryBean.setLocation(new ClassPathResource(quartzPropertiesPath));
		} else {
			quartzPropertiesPath = quartzPropertiesPath.split("file:/")[1];
			propertiesFactoryBean.setLocation(new PathResource(quartzPropertiesPath));
		}
		Properties prop = new Properties();

		try {
			propertiesFactoryBean.afterPropertiesSet();
			prop = propertiesFactoryBean.getObject();
			prop.setProperty("org.quartz.jobStore.tablePrefix", getQuartzSetting("TABLE_PREFIX"));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load quartz.properties", e);
		}

		return prop;
	}

	public String getSchedulerInstance() {
		return schedulerInstance;
	}

	public void setSchedulerInstance(String schedulerInstance) {
		this.schedulerInstance = schedulerInstance;
	}

	public String getOrgQuartzSchedulerInstanceId() {
		return orgQuartzSchedulerInstanceId;
	}

	public void setOrgQuartzSchedulerInstanceId(String orgQuartzSchedulerInstanceId) {
		this.orgQuartzSchedulerInstanceId = orgQuartzSchedulerInstanceId;
	}

	public String getOrgQuartzSchedulerSkipUpdateCheck() {
		return orgQuartzSchedulerSkipUpdateCheck;
	}

	public void setOrgQuartzSchedulerSkipUpdateCheck(String orgQuartzSchedulerSkipUpdateCheck) {
		this.orgQuartzSchedulerSkipUpdateCheck = orgQuartzSchedulerSkipUpdateCheck;
	}

	public String getOrgQuartzThreadPoolClass() {
		return orgQuartzThreadPoolClass;
	}

	public void setOrgQuartzThreadPoolClass(String orgQuartzThreadPoolClass) {
		this.orgQuartzThreadPoolClass = orgQuartzThreadPoolClass;
	}

	public String getOrgQuartzThreadPoolThreadCount() {
		return orgQuartzThreadPoolThreadCount;
	}

	public void setOrgQuartzThreadPoolThreadCount(String orgQuartzThreadPoolThreadCount) {
		this.orgQuartzThreadPoolThreadCount = orgQuartzThreadPoolThreadCount;
	}

	public String getOrgQuartzThreadPoolThreadPriority() {
		return orgQuartzThreadPoolThreadPriority;
	}

	public void setOrgQuartzThreadPoolThreadPriority(String orgQuartzThreadPoolThreadPriority) {
		this.orgQuartzThreadPoolThreadPriority = orgQuartzThreadPoolThreadPriority;
	}

	public String getOrgQuartzJobStoreIsClustered() {
		return orgQuartzJobStoreIsClustered;
	}

	public void setOrgQuartzJobStoreIsClustered(String orgQuartzJobStoreIsClustered) {
		this.orgQuartzJobStoreIsClustered = orgQuartzJobStoreIsClustered;
	}

	public String getOrgQuartzJobStoreUseProperties() {
		return orgQuartzJobStoreUseProperties;
	}

	public void setOrgQuartzJobStoreUseProperties(String orgQuartzJobStoreUseProperties) {
		this.orgQuartzJobStoreUseProperties = orgQuartzJobStoreUseProperties;
	}

	public String getOrgQuartzJobStoreClusterCheckinInterval() {
		return orgQuartzJobStoreClusterCheckinInterval;
	}

	public void setOrgQuartzJobStoreClusterCheckinInterval(String orgQuartzJobStoreClusterCheckinInterval) {
		this.orgQuartzJobStoreClusterCheckinInterval = orgQuartzJobStoreClusterCheckinInterval;
	}

	public String getOrgQuartzJobStoreMisfireThreshold() {
		return orgQuartzJobStoreMisfireThreshold;
	}

	public void setOrgQuartzJobStoreMisfireThreshold(String orgQuartzJobStoreMisfireThreshold) {
		this.orgQuartzJobStoreMisfireThreshold = orgQuartzJobStoreMisfireThreshold;
	}

	public String getOrgQuartzJobStoreClass() {
		return orgQuartzJobStoreClass;
	}

	public void setOrgQuartzJobStoreClass(String orgQuartzJobStoreClass) {
		this.orgQuartzJobStoreClass = orgQuartzJobStoreClass;
	}

	public String getOrgQuartzJobStoreDriverDelegateClass() {
		return orgQuartzJobStoreDriverDelegateClass;
	}

	public void setOrgQuartzJobStoreDriverDelegateClass(String orgQuartzJobStoreDriverDelegateClass) {
		this.orgQuartzJobStoreDriverDelegateClass = orgQuartzJobStoreDriverDelegateClass;
	}

	public String getPcmsQuartzTimezone() {
		return pcmsQuartzTimezone;
	}

	public void setPcmsQuartzTimezone(String pcmsQuartzTimezone) {
		this.pcmsQuartzTimezone = pcmsQuartzTimezone;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public String getJobDescriptionPaymentPosting() {
		return jobDescriptionPaymentPosting;
	}

	public void setJobDescriptionPaymentPosting(String jobDescriptionPaymentPosting) {
		this.jobDescriptionPaymentPosting = jobDescriptionPaymentPosting;
	}

	public String getJobCronExpressionPaymentPosting() {
		return jobCronExpressionPaymentPosting;
	}

	public void setJobCronExpressionPaymentPosting(String jobCronExpressionPaymentPosting) {
		this.jobCronExpressionPaymentPosting = jobCronExpressionPaymentPosting;
	}

	public String getJobDescriptionProvisionPosting() {
		return jobDescriptionProvisionPosting;
	}

	public void setJobDescriptionProvisionPosting(String jobDescriptionProvisionPosting) {
		this.jobDescriptionProvisionPosting = jobDescriptionProvisionPosting;
	}

	public String getJobCronExpressionProvisionPosting() {
		return jobCronExpressionProvisionPosting;
	}

	public void setJobCronExpressionProvisionPosting(String jobCronExpressionProvisionPosting) {
		this.jobCronExpressionProvisionPosting = jobCronExpressionProvisionPosting;
	}

	public String getJobDescriptionPackageSnapshotGeneration() {
		return jobDescriptionPackageSnapshotGeneration;
	}

	public void setJobDescriptionPackageSnapshotGeneration(String jobDescriptionPackageSnapshotGeneration) {
		this.jobDescriptionPackageSnapshotGeneration = jobDescriptionPackageSnapshotGeneration;
	}

	public String getJobCronExpressionPackageSnapshotGeneration() {
		return jobCronExpressionPackageSnapshotGeneration;
	}

	public void setJobCronExpressionPackageSnapshotGeneration(String jobCronExpressionPackageSnapshotGeneration) {
		this.jobCronExpressionPackageSnapshotGeneration = jobCronExpressionPackageSnapshotGeneration;
	}

	public String getJobDescriptionJDEF58001Synchronization() {
		return jobDescriptionJDEF58001Synchronization;
	}

	public void setJobDescriptionJDEF58001Synchronization(String jobDescriptionJDEF58001Synchronization) {
		this.jobDescriptionJDEF58001Synchronization = jobDescriptionJDEF58001Synchronization;
	}

	public String getJobCronExpressionJDEF58001Synchronization() {
		return jobCronExpressionJDEF58001Synchronization;
	}

	public void setJobCronExpressionJDEF58001Synchronization(String jobCronExpressionJDEF58001Synchronization) {
		this.jobCronExpressionJDEF58001Synchronization = jobCronExpressionJDEF58001Synchronization;
	}

	public String getJobDescriptionJDEF58011Synchronization() {
		return jobDescriptionJDEF58011Synchronization;
	}

	public void setJobDescriptionJDEF58011Synchronization(String jobDescriptionJDEF58011Synchronization) {
		this.jobDescriptionJDEF58011Synchronization = jobDescriptionJDEF58011Synchronization;
	}

	public String getJobCronExpressionJDEF58011Synchronization() {
		return jobCronExpressionJDEF58011Synchronization;
	}

	public void setJobCronExpressionJDEF58011Synchronization(String jobCronExpressionJDEF58011Synchronization) {
		this.jobCronExpressionJDEF58011Synchronization = jobCronExpressionJDEF58011Synchronization;
	}

	public String getJobDescriptionMainCertificateSynchronization() {
		return jobDescriptionMainCertificateSynchronization;
	}

	public void setJobDescriptionMainCertificateSynchronization(String jobDescriptionMainCertificateSynchronization) {
		this.jobDescriptionMainCertificateSynchronization = jobDescriptionMainCertificateSynchronization;
	}

	public String getJobCronExpressionMainCertificateSynchronization() {
		return jobCronExpressionMainCertificateSynchronization;
	}

	public void setJobCronExpressionMainCertificateSynchronization(String jobCronExpressionMainCertificateSynchronization) {
		this.jobCronExpressionMainCertificateSynchronization = jobCronExpressionMainCertificateSynchronization;
	}

	public String getJobDescriptionHousekeepAuditTable() {
		return jobDescriptionHousekeepAuditTable;
	}

	public String getJobCronExpressionHousekeepAuditTable() {
		return jobCronExpressionHousekeepAuditTable;
	}
	
	public void prepareQuartzUser(){
		Authentication quartzUser = new UsernamePasswordAuthenticationToken(webServiceConfig.getPcmsApi("USERNAME"), null, null);
		SecurityContextHolder.getContext().setAuthentication(quartzUser);
	}

	/**
	 * @return the quartzSetting
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getQuartzSetting() {
		return (Map<String, String>) quartzSetting.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getQuartzSetting(String key){
		return getQuartzSetting().get(key);
	}
}
