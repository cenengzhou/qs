package com.gammon.pcms.service;

import com.gammon.pcms.dao.RocClassDescMapRepository;
import com.gammon.pcms.dao.RocDetailRepository;
import com.gammon.pcms.dao.RocRepository;
import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.wrapper.RocWrapper;
import com.gammon.qs.service.admin.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

	private boolean validateInputRoc(ROC roc) {
		if (roc == null || roc.getRocCategory() == null || roc.getClassification() == null
				|| roc.getImpact() == null || roc.getStatus() == null || roc.getDescription() == null)
			return false;
		return true;
	}

	public String addRoc(String noJob, ROC roc) {
		String error = "";
		try {
			if (!validateInputRoc(roc))
				return "Please fill in all required fields.";

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
			if (!validateInputRoc(roc))
				return "Please fill in all required fields.";
			if (!noJob.equals(roc.getProjectNo()))
				return "job no. not match";

			ROC newRoc = rocRepository.findOne(roc.getId());
			newRoc.setProjectRef(roc.getProjectRef());
			newRoc.setRocCategory(roc.getRocCategory());;
			newRoc.setClassification(roc.getClassification());
			newRoc.setImpact(roc.getImpact());
			newRoc.setStatus(roc.getStatus());
			newRoc.setDescription(roc.getDescription());
			ROC result = rocRepository.save(newRoc);

		} catch (Exception e) {
			error = "ROC cannot be created";
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

				rocDetailRepository.save(newRocDetail);

			}
		} catch (Exception e) {
			error = "ROC Detail cannot be created";
			e.printStackTrace();
		}
		return error;
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
}
