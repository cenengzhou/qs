package com.gammon.pcms.config;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
@PropertySource("file:${security.properties}")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LdapConfig ldapConfig;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	@Value("${spring.security.debug}")
	private String springSecurityDebug;
	@Value("${loginPath}")
	private String loginPath;

	// Role
	@Value("${role.pcms.ws}")
	private String rolePcmsWs;
	@Value("${role.pcms.api}")
	private String rolePcmsApi;
	@Value("${role.pcms.enq}")
	private String rolePcmsEnq;
	@Value("${role.pcms.ims.enq}")
	private String rolePcmsImsEnq;
	@Value("${role.pcms.ims.admin}")
	private String rolePcmsImsAdmin;
	@Value("${role.pcms.qs}")
	private String rolePcmsQs;
	@Value("${role.pcms.qs.admin}")
	private String rolePcmsQsAdmin;
	@Value("${role.pcms.qs.reviewer}")
	private String rolePcmsQsReviewer;
	@Value("${role.pcms.job.all}")
	private String rolePcmsJobAll;
	@Value("${role.pcms.job.hkg}")
	private String rolePcmsJobHkg;
		
	// Kerberos
	@Value("${kerberos.service-principal}")
	private String kerberosServicePrincipal;
	@Value("${kerberos.keytab-location}")
	private String kerberosKeytabLocation;
	@Value("${kerberos.debug}")
	boolean kerberosDebug;
	@Value("${defaultMaxInactiveInterval}")
	private String defaultMaxInactiveInterval;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.authenticationProvider(ldapAuthenticationProvider())
		.authenticationProvider(kerberosServiceAuthenticationProvider())
		.inMemoryAuthentication().withUser(webServiceConfig.getQsWsUsername()).password(webServiceConfig.getQsWsPassword()).authorities("ROLE_"+getRolePcmsWs());
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
		
		//TODO: Add role security to Swagger page
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
					"/login.htm*",
					"/logout.htm*"
					).permitAll()
			.antMatchers("/**/*").hasAnyRole(
					securityConfig.getRolePcmsEnq(),
					securityConfig.getRolePcmsImsAdmin(),
					securityConfig.getRolePcmsImsEnq(),
					securityConfig.getRolePcmsQs(),
					securityConfig.getRolePcmsQsAdmin(),
					securityConfig.getRolePcmsQsReviewer()
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
			.deleteCookies("JSESSIONID")
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
		ticketValidator.setKeyTabLocation(new FileSystemResource(getKerberosKeytabLocation()));
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
		bean.setUrl(ldapConfig.getLdapServerUrl());
		bean.setBase(ldapConfig.getLdapServerBase());
		bean.setUserDn(ldapConfig.getLdapServerUsername());
		bean.setPassword(ldapConfig.getLdapServerPassword());
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
		return rolePcmsWs;
	}

	public String getRolePcmsApi() {
		return rolePcmsApi;
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
		return rolePcmsEnq;
	}

	/**
	 * @return the rolePcmsImsEnq
	 */
	public String getRolePcmsImsEnq() {
		return rolePcmsImsEnq;
	}

	/**
	 * @return the rolePcmsImsAdmin
	 */
	public String getRolePcmsImsAdmin() {
		return rolePcmsImsAdmin;
	}

	/**
	 * @return the rolePcmsQs
	 */
	public String getRolePcmsQs() {
		return rolePcmsQs;
	}

	/**
	 * @return the rolePcmsQsAdmin
	 */
	public String getRolePcmsQsAdmin() {
		return rolePcmsQsAdmin;
	}

	/**
	 * @return the rolePcmsQsReviewer
	 */
	public String getRolePcmsQsReviewer() {
		return rolePcmsQsReviewer;
	}

	/**
	 * @return the rolePcmsJobAll
	 */
	public String getRolePcmsJobAll() {
		return rolePcmsJobAll;
	}

	/**
	 * @return the rolePcmsJobHkg
	 */
	public String getRolePcmsJobHkg() {
		return rolePcmsJobHkg;
	}
}
