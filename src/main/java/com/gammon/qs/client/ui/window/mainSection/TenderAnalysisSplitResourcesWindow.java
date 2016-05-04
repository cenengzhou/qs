package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ComponentMgr;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class TenderAnalysisSplitResourcesWindow extends Window { 
	public static final String MAIN_PANEL_ID ="ResourceSummaryToTenderAnalysisDetail";
	
	private GlobalSectionController globalSectionController;
	private String packageNo;
		
	private Panel mainPanel;
	private Panel rsPanel; //Top panel, for ResourceSummary
	private Panel taPanel; //Bottom panel, for TenderAnalysisDetail
	private GridPanel rsGridPanel;
	private EditorGridPanel taGridPanel;
	
	private Menu menu;
	
	//data store
	private Store rsDataStore;
	private Store taDataStore;
	
	private int currentRow;
	
	private GlobalMessageTicket globalMessageTicket;
	
	private SCPackage scPackage;
	@SuppressWarnings("unused")
	private String paymentRequestStatus;
	
	private final RecordDef tenderAnalysisRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("billItem"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"),
					new StringFieldDef("rate"),
					new StringFieldDef("amount"),
					new StringFieldDef("resourceNo")
			});
	
	//Map of account code (obj+subs codes) to [0]RS Total, [1]TA Total, [2]Summary Record Index
	private HashMap<String,double[]> accountAmounts = new HashMap<String, double[]>();
	private static int RSTOTAL = 0;
	private static int TATOTAL = 1;
	private static int RECORDIND = 2;
		
	public TenderAnalysisSplitResourcesWindow(final GlobalSectionController globalSectionController, final String packageNo){
		super();
		this.setModal(true);
		this.globalSectionController = globalSectionController;
		this.packageNo = packageNo;
		this.setId(MAIN_PANEL_ID);
		
		scPackage = globalSectionController.getMainSectionController().getPackageEditorFormPanel().getScPackage();
		paymentRequestStatus = globalSectionController.getPackageEditorFormPanel().getPaymentRequestStatus().trim();
		
		this.setTitle("Input Tender Analysis Details for package " + packageNo);
		this.setPaddings(5);
		this.setWidth(900);
		this.setHeight(600);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		rsPanel = new Panel();
		rsPanel.setLayout(new RowLayout());
		taPanel = new Panel();
		taPanel.setLayout(new RowLayout());
		globalMessageTicket = new GlobalMessageTicket();
				
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		rsGridPanel = new GridPanel(); //Can't edit resourceSummaries
		rsGridPanel.setTitle("Resource Summaries in Package");
		rsGridPanel.setAutoHeight(false);
		taGridPanel = new EditorGridPanel();
		taGridPanel.setTitle("Tender Analysis Details");
		taGridPanel.setAutoHeight(false);
		
		ComboBox unitComboBox = new ComboBox();
		
		Store unitStore = globalSectionController.getUnitStore();
		if(unitStore == null)
			unitStore = new SimpleStore(new String[]{"unitCode", "description"}, new String[][]{});
		unitComboBox.setDisplayField("description");
		unitComboBox.setValueField("unitCode");
		unitComboBox.setSelectOnFocus(true);
		unitComboBox.setForceSelection(true);
		unitComboBox.setListWidth(200);
		unitStore.load();
		unitComboBox.setStore(unitStore);
		
		ColumnConfig rsObjectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		ColumnConfig rsSubsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		ColumnConfig rsResourceDescriptionColConfig = new ColumnConfig("Resource Description", "description", 300, false);
		ColumnConfig rsUnitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		ColumnConfig rsQuantityColConfig = new ColumnConfig("Quantity", "quantity", 100, false);
		rsQuantityColConfig.setRenderer(quantityRenderer);
		rsQuantityColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig rsRateColConfig = new ColumnConfig("Rate", "rate", 150, false);
		rsRateColConfig.setRenderer(rateRenderer);
		rsRateColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig rsAmountColConfig = new ColumnConfig("Amount", "amount", 170, false);
		rsAmountColConfig.setRenderer(amountRenderer);
		rsAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] rsColumns = new ColumnConfig[]{
				rsObjectCodeColConfig,
				rsSubsidiaryCodeColConfig,
				rsResourceDescriptionColConfig,
				rsUnitColConfig,
				rsQuantityColConfig,
				rsRateColConfig,
				rsAmountColConfig
		};
		
		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		String rateDP = prefs.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		if(rateDP == null)
			rateDP = "2";
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(Integer.valueOf(rateDP));
		
		ColumnConfig taBillItemColConfig = new ColumnConfig("B/P/I", "billItem", 65, false);
		taBillItemColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig taObjectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		taObjectCodeColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig taSubsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		taSubsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig taResourceDescriptionColConfig = new ColumnConfig("Resource Description", "description", 250, false);
		taResourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig taUnitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		taUnitColConfig.setEditor(new GridEditor(unitComboBox));
		ColumnConfig taQuantityColConfig = new ColumnConfig("Quantity", "quantity", 100, false);
		taQuantityColConfig.setRenderer(quantityRenderer);
		taQuantityColConfig.setAlign(TextAlign.RIGHT);
		taQuantityColConfig.setEditor(new GridEditor(quantNumberField));
		ColumnConfig taRateColConfig = new ColumnConfig("Rate", "rate", 150, false);
		taRateColConfig.setRenderer(rateRenderer);
		taRateColConfig.setAlign(TextAlign.RIGHT);
		taRateColConfig.setEditor(new GridEditor(rateNumberField));
		ColumnConfig taAmountColConfig = new ColumnConfig("Amount", "amount", 170, false);
		taAmountColConfig.setRenderer(amountRenderer);
		taAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] taColumns = new ColumnConfig[]{
				taBillItemColConfig,
				taObjectCodeColConfig,
				taSubsidiaryCodeColConfig,
				taResourceDescriptionColConfig,
				taUnitColConfig,
				taQuantityColConfig,
				taRateColConfig,
				taAmountColConfig
		};
		
		rsGridPanel.setColumnModel(new ColumnModel(rsColumns));
		taGridPanel.setColumnModel(new ColumnModel(taColumns));
		
		MemoryProxy rsProxy = new MemoryProxy(new Object[][]{});
		ArrayReader rsReader = new ArrayReader(tenderAnalysisRecordDef);
		rsDataStore = new Store(rsProxy, rsReader);
		rsDataStore.load();
		rsGridPanel.setStore(rsDataStore);
		MemoryProxy taProxy = new MemoryProxy(new Object[][]{});
		ArrayReader taReader = new ArrayReader(tenderAnalysisRecordDef);
		taDataStore = new Store(taProxy, taReader);
		taDataStore.load();
		taGridPanel.setStore(taDataStore);
		
		rsGridPanel.setEnableColumnMove(false);
		rsGridPanel.setEnableHdMenu(false);
		taGridPanel.setEnableColumnMove(false);
		taGridPanel.setEnableHdMenu(false);
		
		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setFileUpload(true);
		uploadPanel.setPaddings(5);
		uploadPanel.setHeight(35);
		Panel toolbarPanel = new Panel();
		toolbarPanel.setLayout(new HorizontalLayout(20));
		//Add resource
		Button addResourceButton = new Button("Add Resource");
		addResourceButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				addEmptyResource();
			}
		});
		//Copy resources
		Button copyResourcesButton = new Button("Copy Resources");
		copyResourcesButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				copyResourcesToTenderAnalyses();
			}
		});
		//Clear resources
		Button clearResourcesButton = new Button("Clear Resources");
		clearResourcesButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				taDataStore.removeAll();
				for(String accountCode : accountAmounts.keySet()){
					double[] amounts = accountAmounts.get(accountCode);
					Record summaryRecord = rsDataStore.getAt((int)amounts[RECORDIND]);
					summaryRecord.set("amount", Double.toString(amounts[RSTOTAL]));
					summaryRecord.commit();
					amounts[TATOTAL] = 0;
					accountAmounts.put(accountCode, amounts);
				}
			}
		});
		//Export to Excel
		Button downloadButton = new Button("Export to Excel");
		downloadButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				String jobNumber = globalSectionController.getJob().getJobNumber();
				com.google.gwt.user.client.Window.open(GlobalParameter.TENDER_ANALYSIS_EXCEL_DOWNLOAD_URL + "?jobNumber="+jobNumber +"&packageNo="+packageNo, "_blank", "");
			}
		});
		//Import from excel file
		final TextField fileInputField = new TextField("File", "file");
		fileInputField.setInputType("file");
		fileInputField.setAllowBlank(false);
		fileInputField.setHideLabel(true);
		Button uploadButton = new Button("Import from Excel");
		uploadButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				
				if(scPackage.getPaymentStatus()!=null && SCPackage.DIRECT_PAYMENT.equals(scPackage.getPaymentStatus())){
					MessageBox.alert("Payment Requisition does not support 'Import from Excel' function.");
					return;
				}

				if(fileInputField.getValueAsString().length() == 0)
					return;
				if (taDataStore.getModifiedRecords().length != 0 || (taDataStore.getCount() > 0 && !taDataStore.getAt(0).isDirty())) {
					MessageBox.confirm("Confirm", "All the original data will be deleted and cannot be reversed. <br />Are you sure you want to import the excel?",   
	                        new MessageBox.ConfirmCallback() {   
                        public void execute(String btnID) {
                        	if (btnID.equals("yes"))
                				uploadPanel.getForm().submit(GlobalParameter.TENDER_ANALYSIS_EXCEL_UPLOAD_URL, null, Connection.POST, "Importing...", false);
                        }   
                    });
					return;
				}else{
					uploadPanel.getForm().submit(GlobalParameter.TENDER_ANALYSIS_EXCEL_UPLOAD_URL, null, Connection.POST, "Importing...", false);
				}
			}
		});

		final Hidden jobNumberHiddenField = new Hidden("jobNumber", globalSectionController.getJob().getJobNumber());
		final Hidden packageNumberHiddenField = new Hidden("packageNumber", packageNo);
		
		uploadPanel.add(jobNumberHiddenField);
		uploadPanel.add(packageNumberHiddenField);
		
		toolbarPanel.add(addResourceButton);
		toolbarPanel.add(copyResourcesButton);
		toolbarPanel.add(clearResourcesButton);
		toolbarPanel.add(downloadButton);
		toolbarPanel.add(fileInputField);
		toolbarPanel.add(uploadButton);
		uploadPanel.add(toolbarPanel);

		
		uploadPanel.addFormListener(new FormListenerAdapter(){
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				MessageBox.alert("Import Successfully.");
				globalSectionController.closeCurrentWindow();
				globalSectionController.refreshUnawardedPackageStore();
				globalSectionController.refreshAwardedPackageStore();
				globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				JSONValue jsonValue = JSONParser.parse(responseText);
				JSONObject jsonObj = jsonValue.isObject();
				MessageBox.alert("Error", jsonObj.get("error").isString().stringValue());
			}
		});
		
		rsPanel.add(rsGridPanel);
		mainPanel.add(rsPanel);
		mainPanel.add(uploadPanel);
		taPanel.add(taGridPanel);
		mainPanel.add(taPanel);
		this.add(mainPanel);
		
		taGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				if(field.equals("billItem")){
					if(value != null && ((String)value).trim().length() > 0){
						String[] bip = ((String)value).split("/");
						if(bip.length != 5){
							MessageBox.alert("B/P/I code is invalid");
							return false;
						}
					}
				}
				else if(field.equals("objectCode") && (value == null || ((String)value).length() != 6)){
					MessageBox.alert("Object code must be 6 digits in length");
					return false;
				}
				else if(field.equals("subsidiaryCode") && (value == null || ((String)value).length() != 8)){
					MessageBox.alert("Subsidiary code must be 8 digits in length");
					return false;
				}
				else if(field.equals("quantity") || field.equals("rate")){
					double quantity = record.getAsDouble("quantity");
					double rate = record.getAsDouble("rate");
					if(field.equals("quantity"))
						quantity = Double.parseDouble(value.toString());
					else
						rate = Double.parseDouble(value.toString());
					double amount = quantity*rate;
					double oldAmount = record.getAsDouble("amount");
					String accountCode = record.getAsString("objectCode") + "-" + record.getAsString("subsidiaryCode");
					if(!balanceAccounts(accountCode, amount-oldAmount)){
						MessageBox.alert("Account code is invalid");
						return false;
					}
					record.set("amount", Double.toString(amount));
				}
				if(field.equals("objectCode") || field.equals("subsidiaryCode")){
					if(record.getAsDouble("amount") != 0.0){
						double quantity = record.getAsDouble("quantity");
						double rate = record.getAsDouble("rate");
						double amount = quantity*rate;
						String objectCode = record.getAsString("objectCode");
						String subsidiaryCode = record.getAsString("subsidiaryCode");
						String newAccountCode = "";
						if(field.equals("objectCode"))
							newAccountCode = (String)value + "-" + subsidiaryCode;
						else
							newAccountCode = objectCode + "-" + (String)value;
						//try to balance new account
						if(!balanceAccounts(newAccountCode, amount)){
							MessageBox.alert("Account code is invalid");
							return false;
						}
						//balance the old account
						balanceAccounts(objectCode + "-" + subsidiaryCode, -amount);
					}
				}
				return true;
			}
		});
		
		taGridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e){
				globalMessageTicket.refresh();
				e.stopEvent();
				showMenu(rowIndex, e);
			}
		});
				
		GridView rsView = new CustomizedGridView();
		rsView.setAutoFill(true);
		rsGridPanel.setView(rsView);
		GridView taView = new CustomizedGridView();
		taView.setAutoFill(true);
		taGridPanel.setView(taView);
		
		Button saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					saveTADetails();
				};
		});		
		this.addButton(saveButton);
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					if (taDataStore.getModifiedRecords().length!=0 || rsDataStore.getModifiedRecords().length!=0){
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
		this.addButton(closeButton);
	}
	
	public void populateGrid(List<BQResourceSummary> resourceSummaries){
		rsDataStore.removeAll();
		taDataStore.removeAll();
		accountAmounts.clear();
		
		if (resourceSummaries == null || resourceSummaries.size() == 0){
			return;
		}
		
		BQResourceSummary firstResource = resourceSummaries.get(0);
		String lastAC = firstResource.getObjectCode() + "-" + firstResource.getSubsidiaryCode();
		double acAmount = 0;
		String lastOC = firstResource.getObjectCode();
		String lastSC = firstResource.getSubsidiaryCode();
		for (BQResourceSummary resourceSummary : resourceSummaries){
			String objectCode = resourceSummary.getObjectCode();
			String subsidiaryCode = resourceSummary.getSubsidiaryCode(); 
			Double quantity = resourceSummary.getQuantity();
			Double rate = resourceSummary.getRate();
			Double amount = quantity*rate;
			Record record = tenderAnalysisRecordDef.createRecord(new Object[]{
				null,
				objectCode,
				subsidiaryCode,
				resourceSummary.getResourceDescription(),
				resourceSummary.getUnit(),
				quantity,
				rate,
				amount,
				resourceSummary.getId()
			});
			
			String accountCode = objectCode + "-" + subsidiaryCode;			
			if(lastAC.equals(accountCode))
				acAmount += amount;
			else{
				//Insert a summary record, showing account balance
				Record summaryRecord = tenderAnalysisRecordDef.createRecord(new Object[]{null, "<b>"+lastOC+"</b>","<b>"+lastSC+"</b>","<b>Remaining Balance</b>",null,null,null,acAmount, null});
				summaryRecord.set("isTotal", true);
				rsDataStore.add(summaryRecord);
				accountAmounts.put(lastAC, new double[]{acAmount, 0, rsDataStore.getCount()-1});
				lastAC = accountCode;
				acAmount = amount;
				lastOC = objectCode;
				lastSC = subsidiaryCode;
			}
			rsDataStore.add(record);
		}
		//Last resourceSummary
 		Record summaryRecord = tenderAnalysisRecordDef.createRecord(new Object[]{null, "<b>"+lastOC+"</b>","<b>"+lastSC+"</b>","<b>Remaining Balance</b>",null,null,null,acAmount, null});
		summaryRecord.set("isTotal", true);
		rsDataStore.add(summaryRecord);
		accountAmounts.put(lastAC, new double[]{acAmount, 0, rsDataStore.getCount()-1});
		addEmptyResource();
	}
	
	public void populateTAGrid(List<TenderAnalysisDetail> taDetails){
		if(taDataStore.getCount() == 1 && !taDataStore.getAt(0).isDirty())
			taDataStore.removeAll();
		List<String> invalidAccounts = new ArrayList<String>();
		for(TenderAnalysisDetail taDetail : taDetails){
			String accountCode = taDetail.getObjectCode() + "-" + taDetail.getSubsidiaryCode();
			Double amount = taDetail.getFeedbackRateDomestic() * taDetail.getQuantity();
			Record record = tenderAnalysisRecordDef.createRecord(new Object[]{
					taDetail.getBillItem(),
					taDetail.getObjectCode(),
					taDetail.getSubsidiaryCode(),
					taDetail.getDescription(),
					taDetail.getUnit(),
					taDetail.getQuantity(),
					taDetail.getFeedbackRateDomestic(),
					amount,
					taDetail.getResourceNo()
			});
			taDataStore.add(record);
			double[] amounts = accountAmounts.get(accountCode);
			if(amounts == null)
				invalidAccounts.add(accountCode);
			else{
				amounts[TATOTAL] += amount;
				accountAmounts.put(accountCode, amounts);
			}
		}
		for(double[] amounts : accountAmounts.values()){
			double accountBalance = amounts[RSTOTAL] - amounts[TATOTAL];
			Record summaryRecord = rsDataStore.getAt((int)amounts[RECORDIND]);
			summaryRecord.set("amount", Double.toString(accountBalance));
			summaryRecord.commit();
		}
		if(!invalidAccounts.isEmpty()){
			StringBuffer error = new StringBuffer("Invalid account codes:");
			for(String invalidAccount : invalidAccounts){
				error.append(" " + invalidAccount);
			}
			MessageBox.alert(error.toString());
		}
	}

	
	public void addEmptyResource(){
		Record newRecord = tenderAnalysisRecordDef.createRecord(new Object[]{null,null,null,null,null,"0","0","0", null});
		taDataStore.add(newRecord);
	}
	
	public void saveTADetails(){
		//Check that all account amounts are balanced
		for(String accountCode : accountAmounts.keySet()){
			double[] amounts = accountAmounts.get(accountCode);
			if(RoundingUtil.round(amounts[RSTOTAL],2) != RoundingUtil.round(amounts[TATOTAL],2)){
				MessageBox.alert("Amounts are not balanced for account: " + accountCode);
				return;
			}
		}
		List<TenderAnalysisDetail> taDetails = new ArrayList<TenderAnalysisDetail>();
		for(int i = 0; i < taDataStore.getCount(); i++){
			Record record = taDataStore.getAt(i);
			String accountCode = record.getAsString("objectCode") + "-" + record.getAsString("subsidiaryCode");
			if(accountAmounts.get(accountCode) == null){
				MessageBox.alert("Invalid account code: " + accountCode);
				return;
			}
			TenderAnalysisDetail taDetail = tenderAnalysisDetailFromRecord(record);
			if(taDetail == null)
				return;
			taDetail.setSequenceNo(Integer.valueOf(i+1));
			
			taDetails.add(taDetail);
		}
		globalSectionController.saveTenderAnalysisDetails(packageNo, 0, null, 1.0, taDetails, true);
	}
	
	public TenderAnalysisDetail tenderAnalysisDetailFromRecord(Record record){
		TenderAnalysisDetail taDetail = new TenderAnalysisDetail();
		taDetail.setLineType("BQ");
		taDetail.setBillItem(record.getAsString("billItem"));
		String objectCode = record.getAsString("objectCode");
		if(objectCode == null || objectCode.trim().length() != 6){
			MessageBox.alert("Object code must be 6 digits in length");
			return null;
		}
		taDetail.setObjectCode(objectCode);
		String subsidiaryCode = record.getAsString("subsidiaryCode");
		if(subsidiaryCode == null || subsidiaryCode.trim().length() != 8){
			MessageBox.alert("Subsidiary code must be 8 digits in length");
			return null;
		}
		taDetail.setSubsidiaryCode(subsidiaryCode);
		String description = record.getAsString("description");
		if(description == null || description.trim().length() == 0){
			MessageBox.alert("Description must not be blank");
			return null;
		}
		taDetail.setDescription(description);
		String unit = record.getAsString("unit");
		if(unit == null || unit.trim().length() == 0){
			MessageBox.alert("Unit must not be blank");
			return null;
		}
		taDetail.setUnit(unit);
		taDetail.setQuantity(Double.valueOf(record.getAsDouble("quantity")));
		taDetail.setFeedbackRateDomestic(Double.valueOf(record.getAsDouble("rate")));
		if(record.getAsString("resourceNo")!=null && !"".equals(record.getAsString("resourceNo")))
			taDetail.setResourceNo(Integer.valueOf(record.getAsString("resourceNo")));
		return taDetail;
	}
	
	public boolean balanceAccounts(String accountCode, double addedAmount){
		double[] amounts = accountAmounts.get(accountCode);
		if(amounts==null)
			return false; //accountCode is invalid
		amounts[TATOTAL] += addedAmount;
		accountAmounts.put(accountCode, amounts);
		
		double accountBalance = amounts[RSTOTAL] - amounts[TATOTAL];
		Record summaryRecord = rsDataStore.getAt((int)amounts[RECORDIND]);
		summaryRecord.set("amount", Double.toString(accountBalance));
		summaryRecord.commit();
		return true;
	}
	
	public void copyResourcesToTenderAnalyses(){
		if(taDataStore.getCount()>1 || (taDataStore.getCount() == 1 && taDataStore.getAt(0).isDirty())){
			MessageBox.confirm("Please Confirm", "Existing records will be deleted before resources are copied. Continue?", new MessageBox.ConfirmCallback(){
				public void execute(String btnID) {
					if(btnID.equals("yes")){
						copyHelper();
					}
				}
			});
		}
		else{
			copyHelper();
		}
	}
	
	public void copyHelper(){
		taDataStore.removeAll();
		int taRow = 0;
		int rsRow = 0;
		for (Record record : rsDataStore.getRecords()) {
			if (!record.getAsBoolean("isTotal")) {
				Record newRecord = tenderAnalysisRecordDef.createRecord(new Object[]{null,null,null,null,null,"0","0","0", null});
				taDataStore.add(newRecord);
				Record taRecord = taDataStore.getAt(taRow);
				taRecord.set("objectCode", rsDataStore.getAt(rsRow).getAsString("objectCode"));
				taRecord.set("subsidiaryCode", rsDataStore.getAt(rsRow).getAsString("subsidiaryCode"));
				taRecord.set("description", rsDataStore.getAt(rsRow).getAsString("description"));
				taRecord.set("unit", rsDataStore.getAt(rsRow).getAsString("unit"));
				taRecord.set("quantity", rsDataStore.getAt(rsRow).getAsString("quantity"));
				taRecord.set("rate", rsDataStore.getAt(rsRow).getAsString("rate"));
				taRecord.set("amount", rsDataStore.getAt(rsRow).getAsString("amount"));
				taRecord.set("resourceNo", rsDataStore.getAt(rsRow).getAsString("resourceNo"));
				taRow++;
			}
			rsRow++;
		}

		
		for(double[] amounts : accountAmounts.values()){
			amounts[TATOTAL] = amounts[RSTOTAL];
			Record summaryRecord = rsDataStore.getAt((int)amounts[RECORDIND]);
			summaryRecord.set("amount", "0");
		}
		rsDataStore.commitChanges();
	}
	
	public void showMenu(int rowIndex, EventObject e){	
		currentRow = rowIndex;
		if(menu == null){
			menu = new Menu();
			Item deleteItem = (Item) ComponentMgr.getComponent("delete-item");
			if(deleteItem != null)
				deleteItem.destroy();
			deleteItem = new Item("Delete", new BaseItemListenerAdapter(){
				public void onClick(BaseItem item, EventObject e){
					Record record = taDataStore.getAt(currentRow);
					String accountCode = record.getAsString("objectCode") + record.getAsString("subsidiaryCode");
					double amount = record.getAsDouble("amount");
					balanceAccounts(accountCode, -amount);
					taDataStore.remove(record);
				}
			});
			deleteItem.setId("delete-item");
			menu.addItem(deleteItem);
		}
		menu.showAt(e.getXY());
	}
}
