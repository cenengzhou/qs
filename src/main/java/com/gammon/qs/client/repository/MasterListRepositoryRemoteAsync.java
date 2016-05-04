package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MasterListRepositoryRemoteAsync {
	
	public void searchVendorList(String serarchStr, AsyncCallback<List<MasterListVendor> > callback);
	public void searchObjectList(String searchStr, AsyncCallback<List<MasterListObject>> callback);
	public void searchSubsidiaryList(String searchStr, AsyncCallback<List<MasterListSubsidiary>> callback);
	public void searchVendorAddressDetails(String addressNumber, AsyncCallback<MasterListVendor> callback);
	public void getSubcontractorWorkScope(String vendorNo, AsyncCallback<List<WorkScopeWrapper>> callback);
	public void searchVendorListWithUser(String searchStr, String username, AsyncCallback<List<MasterListVendor>> callback);
	public void searchClientListWithUser(String searchStr, String username, AsyncCallback<List<MasterListVendor>> callback);
	public void obtainVendorListByWorkScopeWithUser(String workScope, String username, AsyncCallback<List<MasterListVendor>> callback);
	public void obtainAllVendorList(String searchStr, AsyncCallback<List<MasterListVendor>> callback);
}
