package com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel;


import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.VendorContactPerson;
import com.gammon.qs.domain.VendorPhoneNumber;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * @author koeyyeung
 */
public class SubcontractorInfoDetailPanel extends Panel{
	private static final String TABLE_CELL_BLUE_ROW_STYLE = "background-color: #dfe8f6";
	private static final String TABLE_CELL_WHITE_ROW_STYLE = "background-color: #ffffff";
	
	private FieldSet fieldPanel;
	private Panel basePanel;
	
	private Label nameLabel;
	private Label addressLabel;
	private Label officeLabel;
	private Label faxLabel;
	private Label mobileLabel;
	private Label emailLabel;
	private Label contactPersonLabel;
	
	private Label nameValueLabel;
	private Label addressValueLabel1;
	private Label addressValueLabel2;
	private Label addressValueLabel3;
	private Label addressValueLabel4;
	private Label officeValueLabel;
	private Label faxValueLabel;
	private Label mobileValueLabel;
	private Label emailValueLabel;
	private Label contactPersonValueLabel;
	
	//2nd grid
	private GridPanel secondGridPanel;
	
	private static final String SECOND_GRID_NAME_RECORD_NAME = "secondGridNameRecordName";
	private static final String SECOND_GRID_VALUE_RECORD_NAME = "secondGridValueRecordName";
	
	// records
	private Store secondGridDataStore;
	private final RecordDef secondGridRecordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(SECOND_GRID_NAME_RECORD_NAME),
					new StringFieldDef(SECOND_GRID_VALUE_RECORD_NAME)
					});
	
	
	private String subcontractorNo;
	private MasterListVendor vendor;
	private Boolean requireStatistics;
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	public SubcontractorInfoDetailPanel(GlobalSectionController globalSectionController, String subcontractorNo, MasterListVendor vendor, Boolean requireStatistics) {
		super();
		this.globalSectionController = globalSectionController;
		this.subcontractorNo = subcontractorNo;
		this.vendor = vendor;
		this.requireStatistics = requireStatistics;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		
		setupPanel();
	}

	private void setupPanel() {
		setTitle("Subcontractor Information");
		setIconCls("info-icon");
		setLayout(new HorizontalLayout(10));
		setBorder(false);
		setAutoScroll(true);
		setPaddings(5);
		
		setupFirstPanel();
		if (requireStatistics)
			setupSecondGridPanel();
	}

	private void setupFirstPanel() {
		fieldPanel = new FieldSet();
		fieldPanel.setTitle("Contact Information");
		fieldPanel.setWidth(800);
		
		basePanel = new Panel();
		basePanel.setLayout(new TableLayout(4));
		basePanel.setBorder(false);
		basePanel.setPaddings(5, 5, 0, 0);
		basePanel.setCls("table-cell-base-row");
		basePanel.setCtCls("table-cell-base-row");
		basePanel.setBodyStyle(TABLE_CELL_WHITE_ROW_STYLE);

		nameLabel = new Label("Name");
		addressLabel = new Label("Address");
		officeLabel = new Label("Office");
		faxLabel = new Label("Fax");
		mobileLabel = new Label("Mobile");
		emailLabel = new Label("Email");
		contactPersonLabel = new Label("Contact Person");

		setLabelStyle(nameLabel);
		setLabelStyle(addressLabel);
		setLabelStyle(officeLabel);
		setLabelStyle(faxLabel);
		setLabelStyle(mobileLabel);
		setLabelStyle(emailLabel);
		setLabelStyle(contactPersonLabel);
		
		nameValueLabel = new Label();
		addressValueLabel1 = new Label();
		addressValueLabel2 = new Label();
		addressValueLabel3 = new Label();
		addressValueLabel4 = new Label();
		officeValueLabel = new Label();
		faxValueLabel = new Label();
		mobileValueLabel = new Label();
		emailValueLabel = new Label();
		contactPersonValueLabel = new Label();
		
		setValueStyle(nameValueLabel);
		setValueStyle(addressValueLabel1);
		setValueStyle(addressValueLabel2);
		setValueStyle(addressValueLabel3);
		setValueStyle(addressValueLabel4);
		setValueStyle(officeValueLabel);
		setValueStyle(faxValueLabel);
		setValueStyle(mobileValueLabel);
		setValueStyle(emailValueLabel);
		setValueStyle(contactPersonValueLabel);
		
		addLabelsAndValues();
		populateContentPanel(vendor);

		fieldPanel.add(basePanel);
		add(fieldPanel);
	}

	private void setupSecondGridPanel() {
		secondGridPanel = new GridPanel();
		secondGridPanel.setTitle("Statistics");
		secondGridPanel.setIconCls("stat-icon");
		secondGridPanel.setBorder(true);
		secondGridPanel.setFrame(false);
		secondGridPanel.setPaddings(0);
		secondGridPanel.setAutoScroll(true);
		secondGridPanel.setHeight(180);
		secondGridPanel.setWidth(330);
		secondGridPanel.setView(new CustomizedGridView());
		
		BaseColumnConfig[] columns;
		
		Renderer amountRenderer = new AmountRenderer(globalSectionController.getUser()) {
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value == null)
					return "";
				String result = value.toString();
				if(rowIndex > 1)
					result = render(value.toString());
				return result;
			}
		};	
		
		//Column headers	
		ColumnConfig secondGridNameColConfig = new ColumnConfig("", SECOND_GRID_NAME_RECORD_NAME, 200, false);
		ColumnConfig secondGridValueColConfig = new ColumnConfig("",SECOND_GRID_VALUE_RECORD_NAME,125,false);
		secondGridValueColConfig.setAlign(TextAlign.RIGHT);
		secondGridValueColConfig.setRenderer(amountRenderer);
		
		columns = new BaseColumnConfig[]{ 
				secondGridNameColConfig,
				secondGridValueColConfig
		};
		
		secondGridPanel.setColumnModel(new ColumnModel(columns));
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(secondGridRecordDef);
		secondGridDataStore = new Store(proxy, reader);
		secondGridDataStore.load();
		secondGridPanel.setStore(secondGridDataStore);
		
		add(secondGridPanel);
		populateGridPanel(subcontractorNo);
	}

	

	private void populateGridPanel(String subcontractorNo){
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainSubconctractorStatistics(subcontractorNo, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(List<String> resultList) {
				populateSecondGrid(resultList);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	
	private void populateContentPanel(MasterListVendor vendor) {
		if(vendor==null) return;
		
		nameValueLabel.setText(vendor.getVendorName());
		addressValueLabel1.setText((vendor.getVendorAddress()==null||vendor.getVendorAddress().size()==0)?"":vendor.getVendorAddress().get(0).getAddressLine1()); 
		addressValueLabel2.setText((vendor.getVendorAddress()==null||vendor.getVendorAddress().size()==0)?"":vendor.getVendorAddress().get(0).getAddressLine2()); 
		addressValueLabel3.setText((vendor.getVendorAddress()==null||vendor.getVendorAddress().size()==0)?"":vendor.getVendorAddress().get(0).getAddressLine3()); 
		addressValueLabel4.setText((vendor.getVendorAddress()==null||vendor.getVendorAddress().size()==0)?"":vendor.getVendorAddress().get(0).getAddressLine4()); 
		
		for(VendorPhoneNumber phoneNo: vendor.getVendorPhoneNumber()){
			if(phoneNo.getLineNumberID()==0){
				if("OFF".equalsIgnoreCase(phoneNo.getPhoneNumberType()==null?"":phoneNo.getPhoneNumberType().trim())){
					//office number
					officeValueLabel.setText(phoneNo.getPhoneNumber()==null?"":phoneNo.getPhoneNumber());
				}else if("FAX".equalsIgnoreCase(phoneNo.getPhoneNumberType()==null?"":phoneNo.getPhoneNumberType().trim())){
					//fax number
					faxValueLabel.setText(phoneNo.getPhoneNumber()==null?"":phoneNo.getPhoneNumber());
				}else if("MOB".equalsIgnoreCase(phoneNo.getPhoneNumberType()==null?"":phoneNo.getPhoneNumberType().trim())){
					//mobile number
					mobileValueLabel.setText(phoneNo.getPhoneNumber()==null?"":phoneNo.getPhoneNumber());
				}
			}
		}
		
		//emailValueLabel.setText(); 
 
		//contact person
		int index = 0;
		String contactPerson = "";
		for(VendorContactPerson vendorContactPerson :vendor.getVendorContactPerson()){
			if(vendorContactPerson!=null){
				if(vendorContactPerson!=null && (vendorContactPerson.getNameAlpha()!=null || !"".equals(vendorContactPerson.getNameAlpha()))){
					if(!vendorContactPerson.getNameAlpha().equals(vendor.getVendorName())){
						if (vendorContactPerson.getContactTitle()!=null && vendorContactPerson.getContactTitle().trim().length() > 0) {
							contactPerson = vendorContactPerson.getNameAlpha()+" ("+vendorContactPerson.getContactTitle()+")";
							index++;
							if(index==1)
								contactPersonValueLabel.setText(contactPerson);
							else
								addNewContactPerson(index, contactPerson);
						}else{
							contactPerson = vendorContactPerson.getNameAlpha();
							index++;
							if(index==1)
								contactPersonValueLabel.setText(contactPerson);
							else
								addNewContactPerson(index, contactPerson);
						}
					}
				}
			}
		}
	}

	private void addNewContactPerson(int index, String contactPerson){
		Label contactPersonValueLabel = new Label();
		setValueStyle(contactPersonValueLabel);
		contactPersonValueLabel.setText(contactPerson);
		if(index%2 == 0){
			basePanel.add(new Label());
			basePanel.add(new Label());
			basePanel.add(new Label());
			basePanel.add(contactPersonValueLabel);			
		}
		else{
			colorPanel(new Label());
			colorPanel(new Label());
			colorPanel(new Label());
			colorPanel(contactPersonValueLabel);			
		}
	}
	
	private void populateSecondGrid(List<String> resultList){
		secondGridDataStore.removeAll();
		if(resultList==null || resultList.size()==0) return;
		
		String currentYear = DateUtil.formatDate(new Date(), "yyyy");
		String startYear = "01Jan".concat(String.valueOf(Integer.valueOf(currentYear)-1));
		
		secondGridDataStore.add(secondGridRecordDef.createRecord(new Object[]{"Total No. of Quotation Returned", resultList.get(0)}));
		secondGridDataStore.add(secondGridRecordDef.createRecord(new Object[]{"Total No. of Award", resultList.get(1)}));
		secondGridDataStore.add(secondGridRecordDef.createRecord(new Object[]{"Revised Subcontract Sum </br><i>("+startYear+" - today)</i>", resultList.get(2)}));
		secondGridDataStore.add(secondGridRecordDef.createRecord(new Object[]{"Balance To Complete </br><i>("+startYear+" - today)</i>", resultList.get(3)}));
	}
	
	private void addLabelsAndValues() {
		colorPanel(nameLabel);
		colorPanel(nameValueLabel);
		nameValueLabel.setWidth(240);
		colorPanel(officeLabel);
		colorPanel(officeValueLabel);
		officeValueLabel.setWidth(330);
		
		basePanel.add(addressLabel);
		basePanel.add(addressValueLabel1);
		basePanel.add(faxLabel);
		basePanel.add(faxValueLabel);

		colorPanel(new Label());
		colorPanel(addressValueLabel2);
		colorPanel(mobileLabel);
		colorPanel(mobileValueLabel);

		basePanel.add(new Label());
		basePanel.add(addressValueLabel3);
		basePanel.add(emailLabel);
		basePanel.add(emailValueLabel);

		colorPanel(new Label());
		colorPanel(addressValueLabel4);
		colorPanel(contactPersonLabel);
		colorPanel(contactPersonValueLabel);
	}
	
	private void colorPanel(Label label) {
		Panel colorPanel = new Panel();
		colorPanel.setBodyBorder(false);

		colorPanel.setCls("table-cell-alt-row");
		colorPanel.setCtCls("table-cell-alt-row");
		colorPanel.setBodyStyle(TABLE_CELL_BLUE_ROW_STYLE);

		colorPanel.add(label);

		basePanel.add(colorPanel);
	}
	
	// set the Label
	private Label setLabelStyle(Label label) {
		label.setCtCls("table-cell-Label");
		return label;
	}

	private Label setValueStyle(Label label) {
		label.setCtCls("table-cell-Value-left-align");
		return label;
	}
	
}
