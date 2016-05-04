package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AddendumEnquiryYellowRateRenderer;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.renderer.NonTotalEditableColRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.splitTerminateSC.UpdateSCDetailNewQuantityWrapper;
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
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
 
public class SCSplitTerminateWindow extends Window {
	
	private static final String RESULT_PANEL_ID ="updateResultBQPanel";

	//UI
	private GlobalSectionController globalSectionController;
	private Panel mainPanel;
	private Panel leftTopPanel;
	private Panel rightTopPanel;
	private Panel topPanel;
	private Panel middlePanel;
	private Panel leftMiddlePanel;
	private Panel resultPanel;
	private EditorGridPanel resultEditorGridPanel;
	
	private final TextField packageNumberTextField;
	private final TextField jobNumberTextField;
	
	private final TextField originalSCSumTextField;
	private final TextField originalAddendumSumTextField;
	
	private final TextField revisedSCSumTextField;
	private final TextField revisedAddendumSumTextField;
	
	private final TextField currentWorkDoneAmountTextField;
	private final TextField currentCertifiedAmountTextField;
	
	
	private Label jobDescriptionLabel;
	private Button saveButton;
	private Button submitButton;
	
	//data store
	private Store dataStore;

	private PackageRepositoryRemoteAsync packageRepository;
	private ArrowKeyNavigation arrowKeyNavigation;
	private GlobalMessageTicket globalMessageTicket;

	private SCPackage scPackage;
	private String jobNumber;
	private String userID;
		
	Double originalSCSum = 0.0;
	Double revisedSCSum = 0.0;
	Double orignalAddendumSum = 0.0;
	Double revisedAddendumSum = 0.0;
	
	Double cumWorkdoneAmount = 0.0;
	Double cumCertAmount = 0.0;
	
	AmountRendererNonTotal amountRenderer;
	QuantityRenderer quantityRenderer;
	RateRenderer rateRenderer;
	String splitOrTerminate;
	private final RecordDef scDetailRecordDef = new RecordDef(new FieldDef[] {
					new StringFieldDef("lineType"),
					new StringFieldDef("billItem"),
					new StringFieldDef("description"), 
					new StringFieldDef("bqQuantity"),
					new StringFieldDef("scRate"),
					new StringFieldDef("costRate"),
					new StringFieldDef("sctotalAmount"),
					new StringFieldDef("cumWorkDoneQty"),
					new StringFieldDef("cumWorkDoneAmount"),
					new StringFieldDef("cumCertifiedQuanity"),
					new StringFieldDef("cumCertAmount"),
					new StringFieldDef("newQuantity"),
					new StringFieldDef("uom"),
					new StringFieldDef("sequenceNo"),
					new StringFieldDef("remark"),
					new StringFieldDef("id"),			//reference only
					new StringFieldDef("resourceNo"),	//reference only
					new StringFieldDef("approved")
				}
			);
	
	public SCSplitTerminateWindow(final GlobalSectionController globalSectionController, final SCPackage scPackage, String jobDescription, final String splitOrTerminate){
		super();
		this.setModal(true);
		
		this.globalSectionController =globalSectionController;
		packageRepository = globalSectionController.getPackageRepository();
		globalMessageTicket = new GlobalMessageTicket();
		
		this.splitOrTerminate = splitOrTerminate;	
		this.jobNumber = scPackage.getJob().getJobNumber();
		this.scPackage= scPackage;
		this.userID = globalSectionController.getUser().getUsername().toUpperCase();
		
		//Renderers
		amountRenderer = new AmountRendererNonTotal(globalSectionController.getUser());
		quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		if(SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
			setTitle("Split Subcontract - Job: "+jobNumber+" Subcontract: "+scPackage.getPackageNo());
		else
			setTitle("Terminate Subcontract - Job: "+jobNumber+" Subcontract: "+scPackage.getPackageNo());
		
		setPaddings(5);
		setWidth(1100);
		setHeight(670);
		setClosable(false);
		setLayout(new FitLayout());
	
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setFrame(true);
		
		topPanel = new Panel();
		topPanel.setHeight(80);
		topPanel.setLayout(new HorizontalLayout(2));

		middlePanel = new Panel();
		middlePanel.setHeight(180);
		middlePanel.setLayout(new HorizontalLayout(2));

		
		leftTopPanel = new Panel();
		leftTopPanel.setPaddings(5);
		leftTopPanel.setWidth(612);
		leftTopPanel.setHeight(70);
		TableLayout leftTopPanelLayout = new TableLayout(3);		
		leftTopPanel.setLayout(leftTopPanelLayout);
		
		rightTopPanel = new Panel();
		rightTopPanel.setPaddings(5);
		rightTopPanel.setWidth(412);
		rightTopPanel.setHeight(70);
		TableLayout rightTopPanelLayout = new TableLayout(3);		
		rightTopPanel.setLayout(rightTopPanelLayout);
		
		leftMiddlePanel = new Panel();
		leftMiddlePanel.setPaddings(5);
		leftMiddlePanel.setWidth(700);
		leftMiddlePanel.setHeight(160);
		TableLayout leftMiddlePanelLayout = new TableLayout(3);		
		leftMiddlePanel.setLayout(leftMiddlePanelLayout);

		Label jobNumberLabel = new Label("Job");
		jobNumberLabel.setCls("table-cell");
		leftTopPanel.add(jobNumberLabel);
		jobNumberTextField =new TextField("Job","jobNumber",50);
		jobNumberTextField.setCtCls("table-cell");
		jobNumberTextField.setValue(jobNumber);
		leftTopPanel.add(jobNumberTextField);
		
		jobDescriptionLabel = new Label();
		jobDescriptionLabel.setCtCls("table-cell");
		jobDescriptionLabel.setText(jobDescription);
		leftTopPanel.add(jobDescriptionLabel);

		Label subcontractNumberLabel = new Label("Subcontract");
		subcontractNumberLabel.setCls("table-cell");
		leftTopPanel.add(subcontractNumberLabel);
		packageNumberTextField = new TextField("Subcontract", "subcontractNumber", 50);
		packageNumberTextField.setCtCls("table-cell");
		packageNumberTextField.setValue(scPackage.getPackageNo());
		leftTopPanel.add(packageNumberTextField);

		Label subcontractDescriptionLabel = new Label();
		subcontractDescriptionLabel.setCls("table-cell");
		subcontractDescriptionLabel.setText(scPackage.getDescription());
		leftTopPanel.add(subcontractDescriptionLabel);
		
		topPanel.add(leftTopPanel);		
		topPanel.add(rightTopPanel);
		
		//1
		Label balnkLabel1 = new Label("                       ");
		balnkLabel1.setWidth(140);
		balnkLabel1.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel1);
		
		
		//2
		Label originalLabel;
		if(SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
			originalLabel = new Label("Before Split");
		else
			originalLabel = new Label("Before Termination");
		
		originalLabel.setCls("table-cell");
		leftMiddlePanel.add(originalLabel);
		//3
		Label revisedLabel;
		if(SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
			revisedLabel = new Label("      After Split");
		else
			revisedLabel = new Label("      After Termination");
		revisedLabel.setCls("table-cell");
		leftMiddlePanel.add(revisedLabel);
		
		
		//4
		Label scSumLabel = new Label("Subcontract Sum (BQ, B1)");
		scSumLabel.setCls("table-cell");
		leftMiddlePanel.add(scSumLabel);
		//5
		originalSCSumTextField =new TextField("Current Subcontract Sum","originalSCSum",150);
		originalSCSumTextField.setCtCls("table-cell");
		originalSCSumTextField.setValue(amountRenderer.render(originalSCSum.toString()));
		leftMiddlePanel.add(originalSCSumTextField);
		
		//6
		revisedSCSumTextField =new TextField("After Subcontract Sum","revisedSCSum",150);
		revisedSCSumTextField.setCtCls("table-cell");
		revisedSCSumTextField.setValue(amountRenderer.render(revisedSCSum.toString()));
		leftMiddlePanel.add(revisedSCSumTextField);
		
		//7
		Label addendumSumLabel = new Label("Approved Addendum Amount (V1, V2, V3, L1, L2, D1, D2, CF)");
		addendumSumLabel.setCls("table-cell");
		leftMiddlePanel.add(addendumSumLabel);
		//8
		originalAddendumSumTextField =new TextField("Current Approved Addendum Sum","orignalAddendumSum",150);
		originalAddendumSumTextField.setCtCls("table-cell");
		originalAddendumSumTextField.setValue(amountRenderer.render(orignalAddendumSum.toString()));
		leftMiddlePanel.add(originalAddendumSumTextField);
		//9
		revisedAddendumSumTextField =new TextField("Revised Approved Addendum Sum","revisedAddendumSum",150);
		revisedAddendumSumTextField.setCtCls("table-cell");
		revisedAddendumSumTextField.setValue(amountRenderer.render(revisedAddendumSum.toString()));
		leftMiddlePanel.add(revisedAddendumSumTextField);
		
		
		//10
		Label OriginalWorkDone = new Label("Cumulative Work Done Amount");
		OriginalWorkDone.setCls("table-cell");
		leftMiddlePanel.add(OriginalWorkDone);
		//11
		currentWorkDoneAmountTextField = new TextField("Cum Work Done","cumWorkdoneAmount",150);
		currentWorkDoneAmountTextField.setCtCls("table-cell");
		currentWorkDoneAmountTextField.setValue(amountRenderer.render(cumWorkdoneAmount.toString()));
		leftMiddlePanel.add(currentWorkDoneAmountTextField);
		//12
		Label balnkLabel2 = new Label("                       ");
		balnkLabel2.setWidth(140);
		balnkLabel2.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel2);
		//13
		Label cumCertAmountLabl = new Label("Cumulative Certified Amount");
		cumCertAmountLabl.setCls("table-cell");
		leftMiddlePanel.add(cumCertAmountLabl);
		//14
		currentCertifiedAmountTextField = new TextField("Cum Cert Done","cumCertAmt",150);
		currentCertifiedAmountTextField.setCtCls("table-cell");
		currentCertifiedAmountTextField.setValue(amountRenderer.render(cumCertAmount.toString()));
		leftMiddlePanel.add(currentCertifiedAmountTextField);
		//15
		Label blankkLabel4 = new Label("                       ");
		blankkLabel4.setWidth(140);
		blankkLabel4.setCls("table-cell");
		this.leftMiddlePanel.add(blankkLabel4);
		
		this.middlePanel.add(leftMiddlePanel);

		
		this.mainPanel.add(topPanel);
		this.mainPanel.add(middlePanel);
		
		//Grid Panel 
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);

		this.resultPanel.setBorder(true);
		this.resultPanel.setPaddings(5);
		this.resultPanel.setAutoScroll(true);
		
		resultPanel.setLayout(new FitLayout());
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setHeight(380);
		this.arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);
		
		
		ColumnConfig lineTypeColumn = new ColumnConfig("Line Type", "lineType", 50 , true);
		
		ColumnConfig billItemColumn = new ColumnConfig("BQ Item", "billItem", 100, true);
		
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 250 , true);
		
		ColumnConfig bqQuantityColumn = new ColumnConfig("BQ Qty", "bqQuantity", 120, true);
		bqQuantityColumn.setAlign(TextAlign.RIGHT);
		bqQuantityColumn.setRenderer(new QuantityRenderer(globalSectionController.getUser()));
		
		ColumnConfig scRateColumn = new ColumnConfig("SC Rate", "scRate", 50, true);
		scRateColumn.setAlign(TextAlign.RIGHT);
		scRateColumn.setRenderer(new AddendumEnquiryYellowRateRenderer(globalSectionController.getUser()));
		
		ColumnConfig costRateColumn = new ColumnConfig("Cost Rate", "costRate", 50, true);
		costRateColumn.setRenderer(rateRenderer);
		costRateColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig sctotalAmountColumn = new ColumnConfig("SC Total Amount", "sctotalAmount", 120 , true);
		sctotalAmountColumn.setAlign(TextAlign.RIGHT);
		sctotalAmountColumn.setRenderer(new AmountRendererNonTotal(globalSectionController.getUser()));
		
		ColumnConfig cumWorkDoneQtyColumn = new ColumnConfig("Cum Work Done Quantity", "cumWorkDoneQty", 130 , true);
		cumWorkDoneQtyColumn.setRenderer(quantityRenderer);
		cumWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig cumWorkDoneAmountColumn = new ColumnConfig("Cum Work Done Amount", "cumWorkDoneAmount", 130 , true);
		cumWorkDoneAmountColumn.setRenderer(amountRenderer);
		cumWorkDoneAmountColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig cumCertifiedQuanityColumn = new ColumnConfig("Cum Certified Quantity", "cumCertifiedQuanity", 130 , true);
		cumCertifiedQuanityColumn.setRenderer(quantityRenderer);
		cumCertifiedQuanityColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig cumCertAmountColumn = new ColumnConfig("Cum Certified Amount", "cumCertAmount", 130 , true);
		cumCertAmountColumn.setRenderer(amountRenderer);
		cumCertAmountColumn.setAlign(TextAlign.RIGHT);	
		
		ColumnConfig newQuantityColumn = new ColumnConfig("New Quantity", "newQuantity", 130 , true);
			if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate)) {
				Field newQtyField = FieldFactory.createNegativeNumberField(3);
				newQtyField.addListener(this.arrowKeyNavigation);
				newQuantityColumn.setEditor(new GridEditor(newQtyField));
				newQuantityColumn.setRenderer(new NonTotalEditableColRenderer(quantityRenderer));
			} else 
				newQuantityColumn.setRenderer(quantityRenderer);
	
		newQuantityColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig uomColumn = new ColumnConfig("Unit of Measure", "uom", 90 , true);
		
		ColumnConfig sequenceNoColumn = new ColumnConfig("Sequence Number", "sequenceNo", 100 , true);
		sequenceNoColumn.setAlign(TextAlign.RIGHT);
	
		ColumnConfig approvedColumn = new ColumnConfig("Status", "approved", 130 , true);
		
		ColumnConfig remarkColumn = new ColumnConfig("Remark", "remark", 130 , true);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(scDetailRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		
		ColumnConfig[] columns = new ColumnConfig[] {			
				lineTypeColumn,
				billItemColumn,
				descriptionColumn,
				bqQuantityColumn,
				scRateColumn,
				costRateColumn,
				sctotalAmountColumn,
				cumWorkDoneQtyColumn,
				cumWorkDoneAmountColumn,
				cumCertifiedQuanityColumn,
				cumCertAmountColumn,
				newQuantityColumn,
				uomColumn,
				sequenceNoColumn,
				approvedColumn,
				remarkColumn,
		};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		resultEditorGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex){	
				String lineType = record.getAsString("lineType");
				String approved = record.getAsString("approved");
				
				if(lineType!=null && 
					SCDetails.APPROVED_DESC.equals(approved) && 
					(lineType.equals("BQ") || lineType.equals("V3"))){
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
					globalMessageTicket.refresh();
					return true;
				}
				
				Integer resourceNo = record.getAsInteger("resourceNo");
				Double costRate = record.getAsDouble("costRate");
				if(lineType!=null && 
					SCDetails.APPROVED_DESC.equals(approved) && 
					lineType.equals("V1") && resourceNo!=null && resourceNo!=0 && costRate!=null){
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
					globalMessageTicket.refresh();
					return true;
				}
				
				arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				globalMessageTicket.refresh();
				return false;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record, String field, Object value, Object orginalValue , int rowIndex, int colIndex){
				Double newQty = new Double(value.toString());
				Double bqQty = new Double(record.getAsString("bqQuantity"));
				Double wdQty = new Double(record.getAsString("cumWorkDoneQty"));
				if((newQty< 0 && bqQty >=0 && wdQty >=0) || (newQty> 0 && bqQty <0 && wdQty <0)){
					MessageBox.alert("New Quantity has to be positive number while BQ quantity is positive, vice-versa when negative.");
					return false;
				}else if (Math.abs(newQty)>Math.abs(bqQty) || Math.abs(newQty)<Math.abs(wdQty)){
					MessageBox.alert("New Quantity has to be less than or equal to BQ Quantity and larger than or equal to Cumulative Work Done Quantity, vice-versa when negative.");
					return false;
				}else
					return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex){
				record.set("newQuantity", newValue);
				Double newSCSum = 0.00;
				Double newVoSum = 0.00;
				
				Record[] records = dataStore.getRecords();
				String lineType = "";
				for (Record r:records){
					lineType = r.getAsString("lineType");
					if(lineType!=null && (lineType.equals("BQ") || lineType.equals("B1")))
						newSCSum = newSCSum + (r.getAsDouble("newQuantity") * r.getAsDouble("scRate"));
					else if(lineType!=null && (lineType.equals("V1") || lineType.equals("V2") || lineType.equals("V3") || lineType.equals("L1") || lineType.equals("L2")|| lineType.equals("D1") || lineType.equals("D2") || lineType.equals("CF")))
						newVoSum = newVoSum + (r.getAsDouble("newQuantity") * r.getAsDouble("scRate"));
				}
				
				revisedSCSumTextField.setValue(amountRenderer.render(newSCSum.toString()));
				revisedAddendumSumTextField.setValue(amountRenderer.render(newVoSum.toString()));
			}
		});
		

		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		resultPanel.add(resultEditorGridPanel);
		mainPanel.add(resultPanel);
		add(mainPanel);
		
		//close button
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					if (dataStore.getModifiedRecords().length!=0){
						MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
		                        new MessageBox.ConfirmCallback() {   
		                            public void execute(String btnID) {
		                            	if (btnID.equals("yes")){
		                            		globalSectionController.populateSCPackageMainPanelandDetailPanel(scPackage.getPackageNo());
		                            		globalSectionController.closeCurrentWindow();
		                            	}
		                            }   
		                        });   
					}
					else{
						globalSectionController.populateSCPackageMainPanelandDetailPanel(scPackage.getPackageNo());
						globalSectionController.closeCurrentWindow();
					}
				};
		});	
		
		//Save Button
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();				
				update();
			};
		});

		//Submit Button
		if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
			submitButton = new Button("Submit Split SC Approval");
		else
			submitButton = new Button("Submit Terminate SC Approval");
		
		submitButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e){
				MessageBox.confirm(	"Confirm", 
									"Are you sure you want to submit "+(splitOrTerminate.equals(SCPackage.SPLIT)?"Split Subcontract Approval?":"Terminate Subcontract Approval?"),   
                        new MessageBox.ConfirmCallback() {   
                            public void execute(String btnID) {
                            	if (btnID.equals("yes")){
                            		globalMessageTicket.refresh();
                            		submit();
                            	}
                            }   
                        }); 
				
			}
		});
		
		addButton(saveButton);
		addButton(submitButton);
		addButton(closeWindowButton);
		
		securitySetup();
	}
	
	public void securitySetup(){
		jobNumberTextField.disable();
		packageNumberTextField.disable();
		
		originalSCSumTextField.disable();
		revisedSCSumTextField.disable();
		
		originalAddendumSumTextField.disable();
		revisedAddendumSumTextField.disable();
		
		currentWorkDoneAmountTextField.disable();
		currentCertifiedAmountTextField.disable();
		
		if (SCPackage.SPLIT_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
			SCPackage.TERMINATE_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
			SCPackage.TERMINATE_APPROVED.equals(scPackage.getSplitTerminateStatus().trim())){
			saveButton.setVisible(false);
			submitButton.setVisible(false);
		}
		else{
			saveButton.setVisible(true);
			submitButton.setVisible(true);
		}					
	}
	
	public void populateGrid(String jobNumber, String packageNumber){
		UIUtil.maskPanelById(RESULT_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSCDetails(jobNumber, packageNumber, SCPackage.SUBCONTRACT_PACKAGE, new AsyncCallback<List<SCDetails>>(){
			public void onSuccess(List<SCDetails> scDetails) {
				calculateFigures(scDetails);
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}	
		});	
	}
	
	public void calculateFigures(List<SCDetails> scDetails){
		dataStore.removeAll();
		
		for(SCDetails scDetail:scDetails){
			if (scDetail==null)
				continue;

			String lineType = scDetail.getLineType();
			Integer resourceNo = scDetail.getResourceNo()!=null?scDetail.getResourceNo():0;
			Double costRate = scDetail.getCostRate()!=null ? scDetail.getCostRate():0.00;
			Double scRate = scDetail.getScRate()!=null ? scDetail.getScRate():0.00;
			
			if(SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate)){
				//Case 1: SCDetail that doesn't have to be split will be calculated by the current "Quantity"
				if (scDetail.getNewQuantity()==null)
					scDetail.setNewQuantity(scDetail.getQuantity());
				/*else if(lineType!=null && ((lineType.equals("V1") && resourceNo!=0 && costRate!=0) || lineType.equals("V2") ||
											lineType.equals("L1") || lineType.equals("L2") || 
											lineType.equals("D1") || lineType.equals("D2") || 
											lineType.equals("CF") || lineType.equals("B1"))){
					if(!(scDetail.getNewQuantity() > scDetail.getCumWorkDoneQuantity()))
						scDetail.setNewQuantity(scDetail.getQuantity());
				}
				else if(lineType!=null 
						&& (lineType.equals("V1") || lineType.equals("V2")) 
						&& (resourceNo==0|| resourceNo == null) 
						&& (costRate==0 || costRate==null)){
					scDetail.setNewQuantity(scDetail.getQuantity());
				}*/
			}
			//Case 2: To be terminated package will have all the "New Quantity" filled in by "Cumulative WD Quantity"
			else if (SCPackage.TERMINATE.equalsIgnoreCase(splitOrTerminate)){				
				if(lineType!=null && (lineType.equals("BQ") || lineType.equals("V3")))
					scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());				
				else if(lineType!=null && lineType.equals("V1") && resourceNo!=0 && costRate!=0)
					scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());
				else
					scDetail.setNewQuantity(scDetail.getQuantity());
			}
			
			//Total calculation
			if(lineType!=null && (lineType.equals("BQ") || lineType.equals("B1"))){
				originalSCSum 	= originalSCSum + (scDetail.getQuantity() * scRate);  
				revisedSCSum = revisedSCSum + (scDetail.getNewQuantity() * scRate);
			}
			if(lineType!=null && (lineType.equals("V1") || lineType.equals("V2") || lineType.equals("V3") || lineType.equals("L1") || lineType.equals("L2")|| lineType.equals("D1") || lineType.equals("D2") || lineType.equals("CF"))){
				orignalAddendumSum 	= orignalAddendumSum + (scDetail.getQuantity() * scRate);  
				revisedAddendumSum = revisedAddendumSum + (scDetail.getNewQuantity() * scRate);
			}
			
			//(BQ, B1), (V1, V2, V3, L1, L2, D1, D2, CF), AP, OA
			if(lineType!=null && !(lineType.equals("C1") || lineType.equals("C2") || lineType.equals("RR") || lineType.equals("RA") || lineType.equals("MS"))){
				cumCertAmount = cumCertAmount + (scDetail.getCumCertifiedQuantity() * scRate);
				
				if(!lineType.equals("AP"))
					cumWorkdoneAmount 	= cumWorkdoneAmount + (scDetail.getCumWorkDoneQuantity() * scRate);
			}
			
			Record record = this.scDetailRecordDef.createRecord(
					new Object[]{
							scDetail.getLineType(),
							scDetail.getBillItem(),
							scDetail.getDescription(),
							scDetail.getQuantity(),
							scDetail.getScRate(),
							scDetail.getCostRate(),
							scDetail.getTotalAmount(),
							scDetail.getCumWorkDoneQuantity(),
							scDetail.getCumWorkDoneQuantity() * scRate,	//Cumulative Work Done Amount
							scDetail.getCumCertifiedQuantity(),
							scDetail.getCumCertifiedQuantity() * scRate,	//Cumulative Certified Amount
							scDetail.getNewQuantity(),
							scDetail.getUnit(),
							scDetail.getSequenceNo(),
							scDetail.getRemark(),
							scDetail.getId(),
							scDetail.getResourceNo(),
							SCDetails.convertApprovedStatus(scDetail.getApproved())
					}
			);
			dataStore.add(record);
		}

		originalSCSumTextField.setValue(amountRenderer.render(originalSCSum.toString()));
		revisedSCSumTextField.setValue(amountRenderer.render(revisedSCSum.toString()));
		originalAddendumSumTextField.setValue(amountRenderer.render(orignalAddendumSum.toString()));
		revisedAddendumSumTextField.setValue(amountRenderer.render(revisedAddendumSum.toString()));
		currentCertifiedAmountTextField.setValue(amountRenderer.render(cumCertAmount.toString()));
		currentWorkDoneAmountTextField.setValue(amountRenderer.render(cumWorkdoneAmount.toString()));
	}
	

	private void update(){
		Record[] records = dataStore.getRecords();
		
		if(records==null || records.length==0)
			return;
		
		UIUtil.maskPanelById(SCSplitTerminateWindow.this.getId(), "Updating", true);
		List<UpdateSCDetailNewQuantityWrapper> scDetailWrappers = new ArrayList<UpdateSCDetailNewQuantityWrapper>();
		for (int i=0; i< records.length; i++){
			if(records[i].isModified("newQuantity")){
				UpdateSCDetailNewQuantityWrapper scDetailWrapper = new UpdateSCDetailNewQuantityWrapper();
				scDetailWrapper.setId(new Long(records[i].getAsString("id")));
				scDetailWrapper.setNewQuantity(new Double(records[i].getAsString("newQuantity")));
				scDetailWrappers.add(scDetailWrapper);
			}
		}
		
		//update
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.updateSCDetailsNewQuantity(scDetailWrappers, new AsyncCallback<String>(){	
			public void onSuccess(String message) {
				if (message==null){
					dataStore.commitChanges();
					MessageBox.alert("New Quantities have been saved successfully.");
				}
				else
					MessageBox.alert(message);
				
				UIUtil.unmaskPanelById(SCSplitTerminateWindow.this.getId());
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
	}
	
	private void submit() {
		Record[] records = dataStore.getRecords();

		if (records == null || records.length == 0) {
			MessageBox.alert("Job: " + jobNumber + " SC Package: " + scPackage.getPackageNo() + " has no SCDetail. Split/Terminate subcontract approval cannot be submitted.");
			return;
		}

		saveButton.disable();
		submitButton.disable();

		// 1. update - save New Quantity before submitting for termination
		UIUtil.maskPanelById(SCSplitTerminateWindow.this.getId(), "Updating", true);
		List<UpdateSCDetailNewQuantityWrapper> scDetailWrappers = new ArrayList<UpdateSCDetailNewQuantityWrapper>();
		for (int i = 0; i < records.length; i++) {
			UpdateSCDetailNewQuantityWrapper scDetailWrapper = new UpdateSCDetailNewQuantityWrapper();
			scDetailWrapper.setId(new Long(records[i].getAsString("id")));
			scDetailWrapper.setNewQuantity(new Double(records[i].getAsString("newQuantity")));
			scDetailWrappers.add(scDetailWrapper);
		}
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.updateSCDetailsNewQuantity(scDetailWrappers, new AsyncCallback<String>() {
			//updateSCDetailsNewQuantity - onFailure
			public void onFailure(Throwable e) {
				saveButton.enable();
				submitButton.enable();
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
				UIUtil.unmaskPanelById(SCSplitTerminateWindow.this.getId());
			}
			//updateSCDetailsNewQuantity - onSuccess
			public void onSuccess(String message) {
				if (message == null) {
					dataStore.commitChanges();
					MessageBox.alert("New Quantities have been saved successfully.");

					// 2. submit for termination
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.submitSplitTerminateSC(jobNumber, new Integer(scPackage.getPackageNo()), splitOrTerminate, userID, new AsyncCallback<String>() {
						public void onFailure(Throwable e) {
							saveButton.enable();
							submitButton.enable();
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
							UIUtil.unmaskPanelById(SCSplitTerminateWindow.this.getId());
						}

						public void onSuccess(String message) {
							if (message == null || message.trim().equals("")) {
								if (splitOrTerminate.equals(SCPackage.SPLIT))
									MessageBox.alert("Split subcontract approval has submitted successfully.");
								else
									MessageBox.alert("Terminate subcontract approval has submitted successfully.");

								submitButton.setVisible(false);
								saveButton.setVisible(false);
							}
							else {
								MessageBox.alert(message);
								saveButton.enable();
								submitButton.enable();
							}
							UIUtil.unmaskPanelById(SCSplitTerminateWindow.this.getId());
						}
					});
				}
				else {
					MessageBox.alert(message);
					saveButton.enable();
					submitButton.enable();
				}
			}
		});
	}
	
}
