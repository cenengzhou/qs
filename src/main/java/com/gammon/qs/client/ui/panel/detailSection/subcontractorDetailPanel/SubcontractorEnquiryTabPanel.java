package com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.RowLayout;

/**
 * @author koeyyeung
 * modified by irischau
 * on 14 Apr 2014 
 */
public class SubcontractorEnquiryTabPanel extends Panel{
	private String subcontractorNo;
	private String type;
	private String detailSectionPanel_ID;
	private GlobalSectionController globalSectionController;
	public SubcontractorEnquiryTabPanel(GlobalSectionController globalSectionController, String subcontractorNo, String type) {
		super();
		this.globalSectionController = globalSectionController;
		this.subcontractorNo = subcontractorNo;
		this.type = type;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setBorder(false);
		
		final TabPanel tabPanel = new TabPanel();
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setResizeTabs(true);
		tabPanel.setBorder(false);
		tabPanel.setAutoScroll(true);
		tabPanel.setMinTabWidth(100);
		tabPanel.setTabWidth(200);
		
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMasterListRepository().searchVendorAddressDetails(subcontractorNo, new AsyncCallback<MasterListVendor>() {
			public void onSuccess(MasterListVendor vendor) {
				if(type.equalsIgnoreCase("SubcontractorEnquiry")){
					tabPanel.add(new SubcontractorInfoDetailPanel(globalSectionController, subcontractorNo, vendor, true));
					tabPanel.add(new AwardedSubcontractDetailPanel(globalSectionController, subcontractorNo));
					tabPanel.add(new TenderAnalysisHistoryDetailPanel(globalSectionController, subcontractorNo));
					tabPanel.add(new SubcontractorWorkScopeDetailPanel(globalSectionController, subcontractorNo));
				}
				else if(type.equalsIgnoreCase("ClientEnquiry")){
					SubcontractorInfoDetailPanel subcontractorInfoDetailPanel = new SubcontractorInfoDetailPanel(globalSectionController, subcontractorNo, vendor, false);
					subcontractorInfoDetailPanel.setTitle("Client Information");
					tabPanel.add(subcontractorInfoDetailPanel);
				}
				
				tabPanel.setActiveTab(0);
				add(tabPanel);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
}
