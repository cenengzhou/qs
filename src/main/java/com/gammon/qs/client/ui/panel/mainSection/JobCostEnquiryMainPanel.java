/**
 * paulyiu
 * modified on 2015 07 30
 * Change from search panel to toolbar 
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;
import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.detailSection.AccountLedgerEnquiryDetailPanel;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.AccountBalanceByDateRangePaginationWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class JobCostEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "jobCostEnquiryMainPanel_ID";
	private GlobalSectionController globalSectionController;
	
	//TOOLBAR
	private Toolbar toolbar;
	private ToolbarTextItem objectCodeFilterLabel;
	private TextField objectCodeFilterTextField;
	private ToolbarTextItem subsidiaryCodeFilterLabel;
	private TextField subsidiaryCodeFilterTextField;
	private ToolbarTextItem filterLabel;
	private Checkbox filterZeroCheckbox;
	private ToolbarButton filterButton;	
	private ToolbarButton jobCostReportToolbarButton;
	private Menu reportMenu;
	private Item jobCostReportExcelButton;
	private Item jobCostReportPDFButton;
	
	//PAGINATION
	private PaginationToolbar paginationToolbar;
	private AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangePaginationWrapper;
	
	
	//////////
	//PANELS
	//////////
	private FieldSet generalInfoFieldSet;
	private FieldSet dateOptionFieldSet;
	private FieldSet dateFieldSet;
	private FieldSet periodFieldSet;
//	private FieldSet searchFieldSet;
	private GridPanel gridPanel;
	private CheckboxSelectionModel gridPanelCheckboxSelectionModel;
	
	private Integer gridPanelRowIndex;
	
	private Store dataStore;
	
	private RecordDef accountBalanceRecordDef = new RecordDef(
			new FieldDef[]{
					new ObjectFieldDef("Row Number"),
					new ObjectFieldDef("Check box"),
					//new StringFieldDef("accountID"),
							new StringFieldDef("objectCode"),
							new StringFieldDef("subsidiaryCode"),
							new StringFieldDef("accountCodeDescription"),
							new StringFieldDef("mnAmount"),
							new StringFieldDef("mnAmount2"),
							new StringFieldDef("variance")
							});
	
	private Label subLedgerLabel;
	private TextField subLedgerTextField;
	private Label postLabel;
	private ComboBox postStatusComboBox;
	private Store storePostStatus = new SimpleStore(new String[]{"postValue","postDisplay"}, getPostStatus());
	
	//By Date
	private Date today = new Date();
	private Radio byDateRadio;
	private Label fromDateLabel;
	private DateField fromDateTextField;
	private Label thruDateLabel;
	private DateField thruDateTextField;
	
	//By Period
	private Radio byPeriodRadio;
	private Label yearLabel;
	private Store storeYear = new SimpleStore(new String[]{"year"}, getYear());
	private ComboBox yearComboBox;
	private Label periodLabel;
	private Store storePeriod = new SimpleStore(new String[]{"periodInNumberFormat", "periodInTextFormat"}, getPeriod());
	private ComboBox periodComboBox;
	private Store storeCumMov = new SimpleStore(new String[]{"cumMovValue","cumMovDisplay"}, getCumMov());
	private ComboBox cumMovComboBox;
	protected GridView globalMessageTicket;
	private ToolbarTextItem totalJIToolbarTextItem;
	private ToolbarTextItem totalAAToolbarTextItem;
	private ToolbarTextItem totalVarianceTextItem;
	private ToolbarTextItem totalNumberRecord;

	//storeYear
	private static String[][] getYear() {  
		return new String[][]{  
				new String[]{"2000"},

				new String[]{"2001"},
				new String[]{"2002"},
				new String[]{"2003"},
				new String[]{"2004"},
				new String[]{"2005"},
				new String[]{"2006"},
				new String[]{"2007"},
				new String[]{"2008"},
				new String[]{"2009"},

				new String[]{"2010"},  
				new String[]{"2011"},
				new String[]{"2012"},
				new String[]{"2013"},
				new String[]{"2014"},
				new String[]{"2015"},
				new String[]{"2016"},
				new String[]{"2017"},
				new String[]{"2018"},
				new String[]{"2019"},

				new String[]{"2020"},
				new String[]{"2021"},
				new String[]{"2022"},
				new String[]{"2023"},
				new String[]{"2024"},
				new String[]{"2025"},
				new String[]{"2026"},
				new String[]{"2027"},
				new String[]{"2028"},
				new String[]{"2029"},

				new String[]{"2030"},  
				new String[]{"2031"},
				new String[]{"2032"},
				new String[]{"2033"},
				new String[]{"2034"},
				new String[]{"2035"},
				new String[]{"2036"},
				new String[]{"2037"},
				new String[]{"2038"},
				new String[]{"2039"},

				new String[]{"2040"},  
				new String[]{"2041"},
				new String[]{"2042"},
				new String[]{"2043"},
				new String[]{"2044"},
				new String[]{"2045"},
				new String[]{"2046"},
				new String[]{"2047"},
				new String[]{"2048"},
				new String[]{"2049"},

				new String[]{"2050"}
		};  
	} 
	
	//store Period
	private static String[][] getPeriod() {  
		return new String[][]{  
				new String[]{"1", "January"},  
				new String[]{"2", "February"},
				new String[]{"3", "March"},
				new String[]{"4", "April"},
				new String[]{"5", "May"}, 
				new String[]{"6", "June"}, 
				new String[]{"7", "July"}, 
				new String[]{"8", "August"},  
				new String[]{"9", "September"},
				new String[]{"10", "October"},  
				new String[]{"11", "November"},
				new String[]{"12", "December"}
		};  
	}
	
	//storePostStatus
	private static String[][] getPostStatus() {  
		return new String[][]{  
				new String[]{"posted", "Posted"},  
				new String[]{"unposted", "Unposted"},
				new String[]{"all", "All"}
		};  
	} 
	
	//storecumMov
	private static String[][] getCumMov() {  
		return new String[][]{  
				new String[]{"cumulative", "Cumulative"},  
				new String[]{"movement", "Movement"}
		};  
	}


	public JobCostEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		accountBalanceByDateRangePaginationWrapper = new AccountBalanceByDateRangePaginationWrapper();
		
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
//		setTitle("Job Cost Enquiry Job:"+globalSectionController.getJob().getJobNumber());
		
		setupSearchPanel();
		setupGridPanel();
		
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	private void setupSearchPanel() {
		//////////////////
		//SEARCH PANEL
		//////////////////
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(110);
		searchPanel.setLayout(new TableLayout(6));
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};
		
		int fieldHeight = 80;
		
		//////////////////////
		//TEXTINPUT PANEL
		//////////////////////
		generalInfoFieldSet = new FieldSet("General Info");
		generalInfoFieldSet.setHeight(fieldHeight);
		generalInfoFieldSet.setPaddings(0);
		generalInfoFieldSet.setFrame(true);
		generalInfoFieldSet.setLayout(new TableLayout(2));
		
		
		//SubLedger
		subLedgerLabel = new Label("Subcontract Number:");
		subLedgerLabel.setAutoWidth(true);
		subLedgerLabel.setCls("table-cell");
		subLedgerTextField = new TextField("SubLedger", "subLedgerSearch", 80);
		subLedgerTextField.setEmptyText("e.g. 1001  ");
		subLedgerTextField.setValidator(new Validator(){

			public boolean validate(String value) throws ValidationException {
				if(!value.matches("^[0-9\\*]*$")){
					MessageBox.alert("Please input number only");
					//subLedgerTextField.focus();
					//subLedgerTextField.selectText();
					return false;
				}
				return true;
			}
			
		});
		ToolTip subLedgerTextFieldToolTip = new ToolTip();
		subLedgerTextFieldToolTip.setTitle("Subcontract Number:");
		subLedgerTextFieldToolTip.setHtml("Enter Subcontract Number to search<br>Filter: Match Exactly<br>Number only");
		subLedgerTextFieldToolTip.applyTo(subLedgerTextField);
		subLedgerTextField.setCtCls("table-cell");
		subLedgerTextField.addListener(searchListener);
		generalInfoFieldSet.add(subLedgerLabel);
		generalInfoFieldSet.add(subLedgerTextField);
		
		Label emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(15);		
		
		//Post Code	
		postLabel = new Label("Post Code:");
		postLabel.setAutoWidth(true);
		postLabel.setCls("table-cell");
		  
		storePostStatus.load();
		postStatusComboBox = new ComboBox();
		postStatusComboBox.setCtCls("table-cell");
		postStatusComboBox.setForceSelection(true);  
		postStatusComboBox.setMinChars(1);  
		postStatusComboBox.setFieldLabel("Post Status");  
		postStatusComboBox.setStore(storePostStatus);  
		postStatusComboBox.setDisplayField("postDisplay");  
		postStatusComboBox.setValueField("postValue");
		postStatusComboBox.setMode(ComboBox.LOCAL);  
		postStatusComboBox.setTriggerAction(ComboBox.ALL);  
		postStatusComboBox.setEmptyText("Post Status");  
		postStatusComboBox.setLoadingText("Searching...");  
		postStatusComboBox.setTypeAhead(true);   
		postStatusComboBox.setWidth(80);  
		postStatusComboBox.setValue("all");
		ToolTip postStatusComboBoxToolTip = new ToolTip();
		postStatusComboBoxToolTip.setTitle("Post Code:");
		postStatusComboBoxToolTip.setHtml("Enter post code or choose one from the list<br>Filter: Match Exactly<br>Number only");
		postStatusComboBoxToolTip.applyTo(postStatusComboBox);
		
		generalInfoFieldSet.add(postLabel);
		generalInfoFieldSet.add(postStatusComboBox);
		
		//Export to Excel
		Label emptySpace2 = new Label();
		emptySpace2.setHtml("&nbsp;");
		emptySpace2.setWidth(15);
		
		searchPanel.add(generalInfoFieldSet);
		searchPanel.add(emptySpace);
		
		/////////////////////
		//DATEOPTION PANEL
		/////////////////////
		dateOptionFieldSet = new FieldSet("Date");
		dateOptionFieldSet.setHeight(fieldHeight);
		dateOptionFieldSet.setFrame(true);
		dateOptionFieldSet.setLayout(new TableLayout());

		
		////////////////
		//DATE PANEL
		////////////////		
		byDateRadio = new Radio();
		ToolTip byDateRadioToolTip = new ToolTip();
		byDateRadioToolTip.setTitle("By Date Range:");
		byDateRadioToolTip.setHtml("Search by date range");
		byDateRadioToolTip.applyTo(byDateRadio);
		dateOptionFieldSet.add(byDateRadio);
		
		dateFieldSet = new FieldSet("By Date Range");
		dateFieldSet.setHeight(40);
		dateFieldSet.setPaddings(0);
		dateFieldSet.setFrame(true);
		dateFieldSet.setLayout(new TableLayout());

		//From Date
		fromDateLabel = new Label("From Date:");
		fromDateLabel.setAutoWidth(true);
		fromDateLabel.setCls("table-cell");
		fromDateTextField = new DateField("From Date", "fromDateSearch", 120);
		fromDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		fromDateTextField.setFormat("d/m/y");
		fromDateTextField.setEmptyText("e.g. 01/01/2014  ");
		ToolTip fromDateTextFieldToolTip = new ToolTip();
		fromDateTextFieldToolTip.setTitle("From Date:");
		fromDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		fromDateTextFieldToolTip.applyTo(fromDateTextField);
		fromDateTextField.addListener(searchListener);
		dateFieldSet.add(fromDateLabel);
		dateFieldSet.add(fromDateTextField);
		
		//Thru Date
		thruDateLabel = new Label("Thru Date:");
		thruDateLabel.setAutoWidth(true);
		thruDateLabel.setCls("table-cell");
		thruDateTextField = new DateField("Thru Date", "thruDateSearch", 120);
		thruDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		thruDateTextField.setFormat("d/m/y");
		thruDateTextField.setEmptyText("e.g. 31/01/2014  ");
		ToolTip thruDateTextFieldToolTip = new ToolTip();
		thruDateTextFieldToolTip.setTitle("Thru Date:");
		thruDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		thruDateTextFieldToolTip.applyTo(thruDateTextField);
		thruDateTextField.addListener(searchListener);
		dateFieldSet.add(thruDateLabel);
		dateFieldSet.add(thruDateTextField);
		
		dateOptionFieldSet.add(dateFieldSet);
		dateOptionFieldSet.add(emptySpace);
		
		//////////////////////
		//PERIOD PANEL
		//////////////////////
		byPeriodRadio = new Radio();
		byPeriodRadio.setChecked(true);
		ToolTip byPeriodRadioToolTip = new ToolTip();
		byPeriodRadioToolTip.setTitle("By Period:");
		byPeriodRadioToolTip.setHtml("Search by period");
		byPeriodRadioToolTip.applyTo(byPeriodRadio);
		dateOptionFieldSet.add(byPeriodRadio);
		
		periodFieldSet = new FieldSet("By Period");
		periodFieldSet.setHeight(50);
		periodFieldSet.setPaddings(0);
		periodFieldSet.setFrame(true);
		periodFieldSet.setLayout(new TableLayout());
		
		//Year  
		yearLabel = new Label("Year ");
		yearLabel.setAutoWidth(true);
		yearLabel.setCls("table-cell");
		
		storeYear.load();
		
		yearComboBox = new ComboBox();   
		yearComboBox.setForceSelection(true);  
		yearComboBox.setMinChars(1);  
		yearComboBox.setFieldLabel("Year ");  
		yearComboBox.setStore(storeYear);  
		yearComboBox.setDisplayField("year");  
		yearComboBox.setValueField("year");
		yearComboBox.setMode(ComboBox.LOCAL);  
		yearComboBox.setTriggerAction(ComboBox.ALL);  
		yearComboBox.setEmptyText("Year");  
		yearComboBox.setLoadingText("Searching...");  
		yearComboBox.setTypeAhead(true);   
		yearComboBox.setWidth(70);
		yearComboBox.setValue(Integer.toString(today.getYear()+1900));
		ToolTip yearComboBoxToolTip = new ToolTip();
		yearComboBoxToolTip.setTitle("Year:");
		yearComboBoxToolTip.setHtml("Choose the year from the list ");
		yearComboBoxToolTip.applyTo(yearComboBox);
		yearComboBox.addListener(searchListener);
		periodFieldSet.add(yearComboBox);
		
		//Period  
		periodLabel = new Label("Period ");
		periodLabel.setAutoWidth(true);
		periodLabel.setCls("table-cell");
		
		storePeriod.load();
		
		periodComboBox = new ComboBox();   
		periodComboBox.setForceSelection(true);  
		periodComboBox.setMinChars(1);  
		periodComboBox.setFieldLabel("Period ");  
		periodComboBox.setStore(storePeriod);  
		periodComboBox.setDisplayField("periodInTextFormat");  
		periodComboBox.setValueField("periodInNumberFormat");
		periodComboBox.setMode(ComboBox.LOCAL);  
		periodComboBox.setTriggerAction(ComboBox.ALL);  
		periodComboBox.setEmptyText("Period");  
		periodComboBox.setLoadingText("Searching...");  
		periodComboBox.setTypeAhead(true);   
		periodComboBox.setWidth(80); 
		periodComboBox.setValue(Integer.toString(today.getMonth()+1));
		ToolTip periodComboBoxToolTip = new ToolTip();
		periodComboBoxToolTip.setTitle("Month:");
		periodComboBoxToolTip.setHtml("Choose the month from the list ");
		periodComboBoxToolTip.applyTo(periodComboBox);
		periodComboBox.addListener(searchListener);
		periodFieldSet.add(periodComboBox);
		
		//Cumulative/Movement			  
		storeCumMov.load();
		cumMovComboBox = new ComboBox();   
		cumMovComboBox.setForceSelection(true);  
		cumMovComboBox.setMinChars(1);  
		cumMovComboBox.setFieldLabel("Cumulative");  
		cumMovComboBox.setStore(storeCumMov);  
		cumMovComboBox.setDisplayField("cumMovDisplay");  
		cumMovComboBox.setValueField("cumMovValue");
		cumMovComboBox.setMode(ComboBox.LOCAL);  
		cumMovComboBox.setTriggerAction(ComboBox.ALL);  
		cumMovComboBox.setEmptyText("Cumulative/Movement");  
		cumMovComboBox.setLoadingText("Searching...");  
		cumMovComboBox.setTypeAhead(true);   
		cumMovComboBox.setWidth(100);  
		cumMovComboBox.setValue("cumulative");
		ToolTip cumMovComboBoxToolTip = new ToolTip();
		cumMovComboBoxToolTip.setTitle("");
		cumMovComboBoxToolTip.setHtml("Choose cumulative or movement from the list ");
		cumMovComboBoxToolTip.applyTo(cumMovComboBox);
		cumMovComboBox.addListener(searchListener);
		periodFieldSet.add(cumMovComboBox);
		
		byDateRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					byPeriodRadio.setChecked(false);
					
					yearComboBox.setDisabled(true);
					periodComboBox.setDisabled(true);
					cumMovComboBox.setDisabled(true);
					
					fromDateTextField.setDisabled(false);
					thruDateTextField.setDisabled(false);
				}
				else{
					yearComboBox.setDisabled(false);
					periodComboBox.setDisabled(false);
					cumMovComboBox.setDisabled(false);
					
					if(byPeriodRadio.getValue()){
						fromDateTextField.setDisabled(true);
						thruDateTextField.setDisabled(true);
					}
					else{
						fromDateTextField.setDisabled(false);
						thruDateTextField.setDisabled(false);
					}
				}
			}
		});
		
		byPeriodRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					byDateRadio.setChecked(false);
					
					fromDateTextField.setDisabled(true);
					thruDateTextField.setDisabled(true);
					
					yearComboBox.setDisabled(false);
					periodComboBox.setDisabled(false);
					cumMovComboBox.setDisabled(false);
				}
				else{
					fromDateTextField.setDisabled(false);
					thruDateTextField.setDisabled(false);
					
					if(byDateRadio.getValue()){
						yearComboBox.setDisabled(true);
						periodComboBox.setDisabled(true);
						cumMovComboBox.setDisabled(true);
					}
					else{	
						yearComboBox.setDisabled(false);
						periodComboBox.setDisabled(false);
						cumMovComboBox.setDisabled(false);
					}
				}
			}
		});
		
		dateOptionFieldSet.add(periodFieldSet);
		
		searchPanel.add(dateOptionFieldSet);
		searchPanel.add(emptySpace2);
		///////////////
		//SEARCH BUTTON
		///////////////

		Button searchButton = new Button("Search");
			searchButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e) {
					search();
				};
			});
//		searchFieldSet.add(searchButton);
//
//		searchFieldSet.setAutoHeight(true);
			ToolTip searchButtonToolTip = new ToolTip();
			searchButtonToolTip.setTitle("Search:");
			searchButtonToolTip.setHtml("Press to search");
			searchButtonToolTip.applyTo(searchButton);
		searchPanel.add(searchButton);
	
		add(searchPanel);
	}

	private void setupToolbar(){
		//////////////////
		//TOOLBAR
		//////////////////
		toolbar = new Toolbar();
		
		//Filter Listener
		FieldListener filterListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					filter();
				}
			}
		};
		
		//Object Code Field
		objectCodeFilterLabel = new ToolbarTextItem("Object Code:");
		objectCodeFilterTextField = new TextField("Object Code Filter", "objectCodeFilterSearch", 80);
		objectCodeFilterTextField.addListener(filterListener);
		objectCodeFilterTextField.setEmptyText("e.g. 1402*  ");
		ToolTip objectCodeFilterTextFieldToolTip = new ToolTip();
		objectCodeFilterTextFieldToolTip.setTitle("Object Code:");
		objectCodeFilterTextFieldToolTip.setHtml("Enter object code to filter<br>Filter: Start with<br>eg:\"132399\"");
		objectCodeFilterTextFieldToolTip.applyTo(objectCodeFilterTextField);
		toolbar.addItem(objectCodeFilterLabel);
		toolbar.addField(objectCodeFilterTextField);
		toolbar.addSpacer();
		
		//Subsidiary Code Field
		subsidiaryCodeFilterLabel = new ToolbarTextItem("Subsidiary Code:");
		subsidiaryCodeFilterTextField = new TextField("Subsidiary Code Filter", "subsidiaryCodeFilterSearch", 80);
		subsidiaryCodeFilterTextField.addListener(filterListener);
		subsidiaryCodeFilterTextField.setEmptyText("e.g. 1999*  ");
		ToolTip subsidiaryCodeFilterTextFieldToolTip = new ToolTip();
		subsidiaryCodeFilterTextFieldToolTip.setTitle("Subsidiary Code:");
		subsidiaryCodeFilterTextFieldToolTip.setHtml("Enter subsidiary code to filter<br>Filter: Start with<br>eg:\"19999999\"");
		subsidiaryCodeFilterTextFieldToolTip.applyTo(subsidiaryCodeFilterTextField);
		toolbar.addItem(subsidiaryCodeFilterLabel);
		toolbar.addField(subsidiaryCodeFilterTextField);
		toolbar.addSpacer();
		
		//Filter's Combobox
		filterZeroCheckbox = new Checkbox();
		filterZeroCheckbox.setChecked(true);
		ToolTip filterZeroCheckboxToolTip = new ToolTip();
		filterZeroCheckboxToolTip.setTitle("Do not display zero items:");
		filterZeroCheckboxToolTip.setHtml("Toggle show or hide zero item");
		filterZeroCheckboxToolTip.applyTo(filterZeroCheckbox);
		toolbar.addField(filterZeroCheckbox);
		filterLabel = new ToolbarTextItem("Do not Display Zero items");
		toolbar.addItem(filterLabel);
		toolbar.addSpacer();
		toolbar.addSeparator();

		//Filter's Button
		filterButton = new ToolbarButton("Filter");
		//filterButton.setText("Filter");
		filterButton.setIconCls("filter-icon");
		filterButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				filter();
			};
		});

		toolbar.addButton(filterButton);
		toolbar.addSeparator();
		
		//Toolbar - Filter Button's Tips
		ToolTip filterButtonToolTip = new ToolTip();
		filterButtonToolTip.setTitle("Filter");
		filterButtonToolTip.setHtml("Filter Job Cost Result");
		filterButtonToolTip.applyTo(filterButton);
		
		reportMenu = new Menu();
		
		jobCostReportExcelButton = new Item("To Excel");
		jobCostReportExcelButton.setIconCls("excel-icon");
		jobCostReportExcelButton.addListener(downloadListener(GlobalParameter.ACCOUNT_BALANCE_REPORT_EXPORT_URL,"xls"));
		
		jobCostReportPDFButton = new Item("To PDF");
		jobCostReportPDFButton.setIconCls("pdf-icon");
		jobCostReportPDFButton.addListener(downloadListener(GlobalParameter.ACCOUNT_BALANCE_REPORT_EXPORT_URL,"pdf"));
		
		reportMenu.addItem(jobCostReportExcelButton);
		reportMenu.addItem(jobCostReportPDFButton);
		
		jobCostReportToolbarButton = new ToolbarButton("Export");
		jobCostReportToolbarButton.setMenu(reportMenu);
		jobCostReportToolbarButton.setIconCls("download-icon");
		ToolTip jobCostReportToolbarButtonToolTip = new ToolTip();
		jobCostReportToolbarButtonToolTip.setTitle("Job Cost Report");
		jobCostReportToolbarButtonToolTip.setHtml("Press to download report");
		jobCostReportToolbarButtonToolTip.applyTo(jobCostReportToolbarButton);
		toolbar.addButton(jobCostReportToolbarButton);
		toolbar.addSpacer();

		gridPanel.setTopToolbar(toolbar);
	}
	
	public BaseItemListenerAdapter downloadListener(final String url, final String fileType){
		return new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e){
				downloadReport(url,fileType);
			}
		};
	}

	private void setupGridPanel() {
		///////////////
		//GRID PANEL
		///////////////
		gridPanel = new GridPanel();
		
		setupToolbar();
		
		//Checkbox Selection for Gridpanel
		gridPanelCheckboxSelectionModel = new CheckboxSelectionModel();
		gridPanelCheckboxSelectionModel.addListener(new RowSelectionListenerAdapter() {
            public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
            	if(gridPanelRowIndex != null)
            		gridPanelCheckboxSelectionModel.deselectRow(gridPanelRowIndex);
            	gridPanelRowIndex = new Integer(rowIndex);
            	super.onRowSelect(sm, rowIndex, record);
            }
			}
		);
		
		Renderer numberDP2 = new Renderer(){
			public String render(	Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String htmlAttr = "style='font-weight:bold;";
				if (Double.parseDouble(value.toString()) < 0) {
					htmlAttr += "color:red;";
				}
				cellMetadata.setHtmlAttribute(htmlAttr+"'");
				NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
				value = fmt.format(Double.parseDouble(value.toString()));
				
				if(record.getAsString("accountCodeDescription").equals("TOTAL:")|| record.getAsString("accountCodeDescription").equals("SUB TOTAL:"))
					return "<b>" + value + "</b>";
					
				return (value != null ? value.toString() : null);
			}
			
		};
		
		//Web Service - AccountIDListByJob
		//Account ID
		//ColumnConfig accountIDColConfig = new ColumnConfig("Account ID", "accountID", 150, false);
		//Object Code
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 100, false);
		objectCodeColConfig.setSortable(true);
		//Subsidiary Code
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 120, false);
		subsidiaryCodeColConfig.setSortable(true);
		//Account Code Description
		ColumnConfig accountCodeDescriptionColConfig = new ColumnConfig("Account Code Description", "accountCodeDescription", 300, false);
		accountCodeDescriptionColConfig.setSortable(true);
		
		//Web Service - AccountBalanceByDateRange
		ColumnConfig mnAmountColConfig = new ColumnConfig("Internal Value (JI)", "mnAmount", 150, false);
		mnAmountColConfig.setRenderer(numberDP2);
		mnAmountColConfig.setSortable(true);
		mnAmountColConfig.setAlign(TextAlign.RIGHT);
		
		
		
		ColumnConfig mnAmount2ColConfig = new ColumnConfig("Actual Value (AA)", "mnAmount2", 150, false);
		mnAmount2ColConfig.setRenderer(numberDP2);
		mnAmount2ColConfig.setSortable(true);
		mnAmount2ColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig varianceColConfig = new ColumnConfig("Variance (JI-AA)", "variance", 150, false);
		varianceColConfig.setRenderer(numberDP2);
		varianceColConfig.setSortable(true);
		varianceColConfig.setAlign(TextAlign.RIGHT);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				new CheckboxColumnConfig(gridPanelCheckboxSelectionModel),
				//accountIDColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				accountCodeDescriptionColConfig,
				mnAmountColConfig,
				mnAmount2ColConfig,
				varianceColConfig
		};

		//Put the columns in the grid
		gridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(true);
		gridPanel.setView(view);
		
		//Setup data store(JDE --> data store)
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(accountBalanceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		
		
		//////////////
		//PAGINATION
		//////////////
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				getAccountBalanceByDateRangePaginationWrapperByPage(pageNum);
			}	
		});


		if(accountBalanceByDateRangePaginationWrapper.getCurrentPageContentList()==null || accountBalanceByDateRangePaginationWrapper.getCurrentPageContentList().size()==0)
			paginationToolbar.setTotalPage(1);
		else
			paginationToolbar.setTotalPage(accountBalanceByDateRangePaginationWrapper.getTotalPage());
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.addFill();
		totalJIToolbarTextItem = new ToolbarTextItem("<b>Total Internal Value (JI):</b>");
		totalAAToolbarTextItem = new ToolbarTextItem("<b>Total Actual Value (AA):</b>");
		totalVarianceTextItem = new ToolbarTextItem("<b>Total Variaction (JI-AA):</b>");
		totalNumberRecord = new ToolbarTextItem("<b>Total Number of Records:0</b>");
		paginationToolbar.addItem(totalJIToolbarTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalAAToolbarTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalVarianceTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalNumberRecord);
		
		gridPanel.setBottomToolbar(paginationToolbar);	

		//Checkbox selection function in the grid
		gridPanel.setSelectionModel(gridPanelCheckboxSelectionModel);

		//Pop-up AccountLedgerEnquiryWindow for Details
		gridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e){
					gridPanelRowIndex = new Integer(rowIndex);
					goToAccountLedger();
			}
		});
		
		
		add(gridPanel);
	} 
	
	
	//add by paulyiu 20150728
	public void downloadReport(String url,String fileType){
		
		String jobNumber = globalSectionController.getJob().getJobNumber();
		String subLedger = subLedgerTextField.getText();	//package number (1101)
		String subLedgerType = "";
		if(subLedger!=null && !subLedger.equals(""))
			subLedgerType = "X";
		
		String totalFlag = "I";
		if(byDateRadio.getValue()) //FromDate + ThruDate
			totalFlag = "I";
		if(byPeriodRadio.getValue()){	
			if(cumMovComboBox.getValueAsString().trim().equals("cumulative"))//Year + Period + Cumulative
				totalFlag = "I";
			if(cumMovComboBox.getValueAsString().trim().equals("movement"))	//Year + Period + Movement
				totalFlag = "P";
		}

		String postFlag ="*";
		if(postStatusComboBox.getValueAsString().trim().equals("posted"))
			postFlag = "P";
		if(postStatusComboBox.getValueAsString().trim().equals("unposted"))
			postFlag = "U";
		if(postStatusComboBox.getValueAsString().trim().equals("all"))
			postFlag = "*";	
		
		String fromDate = "";
		String thruDate = "";
		String year = "";
		String period = "";
		if(byDateRadio.getValue()){
			fromDate = date2String(fromDateTextField.getValue());
			thruDate = date2String(thruDateTextField.getValue());
		}
		else if (byPeriodRadio.getValue()){
			year = yearComboBox.getText().substring(yearComboBox.getText().trim().length()-2, yearComboBox.getText().trim().length());
			period = periodComboBox.getText();
		}
		
		com.google.gwt.user.client.Window.open(
				url + 
				"?jobNumber="+jobNumber+
				"&subLedger="+subLedger+
				"&subLedgerType="+subLedgerType+
				"&totalFlag="+totalFlag+
				"&postFlag="+postFlag+
				"&fromDate="+fromDate+
				"&thruDate="+thruDate+
				"&year="+year+
				"&period="+period+
				"&fileType="+fileType, "_blank", "");

	}

	private void search(){
		//validate & search
		String subLedger = subLedgerTextField.getText();	//package number (1101)
		String subLedgerType = null;
		if(subLedger!=null && !subLedger.equals(""))
			subLedgerType = "X";
		
		String totalFlag = "I";
		if(byDateRadio.getValue()) //FromDate + ThruDate
			totalFlag = "I";
		if(byPeriodRadio.getValue()){	
			if(cumMovComboBox.getValueAsString().trim().equals("cumulative"))//Year + Period + Cumulative
				totalFlag = "I";
			if(cumMovComboBox.getValueAsString().trim().equals("movement"))	//Year + Period + Movement
				totalFlag = "P";
		}
		
		String postFlag ="*";
		if(postStatusComboBox.getValueAsString().trim().equals("posted"))
			postFlag = "P";
		if(postStatusComboBox.getValueAsString().trim().equals("unposted"))
			postFlag = "U";
		if(postStatusComboBox.getValueAsString().trim().equals("all"))
			postFlag = "*";	
		
		Date fromDate = null;
		Date thruDate = null;
		String year = null;
		String period = null;
		if(byDateRadio.getValue()){
			fromDate = fromDateTextField.getValue();
			thruDate = thruDateTextField.getValue();
		}
		else if (byPeriodRadio.getValue()){
			year = yearComboBox.getText().substring(yearComboBox.getText().trim().length()-2, yearComboBox.getText().trim().length());
			period = periodComboBox.getText();
		}
				
		//validation
		if(byDateRadio.getValue() && (fromDate==null || thruDate==null))
			MessageBox.alert("Please enter [From Date] and [Thru Date]");
		else
			searchAccountBalanceByDateRange(subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
	}	
	
	public void filter(){
		gridPanelRowIndex = null;
		getAccountBalanceByDateRangePaginationWrapperByFilter(objectCodeFilterTextField.getValueAsString(), subsidiaryCodeFilterTextField.getValueAsString(), filterZeroCheckbox.getValue());
	}
	
	@SuppressWarnings("deprecation")
	public void goToAccountLedger(){		
		Record currentRecord = gridPanelCheckboxSelectionModel.getSelected();
		
		//SUB TOTAL/TOTAL row will not invoke "AccountLedgerEnquiryWindow"
		if(currentRecord.getAsString("accountCodeDescription").equals("SUB TOTAL:") || currentRecord.getAsString("accountCodeDescription").equals("TOTAL:"))
			return;			
		else{
			String accountCode =	globalSectionController.getJob().getJobNumber()+
									((currentRecord.getAsObject("objectCode")!=null && !currentRecord.getAsString("objectCode").trim().equals("0"))?"."+currentRecord.getAsString("objectCode"):"")+
									((currentRecord.getAsObject("subsidiaryCode")!=null && !currentRecord.getAsString("subsidiaryCode").trim().equals("0"))?"."+currentRecord.getAsString("subsidiaryCode"):"");
			//Pack date for AccountLedgerEnquiryWindow
			String ledgerType = "AA";
			String subLedger = subLedgerTextField.getText();
			String subLedgerType = null;
			if(subLedger!=null && !subLedger.equals(""))
				subLedgerType = "X";
			String postFlag =null;
			if(postStatusComboBox.getValueAsString().trim().equals("posted"))
				postFlag = "P";
			if(postStatusComboBox.getValueAsString().trim().equals("unposted"))
				postFlag = " ";
			if(postStatusComboBox.getValueAsString().trim().equals("all"))
				postFlag = null;	
			
			Date fromDate = new Date();
			Date thruDate = new Date();	
			//March - 26/02 - 25/03
			//December - 26/11 - 29/12, January - 01/01 - 25/01 (Special Cases)
			if(byDateRadio.getValue()){
				fromDate = fromDateTextField.getValue();
				thruDate = thruDateTextField.getValue();
			}
			//Fixing: Wrong date for January
			else if (byPeriodRadio.getValue()){
				//from year - yearComboBox=110-->1900+110=2010
				if(cumMovComboBox.getValueAsString().trim().equals("movement"))
					fromDate.setYear(Integer.parseInt(yearComboBox.getText())-1900);	//movement --> current year
				else
					fromDate.setYear(Integer.parseInt(yearComboBox.getText())-1900-2); 	//cumulative --> current year-2
				//from month - periodComboBox=3-->March, setMonth=2
				if(Integer.parseInt(periodComboBox.getText())==1)
					fromDate.setMonth(0);	//January --> January
				else
					fromDate.setMonth(Integer.parseInt(periodComboBox.getText())-2);	//Other months --> current month-1
				//from date
				if(Integer.parseInt(periodComboBox.getText())==1)
					fromDate.setDate(1);	//January --> 1st
				else
					fromDate.setDate(26);	//Other months --> 26th

				//thru year
				thruDate.setYear(Integer.parseInt(yearComboBox.getText())-1900);
				//thru month
				thruDate.setMonth(Integer.parseInt(periodComboBox.getText())-1);
				//thru date
				if(Integer.parseInt(periodComboBox.getText())==12)
					thruDate.setDate(29);	//December --> 29th
				else
					thruDate.setDate(25);	//Other months --> 25th

			}
			showAccountLedgerEnquiryDetailPanel(accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
		}
	}
	
	//Populate data
	public void populateGridByList(List<AccountBalanceByDateRangeWrapper> accountBalanceList){
		dataStore.removeAll();
		Double totalJI = 0.00;
		Double totalAA = 0.00;
		Double totalVariance = 0.00;
		for(AccountBalanceByDateRangeWrapper accountBalance : accountBalanceList){
			double variance = accountBalance.getAmountJI()-accountBalance.getAmountAA();
			
			Record record = accountBalanceRecordDef.createRecord(new Object[]{
					null,
					null,
					//accountBalance.getAccountID(),
					accountBalance.getObjectCode(),
					accountBalance.getSubsidiaryCode(),
					accountBalance.getAccountCodeDescription(),
					accountBalance.getAmountJI(),
					accountBalance.getAmountAA(),
					variance
			});
			totalJI+=accountBalance.getAmountJI();
			totalAA+=accountBalance.getAmountAA();
			totalVariance+=variance;
			
			dataStore.add(record);
		}
		if(accountBalanceList!=null && accountBalanceList.size()>0){

		}
		else{
			this.paginationToolbar.setTotalPage(1);		//Display total page = 1
			this.paginationToolbar.setCurrentPage(0);	//Display current page = 1
			MessageBox.alert("No data was found.");
		}
	}

	public void populateGridByPage(AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangePaginationWrapper) {
		this.accountBalanceByDateRangePaginationWrapper = accountBalanceByDateRangePaginationWrapper;
		this.paginationToolbar.setTotalPage(accountBalanceByDateRangePaginationWrapper.getTotalPage());
		this.paginationToolbar.setCurrentPage(accountBalanceByDateRangePaginationWrapper.getCurrentPage());
		
		//populate the current page
		populateGridByList(accountBalanceByDateRangePaginationWrapper.getCurrentPageContentList());
		NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
		totalJIToolbarTextItem.setText("<b>Total Internal Value (JI):" + fmt.format(accountBalanceByDateRangePaginationWrapper.getTotalJI())+"</b>");
		totalAAToolbarTextItem.setText("<b>Total Actual Value (AA):" + fmt.format(accountBalanceByDateRangePaginationWrapper.getTotalAA())+"</b>");
		totalVarianceTextItem.setText("<b>Total Variaction (JI-AA):" + fmt.format(accountBalanceByDateRangePaginationWrapper.getTotalVariance())+"</b>");
		totalNumberRecord.setText("<b>Total Number of Records:" + accountBalanceByDateRangePaginationWrapper.getTotalRecords()+"</b>");
		
		//hide detail panel
		if(!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}

	//Go to application server to get data page by page(not using web service at all)
	private void getAccountBalanceByDateRangePaginationWrapperByPage(Integer pageNum) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAccountBalanceByDateRangePaginationWrapperByPage(pageNum, new AsyncCallback<AccountBalanceByDateRangePaginationWrapper>(){
			public void onSuccess(AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangePaginationWrapper) {
				populateGridByPage(accountBalanceByDateRangePaginationWrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
		
	}
	
	
	//Search Account Balance By Date Range (First search only, then save the whole list in application server)
	private void searchAccountBalanceByDateRange(String subLedger, String subLedgerType, String totalFlag,
												String postFlag, Date fromDate, Date thruDate, String year, String period) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		//Data from Account Master: accountID, ledgerType(JI), ledgerType2(AA), company
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAccountBalanceByDateRangeList(globalSectionController.getJob().getJobNumber(), subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period, new AsyncCallback<AccountBalanceByDateRangePaginationWrapper>(){
			public void onSuccess(AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangeWrapper) {
				populateGridByPage(accountBalanceByDateRangeWrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				
				//Filter the result if searched successfully
				filter();
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	
	//Go to application server to get the cached list and do the filter(no web service at all)
	private void getAccountBalanceByDateRangePaginationWrapperByFilter(String objectCode, String subsidiaryCode, boolean filterZero){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Filtering...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAccountBalanceByDateRangePaginationWrapperByFilter(objectCode, subsidiaryCode, filterZero, new AsyncCallback<AccountBalanceByDateRangePaginationWrapper>(){
			public void onSuccess(AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangePaginationWrapper) {
				populateGridByPage(accountBalanceByDateRangePaginationWrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
		
	}
	

	private void showAccountLedgerEnquiryDetailPanel(String accountCode, String ledgerType, String subLedger, String subLedgerType, Date fromDate, Date thruDate, String postFlag) {
		globalSectionController.getDetailSectionController().resetPanel();
		AccountLedgerEnquiryDetailPanel accountLedgerEnquiryDetailPanel = new AccountLedgerEnquiryDetailPanel(globalSectionController, accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
		globalSectionController.getDetailSectionController().getMainPanel().setTitle("Detail - Account Code: "+accountCode);
		globalSectionController.getDetailSectionController().setGridPanel(accountLedgerEnquiryDetailPanel);
		globalSectionController.getDetailSectionController().populateGridPanel();
	}
	
	
}