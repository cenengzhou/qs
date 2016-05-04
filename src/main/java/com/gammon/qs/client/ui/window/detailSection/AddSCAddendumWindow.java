package com.gammon.qs.client.ui.window.detailSection;


import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.util.DelayedTask;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class AddSCAddendumWindow extends Window {
	private GlobalSectionController globalSectionController;
	
	
	//UI
	private TextField jobNumberTextField;
	private Label jobDescriptionLabel;
	private TextField scNumberTextField;
	private ComboBox scLineTypeComboBox;
	private Label scLineTypeDescriptionLabel;
	private TextField bqBriefDescriptionTextField;
	private TextField seqNoTextField;
	private TextField billNoTextField;
	private TextField bqItemTextField;
	private TextField objectTextField;
	private TextField subsidiaryTextField;
	
	private ComboBox unitComboBox;
	private Label unitDescriptionLabel;
	private TextField bqQuanitityTextField;
	private TextField scRateTextField;
	private TextField totalAmountTextField;
	
	private TextField corrSCNoTextField;
	private TextField altObjectCodeTextField;
	private TextField remarkTextField;
	private Button saveWindowButton;
	
	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;
	
	private GlobalMessageTicket globalMessageTicket;
	
	//ID
	private String ADD_ADDENDUM_ID = "addAddendum_window_Id";
	
	
	public AddSCAddendumWindow( final GlobalSectionController globalSectionController, String jobNumber, String jobDescription, Integer subcontractNumber, boolean paymentRequisition){
		super();
		this.globalSectionController = globalSectionController;
		
		packageRepository = globalSectionController.getPackageRepository();
		
		globalMessageTicket = new GlobalMessageTicket();
		setTitle("Subcontract Addendum (Add)");
		setPaddings(2);
		setWidth(800);
		setHeight(650);
		setModal(true);
		setClosable(false);
		setLayout(new FitLayout());
		setId(ADD_ADDENDUM_ID);
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		//mainPanel.setId(ADD_ADDENDUM_ID);
		
		Panel topPanel = new Panel();
		topPanel.setLayout(new TableLayout(5));
		topPanel.setFrame(true);
		topPanel.setHeight(100);
		
		Label jobNumberLabel = new Label("Job Number");
		jobNumberLabel.setCtCls("table-cell");
		topPanel.add(jobNumberLabel);
		
		jobNumberTextField =  new TextField();
		jobNumberTextField.disable();
		jobNumberTextField.setWidth(80);
		jobNumberTextField.setCtCls("table-cell");
		jobNumberTextField.setValue(jobNumber);
		topPanel.add(jobNumberTextField);
		
		
		jobDescriptionLabel = new Label();
		jobDescriptionLabel.setCtCls("table-description-cell");
		jobDescriptionLabel.setText(jobDescription);
		topPanel.add(jobDescriptionLabel);
		
		Label scNumberLabel = new Label("Subcontract Number");
		scNumberLabel.setCtCls("table-cell");
		topPanel.add(scNumberLabel);

	
		scNumberTextField = new TextField();
		scNumberTextField.disable();
		scNumberTextField.setWidth(80);
		scNumberTextField.setCtCls("table-cell");
		scNumberTextField.setValue(subcontractNumber.toString());
		topPanel.add(scNumberTextField);
				
		Label scLineTypeLabel = new Label("SC Line Type");
		scLineTypeLabel.setCtCls("table-cell");
		topPanel.add(scLineTypeLabel);
		
		
		scLineTypeComboBox = new ComboBox();		
		scLineTypeComboBox.setDisplayField("description");
		scLineTypeComboBox.setCtCls("table-cell");
		scLineTypeComboBox.setSelectOnFocus(true);
		scLineTypeComboBox.setWidth(180);
		
		Store scLineTypeStore=null;
		if(paymentRequisition){
			String[][] scLineTypeData = new String[][]{
					new String[]{"1","RR",	"RR - Release Retention"},
			};

			scLineTypeStore = new SimpleStore(new String[]{"id","lineType","description"},scLineTypeData);
		}else{
			scLineTypeStore = globalSectionController.getScLineTypeStore();
			scLineTypeStore.load();
		}
		
		scLineTypeComboBox.setStore(scLineTypeStore);
		
		//Add the Control for add addendum
		//By Peter Chan
		scLineTypeComboBox.addListener(new ComboBoxListenerAdapter(){
			
			public void onChange(Field field, java.lang.Object newVal,java.lang.Object oldVal) {
				UIUtil.maskPanelById(AddSCAddendumWindow.this.getId(), "Loading...", true);
			    Timer t = new Timer() {
			        public void run() {	
		
						Object newVal= scLineTypeComboBox.getValue().toUpperCase();
						SessionTimeoutCheck.renewSessionTimer();
						packageRepository.defaultValuesForAddingSCDetailLines(jobNumberTextField.getText(),new Integer(scNumberTextField.getText()), newVal.toString().substring(0, newVal.toString().indexOf(" ")).trim(), new AsyncCallback<com.gammon.qs.domain.SCDetails>(){
							
							public void onFailure(Throwable e) {
								resetFields();
								UIUtil.unmaskPanelById(AddSCAddendumWindow.this.getId());
								UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
							}
		
							
							public void onSuccess(SCDetails scDetail) {
								UIUtil.unmaskPanelById(AddSCAddendumWindow.this.getId());
								resetFields();
								if (scDetail.getDescription()!=null && scDetail.getDescription().length()>0) {
									UIUtil.alert(scDetail.getDescription());
									objectTextField.setDisabled(false);
									subsidiaryTextField.setDisabled(false);
									seqNoTextField.setDisabled(false);
									bqItemTextField.setDisabled(false);
									scLineTypeComboBox.focus();
								}else{
									String lineType = scLineTypeComboBox.getText().substring(0, 2).trim(); 
									bqItemTextField.setValue(scDetail.getBillItem());
									bqItemTextField.setDisabled(true);
									seqNoTextField.setValue(scDetail.getSequenceNo().toString());
									seqNoTextField.setDisabled(true);
									objectTextField.setValue(scDetail.getObjectCode());
									subsidiaryTextField.setValue(scDetail.getSubsidiaryCode());
									subsidiaryTextField.setDisabled(true);
									billNoTextField.setDisabled(true);
									billNoTextField.setValue(scDetail.getBillItem().substring(0, 2));
									if (lineType.equals("C1"))
										objectTextField.setDisabled(false);
									else
										objectTextField.setDisabled(true);
									if (lineType.equals("D2")||lineType.equals("L2"))
										corrSCNoTextField.setDisabled(false);
									else
										corrSCNoTextField.setDisabled(true);
									if (lineType.equals("D2")||lineType.equals("D1"))
										altObjectCodeTextField.setDisabled(false);
									else
										altObjectCodeTextField.setDisabled(true);
									if (lineType.equals("RR")||lineType.equals("RA")){
										bqQuanitityTextField.setDisabled(true);
										scRateTextField.setDisabled(true);
										bqQuanitityTextField.setValue("1");
										if (lineType.equals("RR"))
											scRateTextField.setValue("-1");
										else
											scRateTextField.setValue("1");
									}else{
										bqQuanitityTextField.setDisabled(false);
										scRateTextField.setDisabled(false);
									}
									if (lineType.equals("RR")||lineType.equals("RA")||lineType.equals("MS")||lineType.equals("CF"))
										subsidiaryTextField.setDisabled(true);
									else
										subsidiaryTextField.setDisabled(false);
									saveWindowButton.setDisabled(false);
										
								}
								
							}
							
						});
						
			        }
			    };
			    t.schedule(1000);
			}
		});
		topPanel.add(scLineTypeComboBox);		
		
		scLineTypeDescriptionLabel = new Label();
		scLineTypeDescriptionLabel.setCtCls("table-description-cell");
		topPanel.add(scLineTypeDescriptionLabel);
		
				
		mainPanel.add(topPanel);
		
		Panel centerPanel = new Panel();
		centerPanel.setLayout(new HorizontalLayout(2));
		centerPanel.setFrame(true);
		centerPanel.setHeight(280);
		
		Panel generalInfoPanel = new Panel();
		generalInfoPanel.setWidth(760);
		generalInfoPanel.setHeight(255);
		generalInfoPanel.setLayout(new TableLayout(2));
		generalInfoPanel.setTitle("General Information");
		generalInfoPanel.setFrame(true);
		
		Label bqBriefDescriptionLabel = new Label("BQ Brief Description");
		bqBriefDescriptionLabel.setCtCls("table-cell");
		generalInfoPanel.add(bqBriefDescriptionLabel);
				
		bqBriefDescriptionTextField = new TextField();
		bqBriefDescriptionTextField.setWidth(375);		
		bqBriefDescriptionTextField.setMaxLength(255);
		bqBriefDescriptionTextField.setCtCls("table-cell");
		generalInfoPanel.add(bqBriefDescriptionTextField);
		
		Label seqNoLabel = new Label("Seq Number");
		seqNoLabel.setCtCls("table-cell");		
		generalInfoPanel.add(seqNoLabel);
		
		seqNoTextField = new TextField();		
		seqNoTextField.setCtCls("table-cell");
		generalInfoPanel.add(seqNoTextField);
		
		Label billNoTextLabel = new Label("Bill Number");
		billNoTextLabel.setCtCls("table-cell");
		generalInfoPanel.add(billNoTextLabel);		
		billNoTextField  = new TextField();
		billNoTextField.setCtCls("table-cell");
		
		generalInfoPanel.add(billNoTextField);
		
		Label bqItemLabel = new Label("BQ Item");
		bqItemLabel.setCtCls("table-cell");
		generalInfoPanel.add(bqItemLabel);
		
		bqItemTextField = new TextField();
		bqItemTextField.setCtCls("table-cell");
		generalInfoPanel.add(bqItemTextField);
		
		Label objectLabel = new Label("Object");
		objectLabel.setCtCls("table-cell");
		generalInfoPanel.add(objectLabel);
		
		objectTextField = new TextField();
		objectTextField.setMaxLength(6);
		objectTextField.setMinLength(6);
		objectTextField.setCtCls("table-cell");
		generalInfoPanel.add(objectTextField);
		
		Label subsidiaryLabel = new Label("Subsidiary");
		subsidiaryLabel.setCtCls("table-cell");
		generalInfoPanel.add(subsidiaryLabel);
		
		subsidiaryTextField = new TextField();
		subsidiaryTextField.setMaxLength(8);
		subsidiaryTextField.setMinLength(8);
		subsidiaryTextField.setCtCls("table-cell");
		generalInfoPanel.add(subsidiaryTextField);
		
		centerPanel.add(generalInfoPanel);		
		mainPanel.add(centerPanel);
		
		
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new HorizontalLayout(2));
		bottomPanel.setFrame(true);
		
		Panel amountPanel = new Panel();
		amountPanel.setLayout(new TableLayout(3));
		amountPanel.setTitle("Amount");
		amountPanel.setFrame(true);
		amountPanel.setWidth(380);
		amountPanel.setHeight(180);
		
		
		
		
		Label unitLabel = new Label("Unit");
		unitLabel.setCtCls("table-cell");
		amountPanel.add(unitLabel);
		
		unitComboBox = new ComboBox();		
		unitComboBox.setDisplayField("description");		
		unitComboBox.setCtCls("table-cell");
		unitComboBox.setSelectOnFocus(true);
		unitComboBox.setForceSelection(true);
		unitComboBox.setWidth(180);		
		Store unitStore = globalSectionController.getUnitStore();
		unitStore.load();
		unitComboBox.setStore(unitStore);
		amountPanel.add(unitComboBox);
		
		unitDescriptionLabel = new Label();
		unitDescriptionLabel.setCtCls("table-description-cell");
		amountPanel.add(unitDescriptionLabel);
				
		Label bqQuantityLabel = new Label("BQ Quantity");
		bqQuantityLabel.setCtCls("table-cell");
		amountPanel.add(bqQuantityLabel);
		
		bqQuanitityTextField = new TextField();
		bqQuanitityTextField.setWidth(100);
		bqQuanitityTextField.setCtCls("table-cell");
		bqQuanitityTextField.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				AddSCAddendumWindow.this.bqQuanitityTextField.getEl().addListener("keyup", new EventCallback() {
					public void execute(EventObject e) {
						(new DelayedTask()).delay(500, new Function() {
							public void execute() {
								totalAmountCalcuation();
							}
						});
					}
				});
			}
		});
		amountPanel.add(bqQuanitityTextField);
		
		amountPanel.add(new Label(""));
		
		Label scRateLabel = new Label("SC Rate");
		scRateLabel.setCtCls("table-cell");
		amountPanel.add(scRateLabel);
		
		scRateTextField = new TextField();
		scRateTextField.setWidth(100);
		scRateTextField.setCtCls("table-cell");
		scRateTextField.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				AddSCAddendumWindow.this.scRateTextField.getEl().addListener("keyup", new EventCallback() {
					public void execute(EventObject e) {
						(new DelayedTask()).delay(500, new Function() {
							public void execute() {
								totalAmountCalcuation();
							}
						});
					}
				});
			}
		});
		amountPanel.add(scRateTextField);
		
		amountPanel.add(new Label(""));
		
		Label totalAmountLabel = new Label("Total Amount");
		totalAmountLabel.setCtCls("table-cell");			
		amountPanel.add(totalAmountLabel);
		
		totalAmountTextField = new TextField();
		totalAmountTextField.setCtCls("table-cell");
		totalAmountTextField.setWidth(100);
		totalAmountTextField.disable();
		amountPanel.add(totalAmountTextField);
		
		bottomPanel.add(amountPanel);
		
		
		Panel additionalInformationPanel = new Panel();
		additionalInformationPanel.setLayout(new TableLayout(2));
		additionalInformationPanel.setTitle("Additional Information");
		additionalInformationPanel.setWidth(375);
		additionalInformationPanel.setHeight(180);
		additionalInformationPanel.setFrame(true);
		
		
		Label corrSCNoLabel = new Label("Corr SC #");
		corrSCNoLabel.setCtCls("table-cell");
		additionalInformationPanel.add(corrSCNoLabel);
		
		corrSCNoTextField = new TextField();
		corrSCNoTextField.setCtCls("table-cell");
		corrSCNoTextField.setWidth(50);
		additionalInformationPanel.add(corrSCNoTextField);
		
		Label altObjectCodeLabel = new Label("Alt Object Code");
		altObjectCodeLabel.setCtCls("table-cell");
		additionalInformationPanel.add(altObjectCodeLabel);
		
		altObjectCodeTextField = new TextField();
		altObjectCodeTextField.setCtCls("table-cell");
		altObjectCodeTextField.setWidth(50);
		additionalInformationPanel.add(altObjectCodeTextField);
		
		Label remarkLabel = new Label("Remark");
		remarkLabel.setCtCls("table-cell");
		additionalInformationPanel.add(remarkLabel);
		
		remarkTextField = new TextField();
		remarkTextField.setCtCls("table-cell");
		remarkTextField.setWidth(200);
		additionalInformationPanel.add(remarkTextField);
		
		bottomPanel.add(additionalInformationPanel);
		
		
		mainPanel.add(bottomPanel);
		
		
		this.add(mainPanel);
		
		
		saveWindowButton = new Button("Save");
		saveWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					if (bqBriefDescriptionTextField.getValueAsString()!=null && bqBriefDescriptionTextField.getValueAsString().trim().length()<=255)
						addAddendum();
					else
						MessageBox.alert("BQ Brief Description should be less than or equal 255 characters!");
				};
		});
		saveWindowButton.setDisabled(true);
		this.addButton(saveWindowButton);
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalSectionController.closeCurrentWindow();				
				};
		});		

		this.addButton(closeWindowButton);
		
		
	}
	
	private void addAddendum() {
		AddAddendumWrapper wrapper = new AddAddendumWrapper();

		wrapper.setJobNumber(this.jobNumberTextField.getValueAsString());
		wrapper.setScbcontractNo(new Integer(this.scNumberTextField.getValueAsString()));
		wrapper.setScLineType(this.scLineTypeComboBox.getValueAsString().substring(0, 2));
		String description = bqBriefDescriptionTextField.getValueAsString();
		if (description == null || description.trim().length() == 0) {
			MessageBox.alert("Please input a brief description");
			return;
		}
		wrapper.setBqDescription(description.trim());
		String objectCode = objectTextField.getValueAsString().trim();

		if (objectCode == null || objectCode.trim().length() != 6) {
			MessageBox.alert("Please input an object code (must be 6 digits)");
			return;
		}

		if ("C1".equals(scLineTypeComboBox.getValueAsString().substring(0, 2)) || "C2".equals(scLineTypeComboBox.getValueAsString().substring(
				0,
					2))) {
			if (!"88".equals(objectCode.substring(4, 6))) {
				MessageBox.alert("The last 2 digits of object code should be ended with '88'");
				return;
			}
		}

		wrapper.setObject(objectCode);
		String subsidiaryCode = subsidiaryTextField.getValueAsString();
		if ("V1".equals(wrapper.getScLineType()) ||
			"V2".equals(wrapper.getScLineType()) ||
			"C1".equals(wrapper.getScLineType()) ||
			"OA".equals(wrapper.getScLineType()) ||
			"AP".equals(wrapper.getScLineType()) ||
			"L1".equals(wrapper.getScLineType()) ||
			"L2".equals(wrapper.getScLineType()) ||
			"D1".equals(wrapper.getScLineType()) ||
			"D2".equals(wrapper.getScLineType()) ||
			"BS".equals(wrapper.getScLineType()) ||
			"CF".equals(wrapper.getScLineType()))
			if (subsidiaryCode == null || subsidiaryCode.trim().length() != 8) {
				MessageBox.alert("Please input a subsidiary code (must be 8 digits)");
				return;
			}
		wrapper.setSubsidiary(subsidiaryCode.trim());

		wrapper.setUnit(this.unitComboBox.getValueAsString().substring(0, 2));
		wrapper.setUserID(this.globalSectionController.getUser().getUsername());

		Double bqQuantity = new Double(0);
		try {
			bqQuantity = new Double(this.bqQuanitityTextField.getValueAsString().trim());
		} catch (Exception e) {
			MessageBox.alert("BQ Quantity should be number");
			return;
		}
		wrapper.setBqQuantity(bqQuantity);

		Double scRate = new Double(0);
		try {
			scRate = new Double(this.scRateTextField.getValueAsString().trim());
		} catch (Exception e) {
			MessageBox.alert("SC Rate should be number");
			return;
		}
		wrapper.setScRate(scRate);

		Integer corrSCNo = new Integer(0);
		try {
			corrSCNo = new Integer(this.corrSCNoTextField.getValueAsString().trim());
		} catch (Exception e) {
		}
		wrapper.setCorrSCNo(corrSCNo);

		wrapper.setAltObjectCode(altObjectCodeTextField.getValueAsString());
		wrapper.setRemark(this.remarkTextField.getValueAsString());

		/*
		 * added by matthewlam, 20150212
		 * Bug fix #98 - Negative V2 should not be allowed as the addendum can be updated
		 * a * b < 0 should be avoided for potential stackunderflow/stackoverflow issue
		 * expression a < 0 != b < 0 is applied to check a * b < 0,
		 * ** the expression must not be used for comparing doubles
		 * unless absence of -0.0 is guaranteed (-0.0 is converted to 0.0 via web form)
		 * since -0.0 < 0
		 */
		/*if (wrapper.getScLineType().equals("V2")
			&& (wrapper.getBqQuantity() < 0 != wrapper.getScRate() < 0)) {
			MessageBox.alert("Total amount of V2 must not be negative");
			return;
		}*/

		UIUtil.maskPanelById(ADD_ADDENDUM_ID, "saving...", true);

		// Show the Error Message
		// By Peter Chan
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.addAddendumByWrapperStr(wrapper, new AsyncCallback<String>() {
			public void onSuccess(String result) {

				if ("".equals(result)) {
					MessageBox.alert("Addendum added successfully");
					// REFRESH
					globalSectionController.populateSCPackageMainPanelandDetailPanel(scNumberTextField.getValueAsString());

					globalSectionController.closeCurrentWindow();
				}
				else {
					MessageBox.alert(result);
				}

				UIUtil.unmaskPanelById(ADD_ADDENDUM_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
				UIUtil.unmaskPanelById(ADD_ADDENDUM_ID);
			}

		});

	}

	private void totalAmountCalcuation(){
		Double bqQuantity = new Double(0);
		Double scRate = new Double(0);
		
		try{
			bqQuantity = new Double(this.bqQuanitityTextField.getValueAsString().trim());
		}catch(Exception e){}
		try{
			scRate = new Double(this.scRateTextField.getValueAsString().trim());
		}catch(Exception e){}
		
		Double totalAmount = bqQuantity * scRate;
		
		this.totalAmountTextField.setValue(totalAmount.toString());
	}
	
	protected void resetFields(){
		bqBriefDescriptionTextField.setValue("");
		seqNoTextField.setValue("");
		billNoTextField.setValue("");
		bqItemTextField.setValue("");
		objectTextField.setValue("");
		subsidiaryTextField.setValue("");
			
		bqQuanitityTextField.setValue("");
		scRateTextField.setValue("");
		totalAmountTextField.setValue("");
			
		corrSCNoTextField.setValue("");
		altObjectCodeTextField.setValue("");
		remarkTextField.setValue("");
	}
}
