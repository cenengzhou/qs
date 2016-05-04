package com.gammon.qs.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.PersistentRemoteService;

public abstract class GWTSpringController extends PersistentRemoteService implements Controller, ServletContextAware {

	private static final long serialVersionUID = 8291286443363806806L;
	// public abstract class GWTSpringController extends RemoteServiceServlet
	// implements Controller, ServletContextAware {
	private ServletContext servletContext;
	
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		try {
			super.doPost(arg0, arg1);
		} catch (IndexOutOfBoundsException iobe) {
			System.err.println("Exception has not flow (Check by Exception)");
			iobe.printStackTrace();
		} catch (Exception e) {
			if (e.getMessage().indexOf("java.lang.IndexOutOfBoundsException: Index: 0, Size: 0") > 0) {
				System.err.println("Exception has not flow (Check by String)");
				e.printStackTrace();
			} else
				throw e;

		}
		return null;
	}

	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;

	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

}