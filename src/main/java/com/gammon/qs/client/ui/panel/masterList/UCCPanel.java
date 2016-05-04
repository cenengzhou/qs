package com.gammon.qs.client.ui.panel.masterList;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

@SuppressWarnings("unchecked")
public class UCCPanel extends TabPanel implements MasterListPanel {
	
	//UI
	private Panel objectPanel;
	private Panel subsidiaryPanel;
	
	
	public UCCPanel(GlobalSectionController globalSectionController)
	{
		super();
		
		this.setBorder(false);
		this.setCollapsible(true);
		this.setWidth(300);
		
				
		
		/*TabPanel subidiaryTabPanel = new TabPanel();
		subidiaryTabPanel.setBorder(false);
		subidiaryTabPanel.setTabPosition(Position.BOTTOM);
		subidiaryTabPanel.setResizeTabs(true);		
		subidiaryTabPanel.setTitle("Subsidiary");
		this.add(subidiaryTabPanel);*/
		objectPanel = new ObjectPanel(globalSectionController);
		this.add(objectPanel);
		
		/*
		TabPanel objectTabPanel = new TabPanel();
		objectTabPanel.setBorder(false);
		objectTabPanel.setTabPosition(Position.BOTTOM);
		objectTabPanel.setResizeTabs(true);
		objectTabPanel.setTitle("Object");
		this.add(objectTabPanel);
		*/
		subsidiaryPanel = new SubsidiaryPanel(globalSectionController);
		this.add(subsidiaryPanel);
		
		
		
		
	}

	public void search(String searchStr) {
		
		((MasterListPanel)this.getActiveTab()).search(searchStr);
		
	}
	
	

}
