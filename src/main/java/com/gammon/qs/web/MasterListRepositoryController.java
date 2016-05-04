package com.gammon.qs.web;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.wrapper.WorkScopeWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class MasterListRepositoryController extends GWTSpringController implements MasterListRepositoryRemote {

	private static final long serialVersionUID = 2020016730524548518L;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(MasterListRepositoryController.class.getName());
	@Autowired
	private MasterListService masterListRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public List<MasterListVendor> searchVendorList(String searchStr) throws Exception {
		return masterListRepository.searchVendorList(searchStr);

	}

	public List<MasterListObject> searchObjectList(String searchStr) throws Exception {
		return masterListRepository.searchObjectList(searchStr);
	}

	public List<MasterListSubsidiary> searchSubsidiaryList(String searchStr) throws Exception {
		return masterListRepository.searchSubsidiaryList(searchStr);
	}

	public List<MasterListVendor> searchVendorNameList(String addressNumber) throws Exception {
		return masterListRepository.searchVendorNameList(addressNumber);
	}

	public MasterListVendor searchVendorAddressDetails(String addressNumber) throws Exception {
		return masterListRepository.searchVendorAddressDetails(addressNumber);
	}

	public List<WorkScopeWrapper> getSubcontractorWorkScope(String vendorNo) throws Exception {
		return masterListRepository.getSubcontractorWorkScope(vendorNo);
	}

	public List<MasterListVendor> obtainVendorListByWorkScopeWithUser(String workScope, String username) throws Exception {
		return masterListRepository.obtainVendorListByWorkScopeWithUser(workScope, username);
	}

	public List<MasterListVendor> searchVendorListWithUser(String searchStr, String username) throws Exception {
		return masterListRepository.obtainVendorListWithUser(searchStr, username);
	}
	
	public List<MasterListVendor> searchClientListWithUser(String searchStr, String username) throws Exception {
		return masterListRepository.obtainClientListWithUser(searchStr, username);
	}

	public List<MasterListVendor> obtainAllVendorList(String searchStr) throws Exception {
		return masterListRepository.obtainAllVendorList(searchStr);
	}

}
