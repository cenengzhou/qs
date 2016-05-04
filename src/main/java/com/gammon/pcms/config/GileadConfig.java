package com.gammon.pcms.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateful.HttpSessionProxyStore;

@Configuration
public class GileadConfig {
	
	@Bean
	public HibernateUtil persistenceUtil(SessionFactory sessionFactory) {
		HibernateUtil bean = new HibernateUtil();
		bean.setSessionFactory(sessionFactory);
		return bean;
	}

	@Bean
	public HttpSessionProxyStore proxyStore(HibernateUtil persistenceUtil) {
		HttpSessionProxyStore bean = new HttpSessionProxyStore();
		bean.setPersistenceUtil(persistenceUtil);
		return bean;
	}

	@Bean
	public PersistentBeanManager persistentBeanManager(HttpSessionProxyStore proxyStore,
			HibernateUtil persistenceUtil) {
		PersistentBeanManager bean = new PersistentBeanManager();
		bean.setProxyStore(proxyStore);
		bean.setPersistenceUtil(persistenceUtil);
		return bean;
	}
}
