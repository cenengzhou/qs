package com.gammon.qs.client.ui.window.detailSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.updateAddendum.UpdateAddendumWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class EditSCAddendumWindow extends Window {

	//
	private GlobalSectionController globalSectionController;
	
	
	//UI
	private TextField jobNumberTextField;
	private Label jobDescriptionLabel;
	private TextField scNumberTextField;
	private TextField scLineTypeTextField;
	private TextField bqBriefDescriptionTextField;
	private TextField seqNoTextField;
	private TextField resourceNoTextField;
	private TextField bqItemTextField;
	private TextField objectTextField;
	private TextField subsidiaryTextField;

	private Label approvalLabel;
	private TextField ecaRateTextField;
	private TextField originalBqQtyTextField;
	private TextField toBeApprovedQtyTextField;
	private TextField toBeApprovedRateTextField;
	private TextField toBeApprovedAmountTextField;
	private TextField prevWorkDoneTextField;
	private TextField thisWorkDoneTextField;
	private TextField cumWorkDoneTextField;
	private TextField cumLiabilityAmountTextField;
	private TextField prevCertTextField;
	private TextField thisCertTextField;
	private TextField cumCertTextField;
	private TextField cumCertAmountTextField;
	private TextField provisionAmountTextField;

	//private TextField unitComboBox;
	private ComboBox unitComboBox;
	private TextField bqQuanitityTextField;
	private TextField scRateTextField;
	private TextField totalAmountTextField;
	
	private TextField corrSCNoTextField;
	private TextField altObjectCodeTextField;
	private TextField remarkTextField;
	
	private Button suspendWindowButton;
	private Button deleteWindowButton;
	
	Store unitStore;

	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;
	
	//ID
	private String EDIT_ADDENDUM_ID = "editAddendum_window_Id";
	
	private GlobalMessageTicket globalMessageTicket;
	
	@SuppressWarnings("unused")
	private Label unitDescriptionLabel;
	@SuppressWarnings("unused")
	private String jobNumber;
	@SuppressWarnings("unused")
	private Integer subcontractNumber;
	@SuppressWarnings("unused")
	private Integer sequenceNumber;
	@SuppressWarnings("unused")
	private String billItem;
	@SuppressWarnings("unused")
	private Integer resourceNumber;
	@SuppressWarnings("unused")
	private String subsidiaryCode;
	@SuppressWarnings("unused")
	private String objectCode;
	
	public EditSCAddendumWindow( final GlobalSectionController globalSectionController, String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode)
	{
		
		super();
		this.globalSectionController = globalSectionController;

		globalMessageTicket = new GlobalMessageTicket();
		
		packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget)packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);
			
		
		this.setTitle("Subcontract Addendum (Update)");
		this.setPaddings(2);
		this.setWidth(820);
		this.setHeight(580);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		this.setId(EDIT_ADDENDUM_ID);
		
		Panel topTablePanel = new Panel();
 		topTablePanel.setLayout(new TableLayout(1));
		topTablePanel.setBorder(false);
		topTablePanel.setFrame(true);

		Panel topPanel = new Panel();
 		topPanel.setLayout(new TableLayout(5));
		topPanel.setBorder(false);
		topPanel.setFrame(false);

/*		Panel secondPanel = new Panel();
		secondPanel.setLayout(new HorizontalLayout(5));
		secondPanel.setBorder(false);
		secondPanel.setFrame(false);*/

		Panel forthPanel = new Panel();
		forthPanel.setLayout(new TableLayout(2));
		forthPanel.setBorder(false);
		forthPanel.setFrame(false);

		Panel fifthPanel = new Panel();
		fifthPanel.setLayout(new TableLayout(6));
		fifthPanel.setBorder(false);
		fifthPanel.setFrame(false);

		Label jobNumberLabel = new Label("Job Number");
		jobNumberLabel.setCtCls("left-align-label");
		jobNumberLabel.setWidth(135);
		topPanel.add(jobNumberLabel);
		
		jobNumberTextField =  new TextField();
		jobNumberTextField.setCtCls("left-align");
		jobNumberTextField.setWidth(80);
		jobNumberTextField.setValue(jobNumber);
		topPanel.add(jobNumberTextField);
		
		jobDescriptionLabel = new Label();
		jobDescriptionLabel.setCtCls("left-align");
		jobDescriptionLabel.setWidth(150);
		jobDescriptionLabel.setText(globalSectionController.getJob().getDescription());
		topPanel.add(jobDescriptionLabel);
		
		Label scNumberLabel = new Label("Subcontract Number");
		scNumberLabel.setCtCls("left-align-label");
		scNumberLabel.setWidth(140);
		topPanel.add(scNumberLabel);
		
		scNumberTextField = new TextField();
		scNumberTextField.setCtCls("left-align");
		scNumberTextField.setWidth(80);
		scNumberTextField.setValue(subcontractNumber.toString());
		topPanel.add(scNumberTextField);
		
		Label seqNoLabel = new Label("Seq Number");
		seqNoLabel.setCtCls("left-align-label");		
		topPanel.add(seqNoLabel);

		seqNoTextField = new TextField();		
		seqNoTextField.setCtCls("left-align");
		seqNoTextField.setWidth(80);
		seqNoTextField.setValue(sequenceNumber.toString());
		topPanel.add(seqNoTextField);

		resourceNoTextField = new TextField();		
		resourceNoTextField.setCtCls("left-align");
		resourceNoTextField.setWidth(150);
		resourceNoTextField.setValue(resourceNumber.toString());
		resourceNoTextField.hide();
		topPanel.add(resourceNoTextField);

		approvalLabel = new Label();
		approvalLabel.setCtCls("left-align-label");
		approvalLabel.setText("");
		topPanel.add(approvalLabel);

		topPanel.add(new Label(""));
		
		Label scLineTypeLabel = new Label("SC Line Type");
		scLineTypeLabel.setCtCls("left-align-label");
		topPanel.add(scLineTypeLabel);
		
		scLineTypeTextField = new TextField();		
		scLineTypeTextField.setCtCls("left-align");
		scLineTypeTextField.setWidth(40);
		topPanel.add(scLineTypeTextField);
		
/*		Label spaceLabel = new Label(" ");
		spaceLabel.setWidth(50);
		secondPanel.add(spaceLabel);		

		Label billNoTextLabel = new Label("Bill No");
		billNoTextLabel.setWidth(80);
		billNoTextLabel.setCtCls("left-align");
		secondPanel.add(billNoTextLabel);		

		billNoTextField  = new TextField();
		billNoTextField.setWidth(40);
		billNoTextField.setCtCls("left-align");
		secondPanel.add(billNoTextField);

		secondPanel.setCtCls("left-align");
		topPanel.add(secondPanel, new TableLayoutData(3));*/
		
		topPanel.add(new Label(""));

		Label unitLabel = new Label("Unit");
		unitLabel.setCtCls("left-align-label");
		topPanel.add(unitLabel);
		
//		unitComboBox = new TextField();
//		unitComboBox.setCtCls("left-align");
//		unitComboBox.setWidth(40);
//		topPanel.add(unitComboBox);
		
		unitComboBox = new ComboBox();		
		unitComboBox.setDisplayField("description");
		unitComboBox.setValueField("unitCode");
		unitComboBox.setCtCls("table-cell");
		unitComboBox.setSelectOnFocus(true);
		unitComboBox.setWidth(180);		
		unitStore = globalSectionController.getUnitStore();
		unitStore.load();
		unitComboBox.setStore(unitStore);
		topPanel.add(unitComboBox);

		Label bqItemLabel = new Label("BQ Item");
		bqItemLabel.setCtCls("left-align-label");
		topPanel.add(bqItemLabel);
		
		bqItemTextField = new TextField();
		bqItemTextField.setCtCls("left-align");
		bqItemTextField.setWidth(120);
		bqItemTextField.setValue(billItem);
		topPanel.add(bqItemTextField);

		topPanel.add(new Label(""));
		
		Label origanalBqQtyLabel = new Label("Original BQ Qty");
		origanalBqQtyLabel.setCtCls("left-align-label");
		topPanel.add(origanalBqQtyLabel);
		
		originalBqQtyTextField = new TextField();
		originalBqQtyTextField.setCtCls("right-align");
		originalBqQtyTextField.setWidth(120);
		topPanel.add(originalBqQtyTextField);

		Label subsidiaryLabel = new Label("Subsidiary");
		subsidiaryLabel.setCtCls("left-align-label");
		topPanel.add(subsidiaryLabel);
		
		subsidiaryTextField = new TextField();
		subsidiaryTextField.setCtCls("left-align");
		subsidiaryTextField.setWidth(120);
		subsidiaryTextField.setValue(subsidiaryCode);
		topPanel.add(subsidiaryTextField);

		topPanel.add(new Label(""));

		Label toBeApprovedQtyLabel = new Label("To Be Approved Qty");
		toBeApprovedQtyLabel.setCtCls("left-align-label");
		topPanel.add(toBeApprovedQtyLabel);
		
		toBeApprovedQtyTextField = new TextField();
		toBeApprovedQtyTextField.setCtCls("right-align");
		toBeApprovedQtyTextField.setWidth(120);
		topPanel.add(toBeApprovedQtyTextField);

		Label objectLabel = new Label("Object");
		objectLabel.setCtCls("left-align-label");
		topPanel.add(objectLabel);
		
		objectTextField = new TextField();
		objectTextField.setCtCls("left-align");
		objectTextField.setWidth(120);
		objectTextField.setValue(objectCode);
		topPanel.add(objectTextField);
		
		topPanel.add(new Label(""));

		Label toBeApprovedRateLabel = new Label("To Be Approved Rate");
		toBeApprovedRateLabel.setCtCls("left-align-label");
		topPanel.add(toBeApprovedRateLabel);
		
		toBeApprovedRateTextField = new TextField();
		toBeApprovedRateTextField.setCtCls("right-align");
		toBeApprovedRateTextField.setWidth(120);
		topPanel.add(toBeApprovedRateTextField);

		Label ecaRateLabel = new Label("ECA Rate");
		ecaRateLabel.setCtCls("left-align-label");
		topPanel.add(ecaRateLabel);

		ecaRateTextField = new TextField();
		ecaRateTextField.setCtCls("right-align");
		ecaRateTextField.setWidth(120);
		topPanel.add(ecaRateTextField);

		topPanel.add(new Label(""));
		
		Label toBeApprovedAmountLabel = new Label("To Be Approved Amount");
		toBeApprovedAmountLabel.setCtCls("left-align-label");
		topPanel.add(toBeApprovedAmountLabel);
		
		toBeApprovedAmountTextField = new TextField();
		toBeApprovedAmountTextField.setCtCls("right-align");
		toBeApprovedAmountTextField.setWidth(120);
		topPanel.add(toBeApprovedAmountTextField);
		
		Label bqQuantityLabel = new Label("BQ Quantity");
		bqQuantityLabel.setCtCls("left-align-label");
		topPanel.add(bqQuantityLabel);
		
		bqQuanitityTextField = new TextField();
		bqQuanitityTextField.setCtCls("right-align");
		bqQuanitityTextField.setWidth(120);
		topPanel.add(bqQuanitityTextField);

		topPanel.add(new Label(""));
		topPanel.add(new Label(""));
		topPanel.add(new Label(""));

		Label scRateLabel = new Label("SC Rate");
		scRateLabel.setCtCls("left-align-label");
		topPanel.add(scRateLabel);
		
		scRateTextField = new TextField();
		scRateTextField.setCtCls("right-align");
		scRateTextField.setWidth(120);
		topPanel.add(scRateTextField);

		topPanel.add(new Label(""));

		Label corrSCNoLabel = new Label("Corr SC #");
		corrSCNoLabel.setCtCls("left-align-label");
		topPanel.add(corrSCNoLabel);
		
		corrSCNoTextField = new TextField();
		corrSCNoTextField.setCtCls("left-align");
		corrSCNoTextField.setWidth(120);
		topPanel.add(corrSCNoTextField);

		Label totalAmountLabel = new Label("Total Amount");
		totalAmountLabel.setCtCls("left-align-label");
		topPanel.add(totalAmountLabel);
		
		totalAmountTextField = new TextField();
		totalAmountTextField.setCtCls("right-align");
		totalAmountTextField.setWidth(120);
		topPanel.add(totalAmountTextField);

		topPanel.add(new Label(""));

		Label altObjectCodeLabel = new Label("Alt Object Code");
		altObjectCodeLabel.setCtCls("left-align-label");
		topPanel.add(altObjectCodeLabel);
		
		altObjectCodeTextField = new TextField();
		altObjectCodeTextField.setCtCls("left-align");
		altObjectCodeTextField.setWidth(120);
		topPanel.add(altObjectCodeTextField);

		topTablePanel.add(topPanel);

		Label bqBriefDescriptionLabel = new Label("BQ Brief Description");
		bqBriefDescriptionLabel.setCtCls("left-align-label");
		bqBriefDescriptionLabel.setWidth(135);		
		forthPanel.add(bqBriefDescriptionLabel);
				
		bqBriefDescriptionTextField = new TextField();
		bqBriefDescriptionTextField.setCtCls("left-align");
		bqBriefDescriptionTextField.setWidth(500);		
		forthPanel.add(bqBriefDescriptionTextField);
		
		Label remarkLabel = new Label("Remark");
		remarkLabel.setCtCls("left-align-label");
		forthPanel.add(remarkLabel);
		
		remarkTextField = new TextField();
		remarkTextField.setCtCls("left-align");
		remarkTextField.setWidth(500);
		forthPanel.add(remarkTextField);

		topTablePanel.add(forthPanel);

		Label prevWorkDoneLabel = new Label("Prev Month Work Done");
		prevWorkDoneLabel.setCtCls("left-align-label");
		prevWorkDoneLabel.setWidth(135);
		fifthPanel.add(prevWorkDoneLabel);
		
		prevWorkDoneTextField = new TextField();
		prevWorkDoneTextField.setCtCls("right-align");
		prevWorkDoneTextField.setWidth(120);
		fifthPanel.add(prevWorkDoneTextField);

		Label prevCertLabel = new Label("Prev Month Cert");
		prevCertLabel.setCtCls("left-align-label");
		prevCertLabel.setWidth(110);
		fifthPanel.add(prevCertLabel);
		
		prevCertTextField = new TextField();
		prevCertTextField.setCtCls("right-align");
		prevCertTextField.setWidth(120);
		fifthPanel.add(prevCertTextField);

		fifthPanel.add(new Label(""));
		fifthPanel.add(new Label(""));

		Label thisWorkDoneLabel = new Label("This Month Work Done");
		thisWorkDoneLabel.setCtCls("left-align-label");
		fifthPanel.add(thisWorkDoneLabel);
		
		thisWorkDoneTextField = new TextField();
		thisWorkDoneTextField.setCtCls("right-align");
		thisWorkDoneTextField.setWidth(120);
		fifthPanel.add(thisWorkDoneTextField);

		Label thisCertLabel = new Label("This Month Cert");
		thisCertLabel.setCtCls("left-align-label");
		fifthPanel.add(thisCertLabel);
		
		thisCertTextField = new TextField();
		thisCertTextField.setCtCls("right-align");
		thisCertTextField.setWidth(120);
		fifthPanel.add(thisCertTextField);

		fifthPanel.add(new Label(""));
		fifthPanel.add(new Label(""));

		Label cumWorkDoneLabel = new Label("Cum Month Work Done");
		cumWorkDoneLabel.setCtCls("left-align-label");
		fifthPanel.add(cumWorkDoneLabel);
		
		cumWorkDoneTextField = new TextField();
		cumWorkDoneTextField.setCtCls("right-align");
		cumWorkDoneTextField.setWidth(120);
		fifthPanel.add(cumWorkDoneTextField);

		Label cumCertLabel = new Label("Cum Month Cert");
		cumCertLabel.setCtCls("left-align-label");
		fifthPanel.add(cumCertLabel);
		
		cumCertTextField = new TextField();
		cumCertTextField.setCtCls("right-align");
		cumCertTextField.setWidth(120);
		fifthPanel.add(cumCertTextField);

		fifthPanel.add(new Label(""));
		fifthPanel.add(new Label(""));

		Label cumLiabilityAmountLabel = new Label("Cum Liability Amount");
		cumLiabilityAmountLabel.setCtCls("left-align-label");
		fifthPanel.add(cumLiabilityAmountLabel);
		
		cumLiabilityAmountTextField = new TextField();
		cumLiabilityAmountTextField.setCtCls("right-align");
		cumLiabilityAmountTextField.setWidth(120);
		fifthPanel.add(cumLiabilityAmountTextField);

		Label cumCertAmountLabel = new Label("Cum Cert Amount");
		cumCertAmountLabel.setCtCls("left-align-label");
		fifthPanel.add(cumCertAmountLabel);
		
		cumCertAmountTextField = new TextField();
		cumCertAmountTextField.setCtCls("right-align");
		cumCertAmountTextField.setWidth(120);
		fifthPanel.add(cumCertAmountTextField);

		Label provisionAmountLabel = new Label("Provision Amount");
		provisionAmountLabel.setCtCls("left-align-label");
		provisionAmountLabel.setWidth(110);
		fifthPanel.add(provisionAmountLabel);
		
		provisionAmountTextField = new TextField();
		provisionAmountTextField.setCtCls("right-align");
		provisionAmountTextField.setWidth(120);
		fifthPanel.add(provisionAmountTextField);
		
		topTablePanel.add(fifthPanel);

		mainPanel.add(topTablePanel);

		jobNumberTextField.disable();
		scNumberTextField.disable();
		seqNoTextField.disable();
		scLineTypeTextField.disable();
		bqItemTextField.disable();
		originalBqQtyTextField.disable();
		objectTextField.disable();
		ecaRateTextField.disable();
		toBeApprovedAmountTextField.disable();
		totalAmountTextField.disable();
		prevWorkDoneTextField.disable();
		thisWorkDoneTextField.disable();
		cumWorkDoneTextField.disable();
		prevCertTextField.disable();
		thisCertTextField.disable();
		cumCertTextField.disable();
		cumLiabilityAmountTextField.disable();
		cumCertAmountTextField.disable();
		provisionAmountTextField.disable();

		unitComboBox.disable();
		subsidiaryTextField.disable();
		toBeApprovedQtyTextField.disable();
		toBeApprovedRateTextField.disable();
		bqQuanitityTextField.disable();
		scRateTextField.disable();
		corrSCNoTextField.disable();
		altObjectCodeTextField.disable();
		bqBriefDescriptionTextField.disable();
		remarkTextField.disable();
		
		this.add(mainPanel);
		suspendWindowButton = new Button("Suspend");
			suspendWindowButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					
					String jobNumber = jobNumberTextField.getText().trim();
					String packageNo = scNumberTextField.getText().trim();
					String sequenceNo = seqNoTextField.getText().trim();
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.suspendAddendum(jobNumber, packageNo, sequenceNo, new AsyncCallback<String>() {
						public void onSuccess(String result) {	
							//UIUtil.alert("Not approved".trim());
							if(result!=null){
								if("Suspended".trim().equalsIgnoreCase(result.trim())){
									MessageBox.alert("Addendum has been suspended");
									approvalLabel.setText("Suspended");
									suspendWindowButton.setText("Resume");
							}else if("Not approved".equalsIgnoreCase(result.trim())){
								MessageBox.alert("Addendum status has been resumed to Not Approved");
								approvalLabel.setText("Not approved");
								suspendWindowButton.setText("Suspend");}
							}
							else{
								MessageBox.alert(result);}
						}
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
							UIUtil.unmaskPanelById(EDIT_ADDENDUM_ID);
						}
						
					});
				};
			});
		this.addButton(suspendWindowButton);
		
		deleteWindowButton = new Button("Delete");
		deleteWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				
				// added by Brian on 20110112
				MessageBox.confirm("Confirmation", "Please confirm to delete Addendum.", new MessageBox.ConfirmCallback(){
					public void execute(String btnID) {
						if(btnID.equals("yes"))
							delete();
						
						if(btnID.equals("no"))
							return;
					}
				});
			}
		});
		
				////////////////////////////
				
//				globalMessageTicket.refresh();
//				String jobNumber = jobNumberTextField.getText().trim();
//				String packageNo = scNumberTextField.getText().trim();
//				String sequenceNo = seqNoTextField.getText().trim();
//				SessionTimeoutCheck.renewSessionTimer();
//				packageRepository.deleteAddendum(jobNumber, packageNo, sequenceNo, new AsyncCallback<String>() {
//
//					public void onSuccess(String result) {			
//
//						if(result==null){
//							globalSectionController.closeCurrentWindow();
//							MessageBox.alert(" has been successfully deleted");
//						}
//						else
//							MessageBox.alert(result);
//					}
//
//					public void onFailure(Throwable e) {
//						UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
//						UIUtil.unmaskPanelById(EDIT_ADDENDUM_ID);
//					}
//				});
//			};
//		});
				
		this.addButton(deleteWindowButton);
		
		Button saveWindowButton = new Button("Save");
		saveWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					updateAddendum();
				};
		});
		this.addButton(saveWindowButton);
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalSectionController.closeCurrentWindow();				
				};
		});		

		this.addButton(closeWindowButton);

	}
	
	public void populate(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode)
	{
		UIUtil.maskMainPanel();		
/*		UIUtil.maskPanelById(ADD_ADDENDUM_ID, GlobalParameter.LOADING_MSG, true);*/
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getSCLine(jobNumber, subcontractNumber, sequenceNumber, billItem, resourceNumber, subsidiaryCode, objectCode, new AsyncCallback<SCDetails>(){
			
			public void onSuccess(SCDetails result) {
				
				UIUtil.unmaskMainPanel();
				
				try{

					AmountRenderer amountRenderer = new AmountRenderer(EditSCAddendumWindow.this.globalSectionController.getUser());
					QuantityRenderer quantityRenderer = new QuantityRenderer(EditSCAddendumWindow.this.globalSectionController.getUser());
					RateRenderer rateRenderer = new RateRenderer(EditSCAddendumWindow.this.globalSectionController.getUser());

					String scLineType = "";
					if (result.getLineType()!=null) {
						scLineType = result.getLineType().trim();
					}

					scLineTypeTextField.setValue(scLineType);
					bqBriefDescriptionTextField.setValue(result.getDescription()!=null? result.getDescription().trim():"");
					if (result instanceof SCDetailsBQ) {
						ecaRateTextField.setValue(((SCDetailsBQ)result).getCostRate()!=null?rateRenderer.render(((SCDetailsBQ)result).getCostRate().toString()):"");
						toBeApprovedQtyTextField.setValue(((SCDetailsBQ)result).getToBeApprovedQuantity()!=null?quantityRenderer.render(((SCDetailsBQ)result).getToBeApprovedQuantity().toString()):"");
						totalAmountTextField.setValue(((SCDetailsBQ)result).getTotalAmount()!=null?amountRenderer.render(((SCDetailsBQ)result).getTotalAmount().toString()):"");
						ecaRateTextField.setValue(((SCDetailsBQ)result).getCostRate()!=null? rateRenderer.render(((SCDetailsBQ)result).getCostRate().toString()):"");
					}
					
					originalBqQtyTextField.setValue(result.getOriginalQuantity()!=null?quantityRenderer.render(result.getOriginalQuantity().toString()):"");

					unitComboBox.setValue(result.getUnit());

					bqQuanitityTextField.setValue(result.getQuantity()!=null?quantityRenderer.render(result.getQuantity().toString()):"");
					scRateTextField.setValue(result.getScRate()!=null?rateRenderer.render(result.getScRate().toString()):"");
					if (result instanceof SCDetailsVO) {
						toBeApprovedRateTextField.setValue(((SCDetailsVO)result).getToBeApprovedRate()!=null?rateRenderer.render(((SCDetailsVO)result).getToBeApprovedRate().toString()):"");
						corrSCNoTextField.setValue(((SCDetailsVO)result).getContraChargeSCNo()!=null?((SCDetailsVO)result).getContraChargeSCNo().toString():"");	
					}
					if (result instanceof SCDetailsCC) {
						corrSCNoTextField.setValue(((SCDetailsCC)result).getContraChargeSCNo()!=null?((SCDetailsCC)result).getContraChargeSCNo().toString():"");
						altObjectCodeTextField.setValue(((SCDetailsCC)result).getAltObjectCode()!=null? ((SCDetailsCC)result).getAltObjectCode().trim():"");
					}
					
					remarkTextField.setValue(result.getRemark()!=null? result.getRemark().trim():"");
					
					Double toBeApproveAmount = new Double(0);
	
					if ( result instanceof SCDetailsVO){
						toBeApproveAmount = ((SCDetailsVO)result).getToBeApprovedAmount();
						altObjectCodeTextField.setValue(result.getAltObjectCode());
					}
					else 
						if (result instanceof SCDetailsBQ)
							if (result.getScRate()==0)
								toBeApproveAmount = ((SCDetailsBQ)result).getCostRate()*((SCDetailsBQ)result).getToBeApprovedQuantity();
							else
								toBeApproveAmount = ((SCDetailsBQ)result).getToBeApprovedAmount();
/*					if(scLineType=="BQ") {
						if(result.getScRate()==0) {
							if (result.getToBeApprovedQuantity()==null ||result.getEcaRate()==null )
								;
							else
								toBeApproveAmount = result.getToBeApprovedQuantity()*result.getEcaRate();
							
						} else {
							if (result.getToBeApprovedQuantity()==null || result.getScRate()==null)
								;
							else
								toBeApproveAmount = result.getToBeApprovedQuantity()*result.getScRate();
						}
					} else if(scLineType=="B1") {
						if (result.getToBeApprovedQuantity()==null || result.getScRate()==null)
							;
						else
							toBeApproveAmount = result.getToBeApprovedQuantity()*result.getScRate();
					}*/ 
//					else if(scLineType!="AP" && scLineType!="C1" && scLineType!="C2" && scLineType!="MS" && scLineType!="OA" && scLineType!="RR" && result.getToBeApprovedAmount()!=null) {
					
	
					toBeApprovedAmountTextField.setValue(toBeApproveAmount!=null?amountRenderer.render(toBeApproveAmount.toString()):"");
	
					Double cumLiabilityAmount = new Double(0);
					Double cumCertAmount = new Double(0);
	
/*					if(result.getBalanceType()!=null && "%".equals(result.getBalanceType().trim())) {
						cumLiabilityAmount = result.getCumWorkDoneQuantity()*result.getQuantity()*result.getScRate()/100;
						cumCertAmount = result.getCumCertifiedQuanity()*result.getQuantity()*result.getScRate()/100;
					} else*/
					Double thisWorkDone = null;
					String zeroWorkDoneCert = "N";
					if (result instanceof SCDetailsOA){
						cumLiabilityAmount = ((SCDetailsOA)result).getCumWorkDoneQuantity()*result.getScRate();
						prevWorkDoneTextField.setValue(((SCDetailsOA)result).getPostedWorkDoneQuantity()!=null?quantityRenderer.render(((SCDetailsOA)result).getPostedWorkDoneQuantity().toString()):"");
						cumWorkDoneTextField.setValue(((SCDetailsOA)result).getCumWorkDoneQuantity()!=null?quantityRenderer.render(((SCDetailsOA)result).getCumWorkDoneQuantity().toString()):"");
						thisWorkDone = new Double(((SCDetailsOA)result).getCumWorkDoneQuantity()-((SCDetailsOA)result).getPostedWorkDoneQuantity());
						if ((((SCDetailsOA)result).getPostedWorkDoneQuantity()==null || ((SCDetailsOA)result).getPostedWorkDoneQuantity()==0) && ((SCDetailsOA)result).getCumWorkDoneQuantity()==0 && result.getPostedCertifiedQuantity()==0 && result.getCumCertifiedQuantity()==0) 
							zeroWorkDoneCert = "Y"; 
					}
					cumCertAmount = result.getCumCertifiedQuantity()*result.getScRate();

					//thisWorkDoneTextField.setValue(thisWorkDone!=null?quantityRenderer.render(thisWorkDone.toString()):"");
					
					//TESTING
					thisWorkDoneTextField.setValue(quantityRenderer.render(thisWorkDone!=null?thisWorkDone.toString():"0"));
					
					
					cumLiabilityAmountTextField.setValue(cumLiabilityAmount!=null?amountRenderer.render(cumLiabilityAmount.toString()):"");
					prevCertTextField.setValue(result.getPostedCertifiedQuantity()!=null?quantityRenderer.render(result.getPostedCertifiedQuantity().toString()):"");
					cumCertTextField.setValue(result.getCumCertifiedQuantity()!=null?quantityRenderer.render(result.getCumCertifiedQuantity().toString()):"");
					Double thisCert = new Double((result.getCumCertifiedQuantity()==null?0:result.getCumCertifiedQuantity())-(result.getPostedCertifiedQuantity()==null?0:result.getPostedCertifiedQuantity()));
					thisCertTextField.setValue(thisCert!=null?quantityRenderer.render(thisCert.toString()):"");
					cumCertAmountTextField.setValue(cumCertAmount!=null?amountRenderer.render(cumCertAmount.toString()):"");
					Double provisionAmount = new Double(cumLiabilityAmount-cumCertAmount);
					provisionAmountTextField.setValue(provisionAmount!=null?amountRenderer.render(provisionAmount.toString()):"");
	
					if (result.getApproved()==SCDetails.APPROVED){
						approvalLabel.setText("Approved");
						suspendWindowButton.disable();}
					else if (result.getApproved()==SCDetails.SUSPEND){
							approvalLabel.setText("Suspend");
							suspendWindowButton.setText("Resume");}
							else{
								approvalLabel.setText("Not approved");
								suspendWindowButton.setText("Suspend");
								}
					
					

	
		            if (scLineType== "BQ" || scLineType== "B1") {
		            	if (result.getScRate()!=0) {
			        		remarkTextField.enable();
		            	}
		            }
		            else if (scLineType== "C1") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		subsidiaryTextField.enable();
		            	}
		            	if (zeroWorkDoneCert=="Y") {
		            		unitComboBox.enable();
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		bqQuanitityTextField.enable();
		        		scRateTextField.enable();
		        		remarkTextField.enable();
		            }
		            else if (scLineType==  "C2") {
		            	if (zeroWorkDoneCert=="Y") {
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		remarkTextField.enable();
		            }
		            else if (scLineType==  "MS") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		        		bqQuanitityTextField.enable();
		        		scRateTextField.enable();
		        		remarkTextField.enable();
		        	}
		            else if (scLineType== "D1") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		subsidiaryTextField.enable();
			        		altObjectCodeTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "D2") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		altObjectCodeTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED && ((SCDetailsVO)result).getContraChargeSCNo()=="") {
			        		corrSCNoTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "L1") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "L2") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED && ((SCDetailsVO)result).getContraChargeSCNo()=="") {
			        		corrSCNoTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "V1") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            		unitComboBox.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
//		            	if(result.getResourceNo() == null || result.getResourceNo().intValue() == 0)
		            	if (result.getCostRate()==null || result.getCostRate().intValue()==0)
		            		toBeApprovedQtyTextField.enable();
		            	if (zeroWorkDoneCert=="Y" )
		            		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "V2") {
		            	if (zeroWorkDoneCert=="Y") {
			            	if ( result.getApproved()!=SCDetails.APPROVED) {
				        		bqBriefDescriptionTextField.enable();
			            		unitComboBox.enable();
			            	}
			        		toBeApprovedRateTextField.enable();			            	
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        	}
		            else if (scLineType== "V3") {
		            	if (zeroWorkDoneCert=="Y" && result.getApproved()!=SCDetails.APPROVED) {
			        		bqBriefDescriptionTextField.enable();
		            	}
		            	if (result.getApproved()!=SCDetails.APPROVED) {
			        		remarkTextField.enable();
		            	}
		        		toBeApprovedRateTextField.enable();
		        	}
		            else if (scLineType== "BS") {
		            	if (zeroWorkDoneCert=="Y") {
		            		unitComboBox.enable();
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        		remarkTextField.enable();
		        	}
		            else if (scLineType== "CF") {
		            	if (zeroWorkDoneCert=="Y") {
		            		unitComboBox.enable();
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		toBeApprovedQtyTextField.enable();
		        		toBeApprovedRateTextField.enable();
		        		remarkTextField.enable();
		        	}
		            else if (scLineType== "AP") {
		            	if (zeroWorkDoneCert=="Y") {
		            		unitComboBox.enable();
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		bqQuanitityTextField.enable();
		        		scRateTextField.enable();
		        		remarkTextField.enable();
		        	}
		            else if (scLineType== "OA") {
		            	if (zeroWorkDoneCert=="Y") {
		            		unitComboBox.enable();
			        		subsidiaryTextField.enable();
			        		bqBriefDescriptionTextField.enable();
		            	}
		        		bqQuanitityTextField.enable();
		        		scRateTextField.enable();
		        		remarkTextField.enable();
		        	}
				}catch(Exception e)	{
					UIUtil.alert(e.getMessage());
				}
			}
			
			public void onFailure(Throwable e) {
				
				UIUtil.unmaskMainPanel();
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
			
		});
		
	}
	
	private void updateAddendum() {
		UpdateAddendumWrapper wrapper = new UpdateAddendumWrapper();

		try {
			Double bqQuantity = new Double(this.bqQuanitityTextField.getValueAsString().trim().replaceAll(",", ""));
			wrapper.setBqQuantity(bqQuantity);
		} catch (Exception e) {
			MessageBox.alert("BQ Quantity should be number");
			return;
		}

		try {
			Double scRate = new Double(this.scRateTextField.getValueAsString().trim().replaceAll(",", ""));
			wrapper.setScRate(scRate);
		} catch (Exception e) {
			MessageBox.alert("SC Rate should be number");
			return;
		}
		

		try {
			Double toBeApprovedQty;
			if (this.toBeApprovedQtyTextField == null || this.toBeApprovedQtyTextField.getValueAsString().equals(""))
				toBeApprovedQty = 0.0;
			else
				toBeApprovedQty = new Double(this.toBeApprovedQtyTextField.getValueAsString().trim().replaceAll(",", ""));

			wrapper.setToBeApprovedQty(toBeApprovedQty);
		} catch (Exception e) {
			MessageBox.alert("To Be Approved Quantity should be number");
			return;
		}
		

		try {
			Double toBeApprovedRate;
			if (this.toBeApprovedRateTextField == null || this.toBeApprovedRateTextField.getValueAsString().trim().equals(""))
				toBeApprovedRate = 0.0;
			else
				toBeApprovedRate = new Double(this.toBeApprovedRateTextField.getValueAsString().trim().replaceAll(",", ""));

			wrapper.setToBeApprovedRate(toBeApprovedRate);
		} catch (Exception e) {
			MessageBox.alert("To Be Approved Rate should be number");
			return;
		}

		try {
			Integer corrSCNo = new Integer(0);
			if (!this.corrSCNoTextField.getValueAsString().trim().equals(""))
				corrSCNo = new Integer(this.corrSCNoTextField.getValueAsString().trim());

			wrapper.setCorrSCNo(corrSCNo);
		} catch (Exception e) {
			MessageBox.alert("Corr SC # should be number");
			return;
		}

		try {
			wrapper.setJobNumber(this.jobNumberTextField.getValueAsString());
			wrapper.setSubcontractNo(new Integer(this.scNumberTextField.getValueAsString()));
			wrapper.setSequenceNo(new Integer(this.seqNoTextField.getValueAsString()));
			wrapper.setResourceNo(new Integer(this.resourceNoTextField.getValueAsString()));
			wrapper.setBillItem(this.bqItemTextField.getValueAsString());
			wrapper.setObject(this.objectTextField.getValueAsString());
			wrapper.setSubsidiary(this.subsidiaryTextField.getValueAsString());
			wrapper.setBqDescription(this.bqBriefDescriptionTextField.getValueAsString());
			wrapper.setUnit(this.unitComboBox.getValueAsString().substring(0, 2));
			wrapper.setAltObjectCode(altObjectCodeTextField.getValueAsString());
			wrapper.setRemark(this.remarkTextField.getValueAsString());
		} catch (Exception e) {
		}

		/* added by matthewlam, 20150212
		 * Bug fix #98 - Negative V2 should not be allowed as the addendum can be updated
		 * a * b < 0 should be avoided for potential stackunderflow/stackoverflow issue
		 * expression a < 0 != b < 0 is applied to check a * b < 0,
		 * ** the exepression must not be used for comparing doubles
		 *		unless absence of -0.0 is guaranteed (-0.0 is converted to 0.0 via web form)
		 *		since -0.0 < 0
		 */
		/*if (scLineTypeTextField.getValueAsString().equals("V2")
				&& (wrapper.getToBeApprovedQty() < 0 != wrapper.getToBeApprovedRate() < 0)) { 
			MessageBox.alert("Total amount of V2 must not be negative");
			return;
		}*/

		UIUtil.maskPanelById(EDIT_ADDENDUM_ID, "saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.updateAddendumByWrapper(wrapper, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

				if (result.booleanValue()) {
					MessageBox.alert("Addendum updated successfully");
					globalSectionController.closeCurrentWindow();
				} else
					MessageBox.alert("Fail to update addendum");

				UIUtil.unmaskPanelById(EDIT_ADDENDUM_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
				UIUtil.unmaskPanelById(EDIT_ADDENDUM_ID);
			}

		});

	}
	
	// added by brian
	// for add confirmation box when delete
	private void delete(){
		globalMessageTicket.refresh();
		String jobNumber = jobNumberTextField.getText().trim();
		String packageNo = scNumberTextField.getText().trim();
		String sequenceNo = seqNoTextField.getText().trim();
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.deleteAddendum(jobNumber, packageNo, sequenceNo, new AsyncCallback<String>() {

			public void onSuccess(String result) {			

				if(result==null){
					globalSectionController.closeCurrentWindow();
					MessageBox.alert(" has been successfully deleted");
				}
				else
					MessageBox.alert(result);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(EDIT_ADDENDUM_ID);
			}
		});
	}
}
