package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.RepackagingDetailRepositoryRemote;
import com.gammon.qs.client.repository.RepackagingDetailRepositoryRemoteAsync;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.RepackagingAttachmentWindow;
import com.gammon.qs.client.ui.window.mainSection.AddAddendumToScMethodTwoWindow;
import com.gammon.qs.client.ui.window.mainSection.AddAddendumToSubcontractWindow;
import com.gammon.qs.client.ui.window.mainSection.RepackagingDetailWindow;
import com.gammon.qs.client.ui.window.mainSection.RepackagingUpdateByBQWindow;
import com.gammon.qs.client.ui.window.mainSection.RepackagingUpdateByResourceSummaryWindow;
import com.gammon.qs.client.ui.window.mainSection.RepackagingUpdateByResourceWindow;
import com.gammon.qs.client.ui.window.mainSection.SendRepackagingConfirmEmailWindow;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingAttachment;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;

public class RepackagingListGridPanel extends EditorGridPanel{	
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	private RepackagingEntryRepositoryRemoteAsync repackagingEntryRepository;
	private RepackagingDetailRepositoryRemoteAsync repackagingDetailRepository;
	private BQResourceSummaryRepositoryRemoteAsync bqResourceSummaryRepository;
	private Window childWindow;
	
	private Store dataStore;
	
	// modified by brian on 20110321
	// change screenName to static
	private static String screenName = "Repackaging List";
		
	private Toolbar toolbar = new Toolbar();
	
	private RowSelectionModel rowSelectionModel = new RowSelectionModel();
	
	private ColumnConfig versionColumnConfig;
	private ColumnConfig createdDateColumnConfig;
	private ColumnConfig lastUserColumnConfig;
	private ColumnConfig totalAllowanceColumnConfig;
	private ColumnConfig statusColumnConfig;
	private ColumnConfig statusDescriptionColumnConfig;
	private ColumnConfig remarksColumnConfig;
	
	private ToolbarButton summaryButton;
	private ToolbarButton unlockButton;
	private ToolbarButton updateButton;
	private ToolbarButton updateByBQButton;
	private ToolbarButton updateByResourceButton;
	private ToolbarButton historyButton;
	private ToolbarButton reviewButton;
	private ToolbarButton resetStatusButton;
	private ToolbarButton attachmentButton;
	private ToolbarButton addAddendumToSubcontractButton;
	private ToolbarButton editRemarkButton;
	private ToolbarButton emailButton;
	private ToolbarButton tipsButton;	
	
	private final RecordDef repackagingRecordDef = new RecordDef(
			new FieldDef[] {
				new StringFieldDef("id"),
				new StringFieldDef("version"),
				new DateFieldDef("createdDate"),
				new StringFieldDef("lastUser"),
				new StringFieldDef("totalResourceAllowance"),
				new StringFieldDef("status"),
				new StringFieldDef("statusDescription"),
				new StringFieldDef("remarks")
			}	
		);
	
	private static final Map<String, String> statusDescriptions;
	static{
		Map<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("100", "Unlocked");
		tempMap.put("200", "Updated");
		tempMap.put("300", "Snapshot Generated");
		tempMap.put("900", "Locked");
		statusDescriptions = Collections.unmodifiableMap(tempMap);
	}

	public String getScreenName() {
		return screenName;
	}
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private GlobalMessageTicket globalMessageTicket;
	
	/**
	 * @author koeyyeung
	 * modified on 04 Sep, 2013**/
	public RepackagingListGridPanel(final GlobalSectionController globalSectionController){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.setGlobalSectionController(globalSectionController);
		this.detailSectionController = globalSectionController.getDetailSectionController();
		repackagingEntryRepository = globalSectionController.getRepackagingEntryRepository();
		repackagingDetailRepository = (RepackagingDetailRepositoryRemoteAsync) GWT.create(RepackagingDetailRepositoryRemote.class);
		((ServiceDefTarget)repackagingDetailRepository).setServiceEntryPoint(GlobalParameter.REPACKAGING_DETAIL_REPOSITORY_URL);
		bqResourceSummaryRepository = globalSectionController.getBqResourceSummaryRepository();
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		
		this.setTrackMouseOver(true);
		this.setBorder(false);
		this.setAutoScroll(true);
		
		// added by brian on 20110321
		this.setId(screenName);
		
		setupGridPanel();
		
		//hide detail panel
		detailSectionController.getMainPanel().collapse();
	}
	
	private void setupGridPanel(){
		setupToolbar();
		
		Renderer dateRenderer = new DateRenderer();		
		Renderer amountRender = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		
		versionColumnConfig = new ColumnConfig("Version", "version", 40, false);
		createdDateColumnConfig = new ColumnConfig("Created Date", "createdDate", 60, false);
		createdDateColumnConfig.setRenderer(dateRenderer);
		lastUserColumnConfig = new ColumnConfig("Last User", "lastUser", 60, false);
		totalAllowanceColumnConfig = new ColumnConfig("Total Budget", "totalResourceAllowance", 80, false);
		totalAllowanceColumnConfig.setAlign(TextAlign.RIGHT);
		totalAllowanceColumnConfig.setRenderer(amountRender);
		statusColumnConfig = new ColumnConfig("Status", "status", 30, false);
		statusDescriptionColumnConfig = new ColumnConfig("Status Desc", "statusDescription", 100, false);
		remarksColumnConfig = new ColumnConfig("Remarks", "remarks", 200, false);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(repackagingRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState("version", SortDir.ASC));
		this.setStore(dataStore);
		
		ColumnConfig[] columns = new ColumnConfig[] {			
				versionColumnConfig,
				createdDateColumnConfig,
				lastUserColumnConfig,
				totalAllowanceColumnConfig,
				statusColumnConfig,
				statusDescriptionColumnConfig,
				remarksColumnConfig
		};
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(true);
		this.setView(view);
		
		this.setSelectionModel(rowSelectionModel);
		
		
		// Check for access rights - then add toolbar buttons accordingly
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010104_REPACKAGING_LIST_GRIDPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				try{
					accessRightsList = accessRightsReturned;
					displayButtons();
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);						
				}catch(Exception e){
					UIUtil.alert(e);
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	private void setupToolbar(){
		summaryButton = new ToolbarButton();
		summaryButton.setText("Generate Resource Summaries");
		summaryButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				generateSummaries();
			}
		});
		ToolTip summaryToolTip = new ToolTip();
		summaryToolTip.setTitle("Generate Resource Summaries");
		summaryToolTip.setHtml("Group resources into resource summaries");
		summaryToolTip.applyTo(summaryButton);
		
		unlockButton = new ToolbarButton();
		unlockButton.setText("Unlock");
		unlockButton.setIconCls("unlock-icon");
		unlockButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				unlock();
			}
		});
		ToolTip unlockToolTip = new ToolTip();
		unlockToolTip.setTitle("Unlock");
		unlockToolTip.setHtml("Unlock resources to begin repackaging");
		unlockToolTip.applyTo(unlockButton);
		
		updateButton = new ToolbarButton();
		updateButton.setText("Update");
		updateButton.setIconCls("save-button-icon");
		updateButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showRepackagingEnquiryWindow();
			}
		});
		ToolTip updateToolTip = new ToolTip();
		updateToolTip.setTitle("Update");
		updateToolTip.setHtml("Update, split or merge resources");
		updateToolTip.applyTo(updateButton);
		
		addAddendumToSubcontractButton = new ToolbarButton();
		addAddendumToSubcontractButton.setText("Add Addendum to Subcontract");
		addAddendumToSubcontractButton.setIconCls("add-button-icon");
		addAddendumToSubcontractButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				RepackagingListGridPanel.this.globalSectionController.refreshAwardedPackageStore();
				// added by brian on 20110321
				UIUtil.maskPanelById(screenName, GlobalParameter.LOADING_MSG, true);
				showAddAddendumToSubcontractWindow();
				// added by brian on 20110321
				UIUtil.unmaskPanelById(screenName);
			}
		});
		ToolTip addAddendumToSubcontractToolTip = new ToolTip();
		addAddendumToSubcontractToolTip.setTitle("Add Addendum to Subcontract");
		addAddendumToSubcontractToolTip.setHtml("Add Addendum to awarded subcontract");
		addAddendumToSubcontractToolTip.applyTo(addAddendumToSubcontractButton);
		
		updateByBQButton = new ToolbarButton("Update By BQ Item");
		updateByBQButton.setTooltip("Update By BQ Item", "Update the remeasured quantity of BQ Items");
		updateByBQButton.setIconCls("save-button-icon");
		updateByBQButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showUpdateByBQWindow();
			}
		});
		
		updateByResourceButton = new ToolbarButton("Update By Resource");
		updateByResourceButton.setTooltip("Update By Resource", "Update, split or merge resources");
		updateByResourceButton.setIconCls("save-button-icon");
		updateByResourceButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showUpdateByResourceWindow();
			}
		});
		
		historyButton = new ToolbarButton();
		historyButton.setText("Generate Snapshot");
		historyButton.setIconCls("camera-icon");
		historyButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				checkEntryStatusAndGenerateHistory();
			}
		});
		ToolTip historyToolTip = new ToolTip();
		historyToolTip.setTitle("Generate Snapshot");
		historyToolTip.setHtml("Generate a snapshot of the resources");
		historyToolTip.applyTo(historyButton);
		
		reviewButton = new ToolbarButton();
		reviewButton.setText("Review/Confirm");
		reviewButton.setIconCls("review-icon");
		reviewButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showRepackagingDetailWindow();
			}
		});
		ToolTip reviewToolTip = new ToolTip();
		reviewToolTip.setTitle("Review/Confirm");
		reviewToolTip.setHtml("Review/Confirm the select repackaging version");
		reviewToolTip.applyTo(reviewButton);
		
		/**
		 * @author tikywong
		 * added on December 20, 2011 03:25 PM
		 */
		resetStatusButton = new ToolbarButton();
		resetStatusButton.setText("Reset Status");
		resetStatusButton.setIconCls("reset-icon");
		resetStatusButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				resetStatus();
			}
		});
		ToolTip resetStatusToolTip = new ToolTip();
		resetStatusToolTip.setTitle("Reset Status");
		resetStatusToolTip.setHtml("Reset Repackaging for Repackaging Status = 100");
		resetStatusToolTip.applyTo(resetStatusButton);
		
		
		attachmentButton = new ToolbarButton();
		attachmentButton.setText("Attachments");
		attachmentButton.setIconCls("attachment-icon");
		attachmentButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showAttachmentWindow();
			}
		});
		attachmentButton.setTooltip("Attachments", "View/Edit attachments for the selected repackaging version");
		
		editRemarkButton = new ToolbarButton("Edit Remark");
		editRemarkButton.setIconCls("edit-button-icon");
		editRemarkButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				showEditRemarkPrompt();
			}
		});
		
		emailButton = new ToolbarButton();
		emailButton.setText("Email to Repackaging Approver");
		emailButton.setIconCls("mail-icon");
		emailButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				sendEmailToNotifyRepackagingApprover();
			}
		});
		
		ToolTip emailToolTip = new ToolTip();
		emailToolTip.setTitle("Email To Repackaging Approver");
		emailToolTip.setHtml("Send email to notify the Repackaging Approver to view and confirm the repackaging.");
		emailToolTip.applyTo(emailButton);
		
		/**
		 * Button added by Henry Lai
		 * 02-Dec-2014
		 */
		tipsButton = new ToolbarButton("Repackaging Status Info Tips");
		tipsButton.setIconCls("bulb-icon");
		tipsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByTipsButton(1110);
			}
		});

		toolbar.addButton(summaryButton);
		toolbar.addButton(unlockButton);
		toolbar.addSeparator();
		toolbar.addButton(updateButton);
		toolbar.addButton(updateByBQButton);
		toolbar.addSeparator();
		toolbar.addButton(updateByResourceButton);
		toolbar.addSeparator();
		toolbar.addButton(addAddendumToSubcontractButton);
		toolbar.addSeparator();
		toolbar.addButton(historyButton);
		toolbar.addSeparator();
		toolbar.addButton(reviewButton);
		toolbar.addSeparator();
		toolbar.addButton(resetStatusButton);
		toolbar.addSeparator();
		toolbar.addButton(attachmentButton);
		toolbar.addSeparator();
		toolbar.addButton(editRemarkButton);
		toolbar.addFill();
		toolbar.addSeparator();
		toolbar.addButton(emailButton);
		toolbar.addSeparator();
		toolbar.addButton(tipsButton);
		this.setTopToolbar(toolbar);
		
		summaryButton.hide();
		unlockButton.hide();
		updateButton.hide();
		updateByBQButton.hide();
		updateByResourceButton.hide();
		historyButton.hide();
		reviewButton.hide();
		resetStatusButton.hide();
		attachmentButton.hide();
		addAddendumToSubcontractButton.hide();
		editRemarkButton.hide();
		emailButton.hide();
	}
	
	public void populateGrid(List<RepackagingEntry> repackagingEntries){		
		dataStore.removeAll();
		Record[] data = new Record[repackagingEntries.size()];
		int ind = 0;
		for (RepackagingEntry repackagingEntry : repackagingEntries){
			data[ind++] = repackagingRecordDef.createRecord(new Object[]{
					repackagingEntry.getId(),
					repackagingEntry.getRepackagingVersion(),
					repackagingEntry.getCreateDate(),
					repackagingEntry.getLastModifiedUser(),
					repackagingEntry.getTotalResourceAllowance(),
					repackagingEntry.getStatus(),
					convertStatusDescriptions(repackagingEntry.getStatus()),
					repackagingEntry.getRemarks()
			});
		}
		dataStore.add(data);
		displayButtons();
		
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private String convertStatusDescriptions(String status){
		String statusDesc = statusDescriptions.get(status);
		if("Unlocked".equals(statusDesc) || "Updated".equals(statusDesc))
			return "<font color="+GlobalParameter.GREEN_COLOR+">"+ statusDesc+"</font>";
		else if("Snapshot Generated".equals(statusDesc))
			return "<font color="+GlobalParameter.ORANGE_COLOR+">"+ statusDesc+"</font>";
		else if("Locked".equals(statusDesc))
			return "<font color="+GlobalParameter.GREY_COLOR+">"+ statusDesc+"</font>";
		return statusDesc;
	}
	
	private void displayButtons(){
		summaryButton.hide();
		unlockButton.hide();
		historyButton.hide();
		updateButton.hide();
		updateByBQButton.hide();
		updateByResourceButton.hide();
		reviewButton.hide();
		resetStatusButton.hide();
		attachmentButton.hide();
		addAddendumToSubcontractButton.hide();
		editRemarkButton.hide();
		Record lastRecord = dataStore.getAt(0);
		if(accessRightsList != null){
			if(accessRightsList.contains("WRITE")){
				if(dataStore.getCount() == 0){
					summaryButton.show();
				}
				else{
					reviewButton.show();
					attachmentButton.show();
					editRemarkButton.show();
					if(lastRecord.getAsString("status").equals("900"))
						unlockButton.show();
					else{
						addAddendumToSubcontractButton.show();
						if(globalSectionController.getJob().getRepackagingType().equals("1"))
							updateButton.show();
						else{
							updateByBQButton.show();
							updateByResourceButton.show();
						}
						
						// added by Tiky on December 20, 2011
						if(lastRecord.getAsString("status").equals("100"))
							resetStatusButton.show();
						
						if(lastRecord.getAsString("status").equals("200"))
							historyButton.show();
						
						/**
						 * @author koeyyeung
						 * added on 19 Aug, 2014
						 * **/ 
						if(lastRecord.getAsString("status").equals("300"))
							emailButton.show();
						
					}
				}
			}
			else if(accessRightsList.contains("READ")){
				if(dataStore.getCount() > 0){
					reviewButton.show();
					if(!lastRecord.getAsString("status").equals("900")){
						addAddendumToSubcontractButton.show();
						if(globalSectionController.getJob().getRepackagingType().equals("1"))
							updateButton.show();
						else{
							updateByBQButton.show();
							updateByResourceButton.show();
						}
					}
				}
			}
		}
	}
	
	private void showRepackagingEnquiryWindow(){
		if(childWindow != null)
			return;
		
		Record lastRecord = dataStore.getAt(0);
		if(lastRecord == null || lastRecord.getAsString("status").equals("900")){
			MessageBox.alert("Resources are locked, click 'Unlock' to begin the repackaging process");
			return;
		}
		@SuppressWarnings("unused")
		RepackagingUpdateByResourceSummaryWindow enquiryWindow = new RepackagingUpdateByResourceSummaryWindow(RepackagingListGridPanel.this, new Long(lastRecord.getAsString("id")));
		/*childWindow = enquiryWindow;
		childWindow.show();*/
	}
	
	private void showAddAddendumToSubcontractWindow(){
		if(childWindow != null)
			return;
		
		Record lastRecord = dataStore.getAt(0);
		if(lastRecord == null || lastRecord.getAsString("status").equals("900")){
			MessageBox.alert("Resources are locked, click 'Unlock' to begin the repackaging process");
			return;
		}
		if(globalSectionController.getJob().getRepackagingType().equals("1")){
			AddAddendumToSubcontractWindow addAddendumToSubcontractWindow = new AddAddendumToSubcontractWindow(this, Long.valueOf(lastRecord.getAsString("id")));
			childWindow = addAddendumToSubcontractWindow;
		}
		else{
			AddAddendumToScMethodTwoWindow addAddendumToScMethodTwoWindow = new AddAddendumToScMethodTwoWindow(this);
			childWindow = addAddendumToScMethodTwoWindow;
		}
		childWindow.show();
	}
	
	private void showUpdateByBQWindow(){
		if(childWindow != null)
			return;
		Record lastRecord = dataStore.getAt(0);
		if(lastRecord == null || lastRecord.getAsString("status").equals("900")){
			MessageBox.alert("Resources are locked, click 'Unlock' to begin the repackaging process");
			return;
		}
		RepackagingUpdateByBQWindow updateByBQWindow = new RepackagingUpdateByBQWindow(this);
		childWindow = updateByBQWindow;
		childWindow.show();
	}
	
	private void showUpdateByResourceWindow(){
		if(childWindow != null)
			return;
		Record lastRecord = dataStore.getAt(0);
		if(lastRecord == null || lastRecord.getAsString("status").equals("900")){
			MessageBox.alert("Resources are locked, click 'Unlock' to begin the repackaging process");
			return;
		}
		@SuppressWarnings("unused")
		RepackagingUpdateByResourceWindow updateByResourceWindow = new RepackagingUpdateByResourceWindow(this);
		/*childWindow = updateByResourceWindow;
		childWindow.show();*/
	}
	
	private void showRepackagingDetailWindow(){
		if(childWindow != null)
			return;
		
		Record record = rowSelectionModel.getSelected();
		if(record == null){
			MessageBox.alert("Please select a version to review");
			return;
		}
		else if(Integer.parseInt(record.getAsString("status"))<300){
			MessageBox.alert("A snapshot has not yet been generated for this repackaging version");
			return;
		}
		RepackagingEntry repackagingEntry = repackagingEntryFromRecord(record);
		try{
			RepackagingDetailWindow repackagingDetailWindow = new RepackagingDetailWindow(this, repackagingEntry);
			childWindow = repackagingDetailWindow;
			childWindow.show();
		}catch(Exception e){
			UIUtil.alert(e.getMessage());
		}
	}
	
	private void showAttachmentWindow(){
		Record record = rowSelectionModel.getSelected();
		if(record == null){
			MessageBox.alert("Please select a repackaging version to view/edit attachments");
			return;
		}
		RepackagingEntry repackagingEntry = repackagingEntryFromRecord(record);
		showRepackagingAttachmentWindow(repackagingEntry);
	}
	
	private void showEditRemarkPrompt(){
		final Record record = dataStore.getAt(0);
		if("900".equals(record.getAsString("status"))){
			MessageBox.alert("This remark cannot be edited. The entry has been locked.");
			return;
		}	
		MessageBoxConfig config = new MessageBoxConfig();
		config.setButtons(MessageBox.OKCANCEL);
		config.setClosable(false);
		config.setTitle("Edit Remark");
		config.setMsg("Edit the reason/remark for this repackaging");
		config.setMultiline(true);
		config.setValue(record.getAsString("remarks"));
		config.setCallback(new MessageBox.PromptCallback(){
			public void execute(String btnID, String text) {
				if(btnID.equals("cancel"))
					return;
				//
				//Fix the bug on override the database record when update Remark.
				//By Peter Chan
				
				if(text != null && text.length() > 255)
					text = text.substring(0, 255);
				if (text==null)
					text="";
				final String finalText = text;
				SessionTimeoutCheck.renewSessionTimer();
				repackagingEntryRepository.getRepackagingEntry(Long.valueOf(record.getAsString("id")),new AsyncCallback<RepackagingEntry>(){

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
					}

					public void onSuccess(RepackagingEntry repackageRec) {
						Record updateRecord = repackagingRecordDef.createRecord(new Object[]{
								repackageRec.getId(),
								repackageRec.getRepackagingVersion(),
								repackageRec.getCreateDate(),
								repackageRec.getLastModifiedUser(),
								repackageRec.getTotalResourceAllowance(),
								repackageRec.getStatus(),
								statusDescriptions.get(repackageRec.getStatus()),
								finalText
						});
						record.set("remarks", finalText);
						saveEntry(updateRecord);
					}
					
					
				});
//				record.set("remarks", text);
//				saveEntry(record);
			}
		});
		MessageBox.show(config);
	}
	
	private void saveEntry(Record record){
		RepackagingEntry entry = repackagingEntryFromRecord(record);
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Saving", true);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.saveRepackagingEntry(entry, new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean arg0) {
				dataStore.commitChanges();
				UIUtil.unmaskMainPanel();
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.checkSessionTimeout(e, true,getGlobalSectionController().getUser());
			}
		});
	}
	
	private void generateSummaries(){
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Generating Summaries", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqResourceSummaryRepository.groupResourcesIntoSummaries(globalSectionController.getJob(), new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean repackagingEntry) {
				if(repackagingEntry == null){
					alertAndRefresh("generated the summaries.");
					UIUtil.unmaskMainPanel();
				}
				else{
					SessionTimeoutCheck.renewSessionTimer();
					repackagingDetailRepository.generateResourceSummaries(globalSectionController.getJob(), new AsyncCallback<RepackagingEntry>() {
						public void onSuccess(RepackagingEntry repackagingEntry) {
							List<RepackagingEntry> entryList = new ArrayList<RepackagingEntry>(1);
							entryList.add(repackagingEntry);
							populateGrid(entryList);
							UIUtil.unmaskMainPanel();
						}
						public void onFailure(Throwable e) {
							UIUtil.throwException(e);
							UIUtil.unmaskMainPanel();
						}
					});
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskMainPanel();
			}
		});
	}
	
	/**
	 * @author tikywong
	 * created on December 20, 2011 03:45 PM
	 * The repackaging entry will be removed if the repackaging status is 100
	 */
	private void resetStatus() {
		Record lastRecord = null;
		
		int size = dataStore.getCount();
		if(size > 0){
			lastRecord = dataStore.getAt(0);
			if(!lastRecord.getAsString("status").equals("100")){
				MessageBox.alert("Repackaging can only be reset when the status is 100. Current status: "+lastRecord.getAsString("status").equals("100"));
				return;
			}
		}
		
		RepackagingEntry repackagingEntry = repackagingEntryFromRecord(lastRecord);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.removeRepackagingEntry(repackagingEntry, new AsyncCallback<Boolean>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false, getGlobalSectionController().getUser());				
			}

			public void onSuccess(Boolean removed) {
				MessageBox.alert("Repackaging Entry is removed successfully.");
				globalSectionController.navigateToRepackaging();
			}});
	}
	
	/*
	 * If record already exists with status 100, 200 or 300 (!=900), return error.
	 * Else, create new repackagingEntry
	 */
	private void unlock(){
		int size = dataStore.getCount();
		Record lastRecord;
		Integer newVersion = 1;
		if(size > 0){
			lastRecord = dataStore.getAt(0);
			newVersion = Integer.valueOf(lastRecord.getAsString("version"))+1;
			if(!lastRecord.getAsString("status").equals("900")){
				MessageBox.alert("Repackaging already open: version " + lastRecord.getAsString("version"));
				return;
			}
		}
		final Integer version = newVersion;
		MessageBox.prompt("Reason for Repackaging", "Please input a reason/remark for this repackaging", new MessageBox.PromptCallback(){
			public void execute(String btnID, String text) {
				if(btnID.equals("cancel"))
					return;
				try {
					if(text != null && text.length() > 255)
						text = text.substring(0, 255);
					SessionTimeoutCheck.renewSessionTimer();
					repackagingEntryRepository.createRepackagingEntry(version, 
							getGlobalSectionController().getJob(), 
							getGlobalSectionController().getUser().getUsername(), text, 
							new AsyncCallback<RepackagingEntry>(){
						public void onSuccess(RepackagingEntry newEntry){
							Record data = repackagingRecordDef.createRecord(new Object[]{
									newEntry.getId(),
									newEntry.getRepackagingVersion(),
									newEntry.getCreateDate(),
									newEntry.getLastModifiedUser(),
									newEntry.getTotalResourceAllowance(),
									newEntry.getStatus(),
									convertStatusDescriptions(newEntry.getStatus()),
									newEntry.getRemarks()
							});
							addNewRow(data);
							displayButtons();
						}
						
						public void onFailure(Throwable e){
							alertAndRefresh("unlocked a new entry.");
						}
					});
				} catch (Exception e) {
					UIUtil.alert(e);
				}
			}}, true);
	}
	
	/**
	 * @author koeyyeung
	 * created on 04 Sep, 2013
	 * **/
	private void addNewRow(Record data){
		Record[] recordList = new Record[dataStore.getCount()];
		for(int i = 0; i< dataStore.getCount(); i++)
			recordList[i] = dataStore.getAt(i);

		dataStore.removeAll();
		dataStore.add(data);
		for(Record record: recordList)
			dataStore.add(record);
	}
	
	public void alertAndRefresh(String action){
		MessageBox.alert("Entry Status Has Changed", 
				"Another user has already " + action + "<br/>Click 'OK' to refresh this panel.<br/>If the problem persists, please contact the helpdesk.", 
				new MessageBox.AlertCallback(){
					public void execute() {
						globalSectionController.navigateToRepackaging();
					}
		});
	}
	
	//Update the status of the latest entry
	public void updateStatus(final String status){
		if(!statusDescriptions.containsKey(status))
			return;

		Record lastRecord = dataStore.getAt(0);
		if (lastRecord == null || lastRecord.getAsString("status").equals(status) || lastRecord.getAsString("status").equals("900"))
			return;

		lastRecord.set("status", status);
		lastRecord.set("statusDescription", convertStatusDescriptions(status));
		RepackagingEntry repackagingEntry = repackagingEntryFromRecord(lastRecord);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			repackagingEntryRepository.saveRepackagingEntry(repackagingEntry, new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean arg0) {
					dataStore.commitChanges();
					globalSectionController.setCurrentRepackagingStatus(status);
					displayButtons();
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,getGlobalSectionController().getUser());
				}	
			});
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	
	}
	
	private void checkEntryStatusAndGenerateHistory(){
		final Record lastRecord = dataStore.getAt(0);
		if (lastRecord == null || !lastRecord.getAsString("status").equals("200"))
			return;
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.getRepackagingEntry(new Long(lastRecord.getAsString("id")), new AsyncCallback<RepackagingEntry>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getGlobalSectionController().getUser());
			}

			public void onSuccess(RepackagingEntry repackagingEntry) {
				if("900".equals(repackagingEntry.getStatus()))
					alertAndRefresh("confirmed this repackaging entry.");
				else{
					generateHistory(repackagingEntry);
					//Send Email to notify the Repackaging Approver to confirm the repackaging
					sendEmailToNotifyRepackagingApprover();
				}
			}
		});
	}
	
	private void sendEmailToNotifyRepackagingApprover(){
		MessageBox.confirm("Confirm", "Do you want to send an email to notify the Repackaging Approver to confirm the repackaging?", new MessageBox.ConfirmCallback(){
			public void execute(String btnID) {
				if(btnID.equals("yes")){
					SendRepackagingConfirmEmailWindow sendRepackagingConfirmEmailWindow = new SendRepackagingConfirmEmailWindow(globalSectionController);
					sendRepackagingConfirmEmailWindow.show();
				}
			}
		});
	}
	
	private void generateHistory(final RepackagingEntry repackagingEntry){
		final Record lastRecord = dataStore.getAt(0);
		Job job = globalSectionController.getJob();
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Generating Snapshot", true);
		try{
			if(job.getRepackagingType().equals("1")){
				SessionTimeoutCheck.renewSessionTimer();
				repackagingDetailRepository.prepareRepackagingDetails(repackagingEntry, new AsyncCallback<Double>(){
					public void onSuccess(Double totalResourceAllowance) {
						lastRecord.set("totalResourceAllowance", totalResourceAllowance.toString());
						updateStatus("300");
						dataStore.commitChanges();
						UIUtil.unmaskMainPanel();
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskMainPanel();
					}
				});	
			}
			else if(job.getRepackagingType().equals("2")){
				SessionTimeoutCheck.renewSessionTimer();
				bqResourceSummaryRepository.generateSnapshotMethodTwo(job, repackagingEntry, new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean updated) {
						if(updated){
							SessionTimeoutCheck.renewSessionTimer();
							repackagingDetailRepository.prepareRepackagingDetails(repackagingEntry, new AsyncCallback<Double>(){
								public void onSuccess(Double totalResourceAllowance) {
									lastRecord.set("totalResourceAllowance", totalResourceAllowance.toString());
									updateStatus("300");
									dataStore.commitChanges();
									UIUtil.unmaskMainPanel();
								}
								public void onFailure(Throwable e) {
									UIUtil.throwException(e);
									UIUtil.unmaskMainPanel();
								}
							});	
						}
						UIUtil.unmaskMainPanel();
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskMainPanel();
					}
				});
			}
			else if(job.getRepackagingType().equals("3")){
				SessionTimeoutCheck.renewSessionTimer();
				bqResourceSummaryRepository.generateSnapshotMethodThree(job, repackagingEntry, new AsyncCallback<Boolean>(){
					public void onSuccess(Boolean updated) {
						if(updated){
							SessionTimeoutCheck.renewSessionTimer();
							repackagingDetailRepository.prepareRepackagingDetails(repackagingEntry, new AsyncCallback<Double>(){
								public void onSuccess(Double totalResourceAllowance) {
									lastRecord.set("totalResourceAllowance", totalResourceAllowance.toString());
									updateStatus("300");
									dataStore.commitChanges();
									UIUtil.unmaskMainPanel();
								}
								public void onFailure(Throwable e) {
									UIUtil.throwException(e);
									UIUtil.unmaskMainPanel();
								}
							});	
						}
						UIUtil.unmaskMainPanel();
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskMainPanel();
					}
				});
			}
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}
	
	//Copy data from a record into a RepackagingEntry object
	public RepackagingEntry repackagingEntryFromRecord(Record record){
		RepackagingEntry repackagingEntry = new RepackagingEntry();
		String id = record.getAsString("id");
		if(id != null)
			repackagingEntry.setId(new Long(id));
		String version = record.getAsString("version");
		if(version != null)
			repackagingEntry.setRepackagingVersion(new Integer(version));
		repackagingEntry.setCreateDate(record.getAsDate("createdDate"));
		repackagingEntry.setTotalResourceAllowance(record.getAsDouble("totalResourceAllowance"));
		repackagingEntry.setStatus(record.getAsString("status"));
		repackagingEntry.setRemarks(record.getAsString("remarks"));
		repackagingEntry.setJob(globalSectionController.getJob());
		return repackagingEntry;
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:52:46 AM
	 * 
	 * For RepackagingListGridPanel
	 */
	private void showRepackagingAttachmentWindow(final RepackagingEntry repackagingEntry){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().getRepackagingAttachments(repackagingEntry.getId(), new AsyncCallback<List<RepackagingAttachment>>(){
			public void onSuccess(List<RepackagingAttachment> attachments) {
				if(globalSectionController.getCurrentWindow() == null){
					globalSectionController.setCurrentWindow(new RepackagingAttachmentWindow(globalSectionController, repackagingEntry.getId(), repackagingEntry.getRepackagingVersion()));
					((RepackagingAttachmentWindow)globalSectionController.getCurrentWindow()).populateGrid(attachments);
					globalSectionController.getCurrentWindow().show();
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);				
			}
		});
	}

	public void setGlobalSectionController(GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void closeCurrentWindow() {
		if(childWindow != null){
			childWindow.close();
			childWindow = null;
		}
	}
}
