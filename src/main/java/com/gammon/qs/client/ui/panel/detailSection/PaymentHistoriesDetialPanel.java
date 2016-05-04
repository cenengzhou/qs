package com.gammon.qs.client.ui.panel.detailSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.MasterListVendor;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.TableLayout;
/**
 * koeyyeung
 * modified on Jun 17, 20137:19:00 PM
 */

public class PaymentHistoriesDetialPanel extends Panel{
	private static final String TABLE_CELL_ALT_ROW_STYLE = "background-color: #dfe8f6";
	private static final String TABLE_CELL_BASE_ROW_STYLE = "background-color: #ffffff";
	
	// Panel
	private Panel tablePanel;
	
	// Label
	private Label subcontractNumberLabel;
	private Label supplierNumberLabel;
	private Label invoiceNumberLabel;
	private Label docNoTypeCoLabel;
	private Label invoiceDateLabel;
	private Label gLdateLabel;
	private Label dueDateLabel;
	private Label paymentCurrencyLabel;
	private Label payStatusLabel;
	private Label grossAmountLabel;
	private Label openAmountLabel;
	private Label foreignPaymentAmountLabel;
	private Label foreignOpenAmountLabel;
	
	// Value
	private Label subcontractNumberValue;
	private Label supplierNumberValue;
	private Label supplierNameValue;
	private Label invoiceNumberValue;
	private Label docNoTypeCoValue;
	private Label invoiceDateValue;
	private Label gLdateValue;
	private Label dueDateValue;
	private Label paymentCurrencyValue;
	private Label payStatusValue;
	private Label grossAmountValue;
	private Label openAmountValue;
	private Label foreignPaymentAmountValue;
	private Label foreignOpenAmountValue;
	
	private String subcontractorName;
	
	//GridPanel
	private GridPanel gridPanel;
	
	// renderer
	private AmountRenderer amountRenderer;
	
	// Record names
	private static final String PAYMENT_AMOUNT_RECORD_NAME = "paymentAmountRecordName";
	private static final String PAYMENT_DATE_RECORD_NAME = "paymentDateRecordName";

	private Store dataStore;
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new FloatFieldDef(PAYMENT_AMOUNT_RECORD_NAME),
					new StringFieldDef(PAYMENT_DATE_RECORD_NAME),
			});
	
	@SuppressWarnings("unused")
	private GlobalMessageTicket globalMessageTicket;

	private GlobalSectionController globalSectionController;
	private APRecord apRecord;
	private List<PaymentHistoriesWrapper> paymentHistoriesList;
	
	public PaymentHistoriesDetialPanel(GlobalSectionController globalSectionController, APRecord apRecord, List<PaymentHistoriesWrapper> paymentHistoriesList) {
		super();
		this.globalSectionController = globalSectionController;
		this.apRecord = apRecord;
		this.paymentHistoriesList = paymentHistoriesList;
		
		globalMessageTicket = new GlobalMessageTicket();
		
		setupUI();
		
	}

	private void setupUI() {
		setBorder(false);
		setAutoScroll(true);
		setCollapsible(true);
		setLayout(new ColumnLayout());

		setupContentPanel();
		setupGridPanel();
		
		populateGrid(paymentHistoriesList);
	}

	private void setupContentPanel() {
		amountRenderer = new AmountRenderer(globalSectionController.getUser());

		tablePanel = new Panel();
		tablePanel.setLayout(new TableLayout(4));
		tablePanel.setBodyBorder(false);
		tablePanel.setCls("table-cell-base-row");
		tablePanel.setCtCls("table-cell-base-row");
		tablePanel.setBodyStyle(TABLE_CELL_BASE_ROW_STYLE);
		
		
		if(apRecord.getSubledger() != null && apRecord.getSubledger().trim().length() > 0)
			subcontractNumberLabel = new Label("Subcontract Number:");
		else
			subcontractNumberLabel = new Label("");
		setLabelStyle(subcontractNumberLabel);
		supplierNumberLabel = new Label("Supplier Number:");
		setLabelStyle(supplierNumberLabel);
		invoiceNumberLabel = new Label("Invoice Number:");
		setLabelStyle(invoiceNumberLabel);
		docNoTypeCoLabel = new Label("Doc No/ Type/ Co:");
		setLabelStyle(docNoTypeCoLabel);
		invoiceDateLabel = new Label("Invoice Date:");
		setLabelStyle(invoiceDateLabel);
		gLdateLabel = new Label("G/L Date:");
		setLabelStyle(gLdateLabel);
		dueDateLabel = new Label("Due Date:");
		setLabelStyle(dueDateLabel);
		paymentCurrencyLabel = new Label("Currency:");
		setLabelStyle(paymentCurrencyLabel);
		payStatusLabel = new Label("Pay Status:");
		setLabelStyle(payStatusLabel);
		grossAmountLabel = new Label("Gross Amount:");
		setLabelStyle(grossAmountLabel);
		openAmountLabel = new Label("Open Amount:");
		setLabelStyle(openAmountLabel);
		foreignPaymentAmountLabel = new Label("Foreign Amount:");
		setLabelStyle(foreignPaymentAmountLabel);
		foreignOpenAmountLabel = new Label("Foreign Open Amount:");
		setLabelStyle(foreignOpenAmountLabel);
		
		
		subcontractorName = "";
		subcontractNumberValue = new Label();
		supplierNumberValue = new Label();
		supplierNameValue = new Label();
		invoiceNumberValue = new Label();
		docNoTypeCoValue = new Label();
		invoiceDateValue = new Label();
		gLdateValue = new Label();
		dueDateValue = new Label();
		paymentCurrencyValue = new Label();
		payStatusValue = new Label();
		grossAmountValue = new Label();
		openAmountValue = new Label();
		foreignPaymentAmountValue = new Label();
		foreignOpenAmountValue = new Label();
		
		setValueStyle(subcontractNumberValue);
		setValueStyle(supplierNumberValue);
		setValueStyle(supplierNameValue);
		setValueStyle(invoiceNumberValue);
		setValueStyle(docNoTypeCoValue);
		setValueStyle(invoiceDateValue);
		setValueStyle(gLdateValue);
		setValueStyle(dueDateValue);
		setValueStyle(paymentCurrencyValue);
		setValueStyle(payStatusValue);
		setValueStyle(grossAmountValue);
		setValueStyle(openAmountValue);
		setValueStyle(foreignPaymentAmountValue);
		setValueStyle(foreignOpenAmountValue);
		
		
		if(apRecord!=null){
			subcontractorName = "";
			subcontractNumberValue.setText(apRecord.getSubledger());
			supplierNumberValue.setText(apRecord.getSupplierNumber().toString());
			supplierNameValue.setText("");
			invoiceNumberValue.setText(preventNullString(apRecord.getInvoiceNumber()));
			docNoTypeCoValue.setText(preventNullString(makeDocNoTypeCoValue(apRecord)));
			invoiceDateValue.setText(date2String(apRecord.getInvoiceDate()));
			gLdateValue.setText(date2String(apRecord.getGlDate()));
			dueDateValue.setText(preventNullString(date2String(apRecord.getDueDate())));
			paymentCurrencyValue.setText(preventNullString(apRecord.getCurrency()));
			payStatusValue.setText(preventNullString(apRecord.getPayStatus()));
			grossAmountValue.setText(preventNullString(apRecord.getGrossAmount()));
			openAmountValue.setText(preventNullString(apRecord.getOpenAmount()));
			foreignPaymentAmountValue.setText(preventNullString(apRecord.getForeignAmount()));
			foreignOpenAmountValue.setText(preventNullString(apRecord.getForeignAmountOpen()));
			

			// get the subcontractor Name from web services
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMasterListRepository().searchVendorList(apRecord.getSupplierNumber().toString(), new AsyncCallback<List<MasterListVendor>>(){

				public void onFailure(Throwable e) {
					e.printStackTrace();
				}

				public void onSuccess(List<MasterListVendor> vendorList) {
					if(vendorList != null && vendorList.size() > 0){
						subcontractorName = vendorList.get(0).getVendorName();
						supplierNameValue.setText(preventNullString(subcontractorName));
					}
				}}
			);
		}
		
		addLabelsAndValues();

		add(tablePanel, new ColumnLayoutData(0.6));
	}

	private void setupGridPanel() {
		gridPanel = new GridPanel();
		gridPanel.setTitle("Payment History");
		gridPanel.setHeight(245);
		gridPanel.setPaddings(0);
		gridPanel.setAutoScroll(true);
		CustomizedGridView gridView = new CustomizedGridView();
		gridView.setForceFit(true);
		gridPanel.setView(gridView);

		ColumnConfig receiptAmountColumnConfig = new ColumnConfig("Payment Amount", PAYMENT_AMOUNT_RECORD_NAME, 150, false);
		receiptAmountColumnConfig.setAlign(TextAlign.RIGHT);
		receiptAmountColumnConfig.setRenderer(amountRenderer);
		ColumnConfig receiptDateColumnConfig = new ColumnConfig("Payment Date", PAYMENT_DATE_RECORD_NAME, 150, false);

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				receiptDateColumnConfig,
				receiptAmountColumnConfig
		};

		gridPanel.setColumnModel(new ColumnModel(columns));

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);

		dataStore.load();
		dataStore.setSortInfo(new SortState(PAYMENT_AMOUNT_RECORD_NAME, SortDir.ASC));
		gridPanel.setStore(dataStore);

		add(gridPanel, new ColumnLayoutData(0.4));
	}
	
	private void populateGrid(List<PaymentHistoriesWrapper> paymentHistoriesList) {
		if (dataStore != null)
			dataStore.removeAll();
		if (paymentHistoriesList == null)
			return;
		Double totalPaymentAmount=0.0;
		for (PaymentHistoriesWrapper paymentHistoriesWrapper : paymentHistoriesList) {
			totalPaymentAmount += paymentHistoriesWrapper.getPaymntAmount();
			Record record = recordDef.createRecord(new Object[] {
					paymentHistoriesWrapper.getPaymntAmount(),
					date2String(paymentHistoriesWrapper.getPaymentDate())
			});
			dataStore.add(record);
		}
		Record record = recordDef.createRecord(new Object[] {
				totalPaymentAmount,
				"Total:"
		});
		dataStore.add(record);
		
		gridPanel.doLayout();
	}
	
	private void addLabelsAndValues() {
		itemPanel(supplierNumberLabel);
		itemPanel(supplierNumberValue);
		itemPanel(supplierNameValue);
		itemPanel(new Label());

		tablePanel.add(invoiceNumberLabel);
		tablePanel.add(invoiceNumberValue);
		tablePanel.add(docNoTypeCoLabel);
		tablePanel.add(docNoTypeCoValue);

		itemPanel(invoiceDateLabel);
		itemPanel(invoiceDateValue);
		itemPanel(gLdateLabel);
		itemPanel(gLdateValue);

		tablePanel.add(dueDateLabel);
		tablePanel.add(dueDateValue);
		tablePanel.add(new Label());
		tablePanel.add(new Label());

		itemPanel(paymentCurrencyLabel);
		itemPanel(paymentCurrencyValue);
		itemPanel(payStatusLabel);
		itemPanel(payStatusValue);

		tablePanel.add(grossAmountLabel);
		tablePanel.add(grossAmountValue);
		tablePanel.add(openAmountLabel);
		tablePanel.add(openAmountValue);

		itemPanel(foreignPaymentAmountLabel);
		foreignPaymentAmountValue.setWidth(165);
		itemPanel(foreignPaymentAmountValue);
		itemPanel(foreignOpenAmountLabel);
		foreignOpenAmountValue.setWidth(160);
		itemPanel(foreignOpenAmountValue);
	}

	private void itemPanel(Label label) {
		Panel itemPanel = new Panel();
		itemPanel.setBodyBorder(false);

		itemPanel.setCls("table-cell-alt-row");
		itemPanel.setCtCls("table-cell-alt-row");
		itemPanel.setBodyStyle(TABLE_CELL_ALT_ROW_STYLE);

		itemPanel.add(label);

		tablePanel.add(itemPanel);
	}
	
	// set the Label
	private Label setLabelStyle(Label label) {
		label.setCtCls("table-cell-Label");
		return label;
	}

	private Label setValueStyle(Label label) {
		label.setCtCls("table-cell-Value-right-align");
		return label;
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	private String makeDocNoTypeCoValue(APRecord record){
		return (preventNullString(record.getDocumentNumber()) + "/ " 
			  + preventNullString(record.getDocumentType()) + "/ " 
			  + preventNullString(record.getCompany()));
	}
	
	private String preventNullString(String input){
		if(input == null)
			return "";
		else
			return input;
	}
	
	private String preventNullString(Integer input){
		if(input == null)
			return "";
		else
			return input.toString();
	}
	
	private String preventNullString(Double input){
		if(input == null)
			return "";
		else
			return this.amountRenderer.render(input.toString());
	}
	
}
