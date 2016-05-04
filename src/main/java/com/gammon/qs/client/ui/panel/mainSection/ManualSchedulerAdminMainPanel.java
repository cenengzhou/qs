package com.gammon.qs.client.ui.panel.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**@author koeyyeung
 * created on 24th Apr, 2015**/
public class ManualSchedulerAdminMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "ManualSchedulerAdminMainPanel_ID";

	private TextField provisionJobTextField;
	private DateField glDateTextField;
	
	Radio runAllRadio;
	Radio numDayBackRadio;
	Radio jobRadio;
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	
	public ManualSchedulerAdminMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.globalMessageTicket = new GlobalMessageTicket();
		
		setTitle("Administration");
		setId(MAIN_PANEL_ID);
		setLayout(new RowLayout());
		setupUI();
		
		this.globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private void setupUI() {
		Panel mainPanel = new Panel();
		mainPanel.setBorder(false);
		mainPanel.setPaddings(10);
		mainPanel.setLayout(new ColumnLayout());
		
		Panel firstColumnPanel = new Panel();
		firstColumnPanel.setLayout(new RowLayout());
		firstColumnPanel.setBorder(false);
		firstColumnPanel.setPaddings(10);
		
		/*Provision Posting*/
		FieldSet provisionFieldSet = new FieldSet("Provision Posting",50);
		provisionFieldSet.setWidth(getWidth()/6);
		provisionFieldSet.setHeight(getHeight()/3);
		provisionFieldSet.setPaddings(0, 0, 0, 10);
		
		TableLayout provisionPanelLayout = new TableLayout(2);
		provisionPanelLayout.setSpacing("10");
		provisionFieldSet.setLayout(provisionPanelLayout);
		Label provisionJobNumber = new Label("Job Number:");
		provisionJobNumber.setWidth(150);
		provisionJobNumber.setCtCls("table-cell");
		provisionFieldSet.add(provisionJobNumber);
		provisionJobTextField = new TextField ("Job Number","jobNumber",150);
		provisionJobTextField.setMaxLength(5);
		provisionJobTextField.setCtCls("table-cell");
		provisionJobTextField.setWidth(150);
		provisionFieldSet.add(provisionJobTextField);

		Label glDateLabel = new Label ("GL Date:");
		glDateLabel.setWidth(150);
		glDateLabel.setCtCls("table-cell");
		provisionFieldSet.add(glDateLabel);
		glDateTextField = new DateField("GL Date","Y/m/d");
		glDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		glDateTextField.setWidth(150);
		glDateTextField.setAltFormats("dmy");
		glDateTextField.setCtCls("table-cell");
		provisionFieldSet.add(glDateTextField);

		Button provisionButton = new Button("  Post  ");
		provisionButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				UIUtil.maskPanelById(MAIN_PANEL_ID, "Posting", true );
				globalMessageTicket.refresh();
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().generateSCProvisionManually(provisionJobTextField.getText(), glDateTextField.getValue(), new Boolean(false), globalSectionController.getUser().getUsername(), new AsyncCallback<String>(){
					public void onFailure(Throwable e) {
						UIUtil.alert(e.getMessage());
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}

					public void onSuccess(String errMsg) {
						if (errMsg!=null && errMsg.trim().length()>0)
							MessageBox.alert("         ",errMsg);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
					
				});
			};
		});
		provisionFieldSet.addButton(provisionButton);
		
		/*Payment Posting*/
		FieldSet paymentPostingFieldSet = new FieldSet("Post Subcontract Payment",50);
		paymentPostingFieldSet.setWidth(getWidth()/6);
		paymentPostingFieldSet.setHeight(getHeight()/3);
		paymentPostingFieldSet.setLayout(new RowLayout());
		paymentPostingFieldSet.setPaddings(10, 0, 0, 10);
		
		Label paymentPostingLabel = new Label("Subcontract Payments with status 'Waiting For Posting' will be posted to Finance Account Payable Ledger.");
		paymentPostingLabel.setCtCls("table-cell");
		paymentPostingFieldSet.add(paymentPostingLabel);
		
		Button paymentPostingButton = new Button ("Post");
		paymentPostingButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(MAIN_PANEL_ID, "Posting", true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPaymentRepository().callSCPaymentBatch(new AsyncCallback<String>(){

					public void onFailure(Throwable exception) {
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}

					public void onSuccess(String message) {
						if (message!=null && message.trim().length()>0)
							MessageBox.alert(message);
						else 
							MessageBox.alert("Finished!");
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				});
			};
		});
		paymentPostingFieldSet.addButton(paymentPostingButton);
		firstColumnPanel.add(provisionFieldSet);
		firstColumnPanel.add(paymentPostingFieldSet);
		
		/**
		 * @author koeyyeung
		 * created on 26 Aug, 2014
		 * **/
		Panel secondColumnPanel = new Panel();
		secondColumnPanel.setLayout(new RowLayout());
		secondColumnPanel.setBorder(false);
		secondColumnPanel.setPaddings(10);
		
		/*Generate SCPackage Snapshot*/
		FieldSet generateSCPackageSnapshotFieldSet = new FieldSet("Generate Subcontract Package Snapshot",50);
		generateSCPackageSnapshotFieldSet.setWidth(getWidth()/6);
		generateSCPackageSnapshotFieldSet.setHeight(getHeight()/3);
		generateSCPackageSnapshotFieldSet.setLayout(new RowLayout());
		generateSCPackageSnapshotFieldSet.setPaddings(10, 0, 0, 10);
		
		Label snapshotLabel = new Label("ALL Subcontract Package data will be cloned to \n" +
						"Subcontract Package Snapshot Table serving for Month End History enquiry in Subcontract Enquiry Page.");
		snapshotLabel.setCtCls("table-cell");
		generateSCPackageSnapshotFieldSet.add(snapshotLabel);
		
		Button generateSCPackageSnapshotButton = new Button ("Generate Snapshot");
		generateSCPackageSnapshotButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(MAIN_PANEL_ID, "Processing", true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().generateSCPackageSnapshotManually(new AsyncCallback<Boolean>() {
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
					public void onSuccess(Boolean completed) {
						if(completed){
							MessageBox.alert("Generate Subcontract Package Snapshot completed.");
						}else
							MessageBox.alert("Generate Subcontract Package Snapshot failed.");
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				});
			};
		});
		generateSCPackageSnapshotFieldSet.addButton(generateSCPackageSnapshotButton);
		secondColumnPanel.add(generateSCPackageSnapshotFieldSet);
			
		
		/**
		 * @author koeyyeung
		 * created on 24 April, 2015
		 * Stored procedure to update F58001 from scPackage
		 * **/
		Panel thirdColumnPanel = new Panel();
		thirdColumnPanel.setLayout(new RowLayout());
		thirdColumnPanel.setBorder(false);
		thirdColumnPanel.setPaddings(10);
		
		FieldSet updateF58001FromSCPackageFieldSet = new FieldSet("Synchronize F58001 From Subcontract Package",50);
		updateF58001FromSCPackageFieldSet.setWidth(getWidth()/6);
		updateF58001FromSCPackageFieldSet.setHeight(getHeight()/3);
		updateF58001FromSCPackageFieldSet.setLayout(new RowLayout());
		updateF58001FromSCPackageFieldSet.setPaddings(10, 0, 0, 10);
			
		Label updateF58001Label = new Label(	"Missing records will be inserted into F58001(JDE) from Subcontract Package(QS System). \n" +
							"For mis-matched records, F58001(JDE) will be updated as well. ");
		updateF58001Label.setCtCls("table-cell");
		updateF58001FromSCPackageFieldSet.add(updateF58001Label);
		
		Button updateF58001FromSCPackageButton = new Button ("Synchronize");
		updateF58001FromSCPackageButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				updateF58001();
			};
		});
		updateF58001FromSCPackageFieldSet.addButton(updateF58001FromSCPackageButton);
		thirdColumnPanel.add(updateF58001FromSCPackageFieldSet);
		
		
		/**
		 * @author koeyyeung
		 * created on 24 April, 2015
		 * Stored procedure to update F58001 from scPackage
		 * **/
		FieldSet updateF58011FromSCPaymentFieldSet = new FieldSet("Synchronize F58011 From Subcontract Payment Certificate",50);
		updateF58011FromSCPaymentFieldSet.setWidth(getWidth()/6);
		updateF58011FromSCPaymentFieldSet.setHeight(getHeight()/3);
		updateF58011FromSCPaymentFieldSet.setLayout(new TableLayout(2));
		
		Label updateF58011Label = new Label("Missing records will be inserted into F58011(JDE) from Subcontract Payment Certificate(QS System).");
		updateF58011Label.setCtCls("table-cell");
		updateF58011FromSCPaymentFieldSet.add(updateF58011Label);
		
		Button updateF58011FromSCPaymentButton = new Button ("Synchronize");
		updateF58011FromSCPaymentButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				updateF58011();
			};
		});
		updateF58011FromSCPaymentFieldSet.addButton(updateF58011FromSCPaymentButton);
		thirdColumnPanel.add(updateF58011FromSCPaymentFieldSet);
		
		/**
		 * @author koeyyeung
		 * created on 24 April, 2015
		 * Stored procedure to update F58001 from scPackage
		 * **/
		FieldSet updateMainCertFromF03B14FieldSet = new FieldSet("Synchronize Main Contract Certificate From JDE(F03B14)",50);
		updateMainCertFromF03B14FieldSet.setWidth(getWidth()/6);
		updateMainCertFromF03B14FieldSet.setHeight(getHeight()/3);
		updateMainCertFromF03B14FieldSet.setLayout(new TableLayout(2));
		
		Label updateMainCertFromF03B14Label = new Label("Actual Recipet Date of Main Contract Certificate will be mirrored from JDE(F03B14).");
		updateMainCertFromF03B14Label.setCtCls("table-cell");
		updateMainCertFromF03B14FieldSet.add(updateMainCertFromF03B14Label);
		
		Button updateMainCertFromF03B14Button = new Button ("Synchronize");
		updateMainCertFromF03B14Button.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				updateMainCert();
			};
		});
		updateMainCertFromF03B14FieldSet.addButton(updateMainCertFromF03B14Button);
		thirdColumnPanel.add(updateMainCertFromF03B14FieldSet);
		
		mainPanel.add(firstColumnPanel, new ColumnLayoutData(0.3));
		mainPanel.add(secondColumnPanel, new ColumnLayoutData(0.3));
		mainPanel.add(thirdColumnPanel, new ColumnLayoutData(0.3));
		add(mainPanel);
	}

	private void updateF58001(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Processing", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().updateF58001FromSCPackageManually(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(Boolean completed) {
				if(completed){
					MessageBox.alert("Synchronize F58001 From Subcontract Package completed.");
				}else
					MessageBox.alert("Synchronize F58001 From Subcontract Package failed.");
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	private void updateF58011(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Processing", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPaymentRepository().updateF58011FromSCPaymentCertManually(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(Boolean completed) {
				if(completed){
					MessageBox.alert("Synchronize F58011 From Subcontract Payment Certificate completed.");
				}else
					MessageBox.alert("Synchronize F58011 From Subcontract Payment Certificate failed.");
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	private void updateMainCert(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Processing", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().updateMainCertFromF03B14Manually(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(Boolean completed) {
				if(completed){
					MessageBox.alert("Synchronize Main Contract Certificate From F03B14 completed.");
				}else
					MessageBox.alert("Synchronize Main Contract Certificate From F03B14 failed.");
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
}
