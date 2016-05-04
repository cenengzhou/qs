package com.gammon.qs.client.ui.panel.detailSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
 * @author koeyyeung
 * @modified on 29 April, 2013
 */
public class AccountReceivableDetailPanel extends Panel {
	// UI
	private GlobalSectionController globalSectionController;

	// Panel
	private Panel tablePanel;

	// Label
	private Label customerNumberLabel;
	private Label docNoTypeCoLabel;
	private Label referenceLabel;
	private Label invoiceDateLabel;
	private Label gLDateLabel;
	private Label dueDateLabel;
	private Label dateClosedLabel;
	private Label currencyLabel;
	private Label grossAmountLabel;
	private Label openAmountLabel;
	private Label foreignAmountLabel;
	private Label foreignOpenAmountLabel;
	private Label payStatusLabel;
	private Label remarksLabel;
	private Label descriptionLabel;

	// Value
	private Label customerNumberValue;
	private Label docNoTypeCoValue;
	private Label referenceValue;
	private Label invoiceDateValue;
	private Label gLDateValue;
	private Label dueDateValue;
	private Label dateClosedValue;
	private Label currencyValue;
	private Label grossAmountValue;
	private Label openAmountValue;
	private Label foreignAmountValue;
	private Label foreignOpenAmountValue;
	private Label payStatusValue;
	private Label remarksValue;
	private Label descriptionValue;
	private Label customerNameValue;

	private String customerName = "";

	private static final String TABLE_CELL_ALT_ROW_STYLE = "background-color: #dfe8f6";
	private static final String TABLE_CELL_BASE_ROW_STYLE = "background-color: #ffffff";

	// Record names
	private static final String RECEIPT_AMOUNT_RECORD_NAME = "receiptAmountRecordName";
	private static final String RECEIPT_DATE_RECORD_NAME = "receiptDateRecordName";

	private Store dataStore;
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new FloatFieldDef(RECEIPT_AMOUNT_RECORD_NAME),
					new StringFieldDef(RECEIPT_DATE_RECORD_NAME),
			});

	// renderer
	private AmountRenderer amountRenderer;

	private ARRecord arRecord;
	private List<MainCertReceiveDateWrapper> mainCertReceiveDateWrapperList;

	private GridPanel gridPanel;

	// constructor
	public AccountReceivableDetailPanel(GlobalSectionController globalSectionController, ARRecord arRecord, List<MainCertReceiveDateWrapper> mainCertReceiveDateWrapperList) {
		super();
		this.globalSectionController = globalSectionController;
		this.arRecord = arRecord;
		this.mainCertReceiveDateWrapperList = mainCertReceiveDateWrapperList;
		setupUI();
	}

	private void setupUI() {
		setBorder(false);
		setAutoScroll(true);
		setCollapsible(true);
		setLayout(new ColumnLayout());

		setupContentPanel();
		setupGridPanel();
		populateGrid(mainCertReceiveDateWrapperList);
	}

	private void setupContentPanel() {
		amountRenderer = new AmountRenderer(globalSectionController.getUser());

		tablePanel = new Panel();
		tablePanel.setLayout(new TableLayout(4));
		tablePanel.setBodyBorder(false);
		tablePanel.setCls("table-cell-base-row");
		tablePanel.setCtCls("table-cell-base-row");
		tablePanel.setBodyStyle(TABLE_CELL_BASE_ROW_STYLE);

		customerNumberLabel = new Label("Customer Number:");
		setLabelStyle(customerNumberLabel);
		docNoTypeCoLabel = new Label("Doc No/ Type/ Co");
		setLabelStyle(docNoTypeCoLabel);
		referenceLabel = new Label("Reference:");
		setLabelStyle(referenceLabel);
		invoiceDateLabel = new Label("Invoice Date:");
		setLabelStyle(invoiceDateLabel);
		gLDateLabel = new Label("G/L Date:");
		setLabelStyle(gLDateLabel);
		dueDateLabel = new Label("Due Date:");
		setLabelStyle(dueDateLabel);
		dateClosedLabel = new Label("Date Closed:");
		setLabelStyle(dateClosedLabel);
		currencyLabel = new Label("Currency:");
		setLabelStyle(currencyLabel);
		grossAmountLabel = new Label("Gross Amount:");
		setLabelStyle(grossAmountLabel);
		openAmountLabel = new Label("Open Amount:");
		setLabelStyle(openAmountLabel);
		foreignAmountLabel = new Label("Foreign Amount:");
		setLabelStyle(foreignAmountLabel);
		foreignOpenAmountLabel = new Label("Foreign Open Amount:");
		setLabelStyle(foreignOpenAmountLabel);
		payStatusLabel = new Label("Pay Status:");
		setLabelStyle(payStatusLabel);
		remarksLabel = new Label("Remark:");
		setLabelStyle(remarksLabel);
		descriptionLabel = new Label("Description:");
		setLabelStyle(descriptionLabel);

		
		customerNumberValue = new Label();
		docNoTypeCoValue = new Label();
		referenceValue = new Label();
		invoiceDateValue = new Label();
		gLDateValue = new Label();
		dueDateValue = new Label();
		dateClosedValue = new Label();
		currencyValue = new Label();
		grossAmountValue = new Label();
		openAmountValue = new Label();
		foreignAmountValue = new Label();
		foreignOpenAmountValue = new Label();
		payStatusValue = new Label();
		remarksValue = new Label();
		descriptionValue = new Label();
		customerNameValue = new Label();
		
		setValueStyle(customerNumberValue);
		setValueStyle(docNoTypeCoValue);
		setValueStyle(referenceValue);
		setValueStyle(invoiceDateValue);
		setValueStyle(gLDateValue);
		setValueStyle(dueDateValue);
		setValueStyle(dateClosedValue);
		setValueStyle(currencyValue);
		setValueStyle(grossAmountValue);
		setValueStyle(openAmountValue);
		setValueStyle(foreignAmountValue);
		setValueStyle(foreignOpenAmountValue);
		setValueStyle(payStatusValue);
		setValueStyle(remarksValue);
		setValueStyle(descriptionValue);
		setValueStyle(customerNameValue);
		
		if (arRecord != null) {
			customerNumberValue.setText(preventNullString(arRecord.getCustomerNumber().toString()));
			docNoTypeCoValue.setText(makeDocNoTypeCoValue(arRecord));
			referenceValue.setText(preventNullString(arRecord.getReference()));
			invoiceDateValue.setText(date2String(arRecord.getInvoiceDate()));
			gLDateValue.setText(date2String(arRecord.getGlDate()));
			dueDateValue.setText(date2String(arRecord.getDueDate()));
			dateClosedValue.setText(date2String(arRecord.getDateClosed()));
			currencyValue.setText(preventNullString(arRecord.getCurrency()));
			grossAmountValue.setText(preventNullString(arRecord.getGrossAmount()));
			openAmountValue.setText(preventNullString(arRecord.getOpenAmount()));
			foreignAmountValue.setText(preventNullString(arRecord.getForeignAmount()));
			foreignOpenAmountValue.setText(preventNullString(arRecord.getForeignOpenAmount()));
			payStatusValue.setText(preventNullString(arRecord.getPayStatus()));
			remarksValue.setText(preventNullString(arRecord.getRemark()));
			descriptionValue.setText(preventNullString(arRecord.getCustomerDescription()));

			// get the customer Name from web services
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMasterListRepository().searchVendorList(arRecord.getCustomerNumber().toString(), new AsyncCallback<List<MasterListVendor>>() {
				public void onFailure(Throwable e) {
					e.printStackTrace();
				}

				public void onSuccess(List<MasterListVendor> vendorList) {
					if (vendorList != null && vendorList.size() > 0) {
						customerName = vendorList.get(0).getVendorName();
						customerNameValue.setText(customerName);
					}
				}
			});
		}

		addLabelsAndValues();

		add(tablePanel, new ColumnLayoutData(0.6));
	}

	private void setupGridPanel() {
		gridPanel = new GridPanel();
		gridPanel.setTitle("Receipt History");
		gridPanel.setHeight(245);
		gridPanel.setPaddings(0);
		gridPanel.setAutoScroll(true);
		CustomizedGridView gridView = new CustomizedGridView();
		gridView.setForceFit(true);
		gridPanel.setView(gridView);

		ColumnConfig receiptAmountColumnConfig = new ColumnConfig("Receipt Amount", RECEIPT_AMOUNT_RECORD_NAME, 150, false);
		receiptAmountColumnConfig.setAlign(TextAlign.RIGHT);
		receiptAmountColumnConfig.setRenderer(amountRenderer);
		ColumnConfig receiptDateColumnConfig = new ColumnConfig("Receipt Date", RECEIPT_DATE_RECORD_NAME, 150, false);

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				receiptDateColumnConfig,
				receiptAmountColumnConfig
		};

		gridPanel.setColumnModel(new ColumnModel(columns));

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);

		dataStore.load();
		dataStore.setSortInfo(new SortState(RECEIPT_DATE_RECORD_NAME, SortDir.ASC));
		gridPanel.setStore(dataStore);

		add(gridPanel, new ColumnLayoutData(0.4));
	}

	private void populateGrid(List<MainCertReceiveDateWrapper> mainCertReceiveDateWrapperList) {
		if (dataStore != null)
			dataStore.removeAll();
		if (mainCertReceiveDateWrapperList == null)
			return;
		Double totalPaymentAmount = 0.0;
		for (MainCertReceiveDateWrapper mainCertReceiveDateWrapper : mainCertReceiveDateWrapperList) {
			totalPaymentAmount += mainCertReceiveDateWrapper.getPaymentAmount();
			
			Record record = recordDef.createRecord(new Object[] {
					mainCertReceiveDateWrapper.getPaymentAmount(),
					date2String(mainCertReceiveDateWrapper.getPaymentDate())
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

	// set the Label
	private Label setLabelStyle(Label label) {
		label.setCtCls("table-cell-Label");
		return label;
	}

	private Label setValueStyle(Label label) {
		label.setCtCls("table-cell-Value-right-align");
		return label;
	}

	private void addLabelsAndValues() {
		itemPanel(customerNumberLabel);
		itemPanel(customerNumberValue);
		itemPanel(customerNameValue);
		itemPanel(new Label());

		tablePanel.add(referenceLabel);
		tablePanel.add(referenceValue);
		tablePanel.add(docNoTypeCoLabel);
		tablePanel.add(docNoTypeCoValue);

		itemPanel(invoiceDateLabel);
		itemPanel(invoiceDateValue);
		itemPanel(gLDateLabel);
		itemPanel(gLDateValue);

		tablePanel.add(dueDateLabel);
		tablePanel.add(dueDateValue);
		tablePanel.add(dateClosedLabel);
		tablePanel.add(dateClosedValue);

		itemPanel(currencyLabel);
		itemPanel(currencyValue);
		itemPanel(payStatusLabel);
		itemPanel(payStatusValue);

		tablePanel.add(grossAmountLabel);
		tablePanel.add(grossAmountValue);
		tablePanel.add(openAmountLabel);
		tablePanel.add(openAmountValue);

		itemPanel(foreignAmountLabel);
		foreignAmountValue.setWidth(175);
		itemPanel(foreignAmountValue);
		itemPanel(foreignOpenAmountLabel);
		foreignOpenAmountValue.setWidth(160);
		itemPanel(foreignOpenAmountValue);

		tablePanel.add(descriptionLabel);
		tablePanel.add(descriptionValue);
		tablePanel.add(new Label());
		tablePanel.add(new Label());

		itemPanel(remarksLabel);
		itemPanel(remarksValue);
		itemPanel(new Label());
		itemPanel(new Label());
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

	private String date2String(Date date) {
		if (date != null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}

	private String makeDocNoTypeCoValue(ARRecord arRrecord) {
		return (preventNullString(arRrecord.getDocumentNumber()) + "/ "
				+ preventNullString(arRrecord.getDocumentType()) + "/ " + preventNullString(arRrecord.getCompany()));
	}

	private String preventNullString(String input) {
		if (input == null)
			return "";
		else
			return input;
	}

	private String preventNullString(Integer input) {
		if (input == null)
			return "";
		else
			return input.toString();
	}

	private String preventNullString(Double input) {
		if (input == null)
			return "";
		else
			return amountRenderer.render(input.toString());
	}

}
