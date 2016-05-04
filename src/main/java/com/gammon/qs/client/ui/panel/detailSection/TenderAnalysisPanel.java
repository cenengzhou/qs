package com.gammon.qs.client.ui.panel.detailSection;

import java.util.List;

//import com.gammon.common.ui.GlobalMessageTicket;
import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.nonAwardedSCApproval.NonAwardedSCApprovalResponseWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
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
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * No use
 * 
 */
public class TenderAnalysisPanel extends Panel {
	private Panel infoPanel;
	
	//data store
	private Store dataStore;
	@SuppressWarnings("unused")
	private Integer rowInteger;
	private Integer subcontractorNumber = null;
	private Double subcontractSumInDomCurr = new Double(0);
	private Double comparableBudgetAmount = new Double(0);
	private Toolbar toolbar = new Toolbar();
	private ToolbarButton approvalButton;
	private TextField comparableBudgetAmountTextField;
	private AmountRendererNonTotal amountRenderer;
//	private GlobalMessageTicket globalMessageTicket;
	
	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;
	
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("subcontractorNumber"),
					new StringFieldDef("subcontractorName"),
					new StringFieldDef("subcontractSumInDomCurr"),
					new StringFieldDef("currency"),
					new StringFieldDef("exchangeRate")
				}
			);
	@Deprecated
	public TenderAnalysisPanel(final DetailSectionController detailSectionController, final Integer subcontractNumber, String subcontractDescription) {
		super();
		
//		globalMessageTicket = new GlobalMessageTicket();
		
		packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget)packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);
		
		amountRenderer = new AmountRendererNonTotal(detailSectionController.getGlobalSectionController().getUser());
		
		setFrame(true);
		this.setPaddings(10, 10, 0, 0);

		setLayout(new RowLayout());

		//search Panel
		this.infoPanel = new Panel();
		this.infoPanel.setPaddings(3);
		this.infoPanel.setFrame(true);
		this.infoPanel.setHeight(120);
		this.infoPanel.setWidth(500);

		TableLayout searchPanelLayout = new TableLayout(3);		
		this.infoPanel.setLayout(searchPanelLayout);
		
		this.approvalButton = new ToolbarButton("Submit Approval");
		
		ToolTip approvalRouteButtonTip = new ToolTip();
		approvalRouteButtonTip.setTitle("Submit for Approval");
		approvalRouteButtonTip.setHtml("Submit Non-awarded Subcontract for Approval");
		approvalRouteButtonTip.setCtCls("toolbar-button");
		approvalRouteButtonTip.setDismissDelay(15000);
		approvalRouteButtonTip.setWidth(300);
		approvalRouteButtonTip.setTrackMouse(true);
		approvalRouteButtonTip.applyTo(approvalButton);
		
		this.approvalButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				
				if (subcontractorNumber != null) {
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.nonAwardedSCApproval(detailSectionController.getGlobalSectionController().getJob().getJobNumber(), subcontractNumber, subcontractorNumber, subcontractSumInDomCurr, comparableBudgetAmount, detailSectionController.getGlobalSectionController().getUser().getUsername(), new AsyncCallback<NonAwardedSCApprovalResponseWrapper>(){	
					
					//if (subcontractorNumber != null)
					//SessionTimeoutCheck.renewSessionTimer();
					//packageRepository.addendumApproval(jobNumberTextField.getValueAsString(), ,globalSectionController.getUser().getUsername() , new AsyncCallback<AddendumApprovalResponseWrapper>(){
						
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(TenderAnalysisPanel.this.getId());
							UIUtil.checkSessionTimeout(e, true,detailSectionController.getGlobalSectionController().getUser(),"TenderAnalysisPanel.TenderAnalysisPanel()");
						}
			
						public void onSuccess(NonAwardedSCApprovalResponseWrapper result) {
							UIUtil.unmaskPanelById(TenderAnalysisPanel.this.getId());
							
//							globalMessageTicket.refresh();
							
							if (result.getPermit()) {
								UIUtil.alert("Non-Awarded Subcontract was submitted for approval!\nPlease check status in SC Approval list");
							} else {
								UIUtil.alert(result.getErrorMsg());							
							}
						}
						
					});
				} else {
					MessageBox.alert("Please select a subcontractor for approval!");
				}

//				ApprovalRequest approvalRequest = new ApprovalRequest();
//				approvalRequest.setOrderNumber(packageNumber.toString());
//				approvalRequest.setOrderType("AW");
//				approvalRequest.setJobNumber(globalSectionController.getJob().getJobNumber().trim());
//				approvalRequest.setSupplierNumber(scPackage.getVendorNo().trim());
//				approvalRequest.setSupplierName(receiveVendorName(scPackage.getVendorNo().trim()));
//				approvalRequest.setOrderAmount(certAmount.toString());
//				approvalRequest.setOriginator(userID);
//				approvalRequest.setApprovalSubType("");
				
				
				
			}
		});
		
/*		approvalButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				MessageBox.alert("OK");
				MessageBox.alert("TEST packageNumber " + subcontractNumber 
						+ "Job Number" + detailSectionController.getGlobalSectionController().getJob().getJobNumber());
				
				if (subcontractorNumber != null) {
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.nonAwardedSCApproval(detailSectionController.getGlobalSectionController().getJob().getJobNumber(), subcontractNumber, subcontractorNumber, new Double(0), detailSectionController.getGlobalSectionController().getUser().getUsername(), new AsyncCallback<NonAwardedSCApprovalResponseWrapper>(){	
					
					//if (subcontractorNumber != null)
					//SessionTimeoutCheck.renewSessionTimer();
					//packageRepository.addendumApproval(jobNumberTextField.getValueAsString(), ,globalSectionController.getUser().getUsername() , new AsyncCallback<AddendumApprovalResponseWrapper>(){
						
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(TenderAnalysisPanel.this.getId());
							UIUtil.checkSessoionTimeout(e, true);
						}
			
						public void onSuccess(NonAwardedSCApprovalResponseWrapper result) {
							UIUtil.unmaskPanelById(TenderAnalysisPanel.this.getId());
							
							globalMessageTicket.refresh();
							
							if (result.getPermit()) {
								UIUtil.alert("Non-Awarded Subcontract was submitted for approval!\nPlease check status in SC Approval list");
							} else {
								UIUtil.alert(result.getErrorMsg());							
							}
						}
						
					});
				} else {
					MessageBox.alert("Please select a subcontractor for approval!");
				}

//				ApprovalRequest approvalRequest = new ApprovalRequest();
//				approvalRequest.setOrderNumber(packageNumber.toString());
//				approvalRequest.setOrderType("AW");
//				approvalRequest.setJobNumber(globalSectionController.getJob().getJobNumber().trim());
//				approvalRequest.setSupplierNumber(scPackage.getVendorNo().trim());
//				approvalRequest.setSupplierName(receiveVendorName(scPackage.getVendorNo().trim()));
//				approvalRequest.setOrderAmount(certAmount.toString());
//				approvalRequest.setOriginator(userID);
//				approvalRequest.setApprovalSubType("");
				
				
				
			}
		});	*/
		toolbar.addButton(approvalButton);
//		this.setTopToolbar(new Button[]{
//				this.approvalButton
//				});
		this.setTopToolbar(toolbar);

		Label jobNumberLabel = new Label("Job Number");
		jobNumberLabel.setCls("table-cell");
		this.infoPanel.add(jobNumberLabel);
		TextField jobNumberTextField =new TextField("Job Number","jobNumber",150);
		jobNumberTextField.setCtCls("table-cell");
		jobNumberTextField.setValue(detailSectionController.getGlobalSectionController().getJob().getJobNumber().trim());
		jobNumberTextField.disable();
		this.infoPanel.add(jobNumberTextField);
		
		Label jobNameLabel = new Label(detailSectionController.getGlobalSectionController().getJob().getDescription());
		jobNameLabel.setCls("table-cell");
		this.infoPanel.add(jobNameLabel);
		
		Label subcontractNumberLabel = new Label("Subcontract Number");
		subcontractNumberLabel.setCls("table-cell");
		this.infoPanel.add(subcontractNumberLabel);
		TextField subcontractNumberTextField =new TextField("Subcontract Number","subcontractNumber",150);
		subcontractNumberTextField.setCtCls("table-cell");
		subcontractNumberTextField.setValue(subcontractNumber.toString());
		subcontractNumberTextField.disable();
		this.infoPanel.add(subcontractNumberTextField);
		
		TextField subcontractDescriptionTextField =new TextField("Subcontract Description","subcontractDescription",500);
		subcontractDescriptionTextField.setCtCls("table-cell");
		subcontractDescriptionTextField.setValue(subcontractDescription);
		subcontractDescriptionTextField.disable();
		this.infoPanel.add(subcontractDescriptionTextField);

		Label comparableBudgetAmountLabel = new Label("Comparable Budget Amount");
		comparableBudgetAmountLabel.setCls("table-cell");
		this.infoPanel.add(comparableBudgetAmountLabel);
		Label comparableBudgetAmountBlankLabel = new Label("");
		comparableBudgetAmountBlankLabel.setCtCls("table-cell");
		this.infoPanel.add(comparableBudgetAmountBlankLabel);
		
		comparableBudgetAmountTextField =new TextField("Comparable Budget Amount","comparableBudgetAmount",150);
		comparableBudgetAmountTextField.disable();
		comparableBudgetAmountTextField.setCtCls("table-cell");
		this.infoPanel.add(comparableBudgetAmountTextField);

		add(infoPanel);
		
		EditorGridPanel resultEditorGridPanel = new EditorGridPanel();
		
		ColumnConfig subcontractorNumberColumn = new ColumnConfig("Subcontractor", "subcontractorNumber", 130 , true);
		ColumnConfig subcontractorNameColumn = new ColumnConfig("Name", "subcontractorName", 130 , true);
		ColumnConfig subcontractSumInDomCurrColumn = new ColumnConfig("Subcontract Sum in Dom. Curr.", "subcontractSumInDomCurr", 170 , true);
		ColumnConfig currencyColumn = new ColumnConfig("Currency", "currency", 130 , true);
		ColumnConfig exchangeRateColumn = new ColumnConfig("Exchange Rate", "exchangeRate", 130 , true);
		
		subcontractSumInDomCurrColumn.setRenderer(amountRenderer);
		
		exchangeRateColumn.setRenderer(new Renderer(){

			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				try {
					return NumberFormat.getFormat("#,###.00000000").format(Double.parseDouble(value.toString()));
				}catch (NumberFormatException nfe){
					return value.toString();
				}
			}
			
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		
		ColumnConfig[] columns = new ColumnConfig[] {
				
				subcontractorNumberColumn,
				subcontractorNameColumn,
				subcontractSumInDomCurrColumn,
				currencyColumn,
				exchangeRateColumn
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));

		GridView view = new GridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		
		resultEditorGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				rowInteger= new Integer(rowIndex);
				
				Record curRecord = dataStore.getAt(rowIndex);
				
				subcontractorNumber = Integer.parseInt(curRecord.getAsString("subcontractorNumber").trim());
				subcontractSumInDomCurr = new Double(curRecord.getAsString("subcontractSumInDomCurr").trim());
//				subcontractDescription = curRecord.getAsString("description").trim();
//				//MessageBox.alert("TEST packageNumber " + curRecord.getAsString("packageNumber"));
//				globalSectionController.navigateToTenderAnalysis(globalSectionController.getJob().getJobNumber(), packageNumber, subcontractDescription);
			}


		});

		add(resultEditorGridPanel);
		
		show(); 
		doLayout();
	}
	
	public void populateGrid(List<TenderAnalysisWrapper> listTenderAnalysisWrapper){
		this.dataStore.removeAll();
		
		
		if (listTenderAnalysisWrapper.size()>0) {
			
			comparableBudgetAmountTextField.setValue(amountRenderer.render(listTenderAnalysisWrapper.get((listTenderAnalysisWrapper.size()-1)).getComparableBudgetAmount().toString()));
			comparableBudgetAmount = new Double(listTenderAnalysisWrapper.get((listTenderAnalysisWrapper.size()-1)).getComparableBudgetAmount());
			
			Record[] records = new Record[(listTenderAnalysisWrapper.size())];
			for (int i=0; i<listTenderAnalysisWrapper.size();i++) {
				TenderAnalysisWrapper currTenderAnalysisWrapper = listTenderAnalysisWrapper.get(i);

				records[i] = this.recordDef.createRecord(new Object[] {
						currTenderAnalysisWrapper.getSubcontractorNumber(),
						currTenderAnalysisWrapper.getSubcontractorName(),
						currTenderAnalysisWrapper.getSubcontractSumInDomCurr(),
						currTenderAnalysisWrapper.getCurrency(),
						currTenderAnalysisWrapper.getExchangeRate()
				});
				
			}
			
			this.dataStore.add(records);
		}

	}
}
