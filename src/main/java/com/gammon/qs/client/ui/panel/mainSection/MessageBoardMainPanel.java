package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.detailSection.MessageBoardAttachmentDetailPanel;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.AddNewMessageWindow;
import com.gammon.qs.client.ui.window.MessageBoardAttachmentWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateSubcontractPackagePackageSelectionWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateSubcontractPaymentPackageSelectionWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateWorkDonePackageSelectionWindow;
import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
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
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.AbsoluteLayout;
import com.gwtext.client.widgets.layout.AbsoluteLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * koeyyeung
 * Dec 27, 2013 3:25:46 PM
 * modified by Henry Lai
 * on 17-10-2014 
 */
public class MessageBoardMainPanel extends Panel{
	private String mainSectionPanel_ID;
	private static final String MESSAGE_BOARD_BACKGROUND = GlobalParameter.BASED_URL+"images/note.jpg";
	private static final String WORKFLOW_ICON = GlobalParameter.BASED_URL+"images/workflow.gif";
	private static final String CERTIFICATE_ICON = GlobalParameter.BASED_URL+"images/certificate.png";
	private static final String PAYMENT_ICON = GlobalParameter.BASED_URL+"images/payment.png";
	private static final String ENQUIRY_ICON = GlobalParameter.BASED_URL+"images/search.gif";
	private static final String EXCEL_ICON = GlobalParameter.BASED_URL+"images/page_white_excel.gif";
	private static final String PDF_ICON = GlobalParameter.BASED_URL+"images/pdf.png";
	private static final String SCPACKAGE_ICON = GlobalParameter.BASED_URL+"images/award.png";
	private static final String REPACKAGING_ICON = GlobalParameter.BASED_URL+"images/package.png";
	private static final String IV_ICON = GlobalParameter.BASED_URL+"images/calculator.png";
	private static final String TRANSIT_ICON = GlobalParameter.BASED_URL+"images/bill.png";
	@SuppressWarnings("unused")
	private static final String INFO_ICON = GlobalParameter.BASED_URL+"images/info.png";
	private static final String TABLE_CELL_TRANSPARENT_STYLE = "background-color: transparent";
	private static final String ATTACHMENT_IMAGE = GlobalParameter.BASED_URL+"images/";
	

	private Panel adminPanel;
	private Panel announcementPanel;
	private Panel overviewPanel, transitPanel, tipsPanel;
	private AbsolutePanel tipsPanelAllContent;
	private ScrollPanel tipsScrollPanel;
	private EditorGridPanel adminGridPanel;
	private TabPanel tabPanel;

	private Store dataStoreForAdmin;

	//pagination toolbar
	private PaginationToolbar paginationToolbar;

	private static final String MESSAGE_TYPE_VALUE = "messageTypeValue";
	private static final String MESSAGE_TYPE_DISPLAY = "messageTypeDisplay"; 
	private ComboBox messageTypeComboBox = new ComboBox();
	private String[][] messageTypes = new String[][] {};
	private Store messageTypeStore = new SimpleStore(new String[] {MESSAGE_TYPE_VALUE, MESSAGE_TYPE_DISPLAY}, messageTypes);


	private static final String IS_DISPLAY_VALUE = "isDisplayValue";
	private static final String IS_DISPLAY_DISPLAY = "isDisplayDisplay"; 
	private ComboBox isDisplayComboBox = new ComboBox();
	private String[][] isDisplays = new String[][] {};
	private Store isDisplaysStore = new SimpleStore(new String[] {IS_DISPLAY_VALUE, IS_DISPLAY_DISPLAY}, isDisplays);

	private DateField deliveryDateField;

	//Record names
	private static final String MESSAGE_ID_RECORD_NAME = "messageIDRecordName";
	private static final String TITLE_RECORD_NAME = "titleRecordName";
	private static final String DESCRIPTION_RECORD_NAME = "descriptionRecordName";
	private static final String REQUESTOR_RECORD_NAME = "requestorRecordName";
	private static final String DATE_RECORD_NAME = "dateRecordName";
	private static final String MESSAGE_TYPE_RECORD_NAME = "messageTypeRecordName";
	private static final String IS_DISPLAY_RECORD_NAME = "isDisplayRecordName";


	//records
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(MESSAGE_ID_RECORD_NAME), 
					new StringFieldDef(TITLE_RECORD_NAME),
					new StringFieldDef(DESCRIPTION_RECORD_NAME),
					new StringFieldDef(REQUESTOR_RECORD_NAME),
					new DateFieldDef(DATE_RECORD_NAME),
					new StringFieldDef(MESSAGE_TYPE_RECORD_NAME),
					new StringFieldDef(IS_DISPLAY_RECORD_NAME)
			});

	private String currentTab;
	private Integer currentRowIndex=-1;
	private int scrollToPosition;
	
	private CheckboxSelectionModel selectionModelForAdmin;

	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	private List<MessageBoardAttachment> messageBoardAttachmentList;

	public MessageBoardMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController=globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		mainSectionPanel_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		
		setupUI();

		//hide detail panel
		detailSectionController.getMainPanel().collapse();
		globalSectionController.getMasterListSectionController().getMainPanel().expand();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setBorder(false);

		tabPanel = new TabPanel();
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setResizeTabs(true);
		tabPanel.setBorder(false);
		tabPanel.setAutoScroll(true);
		tabPanel.setMinTabWidth(80);
		tabPanel.setTabWidth(120);
		tabPanel.setActiveTab(0);

		tabPanel.addListener(new TabPanelListenerAdapter(){
			public void onTabChange(TabPanel tab, Panel panel) {
				if(currentTab!=null)
					detailSectionController.resetGridPanel(true);
				currentTab = panel.getTitle();
			}
		});
		
		setupAnnouncementPanel();
		setupAdminPanel();
		setupOverviewPanel();
		setupTransitPanel();
		setupTipsPanelAllContent();
		setupTipsPanel();
		
		announcementPanel.setId("announcement");
		overviewPanel.setId("overview");
		transitPanel.setId("transit");
		tipsPanel.setId("tips");

		tabPanel.add(announcementPanel);
		tabPanel.add(overviewPanel);
		tabPanel.add(transitPanel);
		tabPanel.add(tipsPanel);
		
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.M010008_SYSTEM_ADMIN_TREEPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsList) {
				if (accessRightsList.contains("WRITE")){
					tabPanel.add(adminPanel);
				}
				add(tabPanel);
				populateMessageBoard();
				UIUtil.unmaskPanelById(mainSectionPanel_ID);					
				
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private void setupAnnouncementToolbar(){
		Toolbar toolbar = new Toolbar();

		ToolbarButton refreshButton = new ToolbarButton("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				refresh();
			}
		});
		toolbar.addSeparator();
		toolbar.addButton(refreshButton);
		toolbar.addSeparator();
		
		announcementPanel.setTopToolbar(toolbar);
	}

	private void setupAnnouncementPanel(){
		announcementPanel = new Panel();
		announcementPanel.setIconCls("new-icon");
		announcementPanel.setTitle("Announcement");
		announcementPanel.setAutoScroll(true);
		announcementPanel.setPaddings(5);
		announcementPanel.setLayout(new AbsoluteLayout());

		setupAnnouncementToolbar();
	}

	/**
	 * @author Henry Lai
	 * Created on 17-10-2014 
	 */
	private void setupOverviewPanel(){
		overviewPanel = new Panel();
		overviewPanel.setIconCls("workflow-icon");
		overviewPanel.setTitle("Overview");
		overviewPanel.setAutoScroll(true);
		overviewPanel.setPaddings(5);
		overviewPanel.setLayout(new AbsoluteLayout());

		addOperationPanel();
		addEnquiryPanel();
	}
	
	/**
	 * @author Henry Lai
	 * Created on 17-10-2014 
	 */
	private void setupTransitPanel(){
		transitPanel = new Panel();
		transitPanel.setIconCls("workflow-icon");
		transitPanel.setTitle("Transit");
		transitPanel.setAutoScroll(true);
		transitPanel.setPaddings(5);
		transitPanel.setLayout(new AbsoluteLayout());

		addTransitOperationPanel();
		addTransitEnquiryPanel();
	}
	
	/**
	 * @author Henry Lai
	 * Created on 01-12-2014 
	 */
	private void setupTipsPanelAllContent(){
		tipsScrollPanel = new ScrollPanel();
		tipsScrollPanel.setSize("850px", "575px");
		
		tipsPanelAllContent = new AbsolutePanel();
		tipsPanelAllContent.setSize("830px", "1600px");
		
		addPaymentTermsInfoTips();
		addPaymentStatusInfoTips();
		addPaymentStatusCodeInfoTips();
		addApprovalTypeInfoTips();
		addMainContractCertificateStatusInfoTips();
		addSubcontractStatusInfoTips();
		addRepackagingStatusInfoTips();
		
		tipsScrollPanel.setWidget(tipsPanelAllContent);

	}

	/**
	 * @author Henry Lai
	 * Created on 25-11-2014 
	 */
	private void setupTipsPanel(){
		tipsPanel = new Panel();
		tipsPanel.setIconCls("bulb-icon");
		tipsPanel.setTitle("Tips");
		tipsPanel.setAutoHeight(true);
		tipsPanel.setAutoWidth(true);
		tipsPanel.setPaddings(5);
		tipsPanel.setLayout(new AbsoluteLayout());
		tipsPanel.add(tipsScrollPanel);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 17-10-2014 
	 */
	private void addOperationPanel(){

		AbsolutePanel operationPanel = new AbsolutePanel();
		operationPanel.setSize("830px", "200px");
		operationPanel.setTitle("Operation");
		Label operationPanelTitle = new Label("Operation");
		operationPanelTitle.setStyle("font-weight: bold; font-size:small;");
		operationPanel.add(operationPanelTitle, 10, 10);
		
		HTML flowChartArrow1 = new HTML("<img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-1.png\">");
		HTML flowChartArrow2 = new HTML("<img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-2.png\">");
		HTML flowChartArrow3 = new HTML("<img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\">");
		operationPanel.add(flowChartArrow1, 193, 40);
		operationPanel.add(flowChartArrow2, 393, 40);
		operationPanel.add(flowChartArrow3, 593, 40);
		
        HTML buttonTransitHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+WORKFLOW_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Transit</a>");
        buttonTransitHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					tabPanel.setActiveTab("transit");
			}
		});

        HTML buttonRepackagingHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+REPACKAGING_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Repackaging</a>");
        buttonRepackagingHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().requestNavigateToRepackaging();
			}
		});

        HTML buttonSubcontractPackageHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+SCPACKAGE_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Subcontract Package</a>");
        buttonSubcontractPackageHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else{
					globalSectionController.setCurrentWindow(new UpdateSubcontractPackagePackageSelectionWindow(globalSectionController));
					globalSectionController.getCurrentWindow().show();
				}
			}
		});

        
		HTML buttonSubcontractWockDoneHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+SCPACKAGE_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Subcontract Work Done</a>");
		buttonSubcontractWockDoneHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else{
					globalSectionController.setCurrentWindow(new UpdateWorkDonePackageSelectionWindow(globalSectionController));
					globalSectionController.getCurrentWindow().show();
				}
			}
		});

        HTML buttonInternalValuationHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+IV_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Internal Valuation</a>");
        buttonInternalValuationHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				globalSectionController.getTreeSectionController().getIvTreePanel().showUpdateIVByResourceSummaryMainPanel();
			}
		});
		
        HTML buttonSubcontractPaymentHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+PAYMENT_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Subcontract Payment</a>");
        buttonSubcontractPaymentHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else{
					globalSectionController.setCurrentWindow(new UpdateSubcontractPaymentPackageSelectionWindow(globalSectionController));
					globalSectionController.getCurrentWindow().show();
				}
			}
		});
		
        HTML buttonMainCertHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+CERTIFICATE_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Main Contract Certificate</a>");
        buttonMainCertHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().populateMainCertificate();
			}
		});
		
        //Setup operationPanel & flowChartPanel
		operationPanel.add(buttonTransitHtml, 30, 40);
		operationPanel.add(buttonRepackagingHtml, 230, 40);
		operationPanel.add(buttonSubcontractPackageHtml, 430, 40);
		operationPanel.add(buttonSubcontractWockDoneHtml, 430, 80);
		operationPanel.add(buttonInternalValuationHtml, 430, 120);
		operationPanel.add(buttonSubcontractPaymentHtml, 630, 40);
		operationPanel.add(buttonMainCertHtml, 230, 160);
		overviewPanel.add(operationPanel, new AbsoluteLayoutData(10, 10));
	}

	/**
	 * @author Henry Lai
	 * Created on 17-10-2014 
	 */
	private void addEnquiryPanel(){
		
		AbsolutePanel enquiryPanel = new AbsolutePanel();
		enquiryPanel.setSize("830px", "350px");
		enquiryPanel.setTitle("Enquiry");
		Label enquiryPanelTitle = new Label("Enquiry");
		enquiryPanelTitle.setStyle("font-weight: bold; font-size:small;");
		enquiryPanel.add(enquiryPanelTitle, 10, 10);

		//Job Panel
		AbsolutePanel jobPanel = new AbsolutePanel();
		jobPanel.setSize("190px", "290px");
		jobPanel.setTitle("Job");
		Label jobPanelTitle = new Label("Job");
		jobPanelTitle.setStyle("font-weight: bold; font-size:x-small;");
		jobPanel.add(jobPanelTitle, 5, 5);
		DOM.setStyleAttribute(jobPanel.getElement(), "border", "1px solid #B0C4DE");
		
        HTML buttonGeneralInformationHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;General Information</a>");
        buttonGeneralInformationHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobInformationTreePanel().navigateToJobGeneralInformationMainPanel();
			}
		});
        
        HTML buttonDatesHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Dates</a>");
        buttonDatesHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().navigateToJobDates();
			}
		});
        
        HTML buttonBQHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Bill of Quantities</a>");
        buttonBQHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else{
					BQItemEnquiryMainPanel bqItemMainPanel = new BQItemEnquiryMainPanel(globalSectionController,"","","");																	
					globalSectionController.getMainSectionController().setGridPanel(bqItemMainPanel);
					globalSectionController.getMainSectionController().populateMainPanelWithGridPanel();
					globalSectionController.getDetailSectionController().resetPanel();
				}
			}
		});

		jobPanel.add(buttonGeneralInformationHtml, 10,40);
		jobPanel.add(buttonDatesHtml, 10,80);
		jobPanel.add(buttonBQHtml, 10,120);
		
		//internal Valuation Panel
		AbsolutePanel internalValuationPanel = new AbsolutePanel();
		internalValuationPanel.setSize("190px", "290px");
		internalValuationPanel.setTitle("Internal Valuation");
		Label internalValuationPanelTitle = new Label("Internal Valuation");
		internalValuationPanelTitle.setStyle("font-weight: bold; font-size:x-small;");
		internalValuationPanel.add(internalValuationPanelTitle, 5, 5);
		DOM.setStyleAttribute(internalValuationPanel.getElement(), "border", "1px solid #B0C4DE");
		
		 HTML buttonIVHistoryHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Internal Valuation History</a>");
		 buttonIVHistoryHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getIvTreePanel().showIVHistoryEnquiryMainPanel();
			}
		});
		
		internalValuationPanel.add(buttonIVHistoryHtml, 10,40);

		//Finance Panel
		AbsolutePanel financePanel = new AbsolutePanel();
		financePanel.setSize("190px", "290px");
		financePanel.setTitle("Finance");
		Label financePanelTitle = new Label("Finance");
		financePanelTitle.setStyle("font-weight: bold; font-size:x-small;");
		financePanel.add(financePanelTitle, 5, 5);
		DOM.setStyleAttribute(financePanel.getElement(), "border", "1px solid #B0C4DE");
		
        HTML buttonJobCostHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Job Cost</a>");
        buttonJobCostHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showAccountBalanceByDateRangeMainPanel();
			}
		});
        
        HTML buttonSupplieLedgerHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Supplier Ledger (AP)</a>");
        buttonSupplieLedgerHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showAPRecordEnquiryMainPanel();
			}
		});
        
        HTML buttonCustomerLedgerHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Customer Ledger (AR)</a>");
        buttonCustomerLedgerHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showARRecordEnquiryMainPanel();
			}
		});
        
        HTML buttonAccountLedgerHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Account Ledger</a>");
        buttonAccountLedgerHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showAccountLedgerEnquiryMainPanel();
			}
		});
        
        HTML buttonPurchaseLedgerHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Purchase Ledger (PO)</a>");
        buttonPurchaseLedgerHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showPurchaseOrderEnquiryMainPanel();
			}
		});
        
        HTML buttonPerformanceAppraisalHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Performance Appraisal</a>");
        buttonPerformanceAppraisalHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getJobCostTreePanel().showPerformanceAppraisalEnquiryMainPanel();
			}
		});

		financePanel.add(buttonJobCostHtml, 10,40);
		financePanel.add(buttonSupplieLedgerHtml, 10,80);
		financePanel.add(buttonCustomerLedgerHtml, 10,120);
		financePanel.add(buttonAccountLedgerHtml, 10,160);
		financePanel.add(buttonPurchaseLedgerHtml, 10,200);
		financePanel.add(buttonPerformanceAppraisalHtml, 10,240);
		
		//Subcontract Panel
		AbsolutePanel subcontractPanel = new AbsolutePanel();
		subcontractPanel.setSize("190px", "290px");
		subcontractPanel.setTitle("Subcontract");
		Label subcontractPanelTitle = new Label("Subcontract");
		subcontractPanelTitle.setStyle("font-weight: bold; font-size:x-small;");
		subcontractPanel.add(subcontractPanelTitle, 5, 5);
		DOM.setStyleAttribute(subcontractPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML buttonSubcontractEnquiryHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Subcontract</a>");
		buttonSubcontractEnquiryHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				globalSectionController.getTreeSectionController().getSubcontractorTreePanel().showSubcontractEnquiryMainPanel();
			}
		});
        
		HTML buttonPaymentEnquiryHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Payment</a>");
		buttonPaymentEnquiryHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getPaymentTreePanel().populatePaymentCertificateEnquiryMainPanel();
			}
		});
        
		HTML buttonProvisionHistoryEnquiryHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\">&nbsp;Provision History</a>");
		buttonProvisionHistoryEnquiryHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().getScProvisionTreePanel().showSCProvisionHistoryEnquiryMainPanel();
			}
		});
		
		subcontractPanel.add(buttonSubcontractEnquiryHtml, 10,40);
		subcontractPanel.add(buttonPaymentEnquiryHtml, 10,80);
		subcontractPanel.add(buttonProvisionHistoryEnquiryHtml, 10,120);
		
		//Setup enquiryPanel & flowChartPanel
		enquiryPanel.add(jobPanel, 20, 40);
		enquiryPanel.add(internalValuationPanel, 220, 40);
		enquiryPanel.add(financePanel, 420, 40);
		enquiryPanel.add(subcontractPanel, 620, 40);
		overviewPanel.add(enquiryPanel, new AbsoluteLayoutData(10, 20));
	}
	
	/**
	 * @author Henry Lai
	 * Created on 20-10-2014 
	 */
	private void addTransitOperationPanel(){
		AbsolutePanel operationPanel = new AbsolutePanel();
		operationPanel.setSize("880px", "250px");
		operationPanel.setTitle("Operation");
		Label operationPanelTitle = new Label("Operation");
		operationPanelTitle.setStyle("font-weight: bold; font-size:small;");
		operationPanel.add(operationPanelTitle, 10, 10);
		
		HTML flowChartTransitOperationArrows = new HTML("<img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-transit-operation-arrows.png\">");
		operationPanel.add(flowChartTransitOperationArrows, 90, 6);
		
        HTML buttonTransitHeaderHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>CREATE</b></td></tr><tr><td>Transit</td></tr><tr><td>Header</td></tr></table></a>");
        buttonTransitHeaderHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().requestNavigateToTransitHeader();
			}
		});

        HTML buttonImportBQItemsHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+EXCEL_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>IMPORT</b></td></tr><tr><td>BQ Items</td></tr><tr><td> </td></tr></table></a>");
        buttonImportBQItemsHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().requestNavigateToTransitImport(GlobalParameter.TRANSIT_BQ);
			}
		});

        HTML buttonViewBQItemsHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>VIEW</b></td></tr><tr><td>BQ Items</td></tr><tr><td> </td></tr></table></a>");
        buttonViewBQItemsHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().requestNavigateToTransitItemEnquiry();
			}
		});

		HTML buttonImportResourceHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+EXCEL_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
				+ "<td><b>IMPORT</b></td></tr><tr><td>Resource</td></tr><tr><td> </td></tr></table></a>");
		buttonImportResourceHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().requestNavigateToTransitImport(GlobalParameter.TRANSIT_RESOURCE);
			}
		});

        HTML buttonEditResourceHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>VIEW/EDIT</b></td></tr><tr><td>Resource</td></tr><tr><td> </td></tr></table></a>");
        buttonEditResourceHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().showTransitResourceWindow();
			}
		});
		
        HTML buttonConfirmResourceHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\"><tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>CONFIRM</b></td></tr><tr><td>Resource</td></tr><tr><td> </td></tr></table></a>");
        buttonConfirmResourceHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().confirmTransitResources();
			}
		});
		
        HTML buttonPrintBQReportHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td rowspan=3><img src=\""+PDF_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>PRINT</b></td></tr><tr><td>BQ Items</td></tr><tr><td>Report</td></tr></table></a>");
        buttonPrintBQReportHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().printMasterReport(globalSectionController.getJob().getJobNumber());
			}
		});
        
        HTML buttonPrintResourceReportHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td rowspan=3><img src=\""+PDF_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>PRINT</b></td></tr><tr><td>Resource</td></tr><tr><td>Report</td></tr></table></a>");
        buttonPrintResourceReportHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().printResourceReport(globalSectionController.getJob().getJobNumber());
			}
		});
        
        HTML buttonCompleteTransitHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>COMPLETE</b></td></tr><tr><td>Transit</td></tr><tr><td> </td></tr></table></a>");
        buttonCompleteTransitHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().completeTransit();
			}
		});
        
        HTML buttonPostBudgetHtml = new HTML("<a href=\"#\" class=\"flowChartTransitButton\"><table class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td rowspan=3><img src=\""+TRANSIT_ICON+"\" style=\"width: 20px; height: 20px;\"></td>"
        		+ "<td><b>POST</b></td></tr><tr><td>Budget</td></tr><tr><td> </td></tr></table></a>");
        buttonPostBudgetHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if(globalSectionController.getJob()==null)
					MessageBox.alert("This can only be accessed after a job is selected");
				else
					globalSectionController.getTreeSectionController().postBudget();
			}
		});
		
        //Setup operationPanel & flowChartPanel
		operationPanel.add(buttonTransitHeaderHtml, 30, 40);
		operationPanel.add(buttonImportBQItemsHtml, 150, 40);
		operationPanel.add(buttonViewBQItemsHtml, 270, 40);
		operationPanel.add(buttonImportResourceHtml, 390, 40);
		operationPanel.add(buttonEditResourceHtml, 510, 40);
		operationPanel.add(buttonConfirmResourceHtml, 630, 40);
		operationPanel.add(buttonPrintBQReportHtml, 750, 3);
		operationPanel.add(buttonPrintResourceReportHtml, 750, 77);
		operationPanel.add(buttonCompleteTransitHtml, 630, 185);
		operationPanel.add(buttonPostBudgetHtml, 510, 185);
		transitPanel.add(operationPanel, new AbsoluteLayoutData(10, 40));
	}
	
	/**
	 * @author Henry Lai
	 * Created on 20-10-2014 
	 */
	private void addTransitEnquiryPanel(){
		
		AbsolutePanel enquiryPanel = new AbsolutePanel();
		enquiryPanel.setSize("830px", "150px");
		enquiryPanel.setTitle("Enquiry");
		Label enquiryPanelTitle = new Label("Enquiry");
		enquiryPanelTitle.setStyle("font-weight: bold; font-size:small;");
		enquiryPanel.add(enquiryPanelTitle, 10, 50);
		
        HTML buttonTransitStatusEnquiryHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><table width=\"160\" class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td width=\"20\" rowspan=2><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\"></td><td align=\"center\">Transit Status</td>"
				+ "<tr><td align=\"center\">Enquiry</td></tr></table></a>");
        buttonTransitStatusEnquiryHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				globalSectionController.getTreeSectionController().navigateToTransitStatus();
			}
		});
        
        HTML buttonResourceCodeMatchingHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><table width=\"160\" class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td width=\"20\" rowspan=2><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\"></td><td align=\"center\">Resource Code</td>"
				+ "<tr><td align=\"center\">Matching</td></tr></table></a>");
        buttonResourceCodeMatchingHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				globalSectionController.getTreeSectionController().navigateToTransitCodeMatching();
			}
		});
        
        HTML buttonUnitCodeMatchingHtml = new HTML("<a href=\"#\" class=\"flowChartButton\"><table width=\"160\" class=\"flowChartTransitButtonTable\">"
        		+ "<tr><td width=\"20\" rowspan=2><img src=\""+ENQUIRY_ICON+"\" style=\"width: 20px; height: 20px;\"></td><td align=\"center\">Unit Code</td>"
				+ "<tr><td align=\"center\">Matching</td></tr></table></a>");
        buttonUnitCodeMatchingHtml.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				globalSectionController.getTreeSectionController().navigateToTransitUomMatching();
			}
		});

        enquiryPanel.add(buttonTransitStatusEnquiryHtml, 130,90);
        enquiryPanel.add(buttonResourceCodeMatchingHtml, 330,90);
        enquiryPanel.add(buttonUnitCodeMatchingHtml, 530,90);
		
		//Setup flowChartPanel
        transitPanel.add(enquiryPanel, new AbsoluteLayoutData(10, 20));
	}
	
	
	/**
	 * @author Henry Lai
	 * Created on 25-11-2014 
	 */
	private void addPaymentTermsInfoTips(){
		
		AbsolutePanel paymentTermsInfoTipsPanel = new AbsolutePanel();
		paymentTermsInfoTipsPanel.setSize("800px", "200px");
		paymentTermsInfoTipsPanel.setTitle("Payment Terms Info Tips");
		Label paymentTermsInfoTipsPanelTitle = new Label("Payment Terms Info Tips");
		paymentTermsInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		paymentTermsInfoTipsPanel.add(paymentTermsInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(paymentTermsInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML paymentTermsInfoTipsPanelHtml = new HTML(
				"<table width=\"250\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\"><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS0</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Manual Input Due Date</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS1</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Paid + 7 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS2</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Paid + 14 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS3</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when IPA Received + 56 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS4</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Invoice Received + 28 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS5</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Invoice Received + 30 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS6</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Invoice Received + 45 days</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">QS7</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pay when Invoice Received + 60 days</td>"
				+ "</tr>"
				+ "</table>"
				);

		paymentTermsInfoTipsPanel.add(paymentTermsInfoTipsPanelHtml,40,40);
		
		tipsPanelAllContent.add(paymentTermsInfoTipsPanel, 10, 20);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 25-11-2014 
	 */
	private void addPaymentStatusInfoTips(){
		
		AbsolutePanel paymentStatusInfoTipsPanel = new AbsolutePanel();
		paymentStatusInfoTipsPanel.setSize("800px", "200px");
		paymentStatusInfoTipsPanel.setTitle("Payment Status Info Tips");
		Label paymentStatusInfoTipsPanelTitle = new Label("Payment Status Info Tips");
		paymentStatusInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		paymentStatusInfoTipsPanel.add(paymentStatusInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(paymentStatusInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML paymentStatusInfoTipsPanelFlowHtml = new HTML(
				"<table width=\"320\" style=\"font-size: 13px\">"
				+ "<tr>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#007D00><b>PND</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#E68550><b>SBM</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#E68550><b>PCS</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#E68550><b>UFR</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#707070><b>APR</b></font></td>"
				+ "</tr>"
				+ "</table>"
				);
		
		HTML paymentStatusInfoTipsPanelListHtml = new HTML(
				"<table width=\"250\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\" ><b>Status</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"200\" ><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">PND</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Pending</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SBM</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Submitted</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">PCS</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Waiting for Posting</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">UFR</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Under Finance Review</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">APR</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Posted to Finance</td>"
				+ "</tr>"
				+ "</table>"
				);

		paymentStatusInfoTipsPanel.add(paymentStatusInfoTipsPanelFlowHtml,40,40);
		paymentStatusInfoTipsPanel.add(paymentStatusInfoTipsPanelListHtml,40,80);

		tipsPanelAllContent.add(paymentStatusInfoTipsPanel, 10, 220);
	}
	

	/**
	 * @author koeyyeung
	 * Created on 18th June, 2015 
	 */
	private void addPaymentStatusCodeInfoTips(){
		
		AbsolutePanel paymentStatusCodeInfoTipsPanel = new AbsolutePanel();
		paymentStatusCodeInfoTipsPanel.setSize("800px", "200px");
		paymentStatusCodeInfoTipsPanel.setTitle("Payment Terms Info Tips");
		Label paymentTermsInfoTipsPanelTitle = new Label("Payment Status Code Info Tips");
		paymentTermsInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		paymentStatusCodeInfoTipsPanel.add(paymentTermsInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(paymentStatusCodeInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML paymentStatusCodeInfoTipsPanelHtml = new HTML(
				"<table width=\"250\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\"><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">A</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Approved for Payment</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">P</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Paid in Full</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">H</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Held/Pending Approval</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">#</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Check being Written</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">%</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Withholding Applies</td>"
				+ "</tr>"
				+ "</table>"
				);

		paymentStatusCodeInfoTipsPanel.add(paymentStatusCodeInfoTipsPanelHtml,40,40);
		
		tipsPanelAllContent.add(paymentStatusCodeInfoTipsPanel, 10, 420);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 26-11-2014 
	 */
	private void addApprovalTypeInfoTips(){
		
		AbsolutePanel approvalTypeInfoTipsPanel = new AbsolutePanel();
		approvalTypeInfoTipsPanel.setSize("800px", "300px");
		approvalTypeInfoTipsPanel.setTitle("Approval Type Info Tips");
		Label approvalTypeInfoTipsPanelTitle = new Label("Approval Type Info Tips");
		approvalTypeInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		approvalTypeInfoTipsPanel.add(approvalTypeInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(approvalTypeInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML approvalTypeInfoTipsPanelHtml = new HTML(
				"<table width=\"700\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"300\"><b>Description</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"350\"><b>Details</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SP</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Interim Payment</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For interim payment of awarded subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SF</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Final Account</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For final account of awarded subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">NP</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Payment Requisition for subcontract not yet awarded</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For non-awarded subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SM</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Variation to SC Award (Cumulative amount)</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For addendum</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SL</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SC Addendum (> 25% of Original SC Sum or HKD$250,000)</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For addendum with amount >25% of Original SC Sum or >HKD$250,000</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">VA</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Split Subcontract</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For split subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">VB</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Terminate Subcontract</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For terminate subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">FR</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Under Finance Review</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For finance review with Payment Term 'QS0'</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">AW</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SC Award</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For awarded subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">ST</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">SC Award over budget (Over-budget amount)</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For awarded subcontract with over-budget amount</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">V5</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Varied SC Award</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For awarded subcontract with non-standard term payment</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">V6</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Varied SC Award and over-budget</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For awarded subcontract with non-standard term payment and over-budget amount</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">RM</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Negative Main Contract Certificate</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">For Main Contract Certificate with negative net certificate amount</td>"
				+ "</tr>"
				+ "</table>"
				);

		approvalTypeInfoTipsPanel.add(approvalTypeInfoTipsPanelHtml,40,40);
		
		tipsPanelAllContent.add(approvalTypeInfoTipsPanel, 10, 620);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 26-11-2014 
	 */
	private void addMainContractCertificateStatusInfoTips(){
		
		AbsolutePanel mainContractCertificateStatusInfoTipsPanel = new AbsolutePanel();
		mainContractCertificateStatusInfoTipsPanel.setSize("800px", "240px");
		mainContractCertificateStatusInfoTipsPanel.setTitle("Main Contract Certificate Status Info Tips");
		Label mainContractCertificateStatusInfoTipsPanelTitle = new Label("Main Contract Certificate Status Info Tips");
		mainContractCertificateStatusInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		mainContractCertificateStatusInfoTipsPanel.add(mainContractCertificateStatusInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(mainContractCertificateStatusInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML mainContractCertificateStatusInfoTipsPanelFlowHtml = new HTML(
				"<table width=\"250\" style=\"font-size: 13px\">"
				+ "<tr>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>100</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>120</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>150</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>300</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButtonGray\"><b>400</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-4.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButtonOrange\"><b>200</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-5.png\" style=\"width: 35px;\"></td>"
				+ "<td>&nbsp;</td><td>&nbsp;</td>"
				+ "</tr>"
				+ "</table>"
				);
		
		HTML mainContractCertificateStatusInfoTipsPanelHtml = new HTML(
				"<table width=\"700\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\"><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">100</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Certificate(IPC) Created</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">120</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">IPA Sent</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">150</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Certificate(IPC) Confirmed</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">200</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Certificate(IPC) Waiting for special approval</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">300</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Certificate(IPC) Posted to Finance's Account Receivable(AR) Ledger</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">400</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Certifited Amount Received <BR> Status '400' was used for historical Main Contract Certificates which were created in JDE QS System."
				+ "</td>"
				+ "</tr>"
				+ "</table>"
				);

		mainContractCertificateStatusInfoTipsPanel.add(mainContractCertificateStatusInfoTipsPanelFlowHtml,40,40);
		mainContractCertificateStatusInfoTipsPanel.add(mainContractCertificateStatusInfoTipsPanelHtml,40,110);
		
		tipsPanelAllContent.add(mainContractCertificateStatusInfoTipsPanel, 10, 920);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 26-11-2014 
	 */
	private void addSubcontractStatusInfoTips(){
		
		AbsolutePanel subcontractStatusInfoTipsPanel = new AbsolutePanel();
		subcontractStatusInfoTipsPanel.setSize("800px", "190px");
		subcontractStatusInfoTipsPanel.setTitle("Subcontract Status Info Tips");
		Label subcontractStatusInfoTipsPanelTitle = new Label("Subcontract Status Info Tips");
		subcontractStatusInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		subcontractStatusInfoTipsPanel.add(subcontractStatusInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(subcontractStatusInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML subcontractStatusInfoTipsPanelFlowHtml = new HTML(
				"<table width=\"250\" style=\"font-size: 13px\">"
				+ "<tr>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>100</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>160</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>330</b></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><b>500</b></td>"
				+ "</tr>"
				+ "</table>"
				);
		
		HTML subcontractStatusInfoTipsPanelHtml = new HTML(
				"<table width=\"250\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\"><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">100</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Newly Created Subcontract</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">160</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Tender Analysis Ready</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">330</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Subcontract Award request Submitted</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">340</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Subcontract Award request Rejected</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">500</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Awarded Subcontract</td>"
				+ "</tr>"
				+ "</table>"
				);

		subcontractStatusInfoTipsPanel.add(subcontractStatusInfoTipsPanelFlowHtml,40,40);
		subcontractStatusInfoTipsPanel.add(subcontractStatusInfoTipsPanelHtml,40,80);
		
		tipsPanelAllContent.add(subcontractStatusInfoTipsPanel, 10, 1160);
	}
	
	/**
	 * @author Henry Lai
	 * Created on 26-11-2014 
	 */
	private void addRepackagingStatusInfoTips(){
		
		AbsolutePanel repackagingStatusInfoTipsPanel = new AbsolutePanel();
		repackagingStatusInfoTipsPanel.setSize("800px", "190px");
		repackagingStatusInfoTipsPanel.setTitle("Repackaging Status Info Tips");
		Label repackagingStatusInfoTipsPanelTitle = new Label("Repackaging Status Info Tips");
		repackagingStatusInfoTipsPanelTitle.setStyle("font-weight: bold; font-size:small;");
		repackagingStatusInfoTipsPanel.add(repackagingStatusInfoTipsPanelTitle, 10, 10);
		DOM.setStyleAttribute(repackagingStatusInfoTipsPanel.getElement(), "border", "1px solid #B0C4DE");
		
		HTML repackagingStatusInfoTipsPanelFlowHtml = new HTML(
				"<table width=\"320\" style=\"font-size: 13px\">"
				+ "<tr>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#707070><b>900</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#007D00><b>100</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#007D00><b>200</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#E68550><b>300</b></font></td>"
				+ "<td><img src=\""+GlobalParameter.BASED_URL+"images/flow-chart-arrow-3.png\" style=\"width: 35px;\"></td>"
				+ "<td width=\"40\" class=\"flowChartTipsCodesButton\"><font color=#707070><b>900</b></font></td>"
				+ "</tr>"
				+ "</table>"
				);
		
		
		HTML repackagingStatusInfoTipsPanelHtml = new HTML(
				"<table width=\"250\" boarder=\"1\" style=\"font-size: 11px; border: 1px solid #B0C4DE; border-collapse: collapse;\">"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\" width=\"50\"><b>Code</b></td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\"><b>Description</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">100</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Unlocked</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">200</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Updated</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">300</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Snapshot Generated</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">900</td>"
				+ "<td style=\"border: 1px solid #B0C4DE;\">Locked</td>"
				+ "</tr>"
				+ "</table>"
				);
		
		repackagingStatusInfoTipsPanel.add(repackagingStatusInfoTipsPanelFlowHtml,40,40);
		repackagingStatusInfoTipsPanel.add(repackagingStatusInfoTipsPanelHtml,40,80);

		tipsPanelAllContent.add(repackagingStatusInfoTipsPanel, 10, 1350);
	}
	
	private void setupAdminToolbar(){
		Toolbar adminToolbar = new Toolbar();

		ToolbarButton addButton = new ToolbarButton("Add");
		addButton.setIconCls("add-button-icon");
		addButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				showAddNewMessageWindow();
			}
		});

		ToolbarButton deleteButton = new ToolbarButton("Delete");
		deleteButton.setIconCls("remove-button-icon");
		deleteButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				beforeDelete();
			}
		});

		ToolbarButton updateButton = new ToolbarButton("Update");
		updateButton.setIconCls("save-button-icon");
		updateButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				updateRecords();
			}
		});

		adminToolbar.addButton(addButton);
		adminToolbar.addSeparator();
		adminToolbar.addButton(deleteButton);
		adminToolbar.addSeparator();
		adminToolbar.addButton(updateButton);
		adminToolbar.addSeparator();

		adminGridPanel.setTopToolbar(adminToolbar);
	}

	private void setupAdminPanel(){
		adminPanel = new Panel();
		adminPanel.setPaddings(0);
		adminPanel.setTitle("Administration");
		adminPanel.setIconCls("admin-icon");
		adminPanel.setLayout(new RowLayout());

		//Search Panel
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(50);
		searchPanel.setLayout(new TableLayout(8));

		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					searchAdminPanel();
			}
		};

		//Message Type
		Label messageTypeLabel = new Label("Message Type");
		messageTypeLabel.setCls("table-cell");
		searchPanel.add(messageTypeLabel);

		messageTypeComboBox.setCtCls("table-cell");
		messageTypeComboBox.setForceSelection(true);
		messageTypeComboBox.setMinChars(1);
		messageTypeComboBox.setValueField(MESSAGE_TYPE_VALUE);
		messageTypeComboBox.setDisplayField(MESSAGE_TYPE_DISPLAY);
		messageTypeComboBox.setMode(ComboBox.LOCAL);
		messageTypeComboBox.setTriggerAction(ComboBox.ALL);
		messageTypeComboBox.setValue(MessageBoard.ANNOUNCEMENT);
		messageTypeComboBox.setTypeAhead(true);
		messageTypeComboBox.setSelectOnFocus(true);
		messageTypeComboBox.setWidth(150);
		messageTypeComboBox.addListener(searchListener);

		messageTypes = new String[][]{
				{"","ALL"},
				{MessageBoard.ANNOUNCEMENT, MessageBoard.ANNOUNCEMENT},
				{MessageBoard.PROMOTION,MessageBoard.PROMOTION},
				{MessageBoard.ENHANCEMENT, MessageBoard.ENHANCEMENT}
		};
		messageTypeStore = new SimpleStore(new String[]{MESSAGE_TYPE_VALUE,MESSAGE_TYPE_DISPLAY},messageTypes);
		messageTypeComboBox.setStore(messageTypeStore);
		searchPanel.add(messageTypeComboBox);			


		//Delivery Date
		Label deliveryDateLabel = new Label("Delivery Date");
		deliveryDateLabel.setCls("table-cell");	

		deliveryDateField = new DateField("Delivery Date", "deliveryDateSearch", 100);
		deliveryDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		deliveryDateField.setFormat(GlobalParameter.DATEFIELD_DATEFORMAT);
		deliveryDateField.setCtCls("table-cell");
		deliveryDateField.addListener(searchListener);

		searchPanel.add(deliveryDateLabel);
		searchPanel.add(deliveryDateField);


		//Is Display
		Label isDisplayLabel = new Label("Is Display");
		isDisplayLabel.setCls("table-cell");
		searchPanel.add(isDisplayLabel);

		isDisplayComboBox.setCtCls("table-cell");
		isDisplayComboBox.setForceSelection(true);
		isDisplayComboBox.setMinChars(1);
		isDisplayComboBox.setValueField(IS_DISPLAY_VALUE);
		isDisplayComboBox.setDisplayField(IS_DISPLAY_DISPLAY);
		isDisplayComboBox.setMode(ComboBox.LOCAL);
		isDisplayComboBox.setTriggerAction(ComboBox.ALL);
		isDisplayComboBox.setEmptyText("ALL");
		isDisplayComboBox.setTypeAhead(true);
		isDisplayComboBox.setSelectOnFocus(true);
		isDisplayComboBox.setWidth(100);
		isDisplayComboBox.addListener(searchListener);

		isDisplays = new String[][]{
				{"","ALL"},
				{"Y", "Yes"},
				{"N", "No"}
		};
		isDisplaysStore = new SimpleStore(new String[]{IS_DISPLAY_VALUE,IS_DISPLAY_DISPLAY},isDisplays);
		isDisplayComboBox.setStore(isDisplaysStore);
		searchPanel.add(isDisplayComboBox);

		Label emptyLabel = new Label();
		emptyLabel.setWidth(20);
		searchPanel.add(emptyLabel);
		
		Button searchButton = new Button("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				searchAdminPanel();
			};
		});

		searchPanel.add(searchButton);

		setupAdminGridPanel();

		adminPanel.add(searchPanel);
		adminPanel.add(adminGridPanel);
	}


	private void setupAdminGridPanel(){	
		adminGridPanel = new EditorGridPanel();
		adminGridPanel.setBorder(false);
		adminGridPanel.setFrame(false);
		adminGridPanel.setPaddings(0);
		adminGridPanel.setAutoScroll(true);

		GridView gridView = new CustomizedGridView();
		gridView.setForceFit(true);
		adminGridPanel.setView(gridView);

		setupAdminToolbar();

		selectionModelForAdmin = new CheckboxSelectionModel();

		//Column headers
		CheckboxColumnConfig checkBoxColConfig = new CheckboxColumnConfig(selectionModelForAdmin);
		ColumnConfig titleColConfig = new ColumnConfig("Title",TITLE_RECORD_NAME,200,true);
		ColumnConfig descriptionColConfig = new ColumnConfig("Description", DESCRIPTION_RECORD_NAME, 450, true);
		ColumnConfig requestorColConfig = new ColumnConfig("Requestor", REQUESTOR_RECORD_NAME ,130,true);
		ColumnConfig dateColConfig = new ColumnConfig("Date", DATE_RECORD_NAME, 100,true);
		dateColConfig.setRenderer(new DateRenderer());
		ColumnConfig messageTypeColConfig = new ColumnConfig("Message Type", MESSAGE_TYPE_RECORD_NAME, 150,true);
		ColumnConfig isDisplayColConfig = new ColumnConfig("Is Display", IS_DISPLAY_RECORD_NAME, 100,true);
		ColumnConfig attachmentColConfig = new ColumnConfig("Click to show Attachment", "" ,120,true);
		attachmentColConfig.setRenderer(new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if(MessageBoard.ANNOUNCEMENT.equals(record.getAsString(MESSAGE_TYPE_RECORD_NAME)))
					return "<img src=\""+ATTACHMENT_IMAGE+"attachment.gif"+ "\"/>";
				else
					return "";
			}  
		});  
		
		titleColConfig.setEditor(new GridEditor(new TextField()));
		descriptionColConfig.setEditor(new GridEditor(new TextField()));
		requestorColConfig.setEditor(new GridEditor(new TextField()));
		dateColConfig.setEditor(new GridEditor(new DateField("date",GlobalParameter.DATEFIELD_DATEFORMAT)));


		//Message Type ComboBox Editor
		final String MESSAGE_TYPE_VALUE = "messageTypeValue";
		final String MESSAGE_TYPE_DISPLAY = "messageTypeDisplay"; 

		ComboBox messageTypeEditorComboBox = new ComboBox();
		messageTypeEditorComboBox.setCtCls("table-cell");
		messageTypeEditorComboBox.setForceSelection(true);
		messageTypeEditorComboBox.setMinChars(1);
		messageTypeEditorComboBox.setValueField(MESSAGE_TYPE_VALUE);
		messageTypeEditorComboBox.setDisplayField(MESSAGE_TYPE_DISPLAY);
		messageTypeEditorComboBox.setValue(MessageBoard.ANNOUNCEMENT);
		messageTypeEditorComboBox.setMode(ComboBox.LOCAL);
		messageTypeEditorComboBox.setTriggerAction(ComboBox.ALL);
		messageTypeEditorComboBox.setTypeAhead(true);
		messageTypeEditorComboBox.setSelectOnFocus(true);
		messageTypeEditorComboBox.setWidth(150);

		String[][] messageTypes = new String[][]{
				{MessageBoard.ANNOUNCEMENT, MessageBoard.ANNOUNCEMENT},
				{MessageBoard.PROMOTION,MessageBoard.PROMOTION},
				{MessageBoard.ENHANCEMENT, MessageBoard.ENHANCEMENT}
		};
		Store messageTypeStore = new SimpleStore(new String[]{MESSAGE_TYPE_VALUE,MESSAGE_TYPE_DISPLAY},messageTypes);
		messageTypeEditorComboBox.setStore(messageTypeStore);
		messageTypeColConfig.setEditor(new GridEditor(messageTypeEditorComboBox));


		//Is Display ComboBox Editor
		final String IS_DISPLAY_VALUE = "isDisplayValue";
		final String IS_DISPLAY_DISPLAY = "isDisplayDisplay"; 

		ComboBox isDisplayEditorComboBox = new ComboBox();
		isDisplayEditorComboBox.setCtCls("table-cell");
		isDisplayEditorComboBox.setForceSelection(true);
		isDisplayEditorComboBox.setMinChars(1);
		isDisplayEditorComboBox.setValueField(IS_DISPLAY_VALUE);
		isDisplayEditorComboBox.setDisplayField(IS_DISPLAY_DISPLAY);
		isDisplayEditorComboBox.setMode(ComboBox.LOCAL);
		isDisplayEditorComboBox.setTriggerAction(ComboBox.ALL);
		isDisplayEditorComboBox.setTypeAhead(true);
		isDisplayEditorComboBox.setSelectOnFocus(true);
		isDisplayEditorComboBox.setWidth(50);

		String[][] isDisplays = new String[][]{
				{"Y", "Y"},
				{"N", "N"}
		};
		Store isDisplayStore = new SimpleStore(new String[]{IS_DISPLAY_VALUE,IS_DISPLAY_DISPLAY},isDisplays);
		isDisplayEditorComboBox.setStore(isDisplayStore);
		isDisplayColConfig.setEditor(new GridEditor(isDisplayEditorComboBox));

		
		BaseColumnConfig[] columns;
		columns = new BaseColumnConfig[]{
				checkBoxColConfig,
				new RowNumberingColumnConfig(),
				titleColConfig, 
				descriptionColConfig,
				requestorColConfig,
				dateColConfig,
				messageTypeColConfig,
				isDisplayColConfig,
				attachmentColConfig
		};
		adminGridPanel.setColumnModel(new ColumnModel(columns));

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStoreForAdmin = new Store(proxy, reader);
		dataStoreForAdmin.load();
		adminGridPanel.setStore(dataStoreForAdmin);

		adminGridPanel.setSelectionModel(selectionModelForAdmin);
		
		adminGridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				if(rowIndex!=currentRowIndex){
					detailSectionController.resetGridPanel(true);
				}
			}
		});
		
		adminGridPanel.addGridCellListener(new GridCellListenerAdapter(){
			public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				if(colindex==8){	//Attachment Column
					Record record = grid.getSelectionModel().getSelected();
					if(record!=null && MessageBoard.ANNOUNCEMENT.equals(record.getAsString(MESSAGE_TYPE_RECORD_NAME))){
						currentRowIndex = rowIndex;
						String messageID = record.getAsString(MESSAGE_ID_RECORD_NAME);//get the message ID
						showMessageBoardAttachmentDetailPanel(messageID);
					}else
						detailSectionController.resetGridPanel(true);
				}
			}
		});
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				obtainMessageBoardByPage(pageNum);
			}
		});
		adminGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
	}

	public void populateMessageBoard() {
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().obtainAllDisplayMessages(new AsyncCallback<List<MessageBoard>>() {
			public void onSuccess(List<MessageBoard> messageList) {
				List<MessageBoard> announcementMessages = new ArrayList<MessageBoard>();
				for(MessageBoard message: messageList){
					if(MessageBoard.ANNOUNCEMENT.equals(message.getMessageType()))
						announcementMessages.add(message);
				}
				addAnnouncement(announcementMessages);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private void addAnnouncement(List<MessageBoard> messageList) {
		int x = 40;
		int y = 0;
		int counter = 1;
		if(messageList.size()>0){
			for(MessageBoard message: messageList){
				if(counter==1)
					x = 40;
				else if(counter==2)
					x = 370;
				else if(counter==3)
					x = 700;

				addPanel(message, x, y);

				if(counter < 3){
					counter += 1;
				}
				else if(counter==3){
					counter = 1;
					y += 300;
				}
			}
		}else{
			/*MessageBoard newMessage = new MessageBoard();
			newMessage.setMessageType(MessageBoard.ANNOUNCEMENT);
			newMessage.setDescription("There is currently no announcement.");
			newMessage.setRequestor("System");
			newMessage.setTitle("No Announcement");
			addPanel(newMessage, x, y);*/
			
			/*Timer timer = new Timer() {
				//int index =0;
				public void run() {
					// rotate
					Image backgroundImage = new Image(GlobalParameter.BASED_URL+"images/gammonLtd.gif");
					backgroundImage.setHeight("100%");
					backgroundImage.setWidth("100%");

					Panel panel = new Panel();
					panel.setSize(300, 300);
					panel.setAutoScroll(true);
					panel.setBorder(false);
					panel.setLayout(new AbsoluteLayout());
					panel.add(backgroundImage, new AbsoluteLayoutData(0, 0));

					announcementPanel.clear();
					announcementPanel.add(panel, new AbsoluteLayoutData(300, 80));
					announcementPanel.doLayout();
				}
			};
			// schedule rotation of image
			timer.scheduleRepeating(2500); //2.5s

			Image backgroundImage = new Image(GlobalParameter.BASED_URL+"images/gammon.gif");
			backgroundImage.setHeight("100%");
			backgroundImage.setWidth("100%");


			Panel panel = new Panel();
			panel.setSize(300, 300);
			panel.setAutoScroll(true);
			panel.setBorder(false);
			panel.setLayout(new AbsoluteLayout());
			panel.add(backgroundImage, new AbsoluteLayoutData(0, 0));

			announcementPanel.add(panel, new AbsoluteLayoutData(300, 80));*/
			if(tabPanel.getActiveTab()==null||tabPanel.getActiveTab().getId()=="announcement"){
				tabPanel.setActiveTab("overview");
			}
		}

		announcementPanel.doLayout();
	}

	private void addPanel(MessageBoard message, int x, int y){
		Image backgroundImage = new Image(MESSAGE_BOARD_BACKGROUND);
		backgroundImage.setHeight("100%");
		backgroundImage.setWidth("100%");

		Panel panel = new Panel();
		panel.setSize(300, 300);
		panel.setAutoScroll(true);
		panel.setBorder(false);
		panel.setLayout(new AbsoluteLayout());
		panel.add(backgroundImage, new AbsoluteLayoutData(0, 0));

		Panel titlePanel = new Panel();
		titlePanel.setLayout(new RowLayout());
		titlePanel.setBorder(false);
		titlePanel.setBodyStyle(TABLE_CELL_TRANSPARENT_STYLE);
		titlePanel.setWidth(260);

		Label titleLabel = new Label(message.getTitle()==null?"":message.getTitle());
		titleLabel.setCtCls("message-board-title");
		titlePanel.add(titleLabel);
		panel.add(titlePanel, new AbsoluteLayoutData(25, 40));

		Panel descriptionPanel = new Panel();
		descriptionPanel.setLayout(new RowLayout());
		descriptionPanel.setBorder(false);
		descriptionPanel.setBodyStyle(TABLE_CELL_TRANSPARENT_STYLE);
		descriptionPanel.setWidth(260);

		Label descriptionLabel = new Label(message.getDescription()==null?"":message.getDescription());
		descriptionLabel.setCtCls("message-board-label");
		descriptionPanel.add(descriptionLabel);
		panel.add(descriptionPanel, new AbsoluteLayoutData(25, 80));

		Panel adminPanel = new Panel();
		adminPanel.setLayout(new RowLayout());
		adminPanel.setBorder(false);
		adminPanel.setBodyStyle(TABLE_CELL_TRANSPARENT_STYLE);
		adminPanel.setWidth(90);

		Label adminLabel = new Label((message.getRequestor()==null)?"":message.getRequestor()+" "+dateToString(message.getDeliveryDate()));
		adminLabel.setCtCls("message-board-right-align-label");
		adminPanel.add(adminLabel);
		panel.add(adminPanel, new AbsoluteLayoutData(150, 230));
		
		UIUtil.maskMainPanel();
		globalSectionController.getMessageBoardAttachmentRepository().obtainAttachmentListByID(message.getId(), new AsyncCallback<List<MessageBoardAttachment>>(){

			@Override
			public void onFailure(Throwable e) {
				UIUtil.unmaskMainPanel();
				UIUtil.throwException(e);
			}

			@Override
			public void onSuccess(List<MessageBoardAttachment> result) {
				messageBoardAttachmentList = result;
				UIUtil.unmaskMainPanel();
			}
			
		});
		if(message.getId()!=null && messageBoardAttachmentList !=null && messageBoardAttachmentList.size()>0){
			Anchor detailButton = new Anchor("More...");
			detailButton.setStyleName("table-cell-small-text");
			detailButton.getElement().setId("messageID"+message.getId().toString());
			detailButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					searchImage(sender.getElement().getId().substring(9));//skip the 'messageID' String to get the id number only
				}
			});
			panel.add(detailButton, new AbsoluteLayoutData(30, 230));
		}

		announcementPanel.add(panel, new AbsoluteLayoutData(x, y));

	}

	public void searchAdminPanel(){
		MessageBoard messageBoard = new MessageBoard();
		messageBoard.setMessageType(messageTypeComboBox.getValueAsString());
		messageBoard.setDeliveryDate(deliveryDateField.getValue());
		messageBoard.setIsDisplay(isDisplayComboBox.getValueAsString());

		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().obtainMessageBoardPaginationWrapper(messageBoard, new AsyncCallback<PaginationWrapper<MessageBoard>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<MessageBoard> messageList) {
				globalSectionController.getDetailSectionController().resetGridPanel(true);
				populateAdminPanel(messageList);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private void populateAdminPanel(PaginationWrapper<MessageBoard> messageList){
		dataStoreForAdmin.removeAll();
		
		if(messageList==null || messageList.getCurrentPageContentList().size()==0){
			MessageBox.alert("No data found");
			return;
		}

		paginationToolbar.setTotalPage(messageList.getTotalPage());
		paginationToolbar.setCurrentPage(messageList.getCurrentPage());

		for(MessageBoard message: messageList.getCurrentPageContentList()){
			Record record  = recordDef.createRecord(new Object[]{
					message.getId(),
					message.getTitle(),
					message.getDescription(),
					message.getRequestor(),
					message.getDeliveryDate(),
					message.getMessageType(),
					message.getIsDisplay()
			});
			dataStoreForAdmin.add(record);
		}
	}
	
	private void obtainMessageBoardByPage(int pageNum) {
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().obtainMessageBoardListByPage(pageNum, new AsyncCallback<PaginationWrapper<MessageBoard>>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<MessageBoard> messageList) {
				populateAdminPanel(messageList);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}
	
	private void updateRecords() {
		Record[] recordList = dataStoreForAdmin.getModifiedRecords();

		List<MessageBoard> messageList = new ArrayList<MessageBoard>();

		for (Record record : recordList) {
			MessageBoard message = messageRecord(record);
			if(message==null) 
				return;
			messageList.add(message);
		}
		if (messageList.size() == 0) {
			MessageBox.alert("No data has been changed.");
			return;
		}

		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().updateMessages(messageList, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(Boolean updated) {
				if(updated){
					MessageBox.alert("Message Board has been updated successfully.");
					refreshAll();
				}
				else{
					MessageBox.alert("No record has been changed.");
					refreshAll();
				}
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private MessageBoard messageRecord(Record record) {
		MessageBoard message = new MessageBoard();

		message.setId(Long.valueOf(record.getAsInteger(MESSAGE_ID_RECORD_NAME)));
		message.setTitle(record.getAsString(TITLE_RECORD_NAME));
		message.setDescription(record.getAsString(DESCRIPTION_RECORD_NAME));
		message.setRequestor(record.getAsString(REQUESTOR_RECORD_NAME));
		message.setDeliveryDate(record.getAsDate(DATE_RECORD_NAME));
		message.setMessageType(record.getAsString(MESSAGE_TYPE_RECORD_NAME));
		message.setIsDisplay(record.getAsString(IS_DISPLAY_RECORD_NAME));

		return message;
	}

	
	private void beforeDelete(){
		if(selectionModelForAdmin.getSelections().length == 0){
			MessageBox.alert("Please select a row to delete.");
			return;
		}
		MessageBox.confirm("Message Board", "The selected rows will be deleted. Confirm? ",
				new MessageBox.ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equals("yes")){
					deleteRecord(selectionModelForAdmin.getSelections());
				}
			}
		});
	}


	private void deleteRecord(Record[] records){
		List<Long> messageIDList = new ArrayList<Long>();
		for (Record record: records)
			messageIDList.add((long) record.getAsInteger(MESSAGE_ID_RECORD_NAME));
		if (messageIDList.size() == 0)
			return;

		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().deleteMessages(messageIDList, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(Boolean deleted) {
				if(deleted){
					MessageBox.alert("Record has been deleted successfully.");
					refreshAll();
				}
				else{
					MessageBox.alert("No record has been changed.");
					refreshAll();
				}
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}
	
	private void searchImage(String messageID){
		if(messageID!=null && !"".equals(messageID)){
			UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMessageBoardAttachmentRepository().obtainAttachmentListByMessageID(Long.valueOf(messageID), new AsyncCallback<List<MessageBoardAttachment>>() {
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
				}
				public void onSuccess(List<MessageBoardAttachment> attachmentList) {
					if(attachmentList==null || attachmentList.size()==0)
						MessageBox.alert("There is no attachment.");
					else
						showImageWindow(attachmentList, 0);
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
				}
			});
		}
	}

	public void showAddNewMessageWindow(){
		Window window = new AddNewMessageWindow(globalSectionController);
		window.show();
	}
	
	public void showImageWindow(List<MessageBoardAttachment> attachmentList, int currentImageIndex) {
		Window window = new MessageBoardAttachmentWindow(globalSectionController, attachmentList, currentImageIndex);
		window.show();
	}

	private void showMessageBoardAttachmentDetailPanel(String messageID) {
		if(messageID!=null || !"".equals(messageID)){
			globalSectionController.getDetailSectionController().resetGridPanel(false);
			MessageBoardAttachmentDetailPanel messageBoardAttachmentDetailPanel = new MessageBoardAttachmentDetailPanel(globalSectionController, messageID);
			globalSectionController.getDetailSectionController().getMainPanel().setTitle("Attachment");
			globalSectionController.getDetailSectionController().setGridPanel(messageBoardAttachmentDetailPanel);
			globalSectionController.getDetailSectionController().populateGridPanel();
		}
	}
	
	public void refresh(){
		announcementPanel.clear();
		populateMessageBoard();
	}
	
	public void refreshAll(){
		refresh();
		searchAdminPanel();
	}


	private String dateToString(Date date) {
		if (date != null)
			return (new SimpleDateFormat(GlobalParameter.DATE_FORMAT)).format(date);
		else
			return "";
	}
	
	/**
	 * @author Henry Lai
	 * Created on 02-12-2014 
	 */
	public void tipsPanelScrollToPosition(int position){
		scrollToPosition=position;
		Timer t1 = new Timer()
        {
            public void run() 
            {
            	tipsScrollPanel.setScrollPosition(scrollToPosition);
            }
        };
        // 250ms delay is necessary to make the function 'setScrollPosition' works.
        t1.schedule(250);
	}

	public TabPanel getTabPanel() {
		return tabPanel;
	}

	public void setTabPanel(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

}
