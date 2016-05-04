package com.gammon.qs.client.ui.panel.masterList;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.client.repository.MasterListRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.layout.FitLayout;

@SuppressWarnings({"unchecked" })
public class SubsidiaryPanel extends Panel implements MasterListPanel{

	
	//Remote service
	private MasterListRepositoryRemoteAsync masterListRepository;
	
	//Panel
	private GridPanel subsidiaryGridPanel;
	
	private GlobalSectionController globalSectionController;
	
	private Store store; 
	private final RecordDef contactRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("id"),
			new StringFieldDef("subsidiaryCode"),
			new StringFieldDef("description")
							
	});
	
	public SubsidiaryPanel(GlobalSectionController gsc)
	{
		super();
		
		this.masterListRepository = (MasterListRepositoryRemoteAsync)GWT.create(MasterListRepositoryRemote.class);
		((ServiceDefTarget)this.masterListRepository).setServiceEntryPoint(GlobalParameter.MASTER_LIST_REPOSITORY_URL);
		
		this.setBorder(false);		
		this.setTitle("Subsidiary");
		this.setLayout(new FitLayout());
		
		
		subsidiaryGridPanel = new GridPanel();
		subsidiaryGridPanel.setBorder(false); 
		
		
		ColumnModel contactColumnModel = new ColumnModel(new ColumnConfig[] {
				
				new ColumnConfig("Code", "subsidiaryCode", 40),
				new ColumnConfig("Description", "description", 100)
				
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(this.contactRecordDef);
		
		this.store = new Store(proxy, reader);
		this.store.setSortInfo(new SortState("subsidiaryCode", SortDir.ASC));
		this.store.load();
		
		subsidiaryGridPanel.setStore(store);
		subsidiaryGridPanel.setFrame(false);
		subsidiaryGridPanel.setStripeRows(true);
		subsidiaryGridPanel.setColumnModel(contactColumnModel);
		
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		subsidiaryGridPanel.setView(gridView);
		
		
		this.add(subsidiaryGridPanel);
		globalSectionController = gsc;
	}
	
	
	public void populateGrid(List<MasterListSubsidiary> subsidiaryList)
	{
		
		
		this.store.removeAll();
		
		if(subsidiaryList==null)
			return;
		
		
		Record[] records = new Record[subsidiaryList.size()];
		
		for(int i = 0 ; i < subsidiaryList.size(); i++ )
		{
			MasterListSubsidiary curObject = subsidiaryList.get(i);
			
			records[i] = this.contactRecordDef.createRecord(
					new Object[]{
							i+"",
							curObject.getSubsidiaryCode(),
							curObject.getDescription()
					}
			
			);
			
		}
		
		
		
		this.store.add(records);		
		
	}
	
	public void search(String searchStr) {
		SessionTimeoutCheck.renewSessionTimer();
		this.masterListRepository.searchSubsidiaryList(searchStr, new AsyncCallback<List<MasterListSubsidiary>>(){
			
			public void onSuccess(List<MasterListSubsidiary> result) {
				
				if(result !=null && result.size()>100)
				{
					MessageBox.alert(GlobalParameter.RESULT_OVERFLOW_MESSAGE);
				}
				
				SubsidiaryPanel.this.populateGrid(result);
				SubsidiaryPanel.this.doLayout();
				UIUtil.unmaskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser(),"SubsidiaryPanel.performSearch(String)");				
			}
		});
		
	}

}
