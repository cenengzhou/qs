package com.gammon.qs.client.ui.panel.masterList;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.client.repository.MasterListRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.SubcontractorEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

@SuppressWarnings("unchecked")
public class VendorListPanel extends Panel implements MasterListPanel {

	private GlobalSectionController globalSectionController;
	// Remote service
	private MasterListRepositoryRemoteAsync masterListRepository;

	// Panel
	private GridPanel addressBookGridPanel;

	private GlobalMessageTicket globalMessageTicket;
	private GroupingStore store;
	private final RecordDef contactRecordDef = new RecordDef(new FieldDef[] {
			new StringFieldDef("id"),
			new StringFieldDef("vendorName"),
			new StringFieldDef("vendorNumber"),
			new StringFieldDef("approvalStatus")

	});

	public VendorListPanel(final GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.masterListRepository = (MasterListRepositoryRemoteAsync) GWT.create(MasterListRepositoryRemote.class);
		((ServiceDefTarget) this.masterListRepository).setServiceEntryPoint(GlobalParameter.MASTER_LIST_REPOSITORY_URL);
		globalMessageTicket = new GlobalMessageTicket();
		this.setBorder(false);
		this.setTitle("Vendor No.");
		this.setLayout(new FitLayout());

		addressBookGridPanel = new GridPanel();

		addressBookGridPanel.setBorder(false);

		ColumnModel contactColumnModel = new ColumnModel(new ColumnConfig[] {

		new ColumnConfig("Name", "vendorName", 65),
				new ColumnConfig("Number", "vendorNumber", 30),
				new ColumnConfig("Approved Subcontractor", "approvalStatus", 50)
		});

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(this.contactRecordDef);

		this.store = new GroupingStore();
		this.store.setReader(reader);
		this.store.setDataProxy(proxy);
		this.store.setSortInfo(new SortState("vendorName", SortDir.ASC));
		this.store.setGroupField("vendorName");
		this.store.load();

		addressBookGridPanel.setStore(store);
		addressBookGridPanel.setFrame(false);
		addressBookGridPanel.setStripeRows(true);
		addressBookGridPanel.setColumnModel(contactColumnModel);

		addressBookGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = store.getAt(rowIndex);

				String addressNumber = curRecord.getAsObject("vendorNumber") != null ? curRecord.getAsString("vendorNumber") : "";
				
				globalSectionController.getMainSectionController().resetMainPanel();
				globalSectionController.getDetailSectionController().resetPanel();
				SubcontractorEnquiryMainPanel subcontractorEnquiryMainPanel = new SubcontractorEnquiryMainPanel(globalSectionController);
				subcontractorEnquiryMainPanel.search(addressNumber, "");
				subcontractorEnquiryMainPanel.getSubcontractorTextField().setValue(addressNumber);
				globalSectionController.getMainSectionController().setContentPanel(subcontractorEnquiryMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
				globalSectionController.getMasterListSectionController().getMainPanel().collapse();
			}
		});

		GridView gridView = new GridView();
		gridView.setForceFit(true);
		addressBookGridPanel.setView(gridView);

		this.add(addressBookGridPanel);
	}

	public void populateGrid(List<MasterListVendor> contactList) {
		this.store.removeAll();

		if (contactList == null)
			return;

		Record[] records = new Record[contactList.size()];

		for (int i = 0; i < contactList.size(); i++) {
			MasterListVendor curVendor = contactList.get(i);

			records[i] = this.contactRecordDef.createRecord(new Object[] {
					i + "",
					curVendor.getVendorName(),
					curVendor.getVendorNo(),
					convertSubcontractorApproval(curVendor.getApprovalStatus())
			});
		}

		this.store.add(records);

	}

	public void search(String searchStr) {
		globalMessageTicket.refresh();
		SessionTimeoutCheck.renewSessionTimer();
		this.masterListRepository.searchVendorListWithUser(searchStr, globalSectionController.getUser().getUsername(), new AsyncCallback<List<MasterListVendor>>() {

			public void onSuccess(List<MasterListVendor> result) {
				if (result != null && result.size() > 100)
					MessageBox.alert(GlobalParameter.RESULT_OVERFLOW_MESSAGE);

				VendorListPanel.this.populateGrid(result);
				VendorListPanel.this.doLayout();
				UIUtil.unmaskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
	}

	private String convertSubcontractorApproval(String subcontractorApproval){
		subcontractorApproval = "Y".equals(subcontractorApproval==null?"":subcontractorApproval.trim())?"Yes":"No";
		if("Yes".equals(subcontractorApproval)){
			return "<font color=#007D00>"+ subcontractorApproval+"</font>";
		}
		return subcontractorApproval;
	}
	
	
}
