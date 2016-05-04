package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.JobCostRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.detailSection.PreviewMainContractCertViewWindow;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
/**
 * @author koeyyeung
 * modified on 24 April, 2013
 */
@SuppressWarnings("unchecked")
public class MainCertificateGridPanel extends Panel implements PreferenceSetting{
	private TabPanel tabPanel;

	private GridPanel gridPanelForIPA;
	private GridPanel gridPanelForIPC;
	private GridPanel gridPanelForPostingAmount;

	private static final String ICON_MENU = "menu-show-icon";
	private static final String ICON_EXCEL = "excel-icon";
	private static final String ICON_PDF = "pdf-icon";
	private static final String ICON_DOWNLOAD = "download-icon";
	
	private static final String JOB_NUMBER = "jobNumber";
	private static final String CERTIFICATE_NUMBER = "certificateNumber";
	private static final String CLIENT_CERT_NO = "clientCertNo";
	private static final String APPLIED_MAIN_CONTRACTOR_AMOUNT = "appliedMainContractorAmount";
	private static final String APPLIED_NSCNDSC_AMOUNT =  "appliedNSCNDSCAmount";
	private static final String APPLIED_MOS_AMOUNT = "appliedMOSAmount";
	private static final String APPLIED_MAIN_CONTRACTOR_RETENTION = "appliedMainContractorRetention";
	private static final String APPLIED_MAIN_CONTRACTOR_RETENTION_RELEASED = "appliedMainContractorRetentionReleased";
	private static final String APPLIED_RETENTION_FOR_NSCNDSC = "appliedRetentionforNSCNDSC";
	private static final String APPLIED_RETENTION_FOR_NSCNDSC_RELEASED = "appliedRetentionforNSCNDSCReleased";
	private static final String APPLIED_MOS_RETENTION= 	"appliedMOSRetention";
	private static final String APPLIED_MOS_RETENTION_RELEASED = "appliedMOSRetentionReleased";
	private static final String APPLIED_CONTRA_CHARGE_AMOUNT = "appliedContraChargeAmount";
	private static final String APPLIED_ADJUSTMENT_AMOUNT = "appliedAdjustmentAmount";
	private static final String APPLIED_ADVANCE_PAYMENT = "appliedAdvancePayment";
	private static final String APPLIED_CPF_AMOUNT = "appliedCPFAmount";
	private static final String IPA_SUBMISSION_DATE =	"ipaSubmissionDate";
	@SuppressWarnings("unused")
	private static final String IPD_SENTOUT_DATE= "ipaSentoutDate";
	private static final String CERTIFIED_MAIN_CONTRACTOR_AMOUNT = "certifiedMainContractorAmount";
	private static final String CERTIFIED_NSCNDSC_AMOUNT = "certifiedNSCNDSCAmount";
	private static final String CERTIFIED_MOS_AMOUNT = "certifiedMOSAmount";
	private static final String CERTIFIED_MAIN_CONTRACTOR_RETENTION = "certifiedMainContractorRetention";
	private static final String CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED = "certifiedMainContractorRetentionReleased";
	private static final String CERTIFIED_RETENTION_FOR_NSCNDSC = "certifiedRetentionforNSCNDSC";
	private static final String CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED = "certifiedRetentionforNSCNDSCReleased";
	private static final String CERTIFIED_MOS_RETENTION = "certifiedMOSRetention";
	private static final String CERTIFIED_MOS_RETENTION_RELEASED = "certifiedMOSRetentionReleased";
	private static final String CERTIFIED_CONTRA_CHARGE_AMOUNT = "certifiedContraChargeAmount";
	private static final String CERTIFIED_ADJUSTMENT_AMOUNT = "certifiedAdjustmentAmount";
	private static final String CERTIFIED_ADVANCE_PAYMENT = "certifiedAdvancePayment";
	private static final String CERTIFIED_CPF_AMOUNT = "certifiedCPFAmount";
	private static final String GST_RECEIVABLE = "gstReceivable";
	private static final String GST_PAYABLE = "gstPayable";
	private static final String CERTIFICATE_STATUS = "certificateStatus";
	private static final String CERTIFICATE_STATUS_DESC = "certificateStatusDescription";
	private static final String AR_DOC_NUMBER = "arDocNumber";
	private static final String CERTIFICATE_ISSUE_DATE = "certificateIssueDate";
	private static final String AS_AT_DATE = "asAtDate";
	private static final String CERTIFICATE_STATUS_CHANGE_DATE = "certificateStatusChangeDate";
	private static final String CERTIFICATE_DUE_DATE = "certificateDueDate";
	private static final String REMARK = "remark";
	private static final String NET_MOVEMENT_AMOUNT = "Net Movement Amount";
	private static final String NET_GST_RECEIVABLE_MOVEMENT_AMOUNT = "Net GST Receivable Movement Amount";
	private static final String NET_GST_PAYABLE_MOVEMENT_AMOUNT = "Net GST Payable Movement Amount";
	private static final String ACTUAL_RECEIPT_DATE = "Actual Receipt Date";

	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(JOB_NUMBER),
					new StringFieldDef(CERTIFICATE_NUMBER),
					new StringFieldDef(CLIENT_CERT_NO),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_AMOUNT),
					new StringFieldDef(APPLIED_NSCNDSC_AMOUNT),
					new StringFieldDef(APPLIED_MOS_AMOUNT),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_RETENTION),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_RETENTION_RELEASED),
					new StringFieldDef(APPLIED_RETENTION_FOR_NSCNDSC),
					new StringFieldDef(APPLIED_RETENTION_FOR_NSCNDSC_RELEASED),
					new StringFieldDef(APPLIED_MOS_RETENTION),
					new StringFieldDef(APPLIED_MOS_RETENTION_RELEASED),
					new StringFieldDef(APPLIED_CONTRA_CHARGE_AMOUNT),
					new StringFieldDef(APPLIED_ADJUSTMENT_AMOUNT),
					new StringFieldDef(APPLIED_ADVANCE_PAYMENT),
					new StringFieldDef(APPLIED_CPF_AMOUNT),
					new StringFieldDef(IPA_SUBMISSION_DATE),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_AMOUNT),
					new StringFieldDef(CERTIFIED_NSCNDSC_AMOUNT),
					new StringFieldDef(CERTIFIED_MOS_AMOUNT),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_RETENTION),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED),
					new StringFieldDef(CERTIFIED_RETENTION_FOR_NSCNDSC),
					new StringFieldDef(CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED),
					new StringFieldDef(CERTIFIED_MOS_RETENTION),
					new StringFieldDef(CERTIFIED_MOS_RETENTION_RELEASED),
					new StringFieldDef(CERTIFIED_CONTRA_CHARGE_AMOUNT),
					new StringFieldDef(CERTIFIED_ADJUSTMENT_AMOUNT),
					new StringFieldDef(CERTIFIED_ADVANCE_PAYMENT),
					new StringFieldDef(CERTIFIED_CPF_AMOUNT),
					new StringFieldDef(GST_RECEIVABLE),
					new StringFieldDef(GST_PAYABLE),
					new StringFieldDef(CERTIFICATE_STATUS),
					new StringFieldDef(CERTIFICATE_STATUS_DESC),
					new StringFieldDef(AR_DOC_NUMBER),
					new StringFieldDef(CERTIFICATE_ISSUE_DATE),
					new StringFieldDef(AS_AT_DATE),
					new StringFieldDef(CERTIFICATE_STATUS_CHANGE_DATE),
					new StringFieldDef(CERTIFICATE_DUE_DATE),
					new StringFieldDef(REMARK),
					new StringFieldDef(NET_MOVEMENT_AMOUNT),
					new StringFieldDef(NET_GST_RECEIVABLE_MOVEMENT_AMOUNT),
					new StringFieldDef(NET_GST_PAYABLE_MOVEMENT_AMOUNT),
					new StringFieldDef(ACTUAL_RECEIPT_DATE)
			});

	private Store dataStore;

	private PaginationToolbar paginationToolbarForIPA;
	private PaginationToolbar paginationToolbarForIPC;
	private PaginationToolbar paginationToolbarForPostingAmount;

	private RowSelectionModel rowSelectionModelForIPA;
	private RowSelectionModel rowSelectionModelForIPC;
	private RowSelectionModel rowSelectionModelForPostingAmount;

	private String currentGridPanel;

	private final String screenName = "main-cert-grid";

	private ToolbarButton mainContractCertificateReportButton;
	private ToolbarButton mainContractCertificateButton;
	private ToolbarButton viewButton;
	private ToolbarButton editButton;
	private ToolbarButton addButton;
	private ToolbarButton inputContraChargeButton;
	private ToolbarButton sentIPAButton;
	private ToolbarButton confirmButton;
	private ToolbarButton resetButton;
	private ToolbarButton postButton;
	private ToolbarButton attachmentButton;
	private ToolbarButton refreshButton;
	private ToolbarButton retentionReleaseScheduleButton;
	private ToolbarButton tipsButton;
	
	private MenuItem mainContractCertificateReportAllButton;
	private Item mainContractCertificateReportAllButtonPDF;
	private Item mainContractCertificateReportAllButtonExcel;

	private MenuItem mainContractCertificateReportMainButton;
	private Item mainContractCertificateReportMainButtonPDF;
	private Item mainContractCertificateReportMainButtonExcel;

	private MenuItem mainContractCertificateReportRetentionButton;
	private Item mainContractCertificateReportRetentionButtonPDF;
	private Item mainContractCertificateReportRetentionButtonExcel;

	private MenuItem mainContractCertificateReportContraChargeButton;
	private Item mainContractCertificateReportContraChargeButtonPDF;
	private Item mainContractCertificateReportContraChargeButtonExcel;

	private Item mainContractCertificateExcelButton;
	private Item mainContractCertificatePDFButton;

	private ToolbarButton previewMainContractCertificateButton;
	private GlobalSectionController globalSectionController;

	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private JobCostRepositoryRemoteAsync jobCostRepository;
	private List<String> accessRightsList;

	private Integer currentPage;
	private Integer totalPage;

	private Integer selectedRowIndex;
	private String mainCertNo;
	private String mainCertStatus;

	// added by brian on 20110310
	// store whether it is Data Convention
	private boolean isAfterDataConvertion;

	private MainContractCertificate selectedMainCert;
	private MainContractCertificate previousMainCert;
	private MainContractCertificate latestMainCert;
	private MainCertificateGridPanel mainPanel;
	private GlobalMessageTicket globalMessageTicket;
	
	public static PaginationWrapper<MainContractCertificate> mainCertWrapper = null;
	public static MainContractCertificate mainCert;
	
	public static final int FIRSTPAGE = 0;

	private Double totalPostContraChargeAmt;
	
	public MainCertificateGridPanel(final GlobalSectionController globalSectionController){
		super();
		this.globalSectionController = globalSectionController;
		setupUI();
	}

	private void setupUI(){
		// Repository instantiation
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		jobCostRepository = globalSectionController.getJobCostRepository();

		mainPanel = this;
		this.setBorder(false);
		this.setAutoScroll(true);
		this.setLayout(new RowLayout());

		globalMessageTicket = new GlobalMessageTicket();

		tabPanel = new TabPanel();
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setResizeTabs(true);
		tabPanel.setMinTabWidth(145);
		tabPanel.setTabWidth(150);
		tabPanel.setActiveTab(1);
		
		tabPanel.addListener(new TabPanelListenerAdapter(){
			public void onTabChange(TabPanel tab, Panel panel) {
				if(selectedRowIndex!=null&&rowSelectionModelForIPA!=null&&rowSelectionModelForIPC!=null&&rowSelectionModelForPostingAmount!=null){
					rowSelectionModelForIPC.selectRow(selectedRowIndex);
					if("IPA".equals(panel.getTitle())){
						//highlight the selected row
						rowSelectionModelForIPA.selectRow(selectedRowIndex);
						//set selected row index 
						gridPanelForIPA.getSelectionModel().selectRow(selectedRowIndex);
					}else{
						rowSelectionModelForPostingAmount.selectRow(selectedRowIndex);
						gridPanelForPostingAmount.getSelectionModel().selectRow(selectedRowIndex);
					}
				}
			}
		});

		setupToolbar();
		setupGridForIPA();
		setupGridForIPC();
		setupGridForPostingAmount();
		
		mainPanel.add(tabPanel);
	}

	private void setupToolbar(){
		final Toolbar toolbar = new Toolbar();
		
		setupMainContractCertReportButton();
		setupMainContractCertButton();
		setupPreviewMainContractCertButton();
		
		/**
		 * @author koeyyeung
		 * created on 19 Jun, 2013
		 **/
		viewButton = new ToolbarButton("View");
		viewButton.setIconCls("filter-icon");
		viewButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();

				if (mainCertNo!=null)
					globalSectionController.showRevisieIPACert(mainPanel,Integer.valueOf(mainCertNo));
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});
		
		ToolTip viewToolTip = FieldFactory.createToolTip();
		viewToolTip.setTitle("View");
		viewToolTip.setHtml("View Main Certificate");
		viewToolTip.applyTo(viewButton);	
		
		editButton = new ToolbarButton();
		editButton.setText("Edit"); 
		editButton.setIconCls("edit-button-icon");
		editButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();

				/**
				 * @author tikywong
				 * modified on 3 February, 2012
				 * 
				 * @author koeyyeung
				 * modified on 24 April, 2013
				 * 
				 */
				if (mainCertNo!=null)
					globalSectionController.showRevisieIPACert(mainPanel,Integer.valueOf(mainCertNo));
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		ToolTip selectToolTip = FieldFactory.createToolTip();
		selectToolTip.setTitle("Edit");
		selectToolTip.setHtml("Edit Main Certificate for change");
		selectToolTip.applyTo(editButton);		

		addButton = new ToolbarButton();
		addButton.setText("Add");
		addButton.setIconCls("add-button-icon");
		addButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskMainPanel(GlobalParameter.LOADING_MSG);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getMainContractCertificateRepository().getMainContractCertificateList(globalSectionController.getJob().getJobNumber(), new AsyncCallback<List<MainContractCertificate>>(){

					public void onFailure(Throwable e) {
						UIUtil.unmaskMainPanel();
						UIUtil.checkSessionTimeout(e,true,globalSectionController.getUser(),"MainCertificateGridPanel.mainCerticateGridPanel().mainContractCertificateRepository.getMainContractCertificateList");
					}

					public void onSuccess(List<MainContractCertificate> fullCertList) {
						int newCertNo = 0;

						if (fullCertList!=null)
							for (MainContractCertificate aCert:fullCertList){
								if (aCert.getCertificateNumber()>newCertNo)
									newCertNo=aCert.getCertificateNumber().intValue();
								if (aCert.getCertificateStatus()==null||"".equals(aCert.getCertificateStatus().trim())||new Integer(aCert.getCertificateStatus())<200){
									MessageBox.alert("The Main Contract Certificate is not confimed yet.");
									UIUtil.unmaskMainPanel();
									return;
								}else if (aCert.getCertificateStatus()==null||"".equals(aCert.getCertificateStatus().trim())||new Integer(aCert.getCertificateStatus())==200){
									MessageBox.alert("The Main Contract Certificate is 'Waiting for Approval'.");
									UIUtil.unmaskMainPanel();
									return;
								}
							}
						UIUtil.unmaskMainPanel();
						globalSectionController.showAddIPACert(mainPanel,new Integer(newCertNo+1));

					}
				});
			}
		});

		//Set the tooltip for the add button
		ToolTip addToolTip = FieldFactory.createToolTip();
		addToolTip.setTitle("Add");
		addToolTip.setHtml("Add IPA/Main Certificate");
		addToolTip.applyTo(addButton);	

		//Input Contra Charge Button
		inputContraChargeButton = new ToolbarButton();
		inputContraChargeButton.setText("Input Contra Charge");
		inputContraChargeButton.setIconCls("edit-button-icon");
		inputContraChargeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(selectedRowIndex != null){
					showContraChargeDetail();
				}else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the add button
		ToolTip inputContraChargeToolTip = FieldFactory.createToolTip();
		inputContraChargeToolTip.setTitle("Input Contra Charge");
		inputContraChargeToolTip.setHtml("Input Contra Charge/Main Certificate");
		inputContraChargeToolTip.applyTo(inputContraChargeButton);	

		//Sent out IPA Button
		sentIPAButton = new ToolbarButton();
		sentIPAButton.setText("Sent Out IPA");
		sentIPAButton.setIconCls("send-icon");
		sentIPAButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(selectedRowIndex != null){
					if (!"100".equals(mainCertStatus))
						MessageBox.alert("The current status of the selected Main Contract Certificate is not 100(Cert Created).");
					else
						sendOutIPA();
				}else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the add button
		ToolTip sentIPAToolTip = FieldFactory.createToolTip();
		sentIPAToolTip.setTitle("Sent Out IPA");
		sentIPAToolTip.setHtml("Sent Out IPA/Main Certificate");
		sentIPAToolTip.applyTo(sentIPAButton);	


		//Confirm Certificate Button
		confirmButton = new ToolbarButton();
		confirmButton.setText("Confrim");
		confirmButton.setIconCls("confirm-icon");
		confirmButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				SessionTimeoutCheck.renewSessionTimer();
				if(selectedRowIndex != null){

					// add validation by brian on 20110310 - start
					// change isAfterDataConvertion to TRUE if data convertion
					checkIsAfterDataConvertion();
					if(isAfterDataConvertion && !checkValidation())
						return;

					if (!MainContractCertificate.IPA_SENT.equals(mainCertStatus))
						MessageBox.alert("The selected Main Contract Certificate has not sent out IPA yet. Status has to be 120 to be confirmed.");
					else{
						if(selectedMainCert.getCertDueDate()==null)
							MessageBox.alert("The Certificate Due Date is missing from the selected Main Contract Certificate.");
						else if (selectedMainCert.getCertIssueDate()==null)
							MessageBox.alert("The Certificate Issue Date is missing from the selected Main Contract Certificate.");
						else if (selectedMainCert.getCertAsAtDate()==null)
							MessageBox.alert("The As At Date is missing from the selected Main Contract Certificate.");
						else
							globalSectionController.getRetentionReleaseScheduleRepository().obtainCumulativeRetentionReleaseByJob(selectedMainCert, new AsyncCallback<String>() {
								public void onFailure(Throwable e) {
									UIUtil.throwException(e);
								}
								public void onSuccess(String errorMessage) {
									if (errorMessage.length()==0)
										confirmCertificate();
									else
										MessageBox.alert(errorMessage);

								}

							});
					}
				}else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the add button
		ToolTip confirmToolTip = FieldFactory.createToolTip();
		confirmToolTip.setTitle("Confirm Certificate");
		confirmToolTip.setHtml("Confirm Certificate/Main Certificate");
		confirmToolTip.applyTo(confirmButton);

		//Reset Certificate Button
		resetButton = new ToolbarButton();
		resetButton.setText("Reset");
		resetButton.setIconCls("reset-icon");
		resetButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(selectedRowIndex != null){
					if (!MainContractCertificate.CERT_CONFIRMED.equals(mainCertStatus))
						MessageBox.alert("The selected Main Contract Certificate has not been reset yet. Status has to be 150 to be reset.");
					else
						resetCertificate();
				}else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the add button
		ToolTip restToolTip = FieldFactory.createToolTip();
		restToolTip.setTitle("Reset Certificate");
		restToolTip.setHtml("Reset Certificate/Main Certificate");
		restToolTip.applyTo(resetButton);	

		// added by brian on 20110310
		isAfterDataConvertion = false;

		//Post Certificate Button
		postButton = new ToolbarButton();
		postButton.setText("Post");
		postButton.setIconCls("post-icon");
		postButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(selectedRowIndex != null){
					if (!MainContractCertificate.CERT_CONFIRMED.equals(mainCertStatus))
						MessageBox.alert("The selected Main Contract Certificate has not been confirmed yet. Status has to be 150 to be posted.");
					else{
						Double movement = obtainMainCertNetAmountMovement(latestMainCert, previousMainCert);
						Double netGstReceivable = RoundingUtil.round(latestMainCert.getGstReceivable() - previousMainCert.getGstReceivable(), 2);
						Double netGstPayable = RoundingUtil.round(latestMainCert.getGstPayable() - previousMainCert.getGstPayable(), 2);
							
						if(movement!=null){
							if(!"14".equals(globalSectionController.getJob().getJobNumber().substring(0, 2)) &&
								(netGstReceivable!=0.00 || netGstPayable != 0.00)){
									MessageBox.alert("Alert", 
													"Main Contract Certificate Net Movement Amount (without GST): $"+RoundingUtil.round(movement.doubleValue(), 2)+"<br/>" +
													"Net GST Receivable Movement Amount: $" + netGstReceivable + "<br/>" +
													"Net GST Payable Movement Amount: $" + netGstPayable + "<br/>"+		
													"<b>GST</b> can be posted with <b>Singapore</b> jobs only.");
								return;
							}
								
							if(RoundingUtil.round(movement.doubleValue(), 2) == 0.00 &&
							   (RoundingUtil.round(netGstReceivable.doubleValue(), 2) > 0.00 ||
							    RoundingUtil.round(netGstPayable.doubleValue(), 2) > 0.00)){
								MessageBox.alert("Alert", "Main Contract Certificate Net Movement Amount (without GST): $"+RoundingUtil.round(movement.doubleValue(), 2)+"<br/>" +
												"Net GST Receivable Movement Amount: $" + netGstReceivable + "<br/>" +
												"Net GST Payable Movement Amount: $" + netGstPayable + "<br/>"+
												"Main Contract Certificate cannot be posted with <b>GST only</b>.");
								return;
							}
							
							/**@author koeyyeung
							 * created on 20th March, 2015
							 * New approval route(RM) for Negative Main Contract Certificate
							 * **/
							final Double certAmount = RoundingUtil.round(movement.doubleValue(), 2);
							if(certAmount<0.0){
								MessageBox.confirm("Post Main Certificate", 
										"Main Contract Certificate Net Movement Amount (without GST):  $"+RoundingUtil.round(movement.doubleValue(), 2)+"<br/>" +
										"Net GST Receivable Movement Amount: $" + netGstReceivable + "<br/>" +
										"Net GST Payable Movement Amount: $" + netGstPayable + "<br/><br/>" +
										"Are you sure you want to post this Main Certificate?<br/>" +
										"Approval with approval route(RM) is required.",
										new MessageBox.ConfirmCallback() {
									public void execute(String btnID) {
										if (btnID.equals("yes")){
											postMainCertForApproval(certAmount);
										}
									}
								});
							}else{
								MessageBox.confirm("Post Main Certificate", 
										"Main Contract Certificate Net Movement Amount (without GST):  $"+RoundingUtil.round(movement.doubleValue(), 2)+"<br/>" +
										"Net GST Receivable Movement Amount: $" + netGstReceivable + "<br/>" +
										"Net GST Payable Movement Amount: $" + netGstPayable + "<br/>" +
										"Are you sure you want to post this Main Certificate?",
										new MessageBox.ConfirmCallback() {
									public void execute(String btnID) {
										if (btnID.equals("yes"))
											postMainCert();
									}
								});
							}
						}else{
							MessageBox.alert("The latest Main Contract Certificate does not exist.");
						}
					}
				}else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the add button
		ToolTip postToolTip = FieldFactory.createToolTip();
		postToolTip.setTitle("Post Certificate");
		postToolTip.setHtml("Post Certificate/Main Certificate");
		postToolTip.applyTo(postButton);

		//Attachment Button
		attachmentButton = new ToolbarButton();
		attachmentButton.setText("Attachment");
		attachmentButton.setIconCls("attachment-icon");
		attachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(selectedRowIndex != null)
					showAttachmentWindow();
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});

		//Set the tooltip for the Attachment button
		ToolTip attachmentToolTip = FieldFactory.createToolTip();
		attachmentToolTip.setTitle("Attachment");
		attachmentToolTip.setHtml("View/Edit attachments for the selected Main Certificate");
		attachmentToolTip.applyTo(attachmentButton);

		refreshButton = new ToolbarButton();
		refreshButton.setText("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.getTreeSectionController().populateMainCertificate();
			}
		});
		ToolTip refreshToolTip = FieldFactory.createToolTip();
		refreshToolTip.setTitle("Refresh");
		refreshToolTip.setHtml("refresh");
		refreshToolTip.applyTo(refreshButton);		


		retentionReleaseScheduleButton = new ToolbarButton();
		retentionReleaseScheduleButton.setText("Retention Release Schedule");
		retentionReleaseScheduleButton.setIconCls("schedule-icon");
		retentionReleaseScheduleButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (latestMainCert!=null){
					globalSectionController.showRetentionReleaseInputWindow(latestMainCert, null);
				}
				else
					MessageBox.alert("No main Certificate in the job.");
			}
		});

		ToolTip retentionReleaseScheduleToolTip = FieldFactory.createToolTip();
		retentionReleaseScheduleToolTip.setTitle("Retention Release");
		retentionReleaseScheduleToolTip.setHtml("Update Retention Release Schedule");
		retentionReleaseScheduleToolTip.applyTo(retentionReleaseScheduleButton);		

		viewButton.setVisible(false);
		editButton.setVisible(false);
		addButton.setVisible(false);
		//inputContraChargeButton.setVisible(false);
		sentIPAButton.setVisible(false);
		confirmButton.setVisible(false);
		resetButton.setVisible(false);
		postButton.setVisible(false);
		attachmentButton.setVisible(false);
		retentionReleaseScheduleButton.setVisible(false);
		previewMainContractCertificateButton.setVisible(false);
		mainContractCertificateReportButton.setVisible(false);

		// Check for access rights - then add toolbar buttons accordingly
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010402_MAIN_CONTRACT_CERTIFICATE_MAINPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				SessionTimeoutCheck.renewSessionTimer();
				userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010405_RETENTION_RELEASE_WINDOW, new AsyncCallback<List<String>>() {

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser(), "MainCertificateGridPanel.mainCerticateGridPanel().userAccessRightRepository.getAccessRight(user,F010405)");
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}

					public void onSuccess(List<String> arg0) {
						retentionReleaseScheduleButton.setVisible(true);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}

				});

				securitySetup();
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().isChildJobWithSingleParentJob(globalSectionController.getJob().getJobNumber(), new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
			public void onSuccess(Boolean isChildJob) {
				if(!isChildJob)
					previewMainContractCertificateButton.setVisible(true);
					mainContractCertificateReportButton.setVisible(true);
			}
		});

		
		/**
		 * Button added by Henry Lai
		 * 02-Dec-2014
		 */
		tipsButton = new ToolbarButton("Main Contract Certificate Status Info Tips");
		tipsButton.setIconCls("bulb-icon");
		tipsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByTipsButton(680);
			}
		});
		toolbar.addButton(previewMainContractCertificateButton);
		toolbar.addSeparator();
		toolbar.addButton(viewButton);
		toolbar.addButton(editButton);
		toolbar.addSeparator();
		toolbar.addButton(addButton);
		toolbar.addSeparator();
		toolbar.addButton(inputContraChargeButton);
		toolbar.addSeparator();
		toolbar.addButton(sentIPAButton);
		toolbar.addSeparator();
		toolbar.addButton(confirmButton);
		toolbar.addSeparator();
		toolbar.addButton(resetButton);
		toolbar.addSeparator();
		toolbar.addButton(postButton);
		toolbar.addSeparator();
		toolbar.addButton(attachmentButton);
		toolbar.addSeparator();
		toolbar.addButton(retentionReleaseScheduleButton);
		toolbar.addSeparator();
		toolbar.addButton(mainContractCertificateReportButton);
		toolbar.addSeparator();
		toolbar.addButton(refreshButton);
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addSeparator();
		toolbar.addButton(tipsButton);

		mainPanel.setTopToolbar(toolbar);
		toolbar.setDisabled(true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().isChildJobWithSingleParentJob(globalSectionController.getJob().getJobNumber(), new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
			public void onSuccess(Boolean isChildJob) {
				if(!isChildJob)
					toolbar.setDisabled(false);				
			}
		});
	}
	
	private void setupMainContractCertReportButton(){
		
		/* cretae toolbar button */
		mainContractCertificateReportButton = new ToolbarButton("Report");
		mainContractCertificateReportButton.setIconCls(ICON_MENU);
		ToolTip tempToolTip; 
		tempToolTip = FieldFactory.createToolTip();
		tempToolTip.setTitle("Export");
		tempToolTip.setHtml("Export Main Certificates");
		tempToolTip.applyTo(mainContractCertificateReportButton);

		Menu excelMenu = new Menu();
		
		mainContractCertificateReportAllButtonPDF = new Item("PDF");
		mainContractCertificateReportAllButtonPDF.setIconCls(ICON_PDF);
		mainContractCertificateReportAllButtonPDF.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_PDF_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=All"
												, "_blank", "");

			}
		});


		mainContractCertificateReportAllButtonExcel = new Item("Excel");
		mainContractCertificateReportAllButtonExcel.setIconCls(ICON_EXCEL);
		mainContractCertificateReportAllButtonExcel.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_EXCEL_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=All"
												, "_blank", "");

			}
		});
		
		tempToolTip = FieldFactory.createToolTip();
		tempToolTip.setTitle("Export");
		tempToolTip.setHtml("Export Main Certificate as excel file.");
		tempToolTip.applyTo(mainContractCertificateReportAllButtonExcel);

		mainContractCertificateReportMainButtonPDF = new Item("PDF");
		mainContractCertificateReportMainButtonPDF.setIconCls(ICON_PDF);
		mainContractCertificateReportMainButtonPDF.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_PDF_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=Main"
												, "_blank", "");

			}
		});

		
		mainContractCertificateReportMainButtonExcel = new Item("Excel");
		mainContractCertificateReportMainButtonExcel.setIconCls(ICON_EXCEL);
		mainContractCertificateReportMainButtonExcel.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_EXCEL_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=Main"
												, "_blank", "");

			}
		});

		mainContractCertificateReportRetentionButtonPDF = new Item("PDF");
		mainContractCertificateReportRetentionButtonPDF.setIconCls(ICON_PDF);
		mainContractCertificateReportRetentionButtonPDF.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_PDF_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=RetentionRelease"
												, "_blank", "");

			}
		});
		
		mainContractCertificateReportRetentionButtonExcel = new Item("Excel");
		mainContractCertificateReportRetentionButtonExcel.setIconCls(ICON_EXCEL);
		mainContractCertificateReportRetentionButtonExcel.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_EXCEL_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=RetentionRelease"
												, "_blank", "");

			}
		});

		mainContractCertificateReportContraChargeButtonPDF = new Item("PDF");
		mainContractCertificateReportContraChargeButtonPDF.setIconCls(ICON_PDF);
		mainContractCertificateReportContraChargeButtonPDF.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_PDF_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=ContraCharge"
												, "_blank", "");

			}
		});
		
		mainContractCertificateReportContraChargeButtonExcel = new Item("Excel");
		mainContractCertificateReportContraChargeButtonExcel.setIconCls(ICON_EXCEL);
		mainContractCertificateReportContraChargeButtonExcel.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				/* Not yet done */
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_REPORT_EXCEL_URL
												+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
												+"&type=ContraCharge"
												, "_blank", "");

			}
		});

		mainContractCertificateReportAllButton = this.getMenuItem("All", 
				new Item[]{
					mainContractCertificateReportAllButtonPDF,
					mainContractCertificateReportAllButtonExcel
				});

		mainContractCertificateReportMainButton = this.getMenuItem("Main Contract Certificate", 
				new Item[]{
				mainContractCertificateReportMainButtonPDF,
				mainContractCertificateReportMainButtonExcel
			});

		mainContractCertificateReportRetentionButton = this.getMenuItem("Retention Release", 
				new Item[]{
				mainContractCertificateReportRetentionButtonPDF,
				mainContractCertificateReportRetentionButtonExcel
			});

		mainContractCertificateReportContraChargeButton = this.getMenuItem("Contra Charge", 
				new Item[]{
				mainContractCertificateReportContraChargeButtonPDF,
				mainContractCertificateReportContraChargeButtonExcel
			});


		mainContractCertificateReportButton.setMenu(excelMenu);

		excelMenu.addItem(mainContractCertificateReportAllButton);
		excelMenu.addItem(mainContractCertificateReportMainButton);
		excelMenu.addItem(mainContractCertificateReportRetentionButton);
		excelMenu.addItem(mainContractCertificateReportContraChargeButton);		
	}

	private void setupMainContractCertButton(){
		mainContractCertificateButton = new ToolbarButton("Main Contract Certificate");
		mainContractCertificateButton.setIconCls(ICON_MENU);
		ToolTip tempToolTip; 
		tempToolTip = FieldFactory.createToolTip();
		tempToolTip.setTitle("Export");
		tempToolTip.setHtml("Export Main Certificates");
		tempToolTip.applyTo(mainContractCertificateButton);

		Menu excelMenu = new Menu();
		mainContractCertificateButton.setMenu(excelMenu);

		mainContractCertificatePDFButton = new Item("Export Excel");
		mainContractCertificatePDFButton.setIconCls(ICON_EXCEL);
		mainContractCertificatePDFButton.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				if (mainCertNo!=null){
					globalMessageTicket.refresh();
					com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_EXCEL_URL
													+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
													+ "&mainCertNo="+Integer.valueOf(mainCertNo)
													, "_blank", "");
				}
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});
		
		tempToolTip = FieldFactory.createToolTip();
		tempToolTip.setTitle("Export");
		tempToolTip.setHtml("Export Main Certificate as excel file.");
		tempToolTip.applyTo(mainContractCertificatePDFButton);

		mainContractCertificateExcelButton = new Item("Export PDF");		
		mainContractCertificateExcelButton.setIconCls(ICON_PDF);
		mainContractCertificateExcelButton.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {				
				if (mainCertNo!=null){
					globalMessageTicket.refresh();
					com.google.gwt.user.client.Window.open(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_PDF_URL
													+ "?jobNo=" + globalSectionController.getJob().getJobNumber().trim()
													+ "&mainCertNo="+Integer.valueOf(mainCertNo)
													, "_blank", "");
				}
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});
		tempToolTip = FieldFactory.createToolTip();
		tempToolTip.setTitle("Export");
		tempToolTip.setHtml("Export Main Certificates");
		tempToolTip.applyTo(mainContractCertificateExcelButton);
		excelMenu.addItem(mainContractCertificateExcelButton);
		excelMenu.addItem(mainContractCertificatePDFButton);
	}

	private void setupPreviewMainContractCertButton(){
		previewMainContractCertificateButton = new ToolbarButton();
		previewMainContractCertificateButton.setText("Preview");
		previewMainContractCertificateButton.setIconCls("certificate-icon");
		previewMainContractCertificateButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(mainCertNo != null)
					showPreviewMainCertViewWindow(globalSectionController.getJob().getJobNumber().trim(), mainCertNo);
				else
					MessageBox.alert("No Main Contract Certificate is selected.");
			}
		});
	}

	
	private void setupGridForIPA(){
		gridPanelForIPA = new GridPanel();
		gridPanelForIPA.setTitle("IPA");
		gridPanelForIPA.setBorder(false);
		gridPanelForIPA.setFrame(false);
		gridPanelForIPA.setPaddings(0);
		gridPanelForIPA.setAutoScroll(true);
		gridPanelForIPA.setView(new CustomizedGridView());

		Renderer amountRender = new AmountRendererNonTotal(globalSectionController.getUser());

		ColumnConfig certificateNumberColumnConfig = new ColumnConfig("IPA Number",CERTIFICATE_NUMBER,65,false);

		ColumnConfig clientCertNoColumnConfig = new ColumnConfig("Client Cert No.", CLIENT_CERT_NO, 80, false);

		ColumnConfig appliedMainContractorAmountColumnConfig = new ColumnConfig("Main Contractor Amount",APPLIED_MAIN_CONTRACTOR_AMOUNT,130,false);
		appliedMainContractorAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedNSCNDSCAmountColumnConfig = new ColumnConfig("NSC\n NDSC Amount",APPLIED_NSCNDSC_AMOUNT,100,false);
		appliedNSCNDSCAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedMOSAmountColumnConfig = new ColumnConfig( "MOS Amount" , APPLIED_MOS_AMOUNT,120,false);
		appliedMOSAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedMainContractorRetentionColumnConfig = new ColumnConfig( "Retention" , APPLIED_MAIN_CONTRACTOR_RETENTION,120,false);
		appliedMainContractorRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedMainContractorRetentionReleasedColumnConfig = new ColumnConfig( "Retention Released" , APPLIED_MAIN_CONTRACTOR_RETENTION_RELEASED,120,false);
		appliedMainContractorRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedRetentionforNSCNDSCColumnConfig = new ColumnConfig( "Retention for NSC/NDSC" , APPLIED_RETENTION_FOR_NSCNDSC,125,false);
		appliedRetentionforNSCNDSCColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedRetentionforNSCNDSCReleasedColumnConfig = new ColumnConfig( "Retention for NSC/NDSC Released" , APPLIED_RETENTION_FOR_NSCNDSC_RELEASED,135,false);
		appliedRetentionforNSCNDSCReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedMOSRetentionColumnConfig = new ColumnConfig( "MOS Retention" , APPLIED_MOS_RETENTION,100,false);
		appliedMOSRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedMOSRetentionReleasedColumnConfig = new ColumnConfig( "MOS Retention Released" , APPLIED_MOS_RETENTION_RELEASED,150,false);
		appliedMOSRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedContraChargeAmountColumnConfig = new ColumnConfig( "Contra Charge Amount" , APPLIED_CONTRA_CHARGE_AMOUNT,120,false);
		appliedContraChargeAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedAdjustmentAmountColumnConfig = new ColumnConfig( "Adjustment Amount" , APPLIED_ADJUSTMENT_AMOUNT,110,false);
		appliedAdjustmentAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedAdvancePaymentColumnConfig = new ColumnConfig( "Advanced Payment" , APPLIED_ADVANCE_PAYMENT,110,false);
		appliedAdvancePaymentColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig appliedCPFAmountColumnConfig = new ColumnConfig( "CPF Amount" , APPLIED_CPF_AMOUNT,150,false);
		appliedCPFAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig ipaSubmissionDateColumnConfig = new ColumnConfig("IPA Submission Date",IPA_SUBMISSION_DATE,85,false); 

		ColumnConfig remarkColumnConfig = new ColumnConfig("Remark", REMARK, 400, false);

		appliedMainContractorAmountColumnConfig.setRenderer(amountRender);
		appliedNSCNDSCAmountColumnConfig.setRenderer(amountRender);
		appliedMOSAmountColumnConfig.setRenderer(amountRender);
		appliedMainContractorRetentionColumnConfig.setRenderer(amountRender);
		appliedMainContractorRetentionReleasedColumnConfig.setRenderer(amountRender);
		appliedRetentionforNSCNDSCColumnConfig.setRenderer(amountRender);
		appliedRetentionforNSCNDSCReleasedColumnConfig.setRenderer(amountRender);
		appliedMOSRetentionColumnConfig.setRenderer(amountRender);
		appliedMOSRetentionReleasedColumnConfig.setRenderer(amountRender);
		appliedContraChargeAmountColumnConfig.setRenderer(amountRender);
		appliedAdjustmentAmountColumnConfig.setRenderer(amountRender);
		appliedAdvancePaymentColumnConfig.setRenderer(amountRender);
		appliedCPFAmountColumnConfig.setRenderer(amountRender);


		MemoryProxy proxy = new MemoryProxy(new Object[][]{});	
		JsonReader reader = new JsonReader(recordDef);
		dataStore = new Store(proxy, reader);

		dataStore.load();

		dataStore.setSortInfo(new SortState(CERTIFICATE_NUMBER,SortDir.ASC));
		gridPanelForIPA.setStore(dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {		
				certificateNumberColumnConfig,
				clientCertNoColumnConfig,
				appliedMainContractorAmountColumnConfig,
				appliedNSCNDSCAmountColumnConfig,
				appliedMOSAmountColumnConfig,
				appliedMainContractorRetentionColumnConfig,
				appliedMainContractorRetentionReleasedColumnConfig,
				appliedRetentionforNSCNDSCColumnConfig,
				appliedRetentionforNSCNDSCReleasedColumnConfig,
				appliedMOSRetentionColumnConfig,
				appliedMOSRetentionReleasedColumnConfig,
				appliedContraChargeAmountColumnConfig,
				appliedAdjustmentAmountColumnConfig,
				appliedAdvancePaymentColumnConfig,
				appliedCPFAmountColumnConfig,
				ipaSubmissionDateColumnConfig,
				remarkColumnConfig
		};

		gridPanelForIPA.setColumnModel(new ColumnModel(columns));
		
		rowSelectionModelForIPA = new RowSelectionModel();
		gridPanelForIPA.setSelectionModel(rowSelectionModelForIPA);

		gridPanelForIPA.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.getDetailSectionController().resetPanel();
				currentGridPanel = "IPA";
				
				selectedRowIndex = new Integer(rowIndex);
				
				Record curRecord = dataStore.getAt(selectedRowIndex);
				mainCertNo = (curRecord.getAsObject(CERTIFICATE_NUMBER) != null ? curRecord.getAsString(CERTIFICATE_NUMBER) : "");
				mainCertStatus = (curRecord.getAsObject(CERTIFICATE_STATUS) != null ? curRecord.getAsString(CERTIFICATE_STATUS) : "");
				
				buttonValidation(mainCertStatus);
				UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),new Integer(mainCertNo), new AsyncCallback<MainContractCertificate>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
					}

					public void onSuccess(MainContractCertificate result) {
						selectedMainCert = result;
						setContraChargeList();
						showAccountsReceivableDetails();
					}
				});

				if(Integer.parseInt(mainCertNo)>1){
					UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),(new Integer(mainCertNo))-1, new AsyncCallback<MainContractCertificate>(){
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
						}

						public void onSuccess(MainContractCertificate result) {
							previousMainCert = result;
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						}
					});
				}else
					previousMainCert = new MainContractCertificate();
			}
		});

		paginationToolbarForIPA = new PaginationToolbar();
		paginationToolbarForIPA.setGoToPageAdapter(new GoToPageCommandAdapter(){

			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				goToPageMainCertificate(pageNum);
			}
		});
		paginationToolbarForIPA.addFill();
		gridPanelForIPA.setBottomToolbar(paginationToolbarForIPA);

		tabPanel.add(gridPanelForIPA);
	}

	private void setupGridForIPC() {
		gridPanelForIPC = new GridPanel();
		gridPanelForIPC.setTitle("IPC");
		gridPanelForIPC.setBorder(false);
		gridPanelForIPC.setFrame(false);
		gridPanelForIPC.setPaddings(0);
		gridPanelForIPC.setAutoScroll(true);
		gridPanelForIPC.setView(new CustomizedGridView());

		Renderer amountRender = new AmountRendererNonTotal(globalSectionController.getUser());

		ColumnConfig certificateNumberColumnConfig = new ColumnConfig("Certificate No.",CERTIFICATE_NUMBER,85,false);
		
		ColumnConfig clientCertNoColumnConfig = new ColumnConfig("Client Cert No.", CLIENT_CERT_NO, 80, false);
		
		ColumnConfig certifiedMainContractorAmountColumnConfig = new ColumnConfig("Main Contractor Amount",CERTIFIED_MAIN_CONTRACTOR_AMOUNT,130,false); 
		certifiedMainContractorAmountColumnConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig certifiedNSCNDSCAmountColumnConfig = new ColumnConfig("NSC/NDSC Amount",CERTIFIED_NSCNDSC_AMOUNT,100,false); 
		certifiedNSCNDSCAmountColumnConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig certifiedMOSAmountColumnConfig = new ColumnConfig("MOS Amount",CERTIFIED_MOS_AMOUNT,120,false); 
		certifiedMOSAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMainContractorRetentionColumnConfig = new ColumnConfig("Retention",CERTIFIED_MAIN_CONTRACTOR_RETENTION,120,false);
		certifiedMainContractorRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMainContractorRetentionReleasedColumnConfig = new ColumnConfig("Retention Released",CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED,120,false); 
		certifiedMainContractorRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedRetentionforNSCNDSCColumnConfig = new ColumnConfig("Retention for NSC/NDSC",CERTIFIED_RETENTION_FOR_NSCNDSC,125,false); 
		certifiedRetentionforNSCNDSCColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedRetentionforNSCNDSCReleasedColumnConfig = new ColumnConfig("Retention for NSC/NDSC Released",CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED,140,false); 
		certifiedRetentionforNSCNDSCReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMOSRetentionColumnConfig = new ColumnConfig("MOS Retention",CERTIFIED_MOS_RETENTION,120,false); 
		certifiedMOSRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMOSRetentionReleasedColumnConfig = new ColumnConfig("MOS Retention Released",CERTIFIED_MOS_RETENTION_RELEASED,130,false); 
		certifiedMOSRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedContraChargeAmountColumnConfig = new ColumnConfig("Contra Charge Amount",CERTIFIED_CONTRA_CHARGE_AMOUNT,120,false); 
		certifiedContraChargeAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedAdjustmentAmountColumnConfig = new ColumnConfig("Adjustment Amount",CERTIFIED_ADJUSTMENT_AMOUNT,110,false);
		certifiedAdjustmentAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedAdvancePaymentColumnConfig = new ColumnConfig("Advanced Payment",CERTIFIED_ADVANCE_PAYMENT,110,false);
		certifiedAdvancePaymentColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedCPFAmountColumnConfig = new ColumnConfig("CPF Amount",CERTIFIED_CPF_AMOUNT,150,false); 
		certifiedCPFAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig gstReceivableColumnConfig = new ColumnConfig("GST Amount",GST_RECEIVABLE,120,false);
		gstReceivableColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig gstPayableColumnConfig = new ColumnConfig("GST for Contra Charge",GST_PAYABLE,120,false);
		gstPayableColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certificateStatusColumnConfig = new ColumnConfig("Certificate Status",CERTIFICATE_STATUS,100,false); 
		
		ColumnConfig certificateStatusDescColumnConfig = new ColumnConfig("Certificate Status Description",CERTIFICATE_STATUS_DESC,220,false);
		
		//ColumnConfig arDocNumberColumnConfig = new ColumnConfig("AR Doc No.",AR_DOC_NUMBER,80,false); 

		ColumnConfig certificateIssueDateColumnConfig = new ColumnConfig("Certificate Issue Date",CERTIFICATE_ISSUE_DATE,95,false); 
		
		ColumnConfig asAtDateColumnConfig = new ColumnConfig("As At Date",AS_AT_DATE,75,false); 
		
		ColumnConfig certificateStatusChangeDateColumnConfig = new ColumnConfig("Certificate Status Change Date",CERTIFICATE_STATUS_CHANGE_DATE,150,false); 
		
		ColumnConfig certificateDueDateColumnConfig = new ColumnConfig("Certificate Due Date",CERTIFICATE_DUE_DATE,120,false); 

		ColumnConfig actualReceiptDateColumnConfig = new ColumnConfig("Actual Receipt Date",ACTUAL_RECEIPT_DATE,95,false); 
		
		ColumnConfig remarkColumnConfig = new ColumnConfig("Remark", REMARK, 400, false);
		
		certifiedMainContractorAmountColumnConfig.setRenderer(amountRender);
		certifiedNSCNDSCAmountColumnConfig.setRenderer(amountRender);
		certifiedMOSAmountColumnConfig.setRenderer(amountRender);
		certifiedMainContractorRetentionColumnConfig.setRenderer(amountRender);
		certifiedMainContractorRetentionReleasedColumnConfig.setRenderer(amountRender);
		certifiedRetentionforNSCNDSCColumnConfig.setRenderer(amountRender);
		certifiedRetentionforNSCNDSCReleasedColumnConfig.setRenderer(amountRender);
		certifiedMOSRetentionColumnConfig.setRenderer(amountRender);
		certifiedMOSRetentionReleasedColumnConfig.setRenderer(amountRender);
		certifiedContraChargeAmountColumnConfig.setRenderer(amountRender);
		certifiedAdjustmentAmountColumnConfig.setRenderer(amountRender);
		certifiedAdvancePaymentColumnConfig.setRenderer(amountRender);
		certifiedCPFAmountColumnConfig.setRenderer(amountRender);
		gstReceivableColumnConfig.setRenderer(amountRender);
		gstPayableColumnConfig.setRenderer(amountRender);

		gridPanelForIPC.setStore(dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {		
				certificateNumberColumnConfig,
				clientCertNoColumnConfig,
				certifiedMainContractorAmountColumnConfig,
				certifiedNSCNDSCAmountColumnConfig,
				certifiedMOSAmountColumnConfig,
				certifiedMainContractorRetentionColumnConfig,
				certifiedMainContractorRetentionReleasedColumnConfig,
				certifiedRetentionforNSCNDSCColumnConfig,
				certifiedRetentionforNSCNDSCReleasedColumnConfig,
				certifiedMOSRetentionColumnConfig,
				certifiedMOSRetentionReleasedColumnConfig,
				certifiedContraChargeAmountColumnConfig,
				certifiedAdjustmentAmountColumnConfig,
				certifiedAdvancePaymentColumnConfig,
				certifiedCPFAmountColumnConfig,
				gstReceivableColumnConfig,
				gstPayableColumnConfig,
				certificateStatusColumnConfig,
				certificateStatusDescColumnConfig,
				//arDocNumberColumnConfig,
				certificateIssueDateColumnConfig,
				asAtDateColumnConfig,
				certificateStatusChangeDateColumnConfig,
				certificateDueDateColumnConfig,
				actualReceiptDateColumnConfig,
				remarkColumnConfig
		};

		gridPanelForIPC.setColumnModel(new ColumnModel(columns));
		
		rowSelectionModelForIPC = new RowSelectionModel();
		gridPanelForIPC.setSelectionModel(rowSelectionModelForIPC);

		gridPanelForIPC.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.getDetailSectionController().resetPanel();
				currentGridPanel = "IPC";

				selectedRowIndex = new Integer(rowIndex);

				Record curRecord = dataStore.getAt(selectedRowIndex);

				mainCertNo = (curRecord.getAsObject(CERTIFICATE_NUMBER) != null ? curRecord.getAsString(CERTIFICATE_NUMBER) : "");
				mainCertStatus = (curRecord.getAsObject(CERTIFICATE_STATUS) != null ? curRecord.getAsString(CERTIFICATE_STATUS) : "");

				buttonValidation(mainCertStatus);
				UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),new Integer(mainCertNo), new AsyncCallback<MainContractCertificate>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
					}

					public void onSuccess(MainContractCertificate result) {
						selectedMainCert = result;
						setContraChargeList();
						showAccountsReceivableDetails();
					}
				});

				if(Integer.parseInt(mainCertNo)>1){
					UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),(new Integer(mainCertNo))-1, new AsyncCallback<MainContractCertificate>(){
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
						}

						public void onSuccess(MainContractCertificate result) {
							previousMainCert = result;
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						}
					});
				}else
					previousMainCert = new MainContractCertificate();
			}
		});

		paginationToolbarForIPC = new PaginationToolbar();
		paginationToolbarForIPC.setGoToPageAdapter(new GoToPageCommandAdapter(){

			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				goToPageMainCertificate(pageNum);
			}
		});
		paginationToolbarForIPC.addFill();
		gridPanelForIPC.setBottomToolbar(paginationToolbarForIPC);

		tabPanel.add(gridPanelForIPC);
	}

	private void setupGridForPostingAmount(){
		Panel panel = new Panel();
		panel.setTitle("Posting Amount");
		panel.setLayout(new RowLayout());
		
		//Header
		GridPanel gridPanelHeader = new GridPanel();
		gridPanelHeader.setBorder(false);
		gridPanelHeader.setFrame(false);
		gridPanelHeader.setPaddings(0);
		gridPanelHeader.setHeight(25);

		ColumnConfig firstHeaderColumnConfig = new ColumnConfig("","",85,false);
		firstHeaderColumnConfig.setFixed(true);
		firstHeaderColumnConfig.setResizable(false);
		
		ColumnConfig secondHeaderColumnConfig = new ColumnConfig("<b>Net Movement Amount</b>","",430,false);
		secondHeaderColumnConfig.setFixed(true);
		secondHeaderColumnConfig.setResizable(false);
		secondHeaderColumnConfig.setAlign(TextAlign.CENTER);
		
		ColumnConfig[] headerColumns = new ColumnConfig[] {		
				firstHeaderColumnConfig,
				secondHeaderColumnConfig
		};

		gridPanelHeader.setColumnModel(new ColumnModel(headerColumns));
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});	
		JsonReader reader = new JsonReader(recordDef);
		Store store = new Store(proxy, reader);
		gridPanelHeader.setStore(store);
		
		//Details
		gridPanelForPostingAmount = new GridPanel();
		gridPanelForPostingAmount.setBorder(false);
		gridPanelForPostingAmount.setFrame(false);
		gridPanelForPostingAmount.setPaddings(0);
		gridPanelForPostingAmount.setAutoScroll(true);
		gridPanelForPostingAmount.setView(new CustomizedGridView());

		Renderer amountRender = new AmountRendererNonTotal(globalSectionController.getUser());

		ColumnConfig certificateNumberColumnConfig = new ColumnConfig("Certificate No.",CERTIFICATE_NUMBER,85,false);
		certificateNumberColumnConfig.setFixed(true);
		certificateNumberColumnConfig.setResizable(false);
		
		ColumnConfig certAmountColumnConfig = new ColumnConfig("Certificate Amount(without GST)", NET_MOVEMENT_AMOUNT, 170, false);
		certAmountColumnConfig.setFixed(true);
		certAmountColumnConfig.setResizable(false);
		certAmountColumnConfig.setAlign(TextAlign.RIGHT);
		certAmountColumnConfig.setRenderer(amountRender);
		
		ColumnConfig gstReceivableAmountColumnConfig = new ColumnConfig("GST Receivable Amount",NET_GST_RECEIVABLE_MOVEMENT_AMOUNT,130,false); 
		gstReceivableAmountColumnConfig.setFixed(true);
		gstReceivableAmountColumnConfig.setResizable(false);
		gstReceivableAmountColumnConfig.setAlign(TextAlign.RIGHT);
		gstReceivableAmountColumnConfig.setRenderer(amountRender);

		ColumnConfig gstPayableAmountColumnConfig = new ColumnConfig("GST Payable Amount",NET_GST_PAYABLE_MOVEMENT_AMOUNT,130,false); 
		gstPayableAmountColumnConfig.setFixed(true);
		gstPayableAmountColumnConfig.setResizable(false);
		gstPayableAmountColumnConfig.setAlign(TextAlign.RIGHT);
		gstPayableAmountColumnConfig.setRenderer(amountRender);

		
		gridPanelForPostingAmount.setStore(dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {
				certificateNumberColumnConfig,
				certAmountColumnConfig,
				gstReceivableAmountColumnConfig,
				gstPayableAmountColumnConfig
		};

		gridPanelForPostingAmount.setColumnModel(new ColumnModel(columns));
		
		rowSelectionModelForPostingAmount = new RowSelectionModel();
		gridPanelForPostingAmount.setSelectionModel(rowSelectionModelForPostingAmount);

		gridPanelForPostingAmount.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.getDetailSectionController().resetPanel();
				currentGridPanel = "Posting Amount";

				selectedRowIndex = new Integer(rowIndex);

				Record curRecord = dataStore.getAt(selectedRowIndex);

				mainCertNo = (curRecord.getAsObject(CERTIFICATE_NUMBER) != null ? curRecord.getAsString(CERTIFICATE_NUMBER) : "");
				mainCertStatus = (curRecord.getAsObject(CERTIFICATE_STATUS) != null ? curRecord.getAsString(CERTIFICATE_STATUS) : "");

				buttonValidation(mainCertStatus);
				UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),new Integer(mainCertNo), new AsyncCallback<MainContractCertificate>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
					}

					public void onSuccess(MainContractCertificate result) {
						selectedMainCert = result;
						setContraChargeList();
						showAccountsReceivableDetails();
					}
				});

				if(Integer.parseInt(mainCertNo)>1){
					UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(),(new Integer(mainCertNo))-1, new AsyncCallback<MainContractCertificate>(){
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
						}

						public void onSuccess(MainContractCertificate result) {
							previousMainCert = result;
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						}
					});
				}else
					previousMainCert = new MainContractCertificate();
			}
		});

		paginationToolbarForPostingAmount = new PaginationToolbar();
		paginationToolbarForPostingAmount.setGoToPageAdapter(new GoToPageCommandAdapter(){

			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				goToPageMainCertificate(pageNum);
			}
		});
		paginationToolbarForPostingAmount.addFill();
		gridPanelForPostingAmount.setBottomToolbar(paginationToolbarForPostingAmount);

		panel.add(gridPanelHeader);
		panel.add(gridPanelForPostingAmount);
		tabPanel.add(panel);
	}
	
	/**
	 * @author koeyyeung
	 * modified on 24 April, 2013
	 */
	private void showAttachmentWindow(){
		Record record = null;
		if("IPA".equals(currentGridPanel))
			record = rowSelectionModelForIPA.getSelected();
		else if("IPC".equals(currentGridPanel))
			record = rowSelectionModelForIPC.getSelected();
		else if("Posting Amount".equals(currentGridPanel))
			record = rowSelectionModelForPostingAmount.getSelected();
			

		if(record==null){
			MessageBox.alert("Please select a Main Certificate to view/edit attachments");
			return;
		}else{
			String jobNumber = record.getAsString(JOB_NUMBER);
			Integer mainCertNumber = Integer.parseInt(record.getAsString(CERTIFICATE_NUMBER));

			globalSectionController.showMainCertificateAttachmentWindow(globalSectionController, jobNumber, mainCertNumber);
		}
	}

	/**
	 * @author koeyyeung
	 * modified on 24 April, 2013
	 */
	private void populateGridWithPage(){
		PaginationWrapper<MainContractCertificate> mainCertWrapper = MainCertificateGridPanel.mainCertWrapper;
		MainContractCertificate mainCert = MainCertificateGridPanel.mainCert;
		if(mainCertWrapper == null || mainCertWrapper.getTotalRecords() == 0)
			return;

		currentPage = mainCertWrapper.getCurrentPage();
		totalPage = mainCertWrapper.getTotalPage();
		paginationToolbarForIPA.setTotalPage(totalPage);
		paginationToolbarForIPA.setCurrentPage(currentPage);
		paginationToolbarForIPC.setTotalPage(totalPage);
		paginationToolbarForIPC.setCurrentPage(currentPage);
		paginationToolbarForPostingAmount.setTotalPage(totalPage);
		paginationToolbarForPostingAmount.setCurrentPage(currentPage);
		
		if(mainCertWrapper.getCurrentPageContentList().size()!=0){
			Record[] data = new Record[mainCertWrapper.getCurrentPageContentList().size()];

			int latestCertIndex = 0;
			int latestMainCertNo = 0; 
			int recordIndex = mainCertWrapper.getCurrentPageContentList().size()-1;
			MainContractCertificate  previousCert = null;
			if(mainCertWrapper.getCurrentPage()<mainCertWrapper.getTotalPage()-1)
				previousCert = mainCert;
			for (int i = 0; i < mainCertWrapper.getCurrentPageContentList().size(); i++){
				MainContractCertificate mainContractCertificate = mainCertWrapper.getCurrentPageContentList().get(i);

				if(mainContractCertificate.getCertificateNumber()>latestMainCertNo){
					latestMainCertNo = mainContractCertificate.getCertificateNumber();
					latestCertIndex = i;
				}

				//Calculate posting amount
				double movement = obtainMainCertNetAmountMovement(mainContractCertificate, previousCert);
				double netGstReceivable = 0.0;
				double netGstPayable = 0.0;
				if(previousCert!=null){
					netGstReceivable = RoundingUtil.round(mainContractCertificate.getGstReceivable() - previousCert.getGstReceivable(), 2);
					netGstPayable = RoundingUtil.round(mainContractCertificate.getGstPayable() - previousCert.getGstPayable(), 2);
				}else{
					netGstReceivable = RoundingUtil.round(mainContractCertificate.getGstReceivable(), 2);
					netGstPayable = RoundingUtil.round(mainContractCertificate.getGstPayable(), 2);
				}


				double appliedMainContractorAmount = mainContractCertificate.getAppliedMainContractorAmount()==null?0:mainContractCertificate.getAppliedMainContractorAmount();
				double appliedNSCNDSCAmount = mainContractCertificate.getAppliedNSCNDSCAmount()==null?0:mainContractCertificate.getAppliedNSCNDSCAmount();
				double appliedMOSAmount = mainContractCertificate.getAppliedMOSAmount()==null?0:mainContractCertificate.getAppliedMOSAmount();
				double appliedMainContractorRetention = mainContractCertificate.getAppliedMainContractorRetention()==null?0:mainContractCertificate.getAppliedMainContractorRetention();
				double appliedMainContractorRetentionReleased= mainContractCertificate.getAppliedMainContractorRetentionReleased()==null?0:mainContractCertificate.getAppliedMainContractorRetentionReleased();
				double appliedRetentionforNSCNDSC = mainContractCertificate.getAppliedRetentionforNSCNDSC()==null?0:mainContractCertificate.getAppliedRetentionforNSCNDSC();
				double appliedRetentionforNSCNDSCReleased = mainContractCertificate.getAppliedRetentionforNSCNDSCReleased()==null?0:mainContractCertificate.getAppliedRetentionforNSCNDSCReleased();
				double appliedMOSRetention = mainContractCertificate.getAppliedMOSRetention()==null?0:mainContractCertificate.getAppliedMOSRetention();
				double appliedMOSRetentionReleased = mainContractCertificate.getAppliedMOSRetentionReleased()==null?0:mainContractCertificate.getAppliedMOSRetentionReleased();
				double appliedContraChargeAmount = mainContractCertificate.getAppliedContraChargeAmount()==null?0:mainContractCertificate.getAppliedContraChargeAmount();
				double appliedAdjustmentAmount = mainContractCertificate.getAppliedAdjustmentAmount()==null?0:mainContractCertificate.getAppliedAdjustmentAmount();
				double appliedAdvancePayment = mainContractCertificate.getAppliedAdvancePayment() == null ? 0.0 : mainContractCertificate.getAppliedAdvancePayment();
				double appliedCPFAmount = mainContractCertificate.getAppliedCPFAmount()==null?0:mainContractCertificate.getAppliedCPFAmount();
				double certifiedMainContractorAmount = mainContractCertificate.getCertifiedMainContractorAmount()==null?0:mainContractCertificate.getCertifiedMainContractorAmount();
				double certifiedNSCNDSCAmount = mainContractCertificate.getCertifiedNSCNDSCAmount()==null?0:mainContractCertificate.getCertifiedNSCNDSCAmount();
				double certifiedMOSAmount = mainContractCertificate.getCertifiedMOSAmount()==null?0:mainContractCertificate.getCertifiedMOSAmount();
				double certifiedMainContractorRetention = mainContractCertificate.getCertifiedMainContractorRetention()==null?0:mainContractCertificate.getCertifiedMainContractorRetention();
				double certifiedMainContractorRetentionReleased = mainContractCertificate.getCertifiedMainContractorRetentionReleased()==null?0:mainContractCertificate.getCertifiedMainContractorRetentionReleased();
				double certifiedRetentionforNSCNDSC = mainContractCertificate.getCertifiedRetentionforNSCNDSC()==null?0:mainContractCertificate.getCertifiedRetentionforNSCNDSC();
				double certifiedRetentionforNSCNDSCReleased = mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased()==null?0:mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased();
				double certifiedMOSRetention = mainContractCertificate.getCertifiedMOSRetention()==null?0:mainContractCertificate.getCertifiedMOSRetention();
				double certifiedMOSRetentionReleased = mainContractCertificate.getCertifiedMOSRetentionReleased()==null?0:mainContractCertificate.getCertifiedMOSRetentionReleased();
				double certifiedContraChargeAmount = mainContractCertificate.getCertifiedContraChargeAmount()==null?0:mainContractCertificate.getCertifiedContraChargeAmount();
				double certifiedAdjustmentAmount = mainContractCertificate.getCertifiedAdjustmentAmount()==null?0:mainContractCertificate.getCertifiedAdjustmentAmount();
				double certifiedAdvancePayment = mainContractCertificate.getCertifiedAdvancePayment() == null ? 0.0 : mainContractCertificate.getCertifiedAdvancePayment();
				double certifiedCPFAmount = mainContractCertificate.getCertifiedCPFAmount()==null?0:mainContractCertificate.getCertifiedCPFAmount();
				double gstReceivable = mainContractCertificate.getGstReceivable()==null?0:mainContractCertificate.getGstReceivable();
				double gstPayable = mainContractCertificate.getGstPayable()==null?0:mainContractCertificate.getGstPayable();

				data[recordIndex] = recordDef.createRecord(new Object[]{
						mainContractCertificate.getJobNo(),
						mainContractCertificate.getCertificateNumber(),
						mainContractCertificate.getClientCertNo(),
						appliedMainContractorAmount,
						appliedNSCNDSCAmount,
						appliedMOSAmount,
						appliedMainContractorRetention,
						appliedMainContractorRetentionReleased,
						appliedRetentionforNSCNDSC,
						appliedRetentionforNSCNDSCReleased,
						appliedMOSRetention,
						appliedMOSRetentionReleased,
						appliedContraChargeAmount,
						appliedAdjustmentAmount,
						appliedAdvancePayment,
						appliedCPFAmount,
						date2String(mainContractCertificate.getIpaSubmissionDate()),
						certifiedMainContractorAmount,
						certifiedNSCNDSCAmount,
						certifiedMOSAmount,
						certifiedMainContractorRetention,
						certifiedMainContractorRetentionReleased,
						certifiedRetentionforNSCNDSC,
						certifiedRetentionforNSCNDSCReleased,
						certifiedMOSRetention,
						certifiedMOSRetentionReleased,
						certifiedContraChargeAmount,
						certifiedAdjustmentAmount,
						certifiedAdvancePayment,
						certifiedCPFAmount,
						gstReceivable,
						gstPayable,
						mainContractCertificate.getCertificateStatus(),
						convertCertStatusToDescription(mainContractCertificate.getCertificateStatus()),
						mainContractCertificate.getArDocNumber(),
						date2String(mainContractCertificate.getCertIssueDate()),
						date2String(mainContractCertificate.getCertAsAtDate()),
						date2String(mainContractCertificate.getCertStatusChangeDate()),
						date2String(mainContractCertificate.getCertDueDate()),
						mainContractCertificate.getRemark(),
						movement,
						netGstReceivable,
						netGstPayable,
						date2String(mainContractCertificate.getActualReceiptDate())
				});

				previousCert = mainContractCertificate;
				recordIndex--;
			}

			latestMainCert = mainCertWrapper.getCurrentPageContentList().get(latestCertIndex);

			dataStore.removeAll();
			dataStore.add(data);
		}
	}

	/**
	 * 
	 * @author tikywong
	 * modified on Aug 23, 2012 5:54:15 PM
	 */
	private void confirmCertificate(){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Confirming...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().confirmMainContractCert(selectedMainCert, new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.alert("Failed to confirm Main Certificate, " + e.getLocalizedMessage());
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(String message) {
				if(message==null || "".equals(message.trim())){
					MessageBox.alert("Main Contract Certificate is confirmed.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
				}
				else
					MessageBox.alert("Confrim Main Contract Certificate failed. Error: "+message);

				globalSectionController.getTreeSectionController().populateMainCertificate();
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

		});
	}

	/**
	 * 
	 * @author tikywong
	 * modified on Aug 23, 2012 5:54:07 PM
	 */
	private void resetCertificate(){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().resetMainContractCert(selectedMainCert, new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.alert("Failed to reset Main Certificate, " + e.getLocalizedMessage());
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(String message) {
				if(message==null || "".equals(message.trim())){
					MessageBox.alert("Main Contract Certificate is reset.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
				}
				else
					MessageBox.alert("Reset Main Contract Certificate failed. Error: "+message);

				globalSectionController.getTreeSectionController().populateMainCertificate();
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}

	/**
	 * 
	 * @author tikywong
	 * refactored on Nov 20, 2012 11:02:12 AM
	 */
	private void postMainCert(){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Posting....", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().insertAndPostMainContractCert(selectedMainCert.getJobNo(), selectedMainCert.getCertificateNumber(), selectedMainCert.getCertAsAtDate(), new AsyncCallback<String>() {
			public void onFailure(Throwable e) {
				UIUtil.alert("Failed to post Main Contract Certificate: "+e.getLocalizedMessage());
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(String message) {
				if(message==null || message.trim().equals("")){
					MessageBox.alert("Main Contract Certificate has been posted successfully.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
				}
				else
					MessageBox.alert(message);				
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}	
		});	
	}
	
	/**@author koeyyeung
	 * created on 20th March, 2015
	 * New approval route for Negative Main Contract Certificate
	 * @param certAmount
	 * **/
	private void postMainCertForApproval(Double certAmount){
		String userID = globalSectionController.getUser().getUsername().toUpperCase();
		
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Submitting....", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().submitNegativeMainCertForApproval(selectedMainCert.getJobNo(), selectedMainCert.getCertificateNumber(), certAmount, userID, new AsyncCallback<String>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(String message) {
				if(message==null || message.trim().equals("")){
					MessageBox.alert("Main Contract Certificate has been submitted to Approval System successfully.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
				}
				else
					MessageBox.alert(message);				
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}	
		});	
	}

	/**
	 * 
	 * @author tikywong
	 * modified on Aug 23, 2012 5:35:16 PM
	 */
	private void sendOutIPA(){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Sending...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().insertIPAAndUpdateMainContractCert(selectedMainCert, new AsyncCallback<String>(){

			public void onFailure(Throwable e) {
				UIUtil.alert("Failed to send out IPA, " + e.getLocalizedMessage());
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(String message) {
				if(message==null || message.trim().equals("")){
					MessageBox.alert("IPA has been sent out successfully.");
					globalSectionController.getTreeSectionController().populateMainCertificate();
				}
				else
					MessageBox.alert(message);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}

	
	private void buttonValidation(String mainCertStatus){
		if(!MainContractCertificate.CERT_CREATED.equals(mainCertStatus))
			sentIPAButton.disable();
		else
			sentIPAButton.enable();
		if(!MainContractCertificate.IPA_SENT.equals(mainCertStatus))
			confirmButton.disable();
		else
			confirmButton.enable();
		if(!MainContractCertificate.CERT_CONFIRMED.equals(mainCertStatus)){
			resetButton.disable();
			postButton.disable();
		}else{
			resetButton.enable();
			postButton.enable();
		}
	}

	private void securitySetup(){
		if (accessRightsList.contains("WRITE")){
			editButton.setVisible(true);
			addButton.setVisible(true);
			sentIPAButton.setVisible(true);
			confirmButton.setVisible(true);
			resetButton.setVisible(true);
			postButton.setVisible(true);
			attachmentButton.setVisible(true);
		}else if(accessRightsList.contains("READ")){
			viewButton.setVisible(true);
			attachmentButton.setVisible(true);
		}
	}

	public void showContraChargeDetail(){
		globalSectionController.getDetailSectionController().populateGridPanelForMainCertContraCharge(this, selectedMainCert);
	}

	public String getScreenName() {
		return screenName;
	}

	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}

	public void setContraChargeList(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainCertCertContraChargeList(selectedMainCert, new AsyncCallback<List<MainCertificateContraCharge>>(){

			public void onFailure(Throwable arg0) {
				MessageBox.alert("Fail to get the Contra Charge List./n"+arg0);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onSuccess(List<MainCertificateContraCharge> result) {
				globalSectionController.getMainContractCertificateRepository().deleteMainCertContraCharge(selectedMainCert, new AsyncCallback<Integer>(){

					@Override
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.throwException(e);
					}

					@Override
					public void onSuccess(Integer result) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}
					
				});
				for(MainCertificateContraCharge mainCertificateContraCharge : result){
					mainCertificateContraCharge.setMainCertificate(selectedMainCert);
				}
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}

	/**
	 * @author koeyyeung
	 * modified on 24 April,2013
	 * */
	private void showAccountsReceivableDetails() {
		Record record = null;
		if ("IPA".equals(currentGridPanel))
			record = rowSelectionModelForIPA.getSelected();
		else if ("IPC".equals(currentGridPanel))
			record = rowSelectionModelForIPC.getSelected();
		else if ("Posting Amount".equals(currentGridPanel))
			record = rowSelectionModelForPostingAmount.getSelected();

		
		if (record != null) {
			// get the record representing the selected row
			UIUtil.maskMainPanel(GlobalParameter.LOADING_MSG);
			
			// Load Account Receivable Detail ONLY if Certificate Status 300 / 400
			if (record.getAsString(CERTIFICATE_STATUS).equals(MainContractCertificate.CERT_POSTED) ||
				record.getAsString(CERTIFICATE_STATUS).equals("400")) {

				// reference number = Job Number(left-padded with zeros to 5 digits) + Certificate No. (left padded with zeros to 3 digits)
				// Example: J12345 Certificate No. 8 --> 12345008
				// Example: J13043 Certificate No. 1004 --> 123451004 (Special case)
				String referenceNumber = Format.leftPad(globalSectionController.getJob().getJobNumber(), 5, "0");
				referenceNumber += Format.leftPad(mainCertNo, 3, "0");

				// Obtain AR Record by Reference Number
				SessionTimeoutCheck.renewSessionTimer();
				jobCostRepository.getARRecordList(null, referenceNumber, null, null, null, new AsyncCallback<List<ARRecord>>() {
					public void onSuccess(List<ARRecord> arRecordList) {
						ARRecord arRecord;
						if (arRecordList == null || arRecordList.size() == 0)
							arRecord = null;
						else if (arRecordList.size() > 1) {
							arRecord = null;
							MessageBox.alert("More than ONE Account Receivable Record is Retrieved. Please log a helpdesk call @ http://helpdesk .");
						}
						else
							arRecord = arRecordList.get(0);

						globalSectionController.getDetailSectionController().populateAccountReceivableDetailsPanel(arRecord);
						UIUtil.unmaskMainPanel();
					}

					public void onFailure(Throwable e) {
						UIUtil.alert("Failed to retrieve accounts receivable record: " + e.getMessage());
						UIUtil.unmaskMainPanel();
					}
				});
			} else {
				globalSectionController.getDetailSectionController().populateAccountReceivableDetailsPanel(null);
				UIUtil.unmaskMainPanel();
			}
		}

	}

	// modified by brian on 20110310
	// check the contra charge of last main cert equals to posted 
	private Boolean checkValidation() {
		if (selectedMainCert.getCertificateNumber() > 1) {
			// added rounding for comparison
			Double totalAmt = getTotalPostContraChargeAmt(selectedMainCert);
			if (RoundingUtil.round(previousMainCert.getCertifiedContraChargeAmount(), 2) == (RoundingUtil.round(totalAmt, 2))) {
				return Boolean.TRUE;
			} else {
				MessageBox.alert(	"The total contra charge post amount " +
									"does not match with the last contra charge amount of the last main Cert.<br/><br/>" +
									" Total Contra Charge Post Amount: " + previousMainCert.getCertifiedContraChargeAmount() + "		Contra Charge Amout of last main cert: " + totalAmt);
				return Boolean.FALSE;
			}
		} else
			return Boolean.TRUE;
	}

	private Double getTotalPostContraChargeAmt(MainContractCertificate mainCert) {
		UIUtil.maskMainPanel();
		globalSectionController.getMainContractCertificateRepository().getTotalPostContraChargeAmt(mainCert, new AsyncCallback<Double>(){

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
		return totalPostContraChargeAmt;
	}

	// modified by brian on 20110310
	private void checkIsAfterDataConvertion(){
		Double totalAmt = getTotalPostContraChargeAmt(previousMainCert);
		if(selectedMainCert.getCertificateNumber() > 1 && !totalAmt.equals(previousMainCert.getCertifiedContraChargeAmount()))
			isAfterDataConvertion = Boolean.TRUE;

	}

	/**
	 * @author koeyyeung
	 * modified on 24 April, 2013
	 * */
	public void goToPageMainCertificate(int pageNum){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainContractCertificateByPage(globalSectionController.getJob().getJobNumber(), pageNum, new AsyncCallback<PaginationWrapper<MainContractCertificate>>(){
			public void onSuccess(PaginationWrapper<MainContractCertificate> mainCertWrapper){
				MainCertificateGridPanel.mainCertWrapper = mainCertWrapper;
				//if the current page is not the first page, 
				//load the last certificate from previous page.
				if(mainCertWrapper.getCurrentPage()<mainCertWrapper.getTotalPage()-1) 
					getPreviouseMainCert();
				else{
					MainCertificateGridPanel.mainCert = null;
					populateGridWithPage();	
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser(),"GlobalSectionController."+this.getClass().getName()+".goToPageMainCertificate(int)");
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}
	
	/**
	 * @author xethhung	
	 * modified on 03 Nov, 2015
	 * Bug fix for wrong posting amount of the first certificate of pages.
	 * The bug only affecting pages with page number larger than 1.
	 * Get the last certificate from previous page.   
	 * */
	private void getPreviouseMainCert(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainContractCert(globalSectionController.getJob().getJobNumber(), mainCertWrapper.getCurrentPageContentList().get(0).getCertificateNumber()-1, new AsyncCallback<MainContractCertificate>(){
			public void onSuccess(MainContractCertificate mainCert){
				MainCertificateGridPanel.mainCert = mainCert;
				populateGridWithPage(); 
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser(),"GlobalSectionController."+this.getClass().getName()+".getMainContractCert(int,int)");
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});			
	}

	private Double obtainMainCertNetAmountMovement(MainContractCertificate latestMainCert, MainContractCertificate previousMainCert){
		Double movement = null;
		if(latestMainCert!=null){
			if(previousMainCert!=null)
				movement = latestMainCert.calculateCertifiedNetAmount()-previousMainCert.calculateCertifiedNetAmount();
			else
				movement = latestMainCert.calculateCertifiedNetAmount();
		}

		return movement;
	}

	/** @author koeyyeung
	 *  @created on 7 May, 2013
	 * */
	private String convertCertStatusToDescription(String certStatus){
		if(certStatus!=null){
			if(certStatus.equals(MainContractCertificate.CERT_CREATED))
				return MainContractCertificate.CERT_CREATED_DESC;
			if(certStatus.equals(MainContractCertificate.IPA_SENT))
				return MainContractCertificate.IPA_SENT_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_CONFIRMED))
				return MainContractCertificate.CERT_CONFIRMED_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_WAITING_FOR_APPROVAL))
				return MainContractCertificate.CERT_WAITING_FOR_APPROVAL_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_POSTED))
				return MainContractCertificate.CERT_POSTED_DESC;
		}
		return "";
	}

	public MainContractCertificate getLatestMainCert(){
		return latestMainCert;
	}

	public MainContractCertificate getLastMainCert(){
		return previousMainCert;
	}

	private MenuItem getMenuItem(String menuName, Item[] items){
		Menu subMenu = new Menu();
		for(Item item : items){
			subMenu.addItem(item);
		}		
		MenuItem menu = new MenuItem(menuName, subMenu);
		menu.setIconCls(ICON_DOWNLOAD);
		return menu; 		
	}

	public void showPreviewMainCertViewWindow(String jobNumber, String mainCertNo){
		if(globalSectionController.getCurrentWindow()== null){
			Window previewMainContractCertViewWindow = new PreviewMainContractCertViewWindow(globalSectionController);
			((PreviewMainContractCertViewWindow) previewMainContractCertViewWindow).populate(jobNumber, mainCertNo);
			previewMainContractCertViewWindow.show();
		}
	}


}
