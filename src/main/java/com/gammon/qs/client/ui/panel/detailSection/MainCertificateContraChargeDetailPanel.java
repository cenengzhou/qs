package com.gammon.qs.client.ui.panel.detailSection;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.MainCertificateGridPanel;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.renderer.NonTotalEditableColRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
/**
 * @author koeyyeung
 * modified on 3 May, 2013
 */
public class MainCertificateContraChargeDetailPanel extends EditorGridPanel{
	private static final String MAIN_PANEL_ID ="mainPanel"; 

	//UI
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;

	//data store
	private Store dataStore;

	private Toolbar bottomToolbar = new Toolbar();

	// cursor navigation
	private ArrowKeyNavigation arrowKeyNavigation;

	private Renderer amountRenderer;

	private Double totalAmount = 0.00;
	private Boolean isAfterDataConvertion;

	private static final String POSTED_AMOUNT_RECORD_NAME = "postedAmountRecordName";
	private static final String CUM_AMOUNT_RECORD_NAME = "cumAmountRecordName";
	private static final String OBJECT_CODE_RECORD_NAME = "objectCodeRecordName";
	private static final String SUBSIDARY_CODE_RECORD_NAME = "subsidiaryCodeRecordName";

	private final RecordDef mainCertContraChargeRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef(POSTED_AMOUNT_RECORD_NAME),
					new StringFieldDef(CUM_AMOUNT_RECORD_NAME),
					new StringFieldDef(OBJECT_CODE_RECORD_NAME),
					new StringFieldDef(SUBSIDARY_CODE_RECORD_NAME)
			});

	private ColumnConfig cumAmountColConfig;
	private ColumnConfig postAmountColConfig;
	private ColumnConfig objectCodeColConfig;
	private ColumnConfig subsidiaryCodeColConfig;

	private String objectCodeInput;
	private String subsidiaryInput;
	
	private List<String> uneditablePackageNos;

	private ToolbarTextItem totalAmountTextItem;
	//private String lastEditedId;

	//private MainCertificateGridPanel mainCertificateGridPanel;
	private MainContractCertificate mainContractCertificate;
	private MainContractCertificate lastMainCert;
	private Integer certNo;
	private MainCertificateContraCharge selectedContraCharge;
	private Integer rowInteger = null;

	private ToolbarButton deleteButton;
	private ToolbarButton addButton;
	private ToolbarButton updatesButton;
	private ToolbarButton saveButton;
	private ToolbarButton cancelButton;

	private List<String> accessRightsList;

	private Double totalPostContraChargeAmt;

	public MainCertificateContraChargeDetailPanel(GlobalSectionController globalSectionController, MainCertificateGridPanel mainCertificateGridPanel, MainContractCertificate mainCert) {
		super();

		this.globalSectionController = globalSectionController;
		//this.mainCertificateGridPanel = mainCertificateGridPanel;
		mainContractCertificate = mainCert;
		certNo = mainCert.getCertificateNumber();
		lastMainCert = mainCertificateGridPanel.getLastMainCert();
		globalMessageTicket = new GlobalMessageTicket();
		isAfterDataConvertion = false;

		setId(MAIN_PANEL_ID);

		setupGridPanel();
	}

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		totalAmountTextItem = new ToolbarTextItem("<b>Posted Certified Contra Charge Amount: 0.0"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"Total Cumulative Amount: 0.0 </b>");
		bottomToolbar.addItem(totalAmountTextItem);
		
		deleteButton = new ToolbarButton();
		deleteButton.setText("Delete");
		deleteButton.setIconCls("remove-button-icon");
		deleteButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(rowInteger==null)
					MessageBox.alert("No Contra Charge is selected");
				else{
					MessageBox.confirm("Delete Contra Charge", "Are you sure you want to delete this Contra Charge?<br/>",
							new MessageBox.ConfirmCallback() {
						public void execute(String btnID) {
							if (btnID.equals("yes")) {
								SessionTimeoutCheck.renewSessionTimer();
								globalSectionController.getMainContractCertificateRepository().deleteMainCertContraCharge(selectedContraCharge, new AsyncCallback<Boolean>(){
									public void onFailure(Throwable e) {
										UIUtil.alert("Failed to delete Contra Charge, "+e.getLocalizedMessage());
									}
									public void onSuccess(Boolean result) {
										if(result){ 
											dataStore.commitChanges();
											globalSectionController.getTreeSectionController().populateMainCertificate();
											MessageBox.alert("Contra Charge deleted.");
											populateGrid();
										}else
											MessageBox.alert("Failed to delete contra charge.");
									}
								});
							}
						}
					});
				}
			}
		});
		ToolTip mergeToolTip = new ToolTip();
		mergeToolTip.setTitle("Delete");
		mergeToolTip.setHtml("Delete the selected Contra Charge");
		mergeToolTip.applyTo(deleteButton);
		
		addButton = new ToolbarButton();
		addButton.setText("Add");
		addButton.setIconCls("add-button-icon");
		addButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				addNewContraChargeRow();
			}
		});

		ToolTip addToolTip = new ToolTip();
		addToolTip.setTitle("Add");
		addToolTip.setHtml("Create a Contra Charge");
		addToolTip.applyTo(addButton);
	
		saveButton = new ToolbarButton();
		saveButton.setText("Save");
		saveButton.setIconCls("save-button-icon");
		saveButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				createNewAddedContraCharge();
				globalSectionController.getTreeSectionController().populateMainCertificate();
				
			}
		});		
		
		cancelButton = new ToolbarButton();
		cancelButton.setText("Cancel");
		cancelButton.setIconCls("cancel-icon");
		cancelButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				populateGrid();

				cancelButton.setVisible(false);
				saveButton.setVisible(false);
				
				addButton.enable();
				deleteButton.enable();
				updatesButton.enable();
			}
		});		
	
		updatesButton = new ToolbarButton("Update");
		updatesButton.setIconCls("save-button-icon");
		updatesButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(!isAfterDataConvertion || checkValidation()){
					saveContraCharge(dataStore.getModifiedRecords());
				}
			};
		});

		addButton.setVisible(false);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
		deleteButton.setVisible(false);
		updatesButton.setVisible(false);
		
		toolbar.addButton(addButton);
		toolbar.addSeparator();
		toolbar.addButton(deleteButton);
		toolbar.addSeparator();
		toolbar.addButton(updatesButton);
		toolbar.addSeparator();
		toolbar.addButton(saveButton);
		toolbar.addSeparator();
		toolbar.addButton(cancelButton);

	
		setTopToolbar(toolbar);
		setBottomToolbar(bottomToolbar);
	}

	private void setupGridPanel() {
		amountRenderer = new AmountRendererNonTotal(globalSectionController.getUser());

		arrowKeyNavigation = new ArrowKeyNavigation(this);
		setHeight(400);

		setupToolbar();

		cumAmountColConfig = new ColumnConfig("Cumulative Amount", CUM_AMOUNT_RECORD_NAME, 200, false);
		Field cumAmountField = FieldFactory.createNegativeNumberField(3);
		cumAmountField.addListener(this.arrowKeyNavigation);
		cumAmountColConfig.setEditor(new GridEditor(cumAmountField));
		cumAmountColConfig.setRenderer(new NonTotalEditableColRenderer(amountRenderer));
		cumAmountColConfig.setAlign(TextAlign.RIGHT);

		postAmountColConfig = new ColumnConfig("Posted Amount", POSTED_AMOUNT_RECORD_NAME, 150, false);
		postAmountColConfig.setAlign(TextAlign.RIGHT);

		objectCodeColConfig = new ColumnConfig("Object Code", OBJECT_CODE_RECORD_NAME, 150, false);
		subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", SUBSIDARY_CODE_RECORD_NAME, 150, false);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(mainCertContraChargeRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState(OBJECT_CODE_RECORD_NAME, SortDir.ASC));
		
		setStore(dataStore);
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				postAmountColConfig,
				cumAmountColConfig
		};

		setColumnModel(new ColumnModel(columns));

		//Validation - check postedIVAmount and whether package status > 100 (packageNo in uneditablePackageNos)
		addEditorGridListener(new EditorGridListenerAdapter(){

			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				boolean isEditable = true;
				if(field.equals(POSTED_AMOUNT_RECORD_NAME)&& !isAfterDataConvertion)
					isEditable =  false;
				else{
					isEditable = true;
				}
				if (isEditable) {
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				}else{
					arrowKeyNavigation.resetState();
				}
				return isEditable;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if(field.equals(OBJECT_CODE_RECORD_NAME)){
					if (value == null || ((String)value).length() != 6){
						MessageBox.alert("Object code must be 6 digits in length");
						return false;
					}else{
						objectCodeInput = value.toString();
					}	
				}
				if(field.equals(SUBSIDARY_CODE_RECORD_NAME)){
					if(value == null || ((String)value).length() != 8){
						MessageBox.alert("Subsidiary code must be 8 digits in length");
						return false;
					}else{
						subsidiaryInput = value.toString();
					}
				}
				return true;
			}
		});

		// Check for access rights - then add toolbar buttons accordingly
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010404_MAIN_CERTIFICATE_CONTRA_CHARGE_DETAILPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;

				if(new Integer(mainContractCertificate.getCertificateStatus())<150){
					securitySetup();
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});

		uneditablePackageNos = globalSectionController.getUneditablePackageNos();
		if(uneditablePackageNos == null)
			uneditablePackageNos = new ArrayList<String>();

		addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				if(rowInteger== null || rowInteger.intValue()!= rowIndex){
					rowInteger= new Integer(rowIndex);

					Record curRecord = dataStore.getAt(rowIndex);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getMainContractCertificateRepository().getMainCertContraCharge(curRecord.getAsString(OBJECT_CODE_RECORD_NAME).toString(), curRecord.getAsString(SUBSIDARY_CODE_RECORD_NAME).toString(), mainContractCertificate, new AsyncCallback<MainCertificateContraCharge>(){

						public void onFailure(Throwable e) {
							UIUtil.alert("Failed to get Contra Charge, "+e.getLocalizedMessage());
						}

						public void onSuccess(MainCertificateContraCharge result) {
							selectedContraCharge = result;

						}	
					});
				}
			}
		});
		
		setView(new CustomizedGridView());
		
		checkIsAfterDataConvertion();
		populateGrid();
	}

	public void populateGrid(){
		if(isAfterDataConvertion){
			Field postAmountField = FieldFactory.createNegativeNumberField(3);
			postAmountColConfig.setEditor(new GridEditor(postAmountField));
			postAmountColConfig.setRenderer(new NonTotalEditableColRenderer(amountRenderer));
		}else{
			postAmountColConfig.setRenderer(amountRenderer);}
		dataStore.removeAll();

		setContraChargeList();

	}

	public void setContraChargeList(){
		SessionTimeoutCheck.renewSessionTimer();
		UIUtil.maskMainPanel();
		globalSectionController.getMainContractCertificateRepository().getMainCertCertContraChargeList(mainContractCertificate, new AsyncCallback<List<MainCertificateContraCharge>>(){

			public void onFailure(Throwable arg0) {
				MessageBox.alert("Fail to get the Contra Charge List./n"+arg0);
			}

			public void onSuccess(List<MainCertificateContraCharge> result) {
				globalSectionController.getMainContractCertificateRepository().deleteMainCertContraCharge(mainContractCertificate, new AsyncCallback<Integer>(){

					@Override
					public void onFailure(Throwable e) {
						UIUtil.unmaskMainPanel();
						UIUtil.throwException(e);						
					}

					@Override
					public void onSuccess(Integer result) {
						UIUtil.unmaskMainPanel();	
					}
					
				});
				for(MainCertificateContraCharge mainCertificateContraCharge : result){
					mainCertificateContraCharge.setMainCertificate(mainContractCertificate);
				}
				if (result != null){
					//int rows = mainContractCertificate.getContraChargeList().size();
					calTotalContraCharge();

					Integer counter = 0;
					//totalCumAmount = 0.0;
					for (MainCertificateContraCharge currContraCharge : result){
						counter = counter + 1;
 
						Double cumAmount = currContraCharge.getCurrentAmount();
						//totalCumAmount = totalCumAmount + cumAmount;
						
						Double postAmount = currContraCharge.getPostAmount();
						String curObjectCode = currContraCharge.getObjectCode();
						String curSubsidiaryCode = currContraCharge.getSubsidiary();
						Record record = mainCertContraChargeRecordDef.createRecord(new Object[]{
								postAmount,
								cumAmount,
								curObjectCode,
								curSubsidiaryCode
						});
						dataStore.add(record);
					}
				}
			}
		});
	}

	private void securitySetup(){
		if(accessRightsList.contains("WRITE")){
			addButton.setVisible(true);
			deleteButton.setVisible(true);
			updatesButton.setVisible(true);
		}
	}

	private void calTotalContraCharge(){
		AmountRenderer amountRenderer = new AmountRenderer(globalSectionController.getUser());
		totalAmount = 0.00;
		UIUtil.maskMainPanel();
		globalSectionController.getMainContractCertificateRepository().getMainCertCertContraChargeList(mainContractCertificate, new AsyncCallback<List<MainCertificateContraCharge>>(){

			@Override
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.throwException(e);
			}

			@Override
			public void onSuccess(List<MainCertificateContraCharge> result) {
				for (MainCertificateContraCharge curContraCharge: result){
					totalAmount = totalAmount + curContraCharge.getCurrentAmount();
				}		
				UIUtil.unmaskMainPanel();
			}
			
		});
		totalAmountTextItem.setText("<b>Posted Certified Contra Charge Amount: "+amountRenderer.render(lastMainCert.getCertifiedContraChargeAmount().toString())
									+"&nbsp;&nbsp;&nbsp;&nbsp;"
									+"Total Cumulative Amount: "+amountRenderer.render(totalAmount.toString())+"</b>");
		
	}
	
	private void checkIsAfterDataConvertion(){
		UIUtil.maskMainPanel();
		globalSectionController.getMainContractCertificateRepository().getTotalPostContraChargeAmt(mainContractCertificate, new AsyncCallback<Double>(){

			@Override
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.throwException(e);
			}

			@Override
			public void onSuccess(Double result) {
				totalPostContraChargeAmt = result;
				UIUtil.unmaskMainPanel();
			}
			
		});
		if(certNo>1 && !totalPostContraChargeAmt.equals(lastMainCert.getCertifiedContraChargeAmount()))
			isAfterDataConvertion = Boolean.TRUE;
	}

	public void saveContraCharge(Record[] records){
		List<MainCertificateContraCharge> contraChargeList = new ArrayList<MainCertificateContraCharge>();
		for(Record record : records){
			contraChargeList.add(contraChargeFromRecord(record));
		}
		if(contraChargeList.size() == 0){
			MessageBox.alert("No records changed");
			return;
		}
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving Updates", true);
		SessionTimeoutCheck.renewSessionTimer();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().updateMainCertContraChargeList(contraChargeList,mainContractCertificate, new AsyncCallback<String>(){
			public void onSuccess(String result) {
				if("".equals(result)){
					MessageBox.alert("Records Saved");
					globalSectionController.getTreeSectionController().populateMainCertificate();
					isAfterDataConvertion = Boolean.FALSE;
					populateGrid();

				}else{
					UIUtil.alert("Update Fail. Error: "+result);
				}
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}				
		});
	}

	public MainCertificateContraCharge contraChargeFromRecord(Record record){
		MainCertificateContraCharge contraCharge = new MainCertificateContraCharge();
		if(record.getAsString(CUM_AMOUNT_RECORD_NAME)!=null && !record.getAsString(CUM_AMOUNT_RECORD_NAME).trim().equals(""))
			contraCharge.setCurrentAmount(new Double (record.getAsString(CUM_AMOUNT_RECORD_NAME)));
		else
			contraCharge.setCurrentAmount(0.00);
		contraCharge.setObjectCode(record.getAsString(OBJECT_CODE_RECORD_NAME));
		contraCharge.setSubsidiary(record.getAsString(SUBSIDARY_CODE_RECORD_NAME));
		if(record.getAsString(POSTED_AMOUNT_RECORD_NAME)!=null && !record.getAsString(POSTED_AMOUNT_RECORD_NAME).trim().equals(""))
			contraCharge.setPostAmount(new Double (record.getAsString(POSTED_AMOUNT_RECORD_NAME)));
		else
			contraCharge.setPostAmount(0.00);
		contraCharge.setMainCertificate(mainContractCertificate);
		contraCharge.setCreatedDate(new Date());
		contraCharge.setCreatedUser(globalSectionController.getUser().getUsername());
		contraCharge.setLastModifiedDate(new Date());
		contraCharge.setLastModifiedUser(globalSectionController.getUser().getUsername());
		return contraCharge;
	}

	private Boolean checkValidation(){
		if(certNo>1){
			Double newEditedPosttotal = 0.00;
			if(dataStore.getRecords()!=null && dataStore.getRecords().length>0){
				for(Record record: dataStore.getRecords()){
					newEditedPosttotal = newEditedPosttotal + record.getAsDouble(POSTED_AMOUNT_RECORD_NAME);
				}
			}

			if(RoundingUtil.round(lastMainCert.getCertifiedContraChargeAmount(), 2) == RoundingUtil.round(newEditedPosttotal, 2)){
				return Boolean.TRUE;
			}else{
				MessageBox.alert("The total contra charge post amount " +
						"does not match with the last contra charge amount of the last main Cert.<br/><br/>"+
						" Total Contra Charge Post Amount: " + lastMainCert.getCertifiedContraChargeAmount()+"		Contra Charge Amout of last main cert: " + newEditedPosttotal);
				return Boolean.FALSE;
			}
		}else
			return Boolean.TRUE;
	}

	public void addNewContraChargeRow(){
		Record record = mainCertContraChargeRecordDef.createRecord(new Object[]{
				"0.00",
				"0.00",
				"",
				""
		});
		dataStore.add(record);

		addButton.disable();
		deleteButton.disable();
		updatesButton.disable();
		saveButton.setVisible(true);
		cancelButton.setVisible(true);
		
		objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		cumAmountColConfig.setEditor(new GridEditor(new NumberField()));
		//cumAmountColConfig.setRenderer(amountRenderer);
		cumAmountColConfig.setAlign(TextAlign.RIGHT);
		postAmountColConfig.setAlign(TextAlign.RIGHT);
		if(isAfterDataConvertion){
			postAmountColConfig.setEditor(new GridEditor(new NumberField()));
		}

	}

	private void createNewAddedContraCharge(){
		SessionTimeoutCheck.renewSessionTimer();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainCertContraCharge(objectCodeInput, subsidiaryInput, mainContractCertificate, new AsyncCallback<MainCertificateContraCharge>(){

			public void onFailure(Throwable arg0) {
				MessageBox.alert("Add Contra Charge failed/n" +  arg0);
			}

			public void onSuccess(MainCertificateContraCharge result) {
				if(result!=null){
					MessageBox.alert("This Contra Charge is already existed");
				}else{
					addContraCharge(dataStore.getAt(dataStore.getCount()-1));
				}
			}
		});
	}

	public void addContraCharge(final Record record){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving...", true);
		MainCertificateContraCharge contraCharge = new MainCertificateContraCharge();
		contraCharge = contraChargeFromRecord(record);
		SessionTimeoutCheck.renewSessionTimer();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().addMainCertContraCharge(contraCharge, new AsyncCallback<String>(){
			public void onSuccess(String result) {
				if("".equals(result.trim())){
					MessageBox.alert("Contra charge is added successfully.");
					record.commit();

					dataStore.removeAll();
					
					globalSectionController.getTreeSectionController().populateMainCertificate();
					
					/*saveButton.setVisible(false);
					cancelButton.setVisible(false);
					
					addButton.enable();
					deleteButton.enable();
					updatesButton.enable();*/
				}
				else{
					MessageBox.alert(result);
				}
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.alert(e);
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});

	}

}
