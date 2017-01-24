/**
 * APM-LOC
 * com.gammon.factory
 * AutowiringSpringBeanJobFactory.java
 * @since Aug 12, 2015 11:59:34 AM
 * @author tikywong
 */
package com.gammon.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
	private transient AutowireCapableBeanFactory beanFactory;

	public AutowiringSpringBeanJobFactory() {}

	/**
	 *
	 * @param applicationContext
	 * @throws BeansException
	 * @author tikywong
	 * @since Aug 12, 2015 11:59:34 AM
	 */
	@Override
	public void setApplicationContext(	ApplicationContext applicationContext) throws BeansException {
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

	/**
	 * 
	 *
	 * @param bundle
	 * @return
	 * @throws Exception
	 * @author tikywong
	 * @since Aug 12, 2015 12:01:23 PM
	 */
	@Override
	protected Object createJobInstance(	TriggerFiredBundle bundle) throws Exception {
		final Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}

}
