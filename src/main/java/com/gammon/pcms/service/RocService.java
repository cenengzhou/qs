package com.gammon.pcms.service;

import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.dao.RocClassDescMapRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.dao.RocSubdetailRepository;
import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.wrapper.RocSummaryWrapper;
import com.gammon.pcms.wrapper.RocWrapper;
import com.gammon.qs.dao.RocDetailHBDao;
import com.gammon.qs.dao.RocHBDao;
import com.gammon.qs.service.admin.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Transactional
@Service
public class RocService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RocRepository rocRepository;

	@Autowired
	private RocDetailRepository rocDetailRepository;

	@Autowired
	private RocSubdetailRepository rocSubdetailRepository;

	@Autowired
	private RocClassDescMapRepository rocClassDescMapRepository;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ForecastService forecastService;

	@Autowired
	private RocHBDao rocHBDao;

	@Autowired
	private RocDetailHBDao rocDetailHBDao;

	@Autowired
	private MessageConfig messageConfig;

	@Autowired
	private ApplicationConfig applicationConfig;

	public List<RocWrapper> getRocWrapperList(String jobNo, int year, int month) {
		if (jobNo.isEmpty() || !(month >= 1 && month <= 12))
			throw new IllegalArgumentException("invalid parameters");
		List<RocWrapper> result = new ArrayList<>();
		try {
			List<ROC> rocList = rocRepository.findByJobNo(jobNo);

			// filter roc by year month
			List<ROC> filterRocList = new ArrayList<>();
			for (ROC r : rocList) {
				YearMonth createdYearMonth = findYearMonthFromCutoffDate(r.getOpenDate());
				YearMonth inputYearMonth = YearMonth.of(year, month);
				if (inputYearMonth.isAfter(createdYearMonth) || inputYearMonth.equals(createdYearMonth)) {
					filterRocList.add(r);
				}
			}

			// Merge detail list to detail
			for(ROC roc: filterRocList) {
				// find current year month
				ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(roc.getId(), year, month);

				if (rocDetail == null) {
					YearMonth inputYearMonth = YearMonth.of(year, month).plusMonths(-1);
					Date startDate = findStartDate(inputYearMonth);
					Date endDate = findEndDate(inputYearMonth);
					boolean existSubdetail = rocSubdetailRepository.existsByRocIdAndStartDateAndEndDate(roc.getId(), startDate, endDate);
					if (existSubdetail) {
						calculateRocDetailAmountAndMonthlyMovement(jobNo, roc.getId());
						rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(roc.getId(), year, month);
					} else {
						rocDetail = new ROC_DETAIL(year, month);
					}
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

			// assign number to roc list (special requirement)
			Long assignedNo = new Long(1);
			for (RocWrapper rocWrapper : result) {
				rocWrapper.setAssignedNo(assignedNo);
				assignedNo += 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public List<ROC_SUBDETAIL> getRocSubdetailList(String jobNo, Long rocId) {
		List<ROC_SUBDETAIL> result = new ArrayList<>();
		if (rocId != null) {
			result = rocSubdetailRepository.findByRocId(rocId);
			Long counter = 1L;
			for (ROC_SUBDETAIL subdetail : result) {
				subdetail.setAssignedNo(counter);
				counter++;

				subdetail.setEditable(compareYearMonthFromCutOffDate(subdetail.getInputDate(), new Date()));
			}
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

	private Date toCutOffDate(Date inputDate) {
		int rocCutoffDate = Integer.parseInt(messageConfig.getRocCutoffDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inputDate);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (day <= rocCutoffDate) {
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
		}
		return calendar.getTime();
	}

	public String addRoc(String noJob, ROC roc) {
		String error = "";
		try {
			// perform roc validation
			String basicChecking = validateInputRoc(noJob, roc);
			if (!basicChecking.isEmpty())
				return basicChecking;

			ROC byProjectNoAndRocCategoryAndDescription = rocRepository.findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(roc.getProjectNo(), roc.getRocCategory(), roc.getDescription(), "ACTIVE");
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
					roc.getStatus(),
					roc.getRocOwner(),
					new Date(),
					roc.getStatus().equals(ROC.CLOSED) ? new Date() : null
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
			ROC dbRoc = rocRepository.findOne(roc.getId());
			dbRoc.setProjectRef(roc.getProjectRef());
			dbRoc.setRocCategory(roc.getRocCategory());;
			dbRoc.setClassification(roc.getClassification());
			dbRoc.setImpact(roc.getImpact());

			// handle status change (Live or Closed)
			String oldStatus = dbRoc.getStatus();
			String newStatus = roc.getStatus();
			if (!oldStatus.equals(newStatus)) {
				if (newStatus.equals(ROC.CLOSED)) {
					dbRoc.setClosedDate(new Date());

					YearMonth todayYearMonth = findYearMonthFromCutoffDate(new Date());
					// zero out figures
					ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(dbRoc.getId(), todayYearMonth.getYear(), todayYearMonth.getMonthValue());
					if (rocDetail != null) {
						rocDetail.setAmountBest(BigDecimal.valueOf(0));
						rocDetail.setAmountExpected(BigDecimal.valueOf(0));
						rocDetail.setAmountWorst(BigDecimal.valueOf(0));
						rocDetailRepository.save(rocDetail);

						// calculate monthly movement
						String calculateMonthlyMovement = calculateRocSummaryToMonthlyMovement(noJob, todayYearMonth.getYear(), todayYearMonth.getMonthValue());
						if (!calculateMonthlyMovement.equals(""))
							return calculateMonthlyMovement;
					}

				} else {
					if (!compareYearMonthFromCutOffDate(new Date(), dbRoc.getClosedDate())) {
						return "Cannot reopen other month period";
					}
					dbRoc.setClosedDate(null);
					calculateRocDetailAmountAndMonthlyMovement(noJob, dbRoc.getId());
				}
				dbRoc.setStatus(roc.getStatus());
			}

			dbRoc.setDescription(roc.getDescription());
			dbRoc.setRocOwner(roc.getRocOwner());

			ROC result = rocRepository.save(dbRoc);

		} catch (Exception e) {
			error = "ROC cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	private boolean compareYearMonthFromCutOffDate(Date date1, Date date2) {
		YearMonth a = findYearMonthFromCutoffDate(date1);
		YearMonth b = findYearMonthFromCutoffDate(date2);
		return a.getYear() == b.getYear() && a.getMonthValue() == b.getMonthValue();
	}

	public String updateRocAdmin(String noJob, ROC roc) {
		String error = "";
		try {
			// perform roc validation
			String basicChecking = validateInputRoc(noJob, roc);
			if (!basicChecking.isEmpty())
				return basicChecking;

			// find existing roc and update
			ROC newRoc = rocRepository.findOne(roc.getId());

			newRoc.setProjectRef(roc.getProjectRef());
			newRoc.setRocCategory(roc.getRocCategory());
			newRoc.setClassification(roc.getClassification());
			newRoc.setImpact(roc.getImpact());
			newRoc.setDescription(roc.getDescription());
			newRoc.setStatus(roc.getStatus());
			newRoc.setRocOwner(roc.getRocOwner());
			newRoc.setClosedDate(roc.getClosedDate());

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
				if (roc.getStatus().equals(ROC.CLOSED)) {
					return "ROC is closed";
				}
				if (!noJob.equals(roc.getProjectNo()))
					return "Job no not match";
				ROC_DETAIL inputDetail = rocWrapper.getRocDetail();
				Long id = inputDetail.getId();
				ROC_DETAIL newRocDetail =
						id == null ?
								new ROC_DETAIL(inputDetail.getYear(), inputDetail.getMonth())
								: rocDetailRepository.findOne(id);
				newRocDetail.setRemarks(inputDetail.getRemarks());
				newRocDetail.setRoc(roc);

				ROC_DETAIL result = rocDetailRepository.save(newRocDetail);

			}
		} catch (Exception e) {
			error = "ROC Detail cannot be saved";
			e.printStackTrace();
		}
		return error;
	}

	private YearMonth findYearMonthFromCutoffDate(Date date) {
		return YearMonth.from(toCutOffDate(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public String saveSubdetailList(String jobNo, Long rocId, Long detailId, List<ROC_SUBDETAIL> subdetailList) {
		String result = "";
		try {
			ROC roc = rocRepository.getByID(rocId);
			if (roc.getStatus().equals(ROC.CLOSED)) {
				return "ROC is closed";
			}

			// invalid period for editing (must be the same period for delete)
			YearMonth todayYearMonth = findYearMonthFromCutoffDate(new Date());
			for (ROC_SUBDETAIL s : subdetailList) {
				if (s.getUpdateType().equals(ROC_SUBDETAIL.ADD))
					continue;
				YearMonth inputDateYearMonth = findYearMonthFromCutoffDate(s.getInputDate());
				if (!(todayYearMonth.getYear() == inputDateYearMonth.getYear() && todayYearMonth.getMonthValue() == inputDateYearMonth.getMonthValue()))
					return "Cannot edit other month period";
			}

			// create profile
			if (detailId == null && subdetailList != null && subdetailList.size() > 0) {
				ROC_DETAIL rocDetail = new ROC_DETAIL(todayYearMonth.getYear(), todayYearMonth.getMonthValue());
				rocDetail.setRoc(roc);
				ROC_DETAIL createdProfile = rocDetailRepository.save(rocDetail);
				detailId = createdProfile.getId();
				result = String.valueOf(detailId);
			}

			// handle changes
			List<ROC_SUBDETAIL> changeList = new ArrayList<>();
			for (ROC_SUBDETAIL subdetail: subdetailList) {
				String updateType = subdetail.getUpdateType().toUpperCase();
				if (updateType.equals(ROC_SUBDETAIL.ADD)) {
					// handle add item
					ROC_SUBDETAIL newRecord = new ROC_SUBDETAIL(subdetail);
					newRecord.setRoc(roc);
					changeList.add(newRecord);
				} else if (updateType.equals(ROC_SUBDETAIL.DELETE)) {
					// handle delete item
					ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(subdetail.getId());
					dbRecord.inactivate();
					dbRecord.setRoc(roc);
					changeList.add(dbRecord);
				} else {
					// handle update item
					ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(subdetail.getId());
					dbRecord.setDescription(subdetail.getDescription());
					dbRecord.setAmountBest(subdetail.getAmountBest());
					dbRecord.setAmountExpected(subdetail.getAmountExpected());
					dbRecord.setAmountWorst(subdetail.getAmountWorst());
					dbRecord.setHyperlink(subdetail.getHyperlink());
					dbRecord.setRemarks(subdetail.getRemarks());
					dbRecord.setRoc(roc);
					changeList.add(dbRecord);
				}
			}

			rocSubdetailRepository.save(changeList);

			// calculate roc detail (from start to current month period)
			String calculateRoc = calculateRocDetailAmountAndMonthlyMovement(jobNo, roc.getId());
			if (!calculateRoc.equals(""))
				return calculateRoc;

		} catch (Exception e) {
			result = "ROC Subdetail cannot be saved";
			e.printStackTrace();
		}
		return result;
	}

	public String calculateRocDetailAmountAndMonthlyMovement(String jobNo, Long rocId) {
		String error = "";
		try {
			ROC roc = rocRepository.getByID(rocId);
			YearMonth startYM = findYearMonthFromCutoffDate(roc.getOpenDate());
			YearMonth endYM = findYearMonthFromCutoffDate(roc.getClosedDate() == null ? new Date() : roc.getClosedDate());

			// TODO: remove logger if stable
			if (applicationConfig.getDeployEnvironment().equals("LOC")) {
				logger.info("ROC ID: " + rocId);
				logger.info("Job Open Date: " + roc.getOpenDate() + ", Job Closed Date: " + roc.getClosedDate());
				logger.info("Job Start Period: " + startYM.toString() + ", Job End Period: " + endYM.toString());
			}

			List<ROC_DETAIL> saveList = new ArrayList<>();

			for(YearMonth period = startYM; period.isBefore(endYM) || period.equals(endYM); period = period.plusMonths(1)) {
				Date periodStart = findStartDate(startYM);
				Date periodEnd = findEndDate(period);

				// find sum from subdetail and update to detail
				ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(rocId, period.getYear(), period.getMonthValue());
				if (rocDetail == null) {
					rocDetail = new ROC_DETAIL(period.getYear(), period.getMonthValue());
					rocDetail.setRoc(roc);
					rocDetail = rocDetailRepository.save(rocDetail);
				}
				ROC_SUBDETAIL summary = rocSubdetailRepository.findSumByRocId(roc.getId(), periodStart, periodEnd);
				rocDetail.setAmountBest(summary.getAmountBest());
				rocDetail.setAmountExpected(summary.getAmountExpected());
				rocDetail.setAmountWorst(summary.getAmountWorst());

				ROC_DETAIL saveRocDetail = rocDetailRepository.save(rocDetail);

				// TODO: remove logger if stable
				if (applicationConfig.getDeployEnvironment().equals("LOC")) {
					logger.info("Period Start: " + periodStart.toString() + ", Job End: " + periodEnd.toString(), "Best: " + saveRocDetail.getAmountBest(), "Expected: " + saveRocDetail.getAmountExpected(), "Worst: " + saveRocDetail.getAmountWorst());
				}

				// calculate monthly movement
				String calculateMonthlyMovement = calculateRocSummaryToMonthlyMovement(jobNo, period.getYear(), period.getMonthValue());
				if (!calculateMonthlyMovement.equals(""))
					return calculateMonthlyMovement;
			}

		} catch (Exception e) {
			error = "Failed to calculate roc amount. Job No: "+jobNo+", ROC id: " + rocId;
			e.printStackTrace();
		}
		return error;
	}

	private Date findEndDate(YearMonth period) {
		int cutoffDate = Integer.parseInt(messageConfig.getRocCutoffDate());
		return Date.from(LocalDate.of(period.getYear(), period.getMonthValue(), cutoffDate).plusMonths(1).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private Date findStartDate(YearMonth startYM) {
		int cutoffDate = Integer.parseInt(messageConfig.getRocCutoffDate());
		return Date.from(LocalDate.of(startYM.getYear(), startYM.getMonthValue(), cutoffDate).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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

	public List<String> getRocCategoryList() {
		return ROC.getRocCategoryList();
	}

	public List<String> getImpactList() {
		return ROC.getImpactList();
	}

	public List<String> getStatusList() {
		return ROC.getStatusList();
	}

	public List<ROC> getRocHistory(Long rocId) {
		if (rocId == null || rocId < 0)
			throw new IllegalArgumentException("invalid roc id");
		List<ROC> auditHistory = rocHBDao.getAuditHistory(rocId);
		List<ROC> uniqleResult = auditHistory.stream().filter(distinctByKey(x -> x.getLastModifiedDate())).collect(Collectors.toList());
		return uniqleResult;
	}

	public List<ROC_DETAIL> getRocDetailHistory(Long rocDetailId) {
		if (rocDetailId == null || rocDetailId < 0)
			throw new IllegalArgumentException("invalid roc detail id");
		List<ROC_DETAIL> auditHistory = rocDetailHBDao.getAuditHistory(rocDetailId);
		List<ROC_DETAIL> uniqleResult = auditHistory.stream().filter(distinctByKey(x -> x.getLastModifiedDate())).collect(Collectors.toList());
		return uniqleResult;
	}

	private static <T> Predicate<T> distinctByKey(
			Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public ROC getRocAdmin(String jobNo, String rocCategory, String description) {
		if (jobNo.isEmpty() || description.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		ROC roc = rocRepository.findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(jobNo, rocCategory, description, "ACTIVE");
		return roc;
	}

	public List<ROC_DETAIL> getRocDetailListAdmin(String jobNo, String rocCategory, String description) {
		List<ROC_DETAIL> result = new ArrayList<>();
		if (jobNo.isEmpty() || rocCategory.isEmpty() || description.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		ROC roc = rocRepository.findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(jobNo, rocCategory, description, "ACTIVE");
		if (roc != null)
			result = rocDetailRepository.findRocDetailsByRocIdAdmin(roc.getId());
		return result;
	}

	public List<ROC_SUBDETAIL> getRocSubdetailListAdmin(String jobNo, String rocCategory, String description) {
		List<ROC_SUBDETAIL> result = new ArrayList<>();
		if (jobNo.isEmpty() || rocCategory.isEmpty() || description.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		ROC roc = rocRepository.findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(jobNo, rocCategory, description, "ACTIVE");
		if (roc != null)
			result = rocSubdetailRepository.findByRocId(roc.getId());
		return result;
	}

	public String updateRocDetailListAdmin(String jobNo, List<ROC_DETAIL> rocDetailList) {
		if (jobNo.isEmpty() || rocDetailList == null || rocDetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_DETAIL> saveList = new ArrayList<>();
			for (ROC_DETAIL rocDetail : rocDetailList) {
				ROC_DETAIL dbRecord = rocDetailRepository.findOne(rocDetail.getId());
				dbRecord.setAmountBest(rocDetail.getAmountBest());
				dbRecord.setAmountExpected(rocDetail.getAmountExpected());
				dbRecord.setAmountWorst(rocDetail.getAmountWorst());
				dbRecord.setRemarks(rocDetail.getRemarks());
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocDetailRepository.save(saveList);
			}
		} catch (Exception e) {
			error = "ROC Detail cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String updateRocSubdetailListAdmin(String jobNo, List<ROC_SUBDETAIL> rocSubdetailList) {
		if (jobNo.isEmpty() || rocSubdetailList == null || rocSubdetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_SUBDETAIL> saveList = new ArrayList<>();
			for (ROC_SUBDETAIL rocSubdetail : rocSubdetailList) {
				ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(rocSubdetail.getId());
				dbRecord.setDescription(rocSubdetail.getDescription());
				dbRecord.setAmountBest(rocSubdetail.getAmountBest());
				dbRecord.setAmountExpected(rocSubdetail.getAmountExpected());
				dbRecord.setAmountWorst(rocSubdetail.getAmountWorst());
				dbRecord.setHyperlink(rocSubdetail.getHyperlink());
				dbRecord.setInputDate(rocSubdetail.getInputDate());
				dbRecord.setRemarks(rocSubdetail.getRemarks());
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocSubdetailRepository.save(saveList);
			}
		} catch (Exception e) {
			error = "ROC Subdetail cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteRocDetailListAdmin(String jobNo, List<ROC_DETAIL> rocDetailList) {
		if (jobNo.isEmpty() || rocDetailList == null || rocDetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_DETAIL> saveList = new ArrayList<>();
			for (ROC_DETAIL rocDetail : rocDetailList) {
				ROC_DETAIL dbRecord = rocDetailRepository.findOne(rocDetail.getId());
				dbRecord.inactivate();
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocDetailRepository.save(saveList);
			}
		} catch (Exception e) {
			error = "ROC Detail cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteRocSubdetailListAdmin(String jobNo, List<ROC_SUBDETAIL> rocSubdetailList) {
		if (jobNo.isEmpty() || rocSubdetailList == null || rocSubdetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_SUBDETAIL> saveList = new ArrayList<>();
			for (ROC_SUBDETAIL subdetail : rocSubdetailList) {
				ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(subdetail.getId());
				dbRecord.inactivate();
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocSubdetailRepository.save(saveList);
			}
		} catch (Exception e) {
			error = "ROC Subdetail cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteRocAdmin(String jobNo, ROC roc) {
		if (jobNo.isEmpty() || roc == null)
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			ROC dbRecord = rocRepository.findOne(roc.getId());
			if (dbRecord != null) {
				dbRecord.inactivate();
				rocRepository.save(dbRecord);
			}
		} catch (Exception e) {
			error = "ROC cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}

	public int getCutoffDate() {
		return Integer.parseInt(messageConfig.getRocCutoffDate());
	}
}

