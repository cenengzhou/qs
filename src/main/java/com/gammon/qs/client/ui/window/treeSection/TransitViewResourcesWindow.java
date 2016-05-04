package com.gammon.qs.client.ui.window.treeSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.TransitRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.TransitResourceWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
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
import com.google.gwt.i18n.client.NumberFormat;

public class TransitViewResourcesWindow extends Window {
	public static final String MAIN_PANEL_ID = "TransitViewResourcesWindow";
	private PaginationToolbar paginationToolbar;
	
	private GlobalSectionController globalSectionController;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private TransitRepositoryRemoteAsync transitRepository;
	
	// used to check access rights
	private List<String> accessRightsList;
	
	private Store dataStore;
	
	private ArrowKeyNavigation arrowKeyNavigation;
	
	private EditorGridPanel resultEditorGridPanel;
	
	//Search fields
	private TextField billNoField;
	private TextField subBillNoField;
	private TextField pageNoField;
	private TextField itemNoField;
	private TextField resourceCodeField;
	private TextField objectCodeField;
	private TextField subsidiaryCodeField;
	private TextField descriptionField;
	private TextField applyCodeField;
	private ComboBox applyToComboBox;
	private final Store applyToStore = new SimpleStore(new String[]{"applyValue", "applyDisplay"}, TransitViewResourcesWindow.applyTo());
	
	// store apply all to 
	private static String[][] applyTo(){
		return new String[][]{
				new String[]{"O", "Object Code"},
				new String[]{"S", "Subsidiary Code"}
		};
	}
	
	//Search parameters (used for pagination)
	private String jobNumber;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	private String resourceCode;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	
	private Button saveButton;
	// added by brian on 20110207
	// control the show and hide of the button
	private boolean showSaveButton = false;
	
	private TransitHeader localHeader;
	
	private RecordDef resourceRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("id"),
					new StringFieldDef("bpi"),
					new StringFieldDef("resourceType"),
					new StringFieldDef("resourceCode"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("waste"),
					new StringFieldDef("totalQuantity"),
					new StringFieldDef("rate"),
					new StringFieldDef("value")
				}
			);
	
	public TransitViewResourcesWindow(final GlobalSectionController globalSectionController, TransitHeader header){
		super();
		this.localHeader = header;
		this.setModal(true);
		this.setTitle("Transit - Update Resources");
		this.setId(MAIN_PANEL_ID);
		this.setPaddings(5);
		this.setWidth(1080);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		jobNumber = globalSectionController.getJob().getJobNumber();
		
		this.globalSectionController = globalSectionController;
		
		userAccessRightsRepository = this.globalSectionController.getUserAccessRightsRepository();
		transitRepository = this.globalSectionController.getTransitRepository();		
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		
		//search Panel
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(85);	
		searchPanel.setLayout(new TableLayout(9));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};
		
		Label billNoLabel = new Label("Bill No");
		billNoLabel.setCls("table-cell");
		searchPanel.add(billNoLabel);
		billNoField =new TextField("Bill No", "billNo", 80);
		billNoField.setCtCls("table-cell");
		billNoField.addListener(searchListener);
		searchPanel.add(billNoField);
		
		Label subBillNoLabel = new Label("SubBill No");
		subBillNoLabel.setCls("table-cell");
		searchPanel.add(subBillNoLabel);
		subBillNoField =new TextField("SubBill No", "subBillNo", 80);
		subBillNoField.setCtCls("table-cell");
		subBillNoField.addListener(searchListener);
		searchPanel.add(subBillNoField);
		
		Label pageNoLabel = new Label("Page No");
		pageNoLabel.setCls("table-cell");
		searchPanel.add(pageNoLabel);
		pageNoField =new TextField("Page No", "pageNo", 80);
		pageNoField.setCtCls("table-cell");
		pageNoField.addListener(searchListener);
		searchPanel.add(pageNoField);
		
		Label itemNoLabel = new Label("Item No");
		itemNoLabel.setCls("table-cell");
		searchPanel.add(itemNoLabel);
		itemNoField =new TextField("Item No", "itemNo", 80);
		itemNoField.setCtCls("table-cell");
		itemNoField.addListener(searchListener);
		searchPanel.add(itemNoField);
		
		searchPanel.add(new Label("")); //Empty label
		
		Label resourceCodeLabel = new Label("Resource Code");
		resourceCodeLabel.setCls("table-cell");
		searchPanel.add(resourceCodeLabel);
		resourceCodeField = new TextField("Resource Code", "resourceCode", 80);
		resourceCodeField.setCtCls("table-cell");
		resourceCodeField.addListener(searchListener);
		searchPanel.add(resourceCodeField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel);
		objectCodeField = new TextField("Object Code", "objectCode", 80);
		objectCodeField.setCtCls("table-cell");
		objectCodeField.addListener(searchListener);
		searchPanel.add(objectCodeField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
		subsidiaryCodeField = new TextField("Subsidiary Code", "subsidiaryCode", 80);
		subsidiaryCodeField.setCtCls("table-cell");
		subsidiaryCodeField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeField);
		
		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");
		searchPanel.add(descriptionLabel);
		descriptionField = new TextField("Description", "description", 150);
		descriptionField.setCtCls("table-cell");
		descriptionField.addListener(searchListener);
		searchPanel.add(descriptionField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				search();
			};
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton);
		
		//Grid panel
		User user = globalSectionController.getUser();
		resultEditorGridPanel = new EditorGridPanel();
		
		this.arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);
		
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer rateRenderer = new RateRenderer(user);
		
		// added by brian on 20110124
		// customized amount render to bold the total amount
		Renderer customAmountRenderer = new Renderer(){
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
		
				if(" ".equals(record.getAsString("bpi")) && " ".equals(record.getAsString("resourceType")) && " ".equals(record.getAsString("resourceCode"))){
					return "<b>"+format.format(raw) +"</b>";
				}
				return format.format(raw);
			}
		};
		
		Renderer editableRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = value.toString();
				
				if(" ".equals(record.getAsString("bpi")) && " ".equals(record.getAsString("resourceType")) && " ".equals(record.getAsString("resourceCode")))
					return "<b>" + str + "</b>";
				else
					return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		
		ColumnConfig bpiColumn = new ColumnConfig("B/P/I", "bpi", 80, false);
		ColumnConfig resourceTypeColumn = new ColumnConfig("Type", "resourceType", 30, false);
		resourceTypeColumn.setTooltip("Resource Type");
		ColumnConfig resourceCodeColumn = new ColumnConfig("Resource Code", "resourceCode", 60, false);
		resourceCodeColumn.setTooltip("Resource Code");
		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColumn.setEditor(new GridEditor(new TextField()));
		objectCodeColumn.setRenderer(editableRenderer);
		objectCodeColumn.setTooltip("Object Code");
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColumn.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColumn.setTooltip("Subsidiary Code");
		subsidiaryCodeColumn.setRenderer(editableRenderer);
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 250, false);
		descriptionColumn.setEditor(new GridEditor(new TextField()));
		descriptionColumn.setRenderer(editableRenderer);
		ColumnConfig unitColumn = new ColumnConfig("Unit", "unit", 40, false);
		ColumnConfig wasteColumn = new ColumnConfig("Waste", "waste", 110, false);
		wasteColumn.setRenderer(quantityRenderer);
		wasteColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig totalQuantityColumn = new ColumnConfig("Total Qty", "totalQuantity", 110, false);
		totalQuantityColumn.setRenderer(quantityRenderer);		
		totalQuantityColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig rateColumn = new ColumnConfig("Rate", "rate", 120, false);
		rateColumn.setRenderer(rateRenderer);
		rateColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig valueColumn = new ColumnConfig("Value", "value", 120, false);
		valueColumn.setRenderer(customAmountRenderer);
		valueColumn.setAlign(TextAlign.RIGHT);
			
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
			bpiColumn,
			resourceTypeColumn,
			resourceCodeColumn,
			objectCodeColumn,
			subsidiaryCodeColumn,
			descriptionColumn,
			unitColumn,
			wasteColumn,
			totalQuantityColumn,
			rateColumn,
			valueColumn
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		
		resultEditorGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			
			// added by brian on 20110124
			// get the "description" column field of a record, check if == "TOTAL:" disallow edit
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex){
				boolean isEditable = true;
				
				if(" ".equals(record.getAsString("bpi")) && " ".equals(record.getAsString("resourceType")) && " ".equals(record.getAsString("resourceCode")))
					isEditable = false;
				
				if(" ".equals(record.getAsString("objectCode")))
					isEditable = false;
				
				if(" ".equals(record.getAsString("subsidiaryCode")))
					isEditable = false;
				
				if(isEditable){
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				}
				else{
					arrowKeyNavigation.resetState();
				}
				
				return isEditable;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if(field.equals("objectCode")){
					String objectCode = (String)value;
					if(objectCode == null || objectCode.length() != 6){
						MessageBox.alert("Object code must be 6 digits in length");
						return false;
					}
				}
				else if(field.equals("subsidiaryCode")){
					String subsidiaryCode = (String)value;
					if(subsidiaryCode == null || subsidiaryCode.length() != 8){
						MessageBox.alert("Subsidiary code must be 8 digits in length");
						return false;
					}
				}
				else if(field.equals("description")){
					String description = (String)value;
					if(description == null || description.trim().length() == 0){
						MessageBox.alert("Description must not be blank");
						return false;
					}
				}
				return true;
			}
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				if(dataStore.getModifiedRecords().length > 0){
					MessageBox.confirm("Continue?", "If you continue to next page, the changes you made on this page will be lost.  Continue to next page?", 
							new MessageBox.ConfirmCallback(){
								public void execute(String btnID) {
									if(btnID.equals("yes"))
										searchByPage(pageNum);
								}
					});
				}
				else
					searchByPage(pageNum);
			}
		});
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		
		// mass apply panel
		Panel massApplyPanel;
		massApplyPanel = new Panel();
		massApplyPanel.setPaddings(3);
		massApplyPanel.setFrame(true);
		massApplyPanel.setHeight(50);
		TableLayout massApplyPanelLayout = new TableLayout(8);		
		massApplyPanel.setLayout(massApplyPanelLayout);
		
		Label applyAllLabel = new Label("Apply");
		applyAllLabel.setCls("table-cell");
		massApplyPanel.add(applyAllLabel);
		
		applyCodeField = new TextField("ApplyCode", "applyCode", 80);
		applyCodeField.setCtCls("table-cell");
		massApplyPanel.add(applyCodeField);
		
		Label applyToLabel = new Label("to");
		applyToLabel.setCls("table-cell");
		massApplyPanel.add(applyToLabel);
		
		applyToComboBox = new ComboBox();
		applyToStore.load();
		applyToComboBox.setForceSelection(true);
		applyToComboBox.setStore(applyToStore);
		applyToComboBox.setDisplayField("applyDisplay");
		applyToComboBox.setValueField("applyValue");
		applyToComboBox.setWidth(100);
		applyToComboBox.setValue("O");
		massApplyPanel.add(applyToComboBox);
		
		Label emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(30);
		massApplyPanel.add(emptySpace);
		
		Button applyButton = new Button("Apply");
		applyButton.setCls("table-cell");
		applyButton.addListener( new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if(applyCodeField.getText() != null && applyCodeField.getText().trim().length()>0){
					if(applyAll())
						resultEditorGridPanel.stopEditing();
				}
			};	
		});
		
		massApplyPanel.add(applyButton);
		
		
		mainPanel.add(searchPanel);
		mainPanel.add(massApplyPanel);
		mainPanel.add(resultEditorGridPanel);
		this.add(mainPanel);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				resultEditorGridPanel.stopEditing();
				saveUpdates();
			};
		});
		saveButton.hide();
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				if(dataStore.getModifiedRecords().length == 0)
					globalSectionController.closeCurrentWindow();
				else{
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		globalSectionController.closeCurrentWindow();
	                            }   
	                        });
				}
			};
		});
		
		this.setButtons(new Button[]{saveButton, closeButton});
		
		securitySetup();
		
		controlSaveBotton();
	}
	
	private void search(){
		billNo = billNoField.getValueAsString();
		subBillNo = subBillNoField.getValueAsString();
		pageNo = pageNoField.getValueAsString();
		itemNo = itemNoField.getValueAsString();
		resourceCode = resourceCodeField.getValueAsString();
		objectCode = objectCodeField.getValueAsString();
		subsidiaryCode = subsidiaryCodeField.getValueAsString();
		description = descriptionField.getValueAsString();
		
		searchByPage(0);
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Retrieving Resources", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.searchTransitResourcesByPage(jobNumber, billNo, subBillNo, pageNo, 
				itemNo, resourceCode, objectCode, subsidiaryCode, description, 
				pageNum, new AsyncCallback<PaginationWrapper<TransitResourceWrapper>>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
					}

					public void onSuccess(
							PaginationWrapper<TransitResourceWrapper> wrapper) {
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
						populateGrid(wrapper);
					}
		});	
	}
	
	private void populateGrid(PaginationWrapper<TransitResourceWrapper> wrapper){
		dataStore.rejectChanges();
		dataStore.removeAll();
		if(wrapper == null)
			return;
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		if(wrapper.getCurrentPageContentList() == null)
			return;
		
		for(TransitResourceWrapper resourceWrapper : wrapper.getCurrentPageContentList()){
			dataStore.add(createRecord(resourceWrapper));
		}
	}
	
	// added by brian on 20110121
	// create record to add into datastore + special handle the total line
	private Record createRecord(TransitResourceWrapper resourceWrapper){
		Record record = null;
		// if description is TOTAL: create a special record for total line
		if(isTotalLine(resourceWrapper)){
			
//			MessageBox.alert("making total line: " + resourceWrapper.getDescription() + " " + resourceWrapper.getValue());
			
			record = resourceRecordDef.createRecord(new Object[]{
					" ",
					" ",
					" ",
					" ",
					" ",
					" ",
					resourceWrapper.getDescription() != null ? resourceWrapper.getDescription() : "",
					" ",
					" ",
					" ",
					" ",
					resourceWrapper.getValue() != null ? resourceWrapper.getValue().toString() : "0"
				});
		}
		else{
			String bpi = "";
			bpi += resourceWrapper.getBillNo() == null ? "/" : resourceWrapper.getBillNo() + "/";
			bpi += resourceWrapper.getSubBillNo() == null ? "//" : resourceWrapper.getSubBillNo() + "//";
			bpi += resourceWrapper.getPageNo() == null ? "/" : resourceWrapper.getPageNo() + "/";
			bpi += resourceWrapper.getItemNo() == null ? "" : resourceWrapper.getItemNo();
			
			record = resourceRecordDef.createRecord(new Object[]{
					resourceWrapper.getId().toString(),
					bpi,
					resourceWrapper.getType(),
					resourceWrapper.getResourceCode(),
					resourceWrapper.getObjectCode(),
					resourceWrapper.getSubsidiaryCode(),
					resourceWrapper.getDescription(),
					resourceWrapper.getUnit(),
					resourceWrapper.getWaste() != null ? resourceWrapper.getWaste().toString() : "0",
					resourceWrapper.getTotalQuantity() != null ? resourceWrapper.getTotalQuantity().toString() : "0",
					resourceWrapper.getRate() != null ? resourceWrapper.getRate().toString() : "0",
					resourceWrapper.getValue() != null ? resourceWrapper.getValue().toString() : "0"
				});
		}
		return record;
	}
	
	private void saveUpdates(){
		Record[] records = dataStore.getModifiedRecords();
		if(records.length == 0){
			MessageBox.alert("No records have been updated");
			return;
		}
		
		List<TransitResource> resources = new ArrayList<TransitResource>(records.length);
		for(Record record : records){
			if(!" ".equals(record.getAsString("bpi")) && !" ".equals(record.getAsString("resourceType")) && !" ".equals(record.getAsString("resourceCode")))
				resources.add(resourceFromRecord(record));
		}
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving Updates", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.saveTransitResources(jobNumber, resources, new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}

			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				if(error == null)
					searchByPage(0);
				else
					MessageBox.alert(error);
			}
		});
	}
	
	private void securitySetup(){
		// Enhancement: 
		// Check for access rights
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(this.globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010604_TRANSIT_VIEW_RESOURCES_WINDOW, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					accessRightsList = accessRightsReturned;
					//Display Save Button for "WRITE" only
					if(accessRightsList!=null && accessRightsList.size()> 0 && accessRightsList.contains("WRITE"))
						showSaveButton = true;
//						saveButton.show();
					else
						showSaveButton = false;
//						saveButton.hide();
					controlSaveBotton();
				}
				
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e,true, globalSectionController.getUser());
					controlSaveBotton();
				}
			});
		}
		catch(Exception e){
			UIUtil.alert(e.getMessage());
		}
	}
	
	private TransitResource resourceFromRecord(Record record){
		TransitResource resource = new TransitResource();
		resource.setId(new Long(record.getAsString("id")));
		resource.setObjectCode(record.getAsString("objectCode"));
		resource.setSubsidiaryCode(record.getAsString("subsidiaryCode"));
		resource.setDescription(record.getAsString("description"));
		return resource;
	}
	
	// added by brian on 20110121
	// apply the code that user inputed to object code or subsidiary code
	@SuppressWarnings("null")
	private boolean applyAll(){
		boolean applied = false;
		Record[] records = this.dataStore.getRecords();
//		dataStore.removeAll();
		if(records != null || records.length > 0){
			String regex = "\\d*";
			String applyTo = "";
			String code = "";
			code = applyCodeField.getText();
			applyTo = applyToComboBox.getText().trim();
			
			if("O".equals(applyTo)){
				if(code.length() != 6){
					MessageBox.alert("Object Code must be 6 digits.");
					return applied;
				}
			}
			else if("S".equals(applyTo)){
				if(code.length() != 8){
					MessageBox.alert("Subsidiary Code must be 8 digits.");
					return applied;
				}
			}
			
			if(code != null && code.matches(regex)){
				for(Record curRecord: records){
					if(!" ".equals(curRecord.getAsString("bpi")) && !" ".equals(curRecord.getAsString("resourceType")) && !" ".equals(curRecord.getAsString("resourceCode"))){
						if("O".equals(applyTo))
							curRecord.set("objectCode", code);
						else if("S".equals(applyTo))
							curRecord.set("subsidiaryCode", code);
					}
				}
				applied = true;
//				dataStore.add(records);
			}
			else
				MessageBox.alert("Please input integer to apply.");
		}
		else
			MessageBox.alert("Please search before apply.");
		
		return applied;
	}
	
	private boolean isTotalLine(TransitResourceWrapper resourceWrapper){
		boolean isTotal = resourceWrapper.getBillNo() == null && 
						  resourceWrapper.getSubBillNo() == null && 
						  resourceWrapper.getItemNo() == null &&
						  "TOTAL:".equals(resourceWrapper.getDescription()) &&
						  resourceWrapper.getValue() != null;
		return isTotal;
	}
	
//	private boolean disbaleSaveButton(PaginationWrapper<TransitResourceWrapper> wrapper){
//		// get the status from transit header
//		String status = "";
//		if(wrapper.getCurrentPageContentList() != null && wrapper.getCurrentPageContentList().size() > 0)
//			status = wrapper.getCurrentPageContentList().get(0).getTransitBQ().getTransitHeader().getStatus();
//		
//		// only disable when transit completed
//		if(TransitHeader.TRANSIT_COMPLETED.equals(status))
//			return true;
//		else
//			return false;
//	}
	
	// added by brian on 20110118
	// disable save button if resource is confirmed, report is printed and transit completed
	private void controlSaveBotton(){
		if(showSaveButtonControlByStatus() && this.showSaveButton)
			saveButton.show();
		else
			saveButton.hide();
	}
	
	private boolean showSaveButtonControlByStatus(){
		String status = "";
		
		if(this.localHeader != null)
			status =  this.localHeader.getStatus();
		
		if(TransitHeader.RESOURCES_CONFIRMED.equals(status) || TransitHeader.REPORT_PRINTED.equals(status) || TransitHeader.TRANSIT_COMPLETED.equals(status))
			return false;
		else
			return true;
	}
}
