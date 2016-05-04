package com.gammon.qs.client.ui.window.detailSection;

import java.util.Date;


import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.MainContractCertificateRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * @author xethhung
 * created on 10 Aug, 2015
 * **/
public class PreviewMainContractCertViewWindow extends Window {
	//Remote service
	private MainContractCertificateRepositoryRemoteAsync mainContractCertificateRepository;
	
	//UI
	private GlobalSectionController globalSectionController;
	
	private Panel mainPanel;
	
	private Label jobNumberTextField;
	private Label clientCertNoTextField;
	private Label crtificateNoTextField;
	private Label certificateStatusTextField;
	
	private Label mainContractorAmountIPATextField;
	private Label mainContractorAmountCertificateTextField;
	private Label NSCNDSCAmountIPATextField;
	private Label NSCNDSCAmountCertificateTextField;
	private Label MOSAmountIPATextField;
	private Label MOSAmountCertifcateTextField;
	private Label mainContractorRetentionReleaseIPATextField;
	private Label mainContractorRetentionReleaseCertificateTextField;
	private Label retentionForNSCNDSCReleaseIPALabel;
	private Label retentionForNSCNDSCReleaseCertifciateLabel;
	private Label MOSRetentionReleaseIPALabel;
	private Label MOSRetentionReleaseCertificateLabel;
	private Label adjustmentAmountIPALabel;
	private Label adjustmentAmountCertificateLabel;
	private Label advancePaymentIPALabel;
	private Label advancePaymentCertificateLabel;
	private Label CPFAmountIPALabel;
	private Label CPFAmountCertificateLabel;
	private Label mainContractorRetentionIPALabel;
	private Label mainContractorRetentionCertificateLabel;
	private Label retentionForNSCNDSCIPALabel;
	private Label retentionForNSCNDSCCertificateLabel;
	private Label MOSRetentionIPALabel;
	private Label MOSRetentionCertificateLabel;
	private Label contraChargeAmountIPALabel;
	private Label contraChargeAmountCertificateLabel;
	private Label netAmountIPALabel;
	private Label netAmountCertificateLabel;
	private Label grossAmountIPALabel;
	private Label grossAmountCertificateLabel;
	private Label GSTAmount_GSTRceivableLabel;
	private Label GSTForContraCharge_GSTPayableLabel;
	private Label IPASubmissionDateLabel;
	private Label actualReceiptDateLabel;
	private Label certificateIssueDateLabel;
	private Label certificateAsAtDateLabel;
	private Label certificateDueDateLabel;
	private Label remarkLabel;

	//getParameter
	private String jobNumber;
	private Integer mainCertNo;
	
	private final NumberFormat format = NumberFormat.getFormat("#,##0.00");

	public PreviewMainContractCertViewWindow( final GlobalSectionController globalSectionController){
		super();
		this.globalSectionController = globalSectionController;
	
		mainContractCertificateRepository = globalSectionController.getMainContractCertificateRepository();
		
		setupUI();
	}
	
	private void setupUI(){
		setTitle("Main Contract Certificate");
		setPaddings(2);
		setWidth(700);
		setHeight(700);
		setClosable(true);
		setModal(true);
		setIconCls("certificate-icon");
		setLayout(new RowLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId("popupWindow");

		
		Panel upperPanel = new Panel();
		//upperPanel.setBorder(true);
		upperPanel.setFrame(false);
		upperPanel.setPaddings(2);
		upperPanel.setHeight(50);
		upperPanel.setLayout(new TableLayout(6));
		
		///
		Label jobNumberLabel = new Label("Job Number:");	
		jobNumberLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(jobNumberLabel);
		

		jobNumberTextField =  new Label();		
		jobNumberTextField.setCtCls("cert-table-cell");
		upperPanel.add(jobNumberTextField);
		
		upperPanel.add(new Label());
		upperPanel.add(new Label());
		
		upperPanel.add(new Label());
		upperPanel.add(new Label());
		
		
		Label clientCertNoLabel = new Label("Client Certificate No.:");
		clientCertNoLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(clientCertNoLabel);
		
		clientCertNoTextField = new Label();		
		clientCertNoTextField.setCtCls("cert-table-cell");
		upperPanel.add(clientCertNoTextField);
		
		///		
		Label certificateNoLabel = new Label("Certificate No.:");
		certificateNoLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(certificateNoLabel);
		crtificateNoTextField = new Label();
		crtificateNoTextField.setCtCls("cert-table-cell");
		upperPanel.add(crtificateNoTextField);
		
		Label certificateStatusLabel = new Label("Certificate Status:");
		certificateStatusLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(certificateStatusLabel);
		certificateStatusTextField = new Label();
		certificateStatusTextField.setCtCls("cert-table-cell");
		upperPanel.add(certificateStatusTextField);
		
		////
		mainPanel.add(upperPanel);
		
		Panel lowerPanel_1 = new Panel();
		lowerPanel_1.setHeight(370);
		lowerPanel_1.setPaddings(5);
		lowerPanel_1.setBorder(true);
		lowerPanel_1.setLayout(new TableLayout(3));		
		lowerPanel_1.setAutoScroll(true);
		
		lowerPanel_1.setFrame(false);
		lowerPanel_1.setBorder(false);
		
		lowerPanel_1.add(new Label(""));
		Label IPAHeaderLabel= new Label("IPA");
		IPAHeaderLabel.setCtCls("cert-right-top-heading-table-cell");
		lowerPanel_1.add(IPAHeaderLabel);
		Label certificateHeaderLabel= new Label("Certificate");
		certificateHeaderLabel.setCtCls("cert-right-top-heading-table-cell");
		lowerPanel_1.add(certificateHeaderLabel);
		

		lowerPanel_1.add(new Label());
		lowerPanel_1.add(new Label());
		lowerPanel_1.add(new Label());
		///
		Label mainContractorAmountLabel = new Label("Main Contractor Amount");
		mainContractorAmountLabel.setWidth(250);
		mainContractorAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(mainContractorAmountLabel);
		mainContractorAmountIPATextField = new Label();
		mainContractorAmountIPATextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorAmountIPATextField);
		
		mainContractorAmountCertificateTextField =  new Label();				
		mainContractorAmountCertificateTextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorAmountCertificateTextField);
		
		///
		Label NSCNDSCAmountLabel = new Label("NSC / NDSC Amount");
		NSCNDSCAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(NSCNDSCAmountLabel);
		NSCNDSCAmountIPATextField = new Label();				
		NSCNDSCAmountIPATextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(NSCNDSCAmountIPATextField);
		
		NSCNDSCAmountCertificateTextField = new Label();		
		NSCNDSCAmountCertificateTextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(NSCNDSCAmountCertificateTextField);
		
		
		///
		Label MOSAmountLabel = new Label("MOS Amount");
		MOSAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(MOSAmountLabel);
		MOSAmountIPATextField = new Label();		
		MOSAmountIPATextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSAmountIPATextField);
		
		MOSAmountCertifcateTextField = new Label();		
		MOSAmountCertifcateTextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSAmountCertifcateTextField);
				
		///
		Label mainContractorRetentionReleaseLabel = new Label("Main Contractor Retention Released");
		mainContractorRetentionReleaseLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(mainContractorRetentionReleaseLabel);
		mainContractorRetentionReleaseIPATextField = new Label();		
		mainContractorRetentionReleaseIPATextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorRetentionReleaseIPATextField);
		
		mainContractorRetentionReleaseCertificateTextField = new Label();
		mainContractorRetentionReleaseCertificateTextField.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorRetentionReleaseCertificateTextField);

		Label retentionForNSCNDSCReleaseLabel = new Label("Retention for NSC / NDSC Released");
		retentionForNSCNDSCReleaseLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(retentionForNSCNDSCReleaseLabel);
		retentionForNSCNDSCReleaseIPALabel = new Label();		
		retentionForNSCNDSCReleaseIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(retentionForNSCNDSCReleaseIPALabel);
		
		retentionForNSCNDSCReleaseCertifciateLabel = new Label();
		retentionForNSCNDSCReleaseCertifciateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(retentionForNSCNDSCReleaseCertifciateLabel);

		Label MOSRetentionReleaseLabel = new Label("MOS Retention Released");
		MOSRetentionReleaseLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(MOSRetentionReleaseLabel);
		MOSRetentionReleaseIPALabel = new Label();		
		MOSRetentionReleaseIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSRetentionReleaseIPALabel);
		
		MOSRetentionReleaseCertificateLabel = new Label();
		MOSRetentionReleaseCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSRetentionReleaseCertificateLabel);

		Label adjustmentAmountLabel = new Label("Adjustment Amount");
		adjustmentAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(adjustmentAmountLabel);
		adjustmentAmountIPALabel = new Label();		
		adjustmentAmountIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(adjustmentAmountIPALabel);
		
		adjustmentAmountCertificateLabel = new Label();
		adjustmentAmountCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(adjustmentAmountCertificateLabel);

		Label advancPaymentLabel = new Label("Advance Payment");
		advancPaymentLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(advancPaymentLabel);
		advancePaymentIPALabel = new Label();		
		advancePaymentIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(advancePaymentIPALabel);
		
		advancePaymentCertificateLabel = new Label();
		advancePaymentCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(advancePaymentCertificateLabel);

		Label CPFAmountLabel = new Label("CPF Amount");
		CPFAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(CPFAmountLabel);
		CPFAmountIPALabel = new Label();		
		CPFAmountIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(CPFAmountIPALabel);
		
		CPFAmountCertificateLabel = new Label();
		CPFAmountCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(CPFAmountCertificateLabel);

		Label mainContractorRetentionLabel = new Label("Main Contractor Retention");
		mainContractorRetentionLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(mainContractorRetentionLabel);
		mainContractorRetentionIPALabel = new Label();		
		mainContractorRetentionIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorRetentionIPALabel);
		
		mainContractorRetentionCertificateLabel = new Label();
		mainContractorRetentionCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(mainContractorRetentionCertificateLabel);

		Label retentionForNSCNDSCLabel = new Label("Retention for NSC / NDSC");
		retentionForNSCNDSCLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(retentionForNSCNDSCLabel);
		retentionForNSCNDSCIPALabel = new Label();		
		retentionForNSCNDSCIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(retentionForNSCNDSCIPALabel);
		
		retentionForNSCNDSCCertificateLabel = new Label();
		retentionForNSCNDSCCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(retentionForNSCNDSCCertificateLabel);

		Label MOSRetentionLabel = new Label("MOS Retention");
		MOSRetentionLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(MOSRetentionLabel);
		MOSRetentionIPALabel = new Label();		
		MOSRetentionIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSRetentionIPALabel);
		
		MOSRetentionCertificateLabel = new Label();
		MOSRetentionCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(MOSRetentionCertificateLabel);

		Label contraChargeAmountLabel = new Label("Contra Charge Amount");
		contraChargeAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(contraChargeAmountLabel);
		contraChargeAmountIPALabel = new Label();		
		contraChargeAmountIPALabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(contraChargeAmountIPALabel);
		
		contraChargeAmountCertificateLabel = new Label();
		contraChargeAmountCertificateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_1.add(contraChargeAmountCertificateLabel);

		Label seperatorLabel3 = new Label();
		seperatorLabel3.setCtCls("cert-table-seperator-cell");
		lowerPanel_1.add(seperatorLabel3);
		Label seperatorLabel4 = new Label("----------------------------------------");
		seperatorLabel4.setCtCls("cert-table-seperator-cell");
		lowerPanel_1.add(seperatorLabel4);
		Label seperatorLabel5 = new Label("----------------------------------------");
		seperatorLabel5.setCtCls("cert-table-seperator-cell");
		lowerPanel_1.add(seperatorLabel5);

		Label netAmountLabel = new Label("Net Amount");
		netAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(netAmountLabel);
		netAmountIPALabel = new Label();		
		netAmountIPALabel.setCtCls("cert-table-bold_value-cell");
		lowerPanel_1.add(netAmountIPALabel);
		
		netAmountCertificateLabel = new Label();
		netAmountCertificateLabel.setCtCls("cert-table-bold_value-cell");
		lowerPanel_1.add(netAmountCertificateLabel);

		Label grossAmountLabel = new Label("Gross Amount");
		grossAmountLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_1.add(grossAmountLabel);
		grossAmountIPALabel = new Label();		
		grossAmountIPALabel.setCtCls("cert-table-bold_value-cell");
		lowerPanel_1.add(grossAmountIPALabel);
		
		grossAmountCertificateLabel = new Label();
		grossAmountCertificateLabel.setCtCls("cert-table-bold_value-cell");
		lowerPanel_1.add(grossAmountCertificateLabel);

		Panel lowerPanel_2 = new Panel();
		lowerPanel_2.setHeight(70);
		lowerPanel_2.setPaddings(5);
		lowerPanel_2.setBorder(true);
		lowerPanel_2.setLayout(new TableLayout(3));		
		lowerPanel_2.setAutoScroll(true);

		Label GSTAmount_GSTRceivableHeaderLabel = new Label("GST Amount (GST Receivable)");
		GSTAmount_GSTRceivableHeaderLabel.setWidth(280);
		GSTAmount_GSTRceivableHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_2.add(GSTAmount_GSTRceivableHeaderLabel);
		lowerPanel_2.add(new Label());
		GSTAmount_GSTRceivableLabel = new Label();
		GSTAmount_GSTRceivableLabel.setWidth(280);
		GSTAmount_GSTRceivableLabel.setCtCls("cert-table-value-cell");
		lowerPanel_2.add(GSTAmount_GSTRceivableLabel);
		Label tempLabel;
		
		Label GSTForContraCharge_GSTPayableHeaderLabel = new Label("GST for Contra Charge(GST Payable)");
		GSTForContraCharge_GSTPayableHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		GSTForContraCharge_GSTPayableHeaderLabel.setWidth(280);
		lowerPanel_2.add(GSTForContraCharge_GSTPayableHeaderLabel);
		lowerPanel_2.add(new Label());
		GSTForContraCharge_GSTPayableLabel = new Label();
		GSTForContraCharge_GSTPayableLabel.setWidth(280);
		GSTForContraCharge_GSTPayableLabel.setCtCls("cert-table-value-cell");
		lowerPanel_2.add(GSTForContraCharge_GSTPayableLabel);

		Panel lowerPanel_3 = new Panel();
		lowerPanel_3.setHeight(70);
		lowerPanel_3.setPaddings(5);
		lowerPanel_3.setBorder(true);
		lowerPanel_3.setLayout(new TableLayout(6));		
		lowerPanel_3.setAutoScroll(true);
		
		Label IPASubmissionDateHeaderLabel = new Label("IPA Submission Date");
		IPASubmissionDateHeaderLabel.setWidth(125);
		IPASubmissionDateHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_3.add(IPASubmissionDateHeaderLabel);
		IPASubmissionDateLabel = new Label();		
		IPASubmissionDateLabel.setWidth(125);
		IPASubmissionDateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(IPASubmissionDateLabel);
		
		tempLabel= new Label("");
		tempLabel.setWidth(40);
		tempLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(tempLabel);

		Label actualReceiptDateHeaderLabel = new Label("Actual Receipt Date");
		actualReceiptDateHeaderLabel.setWidth(125);
		actualReceiptDateHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_3.add(actualReceiptDateHeaderLabel);
		actualReceiptDateLabel = new Label();		
		actualReceiptDateLabel.setWidth(125);
		actualReceiptDateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(actualReceiptDateLabel);

		tempLabel= new Label("");
		tempLabel.setWidth(40);
		tempLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(tempLabel);
		
		Label certificateIssueDateHeaderLabel = new Label("Certificate Issue Date");
		certificateIssueDateHeaderLabel.setWidth(125);
		certificateIssueDateHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_3.add(certificateIssueDateHeaderLabel);
		certificateIssueDateLabel = new Label();		
		certificateIssueDateLabel.setWidth(125);
		certificateIssueDateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(certificateIssueDateLabel);
		
		tempLabel= new Label("");
		tempLabel.setWidth(40);
		tempLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(tempLabel);
		
		Label certificateAsAtDateHeaderLabel = new Label("Certificate As At Date");
		certificateAsAtDateHeaderLabel.setWidth(125);
		certificateAsAtDateHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_3.add(certificateAsAtDateHeaderLabel);
		certificateAsAtDateLabel = new Label();		
		certificateAsAtDateLabel.setWidth(125);
		certificateAsAtDateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(certificateAsAtDateLabel);

		tempLabel= new Label("");
		tempLabel.setWidth(40);
		tempLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(tempLabel);

		Label certificateDueDateHeaderLabel = new Label("Certificate Due Date");
		certificateDueDateHeaderLabel.setWidth(125);
		certificateDueDateHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_3.add(certificateDueDateHeaderLabel);
		certificateDueDateLabel = new Label();		
		certificateDueDateLabel.setWidth(125); 
		certificateDueDateLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(certificateDueDateLabel);
		
		tempLabel= new Label("");
		tempLabel.setWidth(40);
		tempLabel.setCtCls("cert-table-value-cell");
		lowerPanel_3.add(tempLabel);

		Panel lowerPanel_4 = new Panel();
		lowerPanel_4.setHeight(65);
		lowerPanel_4.setPaddings(2);
		lowerPanel_4.setBorder(true);
		lowerPanel_4.setLayout(new TableLayout(1));		
		lowerPanel_4.setAutoScroll(true);

		Label remarkHeaderLabel = new Label("Remark");
		remarkHeaderLabel.setCtCls("cert-left-top-heading-table-cell");
		lowerPanel_4.add(remarkHeaderLabel);
		remarkLabel = new Label();		
		remarkLabel.setCtCls("cert-table-value-cell");
		lowerPanel_4.add(remarkLabel);


		mainPanel.add(lowerPanel_1);
		mainPanel.add(lowerPanel_2);
		mainPanel.add(lowerPanel_3);
		mainPanel.add(lowerPanel_4);
		
		setupBottomToolbar();
		add(mainPanel);
	}
	
	private void setupBottomToolbar(){
		Toolbar bottomToolbar = new Toolbar();
		
		ToolbarButton printWithAddendumButton = new ToolbarButton("Export To PDF");
		printWithAddendumButton.setIconCls("pdf-icon");
		printWithAddendumButton.addListener(new ButtonListenerAdapter(){			
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_PDF_URL
						+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
						+ "&mainCertNo="+Integer.valueOf(mainCertNo)
						, "_blank", "");

			};
		});
		
		ToolbarButton printButton = new ToolbarButton("Export To Excel");
		printButton.setIconCls("excel-icon");
		printButton.addListener(new ButtonListenerAdapter(){			
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_EXCEL_URL
						+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
						+ "&mainCertNo="+Integer.valueOf(mainCertNo)
						, "_blank", "");

			};
		});
		
		bottomToolbar.addFill();
		bottomToolbar.addSeparator();
		bottomToolbar.addButton(printWithAddendumButton);
		bottomToolbar.addSeparator();
		bottomToolbar.addButton(printButton);
		bottomToolbar.addSeparator();
			
		mainPanel.add(bottomToolbar);
	}
	
	
	public void populate(String jobNumber, String mainCertNo){
		this.jobNumber = jobNumber;
		this.mainCertNo = Integer.valueOf(mainCertNo);

		UIUtil.maskMainPanel();
		SessionTimeoutCheck.renewSessionTimer();
		mainContractCertificateRepository.getMainContractCert(this.jobNumber, this.mainCertNo, new AsyncCallback<MainContractCertificate>(){
			public void onSuccess(MainContractCertificate result) {
				UIUtil.unmaskMainPanel();
				try{
					
					jobNumberTextField.setText(handleBlanKString(result.getJobNo()));
					clientCertNoTextField.setText(handleBlanKString(result.getClientCertNo()));
					crtificateNoTextField.setText(handleBlanKString(result.getCertificateNumber().toString()));
					certificateStatusTextField.setText(handleBlanKString(result.getCertificateStatus()));
					
					mainContractorAmountIPATextField.setText(renderAmount(result.getAppliedMainContractorAmount()));
					mainContractorAmountCertificateTextField.setText(renderAmount(result.getCertifiedMainContractorAmount()));
					NSCNDSCAmountIPATextField.setText(renderAmount(result.getAppliedNSCNDSCAmount()));
					NSCNDSCAmountCertificateTextField.setText(renderAmount(result.getCertifiedNSCNDSCAmount()));
					MOSAmountIPATextField.setText(renderAmount(result.getAppliedMOSAmount()));
					MOSAmountCertifcateTextField.setText(renderAmount(result.getCertifiedMOSAmount()));
					mainContractorRetentionReleaseIPATextField.setText(renderAmount(result.getAppliedMainContractorRetentionReleased()));
					mainContractorRetentionReleaseCertificateTextField.setText(renderAmount(result.getCertifiedMainContractorRetentionReleased()));
					retentionForNSCNDSCReleaseIPALabel.setText(renderAmount(result.getAppliedRetentionforNSCNDSCReleased()));
					retentionForNSCNDSCReleaseCertifciateLabel.setText(renderAmount(result.getCertifiedRetentionforNSCNDSCReleased()));
					MOSRetentionReleaseIPALabel.setText(renderAmount(result.getAppliedMOSRetentionReleased()));
					MOSRetentionReleaseCertificateLabel.setText(renderAmount(result.getCertifiedMOSRetentionReleased()));
					adjustmentAmountIPALabel.setText(renderAmount(result.getAppliedAdjustmentAmount()));
					adjustmentAmountCertificateLabel.setText(renderAmount(result.getCertifiedAdjustmentAmount()));
					advancePaymentIPALabel.setText(renderAmount(result.getAppliedAdvancePayment()));
					advancePaymentCertificateLabel.setText(renderAmount(result.getCertifiedAdvancePayment()));
					CPFAmountIPALabel.setText(renderAmount(result.getAppliedCPFAmount()));
					CPFAmountCertificateLabel.setText(renderAmount(result.getCertifiedCPFAmount()));
					mainContractorRetentionIPALabel.setHtml(renderRetention(result.getAppliedMainContractorRetention()));
					mainContractorRetentionCertificateLabel.setHtml(renderRetention(result.getCertifiedMainContractorRetention()));
					retentionForNSCNDSCIPALabel.setHtml(renderRetention(result.getAppliedRetentionforNSCNDSC()));
					retentionForNSCNDSCCertificateLabel.setHtml(renderRetention(result.getCertifiedRetentionforNSCNDSC()));
					MOSRetentionIPALabel.setHtml(renderRetention(result.getAppliedMOSRetention()));
					MOSRetentionCertificateLabel.setHtml(renderRetention(result.getCertifiedMOSRetention()));
					contraChargeAmountIPALabel.setHtml(renderRetention(result.getAppliedContraChargeAmount()));
					contraChargeAmountCertificateLabel.setHtml(renderRetention(result.getCertifiedContraChargeAmount()));
					netAmountIPALabel.setText(renderAmount(result.calculateAppliedNetAmount()));
					netAmountCertificateLabel.setText(renderAmount(result.calculateCertifiedNetAmount()));
					grossAmountIPALabel.setText(renderAmount(result.calculateAppliedGrossAmount()));
					grossAmountCertificateLabel.setText(renderAmount(result.calculateCertifiedGrossAmount()));
					GSTAmount_GSTRceivableLabel.setText(renderAmount(result.getGstReceivable()));
					GSTForContraCharge_GSTPayableLabel.setText(renderAmount(result.getGstPayable()));
					IPASubmissionDateLabel.setText(renderDate(result.getIpaSubmissionDate()));
					actualReceiptDateLabel.setText(renderDate(result.getActualReceiptDate()));
					certificateIssueDateLabel.setText(renderDate(result.getCertIssueDate()));
					certificateAsAtDateLabel.setText(renderDate(result.getCertAsAtDate()));
					certificateDueDateLabel.setText(renderDate(result.getCertDueDate()));
					remarkLabel.setText(handleBlanKString(result.getRemark()));
				}catch(Exception e){
					UIUtil.alert(e);
				}
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
		});
	}
	
	private String renderAmount(Double amount){
		Double raw = new Double(0.0);
		try {
			String str;
			if(amount == null)
				str = "";
			else
				str = amount.toString().trim();
			raw = Double.parseDouble(str);
			
			double rawDouble  = raw;
			raw = new Double(rawDouble);
		}catch(Exception ex) {
			return "";
			
		}
		
		return format.format(raw.doubleValue());
	}
	
	private String renderRetention(Double amount){
		return "<font color=#FF0000>"+renderAmount(amount)+"</font>";
	}

	private String renderDate(Date date){
		try {
			if (date == null || "".equals(date))
				return "";

			Date dateObj = (Date) date;

			return DateUtil.formatDate(dateObj);

		} catch (Exception e) {
			UIUtil.alert(e);
			return null;
		}
	}
	private String handleBlanKString(String str){
		if(str == null)
			return "";
		else
			return str.trim();
	}
}
