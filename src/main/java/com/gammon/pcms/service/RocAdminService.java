package com.gammon.pcms.service;

import com.gammon.pcms.dao.RocCutoffPeriodRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.dao.RocSubdetailRepository;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.model.RocCutoffPeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

	public String updateRocAdmin(String noJob, ROC roc) {
		String error = "";
		try {
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
				dbRecord.setYear(rocSubdetail.getYear());
				dbRecord.setMonth(rocSubdetail.getMonth());
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

}

