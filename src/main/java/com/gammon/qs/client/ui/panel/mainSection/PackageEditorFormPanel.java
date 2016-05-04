package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;
import java.util.Map;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.client.repository.MasterListRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCWorkScope;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

/**
 * @author matthewatc
 * 12:11:28 9 Jan 2012 (UTC+8)
 * Panel to display a form for the viewing of information about an scPackage. Code mostly cannibalized from
 * PackageEditorGridPanel (which this class is intended to replace) and EditPackageHeaderWindow.
 * 
 * re-designed by irischau
 * 09 May 2014
 */
public class PackageEditorFormPanel extends Panel {
	private ToolbarButton openSCAttachmentButton;
	private ToolbarButton openSplitWindowButton;
	private ToolbarButton openTerminateWindowButton;
	private ToolbarButton editButton;
	private ToolbarButton inputTAButton;
	private ToolbarButton inputRateButton;
	private ToolbarButton refreshButton;
	private ToolbarButton editDatesButton;
	private ToolbarButton recalculateSCPackageFiguresButton;
	/* added button to invoke TenderAnalysisEnquiryWindow - matthewatc 3/2/12 */
	private ToolbarButton viewTenderDetailsButton;
	private ToolbarButton tipsButton;
	
	private Store dataStore;
	
	private final RecordDef panelRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("field"),
					new StringFieldDef("value"),
					new StringFieldDef("description")
			}
	);

	public static final String PACKAGE_FORM_PANEL = "package-editor-form";
	public static final String PACKAGE_MAIN_PANEL = "package-editor-main-panel";
	private String paymentStatus = null;
	private String submittedAddendum = null;
	private String splitTerminateStatus = null;
	private GlobalSectionController globalSectionController;
	private String legacyJobFlag;
	private String allowManualInputSCWorkdone;
	private String paymentRequestStatus;
	private NumberField vendorNoField;
	private String jobNumber;
	private String packageNo;
	private SCPackage scPackage;
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private GlobalMessageTicket globalMessageTicket;

	// First column fields
	private NumberField packageNoField;
	
	private TextArea descriptionField;

	private Label scStatusValueLabel = new Label();
	private Checkbox labourCheckbox;
	private Checkbox plantCheckbox;
	private Checkbox materialCheckbox;
	private Label scPaymentStatusDesccriptionLabel;
	private Label scPaymentSubmittedDesccriptionLabel;
	private Label addendumSubmittedDescriptionLabel;
	
	private Label originalSCSumField;
	private Label remeasuredSCSumField;
	private Label approvedSCSumField;
	private Label revisedSCSumField;

	
	private final Store storeTerms = GlobalParameter.SC_PAYMENT_TERMS_STORE;
	private TextArea paymentTermsDescriptionField;

	// Second column fields
	
	private Label paymentTermsField;
	private Label currencyCodeTextField;
	private Label exchangeRateTextField;
	private Label splitTerminatedStatusDescriptionLabel;
	
	private FieldSet retention;
/*	private Checkbox noRetentionCheckbox;
	private Checkbox lumpSumCheckbox;
	private Checkbox percentageCheckbox;*/
	private Radio noRetentionRadio;
	private Radio lumpSumRetentionRadio;
	private Radio percentageRadio;
	
	private Radio originalSumRadio;
	private Radio revisedSumRadio;
	private Label maxRetentionField;
	private Label interimRetentionField;
	private Label mosRetentionField;
	private Radio lumpSumRadio;
	private Radio remeasurementRadio;
	
	private Label retentionAmountField;
	private Label accumulatedRetentionField;
	private Label releasedRetentionField;
	private Label retentionBalanceField;

	// Third column fields
	private Radio cpfSubjectRadio;
	private Radio cpfNotSubjectRadio;
	private NumberField cpfBasePeriodField;
	private NumberField cpfBaseYearField;
	private Radio majorRadio;
	private Radio minorRadio;
	private Radio consultancyRadio;
	private Radio internalJobRadio;
	private NumberField internalJobField;
	private Label subcontractorNatureField;
	private Label subcontractorNumberField;
	private Label subcontractorLabel;
	private Label workScopeField;
	private Label workScopeDescription = new Label();
	private TextField approvalRouteField;
	private Label approvalRouteLabel = new Label();
	
	private boolean storePopulated;

	private FieldSet approvalRoute = new FieldSet(
			"Alternative Approval Sub-type");
	//private ToolbarButton paymentbeforeAward;
	private List<SCWorkScope> scWorkScopeList;

	public PackageEditorFormPanel(GlobalSectionController globalSectionController, SCPackage scPackage, SCPaymentCert latestPaymentCert) {
		this.globalSectionController = globalSectionController;
		this.scPackage = scPackage;
		this.setId(PACKAGE_FORM_PANEL);
		this.setAutoScroll(true);
		this.globalSectionController.cacheSCStatusCodeDescriptions();

		globalMessageTicket = new GlobalMessageTicket();
		jobNumber = globalSectionController.getJob().getJobNumber();
		packageNo = scPackage.getPackageNo();
		
		if(latestPaymentCert!=null){
			paymentRequestStatus = latestPaymentCert.getPaymentStatus();
			if(paymentRequestStatus != null)
				paymentRequestStatus=paymentRequestStatus.trim();
			else
				paymentRequestStatus="";
		}else{
			paymentRequestStatus="";
		}
		
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		
		this.setScPackage(scPackage);
		
		addToolbar();
		addForm();
		storePopulated = false;
	}
	

	/**
	 * repopulates the panel with a new package
	 * @param scPackage - the new package
	 */
	public void populate(SCPackage scPackage) {
		this.scPackage = scPackage;
		packageNo = scPackage.getPackageNo();
		addForm();
		//getStore();
	}
	

	public SCPackage getScPackage() {
		return scPackage;
	}

	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}
	
	private void setUpStore() {
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(panelRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();		
		
		this.dataStore.removeAll();
		if (scPackage.isAwarded()){
			this.paymentStatus= scPackage.getPaymentStatus();
			this.submittedAddendum =scPackage.getSubmittedAddendum().trim();
			this.splitTerminateStatus = scPackage.getSplitTerminateStatus();
			/*this.paymentRequestStatus="";
			
			List<SCPaymentCert> paymentCertList = scPackage.getScPaymentCertList(); 
			
			if (paymentCertList!=null && !paymentCertList.isEmpty())
				for (int i =0; i<paymentCertList.size()&&"".equals(this.paymentRequestStatus);i++)
					if (!"APR".equals(paymentCertList.get(i).getPaymentStatus()!=null?paymentCertList.get(i).getPaymentStatus().trim():""))
						this.paymentRequestStatus = paymentCertList.get(i).getPaymentStatus().trim();*/
				
		}else{
			this.paymentStatus= "";
			this.submittedAddendum = "";
			//this.paymentRequestStatus="";
			this.splitTerminateStatus="";
		}
		this.allowManualInputSCWorkdone = scPackage.getJob().getAllowManualInputSCWorkDone();
		this.legacyJobFlag = scPackage.getJob().getLegacyJob();
	

		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Package No",
				scPackage.getPackageNo(),
				((scPackage.isAwarded() && "1".equals(scPackage.getSubmittedAddendum()))?"Submitted Addendum Approval":"")
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Description",
				scPackage.getDescription(),
				(scPackage.isAwarded() &&(paymentRequestStatus.equals("SBM")||paymentRequestStatus.equals("PCS") || paymentRequestStatus.equals("UFR"))?"Submitted Payment Approval":"")
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Package Type",
				scPackage.getPackageType(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Subcontract Status",
				scPackage.getSubcontractStatus()!=null?scPackage.getSubcontractStatus():"",
				""
		}));
		if (scPackage.isAwarded()){
			String vendorNumName = scPackage.getVendorNo();
			if(vendorNumName != null){
				//Vendor name is appended to the vendorNo in the service level
				int ind = vendorNumName.indexOf('-');
				dataStore.add(panelRecordDef.createRecord(new Object[]{
						"Vendor No",
						vendorNumName.substring(0, ind),
						vendorNumName.substring(ind + 1)
				}));
			}
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Subcontractor Nature",
					scPackage.getSubcontractorNature(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Original Subcontract Sum",
					scPackage.getOriginalSubcontractSum(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Remeasured Subcontract Sum",
					scPackage.getRemeasuredSubcontractSum(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Approved VO Amount",
					scPackage.getApprovedVOAmount(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Revised Subcontract Sum",
					scPackage.getSubcontractSum(),
					""
			}));
		}
		else{
			//Show original budget for package.
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Original Budget for Package",
					scPackage.getOriginalSubcontractSum(), //Store budget in originalSubcontractSum for non-awarded packages
					""
			}));
		}
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Retention Terms",
				scPackage.getRetentionTerms(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Max Retention %",
				scPackage.getMaxRetentionPercentage(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Interim Retention %",
				scPackage.getInterimRentionPercentage(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"MOS Retention %",
				scPackage.getMosRetentionPercentage(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Retention Amount",
				scPackage.getRetentionAmount(),
				""
		}));
		if(scPackage.isAwarded()){
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Accumlated Retention",
					scPackage.getAccumlatedRetention(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Retention Released",
					scPackage.getRetentionReleased(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Retention Balance",
					(scPackage.getAccumlatedRetention() + scPackage.getRetentionReleased()),
					""
			}));
		}
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Payment Information",
				scPackage.getPaymentInformation(),
				""
		}));
		if(scPackage.isAwarded()){
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Payment Currency",
					scPackage.getPaymentCurrency(),
					""
			}));
			dataStore.add(panelRecordDef.createRecord(new Object[]{
					"Exchange Rate",
					scPackage.getExchangeRate(),
					""
			}));
		}
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Payment Terms",
				scPackage.getPaymentTerms(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Subcontract Type",
				scPackage.getSubcontractType(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Subcontract Term",
				scPackage.getSubcontractTerm(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"CPF Calculation",
				scPackage.getCpfCalculation(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"CPF Base Period",
				scPackage.getCpfBasePeriod(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"CPF Base Year",
				scPackage.getCpfBaseYear(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Form of Subcontract",
				scPackage.getFormOfSubcontract(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Internal Job No.",
				scPackage.getInternalJobNo(),
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Split/Terminate Status",
				scPackage.getSplitTerminateStatus(),
				""
		}));		
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Payment Status",
				scPackage.getPaymentStatus(),
				""
		}));	
		obtainSCWorkScopeList();

		String workScope = scWorkScopeList == null ? 
				"" : scWorkScopeList.size() == 0 ? 
						"" : scWorkScopeList.get(0).getWorkScope();
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Work Scope",
				workScope,
				""
		}));
		dataStore.add(panelRecordDef.createRecord(new Object[]{
				"Approval Route",
				scPackage.getApprovalRoute(),
				""
		}));
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
	
	/**
	 * This is here for backwards-compatibility with several classes that used to call getStore() on the old PackageEditorGridPanel.
	 * The Store is only populated if this is called, and is then cached for subsequent calls.
	 */
	public Store getStore() {
		if(!storePopulated) {
			setUpStore();
			storePopulated = true;
		}
		return dataStore;
	}

	private void addForm() {
		FormPanel mainPanel = new FormPanel();
		mainPanel.setId(PACKAGE_MAIN_PANEL);
		mainPanel.setBorder(false);
		mainPanel.setWidth(1080);
//		mainPanel.setFrame(true);
		
		Panel columnsPanel = new Panel();
		columnsPanel.setBorder(false);
		columnsPanel.setLayout(new ColumnLayout());

		Panel firstColumnPanel = new Panel();
		firstColumnPanel.setLayout(new RowLayout());
		firstColumnPanel.setBorder(false);
		firstColumnPanel.setPaddings(5);
//		firstColumnPanel.setFrame(true);
		
		

		
		/*------- Subcontract Information field set-------*/
		// First column - packageNo, description, scType, paymentTerms,
		FieldSet scDetailfieldSet = new FieldSet("Subcontract Information");
		scDetailfieldSet.setBorder(true);
		
		// Package No.
		Panel firstColumnUpper = new Panel();
		firstColumnUpper.setLayout(new ColumnLayout());
		firstColumnUpper.setBorder(false);
//		firstColumnUpper.setHeight(150);
		firstColumnUpper.setPaddings(5);

		
		// Package No.
		packageNoField = new NumberField("Package No.", "packageNo");
		packageNoField.setMaxLength(4);
		packageNoField.setMinLength(4);
		packageNoField.setWidth(50);
		packageNoField.setAllowDecimals(false);
		packageNoField.setDisabled(true);
		

		Panel firstColumnTopLeftPanel = new Panel(); 
		firstColumnTopLeftPanel.setLayout(new FormLayout());
		firstColumnTopLeftPanel.setBorder(false);
		firstColumnTopLeftPanel.add(packageNoField);
		
		Panel firstColumnTopRightPanel = new Panel(); 
		firstColumnTopRightPanel.setLayout(new FormLayout());
		firstColumnTopRightPanel.setBorder(false);
		firstColumnTopRightPanel.setPaddings(5);
		
		obtainSCStatusCode();
		
		// SC Status, SC Description
		Panel firstColumnMiddle = new Panel();
//		firstColumnMiddle.setHeight(150);
//		firstColumnMiddle.setPaddings(5);
		firstColumnMiddle.setBorder(false);
		firstColumnMiddle.setLayout(new TableLayout(2));
		
		Label scStatusLabel = new Label("SC Status :      ");
		scStatusLabel.setCtCls("table-cell");
		scStatusLabel.setWidth(120);
		scStatusValueLabel = new Label();
		scStatusValueLabel.setWidth(170);
		scStatusValueLabel.setCtCls("text-Right-Align");
		
		Label scDescriptionLabel = new Label("Description :      ");
		scDescriptionLabel.setCtCls("table-cell");
		scDescriptionLabel.setWidth(120);
		descriptionField = new TextArea();
		descriptionField.setPreventScrollbars(true);
		descriptionField.setWidth(170);
		descriptionField.setDisabled(true);
		descriptionField.setCtCls("text-Right-Align");
		
		firstColumnUpper.add(firstColumnTopLeftPanel, new ColumnLayoutData(0.65));
		firstColumnUpper.add(firstColumnTopRightPanel, new ColumnLayoutData(0.35));
		
		firstColumnMiddle.add(scStatusLabel);
		firstColumnMiddle.add(scStatusValueLabel);
		firstColumnMiddle.add(scDescriptionLabel);
		firstColumnMiddle.add(descriptionField);
//		verticalSplitPanel.setTopWidget(firstColumnMiddle);
		
		Panel firstColumnBottom = new Panel();
		firstColumnBottom.setLayout(new TableLayout(3));
		firstColumnBottom.setMargins(5, 0, 0, 0);
		firstColumnBottom.setPaddings(0, 5, 5, 5);

		
		Label originalSCSumLabel = new Label("Original SC Sum : ");
		originalSCSumLabel.setCtCls("table-cell");
		originalSCSumField = new Label();
		originalSCSumField.setWidth(160);
		originalSCSumField.setCtCls("text-Right-Align");
		
		Label remeasuredSCSumLabel = new Label("Remeasured SC Sum : ");
		remeasuredSCSumLabel.setCtCls("table-cell");
		remeasuredSCSumField = new Label();
		remeasuredSCSumField.setWidth(160);
		remeasuredSCSumField.setCtCls("text-Right-Align");
		
		Label approvedSCSumLabel = new Label("Approved VO Amount : ");
		approvedSCSumLabel.setCtCls("table-cell");
		approvedSCSumField = new Label();
		approvedSCSumField.setWidth(160);
		approvedSCSumField.setCtCls("gwt-Label-right-singleUnderline");
		
		Label revisedSCSumLabel = new Label("Revised SC Sum : ");
		revisedSCSumLabel.setCtCls("table-cell");
		revisedSCSumField = new Label();
		revisedSCSumField.setWidth(160);
		revisedSCSumField.setCtCls("gwt-Label-right-doubleUnderline");
		
		firstColumnBottom.add(originalSCSumLabel);
		firstColumnBottom.add(new Label());
		firstColumnBottom.add(originalSCSumField);
		firstColumnBottom.add(remeasuredSCSumLabel);
		firstColumnBottom.add(new Label());
		firstColumnBottom.add(remeasuredSCSumField);
		firstColumnBottom.add(approvedSCSumLabel);
		firstColumnBottom.add(new Label("+"));
		firstColumnBottom.add(approvedSCSumField);
		firstColumnBottom.add(revisedSCSumLabel);
		firstColumnBottom.add(new Label());
		firstColumnBottom.add(revisedSCSumField);
		
		scDetailfieldSet.add(firstColumnUpper);
		scDetailfieldSet.add(firstColumnMiddle);
		scDetailfieldSet.add(firstColumnBottom);
		/*------- Sc Details field set-------*/
		
		/*------- Form of Subcontract field set-------*/
		FieldSet formOfSubcontractFieldSet = new FieldSet("Form of Subcontract");
		formOfSubcontractFieldSet.setHeight(180);
		Panel formOfSubcontractPanel = new Panel();
		formOfSubcontractPanel.setLayout(new TableLayout(3));
		formOfSubcontractPanel.setBorder(false);
		majorRadio = new Radio(SCPackage.MAJOR, "scFormOptions");
		majorRadio.setHideLabel(true);
		majorRadio.setDisabled(true);
		majorRadio.setCtCls("table-cell");
		if (scPackage.getSubcontractStatus() == null)
			majorRadio.setChecked(true);
		minorRadio = new Radio(SCPackage.MINOR, "scFormOptions");
		minorRadio.setHideLabel(true);
		minorRadio.setDisabled(true);
		minorRadio.setCtCls("table-cell");
		consultancyRadio = new Radio(SCPackage.CONSULTANCY_AGREEMENT, "scFormOptions");
		consultancyRadio.setHideLabel(true);
		consultancyRadio.setDisabled(true);
		consultancyRadio.setCtCls("table-cell");
		internalJobRadio = new Radio(SCPackage.INTERNAL_TRADING, "scFormOptions");
		internalJobRadio.setHideLabel(true);
		internalJobRadio.setDisabled(true);
		internalJobRadio.setCtCls("table-cell");
		Label internalJobLabel = new Label("Job : ");
		internalJobLabel.setCtCls("table-cell");
		internalJobField = new NumberField("Job for Internal Trading", "internalJob");
		internalJobField.setWidth(60);
		internalJobField.setAllowDecimals(false);
		internalJobField.setDisabled(true);
		internalJobField.setCtCls("table-cell");
		
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

		formOfSubcontractFieldSet.add(formOfSubcontractPanel);
		/*------- Form of Subcontract field set-------*/
		
		/*------- Subcontract Type field set-------*/
		FieldSet subcontractTypeFieldSet = new FieldSet("Subcontract Type");
		Panel subcontractTypePanel = new Panel();
		subcontractTypePanel.setLayout(new HorizontalLayout(3));
		subcontractTypePanel.setBorder(false);
		subcontractTypeFieldSet.setBorder(true);
		labourCheckbox = new Checkbox("Labour");
		labourCheckbox.setHideLabel(true);
		labourCheckbox.setDisabled(true);
		labourCheckbox.setCtCls("table-cell");
		plantCheckbox = new Checkbox("Plant");
		plantCheckbox.setHideLabel(true);
		plantCheckbox.setDisabled(true);
		plantCheckbox.setCtCls("table-cell");
		materialCheckbox = new Checkbox("Material");
		materialCheckbox.setHideLabel(true);
		materialCheckbox.setDisabled(true);
		materialCheckbox.setCtCls("table-cell");
		
		subcontractTypePanel.add(labourCheckbox);
		subcontractTypePanel.add(plantCheckbox);
		subcontractTypePanel.add(materialCheckbox);
		
		subcontractTypeFieldSet.add(subcontractTypePanel);
		/*------- Subcontract Type field set-------*/

		/*------- Subcontract Term field set-------*/
		FieldSet subcontractTermFieldSet = new FieldSet("Subcontract Term");
		Panel subcontractTermPanel = new Panel();
		subcontractTermPanel.setLayout(new HorizontalLayout(2));
		subcontractTermPanel.setBorder(false);
		lumpSumRadio = new Radio("Lump Sum", "scTermOptions");
		lumpSumRadio.setHideLabel(true);
		lumpSumRadio.setChecked(true);
		lumpSumRadio.setDisabled(true);
		lumpSumRadio.setCtCls("table-cell");
		remeasurementRadio = new Radio("Re-measurement", "scTermOptions");
		remeasurementRadio.setHideLabel(true);
		remeasurementRadio.setDisabled(true);
		remeasurementRadio.setCtCls("table-cell");
		subcontractTermPanel.add(lumpSumRadio);
		subcontractTermPanel.add(remeasurementRadio);
		
		subcontractTermFieldSet.add(subcontractTermPanel);
		/*------- Subcontract Term field set-------*/
		
		firstColumnPanel.add(scDetailfieldSet);
		firstColumnPanel.add(formOfSubcontractFieldSet);
		firstColumnPanel.add(subcontractTypeFieldSet);
		firstColumnPanel.add(subcontractTermFieldSet);

		// Second column -- retention, scTerm
		Panel secondColumn = new Panel();
		FormLayout secondColumnLayout = new FormLayout();
		secondColumnLayout.setLabelWidth(250);
		secondColumn.setLayout(new FormLayout());
		secondColumn.setBorder(false);
		secondColumn.setPaddings(5);
		
		/*------- Payment Terms field set-------*/
		FieldSet paymentTermsFieldSet = new FieldSet("Payment Terms");
		originalSCSumLabel.setCtCls("table-cell");
		Panel paymentTermsPanel = new Panel();
		paymentTermsPanel.setLayout(new TableLayout(2));
		paymentTermsPanel.setBorder(false);
		
		Label paymentTermLabel = new Label("Terms : ");
		paymentTermLabel.setCtCls("table-cell");
		paymentTermsField = new Label();
		paymentTermsField.setWidth(220);
		paymentTermsField.setCtCls("text-Right-Align");
		storeTerms.load();
		
		Label paymentTermDescriptionLabel = new Label("Description : ");
		paymentTermDescriptionLabel.setCtCls("table-cell");
		paymentTermsDescriptionField = new TextArea("Description", "paymentTermsField");
		paymentTermsDescriptionField.setWidth(220);
		paymentTermsDescriptionField.setDisabled(true);
		
		Label currencyCodeLabel = new Label("Currency : ");
		currencyCodeLabel.setCtCls("table-cell");
		currencyCodeTextField = new Label();
		currencyCodeTextField.setWidth(220);
		currencyCodeTextField.setCtCls("text-Right-Align");
		Label exchangeRateLabel = new Label("Exchange Rate : ");
		exchangeRateLabel.setCtCls("table-cell");
		exchangeRateTextField= new Label();
		exchangeRateTextField.setWidth(220);
		exchangeRateTextField.setCtCls("text-Right-Align");
		
		paymentTermsPanel.add(paymentTermLabel);
		paymentTermsPanel.add(paymentTermsField);
		paymentTermsPanel.add(paymentTermDescriptionLabel);
		paymentTermsPanel.add(paymentTermsDescriptionField);
		paymentTermsPanel.add(currencyCodeLabel);
		paymentTermsPanel.add(currencyCodeTextField);
		paymentTermsPanel.add(exchangeRateLabel);
		paymentTermsPanel.add(exchangeRateTextField);
		
		paymentTermsFieldSet.add(paymentTermsPanel);
		/*------- Payment Terms field set-------*/

		/*------- Retention field set-------*/
		retention = new FieldSet("Retention");
		retention.setHeight(430);
		Panel retentionPanel = new Panel();
		retentionPanel.setLayout(new TableLayout(2));
		retentionPanel.setBorder(false);
		
		
		noRetentionRadio = new Radio("No Retention", "retentionOptions");
		noRetentionRadio.setHideLabel(true);
		noRetentionRadio.setDisabled(true);
		
		lumpSumRetentionRadio = new Radio("Lump Sum Amount Retention", "retentionOptions");
		lumpSumRetentionRadio.setHideLabel(true);
		lumpSumRetentionRadio.setDisabled(true);
		
		percentageRadio = new Radio("Percentage", "retentionOptions");
		percentageRadio.setHideLabel(true);
		percentageRadio.setChecked(true);
		percentageRadio.setDisabled(true);
		
		originalSumRadio = new Radio("Original SC Sum", "percentageOptions");
		revisedSumRadio = new Radio("Revised SC Sum", "percentageOptions");
		revisedSumRadio.setChecked(false);
		originalSumRadio.setDisabled(true);
		revisedSumRadio.setDisabled(true);
		
		Label maxRetentionLabel = new Label("Maximum Retention % : ");
		maxRetentionLabel.setCtCls("table-cell");
		maxRetentionField = new Label();
		maxRetentionField.setWidth(170);
		maxRetentionField.setCtCls("text-Right-Align");
		retentionPanel.add(maxRetentionLabel);
		retentionPanel.add(maxRetentionField);
		
		Label interimRetentionLabel = new Label("Interim Retention % : ");
		interimRetentionLabel.setCtCls("table-cell");
		interimRetentionField = new Label();
		interimRetentionField.setWidth(170);
		interimRetentionField.setCtCls("text-Right-Align");
		retentionPanel.add(interimRetentionLabel);
		retentionPanel.add(interimRetentionField);
		
		Label mosRetentionLabel = new Label("MOS Retention % : ");
		mosRetentionLabel.setCtCls("table-cell");
		mosRetentionField = new Label();
		mosRetentionField.setWidth(170);
		mosRetentionField.setCtCls("text-Right-Align");
		retentionPanel.add(mosRetentionLabel);
		retentionPanel.add(mosRetentionField);

		if (scPackage.getPackageNo() == null) {
			percentageRadio.setChecked(true);
			revisedSumRadio.setChecked(true);
		} else {
			if (scPackage.getRetentionTerms() != null) {
				if (SCPackage.RETENTION_LUMPSUM.equalsIgnoreCase(scPackage
						.getRetentionTerms())){
					lumpSumRetentionRadio.setChecked(true);
					percentageRadio.setChecked(false);
					revisedSumRadio.setChecked(false);
					originalSumRadio.setChecked(false);
				} else if (SCPackage.RETENTION_ORIGINAL
						.equalsIgnoreCase(scPackage.getRetentionTerms())) {
					lumpSumRetentionRadio.setChecked(false);
					percentageRadio.setChecked(true);
					originalSumRadio.setChecked(true);
					revisedSumRadio.setChecked(false);
				} else if (SCPackage.RETENTION_REVISED
						.equalsIgnoreCase(scPackage.getRetentionTerms())) {
					lumpSumRetentionRadio.setChecked(false);
					percentageRadio.setChecked(true);
					originalSumRadio.setChecked(false);
					revisedSumRadio.setChecked(true);
				}
			} else {
				lumpSumRetentionRadio.setChecked(false);
				percentageRadio.setChecked(false);
				revisedSumRadio.setChecked(false);
				originalSumRadio.setChecked(false);
				noRetentionRadio.setChecked(true);
			}
		}
		
		retentionPanel.add(new Label(), new TableLayoutData(2));
		
		Label retentionAmountLabel = new Label("Retention Amount : ");
		retentionAmountLabel.setCtCls("table-cell");
		retentionAmountField = new Label();
		retentionAmountField.setWidth(170);
		retentionAmountField.setCtCls("text-Right-Align");
		retentionPanel.add(retentionAmountLabel);
		retentionPanel.add(retentionAmountField);
		
		Label accumulatedRetentionLabel = new Label("Accumulated Retention : ");
		accumulatedRetentionLabel.setCtCls("table-cell");
		accumulatedRetentionField = new Label();
		accumulatedRetentionField.setWidth(170);
		accumulatedRetentionField.setCtCls("text-Right-Align");
		retentionPanel.add(accumulatedRetentionLabel);
		retentionPanel.add(accumulatedRetentionField);
		
		Label releasedRetentionLabel = new Label("Retention Released : + ");
		releasedRetentionLabel.setCtCls("table-cell");
		releasedRetentionField = new Label();
		releasedRetentionField.setWidth(170);
		releasedRetentionField.setCtCls("gwt-Label-right-singleUnderline");
		retentionPanel.add(releasedRetentionLabel);
		retentionPanel.add(releasedRetentionField);
		
		Label retentionBalanceLabel = new Label("Retention Balance : ");
		retentionBalanceLabel.setCtCls("table-cell");
		retentionBalanceField = new Label();
		retentionBalanceField.setWidth(170);
		retentionBalanceField.setCtCls("gwt-Label-right-doubleUnderline");
		retentionPanel.add(retentionBalanceLabel);
		retentionPanel.add(retentionBalanceField);
		
		
		
		
		retention.add(noRetentionRadio);
		retention.add(lumpSumRetentionRadio);
		retention.add(percentageRadio);
		retention.add(originalSumRadio);
		retention.add(revisedSumRadio);
		retention.add(retentionPanel);
		/*------- Retention field set-------*/

		secondColumn.add(paymentTermsFieldSet);
		secondColumn.add(retention);
		
		/*------ END OF SECOND COLUMN ------*/

		/* Third column -- CPF Calculation, Form of subcontract*/
		Panel thirdColumn = new Panel();
		thirdColumn.setLayout(new FormLayout());
		thirdColumn.setBorder(false);
		thirdColumn.setPaddings(5);
		
		/*------- Subcontractor Info. field set-------*/
		FieldSet subcontractorInfoFieldSet = new FieldSet("Subcontractor Info.");
		subcontractorInfoFieldSet.setBorder(true);
		Panel subcontractInfoPanel = new Panel();
		subcontractInfoPanel.setLayout(new TableLayout(2));
		subcontractInfoPanel.setBorder(false);
		
		Label subcontractorNatureLabel = new Label("Subcontractor Nature : ");
		subcontractorNatureLabel.setCtCls("table-cell");
		subcontractorNatureField = new Label();
		subcontractorNatureField.setWidth(160);
		subcontractorNatureField.setCtCls("table-cell-right-align");
		
		Label subcontractorNumberLabel = new Label("Subcontractor Number : ");
		subcontractorNumberLabel.setCtCls("table-cell");
		subcontractorNumberField = new Label();
		subcontractorNumberField.setWidth(160);
		subcontractorNumberField.setCtCls("table-cell-right-align");

		Label subcontractorDescriptionLabel = new Label("Subcontractor Name : ");
		subcontractorDescriptionLabel.setCtCls("table-cell");
		subcontractorLabel = new Label();
		subcontractorLabel.setWidth(160);
		subcontractorLabel.setCtCls("table-cell-right-align");
		
		Label workScopeLabel = new Label("Work Scope : ");
		workScopeLabel.setCtCls("table-cell");
		workScopeField = new Label();
		workScopeField.setWidth(160);
		workScopeField.setCtCls("table-cell-right-align");

		workScopeField.setText("");
		if (scPackage != null)
			obtainSCWorkScopeList();
			if (scWorkScopeList != null
					&& scWorkScopeList.size() >= 1)
				if (scWorkScopeList.get(0) != null)
					if (scWorkScopeList.get(0).getWorkScope() != null)
						workScopeField.setText(scWorkScopeList
								.get(0).getWorkScope());

		Label workScopeDescriptionLabel = new Label("Work Scope Description : ");
		workScopeDescriptionLabel.setCtCls("table-cell");
		workScopeDescription.setText(" ");
		workScopeDescription.setWidth(160);
		workScopeDescription.setCtCls("table-cell-right-align");
		
		subcontractInfoPanel.add(subcontractorNatureLabel);
		subcontractInfoPanel.add(subcontractorNatureField);
		subcontractInfoPanel.add(subcontractorNumberLabel);
		subcontractInfoPanel.add(subcontractorNumberField);
		subcontractInfoPanel.add(subcontractorDescriptionLabel);
		subcontractInfoPanel.add(subcontractorLabel);
		subcontractInfoPanel.add(workScopeLabel);
		subcontractInfoPanel.add(workScopeField);
		subcontractInfoPanel.add(workScopeDescriptionLabel);
		subcontractInfoPanel.add(workScopeDescription);
		
		subcontractorInfoFieldSet.add(subcontractInfoPanel);
		/*------- Subcontractor Info. field set-------*/
		
		/*------- split/terminate field set-------*/
		FieldSet splitTerminateFieldSet = new FieldSet("Statuses");
		splitTerminateFieldSet.setHeight(200);
		Panel statusColumnPanel = new Panel();
		statusColumnPanel.setLayout(new TableLayout(2));
		statusColumnPanel.setBorder(false);
		
		Label scPaymentStatusLabel = new Label("Payment Status:");
		scPaymentStatusLabel.setCtCls("table-cell");
		scPaymentStatusLabel.setWidth(200);
		statusColumnPanel.add(scPaymentStatusLabel);
		scPaymentStatusDesccriptionLabel = new Label();
		scPaymentStatusDesccriptionLabel.setCtCls("table-cell-right-align");
		statusColumnPanel.add(scPaymentStatusDesccriptionLabel);
		
		Label addendumSubmittedLabel = new Label("Addendum Approval:");
		addendumSubmittedLabel.setCtCls("table-cell");
		addendumSubmittedLabel.setWidth(200);
		statusColumnPanel.add(addendumSubmittedLabel);
		addendumSubmittedDescriptionLabel = new Label();
		addendumSubmittedDescriptionLabel.setCtCls("table-cell-right-align");
		statusColumnPanel.add(addendumSubmittedDescriptionLabel);
		
		
		Label scPaymentSubmittedLabel = new Label("Payment Approval:");
		scPaymentSubmittedLabel.setCtCls("table-cell");
		scPaymentSubmittedLabel.setWidth(200);
		statusColumnPanel.add(scPaymentSubmittedLabel);
		scPaymentSubmittedDesccriptionLabel = new Label();
		scPaymentSubmittedDesccriptionLabel.setCtCls("table-cell-right-align");
		statusColumnPanel.add(scPaymentSubmittedDesccriptionLabel);
		
		Label splitTerminatedStatusLabel = new Label("Split/Terminate:");
		splitTerminatedStatusLabel.setCtCls("table-cell");
		splitTerminatedStatusLabel.setWidth(200);
		statusColumnPanel.add(splitTerminatedStatusLabel);
		splitTerminatedStatusDescriptionLabel = new Label();
		splitTerminatedStatusDescriptionLabel.setCtCls("table-cell-right-align");
		statusColumnPanel.add(splitTerminatedStatusDescriptionLabel);
		
		splitTerminateFieldSet.add(statusColumnPanel);
		/*------- split/terminate field set-------*/

		/*------- CPF Calculation field set-------*/
		FieldSet cpfCalculation = new FieldSet("CPF Calculation");
		Panel cpfCalculationPanel = new Panel();
		cpfCalculationPanel.setLayout(new TableLayout(4));
		cpfCalculationPanel.setBorder(false);
		
		cpfSubjectRadio = new Radio(SCPackage.CPF_SUBJECT_TO, "cpfOptions");
		cpfSubjectRadio.setCtCls("table-cell");
		cpfSubjectRadio.setHideLabel(true);
		cpfSubjectRadio.setDisabled(true);
		
		cpfNotSubjectRadio = new Radio(SCPackage.CPF_NOT_SUBJECT_TO, "cpfNotOptions");
		cpfNotSubjectRadio.setCtCls("table-cell");
		cpfNotSubjectRadio.setHideLabel(true);
		cpfNotSubjectRadio.setDisabled(true);
		
		Label cpfBasePeriodLabel = new Label("	CPF Base Period : ");
		cpfBasePeriodLabel.setCtCls("table-cell-right-align");
		cpfBasePeriodField = new NumberField("CPF Base Period", "cpfBasePeriod");
		cpfBasePeriodField.setCtCls("table-cell");
		cpfBasePeriodField.setWidth(40);
		cpfBasePeriodField.setAllowDecimals(false);
		cpfBasePeriodField.setDisabled(true);
		
		Label cpfBaseYearLabel = new Label("	CPF Base Year : ");
		cpfBaseYearLabel.setCtCls("table-cell-right-align");
		cpfBaseYearField = new NumberField("CPF Base Year", "cpfBaseYear");
		cpfBaseYearField.setCtCls("table-cell");
		cpfBaseYearField.setWidth(40);
		cpfBaseYearField.setAllowDecimals(false);
		cpfBaseYearField.setDisabled(true);

		cpfCalculationPanel.add(cpfSubjectRadio);
		cpfCalculationPanel.add(new Label(), new TableLayoutData(3));
		cpfCalculationPanel.add(cpfBasePeriodLabel);
		cpfCalculationPanel.add(cpfBasePeriodField);
		cpfCalculationPanel.add(cpfBaseYearLabel);
		cpfCalculationPanel.add(cpfBaseYearField);
		cpfCalculationPanel.add(cpfNotSubjectRadio);
		
		cpfCalculation.add(cpfCalculationPanel);
		/*------- CPF Calculation field set-------*/

		/*------ ApprovalRoute field set ------*/
		approvalRoute.setHeight(70);
		Panel approvalRoutePanel = new Panel();
		approvalRoutePanel.setLayout(new TableLayout(4));
		approvalRoutePanel.setBorder(false);

		approvalRouteLabel.setCtCls("table-cell");
		approvalRouteLabel.setText("Approval Sub-type:");

		approvalRouteField = new TextField("Approval Route", "approvalRoute");
		approvalRouteField.setWidth(50);
		approvalRouteField.setDisabled(true);

		approvalRoutePanel.add(approvalRouteLabel);
		approvalRoutePanel.add(approvalRouteField);
		approvalRoute.add(approvalRoutePanel);
		/*------ ApprovalRoute field set ------*/
		
		thirdColumn.add(subcontractorInfoFieldSet);
		thirdColumn.add(splitTerminateFieldSet);
		thirdColumn.add(cpfCalculation);
		
		thirdColumn.add(approvalRoute);
		/*------- END OF THIRD COLUMN-------*/

		// Add columns to panel, panel to window
		columnsPanel.add(firstColumnPanel, new ColumnLayoutData(0.33));
		columnsPanel.add(secondColumn, new ColumnLayoutData(0.33));
		columnsPanel.add(thirdColumn, new ColumnLayoutData(0.34));
		mainPanel.add(columnsPanel);
//		this.setFrame(true);
		this.add(mainPanel);
		populateForm();
	}

	/**
	 * adds the toolbar to the panel
	 */
	private void addToolbar() {
		this.openSplitWindowButton = new ToolbarButton();
		this.openSplitWindowButton.setText("Split");
		this.openSplitWindowButton.setIconCls("split-icon");
		this.openSplitWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				MessageBox
						.confirm(
								"Split Confirmation",
								"Are you sure you want to split this subcontract?<br/>"
										+ "Have You attached supporting informationin the Subcontract Attachment?<br/><br/>"
										+ "You cannot process any payment submission or detail maintenance until the<br/>"
										+ "request has been approved or rejected.<br/><br/>"
										+ "Please click 'Yes' to confirm or 'No' to exit.",
								new MessageBox.ConfirmCallback() {
									public void execute(String btnID) {
										if (btnID.equals("yes")) {
											globalSectionController
													.showSplitTerminateWindow(
															scPackage, SCPackage.SPLIT);
										}
									}
								});
			}
		});
		ToolTip showSplitTip = new ToolTip();
		showSplitTip.setTitle("Split");
		showSplitTip.setHtml("Split this Subcontract.");
		showSplitTip.setCtCls("toolbar-button");
		showSplitTip.setDismissDelay(15000);
		showSplitTip.setWidth(300);
		showSplitTip.setTrackMouse(true);
		showSplitTip.applyTo(this.openSplitWindowButton);
		this.openTerminateWindowButton = new ToolbarButton();
		this.openTerminateWindowButton.setText("Terminate");
		this.openTerminateWindowButton.setIconCls("remove-button-icon");
		this.openTerminateWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				MessageBox
						.confirm(
								"Terminate Confirmation",
								"Are you sure you want to terminate this subcontract?<br/>"
										+ "Have You attached supporting informationin the Subcontract Attachment?<br/><br/>"
										+ "You cannot process any payment submission or detail maintenance until the<br/>"
										+ "request has been approved or rejected.<br/><br/>"
										+ "Please click 'Yes' to confirm or 'No' to exit.",
								new MessageBox.ConfirmCallback() {
									public void execute(String btnID) {
										if (btnID.equals("yes")) {
											globalSectionController
													.showSplitTerminateWindow(
															scPackage, SCPackage.TERMINATE);
										}
									}
								});
			}
		});
		ToolTip showTerminateTip = new ToolTip();
		showTerminateTip.setTitle("Terminate");
		showTerminateTip.setHtml("Terminate this Subcontract.");
		showTerminateTip.setCtCls("toolbar-button");
		showTerminateTip.setDismissDelay(15000);
		showTerminateTip.setWidth(300);
		showTerminateTip.setTrackMouse(true);
		showTerminateTip.applyTo(this.openTerminateWindowButton);
		vendorNoField = new NumberField();
		final Toolbar toolbar = new Toolbar();
		openSCAttachmentButton = new ToolbarButton();
		openSCAttachmentButton.setText("Attachment");
		openSCAttachmentButton.setIconCls("attachment-icon");
		openSCAttachmentButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.showAttachmentWindow(
						"Attachment_Subcontract", jobNumber, packageNo);
			}
		});

		ToolTip showSCAttachmentTip = new ToolTip();
		showSCAttachmentTip.setTitle("Attachment");
		showSCAttachmentTip.setHtml("open window for show Attachment");
		showSCAttachmentTip.setCtCls("toolbar-button");
		showSCAttachmentTip.setDismissDelay(15000);
		showSCAttachmentTip.setWidth(300);
		showSCAttachmentTip.setTrackMouse(true);
		showSCAttachmentTip.applyTo(this.openSCAttachmentButton);

		editDatesButton = new ToolbarButton();
		editDatesButton.setText("Dates");
		editDatesButton.setIconCls("calendar-icon");
		editDatesButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.showEditSCDatesWindow(scPackage);
			}
		});
		ToolTip editDatesTip = new ToolTip();
		editDatesTip.setTitle("Subcontract Dates");
		editDatesTip.setHtml("View and edit important dates for this subcontract");
		editDatesTip.setCtCls("toolbar-button");
		editDatesTip.setDismissDelay(15000);
		editDatesTip.setWidth(300);
		editDatesTip.setTrackMouse(true);
		editDatesTip.applyTo(editDatesButton);
		
		viewTenderDetailsButton = new ToolbarButton();
		viewTenderDetailsButton.setText("View Tender Details");
		viewTenderDetailsButton.setIconCls("report-icon");
		viewTenderDetailsButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalSectionController.showTenderAnalysisEnquiryWindow(packageNo);
			}
		});
		
		ToolTip viewTenderDetailsTip = new ToolTip();
		viewTenderDetailsTip.setTitle("View Tender Details");
		viewTenderDetailsTip.setHtml("View details of tenders for this subcontract");
		viewTenderDetailsTip.setCtCls("toolbar-button");
		viewTenderDetailsTip.setDismissDelay(15000);
		viewTenderDetailsTip.setWidth(300);
		viewTenderDetailsTip.setTrackMouse(true);
		viewTenderDetailsTip.applyTo(viewTenderDetailsButton);
		
		recalculateSCPackageFiguresButton  = new ToolbarButton();
		recalculateSCPackageFiguresButton.setText("Recalculate Subcontract Summary");
		recalculateSCPackageFiguresButton.setIconCls("calculator-icon");
		recalculateSCPackageFiguresButton.setTooltip("Recalculate Subcontract Total Figures", "Recalculate Subcontract Workdone and Payment totals from Subcontract Details of the Job");
		recalculateSCPackageFiguresButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Recalculating", true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().calculateTotalWDandCertAmount(globalSectionController.getJob().getJobNumber(), packageNo, true, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.throwException(e);
					}
					public void onSuccess(Boolean success) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						MessageBox.alert("Subcontract figures have been recalculated.");
						globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
					}
				});
			}
		});
		if (scPackage.isAwarded()) {
			this.refreshButton = new ToolbarButton();
			this.refreshButton.setText("Refresh");
			this.refreshButton.setIconCls("toggle-button-icon");
			this.refreshButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
				}
			});

			ToolTip refreshButtonTip = new ToolTip();
			refreshButtonTip.setTitle("Refresh");
			refreshButtonTip.setHtml("Refresh Current Page");
			refreshButtonTip.setCtCls("toolbar-button");
			refreshButtonTip.setDismissDelay(15000);
			refreshButtonTip.setWidth(300);
			refreshButtonTip.setTrackMouse(true);
			refreshButtonTip.applyTo(this.refreshButton);

			this.openSCAttachmentButton.setVisible(false);
			this.openSplitWindowButton.setVisible(false);
			this.openTerminateWindowButton.setVisible(false);
			recalculateSCPackageFiguresButton.setVisible(false);
			
			toolbar.addButton(openSCAttachmentButton);
			toolbar.addSeparator();
			toolbar.addButton(openSplitWindowButton);
			toolbar.addSeparator();
			toolbar.addButton(openTerminateWindowButton);
			toolbar.addSeparator();
			toolbar.addButton(editDatesButton);
			toolbar.addSeparator();
			toolbar.addButton(viewTenderDetailsButton);
			toolbar.addSeparator();
			toolbar.addButton(recalculateSCPackageFiguresButton);
			toolbar.addSeparator();
			toolbar.addButton(refreshButton);

		} else {
			// Edit Package Header
			editButton = new ToolbarButton();
			editButton.setText("Edit Package");
			editButton.setIconCls("edit-button-icon");
			editButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					
					UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPaymentRepository().obtainPaymentLatestCert(scPackage.getJob().getJobNumber(), scPackage.getPackageNo(), new AsyncCallback<SCPaymentCert>() {
						public void onSuccess(SCPaymentCert paymentCert) {
							if(paymentCert!=null && (paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
									|| paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
									|| paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))){
								
								MessageBox.alert("Payment Requisition Submitted. Subcontract Package Information will be frozen.");
							}
							else
								globalSectionController.showEditPackageHeaderWindow(scPackage);
							
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						}
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							UIUtil.throwException(e);
						}
					});
				}
			});
			// Split resources for TA
			inputTAButton = new ToolbarButton();
			inputTAButton.setText("Input Tender Analysis");
			inputTAButton.setIconCls("analysis-icon");
			inputTAButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					
					if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))
						
						MessageBox.alert("Payment Requisition Submitted. Tender Analysis will be frozen.");
					else{
						if (scPackage.getSubcontractStatus() == null)
							MessageBox
							.alert("Please input package details before continuing the tender analysis.<br/>Click the 'Edit Package Details' button.");
						else if (scPackage.getSubcontractStatus().equals(
								Integer.valueOf(330)))
							MessageBox
							.alert("TA cannot be updated - Package has been submitted for approval");
						else
							globalSectionController
							.showTenderAnalysisSplitResourcesWindow(packageNo);
					}
				}
			});

						
			
			// Input Feedback Rate
			vendorNoField.setAllowDecimals(false);
			inputRateButton = new ToolbarButton();
			inputRateButton.setText("Input Feedback Rates");
			inputRateButton.setIconCls("edit-button-icon");
			inputRateButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					
					if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))
						
						MessageBox.alert("Payment Requisition Submitted. Vendor Information will be frozen.");
					else{
						if (scPackage.getSubcontractStatus() == null)
							MessageBox
							.alert("Please input package details before continuing the tender analysis.<br/>Click the 'Edit Package Details' button.");
						else if (scPackage.getSubcontractStatus().equals(
								Integer.valueOf(330)))
							MessageBox
							.alert("TA cannot be updated - Package has been submitted for approval");
						else if (vendorNoField.getValue() == null)
							MessageBox.alert("Please input a vendor number.");
						else
							globalSectionController.showInputVendorFeedbackRateWindow(packageNo, Integer.valueOf(vendorNoField.getValueAsString()));
					}
				}
			});
			ToolTip inputRateTip = new ToolTip();
			inputRateTip.setTitle("Input/Edit Vendor Feedback Rates");
			inputRateTip
					.setHtml("Input/Edit vendor feedback rates for tender analysis");
			inputRateTip.setCtCls("toolbar-button");
			inputRateTip.setDismissDelay(15000);
			inputRateTip.setWidth(300);
			inputRateTip.setTrackMouse(true);
			inputRateTip.applyTo(inputRateButton);

			vendorNoField.addKeyPressListener(new EventCallback() {
				public void execute(EventObject e) {
					globalMessageTicket.refresh();
					if (e.getCharCode() == EventObject.ENTER)
						inputRateButton.fireEvent(inputRateButton
								.getClickEvent());
				}
			});
			
			inputRateButton.setVisible(false);
			inputTAButton.setVisible(false);
			vendorNoField.disable();
			openSCAttachmentButton.setVisible(false);
			recalculateSCPackageFiguresButton.setVisible(false);
			
			// Add buttons to toolbar
			toolbar.addButton(openSCAttachmentButton);
			toolbar.addSeparator();
			toolbar.addButton(editButton);
			toolbar.addSeparator();
			toolbar.addButton(editDatesButton);
			toolbar.addSeparator();
			toolbar.addButton(inputTAButton);
			toolbar.addSeparator();
			//toolbar.addButton(paymentbeforeAward);
			toolbar.addText("Vendor No:");
			toolbar.addField(vendorNoField);
			toolbar.addButton(inputRateButton);
			toolbar.addSeparator();
			toolbar.addButton(recalculateSCPackageFiguresButton);
		}
		
		/**
		 * Button added by Henry Lai
		 * 02-Dec-2014
		 */
		tipsButton = new ToolbarButton("Subcontract Status Info Tips");
		tipsButton.setIconCls("bulb-icon");
		tipsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByTipsButton(920);
			}
		});
		
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addSeparator();
		toolbar.addButton(tipsButton);

		// Check for access rights - then add toolbar buttons accordingly
		try {
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID,
					GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController
					.getUser().getUsername(), RoleSecurityFunctions.F010201_PACKAGE_EDITOR_FORMPANEL,
					new AsyncCallback<List<String>>() {
						public void onFailure(Throwable e) {
							UIUtil.alert(e.getMessage());
						}

						public void onSuccess(List<String> accessRightsReturned) {
							try {
								accessRightsList = accessRightsReturned;
								UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
								if (scPackage.isAwarded()) {
									securitySetup();
								} else {
									securitySetupForNonAward();
									if (Integer.valueOf(330).equals(scPackage.getSubcontractStatus()))
										setUpForWaitingForApproval();
								}
							} catch (Exception e) {
								UIUtil.alert(e);
								UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							}
						}
					});
		} catch (Exception e) {
			UIUtil.alert(e.getMessage());
		}
		this.setTopToolbar(toolbar);
	}

	/**
	 * @return the allowManualInputSCWorkdone
	 */
	public String getAllowManualInputSCWorkdone() {
		return allowManualInputSCWorkdone;
	}

	public GlobalSectionController getGlobalController() {
		return globalSectionController;
	}

	/**
	 * @return the legacyJobFlag
	 */
	public String getLegacyJobFlag() {
		return legacyJobFlag;
	}

	/**
	 * @return the paymentRequestStatus
	 */
	public String getPaymentRequestStatus() {
		return paymentRequestStatus;
	}

	public String getPaymentStatus() {
		return this.paymentStatus;
	}

	public String getScreenName() {
		return PACKAGE_FORM_PANEL;
	}

	public String getSplitTerminateStatus() {
		return this.splitTerminateStatus;
	}

	public String getSubmmittedAddendum() {
		return this.submittedAddendum;
	}

	private void populateForm() {
	
		AmountRenderer amountRenderer = new AmountRenderer(globalSectionController.getUser());
		
		String packageNo = scPackage.getPackageNo();
		if (packageNo != null && packageNo.trim().length() > 0) {
			packageNoField.setValue(packageNo);
			String nature = scPackage.getSubcontractorNature();
			if (nature == null || nature.trim().length() == 0) {
				if (packageNo.startsWith("1"))
					nature = "DSC";
				else if (packageNo.startsWith("2"))
					nature = "NDSC";
				else if (packageNo.startsWith("3"))
					nature = "NSC";
			}
			subcontractorNatureField.setText(nature);
		}
		packageNoField.setDisabled(true);
		
		
		descriptionField.setValue(scPackage.getDescription());
		currencyCodeTextField.setText(scPackage.getPaymentCurrency());
		exchangeRateTextField.setText(Double.toString(scPackage.getExchangeRate()));

		if(scPackage.getOriginalSubcontractSum() != null) { originalSCSumField.setText(amountRenderer.render(scPackage.getOriginalSubcontractSum().toString())); }
		if(scPackage.getRemeasuredSubcontractSum() != null) { remeasuredSCSumField.setText(amountRenderer.render(scPackage.getRemeasuredSubcontractSum().toString())); }
		if(scPackage.getApprovedVOAmount() != null) { approvedSCSumField.setText(amountRenderer.render(scPackage.getApprovedVOAmount().toString())); }
		if(scPackage.getRemeasuredSubcontractSum() != null && scPackage.getApprovedVOAmount() != null) {
			revisedSCSumField.setText(amountRenderer.render(((Double)(scPackage.getRemeasuredSubcontractSum() + scPackage.getApprovedVOAmount())).toString()));
		}
		
		this.paymentTermsField.setText(scPackage.getPaymentTerms());
		this.paymentTermsDescriptionField.setValue(scPackage.getPaymentTermsDescription());
		
		if(scPackage.getSubmittedAddendum() == SCPackage.ADDENDUM_SUBMITTED) {
			addendumSubmittedDescriptionLabel.setText("Submitted");
			addendumSubmittedDescriptionLabel.setCtCls("table-cell-right-align_in_green");
		} else {
			addendumSubmittedDescriptionLabel.setText("Not Submitted");
		}
		
		if(SCPackage.FINAL_PAYMENT.equals(scPackage.getPaymentStatus())) {
			scPaymentStatusDesccriptionLabel.setText("Final");
			scPaymentStatusDesccriptionLabel.setCtCls("table-cell-right-align_in_red");
		}
		else if(SCPackage.DIRECT_PAYMENT.equals(scPackage.getPaymentStatus())){
			scPaymentStatusDesccriptionLabel.setText("Interim(Requisiition)");
			scPaymentStatusDesccriptionLabel.setCtCls("table-cell-right-align_in_green");
		}
		else if(SCPackage.INTERIM_PAYMENT.equals(scPackage.getPaymentStatus())){
			scPaymentStatusDesccriptionLabel.setText("Interim");
		}
		else{
			scPaymentStatusDesccriptionLabel.setText("Interim(No Payment)");
		}


		
		if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
				|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
				|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){
					
			scPaymentSubmittedDesccriptionLabel.setText("Submitted");
			scPaymentSubmittedDesccriptionLabel.setCtCls("table-cell-right-align_in_green");

			setupBeforeAward();
		}
		else
			scPaymentSubmittedDesccriptionLabel.setText("Not Submitted");

 
		// Subcontract Type
		if (Boolean.TRUE.equals(scPackage.getLabourIncludedContract()))
			labourCheckbox.setChecked(true);
		if (Boolean.TRUE.equals(scPackage.getPlantIncludedContract()))
			plantCheckbox.setChecked(true);
		if (Boolean.TRUE.equals(scPackage.getMaterialIncludedContract()))
			materialCheckbox.setChecked(true);
		// Subcontract Term
		if (("Re-measurement").equals(scPackage.getSubcontractTerm()))
			remeasurementRadio.setChecked(true);
		// CPF Calculation
		if ((SCPackage.CPF_SUBJECT_TO).equals(scPackage.getCpfCalculation())) {
			cpfSubjectRadio.setChecked(true);
			cpfBasePeriodField.setValue(scPackage.getCpfBasePeriod());
			cpfBaseYearField.setValue(scPackage.getCpfBaseYear());
		}
		if ((SCPackage.CPF_NOT_SUBJECT_TO).equals(scPackage.getCpfCalculation())) {
			cpfNotSubjectRadio.setChecked(true);
		}
		
		if(scPackage.getVendorNo() != null) { subcontractorNumberField.setText(scPackage.getVendorNo()); }
		
		MasterListRepositoryRemoteAsync masterListRepository = (MasterListRepositoryRemoteAsync)GWT.create(MasterListRepositoryRemote.class);
		((ServiceDefTarget)masterListRepository).setServiceEntryPoint(GlobalParameter.MASTER_LIST_REPOSITORY_URL);
		
		if(scPackage.getVendorNo() != null) {
			SessionTimeoutCheck.renewSessionTimer();
			masterListRepository.searchVendorList(scPackage.getVendorNo().trim(), new AsyncCallback<List<MasterListVendor>>() {
	
				public void onFailure(Throwable msg) {
					UIUtil.alert("Failed to retrieve vendor name with vendor no: \"" + scPackage.getVendorNo().trim() + "\" and exception: " + msg);
				}
	
				public void onSuccess(List<MasterListVendor> vendorList) {
					if(vendorList != null && vendorList.size() > 0 && vendorList.get(0) != null && vendorList.get(0).getVendorName() != null) { subcontractorLabel.setText(vendorList.get(0).getVendorName()); }
				}
	
			});
		}
		
		if(scPackage.getSplitTerminateStatus() != null) {
			for(String[] str : SCPackage.SPLITTERMINATESTATUSES) {
				if(scPackage.getSplitTerminateStatus().equals(str[0])) {
					splitTerminatedStatusDescriptionLabel.setText(scPackage.getSplitTerminateStatus()+" - "+str[1]);
					break;
				}
			}
		}else{
			splitTerminatedStatusDescriptionLabel.setText("");
		}
		boolean setRetention = true;

		// Form of Subcontract
		String scForm = scPackage.getFormOfSubcontract();
		if (scForm == null)
			scForm = SCPackage.MAJOR; // Default
		else if (scForm.equals(SCPackage.MAJOR))
			majorRadio.setChecked(true);
		else if (scForm.equals(SCPackage.MINOR))
			minorRadio.setChecked(true);
		else if (scForm.equals(SCPackage.CONSULTANCY_AGREEMENT))
			consultancyRadio.setChecked(true);
		else {
			internalJobRadio.setChecked(true);
			internalJobField.setValue(scPackage.getInternalJobNo());
			// If internal, skip retention
			setRetention = false;
		}
		
		if (setRetention) {

			// Retention. Example: Percentage - Original SC Sum
			String retentionTerms = scPackage.getRetentionTerms();
			if (retentionTerms != null) {
				if (retentionTerms.startsWith("Percentage")) {
					percentageRadio.setChecked(true);
					if (retentionTerms.endsWith("Revised SC Sum"))
						revisedSumRadio.setChecked(true);
					else if (retentionTerms.equals(SCPackage.RETENTION_ORIGINAL))
						originalSumRadio.setChecked(true);
				}
			}

			maxRetentionField
					.setText(scPackage.getMaxRetentionPercentage() != null ? scPackage
							.getMaxRetentionPercentage().toString() : "0.00");
			interimRetentionField.setText(scPackage
					.getInterimRentionPercentage() != null ? scPackage
					.getInterimRentionPercentage().toString() : "0.00");
			mosRetentionField
					.setText(scPackage.getMosRetentionPercentage() != null ? scPackage
							.getMosRetentionPercentage().toString() : "0.00");
			
			if(scPackage.getRetentionAmount() != null) { retentionAmountField.setText(amountRenderer.render(scPackage.getRetentionAmount().toString())); }
			if(scPackage.getAccumlatedRetention() != null) { accumulatedRetentionField.setText(amountRenderer.render(scPackage.getAccumlatedRetention().toString())); }
			if(scPackage.getRetentionReleased() != null) { releasedRetentionField.setText(amountRenderer.render(scPackage.getRetentionReleased().toString())); }
			if(scPackage.getAccumlatedRetention() != null && scPackage.getRetentionReleased() != null) {
				retentionBalanceField.setText(amountRenderer.render(((Double)(scPackage.getAccumlatedRetention() + scPackage.getRetentionReleased())).toString()));
			}
		}
		
		// Work Scope
		obtainSCWorkScopeList();
		if (scWorkScopeList != null
				&& scWorkScopeList.size() > 0) {
			workScopeField.setText(scWorkScopeList.get(0).getWorkScope());
			try {
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().obtainWorkScope(scWorkScopeList.get(0).getWorkScope().trim(), new AsyncCallback<UDC>() {

					public void onFailure(Throwable msg) {
						UIUtil.alert(msg);
					}

					public void onSuccess(UDC udc) {
						workScopeDescription.setText(udc.getDescription());
					}

				});
			} catch (Exception e) {
				UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
			}
		}
		approvalRouteField.setValue(scPackage.getApprovalRoute());
	}


	/*
	 * Method for disabling buttons
	 */
	private void securitySetup() {
		if (accessRightsList.contains("WRITE")) {
			openSplitWindowButton.setVisible(true);
			openSCAttachmentButton.setVisible(true);
			openTerminateWindowButton.setVisible(true);
			recalculateSCPackageFiguresButton.setVisible(true);
		}
		if (accessRightsList.contains("READ")) {
			openSCAttachmentButton.setVisible(true);
		}
	}

	private void securitySetupForNonAward() {
		// if accessRights contain "WRITE"
		if (accessRightsList.contains("WRITE")) {
			if (globalSectionController.getJob().getRepackagingType().equals("1"))
				inputTAButton.setVisible(true);
			inputRateButton.setVisible(true);
			vendorNoField.enable();
			openSCAttachmentButton.setVisible(true);
			recalculateSCPackageFiguresButton.setVisible(true);
		}
	}

	/**
	 * @param allowManualInputSCWorkdone
	 *            the allowManualInputSCWorkdone to set
	 */
	public void setAllowManualInputSCWorkdone(String allowManualInputSCWorkdone) {
		this.allowManualInputSCWorkdone = allowManualInputSCWorkdone;
	}

	public void setGlobalSectionController(
			GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	/**
	 * @param legacyJobFlag
	 *            the legacyJobFlag to set
	 */
	public void setLegacyJobFlag(String legacyJobFlag) {
		this.legacyJobFlag = legacyJobFlag;
	}

	private void setUpForWaitingForApproval() {
		editButton.disable();
		inputRateButton.disable();
		inputTAButton.disable();
		vendorNoField.disable();
		recalculateSCPackageFiguresButton.disable();
	}
	
	private void setupBeforeAward() {
		if(scPackage.getSubcontractStatus()<500){
			editButton.disable();
			inputRateButton.disable();
			inputTAButton.disable();
			vendorNoField.disable();
		}
	}
	
	/**
	 * @author irischau
	 * added on Apr 02, 2014
	 * avoid preload data at globalSectionController
	 */	
	private void obtainSCStatusCode(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUnitRepository().getSCStatusCodeMap(new AsyncCallback<Map<String,String>>() {
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}

			public void onSuccess(Map<String, String> result) {
				if(scPackage.getSubcontractStatus() != null && result.containsKey(scPackage.getSubcontractStatus().toString().trim())) {
					scStatusValueLabel.setText(scPackage.getSubcontractStatus().toString() + " - " + result.get(scPackage.getSubcontractStatus().toString().trim()));
				} else {
					if(scPackage.getSubcontractStatus() != null )
						scStatusValueLabel.setText(scPackage.getSubcontractStatus().toString());
					else 
						scStatusValueLabel.setText("");
				}
			}
		});
	}
}
