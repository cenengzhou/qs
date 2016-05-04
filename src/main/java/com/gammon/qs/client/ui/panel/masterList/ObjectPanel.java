package com.gammon.qs.client.ui.panel.masterList;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.client.repository.MasterListRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListObject;
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

@SuppressWarnings("unchecked")
public class ObjectPanel extends Panel implements MasterListPanel {

	//Remote service
	private MasterListRepositoryRemoteAsync masterListRepository;
	
	//Panel
	private GridPanel objectGridPanel;
	
	private Store store; 
	private final RecordDef contactRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("id"),
			new StringFieldDef("objectCode"),
			new StringFieldDef("description")
							
	});
	
	private GlobalSectionController globalSectionController;
	
	public ObjectPanel(GlobalSectionController g)
	{
		super();
		
		this.masterListRepository = (MasterListRepositoryRemoteAsync)GWT.create(MasterListRepositoryRemote.class);
		((ServiceDefTarget)this.masterListRepository).setServiceEntryPoint(GlobalParameter.MASTER_LIST_REPOSITORY_URL);
		
		this.setBorder(false);		
		this.setTitle("Object");
		this.setLayout(new FitLayout());
		
		
		objectGridPanel = new GridPanel();
		objectGridPanel.setBorder(false); 
		
		
		ColumnModel contactColumnModel = new ColumnModel(new ColumnConfig[] {
				
				new ColumnConfig("Code", "objectCode", 40),
				new ColumnConfig("Description", "description", 100)
				
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(this.contactRecordDef);
		
		this.store = new Store(proxy, reader);
		this.store.setSortInfo(new SortState("objectCode", SortDir.ASC));
		this.store.load();
		
		objectGridPanel.setStore(store);
		objectGridPanel.setFrame(false);
		objectGridPanel.setStripeRows(true);
		objectGridPanel.setColumnModel(contactColumnModel);
		
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		objectGridPanel.setView(gridView);
		
		
		this.add(objectGridPanel);
		globalSectionController=g;
		
	}
	
	public void populateGrid(List<MasterListObject> objecctList)
	{
		
		
		this.store.removeAll();
		
		if(objecctList==null)
			return;
		
		
		Record[] records = new Record[objecctList.size()];
		
		for(int i = 0 ; i < objecctList.size(); i++ )
		{
			MasterListObject curObject = objecctList.get(i);
			
			records[i] = this.contactRecordDef.createRecord(
					new Object[]{
							i+"",
							curObject.getObjectCode(),
							curObject.getDescription()
					}
			
			);
			
		}
		
		
		
		this.store.add(records);		
		
	}

	
	public void search(String searchStr) {
		SessionTimeoutCheck.renewSessionTimer();
		this.masterListRepository.searchObjectList(searchStr, new AsyncCallback<List<MasterListObject>>(){
			
			public void onSuccess(List<MasterListObject> result) {
				
				if(result !=null && result.size()>100)
				{
					MessageBox.alert(GlobalParameter.RESULT_OVERFLOW_MESSAGE);
				}
				
				ObjectPanel.this.populateGrid(result);
				ObjectPanel.this.doLayout();
				UIUtil.unmaskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser(),"ObjectPanel.performSearch(String)");
			}
		});
		
	}

}
