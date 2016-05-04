/**
 * GammonQS-PH3
 * BudgetAndForecastImportMainPanel.java
 * @author koeyyeung
 * Created on May 8, 2013 4:26:21 PM
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.BudgetForecastWrapper;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RadioButton;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class BudgetForecastImportMainPanel extends Panel {
	private static final boolean	SORTABLE_TRUE				= true;

	private static final String		OBJECT_CODE_RECORD_NAME		= "objectCodeRecordName";
	private static final String		DESCRIPTION_RECORD_NAME		= "descriptionRecordName";
	private static final String		SUBCONTRACT_NO_RECORD_NAME	= "subconNoRecordName";
	private static final String		ACTUAL_COST_RECORD_NAME		= "actualCostRecordName";
	private static final String		ORIGINAL_BUDGET_RECORD_NAME	= "originalBudgetRecordName";
	private static final String		LATEST_FORECAST_RECORD_NAME	= "latestForecastRecordName";

	private RecordDef				budgetAndForecastRecordDef	= 	new RecordDef(new FieldDef[] { 
																	new StringFieldDef(OBJECT_CODE_RECORD_NAME), 
																	new StringFieldDef(DESCRIPTION_RECORD_NAME), 
																	new StringFieldDef(SUBCONTRACT_NO_RECORD_NAME), 
																	new StringFieldDef(ACTUAL_COST_RECORD_NAME), 
																	new StringFieldDef(ORIGINAL_BUDGET_RECORD_NAME), 
																	new StringFieldDef(LATEST_FORECAST_RECORD_NAME) });
	
	private GridPanel				gridPanel;

	private GlobalMessageTicket		globalMessageTicket;

	// data store
	private Store					dataStore;

	private String					excelType;
	private String					jobNumber;
	private Integer					month;
	private Integer					year;

	// toolbar
	private ToolbarButton			excelToolbarButton;
	private Item					importButton;
	private Item					exportButton;
	private NumberField 			yearField;
	private ComboBox				monthComboBox;

	private List<String>			accessRightsList;

	private GlobalSectionController	globalSectionController;
	private DetailSectionController	detailSectionController;

	public BudgetForecastImportMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		jobNumber = globalSectionController.getJob().getJobNumber();
		globalMessageTicket = new GlobalMessageTicket();

		setupUI();
	}

	private void setupUI() {
		setAutoScroll(true);
		setLayout(new RowLayout());

		setupGridPanel();

		int day = Integer.valueOf(DateUtil.formatDate(new Date(), "dd"));
		month = Integer.valueOf(DateUtil.formatDate(new Date(), "MM"));
		year = Integer.valueOf(DateUtil.formatDate(new Date(), "yyyy"));

		if (!"12".equals(month)) {
			if (day > 25) {
				month = month + 1;
			}
		}
		
		search(month, year);
		
		// hide detail panel
		detailSectionController.getMainPanel().collapse();
	}

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		
		importButton = new Item("Import");
		importButton.setIconCls("upload-icon");
		importButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem button, EventObject e) {
				globalMessageTicket.refresh();
				importExcel();
			}
		});

		exportButton = new Item("Export");
		exportButton.setIconCls("download-icon");
		exportButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem button, EventObject e) {
				globalMessageTicket.refresh();
				exportExcel();
			};
		});

		Menu excelMenu = new Menu();
		excelMenu.addItem(importButton);
		excelMenu.addItem(exportButton);

		excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");

		// check user right
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010304_IMPORT_BUDGET_FORECAST_MAINPANEL, new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				securitySetup();
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});

		String MONTH_VALUE = "monthValue";
		String MONTH_DISPLAY = "monthDisplay"; 
		
		//monthComboBox.setCtCls("table-cell");
		monthComboBox = new ComboBox();
		monthComboBox.setForceSelection(true);
		monthComboBox.setMinChars(1);
		monthComboBox.setValueField(MONTH_VALUE);
		monthComboBox.setDisplayField(MONTH_DISPLAY);
		monthComboBox.setMode(ComboBox.LOCAL);
		monthComboBox.setTriggerAction(ComboBox.ALL);
		monthComboBox.setTypeAhead(true);
		monthComboBox.setSelectOnFocus(true);
		monthComboBox.setWidth(80);
		
		String[][] month = new String[][]{	{"1",	"1"},
											{"2", 	"2"},
											{"3",	"3"},
											{"4", 	"4"},
											{"5", 	"5"},
											{"6",	"6"},
											{"7", 	"7"},
											{"8", 	"8"},
											{"9",	"9"},
											{"10", 	"0"},
											{"11", 	"11"},
											{"12",	"12"}
											};
		Store monthStore = new SimpleStore(new String[]{MONTH_VALUE,MONTH_DISPLAY},month);
		monthComboBox.setStore(monthStore);
		
		final ToolbarButton  searchButton = new ToolbarButton("Search", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				String seletedMonth = monthComboBox.getValue();
				String selectedYear = yearField.getValueAsString();
				String errorMsg = validateBeforeSearch(seletedMonth, selectedYear);
				if(errorMsg.length()>0){
					MessageBox.alert(errorMsg);
					return;
				}
				search(Integer.valueOf(seletedMonth), Integer.valueOf(selectedYear));
			}
		});
		searchButton.setIconCls("find-icon");

		yearField = new NumberField();
		yearField.setAllowDecimals(false);
		yearField.setAllowNegative(false);
		yearField.addKeyPressListener(new EventCallback() {
			public void execute(EventObject e) {
				globalMessageTicket.refresh();
				if (e.getCharCode() == EventObject.ENTER)
					searchButton.fireEvent(searchButton.getClickEvent());
			}
		});
		
		toolbar = new Toolbar();
		toolbar.addItem(new ToolbarTextItem("Month: "));
		toolbar.addField(monthComboBox);
		toolbar.addSeparator();
		toolbar.addItem(new ToolbarTextItem("Fiscal Year: "));
		toolbar.addField(yearField);
		toolbar.addSeparator();
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();

		gridPanel.setTopToolbar(toolbar);
	}

	private void setupGridPanel() {
		gridPanel = new GridPanel();
		gridPanel.setView(new CustomizedGridView());

		setupToolbar();

		final Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer boldAmountRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				if (record.getAsString(SUBCONTRACT_NO_RECORD_NAME).equals("TOTAL:"))
					return "<b>" + str + "</b>";
				else
					return str;
			}
		};

		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", OBJECT_CODE_RECORD_NAME, 80, SORTABLE_TRUE);
		ColumnConfig descriptionColConfig = new ColumnConfig("Description", DESCRIPTION_RECORD_NAME, 250, SORTABLE_TRUE);
		ColumnConfig subconNoColConfig = new ColumnConfig("Subcontract No.", SUBCONTRACT_NO_RECORD_NAME, 120, SORTABLE_TRUE);
		subconNoColConfig.setRenderer(new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {

				if ("TOTAL:".equals(record.getAsString(SUBCONTRACT_NO_RECORD_NAME))) {
					return "<b>" + (String) value + "</b>";
				}
				return (String) value;
			}
		});
		//ColumnConfig fiscalYearConfig = new ColumnConfig("Fiscal Year", FISCAL_YEAR_NAME, 80, SORTABLE_TRUE);
		ColumnConfig actualCostColConfig = new ColumnConfig("Actual Cost", ACTUAL_COST_RECORD_NAME, 130, SORTABLE_TRUE);
		actualCostColConfig.setRenderer(boldAmountRenderer);
		actualCostColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig originalBudgetColConfig = new ColumnConfig("Original Budget", ORIGINAL_BUDGET_RECORD_NAME, 130, SORTABLE_TRUE);
		originalBudgetColConfig.setAlign(TextAlign.RIGHT);
		originalBudgetColConfig.setRenderer(boldAmountRenderer);
		ColumnConfig latestForecastColConfig = new ColumnConfig("Latest Forecast", LATEST_FORECAST_RECORD_NAME, 130, SORTABLE_TRUE);
		latestForecastColConfig.setAlign(TextAlign.RIGHT);
		latestForecastColConfig.setRenderer(boldAmountRenderer);

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(budgetAndForecastRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState(ORIGINAL_BUDGET_RECORD_NAME, SortDir.ASC));

		gridPanel.setStore(dataStore);

		BaseColumnConfig[] columns = new BaseColumnConfig[] { 	objectCodeColConfig, 
																descriptionColConfig, 
																subconNoColConfig, 
																//fiscalYearConfig, 
																actualCostColConfig, 
																originalBudgetColConfig, 
																latestForecastColConfig };

		gridPanel.setColumnModel(new ColumnModel(columns));
		add(gridPanel);
	}

	public void search(final Integer month, final Integer year) {
		monthComboBox.setValue(String.valueOf(month));
		yearField.setValue(String.valueOf(year));
		
		UIUtil.maskPanelById(globalSectionController.getMainSectionController().getMainPanel().getId(), GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAccountLedgerRepository().obtainAccountLedgersByJobNo(jobNumber, month, year, new AsyncCallback<List<BudgetForecastWrapper>>() {
			public void onSuccess(List<BudgetForecastWrapper> budgetAndForecastWrapperList) {
				UIUtil.unmaskPanelById(globalSectionController.getMainSectionController().getMainPanel().getId());
				if(budgetAndForecastWrapperList==null || budgetAndForecastWrapperList.size()==0){
					MessageBox.alert("No data was found for Month: "+month+" - year: "+year+".");
					dataStore.removeAll();
					return;
				}
				
				populateGrid(budgetAndForecastWrapperList);

			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(globalSectionController.getMainSectionController().getMainPanel().getId());
			}
		});
	}

	private void populateGrid(List<BudgetForecastWrapper> budgetAndForecastWrapperList) {
		dataStore.removeAll();

		if (budgetAndForecastWrapperList == null) {
			return;
		}

		Double actualCost = 0.0;
		Double originalBudget = 0.0;
		Double latestForecast = 0.0;
		for (BudgetForecastWrapper budgetAndForecastWrapper : budgetAndForecastWrapperList) {
			actualCost += budgetAndForecastWrapper.getActualCost() != null ? budgetAndForecastWrapper.getActualCost() : 0.0;
			originalBudget += budgetAndForecastWrapper.getOriginalBudget() != null ? budgetAndForecastWrapper.getOriginalBudget() : 0.0;
			latestForecast += budgetAndForecastWrapper.getLatestForecast() != null ? budgetAndForecastWrapper.getLatestForecast() : 0.0;

			Record record = budgetAndForecastRecordDef.createRecord(new Object[] { 
					budgetAndForecastWrapper.getObjectCode(), 
					budgetAndForecastWrapper.getDescription(), 
					budgetAndForecastWrapper.getSubledger(), 
					budgetAndForecastWrapper.getActualCost(), 
					budgetAndForecastWrapper.getOriginalBudget(), 
					budgetAndForecastWrapper.getLatestForecast() });

			dataStore.add(record);
		}


		dataStore.add(budgetAndForecastRecordDef.createRecord(new Object[] {"", 
																			"", 
																			"<b>TOTAL:</b>", 
																			actualCost, 
																			originalBudget, 
																			latestForecast }));
		doLayout();

		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	private void importExcel() {
		final Window importExcelWindow = new Window();
		importExcelWindow.setTitle("Import Excel Window");

		importExcelWindow.setModal(true);
		importExcelWindow.setWidth(300);
		importExcelWindow.setHeight(145);
		importExcelWindow.setLayout(new RowLayout());

		final FormPanel importExcelPanel = new FormPanel();
		importExcelPanel.setHeight(30);
		importExcelPanel.setPaddings(2, 2, 2, 2);

		importExcelPanel.setFileUpload(true);

		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setHideLabel(true);
		fileTextField.setAllowBlank(true);
		fileTextField.setWidth(-1);
		fileTextField.setInputType("file");
		importExcelPanel.add(fileTextField);

		final TextField ledgerTypeTextField = new TextField();
		ledgerTypeTextField.setName("ledgerType");
		ledgerTypeTextField.setVisible(false);
		importExcelPanel.add(ledgerTypeTextField);

		final TextField jobNoTextField = new TextField();
		jobNoTextField.setName("jobNo");
		jobNoTextField.setValue(globalSectionController.getJob().getJobNumber());
		jobNoTextField.setVisible(false);
		importExcelPanel.add(jobNoTextField);

		importExcelPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
				importExcelWindow.close();
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
			}
		});

		Panel radioButtonPanel = new Panel();
		radioButtonPanel.setHeight(50);
		radioButtonPanel.setLayout(new RowLayout());

		String IMPORT_RADIO_GROUP = "importRadioGroup";

		final RadioButton latestForecastRadioButton = new RadioButton(IMPORT_RADIO_GROUP, "Latest Forecast");
		latestForecastRadioButton.setChecked(true);
		final RadioButton originalBudgetRadioButton = new RadioButton(IMPORT_RADIO_GROUP, "Original Budget");

		radioButtonPanel.add(latestForecastRadioButton);
		radioButtonPanel.add(originalBudgetRadioButton);

		importExcelWindow.add(importExcelPanel);
		importExcelWindow.add(radioButtonPanel);

		Button importButton = new Button("Import");
		importButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (latestForecastRadioButton.isChecked()) {
					ledgerTypeTextField.setValue("FC");// forecast
				} else if (originalBudgetRadioButton.isChecked()) {
					ledgerTypeTextField.setValue("OB");// original budget
				}

				if (fileTextField.getValueAsString().length() > 0) {
					importExcelPanel.getForm().submit(GlobalParameter.BUDGET_FORECAST_EXCEL_UPLOAD_URL, null, Connection.POST, "Importing...", false);
				} else {
					MessageBox.alert("Please select an excel file.");
				}
			}
		});

		importExcelWindow.setButtons(new Button[] { importButton });

		importExcelWindow.show();
	}

	private void exportExcel() {
		final Window exportExcelWindow = new Window();
		exportExcelWindow.setTitle("Export Excel Window");

		exportExcelWindow.setModal(true);
		exportExcelWindow.setWidth(300);
		exportExcelWindow.setHeight(120);
		exportExcelWindow.setLayout(new RowLayout());

		Panel radioButtonPanel = new Panel();
		radioButtonPanel.setHeight(50);
		radioButtonPanel.setLayout(new RowLayout());

		String EXPORT_RADIO_GROUP = "exportRadioGroup";

		final RadioButton exportLatestForecastRadioButton = new RadioButton(EXPORT_RADIO_GROUP, "Latest Forecast(Template for import)");
		exportLatestForecastRadioButton.setChecked(true);
		final RadioButton exportAllRadioButton = new RadioButton(EXPORT_RADIO_GROUP, "Actual + Budget + Forecast");

		radioButtonPanel.add(exportLatestForecastRadioButton);
		radioButtonPanel.add(exportAllRadioButton);
		exportExcelWindow.add(radioButtonPanel);

		Button exportButton = new Button("Export");
		exportButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				String seletedMonth = "";
				String selectedYear ="";
				
				if (exportLatestForecastRadioButton.isChecked()) {
					excelType = "FC";
					seletedMonth = String.valueOf(month);
					selectedYear = String.valueOf(year);
				} else if (exportAllRadioButton.isChecked()) {
					excelType = "ALL";
					
					seletedMonth = monthComboBox.getValue();
					selectedYear = yearField.getValueAsString();
					String errorMsg = validateBeforeSearch(seletedMonth, selectedYear);
					if(errorMsg.length()>0){
						MessageBox.alert(errorMsg);
						return;
					}
				}

				com.google.gwt.user.client.Window.open(GlobalParameter.BUDGET_FORECAST_EXCEL_DOWNLOAD_URL +
																		"?jobNumber=" + jobNumber +
																		"&excelType=" + excelType +
																		"&month=" + seletedMonth +
																		"&year=" + selectedYear, 
																		"_blank", "");
				exportExcelWindow.close();
			}
		});

		exportExcelWindow.setButtons(new Button[] { exportButton });
		exportExcelWindow.show();
	}

	private void uploadResponseCallback(String responseText) {
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();

		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert("Imported successfully.");
		} else {
			String error = jsonObj.get("error").isString().toString();
			MessageBox.alert("Import Failed: " + error);
		}
		search(month, year);
	}

	private void securitySetup() {
		if (!accessRightsList.contains("WRITE")) {
			importButton.removeFromParent();
		} else if (!accessRightsList.contains("READ")) {
			excelToolbarButton.removeFromParent();
		}
	}

	private String validateBeforeSearch(String seletedMonth, String selectedYear){
		Date today = new Date();
		Date selectedDate;
		if(seletedMonth.length()>1)
			selectedDate = DateUtil.parseDate(seletedMonth+selectedYear, "MMyyyy");
		else
			selectedDate = DateUtil.parseDate(seletedMonth+selectedYear, "Myyyy");

		if("".equals(seletedMonth) || seletedMonth.length()==0)
			return "Please input month.";
		
		if("".equals(selectedYear) || selectedYear.length()==0)
			return "Please input year.";
		
		if(selectedDate.compareTo(today)>0)
			return "The latest period for searching is ."+"Month: "+String.valueOf(month)+ " - Year: "+ String.valueOf(year);
		
		return "";
	}
	
}
