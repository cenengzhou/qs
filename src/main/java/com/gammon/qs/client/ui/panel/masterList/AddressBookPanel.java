package com.gammon.qs.client.ui.panel.masterList;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

@SuppressWarnings("unchecked")
public class AddressBookPanel extends TabPanel implements MasterListPanel {
	
	//UI
	private Panel vendorListPanel;
	private Panel clientListPanel;
	
	
	public AddressBookPanel(GlobalSectionController globalSectionController)
	{
		super();
		
		this.setBorder(false);
		this.setCollapsible(true);
		this.setWidth(300);
		
				
		vendorListPanel = new VendorListPanel(globalSectionController);
		this.add(vendorListPanel);

		clientListPanel = new ClientListPanel(globalSectionController);
		this.add(clientListPanel);
		
		
		
		
	}

	public void search(String searchStr) {
		
		((MasterListPanel)this.getActiveTab()).search(searchStr);
		
	}
	
	

}
