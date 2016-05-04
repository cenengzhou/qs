package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * modified by matthewlam, 2015-01-28
 * Bug fix #77 - Unable to inactivate System Constant records
 */
public class SystemConstantEnquiryPanel extends Panel {

	public static final int					WINDOW_HEIGHT						= 700;
	public static final int					WINDOW_WIDTH						= 1200;
	public static final String				WINDOW_TITLE						= "System Constant Enquiry";

	public static final int					SEARCH_PANEL_HEIGHT					= 100;
	public static final int					SEARCH_PANEL_TABLE_SIZE				= 8;

	public static final int					TOOLBAR_HEIGHT						= 27;

	public static final int					STATUS_PANEL_HEIGHT					= 16;
	
	public static final int 				SHORT_SEARCH_COMPONENT_WIDTH		=130;
	public static final int 				LONG_SEARCH_COMPONENT_WIDTH			=200;

	public static final String				SYSTEM_CONSTANT_RESULTS_PANEL_ID	= "SystemConstantEnquiryWindowResultsPanel";

	/* these regex are used to validate input for each of the fields */
	public static final String				SC_MAX_RETENTION_REGEX				= "^[0-9]*\\.?[0-9]*$";
	public static final String				SC_INTERIM_RETENTION_REGEX			= "^[0-9]*\\.?[0-9]*$";
	public static final String				SC_MOS_RETENTION_REGEX				= "^[0-9]*\\.?[0-9]*$";
	public static final String				RETENTION_TYPE_REGEX				= "^[A-Za-z0-9\\s_-]*$";
	public static final String				FIN_QS0_REVIEW_REGEX				= "^.$";

	private GlobalSectionController			globalSectionController;

	private PackageRepositoryRemoteAsync	packageRepository;

	private AmountRenderer					amountRenderer;

	private boolean							canWrite							= false;

	private Panel							mainPanel;							
	private Toolbar							buttonToolbar;	//Toolbar contains the function buttons
	private Panel							resultPanel;	//Panel contains the search result grid
	private Panel							statusPanel;	//Panel contains the search result count message

	private EditorGridPanel					gridPanel;

	/* search panel */
	private Panel							searchPanel;

	private FieldListenerAdapter			enterKeyAdapter;

	private Field[]							searchFields;
	
	private NumberField						scMaxRetentionPercentField			= new NumberField();
	private NumberField						scInterimRetentionPercentField		= new NumberField();
	private NumberField						scMOSRetentionPercentField			= new NumberField();
	private ComboBox 						systemCodeComboBox					= new ComboBox();
	private ComboBox 						companyComboBox						= new ComboBox();
	private ComboBox 						scPaymentTermComboBox				= new ComboBox();
	private ComboBox 						retentionTypeComboBox				= new ComboBox();
	private ComboBox 						finQS0ReviewComboBox				= new ComboBox();
	
	private Label							systemCodeLabel						= new Label("System Code:");
	private Label							companyLabel						= new Label("Company:");
	private Label							scPaymentTermLabel					= new Label("SC Payment Term:");
	private Label							scMaxRetentionPercentLabel			= new Label("SC Max Retention %:");
	private Label							scInterimRetentionPercentLabel		= new Label("SC Interim Retention %:");
	private Label							scMOSRetentionPercentLabel			= new Label("SC MOS Retention %:");
	private Label							retentionTypeLabel					= new Label("Retention Type:");
	private Label							finQS0ReviewLabel					= new Label("Reviewed by Finance: ");
	
	private Label[]							searchLabels;

	/* ------------ */
	private Label							statusLabel							= new Label();

	/* ------------ */

	/* button panel */
	private ToolbarButton					addSystemConstantButton;
	private ToolbarButton					updateSystemConstantButton;
	private ToolbarButton					deleteSystemConstantButton;	
	
	/* A temp storage of the data from the asyn service */
	private ArrayList<String>				systemCodeOptionBuffer				= new ArrayList<String>();
	private ArrayList<String>				companyCodeOptionBuffer				= new ArrayList<String>();

	/* ------------ */
	private ColumnConfig[]					columns;
	private final RecordDef					systemConstantRecordDef				= new RecordDef(
																					new FieldDef[] {
																					new StringFieldDef("systemCode"),
																					new StringFieldDef("company"),
																					new StringFieldDef("scPaymentTerm"),
																					new StringFieldDef("scMaxRetentionPercent"),
																					new StringFieldDef("scInterimRetentionPercent"),
																					new StringFieldDef("scMOSRetentionPercent"),
																					new StringFieldDef("retentionType"),
																					new StringFieldDef("finQS0Review"),
																					new StringFieldDef("lastModifiedUser"),
																					new StringFieldDef("lastModifiedDate")
																					});	
	
	private Store							dataStore;
	private int								currentRowIndex						= -1;

	public SystemConstantEnquiryPanel(GlobalSectionController globalSectionControllerArgument) {
		super();
		try {
			this.globalSectionController = globalSectionControllerArgument;

			packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
			((ServiceDefTarget) packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);

			amountRenderer = new AmountRenderer(globalSectionController.getUser());

			setupUI();

			getPermissions();

			systemCodeComboBox.focus();
		} catch (Exception e) {
			//UIUtil.alert("Unexpected exception occured: " + e + ": " + e.getMessage());
			MessageBox.alert("Error", "Unexpected exception occured: " + e + ": " + e.getMessage());
		}
	}

	private void showCreateWindow() {
		if(this.globalSectionController.getCurrentWindow()== null){
			globalSectionController.showCreateSystemConstantWindow();
		}
	}

	private void getPermissions() {
		UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget) userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(
				globalSectionController.getUser().getUsername(),
				RoleSecurityFunctions.F010512_OBJECT_SUBSIDIARY_RULE_WINDOW,
				new AsyncCallback<List<String>>() {
					public void onSuccess(List<String> accessRightsReturned) {
						setCanWrite(accessRightsReturned.contains("WRITE"));
					}

					public void onFailure(Throwable e) {
						setCanWrite(false);
					}
				});
	}

	private void setCanWrite(boolean value) {
		this.canWrite = value;
		addSystemConstantButton.setVisible(this.canWrite);
		updateSystemConstantButton.setVisible(this.canWrite);
	}

	private void setupUI() {
		this.setPaddings(0);
		this.setLayout(new FitLayout());
		this.setHeight(WINDOW_HEIGHT);
		this.setWidth(WINDOW_WIDTH);
		this.setId(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
		this.setClosable(false);

		/* collapse the detail section if the detail section is not collapse */
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();

		mainPanel = new Panel();
		
		mainPanel.setFrame(true);
		mainPanel.setLayout(new RowLayout());		

		/* init search panel */
		constructSearchPanel();
		mainPanel.add(searchPanel);

		/* init tool bar */
		constructToolbar();
		mainPanel.add(buttonToolbar);

		/* init result grid panel */
		constructResultPanel();
		mainPanel.add(resultPanel);
		
		/* init status bar panel */
		constructStatusPanel();
		mainPanel.add(statusPanel);

		this.add(mainPanel);
	}

	private void doCommit() {
		setButtonEanble(updateSystemConstantButton,false);
		UIUtil.maskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID, "Updating records...", false);
		List<SystemConstant> requests = new ArrayList<SystemConstant>();
		
		/* 
		 * Check and validate the value of grid.
		 * if any value is invalid, the action is stopped, 
		 * else requests object is updated and sent to web service.
		 * */
		if(!setUpdateRequest(requests)){
			MessageBox.alert("One of the value in grid is invalid or empty.");
			UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
			return;
		}
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.updateMultipleSystemConstants(
				requests,
				globalSectionController.getUser().getUsername(),
				new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean ret) {
						for (Record rec : dataStore.getModifiedRecords()) {
							rec.set("lastModifiedUser", globalSectionController.getUser().getUsername());
							rec.set(
									"lastModifiedDate",
									DateTimeFormat.getFormat(GlobalParameter.DATETIME_FORMAT).format(new Date()));
						}
						try {
							dataStore.commitChanges();
						} catch (Throwable e) {
							MessageBox.alert("An exception occured while saving data: " + e);
						}

						UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
						setButtonEanble(updateSystemConstantButton,true);
					}

					public void onFailure(Throwable e) {
						MessageBox.alert("Update failed: " + e.getMessage());
						UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
						doSearch();
					}
				});
	}
	
	private boolean setUpdateRequest(List<SystemConstant> requests){
		String tempStr;
		boolean hasIvalidValue = false;
		for (Record rec : dataStore.getModifiedRecords()) {

			SystemConstant request = new SystemConstant();
			
			tempStr = rec.getAsString("systemCode");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setSystemCode(tempStr.trim());
			
			tempStr = rec.getAsString("company");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setCompany(tempStr.trim());
			
			tempStr = rec.getAsString("scPaymentTerm");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setScPaymentTerm(tempStr.trim());
			
			tempStr = rec.getAsString("scMaxRetentionPercent");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setScMaxRetentionPercent(Double.parseDouble(tempStr.trim()));
			
			tempStr = rec.getAsString("scInterimRetentionPercent");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setScInterimRetentionPercent(Double.parseDouble(tempStr.trim()));
			
			tempStr = rec.getAsString("scMOSRetentionPercent");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setScMOSRetentionPercent(Double.parseDouble(tempStr.trim()));
			
			tempStr = rec.getAsString("retentionType");
			if(tempStr == null){
				hasIvalidValue = true;
				break;
			}
			request.setRetentionType(tempStr.trim());
			
			tempStr = rec.getAsString("finQS0Review");
			if(tempStr == null)
				tempStr = SystemConstant.FINQS0REVIEW_N;	//default value of Reviewed by finance column is "N"
			request.setFinQS0Review(tempStr.trim());

			requests.add(request);
		}
		
		if(hasIvalidValue)
			return false;
		else
			return true;		
	}
	
	private void constructToolbar() {		
		buttonToolbar = new Toolbar();
		buttonToolbar.setWidth(WINDOW_WIDTH);
		buttonToolbar.setHeight(TOOLBAR_HEIGHT);		

		/* add update button to toolbar */
		updateSystemConstantButton = new ToolbarButton("Update");
		updateSystemConstantButton.setIconCls("save-button-icon");
		updateSystemConstantButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				doCommit();
			}
		});
		buttonToolbar.addButton(updateSystemConstantButton);
		buttonToolbar.addSeparator();
		setButtonEanble(updateSystemConstantButton,false);

		/* add inactive button to toolbar 
		 * inactive means deleting a record
		 * */
		deleteSystemConstantButton = new ToolbarButton("Delete", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (currentRowIndex < 0)
					return;

				MessageBox.confirm(
						"Delete Record",
						"Are you sure to delete the record?",
						new MessageBox.ConfirmCallback() {
							public void execute(String selectedOption) {
								if (selectedOption.equalsIgnoreCase("no"))
									return;
								UIUtil.maskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID, "Updating record...", false);
								SystemConstant request = new SystemConstant();
								final Record rec = dataStore.getAt(currentRowIndex);

								request.setSystemCode(rec.getAsString("systemCode").trim());
								request.setCompany(rec.getAsString("company").trim());
								SessionTimeoutCheck.renewSessionTimer();
								packageRepository.inactivateSystemConstant(
										request,
										globalSectionController.getUser().getUsername(),
										new AsyncCallback<Boolean>() {
											public void onSuccess(Boolean res) {
												currentRowIndex = -1;
												setButtonEanble(deleteSystemConstantButton,false);
												doSearch();
											}

											public void onFailure(Throwable e) {
												MessageBox.alert("Failed to delete system constant: " + e.getMessage());
											}
										});

								UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
							}
						});
			}

		});
		deleteSystemConstantButton.setIconCls("cancel-icon");
		buttonToolbar.addButton(deleteSystemConstantButton);
		buttonToolbar.addSeparator();
		setButtonEanble(deleteSystemConstantButton,false);

		/* add create button */
		addSystemConstantButton = new ToolbarButton("Add");
		addSystemConstantButton.setIconCls("add-button-icon");
		addSystemConstantButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				showCreateWindow();
			}
		});
		buttonToolbar.addButton(addSystemConstantButton);
		buttonToolbar.addSeparator();	
	}

	private void constructStatusPanel() {
		statusPanel = new Panel();
		statusPanel.setHeight(STATUS_PANEL_HEIGHT);
		setStatus("Total Number of Necords:");
		statusLabel.setCtCls("right-align");
		statusPanel.add(statusLabel);
	}
	
	private void constructResultPanel() {
		resultPanel = new Panel();
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);
		resultPanel.setId(SYSTEM_CONSTANT_RESULTS_PANEL_ID);

		resultPanel.setLayout(new FitLayout());
		gridPanel = new EditorGridPanel();
		gridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				currentRowIndex = rowIndex;
				deleteSystemConstantButton.setDisabled(false);
			}
		});

		Renderer editableAmountRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return setColor(str);
			}
		};

		ColumnConfig systemCodeColumn = new ColumnConfig("System Code", "systemCode", 100, false);
		ColumnConfig companyColumn = new ColumnConfig("Company", "company", 100, false);
		
		ColumnConfig scPaymentTermColumn = new ColumnConfig("SC Payment Term", "scPaymentTerm", 230, false) {
			{
				setRenderer(new Renderer() {
					public String render(Object value,
											com.gwtext.client.widgets.grid.CellMetadata cellMetadata,
											Record record,
											int rowIndex,
											int colNum,
											Store store) {
						for (String[] pair : GlobalParameter.getPaymentTerms(false)) {
							if (pair[0].equals((String) value)) {
								return setColor(pair[1]);
							}
						}
						return setColor((String) value);
					}
				});
			}
		};

		ColumnConfig scMaxRetentionPercentColumn = new ColumnConfig("SC Max Retention %", "scMaxRetentionPercent", 120, false);
		scMaxRetentionPercentColumn.setRenderer(editableAmountRenderer);
		ColumnConfig scInterimRetentionPercentColumn = new ColumnConfig("SC Interim Retention %", "scInterimRetentionPercent", 120, false);
		scInterimRetentionPercentColumn.setRenderer(editableAmountRenderer);
		ColumnConfig scMOSRetentionPercentColumn = new ColumnConfig("SC MOS Retention %", "scMOSRetentionPercent", 120, false);
		scMOSRetentionPercentColumn.setRenderer(editableAmountRenderer);
		//ColumnConfig retentionTypeColumn = new ColumnConfig("Retention Type", "retentionType", 200, false);
		ColumnConfig retentionTypeColumn = new ColumnConfig("Retention Type", "retentionType", 200, false){
			{
				setRenderer(new Renderer() {
					public String render(Object value,
											com.gwtext.client.widgets.grid.CellMetadata cellMetadata,
											Record record,
											int rowIndex,
											int colNum,
											Store store) {
						for (String[] pair : GlobalParameter.getRetentionType(false)) {
							if (pair[0].equals((String) value)) {
								return setColor(pair[1]);
							}
						}
						return setColor((String) value);
					}
				});
			}
		};
		//ColumnConfig finQS0ReviewColumn = new ColumnConfig("Reviewed by Finance", "finQS0Review", 120, false);
		ColumnConfig finQS0ReviewColumn = new ColumnConfig("Reviewed by Finance", "finQS0Review", 120, false){
			{
				setRenderer(new Renderer() {
					public String render(Object value,
											com.gwtext.client.widgets.grid.CellMetadata cellMetadata,
											Record record,
											int rowIndex,
											int colNum,
											Store store) {
						for (String[] pair : GlobalParameter.getReviewedByFinance(false)) {
							if (pair[0].equals((String) value)) {
								//return pair[1];
								return setColor(pair[1]);
							}
						}
						//return (String) value;
						return setColor((String) value);						
					}
				});
			}			
		};
		ColumnConfig lastModifiedUserColumn = new ColumnConfig("Last modified User", "lastModifiedUser", 110, false);
		ColumnConfig lastModifiedDateColumn = new ColumnConfig("Last modified Date", "lastModifiedDate", 110, false);

		columns = new ColumnConfig[] {
				systemCodeColumn,
				companyColumn,
				scPaymentTermColumn,
				scMaxRetentionPercentColumn,
				scInterimRetentionPercentColumn,
				scMOSRetentionPercentColumn,
				retentionTypeColumn,
				finQS0ReviewColumn,
				lastModifiedUserColumn,
				lastModifiedDateColumn
		};

		for (ColumnConfig c : columns) {
			c.setSortable(true);
		}

		Store scPaymentTermStore = new SimpleStore(new String[] { "scPaymentTermCode", "scPaymentTermDescription" }, GlobalParameter.getPaymentTerms(false));
		scPaymentTermStore.load();

		ComboBox scPaymentTermColumnComboBox = new ComboBox();
		scPaymentTermColumnComboBox.setForceSelection(true);
		scPaymentTermColumnComboBox.setMinChars(1);
		scPaymentTermColumnComboBox.setFieldLabel("SC Payment Terms");
		scPaymentTermColumnComboBox.setStore(scPaymentTermStore);
		scPaymentTermColumnComboBox.setDisplayField("scPaymentTermDescription");
		scPaymentTermColumnComboBox.setName("scPaymentTermDescription");
		scPaymentTermColumnComboBox.setValueField("scPaymentTermCode");
		scPaymentTermColumnComboBox.setMode(ComboBox.LOCAL);
		scPaymentTermColumnComboBox.setTriggerAction(ComboBox.ALL);
		scPaymentTermColumnComboBox.setEmptyText("Payment Term");
		scPaymentTermColumnComboBox.setLoadingText("Loading...");
		scPaymentTermColumnComboBox.setTypeAhead(true);
		scPaymentTermColumnComboBox.setWidth(230);

		TextField scMaxRetentionPercentColumnField = new TextField();
		TextField scInterimRetentionPercentColumnField = new TextField();
		TextField scMOSRetentionPercentColumnField = new TextField();
		ComboBox retentionTypeColumnComboBox = FieldFactory.createComboBox();
		setStaticComboBoxOption(retentionTypeColumnComboBox,GlobalParameter.getRetentionType(false));

		ComboBox finQS0ReviewColumnComboBox = FieldFactory.createComboBox();
		setStaticComboBoxOption(finQS0ReviewColumnComboBox,GlobalParameter.getReviewedByFinance(false));

		scMaxRetentionPercentColumnField.setRegex(SC_MAX_RETENTION_REGEX);
		scInterimRetentionPercentColumnField.setRegex(SC_INTERIM_RETENTION_REGEX);
		scMOSRetentionPercentColumnField.setRegex(SC_MOS_RETENTION_REGEX);
		retentionTypeColumnComboBox.setRegex(RETENTION_TYPE_REGEX);
		finQS0ReviewColumnComboBox.setRegex(FIN_QS0_REVIEW_REGEX);

		scPaymentTermColumn.setEditor(new GridEditor(scPaymentTermColumnComboBox));
		scMaxRetentionPercentColumn.setEditor(new GridEditor(scMaxRetentionPercentColumnField));
		scInterimRetentionPercentColumn.setEditor(new GridEditor(scInterimRetentionPercentColumnField));
		scMOSRetentionPercentColumn.setEditor(new GridEditor(scMOSRetentionPercentColumnField));
		retentionTypeColumn.setEditor(new GridEditor(retentionTypeColumnComboBox));
		finQS0ReviewColumn.setEditor(new GridEditor(finQS0ReviewColumnComboBox));

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(systemConstantRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		gridPanel.setStore(this.dataStore);
		gridPanel.setColumnModel(new ColumnModel(columns));

		setUpEditing();

		resultPanel.add(gridPanel);
	}
	
	private String setColor(String str){
		if(str==null)
			str="";
		return "<span style=\"color:blue;\">" + str + "</span>";
	}

	private void setUpEditing() {
		gridPanel.addEditorGridListener(new EditorGridListenerAdapter() {
			public boolean doBeforeEdit(GridPanel grid,
										Record record,
										String field,
										Object value,
										int rowIndex,
										int colIndex) {
				if (!canWrite) {
					return false;
				}
				return true;
			}

			public boolean doValidateEdit(GridPanel grid,
											Record record,
											String field,
											Object value,
											Object originalValue,
											int rowIndex,
											int colIndex) {
				String val = (String) value;
				String origVal = (String) originalValue;
				if (val.equals(origVal)) {
					return false;
				}
				return true;
			}

			public void onAfterEdit(GridPanel grid,
									Record record,
									String field,
									Object newValue,
									Object oldValue,
									int rowIndex,
									int colIndex) {
				setButtonEanble(updateSystemConstantButton,true);
			}
		});

	}

	private void constructSearchPanel() {
		@SuppressWarnings("unused")
		String[][] comboBoxOptions; /*2D array used to defined combobox option*/
		searchPanel = new Panel();
		searchPanel.setLayout(new TableLayout(SEARCH_PANEL_TABLE_SIZE));
		searchPanel.setPaddings(2);
		searchPanel.setHeight(SEARCH_PANEL_HEIGHT);

		enterKeyAdapter = new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					doSearch();
				}
			}
		};		
		
		systemCodeComboBox = FieldFactory.createComboBox();
		systemCodeComboBox.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		queryForComboBoxOptions(systemCodeOptionBuffer, SystemConstant.SEARCHING_FIELD_SYSTEM_CODE);

		companyComboBox = FieldFactory.createComboBox();
		companyComboBox.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		queryForComboBoxOptions(companyCodeOptionBuffer, SystemConstant.SEARCHING_FIELD_COMPANY_CODE);

		/* config payment term combobox */
		scPaymentTermComboBox = FieldFactory.createComboBox();
		scPaymentTermComboBox.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		setStaticComboBoxOption(scPaymentTermComboBox, stringArrAdapter(GlobalParameter.getPaymentTerms(true)));
		
		/* config retention type combobox */
		comboBoxOptions = new String [][]{
				new String[]{"", SystemConstant.CONST_ALL},
				new String[]{SCPackage.RETENTION_LUMPSUM,SCPackage.RETENTION_LUMPSUM},
				new String[]{SCPackage.RETENTION_ORIGINAL,SCPackage.RETENTION_ORIGINAL},
				new String[]{SCPackage.RETENTION_REVISED,SCPackage.RETENTION_REVISED}
		};
		retentionTypeComboBox = FieldFactory.createComboBox();
		retentionTypeComboBox.setWidth(LONG_SEARCH_COMPONENT_WIDTH);
		setStaticComboBoxOption(retentionTypeComboBox, GlobalParameter.getRetentionType(true));

		/* config reviewed by finance combobox */
		comboBoxOptions = new String[][]{
				{"",SystemConstant.FINQS0REVIEW_NA},
				{SystemConstant.FINQS0REVIEW_Y, SystemConstant.FINQS0REVIEW_Y},
				{SystemConstant.FINQS0REVIEW_N, SystemConstant.FINQS0REVIEW_N},
				};

		finQS0ReviewComboBox = FieldFactory.createComboBox();
		finQS0ReviewComboBox.setWidth(LONG_SEARCH_COMPONENT_WIDTH);
		setStaticComboBoxOption(finQS0ReviewComboBox, GlobalParameter.getReviewedByFinance(true));
		
		/* update textfield width*/
		scMaxRetentionPercentField.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		scInterimRetentionPercentField.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		scMOSRetentionPercentField.setWidth(SHORT_SEARCH_COMPONENT_WIDTH);
		
		searchFields = new Field[8];
		searchFields[0] = systemCodeComboBox;
		searchFields[1] = companyComboBox;
		searchFields[2] = scPaymentTermComboBox;
		searchFields[3] = retentionTypeComboBox;
		searchFields[4] = scMaxRetentionPercentField;
		searchFields[5] = scInterimRetentionPercentField;
		searchFields[6] = scMOSRetentionPercentField;
		searchFields[7] = finQS0ReviewComboBox;
		
		searchLabels = new Label[8];
		searchLabels[0] = systemCodeLabel;
		searchLabels[1] = companyLabel;
		searchLabels[2] = scPaymentTermLabel;
		searchLabels[3] = retentionTypeLabel;
		searchLabels[4] = scMaxRetentionPercentLabel;
		searchLabels[5] = scInterimRetentionPercentLabel;
		searchLabels[6] = scMOSRetentionPercentLabel;
		searchLabels[7] = finQS0ReviewLabel;
	
		for (int i = 0; i < searchFields.length; i++) {
			
			/* Add label to the search panel */
			searchPanel.add(searchLabels[i]);
			searchLabels[i].setCtCls("table-cell");
			
			/* Add input field to the search panel */
			searchPanel.add(searchFields[i]);
			searchFields[i].setCtCls("table-cell");
			searchFields[i].addListener(enterKeyAdapter);
			
			if(searchFields[i] instanceof ComboBox){
				((ComboBox)searchFields[i]).setForceSelection(true);
			}
		}
		
		/* Add search button */
		Button searchButton = new Button("Search");
		searchButton.setCtCls("right-align");
		searchButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				/* collapse the detail section if the detail section is not collapse */
				if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
					globalSectionController.getDetailSectionController().getMainPanel().collapse();
				doSearch();
			}
		});
		
		/* Place the search button on the right hand side of search panel */
		for(int i = 0; i < SEARCH_PANEL_TABLE_SIZE-1; i++){
			searchPanel.add(new Label(""));
		}
		searchPanel.add(searchButton);
	}

	private void doSearch() {
		UIUtil.maskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID, "Searching...", false);
		
		String systemCodeQuery = null;
		String companyQuery = null;
		String scPaymentTermQuery = null;
		Double scMaxRetentionPercentQuery = null;
		Double scInterimRetentionPercentQuery = null;
		Double scMOSRetentionPercentQuery = null;
		String retentionTypeQuery = null;
		String finQS0ReviewQuery = null;

		/* set search criteria */
		if (systemCodeComboBox.getText().trim().length() > 0) {
			systemCodeQuery = systemCodeComboBox.getText();
		}		
	
		if (companyComboBox.getText().trim().length() > 0) {
			companyQuery = companyComboBox.getText();
		}
		
		if (scPaymentTermComboBox.getText().trim().length() > 0) {
			scPaymentTermQuery = scPaymentTermComboBox.getText();
		}
		
		if (scMaxRetentionPercentField.getText().trim().length() > 0) {
			scMaxRetentionPercentQuery = Double.parseDouble(scMaxRetentionPercentField.getText());
		}
		
		if (scInterimRetentionPercentField.getText().trim().length() > 0) {
			scInterimRetentionPercentQuery = Double.parseDouble(scInterimRetentionPercentField.getText());
		}
		
		if (scMOSRetentionPercentField.getText().trim().length() > 0) {
			scMOSRetentionPercentQuery = Double.parseDouble(scMOSRetentionPercentField.getText());
		}
		
		if (retentionTypeComboBox.getText().trim().length() > 0) {
			retentionTypeQuery = retentionTypeComboBox.getText();
		}

		if (finQS0ReviewComboBox.getText().trim().length() > 0) {
			finQS0ReviewQuery = finQS0ReviewComboBox.getText();
		}
		SessionTimeoutCheck.renewSessionTimer();
		/* call remote procedure */
		packageRepository.searchSystemConstants(
				systemCodeQuery,
				companyQuery,
				scPaymentTermQuery,
				scMaxRetentionPercentQuery,
				scInterimRetentionPercentQuery,
				scMOSRetentionPercentQuery,
				retentionTypeQuery,
				finQS0ReviewQuery,
				
				new AsyncCallback<List<SystemConstant>>() {
					public void onSuccess(List<SystemConstant> ret) {
						populateGrid(ret);
						UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
					}

					public void onFailure(Throwable e) {
						if (e instanceof com.google.gwt.user.client.rpc.InvocationException)
							MessageBox.alert("Session Timeout; refresh to login.");
						else
							MessageBox.alert("Failed to retrieve system constants from database: " + e.getMessage());
						UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
					}
				});
	}

	private void populateGrid(List<SystemConstant> systemConstantList) {

		try {
			dataStore.removeAll();

			Record[] records = new Record[systemConstantList.size()];

			int index = 0;

			for (SystemConstant syscon : systemConstantList) {
				records[index] = systemConstantRecordDef.createRecord(new Object[] {
						syscon.getSystemCode(),
						syscon.getCompany(),
						syscon.getScPaymentTerm(),
						amountRenderer.render(Double.toString(syscon.getScMaxRetentionPercent())),
						amountRenderer.render(Double.toString(syscon.getScInterimRetentionPercent())),
						amountRenderer.render(Double.toString(syscon.getScMOSRetentionPercent())),
						syscon.getRetentionType(),
						syscon.getFinQS0Review(),
						syscon.getLastModifiedUser(),
						DateTimeFormat.getFormat(GlobalParameter.DATETIME_FORMAT).format(syscon.getLastModifiedDate())
				});

				index++;
			}

			dataStore.add(records);
			
			setStatus("Total Number of Records: "+systemConstantList.size());

		} catch (Exception e1) {
			MessageBox.alert("Unexpected exception: " + e1);
		}
	}
	
	private void setStatus(String msg){
		this.statusLabel.setHtml("<b>"+msg+"</b>");
	}

	/*
	 * search for distinct element of specific column of system constant properties
	 * @author xethhung
	 * 2015-05-21 
	 */
	private void queryForComboBoxOptions(final ArrayList<String> comboBoxData,
			final String searchingField) {
		UIUtil.maskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID, "Initializing...", false);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSystemConstantSearchOption(searchingField,
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						try{
							comboBoxData.removeAll(comboBoxData);
							String[][] ValuePair = new String[result.size()][];
							int index = 0;
							if(result != null){
								for(String str : result){
									ValuePair[index] = new String[2];
									if(str==null)
										continue;
									ValuePair[index][0] = str;
									ValuePair[index][1] = str;
									comboBoxData.add(str);
									index++;
								}
							}
							setDynamicComboBoxOption(comboBoxData);
						}
						catch(Exception ex){
							MessageBox.alert(ex.getMessage());
						}

					}

					public void onFailure(Throwable e) {
						if (e instanceof com.google.gwt.user.client.rpc.InvocationException)
							MessageBox.alert("Session Timeout; refresh to login.");
						else
							MessageBox.alert("Failed to retrieve system constants from database: " + e.getMessage());
						UIUtil.unmaskPanelById(SYSTEM_CONSTANT_RESULTS_PANEL_ID);
					}
				});	
	}

	/*
	 * set combobox option
	 * @author xethhung
	 * 2015-05-21 
	 */
	private void setStaticComboBoxOption(ComboBox combobox, String[][] valuePair){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";
		String[] header = new String[]{valueField,displayField};	
		Store store = new SimpleStore(header, valuePair);

		if(combobox != null){
			combobox.setStore(store);
			combobox.setDisplayField(displayField);
			combobox.setValueField(valueField);
			combobox.setValue(valuePair[0][0]);
			combobox.setEmptyText(valuePair[0][1]);
			combobox.setForceSelection(true);
		}
	}


	/*
	 * Set combobox option(data from database). 
	 * 
	 * @author xethhung
	 * 2015-05-21 
	 */
	private void setDynamicComboBoxOption(ArrayList<String> comboBoxData){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";

		String[] header = new String[]{valueField,displayField};

		RecordDef comboBoxRecordDef = new RecordDef(new FieldDef[]{new StringFieldDef(valueField),
				   new StringFieldDef(displayField)});
		String[][] ValuePair = new String[comboBoxData.size()+1][];
		Store store = new SimpleStore(header, ValuePair);
		
		int index = 1;
		ValuePair[0] = new String[]{"",SystemConstant.CONST_ALL};
		store.add(comboBoxRecordDef.createRecord(ValuePair[0]));
		

		for(String str : comboBoxData){
			ValuePair[index] = new String[]{str,str};
			store.add(comboBoxRecordDef.createRecord(ValuePair[index]));
			index++;
		}		
		store.commitChanges();

		ComboBox tempCombobox = null;
		if(comboBoxData == systemCodeOptionBuffer)
			tempCombobox = this.systemCodeComboBox;
		else if(comboBoxData == companyCodeOptionBuffer)
			tempCombobox = this.companyComboBox;

		if(tempCombobox != null){
			tempCombobox.setStore(store);
			tempCombobox.setCtCls("table-cell");
			tempCombobox.setMinChars(1);
			tempCombobox.setDisplayField(displayField);
			tempCombobox.setValueField(valueField);
			tempCombobox.setValue(ValuePair[0][0]);
			tempCombobox.setEmptyText(ValuePair[0][1]);
			tempCombobox.setEditable(true);
			tempCombobox.setVisible(true);
			tempCombobox.setForceSelection(true);
			tempCombobox.setMode(ComboBox.LOCAL);
			tempCombobox.setTriggerAction(ComboBox.ALL);
			tempCombobox.setSelectOnFocus(true);		
			tempCombobox.setTypeAhead(true);

		}
	}
	
	private void setButtonEanble(Button btn, boolean Enable){
		if(Enable)
			btn.enable();
		else
			btn.disable();
	}
	
	private String[][] stringArrAdapter(String[][] source){
		String[][] strArrSource = source;
		String[][] strArr = new String[strArrSource.length][];
		for(int i = 0 ; i < strArr.length ; i++){
			if(i==0)
				strArr[i] = new String[]{strArrSource[i][0],strArrSource[i][1]};
			else
				strArr[i] = new String[]{strArrSource[i][0],strArrSource[i][0]};
		}
		return strArr;

	}
}
