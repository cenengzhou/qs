package com.gammon.qs.client.ui.window.detailSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.EditableColorRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.updateSCPackage.UpdateSCPackageSaveWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class UpdateWDandCertQtyForPaymentRequisitionWindow extends Window {
	private static final String RESULT_PANEL_ID ="UpdateWDandCertQtyForDirectPaymentWindowResultPanel"; 
	private static final String MAIN_PANEL_ID ="UpdateWDandCertQtyForPaymentRequisitionWindow"; 

	//UI
	private GlobalSectionController globalSectionController;
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel massUpdatePanel;
	private Panel resultPanel;
	private EditorGridPanel resultEditorGridPanel;
	private PaginationToolbar paginationToolbar;

	private ComboBox scLineTypeComboBox;
	private TextField billItemTextField;
	private TextField descriptionTextField;
	private Button saveWindowButton;

	private Store dataStore;
	private ColumnConfig[] columns;
	private ColumnConfig currentCertifiedQtyColumn;
	private ColumnConfig currentWorkDoneQtyColumn;
	
	private String screenName = "search-sc-details-and-mass-update-window";

	//parameter 
	private String jobNumber;
	private String packageNo;
	private String billItem;
	private String description;
	private String lineType;

	//private String paymentStatus="";
	private String paymentRequestStatus="";
	private String repackagingType="";

	private ArrowKeyNavigation arrowKeyNavigation;
	
	private GlobalMessageTicket globalMessageTicket;

	private final RecordDef scDetailsRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("sequenceNo"),
					new StringFieldDef("resourceNo"),
					new StringFieldDef("bqItem"),
					new StringFieldDef("description"),
					new StringFieldDef("bqQuantity"),
					new StringFieldDef("toBeApprovedQuantity"),
					new StringFieldDef("ecaRate"),
					new StringFieldDef("scRate"),
					new StringFieldDef("toBeApprovedRate"),

					new StringFieldDef("totalAmount"),
					new StringFieldDef("toBeApprovedAmount"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("lineType"),
					new StringFieldDef("approved"),
					new StringFieldDef("unit"),
					new StringFieldDef("remark"),
					new StringFieldDef("ccSCNo"),
					new StringFieldDef("thisMonthWorkDoneQty"),
					new StringFieldDef("thisMonthCertQty"),
					new StringFieldDef("postedWorkDoneQty"),
					new StringFieldDef("postedWorkDoneAmt"),
					new StringFieldDef("currentWorkDoneQty"),
					new StringFieldDef("currentWorkDoneAmt"),
					new StringFieldDef("ivAmount"),
					new StringFieldDef("postedCertifiedQty"),
					new StringFieldDef("postedCertifiedAmt"),
					new StringFieldDef("currentCertifiedQty"),
					new StringFieldDef("currentCertifiedAmt"),
					new StringFieldDef("provisionAmt"),
					new StringFieldDef("projProvisionAmt"),
					
					new StringFieldDef("balanceType"),
					new StringFieldDef("packageNo") ,
					// Source Type
					new StringFieldDef("sourceType"),
					new StringFieldDef("oldCurrentWorkDoneAmt"),
					new StringFieldDef("corresponseSCLineSeq")
			}
	);
	private List<String> accessRightsList;
	
	private String selectedFunction;
	
	public UpdateWDandCertQtyForPaymentRequisitionWindow(final GlobalSectionController globalSectionController, String selectedFunction){ 
		super();
		this.setId(this.screenName);
		this.globalSectionController =globalSectionController;
		this.selectedFunction = selectedFunction;
		
		jobNumber = globalSectionController.getJob().getJobNumber().trim();
		packageNo = globalSectionController.getSelectedPackageNumber().trim();
		
		globalMessageTicket = new GlobalMessageTicket();
		
		if (selectedFunction.equals("Cum WD Qty")) 
			this.setTitle("Update Cumulative Work Done Quantity(Payment Requisition) - Job: " + jobNumber + " Package: " + packageNo);
		else if (selectedFunction.equals("Cum Certified Qty"))
			this.setTitle("Update Cumulative Certified Quantity(Payment Requisition) - Job: " + jobNumber + " Package: " + packageNo);

		repackagingType = globalSectionController.getJob().getRepackagingType();
		
		paymentRequestStatus = globalSectionController.getPackageEditorFormPanel().getPaymentRequestStatus().trim();
		
		setupUI();
	}
		
	private void setupUI(){
		setPaddings(5);
		setWidth(1280);
		setHeight(650);
		setClosable(false);
		setLayout(new FitLayout());
		setModal(true);
		
		setupSearchPanel();
		setupGridPanel();
	}
	
	private void setupSearchPanel(){
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		
		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(2);
		searchPanel.setFrame(true);
		searchPanel.setHeight("125");
		TableLayout searchPanelLayout = new TableLayout(9);		
		searchPanel.setLayout(searchPanelLayout);
		
		massUpdatePanel = new Panel();
		massUpdatePanel.setPaddings(2);
		massUpdatePanel.setFrame(true);
		massUpdatePanel.setHeight("125");
		TableLayout massUpdatePanelLayout = new TableLayout(8);		
		massUpdatePanel.setLayout(massUpdatePanelLayout);
		
		Label lineTyeLabel = new Label("Line Type");
		lineTyeLabel.setCls("table-cell");
		searchPanel.add(lineTyeLabel);
		scLineTypeComboBox = new ComboBox();			
		scLineTypeComboBox.setCtCls("table-cell");
		scLineTypeComboBox.setSelectOnFocus(true);
		scLineTypeComboBox.setWidth(220);		
		Store scLineTypeStore = globalSectionController.getScLineTypeStore();
		scLineTypeStore.load();
		scLineTypeComboBox.setStore(scLineTypeStore);
		scLineTypeComboBox.setValueField("lineType");
		scLineTypeComboBox.setDisplayField("description");
		searchPanel.add(scLineTypeComboBox);
		
		Label billItemLabel = new Label("Bill Item");
		billItemLabel.setCls("table-cell");
		searchPanel.add(billItemLabel);
		billItemTextField = new TextField("Bill Item", "billItem", 200);
		billItemTextField.setWidth(200);
		billItemTextField.setCtCls("table-cell");
		searchPanel.add(billItemTextField);

		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");
		searchPanel.add(descriptionLabel);
		descriptionTextField =new TextField("Description","description",200);
		descriptionTextField.setWidth(300);
		descriptionTextField.setCtCls("table-cell");
		searchPanel.add(descriptionTextField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				if(dataStore.getModifiedRecords().length != 0)
					MessageBox.confirm("Continue?", "Any unsaved changes will be lost. Continue?", new MessageBox.ConfirmCallback(){
						public void execute(String btnID) {
							if(btnID.equals("yes"))
								searchFields();
						}
					});
				else
					searchFields();
			};

		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton);
		searchPanel.add(new Label(""));//Blank Label
		
		if (selectedFunction.equals("Cum WD Qty")) {
			Label applyForCumWDQtyLabel = new Label("Apply % for Cum WD Qty");
			applyForCumWDQtyLabel.setCls("table-cell");
			massUpdatePanel.add(applyForCumWDQtyLabel);
			final TextField applyForCumWDQtyTextField = new TextField("Apply % for Cum WD Qty", "applyForCumWDQty", 200);
			applyForCumWDQtyTextField.setWidth(60);
			applyForCumWDQtyTextField.setCtCls("table-cell");
			massUpdatePanel.add(applyForCumWDQtyTextField);

			Button applyButton = new Button();
			applyButton.setText("Apply %");
			applyButton.addListener( new ButtonListenerAdapter(){

				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					if (applyForCumWDQtyTextField.getText().trim().length()>0) {
						UIUtil.maskPanelById(RESULT_PANEL_ID, GlobalParameter.LOADING_MSG, true);
						String inputedValue = applyForCumWDQtyTextField.getText().trim();
						try {
							if (Double.parseDouble(inputedValue)<=100 && Double.parseDouble(inputedValue)>=0)
								applyAllCumWDQtyPercentage(Double.parseDouble(inputedValue));
							else 
								MessageBox.alert("Please input a valid percentage!");
						}catch(NumberFormatException ex)
						{
							MessageBox.alert("Please input a valid numeric percentage!");
						}
						UIUtil.unmaskPanelById(RESULT_PANEL_ID);
					} else { 
						MessageBox.alert("Please input \"Apply % for Cum WD Qty\"");
					}
				};	
			});
			applyButton.setCls("table-cell");
			massUpdatePanel.add(applyButton);
		}
		
		if (selectedFunction.equals("Cum Certified Qty")) {
			Label applyForCumCertQtyLabel = new Label("Apply % for Cum Certified Qty (i.e. BQ Quantity x %)");
			applyForCumCertQtyLabel.setCls("table-cell");
			massUpdatePanel.add(applyForCumCertQtyLabel);
			final TextField applyForCumCertQtyTextField = new TextField("Apply % for Cum Certified Qty", "applyForCumCertQty", 200);
			applyForCumCertQtyTextField.setWidth(60);
			applyForCumCertQtyTextField.setCtCls("table-cell");
			massUpdatePanel.add(applyForCumCertQtyTextField);

			Button applyForCumCertQtyButton = new Button();
			applyForCumCertQtyButton.setText("Apply %");
			applyForCumCertQtyButton.addListener( new ButtonListenerAdapter(){

				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					if (applyForCumCertQtyTextField.getText().trim().length()>0) {
						UIUtil.maskPanelById(RESULT_PANEL_ID, GlobalParameter.LOADING_MSG, true);
						String inputedValue = applyForCumCertQtyTextField.getText().trim();		
						try {
							if (Double.parseDouble(inputedValue)<=100 && Double.parseDouble(inputedValue)>=0) {
								applyAllCumCertQtyPercentage(Double.parseDouble(inputedValue));
							}
							else {
								MessageBox.alert("Please input a valid percentage!");
							}
						}catch(NumberFormatException ex){
							MessageBox.alert("Please input a valid numeric percentage!");
						}
						UIUtil.unmaskPanelById(RESULT_PANEL_ID);
					} else {
						MessageBox.alert("Please input \"Apply % for Cum Certified Qty\"");
					}
				};	
			});
			applyForCumCertQtyButton.setCls("table-cell");
			massUpdatePanel.add(applyForCumCertQtyButton);
		}
		
		mainPanel.add(searchPanel);
		massUpdatePanel.setVisible(false);
		mainPanel.add(massUpdatePanel);
	}
	
	private void setupGridPanel(){
		//Grid Panel 
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		resultPanel.setHeight(475);
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);

		resultPanel.setLayout(new FitLayout());
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setClicksToEdit(1);

		arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);

		final QuantityRenderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		// Last modified: Brian
		Renderer editableQtyRenderer = new EditableColorRenderer(quantityRenderer);
		
		Renderer descriptionRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata, Record record,
					int rowIndex, int colNum, Store store) {
				
				if("TOTAL OF THIS PAGE:".equals(record.getAsString("description")))
					return "<b>"+(String)value +"</b>";
				return (String)value;
			}
		};

		Renderer customRenderer = new Renderer(){

			public String render(Object value, CellMetadata cellMetadata, Record record,
					int rowIndex, int colNum, Store store) {

				String strValue = (String) value;
				strValue = strValue.replaceAll(",", "");
				double raw = Double.parseDouble(strValue.trim());
				StringBuffer formatString = new StringBuffer();
				formatString.append("#,##0");
				
				int decimalPlaces = Integer.parseInt((String)globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES));
				if (decimalPlaces > 0) formatString.append(".");
				
				for(int i=0; i<decimalPlaces; i++) {
					formatString.append("0");
				}
				
				NumberFormat format = NumberFormat.getFormat(formatString.toString());
		
				if("TOTAL OF THIS PAGE:".equals(record.getAsString("description"))){
					return "<b>"+format.format(raw) +"</b>";
				}
				return format.format(raw);
			}
		};

		ColumnConfig resourceNoColumn = new ColumnConfig("Resource No", "resourceNo", 40, false);
		ColumnConfig bqItemColumn = new ColumnConfig("BQ Item", "bqItem", 90, false);
		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 70, false);
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 90, false);
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 200, false);
		descriptionColumn.setRenderer(descriptionRenderer);

		ColumnConfig bqQuantityColumn = new ColumnConfig("BQ Qty", "bqQuantity", 100, false);
		bqQuantityColumn.setRenderer(quantityRenderer);
		bqQuantityColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig costRateColumn = new ColumnConfig("Cost Rate", "ecaRate", 100, false);
		costRateColumn.setRenderer(rateRenderer);
		costRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig scRateColumn = new ColumnConfig("SC Rate", "scRate", 100, false);
		scRateColumn.setRenderer(rateRenderer);
		scRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig totalAmountColumn = new ColumnConfig("Total Amount", "totalAmount", 100, false);
		totalAmountColumn.setAlign(TextAlign.RIGHT);
		totalAmountColumn.setRenderer(customRenderer);

		ColumnConfig toBeApprovedAmountColumn = new ColumnConfig("To Be Approved Amount", "toBeApprovedAmount", 100, false);
		toBeApprovedAmountColumn.setRenderer(customRenderer);
		toBeApprovedAmountColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig lineTypeColumn = new ColumnConfig("Type", "lineType", 40, false);
		ColumnConfig approvedColumn = new ColumnConfig("Approved", "approved", 50, false);
		ColumnConfig unitColumn = new ColumnConfig("Unit", "unit", 40, false);

		ColumnConfig postedWorkDoneQtyColumn = new ColumnConfig("Posted Work Done Qty", "postedWorkDoneQty", 100, false);
		postedWorkDoneQtyColumn.setRenderer(quantityRenderer);
		postedWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig thisMonthWorkDoneQtyColumn = new ColumnConfig("This Month Work Done Qty", "thisMonthWorkDoneQty", 100, false);
		thisMonthWorkDoneQtyColumn.setRenderer(quantityRenderer);
		thisMonthWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig thisMonthCertQtyColumn = new ColumnConfig("This Month Cert Qty", "thisMonthCertQty", 100, false);
		thisMonthCertQtyColumn.setRenderer(quantityRenderer);
		thisMonthCertQtyColumn.setAlign(TextAlign.RIGHT);
		
		currentWorkDoneQtyColumn = new ColumnConfig("Cum Work Done Qty", "currentWorkDoneQty", 100, false);
		if (selectedFunction!=null && selectedFunction.equals("Cum WD Qty")){
			Field currentWorkDoneQtyField = FieldFactory.createNegativeNumberField(3);
			currentWorkDoneQtyField.addListener(arrowKeyNavigation);
			currentWorkDoneQtyColumn.setEditor(new GridEditor(currentWorkDoneQtyField));
			currentWorkDoneQtyColumn.setRenderer(editableQtyRenderer);
		}
		currentWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig postedCertifiedQtyColumn = new ColumnConfig("Posted Certified Qty", "postedCertifiedQty", 100, false);
		postedCertifiedQtyColumn.setRenderer(quantityRenderer);
		postedCertifiedQtyColumn.setAlign(TextAlign.RIGHT);	

		currentCertifiedQtyColumn = new ColumnConfig("Cum Certified Qty", "currentCertifiedQty", 100, false);
		if (selectedFunction!=null && selectedFunction.equals("Cum Certified Qty")){
			Field currentCertifiedQtyField = FieldFactory.createNegativeNumberField(3);
			currentCertifiedQtyField.addListener(arrowKeyNavigation);
			currentCertifiedQtyColumn.setEditor(new GridEditor(currentCertifiedQtyField));
			currentCertifiedQtyColumn.setRenderer(editableQtyRenderer);
		}
		currentCertifiedQtyColumn.setAlign(TextAlign.RIGHT);		

		ColumnConfig postedCertifiedAmtColumn = new ColumnConfig("Posted Certified Amt", "postedCertifiedAmt", 100, false);
		postedCertifiedAmtColumn.setRenderer(customRenderer);
		postedCertifiedAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig currentCertifiedAmtColumn  = new ColumnConfig("Cum Certified Amt", "currentCertifiedAmt", 100, false);
		currentCertifiedAmtColumn.setRenderer(customRenderer);
		currentCertifiedAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig provisionAmtColumn  = new ColumnConfig("Provision", "provisionAmt", 100, false);
		provisionAmtColumn.setRenderer(customRenderer);
		provisionAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig projProvisionAmtColumn  = new ColumnConfig("Projected Provision", "projProvisionAmt", 100, false);
		projProvisionAmtColumn.setRenderer(customRenderer);
		projProvisionAmtColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig ivAmountColumn = new ColumnConfig("IV Amount", "ivAmount", 100, false);
		ivAmountColumn.setRenderer(customRenderer);
		ivAmountColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig balanceTypeColumn = new ColumnConfig("Balance Type", "balanceType", 80, false);

		ColumnConfig currentWorkDoneAmtColumn = new ColumnConfig("Cum Work Done Amt", "currentWorkDoneAmt", 100, false);
		currentWorkDoneAmtColumn.setRenderer(customRenderer);
		currentWorkDoneAmtColumn.setAlign(TextAlign.RIGHT);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(scDetailsRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(dataStore);
		if (selectedFunction.equals("Cum WD Qty")) {
			columns = new ColumnConfig[] { 				
					lineTypeColumn,
					bqItemColumn,
					objectCodeColumn,
					subsidiaryCodeColumn,
					descriptionColumn,
					unitColumn,
					costRateColumn,
					scRateColumn, 
					bqQuantityColumn, 
					totalAmountColumn,
					toBeApprovedAmountColumn,
					postedWorkDoneQtyColumn,
					thisMonthWorkDoneQtyColumn,
					currentWorkDoneQtyColumn,
					currentWorkDoneAmtColumn,
					ivAmountColumn,
					currentCertifiedQtyColumn,
					currentCertifiedAmtColumn,
					resourceNoColumn, 
					approvedColumn,
					balanceTypeColumn,
					provisionAmtColumn,
					projProvisionAmtColumn
			};
		} else if (selectedFunction.equals("Cum Certified Qty")) {
			columns = new ColumnConfig[] { 				
					lineTypeColumn,
					bqItemColumn,
					objectCodeColumn,
					subsidiaryCodeColumn,
					descriptionColumn,
					unitColumn, 
					costRateColumn,
					scRateColumn, 
					bqQuantityColumn, 
					totalAmountColumn,
					toBeApprovedAmountColumn, 
					postedCertifiedQtyColumn,
					thisMonthCertQtyColumn,
					currentCertifiedQtyColumn,
					currentCertifiedAmtColumn,
					currentWorkDoneQtyColumn,
					currentWorkDoneAmtColumn,
					ivAmountColumn, 
					resourceNoColumn, 
					approvedColumn,
					balanceTypeColumn,
					provisionAmtColumn,
					projProvisionAmtColumn
			};
		}
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		resultEditorGridPanel.setColumnModel(new ColumnModel(customizedColumns));

		resultEditorGridPanel.addEditorGridListener( new EditorGridListenerAdapter(){
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex){
				globalMessageTicket.refresh();
				String lineType = record.getAsString("lineType");
				boolean isEditable = true;

				Double scRate = record.getAsObject("scRate") == null ? Double.valueOf(0) : record.getAsDouble("scRate");
				if (field.equals("currentWorkDoneQty")){
					/*if (lineType!=null && lineType.trim().equals("BQ") && !(globalSectionController.getPackageEditorFormPanel().getLegacyJobFlag().trim().equals("Y") || globalSectionController.getPackageEditorFormPanel().getAllowManualInputSCWorkdone().trim().equals("Y"))){
						UIUtil.alert("Workdone cannot be updated in BQ Line");
						isEditable =  false;
					}*/
					if(!lineType.trim().equals("BQ") && !lineType.trim().equals("RR")){
						MessageBox.alert("Only BQ/RR Line can be updated for Payment Requisition.");
						isEditable =  false;	
					}
				}
				
				String description = record.getAsString("description");
				if("TOTAL OF THIS PAGE:".equals(description))
					isEditable = false;

				if (field.equals("currentCertifiedQty")) {
					if (paymentRequestStatus.equals("SBM") || paymentRequestStatus.equals("UFR") || paymentRequestStatus.equals("PCS")){
						UIUtil.alert("Cert Qty cannot be updated when Payment is Submitted");
						isEditable =  false;					
					}
					if(!lineType.trim().equals("BQ") && !lineType.trim().equals("RR")){
						MessageBox.alert("Only BQ/RR Line can be updated for Payment Requisition.");
						isEditable =  false;	
					}
					if (scRate.equals(Double.valueOf(0)) && lineType.trim().equals("BQ")){
						UIUtil.alert("Cert line cannot be updated when BQ Line SC Rate is zero");
						isEditable =  false;
					}
				}

				if (isEditable) {
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				} else {
					arrowKeyNavigation.resetState();
				}
				
				return isEditable;
			}
			public boolean doValidateEdit(GridPanel grid, Record record, String field, Object value, Object orginalValue , int rowIndex, int colIndex){

				String newValueStr = value +"";
				
				if(field.equals("currentCertifiedQty"))	{
					Double bqQty = new Double(record.getAsString("bqQuantity"));
					Double currCertQty = (newValueStr!=null?new Double(newValueStr):new Double(0));
					Double scRate = new Double(record.getAsString("scRate"));
					
					if(bqQty>0 && currCertQty <0){
						MessageBox.alert("Invalid inputed value. Cum Certified Qty cannot be negative.");
						return false;
					}else if (currCertQty > 0 && bqQty < 0){
						MessageBox.alert("Invalid inputed value. Cum Certified Qty must be in range of BQ Qty.");
						return false;
					}else if(Math.abs(currCertQty) > Math.abs(bqQty) && (record.getAsString("sourceType").trim().equals("BQ") || (record.getAsString("sourceType").trim().equals("A"))) && !record.getAsString("balanceType").equals("%")){
						MessageBox.alert("Invalid inputed value. Cum Certified Qty cannot be larger than BQ Qty.");
						return false;						
					}else if(Math.abs(currCertQty) > 100 && (record.getAsString("sourceType").equals("BQ") || record.getAsString("sourceType").equals("A")) && record.getAsString("balanceType").equals("%")){
						MessageBox.alert("Invalid inputed value. Cum Certified Qty cannot be larger than 100 (%).");
						return false;
					}else if(Math.abs(scRate)==0 && record.getAsString("sourceType").equals("BQ") && Math.abs(currCertQty)>0){
						MessageBox.alert("Invalid inputed value. SC Rate is Zero, therefore Cum Certified Qty must be zero.");
						return false;										
					}
				}
				if (field.equals("currentWorkDoneQty")){
					Double currWDQty = new Double(newValueStr);
					double bqQty = record.getAsDouble("bqQuantity");
					@SuppressWarnings("unused")
					double costRate = (record.getAsString("ecaRate")==null||record.getAsString("ecaRate").trim().equals(""))?0.00:record.getAsDouble("ecaRate");
					String lineType = record.getAsString("lineType");
					boolean notAllowToUpdateWDByMethodThree = 	lineType.equals("BQ");
					
					if(repackagingType!=null && repackagingType.equals("3") && notAllowToUpdateWDByMethodThree){
						MessageBox.alert("Please update the workdone of BQ lines from 'IV Update by Resouce' Window or 'IV Update by BQItem' Window.");
						return false;
					}
					
					if (Math.abs(currWDQty)>0 && (lineType.equals("RR"))) {
						MessageBox.alert("Invalid inputed value. Cum Work Done Qty must be zero.");
						return false;
					}
					else if(Math.abs(currWDQty) > Math.abs(bqQty)){
						boolean isVO = false;
						if(record.getAsString("subsidiaryCode").startsWith("4") && "80".equals(record.getAsString("subsidiaryCode").substring(2, 4)))
							isVO = true;
						
						if(!isVO && (lineType.trim().equals("BQ") 
						//Use the Cost rate to check if it has budget rather than using Resource No
								|| ((!"".equals(record.getAsString("ecaRate")))&&(record.getAsDouble("ecaRate") > 0 ||record.getAsDouble("ecaRate")<0 )))){
							MessageBox.alert("Invalid inputed value. Cum Work Done Qty cannot be larger than BQ Qty.");
							return false;
						}
					}
				}
				return true;				

			}

			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex){
				String newValueStr = newValue +"";
				Double scRate = new Double(record.getAsString("scRate"));
				Double costRate = record.getAsString("ecaRate")!=null?new Double(record.getAsString("ecaRate")):new Double(0.00);
				Double postedCertQty = new Double(record.getAsString("postedCertifiedQty"));
				
				String balanceType = (record.getAsString("balanceType")==null || record.getAsString("balanceType").trim().equals("") ) ? " " : record.getAsString("balanceType").trim();
				Double totalAmount = (record.getAsString("totalAmount")==null || record.getAsString("totalAmount").equals("") ) ? new Double(0) : Double.parseDouble(record.getAsString("totalAmount"));
				
				if(field.equals("currentCertifiedQty"))	{
					Double currCertQty = (newValueStr!=null?new Double(newValueStr):new Double(0));
					Double currWDQty = (record.getAsString("currentWorkDoneQty")!=null?new Double(record.getAsString("currentWorkDoneQty")):new Double(0));				
					
					if (!balanceType.trim().equals("%")) {
						record.set("currentCertifiedAmt", String.valueOf(currCertQty));
						record.set("projProvisionAmt", String.valueOf(currWDQty-currCertQty));

					} else {
						record.set("currentCertifiedAmt", String.valueOf(totalAmount*(currCertQty/100)));
						record.set("projProvisionAmt", String.valueOf(totalAmount*(currWDQty-currCertQty)/100));
					}
					record.set("thisMonthCertQty", String.valueOf(currCertQty-postedCertQty));
					
					if (!balanceType.trim().equals("%")) {
						record.set("currentCertifiedAmt", String.valueOf(scRate*currCertQty));
						record.set("projProvisionAmt", String.valueOf(scRate*(currWDQty-currCertQty)));

					} else {
						record.set("currentCertifiedAmt", String.valueOf(totalAmount*(currCertQty/100)));
						record.set("projProvisionAmt", String.valueOf(totalAmount*(currWDQty-currCertQty)/100));
					}
					record.set("thisMonthCertQty", String.valueOf(currCertQty-postedCertQty));


				}
				if (field.equals("currentWorkDoneQty")){
					Double currWDQty = new Double(newValueStr);
					Double currCertQty = new Double(record.getAsString("postedCertifiedQty"));
					Double postedWDQty = new Double(record.getAsString("postedWorkDoneQty"));
			
					record.set("thisMonthWorkDoneQty", String.valueOf(currWDQty-postedWDQty));
					
					if (!balanceType.trim().equals("%")) {
						record.set("currentWorkDoneAmt", String.valueOf(scRate*currWDQty));
						record.set("projProvisionAmt", String.valueOf(scRate*(currWDQty-currCertQty)));
						record.set("provisionAmt", String.valueOf(scRate*(currWDQty-postedCertQty)));
						record.set("ivAmount", String.valueOf(costRate*currWDQty));
					} else {
						record.set("currentWorkDoneAmt", String.valueOf(totalAmount*(currWDQty/100)));
						record.set("projProvisionAmt", String.valueOf(totalAmount*(currWDQty-currCertQty)/100));
						record.set("provisionAmt", String.valueOf(totalAmount*(currWDQty-postedCertQty)/100));
						record.set("ivAmount", String.valueOf(costRate*currWDQty));
					}
				}
			}

		});

		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				if(dataStore.getModifiedRecords().length != 0)
					MessageBox.confirm("Continue?", "Any unsaved changes will be lost. Continue?", new MessageBox.ConfirmCallback(){
						public void execute(String btnID) {
							if(btnID.equals("yes"))
								searchByPage(pageNum);
						}
					});
				else
					searchByPage(pageNum);
			}
		});
		resultPanel.setBottomToolbar(paginationToolbar);

		resultPanel.add(resultEditorGridPanel);

		mainPanel.add(resultPanel);

		add(mainPanel);

		saveWindowButton = new Button("Save");
		saveWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SAVING_MSG, true);

				List <UpdateSCPackageSaveWrapper> pendingSaveRecord = new ArrayList<UpdateSCPackageSaveWrapper>();
				Record[] records = dataStore.getRecords();

				for (int i=0; i< records.length; i++ ) {
					if (records[i].isDirty() ) {
						UpdateSCPackageSaveWrapper updateSCPackageSaveWrapper = new UpdateSCPackageSaveWrapper();

						Integer resourceNo = (records[i].getAsString("resourceNo")==null ||"".equals(records[i].getAsString("resourceNo").trim()) ) ? null : Integer.parseInt(records[i].getAsString("resourceNo"));
						String bqItem = records[i].getAsString("bqItem");
						Double currentWorkDoneQty =(records[i].getAsString("currentWorkDoneQty")==null || records[i].getAsString("currentWorkDoneQty").equals("") ) ? null : Double.parseDouble(records[i].getAsString("currentWorkDoneQty"));
						Double prvWorkDoneQty = (records[i].getAsString("oldCurrentWorkDoneAmt")==null || records[i].getAsString("oldCurrentWorkDoneAmt").equals("") ) ? null : Double.parseDouble(records[i].getAsString("oldCurrentWorkDoneAmt"));
						Double movementWorkDoneQty = 0.00;

						if (currentWorkDoneQty!=null && prvWorkDoneQty!=null)
							movementWorkDoneQty = currentWorkDoneQty - prvWorkDoneQty;
						dataStore.getAt(i).set("oldCurrentWorkDoneAmt", currentWorkDoneQty);
						Double currentCertifiedQty = (records[i].getAsString("currentCertifiedQty")==null || records[i].getAsString("currentCertifiedQty").equals("") ) ? null : Double.parseDouble(records[i].getAsString("currentCertifiedQty"));
						String balanceType = (records[i].getAsString("balanceType")==null || records[i].getAsString("balanceType").equals("") ) ? " " : records[i].getAsString("balanceType");
						Integer packageNo = (records[i].getAsString("packageNo")!=null && !"".equals(records[i].getAsString("packageNo").trim())? new Integer(records[i].getAsString("packageNo").trim()):null);
						Integer sortSeqNo = (records[i].getAsString("sequenceNo")!=null && !"".equals(records[i].getAsString("sequenceNo").trim())? new Integer(records[i].getAsString("sequenceNo").trim()):null);
						String subsidiaryCode = records[i].getAsString("subsidiaryCode");
						String objectCode = records[i].getAsString("objectCode");
						Double costRate = (records[i].getAsString("ecaRate")==null || records[i].getAsString("ecaRate").equals("") ) ? null : Double.parseDouble(records[i].getAsString("ecaRate"));
						Double scRate = (records[i].getAsString("scRate")==null || records[i].getAsString("scRate").equals("") ) ? null : Double.parseDouble(records[i].getAsString("scRate"));;
						Double bqQuantity = (records[i].getAsString("bqQuantity")==null || records[i].getAsString("bqQuantity").equals("") ) ? null : Double.parseDouble(records[i].getAsString("bqQuantity"));
						String sourceType = (records[i].getAsString("sourceType")==null || records[i].getAsString("sourceType").equals("") ) ? " " : records[i].getAsString("sourceType");
						if (records[i].isModified("currentCertifiedQty")
								&&	(sourceType.equals("A")||sourceType.equals("BQ")) 
								&& (( Math.abs(currentCertifiedQty)> Math.abs(bqQuantity) && !balanceType.equals("%"))||(( Math.abs(currentCertifiedQty)> 100 & balanceType.equals("%"))))){
								UIUtil.alert("Current Certified Qty is larger than BQ Qty on BQ Item "+bqItem);
								pendingSaveRecord.clear();
								i = records.length;
								UIUtil.unmaskPanelById(RESULT_PANEL_ID);
							}else if (records[i].isModified("currentCertifiedQty") && 
									sourceType.equals("BQ") && scRate==0 && currentCertifiedQty!=0){
								UIUtil.alert("SC Rate of BQ Line is zero, No cert qty is allowed. @ "+bqItem);
								pendingSaveRecord.clear();
								i = records.length;
								UIUtil.unmaskPanelById(RESULT_PANEL_ID);							
							}else{

							updateSCPackageSaveWrapper.setUserId(globalSectionController.getUser().getUsername());
							updateSCPackageSaveWrapper.setJobNumber(jobNumber);
							updateSCPackageSaveWrapper.setResourceNo(resourceNo);
							updateSCPackageSaveWrapper.setBqItem(bqItem);
							
							updateSCPackageSaveWrapper.setCurrentCertifiedQuanity(currentCertifiedQty);
							updateSCPackageSaveWrapper.setWorkDoneMovementQuantity(movementWorkDoneQty);
							updateSCPackageSaveWrapper.setCurrentWorkDoneQuantity(currentWorkDoneQty);
							
							updateSCPackageSaveWrapper.setPackageNo(packageNo);
							updateSCPackageSaveWrapper.setBalanceType(balanceType);						
							updateSCPackageSaveWrapper.setSortSeqNo(sortSeqNo);
							updateSCPackageSaveWrapper.setSubsidiaryCode(subsidiaryCode);
							updateSCPackageSaveWrapper.setObjectCode(objectCode);
							
							updateSCPackageSaveWrapper.setScRate(scRate);
							updateSCPackageSaveWrapper.setCostRate(costRate);
							updateSCPackageSaveWrapper.setBqQuantity(bqQuantity);
							updateSCPackageSaveWrapper.setSourceType(sourceType);
							if (selectedFunction.equals("Cum Certified Qty"))
								updateSCPackageSaveWrapper.setTriggerSCPaymentUpdate(true);
							else
								updateSCPackageSaveWrapper.setTriggerSCPaymentUpdate(false);
							
							updateSCPackageSaveWrapper.setDirectPaymentIndicator(SCPaymentCert.DIRECT_PAYMENT);
							pendingSaveRecord.add(updateSCPackageSaveWrapper);
						}
					}
				}

				if (pendingSaveRecord.size() > 0) {
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPackageRepository().updateWDandCertQuantity(pendingSaveRecord, new AsyncCallback<String>() {
						public void onSuccess(String message) {
							if(message==null){
								globalSectionController.getTreeSectionController().getPaymentTreePanel().refreshPaymentTreePanel();
								MessageBox.alert("Changes are saved successfully."); 

								dataStore.commitChanges();
							}
							else{
								MessageBox.alert(message);
							}
							UIUtil.unmaskPanelById(MAIN_PANEL_ID);
						}

						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(MAIN_PANEL_ID);
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
						}
					});
				}else {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					MessageBox.alert("No changes are pending for save!");
				}
			}
		});

		saveWindowButton.setVisible(false);
		addButton(saveWindowButton);

		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if (dataStore.getModifiedRecords().length!=0){
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		globalSectionController.closeCurrentWindow();
	                            }   
	                        });   
				}
				else
					globalSectionController.closeCurrentWindow();				
			};
		});		

		addButton(closeWindowButton);

		// Check for access rights - then add toolbar buttons accordingly
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010210_UPDATE_WD_AND_CERTQTY_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				securitySetup();
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
			}
		});
	}


	private void searchFields(){
		billItem = billItemTextField.getValueAsString();
		description = descriptionTextField.getValueAsString();
		lineType = scLineTypeComboBox.getValueAsString();
		String raw = scLineTypeComboBox.getRawValue();
		if(raw == null || raw.trim().length() == 0)
			lineType = null;
		searchByPage(0);
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getScDetailsByPage(jobNumber, packageNo, billItem, description, lineType, pageNum, new AsyncCallback<PaginationWrapper<SCDetails>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.throwException(e);
			}

			public void onSuccess(PaginationWrapper<SCDetails> wrapper) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				populate(wrapper);
			}
		});
	}

	private void applyAllCumWDQtyPercentage(double inputValue){
		Record[] records = dataStore.getRecords();
		@SuppressWarnings("unused")
		int i =0;

		for(Record curRecord: records){
			String curValue = curRecord.getAsString("bqQuantity");
			Double scRate = (curRecord.getAsString("scRate")==null || curRecord.getAsString("scRate").equals("") ) ? null : Double.parseDouble(curRecord.getAsString("scRate"));;
			Double costRate = (curRecord.getAsString("ecaRate")==null || curRecord.getAsString("ecaRate").equals("") ) ? null : Double.parseDouble(curRecord.getAsString("ecaRate"));;

			String lineType = (curRecord.getAsString("lineType")==null || curRecord.getAsString("lineType").trim().equals("") ) ? " " : curRecord.getAsString("lineType").trim();
			Double postedWorkDoneQty = (curRecord.getAsString("postedWorkDoneQty")==null || curRecord.getAsString("postedWorkDoneQty").equals("") ) ? new Double(0) : Double.parseDouble(curRecord.getAsString("postedWorkDoneQty"));
			String balanceType = (curRecord.getAsString("balanceType")==null || curRecord.getAsString("balanceType").trim().equals("") ) ? " " : curRecord.getAsString("balanceType").trim();
			Double totalAmount = (curRecord.getAsString("totalAmount")==null || curRecord.getAsString("totalAmount").equals("") ) ? new Double(0) : Double.parseDouble(curRecord.getAsString("totalAmount"));

			Double postedCertQty = (curRecord.getAsString("postedCertifiedQty")==null || curRecord.getAsString("postedCertifiedQty").equals("") ) ? 0.000: Double.parseDouble(curRecord.getAsString("postedCertifiedQty"));;
			Double currCertQty = (curRecord.getAsString("currentCertifiedQty")==null || curRecord.getAsString("currentCertifiedQty").equals("") ) ? 0.000 : Double.parseDouble(curRecord.getAsString("currentCertifiedQty"));;

			if (costRate == null)
				costRate =0.000;
			if (scRate == null)
				scRate = 0.000;
			if (lineType.equals("BQ")/*&&("Y".equals(globalSectionController.getPackageEditorFormPanel().getAllowManualInputSCWorkdone())||"Y".equals(globalSectionController.getPackageEditorFormPanel().getLegacyJobFlag()))*/){
				double curDoubleValue = 0;
				try{
					if (curValue!=null && !curValue.equals(""))
						curDoubleValue = Double.parseDouble(curValue);
					else
						curDoubleValue=0.000;
				}catch(Exception e){}

				if (!"%".equals(balanceType)) {
					curRecord.set("currentWorkDoneQty", (inputValue/100) * curDoubleValue+"");
					curRecord.set("currentWorkDoneAmt", scRate * ((inputValue/100) * curDoubleValue)+""); 

					curRecord.set("thisMonthWorkDoneQty", (((inputValue/100) * curDoubleValue) - postedWorkDoneQty)+"");

					curRecord.set("ivAmount", String.valueOf( costRate*(inputValue/100) * curDoubleValue));
					curRecord.set("provisionAmt", String.valueOf(scRate*((inputValue/100) * curDoubleValue-postedCertQty)));
					curRecord.set("projProvisionAmt", String.valueOf(scRate*((inputValue/100) * curDoubleValue-currCertQty)));
				} else {
					curRecord.set("currentWorkDoneQty", inputValue+"");
					curRecord.set("currentWorkDoneAmt", (totalAmount * (inputValue/100))+""); 
					curRecord.set("thisMonthWorkDoneQty", (inputValue-postedWorkDoneQty)+"");

					curRecord.set("ivAmount", String.valueOf( costRate*inputValue));
					curRecord.set("provisionAmt", String.valueOf(scRate*(inputValue * curDoubleValue-postedCertQty)));
					curRecord.set("projProvisionAmt", String.valueOf(scRate*(inputValue * curDoubleValue-currCertQty)));
				}
			}
			i++;
		}
	}

	private void applyAllCumCertQtyPercentage(double inputValue){
		if (!("PCS".equals(paymentRequestStatus) || "UFR".equals(paymentRequestStatus) || "SBM".equals(paymentRequestStatus))){
			Record[] records = dataStore.getRecords();
			@SuppressWarnings("unused")
			int i =0;

			for(Record curRecord: records){
			String curValue = curRecord.getAsString("bqQuantity");
			Double scRate = (curRecord.getAsString("scRate")==null || curRecord.getAsString("scRate").equals("") ) ? Double.valueOf(0) : Double.parseDouble(curRecord.getAsString("scRate"));;
			Double postedCertifiedQty = (curRecord.getAsString("postedCertifiedQty")==null || curRecord.getAsString("postedCertifiedQty").equals("") ) ? null : Double.parseDouble(curRecord.getAsString("postedCertifiedQty"));;
			String balanceType = (curRecord.getAsString("balanceType")==null || curRecord.getAsString("balanceType").trim().equals("") ) ? " " : curRecord.getAsString("balanceType").trim();
			Double totalAmount = (curRecord.getAsString("totalAmount")==null || curRecord.getAsString("totalAmount").equals("") ) ? new Double(0) : Double.parseDouble(curRecord.getAsString("totalAmount"));
			Double currWDQty = (curRecord.getAsString("currentWorkDoneQty")==null || curRecord.getAsString("currentWorkDoneQty").equals("") ) ? 0.000 : Double.parseDouble(curRecord.getAsString("currentWorkDoneQty"));;

			if (!scRate.equals(Double.valueOf(0))){
				double curDoubleValue = 0;
				curDoubleValue = Double.parseDouble(curValue);
				if (!balanceType.equals("%")) {
				curRecord.set("currentCertifiedQty", (inputValue/100) * curDoubleValue+"");
				curRecord.set("currentCertifiedAmt", ((inputValue/100) * curDoubleValue)+"");
				curRecord.set("thisMonthCertQty", (((inputValue/100) * curDoubleValue) - postedCertifiedQty)+"");
				curRecord.set("projProvisionAmt", String.valueOf((currWDQty-(inputValue/100) * curDoubleValue)));
				} else {
					curRecord.set("currentCertifiedQty", inputValue+"");
					curRecord.set("currentCertifiedAmt", (totalAmount * (inputValue/100))+"");
					curRecord.set("thisMonthCertQty", (inputValue-postedCertifiedQty)+"");
					curRecord.set("projProvisionAmt", String.valueOf((currWDQty-(inputValue * curDoubleValue))));
				}
			}
			i++;
			}
		} else {
			UIUtil.alert("Cert Qty cannot be updated when Payment is Submitted");
		}
		
	}

	public void populate(PaginationWrapper<SCDetails> wrapper) {
		dataStore.removeAll();
		
		Double toBeApprovedAmount = new Double(0) ;
		Double thisProvisionAmt = new Double(0) ;
		Double thisProjProvisionAmt = new Double(0);
		Double thisCurrentCertAmt = new Double(0);
		Double thisPostedCertAmt = new Double(0);
		Double thisCurrentWorkdoneAmt = new Double(0);
		Double thisPostedWorkdoneAmt = new Double(0);
		Double thisIVAmount = new Double(0);
		
		Double linetotalAmountTotal = new Double(0);
		Double toBeApprovedAmountTotal = new Double(0) ;
		Double thisProvisionAmtTotal = new Double(0) ; // done
		Double thisProjProvisionAmtTotal = new Double(0); // done
		Double thisCurrentCertAmtTotal = new Double(0); //done
		Double thisPostedCertAmtTotal = new Double(0); //done
		Double thisCurrentWorkdoneAmtTotal = new Double(0);
		Double thisPostedWorkdoneAmtTotal = new Double(0);
		Double thisIVAmountTotal = new Double(0); // done
		
		List<SCDetails> scDetails = wrapper.getCurrentPageContentList();
		Record[] records = new Record[scDetails.size()];
		int ind = 0;
		for(SCDetails scDetail : scDetails){
			if(ind == (scDetails.size() - 1)){
				records[ind] = scDetailsRecordDef.createRecord(new Object[] {
						"",
						"",
						"",
						"TOTAL OF THIS PAGE:",
						"",
						"",
						"",
						"",
						"",

						linetotalAmountTotal,
						toBeApprovedAmountTotal,
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						thisPostedWorkdoneAmtTotal,
						"",
						thisCurrentWorkdoneAmtTotal,
						thisIVAmountTotal,
						"",
						0.00,
						"",
						thisCurrentCertAmtTotal,
						thisProvisionAmtTotal,
						thisProjProvisionAmtTotal,
						"",
						"",

						"",
						"",
						""
				});
				
			}
			else{
				Double tobeapprovedQty=0.0;
				Double costRate = 0.0;
				Double tobeapprovedRate=null;
				Double linetotalAmount = 0.0;
				Double linetobetotalAmount = 0.0;
				String contraChargeSCNo ="";
				Double lineCumWorkDone =0.0;
				Double linePostWorkDone = 0.0;
								thisCurrentWorkdoneAmt = scDetail.getCumWorkDoneQuantity()*scDetail.getScRate() ;
								thisPostedWorkdoneAmt = scDetail.getPostedWorkDoneQuantity()*scDetail.getScRate();
				if (scDetail instanceof SCDetailsOA) {
					thisCurrentWorkdoneAmt =((SCDetailsOA)scDetail).getCumWorkDoneQuantity()*scDetail.getScRate();
					thisCurrentWorkdoneAmtTotal += thisCurrentWorkdoneAmt;
					
					thisPostedWorkdoneAmt = ((SCDetailsOA)scDetail).getPostedWorkDoneQuantity()*scDetail.getScRate();
					thisPostedWorkdoneAmtTotal += thisPostedWorkdoneAmt;
					
					lineCumWorkDone = ((SCDetailsOA)scDetail).getCumWorkDoneQuantity();
					linePostWorkDone = ((SCDetailsOA)scDetail).getPostedWorkDoneQuantity();
				}
				if (scDetail instanceof SCDetailsBQ) {
					linetobetotalAmount =((SCDetailsBQ)scDetail).getToBeApprovedAmount();
					toBeApprovedAmount += linetobetotalAmount;
					toBeApprovedAmountTotal += linetobetotalAmount;
					linetotalAmount =((SCDetailsBQ)scDetail).getTotalAmount();
					linetotalAmountTotal += linetotalAmount;
					tobeapprovedQty = ((SCDetailsBQ)scDetail).getToBeApprovedQuantity();
					costRate = ((SCDetailsBQ)scDetail).getCostRate();
				}
				if (scDetail instanceof SCDetailsVO) {
					tobeapprovedRate = ((SCDetailsVO)scDetail).getToBeApprovedRate();
					contraChargeSCNo = ((SCDetailsVO)scDetail).getContraChargeSCNo();
				}
				if (scDetail instanceof SCDetailsCC) {
					contraChargeSCNo = ((SCDetailsCC)scDetail).getContraChargeSCNo();
					thisCurrentWorkdoneAmt=0.0;
					thisPostedWorkdoneAmt=0.0;
				}
				thisPostedCertAmt =scDetail.getPostedCertifiedQuantity()*scDetail.getScRate();
				thisPostedCertAmtTotal += thisPostedCertAmt;
				
				
				thisCurrentCertAmt = scDetail.getCumCertifiedQuantity()*scDetail.getScRate(); 
				thisCurrentCertAmtTotal += thisCurrentCertAmt;
				
				thisIVAmount = 0.0;
				if (scDetail.getCostRate()!= null && scDetail.getCumWorkDoneQuantity()!=null)
					thisIVAmount =scDetail.getCostRate() * scDetail.getCumWorkDoneQuantity();
				thisIVAmountTotal += thisIVAmount;
				thisPostedCertAmt =scDetail.getPostedCertifiedQuantity()*scDetail.getScRate(); 
				thisCurrentCertAmt = scDetail.getCumCertifiedQuantity()*scDetail.getScRate(); 
				
				boolean hasProvision = lineType.equals("C1") || lineType.equals("C2") || lineType.equals("RR") || lineType.equals("RT") || lineType.equals("MS");
				thisProvisionAmt = hasProvision ? 0 : thisCurrentWorkdoneAmt - thisPostedCertAmt;
				thisProvisionAmtTotal += thisProvisionAmt;

				thisProjProvisionAmt = hasProvision ? 0 : thisCurrentWorkdoneAmt - thisCurrentCertAmt;
				thisProjProvisionAmtTotal += thisProjProvisionAmt;
				String ccSCLineSeqNoString = " ";
				if ("C2".equals(scDetail.getLineType())&&((SCDetailsCC)scDetail).getCorrSCLineSeqNo()!=null)
					ccSCLineSeqNoString =((SCDetailsCC)scDetail).getCorrSCLineSeqNo().toString();
				else 
					if ("L2".equals(scDetail.getLineType())||"D2".equals(scDetail.getLineType()))
						if (((SCDetailsVO)scDetail).getCorrSCLineSeqNo()!=null)
							ccSCLineSeqNoString =((SCDetailsVO)scDetail).getCorrSCLineSeqNo().toString();

				records[ind] = scDetailsRecordDef.createRecord(new Object[] {	
						scDetail.getSequenceNo()!=null?scDetail.getSequenceNo():"",
						scDetail.getResourceNo()!=null?scDetail.getResourceNo():"",
						scDetail.getBillItem()!=null?scDetail.getBillItem():"",
						scDetail.getDescription()!=null?scDetail.getDescription():"",
						scDetail.getQuantity()!=null?scDetail.getQuantity():0.00,
						tobeapprovedQty!=null?tobeapprovedQty:0.00,
						costRate!=null?costRate:0.00,
						scDetail.getScRate()!=null?scDetail.getScRate():0.00,
						tobeapprovedRate!=null?tobeapprovedRate:0.00,
		
						linetotalAmount!=null?linetotalAmount:0.00,
						linetobetotalAmount!=null?linetobetotalAmount:0.00,
						scDetail.getObjectCode()!=null?scDetail.getObjectCode():"",
						scDetail.getSubsidiaryCode()!=null?scDetail.getSubsidiaryCode():"",
						scDetail.getLineType()!=null?scDetail.getLineType():"",
						scDetail.getApproved()!=null?scDetail.getApproved():"",
						scDetail.getUnit()!=null?scDetail.getUnit():"",
						scDetail.getRemark()!=null?scDetail.getRemark():"",
						contraChargeSCNo!=null?contraChargeSCNo:"",
						(lineCumWorkDone!=null && linePostWorkDone!=null)?(lineCumWorkDone - linePostWorkDone):0.00,
						(scDetail.getCumCertifiedQuantity()!=null && scDetail.getPostedCertifiedQuantity()!=null)?(scDetail.getCumCertifiedQuantity()-scDetail.getPostedCertifiedQuantity()):0.00,
						linePostWorkDone!=null?linePostWorkDone:0.00,
						thisPostedWorkdoneAmt!=null?thisPostedWorkdoneAmt:0.00,
						lineCumWorkDone!=null?lineCumWorkDone:0.00,
						thisCurrentWorkdoneAmt!=null?thisCurrentWorkdoneAmt:0.00,
						thisIVAmount!=null?thisIVAmount:0.00,
						scDetail.getPostedCertifiedQuantity()!=null?scDetail.getPostedCertifiedQuantity():0.00,
						thisPostedCertAmt!=null?thisPostedCertAmt:0.00,
						scDetail.getCumCertifiedQuantity()!=null?scDetail.getCumCertifiedQuantity():0.00,
						thisCurrentCertAmt!=null?thisCurrentCertAmt:0.00,
						thisProvisionAmt!=null?thisProvisionAmt:0.00,
						thisProjProvisionAmt!=null?thisProjProvisionAmt:0.00,
						scDetail.getBalanceType()!=null?scDetail.getBalanceType():"",
						scDetail.getScPackage().getPackageNo()!=null?scDetail.getScPackage().getPackageNo():"",
		
						scDetail.getSourceType()!=null?scDetail.getSourceType():"",
						scDetail.getCumWorkDoneQuantity()!=null?scDetail.getCumWorkDoneQuantity():0.0,
						ccSCLineSeqNoString
				});
			}
			ind += 1;
		}
		dataStore.add(records);
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
	}

	// calculation 
	public Double calcuTotalSellingValue(Double sellingRate, Double quantity){
		return new Double(sellingRate * quantity);
	}

	public String getScreenName() {
		return screenName;
	}
	
	private void securitySetup() {
		// if accessRights contain "WRITE"
		if (accessRightsList.contains("WRITE")){
			saveWindowButton.setVisible(true);
			massUpdatePanel.setVisible(true);
		}
	}

}
