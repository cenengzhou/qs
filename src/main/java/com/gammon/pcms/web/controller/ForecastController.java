package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.Forecast;

import com.gammon.pcms.service.ForecastService;
import com.gammon.pcms.wrapper.ForecastGroupWrapper;
import com.gammon.pcms.wrapper.ForecastListWrapper;
import com.gammon.pcms.wrapper.ForecastWrapper;

@RestController
@RequestMapping(value = "service/forecast/")
public class ForecastController {
	
	@Autowired
	private ForecastService service;

	
	@RequestMapping(value = "getByTypeDesc/{jobNo}/{year}/{month}/{type}/{desc}", method = RequestMethod.GET)
	public ForecastWrapper getByTypeDesc(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month, @PathVariable String type, @PathVariable String desc) {
		return service.getByTypeDesc(jobNo, year, month, type, desc);
	}
	
	@RequestMapping(value = "getForecastByJobNo/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public ForecastListWrapper getForecastByJobNo(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month) {
		return service.getForecastByJobNo(jobNo, year, month);
	}
	
	
	@RequestMapping(value = "getLatestCriticalProgramList/{jobNo}", method = RequestMethod.GET)
	public List<Forecast> getLatestCriticalProgramList(@PathVariable String jobNo) {
		return service.getLatestCriticalProgramList(jobNo);
	}
	
	@RequestMapping(value = "getCriticalProgramRFList/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public ForecastGroupWrapper getCriticalProgramRFList(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month) {
		return service.getCriticalProgramRFList(jobNo, year, month);
	}
	
	@RequestMapping(value = "{jobNo}", method = RequestMethod.POST)
	public Boolean post(@PathVariable String jobNo, @RequestBody ForecastGroupWrapper forecast) {
		return service.save(jobNo, forecast);
	}

	@RequestMapping(value = "saveForecastByJobNo/{jobNo}", method = RequestMethod.POST)
	public Boolean saveForecastByJobNo(@PathVariable String jobNo, @RequestBody ForecastListWrapper forecast) {
		return service.saveForecastByJobNo(jobNo, forecast);
	}
	
	@RequestMapping(value = "saveList/{jobNo}", method = RequestMethod.POST)
	public List<Forecast> post(@PathVariable String jobNo, @RequestBody List<Forecast> forecastList) {
		return service.saveList(jobNo, forecastList);
	}
	
	@RequestMapping(value = "addCriticalProgram/{jobNo}", method = RequestMethod.POST)
	public String addCriticalProgram(@PathVariable String jobNo, @RequestBody Forecast forecast) {
		return service.addCriticalProgram(jobNo, forecast);
	}
	
	
	@RequestMapping(value = "updateCriticalProgramDesc", method = RequestMethod.POST)
	public String updateCriticalProgramDesc(@RequestBody Forecast program) {
		return service.updateCriticalProgramDesc(program);
	}
	
	@RequestMapping(value = "{jobNo}/{ids}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String jobNo, @PathVariable Long[] ids) {
		service.delete(jobNo, ids);
	}
	
	
	
}
