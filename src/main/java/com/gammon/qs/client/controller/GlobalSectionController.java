package com.gammon.qs.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.AccountLedgerRepositoryRemote;
import com.gammon.qs.client.repository.AccountLedgerRepositoryRemoteAsync;
import com.gammon.qs.client.repository.AttachmentRepositoryRemote;
import com.gammon.qs.client.repository.AttachmentRepositoryRemoteAsync;
import com.gammon.qs.client.repository.BQRepositoryRemote;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemote;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.BudgetPostingRepositoryRemote;
import com.gammon.qs.client.repository.BudgetPostingRepositoryRemoteAsync;
import com.gammon.qs.client.repository.EnvironmentConfigRemote;
import com.gammon.qs.client.repository.EnvironmentConfigRemoteAsync;
import com.gammon.qs.client.repository.IVPostingHistoryRepositoryRemote;
import com.gammon.qs.client.repository.IVPostingHistoryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.JobCostRepositoryRemote;
import com.gammon.qs.client.repository.JobCostRepositoryRemoteAsync;
import com.gammon.qs.client.repository.JobRepositoryRemote;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.repository.MainCertificateAttachmentRepositoryRemote;
import com.gammon.qs.client.repository.MainCertificateAttachmentRepositoryRemoteAsync;
import com.gammon.qs.client.repository.MainContractCertificateRepositoryRemote;
import com.gammon.qs.client.repository.MainContractCertificateRepositoryRemoteAsync;
import com.gammon.qs.client.repository.MasterListRepositoryRemote;
import com.gammon.qs.client.repository.MasterListRepositoryRemoteAsync;
import com.gammon.qs.client.repository.MessageBoardAttachmentRepositoryRemote;
import com.gammon.qs.client.repository.MessageBoardAttachmentRepositoryRemoteAsync;
import com.gammon.qs.client.repository.MessageBoardRepositoryRemote;
import com.gammon.qs.client.repository.MessageBoardRepositoryRemoteAsync;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.PageRepositoryRemote;
import com.gammon.qs.client.repository.PageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.PaymentRepositoryRemote;
import com.gammon.qs.client.repository.PaymentRepositoryRemoteAsync;
import com.gammon.qs.client.repository.PropertiesRepositoryRemote;
import com.gammon.qs.client.repository.PropertiesRepositoryRemoteAsync;
import com.gammon.qs.client.repository.QrtzTriggerServiceRemote;
import com.gammon.qs.client.repository.QrtzTriggerServiceRemoteAsync;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemote;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.ResourceRepositoryRemote;
import com.gammon.qs.client.repository.ResourceRepositoryRemoteAsync;
import com.gammon.qs.client.repository.RetentionReleaseScheduleRepositoryRemote;
import com.gammon.qs.client.repository.RetentionReleaseScheduleRepositoryRemoteAsync;
import com.gammon.qs.client.repository.SCPaymentCertRepositoryRemote;
import com.gammon.qs.client.repository.SCPaymentCertRepositoryRemoteAsync;
import com.gammon.qs.client.repository.SCWorkScopeRepositoryRemote;
import com.gammon.qs.client.repository.SCWorkScopeRepositoryRemoteAsync;
import com.gammon.qs.client.repository.SingleSignOnKeyRepositoryRemote;
import com.gammon.qs.client.repository.SingleSignOnKeyRepositoryRemoteAsync;
import com.gammon.qs.client.repository.SubcontractorRepositoryRemote;
import com.gammon.qs.client.repository.SubcontractorRepositoryRemoteAsync;
import com.gammon.qs.client.repository.SystemMessageRepositoryRemote;
import com.gammon.qs.client.repository.SystemMessageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.TenderAnalysisDetailRepositoryRemote;
import com.gammon.qs.client.repository.TenderAnalysisDetailRepositoryRemoteAsync;
import com.gammon.qs.client.repository.TenderAnalysisRepositoryRemote;
import com.gammon.qs.client.repository.TenderAnalysisRepositoryRemoteAsync;
import com.gammon.qs.client.repository.TransitRepositoryRemote;
import com.gammon.qs.client.repository.TransitRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UnitRepositoryRemote;
import com.gammon.qs.client.repository.UnitRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessJobsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessJobsRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserServiceRemote;
import com.gammon.qs.client.repository.UserServiceRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.panel.detailSection.PaymentDetailGridPanel;
import com.gammon.qs.client.ui.panel.detailSection.SCDetailDetailPanel;
import com.gammon.qs.client.ui.panel.mainSection.JobGeneralInformationMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.MainCertificateGridPanel;
import com.gammon.qs.client.ui.panel.mainSection.MessageBoardMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.PackageEditorFormPanel;
import com.gammon.qs.client.ui.panel.mainSection.RepackagingListGridPanel;
import com.gammon.qs.client.ui.panel.mainSection.TransitStatusGridPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.AddNewAttachmentWindow;
import com.gammon.qs.client.ui.window.MainCertificateAttachmentWindow;
import com.gammon.qs.client.ui.window.OpenJobWindow;
import com.gammon.qs.client.ui.window.ScreenPreferenceWindow;
import com.gammon.qs.client.ui.window.ShowAttachmentWindow;
import com.gammon.qs.client.ui.window.detailSection.EditSCAddendumWindow;
import com.gammon.qs.client.ui.window.detailSection.PaymentCertViewWindow;
import com.gammon.qs.client.ui.window.detailSection.SCAddendumEnquiryWindow;
import com.gammon.qs.client.ui.window.detailSection.SCAttachmentWindow;
import com.gammon.qs.client.ui.window.detailSection.UpdateWDandCertQtyForPaymentRequisitionWindow;
import com.gammon.qs.client.ui.window.detailSection.UpdateWDandCertQtyWindow;
import com.gammon.qs.client.ui.window.mainSection.AddRevisieIPACert;
import com.gammon.qs.client.ui.window.mainSection.EditPackageHeaderWindow;
import com.gammon.qs.client.ui.window.mainSection.EditSCDatesWindow;
import com.gammon.qs.client.ui.window.mainSection.InputVendorFeedbackRateWindow;
import com.gammon.qs.client.ui.window.mainSection.SCSplitTerminateWindow;
import com.gammon.qs.client.ui.window.mainSection.TenderAnalysisEnquiryWindow;
import com.gammon.qs.client.ui.window.mainSection.TenderAnalysisSplitResourcesWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateCronTriggerWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateSCPackageControlWindow;
import com.gammon.qs.client.ui.window.mainSection.UpdateSCPaymentControlWindow;
import com.gammon.qs.client.ui.window.treeSection.JobDatesWindow;
import com.gammon.qs.client.ui.window.treeSection.SCPaymentCertReportWindow;
import com.gammon.qs.client.ui.window.treeSection.TransitViewBqItemsWindow;
import com.gammon.qs.client.ui.window.treeSection.TransitViewResourcesWindow;
import com.gammon.qs.client.ui.window.windowSection.CreateObjectSubsidiaryRuleWindow;
import com.gammon.qs.client.ui.window.windowSection.CreateSystemConstantWindow;
import com.gammon.qs.client.ui.window.windowSection.ErrorDetailsWindow;
import com.gammon.qs.client.ui.window.windowSection.RetentionReleaseScheduleInputWindow;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.domain.quartz.CronTriggers;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentPaginationWrapper;
import com.gammon.qs.wrapper.SCDetailsWrapper;
import com.gammon.qs.wrapper.currencyCode.CurrencyCodeWrapper;
import com.gammon.qs.wrapper.job.JobDatesWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class GlobalSectionController {
	private User user;

	//UI Component 
	private Panel mainPanel;
	private Panel layoutPanel;
	private Window currentWindow;
	private Window promptWindow;
	private Window addendumEnquiryAddFileWindow;
	private Window resourceIVFromBQIVWindow;

	//Remote service
	private BQRepositoryRemoteAsync bqRepository;
	private MasterListRepositoryRemoteAsync masterListRepository;
	private JobRepositoryRemoteAsync jobRepository;
	private PaymentRepositoryRemoteAsync paymentRepository;
	private PackageRepositoryRemoteAsync packageRepository;
	private JobCostRepositoryRemoteAsync jobCostRepository;
	private UserServiceRemoteAsync userService;
	private SystemMessageRepositoryRemoteAsync globalMessageRepository;
	private UnitRepositoryRemoteAsync unitRepository;
	private RepackagingEntryRepositoryRemoteAsync repackagingEntryRepository;
	private BQResourceSummaryRepositoryRemoteAsync bqResourceSummaryRepository;
	private TenderAnalysisRepositoryRemoteAsync tenderAnalysisRepository;
	private MainContractCertificateRepositoryRemoteAsync mainContractCertificateRepository;
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private EnvironmentConfigRemoteAsync environmentConfigRemoteAsync;
	private IVPostingHistoryRepositoryRemoteAsync ivPostingHistoryRepository;
	private SingleSignOnKeyRepositoryRemoteAsync singleSignOnKeyRepository;
	private QrtzTriggerServiceRemoteAsync qrtzTriggerServiceRepository;
	private TransitRepositoryRemoteAsync transitRepository;
	private BudgetPostingRepositoryRemoteAsync budgetPostingService;
	private UserAccessJobsRepositoryRemoteAsync userAccessJobsRepository;
	private RetentionReleaseScheduleRepositoryRemoteAsync retentionReleaseScheduleRepository;
	private AccountLedgerRepositoryRemoteAsync accountLedgerRepository;
	private PropertiesRepositoryRemoteAsync propertiesRepository;
	private SubcontractorRepositoryRemoteAsync subcontractorRepository;
	private MessageBoardRepositoryRemoteAsync messageBoardRepository;
	private MessageBoardAttachmentRepositoryRemoteAsync messageBoardAttachmentRepository;
	private AttachmentRepositoryRemoteAsync attachmentRepository;
	private SCPaymentCertRepositoryRemoteAsync scPaymentCertRepository;
	private SCWorkScopeRepositoryRemoteAsync scWorkScopeRepository;
	private PageRepositoryRemoteAsync pageRepository;
	private ResourceRepositoryRemoteAsync resourceRepository;
	private TenderAnalysisDetailRepositoryRemoteAsync tenderAnalysisDetailRepository;
	private MainCertificateAttachmentRepositoryRemoteAsync mainCertificateAttachmentRepository;
	
	//sub controller 
	private TreeSectionController treeSectionController;
	private MainSectionController mainSectionController;
	private DetailSectionController detailSectionController;
	private MasterListSectionController masterListSectionController;

	//global variable
	private Job job;
	private String currentRepackagingStatus;
	private String selectedPackageNumber;
	private String approvalSystemPath;
	private String baseCurrency;

	//Record Store for comboBox
	private Store unitStore;
	private Store scLineTypeStore;
	private Store unawardedPackageStore;
	private Store awardedPackageStore;
	private Store currencyCodeStore;
	
	private List<String> uneditablePackageNos;
	private List<String> awardedPackageNos;
	private List<String> uneditableUnawardedPackageNos;
	private List<String> uneditableResourceSummaryIDs;	
	private List<String> uneditableResourceIDs;
	private List<String> unawardedPackageNosUnderPaymentRequisition;
	
	private String subcontractHoldMessage;
	private String paymentHoldMessage;
	
	public GlobalSectionController(User user){
		this.user = user;

		userService = (UserServiceRemoteAsync) GWT.create(UserServiceRemote.class);
		((ServiceDefTarget)userService).setServiceEntryPoint(GlobalParameter.USER_SERVICE_URL);

		masterListRepository = (MasterListRepositoryRemoteAsync)GWT.create(MasterListRepositoryRemote.class);
		((ServiceDefTarget)masterListRepository).setServiceEntryPoint(GlobalParameter.MASTER_LIST_REPOSITORY_URL);

		bqRepository = (BQRepositoryRemoteAsync) GWT.create(BQRepositoryRemote.class);
		((ServiceDefTarget)bqRepository).setServiceEntryPoint(GlobalParameter.BQ_REPOSITORY_URL);

		jobRepository = (JobRepositoryRemoteAsync) GWT.create(JobRepositoryRemote.class);
		((ServiceDefTarget)jobRepository).setServiceEntryPoint(GlobalParameter.JOB_REPOSITORY_URL);

		paymentRepository = (PaymentRepositoryRemoteAsync) GWT.create(PaymentRepositoryRemote.class);
		((ServiceDefTarget)paymentRepository).setServiceEntryPoint(GlobalParameter.PAYMENT_REPOSITORY_URL);

		packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget)packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);

		globalMessageRepository = (SystemMessageRepositoryRemoteAsync)GWT.create(SystemMessageRepositoryRemote.class);
		((ServiceDefTarget)globalMessageRepository).setServiceEntryPoint(GlobalParameter.SYSTEM_MESSAGE_URL);

		unitRepository = (UnitRepositoryRemoteAsync)GWT.create(UnitRepositoryRemote.class);
		((ServiceDefTarget)unitRepository).setServiceEntryPoint(GlobalParameter.UNITOFMEASUREMENT_URL);

		jobCostRepository = (JobCostRepositoryRemoteAsync) GWT.create(JobCostRepositoryRemote.class);
		((ServiceDefTarget)jobCostRepository).setServiceEntryPoint(GlobalParameter.JOBCOST_REPOSITORY_URL);

		repackagingEntryRepository = (RepackagingEntryRepositoryRemoteAsync) GWT.create(RepackagingEntryRepositoryRemote.class);
		((ServiceDefTarget)repackagingEntryRepository).setServiceEntryPoint(GlobalParameter.REPACKAGING_ENTRY_REPOSITORY_URL);
		
		bqResourceSummaryRepository = (BQResourceSummaryRepositoryRemoteAsync) GWT.create(BQResourceSummaryRepositoryRemote.class);
		((ServiceDefTarget)bqResourceSummaryRepository).setServiceEntryPoint(GlobalParameter.BQ_RESOURCE_SUMMARY_REPOSITORY_URL);

		tenderAnalysisRepository = (TenderAnalysisRepositoryRemoteAsync) GWT.create(TenderAnalysisRepositoryRemote.class);
		((ServiceDefTarget)tenderAnalysisRepository).setServiceEntryPoint(GlobalParameter.TENDER_ANALYSIS_REPOSITORY_URL);

		mainContractCertificateRepository = (MainContractCertificateRepositoryRemoteAsync) GWT.create(MainContractCertificateRepositoryRemote.class);
		((ServiceDefTarget)mainContractCertificateRepository).setServiceEntryPoint(GlobalParameter.MAINCONTRACTCERTIFICATE_REPOSITORY_URL);

		transitRepository = (TransitRepositoryRemoteAsync) GWT.create(TransitRepositoryRemote.class);
		((ServiceDefTarget)transitRepository).setServiceEntryPoint(GlobalParameter.TRANSIT_REPOSITORY_URL);
		
		budgetPostingService = (BudgetPostingRepositoryRemoteAsync) GWT.create(BudgetPostingRepositoryRemote.class);
		((ServiceDefTarget)budgetPostingService).setServiceEntryPoint(GlobalParameter.BUDGET_POSTING_URL);
		
		ivPostingHistoryRepository = (IVPostingHistoryRepositoryRemoteAsync) GWT.create(IVPostingHistoryRepositoryRemote.class);
		((ServiceDefTarget)ivPostingHistoryRepository).setServiceEntryPoint(GlobalParameter.IV_POSTING_HISTORY_REPOSITORY_URL);
		
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);

		environmentConfigRemoteAsync = (EnvironmentConfigRemoteAsync) GWT.create(EnvironmentConfigRemote.class);
		((ServiceDefTarget)environmentConfigRemoteAsync).setServiceEntryPoint(GlobalParameter.ENVIRONMENT_CONFIG_URL);

		qrtzTriggerServiceRepository = (QrtzTriggerServiceRemoteAsync) GWT.create(QrtzTriggerServiceRemote.class);
		((ServiceDefTarget)qrtzTriggerServiceRepository).setServiceEntryPoint(GlobalParameter.QRTZ_TRIGGER_REPOSITORY_URL);
	
		singleSignOnKeyRepository= (SingleSignOnKeyRepositoryRemoteAsync) GWT.create(SingleSignOnKeyRepositoryRemote.class);
		((ServiceDefTarget)singleSignOnKeyRepository).setServiceEntryPoint(GlobalParameter.SINGLESIGNONKEY_REPOSITORY_URL);
		
		userAccessJobsRepository = (UserAccessJobsRepositoryRemoteAsync)GWT.create(UserAccessJobsRepositoryRemote.class);
		((ServiceDefTarget)userAccessJobsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_JOBS_REPOSITORY_URL);
		
		retentionReleaseScheduleRepository = (RetentionReleaseScheduleRepositoryRemoteAsync)GWT.create(RetentionReleaseScheduleRepositoryRemote.class);
		((ServiceDefTarget)retentionReleaseScheduleRepository).setServiceEntryPoint(GlobalParameter.RETENTION_RELEASE_SCHEDULE_REPOSITORY_URL);
		
				
		accountLedgerRepository = (AccountLedgerRepositoryRemoteAsync) GWT.create(AccountLedgerRepositoryRemote.class);
		((ServiceDefTarget)accountLedgerRepository).setServiceEntryPoint(GlobalParameter.ACCOUNT_LEDGER_URL);
		
		propertiesRepository = (PropertiesRepositoryRemoteAsync) GWT.create(PropertiesRepositoryRemote.class);
		((ServiceDefTarget)propertiesRepository).setServiceEntryPoint(GlobalParameter.APPLICATION_CONTEXT_PROPERTIES_URL);
		
		subcontractorRepository = ((SubcontractorRepositoryRemoteAsync)GWT.create(SubcontractorRepositoryRemote.class));
		((ServiceDefTarget)subcontractorRepository).setServiceEntryPoint(GlobalParameter.SUBCONTRACTOR_REPOSITORY_URL);

		messageBoardRepository = ((MessageBoardRepositoryRemoteAsync)GWT.create(MessageBoardRepositoryRemote.class));
		((ServiceDefTarget)messageBoardRepository).setServiceEntryPoint(GlobalParameter.MESSAGE_BOARD_REPOSITORY_URL);
		
		messageBoardAttachmentRepository = ((MessageBoardAttachmentRepositoryRemoteAsync)GWT.create(MessageBoardAttachmentRepositoryRemote.class));
		((ServiceDefTarget)messageBoardAttachmentRepository).setServiceEntryPoint(GlobalParameter.MESSAGE_BOARD_ATTACH_REPOSITORY_URL);
		
		attachmentRepository = ((AttachmentRepositoryRemoteAsync)GWT.create(AttachmentRepositoryRemote.class));
		((ServiceDefTarget)attachmentRepository).setServiceEntryPoint(GlobalParameter.ATTACHMENT_REPOSITORY_URL);
		
		scPaymentCertRepository = ((SCPaymentCertRepositoryRemoteAsync)GWT.create(SCPaymentCertRepositoryRemote.class));
		((ServiceDefTarget)scPaymentCertRepository).setServiceEntryPoint(GlobalParameter.SCPAYMENTCERT_REPOSITORY_URL);
		
		scWorkScopeRepository = ((SCWorkScopeRepositoryRemoteAsync)GWT.create(SCWorkScopeRepositoryRemote.class));
		((ServiceDefTarget)scWorkScopeRepository).setServiceEntryPoint(GlobalParameter.SCWORKSCOPE_REPOSITORY_URL);
		
		pageRepository = ((PageRepositoryRemoteAsync)GWT.create(PageRepositoryRemote.class));
		((ServiceDefTarget)pageRepository).setServiceEntryPoint(GlobalParameter.PAGE_REPOSITORY_URL);

		resourceRepository = ((ResourceRepositoryRemoteAsync)GWT.create(ResourceRepositoryRemote.class));
		((ServiceDefTarget)resourceRepository).setServiceEntryPoint(GlobalParameter.RESOURCE_REPOSITORY_URL);
		
		tenderAnalysisDetailRepository = ((TenderAnalysisDetailRepositoryRemoteAsync)GWT.create(TenderAnalysisDetailRepositoryRemote.class));
		((ServiceDefTarget)tenderAnalysisDetailRepository).setServiceEntryPoint(GlobalParameter.TENDERANALYSISDETAIL_REPOSITORY_URL);
		
		mainCertificateAttachmentRepository = ((MainCertificateAttachmentRepositoryRemoteAsync)GWT.create(MainCertificateAttachmentRepositoryRemote.class));
		((ServiceDefTarget)mainCertificateAttachmentRepository).setServiceEntryPoint(GlobalParameter.MAINCERTIFICATEATTACHMENT_REPOSITORY_URL);
		
		detailSectionController = new DetailSectionController(this);
		mainSectionController = new MainSectionController(this);
		treeSectionController = new TreeSectionController(this);		
		masterListSectionController =  new MasterListSectionController(this);

		setupUI();
	}
	
	
	public void cacheSCStatusCodeDescriptions() {		
		SessionTimeoutCheck.renewSessionTimer();
		unitRepository.getSCStatusCodeMap(new AsyncCallback<Map<String, String>>() {
			public void onSuccess(Map<String, String> map) {
				GlobalParameter.setScStatusCodeMap(map);
			}
			
			public void onFailure(Throwable e) {
				MessageBox.alert("Database access exception: " + e);
				GlobalParameter.setScStatusCodeMap(new HashMap<String, String>());
			}
		});
	}
	
	public void cachePerformanceGroupDescriptions() {
		SessionTimeoutCheck.renewSessionTimer();
		unitRepository.getAppraisalPerformanceGroupMap(new AsyncCallback<Map<String, String>>() {
			public void onSuccess(Map<String, String> map) {
				GlobalParameter.setPerformanceGroupMap(map);
			}
			
			public void onFailure(Throwable e) {
				MessageBox.alert("Database access exception: " + e);
				GlobalParameter.setPerformanceGroupMap(new HashMap<String, String>());
			}
		});
	}

	private void setupUI(){
		this.mainPanel = new Panel();

		this.mainPanel.setWidth("100%");
		this.mainPanel.setHeight(670);
		this.mainPanel.setId(GlobalParameter.MAIN_PANEL_ID);
		this.mainPanel.setBorder(false);
		this.mainPanel.setLayout(new FitLayout());

		this.layoutPanel = new Panel();
		this.layoutPanel.setId("layout-panel");
		this.layoutPanel.setBorder(false);
		this.layoutPanel.setLayout(new BorderLayout());

		this.layoutPanel.setWidth("100%");
		this.layoutPanel.setHeight("100%");

		BorderLayoutData westLayoutData = new BorderLayoutData(RegionPosition.WEST);
		westLayoutData.setSplit(true);
		this.layoutPanel.add(this.treeSectionController.getMainPanel(), westLayoutData);		

		Panel centerPanel = new Panel();  
		centerPanel.setBorder(false);
		centerPanel.setLayout(new BorderLayout());
		Panel centerUpPanel = new Panel();
		centerUpPanel.setBorder(false);
		centerUpPanel.setLayout(new BorderLayout());

		Panel mainSectionPanel =  this.mainSectionController.getMainPanel();		
		BorderLayoutData centerUpPanelLayoutData = new BorderLayoutData(RegionPosition.CENTER);
		centerUpPanelLayoutData.setSplit(true);		
		centerUpPanel.add(mainSectionPanel, centerUpPanelLayoutData);
		Panel masterListPanel = this.masterListSectionController.getMainPanel();
		BorderLayoutData masterListPanelLayoutData = new BorderLayoutData(RegionPosition.EAST);
		masterListPanelLayoutData.setSplit(true);
		centerUpPanel.add(masterListPanel, masterListPanelLayoutData);

		BorderLayoutData centerPanelLayoutData =new BorderLayoutData(RegionPosition.CENTER);
		centerPanelLayoutData.setSplit(true);
		centerPanel.add(centerUpPanel, centerPanelLayoutData);
		Panel detailSectionPanel = this.detailSectionController.getMainPanel();
		BorderLayoutData detailSectionPanelDataLayout = new BorderLayoutData(RegionPosition.SOUTH);
		detailSectionPanelDataLayout.setSplit(true);
		centerPanel.add(detailSectionPanel, detailSectionPanelDataLayout);

		this.layoutPanel.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));
		this.mainPanel.add(this.layoutPanel);
		RootPanel.get("gwt-main-panel").add(this.mainPanel);
		SessionTimeoutCheck.renewSessionTimer();
		environmentConfigRemoteAsync.getApprovalSystemPath(new AsyncCallback<String>(){

			public void onFailure(Throwable e) {
				approvalSystemPath = e.getMessage();
			}

			public void onSuccess(String url) {
				approvalSystemPath = url;
			}
			
		});

		//to init all the comboBox
		initComboBoxes();
		getCurrencyCodeList();
		initAlertMessage();

		DOM.removeChild(RootPanel.getBodyElement(), RootPanel.get("loading").getElement());

		showOpenJobWindow();
	}
	
	/**
	 * @author koeyyeung
	 * initiate subcontract/payment hold alert message
	 * created on 25th Nov, 2015
	 * **/
	private void initAlertMessage() {
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSubcontractHoldMessage(new AsyncCallback<String> (){
			public void onSuccess(String message) {
				subcontractHoldMessage = message;
			}
			public void onFailure(Throwable e) {
				
			}
		});
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.obtainPaymentHoldMessage(new AsyncCallback<String> (){
			public void onSuccess(String message) {
				paymentHoldMessage = message;
			}
			public void onFailure(Throwable e) {
				
			}
		});
	}


	/**
	 * @author Henry Lai
	 * Created on 27-11-2014 
	 */
	public void showMessageBoardMainPanelByTipsButton(int scrollToPosition){
		this.getDetailSectionController().resetPanel(true);
		this.getMainSectionController().resetMainPanel();
		MessageBoardMainPanel messageBoardMainPanel = new MessageBoardMainPanel(this);
		messageBoardMainPanel.getTabPanel().setActiveTab("tips");	
		if(scrollToPosition!=0){
			messageBoardMainPanel.tipsPanelScrollToPosition(scrollToPosition);
		}
		this.getMainSectionController().setContentPanel(messageBoardMainPanel);
		this.getMainSectionController().populateMainPanelWithContentPanel();
	}

	/**
	 * @author Henry Lai
	 * Created on 29-10-2014 
	 */
	public void showMessageBoardMainPanelByHomeButton(){
		this.getDetailSectionController().resetPanel(true);
		this.getMainSectionController().resetMainPanel();
		MessageBoardMainPanel messageBoardMainPanel = new MessageBoardMainPanel(this);
		messageBoardMainPanel.getTabPanel().setActiveTab("overview");
		this.getMainSectionController().setContentPanel(messageBoardMainPanel);
		this.getMainSectionController().populateMainPanelWithContentPanel();
	}

	public void showMessageBoardMainPanel(){
		this.getDetailSectionController().resetPanel(true);
		this.getMainSectionController().resetMainPanel();
		MessageBoardMainPanel messageBoardMainPanel = new MessageBoardMainPanel(this);
		this.getMainSectionController().setContentPanel(messageBoardMainPanel);
		this.getMainSectionController().populateMainPanelWithContentPanel();
	}
	
	private void initComboBoxes(){
		SessionTimeoutCheck.renewSessionTimer();
		unitRepository.getUnitOfMeasurementList(new AsyncCallback<List<UnitOfMeasurement>>(){

			public void onFailure(Throwable arg0) {
				UIUtil.checkSessionTimeout(arg0, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".initComboBoxes()");
			}

			public void onSuccess(List<UnitOfMeasurement> arg0) {
				
				if (arg0!=null){
					String[][] unitData = new String[arg0.size()][4];
					for (int i = 0; i <arg0.size();i++)
						unitData[i]= new String[]{	i+"",
							arg0.get(i).getUnitCode().trim(),							
							arg0.get(i).getUnitCode().trim() + " - " +
							arg0.get(i).getUnitDescriptiom(),
							arg0.get(i).getBalanceType()};
					GlobalSectionController.this.unitStore = new SimpleStore(new String[]{"id","unitCode", "description","2ndUnitDescription"}, unitData);
				}
			}

		});

		String[][] scLineTypeData = new String[][]{
				new String[]{"1","AP",	"AP - Advanced Payment"},
				new String[]{"2","C1",	"C1 - Contra Charges by GSL"},
				new String[]{"3","C2",	"C2 - Contra Charges by other SC"},
				new String[]{"4","D1",	"D1 - Day Work for GCL"},
				new String[]{"5","D2",	"D2 - Day Work for other SC"},
				new String[]{"6","L1",	"L1 - Claims vs. GSL"},
				new String[]{"7","L2",	"L2 - Claims vs. other SC"},
				new String[]{"8","MS",	"MS - Material On Site"},
				new String[]{"9","CF",	"CF - CPF"},
				new String[]{"10","OA",	"OA - Other Adjustment"},
				new String[]{"11","RA",	"RA - Retention Adjustment"},
				new String[]{"12","RR",	"RR - Release Retention"},
				new String[]{"13","V1",	"V1 - External VO - No Budget"},
				new String[]{"14","V2",	"V2 - Internal VO - No Budget"}
		};

		this.scLineTypeStore = new SimpleStore(new String[]{"id","lineType","description"},scLineTypeData);
	}	

	public void refreshUnawardedPackageStore(){
		if(job == null)
			return;
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getUnawardedPackageNos(job, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".refreshUnawardedPackageStore()");
			}
			public void onSuccess(String[][] unawardedPackageNos) {
				if(unawardedPackageNos == null)
					return;
				unawardedPackageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, unawardedPackageNos);
			}
		});
	}

	public void refreshAwardedPackageStore(){
		if(job == null)
			return;
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getAwardedPackageStore(job, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".refreshAwardedPackageStore()");
			}
			public void onSuccess(String[][] awardedPackageNos) {
				if(awardedPackageNos == null)
					return;
				awardedPackageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, awardedPackageNos);
			}
		});
	}
	
	public void refreshUneditablePackageNos(){
		if(job == null)
			return;
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getUneditablePackageNos(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".refreshUneditablePackageNos()");
			}
			public void onSuccess(List<String> packageNos) {
				uneditablePackageNos = packageNos;
			}
		});
	}
	
	public void refreshUneditableUnawardedPackageNos(){
		if(job == null)
			return;
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainUneditableUnawardedPackageNos(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> packageNos) {
				uneditableUnawardedPackageNos = packageNos;
			}
		});
	}
	
	public void refreshUneditableResourceSummariesID(){
		if(job == null)
			return;
		UIUtil.maskPanelById(currentWindow.getId(), GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqResourceSummaryRepository.obtainUneditableResourceSummaries(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> resourceSummariesID) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				uneditableResourceSummaryIDs = resourceSummariesID;
			}
		});
	}	
	
	public void refreshUneditableResourcesID(){
		if(job == null)
			return;
		UIUtil.maskPanelById(currentWindow.getId(), GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.obtainUneditableResources(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> resourcesID) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				uneditableResourceIDs = resourcesID;
			}
		});
	}
	
	public void refreshUnawardedPackageNosUnderPaymentRequisition(){
		if(job == null)
			return;
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainUnawardedPackageNosUnderPaymentRequisition(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> unawardedPackageNos) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
				unawardedPackageNosUnderPaymentRequisition = unawardedPackageNos;
			}
		});
	}	

	
	public void refreshAwardedPackageNos(){
		if(job == null)
			return;
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getAwardedPackageNos(job, new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".refresgAwardedPackageNos()");
			}
			public void onSuccess(List<String> packageNos) {
				awardedPackageNos = packageNos;
			}
		});
	}

	public void showOpenJobWindow()
	{
		if (this.currentWindow==null) {			
			this.currentWindow = new OpenJobWindow(this);
			this.currentWindow.setModal(true);
			this.currentWindow.show();
		} 
	}
	
	/**
	 * @author matthewatc
	 * shows interface to create new ObjectSubsidiaryRule
	 */
	public void showCreateObjectSubsidiaryRuleWindow()
	{
		if (this.promptWindow == null) {			
			this.promptWindow = new CreateObjectSubsidiaryRuleWindow(this);
			this.promptWindow.show();
		} else {
			this.promptWindow.show();
		}
	}
	
	/**
	 * @author matthewatc
	 * shows interface to create new SystemConstant
	 */
	public void showCreateSystemConstantWindow()
	{
		if (this.promptWindow == null) {			
			this.promptWindow = new CreateSystemConstantWindow(this);
			this.promptWindow.show();
		}
		else {
			this.promptWindow.show();
		}
	}
	
	/**
	 * @author matthewatc
	 * shows interface to display a list of short messages
	 * @param titleText the text to display in the window title bar
	 * @param headerText displayed in bold above the error message lines
	 * @param messages a List of short messages to display
	 */
	public void showErrorDetailsWindow(String titleText, String headerText, List<String> messages)
	{
		if (this.promptWindow==null) {			
			this.promptWindow = new ErrorDetailsWindow(this, titleText, headerText, messages);
			this.promptWindow.show();
		} else {
			this.promptWindow.show();
		}
	}
	
	public void closeCurrentWindow()
	{
		if(this.currentWindow!=null)
		{	
			this.currentWindow.close();
			this.currentWindow.destroy();
			this.currentWindow = null;
		}
	}
	
	public void closePromptWindow()
	{
		if(this.promptWindow!=null)
		{	
			this.promptWindow.close();
			this.promptWindow.destroy();
			this.promptWindow = null;
		}
	}

	public void closeAddendumEnquiryAddFileWindow()
	{
		if(this.addendumEnquiryAddFileWindow!=null)
		{	
			this.addendumEnquiryAddFileWindow.close();
			this.addendumEnquiryAddFileWindow.destroy();
			this.addendumEnquiryAddFileWindow = null;
		}
	}	
	
	
	public void showPaymentCertViewWindow(String jobNumber, String packageNo, Integer paymentCertNo){
		if(this.currentWindow== null){
			Window paymentCertViewWindow = new PaymentCertViewWindow(this);
			((PaymentCertViewWindow) paymentCertViewWindow).populate(jobNumber, packageNo, paymentCertNo);
			paymentCertViewWindow.show();
		}
	}

	public void showSCPaymentCertReportWindow(){
		if (this.currentWindow==null){
			this.currentWindow = new SCPaymentCertReportWindow(this, this.getJob().getJobNumber());
			currentWindow.show();
		}
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3 Jul 2013
	 * **/
	public void postIVMovements(boolean finalized){
		UIUtil.maskPanelById(mainSectionController.getMainPanel().getId(), GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqResourceSummaryRepository.postIVAmounts(job, user.getUsername(), finalized, new AsyncCallback<Boolean>(){
			
			public void onSuccess(Boolean result){
				if(result == null || !result){
					//ERROR
					MessageBox.alert("IV posting failed.");
				}
				else{
					MessageBox.alert("IV posting completed.");
				}
				UIUtil.unmaskPanelById(mainSectionController.getMainPanel().getId());
			}
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionController.getMainPanel().getId());
			}
		});
	}
	
	
	public void clearIVHistoryCacheAndCloseWindow(){
		SessionTimeoutCheck.renewSessionTimer();
		ivPostingHistoryRepository.clearCache(new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean success) {
				closeCurrentWindow();
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".clearIVHistoryCacheAndCloseWindow()");				
			}
		});
	}
	
	/**
	 * 
	 * @author tikywong
	 * modified on Nov 6, 2012 2:09:55 PM
	 */
	public void showSplitTerminateWindow(SCPackage scPackage, String splitOrTerminate){	
		if(this.currentWindow == null){		
			this.currentWindow = new SCSplitTerminateWindow(this, scPackage, job.getDescription(), splitOrTerminate);
			currentWindow.show();
			((SCSplitTerminateWindow)currentWindow).populateGrid(this.job.getJobNumber(), scPackage.getPackageNo());
		}
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:52:00 AM
	 * 
	 * For PackageEditorGridPanel
	 */
	public void showAttachmentWindow(String storePlace, String jobNumber, String subcontractNumber)
	{	
		if(this.currentWindow == null){		
			this.currentWindow = new ShowAttachmentWindow(this, storePlace, subcontractNumber, "c");
			currentWindow.show();
			((ShowAttachmentWindow)this.currentWindow).loadAttachmentList(jobNumber.trim()+"|"+subcontractNumber.trim());
		}
	}
	
	/**
	 * @author koeyyeung
	 * created on 03Dec, 2013
	 * For PackageEditorGridPanel
	 */
	public void showEditSCDatesWindow(SCPackage scPackage){	
		if(this.currentWindow == null){	
			SessionTimeoutCheck.renewSessionTimer();
			packageRepository.obtainSCPackage(job, scPackage.getPackageNo(), new AsyncCallback<SCPackage>(){
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
				}
				public void onSuccess(SCPackage scPackage) {
					currentWindow = new EditSCDatesWindow(GlobalSectionController.this, scPackage, false);
					currentWindow.show();
					currentWindow.setConstrainHeader(true);	
				}
			});
		}
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:48:16 AM
	 * 
	 * For SCAddendumEnquiryWindow
	 */
	public void showAttachmentInAddendumEnquiry(String storePlace, String jobNumber, String subcontractNumber, String sequenceNumber, String billNumber, String resourceNumber, String subsidiaryCode, String objectCode)
	{	
		if(this.promptWindow == null){		
			promptWindow = new ShowAttachmentWindow(this, storePlace, subcontractNumber, "a");
			promptWindow.show();
			((ShowAttachmentWindow)this.promptWindow).loadAttachmentList(jobNumber.trim()+"|"+subcontractNumber.trim()+"|"+sequenceNumber.trim()+"|"+ billNumber.trim()+"|"+ resourceNumber.trim()+"|"+ subsidiaryCode.trim()+"|"+ objectCode.trim());
		}
	}

	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:47:40 AM
	 * 
	 * For SCDetailsEditorGridPanel
	 */
	public void showAttachmentWindow(String storePlace, String jobNumber, String subcontractNumber, String sequenceNumber, String billNumber, String resourceNumber, String subsidiaryCode, String objectCode)
	{	
		if(this.currentWindow == null){		
			this.currentWindow = new ShowAttachmentWindow(this, storePlace, subcontractNumber, "c");
			currentWindow.show();
			((ShowAttachmentWindow)this.currentWindow).loadAttachmentList(jobNumber.trim()+"|"+subcontractNumber.trim()+"|"+sequenceNumber.trim()+"|"+ billNumber.trim()+"|"+ resourceNumber.trim()+"|"+ subsidiaryCode.trim()+"|"+ objectCode.trim());
		}
	}

	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:48:50 AM
	 * 
	 * For PaymentDetailGridPanel
	 */
	public void showAttachmentWindow(String storePlace, String jobNumber, String subcontractNumber, String paymentCertNo)
	{	
		if(this.currentWindow == null){		
			this.currentWindow = new ShowAttachmentWindow(this, storePlace, subcontractNumber, "c");
			currentWindow.show();
			((ShowAttachmentWindow)this.currentWindow).loadAttachmentList(jobNumber.trim()+"|"+subcontractNumber.trim()+"|"+paymentCertNo.trim());
		}
	}

	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:49:12 AM
	 * 
	 * For AddressBookDetailsWindow
	 */
	public void showAttachmentWindow(String storePlace, String vendorNumber){
		if (promptWindow == null){
			this.promptWindow = new ShowAttachmentWindow(this,storePlace,vendorNumber,"v");			
			promptWindow.show();
			((ShowAttachmentWindow)this.promptWindow).loadAttachmentList(vendorNumber.trim());
		}
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:51:12 AM
	 * 
	 * For DetailSectionController
	 */
	public void showSCAttachmentWindow( String subcontractNumber)
	{
		if(this.currentWindow == null){
			this.currentWindow = new SCAttachmentWindow(this,subcontractNumber);
			this.currentWindow.show();
		}
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:56:46 AM
	 * 
	 * For all AttachmentWindow to add new attachement
	 */
	public void showAddNewAttachmentWindow(ShowAttachmentWindow showAttachmentWindow, String nameObject, String textKey, String subcontractNumber, String sequenceNumber, String parentWindow)
	{	
		if (parentWindow.equals("p")){
			if(this.promptWindow == null){		
				promptWindow = new AddNewAttachmentWindow(showAttachmentWindow, this, nameObject, textKey, subcontractNumber, sequenceNumber, "p");
				promptWindow.show();
			}	
		} else if (parentWindow.equals("a")){
			if(this.addendumEnquiryAddFileWindow == null){		
				addendumEnquiryAddFileWindow = new AddNewAttachmentWindow(showAttachmentWindow, this, nameObject, textKey, subcontractNumber, sequenceNumber, "a");
				addendumEnquiryAddFileWindow.show();
			}	
		}  else if (parentWindow.equals("v")){
			if(this.addendumEnquiryAddFileWindow == null){		
				addendumEnquiryAddFileWindow = new AddNewAttachmentWindow(showAttachmentWindow, this, nameObject, textKey, subcontractNumber, sequenceNumber, "v");
				addendumEnquiryAddFileWindow.show();
			}	
		}

	}

	public void showEditAddendumWindow(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode)
	{
		if(this.currentWindow == null){
			this.currentWindow = new EditSCAddendumWindow(this, jobNumber, subcontractNumber, sequenceNumber, billItem, resourceNumber, subsidiaryCode, objectCode);
			((EditSCAddendumWindow)this.currentWindow).populate(jobNumber, subcontractNumber, sequenceNumber, billItem, resourceNumber, subsidiaryCode, objectCode);
			currentWindow.show();
		}		
	}	

	public void showAddendumEnquiryWindow( Integer subcontractNumber, String scLineType, boolean showButton)
	{
		if(this.currentWindow == null){
			this.currentWindow = new SCAddendumEnquiryWindow(this, this.job.getJobNumber(), this.job.getDescription(), subcontractNumber, this.mainSectionController.getPackageEditorFormPanel().getStore(), showButton);	

			currentWindow.show();
			((SCAddendumEnquiryWindow)currentWindow).searchField(this.job.getJobNumber(),  subcontractNumber.toString());
		}		
	}	

	/**
	 * @author Henry Lai
	 * Created on 27-10-2014 
	 */
	public void showWorkDoneUpdateWindow(String selectedFunction)
	{	
		if(this.currentWindow == null){
			this.currentWindow = new UpdateWDandCertQtyWindow(this, selectedFunction);
			currentWindow.show();
		}
	}

	public void showUpdateWDandCertQtyWindow(String selectedFunction){	
		if(this.currentWindow == null){
			this.currentWindow = new UpdateWDandCertQtyWindow(this, selectedFunction);
			this.mainSectionController.getPackageEditorFormPanel().getStore();
			currentWindow.show();
		}
	}

	public void showUpdateWDandCertQtyForPaymentReqWindow(String selectedFunction){
		if(this.currentWindow == null){
			this.currentWindow = new UpdateWDandCertQtyForPaymentRequisitionWindow(this, selectedFunction);
			this.mainSectionController.getPackageEditorFormPanel().getStore();
			currentWindow.show();
		}
	}
	
	public Window getCurrentWindow() {
		return currentWindow;
	}

	public void setCurrentWindow(Window currentWindow) {
		this.currentWindow = currentWindow;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	

	public void checkUserAccessAndSetJob(final Job job){
		SessionTimeoutCheck.renewSessionTimer();
		userAccessJobsRepository.canAccessJob(user.getUsername(), job.getJobNumber(), new AsyncCallback<Boolean>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".checkUserAccessAndSetJob(Job)");
			}

			public void onSuccess(Boolean access) {
				if(access){
					setJob(job);
					mainSectionController.populateJobName(job.getJobNumber()+"-"+job.getDescription());
				}
				else
					UIUtil.alert("User does not have access to this job");
			}
			
		});
	}



	/**
	 * Refresh the content of the tree view Panel 
	 * @param job The opened job
	 */
	public void refreshTreeSectionPanels(Job job){	
		resetMainSectionDetailSection();
		
		//Set job name on the main section controller tool bar
		mainSectionController.populateJobName(job.getJobNumber() + "-" + job.getDescription());
		mainSectionController.getMainPanel().doLayout();

		treeSectionController.refreshTreePanels(job);

		mainPanel.doLayout();
	}
	
	public void saveCurrentScreenPreference() {
		GridPanel mainSectionPanel = this.mainSectionController.getGridPanel();
		GridPanel detailSectionPanel = this.detailSectionController.getGridPanel();
		boolean dirty = false;
		
		if (mainSectionPanel != null) {
			StringBuffer mainSectionWidth = new StringBuffer();
			StringBuffer mainSectionOrder = new StringBuffer();
			for (int i=0; i < mainSectionPanel.getColumnModel().getColumnCount(); i++) {
				mainSectionWidth.append(mainSectionPanel.getColumnModel().getColumnWidth(i));
				mainSectionOrder.append(mainSectionPanel.getColumnModel().getColumnHeader(i));
				if (i+1 < mainSectionPanel.getColumnModel().getColumnCount()) {
					mainSectionWidth.append(",");
					mainSectionOrder.append(",");
				}
			}
			user.getScreenPreferences().put(((PreferenceSetting)mainSectionPanel).getScreenName() + "-width", mainSectionWidth.toString());
			user.getScreenPreferences().put(((PreferenceSetting)mainSectionPanel).getScreenName() + "-order", mainSectionOrder.toString());
			dirty = true;
		}

		if (detailSectionPanel != null) {
			StringBuffer detailSectionWidth = new StringBuffer();
			StringBuffer detailSectionOrder = new StringBuffer();
			for (int i=0; i < detailSectionPanel.getColumnModel().getColumnCount(); i++) {
				detailSectionWidth.append(detailSectionPanel.getColumnModel().getColumnWidth(i));
				detailSectionOrder.append(detailSectionPanel.getColumnModel().getColumnHeader(i));
				if (i+1 < detailSectionPanel.getColumnModel().getColumnCount()) {
					detailSectionWidth.append(",");
					detailSectionOrder.append(",");
				}
			}
			user.getScreenPreferences().put(((PreferenceSetting)detailSectionPanel).getScreenName() + "-width", detailSectionWidth.toString());
			user.getScreenPreferences().put(((PreferenceSetting)detailSectionPanel).getScreenName() + "-order", detailSectionOrder.toString());
			dirty = true;
		}

		if (dirty) {
			SessionTimeoutCheck.renewSessionTimer();
			this.userService.saveUserScreenPreferences(user, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					MessageBox.alert("Saved Successfully");
				}

				public void onFailure(Throwable ex) {
					UIUtil.checkSessionTimeout(ex, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".saveCurrentScreenPreference()");					
				}
			});
		}
	}
	
	public void resetCurrentScreenPreference(){
		GridPanel mainSectionPanel = this.mainSectionController.getGridPanel();
		GridPanel detailSectionPanel = this.detailSectionController.getGridPanel();
		boolean dirty = false;

		if (mainSectionPanel != null) {
			
			/*user.getScreenPreferences().remove(((PreferenceSetting)mainSectionPanel).getScreenName() + "-width");
			user.getScreenPreferences().remove(((PreferenceSetting)mainSectionPanel).getScreenName() + "-order");*/
			user.getScreenPreferences().put(((PreferenceSetting)mainSectionPanel).getScreenName() + "-width", null);
			user.getScreenPreferences().put(((PreferenceSetting)mainSectionPanel).getScreenName() + "-order", null);
			dirty = true;
		}

		if (detailSectionPanel != null) {
			/*user.getScreenPreferences().remove(((PreferenceSetting)detailSectionPanel).getScreenName() + "-width");
			user.getScreenPreferences().remove(((PreferenceSetting)detailSectionPanel).getScreenName() + "-order");*/
			user.getScreenPreferences().put(((PreferenceSetting)detailSectionPanel).getScreenName() + "-width", null);
			user.getScreenPreferences().put(((PreferenceSetting)detailSectionPanel).getScreenName() + "-order", null);
			dirty = true;
		}

		if (dirty) {
			SessionTimeoutCheck.renewSessionTimer();
			this.userService.saveUserScreenPreferences(user, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					MessageBox.alert("Reset Successfully");
				}
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);	
				}
			});
		}
	}
	
	public void saveGeneralPreference(){
		SessionTimeoutCheck.renewSessionTimer();
		this.userService.saveUserGeneralPreferences(user, new AsyncCallback<Void>() {

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}

			public void onSuccess(Void arg0) {
				
			}
		});
	}
	
	/**
	 * @author koeyyeung
	 * modified on 25Sep,2013
	 * **/
	public ColumnConfig[] applyScreenPreferences(String screenId, ColumnConfig[] columns) {
		List<ColumnConfig> result = new LinkedList<ColumnConfig>();
		String order = user.getScreenPreferences().get(screenId+"-order");
		if(order == null) {
			for(int i=0; i<columns.length;i++){
				result.add(columns[i]);
			}
		} else {
			String[] headers = order.split(",");
			String width = user.getScreenPreferences().get(screenId+"-width");
			String[] widths = width.split(",");
			for (int i = 0; i < headers.length; i++) {
				for (int j = 0; j < columns.length; j++) {
					if (columns[j].getHeader().equalsIgnoreCase(headers[i])) {
						columns[j].setWidth(Integer.parseInt(widths[i]));
						result.add(columns[j]);
					}
				}
			}

			for (int i = 0; i < columns.length; i++) {
				boolean columnExists = false;
				for (int j = 0; j < headers.length; j++) {
					if (columns[i].getHeader().equalsIgnoreCase(headers[j])) {
						columnExists = true;
						break;
					}
				}
				if (!columnExists) {
					if (headers.length > 0) {
						user.getScreenPreferences().put(screenId + "-width", null);
						user.getScreenPreferences().put(screenId + "-order", null);
						SessionTimeoutCheck.renewSessionTimer();
						userService.saveUserScreenPreferences(user, new AsyncCallback<Void>() {
							public void onSuccess(Void result) {
								// delete preference ordering in setting
								// successfully
							}

							public void onFailure(Throwable e) {
								UIUtil.throwException(e);
							}
						});
					}
					return columns;
				}
			}
		}
		return result.toArray(new ColumnConfig[columns.length]);
	}


	public void navigateToJobInformation() {
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		this.mainSectionController.resetMainPanel();
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.obtainJob(job.getJobNumber(), new AsyncCallback<Job>() {

			public void onSuccess(Job job){
				JobGeneralInformationMainPanel jobHeaderFormPanel = new JobGeneralInformationMainPanel(GlobalSectionController.this, job);

				mainSectionController.setContentPanel(jobHeaderFormPanel);
				mainSectionController.populateMainPanelWithContentPanel();
				detailSectionController.resetPanel();

				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e){
				UIUtil.checkSessionTimeout(e, true, getUser(), "GlobalSectionController." + this.getClass().getName() + ".navigateToJobInformation()");
			}

		});

	}

	public void navigateToTransitImport(final String type) {
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		if(type.equals(GlobalParameter.TRANSIT_CODE_MATCHING) || type.equals(GlobalParameter.TRANSIT_UOM_MATCHING))
			mainSectionController.navigateToTransitImport(type);
		else{
			SessionTimeoutCheck.renewSessionTimer();
			transitRepository.getTransitHeader(job.getJobNumber(), new AsyncCallback<TransitHeader>(){
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToTransitImport(String)");
				}
	
				public void onSuccess(TransitHeader header) {
					if(header == null){
						MessageBox.alert("Please create a header before importing items");
						return;
					}
					else if(!TransitHeader.HEADER_CREATED.equals(header.getStatus())){
						if(TransitHeader.TRANSIT_COMPLETED.equals(header.getStatus())){
							MessageBox.alert("Transit for the job has already been completed");
							return;
						}
						if(type.equals(GlobalParameter.TRANSIT_BQ))
							MessageBox.alert("Warning", "Please note that if you re-import BQ items, all current items and resources will be deleted");
						else if(!TransitHeader.BQ_IMPORTED.equals(header.getStatus()))
							MessageBox.alert("Warning", "Please note that if you re-import Resources, all resources will be deleted");
					}
					mainSectionController.navigateToTransitImport(type);
				}
			});
		}
	}
	
	public void navigateToTransitCodeMatching(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		mainSectionController.navigateToTransitCodeMatching();
	}
	
	public void navigateToTransitUomMatching(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		mainSectionController.navigateToTransitUomMatching();
	}
	
	public void navigateToTransitItemEnquiry() {
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.getTransitHeader(job.getJobNumber(), new AsyncCallback<TransitHeader>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToTransitItemEnquiry()");				
			}

			public void onSuccess(TransitHeader header) {
				if(header == null){
					MessageBox.alert("Please create a header to begin the transit process");
					return;
				}
				else if(TransitHeader.HEADER_CREATED.equals(header.getStatus())){
					MessageBox.alert("BQ Items have not been imported yet.");
					return;
				}
				showTransitViewBqItemsWindow();
			}
		});
	}
	
	private void showTransitViewBqItemsWindow(){
		if(currentWindow == null){
			currentWindow = new TransitViewBqItemsWindow(this);
			currentWindow.show();
		}
	}
	
	public void showTransitResourceWindow(){
		
		if(job != null){
			SessionTimeoutCheck.renewSessionTimer();
			transitRepository.getTransitHeader(job.getJobNumber(), new AsyncCallback<TransitHeader>(){
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".showTransitResourceWindows()");					
				}
	
				public void onSuccess(TransitHeader header) {
					TransitViewResourcesWindow viewTransitResourceWindow = new TransitViewResourcesWindow(GlobalSectionController.this, header);
					currentWindow = viewTransitResourceWindow;
					currentWindow.show();
				}
			});
		}
	}
	
	public void navigateToTransitHeader() {
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		if(job != null){
			SessionTimeoutCheck.renewSessionTimer();
			transitRepository.getTransitHeader(job.getJobNumber(), new AsyncCallback<TransitHeader>(){
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToTransitHeader()");					
				}
	
				public void onSuccess(TransitHeader header) {
					mainSectionController.navigateToTransitHeader(header);
				}
			});
		}
		else
			mainSectionController.navigateToTransitHeader(null);
	}
	
	public void confirmTransitResources(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
			
		try{		
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(user.getUsername(), RoleSecurityFunctions.M010010_UPDATE_TRANSIT_RESOURCES, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					List<String> accessRights = accessRightsReturned;

					if(accessRights!=null && accessRights.size()> 0 && accessRights.contains("WRITE")){
						MessageBox.confirm("Confirm", "Are you sure you want to confirm resources and group into packages?", 
								new MessageBox.ConfirmCallback(){
									public void execute(String btnID) {
										if(btnID.equals("yes")){
											UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Confirming Resources", true);
											SessionTimeoutCheck.renewSessionTimer();
											transitRepository.confirmResourcesAndCreatePackages(job.getJobNumber(), new AsyncCallback<String>(){
												public void onFailure(Throwable e) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".confirmTransitResouce().MessageBox().transitRespository");													
												}

												public void onSuccess(String error) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													if(error == null)
														MessageBox.alert("Resources confirmed successfully");
													else
														MessageBox.alert(error);
												}
											});				
										}
									}
						});
					}
					else
						MessageBox.alert("You are not authorized to confirm the transit resources.");
				}
				
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".confirmTransitResources()");					
				}
			});
			

			

		}catch(Exception e){
			UIUtil.checkSessionTimeout(e, true, getUser(), this.getClass().getName()+".confirmTransitResources()");			
		}
		
		
	}
	
	public void completeTransit(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(user.getUsername(), RoleSecurityFunctions.M010010_UPDATE_TRANSIT_RESOURCES, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					List<String> accessRights = accessRightsReturned;
					
					if(accessRights!=null && accessRights.size()> 0 && accessRights.contains("WRITE")){
						MessageBox.confirm("Confirm", 
								"Are you sure you want to complete the transit process?<br/><br/>" +
								"<b>Caution:</b>" +
								"<br/><p style=\"padding-left:47px;\">Before you complete the transit process, the Transit Verification Confirmation Form must be signed to confirm and approve all significant contract values in Transit Reconciliation Reports.</p>",
								
								new MessageBox.ConfirmCallback(){
									public void execute(String btnID) {
										if(btnID.equals("yes")){
											UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Transferring transit items.", true);
											SessionTimeoutCheck.renewSessionTimer();
											transitRepository.completeTransit(job.getJobNumber(), new AsyncCallback<String>(){
												public void onFailure(Throwable e) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".completeTransit().transitRepository.completeTransit");													
												}
				
												public void onSuccess(String error) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													if(error == null){
														MessageBox.alert("Transit completed successfully");
														createAccountMasterAfterTransit();
													}
													else
														MessageBox.alert(error);
												}
											});				
										}
									}
						});
					}
					else
						MessageBox.alert("You are not authorized to complete the transit.");
				}
				
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".completeTransit().userAccessRightRepository.getAccessRights(..)");					
				}
			});			
		}catch(Exception e){
			UIUtil.checkSessionTimeout(e, true, getUser(), getClass().getName()+".completeTransit()");			
		}
	}
	
	// added by brian on 20110127
	// create Account Master After Transit
	public void createAccountMasterAfterTransit(){
		
		boolean scResource = true;
		boolean bqResourceSummary = false;
		boolean scDetails = false;
		boolean forecast = false;
		SessionTimeoutCheck.renewSessionTimer();
		this.jobCostRepository.createAccountMasterByGroup(scResource, bqResourceSummary, scDetails, forecast, job.getJobNumber(), 
			new AsyncCallback<String>(){

				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".createAccountMasterAfterTransit().jobCostRepository.createAccountMasterByGroup()");
				}

				public void onSuccess(String errMsg) {
					if (errMsg!=null && errMsg.trim().length()>0)
						MessageBox.alert(errMsg);
				}
			
		});
	}
	
	public void postBudgetAfterTransit(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
		
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(user.getUsername(), RoleSecurityFunctions.M010010_UPDATE_TRANSIT_RESOURCES, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					List<String> accessRights = accessRightsReturned;
					
					if(accessRights!=null && accessRights.size()> 0 && accessRights.contains("WRITE")){		
						MessageBox.confirm("Confirm", "Are you sure you want to post budget details to JDE?", 
								new MessageBox.ConfirmCallback(){
									public void execute(String btnID) {
										if(btnID.equals("yes")){
											UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Posting budget.", true);
											SessionTimeoutCheck.renewSessionTimer();
											budgetPostingService.postBudget(job.getJobNumber(), user.getUsername(), new AsyncCallback<String>(){
												public void onFailure(Throwable e) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													UIUtil.checkSessionTimeout(e, false,getUser(),"GlobalSectionController."+this.getClass().getName()+".postBudgetAfterTransit().budgetPostingService.postBudget()");
												}
				
												public void onSuccess(String error) {
													UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);
													if(error == null)
														MessageBox.alert("Budget posting completed succesfully");
													else
														MessageBox.alert("Budget posting could not be completed:<br/>" + error);
												}
												
											});
										}
									}
						});
					}
					else
						MessageBox.alert("You are not authorized to post budget.");
				}
				
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".postBudgetAfterTransit().userAccessRightRespository.getAccessRights(..)");
				}
			});

			
		}catch(Exception e){
			UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".postBudgetAfterTransit()2");			
		}
	}

	public void navigateToRepackaging(){
		mainSectionController.resetMainPanel();
		detailSectionController.resetPanel();
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.getRepackagingEntriesByJob(job, new AsyncCallback<List<RepackagingEntry>> (){

			public void onSuccess(List<RepackagingEntry> repackagingEntries)
			{
				RepackagingListGridPanel repackagingListEditorGridPanel = new RepackagingListGridPanel(GlobalSectionController.this);
				repackagingListEditorGridPanel.populateGrid(repackagingEntries);

				mainSectionController.setGridPanel(repackagingListEditorGridPanel);
				mainSectionController.populateMainPanelWithGridPanel();

				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e)
			{
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToRepackaging()");
			}

		});
	}

	//END: Job Method//
	///////////////////


	/////////////////////////////////////
	// START: Payment (SC) Methods //////

	
	
	// modified by brian on 20110317
	// add line type for filtering
	public void navigateToPaymentDetails(final String packageNo, final Integer paymentCertNo, final String lineTypeFilter) {
		//UIUtil.alert("Job Number : " +this.job.getJobNumber() + "  Package No : "+packageNo + " Payment Cert No : "+paymentCertNo);
		UIUtil.maskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		//this.mainSectionController.resetEditorGridPanel();
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.refreshPaymentDetailsCache(this.job.getJobNumber(), packageNo, paymentCertNo, lineTypeFilter, new AsyncCallback<PaymentPaginationWrapper>() {

			public void onSuccess(PaymentPaginationWrapper wrapper) {

				try{
					PaymentDetailGridPanel paymentDetailGridPanel = new PaymentDetailGridPanel(GlobalSectionController.this, job.getJobNumber(), packageNo, paymentCertNo);
					paymentDetailGridPanel.populateGrid(wrapper);

					detailSectionController.getMainPanel().setTitle("Payment Cert No.: "+paymentCertNo);
					detailSectionController.setGridPanel(paymentDetailGridPanel);
					detailSectionController.populateGridPanel();

					UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);

				}catch(Exception e){
					UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToPaymentDetails(String,Integer,String).Panel");
					UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToPaymentDetails(String,Integer,String)");
				UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
			}
		});
	}
	
	public void goToPaymentDetailsPage(final PaymentDetailGridPanel panel, int pageNum){
		UIUtil.maskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.getPaymentDetailsByPage(pageNum, new AsyncCallback<PaymentPaginationWrapper>(){
			public void onSuccess(PaymentPaginationWrapper wrapper) {
				panel.populateGrid(wrapper);
				UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".goToPaymentDetailsPage(PaymentDetailGridPanel,int)");
			}
		});
	}


	// END: Payment (SC) Methods ////////
	/////////////////////////////////////

	public void resetMainSectionDetailSection(){
		this.mainSectionController.resetMainPanel();
		this.detailSectionController.resetPanel();
	}

	public void openSreenPreferenceWindow(){
		if(this.currentWindow==null){
			this.currentWindow = new ScreenPreferenceWindow(this);
			this.currentWindow.show();
		}
	}

	public BQRepositoryRemoteAsync getBqRepository() {
		return bqRepository;
	}

	public void setBqRepository(BQRepositoryRemoteAsync bqRepository) {
		this.bqRepository = bqRepository;
	}

	public void populateSCPackagesTree(){
		SessionTimeoutCheck.renewSessionTimer();
		this.packageRepository.getSCPackages(job, new AsyncCallback<List<SCPackage>>(){
			public void onSuccess(List<SCPackage> scPackages) {
				treeSectionController.refreshPackageTreePanel();
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".populateSCPackagesTree()");
			}
		});
	}

	

	public void populateSCPackageMainPanelandDetailPanel(final String packageNo) {
		UIUtil.maskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		// Main section
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSCPackage(job, packageNo, new AsyncCallback<SCPackage>() {
			public void onSuccess(final SCPackage scPackage) {
				SessionTimeoutCheck.renewSessionTimer();
				getPaymentRepository().obtainPaymentLatestCert(scPackage.getJob().getJobNumber(), scPackage.getPackageNo(), new AsyncCallback<SCPaymentCert>() {
					public void onSuccess(SCPaymentCert latestPaymentCert) {
						mainSectionController.resetMainPanel();
						mainSectionController.showPackageFormPanel(scPackage, latestPaymentCert);

						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						// Detail section
						if (scPackage.isAwarded()) {
							SessionTimeoutCheck.renewSessionTimer();
							packageRepository.getSCDetailsOfPackageByPagination(job.getJobNumber(), packageNo, "", new AsyncCallback<PaginationWrapper<SCDetailsWrapper>>() {
								public void onSuccess(PaginationWrapper<SCDetailsWrapper> scDetailsWrapper){
									detailSectionController.resetPanel();
									detailSectionController.populateGridPanelForSCDetails(scDetailsWrapper);
									UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
								}
								public void onFailure(Throwable e) {
									UIUtil.throwException(e);
								}
							});
						}
						else 
							populateTenderAnalysisComparisonPanel(packageNo);
						
						
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
					}
				});
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}	
	
	public PackageEditorFormPanel getPackageEditorFormPanel() {
		return mainSectionController.getPackageEditorFormPanel();
	}
	
	public void showTenderAnalysisEnquiryWindow(String packageNo) {
		SessionTimeoutCheck.renewSessionTimer();
		tenderAnalysisRepository.getTenderAnalysisComparison(job, packageNo, new AsyncCallback<TenderAnalysisComparisonWrapper>(){
			public void onSuccess(TenderAnalysisComparisonWrapper comparisonWrapper) {	
				populateTenderAnalysisEnquiryWindow(comparisonWrapper);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, getUser());				
			}
		});
	}
	
	public void populateTenderAnalysisEnquiryWindow(final TenderAnalysisComparisonWrapper tenderAnalysisComparison) {
		
		String jobNumber = getJob().getJobNumber();
		SessionTimeoutCheck.renewSessionTimer();
		getPackageRepository().getCompanyBaseCurrency(jobNumber, new AsyncCallback<String>(){
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, getUser());				
			}
			
			public void onSuccess(String currency) {
				if(currentWindow == null) {			
					currentWindow = new TenderAnalysisEnquiryWindow(detailSectionController, tenderAnalysisComparison, currency);
					currentWindow.show();
				}
			}
		});
	}

	



	public void populateTenderAnalysisComparisonPanel(String packageNo){
		SessionTimeoutCheck.renewSessionTimer();
		tenderAnalysisRepository.getTenderAnalysisComparison(job, packageNo, new AsyncCallback<TenderAnalysisComparisonWrapper>(){
			public void onSuccess(TenderAnalysisComparisonWrapper comparisonWrapper)
			{	
				detailSectionController.resetPanel();
				detailSectionController.populateGridPanelForTenderAnalysisComparison(comparisonWrapper);
				UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".populateTenderAnalysisComparisonPanel(String)");				
			}
		});
	}

	public void showTenderAnalysisSplitResourcesWindow(final String packageNo){
		if(currentWindow != null)
			return;
		try {
			//Check that no TA details already exist
			SessionTimeoutCheck.renewSessionTimer();
			tenderAnalysisRepository.getTenderAnalyses(job, packageNo, new AsyncCallback<List<TenderAnalysis>>(){
				public void onSuccess(List<TenderAnalysis> tenderAnalyses) {
					if(tenderAnalyses.size() > 1){
						MessageBox.confirm("Warning", "Vendor feedback rates have already been input. If you continue, these records will be deleted.<br/>Continue?", new MessageBox.ConfirmCallback(){
							public void execute(String btnID) {
								if(btnID.equals("yes")){
									getResourcesAndShowTAWindow(packageNo);
									SessionTimeoutCheck.renewSessionTimer();
									tenderAnalysisRepository.deleteTenderAnalyses(job, packageNo, new AsyncCallback<Boolean>(){
										public void onSuccess(Boolean arg0) {
										}
										public void onFailure(Throwable e) {	
											UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showtenderAnalysisSplitResourcesWindows(String).tenderAnalysisRepository.deleteTenderAnalysis(..)");
										}
									});
								}
							}
						});
					}
					else
						getResourcesAndShowTAWindow(packageNo);
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showTenderAnalysisSplitResourcesWindow(String)");
				}
			});
		} catch (Exception e) {
//			UIUtil.alert(e);
			UIUtil.checkSessionTimeout(e, true, getUser(),"GlobalSectionController."+this.getClass().getName()+".showTenderAnalysisSplitResourcesWindow(String)2");
		}
	}

	
	public void getTenderAnalysisVendorUpload(){
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Loading", true);
			SessionTimeoutCheck.renewSessionTimer();
			tenderAnalysisRepository.getVendorTACache(new AsyncCallback<TenderAnalysis>(){
				public void onSuccess(TenderAnalysis vendorTA) {
					// added by brian on 20110223 - start
					final TenderAnalysis tmpVendorTA = vendorTA;
					SessionTimeoutCheck.renewSessionTimer();
					GlobalSectionController.this.packageRepository.getCompanyBaseCurrency(GlobalSectionController.this.getJob().getJobNumber(), new AsyncCallback<String>(){
						
						public void onFailure(Throwable e) {
//							UIUtil.alert(e);
							UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+"."+this.getClass().getName()+".getTenderAnalysisVendorUpload().packageRepository.getCompanyBaseCurrency(Job)");
						}
						
						public void onSuccess(String currency) {
							InputVendorFeedbackRateWindow inputWindow = new InputVendorFeedbackRateWindow(GlobalSectionController.this, selectedPackageNumber, tmpVendorTA.getVendorNo(), currency);
							inputWindow.populateGrid(obtainTenderAnalysisDetailByTenderAnalysis(tmpVendorTA), tmpVendorTA.getCurrencyCode(), tmpVendorTA.getExchangeRate());
							/*currentWindow = inputWindow;
							currentWindow.show();*/
						}
					});
					// added by brian on 20110223 - end
					UIUtil.unmaskMainPanel();
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskMainPanel();
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".getTenderAnalysisVendorUpload()");
				}
			});
		}
		catch(Exception e){
//			UIUtil.alert(e);
			UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+"."+this.getClass().getName()+".getTenderAnalysisVendorUpload()2");
		}
	}

	public List<TenderAnalysisDetail> obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis tenderAnalysis){
		UIUtil.maskMainPanel();
		final List<TenderAnalysisDetail> tenderAnalysisDetail = new ArrayList<TenderAnalysisDetail>();
		try {
			tenderAnalysisDetailRepository.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis, new AsyncCallback<List<TenderAnalysisDetail>>(){
				
				@Override
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskMainPanel();
				}

				@Override
				public void onSuccess(List<TenderAnalysisDetail> result) {
					UIUtil.unmaskMainPanel();
					tenderAnalysisDetail.addAll(result);
				}
			});
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return tenderAnalysisDetail;
	}
	
	public void getResourcesAndShowTAWindow(final String packageNo){
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Loading Resources", true);
		try {
			SessionTimeoutCheck.renewSessionTimer();
			bqResourceSummaryRepository.getResourceSummariesSearch(job, packageNo, "14*", null, new AsyncCallback<List<BQResourceSummary>>(){	
				public void onSuccess(final List<BQResourceSummary> resourceSummaries){
					SessionTimeoutCheck.renewSessionTimer();
					tenderAnalysisRepository.getTenderAnalysisDetails(job, packageNo, 0, new AsyncCallback<List<TenderAnalysisDetail>>(){
						public void onSuccess(final List<TenderAnalysisDetail> taDetails){
							TenderAnalysisSplitResourcesWindow window = new TenderAnalysisSplitResourcesWindow(GlobalSectionController.this, packageNo);
							window.populateGrid(resourceSummaries);
							if(taDetails != null)
								window.populateTAGrid(taDetails);
							currentWindow = window;
							currentWindow.show();
							UIUtil.unmaskMainPanel();
						}
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".getResoucesAndShowTAWindow(String).tenderAnalysisRepository.getTenderAnaysisDetails()");		
							UIUtil.unmaskMainPanel();
						}
					});
				}

				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".getResourcesAndShowTAWindow");		
					UIUtil.unmaskMainPanel();
				}
			});
		} catch (Exception e) {
//			UIUtil.alert(e);
			UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".getResourcesAndShowTAWindow()2");
			UIUtil.unmaskMainPanel();
		}
	}
	
	public void deleteTenderAnalysis(final String packageNo, Integer vendorNo){
		UIUtil.maskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		tenderAnalysisRepository.deleteTenderAnalysis(job.getJobNumber(), packageNo, vendorNo, new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean success) {
				if(success)
					populateTenderAnalysisComparisonPanel(packageNo);
				else{
					MessageBox.alert("Could not find vendor TA record OR direct payment exists.");
					UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);	
				}
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".deleteTenderAnalysis(String,Integer)");
				UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);	
			}			
		});
	}

	public void showInputVendorFeedbackRateWindow(final String packageNo, final Integer vendorNo){
		if(currentWindow != null)
			return;
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, "Loading", true);
		//Validate the vendor no first
		SessionTimeoutCheck.renewSessionTimer();
		masterListRepository.searchVendorList(vendorNo.toString(), new AsyncCallback<List<MasterListVendor>>(){
			public void onSuccess(List<MasterListVendor> vendors) {
				if(vendors == null){
					MessageBox.alert("Vendor No is invalid");
					UIUtil.unmaskMainPanel();
					return;
				}
				boolean match = false;
				for(MasterListVendor vendor : vendors){
					if(vendor.getVendorNo().equals(vendorNo.toString())){
						if(vendor.getScFinancialAlert()!= null && !"".equals(vendor.getScFinancialAlert())){
							MessageBox.confirm("SC Credit Warning", subcontractHoldMessage,
									new MessageBox.ConfirmCallback() {
										public void execute(String btnID) {
											if(btnID.equals("yes")){
												openTenderAnalysisForRateInputWindow(packageNo, vendorNo);
											}else{
												return;
											} 
										}
									});						
						} else {
							openTenderAnalysisForRateInputWindow(packageNo, vendorNo);
						}
						UIUtil.unmaskMainPanel();
						match = true;
						break;
					}
				}
				if(!match) {
					MessageBox.alert("Vendor No is invalid");
					UIUtil.unmaskMainPanel();
					return;
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showInputVendorFeedbackRateWindow().searchVendorList()");
			}
		});
	}
	
	/**
	 * Open tender analysis for rate input window
	 * @param packageNo
	 * @param vendorNo
	 * @author paulnpyiu
	 * @since Jan 22, 2016
	 */
	public void openTenderAnalysisForRateInputWindow(final String packageNo, final Integer vendorNo){
		//If vendor no is valid retrieve ta details and show window
		SessionTimeoutCheck.renewSessionTimer();
		tenderAnalysisRepository.getTenderAnalysisForRateInput(job, packageNo, vendorNo, new AsyncCallback<TenderAnalysis>(){
			public void onSuccess(TenderAnalysis tenderAnalysis) {
				UIUtil.unmaskMainPanel();
				
				final List<TenderAnalysisDetail> taDetails = obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
				if(taDetails == null || taDetails.size() == 0){
					MessageBox.alert("Please prepare the package for tender analysis (you must split/merge the resources)");
					return;
				}
				String currencyCode = tenderAnalysis.getCurrencyCode();
				Double exchangeRate = tenderAnalysis.getExchangeRate();
				if(currencyCode == null || currencyCode.length() == 0){
					currencyCode = job.getBillingCurrency();
					exchangeRate = 1.0;
				}
				// added by brian on 20110223 - start
				final String finalCurrencyCode = currencyCode;
				final Double finalExchangeRate = exchangeRate;
				SessionTimeoutCheck.renewSessionTimer();
				GlobalSectionController.this.packageRepository.getCompanyBaseCurrency(GlobalSectionController.this.getJob().getJobNumber(), new AsyncCallback<String>(){
					
					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showInputVendorFeedbackRateWindow().packageRepository.getCompanyBaseCurrency()");
					}
					
					public void onSuccess(String currency) {
						InputVendorFeedbackRateWindow inputWindow = new InputVendorFeedbackRateWindow(GlobalSectionController.this, packageNo, vendorNo, currency);
						inputWindow.populateGrid(taDetails, finalCurrencyCode, finalExchangeRate);
						/*currentWindow = inputWindow;
						currentWindow.show();*/
					}
				});
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showInputVendorFeedbackRateWindow(..).tenderAnalysisRepository.getTenderAnalysisForRateInput()");
			}
		});
	}

	public void goToPagePackageSCDetails(int pageNum){

		UIUtil.maskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getSCDetailListAtPage(pageNum, new AsyncCallback<PaginationWrapper<SCDetailsWrapper>>(){
			public void onSuccess(PaginationWrapper<SCDetailsWrapper> result){

				((SCDetailDetailPanel)detailSectionController.getGridPanel()).populate(result);	
				UIUtil.unmaskPanelById(GlobalParameter.DETAIL_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".goToPagePackageSCDetails(int)");				
			}


		});


	}

	public User getUser() {
		return user;
	}

	public void setCurrentRepackagingStatus(String currentRepackagingStatus) {
		this.currentRepackagingStatus = currentRepackagingStatus;
	}

	public String getCurrentRepackagingStatus() {
		return currentRepackagingStatus;
	}

	public String getSelectedPackageNumber() {
		return selectedPackageNumber;
	}

	public void setSelectedPackageNumber(String selectedPackageNumber) {
		this.selectedPackageNumber = selectedPackageNumber;
	}


	//combox 
	public Store getUnitStore() {
		return unitStore;
	}

	public Store getScLineTypeStore() {
		return scLineTypeStore;
	}

	public Store getUnawardedPackageStore(){
		return unawardedPackageStore;
	}

	public Store getAwardedPackageStore(){
		return awardedPackageStore;
	}
	public PackageRepositoryRemoteAsync getPackageRepository() {
		return packageRepository;
	}	


	public void saveTenderAnalysisDetails(final String packageNo, final Integer vendorNo, String currencyCode, 
			Double exchangeRate, List<TenderAnalysisDetail> taDetails, boolean validate){
		if(currentWindow != null)
			UIUtil.maskPanelById(currentWindow.getId(), "Saving", true);
		SessionTimeoutCheck.renewSessionTimer();
		tenderAnalysisRepository.updateTenderAnalysisDetails(job, packageNo, vendorNo, currencyCode, 
				exchangeRate, taDetails, validate, new AsyncCallback<String>(){
			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				if(error != null && error.length() > 0){
					MessageBox.alert(error);
				}
				else{
					closeCurrentWindow();
					if(vendorNo.equals(Integer.valueOf(0))){
						refreshUnawardedPackageStore();
						refreshAwardedPackageStore();
					}
					populateSCPackageMainPanelandDetailPanel(packageNo);
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(currentWindow.getId());
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".saveTenderAnaysisDetails(String,Integer,String,Double,List,boolean)");				
			}
		});
	}

	public void showEditPackageHeaderWindow(final SCPackage scPackage){
		if (this.currentWindow==null){
			SessionTimeoutCheck.renewSessionTimer();
			packageRepository.obtainSCPackage(job,scPackage.getPackageNo(), new AsyncCallback<SCPackage>() {

				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".showEditPackageHeaderWindow(SCPackage)");
				}

				public void onSuccess(SCPackage editPackage) {
					if (editPackage!=null)
						currentWindow = new EditPackageHeaderWindow(GlobalSectionController.this, editPackage);
					else
						currentWindow = new EditPackageHeaderWindow(GlobalSectionController.this, scPackage);
					currentWindow.show();
					
				}
				
			});
		}
	}

	public void showRevisieIPACert(MainCertificateGridPanel panel, Integer certificateNumber) {
		if (this.currentWindow==null){
			this.currentWindow = new AddRevisieIPACert(panel,this,certificateNumber,AddRevisieIPACert.UPDATE);
			currentWindow.show();
		}

	}

	public void showAddIPACert(MainCertificateGridPanel panel, Integer certificateNumber){
		if (this.currentWindow==null){
			this.currentWindow = new AddRevisieIPACert(panel,this,certificateNumber, AddRevisieIPACert.ADD);
			currentWindow.show();
		}
	}

	public MainContractCertificateRepositoryRemoteAsync getMainContractCertificateRepository() {
		return mainContractCertificateRepository;
	}	
	
	
	public void setUneditablePackageNos(List<String> uneditablePackageNos) {
		this.uneditablePackageNos = uneditablePackageNos;
	}

	public List<String> getUneditablePackageNos() {
		return uneditablePackageNos;
	}
	
	public List<String> getAwardedPackageNos(){
		return awardedPackageNos;
	}

	public List<String> getUneditableUnawardedPackageNos() {
		return uneditableUnawardedPackageNos;
	}

	public void setUneditableUnawardedPackageNos(List<String> uneditableUnawardedPackageNos) {
		this.uneditableUnawardedPackageNos = uneditableUnawardedPackageNos;
	}
	public List<String> getUneditableResourceSummaries() {
		return uneditableResourceSummaryIDs;
	}
	public void setUneditableResourceSummaries(List<String> uneditableResourceSummaries) {
		this.uneditableResourceSummaryIDs = uneditableResourceSummaries;
	}
	public List<String> getUnawardedPackageNosUnderPaymentRequisition() {
		return unawardedPackageNosUnderPaymentRequisition;
	}
	public void setUnawardedPackageNosUnderPaymentRequisition(List<String> unawardedPackageNosUnderPaymentRequisition) {
		this.unawardedPackageNosUnderPaymentRequisition = unawardedPackageNosUnderPaymentRequisition;
	}
	public List<String> getUneditableResourceIDs() {
		return uneditableResourceIDs;
	}
	public void setUneditableResourceIDs(List<String> uneditableResourceIDs) {
		this.uneditableResourceIDs = uneditableResourceIDs;
	}


	public MasterListRepositoryRemoteAsync getMasterListRepository() {
		return masterListRepository;
	}

	public void setMasterListRepository(
			MasterListRepositoryRemoteAsync masterListRepository) {
		this.masterListRepository = masterListRepository;
	}

	public void setApprovalSystemPath(String approvalSystemPath) {
		this.approvalSystemPath = approvalSystemPath;
	}

	public String getApprovalSystemPath() {
		return approvalSystemPath;
	}

	public void insertSingleSignOnKey(String description) {
		try{
			SessionTimeoutCheck.renewSessionTimer();
           singleSignOnKeyRepository.getSingleSignOnKey(description, getUser().getUsername(), new AsyncCallback<String>(){

			public void onFailure(Throwable arg0) {
				
			}

			public void onSuccess(String arg0) {
				if (getApprovalSystemPath()!=null && !"".equals(getApprovalSystemPath().trim()))
					com.google.gwt.user.client.Window.open(getApprovalSystemPath()+"dispatcherApproval.htm?preAuthenticated="+arg0, "Approval_Enquiry", "menubar=1,resizable=1,scrollbars=yes");
				else
					com.google.gwt.user.client.Window.open("/../GammonQSuat/ap/enquiry/index.htm", "Approval_Enquiry", "menubar=1,resizable=1,scrollbars=yes");
			
			}
			
		});
		}
		catch(Exception e){
			e.printStackTrace();
			UIUtil.alert("Failed insert Single SignOn");
		}
		
	
	}

	public void updateJobInfo(Job job) {
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Saving", true);
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.updateJobHeaderInfo(job, new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				if(result != null && result==true)
					MessageBox.alert("Saved Changes");
				else{
					MessageBox.alert("Save Failed - Job does not exist");
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".updateJobInfo(Job)");
			}
		});
	}


	public PaymentRepositoryRemoteAsync getPaymentRepository() {
		return paymentRepository;
	}

	public JobRepositoryRemoteAsync getJobRepository() {
		return jobRepository;
	}

	public SystemMessageRepositoryRemoteAsync getGlobalMessageRepository() {
		return globalMessageRepository;
	}

	public QrtzTriggerServiceRemoteAsync getQrtzTriggerServiceRepository() {
		return qrtzTriggerServiceRepository;
	}

	

	public void openCronTriggerWindows(String triggerName, String triggerGroup) {
		if (this.currentWindow!=null){
			currentWindow.close();
			currentWindow.destroy();
		}
		SessionTimeoutCheck.renewSessionTimer();
		qrtzTriggerServiceRepository.getCronTrigger(triggerName, triggerGroup, new AsyncCallback<CronTriggers>(){

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".openCronTriggerWindows(String,String)");
			}

			public void onSuccess(CronTriggers cronTrigger) {
				currentWindow = new UpdateCronTriggerWindow(GlobalSectionController.this,cronTrigger);
				currentWindow.show();
			}
			
		});
		
	}

	public UserAccessRightsRepositoryRemoteAsync getUserAccessRightsRepository() {
		return userAccessRightsRepository;
	}

	

	
	public JobCostRepositoryRemoteAsync getJobCostRepository() {
		return jobCostRepository;
	}

	public TransitRepositoryRemoteAsync getTransitRepository() {
		return transitRepository;
	}

	public void setTransitRepository(TransitRepositoryRemoteAsync transitRepository) {
		this.transitRepository = transitRepository;
	}
	

	// added by brian on 20110223
	public String getBaseCurrency() {
		return baseCurrency;
	}

	// added by brian on 20110223
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	// added by brian on 20110301
	public void navigateToTransitStatus(String status, final boolean checked){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		
		// by default get all transit status by passing empty
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.getAllTransitHeaders(status, new AsyncCallback<List<TransitHeader>>(){
			
			public void onFailure(Throwable e) {
//				UIUtil.alert(e);
				UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToTransitStatus(String,boolean)");
			}
			
			
			public void onSuccess(List<TransitHeader> headersList) {
				mainSectionController.resetMainPanel();
				TransitStatusGridPanel transitStatusGridPanel = new TransitStatusGridPanel(GlobalSectionController.this, checked);
				transitStatusGridPanel.populateGrid(headersList);
				mainSectionController.setGridPanel(transitStatusGridPanel);
				mainSectionController.populateMainPanelWithGridPanel();
				detailSectionController.resetPanel();
			}
			
		});
		UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
		
	}
	
	
	// added by brian on 20110421
	// get the currency code list
	public void getCurrencyCodeList(){
		String currencyCode = "";
		SessionTimeoutCheck.renewSessionTimer();
		this.packageRepository.getCurrencyCodeList(currencyCode, new AsyncCallback<List<CurrencyCodeWrapper>>(){
			
			public void onFailure(Throwable msg) {
//				UIUtil.alert(msg);
				UIUtil.checkSessionTimeout(msg, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".getCurrencyCodeList()");				
			}
			
			public void onSuccess(List<CurrencyCodeWrapper> currencyCodeList) {
				GlobalSectionController.this.currencyCodeStore = new SimpleStore(new String[]{"currencyCode","currencyDescription"}, currencyCodeWrapperToStore(currencyCodeList));
			}});
	}
	
	// added by brian on 20110421
	// used by getCurrencyCodeList
	// convert CurrencyCodeWrapper list to Store
	private String[][] currencyCodeWrapperToStore(List<CurrencyCodeWrapper> currencyCodeList){
		if(currencyCodeList == null){
			UIUtil.alert("Currency Code List is null");
			return new String[0][2];
		}
		
		String[][] currencyData = new String[currencyCodeList.size()][2];
		for (int i = 0; i <currencyCodeList.size();i++)
			currencyData[i]= new String[]{ 
				currencyCodeList.get(i).getCurrencyCode(), 
				currencyCodeList.get(i).getCurrencyCode() + " - " + currencyCodeList.get(i).getCurrencyDescription()};
		return currencyData;
	}

	// added by brian on 20110421
	public void setCurrencyCodeStore(Store currencyCodeStore) {
		this.currencyCodeStore = currencyCodeStore;
	}

	// added by brian on 20110421
	public Store getCurrencyCodeStore() {
		return currencyCodeStore;
	}
	
	
	// added by brian on 20110427
	public void navigateToJobDates(){
		
		if(currentWindow != null){
			currentWindow.close();
			currentWindow.destroy();
		}
		
		UIUtil.maskMainPanel();
		
		if(this.job != null){
			SessionTimeoutCheck.renewSessionTimer();
			this.jobRepository.getJobDatesByJobNumber(this.job.getJobNumber(), new AsyncCallback<JobDatesWrapper>(){
				
				public void onFailure(Throwable e) {
//					UIUtil.alert(e.getMessage());
					UIUtil.checkSessionTimeout(e, true,getUser(),"GlobalSectionController."+this.getClass().getName()+".navigateToJobDates()");
					UIUtil.unmaskMainPanel();
				}

				public void onSuccess(JobDatesWrapper jobDates) {
					currentWindow = new JobDatesWindow(GlobalSectionController.this, jobDates);
					UIUtil.unmaskMainPanel();
					currentWindow.show();
				}
			});
		}
		else
			MessageBox.alert("Please select a job first");
	}
	
	public RetentionReleaseScheduleRepositoryRemoteAsync getRetentionReleaseScheduleRepository() {
		return retentionReleaseScheduleRepository;
	}

	public UserAccessJobsRepositoryRemoteAsync getUserAccessJobsRepository() {
		return userAccessJobsRepository;
	}

	public void setUserAccessJobsRepository(UserAccessJobsRepositoryRemoteAsync userAccessJobsRepository) {
		this.userAccessJobsRepository = userAccessJobsRepository;
	}

	
	public void showRetentionReleaseInputWindow(MainContractCertificate mainCert, AddRevisieIPACert addRevisieIPACert) {

		RetentionReleaseScheduleInputWindow mainCertWindow = new RetentionReleaseScheduleInputWindow(this);
		mainCertWindow.populateGrid(mainCert, addRevisieIPACert);
		mainCertWindow.show();
	}

	/**
	 * @author peterchan
	 * May 9, 2011 9:46:20 AM
	 */
	public void closeIVBQResouceCurrentWindow() {
		if(resourceIVFromBQIVWindow != null){
			resourceIVFromBQIVWindow.close();
			resourceIVFromBQIVWindow.destroy();
			resourceIVFromBQIVWindow = null;
		}else if(this.currentWindow!=null){	
			this.currentWindow.close();
			this.currentWindow.destroy();
			this.currentWindow = null;
		}
	}
	
	/**
	 * 
	 * @author peterchan
	 * May 11, 2011 4:14:17 PM
	 */
	public BQResourceSummaryRepositoryRemoteAsync getBqResourceSummaryRepository() {
		return bqResourceSummaryRepository;
	}

	/**
	 * Open windows for SC Package admin function
	 * 
	 * @author peterchan 
	 * Date: Nov 7, 2011
	 * @param scPackage
	 */
	public void showUpdateSCPackageWindows(SCPackage scPackage) {
		closeCurrentWindow();
		currentWindow = new UpdateSCPackageControlWindow(this);
		((UpdateSCPackageControlWindow)currentWindow).populateWindows(scPackage);
		currentWindow.show();
	}

	/**
	 * Open windows for update SC Payment Cert admin function
	 *  
	 * @author peterchan 
	 * Date: Nov 7, 2011
	 * @param scPaymentCert
	 */
	public void showUpdateSCPaymentCertWindows(SCPaymentCert scPaymentCert) {
		closeCurrentWindow();
		UpdateSCPaymentControlWindow updateWindow = new UpdateSCPaymentControlWindow(this);
		updateWindow.populateWindows(scPaymentCert);
		currentWindow=updateWindow;
		currentWindow.show();
		currentWindow.doLayout();
	}

	/**
	 * 
	 * @author tikywong
	 * created on Jan 27, 2012 2:07:16 PM
	 */
	public void showMainCertificateAttachmentWindow(GlobalSectionController globalSectionController, String jobNumber, Integer mainCertNumber) {
		closeCurrentWindow();
		currentWindow = new MainCertificateAttachmentWindow(globalSectionController, jobNumber, mainCertNumber);
		currentWindow.show();		
	}
	
	public TreeSectionController getTreeSectionController() {
		return treeSectionController;
	}

	public void setTreeSectionController(TreeSectionController treeSectionController) {
		this.treeSectionController = treeSectionController;
	}

	public MainSectionController getMainSectionController() {
		return mainSectionController;
	}

	public void setMainSectionController(MainSectionController mainSectionController) {
		this.mainSectionController = mainSectionController;
	}

	public DetailSectionController getDetailSectionController() {
		return detailSectionController;
	}

	public void setDetailSectionController(
			DetailSectionController detailSectionController) {
		this.detailSectionController = detailSectionController;
	}

	public MasterListSectionController getMasterListSectionController() {
		return masterListSectionController;
	}

	public void setMasterListSectionController(
			MasterListSectionController masterListSectionController) {
		this.masterListSectionController = masterListSectionController;
	}

	public void setPropertiesRepository(
			PropertiesRepositoryRemoteAsync propertiesRepository) {
		this.propertiesRepository = propertiesRepository;
	}

	public PropertiesRepositoryRemoteAsync getPropertiesRepository() {
		return propertiesRepository;
	}
	
	public UnitRepositoryRemoteAsync getUnitRepository() {
		return unitRepository;
	}

	public void setUnitRepository(UnitRepositoryRemoteAsync unitRepository) {
		this.unitRepository = unitRepository;
	}

	public AccountLedgerRepositoryRemoteAsync getAccountLedgerRepository() {
		return accountLedgerRepository;
	}

	public void setAccountLedgerRepository(AccountLedgerRepositoryRemoteAsync accountLedgerRepository) {
		this.accountLedgerRepository = accountLedgerRepository;
	}
	
	public RepackagingEntryRepositoryRemoteAsync getRepackagingEntryRepository() {
		return repackagingEntryRepository;
	}

	public void setRepackagingEntryRepository(
			RepackagingEntryRepositoryRemoteAsync repackagingEntryRepository) {
		this.repackagingEntryRepository = repackagingEntryRepository;
	}	
	
	public IVPostingHistoryRepositoryRemoteAsync getIvPostingHistoryRepository() {
		return ivPostingHistoryRepository;
	}
	public void setIvPostingHistoryRepository(
			IVPostingHistoryRepositoryRemoteAsync ivPostingHistoryRepository) {
		this.ivPostingHistoryRepository = ivPostingHistoryRepository;
	}
	public void setSubcontractorRepository(SubcontractorRepositoryRemoteAsync subcontractorRepository) {
		this.subcontractorRepository = subcontractorRepository;
	}
	public SubcontractorRepositoryRemoteAsync getSubcontractorRepository() {
		return subcontractorRepository;
	}
	public void setMessageBoardRepository(MessageBoardRepositoryRemoteAsync messageBoardRepository) {
		this.messageBoardRepository = messageBoardRepository;
	}
	public MessageBoardRepositoryRemoteAsync getMessageBoardRepository() {
		return messageBoardRepository;
	}
	public MessageBoardAttachmentRepositoryRemoteAsync getMessageBoardAttachmentRepository() {
		return messageBoardAttachmentRepository;
	}
	public void setMessageBoardAttachmentRepository(
			MessageBoardAttachmentRepositoryRemoteAsync messageBoardAttachmentRepository) {
		this.messageBoardAttachmentRepository = messageBoardAttachmentRepository;
	}
	public void setAttachmentRepository(AttachmentRepositoryRemoteAsync attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}
	public AttachmentRepositoryRemoteAsync getAttachmentRepository() {
		return attachmentRepository;
	}
	public TenderAnalysisRepositoryRemoteAsync getTenderAnalysisRepository() {
		return tenderAnalysisRepository;
	}
	public void setTenderAnalysisRepository(TenderAnalysisRepositoryRemoteAsync tenderAnalysisRepository) {
		this.tenderAnalysisRepository = tenderAnalysisRepository;
	}
	public String getSubcontractHoldMessage() {
		return subcontractHoldMessage;
	}
	public void setSubcontractHoldMessage(String subcontractHoldMessage) {
		this.subcontractHoldMessage = subcontractHoldMessage;
	}
	public String getPaymentHoldMessage() {
		return paymentHoldMessage;
	}
	public void setPaymentHoldMessage(String paymentHoldMessage) {
		this.paymentHoldMessage = paymentHoldMessage;
	}


	public SCPaymentCertRepositoryRemoteAsync getScPaymentCertRepository() {
		return scPaymentCertRepository;
	}


	public SCWorkScopeRepositoryRemoteAsync getScWorkScopeRepository() {
		return scWorkScopeRepository;
	}


	public PageRepositoryRemoteAsync getPageRepository() {
		return pageRepository;
	}


	public ResourceRepositoryRemoteAsync getResourceRepository() {
		return resourceRepository;
	}


	public MainCertificateAttachmentRepositoryRemoteAsync getMainCertificateAttachmentRepository() {
		return mainCertificateAttachmentRepository;
	}
	
	
}
