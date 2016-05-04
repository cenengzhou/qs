package com.gammon.pcms.config;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.UserDetailsServiceLdapAuthoritiesPopulator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.gammon.pcms.web.filter.HeaderAuthenticationFilter;
import com.gammon.pcms.web.security.handler.KerberosLoginFailureHandler;
import com.gammon.pcms.web.security.handler.LoginFailureHandler;
import com.gammon.pcms.web.security.handler.LoginSuccessHandler;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.LdapUserDetailsContextMapper;
import com.gammon.qs.service.security.UserDetailsServiceImpl;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@PropertySource("file:${security.properties}")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// Baisc auth setting for web service can be found at web.xml and weblogic.xml
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private LdapConfig ldapConfig;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	@Value("${spring.security.debug}")
	private String springSecurityDebug;
	@Value("${loginPath}")
	private String loginPath;

	// Role
	@Value("${role.pcms-ws}")
	private String rolePcmsWs;
	@Value("${role.pcms-api}")
	private String rolePcmsApi;
	@Value("${role.qs-qs}")
	private String roleQsQs;
	@Value("${role.qs-enquiry}")
	private String roleQsEnquiry;
	@Value("${role.qs-approver}")
	private String roleQsApprover;
	@Value("${role.qs-admin}")
	private String roleQsAdmin;
	
	// Kerberos
	@Value("${kerberos.service-principal}")
	private String kerberosServicePrincipal;
	@Value("${kerberos.keytab-location}")
	private String kerberosKeytabLocation;
	@Value("${kerberos.debug}")
	boolean kerberosDebug;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.authenticationProvider(formAuthenticationProvider())
		.authenticationProvider(kerberosTicketAuthenticationProvider())
		.inMemoryAuthentication().withUser(webServiceConfig.getQsWsUsername()).password(webServiceConfig.getQsWsPassword()).authorities("ROLE_"+rolePcmsWs);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/spring-ws/*", 
				"/resources/**",
				"/gammonqs/systemMessage.smvc", 
				"/gammonqs/uiErrorMessageLog.smvc",
				"/badCredentials.jsp",
				"/favicon.ico",
				"/403.html",
				"/login.htm*"
				);
		web.debug(StringUtils.isEmpty(springSecurityDebug)?false:new Boolean(springSecurityDebug));
	}


	/**
	 * For Web Service Login
	 */
	@Configuration
	@Order(1)
	public static class RsSecurityConfig extends WebSecurityConfigurerAdapter {
		// Role
		@Value("${role.pcms-ws}")
		private String rolePcmsWs;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.headers()
			.cacheControl().and()
			.frameOptions().sameOrigin().and()
			.csrf().disable()
			.antMatcher("/ws/**").authorizeRequests()
			.anyRequest().hasAnyRole(rolePcmsWs).and()
			.exceptionHandling().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.httpBasic().and()
			.addFilterBefore(new HeaderAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
		}
	}
	
	/**
	 * SSO and fallback to Form Login with Username and Password
	 */
	@EnableWebSecurity
	@Configuration
	@Order(2)
	public static class KerberosSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter;
		@Autowired
		private AuthenticationSuccessHandler loginSuccessHandler;
		@Autowired
		private AuthenticationFailureHandler loginFailureHandler;
		@Autowired
		private SecurityConfig securityConfig;
		
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.headers()
			.cacheControl().and()
			.frameOptions().sameOrigin().and()
			.csrf().disable()
			.sessionManagement()
			.sessionFixation().migrateSession().and()
			.authorizeRequests()
			.antMatchers("/**").hasAnyRole(securityConfig.getRoleQsQs(), securityConfig.getRoleQsEnquiry(), securityConfig.getRoleQsApprover(), securityConfig.getRoleQsAdmin()).and()
			.exceptionHandling()
			.authenticationEntryPoint(new SpnegoEntryPoint(securityConfig.getLoginPath())).and()
			.formLogin()
			.loginPage(securityConfig.getLoginPath()).permitAll()
			.usernameParameter("username")
			.passwordParameter("password")
			.loginProcessingUrl("/formlogin").permitAll()
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailureHandler).and()
			.logout().permitAll()
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.logoutSuccessUrl(securityConfig.getLoginPath()).and()
			.exceptionHandling().accessDeniedPage("/403.html").and()
			.addFilterAfter(spnegoAuthenticationProcessingFilter, BasicAuthenticationFilter.class);
		}
		
		/**
		 * for weblogic publishing which classloader does not set
		 * objectPostProcessor, context and authenticationConfiguration and cause NPE
		 * (no need when use weblogic maven plugin)
		 */
		@Override
		@Autowired
		public void setObjectPostProcessor(ObjectPostProcessor<Object> objectPostProcessor) {
			super.setObjectPostProcessor(objectPostProcessor);
		}

		@Override
		@Autowired
		public void setApplicationContext(ApplicationContext context) {
			super.setApplicationContext(context);
		}

		@Override
		@Autowired
		public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
			super.setAuthenticationConfiguration(authenticationConfiguration);
		}
	}

	@Bean
	public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter() throws Exception {
		SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
		filter.setFailureHandler(kerberosLoginFailureHandler());
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}
	
	@Bean
	public AuthenticationFailureHandler kerberosLoginFailureHandler() {
		return new KerberosLoginFailureHandler(loginPath);
	}
	
	@Bean(name = "formAuthenticationProvider")
	public LdapAuthenticationProvider formAuthenticationProvider() {
		LdapAuthenticationProvider bean = new LdapAuthenticationProvider(bindAuthenticator(), userDetailsServiceLdapAuthoritiesPopulator());
		bean.setUserDetailsContextMapper(userDetailsContextMapper());
		return bean;
	}

	@Bean(name = "bindAuthernticator")
	public BindAuthenticator bindAuthenticator() {
		BindAuthenticator bean = new BindAuthenticator(authenticationLdapContextSource());
		bean.setUserSearch(userSearchBean());
		return bean;
	}
	
	@Bean(name = "userSearchBean")
	public FilterBasedLdapUserSearch userSearchBean() {
		FilterBasedLdapUserSearch bean = new FilterBasedLdapUserSearch("", "(sAMAccountName={0})", authenticationLdapContextSource());
		return bean;
	}

	@Bean(name = "authenticationLdapContextSource")
	public DefaultSpringSecurityContextSource authenticationLdapContextSource() {
		DefaultSpringSecurityContextSource bean = new DefaultSpringSecurityContextSource(
				ldapConfig.getLdapServerUrl() + "/" + ldapConfig.getLdapServerBase());
		bean.setUserDn(ldapConfig.getLdapServerUsername());
		bean.setPassword(ldapConfig.getLdapServerPassword());
		return bean;
	}

	@Bean(name = "userDetailsServiceLdapAuthoritiesPopulator")
	public UserDetailsServiceLdapAuthoritiesPopulator userDetailsServiceLdapAuthoritiesPopulator() {
		UserDetailsServiceLdapAuthoritiesPopulator bean = new UserDetailsServiceLdapAuthoritiesPopulator(userDetailsService());
		return bean;
	}
	
	@Bean(name = "userDetailsService")
	public UserDetailsServiceImpl userDetailsService() {
		UserDetailsServiceImpl bean = new UserDetailsServiceImpl();
		return bean;
	}

	@Bean(name = "userDetailsContextMapper")
	public LdapUserDetailsContextMapper userDetailsContextMapper() {
		LdapUserDetailsContextMapper bean = new LdapUserDetailsContextMapper();
		bean.setAdminService(adminService);
		return bean;
	}
	
	@Bean(name = "ldapContextSource")
	public LdapContextSource ldapContextSource() {
		LdapContextSource bean = new LdapContextSource();
		bean.setUrl(ldapConfig.getLdapServerUrl());
		bean.setBase(ldapConfig.getLdapServerBase());
		bean.setUserDn(ldapConfig.getLdapServerUsername());
		bean.setPassword(ldapConfig.getLdapServerPassword());
		return bean;
	}

	@Bean(name = "ldapTemplate")
	public LdapTemplate ldapTemplate(ContextSource ldapContextSource) {
		LdapTemplate bean = new LdapTemplate(ldapContextSource);
		return bean;
	}

	@Bean
	public KerberosServiceAuthenticationProvider kerberosTicketAuthenticationProvider() {
		KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
		provider.setTicketValidator(sunJaasKerberosTicketValidator());
		provider.setUserDetailsService(userDetailsService());
		return provider;
	}

	@Bean
	public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
		SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
		ticketValidator.setServicePrincipal(kerberosServicePrincipal);
		ticketValidator.setKeyTabLocation(new FileSystemResource(kerberosKeytabLocation));
		ticketValidator.setDebug(kerberosDebug);
		return ticketValidator;
	}
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(servletContext.getContextPath()+"/index.htm", false);
	}

	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new LoginFailureHandler(loginPath);
	}

	/**
	 * to keep Spring Security updated about session lifecycle events
	 * @return
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher(){
		HttpSessionEventPublisher bean = new HttpSessionEventPublisher();
		return bean;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getSpringSecurityDebug() {
		return springSecurityDebug;
	}

	public String getLoginPath() {
		return loginPath;
	}

	public String getRolePcmsWs() {
		return rolePcmsWs;
	}

	public String getRolePcmsApi() {
		return rolePcmsApi;
	}

	public String getRoleQsQs() {
		return roleQsQs;
	}

	public String getRoleQsEnquiry() {
		return roleQsEnquiry;
	}

	public String getRoleQsApprover() {
		return roleQsApprover;
	}

	public String getRoleQsAdmin() {
		return roleQsAdmin;
	}

	public String getKerberosServicePrincipal() {
		return kerberosServicePrincipal;
	}

	public String getKerberosKeytabLocation() {
		return kerberosKeytabLocation;
	}

	public boolean isKerberosDebug() {
		return kerberosDebug;
	}
}
