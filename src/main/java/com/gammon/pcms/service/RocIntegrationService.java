package com.gammon.pcms.service;

import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.dao.RocClassDescMapRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.dao.RocSubdetailRepository;
import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.wrapper.RocSummaryWrapper;
import com.gammon.qs.dao.RocDetailHBDao;
import com.gammon.qs.dao.RocHBDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RocIntegrationService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RocDetailRepository rocDetailRepository;

	@Autowired
	private ForecastService forecastService;

	public String calculateRocSummaryToMonthlyMovement(String jobNo, Integer year, Integer month) {
		String error = "";
		try {
			List<Forecast> forecastList = forecastService.getForecastList(jobNo, year, month, Forecast.ROLLING_FORECAST);
			List<Forecast> newForecastList = new ArrayList<>();
			List<RocSummaryWrapper> sumOfAmountExpectedGroupByRocCat = rocDetailRepository.findSumOfAmountExpectedGroupByRocCat(jobNo, year, month);

			Forecast actualTurnover = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.ACTUAL, Forecast.INTERNAL_VALUE, forecastList);
			Forecast actualCost = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.ACTUAL, Forecast.COST, forecastList);
			Forecast turnover = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.EOJ, Forecast.TURNOVER, forecastList);
			Forecast cost = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.EOJ, Forecast.COST, forecastList);
			Forecast unTurnover = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.UNSECURED_EOJ, Forecast.UNSECURED_TURNOVER, forecastList);
			Forecast unCost = findDbOrCreateNewForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.UNSECURED_EOJ, Forecast.UNSECURED_COST, forecastList);

			Forecast tenderRisk = findDbAndUpdateSumForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.CONTINGENCY, Forecast.TENDER_RISKS, ROC.TENDER_RISK, sumOfAmountExpectedGroupByRocCat, forecastList);
			Forecast tenderOpps = findDbAndUpdateSumForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.CONTINGENCY, Forecast.TENDER_OPPS, ROC.TENDER_OPPS, sumOfAmountExpectedGroupByRocCat, forecastList);
			Forecast others = findDbAndUpdateSumForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.CONTINGENCY, Forecast.OTHERS, ROC.CONTINGENCY, sumOfAmountExpectedGroupByRocCat, forecastList);
			Forecast risk = findDbAndUpdateSumForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.RISKS, Forecast.RISKS, ROC.RISK, sumOfAmountExpectedGroupByRocCat, forecastList);
			Forecast opps = findDbAndUpdateSumForecast(jobNo, year, month, Forecast.ROLLING_FORECAST, Forecast.RISKS, Forecast.OPPS, ROC.OPPS, sumOfAmountExpectedGroupByRocCat, forecastList);

			tenderOpps.setAmount(tenderOpps.getAmount().negate());
			risk.setAmount(risk.getAmount().negate());

			newForecastList.add(actualTurnover);
			newForecastList.add(actualCost);
			newForecastList.add(turnover);
			newForecastList.add(cost);
			newForecastList.add(unTurnover);
			newForecastList.add(unCost);
			newForecastList.add(tenderRisk);
			newForecastList.add(tenderOpps);
			newForecastList.add(others);
			newForecastList.add(risk);
			newForecastList.add(opps);

			forecastService.saveList(jobNo, newForecastList);

		} catch (Exception e) {
			error = "Failed to calculate roc summary to monthly movement. Year: " + year + ", Month: " + month ;
			e.printStackTrace();
		}
		return error;
	}

	private Forecast findDbAndUpdateSumForecast(String jobNo, Integer year, Integer month, String forecastFlag, String forecastType, String forecastDesc, String rocCategory, List<RocSummaryWrapper> sumOfAmountExpectedGroupByRocCat, List<Forecast> forecastList) {
		Forecast forecast = findDbOrCreateNewForecast(jobNo, year, month, forecastFlag, forecastType, forecastDesc, forecastList);
		Optional<RocSummaryWrapper> dbSummary = sumOfAmountExpectedGroupByRocCat.stream().filter(x -> x.getRocCategory().equals(rocCategory)).findFirst();
		if (dbSummary.isPresent()) {
			forecast.setAmount(dbSummary.get().getSumAmountExpected());
		} else {
			forecast.setAmount(BigDecimal.valueOf(0));
		}
		return forecast;
	}

	private Forecast findDbOrCreateNewForecast(String jobNo, Integer year, Integer month, String forecastFlag, String forecastType, String forecastDesc, List<Forecast> forecastList) {
		Forecast forecast;
		Optional<Forecast> dbForecast = forecastList.stream().filter(x -> x.getForecastDesc().equals(forecastDesc)).findFirst();
		if (dbForecast.isPresent()) {
			forecast = dbForecast.get();
		} else {
			forecast = new Forecast(jobNo, year, month, forecastFlag, forecastType, forecastDesc, BigDecimal.valueOf(0));
		}
		return forecast;
	}

}

