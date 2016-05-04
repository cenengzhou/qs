/**
 * 
 */
package com.gammon.qs.client.ui.window.treeSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.job.JobDatesWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * @author briantse
 * @Create_Date Apr 29, 2011
 */
public class JobDatesWindow extends Window{
	public static final String WINDOW_ID = "JobDatesWindow";

	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private JobRepositoryRemoteAsync jobRepository;
	
	/**
	 * PANELS
	 */
//	private Panel mainPanel;
	private Panel datePanel;

	/**
	 * WINDOW BUTTON
	 */
	private Button saveButton;
	private Button closeButton;
	
	// Dates field label
	private Label plannedStartDateLabel;
	private Label actualStartDateLabel;
	private Label plannedEndDateLabel;
	private Label actualEndDateLabel;
	private Label anticipatedCompletionDateLabel;
	private Label revisedCompletionDateLabel;
	
	private Label emptySpace;
	
	// Dates input fields
	private DateField plannedStartDateField;
	private DateField actualStartDateField;
	private DateField plannedEndDateField;
	private DateField actualEndDateField;
	private DateField anticipatedCompletionDateField;
	private DateField revisedCompletionDateField;
	
	public JobDatesWindow(final GlobalSectionController globalSectionController, JobDatesWrapper jobDates){
		super();
		this.setId(WINDOW_ID);
		this.setTitle("Job Dates");
		this.globalSectionController = globalSectionController;
		this.setPaddings(2);
		this.setWidth(700);
		this.setHeight(200);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		globalMessageTicket = new GlobalMessageTicket();
		this.globalSectionController = globalSectionController;
		jobRepository = globalSectionController.getJobRepository();
		
		initalizeDatePanel(jobDates);
		
		saveButton = new Button("Save Updates");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				saveDates();
			}
		});
		saveButton.setVisible(false);
		/**
		 * Add Security Control for Job Dates Windows
		 * @author peterchan
		 */
		UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010510_JOB_DATES_WINDOW, new AsyncCallback<List<String>>() {
			
			public void onSuccess(List<String> securityList) {
				if (securityList!=null && securityList.size()>0 && securityList.contains("WRITE"))
					saveButton.setVisible(true);
				UIUtil.unmaskPanelById(WINDOW_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(WINDOW_ID);
			}
		});
		addButton(saveButton);
		closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();
			}
		});
		this.addButton(closeButton);
	}
	
	private void initalizeDatePanel(JobDatesWrapper jobDates){
		
		this.datePanel = new Panel();
		this.datePanel.setPaddings(5);
		this.datePanel.setFrame(true);
		this.datePanel.setAutoHeight(true);
		this.datePanel.setLayout(new TableLayout(5));
		// Dates field label
		plannedStartDateLabel = new Label("Planned Start Date:");
		plannedStartDateLabel.setCtCls("mainCert-table-cell");
		plannedStartDateLabel.setWidth(150);
		datePanel.add(plannedStartDateLabel);
		
		plannedStartDateField = new DateField("Planned Start Date", "plannedStartDate", 140);
		plannedStartDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		plannedStartDateField.setCtCls("mainCert-table-cell");
		plannedStartDateField.setFormat("d/m/Y");
		plannedStartDateField.setAltFormats("dmY");
		if(jobDates.getPlannedStartDate() != null)
			plannedStartDateField.setValue(jobDates.getPlannedStartDate());
		datePanel.add(plannedStartDateField);
		
		emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(50);
		emptySpace.setCtCls("mainCert-table-cell");
		datePanel.add(emptySpace);
		
		actualStartDateLabel = new Label("Actual Start Date:");
		actualStartDateLabel.setCtCls("mainCert-table-cell");
		actualStartDateLabel.setWidth(150);
		datePanel.add(actualStartDateLabel);
		
		actualStartDateField = new DateField("Actual Start Date", "actualStartDate", 140);
		actualStartDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		actualStartDateField.setCtCls("mainCert-table-cell");
		actualStartDateField.setFormat("d/m/Y");
		actualStartDateField.setAltFormats("dmY");
		if(jobDates.getActualStartDate() != null)
			actualStartDateField.setValue(DateUtil.parseDate(DateUtil.formatDate(jobDates.getActualStartDate()), "dd/MM/yyyy"));
		datePanel.add(actualStartDateField);
		
		plannedEndDateLabel = new Label("Planned End Date:");
		plannedEndDateLabel.setCtCls("mainCert-table-cell");
		plannedEndDateLabel.setWidth(150);
		datePanel.add(plannedEndDateLabel);
		
		plannedEndDateField = new DateField("Planned End Date", "plannedEndDate", 140);
		plannedEndDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		plannedEndDateField.setCtCls("mainCert-table-cell");
		plannedEndDateField.setFormat("d/m/Y");
		plannedEndDateField.setAltFormats("dmY");
		if(jobDates.getPlannedEndDate() != null)
			plannedEndDateField.setValue(DateUtil.parseDate(DateUtil.formatDate(jobDates.getPlannedEndDate()), "dd/MM/yyyy"));
		datePanel.add(plannedEndDateField);
		
		emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(50);
		emptySpace.setCtCls("mainCert-table-cell");
		datePanel.add(emptySpace);
		
		actualEndDateLabel = new Label("Actual End Date:");
		actualEndDateLabel.setCtCls("mainCert-table-cell");
		actualEndDateLabel.setWidth(150);
		datePanel.add(actualEndDateLabel);
		
		actualEndDateField = new DateField("Actual End Date", "actualEndDate", 140);
		actualEndDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		actualEndDateField.setCtCls("mainCert-table-cell");
		actualEndDateField.setFormat("d/m/Y");
		actualEndDateField.setAltFormats("dmY");
		if(jobDates.getActualEndDate() != null)
			actualEndDateField.setValue(DateUtil.parseDate(DateUtil.formatDate(jobDates.getActualEndDate()), "dd/MM/yyyy"));
		datePanel.add(actualEndDateField);
		
		anticipatedCompletionDateLabel = new Label("Anticipated Completion Date:");
		anticipatedCompletionDateLabel.setCtCls("mainCert-table-cell");
		anticipatedCompletionDateLabel.setWidth(150);
		datePanel.add(anticipatedCompletionDateLabel);
		
		anticipatedCompletionDateField = new DateField("Anticipated Completion Date", "anticipatedCompletionDate", 140);
		anticipatedCompletionDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		anticipatedCompletionDateField.setCtCls("mainCert-table-cell");
		anticipatedCompletionDateField.setFormat("d/m/Y");
		anticipatedCompletionDateField.setAltFormats("dmY");
		if(jobDates.getAnticipatedCompletionDate() != null)
			anticipatedCompletionDateField.setValue(DateUtil.parseDate(DateUtil.formatDate(jobDates.getAnticipatedCompletionDate()), "dd/MM/yyyy"));
		datePanel.add(anticipatedCompletionDateField);
		
		emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(50);
		emptySpace.setCtCls("mainCert-table-cell");
		datePanel.add(emptySpace);
		
		revisedCompletionDateLabel = new Label("Revised Completion Date");
		revisedCompletionDateLabel.setCtCls("mainCert-table-cell");
		revisedCompletionDateLabel.setWidth(150);
		datePanel.add(revisedCompletionDateLabel);		
		
		revisedCompletionDateField = new DateField("Revised Completion Date", "revisedCompletionDate", 140);
		revisedCompletionDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		revisedCompletionDateField.setCtCls("mainCert-table-cell");
		revisedCompletionDateField.setFormat("d/m/Y");
		revisedCompletionDateField.setAltFormats("dmY");
		if(jobDates.getRevisedCompletionDate() != null)
			revisedCompletionDateField.setValue(DateUtil.parseDate(DateUtil.formatDate(jobDates.getRevisedCompletionDate()), "dd/MM/yyyy"));
		datePanel.add(revisedCompletionDateField);
		
		this.add(datePanel);
	}
	
	// added by brian on 20110504
	// call web service to update the dates in table - F0006 in JDE
	private void saveDates(){
		JobDatesWrapper jobDates = new JobDatesWrapper();
		
		jobDates.setActualEndDate(preventNullException(actualEndDateField.getValue()));
		jobDates.setActualStartDate(preventNullException(actualStartDateField.getValue()));
		jobDates.setAnticipatedCompletionDate(preventNullException(anticipatedCompletionDateField.getValue()));
		jobDates.setJobNumber(this.globalSectionController.getJob().getJobNumber());
		jobDates.setPlannedEndDate(preventNullException(plannedEndDateField.getValue()));
		jobDates.setPlannedStartDate(preventNullException(plannedStartDateField.getValue()));
		jobDates.setRevisedCompletionDate(preventNullException(revisedCompletionDateField.getValue()));
		
		UIUtil.maskPanelById(JobDatesWindow.WINDOW_ID, "now saving...", true);
		String userId = globalSectionController.getUser().getUsername();
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.updateJobDates(jobDates, userId, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(JobDatesWindow.WINDOW_ID);
			}

			public void onSuccess(Boolean updated) {
				if (updated)
					MessageBox.alert("Job Dates have been updated successfully.");
				else
					MessageBox.alert("Failed: Job Dates have not been updated yet.");
				UIUtil.unmaskPanelById(JobDatesWindow.WINDOW_ID);
			}
		});
	}
	
	// prevent pass "" when saving empty Date field
	private Date preventNullException(Date inputDate){
		if(null == inputDate)
			return null;
		else
			return inputDate;
	}
}
