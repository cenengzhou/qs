/**
 * koeyyeung
 * Jul 18, 20139:35:49 AM
 * Change from Window to Panel
 */
package com.gammon.qs.client.ui.panel.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.directPayment.SCPaymentExceptionalWrapper;
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
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

public class SubcontractorPaymentExceptionalReportMainPanel extends Panel{
public static final String MAIN_PANEL_ID = "SubcontractorPaymentExceptionalReportMainPanel";
	
	private GlobalSectionController globalSectionController;
	private final FieldDef[] gridFieldDef = new FieldDef[]{
			new StringFieldDef("division"),
			new StringFieldDef("company"),
			new StringFieldDef("jobnumber"),
			new StringFieldDef("jobDescription"),
			new StringFieldDef("packageDescription"),
			new StringFieldDef("packageNo"),
			new StringFieldDef("subcontractorName"),
			new StringFieldDef("subcontractorNumber"),
			new StringFieldDef("paymentNo"),
			new StringFieldDef("paymentAmount"),
			new StringFieldDef("asAtDate"),
			new StringFieldDef("certIssueDate"),
			new StringFieldDef("dueDate"),
			new StringFieldDef("createUser"),
			new StringFieldDef("scStatus")
	};
	
	
	private final RecordDef paymentRequisitionRecordDef = new RecordDef(gridFieldDef);

	private TextField divisionTextField;

	private TextField companyTextField;

	private TextField jobNumberTextField;

	private TextField packageNoTextField;

	private TextField subcontractorTextField;

	private Store dataStore;

	private ComboBox scStatusComboBox;

	private PaginationToolbar paginationToolbar;

	/**
	 * Subcontractor Payment Exceptional report
	 * List out the subcontract has paid by payment requisition but still have not submitted the contract awarded
	 *  
	 * @author peterchan 
	 * 
	 */
	public SubcontractorPaymentExceptionalReportMainPanel(GlobalSectionController globalSectionController) {
		super();
		
		this.globalSectionController=globalSectionController;
		
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		
		setupSearchPanel();
		setupGridPanel();
		
		setId(MAIN_PANEL_ID);		
		
		//hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	private void setupSearchPanel() {
		Panel searchPanel = new Panel();
		searchPanel.setFrame(true);
		searchPanel.setPaddings(10);
		searchPanel.setLayout(new TableLayout(8));
		searchPanel.setHeight(100);
		
		Label divisionLabel = new Label("Division");
		divisionLabel.setCtCls("table-cell");
		divisionTextField = new TextField();
		divisionTextField.setCtCls("table-cell");
		
		Label companyLabel = new Label("Company");
		companyLabel.setCtCls("table-cell");
		companyTextField = new TextField();
		companyTextField.setCtCls("table-cell");
		
		Label jobNumberLabel = new Label("Job Number");
		jobNumberLabel.setCtCls("table-cell");
		jobNumberTextField = new TextField();
		jobNumberTextField.setCtCls("table-cell");
		
		if (globalSectionController.getJob()!=null)
			jobNumberTextField.setValue(globalSectionController.getJob().getJobNumber().trim());
		else 
			jobNumberTextField.setValue("");
		
		Label subcontractorLabel = new Label("Vendor No");
		subcontractorLabel.setCtCls("table-cell");
		subcontractorTextField = new TextField();
		subcontractorTextField.setCtCls("table-cell");
		
		Label packageNoLabel = new Label("Package No");
		packageNoLabel.setCtCls("table-cell");
		packageNoTextField = new TextField();
		packageNoTextField.setCtCls("table-cell");
		
		Label scStatusLabel = new Label("SC Status");
		scStatusLabel.setCtCls("table-cell");
		scStatusComboBox = new ComboBox();
		scStatusComboBox.setStore(GlobalParameter.DIRECTPAYMENT_EXCEPTIONAL_ENQUIRY_SC_STATUS_STORE);
		scStatusComboBox.setDisplayField("description");
		scStatusComboBox.setValueField("value");
		scStatusComboBox.setForceSelection(true);
		scStatusComboBox.setReadOnly(true);
		scStatusComboBox.setWidth(300);
		scStatusComboBox.setValue("all");
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				search();
			}
			
		});
		Button exportButton = new Button("Export");
		exportButton.addListener(new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				String url=GlobalParameter.SC_PAYMENT_EXCEPTION_REPORT_DOWNLOAD_URL+
							"?scStatus="+scStatusComboBox.getValue();
				if (jobNumberTextField.getText()!=null && jobNumberTextField.getText().trim().length()>0)
					url+="&jobNumber="+jobNumberTextField.getText().trim();
				if (divisionTextField.getText()!=null && divisionTextField.getText().trim().length()>0)
					url+="&division="+divisionTextField.getText().trim().replaceAll("&", "%26");
				if (companyTextField.getText()!=null && companyTextField.getText().trim().length()>0)
					url+="&company="+companyTextField.getText().trim();
				if (subcontractorTextField.getText()!=null && subcontractorTextField.getText().trim().length()>0)
					url+="&vendorNo="+subcontractorTextField.getText().trim();
				if (packageNoTextField.getText()!=null && packageNoTextField.getText().trim().length()>0)
					url="&packageNo="+packageNoTextField.getText().trim();
				
				com.google.gwt.user.client.Window.open(url, "_blank", "");
			}
			
		});
		
		searchPanel.add(divisionLabel);
		searchPanel.add(divisionTextField);
		
		searchPanel.add(companyLabel);
		searchPanel.add(companyTextField);
		
		
		searchPanel.add(jobNumberLabel);
		searchPanel.add(jobNumberTextField, new TableLayoutData(3));
		
		searchPanel.add(subcontractorLabel);
		searchPanel.add(subcontractorTextField);
		
		searchPanel.add(packageNoLabel);
		searchPanel.add(packageNoTextField);
		
		searchPanel.add(scStatusLabel);
		searchPanel.add(scStatusComboBox);

		
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new HorizontalLayout(10));
		buttonPanel.add(searchButton);
		buttonPanel.add(exportButton);
		
		Panel emptyPanel = new Panel();
		emptyPanel.setWidth(10);
		
		searchPanel.add(emptyPanel);
		searchPanel.add(buttonPanel);
		
		add(searchPanel);
	}

	private void setupGridPanel() {
		GridPanel resultGridPanel = new GridPanel();
		GridView view = new GridView();
		view.setForceFit(false);
		resultGridPanel.setView(view);
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		ColumnConfig divisionColConfig = new ColumnConfig("Division","division",50,false);
		divisionColConfig.setAlign(TextAlign.CENTER);
		
		ColumnConfig companyColConfig = new ColumnConfig("Company","company",50,false);
		companyColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig jobNumberColConfig = new ColumnConfig("Job Number", "jobnumber",50,false);
		jobNumberColConfig.setAlign(TextAlign.CENTER);
		
		ColumnConfig jobDescColConfig = new ColumnConfig("Job Description","jobDescription",150,false);
		jobDescColConfig.setAlign(TextAlign.LEFT);

		ColumnConfig packageDescColConfig = new ColumnConfig("Package Description","packageDescription",150,false);
		packageDescColConfig.setAlign(TextAlign.LEFT);
		
		ColumnConfig packageNoColConfig = new ColumnConfig("Package Number","packageNo",50,false);
		packageNoColConfig.setAlign(TextAlign.CENTER);
		
		ColumnConfig subcontractorNameColConfig = new ColumnConfig("Subcontractor","subcontractorName",150,false);
		subcontractorNameColConfig.setAlign(TextAlign.LEFT);
		
		ColumnConfig subcontractorNoColConfig = new ColumnConfig("Subcontractor Number","subcontractorNumber",75,false);
		subcontractorNoColConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig paymentNoColConfig = new ColumnConfig("SC Payment No","paymentNo",75,false);
		paymentNoColConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig paymentAmountColConfig = new ColumnConfig("Payment Amount","paymentAmount",100,false);
		paymentAmountColConfig.setAlign(TextAlign.RIGHT);
		paymentAmountColConfig.setRenderer(amountRenderer);
		
		ColumnConfig asAtDateColConfig = new ColumnConfig("As At Date","asAtDate",100,false);
		asAtDateColConfig.setAlign(TextAlign.CENTER);
//		asAtDateColConfig.

		ColumnConfig certIssueDateColConfig = new ColumnConfig("Cert. Issue Date","certIssueDate",100,false);
		certIssueDateColConfig.setAlign(TextAlign.CENTER);

		ColumnConfig dueDateColConfig = new ColumnConfig("Due Date","dueDate",100,false);
		dueDateColConfig.setAlign(TextAlign.CENTER);
		
		ColumnConfig createdUserColConfig = new ColumnConfig("Created User","createUser",100,false);
		createdUserColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig scStatusColConfig = new ColumnConfig("SC Status","scStatus",50,false);
		scStatusColConfig.setAlign(TextAlign.CENTER);


		BaseColumnConfig[] columns = new BaseColumnConfig[]{
			divisionColConfig,
			companyColConfig,
			jobNumberColConfig,
			jobDescColConfig,
			packageDescColConfig,
			packageNoColConfig,
			scStatusColConfig,
			subcontractorNameColConfig,
			subcontractorNoColConfig,
			paymentNoColConfig,
			paymentAmountColConfig,
			asAtDateColConfig,
			certIssueDateColConfig,
			dueDateColConfig,
			createdUserColConfig
		};
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){

			public void goToPage(int pageNum) {
				UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPaymentRepository().obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(pageNum, new AsyncCallback<PaginationWrapper<SCPaymentExceptionalWrapper>>() {

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}

					public void onSuccess(
							PaginationWrapper<SCPaymentExceptionalWrapper> result) {
						popularGrid(result);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				});
			}
			
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});

		ArrayReader reader = new ArrayReader(paymentRequisitionRecordDef);
		dataStore = new Store(proxy, reader,true);
		
		resultGridPanel.setColumnModel(new ColumnModel(columns));
		resultGridPanel.setStore(dataStore);
		resultGridPanel.setBottomToolbar(paginationToolbar);
		
		add(resultGridPanel);
	}

	private void search(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPaymentRepository().	obtainSubcontractorPaymentExceptionReport(divisionTextField.getText().trim(),
		companyTextField.getText().trim(),jobNumberTextField.getText().trim(), subcontractorTextField.getText().trim(), 
		packageNoTextField.getText().trim(), scStatusComboBox.getValue().trim(), globalSectionController.getUser().getUsername(),
		new AsyncCallback<PaginationWrapper<SCPaymentExceptionalWrapper>>() {
			public void onSuccess(PaginationWrapper<SCPaymentExceptionalWrapper> result) {
				popularGrid(result);
				paginationToolbar.setCurrentPage(0);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	protected void popularGrid(PaginationWrapper<SCPaymentExceptionalWrapper> result) {
		dataStore.removeAll();
		if (result==null||result.getCurrentPageContentList()==null||result.getCurrentPageContentList().size()<1){
			MessageBox.alert("No data can be found");
			paginationToolbar.setTotalPage(0);
//			paginationToolbar.setCurrentPage(result.getCurrentPage());

		}else {
			for (SCPaymentExceptionalWrapper scPaymentExceptionalWrapper:result.getCurrentPageContentList()){
				Record record = paymentRequisitionRecordDef.createRecord(new Object[]{
						scPaymentExceptionalWrapper.getDivision(),
						scPaymentExceptionalWrapper.getCompany(),
						scPaymentExceptionalWrapper.getJobNumber(),
						scPaymentExceptionalWrapper.getJobDescription(),
						scPaymentExceptionalWrapper.getPackageDescription(),
						scPaymentExceptionalWrapper.getPackageNo(),
						scPaymentExceptionalWrapper.getSubcontractorName(),
						scPaymentExceptionalWrapper.getSubcontractorNumber(),
						scPaymentExceptionalWrapper.getPaymentNo(),
						scPaymentExceptionalWrapper.getPaymentAmount(),
						DateUtil.formatDate(scPaymentExceptionalWrapper.getAsAtDate()),
						DateUtil.formatDate(scPaymentExceptionalWrapper.getCertIssueDate()),
						DateUtil.formatDate(scPaymentExceptionalWrapper.getDueDate()),
						scPaymentExceptionalWrapper.getCreateUser(),
						scPaymentExceptionalWrapper.getScStatus()
				});
				dataStore.add(record);
			}
			paginationToolbar.setTotalPage(result.getTotalPage());
			paginationToolbar.setCurrentPage(result.getCurrentPage());
		}
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
}
