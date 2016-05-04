package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
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

public class BalanceResourcesWindow extends Window {
	public static final String MAIN_PANEL_ID = "balanceResourcesWindow";
	
	private double oldTotalAmount;
	private double oldQty;
	private double newTotalAmount;
	private boolean hasIV;
	
	private RepackagingUpdateByResourceWindow updateByResourceWindow;
	private RepackagingUpdateByBQWindow updateByBqWindow;
	private GlobalSectionController globalSectionController;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private List<String> uneditablePackageNos;
	
	private Button saveButton;
	
	private Panel mainPanel;
	private EditorGridPanel gridPanel;
	
	private Panel bqQuantityPanel;
	private Record bqItemRecord;
	private NumberField originalQuantityField;
	private NumberField remeasuredQuantityField;
	
	private Store dataStore;
	
	private String bpi;
	private double remeasuredFactor;
	
	private String lastEditedId;
	
	private RecordDef resourceRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("id"),
					new StringFieldDef("bpi"),
					new StringFieldDef("packageNo"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("oldQuantity"),
					new StringFieldDef("quantity"), //remeasured
					new StringFieldDef("remeasuredFactor"),
					new StringFieldDef("rate"),
					new StringFieldDef("amount"),
					new StringFieldDef("resourceType"),
					//Reference Only
					new StringFieldDef("postedIVAmount"),
					new StringFieldDef("cumulativeIVAmount"),
					new StringFieldDef("resourceNo")
				}
			);
	
	private ToolbarTextItem oldAmountTextItem;
	private ToolbarTextItem newAmountTextItem;
	private ToolbarTextItem differenceTextItem;
	private NumberFormat numberFormat = NumberFormat.getFormat("#,##0.0###");
	
	public BalanceResourcesWindow(final RepackagingUpdateByResourceWindow updateByResourceWindow){
		super();
		this.updateByResourceWindow = updateByResourceWindow;
		this.globalSectionController = updateByResourceWindow.getGlobalSectionController();
		setupUI();
	}
	
	public BalanceResourcesWindow(final RepackagingUpdateByBQWindow updateByBqWindow, Record bqItemRecord){
		super();
		this.updateByBqWindow = updateByBqWindow;
		this.bqItemRecord = bqItemRecord;
		this.globalSectionController = updateByBqWindow.getGlobalSectionController();
		setupUI();
	}
	
	private void setupUI(){
		this.setModal(true);
		this.setTitle("Balance Resources");
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(550);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		uneditablePackageNos = globalSectionController.getUneditablePackageNos();
		if(uneditablePackageNos == null)
			uneditablePackageNos = new ArrayList<String>();
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		gridPanel = new EditorGridPanel();
		
		if(updateByBqWindow != null){
			bqQuantityPanel = new Panel();
			bqQuantityPanel.setPaddings(3);
			bqQuantityPanel.setFrame(true);
			bqQuantityPanel.setHeight(40);
			bqQuantityPanel.setLayout(new TableLayout(6));
			
			Label originalQuantityLabel = new Label("Original Quantity:");
			originalQuantityLabel.setCls("table-cell");
			originalQuantityField = new NumberField();
			originalQuantityField.setCtCls("table-cell");
			bqQuantityPanel.add(originalQuantityLabel);
			bqQuantityPanel.add(originalQuantityField);
			
			Label remeasuredQuantityLabel = new Label("Remeasured Quantity:");
			remeasuredQuantityLabel.setCls("table-cell");
			remeasuredQuantityField = new NumberField();
			remeasuredQuantityField.setCtCls("table-cell");
			bqQuantityPanel.add(remeasuredQuantityLabel);
			bqQuantityPanel.add(remeasuredQuantityField);
			
			Button recalculateButton = new Button("Recalculate Quantities");
			recalculateButton.setTooltip("Recalculate", "Set the BQ Item remeasured quantity, and recalculate the resource quantities");
			recalculateButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e) {
					if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
						if (hasIV && Double.valueOf(remeasuredQuantityField.getValueAsString())<oldQty){
							MessageBox.alert("New quantity cannot smaller than original quantity if IV Amount is not zero");
							return;
						}
					}
					recalculateQuantities();
					
				};
			});
			bqQuantityPanel.add(new Label(""));
			bqQuantityPanel.add(recalculateButton);
			
			originalQuantityField.setValue(bqItemRecord.getAsString("quantity"));
			originalQuantityField.disable();
			remeasuredQuantityField.setValue(bqItemRecord.getAsString("remeasuredQuantity"));
			
			oldQty = bqItemRecord.getAsDouble("remeasuredQuantity");
			mainPanel.add(bqQuantityPanel);
		}
		
		final Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer editableQuantityRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = quantityRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(4);
		
		//Columns - can only edit quantity and rate
		ColumnConfig bpiColumn = new ColumnConfig("B/P/I", "bpi", 120, false);
		ColumnConfig packageNoColumn = new ColumnConfig("Package No.", "packageNo", 40, false);
		packageNoColumn.setTooltip("Package No.");
		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColumn.setTooltip("Object Code");
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColumn.setTooltip("Subsidiary Code");
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 200, false);
		ColumnConfig unitColumn = new ColumnConfig("Unit", "unit", 40, false);
		ColumnConfig oldQuantityColumn = new ColumnConfig("Orig. Qty", "oldQuantity", 100, false);
		oldQuantityColumn.setRenderer(quantityRenderer);
		oldQuantityColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig quantityColumn = new ColumnConfig("Qty", "quantity", 100, false);
		quantityColumn.setRenderer(editableQuantityRenderer);
		quantityColumn.setAlign(TextAlign.RIGHT);
		quantityColumn.setEditor(new GridEditor(quantNumberField));
		ColumnConfig rateColumn = new ColumnConfig("Rate", "rate", 100, false);
		rateColumn.setRenderer(rateRenderer);
		rateColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig amountColumn = new ColumnConfig("Amount", "amount", 120, false);
		amountColumn.setRenderer(amountRenderer);
		amountColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig resourceTypeColumn = new ColumnConfig("Type", "resourceType", 30, false);
		resourceTypeColumn.setTooltip("Resource Type");
		ColumnConfig postedIVAmountColumn = new ColumnConfig("Posted IV Amount", "postedIVAmount", 120, false);
		postedIVAmountColumn.setRenderer(amountRenderer);
		postedIVAmountColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig currentIVAmountColumn = new ColumnConfig("Cum IV Amount", "cumulativeIVAmount", 120, false);
		currentIVAmountColumn.setRenderer(amountRenderer);
		currentIVAmountColumn.setAlign(TextAlign.RIGHT);
		if ("3".equals(globalSectionController.getJob().getRepackagingType())){
			postedIVAmountColumn.setHidden(false);
			currentIVAmountColumn.setHidden(false);
		}else{
			postedIVAmountColumn.setHidden(true);
			currentIVAmountColumn.setHidden(true);
		}
		
		ColumnConfig[] columns = new ColumnConfig[]{
			bpiColumn,
			packageNoColumn,
			objectCodeColumn,
			subsidiaryCodeColumn,
			descriptionColumn,
			unitColumn,
			oldQuantityColumn,
			quantityColumn,
			rateColumn,
			amountColumn,
			resourceTypeColumn,
			postedIVAmountColumn,
			currentIVAmountColumn
		};
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		gridPanel.setView(view);
		gridPanel.setEnableColumnMove(false);
		
		Toolbar toolbar = new Toolbar();
		toolbar.addFill();
		oldAmountTextItem = new ToolbarTextItem("BQ Item Amount: ");
		newAmountTextItem = new ToolbarTextItem("Resources Amount: ");
		differenceTextItem = new ToolbarTextItem("Difference: ");
		toolbar.addItem(oldAmountTextItem);
		toolbar.addSpacer();
		toolbar.addSeparator();
		toolbar.addSpacer();
		toolbar.addItem(newAmountTextItem);
		toolbar.addSpacer();
		toolbar.addSeparator();
		toolbar.addSpacer();
		toolbar.addItem(differenceTextItem);
		gridPanel.setBottomToolbar(toolbar);
		
		ToolbarButton addResourceButton = new ToolbarButton("Add Resource");
		addResourceButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				addResource();
			};
		});
		gridPanel.setTopToolbar(addResourceButton);
		
		gridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			/**
			 * Remove doBeforeEdit as allowing update Qty if there is IV amount posted in Method 3
			 * @author peterchan
			 * 
			 * @author tikywong
			 * April 18, 2011
			 */
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				uneditablePackageNos = globalSectionController.getUneditablePackageNos();
				String error = null;
				if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) 
						&& record.getAsString("objectCode").startsWith("14"))
					error = "This package is submitted or awarded.";
				
				if(error != null){
					if(record.getId().equals(lastEditedId))
						MessageBox.alert(error);
					else
						lastEditedId = record.getId();
					return false;
				}
				lastEditedId = record.getId();
				return true;
				
				/*if(globalSectionController.getJob().getRepackagingType().equals("3") &&
						(record.isNull("postedIVAmount") || record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
								record.isNull("cumulativeIVAmount") || record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
						MessageBox.alert("Selected resource cannot be balanced - posted IV Amount or cumulative IV Amount are not zero.");
						return false;
					}
				return true;*/
			}
			
			
			
			public void onAfterEdit(GridPanel grid, Record record, String field, 
					Object newValue, Object oldValue, int rowIndex, int colIndex) {
				double quantity = 0;
				if(newValue == null)
					record.set("quantity", "0");
				else
					quantity = Double.parseDouble(newValue.toString());
				double rate = record.getAsDouble("rate");
				double oldAmount = record.getAsDouble("amount");
				double newAmount = quantity * rate;
				newTotalAmount += newAmount - oldAmount;
				refreshAmountTextItems();
				record.set("amount", Double.toString(newAmount));
			}

			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if (globalSectionController.getJob().getRepackagingType().equals("3")){
					if (record.getAsString("postedIVAmount")==null)
						return false;
					if ("".equals(record.getAsString("postedIVAmount").trim()))
						return false;
					if (record.getAsString("cumulativeIVAmount")==null)
						return false;
					if ("".equals(record.getAsString("cumulativeIVAmount").trim()))
						return false;
					double postedIVAmt = record.getAsDouble("postedIVAmount");
					double cumIVAmt = record.getAsDouble("cumulativeIVAmount");
					double costRate = 0.0;
//					double remeasuredFactor = 1.000;
//					if (record.getAsString("remeasuredFactor")!=null&&!"".equals(record.getAsString("remeasuredFactor").trim()))
//						remeasuredFactor = record.getAsDouble("remeasuredFactor"); 
					if (record.getAsString("rate")!=null && !"".equals(record.getAsString("rate").trim()))
						costRate = record.getAsDouble("rate");
					double newQty = 0;
					if (value!=null && !"".equals(value.toString().trim()))
						newQty = Double.parseDouble(value.toString());
					if (RoundingUtil.round(Math.abs(RoundingUtil.round(newQty, 2)*costRate), 2)<RoundingUtil.round(Math.abs(postedIVAmt), 2)){
						MessageBox.alert(	"New Amount: "+RoundingUtil.round(RoundingUtil.round(newQty, 2)*costRate, 2)+" <br>"+
											"New Quantity is too small - New total amount should be larger than Posted IV Amount");
						return false;
					}
					if (RoundingUtil.round(Math.abs(RoundingUtil.round(newQty, 2)*costRate), 2)<RoundingUtil.round(Math.abs(cumIVAmt), 2)){
						MessageBox.alert(	"New Amount: "+RoundingUtil.round(RoundingUtil.round(newQty, 2)*costRate, 2)+" <br>"+
											"New Quantity is too small - New total amount should be larger than Cumulative IV Amount");
						return false;
					}
					if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) 
							&& record.getAsString("objectCode").startsWith("14")){
						MessageBox.alert("This package is submitted or awarded.");
						return false;
					}
				}
				return true;
			}
		});
		
		mainPanel.add(gridPanel);
		this.add(mainPanel);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e) {
					save();
				};
		});		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					if(updateByResourceWindow != null)
						updateByResourceWindow.closeChildWindow();
					else
						updateByBqWindow.closeChildWindow();
				};
		});
		this.setButtons(new Button[]{saveButton, closeWindowButton});
		saveButton.hide();
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010112_BALANCE_RESOURCES_WINDOW, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					try{
						accessRightsList = accessRightsReturned;
						displayButtons();
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);						
					}catch(Exception e){
						UIUtil.alert(e);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}
				}
				
				public void onFailure(Throwable e) {
					UIUtil.alert(e.getMessage());
				}
			});
		}
		catch(Exception e){
			UIUtil.alert(e.getMessage());
		}
	}
	
	private void displayButtons(){
		if(accessRightsList != null && accessRightsList.contains("WRITE"))
			saveButton.show();
	}
	
	private void addResource(){
		if(dataStore.getCount() == 0)
			return;
		Record record = resourceRecordDef.createRecord(new Object[]{
				null,
				bpi,
				"0",
				"140299",
				"29999999",
				"Balance",
				"AM",
				"0",
				"0",
				Double.toString(remeasuredFactor),
				"1.0",
				"0",
				"SC",
				"0",
				"0",
				""
		});
		dataStore.add(record);
		double difference = oldTotalAmount - newTotalAmount;
		if (difference<0){
			difference = difference*-1;
			record.set("rate","-1");
		}
		record.set("quantity", Double.toString(difference));
		record.set("amount", Double.toString(difference));
		newTotalAmount = oldTotalAmount;
		refreshAmountTextItems();
	}
	
	/**
	 * 
	 * @author tikywong
	 * modified on Jul 24, 2012 4:32:50 PM
	 * For equations that involve division, handle it by setting the value to zero
	 */
	private void recalculateQuantities(){
		if(dataStore.getCount() == 0)
			return;
		double originalQty = originalQuantityField.getValue().doubleValue();
		double remeasuredQty = remeasuredQuantityField.getValue().doubleValue();
		double oldFactor = remeasuredFactor;
		remeasuredFactor = remeasuredQty==0 || originalQty==0? 0.0 : (remeasuredQty/originalQty);
		for(Record record : dataStore.getRecords()){
			double quantity = record.getAsDouble("quantity");
			quantity *= (remeasuredFactor==0 || oldFactor==0? 0.0 : (remeasuredFactor / oldFactor));
			double rate = record.getAsDouble("rate");
			record.set("quantity", Double.toString(quantity));
			record.set("remeasuredFactor", Double.toString(remeasuredFactor));
			record.set("amount", Double.toString(quantity * rate));
		}
		oldTotalAmount *= (remeasuredFactor==0 || oldFactor==0? 0.0 : (remeasuredFactor / oldFactor));
		newTotalAmount *= (remeasuredFactor==0 || oldFactor==0? 0.0 : (remeasuredFactor / oldFactor));
		refreshAmountTextItems();
	}
	
	private void refreshAmountTextItems(){
		oldAmountTextItem.setText("<b>BQ Item Amount: " + numberFormat.format(oldTotalAmount) + "</b>");
		newAmountTextItem.setText("<b>Resources Amount: " + numberFormat.format(newTotalAmount) + "</b>");
		differenceTextItem.setText("<b>Difference: " + numberFormat.format(newTotalAmount - oldTotalAmount) + "</b>");
	}
		
	public void populateGrid(List<Resource> resources){
		dataStore.removeAll();
		oldTotalAmount = 0;
		hasIV=false;
		if(resources == null || resources.size() == 0)
			return;
		
		//bpi must be the same for all the resources
		final Resource firstResource = resources.get(0);
		bpi = "";
		bpi += firstResource.getRefBillNo() == null ? "/" : firstResource.getRefBillNo() + "/";
		bpi += firstResource.getRefSubBillNo() == null ? "/" : firstResource.getRefSubBillNo() + "/";
		bpi += firstResource.getRefSectionNo() == null ? "/" : firstResource.getRefSectionNo() + "/";
		bpi += firstResource.getRefPageNo() == null ? "/" : firstResource.getRefPageNo() + "/";
		bpi += firstResource.getRefItemNo() == null ? "" : firstResource.getRefItemNo();
		remeasuredFactor = firstResource.getRemeasuredFactor().doubleValue();
		for(Resource resource : resources){
			Double quantity = resource.getQuantity() * remeasuredFactor;
			Double amount = quantity * resource.getCostRate();
			Record record = resourceRecordDef.createRecord(new Object[]{
					resource.getId(),
					bpi,
					resource.getPackageNo(),
					resource.getObjectCode(),
					resource.getSubsidiaryCode(),
					resource.getDescription(),
					resource.getUnit(),
					quantity,
					quantity,
					Double.toString(remeasuredFactor),
					resource.getCostRate(),
					amount,
					resource.getResourceType(),
					//Reference only
					resource.getIvPostedAmount(),
					resource.getIvCumAmount(),
					resource.getResourceNo()
			});
			oldTotalAmount += amount;
			if (RoundingUtil.round(resource.getIvPostedAmount(),4)!=0 
					||RoundingUtil.round(resource.getIvCumAmount(),4)!=0 )
				hasIV=true;
			dataStore.add(record);
		}
		newTotalAmount = oldTotalAmount;
		refreshAmountTextItems();
		
		if(updateByBqWindow != null)
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getBqRepository().getBQItemListByBPI(globalSectionController.getJob().getJobNumber(), firstResource.getRefBillNo(), 
					firstResource.getRefSubBillNo()==null?"":firstResource.getRefSubBillNo(), 
					firstResource.getRefSectionNo()==null?"":firstResource.getRefSectionNo(),
					firstResource.getRefPageNo()==null?"":firstResource.getRefPageNo(), new AsyncCallback<List<BQItem>>() {
	
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
						}
	
						public void onSuccess(List<BQItem> bqItems) {
							
							for (BQItem bqitem : bqItems)
								if (firstResource.getRefItemNo().equals(bqitem.getItemNo()))
									oldTotalAmount=bqitem.getRemeasuredQuantity()*bqitem.getCostRate();
							recalculateQuantities();
						}
					});		
	}
	
	private void save(){
		gridPanel.stopEditing();
		if(RoundingUtil.round(oldTotalAmount, 4) != RoundingUtil.round(newTotalAmount, 4)){
			MessageBox.alert("Total amounts must match!");
			return;
		}
		Record[] records = dataStore.getRecords();
		if(updateByResourceWindow != null)
			updateByResourceWindow.saveBalancedResources(records);
		else{
			/**
			 * 
			 * @author tikywong
			 * modified on Jul 24, 2012 4:33:50 PM
			 * For equations that involve division, handle it by setting the value to zero
			 */
			double newFactor = remeasuredQuantityField.getValue().doubleValue()==0 || originalQuantityField.getValue().doubleValue()==0? 0 : (remeasuredQuantityField.getValue().doubleValue() / originalQuantityField.getValue().doubleValue());
			if(newFactor != remeasuredFactor){
				MessageBox.alert("The remeasurement quantity has been changed - please press the 'Recalculate Quantities' button");
				return;
			}
			Double remeasuredQuantity = new Double(remeasuredQuantityField.getValue().doubleValue());
			updateByBqWindow.saveBalancedResources(records, remeasuredQuantity);
		}
	}
}
