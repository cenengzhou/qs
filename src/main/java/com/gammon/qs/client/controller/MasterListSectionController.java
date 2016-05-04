package com.gammon.qs.client.controller;


import com.gammon.qs.client.ui.panel.masterList.AddressBookPanel;
import com.gammon.qs.client.ui.panel.masterList.MasterListPanel;
import com.gammon.qs.client.ui.panel.masterList.UCCPanel;
import com.gammon.qs.client.ui.panel.masterList.WorkScopePanel;
import com.gammon.qs.client.ui.toolbar.MasterListSectionToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;


public class MasterListSectionController {
	private GlobalSectionController globalSectionController; 
	
	//UI
	private Panel mainPanel;
	private MasterListSectionToolbar masterListSectionToolbar;
	private TabPanel tabPanel;
	private TabPanel addressBookTabPanel;
	private Panel workScopeTabPanel;
	private TabPanel uccCodeTabPanel;
	
	public MasterListSectionController(GlobalSectionController globalSectionController){
		this.globalSectionController = globalSectionController; 
		setupUI();
	}
	
	public void setupUI(){
		mainPanel = new Panel();
		masterListSectionToolbar = new MasterListSectionToolbar(this);
		
		mainPanel.setTopToolbar(masterListSectionToolbar);
		mainPanel.setTitle("Master List");
		mainPanel.setBorder(false);
		mainPanel.setCollapsible(true);
		mainPanel.setWidth(300);
		mainPanel.setLayout(new FitLayout());
		mainPanel.collapse(true);
		
		tabPanel = new TabPanel();
		tabPanel.setBorder(false);
		tabPanel.setTabPosition(Position.BOTTOM);
		tabPanel.setResizeTabs(true);
		tabPanel.setActiveTab(0);
		tabPanel.setId("masterListTabPanel");
		
		addressBookTabPanel = new AddressBookPanel(globalSectionController);
		addressBookTabPanel.setBorder(false);
		addressBookTabPanel.setTabPosition(Position.BOTTOM);
		addressBookTabPanel.setTitle("Address Book");	
		tabPanel.add(addressBookTabPanel);

		workScopeTabPanel = new WorkScopePanel(globalSectionController);
		workScopeTabPanel.setBodyBorder(false);
		workScopeTabPanel.setTitle("Work Scope");
		tabPanel.add(workScopeTabPanel);
		
		uccCodeTabPanel = new UCCPanel(globalSectionController);
		uccCodeTabPanel.setBorder(false);
		uccCodeTabPanel.setTabPosition(Position.BOTTOM);
		uccCodeTabPanel.setTitle("Account Code");		
		tabPanel.add(uccCodeTabPanel);
		
		tabPanel.addListener(new TabPanelListenerAdapter(){
			public void onTabChange(TabPanel source, Panel tab) {
				if(tab.equals(workScopeTabPanel)||tab.equals(uccCodeTabPanel)) {
					masterListSectionToolbar.showExportButton();
				} else {
					masterListSectionToolbar.hideExportButton();
				}
			}
		});
		mainPanel.add(tabPanel);
	}
	
	public void applySearch(String searchStr){
		if(searchStr!=null && searchStr.length()>0 && !searchStr.trim().equals("*")){
			try {
				Integer.parseInt(searchStr.trim());
			} catch (NumberFormatException e) {
				searchStr = ("*".concat(searchStr)).concat("*"); // input String is a description but not a number	
			}
			
			//to get the active panel 
			UIUtil.maskPanelById(GlobalParameter.MASTER_LIST_TAB_PANEL_ID, "fetching item...", true);
			Panel activePanel = this.tabPanel.getActiveTab();			
			((MasterListPanel)activePanel).search(searchStr);
		}
		else{
			MessageBox.alert("The searching criteria cannot be blank or '*'");
		}
	}
	
	public void exportWorkScope(String searchStr){
		if(this.tabPanel.getActiveTab().getTitle().equals("Work Scope"))
		{
			com.google.gwt.user.client.Window.open( 
					GlobalParameter.WORK_SCOPE_EXCEL_DOWNLOAD_URL + 
					"?query=" + searchStr, "_blank", "");
		}
		else if(this.tabPanel.getActiveTab().getTitle().equals("Account Code"))
		{
			if(((UCCPanel) this.tabPanel.getActiveTab()).getActiveTab().getTitle().equals("Object"))
			{
				com.google.gwt.user.client.Window.open( 
				GlobalParameter.ACCOUNT_CODE_OBJECT_EXCEL_DOWNLOAD_URL + 
				"?query=" + searchStr, "_blank", "");
			}
			if(((UCCPanel) this.tabPanel.getActiveTab()).getActiveTab().getTitle().equals("Subsidiary"))
			{
				com.google.gwt.user.client.Window.open( 
				GlobalParameter.ACCOUNT_CODE_SUBSIDIARY_EXCEL_DOWNLOAD_URL + 
				"?query=" + searchStr, "_blank", "");
			}
		}
	}
	
	public Panel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
