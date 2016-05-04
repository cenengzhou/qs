package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.MainCertificateGridPanel;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class AddRevisieIPACert extends Window {
	public static final String ADD = "ADD";
	public static final String UPDATE = "UPDATE";

	private static final String Add_REVISE_IPA_CERT_ID="AddRevisieIPACert";

	//CertNo Panel
	private TextField certificateNoTextField;
	private TextField clientCertNoTextField;
	private TextField certificateStatusTextField;

	//Amount Panel - IPA
	private TextField appliedMainContractorAmountTextField;
	private TextField appliedNSCNDSCAmountTextField;
	private TextField appliedMOSAmountTextField;
	private TextField appliedMainContractorRetentionTextField;
	private TextField appliedMainContractorRetentionReleasedTextField;
	private TextField appliedRetentionforNSCNDSCTextField;
	private TextField appliedRetentionforNSCNDSCReleasedTextField;
	private TextField appliedMOSRetentionTextField;
	private TextField appliedMOSRetentionReleasedTextField;
	private TextField appliedContraChargeAmountTextField;
	private TextField appliedAdjustmentAmountTextField;
	private TextField appliedAdvancePaymentTextField;
	private TextField appliedCPFAmountTextField;
	private TextField appliedNetAmountTextField; 
	private TextField appliedGrossAmountTextField;

	//Amount Panel - IPC
	private TextField certifiedMainContractorAmountTextField;
	private TextField certifiedNSCNDSCAmountTextField;
	private TextField certifiedMOSAmountTextField;
	private TextField certifiedMainContractorRetentionTextField;
	private TextField certifiedMainContractorRetentionReleasedTextField;
	private TextField certifiedRetentionforNSCNDSCTextField;
	private TextField certifiedRetentionforNSCNDSCReleasedTextField;
	private TextField certifiedMOSRetentionTextField;
	private TextField certifiedMOSRetentionReleasedTextField;
	private TextField certifiedContraChargeAmountTextField;
	private TextField certifiedAdjustmentAmountTextField;
	private TextField certifiedAdvancePaymentTextField;
	private TextField certifiedCPFAmountTextField;
	private TextField certifiedNetAmountTextField;
	private TextField certifiedGrossAmountTextField;

	//GST Panel
	private TextField gstReceivableTextField;
	private TextField gstPayableTextField;

	//Date Panel
	private DateField ipaSubmissionDateField;
	//private DateField ipaSentoutDateTextField;
	private DateField certAsAtDateField;
	private DateField certIssueDateField;
	private DateField certDueDateField;

	//Remark Panel
	private TextField remarkTextField;

	private Button saveButton;

	private final AmountRenderer amountRenderer;

	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;

	private GlobalSectionController globalSectionController;
	private MainCertificateGridPanel mainCertificateGridPanel;
	private Panel mainPanel;

	private MainContractCertificate mainContractCertificate = new MainContractCertificate();
	private List<MainCertificateContraCharge> lastContraChargeList;
	private List<String> accessRightsList;
	private Integer currentCertificateNumber;
	private Integer previousCertificateNumber;

	private GlobalMessageTicket globalMessageTicket;
	private Boolean addCertFlag;

	public AddRevisieIPACert (final MainCertificateGridPanel mainCertificateGridPanel,
			final GlobalSectionController globalSectionController, final Integer certificateNumber , String addUpdateFlag) {
		super();
		this.globalSectionController = globalSectionController;
		this.mainCertificateGridPanel = mainCertificateGridPanel;
		this.currentCertificateNumber = certificateNumber;
		this.previousCertificateNumber = (certificateNumber != null && certificateNumber > 1)
				? certificateNumber - 1
				: certificateNumber;

		addCertFlag = (addUpdateFlag != null) && (addUpdateFlag.equals(ADD));

		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		globalMessageTicket = new GlobalMessageTicket();
		amountRenderer = new AmountRenderer(globalSectionController.getUser());	

		setupUI();
	}

	/**
	* modified by matthewlam on 2015-01-16
	* Bug Fix #90: Gross Amount cannot be shown in IE9
	*/
	private void setupUI() {
		setTitle("Add / Revise Certificate");
		setPaddings(2);
		setWidth(650);
		setAutoHeight(true);
		setClosable(false);
		setLayout(new FitLayout());
		setId(Add_REVISE_IPA_CERT_ID);

		setupMainPanel();
		setupButtons();
	}

	/**
	* modified by matthewlam on 2015-01-16
	* Bug Fix #90: Gross Amount cannot be shown in IE9
	*/
	private void setupMainPanel() {
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setAutoHeight(true);

		setupCertNoPanel();
		setupAmountPanel();
		setupGSTPanel();
		setupDatePanel();
		setupRemarkPanel();

		setupTextFieldListeners();
		populatePanelwithFigures();

		add(mainPanel);
	}

	private void setupCertNoPanel() {
		Panel certNoPanel = new Panel();
		certNoPanel.setLayout(new TableLayout(7));
		certNoPanel.setFrame(true);
		certNoPanel.setHeight(40);

		Label certificateNoLabel = new Label ("Certificate No.");
		certificateNoLabel.setCtCls("mainCert-table-cell");
		certificateNoLabel.setWidth(100);
		certNoPanel.add(certificateNoLabel);

		certificateNoTextField = new TextField();
		certificateNoTextField.disable();
		certificateNoTextField.setWidth(50);
		certificateNoTextField.setCtCls("mainCert-table-cell");
		certNoPanel.add(certificateNoTextField);

		Label clientCertNoLabel = new Label ("Client Certificate No.");
		clientCertNoLabel.setCtCls("mainCert-table-cell");
		clientCertNoLabel.setWidth(100);
		certNoPanel.add(clientCertNoLabel);

		clientCertNoTextField = new TextField();
		clientCertNoTextField.setWidth(100);
		clientCertNoTextField.setCtCls("mainCert-table-cell");
		certNoPanel.add(clientCertNoTextField);

		Label certificateStatusNoLabel = new Label ("Certificate Status");
		certificateStatusNoLabel.setCtCls("mainCert-table-cell");
		certificateStatusNoLabel.setWidth(100);
		certNoPanel.add(certificateStatusNoLabel);

		certificateStatusTextField = new TextField();
		certificateStatusTextField.disable();
		certificateStatusTextField.setWidth(50);
		certificateStatusTextField.setCtCls("mainCert-table-cell");
		certNoPanel.add(certificateStatusTextField);

		mainPanel.add(certNoPanel);
	}

	/**
	* modified by matthewlam on 2015-01-16
	* Bug Fix #90: Gross Amount cannot be shown in IE9
	*/
	private void setupAmountPanel() {
		Panel amountPanel = new Panel();
		amountPanel.setLayout(new TableLayout(3));
		amountPanel.setFrame(true);
		amountPanel.setAutoHeight(true);
		amountPanel.add(new Label());

		Label ipaHeaderLabel = new Label("IPA");
		ipaHeaderLabel.setCtCls("mainCert-table-cell");
		amountPanel.add(ipaHeaderLabel);

		Label certificateHeaderLabel = new Label("Certificate");
		certificateHeaderLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(certificateHeaderLabel);

		Label mainContractorAmountLabel = new Label("Main Contractor Amount");
		mainContractorAmountLabel.setCtCls("mainCert-table-cell");
		mainContractorAmountLabel.setWidth(200);
		amountPanel.add(mainContractorAmountLabel);

		appliedMainContractorAmountTextField = new TextField(); 
		appliedMainContractorAmountTextField.setWidth(140); 
		appliedMainContractorAmountTextField.setCtCls("mainCert-table-cell");
		appliedMainContractorAmountTextField.setValue("0.00");
		amountPanel.add(appliedMainContractorAmountTextField); 

		certifiedMainContractorAmountTextField = new TextField(); 
		certifiedMainContractorAmountTextField.setWidth(140); 
		certifiedMainContractorAmountTextField.setCtCls("mainCert-table-cell");
		certifiedMainContractorAmountTextField.setValue("0.00");
		amountPanel.add(certifiedMainContractorAmountTextField); 

		Label nscNDSCAmountLabel = new Label("NSC/NDSC Amount"); 
		nscNDSCAmountLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(nscNDSCAmountLabel);

		appliedNSCNDSCAmountTextField = new TextField(); 
		appliedNSCNDSCAmountTextField.setWidth(140); 
		appliedNSCNDSCAmountTextField.setCtCls("mainCert-table-cell"); 
		appliedNSCNDSCAmountTextField.setValue("0.00");
		amountPanel.add(appliedNSCNDSCAmountTextField); 

		certifiedNSCNDSCAmountTextField = new TextField(); 
		certifiedNSCNDSCAmountTextField.setWidth(140); 
		certifiedNSCNDSCAmountTextField.setCtCls("mainCert-table-cell"); 
		certifiedNSCNDSCAmountTextField.setValue("0.00");
		amountPanel.add(certifiedNSCNDSCAmountTextField); 

		Label mosAmountLabel = new Label("MOS Amount"); 
		mosAmountLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(mosAmountLabel);

		appliedMOSAmountTextField = new TextField(); 
		appliedMOSAmountTextField.setWidth(140); 
		appliedMOSAmountTextField.setCtCls("mainCert-table-cell"); 
		appliedMOSAmountTextField.setValue("0.00");
		amountPanel.add(appliedMOSAmountTextField); 

		certifiedMOSAmountTextField = new TextField(); 
		certifiedMOSAmountTextField.setWidth(140); 
		certifiedMOSAmountTextField.setCtCls("mainCert-table-cell"); 
		certifiedMOSAmountTextField.setValue("0.00");
		amountPanel.add(certifiedMOSAmountTextField); 

		Label mainContractorRetentionLabel = new Label("Main Contractor Retention"); 
		mainContractorRetentionLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(mainContractorRetentionLabel);

		appliedMainContractorRetentionTextField = new TextField(); 
		appliedMainContractorRetentionTextField.setWidth(140); 
		appliedMainContractorRetentionTextField.setCtCls("mainCert-table-cell"); 
		appliedMainContractorRetentionTextField.setValue("0.00");
		amountPanel.add(appliedMainContractorRetentionTextField); 

		certifiedMainContractorRetentionTextField = new TextField(); 
		certifiedMainContractorRetentionTextField.setWidth(140); 
		certifiedMainContractorRetentionTextField.setCtCls("mainCert-table-cell");
		certifiedMainContractorRetentionTextField.setValue("0.00");
		amountPanel.add(certifiedMainContractorRetentionTextField); 

		Label mainContractorRetentionReleasedLabel = new Label("Main Contractor Retention Released"); 
		mainContractorRetentionReleasedLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(mainContractorRetentionReleasedLabel);

		appliedMainContractorRetentionReleasedTextField = new TextField(); 
		appliedMainContractorRetentionReleasedTextField.setWidth(140); 
		appliedMainContractorRetentionReleasedTextField.setCtCls("mainCert-table-cell"); 
		appliedMainContractorRetentionReleasedTextField.setValue("0.00");
		amountPanel.add(appliedMainContractorRetentionReleasedTextField); 

		certifiedMainContractorRetentionReleasedTextField = new TextField(); 
		certifiedMainContractorRetentionReleasedTextField.setWidth(140); 
		certifiedMainContractorRetentionReleasedTextField.setCtCls("mainCert-table-cell"); 
		certifiedMainContractorRetentionReleasedTextField.setValue("0.00");
		amountPanel.add(certifiedMainContractorRetentionReleasedTextField); 

		Label retentionforNSCNDSCLabel = new Label("Retention for NSC/NDSC"); 
		retentionforNSCNDSCLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(retentionforNSCNDSCLabel);

		appliedRetentionforNSCNDSCTextField = new TextField(); 
		appliedRetentionforNSCNDSCTextField.setWidth(140); 
		appliedRetentionforNSCNDSCTextField.setCtCls("mainCert-table-cell");
		appliedRetentionforNSCNDSCTextField.setValue("0.00");
		amountPanel.add(appliedRetentionforNSCNDSCTextField); 

		certifiedRetentionforNSCNDSCTextField = new TextField(); 
		certifiedRetentionforNSCNDSCTextField.setWidth(140); 
		certifiedRetentionforNSCNDSCTextField.setCtCls("mainCert-table-cell"); 
		certifiedRetentionforNSCNDSCTextField.setValue("0.00");
		amountPanel.add(certifiedRetentionforNSCNDSCTextField); 

		Label retentionforNSCNDSCReleasedLabel = new Label("Retention for NSC/NDSC Released"); 
		retentionforNSCNDSCReleasedLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(retentionforNSCNDSCReleasedLabel);

		appliedRetentionforNSCNDSCReleasedTextField = new TextField(); 
		appliedRetentionforNSCNDSCReleasedTextField.setWidth(140); 
		appliedRetentionforNSCNDSCReleasedTextField.setCtCls("mainCert-table-cell"); 
		appliedRetentionforNSCNDSCReleasedTextField.setValue("0.00");
		amountPanel.add(appliedRetentionforNSCNDSCReleasedTextField); 

		certifiedRetentionforNSCNDSCReleasedTextField = new TextField(); 
		certifiedRetentionforNSCNDSCReleasedTextField.setWidth(140); 
		certifiedRetentionforNSCNDSCReleasedTextField.setCtCls("mainCert-table-cell"); 
		certifiedRetentionforNSCNDSCReleasedTextField.setValue("0.00");
		amountPanel.add(certifiedRetentionforNSCNDSCReleasedTextField); 

		Label mosRetentionLabel = new Label("MOS Retention"); 
		mosRetentionLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(mosRetentionLabel);

		appliedMOSRetentionTextField = new TextField(); 
		appliedMOSRetentionTextField.setWidth(140); 
		appliedMOSRetentionTextField.setCtCls("mainCert-table-cell");
		appliedMOSRetentionTextField.setValue("0.00");
		amountPanel.add(appliedMOSRetentionTextField); 

		certifiedMOSRetentionTextField = new TextField(); 
		certifiedMOSRetentionTextField.setWidth(140); 
		certifiedMOSRetentionTextField.setCtCls("mainCert-table-cell"); 
		certifiedMOSRetentionTextField.setValue("0.00");
		amountPanel.add(certifiedMOSRetentionTextField); 

		Label mosRetentionReleasedLabel = new Label("MOS Retention Released"); 
		mosRetentionReleasedLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(mosRetentionReleasedLabel);

		appliedMOSRetentionReleasedTextField = new TextField(); 
		appliedMOSRetentionReleasedTextField.setWidth(140); 
		appliedMOSRetentionReleasedTextField.setCtCls("mainCert-table-cell"); 
		appliedMOSRetentionReleasedTextField.setValue("0.00");
		amountPanel.add(appliedMOSRetentionReleasedTextField);

		certifiedMOSRetentionReleasedTextField = new TextField(); 
		certifiedMOSRetentionReleasedTextField.setWidth(140); 
		certifiedMOSRetentionReleasedTextField.setCtCls("mainCert-table-cell"); 
		certifiedMOSRetentionReleasedTextField.setValue("0.00");
		amountPanel.add(certifiedMOSRetentionReleasedTextField); 

		Label contraChargeAmountLabel = new Label("Contra Charge Amount"); 
		contraChargeAmountLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(contraChargeAmountLabel);

		appliedContraChargeAmountTextField = new TextField(); 
		appliedContraChargeAmountTextField.setWidth(140); 
		appliedContraChargeAmountTextField.setCtCls("mainCert-table-cell"); 
		appliedContraChargeAmountTextField.setValue("0.00");
		amountPanel.add(appliedContraChargeAmountTextField); 

		certifiedContraChargeAmountTextField = new TextField(); 
		certifiedContraChargeAmountTextField.setWidth(140); 
		certifiedContraChargeAmountTextField.setCtCls("mainCert-table-cell"); 
		certifiedContraChargeAmountTextField.disable();
		certifiedContraChargeAmountTextField.setValue("0.00");
		amountPanel.add(certifiedContraChargeAmountTextField); 

		Label adjustmentAmountLabel = new Label("Adjustment Amount"); 
		adjustmentAmountLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(adjustmentAmountLabel);

		appliedAdjustmentAmountTextField = new TextField(); 
		appliedAdjustmentAmountTextField.setWidth(140); 
		appliedAdjustmentAmountTextField.setCtCls("mainCert-table-cell"); 
		appliedAdjustmentAmountTextField.setValue("0.00");
		amountPanel.add(appliedAdjustmentAmountTextField); 

		certifiedAdjustmentAmountTextField = new TextField(); 
		certifiedAdjustmentAmountTextField.setWidth(140); 
		certifiedAdjustmentAmountTextField.setCtCls("mainCert-table-cell"); 
		certifiedAdjustmentAmountTextField.setValue("0.00");
		amountPanel.add(certifiedAdjustmentAmountTextField); 

		Label advancePaymentLabel = new Label("Advance Payment"); 
		advancePaymentLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(advancePaymentLabel);

		appliedAdvancePaymentTextField = new TextField(); 
		appliedAdvancePaymentTextField.setWidth(140); 
		appliedAdvancePaymentTextField.setCtCls("mainCert-table-cell"); 
		appliedAdvancePaymentTextField.setValue("0.00");
		amountPanel.add(appliedAdvancePaymentTextField); 

		certifiedAdvancePaymentTextField = new TextField(); 
		certifiedAdvancePaymentTextField.setWidth(140); 
		certifiedAdvancePaymentTextField.setCtCls("mainCert-table-cell"); 
		certifiedAdvancePaymentTextField.setValue("0.00");
		amountPanel.add(certifiedAdvancePaymentTextField); 

		Label cpfAmountLabel = new Label("CPF Amount"); 
		cpfAmountLabel.setCtCls("mainCert-table-cell"); 
		amountPanel.add(cpfAmountLabel);

		appliedCPFAmountTextField = new TextField(); 
		appliedCPFAmountTextField.setWidth(140); 
		appliedCPFAmountTextField.setCtCls("mainCert-table-cell"); 
		appliedCPFAmountTextField.setValue("0.00");
		amountPanel.add(appliedCPFAmountTextField); 

		certifiedCPFAmountTextField = new TextField(); 
		certifiedCPFAmountTextField.setWidth(140); 
		certifiedCPFAmountTextField.setCtCls("mainCert-table-cell"); 
		certifiedCPFAmountTextField.setValue("0.00");
		amountPanel.add(certifiedCPFAmountTextField); 

		Label netAmountLabel = new Label("Net Amount");
		netAmountLabel.setCtCls("mainCert-table-cell");
		amountPanel.add(netAmountLabel);

		appliedNetAmountTextField = new TextField();
		appliedNetAmountTextField.setWidth(140);
		appliedNetAmountTextField.setCtCls("mainCert-table-cell");
		appliedNetAmountTextField.disable();
		appliedNetAmountTextField.setValue("0.00");
		amountPanel.add(appliedNetAmountTextField);

		certifiedNetAmountTextField = new TextField();
		certifiedNetAmountTextField.setWidth(140);
		certifiedNetAmountTextField.setCtCls("mainCert-table-cell");
		certifiedNetAmountTextField.disable();
		certifiedNetAmountTextField.setValue("0.00");
		amountPanel.add(certifiedNetAmountTextField);

		Label grossAmountLabel = new Label("Gross Amount");
		grossAmountLabel.setCtCls("mainCert-table-cell");
		amountPanel.add(grossAmountLabel);

		appliedGrossAmountTextField = new TextField();
		appliedGrossAmountTextField.setWidth(140);
		appliedGrossAmountTextField.setCtCls("mainCert-table-cell");
		appliedGrossAmountTextField.disable();
		appliedGrossAmountTextField.setValue("0.00");
		amountPanel.add(appliedGrossAmountTextField);

		certifiedGrossAmountTextField = new TextField();
		certifiedGrossAmountTextField.setWidth(140);
		certifiedGrossAmountTextField.setCtCls("mainCert-table-cell");
		certifiedGrossAmountTextField.disable();
		certifiedGrossAmountTextField.setValue("0.00");
		amountPanel.add(certifiedGrossAmountTextField);

		mainPanel.add(amountPanel);
	}

	private void setupGSTPanel() {
		Panel gstPanel = new Panel();
		gstPanel.setLayout(new TableLayout(3));
		gstPanel.setFrame(true);
		gstPanel.setHeight(65);

		Label gstReceivableLabel = new Label("GST Amount(GST Receivable)");
		gstReceivableLabel.setCtCls("mainCert-table-cell");
		gstReceivableLabel.setWidth(200);
		gstPanel.add(gstReceivableLabel);

		Label emptyLabel = new Label();
		emptyLabel.setWidth(140);
		gstPanel.add(emptyLabel);

		gstReceivableTextField = new TextField();
		gstReceivableTextField.setWidth(140);
		gstReceivableTextField.setCtCls("mainCert-table-cell");
		gstReceivableTextField.setValue("0.00");
		gstPanel.add(gstReceivableTextField);

		Label gstPayableLabel = new Label("GST for Contra Charge(GST Payable)");
		gstPayableLabel.setCtCls("mainCert-table-cell");  
		gstPanel.add(gstPayableLabel);

		gstPanel.add(new Label());

		gstPayableTextField = new TextField();
		gstPayableTextField.setWidth(140);
		gstPayableTextField.setCtCls("mainCert-table-cell");
		gstPayableTextField.setValue("0.00");
		gstPanel.add(gstPayableTextField);

		mainPanel.add(gstPanel);
	}

	private void setupDatePanel() {
		Panel datePanel = new Panel();
		datePanel.setLayout(new TableLayout(6));
		datePanel.setAutoHeight(true);
		datePanel.setFrame(true);

		Label ipaDateLabel = new Label("IPA Submission Date");  
		ipaDateLabel.setCtCls("mainCert-table-cell");
		datePanel.add(ipaDateLabel);

		ipaSubmissionDateField = new DateField();
		ipaSubmissionDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		ipaSubmissionDateField.setWidth(80);
		ipaSubmissionDateField.setCtCls("mainCert-table-cell");
		ipaSubmissionDateField.setFormat("d/m/y");
		datePanel.add(ipaSubmissionDateField);

		Label certIssueDateLabel = new Label("Certificate Issue Date");  
		certIssueDateLabel.setCtCls("mainCert-table-cell");  
		datePanel.add(certIssueDateLabel);

		certIssueDateField = new DateField();
		certIssueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		certIssueDateField.setWidth(80);
		certIssueDateField.setCtCls("mainCert-table-cell");
		certIssueDateField.setFormat("d/m/y");
		datePanel.add(certIssueDateField);

		datePanel.add(new Label());
		datePanel.add(new Label());
		
		Label certAsAtDateLabel = new Label("Certificate As At Date"); 
		certAsAtDateLabel.setCtCls("mainCert-table-cell");
		certAsAtDateLabel.addClass("label-padded-left");
		datePanel.add(certAsAtDateLabel);

		certAsAtDateField = new DateField();
		certAsAtDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		certAsAtDateField.setWidth(80);
		certAsAtDateField.setCtCls("mainCert-table-cell");
		certAsAtDateField.setFormat("d/m/y");
		datePanel.add(certAsAtDateField);

		Label certDueDateLabel = new Label("Certificate Due Date");  
		certDueDateLabel.setCtCls("mainCert-table-cell");
		certDueDateLabel.addClass("label-padded-left");
		datePanel.add(certDueDateLabel);

		certDueDateField = new DateField();
		certDueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		certDueDateField.setWidth(80);
		certDueDateField.setCtCls("mainCert-table-cell");
		certDueDateField.setFormat("d/m/y");
		datePanel.add(certDueDateField);

		mainPanel.add(datePanel);
	}

	private void setupRemarkPanel() {
		Panel remarkPanel = new Panel();
		remarkPanel.setLayout(new TableLayout(2));
		remarkPanel.setFrame(true);
		remarkPanel.setHeight(40);

		Label remarkLabel = new Label ("Remark");
		remarkLabel.setCtCls("mainCert-table-cell");
		remarkLabel.setWidth(100);
		remarkPanel.add(remarkLabel);

		remarkTextField = new TextField();
		remarkTextField.setWidth(200);
		remarkTextField.setCtCls("mainCert-table-cell");
		remarkPanel.add(remarkTextField);

		mainPanel.add(remarkPanel);
	}

	private void setupTextFieldListeners() {
		TextFieldListenerAdapter listener = new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newValue, Object oldValue) {
				Double value = stringToDouble(newValue.toString());
				if (field == appliedMainContractorAmountTextField)
					mainContractCertificate.setAppliedMainContractorAmount(value);
				else if (field == appliedNSCNDSCAmountTextField)
					mainContractCertificate.setAppliedNSCNDSCAmount(value);
				else if (field == appliedMOSAmountTextField)
					mainContractCertificate.setAppliedMOSAmount(value);
				else if (field == appliedMainContractorRetentionTextField)
					mainContractCertificate.setAppliedMainContractorRetention(value);
				else if (field == appliedMainContractorRetentionReleasedTextField)
					mainContractCertificate.setAppliedMainContractorRetentionReleased(value);
				else if (field == appliedRetentionforNSCNDSCTextField)
					mainContractCertificate.setAppliedRetentionforNSCNDSC(value);
				else if (field == appliedRetentionforNSCNDSCReleasedTextField)
					mainContractCertificate.setAppliedRetentionforNSCNDSCReleased(value);
				else if (field == appliedMOSRetentionTextField)
					mainContractCertificate.setAppliedMOSRetention(value);
				else if (field == appliedMOSRetentionReleasedTextField)
					mainContractCertificate.setAppliedMOSRetentionReleased(value);
				else if (field == appliedContraChargeAmountTextField)
					mainContractCertificate.setAppliedContraChargeAmount(value);
				else if (field == appliedAdjustmentAmountTextField)
					mainContractCertificate.setAppliedAdjustmentAmount(value);
				else if (field == appliedAdvancePaymentTextField)
					mainContractCertificate.setAppliedAdvancePayment(value);
				else if (field == appliedCPFAmountTextField)
					mainContractCertificate.setAppliedCPFAmount(value);
				else if (field == certifiedMainContractorAmountTextField)
					mainContractCertificate.setCertifiedMainContractorAmount(value);
				else if (field == certifiedNSCNDSCAmountTextField)
					mainContractCertificate.setCertifiedNSCNDSCAmount(value);
				else if (field == certifiedMOSAmountTextField)
					mainContractCertificate.setCertifiedMOSAmount(value);
				else if (field == certifiedMainContractorRetentionTextField)
					mainContractCertificate.setCertifiedMainContractorRetention(value);
				else if (field == certifiedMainContractorRetentionReleasedTextField)
					mainContractCertificate.setCertifiedMainContractorRetentionReleased(value);
				else if (field == certifiedRetentionforNSCNDSCTextField)
					mainContractCertificate.setCertifiedRetentionforNSCNDSC(value);
				else if (field == certifiedRetentionforNSCNDSCReleasedTextField)
					mainContractCertificate.setCertifiedRetentionforNSCNDSCReleased(value);
				else if (field == certifiedMOSRetentionTextField)
					mainContractCertificate.setCertifiedMOSRetention(value);
				else if (field == certifiedMOSRetentionReleasedTextField)
					mainContractCertificate.setCertifiedMOSRetentionReleased(value);
				else if (field == certifiedContraChargeAmountTextField)
					mainContractCertificate.setCertifiedContraChargeAmount(value);
				else if (field == certifiedAdjustmentAmountTextField)
					mainContractCertificate.setCertifiedAdjustmentAmount(value);
				else if (field == certifiedAdvancePaymentTextField)
					mainContractCertificate.setCertifiedAdvancePayment(value);
				else if (field == certifiedCPFAmountTextField)
					mainContractCertificate.setCertifiedCPFAmount(value);
				else if (field == gstReceivableTextField)
					mainContractCertificate.setGstReceivable(value);
				else if (field == gstPayableTextField)
					mainContractCertificate.setGstPayable(value);

				totalAmountCalcuation();
				field.setValue(amountRenderer.render(value.toString()));
			}
		};

		appliedMainContractorAmountTextField.addListener(listener);
		appliedNSCNDSCAmountTextField.addListener(listener);
		appliedMOSAmountTextField.addListener(listener);
		appliedMainContractorRetentionTextField.addListener(listener);
		appliedMainContractorRetentionReleasedTextField.addListener(listener);
		appliedRetentionforNSCNDSCTextField.addListener(listener);
		appliedRetentionforNSCNDSCReleasedTextField.addListener(listener);
		appliedMOSRetentionTextField.addListener(listener);
		appliedMOSRetentionReleasedTextField.addListener(listener);
		appliedContraChargeAmountTextField.addListener(listener);
		appliedAdjustmentAmountTextField.addListener(listener);
		appliedAdvancePaymentTextField.addListener(listener);
		appliedCPFAmountTextField.addListener(listener);

		certifiedMainContractorAmountTextField.addListener(listener);
		certifiedNSCNDSCAmountTextField.addListener(listener);
		certifiedMOSAmountTextField.addListener(listener);
		certifiedMainContractorRetentionTextField.addListener(listener);
		certifiedMainContractorRetentionReleasedTextField.addListener(listener);
		certifiedRetentionforNSCNDSCTextField.addListener(listener);
		certifiedRetentionforNSCNDSCReleasedTextField.addListener(listener);
		certifiedMOSRetentionTextField.addListener(listener);
		certifiedMOSRetentionReleasedTextField.addListener(listener);
		certifiedContraChargeAmountTextField.addListener(listener);
		certifiedAdjustmentAmountTextField.addListener(listener);
		certifiedAdvancePaymentTextField.addListener(listener);
		certifiedCPFAmountTextField.addListener(listener);

		gstReceivableTextField.addListener(listener);
		gstPayableTextField.addListener(listener);
	}

	private void setupButtons() {
		saveButton = new Button("Save");	
		if (addCertFlag)
			saveButton = new Button("Add");

		saveButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				saveButton.disable();

				if (ipaSubmissionDateField.getValue()==null || "".equalsIgnoreCase(ipaSubmissionDateField.getText().trim())) {
					MessageBox.alert("Please fill in the IPA Submission Date.");
				} else {
					UIUtil.maskPanelById(getId(), "Loading", true);
					if (addCertFlag)
						addMainContractCertificate();
					else
						editMainContractCertficate();
				}
			}
		});

		saveButton.setVisible(false);
		securitySetup();
		addButton(saveButton);

		//Close Button
		Button cancelButton = new Button("Cancel");
		cancelButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalSectionController.closeCurrentWindow();
			}
		});

		addButton(cancelButton);
	}

	private void securitySetup() {
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(),
				RoleSecurityFunctions.F010403_ADD_REVISIE_IPA_CERT, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;

				if (accessRightsList.contains("WRITE"))
					saveButton.setVisible(true);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}

	private Double stringToDouble(String inStr) {
		Double result;
		if (inStr == null || "".equals(inStr.trim())) {
			result = new Double(0.0);
		} else {
			try{
				result = new Double(inStr.trim().replace(",", ""));
			} catch(NumberFormatException e) {
				result = new Double(0.0);
			}
		}
		return result;
	}

	private void maskAfterConfirmed() {
		certificateNoTextField.disable();
		clientCertNoTextField.disable();
		certifiedMainContractorAmountTextField.disable();
		certifiedNSCNDSCAmountTextField.disable();
		certifiedMOSAmountTextField.disable();
		certifiedMainContractorRetentionTextField.disable();
		certifiedMainContractorRetentionReleasedTextField.disable();
		certifiedRetentionforNSCNDSCTextField.disable();
		certifiedRetentionforNSCNDSCReleasedTextField.disable();
		certifiedMOSRetentionTextField.disable();
		certifiedMOSRetentionReleasedTextField.disable();
		certifiedContraChargeAmountTextField.disable();
		certifiedAdjustmentAmountTextField.disable();
		certifiedAdvancePaymentTextField.disable();
		certifiedCPFAmountTextField.disable();
		gstReceivableTextField.disable();
		gstPayableTextField.disable();
		certificateStatusTextField.disable();
		certIssueDateField.disable();
		certAsAtDateField.disable();
		certifiedNetAmountTextField.disable();
		certifiedGrossAmountTextField.disable();
		certificateStatusTextField.disable();
		certDueDateField.disable();
		remarkTextField.disable();
	}
	
	private void maskAfterSentOutIPA() {
		appliedMainContractorAmountTextField.disable();
		appliedNSCNDSCAmountTextField.disable();
		appliedMOSAmountTextField.disable();
		appliedMainContractorRetentionTextField.disable();
		appliedMainContractorRetentionReleasedTextField.disable();
		appliedRetentionforNSCNDSCTextField.disable();
		appliedRetentionforNSCNDSCReleasedTextField.disable();
		appliedMOSRetentionTextField.disable();
		appliedMOSRetentionReleasedTextField.disable();
		appliedContraChargeAmountTextField.disable();
		appliedAdjustmentAmountTextField.disable();
		appliedAdvancePaymentTextField.disable();
		appliedCPFAmountTextField.disable();
		appliedNetAmountTextField.disable();
		appliedGrossAmountTextField.disable();
		ipaSubmissionDateField.disable();
	}
	
	private void totalAmountCalcuation() {
		appliedGrossAmountTextField.setValue(amountRenderer.render(mainContractCertificate.calculateAppliedGrossAmount().toString()));
		certifiedGrossAmountTextField.setValue(amountRenderer.render(mainContractCertificate.calculateCertifiedGrossAmount().toString()));
		appliedNetAmountTextField.setValue(amountRenderer.render(mainContractCertificate.calculateAppliedNetAmount().toString()));
		certifiedNetAmountTextField.setValue(amountRenderer.render(mainContractCertificate.calculateCertifiedNetAmount().toString()));
	}
	
	private void populateTextFields(MainContractCertificate returnMainCert) {
		certificateNoTextField.setValue(mainContractCertificate.getCertificateNumber()==null?"0":mainContractCertificate.getCertificateNumber().toString());
		clientCertNoTextField.setValue(mainContractCertificate.getClientCertNo()==null?"":mainContractCertificate.getClientCertNo());
		certificateStatusTextField.setValue(mainContractCertificate.getCertificateStatus());
		appliedMainContractorAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMainContractorAmount()==null?"0":mainContractCertificate.getAppliedMainContractorAmount().toString()));
		appliedNSCNDSCAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedNSCNDSCAmount()==null?"0":mainContractCertificate.getAppliedNSCNDSCAmount().toString()));
		appliedMOSAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMOSAmount()==null?"0":mainContractCertificate.getAppliedMOSAmount().toString()));
		appliedMainContractorRetentionTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMainContractorRetention()==null?"0":mainContractCertificate.getAppliedMainContractorRetention().toString()));
		appliedMainContractorRetentionReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMainContractorRetentionReleased()==null?"0":mainContractCertificate.getAppliedMainContractorRetentionReleased().toString()));
		appliedRetentionforNSCNDSCTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedRetentionforNSCNDSC()==null?"0":mainContractCertificate.getAppliedRetentionforNSCNDSC().toString()));
		appliedRetentionforNSCNDSCReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedRetentionforNSCNDSCReleased()==null?"0":mainContractCertificate.getAppliedRetentionforNSCNDSCReleased().toString()));
		appliedMOSRetentionTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMOSRetention()==null?"0":mainContractCertificate.getAppliedMOSRetention().toString()));
		appliedMOSRetentionReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedMOSRetentionReleased()==null?"0":mainContractCertificate.getAppliedMOSRetentionReleased().toString()));
		appliedContraChargeAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedContraChargeAmount()==null?"0":mainContractCertificate.getAppliedContraChargeAmount().toString()));
		appliedAdjustmentAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedAdjustmentAmount()==null?"0":mainContractCertificate.getAppliedAdjustmentAmount().toString()));
		appliedAdvancePaymentTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedAdvancePayment()==null?"0":mainContractCertificate.getAppliedAdvancePayment().toString()));
		appliedCPFAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getAppliedCPFAmount()==null?"0":mainContractCertificate.getAppliedCPFAmount().toString()));
		certifiedMainContractorAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMainContractorAmount()==null?"0":mainContractCertificate.getCertifiedMainContractorAmount().toString()));
		certifiedNSCNDSCAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedNSCNDSCAmount()==null?"0":mainContractCertificate.getCertifiedNSCNDSCAmount().toString()));
		certifiedMOSAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMOSAmount()==null?"0":mainContractCertificate.getCertifiedMOSAmount().toString()));
		certifiedMainContractorRetentionTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMainContractorRetention()==null?"0":mainContractCertificate.getCertifiedMainContractorRetention().toString()));
		certifiedMainContractorRetentionReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMainContractorRetentionReleased()==null?"0":mainContractCertificate.getCertifiedMainContractorRetentionReleased().toString()));
		certifiedRetentionforNSCNDSCTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedRetentionforNSCNDSC()==null?"0":mainContractCertificate.getCertifiedRetentionforNSCNDSC().toString()));
		certifiedRetentionforNSCNDSCReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased()==null?"0":mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased().toString()));
		certifiedMOSRetentionTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMOSRetention()==null?"0":mainContractCertificate.getCertifiedMOSRetention().toString()));
		certifiedMOSRetentionReleasedTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedMOSRetentionReleased()==null?"0":mainContractCertificate.getCertifiedMOSRetentionReleased().toString()));
		certifiedContraChargeAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedContraChargeAmount()==null?"0":mainContractCertificate.getCertifiedContraChargeAmount().toString()));
		certifiedAdjustmentAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedAdjustmentAmount()==null?"0":mainContractCertificate.getCertifiedAdjustmentAmount().toString()));
		certifiedAdvancePaymentTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedAdvancePayment()==null?"0":mainContractCertificate.getCertifiedAdvancePayment().toString()));
		certifiedCPFAmountTextField.setValue(amountRenderer.render(mainContractCertificate.getCertifiedCPFAmount()==null?"0":mainContractCertificate.getCertifiedCPFAmount().toString()));
		gstReceivableTextField.setValue(amountRenderer.render(mainContractCertificate.getGstReceivable()==null?"0":mainContractCertificate.getGstReceivable().toString()));
		gstPayableTextField.setValue(amountRenderer.render(mainContractCertificate.getGstPayable()==null?"0":mainContractCertificate.getGstPayable().toString()));

		if (mainContractCertificate.getIpaSubmissionDate()!= null)
			ipaSubmissionDateField.setValue(mainContractCertificate.getIpaSubmissionDate());
		if (mainContractCertificate.getCertIssueDate()!= null)
			certIssueDateField.setValue(mainContractCertificate.getCertIssueDate());
		if (mainContractCertificate.getCertAsAtDate()!= null)
			certAsAtDateField.setValue(mainContractCertificate.getCertAsAtDate());
		if (mainContractCertificate.getCertDueDate()!= null)
			certDueDateField.setValue(mainContractCertificate.getCertDueDate());
		remarkTextField.setValue(mainContractCertificate.getRemark()==null?"":mainContractCertificate.getRemark());
	}
	
	private void populateMainContractCertificateFromTextFields() {
		mainContractCertificate.setClientCertNo(clientCertNoTextField.getValueAsString());
		
		mainContractCertificate.setAppliedMainContractorAmount(stringToDouble(appliedMainContractorAmountTextField.getText()));
		mainContractCertificate.setAppliedNSCNDSCAmount(stringToDouble(appliedNSCNDSCAmountTextField.getText()));
		mainContractCertificate.setAppliedMOSAmount(stringToDouble(appliedMOSAmountTextField.getText()));
		mainContractCertificate.setAppliedMainContractorRetention(stringToDouble(appliedMainContractorRetentionTextField.getText()));
		mainContractCertificate.setAppliedMainContractorRetentionReleased(stringToDouble(appliedMainContractorRetentionReleasedTextField.getText()));
		mainContractCertificate.setAppliedRetentionforNSCNDSC(stringToDouble(appliedRetentionforNSCNDSCTextField.getText()));
		mainContractCertificate.setAppliedRetentionforNSCNDSCReleased(stringToDouble(appliedRetentionforNSCNDSCReleasedTextField.getText()));
		mainContractCertificate.setAppliedMOSRetention(stringToDouble(appliedMOSRetentionTextField.getText()));
		mainContractCertificate.setAppliedMOSRetentionReleased(stringToDouble(appliedMOSRetentionReleasedTextField.getText()));
		mainContractCertificate.setAppliedContraChargeAmount(stringToDouble(appliedContraChargeAmountTextField.getText()));
		mainContractCertificate.setAppliedAdjustmentAmount(stringToDouble(appliedAdjustmentAmountTextField.getText()));
		mainContractCertificate.setAppliedAdvancePayment(stringToDouble(appliedAdvancePaymentTextField.getText()));
		mainContractCertificate.setAppliedCPFAmount(stringToDouble(appliedCPFAmountTextField.getText()));
		
		mainContractCertificate.setCertifiedMainContractorAmount(stringToDouble(certifiedMainContractorAmountTextField.getText()));
		mainContractCertificate.setCertifiedNSCNDSCAmount(stringToDouble(certifiedNSCNDSCAmountTextField.getText()));
		mainContractCertificate.setCertifiedMOSAmount(stringToDouble(certifiedMOSAmountTextField.getText()));
		mainContractCertificate.setCertifiedMainContractorRetention(stringToDouble(certifiedMainContractorRetentionTextField.getText()));
		mainContractCertificate.setCertifiedMainContractorRetentionReleased(stringToDouble(certifiedMainContractorRetentionReleasedTextField.getText()));
		mainContractCertificate.setCertifiedRetentionforNSCNDSC(stringToDouble(certifiedRetentionforNSCNDSCTextField.getText()));
		mainContractCertificate.setCertifiedRetentionforNSCNDSCReleased(stringToDouble(certifiedRetentionforNSCNDSCReleasedTextField.getText()));
		mainContractCertificate.setCertifiedMOSRetention(stringToDouble(certifiedMOSRetentionTextField.getText()));
		mainContractCertificate.setCertifiedMOSRetentionReleased(stringToDouble(certifiedMOSRetentionReleasedTextField.getText()));
		mainContractCertificate.setCertifiedContraChargeAmount(stringToDouble(certifiedContraChargeAmountTextField.getText()));
		mainContractCertificate.setCertifiedAdjustmentAmount(stringToDouble(certifiedAdjustmentAmountTextField.getText()));
		mainContractCertificate.setCertifiedAdvancePayment(stringToDouble(certifiedAdvancePaymentTextField.getText()));
		mainContractCertificate.setCertifiedCPFAmount(stringToDouble(certifiedCPFAmountTextField.getText()));
		
		mainContractCertificate.setGstReceivable(stringToDouble(gstReceivableTextField.getText()));
		mainContractCertificate.setGstPayable(stringToDouble(gstPayableTextField.getText()));

		mainContractCertificate.setIpaSubmissionDate(ipaSubmissionDateField.getValue());
		mainContractCertificate.setCertIssueDate(certIssueDateField.getValue());
		mainContractCertificate.setCertAsAtDate(certAsAtDateField.getValue());
		mainContractCertificate.setCertDueDate(certDueDateField.getValue());
		
		mainContractCertificate.setRemark(remarkTextField.getValueAsString());
	}
	
	private void poulateNewMainContractCertificate(MainContractCertificate returnMainCert) {		
		mainContractCertificate.setAppliedMainContractorAmount(returnMainCert.getAppliedMainContractorAmount());
		mainContractCertificate.setAppliedNSCNDSCAmount(returnMainCert.getAppliedNSCNDSCAmount());
		mainContractCertificate.setAppliedMOSAmount(returnMainCert.getAppliedMOSAmount());
		mainContractCertificate.setAppliedMainContractorRetention(returnMainCert.getAppliedMainContractorRetention());
		mainContractCertificate.setAppliedMainContractorRetentionReleased(returnMainCert.getAppliedMainContractorRetentionReleased());
		mainContractCertificate.setAppliedRetentionforNSCNDSC(returnMainCert.getAppliedRetentionforNSCNDSC());
		mainContractCertificate.setAppliedRetentionforNSCNDSCReleased(returnMainCert.getAppliedRetentionforNSCNDSCReleased());
		mainContractCertificate.setAppliedMOSRetention(returnMainCert.getAppliedMOSRetention());
		mainContractCertificate.setAppliedMOSRetentionReleased(returnMainCert.getAppliedMOSRetentionReleased());
		mainContractCertificate.setAppliedContraChargeAmount(returnMainCert.getAppliedContraChargeAmount());
		mainContractCertificate.setAppliedAdjustmentAmount(returnMainCert.getAppliedAdjustmentAmount());
		mainContractCertificate.setAppliedAdvancePayment(returnMainCert.getAppliedAdvancePayment());
		mainContractCertificate.setAppliedCPFAmount(returnMainCert.getAppliedCPFAmount());
		
		mainContractCertificate.setCertifiedMainContractorAmount(returnMainCert.getCertifiedMainContractorAmount());
		mainContractCertificate.setCertifiedNSCNDSCAmount(returnMainCert.getCertifiedNSCNDSCAmount());
		mainContractCertificate.setCertifiedMOSAmount(returnMainCert.getCertifiedMOSAmount());
		mainContractCertificate.setCertifiedMainContractorRetention(returnMainCert.getCertifiedMainContractorRetention());
		mainContractCertificate.setCertifiedMainContractorRetentionReleased(returnMainCert.getCertifiedMainContractorRetentionReleased());
		mainContractCertificate.setCertifiedRetentionforNSCNDSC(returnMainCert.getCertifiedRetentionforNSCNDSC());
		mainContractCertificate.setCertifiedRetentionforNSCNDSCReleased(returnMainCert.getCertifiedRetentionforNSCNDSCReleased());
		mainContractCertificate.setCertifiedMOSRetention(returnMainCert.getCertifiedMOSRetention());
		mainContractCertificate.setCertifiedMOSRetentionReleased(returnMainCert.getCertifiedMOSRetentionReleased());
		mainContractCertificate.setCertifiedContraChargeAmount(returnMainCert.getCertifiedContraChargeAmount());
		mainContractCertificate.setCertifiedAdjustmentAmount(returnMainCert.getCertifiedAdjustmentAmount());
		mainContractCertificate.setCertifiedAdvancePayment(returnMainCert.getCertifiedAdvancePayment());
		mainContractCertificate.setCertifiedCPFAmount(returnMainCert.getCertifiedCPFAmount());
		
		mainContractCertificate.setGstPayable(returnMainCert.getGstPayable());
		mainContractCertificate.setGstReceivable(returnMainCert.getGstReceivable());
	}
	
	private void populatePanelwithFigures() {		
		Integer certNo = currentCertificateNumber;
		if (addCertFlag)
			certNo = previousCertificateNumber;
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),certNo, new AsyncCallback<MainContractCertificate>() {
			public void onFailure(Throwable e1) {
				UIUtil.alert("Failed to obtain Main Contract Certificate. \n" +e1);
				
			}
			public void onSuccess(MainContractCertificate returnMainCert) {
				mainContractCertificate = new MainContractCertificate();
				mainContractCertificate.setJobNo(globalSectionController.getJob().getJobNumber());
				mainContractCertificate.setCertificateNumber(currentCertificateNumber);
				mainContractCertificate.setCertificateStatus("100");
				
				if (returnMainCert!=null) {
					if (addCertFlag)
						poulateNewMainContractCertificate(returnMainCert);	
					else
						mainContractCertificate = returnMainCert;
				}
				
				populateTextFields(mainContractCertificate);
				totalAmountCalcuation();
				
				if (!addCertFlag) {
					if (!"100".equals(mainContractCertificate.getCertificateStatus()))
						maskAfterSentOutIPA();
					if ((new Integer(mainContractCertificate.getCertificateStatus())>=150))
						maskAfterConfirmed();
				}
			}
		});
	}
	
	private MainContractCertificate setContraChargeListToNewMainCert(MainContractCertificate mainCert, List<MainCertificateContraCharge> contraChargeList) {
		List<MainCertificateContraCharge> newContraChargeList = new ArrayList<MainCertificateContraCharge>();
		MainContractCertificate newMainCert = mainCert;
		for(MainCertificateContraCharge curContraCharge: lastContraChargeList) {
			MainCertificateContraCharge newContraCharge = new MainCertificateContraCharge();
			newContraCharge.setPostAmount(curContraCharge.getCurrentAmount());
			newContraCharge.setObjectCode(curContraCharge.getObjectCode());
			newContraCharge.setCurrentAmount(curContraCharge.getCurrentAmount());
			newContraCharge.setSubsidiary(curContraCharge.getSubsidiary());
			newContraCharge.setMainCertificate(mainContractCertificate);
			newContraCharge.setCreatedDate(new Date());
			newContraCharge.setCreatedUser(globalSectionController.getUser().getUsername());
			newContraCharge.setLastModifiedDate(new Date());
			newContraCharge.setLastModifiedUser(globalSectionController.getUser().getUsername());
			
			newContraChargeList.add(newContraCharge);
		}
		UIUtil.maskMainPanel();
		globalSectionController.getMainContractCertificateRepository().deleteMainCertContraCharge(newMainCert, new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.throwException(e);
			}

			@Override
			public void onSuccess(Integer result) {
				UIUtil.unmaskMainPanel();
			}
			
		});
		for(MainCertificateContraCharge mainCertificateContraCharge : newContraChargeList){
			mainCertificateContraCharge.setMainCertificate(newMainCert);
		}
		newMainCert.setCertifiedContraChargeAmount(this.mainCertificateGridPanel.getLatestMainCert().getCertifiedContraChargeAmount());
		return newMainCert;
	}
	
	private void addAndShowRetentionReleaseInputWindow(final MainContractCertificate mainContractCertificate) {
		SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMainContractCertificateRepository().addMainContractCert(mainContractCertificate,new AsyncCallback<String>() {
				public void onFailure(Throwable e1) {
					MessageBox.alert("Failed to add Main Contract Certificate. \n"+e1.getMessage());
					UIUtil.unmaskPanelById(getId());
				}

				public void onSuccess(String returnMsg) {
					if (returnMsg==null) {
						globalSectionController.showRetentionReleaseInputWindow(mainContractCertificate,AddRevisieIPACert.this);
						saveButton.enable();
						UIUtil.unmaskPanelById(getId());
					}else {
						MessageBox.alert(returnMsg);
						UIUtil.unmaskPanelById(getId());
					}
				}
			});
	}
	
	private void addMainContractCertificate() {
		populateMainContractCertificateFromTextFields();
		
		//set systematic info
		mainContractCertificate.setCreatedUser(globalSectionController.getUser().getUsername());
		mainContractCertificate.setCertificateStatus("100");
		mainContractCertificate.setCertStatusChangeDate(new Date());
		
		if (currentCertificateNumber==1) {
			addAndShowRetentionReleaseInputWindow(mainContractCertificate);
		}else{
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMainContractCertificateRepository().getMainCertCertContraChargeList(AddRevisieIPACert.this.mainCertificateGridPanel.getLatestMainCert(), new AsyncCallback<List<MainCertificateContraCharge>>() {
				public void onFailure(Throwable arg0) {
					UIUtil.unmaskPanelById(getId());
				}

				public void onSuccess(List<MainCertificateContraCharge> resultList) {
					lastContraChargeList = resultList;
					mainContractCertificate = setContraChargeListToNewMainCert(mainContractCertificate, resultList);
					addAndShowRetentionReleaseInputWindow(mainContractCertificate);
				}

			});
		}
	}
	
	private void editMainContractCertficate() {
		mainContractCertificate.setClientCertNo("".equalsIgnoreCase(clientCertNoTextField.getText())?null:clientCertNoTextField.getText().trim());
		
		mainContractCertificate.setIpaSubmissionDate("".equalsIgnoreCase(ipaSubmissionDateField.getText().trim())? null:ipaSubmissionDateField.getValue());
		mainContractCertificate.setCertIssueDate("".equalsIgnoreCase(certIssueDateField.getText().trim())? null:certIssueDateField.getValue());
		mainContractCertificate.setCertAsAtDate("".equalsIgnoreCase(certAsAtDateField.getText().trim())? null:certAsAtDateField.getValue());
		mainContractCertificate.setCertDueDate("".equalsIgnoreCase(certDueDateField.getText().trim())? null:certDueDateField.getValue());
		mainContractCertificate.setLastModifiedUser(globalSectionController.getUser().getUsername());
		
		mainContractCertificate.setRemark("".equalsIgnoreCase(remarkTextField.getText())?null:remarkTextField.getText().trim());
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().updateMainContractCert(mainContractCertificate, new AsyncCallback<String>() {
			public void onFailure(Throwable e1) {
				UIUtil.alert("Failed to update Main Contract Certificate. \n"+e1);
				UIUtil.unmaskPanelById(getId());
			}

			public void onSuccess(String returnMsg) {
				if (returnMsg==null) {
					MessageBox.alert("Main Contract Certificate has been updated successfully.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
					UIUtil.unmaskPanelById(getId());
				} else {
					globalSectionController.showRetentionReleaseInputWindow(mainContractCertificate, AddRevisieIPACert.this);
					saveButton.enable();
					UIUtil.unmaskPanelById(getId());
				}
				globalSectionController.closeCurrentWindow();
			}
		});
	}

	public void closeWindows() {
		if (addCertFlag)
			MessageBox.alert("Main Contract Certificate Added");
		else
			MessageBox.alert("Main Contract Certificate Saved");
		UIUtil.unmaskPanelById(getId());
		globalSectionController.closeCurrentWindow();
		globalSectionController.getTreeSectionController().populateMainCertificate();;
		UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
	}
}
