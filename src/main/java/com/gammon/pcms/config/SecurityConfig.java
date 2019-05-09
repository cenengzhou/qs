package com.gammon.pcms.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import org.springframework.security.web.session.InvalidSessionStrategy;

import com.gammon.pcms.web.filter.HeaderAuthenticationFilter;
import com.gammon.pcms.web.security.handler.KerberosLoginFailureHandler;
import com.gammon.pcms.web.security.handler.LoginFailureHandler;
import com.gammon.pcms.web.security.handler.LoginSuccessHandler;
import com.gammon.qs.service.security.LdapUserDetailsContextMapper;
import com.gammon.qs.service.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@PropertySource("${security.properties}")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LdapConfig ldapConfig;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Value("${spring.security.debug}")
	private String springSecurityDebug;
	@Value("${loginPath}")
	private String loginPath;

	// Role
	@Value("#{${pcms.role}}")
	private Map<String, String> pcmsRole;
	@Value("#{${pcms.job}}")
	private Map<String, String> pcmsJob;
	@Value("#{${function.security.methods}}")
	private Map<String, Map<String, String>> fnMethods;
	
	// Kerberos
	@Value("#{${kerberos.service-principal}}")
	private Map<String, String> kerberosServicePrincipal;
	@Value("${kerberos.keytab-location}")
	private String kerberosKeytabLocation;
	@Value("${kerberos.debug}")
	boolean kerberosDebug;
	@Value("${defaultMaxInactiveInterval}")
	private String defaultMaxInactiveInterval;
	
	//cache
	@Value("#{${cacheKey.prefix}}")
	private Map<String, String> cacheKeyPrefix;
	
	public static final String FN_ENABLE = "Enable";
	public static final String FN_DISABLE = "Disable";
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.authenticationProvider(ldapAuthenticationProvider())
		.authenticationProvider(kerberosServiceAuthenticationProvider())
		.inMemoryAuthentication().withUser(webServiceConfig.getPcmsApi("USERNAME")).password(webServiceConfig.getPcmsApi("PASSWORD")).authorities("ROLE_"+getRolePcmsWs());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers();
		web.debug(StringUtils.isEmpty(springSecurityDebug)?false:new Boolean(springSecurityDebug));
	}

	/**
	 * For Web Service Login
	 */
	@Configuration
	@Order(1)
	public static class RsSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityConfig securityConfig;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.headers()
			.cacheControl().and()
			.frameOptions().sameOrigin().and()
			.csrf().disable()
			.antMatcher("/ws/**").authorizeRequests()
			.anyRequest().hasAnyRole(securityConfig.getRolePcmsWs()).and()
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
		private ServletContext servletContext;
		@Autowired
		private SecurityConfig securityConfig;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.headers()
			.cacheControl().and()
			.frameOptions().sameOrigin().and()
			.csrf().disable()
			.sessionManagement().invalidSessionStrategy(invalidSessionStrategy())
			.enableSessionUrlRewriting(false)
			.sessionAuthenticationErrorUrl(securityConfig.getLoginPath() + "?UnAuthorized")
			.invalidSessionUrl(securityConfig.getLoginPath() + "?InvalidSession")
			.maximumSessions(-1)
			.expiredUrl(securityConfig.getLoginPath() + "?SessionExpired")
			.sessionRegistry(securityConfig.sessionRegistry()).and()
			.sessionFixation().migrateSession().and()
			.authorizeRequests()
			.antMatchers(
					"/admintest/**",
					"/spring-ws/*", 
					"/resources/**",
					"/plugins/**",
					"/css/**",
					"/image/**",
					"/badCredentials.jsp",
					"/index.html",
//					"/service/GetCurrentSessionId",
					"/service/ValidateCurrentSession",
//					"/js/**/*.js",
//					"/view/**",
					"/favicon.ico",
					"/403.html",
					"/503.html",
					"/login.htm*",
					"/logout.htm*"
					).permitAll()
			.antMatchers("/swagger-ui.html*")
			.hasAnyRole(securityConfig.getRolePcmsImsEnq())
			.antMatchers("/**/*")
			.hasAnyRole(
					securityConfig.getPcmsRole("MAINTENANCE"),
					securityConfig.getRolePcmsEnq(),
					securityConfig.getRolePcmsImsAdmin(),
					securityConfig.getRolePcmsImsEnq(),
					securityConfig.getRolePcmsQs(),
					securityConfig.getRolePcmsQsAdmin(),
					securityConfig.getRolePcmsQsReviewer(),
					securityConfig.getRolePcmsQsDloa(),
					securityConfig.getRolePcmsQsSiteAdm(),
					securityConfig.getRolePcmsQsDoc()
					).and()
			.exceptionHandling()
			.authenticationEntryPoint(new SpnegoEntryPoint(securityConfig.getLoginPath())).and()
			.formLogin()
			.loginPage(securityConfig.getLoginPath()).permitAll()
			.usernameParameter("username")
			.passwordParameter("password")
			.loginProcessingUrl("/formlogin").permitAll()
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailureHandler()).and()
			.logout().permitAll()
			.invalidateHttpSession(true)
			.deleteCookies("PCMS_SESSIONID")
			.logoutSuccessUrl(securityConfig.getLoginPath() + "?logout").and()
			.exceptionHandling().accessDeniedPage("/403.html").and()
			.addFilterAfter(spnegoAuthenticationProcessingFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);
		}

		@Bean InvalidSessionStrategy invalidSessionStrategy(){
			InvalidSessionStrategy bean = new InvalidSessionStrategy(){
				@Override
				public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
						throws IOException, ServletException {
					response.addHeader("WWW-Authenticate", "Negotiate");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}};
			return bean;
		}
		@Bean
		public AuthenticationSuccessHandler loginSuccessHandler() {
			return new LoginSuccessHandler(servletContext.getContextPath()+"/home.html", false);
		}

		@Bean
		public AuthenticationFailureHandler loginFailureHandler() {
			return new LoginFailureHandler(securityConfig.loginPath);
		}

		@Bean
		public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(AuthenticationManager authenticationManager) throws Exception {
			SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
			filter.setFailureHandler(kerberosLoginFailureHandler());
			filter.setAuthenticationManager(authenticationManager);
			return filter;
		}
		
		@Bean
		public AuthenticationFailureHandler kerberosLoginFailureHandler() {
			return new KerberosLoginFailureHandler(securityConfig.loginPath);
		}
	}	
	
	//ldapAuthenticationProvider
	@Bean(name = "ldapAuthenticationProvider")
	public LdapAuthenticationProvider ldapAuthenticationProvider() {
		LdapAuthenticationProvider bean = new LdapAuthenticationProvider(bindAuthenticator(), userDetailsServiceLdapAuthoritiesPopulator());
		bean.setUserDetailsContextMapper(userDetailsContextMapper());
		return bean;
	}

	@Bean(name = "userDetailsContextMapper")
	public LdapUserDetailsContextMapper userDetailsContextMapper() {
		LdapUserDetailsContextMapper bean = new LdapUserDetailsContextMapper();
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
				ldapConfig.getLdapServer("URL") + "/" + ldapConfig.getLdapServer("BASE"));
		bean.setUserDn(ldapConfig.getLdapServer("USERNAME"));
		bean.setPassword(ldapConfig.getLdapServer("PASSWORD"));
		return bean;
	}

	@Bean(name = "userDetailsServiceLdapAuthoritiesPopulator")
	public UserDetailsServiceLdapAuthoritiesPopulator userDetailsServiceLdapAuthoritiesPopulator() {
		UserDetailsServiceLdapAuthoritiesPopulator bean = new UserDetailsServiceLdapAuthoritiesPopulator(userDetailsService());
		return bean;
	}
    
	@Bean(name = "userDetailsService")
	public UserDetailsService userDetailsService() {
		UserDetailsServiceImpl bean = new UserDetailsServiceImpl();
		return bean;
	}
	
	//kerberosServiceAuthenticationProvider
	@Bean
	public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
		KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
		provider.setTicketValidator(sunJaasKerberosTicketValidator());
		provider.setUserDetailsService(userDetailsService());
		return provider;
	}

	@Bean
	public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
		SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
		ticketValidator.setServicePrincipal(getKerberosServicePrincipal());
		String keyTabPath = getKerberosKeytabLocation().replace("file:/", ""); 
		LoggerFactory.getLogger(getClass()).info(keyTabPath);
		ticketValidator.setKeyTabLocation(new FileSystemResource(keyTabPath));
		ticketValidator.setDebug(getKerberosDebug());
		return ticketValidator;
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

	@Bean 
	public SessionRegistry sessionRegistry(){
		SessionRegistry bean = new SessionRegistryImpl();
		return bean;
	}
	
	@Bean
	public LdapTemplate ldapTemplate(ContextSource ldapContextSource) {
		LdapTemplate bean = new LdapTemplate(ldapContextSource);
		return bean;
	}

	@Bean(name = "ldapContextSource")
	public LdapContextSource ldapContextSource() {
		LdapContextSource bean = new LdapContextSource();
		bean.setUrl(ldapConfig.getLdapServer("URL"));
		bean.setBase(ldapConfig.getLdapServer("BASE"));
		bean.setUserDn(ldapConfig.getLdapServer("USERNAME"));
		bean.setPassword(ldapConfig.getLdapServer("PASSWORD"));
		return bean;
	}

	//getter
	public boolean getKerberosDebug() {
		return kerberosDebug;
	}

	public String getSpringSecurityDebug() {
		return springSecurityDebug;
	}

	public String getLoginPath() {
		return loginPath;
	}

	public String getRolePcmsWs() {
		return getPcmsRole("WS");
	}


	public String getKerberosServicePrincipal() {
		return kerberosServicePrincipal.get(applicationConfig.getDeployEnvironment());
	}

	public String getKerberosKeytabLocation() {
		return kerberosKeytabLocation;
	}

	public boolean isKerberosDebug() {
		return kerberosDebug;
	}

	/**
	 * @return the defaultMaxInactiveInterval
	 */
	public String getDefaultMaxInactiveInterval() {
		return defaultMaxInactiveInterval;
	}

	/**
	 * @return the rolePcmsEnq
	 */
	public String getRolePcmsEnq() {
		return getPcmsRole("ENQ");
	}

	/**
	 * @return the rolePcmsImsEnq
	 */
	public String getRolePcmsImsEnq() {
		return getPcmsRole("IMS_ENQ");
	}

	/**
	 * @return the rolePcmsImsAdmin
	 */
	public String getRolePcmsImsAdmin() {
		return getPcmsRole("IMS_ADMIN");
	}

	/**
	 * @return the rolePcmsQs
	 */
	public String getRolePcmsQs() {
		return getPcmsRole("QS");
	}
	
	/**
	 * @return the rolePcmsQsDoc
	 */
	public String getRolePcmsQsDoc() {
		return getPcmsRole("QS_DOC");
	}
	
	public String getRolePcmsQsDloa() {
		return getPcmsRole("QS_DLOA");
	}
	
	public String getRolePcmsQsSiteAdm() {
		return getPcmsRole("QS_SITE_ADM");
	}

	/**
	 * @return the rolePcmsQsAdmin
	 */
	public String getRolePcmsQsAdmin() {
		return getPcmsRole("QS_ADMIN");
	}

	/**
	 * @return the rolePcmsQsReviewer
	 */
	public String getRolePcmsQsReviewer() {
		return getPcmsRole("QS_REVIEWER");
	}

	/**
	 * @return the rolePcmsJobAll
	 */
	public String getRolePcmsJobAll() {
		return getPcmsJob("ALL");
	}

	/**
	 * @return the pcmsRole
	 */
	public Map<String, String> getPcmsRole() {
		return pcmsRole;
	}

	public String getPcmsRole(String key){
		return getPcmsRole().get(key);
	}
	
	/**
	 * @return the pcmsJob
	 */
	public Map<String, String> getPcmsJob() {
		return pcmsJob;
	}
	
	public String getPcmsJob(String key){
		return getPcmsJob().get(key);
	}

	/**
	 * @return the cacheKeyPrefix
	 */
	public Map<String, String> getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}
	
	public String getCacheKeyPrefix(String key){
		return getCacheKeyPrefix().get(key);
	}

	/**
	 * @return the fnMethods
	 */
	public Map<String, Map<String, String>> getFnMethods() {
		return fnMethods;
	}
	public Map<String, String> getFnMethodsCtrl(String ctrl){
		return (Map<String, String>) getFnMethods().get(ctrl);
	}
	public String getFnMethodsCtrlMethod(String ctrl, String method){
		return getFnMethodsCtrl(ctrl).get(method);
	}
	
	public String getFunctionSecurity(String ctrl, String method){
		return getFnMethodsCtrlMethod(ctrl, method);
	}
	
}
