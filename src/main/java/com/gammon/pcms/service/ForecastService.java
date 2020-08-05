package com.gammon.pcms.service;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.ForecastRepository;
import com.gammon.pcms.dao.adl.JDEForecastEOJRepository;
import com.gammon.pcms.dao.adl.JDEForecastRepository;
import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.adl.JDEForecast;
import com.gammon.pcms.model.adl.JDEForecastEOJ;
import com.gammon.pcms.wrapper.ForecastGroupWrapper;
import com.gammon.pcms.wrapper.ForecastListWrapper;
import com.gammon.pcms.wrapper.ForecastWrapper;

import edu.emory.mathcs.backport.java.util.Arrays;

@Transactional
@Service
public class ForecastService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ForecastRepository repository;
	@Autowired
	private JDEForecastEOJRepository jdeForecastEOJRepository;
	@Autowired
	private JDEForecastRepository jdeForecasRepository;
	
	
	public List<Forecast> findByJobNo(String jobNo) {
		return repository.findByNoJob(jobNo);
	}
	
	
	public ForecastWrapper getByTypeDesc(String jobNo, int year, int month, String type, String desc) {
		ForecastWrapper wrapper  = new ForecastWrapper();
		try {
			Forecast currentForecast = repository.getByTypeDesc(jobNo, year, month, Forecast.ROLLING_FORECAST, type, desc);
			wrapper.setForecast(currentForecast);
			
			if(type.equals(Forecast.ACTUAL)){
				List<JDEForecast> forecastList = new ArrayList<JDEForecast>();
				if(desc.equals(Forecast.INTERNAL_VALUE))
					forecastList = jdeForecasRepository.getJDEForecast(jobNo, year, month, Forecast.TURNOVER);
				else
					forecastList = jdeForecasRepository.getJDEForecast(jobNo, year, month, desc);//Cost
				
				for(JDEForecast forecast: forecastList){
					if(JDEForecast.AA.equals(forecast.getAccLedgerType())){
						if(JDEForecast.CTD.equals(forecast.getFigureType()))
							wrapper.setActualCTD(forecast);
						else
							wrapper.setActualMTD(forecast);
					}else{
						if(JDEForecast.CTD.equals(forecast.getFigureType()))
							wrapper.setForecastCTD(forecast);
						else
							wrapper.setForecastMTD(forecast);
					}
				}
			}
			else if(type.equals(Forecast.EOJ)){
				Forecast preForecast = repository.getByTypeDesc(jobNo, year, month-1, Forecast.ROLLING_FORECAST, type, desc);
				wrapper.setPreForecast(preForecast);
				
				JDEForecastEOJ jdeForecast =  jdeForecastEOJRepository.getLatestForecastEOJ(jobNo, desc);
				
				if (jdeForecast !=null)
					wrapper.setJdeForecast(jdeForecast);
				
			}else{
				Forecast preForecast = repository.getByTypeDesc(jobNo, year, month-1, Forecast.ROLLING_FORECAST, type, desc);
				Forecast firstForecast = repository.getLatestForecast(jobNo, Forecast.FORECAST, type, desc);
				
				wrapper.setPreForecast(preForecast);
				wrapper.setFirstForecast(firstForecast);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public ForecastListWrapper getForecastByJobNo(String jobNo, int year, int month) {
		ForecastListWrapper wrapper  = new ForecastListWrapper();
		try {
			
			if(year == 0 && month ==0){
				Forecast latestForecast = repository.getLatestForecastPeriod(jobNo, Forecast.FORECAST);
				year = latestForecast.getYear();
				month = latestForecast.getMonth();
			}
				
			List<Forecast> forecastList = repository.getByPeriod(jobNo, year, month, Forecast.FORECAST);
			List<Forecast> programmeList = new ArrayList<Forecast>();
			
			if(forecastList != null && forecastList.size() >0 ){
				for (Forecast forecast: forecastList){
					if(Forecast.CONTINGENCY.equals(forecast.getForecastType())){
						if(Forecast.TENDER_RISKS.equals(forecast.getForecastDesc())){
							wrapper.setTenderRisk(forecast);
						}else if(Forecast.TENDER_OPPS.equals(forecast.getForecastDesc())){
							wrapper.setTenderOpps(forecast);
						}else if(Forecast.OTHERS.equals(forecast.getForecastDesc())){
							wrapper.setOthers(forecast);
						}
					}else if(Forecast.RISKS.equals(forecast.getForecastType())){
						if(Forecast.RISKS.equals(forecast.getForecastDesc())){
							wrapper.setRisk(forecast);
						}else if(Forecast.OPPS.equals(forecast.getForecastDesc())){
							wrapper.setOpps(forecast);
						}
					}else if(Forecast.UNSECURED_EOJ.equals(forecast.getForecastType())){
						if(Forecast.UNSECURED_TURNOVER.equals(forecast.getForecastDesc())){
							wrapper.setUnTurnover(forecast);
						}else if(Forecast.UNSECURED_COST.equals(forecast.getForecastDesc())){
							wrapper.setUnCost(forecast);
						}
					}else if(Forecast.CRITICAL_PROGRAMME.equals(forecast.getForecastType())){
						programmeList.add(forecast);
					}
				}
			}else{
				Forecast latestProgram = repository.getLatestProgramPeriod(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
				
				if(latestProgram != null){
					forecastList = repository.getCriticalProgrammeList(jobNo, latestProgram.getYear(), latestProgram.getMonth(), Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
					
					for(Forecast forecast: forecastList){
						
						Forecast cpForecast = new Forecast();
						cpForecast.setNoJob(jobNo);
						cpForecast.setYear(year);
						cpForecast.setMonth(month);
						cpForecast.setForecastFlag(Forecast.FORECAST);
						cpForecast.setForecastType(Forecast.CRITICAL_PROGRAMME);
						cpForecast.setForecastDesc(forecast.getForecastDesc());
						
						
						programmeList.add(cpForecast);
					}
				
				}
			}
			wrapper.setCriticalPrograms(programmeList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	
	
	public ForecastGroupWrapper getCriticalProgrammeRFList(String jobNo, int year, int month) {
		ForecastGroupWrapper wrapper = new ForecastGroupWrapper();
		try {
			
			List<ForecastWrapper> programmeList = new ArrayList<ForecastWrapper>();
			
			List<Forecast> forecastList = repository.getCriticalProgrammeList(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
			if(forecastList == null || forecastList.size() == 0){
				Forecast latestProgram = repository.getLatestProgramPeriod(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
				
				if(latestProgram != null){
					forecastList = repository.getCriticalProgrammeList(jobNo, latestProgram.getYear(), latestProgram.getMonth(), Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
					
					for(Forecast forecast: forecastList){
						ForecastWrapper programme = new ForecastWrapper();
						Forecast preForecast = repository.getByTypeDesc(jobNo, year, month-1, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME, forecast.getForecastDesc());
						Forecast firstForecast = repository.getLatestForecast(jobNo, Forecast.FORECAST, Forecast.CRITICAL_PROGRAMME, forecast.getForecastDesc());
						
						Forecast cpForecast = new Forecast();
						cpForecast.setNoJob(jobNo);
						cpForecast.setYear(year);
						cpForecast.setMonth(month);
						cpForecast.setForecastFlag(Forecast.ROLLING_FORECAST);
						cpForecast.setForecastType(Forecast.CRITICAL_PROGRAMME);
						cpForecast.setForecastDesc(forecast.getForecastDesc());
						
						programme.setForecast(cpForecast);
						programme.setPreForecast(preForecast);
						programme.setFirstForecast(firstForecast);
						
						programmeList.add(programme);
					}
				}
			}
			else{
				for(Forecast forecast: forecastList){
					ForecastWrapper programme = new ForecastWrapper();
					
					Forecast preForecast = repository.getByTypeDesc(jobNo, year, month-1, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME, forecast.getForecastDesc());
					Forecast firstForecast = repository.getLatestForecast(jobNo, Forecast.FORECAST, Forecast.CRITICAL_PROGRAMME, forecast.getForecastDesc());
					
					programme.setForecast(forecast);
					programme.setPreForecast(preForecast);
					programme.setFirstForecast(firstForecast);
					
					programmeList.add(programme);
				}
			}
			wrapper.setCriticalProgrammeList(programmeList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public List<Forecast> saveList(String jobNo, List<Forecast> forecastList) {
		return repository.save(forecastList);
	}
	
	public Boolean save(String jobNo, ForecastGroupWrapper wrapper) {
		boolean update = false;
		
		try {
			update = true;
			
			repository.save(wrapper.getActualTurnover().getForecast());
			repository.save(wrapper.getActualCost().getForecast());
			repository.save(wrapper.getTurnover().getForecast());
			repository.save(wrapper.getCost().getForecast());
			repository.save(wrapper.getTenderRisk().getForecast());
			repository.save(wrapper.getTenderOpps().getForecast());
			repository.save(wrapper.getOthers().getForecast());
			repository.save(wrapper.getRisk().getForecast());
			repository.save(wrapper.getOpps().getForecast());
			repository.save(wrapper.getUnTurnover().getForecast());
			repository.save(wrapper.getUnCost().getForecast());
			
			for(ForecastWrapper cp: wrapper.getCriticalProgrammeList()){
				repository.save(cp.getForecast());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return update;
	}
	

	public Boolean saveForecastByJobNo(String jobNo, ForecastListWrapper wrapper) {
		boolean update = false;
		
		try {
			update = true;
			
			repository.save(wrapper.getTenderRisk());
			repository.save(wrapper.getTenderOpps());
			repository.save(wrapper.getOthers());
			repository.save(wrapper.getRisk());
			repository.save(wrapper.getOpps());
			repository.save(wrapper.getUnTurnover());
			repository.save(wrapper.getUnCost());
			repository.save(wrapper.getCriticalPrograms());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return update;
	}
	
	public String addCriticalProgram(String jobNo, Forecast forecast) {
		String result= "";
		
		try {
			
			//add rolling forecast
			Forecast rollingForecastInDb = repository.getByTypeDesc(jobNo, forecast.getYear(), forecast.getMonth(), Forecast.ROLLING_FORECAST, forecast.getForecastType(), forecast.getForecastDesc());
			if(rollingForecastInDb !=null)
				return "Critical Programme: "+forecast.getForecastDesc() +" already exists.";
			else
				repository.save(forecast);
			
			
			//add program to the latest forecast
			Forecast latestForecast = repository.getLatestForecastPeriod(jobNo, Forecast.FORECAST);
			Forecast forecastInDb = repository.getByTypeDesc(jobNo, latestForecast.getYear(), latestForecast.getMonth(), Forecast.FORECAST, forecast.getForecastType(), forecast.getForecastDesc());
			
			if(forecastInDb == null){
				Forecast newForecast = new Forecast();
				newForecast.setNoJob(forecast.getNoJob());
				newForecast.setForecastType(forecast.getForecastType());
				newForecast.setForecastDesc(forecast.getForecastDesc());
				newForecast.setForecastFlag(Forecast.FORECAST);
				newForecast.setYear(latestForecast.getYear());
				newForecast.setMonth(latestForecast.getMonth());
				repository.save(newForecast);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
	
	public void delete(String jobNo, Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		idList.forEach(id -> {
			Forecast forecastDb = repository.getOne((Long)id);
			if(forecastDb.getNoJob().equals(jobNo)) {
				repository.delete(forecastDb);
			} else {
				throw new IllegalAccessError("cannot delete forecast");
			}
		});
	}
	
}
