package com.gammon.qs.client.ui.window.mainSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCWorkScope;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

public class EditPackageHeaderWindow extends Window {
	private static final String WINDOW = "editPackageHeaderWindow";

	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	private SCPackage scPackage;
	
	//-------------------------
	//First column fields
	//-------------------------
	private NumberField packageNoField;
	
	private ComboBox subcontractorNatureField;
	
	private TextArea descriptionField;
	
	private ComboBox currencyCodeListComboBox;
	private NumberField exchangeRateTextField;
	
	private FieldSet subcontractTypeFieldSet = new FieldSet("Subcontract Type");
	private Checkbox labourCheckbox;
	private Checkbox plantCheckbox;
	private Checkbox materialCheckbox;
	
	private FieldSet paymentTermsFieldSet = new FieldSet("Payment Terms");
	private ComboBox paymentTermsComboBox;
	private final Store storeTerms = GlobalParameter.SC_PAYMENT_TERMS_STORE;
	private String[] validPaymentTerms = new String[]{"QS0", "QS1", "QS2", "QS3", "QS4", "QS5", "QS6", "QS7"};
	private TextField paymentTermsDescriptionTextField;
	
	private FieldSet paymentInformationFieldSet = new FieldSet("Payment Information");
	private Checkbox interimPaymentCheckbox;
	private Radio mainContractRadio;
	private Radio subcontractRadio;
	
	//-------------------------
	//Second column fields
	//-------------------------
	private FieldSet retentionFieldSet;
	private Radio noRetentionRadio;
	private Radio lumpSumRetentionRadio;
	private Radio percentageRadio;
	private Radio originalSumRadio;
	private Radio revisedSumRadio;
	private NumberField lumpSumField;
	private NumberField maxRetentionField;
	private NumberField interimRetentionField;
	private NumberField mosRetentionField;
	
	private FieldSet subcontractTermFieldSet = new FieldSet("Subcontract Term");
	private Radio lumpSumRadio;
	private Radio remeasurementRadio;
	
	//-------------------------
	//Third column fields
	//-------------------------
	private FieldSet cpfCalculationFieldSet = new FieldSet("CPF Calculation");
	private Radio cpfSubjectRadio;
	private Radio cpfNotSubjectRadio;
	private NumberField cpfBasePeriodField;
	private NumberField cpfBaseYearField;
	
	FieldSet formOfSubcontract = new FieldSet("Form of Subcontract");
	private Radio majorRadio;
	private Radio minorRadio;
	private Radio consultancyRadio;
	private Radio internalJobRadio;
	private NumberField internalJobField;
	
	private Panel workScopePanel = new Panel();
	private FieldSet workScopeFieldSet = new FieldSet("Work Scope");
	private TextField workScopeTextField;
	private Label workScopeLabel = new Label("Work Scope: ");
	private Label workScopeDescription = new Label();
	
	private FieldSet approvalRoute = new FieldSet("Alternative Approval Sub-type");
	private TextField approvalRouteField;
	private Label approvalRouteLabel = new Label();	
	
	private Window childWindow;
	private EditPackageHeaderWindow thisWindow;
		
	private String workScopeSelectedFromList = "";
	
	
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button saveButton;

	private List<SCWorkScope> scWorkScopeList;
	
	public EditPackageHeaderWindow(final GlobalSectionController globalSectionController, final SCPackage scPackage){
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		this.scPackage = scPackage;
		this.setTitle("Create/Edit Package");
		this.setWidth(930);
		this.setModal(true);
		thisWindow = this;
		this.setId(WINDOW);
		this.setClosable(false);
		
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		FormPanel mainPanel = new FormPanel();
		Panel columnsPanel = new Panel();
		columnsPanel.setLayout(new ColumnLayout());
		
		//First column - packageNo, description, scType, paymentTerms, paymentInfo
		Panel firstColumn = new Panel();
		firstColumn.setLayout(new FormLayout());
		firstColumn.setBorder(false);
		firstColumn.setPaddings(5);
	
		// Package No. 
		packageNoField = new NumberField("Package No.", "packageNo");
		packageNoField.setMaxLength(4);
		packageNoField.setMinLength(4);
		packageNoField.setWidth(50);
		packageNoField.setAllowDecimals(false);
		
		// Subcontract Nature
		subcontractorNatureField = new ComboBox("Subcontractor Nature", "subcontractorNature", 60);
		SimpleStore naturesStore = new SimpleStore("nature", new String[]{"DSC", "NDSC", "NSC"});
		subcontractorNatureField.setStore(naturesStore);
		subcontractorNatureField.setValueField("nature");
		subcontractorNatureField.setDisplayField("nature");
		subcontractorNatureField.setForceSelection(true);
		
		// description
		descriptionField = new TextArea("Description", "description");
		descriptionField.setWidth(170);
		

		/*------- Subcontract Type field set-------*/
		subcontractTypeFieldSet.setBorder(true);
		Panel subcontractTypePanel = new Panel();
		subcontractTypePanel.setLayout(new TableLayout(3));
		subcontractTypePanel.setBorder(false);
		
		labourCheckbox = new Checkbox("Labour");
		labourCheckbox.setCtCls("table-cell");
		labourCheckbox.setHideLabel(true);
		plantCheckbox = new Checkbox("Plant");
		plantCheckbox.setCtCls("table-cell");
		plantCheckbox.setHideLabel(true);
		materialCheckbox = new Checkbox("Material");
		materialCheckbox.setCtCls("table-cell");
		materialCheckbox.setHideLabel(true);
		
		subcontractTypePanel.add(labourCheckbox);
		subcontractTypePanel.add(plantCheckbox);
		subcontractTypePanel.add(materialCheckbox);
		
		subcontractTypeFieldSet.add(subcontractTypePanel);
		/*------- Subcontract Type field set-------*/
		
		/*------- Subcontract Term Field set-------*/
		Panel subcontractTermPanel = new Panel();
		subcontractTermPanel.setLayout(new TableLayout(2));
		subcontractTermPanel.setBorder(false);
		
		lumpSumRadio = new Radio("Lump Sum", "scTermOptions");
		lumpSumRadio.setHideLabel(true);
		lumpSumRadio.setCtCls("table-cell");
		lumpSumRadio.setChecked(true);
		remeasurementRadio = new Radio("Re-measurement", "scTermOptions");
		remeasurementRadio.setHideLabel(true);
		remeasurementRadio.setCtCls("table-cell");
		subcontractTermPanel.add(lumpSumRadio);
		subcontractTermPanel.add(remeasurementRadio);
		
		subcontractTermFieldSet.add(subcontractTermPanel);
		/*------- Subcontract Term Field set-------*/
		
		/*-----Form Of Subcontract Field Set-----*/
		Panel formOfSubcontractPanel = new Panel();
		formOfSubcontractPanel.setLayout(new TableLayout(3));
		formOfSubcontractPanel.setBorder(false);
		
		majorRadio = new Radio(SCPackage.MAJOR, "scFormOptions");
		majorRadio.setCtCls("table-cell");
		majorRadio.setHideLabel(true);
		if(scPackage.getSubcontractStatus() == null)
			majorRadio.setChecked(true);
		minorRadio = new Radio(SCPackage.MINOR, "scFormOptions");
		minorRadio.setCtCls("table-cell");
		minorRadio.setHideLabel(true);
		consultancyRadio = new Radio(SCPackage.CONSULTANCY_AGREEMENT, "scFormOptions");
		consultancyRadio.setCtCls("table-cell");
		consultancyRadio.setHideLabel(true);
		internalJobRadio = new Radio(SCPackage.INTERNAL_TRADING, "scFormOptions");
		internalJobRadio.setCtCls("table-cell");
		internalJobRadio.setHideLabel(true);
		Label internalJobLabel = new Label("Job : ");
		internalJobLabel.setCtCls("table-cell");
		internalJobField = new NumberField("Job for Internal Trading", "internalJob");
		internalJobField.setCtCls("table-cell");
		internalJobField.setWidth(60);
		internalJobField.setAllowDecimals(false);
		internalJobField.setDisabled(true);
		internalJobRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				internalJobField.setDisabled(!checked);
				retentionFieldSet.setDisabled(checked);
				if(!checked)
					internalJobField.setValue("");
			}
		});
		
		formOfSubcontractPanel.add(majorRadio);
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(minorRadio);
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(consultancyRadio);
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(new Label());
		formOfSubcontractPanel.add(internalJobRadio);
		formOfSubcontractPanel.add(internalJobLabel);
		formOfSubcontractPanel.add(internalJobField);
		
		formOfSubcontract.add(formOfSubcontractPanel);
		/*-----Form Of Subcontract Field Set-----*/
		

		
		firstColumn.add(packageNoField);
		firstColumn.add(subcontractorNatureField);
		firstColumn.add(descriptionField);

		firstColumn.add(subcontractTypeFieldSet);
		firstColumn.add(subcontractTermFieldSet);
		firstColumn.add(formOfSubcontract);

		/*----- END OF FIRST COLUMN -----*/
		
		//Second column -- retention, scTerm
		Panel secondColumn = new Panel();
		secondColumn.setLayout(new FormLayout());
		secondColumn.setBorder(false);
		secondColumn.setPaddings(5);
		
		/*------- Payment Information field set-------*/
		interimPaymentCheckbox = new Checkbox("Interim Payment Schedule");
		interimPaymentCheckbox.setHideLabel(true);
		mainContractRadio = new Radio("Main Contract", "paymentInfo");
		mainContractRadio.setChecked(true);
		mainContractRadio.setDisabled(true);
		subcontractRadio = new Radio("Subcontract", "paymentInfo");
		subcontractRadio.setDisabled(true);
		interimPaymentCheckbox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				mainContractRadio.setDisabled(!checked);
				subcontractRadio.setDisabled(!checked);
			}
		});
		paymentInformationFieldSet.add(interimPaymentCheckbox);
		paymentInformationFieldSet.add(mainContractRadio, new TableLayoutData(10));
		paymentInformationFieldSet.add(subcontractRadio, new TableLayoutData(10));
		/*------- Payment Information field set-------*/
		
		/*------- Payment Terms field set-------*/
		paymentTermsFieldSet = new FieldSet("Payment Terms");
		storeTerms.load();
		paymentTermsComboBox = new ComboBox("Terms", "paymentTermsCombo", 150);
		paymentTermsComboBox.setForceSelection(true);
		paymentTermsComboBox.setListWidth(250);
		paymentTermsComboBox.setStore(storeTerms);
		paymentTermsComboBox.setDisplayField("termsDisplay");
		paymentTermsComboBox.setValueField("termsValue");
		paymentTermsComboBox.setValue("QS2");
		
		paymentTermsDescriptionTextField = new TextField("Description");
		paymentTermsDescriptionTextField.setWidth(150);
		paymentTermsDescriptionTextField.setMaxLength(255);
		paymentTermsDescriptionTextField.setDisabled(paymentTermsComboBox.getValueAsString().equals("QS0")?true:false);
		
		currencyCodeListComboBox = new ComboBox("Currency");
		Store currencyStore = globalSectionController.getCurrencyCodeStore();
		currencyCodeListComboBox.setDisplayField("currencyDescription");
		currencyCodeListComboBox.setValueField("currencyCode");
		currencyCodeListComboBox.setSelectOnFocus(true);
		currencyCodeListComboBox.setForceSelection(true);
		currencyCodeListComboBox.setWidth(150);
		currencyStore.load();
		currencyCodeListComboBox.setStore(currencyStore);
		currencyCodeListComboBox.disable();
		
		exchangeRateTextField = new NumberField("Exchange Rate", "exchangeRate", 150);
		exchangeRateTextField.disable();
		
		paymentTermsFieldSet.add(paymentTermsComboBox);
		paymentTermsFieldSet.add(paymentTermsDescriptionTextField);
		paymentTermsFieldSet.add(currencyCodeListComboBox);
		paymentTermsFieldSet.add(exchangeRateTextField);
		/*------- Payment Terms field set-------*/
		
		/*------- Retention field set-------*/
		retentionFieldSet = new FieldSet("Retention");
		noRetentionRadio = new Radio("No Retention", "retentionOptions");
		noRetentionRadio.setHideLabel(true);
		noRetentionRadio.setChecked(false);
		lumpSumRetentionRadio = new Radio("Lump Sum Amount Retention", "retentionOptions");
		lumpSumRetentionRadio.setHideLabel(true);
		percentageRadio = new Radio("Percentage", "retentionOptions");
		percentageRadio.setHideLabel(true);
		percentageRadio.setChecked(true);
		originalSumRadio = new Radio("Original SC Sum", "percentageOptions");
		revisedSumRadio = new Radio("Revised SC Sum", "percentageOptions");
		revisedSumRadio.setChecked(true);
		originalSumRadio.setDisabled(true);
		revisedSumRadio.setDisabled(true);
		lumpSumField = new NumberField("Lump Sum Retention", "lumpSumRetention");
		lumpSumField.setDisabled(true);
		maxRetentionField = new NumberField("Maximum Retention %", "maxRetention");
		maxRetentionField.setDisabled(true);
		interimRetentionField = new NumberField("Interim Retention %", "interimRetention");
		interimRetentionField.setDisabled(true);
		mosRetentionField = new NumberField("MOS Retention %", "mosRetention");
		mosRetentionField.setDisabled(true);
		
		
		if(scPackage.getPackageNo()==null){
			percentageRadio.setChecked(true);
			revisedSumRadio.setChecked(true);
			lumpSumField.setDisabled(true);
		}else{
			if(scPackage.getRetentionTerms()!=null){
				if(SCPackage.RETENTION_LUMPSUM.equalsIgnoreCase(scPackage.getRetentionTerms()))
					lumpSumRetentionRadio.setChecked(true);
				else if(SCPackage.RETENTION_ORIGINAL.equalsIgnoreCase(scPackage.getRetentionTerms())){
					percentageRadio.setChecked(true);
					originalSumRadio.setChecked(true);
				}else if(SCPackage.RETENTION_REVISED.equalsIgnoreCase(scPackage.getRetentionTerms())){
					percentageRadio.setChecked(true);
					revisedSumRadio.setChecked(true);
				}
			}else{
				noRetentionRadio.setChecked(true);
				lumpSumRetentionRadio.setChecked(false);
				percentageRadio.setChecked(false);
			}
		}
		
		noRetentionRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){					
					percentageRadio.setChecked(false);
					lumpSumRetentionRadio.setChecked(false);
					
					originalSumRadio.setDisabled(true);
					revisedSumRadio.setDisabled(true);
					
					lumpSumField.setDisabled(true);
					maxRetentionField.setDisabled(true);
					interimRetentionField.setDisabled(true);
					mosRetentionField.setDisabled(true);
					
					//initialize
					lumpSumField.setValue(scPackage.getRetentionAmount()!=null?scPackage.getRetentionAmount():0.00);
					interimRetentionField.setValue(scPackage.getInterimRentionPercentage()!=null?scPackage.getInterimRentionPercentage():10.00);							
					mosRetentionField.setValue(scPackage.getMosRetentionPercentage()!=null?scPackage.getMosRetentionPercentage():10.00);
				}else{
					lumpSumField.setValue(0);
					interimRetentionField.setValue(0);
					mosRetentionField.setValue(0);
					
					lumpSumField.setDisabled(true);
					interimRetentionField.setDisabled(true);
					mosRetentionField.setDisabled(true);
				}
			}
		});
		
		lumpSumRetentionRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){					
					percentageRadio.setChecked(false);
					noRetentionRadio.setChecked(false);
					
					originalSumRadio.setDisabled(true);
					revisedSumRadio.setDisabled(true);
					
					lumpSumField.setDisabled(false);
					maxRetentionField.setDisabled(true);
					interimRetentionField.setDisabled(false);
					mosRetentionField.setDisabled(false);
					
					//initialize
					lumpSumField.setValue(scPackage.getRetentionAmount()!=null?scPackage.getRetentionAmount():0.00);
					interimRetentionField.setValue(scPackage.getInterimRentionPercentage()!=null?scPackage.getInterimRentionPercentage():10.00);							
					mosRetentionField.setValue(scPackage.getMosRetentionPercentage()!=null?scPackage.getMosRetentionPercentage():10.00);
				}else{
					lumpSumField.setValue(0);
					interimRetentionField.setValue(0);
					mosRetentionField.setValue(0);
					
					lumpSumField.setDisabled(true);
					interimRetentionField.setDisabled(true);
					mosRetentionField.setDisabled(true);
				}
			}
		});
		
		percentageRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					lumpSumRetentionRadio.setChecked(false);
					noRetentionRadio.setChecked(false);
					
					lumpSumField.setValue(0);
					
					lumpSumField.setDisabled(true);
					originalSumRadio.setDisabled(false);
					revisedSumRadio.setDisabled(false);
					
					maxRetentionField.setDisabled(false);
					interimRetentionField.setDisabled(false);
					mosRetentionField.setDisabled(false);
					
					//initialize
					maxRetentionField.setValue(scPackage.getMaxRetentionPercentage()!=null?scPackage.getMaxRetentionPercentage():5.00);
					interimRetentionField.setValue(scPackage.getInterimRentionPercentage()!=null?scPackage.getInterimRentionPercentage():10.00);
					mosRetentionField.setValue(scPackage.getMosRetentionPercentage()!=null?scPackage.getMosRetentionPercentage():10.00);
				}else{
					maxRetentionField.setValue(0);
					interimRetentionField.setValue(0);
					mosRetentionField.setValue(0);
					
					originalSumRadio.setDisabled(true);
					revisedSumRadio.setDisabled(true);
					maxRetentionField.setDisabled(true);
				}
				
				boolean lumpSumCheck = lumpSumRetentionRadio.getValue();
				lumpSumField.setDisabled(!lumpSumCheck);

				//Disable when neither "LumpSum" nor "Percentage" is on
				interimRetentionField.setDisabled(!checked & !lumpSumCheck);
				mosRetentionField.setDisabled(!checked & !lumpSumCheck);
			}
		});
		
		retentionFieldSet.add(noRetentionRadio);
		retentionFieldSet.add(lumpSumRetentionRadio);
		retentionFieldSet.add(percentageRadio);
		retentionFieldSet.add(originalSumRadio);
		retentionFieldSet.add(revisedSumRadio);
		retentionFieldSet.add(lumpSumField);
		retentionFieldSet.add(maxRetentionField);
		retentionFieldSet.add(interimRetentionField);
		retentionFieldSet.add(mosRetentionField);
		/*------- Retention field set-------*/
		
		secondColumn.add(retentionFieldSet);
		secondColumn.add(paymentInformationFieldSet);
		secondColumn.add(paymentTermsFieldSet);
		/*----- END OF SECOND COLUMN -----*/
		
		/**
		 * @author tikywong
		 * modified on 20131205
		 */
		//Third column -- CPF Calculation, Form of subcontract
		Panel thirdColumn = new Panel();
		thirdColumn.setLayout(new FormLayout());
		thirdColumn.setBorder(false);
		thirdColumn.setPaddings(5);
		
		/*-----cpf Calculation Field Set-----*/
		Panel cpfCalculationPanel = new Panel();
		cpfCalculationPanel.setLayout(new TableLayout(2));
		cpfCalculationPanel.setBorder(false);
		
		cpfSubjectRadio = new Radio(SCPackage.CPF_SUBJECT_TO, "cpfOptions");
		cpfSubjectRadio.setHideLabel(true);
		cpfSubjectRadio.setCtCls("table-cell");

		cpfNotSubjectRadio = new Radio(SCPackage.CPF_NOT_SUBJECT_TO, "cpfNotOptions");
		cpfNotSubjectRadio.setHideLabel(true);
		cpfNotSubjectRadio.setCtCls("table-cell");
		
		Label cpfBasePeriodLabel = new Label("	CPF Base Period : ");
		cpfBasePeriodLabel.setCtCls("table-cell-right-align");
		cpfBasePeriodField = new NumberField("CPF Base Period", "cpfBasePeriod");
		cpfBasePeriodField.setWidth(40);
		cpfBasePeriodField.setAllowDecimals(false);
		cpfBasePeriodField.setCtCls("table-cell");
		
		Label cpfBaseYearLabel = new Label("	CPF Base Year : ");
		cpfBaseYearLabel.setCtCls("table-cell-right-align");
		cpfBaseYearField = new NumberField("CPF Base Year", "cpfBaseYear");
		cpfBaseYearField.setWidth(40);
		cpfBaseYearField.setAllowDecimals(false);
		cpfBaseYearField.setCtCls("table-cell");
		
		//0 - not applicable, 1 - applicable
		String cpfApplicable = globalSectionController.getJob().getCpfApplicable();
		
		if(cpfApplicable.equals("1")){
			cpfSubjectRadio.setDisabled(false);
			cpfNotSubjectRadio.setDisabled(false);
			
			if(scPackage.getCpfCalculation()!=null && scPackage.getCpfCalculation().equals(SCPackage.CPF_SUBJECT_TO)){
				cpfSubjectRadio.setChecked(true);
				cpfNotSubjectRadio.setChecked(false);

				cpfBasePeriodField.setDisabled(false);
				cpfBaseYearField.setDisabled(false);
			}
			else{
				cpfSubjectRadio.setChecked(false);
				cpfNotSubjectRadio.setChecked(true);
				
				cpfBasePeriodField.setDisabled(true);
				cpfBaseYearField.setDisabled(true);
			}
		}
		else{
			cpfSubjectRadio.setDisabled(true);
			cpfNotSubjectRadio.setDisabled(true);
			cpfNotSubjectRadio.setChecked(true);
			
			cpfBasePeriodField.setDisabled(true);
			cpfBaseYearField.setDisabled(true);
		}
		
		cpfSubjectRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					cpfNotSubjectRadio.setChecked(false);
					
					if(scPackage.getCpfBasePeriod()!=null)
						cpfBasePeriodField.setValue(scPackage.getCpfBasePeriod());
					if(scPackage.getCpfBaseYear()!=null)
						cpfBaseYearField.setValue(scPackage.getCpfBaseYear());
					
					cpfBasePeriodField.setDisabled(false);
					cpfBaseYearField.setDisabled(false);
				}
			}
		});
		
		cpfNotSubjectRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					cpfSubjectRadio.setChecked(false);
					
					cpfBasePeriodField.setDisabled(true);
					cpfBasePeriodField.setValue("");
					cpfBaseYearField.setDisabled(true);	
					cpfBaseYearField.setValue("");
				}
			}
		});
		
		cpfCalculationPanel.add(cpfSubjectRadio);
		cpfCalculationPanel.add(new Label());
		cpfCalculationPanel.add(cpfBasePeriodLabel);
		cpfCalculationPanel.add(cpfBasePeriodField);
		cpfCalculationPanel.add(cpfBaseYearLabel);
		cpfCalculationPanel.add(cpfBaseYearField);
		cpfCalculationPanel.add(cpfNotSubjectRadio);
		
		cpfCalculationFieldSet.add(cpfCalculationPanel);
		/*-----cpf Calculation Field Set-----*/
		
		
		
		/*-----Work Scope Field Set-----*/
		//		workScopeLabel
		workScopeTextField = new TextField("Work Scope","workScope");
		workScopeTextField.setWidth(40);
		workScopeTextField.setMaxLength(3);
		workScopeTextField.setValue("");
		
		if(scPackage != null)
			obtainSCWorkScopeList();
			if(scWorkScopeList != null && scWorkScopeList.size() >= 1)
				if(scWorkScopeList.get(0) != null)
					if(scWorkScopeList.get(0).getWorkScope() != null)
						workScopeTextField.setValue(scWorkScopeList.get(0).getWorkScope());
		
		Button workScopeSearchButton = new Button("Search");
		workScopeSearchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				WorkScopeListWindow curWindow = new WorkScopeListWindow(thisWindow);
				childWindow = curWindow;
				childWindow.show();
			};
		});
		workScopeSearchButton.setCtCls("table-cell-right-align");
		//Work Scope
		workScopeLabel.setCtCls("table-cell");
		workScopeLabel.setText("Work Scope:");
		workScopePanel.setLayout(new TableLayout(4));
		workScopePanel.setBorder(false);
		workScopePanel.add(workScopeLabel);
		workScopePanel.add(workScopeTextField);
		workScopePanel.add(workScopeSearchButton);
		
		workScopeDescription.setCtCls("table-cell");
		workScopeDescription.setText(" ");

		workScopeFieldSet.add(workScopePanel);
		workScopeFieldSet.add(workScopeDescription);
		
		//ApprovalRoute
		Panel approvalRoutePanel = new Panel();
		approvalRoutePanel.setLayout(new TableLayout(4));
		approvalRoutePanel.setBorder(false);
		
		approvalRouteLabel.setCtCls("table-cell");	
		approvalRouteLabel.setText("Approval Sub-type:");
		
		approvalRouteField = new TextField("Approval Route", "approvalRoute");
		approvalRouteField.setWidth(50);	
		
		approvalRoutePanel.add(approvalRouteLabel);
		approvalRoutePanel.add(approvalRouteField);
		approvalRoute.add(approvalRoutePanel);
		/*-----Work Scope Field Set-----*/
		
		thirdColumn.add(cpfCalculationFieldSet);
		thirdColumn.add(workScopeFieldSet);
		thirdColumn.add(approvalRoute);
		/*----- END OF THIRD COLUMN -----*/
				
		//Add columns to panel, panel to window
		columnsPanel.add(firstColumn, new ColumnLayoutData(0.33));
		columnsPanel.add(secondColumn, new ColumnLayoutData(0.34));
		columnsPanel.add(thirdColumn, new ColumnLayoutData(0.33));
		mainPanel.add(columnsPanel);
		this.add(mainPanel);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					removePendingPaymentCertBeforeSave();
				};
		});
		saveButton.setVisible(false);
		// Check for access rights - then add toolbar buttons accordingly
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010403_ADD_REVISIE_IPA_CERT, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					try{
						accessRightsList = accessRightsReturned;
						securitySetup();
					}catch(Exception e){
						UIUtil.alert(e);
					}
				}
				
				public void onFailure(Throwable e) {
					UIUtil.alert(e.getMessage());
				}
			});
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		this.addButton(saveButton);
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalSectionController.closeCurrentWindow();				
				};
		});		
		this.addButton(closeButton);
		
		populateForm();
		doLayout();
	}
	
	private void populateForm(){
		//Package No.  if package already has a packageNo, disable the field
		String packageNo = scPackage.getPackageNo();
		if(packageNo != null && packageNo.trim().length() > 0){
			packageNoField.setValue(packageNo);
			packageNoField.setDisabled(true);
			String nature = scPackage.getSubcontractorNature();
			if(nature == null || nature.trim().length() == 0){
				if(packageNo.startsWith("1"))
					nature = "DSC";
				else if(packageNo.startsWith("2"))
					nature = "NDSC";
				else if(packageNo.startsWith("3"))
					nature = "NSC";
			}
			subcontractorNatureField.setValue(nature);
			subcontractorNatureField.setDisabled(true);
		}

		currencyCodeListComboBox.setValue(scPackage.getPaymentCurrency());
		exchangeRateTextField.setValue(scPackage.getExchangeRate().toString());
		descriptionField.setValue(scPackage.getDescription());
		paymentTermsDescriptionTextField.setValue(scPackage.getPaymentTermsDescription());

		if(scPackage.getPackageNo()==null && "14".equals(globalSectionController.getJob().getJobNumber().substring(0, 2)))
			paymentTermsComboBox.setValue("QS3");
		else
			paymentTermsComboBox.setValue(scPackage.getPaymentTerms());
		//Payment Information. Example: Interim Payment Schedule (Main Contract)
		String paymentInfo = scPackage.getPaymentInformation();
		if(paymentInfo != null && paymentInfo.startsWith("Interim")){
			interimPaymentCheckbox.setChecked(true);
			if(paymentInfo.endsWith("Subcontract)"))
				subcontractRadio.setChecked(true);
		}
		//Subcontract Type
		if(Boolean.TRUE.equals(scPackage.getLabourIncludedContract()))
			labourCheckbox.setChecked(true);
		if(Boolean.TRUE.equals(scPackage.getPlantIncludedContract()))
			plantCheckbox.setChecked(true);
		if(Boolean.TRUE.equals(scPackage.getMaterialIncludedContract()))
			materialCheckbox.setChecked(true);
		//Subcontract Term
		if(("Re-measurement").equals(scPackage.getSubcontractTerm()))
			remeasurementRadio.setChecked(true);
		//CPF Calculation
		if((SCPackage.CPF_SUBJECT_TO).equals(scPackage.getCpfCalculation())){
			cpfSubjectRadio.setChecked(true);
			cpfBasePeriodField.setValue(scPackage.getCpfBasePeriod());
			cpfBaseYearField.setValue(scPackage.getCpfBaseYear());
		}
		
		boolean setRetention = true;
		
		//Form of Subcontract
		String scForm = scPackage.getFormOfSubcontract();
		if(scForm == null)
			scForm = SCPackage.MAJOR; //Default
		if(scForm.equals(SCPackage.MAJOR))
			majorRadio.setChecked(true);
		else if(scForm.equals(SCPackage.MINOR))
			minorRadio.setChecked(true);
		else if(scForm.equals(SCPackage.CONSULTANCY_AGREEMENT))
			consultancyRadio.setChecked(true);
		else{
			internalJobRadio.setChecked(true);
			internalJobField.setValue(scPackage.getInternalJobNo());
			//If internal, skip retention
			setRetention = false;
//			return; //If internal, skip retention
		}
		
		if(setRetention){
			//Retention. Example: Percentage - Original SC Sum
			String retentionTerms = scPackage.getRetentionTerms();
			if(scPackage.getPackageNo()!=null){
				if(retentionTerms != null){
					if(retentionTerms.startsWith("Percentage")){
						percentageRadio.setChecked(true);
						if(retentionTerms.endsWith("Revised SC Sum"))
							revisedSumRadio.setChecked(true);
					}
					else if(retentionTerms.startsWith("Lump")){
						lumpSumRetentionRadio.setChecked(true);
						lumpSumField.setValue(scPackage.getRetentionAmount()!=null?scPackage.getRetentionAmount():0.00);
					}
				}else{
					noRetentionRadio.setChecked(true);
				}
			}
		
			lumpSumField.setValue(scPackage.getRetentionAmount()!=null?scPackage.getRetentionAmount():0.00);
			maxRetentionField.setValue(scPackage.getMaxRetentionPercentage()!=null?scPackage.getMaxRetentionPercentage():0.00);
			interimRetentionField.setValue(scPackage.getInterimRentionPercentage()!=null?scPackage.getInterimRentionPercentage():0.00);
			mosRetentionField.setValue(scPackage.getMosRetentionPercentage()!=null?scPackage.getMosRetentionPercentage():0.00);
		}
		
		//Work Scope
		obtainSCWorkScopeList();
		if(scWorkScopeList != null && scWorkScopeList.size() > 0){
			workScopeTextField.setValue(scWorkScopeList.get(0).getWorkScope());
			try {
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().obtainWorkScope(scWorkScopeList.get(0).getWorkScope().trim(), new AsyncCallback<UDC>(){

					public void onFailure(Throwable msg) {
						UIUtil.alert(msg);
					}

					public void onSuccess(UDC udc) {
						workScopeDescription.setText(udc.getDescription());
					}
					
				});
			} catch (Exception e) {
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
		}
		approvalRouteField.setValue(scPackage.getApprovalRoute());
	}
	
	private void removePendingPaymentCertBeforeSave(){
		if(scPackage.getId()!=null){
			UIUtil.maskPanelById(globalSectionController.getCurrentWindow().getId(), GlobalParameter.SAVING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getPaymentRepository().obtainPaymentLatestCert(globalSectionController.getJob().getJobNumber(), packageNoField.getText().trim(), new AsyncCallback<SCPaymentCert>() {
				public void onSuccess(final SCPaymentCert paymentCert) {
					if(paymentCert!=null && paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
						MessageBox.confirm("Payment Requisition", "Payment Requisition with status 'Pending' will be deleted. Proceed?",
								new MessageBox.ConfirmCallback(){
							public void execute(String btnID) {
								if(btnID.equals("yes")){
									SessionTimeoutCheck.renewSessionTimer();
									globalSectionController.getPaymentRepository().deletePaymentCert(paymentCert, new AsyncCallback<Boolean>() {
										public void onSuccess(Boolean result) {
											obtainSCPackgeBeforeSave();
										}
										public void onFailure(Throwable e) {
											UIUtil.throwException(e);
										}
									});
								}
								UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
							}
						});
					}
					else
						obtainSCPackgeBeforeSave();
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
					UIUtil.throwException(e);
				}
			});
		}else
			obtainSCPackgeBeforeSave();
	}
	
	private void obtainSCPackgeBeforeSave(){
		UIUtil.maskPanelById(globalSectionController.getCurrentWindow().getId(), GlobalParameter.SAVING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().obtainSCPackage(globalSectionController.getJob(), packageNoField.getText().trim(), new AsyncCallback<SCPackage>() {
			public void onSuccess(SCPackage scPackageInDB) {
				if(scPackageInDB!=null)
					scPackage=scPackageInDB;
				if(scPackage.isAwarded() || (scPackage.getSubcontractStatus()!=null && scPackage.getSubcontractStatus()==330)){
					MessageBox.alert("Package has been awarded or submitted.");
					UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
					return;
				}
				saveSCPackage();
				UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
				UIUtil.throwException(e);
			}
		});
	}
	
	private void saveSCPackage(){
		String packageNo = packageNoField.getValueAsString();
		if(packageNo == null || packageNo.trim().length() == 0){
			MessageBox.alert("Package number must not be blank");
			return;
		}else if(packageNo.trim().length() != 4){
			MessageBox.alert("Package number must be 4 digits.");
			return;
		}
		String scNature = subcontractorNatureField.getValue();
		if(scNature == null){
			MessageBox.alert("Subcontractor Nature must not be blank");
			return;
		}
		if(packageNo.startsWith("1")){
			if(!"DSC".equals(scNature)){
				MessageBox.alert("Package numbers beginning with '1' are reserved for 'DSC' subcontracts");
				return;
			}
		}
		else if(packageNo.startsWith("2")){
			if(!"NDSC".equals(scNature)){
				MessageBox.alert("Package numbers beginning with '2' are reserved for 'NDSC' subcontracts");
				return;
			}
		}
		else if(packageNo.startsWith("3")){
			if(!"NSC".equals(scNature)){
				MessageBox.alert("Package numbers beginning with '3' are reserved for 'NSC' subcontracts");
				return;
			}
		}
		else{
			MessageBox.alert("Package number must begin with 1, 2 or 3");
			return;
		}
		scPackage.setPackageNo(packageNo.trim());
		scPackage.setSubcontractorNature(scNature);
		//scPackage.setPaymentCurrency(currencyCodeListComboBox.getValue() != null ? currencyCodeListComboBox.getValue() : "");
		//scPackage.setExchangeRate(Double.parseDouble(exchangeRateTextField.getText() != null ? exchangeRateTextField.getText() : "0.0"));
		String description = descriptionField.getValueAsString();
		if(description == null || description.trim().length() == 0){
			MessageBox.alert("Description must not be blank");
			return;
		}
		scPackage.setDescription(description.trim());
		if(labourCheckbox.getValue()==false && plantCheckbox.getValue()==false && materialCheckbox.getValue()==false){
			MessageBox.alert("No subcontract type is selected.");
			return;
		}
		String paymentTerms = this.paymentTermsComboBox.getValueAsString().trim();
		if(paymentTerms == null || paymentTerms.trim().length() == 0){
			MessageBox.alert("Payment terms must not be blank");
			return;
		}
		else{
			boolean valid = false;
			for(String validPT : validPaymentTerms){
				if(validPT.equals(paymentTerms.trim())){
					valid = true;
					break;
				}
			}
			if(!valid){
				MessageBox.alert("Invalid payment terms");
				return;	
			}
		}
		scPackage.setPaymentTerms(paymentTerms);

		String paymentTermsDescription = paymentTermsDescriptionTextField.getValueAsString();
		scPackage.setPaymentTermsDescription(paymentTermsDescription);
		if ("QS0".equals(paymentTerms) && (paymentTermsDescription == null || paymentTermsDescription.trim().isEmpty())) {
			MessageBox.alert("Payment Terms Description is compulsory for QS0");
			return;
		}

		String paymentInfo = null;
		if(interimPaymentCheckbox.getValue()){
			paymentInfo = "Interim Payment Schedule";
			if(mainContractRadio.getValue())
				paymentInfo += " (Main Contract)";
			else
				paymentInfo += " (Subcontract)";
		}
		scPackage.setPaymentInformation(paymentInfo);
		
		scPackage.setLabourIncludedContract(labourCheckbox.getValue());
		scPackage.setPlantIncludedContract(plantCheckbox.getValue());
		scPackage.setMaterialIncludedContract(materialCheckbox.getValue());
		
		String scTerm = "Lump Sum";
		if(remeasurementRadio.getValue())
			scTerm = "Re-measurement";
		scPackage.setSubcontractTerm(scTerm);
		
		if(cpfSubjectRadio.getValue()){
			if(cpfBasePeriodField.getValueAsString().trim().length()==0 
				|| cpfBasePeriodField.getValue().intValue()>12 
				|| cpfBasePeriodField.getValue().intValue()<1){
				MessageBox.alert("Please input 1 - 12 for CPF Base Period.");
				return;
			}
			if(cpfBaseYearField.getValueAsString().trim().length()!=4){
				MessageBox.alert("Please input 4 digits for CPF Base Year.");
				return;
			}
				
			scPackage.setCpfCalculation(SCPackage.CPF_SUBJECT_TO);
			scPackage.setCpfBasePeriod(cpfBasePeriodField.getValue().intValue());
			scPackage.setCpfBaseYear(cpfBaseYearField.getValue().intValue());
		}
		else{
			scPackage.setCpfCalculation(SCPackage.CPF_NOT_SUBJECT_TO);
			scPackage.setCpfBasePeriod(null);
			scPackage.setCpfBaseYear(null);
		}
		
		scPackage.setInternalJobNo(null);		
		if(majorRadio.getValue())
			scPackage.setFormOfSubcontract(SCPackage.MAJOR);
		else if(minorRadio.getValue())
			scPackage.setFormOfSubcontract(SCPackage.MINOR);
		else if(consultancyRadio.getValue())
			scPackage.setFormOfSubcontract(SCPackage.CONSULTANCY_AGREEMENT);
		else{
			scPackage.setFormOfSubcontract(SCPackage.INTERNAL_TRADING);
			if(internalJobField.getValue() == null || internalJobField.getValueAsString().trim().length() == 0){
				MessageBox.alert("Internal Job No. must not be blank.");
				return;
			}
			else{
				if (internalJobField.getValueAsString().length()>12){
					MessageBox.alert("The Job No. is too large.");
					return;
				}
				scPackage.setInternalJobNo(internalJobField.getValueAsString().trim());
			}
		}

		if(retentionFieldSet.isDisabled()){
			scPackage.setRetentionTerms(null);
			scPackage.setRetentionAmount(null);
			scPackage.setMaxRetentionPercentage(null);
			scPackage.setInterimRentionPercentage(null);
			scPackage.setMosRetentionPercentage(null);
		}
		else{
			if(lumpSumRetentionRadio.getValue())
				scPackage.setRetentionTerms("Lump Sum Amount Retention");
			else if(percentageRadio.getValue()){
				String retTerms = "Percentage";
				if(originalSumRadio.getValue())
					retTerms += " - Original SC Sum";
				else
					retTerms += " - Revised SC Sum";
				scPackage.setRetentionTerms(retTerms);
			}
			else
				scPackage.setRetentionTerms(null);
			
			//Lum Sum
			if(lumpSumField.isDisabled())
				scPackage.setRetentionAmount(null);
			else if(lumpSumField.getValue() != null && lumpSumField.getValue().doubleValue()>0)
				scPackage.setRetentionAmount(lumpSumField.getValue().doubleValue());
			else{
				MessageBox.alert("Lump Sum Amount cannot be zero");
				return;
			}
			
			//Max Retention
			if(maxRetentionField.isDisabled())
				scPackage.setMaxRetentionPercentage(null);
			else{
				if(maxRetentionField.getValue() == null || maxRetentionField.getValueAsString().trim().equals("")){
					MessageBox.alert("Max Retention cannot be zero.");
					return;
				}
				else
					scPackage.setMaxRetentionPercentage(maxRetentionField.getValue().doubleValue());
			}
			
			//Interim Retention
			if(interimRetentionField.isDisabled())
				scPackage.setInterimRentionPercentage(null);
			else{
				if(interimRetentionField.getValue() == null || interimRetentionField.getValueAsString().trim().equals("")){
					MessageBox.alert("Interim Retention cannot be zero.");
					return;
				}
				else
					scPackage.setInterimRentionPercentage(interimRetentionField.getValue().doubleValue());
			}
			
			//MOS Retention
			if(mosRetentionField.isDisabled())
				scPackage.setMosRetentionPercentage(null);
			else{
				if(mosRetentionField.getValue() == null || mosRetentionField.getValueAsString().trim().equals("")){
					MessageBox.alert("MOS Retention cannot be zero.");
					return;
				}
				else
					scPackage.setMosRetentionPercentage(mosRetentionField.getValue().doubleValue());
			}
		}

		//Approval Route
		String approvalRoute = approvalRouteField.getValueAsString().trim();
		if(approvalRoute.equals(null) || approvalRoute.length() == 0)
			approvalRoute = "";
		else if(approvalRoute.length() > 5){
			MessageBox.alert("Approval Route has a maximum of 5 characters.");
			return;
		}
		else{ //[0-9] 48-57, [A-Z] 65-90, [a-z] 97-122
			for(int x=0; x<approvalRoute.length(); x++){ 
				if(approvalRoute.charAt(x)<48 ||(approvalRoute.charAt(x)>57 && approvalRoute.charAt(x)<65) || (approvalRoute.charAt(x)>90&&approvalRoute.charAt(x)<97) || approvalRoute.charAt(x)>122){
					MessageBox.alert("Approval Route has to be in A-Z, a-z or numbers.");
					return;
				}
			}
		}
		scPackage.setApprovalRoute(approvalRoute);
		
		//Work Scope
		if(workScopeTextField.getValueAsString() != null && workScopeTextField.getValueAsString().trim().length() > 0){
			if(workScopeTextField.getValueAsString().trim().length() != 3){
				MessageBox.alert("Invalid work scope");
				return;
			}
			if(workScopeTextField.getValueAsString().equalsIgnoreCase(workScopeSelectedFromList)){
				updateWorkScope();
				savePackageAfterValidation();
			}
			else{
				try {
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPackageRepository().obtainWorkScope(workScopeTextField.getValueAsString(), new AsyncCallback<UDC>(){

						public void onFailure(Throwable msg) {
							UIUtil.alert(msg);
						}

						public void onSuccess(UDC udc) {
							if(udc==null){
								MessageBox.alert("Work Scope "+workScopeTextField.getValueAsString()+" does not exist.");
							}
							else{
								updateWorkScope();
								savePackageAfterValidation();
							}
						}		
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else{
			obtainSCWorkScopeList();
			if(scWorkScopeList != null)
				deleteWorkScope(scPackage);
			savePackageAfterValidation();
		}
	}
	
	public void deleteWorkScope(SCPackage scPackage){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Loading...", true);
		try {
			globalSectionController.getScWorkScopeRepository().obtainSCWorkScopeListBySCPackage(scPackage, new AsyncCallback<List<SCWorkScope>>(){

				@Override
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					UIUtil.throwException(e);
				}

				@Override
				public void onSuccess(List<SCWorkScope> result) {
					scWorkScopeList = result;
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				}
				
			});
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
	}
	
	public void updateWorkScope(){
		String currentWorkScope = null;
		obtainSCWorkScopeList();
		if(scWorkScopeList != null && scWorkScopeList.size() == 1)
			currentWorkScope = scWorkScopeList.get(0).getWorkScope();
		
		if(currentWorkScope == null || !currentWorkScope.equals(workScopeTextField.getValueAsString().trim())){
			SCWorkScope workScope = new SCWorkScope();
			workScope.setWorkScope(workScopeTextField.getValueAsString().trim());
			workScope.setScPackage(scPackage);
		}
	}
	
	public void closeCurrentWindow() {
		if(childWindow != null){
			childWindow.close();
			childWindow = null;
		}
	}
	
	public void setSelectedWorkScope(String workScope, String description){
		workScopeTextField.setValue(workScope);
		workScopeDescription.setText(description);
		workScopeSelectedFromList = workScope;
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void setGlobalSectionController(
			GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}
	
	private void securitySetup(){
		if(accessRightsList.contains("WRITE")){
			saveButton.setVisible(true);
		}
	}
	
	private void savePackageAfterValidation(){
		if(scPackage.getSubcontractStatus() == null){
			scPackage.setSubcontractStatus(Integer.valueOf(100));
			scPackage.setScCreatedDate(new Date());
		}
		
		saveOrUpdateSCPackage(scPackage);
	}
	
	private void saveOrUpdateSCPackage(final SCPackage scPackage){
		if(globalSectionController.getCurrentWindow() != null){
			UIUtil.maskPanelById(globalSectionController.getCurrentWindow().getId(), GlobalParameter.SAVING_MSG, true);
			scPackage.setJob(globalSectionController.getJob());
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getPackageRepository().saveOrUpdateSCPackage(scPackage, new AsyncCallback<String>(){
				public void onSuccess(String error) {
					UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
					if(error != null)
						MessageBox.alert(error);
					else{
						globalSectionController.closeCurrentWindow();
						globalSectionController.refreshUnawardedPackageStore();
						globalSectionController.refreshAwardedPackageStore();
						globalSectionController.refreshUneditablePackageNos();
						globalSectionController.getTreeSectionController().refreshPackageTreePanel();
						globalSectionController.populateSCPackageMainPanelandDetailPanel(scPackage.getPackageNo());
					}
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
					UIUtil.throwException(e);
				}
			});
		}
	}
	
	private void obtainSCWorkScopeList() {
		scWorkScopeList = null;
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Loading...", true);
		try {
			globalSectionController.getScWorkScopeRepository().obtainSCWorkScopeListBySCPackage(scPackage, new AsyncCallback<List<SCWorkScope>>(){

				@Override
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					UIUtil.throwException(e);
				}

				@Override
				public void onSuccess(List<SCWorkScope> result) {
					scWorkScopeList = result;
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				}
				
			});
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
	}
}
