package com.gammon.qs.web.mvc.controllers.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.gammon.qs.application.SystemMessage;
import com.gammon.qs.service.SystemMessageService;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.MapUtil;
import com.gammon.qs.web.SiteNavigation;
import com.gammon.qs.web.WebAttributeKeys;
import com.gammon.qs.web.mvc.controllers.BaseController;

@Controller
@RequestMapping(value="/admin/globalSystemMessage/*")
public class GlobalSystemMessageController extends BaseController{
	
	@Autowired
	private SystemMessageService systemMessageRepository;
	
	private Map<String, SystemMessage> systemMessageMap; 
	private SystemMessage systemMessage;
	
	
	@RequestMapping(value="index.htm", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("admin/globalSystemMessage/index");
		return index(request, mav);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView index(HttpServletRequest request,ModelAndView mav) {
		try {
			List<SystemMessage> systemMessageList = systemMessageRepository.getAllSystemMessage();
			this.systemMessageMap = MapUtil.getIdHashMap(systemMessageList);
			
			mav.addObject("systemMessageList", systemMessageList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		mav.addAllObjects(getDefaultModel(request));
		mav.addObject(WebAttributeKeys.CURRENT_NAVIGATION, SiteNavigation.ADMIN_GLOBAL_SYSTEM_MESSAGE_KEY);
		return mav;
	}
	
	
	@RequestMapping(value="addSystemMessage.htm", method=RequestMethod.POST)
	public ModelAndView saveGlobalSystemMessage(HttpServletRequest httpRequest,WebRequest request) {
		
		try{
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setMessage(request.getParameter("message"));
			Date scheduledDate = DateUtil.parseDate(request.getParameter("scheduledDate"), "dd-MM-yyyy HH:mm");
			systemMessage.setScheduledDate(scheduledDate);
			systemMessage.setUsername("ALL");//username is "ALL" for global message

			if(scheduledDate == null)
			{
				// erorr handling
				
			}
			
			systemMessageRepository.saveGlobalSystemMessage(systemMessage, request.getRemoteUser());
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("admin/globalSystemMessage/index");
		
		return index(httpRequest,  mav);
		
	}
	
	@RequestMapping(value="editSystemMessage.htm")
	public ModelAndView edit(WebRequest request, @RequestParam(value="systemMessageId", required=false)String id) {
		
		ModelAndView mav = new ModelAndView("admin/globalSystemMessage/edit");
		
		SystemMessage curSystemMessage = (SystemMessage) this.systemMessageMap.get(id);
		mav.addObject("systemMessage", curSystemMessage);
		this.systemMessage = curSystemMessage;
		
		
		return mav;
	}
	
	@RequestMapping(value="removeSystemMessage.htm")
	public ModelAndView remove(HttpServletRequest httpRequest, WebRequest request, @RequestParam(value="systemMessageId", required=false)String id) {
				
		try{	
			systemMessageRepository.removeSystemMessageById(new Long(id));
		}catch(Exception e)
		{
			e.printStackTrace();			
		}
		
		ModelAndView mav = new ModelAndView("admin/globalSystemMessage/index");
		
		return index(httpRequest, mav);
		
	}
	
	@RequestMapping(value="saveSystemMessage.htm", method=RequestMethod.POST)
	public ModelAndView saveSystemMessage(HttpServletRequest httpRequest,WebRequest request){
		
		try{
			
			String action = request.getParameter("action");
			
			if("save".equals(action))
			{
			
				this.systemMessage.setMessage(request.getParameter("message")!=null?request.getParameter("message"):"" );
				String requestDateStr = request.getParameter("scheduledDate");
				Date scheduledDate = DateUtil.parseDate(requestDateStr, "dd-MM-yyyy HH:mm");
				
				this.systemMessage.setScheduledDate(scheduledDate);
				
				systemMessageRepository.saveGlobalSystemMessage(systemMessage, request.getRemoteUser());
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("admin/globalSystemMessage/index");
		
		return index( httpRequest, mav);
		
		
	}
	

}
