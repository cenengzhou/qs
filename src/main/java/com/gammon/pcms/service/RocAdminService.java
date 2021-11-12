package com.gammon.pcms.service;

import com.gammon.pcms.dao.RocCutoffPeriodRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.dao.RocSubdetailRepository;
import com.gammon.pcms.dto.RocAndRocDetailWrapper;
import com.gammon.pcms.dto.RocAndRocSubdetailWrapper;
import com.gammon.pcms.helper.RocDateUtils;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.model.RocCutoffPeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class RocAdminService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RocRepository rocRepository;

	@Autowired
	private RocDetailRepository rocDetailRepository;

	@Autowired
	private RocSubdetailRepository rocSubdetailRepository;

	@Autowired
	private RocCutoffPeriodRepository rocCutoffPeriodRepository;

	public List<ROC> getRocAdmin(String jobNo, String itemNo) {
		if (jobNo.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		List<ROC> result = rocRepository.getRocListAdmin(jobNo);
		if (itemNo != null && !itemNo.equals("")) {
			result = result.stream().filter(x -> x.getItemNo().equals(Long.valueOf(itemNo))).collect(Collectors.toList());
		}
		return result;
	}

	public List<RocAndRocDetailWrapper> getRocDetailListAdmin(String jobNo, String itemNo, String period) {
		if (jobNo.isEmpty() || period.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		YearMonth yearMonth = YearMonth.parse(period);
		List<RocAndRocDetailWrapper> result = rocRepository.getRocDetailListAdmin(jobNo, yearMonth.getYear(), yearMonth.getMonthValue());
		if (itemNo != null && !itemNo.equals("")) {
			result = result.stream().filter(x -> x.getItemNo().equals(Long.valueOf(itemNo))).collect(Collectors.toList());
		}
		return result;
	}

	public List<RocAndRocSubdetailWrapper> getRocSubdetailListAdmin(String jobNo, String itemNo, String period) {
		if (jobNo.isEmpty() || period.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		YearMonth yearMonth = YearMonth.parse(period);
		List<RocAndRocSubdetailWrapper> result = rocRepository.getRocSubdetailListAdmin(jobNo, yearMonth.getYear(), yearMonth.getMonthValue());
		if (itemNo != null && !itemNo.equals("")) {
			result = result.stream().filter(x -> x.getItemNo().equals(Long.valueOf(itemNo))).collect(Collectors.toList());
		}
		return result;
	}

	public String updateRocAdmin(String noJob, List<ROC> rocList) {
		String error = "";
		try {
			List<ROC> saveList = new ArrayList<>();
			for (ROC roc : rocList) {
				ROC dbRecord = rocRepository.findOne(roc.getId());
				dbRecord.setProjectRef(roc.getProjectRef());
				dbRecord.setRocCategory(roc.getRocCategory());
				dbRecord.setClassification(roc.getClassification());
				dbRecord.setImpact(roc.getImpact());
				dbRecord.setDescription(roc.getDescription());
				dbRecord.setStatus(roc.getStatus());
				dbRecord.setRocOwner(roc.getRocOwner());
				dbRecord.setOpenDate(roc.getOpenDate());
				dbRecord.setClosedDate(roc.getClosedDate());
				dbRecord.setStatus(roc.getStatus());
				dbRecord.setSystemStatus(roc.getSystemStatus());
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocRepository.save(saveList);
			}

		} catch (Exception e) {
			error = "ROC cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String updateRocDetailListAdmin(String jobNo, List<RocAndRocDetailWrapper> rocDetailList) {
		if (jobNo.isEmpty() || rocDetailList == null || rocDetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_DETAIL> saveList = new ArrayList<>();
			for (RocAndRocDetailWrapper rocDetail : rocDetailList) {
				ROC_DETAIL dbRecord = rocDetailRepository.findOne(rocDetail.getDetailId());
				dbRecord.setAmountBest(rocDetail.getAmountBest());
				dbRecord.setAmountExpected(rocDetail.getAmountRealistic());
				dbRecord.setAmountWorst(rocDetail.getAmountWorst());
				dbRecord.setRemarks(rocDetail.getRemarks());
				dbRecord.setStatus(rocDetail.getStatus());
				dbRecord.setSystemStatus(rocDetail.getSystemStatus());
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

	public String updateRocSubdetailListAdmin(String jobNo, List<RocAndRocSubdetailWrapper> rocSubdetailList) {
		if (jobNo.isEmpty() || rocSubdetailList == null || rocSubdetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_SUBDETAIL> saveList = new ArrayList<>();
			for (RocAndRocSubdetailWrapper rocSubdetail : rocSubdetailList) {
				ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(rocSubdetail.getSubdetailId());
				dbRecord.setDescription(rocSubdetail.getDescription());
				dbRecord.setAmountBest(rocSubdetail.getAmountBest());
				dbRecord.setAmountExpected(rocSubdetail.getAmountRealistic());
				dbRecord.setAmountWorst(rocSubdetail.getAmountWorst());
				dbRecord.setHyperlink(rocSubdetail.getHyperlink());
				dbRecord.setYear(rocSubdetail.getYear());
				dbRecord.setMonth(rocSubdetail.getMonth());
				dbRecord.setRemarks(rocSubdetail.getRemarks());
				dbRecord.setSystemStatus(rocSubdetail.getSystemStatus());
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

	public String updateRocCutoffAdmin(RocCutoffPeriod rocCutoffPeriod) {
		String error = "";
		try {
			RocCutoffPeriod db = rocCutoffPeriodRepository.findOne(1L);
			db.setCutoffDate(rocCutoffPeriod.getCutoffDate());
			db.setPeriod(rocCutoffPeriod.getPeriod());
			rocCutoffPeriodRepository.save(rocCutoffPeriod);

		} catch (Exception e) {
			error = "RocCutoffPeriod cannot be updated";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteRocDetailListAdmin(String jobNo, List<RocAndRocDetailWrapper> rocDetailList) {
		if (jobNo.isEmpty() || rocDetailList == null || rocDetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_DETAIL> saveList = new ArrayList<>();
			for (RocAndRocDetailWrapper rocDetail : rocDetailList) {
				ROC_DETAIL dbRecord = rocDetailRepository.findOne(rocDetail.getDetailId());
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

	public String deleteRocSubdetailListAdmin(String jobNo, List<RocAndRocSubdetailWrapper> rocSubdetailList) {
		if (jobNo.isEmpty() || rocSubdetailList == null || rocSubdetailList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC_SUBDETAIL> saveList = new ArrayList<>();
			for (RocAndRocSubdetailWrapper subdetail : rocSubdetailList) {
				ROC_SUBDETAIL dbRecord = rocSubdetailRepository.findOne(subdetail.getSubdetailId());
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

	public String deleteRocListAdmin(String jobNo, List<ROC> rocList) {
		if (jobNo.isEmpty() || rocList == null || rocList.isEmpty())
			throw new IllegalArgumentException("invalid parameters");
		String error = "";
		try {
			List<ROC> saveList = new ArrayList<>();
			for (ROC roc : rocList) {
				ROC dbRecord = rocRepository.findOne(roc.getId());
				if (dbRecord != null) {
					dbRecord.inactivate();
				}
				saveList.add(dbRecord);
			}
			if (!saveList.isEmpty()) {
				rocRepository.save(saveList);
			}


		} catch (Exception e) {
			error = "ROC cannot be deleted";
			e.printStackTrace();
		}
		return error;
	}


}

