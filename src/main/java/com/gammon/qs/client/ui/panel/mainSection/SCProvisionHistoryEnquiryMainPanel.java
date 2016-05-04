/**
 * koeyyeung
 * Jul 4, 20132:53:05 PM
 * Change from Window to Panel
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.util.DateUtil;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryPaginationWrapper;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * modified by matthewlam, 2015-01-22
 * Delete button related codes removed
 * RoleSecurityFunctions.F010511_SUBCONTRACT_PROVISION_POSTING_HISTORY_MAINPANEL is found does not contain "WRITE"
 * i.e. the original code never shows the delete button
 */
public class SCProvisionHistoryEnquiryMainPanel extends Panel {
	public static final String		MAIN_PANEL_ID				= "SCProvisionEnquiryMainPanel_ID";
	private GlobalSectionController	globalSectionController;

	private PaginationToolbar		paginationToolbar			= null;
	private GridPanel				gridPanel;

	private TextField				jobNumberTextField;
	private TextField				packageNumberTextField;
	private TextField				postedYearTextField;
	private TextField				postedMonthTextField;

	private String					jobNumber;

	private Store					dataStore;

	private FieldDef[]				fieldDef					= new FieldDef[] {
																new StringFieldDef("jobNumber"),
																new StringFieldDef("packageNumber"),
																new StringFieldDef("postedMonth"),
																new StringFieldDef("postedYear"),
																new StringFieldDef("objectCode"),
																new StringFieldDef("subsidiaryCode"),
																new StringFieldDef("scRate"),
																new StringFieldDef("cumulativeWorkdoneQuantity"),
																new StringFieldDef("cumulativeCertifiedQuantity"),
																new StringFieldDef("cumulativeWorkdoneAmount"),
																new StringFieldDef("postedCertAmt"),
																new StringFieldDef("provision"),
																new StringFieldDef("username") };

	private RecordDef				provisionHistoryRecordDef	= new RecordDef(fieldDef);

	private ToolbarTextItem			totalCumulativeWorkdoneAmountTextItem;
	private ToolbarTextItem			totalPostedCertifiedAmountTextItem;
	private ToolbarTextItem			totalProvisionTextItem;

	public SCProvisionHistoryEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		jobNumber = this.globalSectionController.getJob().getJobNumber();

		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());

		setupGridPanel();

		setId(MAIN_PANEL_ID);

		// hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	/**
	 * modified by matthewlam, 2015-01-19
	 */
	private void setupToolbar() {
		FieldListenerAdapter enterKeyAdapter = new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					search();
				}
			}
		};

		Toolbar toolbar = new Toolbar();
		toolbar.setHeight(30);

		ToolbarTextItem jobNumberLabel = new ToolbarTextItem("Job No.:");
		toolbar.addItem(jobNumberLabel);

		jobNumberTextField = new TextField("Job No.", "jobNumber", 100);
		jobNumberTextField.setValue(jobNumber);
		jobNumberTextField.setDisabled(true);
		toolbar.addField(jobNumberTextField);

		toolbar.addSpacer();
		ToolbarTextItem packageNumberLabel = new ToolbarTextItem("Package No.:");
		toolbar.addItem(packageNumberLabel);

		packageNumberTextField = new TextField("Package No.", "packageNumber", 100);
		packageNumberTextField.addListener(enterKeyAdapter);
		toolbar.addField(packageNumberTextField);

		toolbar.addSpacer();
		ToolbarTextItem postedMonthLabel = new ToolbarTextItem("Posted Month:");
		toolbar.addItem(postedMonthLabel);

		postedMonthTextField = new TextField("Posted Month", "postedMonth", 100);
		postedMonthTextField.addListener(enterKeyAdapter);
		toolbar.addField(postedMonthTextField);

		toolbar.addSpacer();
		ToolbarTextItem postedYearLabel = new ToolbarTextItem("Posted Year:");
		toolbar.addItem(postedYearLabel);

		postedYearTextField = new TextField("Posted Year", "postedYear", 100);
		postedYearTextField.addListener(enterKeyAdapter);
		toolbar.addField(postedYearTextField);

		ToolbarButton searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				search();
			};
		});
		toolbar.addButton(searchButton);
		toolbar.addSeparator();

		Item exportButton = new Item("Export");
		exportButton.setIconCls("download-icon");
		exportButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem button, EventObject e) {
				exportToExcel();
			};
		});

		Menu excelMenu = new Menu();
		excelMenu.addItem(exportButton);
		
		ToolbarButton excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");

		toolbar.addButton(excelToolbarButton);

		packageNumberTextField.focus();

		gridPanel.setTopToolbar(toolbar);

	}

	/**
	 * modified by matthewlam, 2015-01-16
	 * Bug Fix #92: rearrange column names and order for SC Provision History Panel
	 */
	private void setupGridPanel() {
		gridPanel = new GridPanel();
		GridView view = new CustomizedGridView();
		User user = globalSectionController.getUser();

		view.setForceFit(false);
		gridPanel.setView(view);

		setupToolbar();

		// Set the rate column follow the user's preference
		final Renderer rateRenderer = new RateRenderer(user);
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer amountRenderer = new AmountRenderer(user);

		ColumnConfig jobNumberColConfig = new ColumnConfig("Job No.", "jobNumber", 80, true);
		ColumnConfig packageNumberColConfig = new ColumnConfig("Package No.", "packageNumber", 80, true);
		ColumnConfig postedMonthColConfig = new ColumnConfig("Posted Month", "postedMonth", 80, true);
		ColumnConfig postedYearColConfig = new ColumnConfig("Posted Year", "postedYear", 80, true);
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 80, true);
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 100, true);
		ColumnConfig scRateColConfig = new ColumnConfig("SC Rate", "scRate", 80, true, rateRenderer);
		ColumnConfig cumulativeWorkdoneQuantityColumnConfig = new ColumnConfig("Cumulative Workdone Qty", "cumulativeWorkdoneQuantity", 175, true, quantityRenderer);
		ColumnConfig cumulativeCertifiedQuantityColumnConfig = new ColumnConfig("Cumulative Certified Qty", "cumulativeCertifiedQuantity", 165, true, quantityRenderer);
		ColumnConfig cumulativeWorkdoneAmountColumnConfig = new ColumnConfig("<b>Cumulative Workdone Amount</b>", "cumulativeWorkdoneAmount", 185, true, amountRenderer);
		ColumnConfig postedCertifiedAmountColumnConfig = new ColumnConfig("<b>Posted Certified Amount</b>", "postedCertAmt", 160, true, amountRenderer);
		ColumnConfig provisionColumnConfig = new ColumnConfig("<b>Provision</b>", "provision", 80, true, amountRenderer);
		ColumnConfig usernameColumnConfig = new ColumnConfig("Username", "username", 80, true);

		postedCertifiedAmountColumnConfig.setAlign(TextAlign.RIGHT);
		cumulativeWorkdoneAmountColumnConfig.setAlign(TextAlign.RIGHT);

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				new RowNumberingColumnConfig(),
				jobNumberColConfig,
				packageNumberColConfig,
				postedMonthColConfig,
				postedYearColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				scRateColConfig,
				cumulativeWorkdoneQuantityColumnConfig,
				cumulativeCertifiedQuantityColumnConfig,
				cumulativeWorkdoneAmountColumnConfig,
				postedCertifiedAmountColumnConfig,
				provisionColumnConfig,
				usernameColumnConfig };

		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter() {
			public void goToPage(int pageNumber) {
				populateProvisionHistoriesByPage(pageNumber);
			}
		});

		totalCumulativeWorkdoneAmountTextItem = new ToolbarTextItem("<b>Total Cumulative Workdone Amount:</b>");
		totalPostedCertifiedAmountTextItem = new ToolbarTextItem("<b>Total Posted Certified Amount:</b>");
		totalProvisionTextItem = new ToolbarTextItem("<b>Total Provision:</b>");

		paginationToolbar.addFill();
		paginationToolbar.addItem(totalCumulativeWorkdoneAmountTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalPostedCertifiedAmountTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalProvisionTextItem);
		paginationToolbar.addSpacer();

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(provisionHistoryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();

		gridPanel.setColumnModel(new ColumnModel(columns));
		gridPanel.setStore(dataStore);
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setDisabled(true);

		add(gridPanel);
	}

	/**
	 * @author matthewatc
	 *         exports the results for the query specified by the values in the search fields as an excel spreadsheet
	 *         (note that this does not export the contents of the gridPanel, but rather exports what would be the
	 *         contents of the gridPanel if a search had been conducted with the currently-entered query parameters)
	 */
	private void exportToExcel() {
		String packageNo = packageNumberTextField.getText();
		String postedYear = postedYearTextField.getText();
		String postedMonth = postedMonthTextField.getText();

		if (!DateUtil.validateMonthInput(postedMonth, true)) {
			MessageBox.alert("Please input a valid Month.");
			return;
		}

		if (!DateUtil.validateYearInput(postedYear, true)) {
			MessageBox.alert("Please input a valid Year.");
			return;
		}

		com.google.gwt.user.client.Window.open(
				GlobalParameter.SC_PROVISION_HISTORY_EXCEL_DOWNLOAD_URL + "?jobNumber=" + jobNumber + "&packageNo=" + packageNo + "&postedYear=" + postedYear + "&postedMonth=" + postedMonth,
					"_blank",
					"");
	}

	private void search() {
		String packageNo = packageNumberTextField.getText();
		String postedYear = postedYearTextField.getText();
		String postedMonth = postedMonthTextField.getText();

		if (!DateUtil.validateMonthInput(postedMonth, true)) {
			MessageBox.alert("Please input a valid Month.");
			return;
		}

		if (!DateUtil.validateYearInput(postedYear, true)) {
			MessageBox.alert("Please input a valid Year.");
			return;
		}

		obtainProvisionHistory(jobNumber, packageNo, postedYear, postedMonth);
	}

	public void populateGridByPage(SCDetailProvisionHistoryPaginationWrapper wrapper) {
		if (wrapper == null || wrapper.getCurrentPageContentList() == null || wrapper.getCurrentPageContentList().isEmpty()) {
			dataStore.removeAll();
			paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
			return;
		}

		paginationToolbar.setDisabled(false);
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());

		NumberFormat nf = NumberFormat.getFormat("#,##0.00");
		totalCumulativeWorkdoneAmountTextItem.setText("<b>Total Cumulative Workdone Amount: " + nf.format(wrapper.getTotalCumulativeAmount()) + "</b>");
		totalPostedCertifiedAmountTextItem.setText("<b>Total Posted Certified Amount " + nf.format(wrapper.getTotalPostedCertifiedAmount()) + "</b>");
		totalProvisionTextItem.setText("<b>Total Provision: " + nf.format(wrapper.getTotalProvision()) + "</b>");

		populateContentList(wrapper.getCurrentPageContentList());

		// hide detail panel
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	public void populateContentList(List<SCDetailProvisionHistoryWrapper> historyWrappers) {
		dataStore.removeAll();

		if (historyWrappers == null || historyWrappers.isEmpty()) {
			paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
			return;
		}

		for (SCDetailProvisionHistoryWrapper history : historyWrappers) {
			Record record = provisionHistoryRecordDef.createRecord(new Object[] {
					preventNullString(jobNumber),
					preventNullString(history.getPackageNo()),
					preventNullString(history.getPostedMonth()),
					preventNullString(history.getPostedYr()),
					preventNullString(history.getObjectCode()),
					preventNullString(history.getSubsidiaryCode()),
					preventNullString(history.getScRate()),
					preventNullString(history.getCumLiabilitiesQty()),
					preventNullString(history.getCumulativeCertifiedQuantity()),
					preventNullString(history.getCumLiabilitiesAmount()),
					preventNullString(history.getPostedCertAmount()),
					preventNullString(history.getProvision()),
					preventNullString(history.getCreatedUser()), });

			dataStore.add(record);
		}
	}

	// added by brian on 20110427
	// search the provision history and get the whole list
	// called by search button in Provision History Window
	public void obtainProvisionHistory(final String jobNumber,
										final String packageNo,
										final String year,
										final String month) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().searchProvisionHistory(
				jobNumber,
				packageNo,
				year,
				month,
				new AsyncCallback<List<SCDetailProvisionHistoryWrapper>>() {
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}

					public void onSuccess(List<SCDetailProvisionHistoryWrapper> paginationWrapper) {
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
						populateProvisionHistoriesByPage(0);
					}
				});
	}

	// added by brian on 20110428
	// populate provision history by page
	public void populateProvisionHistoriesByPage(int pageNumber) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().populateGridByPage(
				pageNumber,
				new AsyncCallback<SCDetailProvisionHistoryPaginationWrapper>() {
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}

					public void onSuccess(SCDetailProvisionHistoryPaginationWrapper wrapper) {
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
						populateGridByPage(wrapper);
					}
				});
	}

	// convert null string to ""
	private String preventNullString(Object input) {
		return input == null ? "" : input.toString();
	}

}
