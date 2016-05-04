/**
 * koeyyeung
 * Jul 30, 2013 3:03:28 PM
 */
package com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;


public class SubcontractorWorkScopeDetailPanel extends GridPanel{
	private static final String WORKSCOPE_RECORD_NAME = "workScopeRecordName";
	private static final String DESCRIPTION_RECORD_NAME = "descriptionRecordName";
	private static final String STATUS_RECORD_NAME = "statusRecordName";

	// records
	private Store dataStore;
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(WORKSCOPE_RECORD_NAME),
					new StringFieldDef(DESCRIPTION_RECORD_NAME),
					new StringFieldDef(STATUS_RECORD_NAME),
					});
	
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	public SubcontractorWorkScopeDetailPanel(GlobalSectionController globalSectionController, String subcontractorNo) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		setupGridPanel();
		
		search(subcontractorNo);
	}

	private void setupGridPanel() {
		setTitle("Work Scope");
		setIconCls("grid-icon");
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());
		
		BaseColumnConfig[] columns;
		
		//Column headers	
		ColumnConfig workScopeColConfig = new ColumnConfig("Work Scope", WORKSCOPE_RECORD_NAME, 100, false);
		ColumnConfig descriptionColConfig = new ColumnConfig("Description",DESCRIPTION_RECORD_NAME, 350, false);
		ColumnConfig statusColConfig = new ColumnConfig("Status",STATUS_RECORD_NAME, 100, false);
		
		columns = new BaseColumnConfig[]{ 
				workScopeColConfig,
				descriptionColConfig,
				statusColConfig
		};
		
		setColumnModel(new ColumnModel(columns));
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState(WORKSCOPE_RECORD_NAME,SortDir.ASC));
		setStore(dataStore);
	}

	private void search(String subcontractorNo){
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMasterListRepository().getSubcontractorWorkScope(subcontractorNo, new AsyncCallback<List<WorkScopeWrapper>>() {
			public void onSuccess(List<WorkScopeWrapper> workScopeList) {
				populateGrid(workScopeList);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	
	private void populateGrid(List<WorkScopeWrapper> workScopeList) {
		dataStore.removeAll();
		if(workScopeList==null || workScopeList.size()==0) return;
		
		for (WorkScopeWrapper workScopeWrapper: workScopeList){
			Record record = recordDef.createRecord(new Object[]{
				workScopeWrapper.getWorkScopeCode(),
				workScopeWrapper.getDescription(),
				(workScopeWrapper.getIsApproved()!=null && workScopeWrapper.getIsApproved().length()>0 && "A".equals(workScopeWrapper.getIsApproved().trim()))?"Approved":"Not Approved"
			});
			dataStore.add(record);
		}
	}
}
