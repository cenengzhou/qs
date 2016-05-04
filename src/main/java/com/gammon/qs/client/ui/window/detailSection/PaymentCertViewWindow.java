package com.gammon.qs.client.ui.window.detailSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PaymentRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.PaymentCertAmountRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
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
 * @author koeyyeung
 * modified on 27Sep, 2013
 * **/
public class PaymentCertViewWindow extends Window {
	//Remote service
	private PaymentRepositoryRemoteAsync paymentRepository;
	
	//UI
	private GlobalSectionController globalSectionController;
	
	private Panel mainPanel;
	
	private Label jobNumberTextField;
	private Label jobDescriptionLabel;
	private Label paymentNumberTextField;
	private Label subcontractNumberTextField;
	private Label subcontractDescriptionTextField;
	private Label paymentTypeTextField;
	private Label subcontractorNumberTextField;
	private Label subcontractorDescriptionLabel;
	private Label subcontractSumTextField;
	private Label approvalStatusLabel;
	private Label addendumTextField;
	private Label revisedValueTextField;
	
	private Label measuredWorksMovementTextField;
	private Label measuredWorksTotalTextField;
	private Label dayWorkSheetMovementTextField;
	private Label dayWorkSheetTotalTextField;
	private Label variationMovementTextField;
	private Label variationTotalTextField;
	private Label otherMovementTextField;
	private Label otherTotalTextField;
	
	private Label subMovement1TextField;
	private Label subTotal1TextField;
	private Label lessRetentionMovement1TextField;
	private Label lessRetentionTotal1TextField;
	
	private Label subMovement2TextField;
	private Label subTotal2TextField;
	private Label materialOnSiteMovementTextField;
	private Label materialOnSiteTotalTextField;
	private Label lessRetentionMovement2TextField;
	private Label lessRetentionTotal2TextField;
	
	private Label subMovement3TextField;
	private Label subTotal3TextField;
	private Label adjustmentMovementTextField;
	private Label adjustmentTotalTextField;
	
	private Label gstPayableMovementTextField;
	private Label gstPayableTotalTextField;
	
	private Label subMovement4TextField;
	private Label subTotal4TextField;
	private Label lessContraChargesMovementTextField;
	private Label lessContraChargesTotalTextField;
	private Label lessGSTReceivableMovementTextField;
	private Label lessGSTReceivableTotalTextField;
	
	private Label subMovement5TextField;
	private Label subTotal5TextField;
	
	private Label lessPreviousCertificationsMovementTextField;
	private Label amountDueMovementLabel;
	
	
	//getParameter
	private String jobNumber;
	private String packageNo;
	private Integer paymentCertNo;
	
	@SuppressWarnings("unused")
	private GlobalMessageTicket globalMessageTicket;
	
	public PaymentCertViewWindow( final GlobalSectionController globalSectionController){
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
	
		paymentRepository = globalSectionController.getPaymentRepository();
		
		setupUI();
	}
	
	private void setupUI(){
		setTitle("Subcontract Payment Certificate");
		setPaddings(2);
		setWidth(800);
		setHeight(660);
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
		upperPanel.setHeight(120);
		upperPanel.setLayout(new TableLayout(5));
		
		///
		Label jobNumberLabel = new Label("Job Number:");	
		jobNumberLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(jobNumberLabel);
		

		jobNumberTextField =  new Label();		
		jobNumberTextField.setCtCls("cert-table-cell");
		upperPanel.add(jobNumberTextField);
		
		
		jobDescriptionLabel = new Label();
		jobDescriptionLabel.setCls("cert-table-cell");
		upperPanel.add(jobDescriptionLabel);
		
		Label paymentNoLabel = new Label("Payment No.:");
		paymentNoLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(paymentNoLabel);
		
		paymentNumberTextField = new Label();		
		paymentNumberTextField.setCtCls("cert-table-cell");
		upperPanel.add(paymentNumberTextField);
		
		///		
		Label subcontractLabel = new Label("Subcontract:");
		subcontractLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(subcontractLabel);
		subcontractNumberTextField = new Label();
		subcontractNumberTextField.setCtCls("cert-table-cell");
		upperPanel.add(subcontractNumberTextField);
		
		subcontractDescriptionTextField = new Label();
		subcontractDescriptionTextField.setCtCls("cert-table-cell");
		upperPanel.add(subcontractDescriptionTextField);
		
		Label paymentTypeLabel = new Label("Payment Type:");
		paymentTypeLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(paymentTypeLabel);
		paymentTypeTextField = new Label();
		paymentTypeTextField.setCtCls("cert-table-cell");
		upperPanel.add(paymentTypeTextField);
		
		///

		Label subcontractorLabel = new Label("Subcontractor:");
		subcontractorLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(subcontractorLabel);
		subcontractorNumberTextField = new Label();
		subcontractorNumberTextField.setCtCls("cert-table-cell");
		upperPanel.add(subcontractorNumberTextField);
		
		subcontractorDescriptionLabel = new Label();
		subcontractorDescriptionLabel.setCtCls("cert-table-cell");
		upperPanel.add(subcontractorDescriptionLabel);
		
		upperPanel.add(new Label(""));
		upperPanel.add(new Label(""));
		
		
		///
		Label subcontractSumLabel = new Label("Subcontract Sum:");
		subcontractSumLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(subcontractSumLabel);
		subcontractSumTextField = new Label();
		subcontractSumTextField.setCtCls("cert-table-cell");
		upperPanel.add(subcontractSumTextField);
		
		upperPanel.add(new Label(""));
		Label statusLabel = new Label("Status:");
		statusLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(statusLabel);
		approvalStatusLabel = new Label();
		approvalStatusLabel.setCtCls("cert-table-cell");
		upperPanel.add(approvalStatusLabel);
		
		///

		Label addendumLabel = new Label("Addendum:");
		addendumLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(addendumLabel);
		addendumTextField = new Label();
		addendumTextField.setCtCls("cert-table-cell");
		upperPanel.add(addendumTextField);
		
		upperPanel.add(new Label(""));
		upperPanel.add(new Label(""));
		upperPanel.add(new Label(""));
		
		///				
		Label revisedValueLabel = new Label("Revised Value:");
		revisedValueLabel.setCtCls("cert-table-title-cell");
		upperPanel.add(revisedValueLabel);
		revisedValueTextField = new Label();
		revisedValueTextField.setCtCls("cert-table-cell");
		upperPanel.add(revisedValueTextField);
		
		upperPanel.add(new Label(""));
		upperPanel.add(new Label(""));
		upperPanel.add(new Label(""));
		
		////
		mainPanel.add(upperPanel);
		
		Panel lowerPanel = new Panel();
		lowerPanel.setHeight(470);
		lowerPanel.setPaddings(5);
		lowerPanel.setBorder(true);
		lowerPanel.setLayout(new TableLayout(3));		
		lowerPanel.setAutoScroll(true);
		
		lowerPanel.setFrame(false);
		lowerPanel.setBorder(false);
		
		lowerPanel.add(new Label(""));
		Label movementLabel= new Label("MOVEMENT");
		movementLabel.setCtCls("cert-top-heading-table-cell");
		lowerPanel.add(movementLabel);
		Label totalLabel= new Label("TOTAL");
		totalLabel.setCtCls("cert-top-heading-table-cell");
		lowerPanel.add(totalLabel);
		

		///
		Label measuredWorksLabel = new Label("VALUE OF MEASURED WORKS/GOODS");
		measuredWorksLabel.setWidth(250);
		measuredWorksLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(measuredWorksLabel);
		measuredWorksMovementTextField = new Label();
		measuredWorksMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(measuredWorksMovementTextField);
		
		measuredWorksTotalTextField =  new Label();				
		measuredWorksTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(measuredWorksTotalTextField);
		
		///
		Label valueOfDayWorkSheetLabel = new Label("VALUE OF DAY WORK SHEET");
		valueOfDayWorkSheetLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(valueOfDayWorkSheetLabel);
		dayWorkSheetMovementTextField = new Label();				
		dayWorkSheetMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(dayWorkSheetMovementTextField);
		
		dayWorkSheetTotalTextField = new Label();		
		dayWorkSheetTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(dayWorkSheetTotalTextField);
		
		
		///
		Label valueOfVariationLabel = new Label("VALUE OF VARIATION");
		valueOfVariationLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(valueOfVariationLabel);
		variationMovementTextField = new Label();		
		variationMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(variationMovementTextField);
		
		variationTotalTextField = new Label();		
		variationTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(variationTotalTextField);
		

		
		
		///
		Label othersLabel = new Label("OTHERS");
		othersLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(othersLabel);
		otherMovementTextField = new Label();		
		otherMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(otherMovementTextField);
		
		otherTotalTextField = new Label();
		otherTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(otherTotalTextField);
		
		///
		lowerPanel.add(new Label(""));
		Label seperatorLabel1 = new Label("----------------------------------------");
		seperatorLabel1.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel1);
		Label seperatorLabel2 = new Label("----------------------------------------");
		seperatorLabel2.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel2);
		
		///
		lowerPanel.add(new Label(""));
		subMovement1TextField = new Label();
		subMovement1TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subMovement1TextField);
		
		subTotal1TextField = new Label();
		subTotal1TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subTotal1TextField);
		
		///
		Label lessRetentionLabel =new Label("LESS RETENTION");
		lessRetentionLabel.setCtCls("cert-right-subHeading-table-cell");
		lowerPanel.add(lessRetentionLabel);
		lessRetentionMovement1TextField = new Label();
		lessRetentionMovement1TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessRetentionMovement1TextField);
		
		lessRetentionTotal1TextField = new Label();
		lessRetentionTotal1TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessRetentionTotal1TextField);
		
		
		///
		lowerPanel.add(new Label(""));
		Label seperatorLabel3 = new Label("----------------------------------------");
		seperatorLabel3.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel3);
		Label seperatorLabel4 = new Label("----------------------------------------");
		seperatorLabel4.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel4);
		
		///
		
		lowerPanel.add(new Label(""));
		subMovement2TextField = new Label();
		subMovement2TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subMovement2TextField);
		
		subTotal2TextField = new Label();
		subTotal2TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subTotal2TextField);
		
		
		///
		Label valueOfMaterialOnSiteLabel = new Label("VALUE OF MATERIAL ON SITE");
		valueOfMaterialOnSiteLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(valueOfMaterialOnSiteLabel);
		materialOnSiteMovementTextField = new Label();
		materialOnSiteMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(materialOnSiteMovementTextField);
		
		materialOnSiteTotalTextField = new Label();
		materialOnSiteTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(materialOnSiteTotalTextField);
		
		///
		Label lessRetentionLabel2 =new Label("LESS RETENTION");
		lessRetentionLabel2.setCtCls("cert-right-subHeading-table-cell");
		lowerPanel.add(lessRetentionLabel2);
		lessRetentionMovement2TextField = new Label();
		lessRetentionMovement2TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessRetentionMovement2TextField);
		
		lessRetentionTotal2TextField = new Label();
		lessRetentionTotal2TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessRetentionTotal2TextField);
		
		///
		lowerPanel.add(new Label(""));
		Label seperatorLabel5 = new Label("----------------------------------------");
		seperatorLabel5.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel5);
		Label seperatorLabel6 = new Label("----------------------------------------");
		seperatorLabel6.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel6);
		
		///
		
		lowerPanel.add(new Label(""));
		subMovement3TextField = new Label();
		subMovement3TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subMovement3TextField);
		
		subTotal3TextField = new Label();
		subTotal3TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subTotal3TextField);
		
		///
		Label valueOfAdjustmentLabel = new Label("VALUE OF ADJUSTMENTS");
		valueOfAdjustmentLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(valueOfAdjustmentLabel);
		adjustmentMovementTextField = new Label();
		adjustmentMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(adjustmentMovementTextField);
		
		adjustmentTotalTextField = new Label();
		adjustmentTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(adjustmentTotalTextField);
		///
		
		
		Label valueOfGSTPayableLabel = new Label("VALUE OF GST PAYABLE");
		valueOfGSTPayableLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(valueOfGSTPayableLabel);
		gstPayableMovementTextField = new Label();
		gstPayableMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(gstPayableMovementTextField);
		
		gstPayableTotalTextField = new Label();
		gstPayableTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(gstPayableTotalTextField);
		
		///
		lowerPanel.add(new Label(""));
		Label seperatorLabel7 = new Label("----------------------------------------");
		seperatorLabel7.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel7);
		Label seperatorLabel8 = new Label("----------------------------------------");
		seperatorLabel8.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel8);
		
		///
		
		lowerPanel.add(new Label(""));
		subMovement4TextField = new Label();
		subMovement4TextField.setCtCls("cert-table-value-cell");		
		lowerPanel.add(subMovement4TextField);
		
		subTotal4TextField = new Label();
		subTotal4TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subTotal4TextField);
		
		///
		Label lessContraCharges = new Label("LESS CONTRA CHARGES");
		lessContraCharges.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(lessContraCharges);
		lessContraChargesMovementTextField = new Label();
		lessContraChargesMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessContraChargesMovementTextField);
		
		lessContraChargesTotalTextField = new Label();
		lessContraChargesTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessContraChargesTotalTextField);
		
		///
		Label lessGSTReceivableLabel =new Label("LESS GST RECEIVABLE");
		lessGSTReceivableLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(lessGSTReceivableLabel);
		lessGSTReceivableMovementTextField = new Label();
		lessGSTReceivableMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessGSTReceivableMovementTextField);
		
		lessGSTReceivableTotalTextField = new Label();
		lessGSTReceivableTotalTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessGSTReceivableTotalTextField);
		
		
		///
		lowerPanel.add(new Label(""));
		Label seperatorLabel9 = new Label("----------------------------------------");
		seperatorLabel9.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel9);
		Label seperatorLabel10 = new Label("----------------------------------------");
		seperatorLabel10.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel10);
		
		///
		lowerPanel.add(new Label(""));
		subMovement5TextField = new Label();
		subMovement5TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subMovement5TextField);
		
		subTotal5TextField = new Label();
		subTotal5TextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(subTotal5TextField);
		
		
		///
		Label lessPreviousCertificationsLabel = new Label("LESS PREVIOUS CERTIFICATIONS");
		lessPreviousCertificationsLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(lessPreviousCertificationsLabel);
		lowerPanel.add(new Label(""));
		
		
		lessPreviousCertificationsMovementTextField = new Label();
		lessPreviousCertificationsMovementTextField.setCtCls("cert-table-value-cell");
		lowerPanel.add(lessPreviousCertificationsMovementTextField);
		
		
		///
		lowerPanel.add(new Label(""));
		lowerPanel.add(new Label(""));
		Label seperatorLabel11 = new Label("----------------------------------------");
		seperatorLabel11.setCtCls("cert-table-seperator-cell");
		lowerPanel.add(seperatorLabel11);
		///
		
		Label amountDueLabel = new Label("AMOUNT DUE");
		amountDueLabel.setCtCls("cert-right-heading-table-cell");
		lowerPanel.add(amountDueLabel );
		lowerPanel.add(new Label(""));
		
		
		amountDueMovementLabel = new Label();
		amountDueMovementLabel.setCtCls("cert-table-bold_value-cell");
		lowerPanel.add(amountDueMovementLabel);
		
		
		mainPanel.add(lowerPanel);
		
		setupBottomToolbar();
		add(mainPanel);
	}
	
	private void setupBottomToolbar(){
		Toolbar bottomToolbar = new Toolbar();
		
		ToolbarButton printWithAddendumButton = new ToolbarButton("Print with Addendum");
		printWithAddendumButton.setIconCls("pdf-icon");
		printWithAddendumButton.addListener(new ButtonListenerAdapter(){			
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				com.google.gwt.user.client.Window.open(GlobalParameter.SCPAMENTCERT_EXCEL_DOWNLOAD_URL+"?jobNumber="+jobNumber +"&packageNumber="+packageNo+"&paymentCertNo="+paymentCertNo.toString()+"&addendum=Y", "_blank", "");
			};
		});
		
		ToolbarButton printButton = new ToolbarButton("Print without Addendum");
		printButton.setIconCls("pdf-icon");
		printButton.addListener(new ButtonListenerAdapter(){			
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				com.google.gwt.user.client.Window.open(GlobalParameter.SCPAMENTCERT_EXCEL_DOWNLOAD_URL+"?jobNumber="+jobNumber +"&packageNumber="+packageNo+"&paymentCertNo="+paymentCertNo.toString()+"&addendum=N", "_blank", "");
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
	
	
	public void populate(String jobNumber, String packageNo, Integer paymentCertNo){
		this.jobNumber = jobNumber;
		this.packageNo = packageNo;
		this.paymentCertNo = paymentCertNo;
		
		UIUtil.maskMainPanel();		
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.getSCPaymentDetailConsolidateView(jobNumber, packageNo, paymentCertNo, new AsyncCallback<PaymentCertViewWrapper>(){
			public void onSuccess(PaymentCertViewWrapper result) {
				UIUtil.unmaskMainPanel();
				try{
				PaymentCertAmountRenderer amountRenderer = new PaymentCertAmountRenderer();
				
				jobNumberTextField.setText(result.getJobNumber());
				jobDescriptionLabel.setText(result.getJobDescription());
				paymentNumberTextField.setText(result.getPaymentCertNo()!=null?result.getPaymentCertNo().toString():"");
				subcontractNumberTextField.setText(result.getSubContractNo()!=null?result.getSubContractNo().toString():"");
				subcontractDescriptionTextField.setText(result.getSubContractDescription()!=null? result.getSubContractDescription():"");
				
				
				paymentTypeTextField.setText(result.getPaymentType());
				subcontractorNumberTextField.setText(result.getSubcontractorNo()!=null?result.getSubcontractorNo().toString():"");
				subcontractorDescriptionLabel.setText(result.getSubcontractorDescription());
				subcontractSumTextField.setText(result.getSubcontractSum()!=null?amountRenderer.render( result.getSubcontractSum().toString()):amountRenderer.render(""));
				approvalStatusLabel.setText(result.getApprovalStatus());
				
				
				addendumTextField.setText(result.getAddendum()!=null?amountRenderer.render( result.getAddendum().toString()):amountRenderer.render(""));
				revisedValueTextField.setText(result.getRevisedValue()!=null?amountRenderer.render( result.getRevisedValue().toString()):amountRenderer.render(""));

				measuredWorksMovementTextField.setText(result.getMeasuredWorksMovement()!=null?amountRenderer.render( result.getMeasuredWorksMovement().toString()):amountRenderer.render(""));
				measuredWorksTotalTextField.setText(result.getMeasuredWorksTotal()!=null? amountRenderer.render(result.getMeasuredWorksTotal().toString()):amountRenderer.render(""));
				dayWorkSheetMovementTextField.setText(result.getDayWorkSheetMovement()!=null?amountRenderer.render(result.getDayWorkSheetMovement().toString()):amountRenderer.render(""));
				
				
				dayWorkSheetTotalTextField.setText(result.getDayWorkSheetTotal()!=null?amountRenderer.render(result.getDayWorkSheetTotal().toString()):amountRenderer.render(""));
				variationMovementTextField.setText(result.getVariationMovement()!=null?amountRenderer.render(result.getVariationMovement().toString()):amountRenderer.render(""));
				variationTotalTextField.setText(result.getVariationTotal()!=null?amountRenderer.render(result.getVariationTotal().toString()):amountRenderer.render(""));
				otherMovementTextField.setText(result.getOtherMovement()!=null?amountRenderer.render(result.getOtherMovement().toString()):amountRenderer.render(""));
				otherTotalTextField.setText(result.getOtherTotal()!=null? amountRenderer.render(result.getOtherTotal().toString()):amountRenderer.render(""));
				
				
				subMovement1TextField.setText(result.getSubMovement1()!=null? amountRenderer.render(result.getSubMovement1().toString()):amountRenderer.render(""));
				subTotal1TextField.setText(result.getSubTotal1()!=null? amountRenderer.render(result.getSubTotal1().toString()):amountRenderer.render(""));
				lessRetentionMovement1TextField.setText(result.getLessRetentionMovement1()!=null?amountRenderer.render(result.getLessRetentionMovement1().toString()):amountRenderer.render(""));
				lessRetentionTotal1TextField.setText(result.getLessRetentionTotal1()!=null?amountRenderer.render(result.getLessRetentionTotal1().toString()):amountRenderer.render(""));
				
				subMovement2TextField.setText(result.getSubMovement2()!=null?amountRenderer.render( result.getSubMovement2().toString()):amountRenderer.render(""));
				
				
				subTotal2TextField.setText(result.getSubTotal2()!=null? amountRenderer.render(result.getSubTotal2().toString()):amountRenderer.render(""));
				materialOnSiteMovementTextField.setText(result.getMaterialOnSiteMovement()!=null ? amountRenderer.render(result.getMaterialOnSiteMovement().toString()):amountRenderer.render(""));
				materialOnSiteTotalTextField.setText(result.getMaterialOnSiteTotal()!=null? amountRenderer.render(result.getMaterialOnSiteTotal().toString()):amountRenderer.render(""));
				lessRetentionMovement2TextField.setText(result.getLessRetentionMovement2()!=null?amountRenderer.render(result.getLessRetentionMovement2().toString()):amountRenderer.render(""));
				lessRetentionTotal2TextField.setText(result.getLessRetentionTotal2()!=null?amountRenderer.render(result.getLessRetentionTotal2().toString()):amountRenderer.render(""));
				
				
				
				subMovement3TextField.setText(result.getSubMovement3()!=null? amountRenderer.render(result.getSubMovement3().toString()):amountRenderer.render(""));
				subTotal3TextField.setText(result.getSubTotal3()!=null?amountRenderer.render(result.getSubTotal3().toString()):amountRenderer.render(""));
				adjustmentMovementTextField.setText(result.getAdjustmentMovement()!=null?amountRenderer.render(result.getAdjustmentMovement().toString()):amountRenderer.render(""));
				adjustmentTotalTextField.setText(result.getAdjustmentTotal()!=null? amountRenderer.render(result.getAdjustmentTotal().toString()):amountRenderer.render(""));
				
				gstPayableMovementTextField.setText(result.getGstPayableMovement()!=null?amountRenderer.render(result.getGstPayableMovement().toString()):amountRenderer.render(""));
				gstPayableTotalTextField.setText(result.getGstPayableTotal()!=null?amountRenderer.render(result.getGstPayableTotal().toString()):amountRenderer.render(""));
				
				
				
				subMovement4TextField.setText(result.getSubMovement4()!=null?amountRenderer.render(result.getSubMovement4().toString()):amountRenderer.render(""));
				subTotal4TextField.setText(result.getSubTotal4()!=null?amountRenderer.render(result.getSubTotal4().toString()):amountRenderer.render(""));
				lessContraChargesMovementTextField.setText(result.getLessContraChargesMovement()!=null?amountRenderer.render(result.getLessContraChargesMovement().toString()):amountRenderer.render(""));
				lessContraChargesTotalTextField.setText(result.getLessContraChargesTotal()!=null?amountRenderer.render( result.getLessContraChargesTotal().toString()):amountRenderer.render(""));
				lessGSTReceivableMovementTextField.setText(result.getLessGSTReceivableMovement()!=null?amountRenderer.render(result.getLessGSTReceivableMovement().toString()):amountRenderer.render(""));
				lessGSTReceivableTotalTextField.setText(result.getLessGSTReceivableTotal()!=null?amountRenderer.render( result.getLessGSTReceivableTotal().toString()):amountRenderer.render(""));
				
				
				
				subMovement5TextField.setText(result.getSubMovement5()!=null?amountRenderer.render(result.getSubMovement5().toString()):amountRenderer.render(""));
				subTotal5TextField.setText(result.getSubTotal5()!=null? amountRenderer.render(result.getSubTotal5().toString()):amountRenderer.render(""));
				
				lessPreviousCertificationsMovementTextField.setText(result.getLessPreviousCertificationsMovement()!=null?amountRenderer.render(result.getLessPreviousCertificationsMovement().toString()):amountRenderer.render(""));
				amountDueMovementLabel.setText(result.getAmountDueMovement()!=null?amountRenderer.render(result.getAmountDueMovement().toString()):amountRenderer.render(""));
				
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
}
