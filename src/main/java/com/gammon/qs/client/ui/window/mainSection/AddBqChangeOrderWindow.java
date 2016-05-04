package com.gammon.qs.client.ui.window.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class AddBqChangeOrderWindow extends Window {
	private RepackagingUpdateByBQWindow repackagingUpdateByBQWindow;
	
	private ComboBox typeField;
	private TextField billField;
	private TextField subBillField;
	private TextField pageField;
	private TextField itemField;
	private TextField descriptionField;
	private NumberField costRateField;
	private NumberField sellingRateField;
	private NumberField quantityField;
	private ComboBox unitField;
	
	private FormPanel typePanel;  //contains the type field
	private FormPanel otherPanel; //contains every other filed - disabled until the type is chosen
	
	private Store typeStore = new SimpleStore(new String[]{"type", "description"}, new String[][]{
			new String[]{"CC", "CC - Contra Charge"},
			new String[]{"CL", "CL - Claims"},
			new String[]{"DW", "DW - Daywork"},
			new String[]{"OI", "OI - Omitted Items"},
			new String[]{"VO", "VO - Variation Order"}
	});
	
	private BQItem bqItem;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button continueButton;
	
	public AddBqChangeOrderWindow(final RepackagingUpdateByBQWindow repackagingUpdateByBQWindow){
		super();
		this.setModal(true);
		this.setClosable(false);
		this.repackagingUpdateByBQWindow = repackagingUpdateByBQWindow;
		GlobalSectionController globalSectionController = repackagingUpdateByBQWindow.getGlobalSectionController();
		this.setTitle("Create/Edit BQ Item");
		this.setWidth(390);
		
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		Panel mainPanel = new Panel();
		mainPanel.setBorder(false);
		
		//Type
		typePanel = new FormPanel();
		typePanel.setLabelAlign(Position.TOP);
		typePanel.setPaddings(10);
		typeField = new ComboBox("Type", "typeField", 50);
		typeField.setStore(typeStore);
		typeField.setDisplayField("description");
		typeField.setValueField("type");
		typeField.setForceSelection(true);
		typeField.setListWidth(200);
		typeField.addListener(new ComboBoxListenerAdapter(){
			public void onSelect(ComboBox comboBox, Record record, int index) {
				getBillItemFields();
			}
		});
		typePanel.add(typeField);
		
		//Other Fields - in a separate panel, which is disabled until the user selects the type
		otherPanel = new FormPanel();
		otherPanel.setLabelAlign(Position.TOP);
		otherPanel.setPaddings(10, 5, 5, 10);
		
		//bpi
		MultiFieldPanel bpiPanel = new MultiFieldPanel();
		bpiPanel.setBorder(false);
		billField = new TextField("Bill", "billField", 50);
		subBillField = new TextField("Sub-Bill", "subBillField", 50);
		pageField = new TextField("Page", "pageField", 50);
		itemField = new TextField("Item", "itemField", 60);
		bpiPanel.addToRow(billField, 70);
		bpiPanel.addToRow(subBillField, 70);
		bpiPanel.addToRow(pageField, 70);
		bpiPanel.addToRow(itemField, 80);
		
		//description
		descriptionField = new TextField("Description", "descriptionField", 250);
		
		//rates
		MultiFieldPanel ratesPanel = new MultiFieldPanel();
		ratesPanel.setBorder(false);
		costRateField = new NumberField("Cost Rate", "costRateField", 100);
		sellingRateField = new NumberField("Selling Rate", "sellingRateField", 100);
		ratesPanel.addToRow(costRateField, 130);
		ratesPanel.addToRow(sellingRateField, 130);
		
		//Quant and unit
		MultiFieldPanel quantPanel = new MultiFieldPanel();
		quantPanel.setBorder(false);
		quantityField = new NumberField("Quantity", "quantityField", 100);
		unitField = new ComboBox("Unit", "unitField", 50);				
		Store unitStore = globalSectionController.getUnitStore();
		if(unitStore == null)
			unitStore = new SimpleStore(new String[]{"unitCode", "description"}, new String[][]{});
		unitField.setDisplayField("description");
		unitField.setValueField("unitCode");
		unitField.setSelectOnFocus(true);
		unitField.setForceSelection(true);
		unitField.setListWidth(200);
		unitStore.load();
		unitField.setStore(unitStore);
		quantPanel.addToRow(quantityField, 130);
		quantPanel.addToRow(unitField, 80);
		
		otherPanel.add(bpiPanel);
		otherPanel.add(descriptionField);
		otherPanel.add(ratesPanel);
		otherPanel.add(quantPanel);		
		
		mainPanel.add(typePanel);
		mainPanel.add(otherPanel);
		this.add(mainPanel);
		
		continueButton = new Button("Continue (Add/Edit Resources)");
		continueButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				saveChangesAndShowAddResourceWindow();
			};
		});		
			
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					repackagingUpdateByBQWindow.closeChildWindow();		
				};
		});
		
		this.setButtons(new Button[]{continueButton, closeButton});
		continueButton.hide();
		
		//Disable other panel
		otherPanel.setDisabled(true);
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010110_ADD_BQ_CHANGE_ORDER_WINDOW, new AsyncCallback<List<String>>(){
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
			continueButton.show();
	}
	
	private void getBillItemFields(){
		repackagingUpdateByBQWindow.getBillItemFieldsForChangeOrder(typeField.getValue());
	}
	
	public void populateFields(BQItem bqItem){
		otherPanel.setDisabled(false);
		typeField.setDisabled(true);
		this.bqItem = bqItem;
		typeField.setValue(bqItem.getBqType());
		billField.setValue(bqItem.getRefBillNo());
		subBillField.setValue(bqItem.getRefSubBillNo());
		pageField.setValue(bqItem.getRefPageNo());
		itemField.setValue(bqItem.getItemNo());
		descriptionField.setValue(bqItem.getDescription());
		if(bqItem.getCostRate() != null)
			costRateField.setValue(bqItem.getCostRate());
		if(bqItem.getSellingRate() != null)
			sellingRateField.setValue(bqItem.getSellingRate());
		if(bqItem.getQuantity() != null)
			quantityField.setValue(bqItem.getQuantity());
		if(bqItem.getUnit() != null)
			unitField.setValue(bqItem.getUnit());
		
		String bqType = bqItem.getBqType();
		if(!"OI".equals(bqType) || bqItem.getId() != null){
			billField.setDisabled(true);
			subBillField.setDisabled(true);
			pageField.setDisabled(true);
			itemField.setDisabled(true);
		}
		if("CL".equals(bqType) || "VO".equals(bqType)){
			costRateField.setDisabled(true);
		}
	}
	
	private void saveChangesAndShowAddResourceWindow(){
		//Check that all fields have been filled - further validation is done in the service layer
		String errorMsg = "Please fill the following fields:";
		boolean error = false;
		if("OI".equals(bqItem.getBqType())){
			//At least bill and item must be filled
			String billNo = billField.getValueAsString();
			if(billNo == null || billNo.trim().length() == 0){
				errorMsg += "</br>Bill No";
				error = true;
			}
			bqItem.setRefBillNo(billNo);
			bqItem.setRefSubBillNo(subBillField.getValueAsString());
			bqItem.setRefPageNo(pageField.getValueAsString());
			String itemNo = itemField.getValueAsString();
			if(itemNo == null || itemNo.trim().length() == 0){
				errorMsg += "</br>Item No";
				error = true;
			}
			bqItem.setItemNo(itemField.getValueAsString());
		}
		String description = descriptionField.getValueAsString();
		if(description == null || description.trim().length() == 0){
			errorMsg += "</br>Description";
			error = true;
		}
		bqItem.setDescription(description);
		if(costRateField.getValue() == null){
			errorMsg += "</br>Cost Rate";
			error = true;
		}
		else
			bqItem.setCostRate(costRateField.getValue().doubleValue());
		if(sellingRateField.getValue() == null){
			errorMsg += "</br>Selling Rate";
			error = true;
		}
		else
			bqItem.setSellingRate(sellingRateField.getValue().doubleValue());
		if(quantityField.getValue() == null){
			errorMsg += "</br>Quantity";
			error = true;
		}
		else{
			bqItem.setQuantity(quantityField.getValue().doubleValue());
			bqItem.setRemeasuredQuantity(quantityField.getValue().doubleValue());
		}
		if(unitField.getValue() == null){
			errorMsg += "</br>Unit";
			error = true;
		}
		bqItem.setUnit(unitField.getValue());
		
		if(error){
			MessageBox.alert(errorMsg);
			return;
		}
		//If no error, continue (validate, then open add resource window)
		repackagingUpdateByBQWindow.validateBqItemThenAddResources(bqItem);
	}
}
