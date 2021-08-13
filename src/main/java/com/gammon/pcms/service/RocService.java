package com.gammon.pcms.service;

import com.gammon.pcms.dao.RocClassDescMapRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.wrapper.RocSummaryWrapper;
import com.gammon.pcms.wrapper.RocWrapper;
import com.gammon.qs.service.admin.AdminService;
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
public class RocService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RocRepository rocRepository;

	@Autowired
	private RocDetailRepository rocDetailRepository;

	@Autowired
	private RocClassDescMapRepository rocClassDescMapRepository;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ForecastService forecastService;

	public List<RocWrapper> getRocWrapperList(String jobNo, int year, int month) {
		List<RocWrapper> result = new ArrayList<>();
		try {
			List<ROC> rocList = rocRepository.findByJobNo(jobNo);
			// Merge detail list to detail
			for(ROC roc: rocList) {
				// find current year month
				ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(roc.getId(), year, month);

				if (rocDetail == null) {
					rocDetail = new ROC_DETAIL();
					rocDetail.setYear(year);
					rocDetail.setMonth(month);
				}

				// find previous month
				int preYear = year;
				int preMonth = month -1;
				if(month == 1){
					preYear = year - 1;
					preMonth = 12;
				}
				ROC_DETAIL previousDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(roc.getId(), preYear, preMonth);
				if (previousDetail != null) {
					rocDetail.setPreviousAmountBest(previousDetail.getAmountBest());
					rocDetail.setPreviousAmountExpected(previousDetail.getAmountExpected());
					rocDetail.setPreviousAmountWorst(previousDetail.getAmountWorst());
				}
				RocWrapper rocWrapper = new RocWrapper(roc, rocDetail);
				result.add(rocWrapper);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<ROC_CLASS_DESC_MAP> getRocClassDescMap() {
		return rocClassDescMapRepository.findAll();
	}

	private String validateInputRoc(String noJob, ROC roc) {
		String error = "";

		if (roc == null || roc.getRocCategory() == null || roc.getClassification() == null
				|| roc.getImpact() == null || roc.getStatus() == null || roc.getDescription() == null)
			return "missing information";

		if (!noJob.equals(roc.getProjectNo()))
			return "job no. not match";

		List<String> rocCategoryList = ROC.getRocCategoryList();
		if (!rocCategoryList.contains(roc.getRocCategory()))
			return "roc category is not valid";

		// check if classification is valid
		List<ROC_CLASS_DESC_MAP> rocClassDescMap = getRocClassDescMap();
		Optional<ROC_CLASS_DESC_MAP> findElement = rocClassDescMap.stream().filter(x -> x.getClassification().equals(roc.getClassification())).findFirst();
		if (!findElement.isPresent())
			return "classification is not valid";

		List<String> impactList = ROC.getImpactList();
		if (!impactList.contains(roc.getImpact()))
			return "impact is not valid";

		List<String> statusList = ROC.getStatusList();
		if (!statusList.contains(roc.getStatus()))
			return "status is not valid";

		return error;
	}

	public String addRoc(String noJob, ROC roc) {
		String error = "";
		try {
			// perform roc validation
			String basicChecking = validateInputRoc(noJob, roc);
			if (!basicChecking.isEmpty())
				return basicChecking;

			ROC byProjectNoAndRocCategoryAndDescription = rocRepository.findByProjectNoAndRocCategoryAndDescription(roc.getProjectNo(), roc.getRocCategory(), roc.getDescription());
			if (byProjectNoAndRocCategoryAndDescription != null)
				return String.format("Description of %s is duplicated", byProjectNoAndRocCategoryAndDescription.getDescription());

			// form new roc and save to db
			ROC newRoc = new ROC(
					noJob,
					roc.getProjectRef(),
					roc.getRocCategory(),
					roc.getClassification(),
					roc.getImpact(),
					roc.getDescription(),
					roc.getStatus()
			);
			ROC result = rocRepository.save(newRoc);

		} catch (Exception e) {
			error = "ROC cannot be created";
			e.printStackTrace();
		}
		return error;
	}

	public String updateRoc(String noJob, ROC roc) {
		String error = "";
		try {
			// perform roc validation
			String basicChecking = validateInputRoc(noJob, roc);
			if (!basicChecking.isEmpty())
				return basicChecking;

			// find existing roc and update
			ROC newRoc = rocRepository.findOne(roc.getId());
			newRoc.setProjectRef(roc.getProjectRef());
			newRoc.setRocCategory(roc.getRocCategory());;
			newRoc.setClassification(roc.getClassification());
			newRoc.setImpact(roc.getImpact());
			newRoc.setStatus(roc.getStatus());
			newRoc.setDescription(roc.getDescription());
			ROC result = rocRepository.save(newRoc);

		} catch (Exception e) {
			error = "ROC cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String saveRocDetails(String noJob, List<RocWrapper> rocWrapperList) {
		String error = "";
		try {
			for (RocWrapper rocWrapper : rocWrapperList) {
				ROC roc = rocRepository.findOne(rocWrapper.getId());
				if (!noJob.equals(roc.getProjectNo()))
					return "Job no not match";
				ROC_DETAIL newRocDetail = rocWrapper.getRocDetail();

				newRocDetail.setRoc(roc);

				ROC_DETAIL result = rocDetailRepository.save(newRocDetail);

				// calculate roc sum to forecast
				String calResult = calculateRocSummaryToMonthlyMovement(result.getRoc().getProjectNo(), result.getYear(), result.getMonth());
				if (calResult != "")
					return calResult;
			}
		} catch (Exception e) {
			error = "ROC Detail cannot be saved";
			e.printStackTrace();
		}
		return error;
	}

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
			error = "Failed to calculate roc summary.";
			e.printStackTrace();
		}
		return error;
	}

	private Forecast findDbAndUpdateSumForecast(String jobNo, Integer year, Integer month, String forecastFlag, String forecastType, String forecastDesc, String rocCategory, List<RocSummaryWrapper> sumOfAmountExpectedGroupByRocCat, List<Forecast> forecastList) {
		Forecast forecast = findDbOrCreateNewForecast(jobNo, year, month, forecastFlag, forecastType, forecastDesc, forecastList);
		Optional<RocSummaryWrapper> dbSummary = sumOfAmountExpectedGroupByRocCat.stream().filter(x -> x.getRocCategory().equals(rocCategory)).findFirst();
		if (dbSummary.isPresent()) {
			forecast.setAmount(dbSummary.get().getSumAmountExpected());
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

	public String deleteRoc(ROC roc) {
		String error = "";
		try {
			logger.info("delete roc:" + roc);

			Long id = roc.getId();

			boolean isRocExist = rocRepository.exists(roc.getId());
			if (!isRocExist) return "ROC cannot be deleted";

			ROC dbRoc = rocRepository.getByID(roc.getId());
			List<ROC_DETAIL> rocDetails = dbRoc.getRocDetails();
			if (rocDetails != null || !rocDetails.isEmpty()) {
				return "ROC Detail exists";
			}

			String jobNo = dbRoc.getProjectNo();

			long itemDeleted = 0;
			if (adminService.canAccessJob(jobNo)) {
				itemDeleted = rocRepository.deleteByProjectNoAndId(jobNo, id);
				logger.info("no. of roc items deleted: " + itemDeleted);
			}
		} catch (Exception e) {
			error = "ROC cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}


	public List<String> getRocCategoryList() {
		return ROC.getRocCategoryList();
	}

	public List<String> getImpactList() {
		return ROC.getImpactList();
	}

	public List<String> getStatusList() {
		return ROC.getStatusList();
	}
}

