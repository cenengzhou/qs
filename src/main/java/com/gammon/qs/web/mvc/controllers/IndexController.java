/*package com.gammon.qs.web.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

	@RequestMapping(value={"homeindex.html",""},method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest httpRequest) throws Exception {

		ModelAndView mav = new ModelAndView("homeindex");
		mav.addAllObjects(getDefaultModel(httpRequest));
		
		return mav;
	}

}
*/