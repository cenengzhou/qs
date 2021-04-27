package com.gammon.pcms.service;

import com.gammon.pcms.dao.CommercialActionRepository;
import com.gammon.pcms.model.CommercialAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CommercialActionService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommercialActionRepository caRepository;

	public List<CommercialAction> getCommercialActionList(String jobNo, String year, String month) {
		List<CommercialAction> commercialActionList = new ArrayList<>();
		try {
			commercialActionList = caRepository.findCommercialActionList(jobNo, Integer.valueOf(year), Integer.valueOf(month));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commercialActionList;
	}

	public List<CommercialAction> getAllCommercialAction() {
		List<CommercialAction> commercialAction = new ArrayList<>();
		try {
			commercialAction = caRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commercialAction;
	}

	public String addCommercialAction(String jobNo, CommercialAction inputCommercialAction) {
		String result = "";
		try {
			CommercialAction commercialAction = new CommercialAction();
			commercialAction.setItemNo(inputCommercialAction.getItemNo());
			commercialAction.setActionDesc(inputCommercialAction.getActionDesc());
			commercialAction.setActionStatus(inputCommercialAction.getActionStatus());
			commercialAction.setActionDate(inputCommercialAction.getActionDate());
			commercialAction.setActionResp(inputCommercialAction.getActionResp());
			commercialAction.setNoJob(jobNo);

			CommercialAction savedCommercialAction = caRepository.save(commercialAction);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void deleteById(String id) {
		try {
			Long lid = Long.valueOf(id);
			caRepository.delete(lid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean saveById(String id, CommercialAction ca) {
		boolean update = false;
		try {
			update = true;
			caRepository.save(ca);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	public List<CommercialAction> saveList(String jobNo, List<CommercialAction> commercialActionList) {
		return caRepository.save(commercialActionList);
	}
}
