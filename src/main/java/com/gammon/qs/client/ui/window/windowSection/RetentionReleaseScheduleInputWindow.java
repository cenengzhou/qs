package com.gammon.qs.client.ui.window.windowSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.DateRendererWithFormat;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.renderer.RateRendererBoldInTotal;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.mainSection.AddRevisieIPACert;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.RetentionRelease;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.shared.util.CalculationUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
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
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;

public class RetentionReleaseScheduleInputWindow extends Window {
	private static final String MAIN_PANEL_ID ="RetentionReleaseScheduleWindow";
	private static String RR_STATUS_ACTUAL = "Actual";
	private static String RR_STATUS_FORECAST = "Forecast";
	private GlobalSectionController globalSectionController;
	
	private Panel upperPanel;
	private Panel bottomPanel;
	private TextField jobNumberTextField;
	private TextField cumRetentionAmountTextField;
	private EditorGridPanel retentionReleaseGridPanel;
	private double cumRetentionAmount =0.0;
	
	
	private final RecordDef retentionReleaseRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("id"),
					new StringFieldDef("rrSeq"),
					new StringFieldDef("mainCertNo"),
					new DateFieldDef("dueDate","d/m/Y"),
					new DateFieldDef("contractualDueDate","d/m/Y"),
					new StringFieldDef("rrPercentage"),
					new StringFieldDef("rrAmount"),
					new StringFieldDef("status"),
					new ObjectFieldDef("dbObj")
			});
	private AmountRenderer amountRenderer;
	private Integer gridPanelRowIndex=null;
	private Store dataStore;
	private AddRevisieIPACert parentWindow;
	private TextField projectContractValueTextbox;
	private GlobalMessageTicket globalMessageTicket;
	private TextField mainCertMaxRetentionPercentageTextbox;
	
	public RetentionReleaseScheduleInputWindow(GlobalSectionController glsc){
		super();
		globalSectionController=glsc;
		globalMessageTicket = new GlobalMessageTicket();
		setTitle("Update Retention Release Schedule");
		setPaddings(10);
		setWidth(900);
		setHeight(500);
		setClosable(false);
		setFrame(true);
		setModal(true);
		setLayout(new RowLayout());
		setId(MAIN_PANEL_ID);
		upperPanel = new Panel();
		HorizontalLayout upperPanelLayout = new HorizontalLayout(30);
		upperPanelLayout.setColumns(2);
		
		upperPanel.setLayout(upperPanelLayout);
		
		Panel jobNumberPanel = new Panel();
		Label jobNumberLabel = new Label("Job Number   ");
		jobNumberLabel.setCtCls("table-cell");
		jobNumberPanel.add(jobNumberLabel);
		jobNumberTextField = new TextField("");
		jobNumberTextField.setCtCls("table-cell");
		jobNumberTextField.setDisabled(true);
		jobNumberTextField.setWidth(100);
		jobNumberPanel.add(jobNumberTextField);
		
		Panel cumRententionAmtPanel = new Panel();
		cumRententionAmtPanel.setLayout(new HorizontalLayout(10));
		Label cumRententionAmountLabel = new Label("Cumulative Retention Amount ");
		cumRententionAmountLabel.setCtCls("table-cell");
		cumRententionAmtPanel.add(cumRententionAmountLabel);
		cumRetentionAmountTextField = new TextField("Cumulative Retention Amount");
		cumRetentionAmountTextField.setWidth(150);
		cumRetentionAmountTextField.setDisabled(true);
		cumRetentionAmountTextField.setCtCls("table-cell");
		cumRententionAmtPanel.add(cumRetentionAmountTextField);
		
		Panel projectContractValuePanel = new Panel();
		Label projectContractValueLabel = new Label("Projected Contract Value");
		projectContractValueLabel.setCtCls("table-cell");
		projectContractValueTextbox = new TextField("Project Contract Value");
		projectContractValueTextbox.setCtCls("table-cell");
		projectContractValueTextbox.setDisabled(true);
		projectContractValuePanel.add(projectContractValueLabel);
		projectContractValuePanel.add(projectContractValueTextbox);

		Panel mainCertMaxRetentionPercentagePanel = new Panel();
		Label mainCertMaxRetentionPercentageLabel = new Label("Max. Retention %");
		mainCertMaxRetentionPercentageLabel.setCtCls("table-cell");
		mainCertMaxRetentionPercentageTextbox = new TextField("Max Retention %");
		mainCertMaxRetentionPercentageTextbox.setCtCls("table-cell");
		mainCertMaxRetentionPercentageTextbox.setDisabled(true);
		mainCertMaxRetentionPercentagePanel.add(mainCertMaxRetentionPercentageLabel);
		mainCertMaxRetentionPercentagePanel.add(mainCertMaxRetentionPercentageTextbox);
		
		upperPanel.add(jobNumberPanel);
		upperPanel.add(cumRententionAmtPanel);
		upperPanel.add(projectContractValuePanel);
		upperPanel.add(mainCertMaxRetentionPercentagePanel);
		upperPanel.setFrame(true);
		upperPanel.setHeight(100);
		add(upperPanel);
		
		
		bottomPanel = new Panel();
		bottomPanel.setFrame(true);
		bottomPanel.setLayout(new FitLayout());
		
		retentionReleaseGridPanel = new EditorGridPanel();
				
		amountRenderer = new AmountRenderer(globalSectionController.getUser()){

			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if ("Actual".equals(record.getAsString("status"))|| store.getCount()-1==rowIndex)
					return super.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<font color=#0000FF>"+super.render(value, cellMetadata, record, rowIndex, colNum, store)+"</font>";
			}			
		};
		Renderer rateRenderer = new RateRendererBoldInTotal(globalSectionController.getUser()){

			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if ("Actual".equals(record.getAsString("status")) || store.getCount()-1==rowIndex)
					return super.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<font color=#0000FF>"+super.render(value, cellMetadata, record, rowIndex, colNum, store)+"</font>";
			}
		};
		
		ColumnConfig retentionReleaseSeqColConfig = new ColumnConfig("Sequence", "rrSeq", 50, false);
		
		ColumnConfig mainCertColConfig = new ColumnConfig("Certificate Number", "mainCertNo", 80, false);
		mainCertColConfig.setEditor(new GridEditor(new NumberField()));
		
		ColumnConfig dueDateColConfig = new ColumnConfig("Forecast/ Actual Due Date", "dueDate", 100, false);
		DateField dueDateField = new DateField("Due Date", "d/m/Y");
		dueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		dueDateField.setFormat("d/m/Y");
		dueDateField.setAltFormats("dmY");
		dueDateColConfig.setEditor(new GridEditor(dueDateField));
		dueDateColConfig.setRenderer(new DateRendererWithFormat("dd/MM/yyyy"){

			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if ("Actual".equals(record.getAsString("status")) || store.getCount()-1==rowIndex)
					return super.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<font color=#0000FF>"+super.render(value, cellMetadata, record, rowIndex, colNum, store)+"</font>";
			}
			
		});
		
		ColumnConfig contractualDueDateColConfig = new ColumnConfig("Contractual Due Date", "contractualDueDate", 100, false);
		DateField contractualDueDateField = new DateField("Contractual Due Date", "d/m/Y");
		contractualDueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		contractualDueDateField.setFormat("d/m/Y");
		contractualDueDateField.setAltFormats("dmY");
		contractualDueDateColConfig.setEditor(new GridEditor(contractualDueDateField));
		contractualDueDateColConfig.setRenderer(new DateRendererWithFormat("dd/MM/yyyy"){

			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if ("Actual".equals(record.getAsString("status")) || store.getCount()-1==rowIndex)
					return super.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<font color=#0000FF>"+super.render(value, cellMetadata, record, rowIndex, colNum, store)+"</font>";
			}
			
		});
		ColumnConfig retentionReleasePercentageColConfig = new ColumnConfig("Release Percentage", "rrPercentage", 100, false);
		retentionReleasePercentageColConfig.setRenderer(rateRenderer);
		retentionReleasePercentageColConfig.setAlign(TextAlign.RIGHT);
		retentionReleasePercentageColConfig.setEditor(new GridEditor(new NumberField()));
		ColumnConfig retentionReleaseAmountColConfig = new ColumnConfig("Release Amount", "rrAmount", 150, false);
		retentionReleaseAmountColConfig.setRenderer(amountRenderer);
		retentionReleaseAmountColConfig.setEditor(new GridEditor(new TextField()));
		retentionReleaseAmountColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig statusColConfig = new ColumnConfig("Status", "status", 50, true);

		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(retentionReleaseRecordDef);
		dataStore = new Store(proxy,reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState("status", SortDir.ASC));
		retentionReleaseGridPanel.setStore(dataStore);

		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				retentionReleaseSeqColConfig,
				mainCertColConfig,
				contractualDueDateColConfig,
				dueDateColConfig,
				retentionReleasePercentageColConfig,
				retentionReleaseAmountColConfig,
				statusColConfig
		};
		
		retentionReleaseGridPanel.setColumnModel(new ColumnModel(columns));
		
		retentionReleaseGridPanel.addGridCellListener(new GridCellListenerAdapter(){

			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				gridPanelRowIndex = new Integer(rowIndex);
			}
			
		});

		retentionReleaseGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){

			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				gridPanelRowIndex = new Integer(rowIndex);
				if (RR_STATUS_FORECAST.equalsIgnoreCase(record.getAsString("status"))&&
						("rrPercentage".equals(field)||"dueDate".equals(field)||"rrAmount".equals(field)||"contractualDueDate".equals(field)) && 
						rowIndex<dataStore.getRecords().length-1)
					return true;
				return false; 
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if ("rrPercentage".equals(field)){
					if (value==null || "".equals(value.toString().trim())){
						MessageBox.alert("Percentage cannot be empty");
						return false;
					}
					if(Double.parseDouble(value.toString().trim())>100){
						MessageBox.alert("Percentage cannot be larger than 100");
						return false;
					}
				}
				return true;
			}

			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				RetentionRelease rr = (RetentionRelease) record.getAsObject("dbObj");
				if ("dueDate".equals(field))
					rr.setDueDate((Date)newValue);
				if ("contractualDueDate".equals(field))
					rr.setContractualDueDate((Date)newValue);
				if ("rrPercentage".equals(field)){
					rr.setReleasePercent(CalculationUtil.round((Double.parseDouble(newValue.toString())),2));
					rr.setForecastReleaseAmt(CalculationUtil.round(rr.getReleasePercent()/100*cumRetentionAmount,2));
					record.set("rrAmount", rr.getForecastReleaseAmt().toString());
					recalculateRecords();
				}
				if ("rrAmount".equals(field)){
					rr.setForecastReleaseAmt(CalculationUtil.round(CalculationUtil.stringToDouble(newValue.toString()),2));
					if(cumRetentionAmount!=0.00)
						rr.setReleasePercent(CalculationUtil.round(rr.getForecastReleaseAmt()*100/cumRetentionAmount,2));
					record.set("rrPercentage", rr.getReleasePercent().toString());
					recalculateRecords();
				}
					
			}			
		});
		

		final Toolbar rrGridPanelToolbar = new Toolbar();
		
		final ToolbarButton addButton = new ToolbarButton("Add Line");
		addButton.addListener(new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (dataStore.getCount()>0)
					dataStore.insert(dataStore.getCount()-1,retentionReleaseRecordDef.createRecord(new Object[]{"",dataStore.getCount(),"","","","","","Forecast",createNewRR(dataStore.getCount())}));
				else
					dataStore.insert(0,retentionReleaseRecordDef.createRecord(new Object[]{"","1","","","","","","Forecast",createNewRR(1)}));
			}
		});
		
		final ToolbarButton deleteButton = new ToolbarButton("Delete Line"); 
		
		deleteButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (gridPanelRowIndex==null)
					MessageBox.alert("No Record Selected!");
				else if ( gridPanelRowIndex>=(dataStore.getCount()-1))
					MessageBox.alert("Total line cannot be deleted!");
				else if (dataStore.getAt(gridPanelRowIndex.intValue()).getAsString("status").equals(RR_STATUS_ACTUAL))
					MessageBox.alert("Actual Retention Release cannot be deleted!");
				else {
					MessageBox.confirm("Delete Retention Release Schedule","Sequence No:"+dataStore.getAt(gridPanelRowIndex.intValue()).getAsString("rrSeq")+" will be deleted.", new ConfirmCallback(){

						public void execute(String btnID) {
							if ("yes".equalsIgnoreCase(btnID)){
								Record delRecord = dataStore.getAt(gridPanelRowIndex.intValue());
								dataStore.remove(delRecord);
								for (int i=0; i<dataStore.getCount()-1;i++){
									dataStore.getAt(i).set("rrSeq", i+1);
								}
							}
							recalculateRecords();
						}
					});
				};
			}
		});

		retentionReleaseGridPanel.setTopToolbar(rrGridPanelToolbar);
		retentionReleaseGridPanel.enable();
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(true);
		retentionReleaseGridPanel.setView(view);
		
		bottomPanel.add(retentionReleaseGridPanel);
		final Button updateButton = new Button("Save");
		updateButton.addListener(new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SAVING_MSG, true);
				final ArrayList<RetentionRelease> saveList = new ArrayList<RetentionRelease>();
				
				//if (dataStore.getCount()>1){//skip total line
					double totalRetentionRelease = 0; 
					boolean noError=true;
					for (int i = 0;(i<dataStore.getCount()-1)&&noError;i++){
						totalRetentionRelease+=dataStore.getAt(i).getAsDouble("rrAmount");
						if (RR_STATUS_FORECAST.equals(dataStore.getAt(i).getAsString("status"))){
							if (dataStore.getAt(i).getAsDate("contractualDueDate")==null){
								MessageBox.alert("Error","Contractual Due Date is missing in Line "+dataStore.getAt(i).getAsString("rrSeq")+".<br/>No Retention Release Schedule saved.");
								noError  = false;
							}else
							if (dataStore.getAt(i).getAsString("dueDate")==null){
								MessageBox.alert("Error","Due Date is missing in Line "+dataStore.getAt(i).getAsString("rrSeq")+".<br/>No Retention Release Schedule saved.");
								noError  = false;
							}else if (dataStore.getAt(i).getAsString("rrAmount")==null||"".equals(dataStore.getAt(i).getAsString("rrAmount").trim())||CalculationUtil.stringToDouble(dataStore.getAt(i).getAsString("rrAmount"))==0.0){
								MessageBox.alert("Error","Release Amount is zero in Line "+dataStore.getAt(i).getAsString("rrSeq")+".<br/>No Retention Release Schedule saved.");
								noError  = false;
							}
								
						}
						saveList.add((RetentionRelease) dataStore.getAt(i).getAsObject("dbObj"));
					}
					
					totalRetentionRelease = CalculationUtil.round(totalRetentionRelease, 2);
					SessionTimeoutCheck.renewSessionTimer();
					if (!noError){
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}else if (cumRetentionAmount!= totalRetentionRelease){
						MessageBox.alert("Total Release Amount must be equal to Cumulative Retention Amount." +
										"<br>Total Release Amount: "+totalRetentionRelease+
										"<br>Cumulative Retention Amount: "+cumRetentionAmount
										);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}else if (noError)
						globalSectionController.getRetentionReleaseScheduleRepository().updateRetentionRelease(jobNumberTextField.getText(), saveList, new AsyncCallback<Boolean>() {
							public void onSuccess(Boolean updated) {
								if (updated){
									setVisible(false);
									if (parentWindow!=null)
										parentWindow.closeWindows();
									else
										MessageBox.alert("Retention Release Schedule saved!");
									destroy();
								}else {
									MessageBox.alert("No record has been changed.");
								}
								UIUtil.unmaskPanelById(MAIN_PANEL_ID);
							}
							public void onFailure(Throwable e) {
								UIUtil.throwException(e);
								UIUtil.unmaskPanelById(MAIN_PANEL_ID);
							}
						});
				/*}else{
					MessageBox.alert("No Retention Release Schedule saved.");
					if (parentWindow!=null)
						parentWindow.closeWindows();
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					destroy();
				}*/
			}
		});
		final Button closeButton = new Button("Cancel");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (dataStore.getModifiedRecords()!=null && dataStore.getModifiedRecords().length>0 || !checkingRetentionReleaseScheduleBalance())
					MessageBox.confirm("Confirm", "Do you want to discard the changes?", new ConfirmCallback() {
						
						public void execute(String btnID) {
							if ("yes".equalsIgnoreCase(btnID)){
								setVisible(false);
								if (parentWindow!=null)
									parentWindow.closeWindows();
								UIUtil.unmaskMainPanel();
								destroy();								
							}
						}
					});
				else {
					setVisible(false);
					if (parentWindow!=null)
						parentWindow.closeWindows();
					UIUtil.unmaskMainPanel();
					destroy();
				}
			}
			
		});
		addButton.setVisible(false);
		deleteButton.setVisible(false);
		updateButton.setVisible(false);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010405_RETENTION_RELEASE_WINDOW, new AsyncCallback<List<String>>() {
			
			public void onSuccess(List<String> securityList) {
				if (securityList.contains("WRITE")){
					addButton.setVisible(true);
					deleteButton.setVisible(true);
					updateButton.setVisible(true);
				}
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
		});
		rrGridPanelToolbar.addButton(addButton);
		rrGridPanelToolbar.addSeparator();
		rrGridPanelToolbar.addButton(deleteButton);
		bottomPanel.addButton(updateButton);
		bottomPanel.addButton(closeButton);
		add(bottomPanel);

	}
	
	private void recalculateRecords(){
		Double totalPercentage = new Double(0.0);
		Double totalRetentionReleaseAmt = new Double(0.0);
		for (int i = 0;i<dataStore.getRecords().length-1;i++){
			totalPercentage+=Double.parseDouble(dataStore.getRecordAt(i).getAsString("rrPercentage"));
			totalRetentionReleaseAmt+=(!"".equals(dataStore.getRecordAt(i).getAsString("rrAmount")))?Double.parseDouble(dataStore.getRecordAt(i).getAsString("rrAmount")):0.0;
		}
		dataStore.getRecordAt(dataStore.getRecords().length-1).set("rrPercentage", totalPercentage.toString());
		dataStore.getRecordAt(dataStore.getRecords().length-1).set("rrAmount", totalRetentionReleaseAmt.toString());
		
		totalRetentionReleaseAmt = CalculationUtil.round(totalRetentionReleaseAmt, 2);
		
		if (cumRetentionAmount!=0.00 && totalRetentionReleaseAmt!=0.00 && cumRetentionAmount==totalRetentionReleaseAmt){
			beforeCalculatePercentage(totalPercentage);
		}
	}
	
	private void beforeCalculatePercentage(Double totalPercentage){
		double percentDiff=  0.0;
		percentDiff = totalPercentage - 100;

		if(percentDiff!=0.0){
			if(percentDiff > 0.0)
				calculatePercentage(Math.abs(percentDiff), false);
			else if(percentDiff < 0.0)
				calculatePercentage(Math.abs(percentDiff), true);

			totalPercentage = new Double(0.0);
			for (int i = 0;i<dataStore.getRecords().length-1;i++){
				totalPercentage+=Double.parseDouble(dataStore.getRecordAt(i).getAsString("rrPercentage"));
			}
			dataStore.getRecordAt(dataStore.getRecords().length-1).set("rrPercentage", totalPercentage.toString());
		}
	}
	
	private void calculatePercentage(double percentDiff, boolean addition){
		while(percentDiff > 0.0){
			for (Record record: dataStore.getRecords()){
				if("Forecast".equals(record.getAsString("status")) || "Actual".equals(record.getAsString("status"))){
					if(addition){
						if(percentDiff> 0.5){
							record.set("rrPercentage", CalculationUtil.round((Double.parseDouble(record.getAsString("rrPercentage"))+0.5),2));
							percentDiff -=0.5;
						}else{
							record.set("rrPercentage", CalculationUtil.round((Double.parseDouble(record.getAsString("rrPercentage"))+percentDiff),2));
							percentDiff = 0.0;
						}
					}else if(!addition && Double.parseDouble(record.getAsString("rrPercentage"))>1.0){
						if(percentDiff> 0.5){
							record.set("rrPercentage", CalculationUtil.round((Double.parseDouble(record.getAsString("rrPercentage"))-0.5),2));
							percentDiff -=0.5;
						}else{
							record.set("rrPercentage", CalculationUtil.round((Double.parseDouble(record.getAsString("rrPercentage"))-percentDiff),2));
							percentDiff = 0.0;
						}
					}
				}
			}
		}
	}
	
	private RetentionRelease createNewRR(int count) {
		RetentionRelease rr = new RetentionRelease();
		rr.setId(new Long(-1));
		//rr.setSequenceNo(count);
		rr.setJobNumber(jobNumberTextField.getText());
		rr.setActualReleaseAmt(Double.valueOf(0.0));
		rr.setReleasePercent(Double.valueOf(0.0));
		rr.setForecastReleaseAmt(Double.valueOf(0.0));
		rr.setStatus(RetentionRelease.STATUS_FORECAST);
		return rr;
	}
	
	private void loadData(List<RetentionRelease> resultSet){
		dataStore.removeAll();
		Integer seqNo = 1;
		Double totalPercentage = 0.0;
		Double totalRetentionReleaseAmt = new Double(0.0);
		ArrayList<Record> tmpRec = new ArrayList<Record>();
		for (RetentionRelease rr:resultSet){
			tmpRec.add(retentionReleaseRecordDef.createRecord(new Object[]{
					rr.getId(),
					seqNo++,
					rr.getMainCertNo(),
					rr.getDueDate()==null?"":rr.getDueDate(),
					rr.getContractualDueDate()==null?"":rr.getContractualDueDate(),
					"",//rr.getReleasePercent()--> obsolete in database, percentage will be calculated in real time 
					rr.getStatus().trim().equalsIgnoreCase(RetentionRelease.STATUS_ACTUAL)?rr.getActualReleaseAmt():rr.getForecastReleaseAmt(),
					rr.getStatus().trim().equalsIgnoreCase(RetentionRelease.STATUS_ACTUAL)?RR_STATUS_ACTUAL:RR_STATUS_FORECAST,
					rr
			}));
			totalRetentionReleaseAmt +=rr.getStatus().trim().equalsIgnoreCase("A")?rr.getActualReleaseAmt():rr.getForecastReleaseAmt();
		}
		tmpRec.add(retentionReleaseRecordDef.createRecord(new Object[]{
				"",
				"",
				"Total:",
				"",
				"",
				"",
				totalRetentionReleaseAmt,
				"",
				""
		}));
		dataStore.add(tmpRec.toArray(new Record[tmpRec.size()]));
		
		totalRetentionReleaseAmt = CalculationUtil.round(totalRetentionReleaseAmt, 2);
		
		//calculate percentage in real time, won't be stored in database anymore
		for (int i = 0;i<dataStore.getRecords().length;i++){
			if(i!=dataStore.getRecords().length-1){
				if(cumRetentionAmount!=0.0){
					dataStore.getRecordAt(i).set("rrPercentage", CalculationUtil.round(dataStore.getRecordAt(i).getAsDouble("rrAmount")/cumRetentionAmount*100, 2));
					totalPercentage += Double.parseDouble(dataStore.getRecordAt(i).getAsString("rrPercentage"));
				}
			}else{
				dataStore.getRecordAt(i).set("rrPercentage", totalPercentage.toString());
			}
		}
		
		//percentage adjustment
		if (cumRetentionAmount!=0.00 && totalRetentionReleaseAmt!=0.00 && cumRetentionAmount==totalRetentionReleaseAmt){
			beforeCalculatePercentage(totalPercentage);
		}
		
		dataStore.commitChanges();
		UIUtil.unmaskPanelById(getId());
	}
	
	
	
	public void populateGrid(final MainContractCertificate mainCert, AddRevisieIPACert addRevisieIPACert){
		parentWindow = addRevisieIPACert;
		jobNumberTextField.setValue(mainCert.getJobNo());
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().obtainJob(mainCert.getJobNo(), new AsyncCallback<Job>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(Job aJob) {
				projectContractValueTextbox.setValue(aJob.getProjectedContractValue()==null?"":amountRenderer.render(aJob.getProjectedContractValue().toString()));
				mainCertMaxRetentionPercentageTextbox.setValue(aJob.getMaxRetentionPercentage()==null?"":
										new RateRenderer(globalSectionController.getUser()).render(aJob.getMaxRetentionPercentage().toString()));
			}
		});
		cumRetentionAmount = CalculationUtil.round(calRetentionAmount(mainCert), 2);
		cumRetentionAmountTextField.setValue(amountRenderer.render(cumRetentionAmount+""));
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRetentionReleaseScheduleRepository().calculateRetentionReleaseScheduleByJob(mainCert,new AsyncCallback<List<RetentionRelease>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(final List<RetentionRelease> rrList) {
				loadData(rrList);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	private double calRetentionAmount(MainContractCertificate mainCert){
		if (mainCert==null)
			return 0.0;
		return 
			  (mainCert.getCertifiedRetentionforNSCNDSC()==null?0:mainCert.getCertifiedRetentionforNSCNDSC().doubleValue())
			+ (mainCert.getCertifiedMainContractorRetention()==null?0:mainCert.getCertifiedMainContractorRetention().doubleValue())
			+ (mainCert.getCertifiedMOSRetention()==null?0:mainCert.getCertifiedMOSRetention().doubleValue());

	}

	private boolean checkingRetentionReleaseScheduleBalance(){
		double rrTotal = 0.0;
		for (int i= 0; i<dataStore.getRecords().length-1;i++)
			if (dataStore.getAt(i).getAsString("rrAmount")!=null && dataStore.getAt(i).getAsString("rrAmount").trim().length()>0)
				rrTotal += CalculationUtil.stringToDouble(dataStore.getAt(i).getAsString("rrAmount"));
		if (CalculationUtil.round(rrTotal, 2)!=cumRetentionAmount)
			return false;
		return true;
	}
}
