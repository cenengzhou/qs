package com.gammon.qs.web.mvc.controllers.user;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.gammon.qs.application.User;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.user.PreferencesService;
import com.gammon.qs.web.formbean.GeneralPreferencesBean;
import com.gammon.qs.web.mvc.controllers.BaseController;

@Controller
@RequestMapping(value="/user/*")
public class PreferencesController extends BaseController{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private PreferencesService preferencesService;
	
	@Autowired
	private AdminService adminService;
	
	@ModelAttribute("decimalPlacesOptions")
	public Map<String, String> getDecimalPlacesOptions() {
		Map<String, String> options = new LinkedHashMap<String, String>();
		options.put("0", "0");
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		return options;
	}
	
	@ModelAttribute("quantDecimalPlacesOptions")
	public Map<String, String> getQuantDecimalPlacesOptions() {
		Map<String, String> options = new LinkedHashMap<String, String>();
		for(int i = 0; i <= 10; i++)
			options.put(Integer.toString(i), Integer.toString(i));
		return options;
	}
	
	@RequestMapping(value="preferences.htm", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest httpRequest,WebRequest request) {
		ModelAndView mav = new ModelAndView("user/preferences");
		try {
			User user = adminService.getUserByUsername(request.getRemoteUser());
			mav.addObject("generalPreferences", new GeneralPreferencesBean(user.getGeneralPreferences()));
		} catch(Exception ex) {
			logger.info("Failure at loading general preferences of " + request.getRemoteUser());
			ex.printStackTrace();
		}
		mav.addAllObjects(getDefaultModel(httpRequest));
		
		if (request.getParameter("success") != null) 
			mav.addObject("success", true);
		
		return mav;
	}
	
	@RequestMapping(value="preferences.htm", method=RequestMethod.POST)
	public ModelAndView savePreferences(HttpServletRequest httpRequest,WebRequest request, @ModelAttribute("generalPreferences") GeneralPreferencesBean preferences) {
		try {
			preferencesService.saveGeneralPreferences(request.getRemoteUser(), preferences);
		} catch(Exception ex) {
			logger.info("Failure at saving general preferences of " + request.getRemoteUser());
			ex.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("redirect:/user/preferences.htm");
		mav.addAllObjects(getDefaultModel(httpRequest));
		mav.addObject("success", true);
		
		return mav;
	}
}
