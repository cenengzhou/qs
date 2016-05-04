package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.domain.Job;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * @author matthewatc 14:13:46 4 Jan 2012 (UTC+8) Panel to display and edit
 *         information about the selected job.
 *         To add/remove/edit a field, both the loadFields and saveFields
 *         methods must be amended (the rest is dynamically generated): a field
 *         can be inserted anywhere in the order, but the corresponding load and
 *         save lines for each field must appear in the same position in the
 *         order (as the FieldData list is iterated through)
 */
public class JobGeneralInformationMainPanel extends Panel {

	private GlobalSectionController					globalSectionController;
	private UserAccessRightsRepositoryRemoteAsync	userAccessRightsRepository;

	private Job										job;

	public static final String						JOB_HEADER_FORM_PANEL_ID	= "JobHeaderFormPanel";										// the id of this panel

	public static final String						FORM_TEXT_FIELD_STYLE		= "right-align";												// the css style to be applied to the text fields
	public static final String						FORM_DATE_FIELD_STYLE		= "left-align";												// css style to apply to the date fields
	public static final String						FORM_LABEL_STYLE			= "table-cell";												// css style to apply to the field labels
	public static final String						INVALID_TEXT_FIELD_STYLE	= "text-red";													// css style to apply to fields marked invalid
	public static final String						SAVE_BUTTON_STYLE			= "table-cell";
	public static final String						STATUS_LABEL_STYLE			= "table-cell-right-align";
	public static final String						DESCRIPTION_LABEL_STYLE		= "table-description-cell";

	public static final String						DEFAULT_REGEX				= "^[A-Za-z0-9_]*$";											// regex with which to validate text input by default
	public static final String						ANYSTRING_REGEX				= "^[\\x20-\\x7E]*$";											// reges without limitation
	public static final String						INTEGER_REGEX				= "^[0-9]*$";													// regex with which to validate integer-only text input
	public static final String						DECIMAL_REGEX				= "^[0-9\\,]*\\.?[0-9]*$";										// regex with which to validate decimal-only text input
	public static final String						SINGLE_CHAR_REGEX			= "^.$";														// regex with which to validate single-character text input

	public static final String						DATE_FIELD_FORMAT			= GlobalParameter.DATEFIELD_DATEFORMAT;
	public static final String						DATE_FIELD_FORMAT_ALT		= "dmY|dmy|d/m/y|d/m/Y|j/n/y|j/n/Y|d.m.y|d.m.Y|j.n.y|j.n.Y";

	public static final String						SAVE_BUTTON_TEXT			= "Save";														// the text to appear on the save button

	public static final int							SAVE_PANEL_HEIGHT			= 28;															// set empirically
	public static final int							SPACER_HEIGHT				= 20;

	private ArrayList<FieldDatum>					fieldData;																					// holds all of the actual information for constructing the form

	private Panel mainPanel; 
	private Panel									formPanel;

	private Toolbar									toolbar;
	private ToolbarButton							saveToolbarButton;

	private boolean									canWrite;																					// is set to true if it is known that the user has write permission (if false, user may or may not have permission)

	final AmountRenderer							amountRenderer;
	final RateRenderer								rateRenderer;

	private DetailSectionController					detailSectionController;

	public JobGeneralInformationMainPanel(final GlobalSectionController globalSectionController, Job job) {
		super();

		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();

		// hide detailSectionPanel
		detailSectionController.getMainPanel().collapse();

		amountRenderer = new AmountRenderer(globalSectionController.getUser());
		rateRenderer = new RateRenderer(globalSectionController.getUser());

		this.job = job;

		this.setLayout(new RowLayout());
		this.setId(JOB_HEADER_FORM_PANEL_ID);
		this.setBorder(false);
		this.setFrame(true);

		canWrite = false;
		addElementsToPanel();
		getPermissions();

	}

	/*
	 * actually constructs the form
	 */
	private void addElementsToPanel() {

		fieldData = new ArrayList<FieldDatum>();

		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setBorder(false);

		formPanel = new Panel();
		formPanel.setLayout(new TableLayout(6));
		formPanel.setAutoScroll(true);
		formPanel.setBorder(false);
		formPanel.setPaddings(2);

		loadFields();

		for (final FieldDatum fdt : fieldData) {
			if (fdt.labelText != null) {
				fdt.label = new Label(fdt.labelText);
				fdt.label.setCtCls(FORM_LABEL_STYLE);
				formPanel.add(fdt.label);

				if (fdt.isDate){ // if the property is a date, use the DateField
					// input
					Panel fieldPanel = new Panel();
					fieldPanel.setLayout(new FormLayout());
					fieldPanel.setBorder(false);
					fdt.dateField = new DateField();
					fdt.dateField.addListener(FieldFactory.updateDatePickerWidthListener());
					fdt.dateField.setHideLabel(true);
					fdt.dateField.setWidth(fdt.width);
					fdt.dateField.setFieldClass(FORM_DATE_FIELD_STYLE);
					fdt.dateField.setFormat(DATE_FIELD_FORMAT);
					fdt.dateField.setCtCls(FORM_LABEL_STYLE);
					fdt.dateField.setAltFormats(DATE_FIELD_FORMAT_ALT);
					if (fdt.dateValue != null) {
						fdt.dateField.setValue(fdt.dateValue);
					}
					fieldPanel.add(fdt.dateField);
					formPanel.add(fieldPanel);

					fdt.dateField.setDisabled(!canWrite);
				}
				else if(fdt.isComboBox){
					String CPF_APPLICABLE_VALUE = "cpfApplicableValue";
					String CPF_APPLICABLE_DISPLAY = "cpfApplicableDisplay"; 
					String[][] cpfApplicable = new String[][] {};

					cpfApplicable = new String[][]{
							{"0", "Not Applicable"},
							{"1","Applicable"}
					};
					Store cpfApplicableStore = new SimpleStore(new String[]{CPF_APPLICABLE_VALUE,CPF_APPLICABLE_DISPLAY},cpfApplicable);

					fdt.comboBox = new ComboBox("CPF Applicable", "cpfApplicable", fdt.width);
					fdt.comboBox.setStore(cpfApplicableStore);
					fdt.comboBox.setValueField(CPF_APPLICABLE_VALUE);
					fdt.comboBox.setDisplayField(CPF_APPLICABLE_DISPLAY);
					fdt.comboBox.setForceSelection(true);

					if(!fdt.fieldValue.equals("1"))
						fdt.comboBox.setValue(cpfApplicable[0][0]);
					else
						fdt.comboBox.setValue(fdt.fieldValue);

					formPanel.add(fdt.comboBox);
				}
				//Text Field
				else {
					Panel fieldPanel = new Panel();
					fieldPanel.setLayout(new FormLayout());
					fieldPanel.setBorder(false);
					if (fdt.editable != null && Editable.NONE.equals(fdt.editable)) {
						Label labelValue = new Label();;
						labelValue.setCtCls(FORM_LABEL_STYLE);
						if (fdt.fieldValue != null) {
							labelValue.setText(fdt.fieldValue);
						}

						fieldPanel.add(labelValue);
						formPanel.add(fieldPanel);
					} else {
						fdt.field = new TextField();
						fdt.field.setHideLabel(true);
						fdt.field.setWidth(fdt.width);
						fdt.field.setCtCls(FORM_LABEL_STYLE);
						fdt.field.setFieldClass(FORM_TEXT_FIELD_STYLE);
						fdt.field.setRegex(fdt.regex);
						if (fdt.fieldValue != null) {
							fdt.field.setValue(fdt.fieldValue);
						}
						fieldPanel.add(fdt.field);
						formPanel.add(fieldPanel);

						fdt.field.setDisabled(!canWrite);
					}
				}

				fdt.descriptionLabel = new Label(); // labels to contain descriptions of field contents (values set in populateDescriptions)
				fdt.descriptionLabel.setCtCls(STATUS_LABEL_STYLE);
				formPanel.add(fdt.descriptionLabel);

			} else {
				/*
				 * skip this position in the layout (the spacer height is used
				 * to leave a vertical space on table lines which contain no
				 * fields)
				 */
				for (int i = 0; i < 3; i++) {
					Panel formSpacer = new Panel();
					formSpacer.setBorder(false);
					formSpacer.setHeight(SPACER_HEIGHT);
					formPanel.add(formSpacer);
				}
			}
		}

		/*
		 * modified by matthewlam, 2015-01-30
		 * Bug fix #98 - Job Information - Cannot scroll to the bottom
		 */
		toolbar = new Toolbar();
		toolbar.setWidth(0);
		saveToolbarButton = new ToolbarButton(SAVE_BUTTON_TEXT);
		saveToolbarButton.setIconCls("save-button-icon");
		saveToolbarButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				saveFields();
			}
		});

		toolbar.addButton(saveToolbarButton);
		toolbar.addSeparator();
		mainPanel.setTopToolbar(toolbar);
		mainPanel.add(formPanel);
		this.add(mainPanel);
	}

	private void getPermissions() {
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget) userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);

		try {
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(
					globalSectionController.getUser().getUsername(),
						RoleSecurityFunctions.F010503_JOB_GENERAL_INFORMATION_MAINPANEL,
						new AsyncCallback<List<String>>() {
							public void onSuccess(List<String> accessRightsReturned) {
								securitySetup(accessRightsReturned);
							}

							public void onFailure(Throwable e) {
								MessageBox.alert("Failed to retrieve user permissions: " + e.getMessage());
							}
						});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void securitySetup(List<String> accessRightsList) {
		if (accessRightsList.contains("WRITE") || accessRightsList.contains("ADMIN")) {
			canWrite = true;
			toolbar.setWidth("100%");
			for (FieldDatum fdt : fieldData) {
				// for each fieldDatum, enable if the user has the relevant permissions
				if (fdt.labelText != null) {
					if (fdt.isDate){
						if (fdt.editable == Editable.ALL) {
							fdt.dateField.setDisabled(false);
						} else if ((fdt.editable == Editable.ADMIN) && !(accessRightsList == null) && (accessRightsList.contains("ADMIN"))) {
							fdt.dateField.setDisabled(false);
						}
					}
					else if (fdt.isComboBox){
						if (fdt.editable == Editable.ALL) {
							fdt.comboBox.setDisabled(false);
						} else if ((fdt.editable == Editable.ADMIN) && !(accessRightsList == null) && (accessRightsList.contains("ADMIN"))) {
							fdt.comboBox.setDisabled(false);
						}
					}
					else {//Text Field
						if (fdt.editable == Editable.ALL) {
							fdt.field.setDisabled(false);
						} else if ((fdt.editable == Editable.ADMIN) && !(accessRightsList == null) && (accessRightsList.contains("ADMIN"))) {
							fdt.field.setDisabled(false);
						}
					}
				}
			}
		}
	}

	/*
	 * populates the fieldData array with the data from the Job object
	 */
	private void loadFields() {
		fieldData.add(new FieldDatum()); // the blank field
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum("Job Number:", job.getJobNumber(), INTEGER_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Job Name:", job.getDescription(), INTEGER_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Company:", job.getCompany(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Customer No.:", job.getEmployer(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Contract Type:", job.getContractType(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Division:", job.getDivision(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Department:", job.getDepartment(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Solo/JV:", job.getSoloJV(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Completion Status:", job.getCompletionStatus(), DEFAULT_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Insurance CAR:", job.getInsuranceCAR(), DECIMAL_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Insurance ECI:", job.getInsuranceECI(), DECIMAL_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Insurance TPL:", job.getInsuranceTPL(), DECIMAL_REGEX, Editable.NONE, 170));
		fieldData.add(new FieldDatum("Client Contract Number:", job.getClientContractNo(), ANYSTRING_REGEX, false, 170));
		fieldData.add(new FieldDatum("JV Partner Number:", job.getJvPartnerNo(), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("JV %:", doublePercentageToString(job.getJvPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum("Original Contract Value:", doubleAmountToString(job.getOriginalContractValue()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Projected Contract Value:", doubleAmountToString(job.getProjectedContractValue()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Orginal Nominated SC Contract Value:", doubleAmountToString(job.getOrginalNominatedSCContractValue()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Tender GP:", doubleToString(job.getTenderGP()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Forecast End Year:", integerToString(job.getForecastEndYear()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("Forecast End Period:", integerToString(job.getForecastEndPeriod()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("Max Retention %:", doublePercentageToString(job.getMaxRetentionPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Interim Retention %:", doublePercentageToString(job.getInterimRetentionPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Interim Retention %:", doublePercentageToString(job.getMosRetentionPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Value of BS Work:", doubleAmountToString(job.getValueOfBSWork()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Gross Floor Area:", doubleToString(job.getGrossFloorArea()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Gross Floor Area Unit:", job.getGrossFloorAreaUnit().trim(), DEFAULT_REGEX, false, 170));
		fieldData.add(new FieldDatum("Payment Terms For Nominated SC:", job.getPaymentTermsForNominatedSC(), DEFAULT_REGEX, false, 170));
		fieldData.add(new FieldDatum("Billing Currency:", job.getBillingCurrency().trim(), DEFAULT_REGEX, false, 170));
		fieldData.add(new FieldDatum("Defect Provision %:", doublePercentageToString(job.getDefectProvisionPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("CPF Applicable:", job.getCpfApplicable(), SINGLE_CHAR_REGEX, true, 170));
		fieldData.add(new FieldDatum("CPF Index Name:", job.getCpfIndexName(), DEFAULT_REGEX, false, 170));
		fieldData.add(new FieldDatum("CPF Base Year:", integerToString(job.getCpfBaseYear()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("CPF Base Period:", integerToString(job.getCpfBasePeriod()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("Levy Applicable:", job.getLevyApplicable(), DEFAULT_REGEX, false, 170));
		fieldData.add(new FieldDatum("Levy CITA %:", doublePercentageToString(job.getLevyCITAPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum("Levy PCFB %:", doublePercentageToString(job.getLevyPCFBPercentage()), DECIMAL_REGEX, false, 170));
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum("Actual PCC Date:", job.getActualPCCDate(), 170));
		fieldData.add(new FieldDatum("Actual Making Good Date:", job.getActualMakingGoodDate(), 170));
		fieldData.add(new FieldDatum("Defect Liability Period:", integerToString(job.getDefectLiabilityPeriod()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("Defect List Issued Date:", job.getDefectListIssuedDate(), 170));
		fieldData.add(new FieldDatum("Financial End Date:", job.getFinancialEndDate(), 170));
		fieldData.add(new FieldDatum("Final A/C Settlement Date:", job.getDateFinalACSettlement(), 170));
		fieldData.add(new FieldDatum("Year of Completion:", integerToString(job.getYearOfCompletion()), INTEGER_REGEX, false, 170));
		fieldData.add(new FieldDatum("BQ Finalized Flag:", job.getBqFinalizedFlag() != null ? job.getBqFinalizedFlag().trim() : "", DEFAULT_REGEX, Editable.ADMIN, 170));
		fieldData.add(new FieldDatum("Allow Manual Input SC Work Done:", job.getAllowManualInputSCWorkDone().trim(), SINGLE_CHAR_REGEX, Editable.ADMIN, 170));
		fieldData.add(new FieldDatum("Conversion Status:", job.getConversionStatus(), SINGLE_CHAR_REGEX, Editable.ADMIN, 170));
		fieldData.add(new FieldDatum("Repackaging Type:", job.getRepackagingType(), SINGLE_CHAR_REGEX, Editable.ADMIN, 170));
		fieldData.add(new FieldDatum("QS0 payment Reviewed by Finance:", job.getFinQS0Review(), SINGLE_CHAR_REGEX, Editable.ADMIN, 170));
		fieldData.add(new FieldDatum());
		fieldData.add(new FieldDatum());
	}

	/*
	 * Takes all of the fields in the form, validates them, and updates the Job
	 * object with the new values if validation passes for all fields.
	 */
	private void saveFields() {
		if (!canWrite)
			return;

		for (FieldDatum fdt : fieldData) {
			if ((fdt.labelText != null) && (!fdt.isDate)) {
				if (fdt.field != null && (!fdt.field.isValid())) {
					MessageBox.alert("Save failed: one or more fields contains invalid input");
					return;
				}
			}
		}

		Iterator<FieldDatum> data = fieldData.iterator();
		FieldDatum next;

		try {

			next = data.next(); // the text field that cannot be change
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			if (next.field.isDirty()) {
				job.setClientContractNo(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setJvPartnerNo(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setJvPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			next = data.next();
			next = data.next();
			next = data.next();
			if (next.field.isDirty()) {
				job.setOriginalContractValue(stringToDoubleAmount(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setProjectedContractValue(stringToDoubleAmount(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setOrginalNominatedSCContractValue(stringToDoubleAmount(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setTenderGP(stringToDouble(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setForecastEndYear(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setForecastEndPeriod(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setMaxRetentionPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setInterimRetentionPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setMosRetentionPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setValueOfBSWork(stringToDoubleAmount(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setGrossFloorArea(stringToDouble(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setGrossFloorAreaUnit(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setPaymentTermsForNominatedSC(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setBillingCurrency(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setDefectProvisionPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			//if (next.comboBox.isDirty()) {
				job.setCpfApplicable(next.comboBox.getValueAsString());
			//}
			next = data.next();
			if (next.field.isDirty()) {
				job.setCpfIndexName(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setCpfBaseYear(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setCpfBasePeriod(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setLevyApplicable(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setLevyCITAPercentage(stringToDoublePercentage(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setLevyPCFBPercentage(stringToDoublePercentage(next.field.getText()));
			}
			data.next();
			data.next();
			next = data.next();
			if (next.dateField.isDirty()) {
				job.setActualPCCDate(next.dateField.getValue());
			}
			next = data.next();
			if (next.dateField.isDirty()) {
				job.setActualMakingGoodDate(next.dateField.getValue());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setDefectLiabilityPeriod(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.dateField.isDirty()) {
				job.setDefectListIssuedDate(next.dateField.getValue());
			}
			next = data.next();
			if (next.dateField.isDirty()) {
				job.setFinancialEndDate(next.dateField.getValue());
			}
			next = data.next();
			if (next.dateField.isDirty()) {
				job.setDateFinalACSettlement(next.dateField.getValue());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setYearOfCompletion(stringToInteger(next.field.getText()));
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setBqFinalizedFlag(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setAllowManualInputSCWorkDone(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setConversionStatus(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setRepackagingType(next.field.getText());
			}
			next = data.next();
			if (next.field.isDirty()) {
				job.setFinQS0Review(next.field.getText());
			}
		} catch (NoSuchElementException e) {
			// this should never happen
			MessageBox.alert("Error in saving fields: " + e.getMessage());
		}

		this.globalSectionController.updateJobInfo(this.job);
	}

	/*
	 * Methods for converting the various types returned by the Job methods to
	 * strings and back again. Used in loadFields and saveFields, so changing
	 * the implementation of these methods will change the behavior of the
	 * fields that correspond to properties of the job having the relevant type
	 * (in terms of number of decimal places, representation of percentages
	 * etc).
	 */
	private String integerToString(Integer x) {
		return (x != null) ? x.toString() : null;
	}

	private Integer stringToInteger(String s) {
		return (s != null) ? Integer.parseInt(s) : null;
	}

	private String doubleToString(Double d) {
		return (d != null) ? d.toString() : null;
	}

	private String doublePercentageToString(Double d) {
		return rateRenderer.render(doubleToString(d));
	}

	private String doubleAmountToString(Double d) {
		return amountRenderer.render(doubleToString(d));
	}

	private Double stringToDouble(String s) {
		return (s != null) ? Double.parseDouble(s.replaceAll(",", "")) : null;
	}

	private Double stringToDoublePercentage(String s) {
		return stringToDouble(s);
	}

	private Double stringToDoubleAmount(String s) {
		return stringToDouble(s);
	}

	/*
	 * represents the permissions required to edit a given field NONE: nobody
	 * can edit, regardless of permission ALL: only WRITE permission is required
	 * to edit ADMIN: ADMIN permission is required to edit
	 */
	private enum Editable {
		NONE, ALL, ADMIN
	}

	/*
	 * FieldDatum is used to hold all the relevant information about a field in
	 * the form except the getter and setter of the relevant property in the Job
	 * class (due to limitations of gwt)
	 */
	private class FieldDatum {

		/*
		 * the text to display in the form's label. null in this field is used
		 * to indicate an 'empty' FieldDatum, which if used in the FieldData
		 * list will cause the associated position in the form table to be
		 * skipped
		 */
		public String		labelText;

		public String		fieldValue;		// the value to initially populate the field with if it is a text field
		public String		regex;				// a regex to validate the field if it is a text field
		public TextField	field;				// the TextField itself if it is a text field
		public ComboBox		comboBox;				// the ComboBox itself if it is a comboBox
		public DateField	dateField;			// the DateField itself if it is a date field
		public Label		descriptionLabel;	// a label to contain a description of the field contents
		public Label		label;				// the label to describe the nature of the field
		public Date			dateValue;			// the value to initially populate the field with if it is a date field
		public boolean		isDate;			// true if the field is a date field, false if it is a text field
		public boolean		isComboBox;			// true if the field is a combo box, false if it is a text field
		public Editable		editable;			// an Editable enum describing the permissions required to edit the field
		public int			width;				// the width of the text field

		/* constructor for a text field with default edit permissions */
		public FieldDatum(String labelText, String fieldValue, String regex, boolean isComboBox, int width) {
			this.labelText = labelText;
			this.fieldValue = fieldValue;
			this.regex = regex;
			this.isDate = false;
			this.editable = Editable.ALL;
			this.isComboBox = isComboBox;
			this.width = width;
		}

		/* constructor for a text field */
		public FieldDatum(String labelText, String fieldValue, String regex, Editable editable, int width) {
			this.labelText = labelText;
			this.fieldValue = fieldValue;
			this.regex = regex;
			this.isDate = false;
			this.editable = editable;
			this.width = width;
		}

		/* constructor for a date field */
		public FieldDatum(String labelText, Date dateValue, int width) {
			this.labelText = labelText;
			this.dateValue = dateValue;
			this.isDate = true;
			this.editable = Editable.ALL;
			this.width = width;
		}

		/* constructor for an 'empty' field */
		public FieldDatum() {
			this.labelText = null;
			this.fieldValue = null;
			this.regex = null;
			this.isDate = false;
		}
	}

}