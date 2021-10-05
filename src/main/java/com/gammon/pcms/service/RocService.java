package com.gammon.pcms.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.dao.RocClassDescMapRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.dao.RocSubdetailRepository;
import com.gammon.pcms.dto.IRocDetailJasperWrapper;
import com.gammon.pcms.dto.RocAmountWrapper;
import com.gammon.pcms.dto.RocCaseWrapper;
import com.gammon.pcms.dto.RocDetailJasperWrapper;
import com.gammon.pcms.dto.RocJasperWrapper;
import com.gammon.pcms.helper.FileHelper;
import com.gammon.pcms.helper.RocDateUtils;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.wrapper.RocWrapper;
import com.gammon.qs.dao.RocDetailHBDao;
import com.gammon.qs.dao.RocHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.util.JasperReportHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

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
	private RocIntegrationService rocIntegrationService;

	@Autowired
	private RocHBDao rocHBDao;

	@Autowired
	private RocDetailHBDao rocDetailHBDao;

	@Autowired
	private MessageConfig messageConfig;
	
	@Autowired
	private transient JasperConfig jasperConfig;

	@Autowired
	private JobInfoService jobInfoService;

	private static String noDataMarker = "NO DATA";

	public List<RocWrapper> getRocWrapperList(String jobNo, int year, int month) {
		if (jobNo.isEmpty() || !(month >= 1 && month <= 12))
			throw new IllegalArgumentException("invalid parameters");
		List<RocWrapper> result = new ArrayList<>();
		try {
			List<ROC> rocList = rocRepository.findByJobNo(jobNo);
			YearMonth inputYearMonth = YearMonth.of(year, month);

			// filter roc by year month
			List<ROC> filterRocList = new ArrayList<>();
			for (ROC r : rocList) {
				YearMonth createdYearMonth = RocDateUtils.findYearMonthFromCutoffDate(r.getOpenDate(), getCutoffDate());
				if (inputYearMonth.isAfter(createdYearMonth) || inputYearMonth.equals(createdYearMonth)) {
					if (r.getStatus().equals(ROC.CLOSED)) {
						YearMonth closedYearMonth = RocDateUtils.findYearMonthFromCutoffDate(r.getClosedDate(), getCutoffDate());
						if (inputYearMonth.isAfter(closedYearMonth))
							continue;
					}
					filterRocList.add(r);
				}
			}

			// Merge detail list to detail
			for(ROC roc: filterRocList) {
				ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(roc.getId(), year, month);
				if (rocDetail == null) {
					// mirror last record
					rocDetail = findDetailBackwardOrCreateOne(year, month, roc);
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

	private ROC_DETAIL findDetailBackwardOrCreateOne(int year, int month, ROC roc) {
		ROC_DETAIL rocDetail;
		rocDetail = new ROC_DETAIL(year, month, roc);

		// find figures backward and fill in the row
		List<ROC_DETAIL> findDetails = rocDetailRepository.findByRocIdAndYearMonthOrderByYearDescAndMonthDesc(roc.getId(), year, month);
		if (!findDetails.isEmpty()) {
			ROC_DETAIL latest = findDetails.get(0);
			rocDetail.setAmountBest(latest.getAmountBest());
			rocDetail.setAmountExpected(latest.getAmountExpected());
			rocDetail.setAmountWorst(latest.getAmountWorst());
			rocDetail.setRemarks(latest.getRemarks());
		}
		return rocDetail;
	}


	public List<ROC_SUBDETAIL> getRocSubdetailList(String jobNo, Long rocId, int year, int month) {
		List<ROC_SUBDETAIL> result = new ArrayList<>();
		if (rocId != null) {
			result = rocSubdetailRepository.findByRocId(rocId);
			Long counter = 1L;
			for (ROC_SUBDETAIL subdetail : result) {
				subdetail.setAssignedNo(counter);
				counter++;

				subdetail.setEditable(
						RocDateUtils.compareYearMonthPeriod(
								YearMonth.of(year, month),
								YearMonth.of(subdetail.getYear(), subdetail.getMonth())
						)
				);
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

	public String addRoc(String noJob, ROC roc, ROC_DETAIL rocDetail) {
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
			ROC topByProjectNoOrderByItemNoDesc = rocRepository.findTopByProjectNoOrderByItemNoDesc(noJob);
			Long itemNo = topByProjectNoOrderByItemNoDesc == null ? 1 : topByProjectNoOrderByItemNoDesc.getItemNo() + 1;
			ROC newRoc = new ROC(
					noJob,
					itemNo,
					roc.getProjectRef(),
					roc.getRocCategory(),
					roc.getClassification(),
					roc.getImpact(),
					roc.getDescription(),
					roc.getStatus(),
					roc.getRocOwner(),
					roc.getOpenDate(),
					roc.getStatus().equals(ROC.CLOSED) ? findClosedPeriod() : null
			);
			ROC newRocResult = rocRepository.save(newRoc);

			// create detail
			YearMonth yearMonth = RocDateUtils.findYearMonthFromCutoffDate(roc.getOpenDate(), getCutoffDate());
			ROC_DETAIL newRocDetail = new ROC_DETAIL(yearMonth.getYear(), yearMonth.getMonthValue(), newRocResult);
			if (rocDetail != null && rocDetail.getRemarks() != null && !rocDetail.getRemarks().isEmpty()) {
				newRocDetail.setRemarks(rocDetail.getRemarks());
			}
			rocDetailRepository.save(newRocDetail);

		} catch (Exception e) {
			error = "ROC cannot be created";
			e.printStackTrace();
		}
		return error;
	}

	private Date findClosedPeriod() {
		return Date.from(YearMonth.now().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
			String rocStatusChange = handleRocStatusChange(noJob, roc, dbRoc);
			if (rocStatusChange != "") return rocStatusChange;

			dbRoc.setDescription(roc.getDescription());
			dbRoc.setRocOwner(roc.getRocOwner());

			ROC result = rocRepository.save(dbRoc);

		} catch (Exception e) {
			error = "ROC cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	private String handleRocStatusChange(String noJob, ROC roc, ROC dbRoc) {
		String oldStatus = dbRoc.getStatus();
		String newStatus = roc.getStatus();
		YearMonth todayYearMonth = RocDateUtils.findYearMonthFromCutoffDate(today(), getCutoffDate());
		ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(dbRoc.getId(), todayYearMonth.getYear(), todayYearMonth.getMonthValue());
		if (rocDetail == null) {
			rocDetail = findDetailBackwardOrCreateOne(todayYearMonth.getYear(), todayYearMonth.getMonthValue(), roc);
		}
		if (!oldStatus.equals(newStatus)) {
			if (newStatus.equals(ROC.CLOSED)) {
				dbRoc.setClosedDate(findClosedPeriod());

				// zero out figures
				rocDetail.setAmountBest(BigDecimal.valueOf(0));
				rocDetail.setAmountExpected(BigDecimal.valueOf(0));
				rocDetail.setAmountWorst(BigDecimal.valueOf(0));
				rocDetail.setStatus(ROC.CLOSED);
				rocDetailRepository.save(rocDetail);

				rocSubdetailRepository.updateSubdetailToZeroByRocIdAndPeriod(dbRoc.getId(), todayYearMonth.getYear(), todayYearMonth.getMonthValue());

			} else {
				if (!RocDateUtils.compareDatePeriodCutoff(today(), dbRoc.getClosedDate(), getCutoffDate())) {
					return "Cannot reopen other month period";
				}
				dbRoc.setClosedDate(null);
				rocDetail.setStatus(ROC.LIVE);
				calculateRocDetailAmount(dbRoc.getId(), todayYearMonth);
//				calculateRocDetailAmountAndMonthlyMovementByPeriod(noJob, dbRoc.getId(), todayYearMonth.getYear(), todayYearMonth.getMonthValue());
			}
			dbRoc.setStatus(roc.getStatus());
		}
		return "";
	}

	public String saveRocDetails(String noJob, List<RocWrapper> rocWrapperList) {
		String error = "";
		try {
			for (RocWrapper rocWrapper : rocWrapperList) {
				Long rocId = rocWrapper.getId();
				ROC_DETAIL rocDetailWrapper = rocWrapper.getRocDetail();
//				YearMonth todayYearMonth = RocDateUtils.findYearMonthFromCutoffDate(today(), getCutoffDate());
				YearMonth yearMonth = YearMonth.of(rocDetailWrapper.getYear(), rocDetailWrapper.getMonth());

				// handle ROC (add/ update)
				if (rocWrapper.getUpdateType().equals("ADD")) {
					ROC newRoc = new ROC(
							noJob,
							null,
							rocWrapper.getProjectRef(),
							rocWrapper.getRocCategory(),
							rocWrapper.getClassification(),
							rocWrapper.getImpact(),
							rocWrapper.getDescription(),
							rocWrapper.getStatus(),
							rocWrapper.getRocOwner(),
							rocWrapper.getOpenDate(),
							null
					);
					String s = addRoc(noJob, newRoc, rocWrapper.getRocDetail());
					if (s != "") return s;

				} else if (rocWrapper.getUpdateType().equals("UPDATE")) {
					ROC roc = rocRepository.findOne(rocId);
					ROC newRoc = new ROC(
							rocId,
							noJob,
							rocWrapper.getItemNo(),
							rocWrapper.getProjectRef(),
							rocWrapper.getRocCategory(),
							rocWrapper.getClassification(),
							rocWrapper.getImpact(),
							rocWrapper.getDescription(),
							rocWrapper.getStatus(),
							null,
							null,
							null,
							null,
							rocWrapper.getRocOwner(),
							rocWrapper.getOpenDate(),
							rocWrapper.getClosedDate()
					);
					String s = updateRoc(noJob, newRoc);
					if (s != "") return s;

					// handle ROC detail
					ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(rocId, yearMonth.getYear(), yearMonth.getMonthValue());
					if (rocDetail == null) {
						rocDetail = findDetailBackwardOrCreateOne(yearMonth.getYear(), yearMonth.getMonthValue(), roc);
					}

					rocDetail.setRemarks(rocDetailWrapper.getRemarks());

					ROC_DETAIL result = rocDetailRepository.save(rocDetail);
				}

			}
		} catch (Exception e) {
			error = "ROC Detail cannot be saved";
			e.printStackTrace();
		}
		return error;
	}

	public String saveSubdetailList(String jobNo, Long rocId, List<ROC_SUBDETAIL> subdetailList, int year, int month) {
		String result = "";
		try {
			ROC roc = rocRepository.getByID(rocId);
			if (roc.getStatus().equals(ROC.CLOSED)) {
				return "ROC is closed";
			}

			// handle changes
			List<ROC_SUBDETAIL> changeList = new ArrayList<>();
			for (ROC_SUBDETAIL subdetail: subdetailList) {
				String updateType = subdetail.getUpdateType().toUpperCase();
				if (updateType.equals(ROC_SUBDETAIL.ADD)) {
					// handle add item
					ROC_SUBDETAIL newRecord = new ROC_SUBDETAIL(subdetail);
//					newRecord.setYear(todayYearMonth.getYear());
//					newRecord.setMonth(todayYearMonth.getMonthValue());
					newRecord.setYear(subdetail.getYear());
					newRecord.setMonth(subdetail.getMonth());
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

					dbRecord.setYear(subdetail.getYear());
					dbRecord.setMonth(subdetail.getMonth());

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

			// calculate roc detail
			String calculateRoc = calculateRocDetailAmountAndMonthlyMovementByPeriod(jobNo, roc.getId(), year, month);
			if (!calculateRoc.equals(""))
				return calculateRoc;

		} catch (Exception e) {
			result = "ROC Subdetail cannot be saved";
			e.printStackTrace();
		}
		return result;
	}


	public String calculateRocDetailAmountAndMonthlyMovementByPeriod(String jobNo, Long rocId, int year, int month) {
		String error = "";
		try {
//			YearMonth yearMonth = RocDateUtils.findYearMonthFromCutoffDate(today(), getCutoffDate());
			YearMonth yearMonth = YearMonth.of(year, month);

			calculateRocDetailAmount(rocId, yearMonth);

			// calculate monthly movement
			String calculateMonthlyMovement = rocIntegrationService.calculateRocSummaryToMonthlyMovement(jobNo, yearMonth.getYear(), yearMonth.getMonthValue());
			if (!calculateMonthlyMovement.equals(""))
				return calculateMonthlyMovement;

		} catch (Exception e) {
			error = "Failed to calculate roc amount. Job No: "+jobNo+", ROC id: " + rocId;
			e.printStackTrace();
		}
		return error;
	}

	private void calculateRocDetailAmount(Long rocId, YearMonth yearMonth) {
		ROC roc = rocRepository.getByID(rocId);
		// check if roc detail exist, create one if not
		ROC_DETAIL rocDetail = rocDetailRepository.findDetailByRocIdAndYearMonth(rocId, yearMonth.getYear(), yearMonth.getMonthValue());
		if (rocDetail == null) {
			rocDetail = new ROC_DETAIL(yearMonth.getYear(), yearMonth.getMonthValue(), roc);
			rocDetail = rocDetailRepository.save(rocDetail);
		}

		// find sum by the period
		ROC_SUBDETAIL summary = rocSubdetailRepository.findSumByRocIdAndEndPeriod(roc.getId(), yearMonth.getYear(), yearMonth.getMonthValue());
		rocDetail.setAmountBest(summary.getAmountBest());
		rocDetail.setAmountExpected(summary.getAmountExpected());
		rocDetail.setAmountWorst(summary.getAmountWorst());

		ROC_DETAIL saveRocDetail = rocDetailRepository.save(rocDetail);

	}

	public List<String> getRocCategoryList() {return ROC.getRocCategoryList();}

	public List<String> getImpactList() {return ROC.getImpactList();}

	public List<String> getStatusList() {return ROC.getStatusList();}

	public List<ROC> getRocHistory(Long rocId) {
		if (rocId == null || rocId < 0)
			throw new IllegalArgumentException("invalid roc id");
		List<ROC> auditHistory = rocHBDao.getAuditHistory(rocId);
		List<ROC> uniqleResult = auditHistory.stream().filter(RocDateUtils.distinctByKey(x -> x.getLastModifiedDate())).collect(Collectors.toList());
		return uniqleResult;
	}

	public List<ROC_DETAIL> getRocDetailHistory(Long rocDetailId) {
		if (rocDetailId == null || rocDetailId < 0)
			throw new IllegalArgumentException("invalid roc detail id");
		List<ROC_DETAIL> auditHistory = rocDetailHBDao.getAuditHistory(rocDetailId);
		List<ROC_DETAIL> uniqleResult = auditHistory.stream().filter(RocDateUtils.distinctByKey(x -> x.getLastModifiedDate())).collect(Collectors.toList());
		return uniqleResult;
	}

	public int getCutoffDate() {return Integer.parseInt(messageConfig.getRocCutoffDate());}

	public String recalculateRoc(String jobNo, int year, int month) {
		String error = "";
		try {
			if (jobNo.isEmpty())
				throw new IllegalArgumentException("invalid parameters");
			List<ROC> rocList = rocRepository.findByJobNo(jobNo);
			for (ROC roc : rocList) {
				String s =  calculateRocDetailAmountAndMonthlyMovementByPeriod(jobNo, roc.getId(), year, month);
				if (!s.equals(""))
					return s;
			}
		} catch (Exception e) {
			error = "ROC cannot be recalculated";
			e.printStackTrace();
		}
		return error;
	}

	public Date today() {
		return new Date();
	}
	
	public ByteArrayOutputStream GenerateRocReport(String jobNumber, int year, int month, String format) {
		String templateName = jasperConfig.getReportRoc();
		RocJasperWrapper wrapper = this.prepareRocJasperWrapper(templateName, jobNumber, year, month);
		List<RocJasperWrapper> reportWrapperList = Arrays.asList(wrapper);
		Map<String, Object> parameters = new HashMap<String, Object>();


		try {
			JasperReportHelper reportHelper = JasperReportHelper.get()
					.setCurrentReport(
						reportWrapperList,
						FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath() + templateName,
						parameters
					)
					.compileAndAddReport();
			switch(format.toLowerCase()){
				case "xlsx":
					SimpleXlsxReportConfiguration xlsxReportConfig = new SimpleXlsxReportConfiguration();
					xlsxReportConfig.setOnePagePerSheet(true);
					xlsxReportConfig.setDetectCellType(true);
					xlsxReportConfig.setWhitePageBackground(true);
					xlsxReportConfig.setRemoveEmptySpaceBetweenRows(false);
					xlsxReportConfig.setFontSizeFixEnabled(true);
					xlsxReportConfig.setIgnorePageMargins(false);
					xlsxReportConfig.setCollapseRowSpan(false);
					xlsxReportConfig.setIgnoreGraphics(false);
					xlsxReportConfig.setRemoveEmptySpaceBetweenColumns(false);
					return reportHelper.exportAsExcel(new String[] { "Summary", "Appendix 5a Contingencies", "Appendix 5b Risk & Opps" }, xlsxReportConfig);
				case "pdf":
					return reportHelper.exportAsPDF();
				default:
					throw new FileNotFoundException("format " + format + " not supported");
			}
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
	}

	private RocJasperWrapper prepareRocJasperWrapper(String templateName, String jobNumber, int year, int month) {
		JobInfo job;
		try{
			job = jobInfoService.obtainJob(jobNumber);
		} catch (Exception e) {
			logger.error("error", e);
			throw new IllegalArgumentException("cannot generate RocJasperWrapper: " + jobNumber + "-" + year + "-" + month);
		}
		RocJasperWrapper wrapper = new RocJasperWrapper();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		wrapper.setAsAtDate(new SimpleDateFormat("MMM-yyyy").format(calendar.getTime()));
		wrapper.setProjectNumber(jobNumber);
		wrapper.setProjectName(job.getDescription());

		List<IRocDetailJasperWrapper> currentList = rocRepository.getRocJasperWrapper(jobNumber, year, month);
		List<IRocDetailJasperWrapper> previousList = rocRepository.getRocJasperWrapper(jobNumber, year, month - 1);

		wrapper.setSumCaseTenderRisks(generateRocCaseWrapper(ROC.TENDER_RISK, currentList, previousList));
		wrapper.setSumCaseTenderOpps(generateRocCaseWrapper(ROC.TENDER_OPPS, currentList, previousList));
		wrapper.setSumCaseTenderOther(generateRocCaseWrapper(ROC.CONTINGENCY, currentList, previousList));
		wrapper.setSumCaseRisks(generateRocCaseWrapper(ROC.RISK, currentList, previousList));
		wrapper.setSumCaseOpps(generateRocCaseWrapper(ROC.OPPS, currentList, previousList));

		List<IRocDetailJasperWrapper> detailsTenderRisks = this.filterRocDetailsList(ROC.TENDER_RISK, currentList);
		List<IRocDetailJasperWrapper> detailsTenderOppss = this.filterRocDetailsList(ROC.TENDER_OPPS, currentList);
		List<IRocDetailJasperWrapper> detailsTenderOther =  this.filterRocDetailsList(ROC.CONTINGENCY, currentList);
		List<IRocDetailJasperWrapper> detailsRisks = this.filterRocDetailsList(ROC.RISK, currentList);
		List<IRocDetailJasperWrapper> detailsOpps = this.filterRocDetailsList(ROC.OPPS, currentList);
		
		switch (templateName) {
			case "RisksOppsContingenciesCombineDetail":
				Comparator<IRocDetailJasperWrapper> compareByCategoryId = Comparator
				.comparing(IRocDetailJasperWrapper::getCategory).reversed()
				.thenComparing(IRocDetailJasperWrapper::getRocId);
				
				List<IRocDetailJasperWrapper> detailsContingencies = Stream
				.of(detailsTenderRisks, detailsTenderOppss, detailsTenderOther)
				.flatMap(Collection::stream)
				.sorted(compareByCategoryId)
				.collect(Collectors.toList());
				
				List<IRocDetailJasperWrapper> detailsRisksOpps = Stream
				.of(detailsRisks, detailsOpps)
				.flatMap(Collection::stream)
				.sorted(compareByCategoryId)
				.collect(Collectors.toList());
				
				wrapper.getDetailsContingencies().addAll(addNoDataRocDetailJasperWrapper(detailsContingencies));
				wrapper.getDetailsRisksOpps().addAll(addNoDataRocDetailJasperWrapper(detailsRisksOpps));
				break;
			default:
				wrapper.setDetailsTenderRisks(addNoDataRocDetailJasperWrapper(detailsTenderRisks));
				wrapper.setDetailsTenderOpps(addNoDataRocDetailJasperWrapper(detailsTenderOppss));
				wrapper.setDetailsTenderOther(addNoDataRocDetailJasperWrapper(detailsTenderOther));
				wrapper.setDetailsRisks(addNoDataRocDetailJasperWrapper(detailsRisks));
				wrapper.setDetailsOpps(addNoDataRocDetailJasperWrapper(detailsOpps));
				break;
		}
		return wrapper;
	}
	
	private List<IRocDetailJasperWrapper> filterRocDetailsList(String category, List<IRocDetailJasperWrapper> list) {
		List<IRocDetailJasperWrapper> wrapperList = list.stream().filter(d -> d.getCategory().equals(category))
				.collect(Collectors.toList());
		return wrapperList;
	}
	
	private List<IRocDetailJasperWrapper> addNoDataRocDetailJasperWrapper(List<IRocDetailJasperWrapper> list) {
		return list.size() > 0 ? list : Arrays.asList(new RocDetailJasperWrapper(0, 0, 0, "", "", "", noDataMarker, ""));
	}

	private boolean isNoDataWrapper(IRocDetailJasperWrapper wrapper) {
		return wrapper.getRemark().equals(noDataMarker);
	}
	
	private RocCaseWrapper generateRocCaseWrapper(
		String category, 
		List<IRocDetailJasperWrapper> currentList,
		List<IRocDetailJasperWrapper> previousList
	) {
		RocAmountWrapper best = this.generateRocAmountWrapper(category, currentList, previousList, r -> r.getAmountBest());
		RocAmountWrapper relistic = this.generateRocAmountWrapper(category, currentList, previousList, r -> r.getAmountRealistic());
		RocAmountWrapper worst = this.generateRocAmountWrapper(category, currentList, previousList, r -> r.getAmountWorst());

		RocCaseWrapper wrapper = new RocCaseWrapper(best, relistic, worst);
		return wrapper;
	}

	private RocAmountWrapper generateRocAmountWrapper(
		String category, List<IRocDetailJasperWrapper> currentList,
		List<IRocDetailJasperWrapper> previousList,
		ToDoubleFunction<? super IRocDetailJasperWrapper> mapper
	) {
		return new RocAmountWrapper(
			this.filterRocDetailsList(category, currentList).stream().mapToDouble(mapper).sum(),
			this.filterRocDetailsList(category, previousList).stream().mapToDouble(mapper).sum()
		);
	}
	
}
