package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.time.YearMonth;
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
import com.gammon.pcms.model.Forecast.DeleteByEnum;
import com.gammon.pcms.model.adl.JDEForecast;
import com.gammon.pcms.model.adl.JDEForecastEOJ;
import com.gammon.pcms.wrapper.ForecastGroupWrapper;
import com.gammon.pcms.wrapper.ForecastListWrapper;
import com.gammon.pcms.wrapper.ForecastWrapper;
import com.gammon.qs.service.admin.AdminService;

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
	@Autowired
	private AdminService adminService;
	
	public Forecast getLatestForecastPeriod(String jobNo){
		Forecast latestForecast = new Forecast();
		try {
			latestForecast = repository.findTopByNoJobAndForecastFlagOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latestForecast;
	}
	
	public ForecastWrapper getByTypeDesc(String jobNo, int year, int month, String type, String desc) {
		ForecastWrapper wrapper  = new ForecastWrapper();
		
		int preYear = year;
		int preMonth = month -1;
		
		if(month == 1){
			preYear = year - 1;
			preMonth = 12;
		}
		
		try {
			Forecast currentForecast = repository.getByTypeDesc(jobNo, year, month, Forecast.ROLLING_FORECAST, type, desc);
			wrapper.setForecast(currentForecast);
			
			if(type.equals(Forecast.ACTUAL)){
				List<JDEForecast> forecastList = new ArrayList<JDEForecast>();
				
				forecastList = jdeForecasRepository.getJDEForecast(jobNo, year, month, desc);
				
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
				Forecast preForecast = repository.getByTypeDesc(jobNo, preYear, preMonth, Forecast.ROLLING_FORECAST, type, desc);
				wrapper.setPreForecast(preForecast);
				
				
				Forecast firstForecast = repository.findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST, type, desc);
				
				if(firstForecast != null){
					wrapper.setFirstForecast(firstForecast);					
				}
				else{
					JDEForecastEOJ jdeForecast =  jdeForecastEOJRepository.getLatestForecastEOJ(jobNo, desc);
					
					if (jdeForecast !=null){
						wrapper.setJdeForecast(jdeForecast);
						
						firstForecast= new Forecast();
						firstForecast.setNoJob(jobNo);
						firstForecast.setAmount(new BigDecimal(jdeForecast.getAmount()));
						firstForecast.setForecastFlag(Forecast.FORECAST);
						firstForecast.setForecastType(type);
						firstForecast.setForecastDesc(desc);
						firstForecast.setYear(year);
						firstForecast.setMonth(month);
						firstForecast.setForecastPeriod(jdeForecast.getLatestForecastLedger());
						wrapper.setFirstForecast(firstForecast);
						
					}
				}
				
			}else{
				Forecast preForecast = repository.getByTypeDesc(jobNo, preYear, preMonth, Forecast.ROLLING_FORECAST, type, desc);
				Forecast firstForecast = repository.findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST, type, desc);
				
				wrapper.setPreForecast(preForecast);
				wrapper.setFirstForecast(firstForecast);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper;
	}

	public List<Forecast> getForecastList(String jobNo, int year, int month, String forecastFlag) {
		List<Forecast> result = repository.getByPeriod(jobNo, year, month, forecastFlag);
		return result;
	}
	
	public ForecastListWrapper getForecastByJobNo(String jobNo, int year, int month) {
		ForecastListWrapper wrapper  = new ForecastListWrapper();
		try {
			
			if(year == 0 && month ==0){
				Forecast latestForecast = repository.findTopByNoJobAndForecastFlagOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST);
				
				if(latestForecast != null){
					year = latestForecast.getYear();
					month = latestForecast.getMonth();
				}
			}
				
			List<Forecast> forecastList = repository.getByPeriod(jobNo, year, month, Forecast.FORECAST);
			List<Forecast> programmeList = new ArrayList<Forecast>();
			
			if(forecastList != null && forecastList.size() >0 ){
				for (Forecast forecast: forecastList){
					if(Forecast.EOJ.equals(forecast.getForecastType())){
						if(Forecast.TURNOVER.equals(forecast.getForecastDesc())){
							wrapper.setTurnover(forecast);
						}else if(Forecast.COST.equals(forecast.getForecastDesc())){
							wrapper.setCost(forecast);
						}
					}
					else if(Forecast.CONTINGENCY.equals(forecast.getForecastType())){
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
				Forecast latestProgram = repository.findTopByNoJobAndForecastFlagAndForecastTypeOrderByYearDescMonthDesc(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
				
				if(latestProgram != null){
					forecastList = repository.getCriticalProgramList(jobNo, latestProgram.getYear(), latestProgram.getMonth(), Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
					
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
	
	public List<Forecast> getLatestCriticalProgramList(String jobNo) {
		
		List<Forecast> programList = new ArrayList<Forecast>();
		try {
			Forecast latestProgram = repository.findTopByNoJobAndForecastFlagAndForecastTypeOrderByYearDescMonthDesc(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
			if(latestProgram != null)
				programList = repository.getCriticalProgramList(jobNo, latestProgram.getYear(), latestProgram.getMonth(), Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return programList;
	}
	
	public ForecastGroupWrapper getCriticalProgramRFList(String jobNo, int year, int month) {
		ForecastGroupWrapper wrapper = new ForecastGroupWrapper();
		try {
			int preYear = year;
			int preMonth = month - 1;
			
			if(month == 1){
				preYear = year - 1;
				preMonth = 12;
			}
			
			List<ForecastWrapper> programmeWrapperList = new ArrayList<ForecastWrapper>();
				
			//get selected month RF
			List<Forecast> programList = repository.getCriticalProgramList(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);

			//get latest month RF
			Forecast latestProgram = repository.findTopByNoJobAndForecastFlagAndForecastTypeOrderByYearDescMonthDesc(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
			List<Forecast> latestProgramList = new ArrayList<Forecast>();
			if(latestProgram != null)
				latestProgramList = repository.getCriticalProgramList(jobNo, latestProgram.getYear(), latestProgram.getMonth(), Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
			
			//1. Selected month CP = 0, copy from latest month CP
			if(programList == null || programList.size() == 0){
				for(Forecast program: latestProgramList){
					ForecastWrapper programWrapper = new ForecastWrapper();
					Forecast preForecast = repository.getByTypeDesc(jobNo, preYear, preMonth, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME, program.getForecastDesc());
					Forecast firstForecast = repository.findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST, Forecast.CRITICAL_PROGRAMME, program.getForecastDesc());
					
					Forecast cpForecast = new Forecast();
					cpForecast.setNoJob(jobNo);
					cpForecast.setYear(year);
					cpForecast.setMonth(month);
					cpForecast.setForecastFlag(Forecast.ROLLING_FORECAST);
					cpForecast.setForecastType(Forecast.CRITICAL_PROGRAMME);
					cpForecast.setForecastDesc(program.getForecastDesc());
					
					programWrapper.setForecast(cpForecast);
					programWrapper.setPreForecast(preForecast);
					programWrapper.setFirstForecast(firstForecast);
					
					programmeWrapperList.add(programWrapper);
				}
			}
			else{
				//2. Selected month CP > 0
				List<String> programNames = new ArrayList<String>();
				
				for(Forecast currentProgram: programList){
					programNames.add(currentProgram.getForecastDesc());
					
					ForecastWrapper programWrapper = new ForecastWrapper();
					
					Forecast preForecast = repository.getByTypeDesc(jobNo, preYear, preMonth, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME, currentProgram.getForecastDesc());
					Forecast firstForecast = repository.findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST, Forecast.CRITICAL_PROGRAMME, currentProgram.getForecastDesc());
					
					programWrapper.setForecast(currentProgram);
					programWrapper.setPreForecast(preForecast);
					programWrapper.setFirstForecast(firstForecast);
					
					programmeWrapperList.add(programWrapper);
				}
				
				//Group with latest Program forecast 
				for(Forecast program: latestProgramList){
					if(programNames.contains(program.getForecastDesc()))
						continue;
					
					ForecastWrapper programWrapper = new ForecastWrapper();
					Forecast preForecast = repository.getByTypeDesc(jobNo, preYear, preMonth, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME, program.getForecastDesc());
					Forecast firstForecast = repository.findTopByNoJobAndForecastFlagAndForecastTypeAndForecastDescOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST, Forecast.CRITICAL_PROGRAMME, program.getForecastDesc());
					
					Forecast cpForecast = new Forecast();
					cpForecast.setNoJob(jobNo);
					cpForecast.setYear(year);
					cpForecast.setMonth(month);
					cpForecast.setForecastFlag(Forecast.ROLLING_FORECAST);
					cpForecast.setForecastType(Forecast.CRITICAL_PROGRAMME);
					cpForecast.setForecastDesc(program.getForecastDesc());
					
					programWrapper.setForecast(cpForecast);
					programWrapper.setPreForecast(preForecast);
					programWrapper.setFirstForecast(firstForecast);
					
					programmeWrapperList.add(programWrapper);
				}
				
			}
			wrapper.setCriticalProgrammeList(programmeWrapperList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public String addCriticalProgram(String jobNo, Forecast forecast) {
		String result= "";
		
		try {
			Forecast latestRF = repository.findTopByNoJobAndForecastFlagOrderByYearDescMonthDesc(jobNo, Forecast.ROLLING_FORECAST);
			//Forecast latestProgram = repository.getLatestProgramPeriod(jobNo, Forecast.ROLLING_FORECAST, Forecast.CRITICAL_PROGRAMME);
			
			if(latestRF != null){
				Forecast programInDb = repository.getByTypeDesc(jobNo, latestRF.getYear(), latestRF.getMonth(), Forecast.ROLLING_FORECAST, forecast.getForecastType(), forecast.getForecastDesc());
			
				if(programInDb !=null)
					return "Critical Programme: "+forecast.getForecastDesc() +" already exists.";
				else{
					forecast.setYear(latestRF.getYear());
					forecast.setMonth(latestRF.getMonth());
					repository.save(forecast);
				}
			}else{
				return "Pleae input at least one Rolling Forecast before adding Critical Program.";
			}
			
			//add program to the latest forecast
			Forecast latestForecast = repository.findTopByNoJobAndForecastFlagOrderByYearDescMonthDesc(jobNo, Forecast.FORECAST);
			if(latestForecast != null){
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
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public String updateCriticalProgramDesc(Forecast program) {
		String result = "";

		try {
			Forecast programInDB = repository.getByID(program.getId());
			if(programInDB != null && !programInDB.getForecastDesc().equals(program.getForecastDesc())){
				repository.updateCriticalProgramDesc(program.getNoJob(), programInDB.getForecastDesc(), program.getForecastDesc());
				
				
			}else{
				result = "No record has been changed.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public void updateForecastAdmin(Forecast forecast) throws Exception {
		Forecast save = repository.save(forecast);
		return;
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
			repository.save(wrapper.getSiteProfit().getForecast());
			repository.save(wrapper.getTenderRisk().getForecast());
			repository.save(wrapper.getTenderOpps().getForecast());
			repository.save(wrapper.getOthers().getForecast());
			repository.save(wrapper.getTotalContingency().getForecast());
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
			repository.save(wrapper.getTurnover());
			repository.save(wrapper.getCost());
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
	
	public void deleteBy(DeleteByEnum by, Forecast forecast){
		logger.info("delete by:" + by + " " + forecast);	
		String jobNo = forecast.getNoJob();
		Integer year = forecast.getYear();
		Integer month = forecast.getMonth();
		String flag = forecast.getForecastFlag();
		String forecastDesc = forecast.getForecastDesc();
		long itemDeleted = 0;
		adminService.canAccessJob(jobNo);
		switch(by){
			case YEAR_MONTH_FLAG:
			itemDeleted = repository.deleteByNoJobAndYearAndMonthAndForecastFlag(jobNo, year, month, flag);
			break;
			case FORECAST_DESC:
			itemDeleted = repository.deleteByNoJobAndForecastDescIgnoreCase(jobNo, forecastDesc);
			break;
			default:
				logger.error(by + " not supported");	
			break;
		}
		logger.info("item deleted:" + itemDeleted);	
	}

    public void cloneMonthlyMovement(YearMonth from, YearMonth to) {
		logger.info("-- Start cloning monthly movement --");
		List<String> jobs = repository.findAllJobNumber();
		List<String> filterJobList = new ArrayList<>();

		// 1. filter job list
		for (String job: jobs) {
			List<Forecast> lastMonth = repository.getByPeriod(job, from.getYear(), from.getMonthValue(), Forecast.ROLLING_FORECAST);
			List<Forecast> currentMonth = repository.getByPeriod(job, to.getYear(), to.getMonthValue(), Forecast.ROLLING_FORECAST);
			if ((currentMonth == null || currentMonth.size() == 0)
					&& (lastMonth != null && lastMonth.size() > 0)) {
				filterJobList.add(job);
			}
		}

		int seqNo = 1;
		int totalNumberOfJobs = filterJobList.size();

		// 2. create record if not exist
		for (String job: filterJobList) {
			List<Forecast> lastMonth = repository.getByPeriod(job, from.getYear(), from.getMonthValue(), Forecast.ROLLING_FORECAST);
			List<Forecast> currentMonth = repository.getByPeriod(job, to.getYear(), to.getMonthValue(), Forecast.ROLLING_FORECAST);

			if ((currentMonth == null || currentMonth.size() == 0)
					&& (lastMonth != null && lastMonth.size() > 0)) {
				// clone
				for (Forecast f : lastMonth) {
					try {
						repository.save(
								new Forecast(
										f.getNoJob(),
										f.getYear(),
										to.getMonthValue(),
										f.getForecastFlag(),
										f.getForecastType(),
										f.getForecastDesc(),
										f.getAmount(),
										f.getDate(),
										f.getExplanation(),
										f.getForecastPeriod()
								)
						);
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("[FAIL] ("+seqNo+"/"+ totalNumberOfJobs +") Job no = " + job + ", Forecast id = " + f.getId() + ", From = " + from + ", To = " + to + ", Exception message = " + e.getMessage());
						throw e;
					}
				}
				logger.info("[SUCCESS] ("+seqNo+"/"+ totalNumberOfJobs +") Job no = " + job + ", From = " + from + ", To = " + to);
				seqNo++;
			}

		}
		logger.info("-- End cloning monthly movement --");
    }
}
