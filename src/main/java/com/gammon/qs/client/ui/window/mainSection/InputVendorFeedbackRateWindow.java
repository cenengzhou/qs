package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.renderer.NonTotalEditableColRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
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

public class InputVendorFeedbackRateWindow extends Window {
	public static final String MAIN_PANEL_ID = "InputVendorFeedbackRateWindow";
	
	private GlobalSectionController globalSectionController;
	private String packageNo;
	private Integer vendorNo;
	
	private Panel mainPanel;
	private EditorGridPanel gridPanel;
	
	private ArrowKeyNavigation arrowKeyNavigation;
	
	private NumberField exchangeRateField;
	
	private Store dataStore;
	
	private GlobalMessageTicket globalMessageTicket;
	
	private final RecordDef taDetailRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("billItem"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"),
					new StringFieldDef("budgetedRate"),
					new StringFieldDef("feedbackRate"),
					new StringFieldDef("feedbackRateDomestic"),
					new StringFieldDef("lineType"),
					new IntegerFieldDef("resourceNo"),
					new StringFieldDef("id")
			});
	
	private ComboBox currencyCodeListComboBox;
	
	private List<String> uneditableTADetailIDs;
	private String companyBaseCurrency;
	
	private Boolean zeroCostRate = false;
	private Boolean zeroSCRate = false;
	
	public InputVendorFeedbackRateWindow(final GlobalSectionController globalSectionController, final String packageNo, final Integer vendorNo, String companyBaseCurrency){
		super();
		this.globalSectionController = globalSectionController;
		this.packageNo = packageNo;
		this.vendorNo = vendorNo;
		this.companyBaseCurrency = companyBaseCurrency;
		globalMessageTicket = new GlobalMessageTicket();
		
		setupUI();
		
		globalSectionController.setCurrentWindow(this);
		globalSectionController.getCurrentWindow().show();
		
		obtainUneditableTADetailIDs(globalSectionController.getJob().getJobNumber(), packageNo, vendorNo);
	}
	
	private void setupUI(){
		setModal(true);
		setTitle("Input/Edit Feedback Rates - Vendor: " + vendorNo + " Package: " + packageNo);
		setPaddings(5);
		setWidth(900);
		setHeight(500);
		setClosable(false);
		setId(MAIN_PANEL_ID);
		setLayout(new FitLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		gridPanel = new EditorGridPanel();
		
		this.arrowKeyNavigation = new ArrowKeyNavigation(gridPanel);
		
		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		String rateDP = prefs.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		if(rateDP == null)
			rateDP = "2";		
		
		Renderer quantityRenderer = new QuantityRenderer(user);
		Renderer amountRendererNonTotal = new AmountRendererNonTotal(user);
		Renderer rateRenderer = new RateRenderer(user);
		
		ComboBox unitComboBox = new ComboBox();
		Store unitStore = globalSectionController.getUnitStore();
		unitComboBox.setDisplayField("description");
		unitComboBox.setValueField("unitCode");
		unitComboBox.setSelectOnFocus(true);
		unitComboBox.setForceSelection(true);
		unitComboBox.setListWidth(200);
		unitStore.load();
		unitComboBox.setStore(unitStore);
		
		currencyCodeListComboBox = new ComboBox();
		Store currencyStore = globalSectionController.getCurrencyCodeStore();
		currencyCodeListComboBox.setDisplayField("currencyDescription");
		currencyCodeListComboBox.setValueField("currencyCode");
		currencyCodeListComboBox.setSelectOnFocus(true);
		currencyCodeListComboBox.setForceSelection(true);
		currencyCodeListComboBox.setListWidth(170);
		currencyStore.load();
		currencyCodeListComboBox.setStore(currencyStore);

		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(Integer.valueOf(rateDP));
		
		String currency = globalSectionController.getJob().getBillingCurrency();
		if (currency!=null )
			currency = currency.trim();
		else 
			currency = "";
			 
		ColumnConfig billItemColConfig = new ColumnConfig("B/P/I", "billItem", 65, false);
		
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		
		ColumnConfig resourceDescriptionColConfig = new ColumnConfig("Resource Description", "description", 210, false);
		resourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		
		ColumnConfig unitColConfig = new ColumnConfig("Unit", "unit", 25, false);
		unitColConfig.setEditor(new GridEditor(unitComboBox));
		
		ColumnConfig quantityColConfig = new ColumnConfig("Quantity", "quantity", 100, false);
		quantityColConfig.setEditor(new GridEditor(quantNumberField));
		quantityColConfig.setRenderer(quantityRenderer);
		quantityColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig budgetedRateColConfig = new ColumnConfig("Budgeted Rate (" + companyBaseCurrency + ")", "budgetedRate", 110, false);
		budgetedRateColConfig.setRenderer(rateRenderer);
		budgetedRateColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig feedbackRateColConfig = new ColumnConfig("Feedback Rate", "feedbackRate", 110, false);
		Field feedBackRateField = FieldFactory.createNegativeNumberField(3);
		feedBackRateField.addListener(this.arrowKeyNavigation);
		feedbackRateColConfig.setAlign(TextAlign.RIGHT);
		feedbackRateColConfig.setEditor(new GridEditor(feedBackRateField));
		feedbackRateColConfig.setRenderer(new NonTotalEditableColRenderer(rateRenderer));
		
		ColumnConfig feedbackRateDomesticColConfig = new ColumnConfig("Feedback Rate (" + companyBaseCurrency + ")", "feedbackRateDomestic", 110, false);
		feedbackRateDomesticColConfig.setRenderer(amountRendererNonTotal);
		feedbackRateDomesticColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] columns = new ColumnConfig[]{
				billItemColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				resourceDescriptionColConfig,
				unitColConfig,
				quantityColConfig,
				budgetedRateColConfig,
				feedbackRateColConfig,
				feedbackRateDomesticColConfig
		};
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		//EditorGridListener for feedback rate
		gridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value,
					int rowIndex, int colIndex){
				boolean isEditable = false;
				globalMessageTicket.refresh();
				
				if(field.equals("feedbackRate")){
					if(record.getAsString("id") != null && uneditableTADetailIDs.contains(record.getAsString("id")))
						MessageBox.alert("Feedback Rate cannot be edited - this package has Posted Certified Quantity or Posted/Cumulative Work Done Quantity");
					else
						isEditable = true;
				}
				else if(record.getAsString("lineType").equals("B1"))
					isEditable =  true;
				else
					isEditable = false;
				
				if (isEditable)
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				else
					arrowKeyNavigation.resetState();
				
				return isEditable;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if(field.equals("objectCode") && (value == null || value.toString().length() != 6)){
					MessageBox.alert("Object code must be 6 digits in length");
					return false;
				}
				else if(field.equals("subsidiaryCode") && (value == null || value.toString().length() != 8)){
					MessageBox.alert("Subsidiary code must be 8 digits in length");
					return false;
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, 
					Object oldValue, int rowIndex, int colIndex){
				if(!field.equals("feedbackRate"))
					return;
				double feedbackRate = (newValue == null) ? 0.0 : Double.parseDouble(newValue.toString());
				double exchangeRate = exchangeRateField.getValue().doubleValue();
				record.set("feedbackRateDomestic", Double.toString(feedbackRate*exchangeRate));
			}
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(taDetailRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		gridPanel.setEnableColumnMove(false);
		gridPanel.setEnableHdMenu(false);
		
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		gridPanel.setView(view);
		
		mainPanel.add(gridPanel);
		this.add(mainPanel);
		
		exchangeRateField = new NumberField();
		exchangeRateField.setDecimalPrecision(Integer.valueOf(7));
		exchangeRateField.addListener(new FieldListenerAdapter(){
			public void onChange(Field field, Object newVal, Object oldVal){
				recalculateDomesticFeedbackRates();
			}
			public void onSpecialKey(Field field, EventObject e){
				if(e.getCharCode() == EventObject.ENTER)
					recalculateDomesticFeedbackRates();
			}
		});
		Toolbar toolbar = new Toolbar();
		toolbar.addText("Currency Code:");
		toolbar.addField(currencyCodeListComboBox);
		toolbar.addSpacer();
		toolbar.addSpacer();
		toolbar.addText("Exchange Rate:");
		toolbar.addField(exchangeRateField);
		toolbar.addFill();
		toolbar.addText("Feedback Rate(" + companyBaseCurrency + ") = Feedback Rate  * Exchange Rate");
		
		setTopToolbar(toolbar);
		
		Button saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				
				UIUtil.maskPanelById(globalSectionController.getCurrentWindow().getId(), GlobalParameter.SAVING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPaymentRepository().obtainPaymentLatestCert(globalSectionController.getJob().getJobNumber(), packageNo, new AsyncCallback<SCPaymentCert>() {
					public void onSuccess(final SCPaymentCert paymentCert) {
						if(paymentCert!=null && paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
							MessageBox.confirm("Payment Requisition", "Payment Requisition with status 'Pending' will be deleted. Proceed?",
									new MessageBox.ConfirmCallback(){
								public void execute(String btnID) {
									if(btnID.equals("yes")){
										saveTADetails();
									}else
										UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
								}
							});
						}
						else
							saveTADetails();
					}
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
						UIUtil.throwException(e);
					}
				});
			};
		});		
		
		addButton(saveButton);
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalSectionController.closeCurrentWindow();
				};
		});		
		this.addButton(closeButton);
	}
	

	public void populateGrid(List<TenderAnalysisDetail> taDetails, String currencyCode, Double exchangeRate){
		dataStore.removeAll();
		
		for (TenderAnalysisDetail taDetail : taDetails){
			Double vendorRate  = taDetail.getFeedbackRateForeign();
			if(vendorRate == null)
				vendorRate = 0.0;
			Record record = taDetailRecordDef.createRecord(new Object[]{
					taDetail.getBillItem(),
					taDetail.getObjectCode(),
					taDetail.getSubsidiaryCode(),
					taDetail.getDescription(),
					taDetail.getUnit(),
					taDetail.getQuantity(),
					taDetail.getFeedbackRateDomestic(), //Budgeted rate will be stored (temporarily) in feedbackRateDomestic
					vendorRate,
					vendorRate*exchangeRate,
					taDetail.getLineType(),
					taDetail.getResourceNo(),
					taDetail.getId()
			});
			dataStore.add(record);
		}
		if(currencyCode == null)
			currencyCode = "";
		else
			currencyCode = currencyCode.trim();
		
		currencyCodeListComboBox.setValue(currencyCode);
		exchangeRateField.setValue(exchangeRate);
	}
	
	public void saveTADetails() {
		String currencyCode = currencyCodeListComboBox.getValue();

		if (currencyCode == null || currencyCode.trim().length() == 0) {
			UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
			MessageBox.alert("Please input a currency code");
			return;
		} else {
			try {
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().currencyCodeValidation(currencyCode.trim(), new AsyncCallback<String>() {

					public void onFailure(Throwable msg) {
						UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
						UIUtil.alert(msg);
					}

					public void onSuccess(String result) {
						UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
						if (result != null) {
							double exchangeRate = exchangeRateField.getValue() == null ? 0.0 : exchangeRateField.getValue().doubleValue();
							if (exchangeRate <= 0.0) {
								MessageBox.alert("Exchange rate must be greater than 0");
								
								return;
							}
							List<TenderAnalysisDetail> taDetails = new ArrayList<TenderAnalysisDetail>();
							int sequenceNo = 1;
							zeroCostRate=false;
							zeroSCRate=false;
							for (Record record : dataStore.getRecords()) {
								TenderAnalysisDetail taDetail = tenderAnalysisDetailFromRecord(record);
								if (taDetail == null)
									return;
								taDetail.setSequenceNo(sequenceNo++);
								taDetails.add(taDetail);
							}
							globalSectionController.saveTenderAnalysisDetails(packageNo, vendorNo, currencyCodeListComboBox.getValueAsString().trim(), exchangeRate, taDetails, false);
						} else {
							MessageBox.alert("Currency Code does not exist.");
						}
					}

				});
			} catch (Exception e) {
				UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
				UIUtil.alert(e.getMessage());
			}
		}
	}
	
	public TenderAnalysisDetail tenderAnalysisDetailFromRecord(Record record) {
		TenderAnalysisDetail taDetail = new TenderAnalysisDetail();
		
		//Block the case of (Cost=0 and SCRate!=0) and (Cost!=0 and SCRate=0)
		if(Double.valueOf(record.getAsString("feedbackRate"))==0.0 && Double.valueOf(record.getAsString("budgetedRate"))!=0.0)
			zeroSCRate=true;
		else if(Double.valueOf(record.getAsString("budgetedRate"))==0.0 && Double.valueOf(record.getAsString("feedbackRate"))!=0.0)
			zeroCostRate=true;

		if(zeroCostRate && zeroSCRate){
			MessageBox.alert(	"Cases: </br>" +
								"Cost Rate =0 AND SC Rate >0 </br>"+
								"Cost Rate >0 AND SC Rate =0 </br>"+
								"are not allowed."
								);
			return null;
		}
		
		taDetail.setLineType(record.getAsString("lineType"));
		taDetail.setBillItem(record.getAsString("billItem"));
		taDetail.setResourceNo(record.getAsInteger("resourceNo"));
		String objectCode = record.getAsString("objectCode");
		if (objectCode == null || objectCode.trim().length() != 6) {
			MessageBox.alert("Object code must be 6 digits in length");
			return null;
		}
		taDetail.setObjectCode(objectCode);
		String subsidiaryCode = record.getAsString("subsidiaryCode");
		if (subsidiaryCode == null || subsidiaryCode.trim().length() != 8) {
			MessageBox.alert("Subsidiary code must be 8 digits in length");
			return null;
		}
		taDetail.setSubsidiaryCode(subsidiaryCode);
		String description = record.getAsString("description");
		if (description == null || description.trim().length() == 0) {
			MessageBox.alert("Description must not be blank");
			return null;
		}
		taDetail.setDescription(description);
		String unit = record.getAsString("unit");
		if (unit == null || unit.trim().length() == 0) {
			MessageBox.alert("Unit must not be blank");
			return null;
		}
		taDetail.setUnit(unit);
		taDetail.setQuantity(Double.valueOf(record.getAsDouble("quantity")));
		Double feedbackRate = Double.valueOf(record.getAsDouble("feedbackRate"));
		/*if(feedbackRate==0.0){
			MessageBox.alert("Feedback Rate cannot be 0.");
			return null;
		}*/
			
		taDetail.setFeedbackRateForeign(feedbackRate);
		double exchangeRate = exchangeRateField.getValue().doubleValue();
		taDetail.setFeedbackRateDomestic(feedbackRate * exchangeRate);
		if(record.getAsString("id")!=null && !"".equals(record.getAsString("id")))
			taDetail.setId(Long.valueOf(record.getAsString("id")));
		
		return taDetail;
	}
	
	public void recalculateDomesticFeedbackRates() {
		double exchangeRate = exchangeRateField.getValue().doubleValue();
		if (exchangeRate <= 0.0) {
			MessageBox.alert("Exchange rate must be greater than 0");
			return;
		}

		for (Record record : dataStore.getRecords()) {
			double feedbackRate = record.getAsDouble("feedbackRate");
			double feedbackRateDomestic = feedbackRate * exchangeRate;
			record.set("feedbackRateDomestic", Double.toString(feedbackRateDomestic));
		}
	}
	
	private void obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo) {
		UIUtil.maskPanelById(globalSectionController.getCurrentWindow().getId(), GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getTenderAnalysisRepository().obtainUneditableTADetailIDs(jobNo, packageNo, vendorNo, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> resultList) {
				UIUtil.unmaskPanelById(globalSectionController.getCurrentWindow().getId());
				uneditableTADetailIDs = resultList;
			}
		});
	}
}
