package com.gammon.qs.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gammon.pcms.config.DeploymentConfig;
import com.gammon.qs.service.admin.EnvironmentConfig;

@Component("baseInformationFilter")
public class BaseInformationFilter implements Filter {
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private DeploymentConfig deploymentConfig;

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (environmentConfig != null) {
			// JDE Environment
			String jdeEnviroment = environmentConfig.getJdeWebserviceEnvironment();
			int endIndex = jdeEnviroment.lastIndexOf("/");
			int startIndex = jdeEnviroment.substring(0, jdeEnviroment.length() - 1).lastIndexOf("/") + 1;
			request.setAttribute("jdeEnvironment", jdeEnviroment.substring(startIndex, endIndex));

			// Version
			if (deploymentConfig.getVersionDate() != null && !"".equals(deploymentConfig.getVersionDate().trim()))
				request.setAttribute("versionDescription", "Version delivered on " + deploymentConfig.getVersionDate());
			else
				request.setAttribute("versionDescription", "Version delivered on ");
		}

		request.setAttribute("serverEnvironment", request.getServerName());

		chain.doFilter(request, response);

	}

	public void init(FilterConfig filterCfg) throws ServletException {}
}
