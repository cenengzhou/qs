package com.gammon.qs.client.ui.panel.masterList;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.SubcontractorEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

@SuppressWarnings("unchecked")
public class WorkScopePanel extends Panel implements MasterListPanel {

	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;
	
	//Panel
	private GridPanel workScopeGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	
	private Store store;
	private final RecordDef workScopeRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("workScope"),
			new StringFieldDef("description")
							
	});
	
	private Window childWindow;
	private GlobalSectionController globalSectionController;
	
	
	public WorkScopePanel(final GlobalSectionController globalSectionController) {
		super();

		this.packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget) this.packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);

		this.setBorder(false);
		this.setTitle("Work Scope");
		this.setLayout(new FitLayout());
		this.setGlobalSectionController(globalSectionController);

		globalMessageTicket = new GlobalMessageTicket();

		workScopeGridPanel = new GridPanel();
		workScopeGridPanel.setBorder(false);

		ColumnModel contactColumnModel = new ColumnModel(new ColumnConfig[] {

		new ColumnConfig("Work Scope", "workScope", 40),
				new ColumnConfig("Description", "description", 100)

		});

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(this.workScopeRecordDef);

		this.store = new Store(proxy, reader);
		this.store.setSortInfo(new SortState("workScope", SortDir.ASC));
		this.store.load();

		workScopeGridPanel.setStore(store);
		workScopeGridPanel.setFrame(false);
		workScopeGridPanel.setStripeRows(true);
		workScopeGridPanel.setColumnModel(contactColumnModel);

		workScopeGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				globalMessageTicket.refresh();

				Record curRecord = store.getAt(rowIndex);
				String workScope = curRecord.getAsString("workScope");

				globalSectionController.getMainSectionController().resetMainPanel();
				globalSectionController.getDetailSectionController().resetPanel();
				SubcontractorEnquiryMainPanel subcontractorEnquiryMainPanel = new SubcontractorEnquiryMainPanel(globalSectionController);
				subcontractorEnquiryMainPanel.search("", workScope);
				subcontractorEnquiryMainPanel.getWorkScopeComboBox().setValue(workScope);
				globalSectionController.getMainSectionController().setContentPanel(subcontractorEnquiryMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
				globalSectionController.getMasterListSectionController().getMainPanel().collapse();
			}
		});

		GridView gridView = new GridView();
		gridView.setForceFit(true);
		workScopeGridPanel.setView(gridView);

		this.add(workScopeGridPanel);
		getWorkScopeList();
	}
	
	public void populateGrid(List<UDC> workScopeList) {
		this.store.removeAll();

		if (workScopeList == null)
			return;

		Record[] records = new Record[workScopeList.size()];

		for (int i = 0; i < workScopeList.size(); i++) {
			UDC workScope = workScopeList.get(i);

			records[i] = this.workScopeRecordDef.createRecord(
					new Object[] { 	workScope.getCode(),
									workScope.getDescription()});
		}

		this.store.add(records);
	}

	
	public void search(String searchStr) {
		globalMessageTicket.refresh();
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainWorkScopeList(searchStr, new AsyncCallback<List<UDC>>() {
			public void onSuccess(List<UDC> udcList) {
				populateGrid(udcList);
				doLayout();
				UIUtil.unmaskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});

	}
	
	public void getWorkScopeList() {
		SessionTimeoutCheck.renewSessionTimer();
		this.packageRepository.obtainWorkScopeList(new AsyncCallback<List<UDC>>() {
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}

			public void onSuccess(List<UDC> udcList) {
				WorkScopePanel.this.populateGrid(udcList);
				WorkScopePanel.this.doLayout();
				UIUtil.unmaskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID);
			}
		});
	}
	
	public void closeCurrentWindow() {
		if(childWindow != null){
			childWindow.close();
			childWindow = null;
		}
		UIUtil.unmaskMainPanel();
	}

	public void setGlobalSectionController(GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

}
