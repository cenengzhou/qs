package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.detailSection.TenderAnalysisComparisonPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;

public class EditSCDatesWindow extends Window{
	
	private static final String WINDOW_ID ="EditSCDatesWindow_ID";
	
	private DateField requisitionApprovedDateField; 
	private DateField tenderAnalysisApprovedDateField;
	private DateField preAwardMeetingDateField;
	private DateField loaSignedDateField;
	private DateField scDocScrDateField;
	private DateField scDocLegalDateField;
	private DateField workCommenceDateField;
	private DateField onSiteStartDateField;
	private ToolbarButton updateButton;
	
	private boolean awardSubcontract;
	private SCPackage scPackage;
	private GlobalSectionController globalSectionController;
	
	//added by heisonwong
	private TabPanel tabPanel;
	private Panel viewPanel;
	private Panel editPanel;
	//added by heisonwong
	
	public EditSCDatesWindow(GlobalSectionController globalSectionController, SCPackage scPackage, boolean awardSubcontract) {
		super();
		this.globalSectionController = globalSectionController;
		this.scPackage = scPackage;
		this.awardSubcontract = awardSubcontract;
		setupUI();
	}

	private void setupUI() {
		
		setTitle("Subcontract Dates"); //modified window's title by heisonwong
		setLayout(new RowLayout());
		setHeight(440);
		setWidth(480);
		setModal(false);
		setClosable(false);		
		setId(WINDOW_ID);
		
		//added by heisonwong, added the Tab Panel
		tabPanel = new TabPanel();
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setResizeTabs(true);
		tabPanel.setBorder(false);
		tabPanel.setAutoScroll(true);
		tabPanel.setMinTabWidth(80);
		tabPanel.setTabWidth(230);
		setupInputPanel();
		setupViewPanel();
		viewPanel.setId("viewPanel");
		editPanel.setId("editPanel");		
		tabPanel.setActiveTab("editPanel");
		add(tabPanel);
		//added by heisonwong
		
	}
	
	private void setupViewPanel(){		//added by heisonwong, added SC related dates and put into the "View Dates"
		
		viewPanel = new Panel();
		viewPanel.setPaddings(5);
		viewPanel.setLayout(new TableLayout(2)); //2 columns in a row
		viewPanel.setTitle("Subcontract Tracking Information");
		
		
		List<Label> newDateLabels = new ArrayList<Label>();
/*		List<Label> newEmptySpaces = new ArrayList<Label>();
		HTML newEmptySpaces2 = new HTML();*/
		List<Label> newDateFields = new ArrayList<Label>();
		SimpleDateFormat sdf = new SimpleDateFormat(GlobalParameter.DATE_FORMAT); //date format : dd/MM/yyyy
		String emptyDate = "- - / - - / - - - -";
		String emptySpaceBeforeDate = "          ";
		setupViewPageButtons();
		
		newDateLabels.add(new Label("Subcontract Created Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("Subcontract Award Approval Request Sent out Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("Subcontract Award Approval Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("Latest Addendum Approval Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("1st Payment Certificate Issued Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("Latest Payment Certificate Issued Date".concat(emptySpaceBeforeDate)));
		newDateLabels.add(new Label("Final Payment Certificate Issued Date"));

		if(scPackage != null) { 
			if(scPackage.getScCreatedDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getScCreatedDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getScAwardApprovalRequestSentDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getScAwardApprovalRequestSentDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getScApprovalDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getScApprovalDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getLatestAddendumValueUpdatedDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getLatestAddendumValueUpdatedDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getFirstPaymentCertIssuedDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getFirstPaymentCertIssuedDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getLastPaymentCertIssuedDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getLastPaymentCertIssuedDate())));
				}
			else{ newDateFields.add(new Label(emptyDate));}
			if(scPackage.getFinalPaymentIssuedDate()!=null){
				newDateFields.add(new Label(sdf.format(scPackage.getFinalPaymentIssuedDate()).concat(emptySpaceBeforeDate)));
				}
			else{ newDateFields.add(new Label(emptyDate.concat(emptySpaceBeforeDate)));}
		}
		
		for(int i = 0; i < newDateFields.size() ; i++) {
			newDateLabels.get(i).setCtCls("table-cell-with-underline");	//added in css to format the dates description
			newDateFields.get(i).setCtCls("table-cell-with-underline-right");  //added in css to format the dates
			viewPanel.add(newDateLabels.get(i));
			viewPanel.add(newDateFields.get(i));
		}
		tabPanel.add(viewPanel);	
	}	
	
	private void setupInputPanel(){ //modified by heisonwong, move all the content into "Input Dates"
		
		editPanel = new Panel();
		editPanel.setLayout(new TableLayout(2));
		editPanel.setPaddings(5);
		editPanel.setTitle("Contractual Dates");
		List<Label> dateLabels = new ArrayList<Label>();
		List<DateField> dateFields = new ArrayList<DateField>();
		setupEditPageButton();
		
		dateLabels.add(new Label("Subcontract Requisition Approved Date"));
		dateLabels.add(new Label("Subcontract Tender Analysis Approved Date"));
		dateLabels.add(new Label("Pre-Award Finalization Meeting Date"));
		dateLabels.add(new Label("Letter of Acceptance Signed by Subcontractor Date"));
		dateLabels.add(new Label("Subcontract Document Executed by Subcontractor Date"));
		dateLabels.add(new Label("Subcontract Document Executed by Legal Date"));
		dateLabels.add(new Label("Works Commencement Date"));
		dateLabels.add(new Label("Subcontractor Start on-site Date"));

		dateFields.add(requisitionApprovedDateField 	= new DateField("Subcontract Requisition Approved Date", "subcontractRequistionDateSearch", 150));
		dateFields.add(tenderAnalysisApprovedDateField 	= new DateField("Subcontract Tender Analysis Approved Date", "tenderAnalysisApprovedDateSearch", 150));
		dateFields.add(preAwardMeetingDateField	 		= new DateField("Pre-Award Finalization Meeting Date", "preAwardMeetingDateSearch", 150));
		dateFields.add(loaSignedDateField 				= new DateField("Letter of Acceptance Signed by Subcontractor Date", "loaSignedDateSearch", 150));
		dateFields.add(scDocScrDateField 				= new DateField("Subcontract Document Executed by Subcontractor Date", "scDocScrDateSearch", 150));
		dateFields.add(scDocLegalDateField 				= new DateField("Subcontract Document Executed by Legal Date", "scDocLegalDateSearch", 150));
		dateFields.add(workCommenceDateField 			= new DateField("Works Commencement Date", "workCommenceDateSearch", 150));
		dateFields.add(onSiteStartDateField 			= new DateField("Subcontractor Start on-site Date", "onSiteStartDateSearch", 150));

		requisitionApprovedDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		tenderAnalysisApprovedDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		preAwardMeetingDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		loaSignedDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		scDocScrDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		scDocLegalDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		workCommenceDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		onSiteStartDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		
		if(scPackage != null) { 
			if(scPackage.getRequisitionApprovedDate()!=null)		requisitionApprovedDateField.setValue(scPackage.getRequisitionApprovedDate());
			if(scPackage.getTenderAnalysisApprovedDate()!=null)		tenderAnalysisApprovedDateField.setValue(scPackage.getTenderAnalysisApprovedDate());
			if(scPackage.getPreAwardMeetingDate()!=null)			preAwardMeetingDateField.setValue(scPackage.getPreAwardMeetingDate());
			if(scPackage.getLoaSignedDate()!=null)					loaSignedDateField.setValue(scPackage.getLoaSignedDate());
			if(scPackage.getScDocScrDate()!=null)					scDocScrDateField.setValue(scPackage.getScDocScrDate());
			if(scPackage.getScDocLegalDate()!=null)					scDocLegalDateField.setValue(scPackage.getScDocLegalDate());
			if(scPackage.getWorkCommenceDate()!=null)				workCommenceDateField.setValue(scPackage.getWorkCommenceDate());
			if(scPackage.getOnSiteStartDate()!=null)				onSiteStartDateField.setValue(scPackage.getOnSiteStartDate());
		}

		for(int i = 0; i < dateFields.size() ; i++) {
			dateLabels.get(i).setCtCls("table-cell");
			dateFields.get(i).setCtCls("table-cell");
			dateFields.get(i).setFormat(GlobalParameter.DATEFIELD_DATEFORMAT);
			editPanel.add(dateLabels.get(i));
			editPanel.add(dateFields.get(i));
		} 
		tabPanel.add(editPanel);
	}
	
	private void setupEditPageButton(){ //modified by heisonwong, move the buttons to "Input Dates"
		
		updateButton = new ToolbarButton("Update");
		updateButton.setIconCls("save-button-icon");
		updateButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				save();
			}
		});
		updateButton.setVisible(false);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010217_EDIT_SCDATES_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
					if(accessRightsReturned.contains("WRITE")) {
						updateButton.setVisible(true);
					} else {
						updateButton.setVisible(false);
					}
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
		ToolbarButton closeButton = new ToolbarButton("Cancel");
		closeButton.setIconCls("cancel-icon");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();
			}
		});
		editPanel.addButton(updateButton);
		editPanel.addButton(closeButton);
	}
	
	
	private void setupViewPageButtons(){  //added by heisonwong, preparing the "Close" button for the "View Dates"

		ToolbarButton closeButton = new ToolbarButton("Close");
		//closeButton.setIconCls("cancel-icon");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();
			}
		});
		viewPanel.addButton(closeButton);
	}
	
	private void save() {
		
		updateButton.disable();
		scPackage.setRequisitionApprovedDate(requisitionApprovedDateField.getValue());
		scPackage.setTenderAnalysisApprovedDate(tenderAnalysisApprovedDateField.getValue());
		scPackage.setPreAwardMeetingDate(preAwardMeetingDateField.getValue());
		scPackage.setLoaSignedDate(loaSignedDateField.getValue());
		scPackage.setScDocScrDate(scDocScrDateField.getValue());
		scPackage.setScDocLegalDate(scDocLegalDateField.getValue());
		scPackage.setWorkCommenceDate(workCommenceDateField.getValue());
		scPackage.setOnSiteStartDate(onSiteStartDateField.getValue());
		
		UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.SAVING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().saveOrUpdateSCPackage(scPackage, new AsyncCallback<String>() {
			public void onFailure(Throwable e){
				UIUtil.throwException(e);
				updateButton.enable();
				UIUtil.unmaskPanelById(WINDOW_ID);
			}
			public void onSuccess(String returnMsg) {	
				if(awardSubcontract){
					((TenderAnalysisComparisonPanel)globalSectionController.getDetailSectionController().getGridPanel()).awardSC();
					globalSectionController.closeCurrentWindow();
					UIUtil.unmaskPanelById(WINDOW_ID);
				}
				else{
					MessageBox.alert("Dates have been saved successfully.");
					updateButton.enable();
					UIUtil.unmaskPanelById(WINDOW_ID);
				}
			}
		});
	}
	
}
	