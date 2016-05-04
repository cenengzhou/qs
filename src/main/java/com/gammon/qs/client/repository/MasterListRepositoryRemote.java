package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface MasterListRepositoryRemote extends RemoteService{
	
	public List<MasterListVendor> searchVendorList(String serarchStr) throws Exception;
	public List<MasterListObject> searchObjectList(String searchStr) throws Exception;
	public List<MasterListSubsidiary> searchSubsidiaryList(String searchStr) throws Exception;
	public MasterListVendor searchVendorAddressDetails(String addressNumber) throws Exception;
	public List<WorkScopeWrapper> getSubcontractorWorkScope(String vendorNo)throws Exception;
	public List<MasterListVendor> searchVendorListWithUser(String searchStr, String username) throws Exception;
	public List<MasterListVendor> searchClientListWithUser(String searchStr, String username) throws Exception;
	public List<MasterListVendor> obtainVendorListByWorkScopeWithUser(String workScope, String username) throws Exception;
	public List<MasterListVendor> obtainAllVendorList(String searchStr) throws Exception;

}
