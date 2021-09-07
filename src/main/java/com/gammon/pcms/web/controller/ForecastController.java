package com.gammon.pcms.web.controller;

import java.util.List;

import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.Forecast.DeleteByEnum;
import com.gammon.pcms.service.ForecastService;
import com.gammon.pcms.wrapper.ForecastGroupWrapper;
import com.gammon.pcms.wrapper.ForecastListWrapper;
import com.gammon.pcms.wrapper.ForecastWrapper;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.admin.MailContentGenerator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "service/forecast/")
public class ForecastController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ForecastService service;
	@Autowired
	private JobInfoService jobInfoService;

	@Autowired
	private MailContentGenerator mailContentGenerator;

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getLatestForecastPeriod', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getLatestForecastPeriod/{jobNo}", method = RequestMethod.GET)
	public Forecast getLatestForecastPeriod(@PathVariable String jobNo){
		return service.getLatestForecastPeriod(jobNo);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getByTypeDesc', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getByTypeDesc/{jobNo}/{year}/{month}/{type}/{desc}", method = RequestMethod.GET)
	public ForecastWrapper getByTypeDesc(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month, @PathVariable String type, @PathVariable String desc) {
		return service.getByTypeDesc(jobNo, year, month, type, desc);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getForecastByJobNo', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getForecastByJobNo/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public ForecastListWrapper getForecastByJobNo(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month) {
		return service.getForecastByJobNo(jobNo, year, month);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getForecastList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getForecastList/{jobNo}/{year}/{month}/{forecastFlag}", method = RequestMethod.GET)
	public List<Forecast> getForecastList(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month, @PathVariable String forecastFlag) {
		return service.getForecastList(jobNo, year, month, forecastFlag);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getLatestCriticalProgramList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getLatestCriticalProgramList/{jobNo}", method = RequestMethod.GET)
	public List<Forecast> getLatestCriticalProgramList(@PathVariable String jobNo) {
		return service.getLatestCriticalProgramList(jobNo);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','getCriticalProgramRFList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCriticalProgramRFList/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public ForecastGroupWrapper getCriticalProgramRFList(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month) {
		return service.getCriticalProgramRFList(jobNo, year, month);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','post', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "{jobNo}", method = RequestMethod.POST)
	public Boolean post(@PathVariable String jobNo, @RequestBody ForecastGroupWrapper forecast) {
		return service.save(jobNo, forecast);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','saveForecastByJobNo', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveForecastByJobNo/{jobNo}", method = RequestMethod.POST)
	public Boolean saveForecastByJobNo(@PathVariable String jobNo, @RequestBody ForecastListWrapper forecast) {
		return service.saveForecastByJobNo(jobNo, forecast);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','post', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public List<Forecast> post(@PathVariable String jobNo, @RequestBody List<Forecast> forecastList) {
		return service.saveList(jobNo, forecastList);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','addCriticalProgram', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "addCriticalProgram/{jobNo}", method = RequestMethod.POST)
	public String addCriticalProgram(@PathVariable String jobNo, @RequestBody Forecast forecast) {
		return service.addCriticalProgram(jobNo, forecast);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','updateCriticalProgramDesc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateCriticalProgramDesc", method = RequestMethod.POST)
	public String updateCriticalProgramDesc(@RequestBody Forecast program) {
		return service.updateCriticalProgramDesc(program);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','updateForecastAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateForecastAdmin", method = RequestMethod.POST)
	public void updateForecastAdmin(@RequestBody Forecast forecast) throws Exception {
		if((forecast).getId() == null) throw new IllegalArgumentException("Invalid Forecast");
		String result = jobInfoService.canAdminJob(forecast.getNoJob());
		if(StringUtils.isEmpty(result)){
			service.updateForecastAdmin(forecast);
			mailContentGenerator.sendAdminFnEmail("updateForecastAdmin", forecast.toString());
		} else {
			throw new IllegalAccessException(result);
		}
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','updateForecastListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateForecastListAdmin", method = RequestMethod.POST)
	public void updateForecastListAdmin(@RequestBody List<Forecast> forecastList) throws Exception {
		String jobNumber = forecastList.get(0).getNoJob();
		String result = jobInfoService.canAdminJob(jobNumber);
		if(StringUtils.isEmpty(result)){
			forecastList.forEach(forecast -> {
				try {
					updateForecastAdmin(forecast);
				} catch (Exception e) {
					logger.error("error", e);
				}
			});
		} else {
			throw new IllegalAccessException(result);
		}
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','delete', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "{jobNo}/{ids}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String jobNo, @PathVariable Long[] ids) {
		service.delete(jobNo, ids);
	}
	
	@PreAuthorize(value = "@GSFService.isRoleExisted('ForecastController','delete', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "deleteBy/{by}", method = RequestMethod.POST)
	public void deleteBy(@PathVariable DeleteByEnum by, @RequestBody Forecast forecast) {
		service.deleteBy(by, forecast);
	}
	
}
