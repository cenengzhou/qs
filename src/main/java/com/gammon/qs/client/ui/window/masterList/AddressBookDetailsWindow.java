package com.gammon.qs.client.ui.window.masterList;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.VendorAddress;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;

public class AddressBookDetailsWindow extends Window {
	private Store addressStore;
	private Store workScopeStore;
	private GlobalSectionController globalSectionController;
	private final RecordDef addressRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("name"),
			new StringFieldDef("value")
	});
	private final RecordDef workScopeRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("workScope"),
			new StringFieldDef("description"),
			new StringFieldDef("isApproved")
			
	});
	
	private MasterListVendor masterListVendor;
	
	public AddressBookDetailsWindow(final GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		
		this.setTitle("Contact Information");
		this.setPaddings(5);
		this.setWidth(530);
		this.setHeight(410);
		this.setLayout(new FitLayout());
		this.setClosable(false);
		this.setModal(true);

		Panel mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setBorder(true);
		
		ColumnConfig nameCol 	= new ColumnConfig("Address Book", "name", 100);
		ColumnConfig valueCol	= new ColumnConfig(" ", "value", 400);	
		ColumnModel addressColumnModel = new ColumnModel(new ColumnConfig[] {
				nameCol,
				valueCol
		});
		
		ColumnConfig workScopeCol = new ColumnConfig("Work Scope", "workScope", 100);
		ColumnConfig descriptionCol = new ColumnConfig("Description", "description", 200);
		ColumnConfig isApprovedCol = new ColumnConfig("Approval Status", "isApproved", 200);				
		ColumnModel workScopeColumnModel = new ColumnModel(new ColumnConfig[] {
				workScopeCol,
				descriptionCol,
				isApprovedCol
		});
		
		EditorGridPanel addressEditorPanel = new EditorGridPanel();
		EditorGridPanel workScopeEditorPanel = new EditorGridPanel();
		
		MemoryProxy wsProxy = new MemoryProxy(new Object[][]{});
		ArrayReader wsReader = new ArrayReader(workScopeRecordDef);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(this.addressRecordDef);
		
		workScopeStore = new Store(wsProxy,wsReader);
		workScopeStore.setSortInfo(new SortState("workScope", SortDir.ASC));
		workScopeStore.load();
		
		this.addressStore = new Store(proxy, reader);
		this.addressStore.setSortInfo(new SortState("name", SortDir.ASC)); 
		this.addressStore.load(); 
		
		
		workScopeEditorPanel.setColumnModel(workScopeColumnModel);
		workScopeEditorPanel.setStore(workScopeStore);
	
		addressEditorPanel.setColumnModel(addressColumnModel);
		addressEditorPanel.setStore(this.addressStore);
		
		addressEditorPanel.setHeight(170);
		workScopeEditorPanel.setHeight(150);
		workScopeEditorPanel.setTitle("Work Scope");
		mainPanel.add(addressEditorPanel);
		mainPanel.add(workScopeEditorPanel);
		this.add(mainPanel);
		
		Button attachmentButton = new Button("Attachment");
		attachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.showAttachmentWindow("Attachment_Vendor", masterListVendor.getVendorNo());
			}
		});
		mainPanel.addButton(attachmentButton);
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalSectionController.closeCurrentWindow();				
				};
		});
		mainPanel.addButton(closeButton);
	
		
	}
	
	public void populateGrid(MasterListVendor masterListVendor) {
		UIUtil.maskMainPanel();
		this.masterListVendor = masterListVendor;

		if (masterListVendor.getVendorName() != null) {
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "Company", masterListVendor.getVendorName() }));
		} else {
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "", "" }));
		}

		String username = globalSectionController.getUser().getUsername();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(username, RoleSecurityFunctions.C010001_ADDRESS_BOOK, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
			}

			public void onSuccess(List<String> rights) {
				if (rights != null && rights.contains("GAMMON"))
					populateGammonInfo();
			}

		});
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMasterListRepository().getSubcontractorWorkScope(masterListVendor.getVendorNo(), new AsyncCallback<List<WorkScopeWrapper>>() {

			public void onFailure(Throwable msg) {
				UIUtil.alert(msg);
			}

			public void onSuccess(List<WorkScopeWrapper> resultList) {
				populateWorkScope(resultList);
			}

		});
	}
	
	private void populateGammonInfo() {
		if (masterListVendor.getVendorAddress() != null && masterListVendor.getVendorAddress().size() > 0) {
			VendorAddress latestAddress = masterListVendor.getVendorAddress().get(0);
			Date latestDate = masterListVendor.getVendorAddress().get(0).getDateBeginningEffective();
			for (VendorAddress cur : masterListVendor.getVendorAddress()) {
				if (latestDate != null && (cur.getDateBeginningEffective() == null || latestDate.compareTo(cur.getDateBeginningEffective()) < 0)) {
					latestAddress = cur;
					latestDate = cur.getDateBeginningEffective();
				}
			}
			if (latestAddress == null)
				latestAddress = masterListVendor.getVendorAddress().get(0);
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "Address", latestAddress.getAddressLine1() + " " + latestAddress.getAddressLine2() + " " + latestAddress.getAddressLine3() + " " + latestAddress.getAddressLine4() }));
		} else {
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "", "" }));
		}
		if (masterListVendor.getVendorPhoneNumber() != null /* && masterListVendor.getVendorPhoneNumber().size()>= 0 */) {
			for (int i = 0; i < masterListVendor.getVendorPhoneNumber().size(); i++) {
				addressStore.add(this.addressRecordDef.createRecord(new Object[] {
						getPhoneType(masterListVendor, i),
						masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumber() }));
			}
		} else {
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "", "" }));
		}
		if (masterListVendor.getVendorContactPerson() != null)
			for (int i = 0; i < masterListVendor.getVendorContactPerson().size() && masterListVendor.getVendorContactPerson().get(i) != null; i++) {
				if (masterListVendor.getVendorContactPerson().get(i).getNameSurname().trim().length() > 0 || masterListVendor.getVendorContactPerson().get(i).getNameGiven().trim().length() > 0 || masterListVendor.getVendorContactPerson().get(i).getNameMiddle().trim().length() > 0) {
					if (masterListVendor.getVendorContactPerson().get(i).getContactTitle().trim().length() > 0) {
						addressStore.add(this.addressRecordDef.createRecord(new Object[] {
								"Contact Person",
								masterListVendor.getVendorContactPerson().get(i).getNameGiven() + " " + masterListVendor.getVendorContactPerson().get(i).getNameMiddle() + " " + masterListVendor.getVendorContactPerson().get(i).getNameSurname() + " (" + masterListVendor.getVendorContactPerson().get(i).getContactTitle() + ")" }));
					} else {
						addressStore.add(this.addressRecordDef.createRecord(new Object[] {
								"Contact Person",
								masterListVendor.getVendorContactPerson().get(i).getNameGiven() + " " + masterListVendor.getVendorContactPerson().get(i).getNameMiddle() + " " + masterListVendor.getVendorContactPerson().get(i).getNameSurname() }));
					}
				} else {
					if (masterListVendor.getVendorContactPerson().get(i).getContactTitle().trim().length() > 0) {
						addressStore.add(this.addressRecordDef.createRecord(new Object[] {
								"Contact Person",
								masterListVendor.getVendorContactPerson().get(i).getNameAlpha() + " (" + masterListVendor.getVendorContactPerson().get(i).getContactTitle() + ")" }));
					} else {
						// recordList.add(this.addressRecordDef.createRecord(new Object[]{"Contact Person",masterListVendor.getVendorContactPerson().get(i).getNameAlpha()}));
					}
				}

				// count++;
			}
		else {
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "", "" }));
		}
		if (masterListVendor.getVendorType() != null && !"".equals(masterListVendor.getVendorType().trim())) {
			String vendorType;
			int type = Integer.parseInt(masterListVendor.getVendorType().trim());
			switch (type) {
			case 1:
				vendorType = "Supplier";
				break;
			case 2:
				vendorType = "Subcontractor";
				break;
			case 3:
				vendorType = "Both(Supplier & Subcontractor)";
				break;
			default:
				vendorType = "";
			}
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "Vendor Type", vendorType }));
		}
		if (masterListVendor.getVendorStatus() != null && !"".equals(masterListVendor.getVendorStatus().trim())) {
			String vendorStatus;
			int status = Integer.parseInt(masterListVendor.getVendorStatus().trim());
			switch (status) {
			case 1:
				vendorStatus = "Performance being observed";
				break;
			case 2:
				vendorStatus = "Suspended";
				break;
			case 3:
				vendorStatus = "Blacklisted";
				break;
			case 4:
				vendorStatus = "Obsolete";
				break;
			case 5:
				vendorStatus = "On HSE League Table";
				break;
			case 6:
				vendorStatus = "Observed & On HSE League";
				break;
			case 7:
				vendorStatus = "Suspended & On HSE League";
				break;
			default:
				vendorStatus = "";
			}
			addressStore.add(this.addressRecordDef.createRecord(new Object[] { "Vendor Status", vendorStatus }));
		}
	}
	
	private void populateWorkScope(List<WorkScopeWrapper> resultList) {
		if (resultList != null) {
			for (WorkScopeWrapper wrapper : resultList) {
				String isApproved = wrapper.getIsApproved().trim();
				if ("A".equalsIgnoreCase(isApproved))
					isApproved = "Approved";
				else
					isApproved = "Not Approved";
				workScopeStore.add(workScopeRecordDef.createRecord(new Object[] {
						wrapper.getWorkScopeCode().trim(),
						wrapper.getDescription().trim(),
						isApproved
				}));
			}
		}
	}
	
	private String getPhoneType(MasterListVendor masterListVendor, int i) {
		if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("FAX")) {
			return "FAX Number";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("OFF")) {
			return "Office Number";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("MOB")) {
			return "Mobile Number";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("HOM")) {
			return "Home Number";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("CAR")) {
			return "Car or Mobile Number";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("WEMG")) {
			return "Work-Emergency Contact";
		} else if (masterListVendor.getVendorPhoneNumber().get(i).getPhoneNumberType().trim().equalsIgnoreCase("HEMG")) {
			return "Home-Emergency Contact";
		} else {
			return "Business Number";
		}
	}
}